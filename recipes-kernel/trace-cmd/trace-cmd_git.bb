#
# Copyright (C) 2013 Wind River Systems, Inc.
#

DESCRIPTION = "User interface to Ftrace"
LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
                    file://trace-cmd.c;beginline=6;endline=8;md5=2c22c965a649ddd7973d7913c5634a5e \
                    file://COPYING.LIB;md5=bbb461211a33b134d42ed5ee802b37ff \
                    file://trace-input.c;beginline=5;endine=8;md5=7761d15a6ecf95820903ad4dec6827a8"

SRCREV = "0f2c2fb5dc22fd95812c754ab4829ed7c26e8844"
PV = "2.2.0+git${SRCPV}"

inherit pkgconfig pythonnative

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/rostedt/trace-cmd.git \
	   file://trace-cmd-Remove-prefix-before-libdir-to-install-plu.patch \
	  "

S = "${WORKDIR}/git"

EXTRA_OEMAKE = "'prefix=${prefix}' NO_PYTHON=1"

FILES_${PN}-dbg += "${libdir}/trace-cmd/plugins/.debug/"

do_install() {
        oe_runmake prefix="${prefix}" DESTDIR="${D}" install
}

