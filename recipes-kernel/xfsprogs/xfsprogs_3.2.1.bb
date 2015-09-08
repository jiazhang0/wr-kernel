#
# Copyright (C) 2013 - 2014 Wind River Systems, Inc.
#

#The recipe and patches are taken from
#http://cgit.openembedded.org/meta-openembedded/tree/meta-filesystems/recipes-utils/xfsprogs

DESCRIPTION = "XFS Filesystem Utilities"
HOMEPAGE = "http://oss.sgi.com/projects/xfs"
SECTION = "base"
LICENSE = "GPLv2"
LICENSE_libhandle = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://doc/COPYING;md5=dbdb5f4329b7e7145de650e9ecd4ac2a"
DEPENDS = "util-linux"

SRC_URI = "ftp://oss.sgi.com/projects/xfs/cmd_tars/${BP}.tar.gz \
    file://0001-xfsprogs-generate-crctable-which-is-moved-into-runti.patch \
    file://remove-install-as-user.patch \
    file://drop-configure-check-for-aio.patch \
    file://0001-libxlog-add-xlog_is_empty-helper.patch \
    file://0001-metadump-rename-dont_obfuscate-variable.patch \
    file://0002-metadump-zero-out-clean-log.patch \
    file://0003-xfs_metadump-don-t-zero-log-if-not-obfuscating.patch \
    file://0004-xfs_metadump-obfuscate-remote-symlinks-on-CRC-filesy.patch \
    file://0005-metadump-obfuscate-attrs-on-CRC-fs.patch \
    file://0006-metadump-Fill-attribute-values-with-v-rather-than-NU.patch \
    file://0007-metadump-Obfuscate-the-filesystem-label.patch \
    file://0008-metadump-handle-multi-block-directories.patch \
    file://0009-metadump-Add-option-to-copy-metadata-blocks-intact.patch \
    file://0010-metadump-Copy-the-log-if-not-obfuscating-or-zeroing.patch \
    file://0011-metadump-Zero-out-unused-portion-of-the-AGFL.patch \
    file://0012-metadump-Zero-literal-area-of-unused-inodes.patch \
    file://0013-metadump-Zero-unused-portions-of-inode-literal-area.patch \
    file://0014-metadump-Zero-sparse-unused-regions-of-dir2.patch \
    file://0015-metadump-Zero-unused-tail-of-symlink-blocks.patch \
    file://0016-metadump-Zero-unused-portions-of-attribute-blocks.patch \
    file://0017-metadump-Zero-unused-portions-of-DA_NODE-blocks.patch \
    file://0018-metadump-Zero-unused-portions-of-inode-BMAP-and-allo.patch \
"

SRC_URI[md5sum] = "5c6905932029c8f9207fe5a0a8aac24b"
SRC_URI[sha256sum] = "83f8ea4c38fe9f42b9f12cd523519287a9695f2cf4f3064e9e9a839f71185767"

inherit autotools-brokensep

PACKAGES =+ "${PN}-fsck ${PN}-mkfs libhandle"

RDEPENDS_${PN} = "${PN}-fsck ${PN}-mkfs"

FILES_${PN}-fsck = "${base_sbindir}/fsck.xfs"
FILES_${PN}-mkfs = "${base_sbindir}/mkfs.xfs"
FILES_libhandle = "${base_libdir}/libhandle${SOLIBS}"

EXTRA_OECONF = "--enable-gettext=no"
do_configure () {
    # Prevent Makefile from calling configure without arguments,
    # when do_configure gets called for a second time.
    rm -f include/builddefs include/platform_defs.h
    # Recreate configure script.
    rm -f configure
    oe_runmake configure
    # Configure.
    export DEBUG="-DNDEBUG"
    gnu-configize --force
    oe_runconf
}

LIBTOOL = "${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}'"
TARGET_CC_ARCH += "${LDFLAGS}"
PARALLEL_MAKE = ""

do_install () {
    export DIST_ROOT=${D}
    oe_runmake install
    # needed for xfsdump
    oe_runmake install-dev
    rm ${D}${base_libdir}/libhandle.a
    rm ${D}${base_libdir}/libhandle.la
    rm ${D}${base_libdir}/libhandle.so
    rm ${D}${libdir}/libhandle.so
    ln -s ../..${base_libdir}/libhandle.so.1 ${D}${libdir}/libhandle.so
}
