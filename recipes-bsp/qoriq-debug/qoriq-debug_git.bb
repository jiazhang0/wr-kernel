#
# Copyright (C) 2014 Wind River Systems, Inc.
#
# This bb and tarball are copied from fsl sdk 1.6
DESCRIPTION = "QorIQ Debug File System Module"
SECTION = "qoriq-debug"
LICENSE = "GPL-2.0"
PR = "r4"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

COMPATIBLE_HOST = 'powerpc.*-linux'

COMPATIBLE_MACHINE = "(fsl)"

# When building on a multilib capable BSP, it's possible for the
# kernel bit size to be different then the userspace. Avoid the QA test
# when building modules
INSANE_SKIP_${PN} = "arch"

inherit module autotools-brokensep

SRC_URI = "git://git.freescale.com/ppc/sdk/qoriq-debug.git;nobranch=1"
SRCREV = "20615c1ea332102635f8314cee5787c48c1a4254"

S = "${WORKDIR}/git"

EXTRA_OECONF += "--with-linux=${STAGING_KERNEL_DIR}"
EXTRA_OEMAKE += 'SYSROOT="${D}"'

python () {
	ma = d.getVar("DISTRO_FEATURES", True)
	arch = d.getVar("OVERRIDES", True)

	# the : after the arch is to skip the message on 64b
	if not "multiarch" in ma and ("e5500:" in arch or "e6500:" in arch):
		raise bb.parse.SkipPackage("Building the kernel for this arch requires multiarch to be in DISTRO_FEATURES")

	promote_kernel = d.getVar('BUILD_64BIT_KERNEL')

	if promote_kernel == "1":
		d.setVar('KERNEL_CC_append', ' -m64')
		d.setVar('KERNEL_LD_append', ' -melf64ppc')

	error_qa = d.getVar('ERROR_QA', True)
	if 'arch' in error_qa:
		d.setVar('ERROR_QA', error_qa.replace(' arch', ''))
}
