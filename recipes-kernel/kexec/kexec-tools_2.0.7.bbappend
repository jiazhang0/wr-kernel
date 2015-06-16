#
# Copyright (C) 2015 Wind River Systems, Inc.
#
FILESEXTRAPATHS_prepend := "${THISDIR}:"

SRC_URI += "file://0001-purgatory-Disabling-GCC-s-stack-protection.patch \
	    file://0002-powerpc-change-the-memory-size-limit.patch"
