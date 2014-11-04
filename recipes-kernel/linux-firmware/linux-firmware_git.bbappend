#
# Copyright (C) 2014 Wind River Systems, Inc.
#

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = " \
		    file://LICENSE.radeon;md5=07b0c31777bd686d8e1609c6940b5e74 \
                    file://LICENSE.dib0700;md5=f7411825c8a555a1a3e5eab9ca773431 \
                    file://LICENCE.xc5000;md5=1e170c13175323c32c7f4d0998d53f66 \
                    file://LICENCE.ralink-firmware.txt;md5=ab2c269277c45476fb449673911a2dfd \
                    file://LICENCE.qla2xxx;md5=f5ce8529ec5c17cb7f911d2721d90e91 \
                    file://LICENCE.iwlwifi_firmware;md5=5106226b2863d00d8ed553221ddf8cd2 \
                    file://LICENCE.i2400m;md5=14b901969e23c41881327c0d9e4b7d36 \
                    file://LICENCE.atheros_firmware;md5=30a14c7823beedac9fa39c64fdd01a13 \
                    file://LICENCE.agere;md5=af0133de6b4a9b2522defd5f188afd31 \
                    file://LICENCE.rtlwifi_firmware.txt;md5=00d06cfd3eddd5a2698948ead2ad54a5 \
                    file://LICENCE.ibt_firmware;md5=fdbee1ddfe0fb7ab0b2fcd6b454a366b \
		   "

SRCREV = "365e80cce17e033cc612ac6b3f74a28c2753f184"

FWPATH = "/lib/firmware"

do_install_append() {
	install -d ${D}${FWPATH}
	install -d ${D}${FWPATH}/bnx2x
	install -d ${D}${FWPATH}/bnx2
	install -m 0644 LICENCE.iwlwifi_firmware ${D}${FWPATH}
	install -m 0644 iwlwifi-5000-5.ucode ${D}${FWPATH}
	install -m 0644 iwlwifi-7260-9.ucode ${D}${FWPATH}
	install -m 0644 iwlwifi-7265-9.ucode ${D}${FWPATH}
	install -d ${D}${FWPATH}/intel
	install -m 0644 intel/ibt-hw-37.7.10-fw-1.80.2.3.d.bseq ${D}${FWPATH}/intel
	install -m 0644 intel/ibt-hw-37.8.10-fw-1.10.2.27.d.bseq ${D}${FWPATH}/intel
	install -m 0644 bnx2x/bnx2x-e2-7.2.16.0.fw ${D}${FWPATH}/bnx2x
	install -m 0644 bnx2x/bnx2x-e2-7.8.17.0.fw ${D}${FWPATH}/bnx2x
	install -m 0644 bnx2/bnx2-rv2p-09-6.0.17.fw ${D}${FWPATH}/bnx2
	install -m 0644 bnx2/bnx2-rv2p-09ax-6.0.17.fw ${D}${FWPATH}/bnx2
	install -m 0644 bnx2/bnx2-mips-09-6.2.1b.fw ${D}${FWPATH}/bnx2
	install -m 0644 bnx2/bnx2-rv2p-06-6.0.15.fw ${D}${FWPATH}/bnx2
	install -m 0644 bnx2/bnx2-mips-06-6.2.3.fw ${D}${FWPATH}/bnx2
	install -d ${D}${FWPATH}/rtl_nic
	install -m 0644 rtl_nic/rtl8168g-2.fw ${D}${FWPATH}/rtl_nic
}

PACKAGES =+ "\
	     ${PN}-iwlwifi-5000-5 \
	     ${PN}-iwlwifi-7260-9 \
	     ${PN}-iwlwifi-7265-9 \
	     ${PN}-ibt-hw-37.7.10-fw-1.80.2.3.d \
	     ${PN}-ibt-hw-37.8.10-fw-1.10.2.27.d \
	     ${PN}-bnx2x-e2-7.2.16.0 \
	     ${PN}-bnx2x-e2-7.8.17.0 \
	     ${PN}-bnx2-rv2p-09-6.0.17 \
	     ${PN}-bnx2-rv2p-09ax-6.0.17 \
	     ${PN}-bnx2-mips-09-6.2.1b \
	     ${PN}-bnx2-rv2p-06-6.0.15 \
	     ${PN}-bnx2-mips-06-6.2.3 \
	     ${PN}-rtl8168g-2 \
	    "

