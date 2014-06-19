#
# Copyright (C) 2013 Wind River Systems, Inc.
#
SUMMARY = "Kernel module component of KVM."

DESCRIPTION = "KVM kernel modules are included as part of the mainline \
kernel. This kvm-kmod package can be used to replace the mainline \
kernel support with more up to date KVM kernel module support."

HOMEPAGE = "http://www.linux-kvm.org"
BUGTRACKER = "https://bugzilla.kernel.org/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=c616d0e7924e9e78ee192d99a3b26fbd \
                    file://x86/kvm_main.c;beginline=48;endline=49;md5=02f5efd90a6759e970423e87f595d9bf \
                   "

COMPATIBLE_HOST = '(i.86|x86_64|powerpc).*-linux'

# To build kvm-kmod successfully we need to configure
# our project with a template.  Since this is not normally
# done for world builds, we keep kvm-kmod out by default.
# If we configure with the template, kvm-kmod will be in world.
#
EXCLUDE_FROM_WORLD = "1"

SRC_URI = "${SOURCEFORGE_MIRROR}/kvm/${BPN}-${PV}.tar.bz2 \
           file://configure-fix-kernel_extraversion-handling.patch \
          "

SRC_URI[md5sum] = "768b0d4daa9ba49e9b09fed1e702a0a9"
SRC_URI[sha256sum] = "2b9ae4e36690804fabb1eac4028495f99939f6abe5518c3f98c3d7c8dd488898"

inherit module

CONF_OPTIONS = "--arch=${TARGET_ARCH} \
                --cross-prefix=${TARGET_PREFIX} \
                --kerneldir=${STAGING_KERNEL_DIR} \
               "

do_configure() {
    ${S}/configure ${CONF_OPTIONS}
}

python do_kvm_kmod_sanity () {
    # The package should be just used by end customers rather than developers
    bb.warn("[info]: kvm-kmod should be used with caution, and not for active development\n")
}
addtask kvm_kmod_sanity before do_configure
