#
# Copyright (C) 2014 Wind River Systems, Inc.
#
SUMMARY = "Dummy Linux kernel"
DESCRIPTION = "Dummy Linux kernel, to be selected as the preferred \
provider for virtual/kernel to satisfy dependencies for situations \
where you wish to build the kernel externally from the build system."
SECTION = "kernel"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING.GPL;md5=751419260aa954499f7abaabaa882bbe"

PROVIDES += "virtual/kernel"

PACKAGES_DYNAMIC += "^kernel-module-.*"
PACKAGES_DYNAMIC += "^kernel-image-.*"
PACKAGES_DYNAMIC += "^kernel-firmware-.*"

PACKAGES += "kernel-modules"
FILES_kernel-modules = ""
ALLOW_EMPTY_kernel-modules = "1"
DESCRIPTION_kernel-modules = "Kernel modules meta package"

#COMPATIBLE_MACHINE = "your_machine"

LINUX_VERSION ?= "3.10"
LINUX_VERSION_EXTENSION ?= "-dummy"

PR = "r1"

SRC_URI = "file://COPYING.GPL"
S = "${WORKDIR}"

do_configure() {
	:
}

do_compile () {
	:
}

do_install() {
	:
}

do_bundle_initramfs() {
	:
}

do_deploy() {
	:
}

do_bundle_initramfs[nostamp] = "1"
addtask bundle_initramfs after do_compile
addtask deploy after do_install
