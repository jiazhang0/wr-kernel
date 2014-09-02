#
# Copyright (C) 2012 Wind River Systems, Inc.
#
# This recipe is adapted from 
# ./openembedded/recipes/i2c-tools/i2c-tools_3.0.3.bb
# in git://git.openembedded.org/openembedded

SUMMARY = "Set of i2c tools for linux"
HOMEPAGE = "http://www.lm-sensors.org/wiki/I2CTools"
DESCRIPTION = "The I2C tools that used to be part of the lm-sensors \
package have been split to a separate package. The rationale for \
that move is that not all hardware monitoring chips are I2C devices, \
and not all I2C devices are hardware monitoring chips, so having \
everything in a single package was confusing and impractical."
SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

PR = "r0"

SRC_URI = "http://dl.lm-sensors.org/i2c-tools/releases/i2c-tools-${PV}.tar.bz2 \
           file://Module.mk \
          "

inherit autotools-brokensep

do_compile_prepend() {
	cp ${WORKDIR}/Module.mk ${S}/eepromer/
	sed -i 's#/usr/local#/usr#' Makefile
	echo "include eepromer/Module.mk" >> Makefile
}

do_install_append() {
	install -d ${D}${includedir}/linux
	install -m 0644 include/linux/i2c-dev.h ${D}${includedir}/linux/i2c-dev-user.h
	rm -f ${D}${includedir}/linux/i2c-dev.h
}

SRC_URI[md5sum] = "f15019e559e378c6e9d5d6299a00df21"
SRC_URI[sha256sum] = "960023f61de292c6dd757fcedec4bffa7dd036e8594e24b26a706094ca4c142a"

PACKAGES =+ "${PN}-misc"
RDEPENDS_${PN}-misc += "perl"
FILES_${PN}-misc = "${sbindir}/i2c-stub-from-dump \
                        ${bindir}/ddcmon \
                        ${bindir}/decode-edid \
                        ${bindir}/decode-dimms \
                        ${bindir}/decode-vaio \
			"

PACKAGECONFIG[misc] = ",,,${PN}-misc perl"
