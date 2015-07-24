#
# Copyright (C) 2012 Wind River Systems, Inc.
#
# This recipe is adapted from 
# ./openembedded/recipes/lm_sensors/lmsensors-apps_3.2.0.bb
# in git://git.openembedded.org/openembedded

SUMMARY = "A hardware health monitoring package for Linux"
DESCRIPTION = "Lm-sensors is a hardware health monitoring package for Linux. \
               It allows you to access information from temperature, voltage, \
               and fan speed sensors."
HOMEPAGE = "http://www.lm-sensors.org/"
DEPENDS = "sysfsutils virtual/libiconv \
           bison-native flex-native rrdtool"
LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
                    file://COPYING.LGPL;md5=4fbd65380cdd255951079008b364516c"

PR = "r1"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "http://dl.lm-sensors.org/lm-sensors/releases/lm_sensors-${PV}.tar.bz2 \
           file://lm_sensors-pci-not-required.patch \
           file://sensord.init \
           file://sensord.conf \
           file://sensord.service \
"

SRC_URI[md5sum] = "f357ba00b080ab102a170f7bf8bb2578"
SRC_URI[sha256sum] = "f13dd885406841a7352ccfb8b9ccb23c4c057abe3de4258da5444c149a9e3ae1"

inherit update-rc.d systemd

INITSCRIPT_PACKAGES = "lmsensors-sensord"
INITSCRIPT_NAME_${PN}-sensord = "sensord"
INITSCRIPT_PARAMS_${PN}-sensord = "defaults 67"

SYSTEMD_PACKAGES = "lmsensors-sensord"
SYSTEMD_SERVICE_${PN}-sensord = "sensord.service"
SYSTEMD_AUTO_ENABLE = "disable"

S = "${WORKDIR}/lm_sensors-${PV}"

EXTRA_OEMAKE = 'LINUX=${STAGING_KERNEL_DIR} EXLDFLAGS="${LDFLAGS}" \
		MACHINE=${TARGET_ARCH} PREFIX=${prefix} CC="${CC}" \
		AR="${AR}" MANDIR=${mandir}'

do_compile() {
	oe_runmake user PROG_EXTRA="sensors sensord"
}

do_install() {
	oe_runmake user_install DESTDIR=${D} LIBDIR=${libdir}
	install -m 0755 ${S}/prog/sensord/sensord ${D}${bindir}
	install -m 0644 ${S}/prog/sensord/sensord.8 ${D}${mandir}/man8

	# Install directory
	install -d ${D}${sysconfdir}/init.d

	# Install sensord configuration file
	install -m 0644 ${WORKDIR}/sensord.conf ${D}${sysconfdir}

	# Install sensord init script
	install -m 0755 ${WORKDIR}/sensord.init ${D}${sysconfdir}/init.d/sensord

	# Insall sensord service script
	if ${@base_contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		install -d ${D}${systemd_unitdir}/system
		install -m 0644 ${WORKDIR}/sensord.service ${D}${systemd_unitdir}/system

		install -d ${D}${sysconfdir}/systemd/system
		ln -s ${systemd_unitdir}/system/sensord.service ${D}${sysconfdir}/systemd/system/sensord.service

		sed -i -e 's#@SYSCONFDIR@#${sysconfdir}#g' ${D}${systemd_unitdir}/system/sensord.service
		sed -i -e 's#@LOCALSTATEDIR@#${localstatedir}#g' ${D}${systemd_unitdir}/system/sensord.service
		sed -i -e 's#@BINDIR@#${bindir}#g' ${D}${systemd_unitdir}/system/sensord.service
	fi
}

PACKAGES =+ "libsensors libsensors-dev libsensors-staticdev libsensors-dbg libsensors-doc"
PACKAGES =+ "lmsensors-sensors lmsensors-sensors-dbg lmsensors-sensors-doc"
PACKAGES =+ "lmsensors-scripts"
PACKAGES =+ "lmsensors-sensord"

FILES_lmsensors-scripts = "${bindir}/*.pl ${bindir}/ddcmon ${sbindir}/fancontrol* ${sbindir}/pwmconfig ${sbindir}/sensors-detect"
RDEPENDS_lmsensors-scripts += "\
	bash \
	perl \
	lmsensors-sensors \
	perl-module-io \
	perl-module-vars \
	perl-module-carp \
	perl-module-fcntl \
	perl-module-config \
	perl-module-strict \
	perl-module-symbol \
	perl-module-xsloader \
	perl-module-constant \
	perl-module-warnings \
	perl-module-io-handle \
	perl-module-exporter-heavy \
	perl-module-warnings-register \
	perl-module-file-basename \
"
RDEPENDS_${PN} += "lmsensors-scripts"
RDEPENDS_lmsensors-sensord = "lmsensors-sensors rrdtool"

FILES_lmsensors-sensors = "${bindir}/sensors ${sysconfdir}"
FILES_lmsensors-sensors-dbg += "${bindir}/.debug/sensors"
FILES_lmsensors-sensors-doc = "${mandir}/man1 ${mandir}/man5"
FILES_lmsensors-sensord = "${bindir}/sensord ${sysconfdir}/init.d/sensord ${sysconfdir}/sensord.conf ${systemd_unitdir}/system/sensord.service"
FILES_libsensors = "${libdir}/libsensors.so.*"
FILES_libsensors-dbg += "${libdir}/.debug"
FILES_libsensors-dev = "${libdir}/libsensors.so ${includedir}"
FILES_libsensors-staticdev = "${libdir}/libsensors.a"
FILES_libsensors-doc = "${mandir}/man3"
