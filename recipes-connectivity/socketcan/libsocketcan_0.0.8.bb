#
# Copyright (C) 2012 Wind River Systems, Inc.
#
SUMMARY = "Can bus socket lib"
DESCRIPTION = "Control basic functions in socketcan from userspace."
HOMEPAGE = "http://www.pengutronix.de/software/libsocketcan"
SECTION = "libs/network"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://src/libsocketcan.c;beginline=1;endline=18;md5=0c0203be012a4e5f2d835b66672bb50b"

inherit autotools-brokensep pkgconfig

PR = "r1"

SRC_URI = "http://www.pengutronix.de/software/libsocketcan/download/libsocketcan-${PV}.tar.bz2"
SRC_URI[md5sum] = "47ca129dc9459c534c8269cc542d00c1"
SRC_URI[sha256sum] = "1f367aacdec9b916e6e947ff03187ab11cd385843010b2d3949d305176074d78"

RDEPENDS_${PN} += "libsocketcan"

do_configure_prepend () {
    sed -iq 's/AC_INIT(\[libsocketcan\], \[trunk\]/AC_INIT(\[libsocketcan\], \[0.0.8\]/' configure.ac
}
