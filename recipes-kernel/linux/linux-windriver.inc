#
# Copyright (C) 2012 Wind River Systems, Inc.
#
DESCRIPTION = "Wind River Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"

DEPENDS += "${@base_contains('IMAGE_FEATURES', 'lzo','lzop-native','',d)}"

# this gets us base linux-yocto bbclass support
require recipes-kernel/linux/linux-yocto.inc
# and this allows Wind River overrides
inherit kernel-windriver

# A method to install/update for bzImage on a deployed target
inherit kernel-grub

KMETA = "meta"

SRCREV="${AUTOREV}"
SRCREV_meta="${AUTOREV}"
SRCREV_machine="${AUTOREV}"

KBRANCH ?= "master"
KMACHINE ?= "${MACHINE}"

LINUX_VERSION_EXTENSION = "-WR${DISTRO_VERSION}_${LINUX_KERNEL_TYPE}"

# Align TARGET_SUPPORTED_KTYPES and KTYPE_ENABLED with LINUX_KERNEL_TYPE
TARGET_SUPPORTED_KTYPES ?= "standard"
KTYPE_ENABLED ?= "standard"

# Use the raw toolchain for kernel and kernel devshell builds
TARGET_RAW_PREFIX ?= "${TARGET_PREFIX}"
HOST_PREFIX_pn-linux-windriver = "${TARGET_RAW_PREFIX}"
CROSS_COMPILE_pn-linux-windriver = "${TARGET_RAW_PREFIX}"

FILESEXTRAPATHS_prepend := "${EXTRA_KERNEL_FILES}:"

# The following enables extra variable overrides within kernel recipes and
# bbappends
#
#  To override existing variables, to set features that are only
#  triggered based on a kernel type:
#     KERNEL_FEATURES_append_standard = "cfg/myboards_feature"
#
#  To be even more specific, the override of $machine-$kerneltype will
#  supercede $kernel-type overrides
#     KERNEL_FEATURES_append_qemux86_standard = "cfg/myboards_feature"
#
KERNELOVERRIDE = ":${LINUX_KERNEL_TYPE}:${MACHINE}-${LINUX_KERNEL_TYPE}"
OVERRIDES .= "${KERNELOVERRIDE}"

def get_kernel_install_header(d):
    headers = []

    install_headers = d.getVar('KERNEL_INSTALL_HEADER', True)
    if not install_headers:
        return ""

    for header in install_headers.split():
        headers.append("%s/%s" % (d.getVar('includedir', True), header))

    return " ".join(headers)

# Package the extra headers to avoid QA warnings
PACKAGES += "kernel-extra-headers-misc"
DESCRIPTION_kernel-extra-headers-misc = "Gather misc headers to avoid QA warning, should never be install to system."

KERNEL_MODULE_DEPEND_BLACKLIST = " kernel-extra-headers-misc"
FILES_kernel-extra-headers-misc = "${@get_kernel_install_header(d)}"

#
# If KERNEL_INSTALL_HEADER variable is set, copy headers to the
# sysroot
#

do_install_kernel_headers() {
	echo -e "install header list:\n\t${KERNEL_INSTALL_HEADER}\n"

	# for each file/dir in global variable, copy to ${D}
	for f in ${KERNEL_INSTALL_HEADER}; do
		# expect each entry to be a reference under ${S}/include/
		src_file="${S}/include/${f}"
		dest_path="${D}/usr/include/${f}"

		# and expect either a directory, or file name
		if [ -a "${src_file}" ]; then
			if [ -a "${dest_path}" ]; then
				echo "WARNING path already exists (not clobbering): ${dest_path}"
			fi

			mkdir -p `dirname $dest_path`
			cp -Rn ${src_file} `dirname $dest_path`

			echo "from: ${src_file}"
			echo -e "  to: ${dest_path}\n"
		else
			echo "WARNING: header file not found: ${f}"
		fi
	done
}

addtask install_kernel_headers before do_populate_sysroot after do_install

do_kernel_config_externalsrc() {
	if [ "${EXTERNALSRC}" != "" ]; then
		cd ${S}
		oe_runmake O=${B} \
		    KBUILD_DEFCONFIG=defconfig \
		    defconfig > ${B}/config.log 2>&1
	fi
}

addtask kernel_config_externalsrc after do_patch before do_configure

EXTRACTDIR ??= "${DEPLOYDIR}/kernel_extract"

do_extract_kernel_output[dirs] = "${EXTRACTDIR} ${B}"

#
# Extract kernel build output without install, deploy and package
#
do_extract_kernel_output() {
	# Extract kernel image
	install -m 0644 ${KERNEL_OUTPUT} ${EXTRACTDIR}

	# Extract kernel modules and tar them together
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS MACHINE
	if (grep -q -i -e '^CONFIG_MODULES=y$' .config); then
		oe_runmake DEPMOD=echo INSTALL_MOD_PATH="${EXTRACTDIR}" modules_install
		rm "${EXTRACTDIR}/lib/modules/${KERNEL_VERSION}/build"
		rm "${EXTRACTDIR}/lib/modules/${KERNEL_VERSION}/source"
		tar -cvzf ${EXTRACTDIR}/${MODULE_TARBALL_BASE_NAME} -C ${EXTRACTDIR} lib
		rm -rf "${EXTRACTDIR}/lib"
	else
		bbnote "no modules to extract"
	fi
}

# This is a standalone task
addtask extract_kernel_output after do_compile_kernelmodules
