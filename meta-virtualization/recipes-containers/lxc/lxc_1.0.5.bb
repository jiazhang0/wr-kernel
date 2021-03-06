DESCRIPTION = "lxc aims to use these new functionnalities to provide an userspace container object"
SECTION = "console/utils"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"
PRIORITY = "optional"
DEPENDS = "libxml2 libcap"
RDEPENDS_${PN} = " \
		rsync \
		gzip \
		libcap-bin \
		bridge-utils \
		dnsmasq \
		perl-module-strict \
		perl-module-getopt-long \
		perl-module-vars \
		perl-module-warnings-register \
		perl-module-exporter \
		perl-module-constant \
		perl-module-overload \
		perl-module-exporter-heavy \
"
RDEPENDS_${PN}-ptest += "file make"

SRC_URI = "http://linuxcontainers.org/downloads/${BPN}-${PV}.tar.gz \
	file://lxc-1.0.0-disable-udhcp-from-busybox-template.patch \
	file://runtest.patch \
	file://run-ptest \
	file://automake-ensure-VPATH-builds-work-correctly.patch \
	file://lxc-Add-template-script-for-WRLinux.patch \
        file://lxc-Add-container-name-check-in-lxc-wrlinux.patch \
	file://add_wrlinux_distro.patch \
	file://lxc-Tweak-lxc-template-script-for-systemd-sysvinit.patch \
	"
SRC_URI[md5sum] = "9d9af9e9e69a831cd50b58d91c786013"
SRC_URI[sha256sum] = "02ac82e69a76d424e5443b9c577e84a8eaafcebf17cfd865eedee147e8ef8844"

S = "${WORKDIR}/${BPN}-${PV}"

# Let's not configure for the host distro.
#
PTEST_CONF = "${@base_contains('DISTRO_FEATURES', 'ptest', '--enable-tests', '', d)}"
EXTRA_OECONF += "--with-distro=${DISTRO} ${PTEST_CONF} --disable-api-docs"

PACKAGECONFIG ??= "templates"
PACKAGECONFIG[doc] = "--enable-doc,--disable-doc,,"
PACKAGECONFIG[rpath] = "--enable-rpath,--disable-rpath,,"
PACKAGECONFIG[apparmour] = "--enable-apparmor,--disable-apparmor,apparmor,apparmor"
PACKAGECONFIG[templates] = ",,, ${PN}-templates"

# Not currently supported.
PACKAGECONFIG[lua] = "--enable-lua,--disable-lua,lua,"
PACKAGECONFIG[python3] = "--enable-python,--disable-python,python3,"
PACKAGECONFIG[seccomp] = "--enable-seccomp,--disable-seccomp,libseccomp,"

inherit autotools pkgconfig ptest

# For aarch64
COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|aarch64.*|powerpc.*|mips.*)-linux'

FILES_${PN}-doc = "${mandir} ${infodir}"
# For LXC the docdir only contains example configuration files and should be included in the lxc package
FILES_${PN} += "${docdir}"
FILES_${PN}-dbg += "${libexecdir}/lxc/.debug"
PACKAGES =+ "${PN}-templates"
FILES_${PN}-templates += "${datadir}/lxc/templates"
RDEPENDS_${PN}-templates += "bash"

PRIVATE_LIBS_${PN}-ptest = "liblxc.so.1"

do_install_append() {
	# The /var/cache/lxc directory created by the Makefile
	# is wiped out in volatile, we need to create this at boot.
	rm -rf ${D}${localstatedir}/cache
	install -d ${D}${sysconfdir}/default/volatiles
	echo "d root root 0755 ${localstatedir}/cache/lxc none" \
	     > ${D}${sysconfdir}/default/volatiles/99_lxc

	for i in `grep -l "#! */bin/bash" ${D}${datadir}/lxc/hooks/*`; do \
	    sed -e 's|#! */bin/bash|#!/bin/sh|' -i $i; done
}

EXTRA_OEMAKE += "TEST_DIR=${D}${PTEST_PATH}/src/tests"

do_install_ptest() {
	oe_runmake -C src/tests install-ptest
}

pkg_postinst_${PN}() {
	if [ -z "$D" ] && [ -e /etc/init.d/populate-volatile.sh ] ; then
		/etc/init.d/populate-volatile.sh update
	fi
}
