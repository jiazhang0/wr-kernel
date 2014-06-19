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

PR = "r2"

SRC_URI = "http://oss.linbit.com/drbd/8.3/drbd-${PV}.tar.gz \
           file://build-remove-old-sanity-check-to-allow-build-against.patch \
           file://scripts-make-Makefile.in-respect-libdir.patch \
           file://0001-drbd-tools-only-rmmod-if-DRBD-is-a-module.patch \
           file://drbd-init-stop.patch \
          "

SRC_URI[md5sum] = "e47a35a80143b72e9708844efbe2e608"
SRC_URI[sha256sum] = "bb5d3d0740788e779ba3b3a78c21362969dd0648eaced3ff37d9e52fff1df883"

inherit autotools
inherit update-rc.d useradd

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "--system haclient"

S = "${WORKDIR}/drbd-${PV}"

RDEPENDS_${PN} += "perl-module-strict perl-module-warnings"

EXTRA_OECONF = " \
    --libdir=${libdir} \
    --with-distro=generic \
    --with-km=no \
    --with-xen=no \
"

EXTRA_OEMAKE = "KDIR=${STAGING_KERNEL_DIR}"

INITSCRIPT_NAME = "drbd"
INITSCRIPT_PARAMS = "defaults"

FILES_${PN} += "${libdir}/drbd ${libdir}/ocf/resource.d/linbit/drbd"

do_install_append() {
    # Remove /var/lock as it is created on startup
    rm -rf ${D}${localstatedir}/lock
}
