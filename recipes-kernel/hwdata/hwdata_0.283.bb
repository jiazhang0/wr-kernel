#
# Copyright (C) 2015 Wind River Systems, Inc.
#
SUMMARY = "Hardware identification and configuration data."
DESCRIPTION = "This package contains various hardware identification and configuration data, such as the pci.ids database, or the XFree86/xorg Cards database."

LICENSE = "GPL-2.0 | XFree86-1.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263\
                    file://LICENSE;md5=1556547711e8246992b999edd9445a57"

DEPENDS = "pciutils usbutils"

#S="${WORKDIR}/${BPN}-${BP}"

SRC_URI = "https://git.fedorahosted.org/cgit/hwdata.git/snapshot/${BP}.tar.gz"
SRC_URI[md5sum] = "135c729ef9ab4d484805efccc15aca8d"
SRC_URI[sha256sum] = "307e412e243f1373e9075f8eec86b7feefa3d90737139d883b5d4f6114d35e0f"

do_configure() {
     ${S}/configure
}

do_install() {
        make install DESTDIR=${D}
}

PACKAGE_ARCH = "all"
FILES_${PN} += "${libdir}/modprobe.d/dist-blacklist.conf"
