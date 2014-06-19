#
# Copyright (C) 2012 Wind River Systems, Inc.
#
SUMMARY = "Hardware identification and configuration data." 
DESCRIPTION = "This package contains various hardware identification and configuration data, such as the pci.ids database, or the XFree86/xorg Cards database."

LICENSE = "GPL-2.0 XFree86-1.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://LICENSE;md5=1556547711e8246992b999edd9445a57 "

PR = "r0"

DEPENDS = "pciutils usbutils"

SRC_URI = "http://sourceforge.net/projects/buluoos/files/0.4/src/hwdata-0.234.tar.bz2;name=archive "
SRC_URI[archive.md5sum] = "8108ff494a0a46358894c1b0e61592c6"
SRC_URI[archive.sha256sum] = "d3da2f2e39cac20aaccd5e3d9ea0fe93baa3c999ebfa9a54e891f4c99277228f"

do_install() {
        make install DESTDIR=${D}

        #Use the pci.ids.gz from pciutils instead of from hwdata
        cd ${D}${datadir}/hwdata/ && rm pci.ids && ln -s ../pci.ids.gz pci.ids.gz

        #Use the usb.ids.gz from usbutils instead of from hwdata
        cd ${D}${datadir}/hwdata/ && rm usb.ids && ln -s ../usb.ids.gz usb.ids.gz
}

PACKAGE_ARCH = "all"
