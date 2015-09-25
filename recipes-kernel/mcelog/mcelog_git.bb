#
# Copyright (C) 2012 - 2014 Wind River Systems, Inc.
#
SUMMARY = "user space backend for logging machine check errors"

DESCRIPTION = "mcelog is the user space backend for logging machine check errors \
reported by the hardware to the kernel. The kernel does the immediate \
actions (like killing processes etc.) and mcelog decodes the errors \
and manages various other advanced error responses like \
offlining memory, CPUs or triggering events."

HOMEPAGE = "http://www.mcelog.org/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://README;md5=3eb76ca64fa07ad53ebb0ebb5b4c8ede"

PR = "r1"

SRC_URI = "git://git.kernel.org/pub/scm/utils/cpu/mce/mcelog.git \
           file://mcelog-debash.patch \
           file://mcelog-make-mcelog-work-on-wrlinux.patch \
        "

SRCREV = "eba9d233470e9cfdc0c40a00f67feb3e1dcae40f"

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'

S ="${WORKDIR}/git"

inherit autotools-brokensep

do_install_append () {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${S}/mcelog.init ${D}${sysconfdir}/init.d/mcelog
}

PACKAGES =+ "${PN}-mcelog_init"

FILES_${PN}-mcelog_init = "${sysconfdir}/init.d/mcelog"

INITSCRIPT_PACKAGES = "${PN}-mcelog_init"

INITSCRIPT_NAME_${PN}-mcelog_init = "mcelog"

