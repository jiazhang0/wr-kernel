#The file is based on SDK1.6

DESCRIPTION = "Reset Control Words (RCW)"
SECTION = "rcw"
LICENSE = "BSD"
PR = "r9"

LIC_FILES_CHKSUM = "file://rcw.py;beginline=8;endline=28;md5=9ba0b28922dd187b06b6c8ebcfdd208e"

# this package is specific to the machine itself
INHIBIT_DEFAULT_DEPS = "1"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy

BR="sdk-v1.6.x"
SRC_URI = "git://git.freescale.com/ppc/sdk/rcw.git;branch=$BR;protocol=git \
	file://0001-fsl_e500mc-Add-RCW-of-P2041-for-SRIO.patch"
SRCREV = "261b2355e9936ecb37b61e6f58dfc48dcfb805b3"

COMPATIBLE_MACHINE = "(fsl-e500mc|fsl-p50xx|fsl-t4xxx|fsl-b4xxx|fsl-t2xxx)"

S = "${WORKDIR}/git"
M_fsl-t4xxx="t4240qds"
M_fsl-e500mc="p2041rdb p3041ds p4080ds"
M_fsl-p50xx="p5040ds p5020ds"
M_fsl-b4xxx="b4860qds b4420qds"
M_fsl-t2xxx="t2080qds t2080rdb"

do_install () {
	make install
}

do_deploy () {
	for i in ${M};do
	  install -d ${DEPLOYDIR}/rcw-${i}
	  install -d ${DEPLOY_DIR_IMAGE}/rcw-${i}
      cp -r ${S}/${i}/${i}/* ${DEPLOYDIR}/rcw-${i}
      cp -r ${S}/${i}/${i}/* ${DEPLOY_DIR_IMAGE}/rcw-${i}
	  cd ${WRL_EXPORT_DIR}
	  ln -s ${DEPLOY_DIR_IMAGE}/rcw-${i} rcw-${i}
	
    done
}
addtask deploy after do_install

ALLOW_EMPTY_${PN} = "1"
