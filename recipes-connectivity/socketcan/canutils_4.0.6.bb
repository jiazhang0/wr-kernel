#
# Copyright (C) 2012 Wind River Systems, Inc.
#
#require libsocketcan.inc

HOMEPAGE = "http://www.pengutronix.de/software/socket-can/download/canutils"
SUMMARY = "CAN BUS utils"
DESCRIPTION = "canutils"
SECTION = "console/network"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = "libsocketcan"

inherit autotools pkgconfig

SRC_URI = "http://www.pengutronix.de/software/socket-can/download/canutils/v4.0/canutils-${PV}.tar.bz2"
SRC_URI[md5sum] = "c6e1c5bcee6fd96cb1d57f4fee4cbdcd"
SRC_URI[sha256sum] = "458881a8ee494dc5c0941f333121673daeb38b97eba406b970b04fec7f5362fd"

PR = "r1"

RDEPENDS_${PN} += "canutils"
