#
# Copyright (C) 2013 Wind River Systems, Inc.
#

require tipcutils.inc

PR = "${INC_PR}.0"
PV = "2.0.5+git${SRCPV}"

SRC_URI += "git://git.code.sf.net/p/tipc/tipcutils.git;protocol=git"

SRCREV = "292a03e17f889013fca2c7bd0aaeebd600c88f40"

S = "${WORKDIR}/git"

do_install() {
	install -d ${D}/${sbindir}
	install -m 0755 tipc-config/tipc-config ${D}/${sbindir}/tipc-config
	install -m 0755 ptts/tipcTC ${D}/${sbindir}/tipcTC
	install -m 0755 ptts/tipcTS ${D}/${sbindir}/tipcTS
	install -m 0755 tipc-pipe/tipc-pipe ${D}/${sbindir}/tipc-pipe
}
