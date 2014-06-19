#
# Copyright (C) 2013 Wind River Systems, Inc.
#

DESCRIPTION = "Graphical trace viewer for Ftrace"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
                    file://kernel-shark.c;beginline=6;endline=8;md5=2c22c965a649ddd7973d7913c5634a5e"

SRCREV = "0f2c2fb5dc22fd95812c754ab4829ed7c26e8844"
PV = "0.2+git${SRCPV}"

DEPENDS = "gtk+ libxml2"
RDEPENDS_${PN} = "trace-cmd"

inherit pkgconfig pythonnative

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/rostedt/trace-cmd.git \
	   file://trace-cmd-Remove-prefix-before-libdir-to-install-plu.patch \
	  "

S = "${WORKDIR}/git"

EXTRA_OEMAKE = "'CC=${CC}' 'AR=${AR}' 'prefix=${prefix}' gui"

FILESPATH = "${FILE_DIRNAME}/trace-cmd"

do_install() {
        oe_runmake CC="${CC}" AR="${AR}" prefix="${prefix}" DESTDIR="${D}" install_gui
        rm -rf ${D}${libdir}/trace-cmd
	# Remove the empty dir
        rmdir --ignore-fail-on-non-empty ${D}${libdir}
}
