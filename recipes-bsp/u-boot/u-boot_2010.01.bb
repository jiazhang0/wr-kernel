#
# Copyright (C) 2012 Wind River Systems, Inc.
#
require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb"

# This revision corresponds to the tag "v2010.01"
# We use the revision in order to avoid having to fetch it from the repo during parse
SRCREV = "3a9d879f6f64585b819af728b53be0a05037fe0d"

SRC_URI = "git://git.denx.de/u-boot.git;branch=master;protocol=git "
SRC_URI_append_lsi-axm55xx = "file://0001-u-boot-Add-u-boot-support-for-lsi.patch "

S = "${WORKDIR}/git"

do_compile () {
	unset LDFLAGS
	unset CFLAGS
	unset CPPFLAGS

	if [ "x${UBOOT_MACHINES}" == "x" ]; then
		UBOOT_MACHINES=${UBOOT_MACHINE}
	fi

	for board in ${UBOOT_MACHINES}; do
		oe_runmake distclean
		oe_runmake ${board}_config
		oe_runmake all
		
		cp ${S}/u-boot.img  ${S}/u-boot-${board}.img
		cp ${S}/spl/u-boot-spl.bin ${S}/u-boot-spl-${board}.bin
	done
}

do_install(){
	if [ "x${UBOOT_MACHINES}" == "x" ]; then
		UBOOT_MACHINES=${UBOOT_MACHINE}
	fi
	
	echo ${UBOOT_MACHINES}

	for board in ${UBOOT_MACHINES}; do
		if [ -f ${S}/u-boot-${board}.img ]; then
			mkdir -p ${D}/boot/
			install ${S}/u-boot-${board}.img ${D}/boot/u-boot-${board}-${PV}-${PR}.img
			install ${S}/u-boot-spl-${board}.bin ${D}/boot/u-boot-spl-${board}-${PV}-${PR}.bin
		fi
	done
}

do_deploy(){
	if [ "x${UBOOT_MACHINES}" == "x" ]; then
		UBOOT_MACHINES=${UBOOT_MACHINE}
	fi

	for board in ${UBOOT_MACHINES}; do
	        if [ -f ${S}/${board}/u-boot-${board}.img ]; then
			mkdir -p ${DEPLOYDIR}
			install ${S}/u-boot-${board}.img ${DEPLOYDIR}/u-boot-${board}-${PV}-${PR}.img
			install ${S}/u-boot-spl-${board}.bin ${DEPLOYDIR}/u-boot-spl-${board}-${PV}-${PR}.bin

			cd ${DEPLOYDIR}
			rm -f u-boot-${board}.img
			rm -f u-boot-spl-${board}.bin
		fi
	done
}
addtask deploy after do_install
