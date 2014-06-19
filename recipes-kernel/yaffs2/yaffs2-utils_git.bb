#
# Copyright (C) 2013 Wind River Systems, Inc.
#
SUMMARY = "Yet Another Flash File System"

DESCRIPTION = "Tools for managing 'yaffs2' file systems."

SECTION = "base"
HOMEPAGE = "http://www.yaffs.net"
LICENSE = "GPLv2"

PR = "r0"
PV = "git${SRCPV}"

DEPENDS = "mtd-utils"

S = "${WORKDIR}/git"

# Source is the HEAD of aleph1-release-branch at the time of writing this recipe
SRC_URI = "git://www.aleph1.co.uk/yaffs2;protocol=git \
          "
SRCREV = "bc76682d93955cfb33051beb503ad9f8a5450578"

LIC_FILES_CHKSUM = "file://utils/mkyaffs2image.c;beginline=12;endline=14;md5=5f5464f9b3e981ca574e65b00e438561"

CFLAGS += "-I.. -DCONFIG_YAFFS_UTIL -DCONFIG_YAFFS_DEFINES_TYPES"

do_compile() {
	cd utils && oe_runmake
}

INSTALL_FILES = "mkyaffsimage \
                 mkyaffs2image \
                "
do_install() {
	install -d ${D}${sbindir}/
	for i in ${INSTALL_FILES}; do
		install -m 0755 utils/$i ${D}${sbindir}/
	done
}
