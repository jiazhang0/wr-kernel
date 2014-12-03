#
# Copyright (C) 2012-2013 Wind River Systems, Inc.
#
DESCRIPTION = "A basic initramfs image that boots to a console."

LICENSE = "MIT"

inherit core-image

# allows root login without a password
#
IMAGE_FEATURES += "debug-tweaks"

IMAGE_INSTALL_INITRAMFS = "packagegroup-core-boot-wrs shadow"
IMAGE_LINGUAS = ""

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"

export IMAGE_BASENAME = "wrlinux-image-initramfs"

IMAGE_ROOTFS_SIZE = "8192"

IMAGE_INSTALL = "packagegroup-core-boot-wrs shadow"
# Add any additional packages requested by IMAGE_INSTALL_INITRAMFS
IMAGE_INSTALL += "${IMAGE_INSTALL_INITRAMFS}"

USE_DEVFS = "0"
