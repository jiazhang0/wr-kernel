SUMMARY = "Initramfs kernel boot"
DESCRIPTION = "This package provides a compressed cpio image used for an \
initial ram disk for the kernel boot."

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

EXCLUDE_FROM_WORLD = "1"

DEPENDS = "virtual/kernel"

PROVIDES = "virtual/kernel-initramfs-image"

inherit linux-kernel-base kernel-arch

do_populate_lic[depends] += "virtual/kernel:do_populate_sysroot"

S = "${STAGING_KERNEL_DIR}"
do_unpack[depends] += "virtual/kernel:do_populate_sysroot"

B = "${WORKDIR}/${BPN}-${PV}"

FILES_${PN} = "/boot/${KERNEL_IMAGETYPE}-initramfs-*.bin"
INITRAMFS_BASE_NAME = "${KERNEL_IMAGETYPE}-initramfs-${PV}-${PR}-${MACHINE}-${DATETIME}"
INITRAMFS_BASE_NAME[vardepsexclude] = "DATETIME"

python __anonymous () {
    image = d.getVar('INITRAMFS_IMAGE', True)
    if image:
        d.appendVarFlag('do_install', 'depends', ' ${INITRAMFS_IMAGE}:do_rootfs')
}

do_install() {
	if [ -z "${INITRAMFS_IMAGE}" ] ; then
		exit 0
	fi
	echo "Copying initramfs from ${DEPLOY_DIR_IMAGE} ..."
	for img in cpio.gz cpio.lzo cpio.lzma cpio.xz; do
		if [ -e "${DEPLOY_DIR_IMAGE}/${INITRAMFS_IMAGE}-${MACHINE}.$img" ]; then
			install -d ${D}/boot
			install -m 0644 ${DEPLOY_DIR_IMAGE}/${INITRAMFS_IMAGE}-${MACHINE}.$img ${D}/boot/${KERNEL_IMAGETYPE}-initramfs-${MACHINE}.bin
			break
		fi
	done
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
