#
# Copyright (C) 2013 Wind River Systems, Inc.
#
inherit kernel
# Decide which kernel inc to pull in.  MULTI_KVM_GUEST_TRIGGER is
# defined in local.conf and is set by configure flag --enable-kvm-multi-guest-build
KERNEL_INC = "${@base_conditional('MULTI_KVM_GUEST_TRIGGER', '1','kvm-guest-kernel.inc','linux-windriver.inc',d)}"
require recipes-kernel/linux/${KERNEL_INC}

KBRANCH_DEFAULT ?= "standard/base"
KBRANCH_DEFAULT_preempt-rt ?= "standard/preempt-rt/base"
KBRANCH_DEFAULT_cgl ?= "standard/cgl/base"
KBRANCH_DEFAULT_tiny ?= "standard/tiny/base"

KBRANCH = "${KBRANCH_DEFAULT}"

LINUX_VERSION = "3.14"

PR = "r0"
PV = "${LINUX_VERSION}"

# This checks the MACHINE and KTYPE_ENABLED against the TARGET_SUPPORTED_KTYPES.
# If the MACHINE supports the kernel type, and the ktype is enabled in local.conf
# the machine is declared compatible
COMPATIBLE_MACHINE ?= '${@ machine_ktype_compatibility(d,"${LINUX_KERNEL_TYPE}")}'

# disabled: linux-tools.inc provides a perf building/packaging
# hook. Instead we'll use a standalone perf package.
# require recipes-kernel/linux/linux-tools.inc

KSRC_linux_windriver_3_14 ?= "${THISDIR}/../../git/kernel-3.14.x.git"
SRC_URI = "git://${KSRC_linux_windriver_3_14};protocol=file;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta${EXTRA_KERNEL_SRC_URI}"