RDEPENDS_${PN}-iwlwifi-5000-5 = "${PN}-iwlwifi-license"
FILES_${PN}-iwlwifi-5000-5 = " \
  ${FWPATH}/iwlwifi-5000-5.ucode \
"

RDEPENDS_${PN}-iwlwifi-7260-9 = "${PN}-iwlwifi-license"
FILES_${PN}-iwlwifi-7260-9 = " \
  ${FWPATH}/iwlwifi-7260-9.ucode \
"

RDEPENDS_${PN}-iwlwifi-7265-9 = "${PN}-iwlwifi-license"
FILES_${PN}-iwlwifi-7265-9 = " \
  ${FWPATH}/iwlwifi-7265-9.ucode \
"

RDEPENDS_${PN}-ibt-hw-37.7.10-fw-1.80.2.3.d = "${PN}-iwlwifi-license"
FILES_${PN}-ibt-hw-37.7.10-fw-1.80.2.3.d = " \
  ${FWPATH}/intel/ibt-hw-37.7.10-fw-1.80.2.3.d.bseq \
"

RDEPENDS_${PN}-ibt-hw-37.8.10-fw-1.10.2.27.d = "${PN}-iwlwifi-license"
FILES_${PN}-ibt-hw-37.8.10-fw-1.10.2.27.d = " \
  ${FWPATH}/intel/ibt-hw-37.8.10-fw-1.10.2.27.d.bseq \
"

FILES_${PN}-bnx2x-e2-7.2.16.0 = " \
  ${FWPATH}/bnx2x/bnx2x-e2-7.2.16.0.fw \
"

FILES_${PN}-bnx2x-e2-7.8.17.0 = " \
  ${FWPATH}/bnx2x/bnx2x-e2-7.8.17.0.fw \
"

FILES_${PN}-bnx2-rv2p-09-6.0.17 = " \
  ${FWPATH}/bnx2/bnx2-rv2p-09-6.0.17.fw \
"

FILES_${PN}-bnx2-rv2p-09ax-6.0.17 = " \
  ${FWPATH}/bnx2/bnx2-rv2p-09ax-6.0.17.fw \
"

FILES_${PN}-bnx2-mips-09-6.2.1b = " \
  ${FWPATH}/bnx2/bnx2-mips-09-6.2.1b.fw \
"

FILES_${PN}-bnx2-rv2p-06-6.0.15 = " \
  ${FWPATH}/bnx2/bnx2-rv2p-06-6.0.15.fw \
"

FILES_${PN}-bnx2-mips-06-6.2.3 = " \
  ${FWPATH}/bnx2/bnx2-mips-06-6.2.3.fw \
"

FILES_${PN}-rtl8168g-2 = " \
  ${FWPATH}/rtl_nic/rtl8168g-2.fw \
"

FILES_${PN}-bcm4329 = " \
  /lib/firmware/brcm/brcmfmac4329-sdio.bin \
"

FILES_${PN}-bcm4330 = " \
  /lib/firmware/brcm/brcmfmac4330-sdio.bin \
"

FILES_${PN}-bcm4334 = " \
  /lib/firmware/brcm/brcmfmac4334-sdio.bin \
"
ALTERNATIVE_TARGET_linux-firmware-bcm4329[brcmfmac-sdio.bin] = "/lib/firmware/brcm/brcmfmac4329-sdio.bin"
ALTERNATIVE_TARGET_linux-firmware-bcm4330[brcmfmac-sdio.bin] = "/lib/firmware/brcm/brcmfmac4330-sdio.bin"
ALTERNATIVE_TARGET_linux-firmware-bcm4334[brcmfmac-sdio.bin] = "/lib/firmware/brcm/brcmfmac4334-sdio.bin"
