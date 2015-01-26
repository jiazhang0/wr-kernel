#
# Copyright (C) 2012 Wind River Systems, Inc.
#
def machine_ktype_compatibility(d,match):
    ktype_enabled = d.getVar('KTYPE_ENABLED', True)
    supported_types = d.getVar('TARGET_SUPPORTED_KTYPES', True)
    if not supported_types or not ktype_enabled:
        return "%s" % "none"

    if ktype_enabled not in (supported_types):
        return "%s" % "none"

    if ktype_enabled in (match):
        return "%s" % d.getVar('MACHINE', True)
    else:
        return "%s" % "none"

do_patch() {
	# ------------------------------------------------------
	# Allow KMACHINE to be overriden if they pass it via $1.
	# No other env overrides are supported.  Otherwise, use the
	# original KMACHINE.
	# ------------------------------------------------------
	env_overrides=$1
	if [ -n "$env_overrides" ]; then
		echo "do_patch: override request found checking: $env_overrides"
		for i in $env_overrides; do
			if [[ "$i" == "KMACHINE="* ]]; then
				echo "Overriding $i"
				KMACHINE=${i/KMACHINE=/}
			fi
		done
	else
		KMACHINE=${KMACHINE}
	fi
	cd ${S}
	export KMETA=${KMETA}

	# if kernel tools are available in-tree, they are preferred
	# and are placed on the path before any external tools. Unless
	# the external tools flag is set, in that case we do nothing.
	if [ -f "${S}/scripts/util/configme" ]; then
		if [ -z "${EXTERNAL_KERNEL_TOOLS}" ]; then
			PATH=${S}/scripts/util:${PATH}
		fi
	fi

	kbranch=${KBRANCH}

	# if we have a defined/set meta branch we should not be generating
	# any meta data. The passed branch has what we need.
	if [ -n "${KMETA}" ]; then
		createme_flags="--disable-meta-gen --meta ${KMETA}"
	fi

	createme ${createme_flags} ${ARCH} ${kbranch}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not create ${kbranch}"
		exit 1
	fi

	sccs="${@" ".join(find_sccs(d))}"
	patches="${@" ".join(find_patches(d))}"

	set +e
	# add any explicitly referenced features onto the end of the feature
	# list that is passed to the kernel build scripts.
	if [ -n "${KERNEL_FEATURES}" ]; then
		for feat in ${KERNEL_FEATURES}; do
			addon_features="$addon_features --feature $feat"
		done
	fi

	if [ "${kbranch}" != "${KBRANCH_DEFAULT}" ]; then
		updateme_flags="--branch ${kbranch}"
	fi

	# updates or generates the target description
	updateme ${updateme_flags} -DKDESC=$KMACHINE:${LINUX_KERNEL_TYPE} \
                           ${addon_features} ${ARCH} $KMACHINE ${sccs} ${patches}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not update ${kbranch}"
		exit 1
	fi

	# executes and modifies the source tree as required
	patchme $KMACHINE
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not apply patches for $KMACHINE."
		echo "       Patch failures can be resolved in the devshell (bitbake -c devshell ${PN})"
		exit 1
	fi

	# Perform a final check. If something other than the default kernel
	# branch was requested, and that's not where we ended up, then we 
	# should thrown an error, since we aren't building what was expected
	final_branch="$(git symbolic-ref HEAD 2>/dev/null)"
	final_branch=${final_branch##refs/heads/}
	if [ "${kbranch}" != "${KBRANCH_DEFAULT}" ] &&
	   [ "${final_branch}" != "${kbranch}" ]; then
		echo "ERROR: branch ${kbranch} was requested, but was not properly"
		echo "       configured to be built. The current branch is ${final_branch}"
		exit 1
	fi
}

KERNEL_SYSTEM_MAP_BASE_NAME ?= "System.map-${PV}-${PR}-${MACHINE}-${DATETIME}"
VMLINUX_SYMBOLS_BASE_NAME ?= "vmlinux-symbols-${PV}-${PR}-${MACHINE}-${DATETIME}"

# Don't include the DATETIME variable in the sstate package signatures
KERNEL_SYSTEM_MAP_BASE_NAME[vardepsexclude] = "DATETIME"
VMLINUX_SYMBOLS_BASE_NAME[vardepsexclude] = "DATETIME"

do_deploy_append() {
        set +e

	install -d ${DEPLOYDIR}

	install -m 0644 ${D}/boot/System.map-${KERNEL_VERSION} ${DEPLOYDIR}/${KERNEL_SYSTEM_MAP_BASE_NAME}
	install -m 0644 ${D}/boot/vmlinux-${KERNEL_VERSION} ${DEPLOYDIR}/${VMLINUX_SYMBOLS_BASE_NAME}

	cd ${DEPLOYDIR}

	ln -sf ${KERNEL_SYSTEM_MAP_BASE_NAME} System.map-${MACHINE}
	ln -sf ${VMLINUX_SYMBOLS_BASE_NAME} vmlinux-symbols-${MACHINE}
}


OE_TERMINAL_EXPORTS += "GUILT_BASE KBUILD_OUTPUT LDFLAGS CC"
GUILT_BASE = "meta"
python do_devshell () {
    # The CROSS_COMPILE and ARCH are already exported by the global
    # OE_TERMINAL_EXPORTS, and hence we don't need to add them explicitly
    # in the list.
    d.setVar("GUILT_BASE", "meta")
    d.setVar("KBUILD_OUTPUT", "${B}")
    d.setVar("LDFLAGS", "")
    # We clear CC, so the kernel can use CROSS_COMPILE directly
    d.setVar("CC", "")
    oe_terminal( d.getVar('SHELL', True), 'Wind River Kernel Developer Shell', d)
}

# builds an individual external module in directory M or builds
# all modules if M is not set.
do_module() {
	if [ -n "${M}" ]; then
		unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS MACHINE
		oe_runmake ${PARALLEL_MAKE} M=${M} CC="${KERNEL_CC}" LD="${KERNEL_LD}"
	else
		do_compile_kernelmodules
	fi
}
addtask do_module after do_compile

kern_config_helper() {
	export DISPLAY="${DISPLAY}"
	# Pickup host's pkg-config
	export PATH=/usr/bin:/bin:$PATH
	for f in PKG_CONFIG_DIR PKG_CONFIG_PATH PKG_CONFIG_DISABLE_UNINSTALLED PKG_CONFIG_SYSROOT_DIR PKG_CONFIG_LIBDIR ; do
		unset $f
	done
	if [ -f /usr/lib/x86_64-linux-gnu/pkgconfig/QtCore.pc ] ; then
		export PKG_CONFIG_LIBDIR=/usr/lib/x86_64-linux-gnu/pkgconfig
	fi
	bbnote Starting: make ${kern_config_type}
	oe_runmake ${kern_config_type}
}

python do_xconfig() {
    d.setVar('kern_config_type', 'xconfig')
    res = bb.build.exec_func("kern_config_helper", d)
    config_stamp_clean_helper(d)
}
do_xconfig[nostamp] = "1"
addtask xconfig after do_configure

python do_gconfig() {
    d.setVar('kern_config_type', 'gconfig')
    res = bb.build.exec_func("kern_config_helper", d)
    config_stamp_clean_helper(d)
}
do_gconfig[nostamp] = "1"
addtask gconfig after do_configure

def config_stamp_clean_helper(d):
    import bb, re, string, sys, commands

    # invalidate stamps for force a rebuild. This is temporary.
    cmd = d.expand("rm -f ${STAMP}.do_compile*; rm -f ${STAMP}.do_install*; rm -f ${STAMP}.do_configure*")
    ret, result = commands.getstatusoutput("%s" % (cmd))

    # save the .config
    cmd = d.expand("cp -f ${B}/.config ${WORKDIR}/${PV}-${PR}-${MACHINE}-${DATETIME}")
    ret, result = commands.getstatusoutput("%s" % (cmd))

    bb.plain(d.expand("Saving .config to ${WORKDIR}/${PV}-${PR}-${MACHINE}-${DATETIME}"))

python do_menuconfig_append() {
    config_stamp_clean_helper(d)
}

python do_oldconfig() {
    oe_terminal("make oldconfig", '${PN} Configuration', d)
    config_stamp_clean_helper(d)
}
do_oldconfig[nostamp] = "1"
addtask oldconfig after do_configure

do_cscope() {
	oe_runmake cscope
}
do_cscope[nostamp] = "1"
addtask cscope after do_configure

# The do_deploy_post is a hook point which can be used by the
# wrlcompat.bbclass to complete the linking of the kernel provided
# files into the export directory.
python do_deploy_post() {
    return
}

addtask deploy_post after do_deploy before do_build

# sanity updates. The do_package_qa_prepend and vmlinux sanity
# flags allow a 64 bit kernel + modules to be packaged against a
# 32 bit userspace. If external modules are built, they must supply
# their own INSANE_SKIP_<module> = "arch" if they want to be properly
# packaged.
python do_package_qa_prepend () {
    pkgs = d.getVar( 'PACKAGES', True )
    module_pattern = 'kernel-module-%s'
    module_regex = '^(.*)\.k?o$'
    dvar = d.getVar('PKGD', True)
    root = '/lib/modules'

    objs = []
    for walkroot, dirs, files in os.walk(dvar + root):
        for file in files:
            relpath = os.path.join(walkroot, file).replace(dvar + root + '/', '', 1)
            if relpath:
                objs.append(relpath)

    for o in sorted(objs):
        import re, stat

        m = re.match(module_regex, os.path.basename(o))

        if not m:
            continue

        f = os.path.join(dvar + root, o)
        mode = os.lstat(f).st_mode
        if not (stat.S_ISREG(mode) or (allow_links and stat.S_ISLNK(mode)) or (allow_dirs and stat.S_ISDIR(mode))):
            continue
        on = legitimize_package_name(m.group(1))
        pkg = module_pattern % on

        bb.note( "INFO: flagging %s to skip arch sanity checking" % pkg )

        d.setVar('INSANE_SKIP_%s' % pkg, "arch")
}
INSANE_SKIP_kernel-vmlinux += "arch"
INSANE_SKIP_kernel-image += "arch"
