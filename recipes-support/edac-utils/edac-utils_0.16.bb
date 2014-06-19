#
# Copyright (C) 2012 Wind River Systems, Inc.
#
SUMMARY = "Userspace helper for Linux kernel EDAC drivers"
DESCRIPTION = "\
This package contains the user-space utilities for use with the EDAC \
kernel subsystem. EDAC (Error Detection and Correction) is a set of \
Linux kernel modules for handling hardware-related errors. Currently \
its major focus is ECC memory error handling. However it also detects \
and reports PCI bus parity errors. \
\
PCI parity errors are supported on all architectures (and are a mandatory \
part of the PCI specification). \
\
Main memory ECC drivers are memory controller specific. At the time of \
writing, drivers exist for many x86-specific chipsets and CPUs, and some \
PowerPC, and MIPS systems. \
"
HOMEPAGE = "http://bluesmoke.sourceforge.net/"
SECTION = "console/kernel"
LICENSE = "GPLv2"
DEPENDS = "sysfsutils perl-native"
RDEPENDS_${PN}_x86 = "dmidecode"
RDEPENDS_${PN}_x86-64 = "dmidecode"
RDEPENDS_${PN}_powerpc = "dmidecode"
RDEPENDS_${PN}_append = " perl"
PR = "r2"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SRC_URI = "http://sourceforge.net/projects/edac-utils/${BPN}-${PV}.tar.bz2 \
           file://make-init-script-be-able-to-automatically-load-EDAC-.patch \
           file://add-restart-to-initscript.patch \
"
SRC_URI[md5sum] = "77dda84f25ddba732da1d94fe357bf87"
SRC_URI[sha256sum] = "4e4b4013e356e1c4d3262aae95983d23094a0d16e7be3c563ec7f8c402d42ab6"

inherit autotools
