#
# Copyright (C) 2013 Wind River Systems, Inc.
#
SUMMARY = "Distributed Replicated Block Device (DRBD) tools"

DESCRIPTION = "DRBD is a block device which is designed to build high \
availability clusters. This is done by mirroring a whole block device \
via (a dedicated) network. You could see it as a network raid-1."

HOMEPAGE = "http://www.drbd.org/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5574c6965ae5f583e55880e397fbb018"

SRC_URI = "git://git.drbd.org/drbd-8.4.git \
           file://scripts-make-Makefile.in-respect-libdir.patch \
           file://drbd-8.4.3-drbd-tools-only-rmmod-if-DRBD-is-a-module.patch \
           file://documentation-do-not-try-to-install-unbuilt-document.patch \
           file://remove-definition-of-unused-define-__unused.patch \
          "

SRCREV = "89a294209144b68adb3ee85a73221f964d3ee515"

inherit autotools-brokensep
inherit update-rc.d useradd

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "--system haclient"

S = "${WORKDIR}/git"

RDEPENDS_${PN} += "perl-module-strict perl-module-warnings"

EXTRA_OECONF = " \
    --libdir=${libdir} \
    --with-distro=generic \
    --with-km=no \
    --with-xen=no \
    --with-legacy_utils=no \
"

EXTRA_OEMAKE = "KDIR=${STAGING_KERNEL_DIR}"

INITSCRIPT_NAME = "drbd"
INITSCRIPT_PARAMS = "defaults"

FILES_${PN} += "${libdir}/drbd ${libdir}/ocf/resource.d/linbit/drbd"

do_install_append() {
    # Remove /var/lock as it is created on startup
    rm -rf ${D}${localstatedir}/lock
}
