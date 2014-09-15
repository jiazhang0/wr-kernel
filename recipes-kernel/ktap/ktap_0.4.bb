#
# Copyright (C) 2014 Wind River Systems, Inc.
#
SUMMARY = "Script-based dynamic tracing tool for Linux"

DESCRIPTION = "ktap uses a scripting language and lets users trace the Linux \
kernel dynamically. ktap is designed to give operational insights with \
interoperability that allows users to tune, troubleshoot and extend kernel and \
application"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*)-linux'

DEPENDS = "virtual/kernel elfutils"

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"

# This looks in S, so we better make sure there's
# something in the directory.
#
do_populate_lic[depends] = "${PN}:do_configure"

EXTRA_OEMAKE = '\
                CC="${CC}" \
		'CFLAGS += -I${STAGING_KERNEL_DIR}/include/uapi -I${STAGING_KERNEL_DIR}/include -D__EXPORTED_HEADERS__' \
               '

# If we build under STAGING_KERNEL_DIR, source will not be put
# into the dbg rpm.  STAGING_KERNEL_DIR will exist by the time
# do_configure() is invoked so we can safely copy from it.
#
do_configure_prepend() {
	mkdir -p ${S}
	cp -r ${STAGING_KERNEL_DIR}/tools/ktap/* ${S}
	cp -r ${STAGING_KERNEL_DIR}/COPYING ${S}
}

do_install() {
	oe_runmake DESTDIR="${D}" install
}
