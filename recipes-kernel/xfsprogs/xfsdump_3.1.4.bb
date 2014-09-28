#
# Copyright (C) 2014 Wind River Systems, Inc.
#
SUMMARY = "XFS Filesystem Dump Utility"

DESCRIPTION = "xfsdump and xfsrestore can be used for backup and \
restore of XFS file systems to local/remote SCSI tapes or files. It \
supports dumping of extended attributes and quota information. As the \
xfsdump format has been preserved and is now endian neutral, dumps \
created on one platform can be restored onto an XFS filesystem on \
another."

HOMEPAGE = "http://oss.sgi.com/projects/xfs"
LICENSE = "GPLv2"
SECTION = "base"
DEPENDS = "xfsprogs attr"

SRC_URI = "ftp://oss.sgi.com/projects/xfs/cmd_tars/${BP}.tar.gz"
SRC_URI[md5sum] = "a8b1761be5feb363131e7b506639ad4c"
SRC_URI[sha256sum] = "570eafd0721515bdd79cb0e295b701d49cdf81e71a0a0ff0df6d4c5cc1960943"

LIC_FILES_CHKSUM = "file://doc/COPYING;md5=15c832894d10ddd00dfcf57bee490ecc \
                    file://common/main.c;beginline=5;endline=16;md5=0f014348ec9a22b0e1bb7fa8a7d17611 \
                   "

inherit autotools-brokensep

EXTRA_OECONF = "INSTALL_USER=root \
                INSTALL_GROUP=root \
                --enable-gettext=no \
               "

LIBTOOL = "${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}'"

do_configure () {
	export DEBUG="-DNDEBUG"
	oe_runconf
}

do_install () {
	export DIST_ROOT=${D}
	oe_runmake install
}

