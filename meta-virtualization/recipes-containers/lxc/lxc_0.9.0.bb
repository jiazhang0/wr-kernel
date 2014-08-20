#
# Copyright (C) 2013 Wind River Systems, Inc.
#
SUMMARY = "A userspace control package for Linux Containers"

DESCRIPTION = "LXC builds up from chroot to implement complete virtual \
systems, adding resource management and isolation mechanisms to \
Linux’s existing process management infrastructure. The LXC package \
combines the Linux kernel containers mechanisms to provide a userspace \
container object, a lightweight virtual system with full resource \
isolation and resource control for an application or a system."

HOMEPAGE = "http://lxc.sourceforge.net/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=163076&atid=826303"

DEPENDS = "virtual/kernel libpcap libcap"

LICENSE = "LGPLv2.1+"

PR = "r1"

LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

SRC_URI = "${SOURCEFORGE_MIRROR}/${BPN}/${BPN}-${PV}.tar.gz \
           file://add_wrlinux_distro.patch \
           file://AC_ARG_ENABLE_fix.patch \
           file://lxc-Add-template-script-for-WRLinux.patch \
           file://lxc-Add-container-name-check-in-lxc-wrlinux.patch \
           file://lxc-fix-the-subdir-objects-error.patch \
	   file://0001-sshd-Don-t-bind-mount-sbin-init-read-write.patch \
          "

SRC_URI[md5sum] = "8552a4479090616f4bc04d8473765fc9"
SRC_URI[sha256sum] = "1e1767eae6cc5fbf892c0e193d25da420ba19f2db203716c38f7cdea3b654120"

COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|powerpc.*|mips.*)-linux'

inherit autotools-brokensep

EXTRA_OECONF_append = " --disable-doc \
                        --disable-rpath \
                        --with-distro=wrlinux \
                        --disable-examples \
                        --disable-tests \
                        --disable-configpath-log \
                        --enable-capabilities \
                      "

FILES_${PN}-dbg += "${libexecdir}/lxc/.debug"

# Fix some PACKAGECONFIG syntax in the bb file.
PACKAGECONFIG[doc] = "--enable-doc,--disable-doc,,"
PACKAGECONFIG[rpath] = "--enable-rpath,--disable-rpath,,"

# Not currently supported.
PACKAGECONFIG[lua] = "--enable-lua,--disable-lua,lua,"
PACKAGECONFIG[python3] = "--enable-python,--disable-python,python3,"
PACKAGECONFIG[seccomp] = "--enable-seccomp,--disable-seccomp,libseccomp,"
