#
# Copyright (C) 2012 Wind River Systems, Inc.
#
SUMMARY = "A library which provides easy access to huge pages of memory"
DESCRIPTION = "libhugetlbfs is a library which provides easy access to huge \
pages of memory. It is a wrapper for the hugetlbfs file system. Applications \
can use huge pages to fulfill malloc() requests without being recompiled by \
using LD_PRELOAD. Alternatively, applications can be linked against \
libhugetlbfs without source modifications to load BSS or BSS, data, and text \
segments into large pages."
HOMEPAGE = "http://libhugetlbfs.sourceforge.net/"
SECTION = "kernel/userland"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://LGPL-2.1;md5=2d5025d4aa3495befef8f17206a5b0a1"

SRC_URI = "${SOURCEFORGE_MIRROR}/libhugetlbfs/libhugetlbfs-${PV}.tar.gz \
          file://Aarch64-support.patch \
          file://Aarch64-unit-test-fixes.patch \
          file://libhugetlbfs-Fix-cross-compile.patch \
          file://libhugetlbfs-Only-update-mtab-when-it-s-not-a-symlin.patch \
          file://libhugetlbfs-Fix-tests-install.patch \
          file://libhugetlbfs-Don-t-install-LDSCRIPT_TESTS.patch \
          file://libhugetlbfs-Fix-hugeadm-path-in-run_tests.py.patch \
          file://libhugetlbfs-Add-install-ptest-target.patch \
          file://run-ptest \
          file://libhugetlbfs-Fix-typo-error-in-Makfile.patch \
          file://libhugetlbfs-avoid-search-host-library-path-for-cros.patch \
          "

inherit autotools-brokensep ptest

PR = "r0"

COMPATIBLE_HOST = '(i.86|x86_64|powerpc|powerpc64|arm).*-linux'

# Specify arch according to TARGET_ARCH, instead of letting it use `uname`.
# Define BUILDTYPE=NATIVEONLY so that it won't try to cross-compile a 32bit
# version when ARCH is 64bit.
EXTRA_OEMAKE = "PREFIX=${prefix} ARCH=${TARGET_ARCH} BUILDTYPE=NATIVEONLY V=1"

# Parallel builds are not reliable for this package.
# Serialize since the compile time is small ( ~10 sec)
PARALLEL_MAKE = ""

do_install_append() {
    # we don't need perl modules for now
    rm -rf ${D}/usr/lib/perl5
    rm ${D}${bindir}/cpupcstat ${D}${bindir}/oprofile_*
    # remove the empty /usr/lib once /usr/lib/perl5 is gone
    if [ "${baselib}" != "lib" ]; then
        rm -rf ${D}/usr/lib
    fi
}

do_install_ptest() {
	oe_runmake prefix="${prefix}" DESTDIR="${D}" install-tests
	mv ${D}${libdir}/libhugetlbfs/tests ${D}${PTEST_PATH}
	mv ${D}${bindir}/oprofile* ${D}${PTEST_PATH}/

	# we don't need perl modules for now
	rm -rf ${D}/usr/lib/perl5
	# remove the empty /usr/lib once /usr/lib/perl5 is gone
	if [ "${baselib}" != "lib" ]; then
		rm -rf ${D}/usr/lib
	fi
}

FILES_${PN} += "${libdir}/libhugetlbfs.so ${libdir}/libhugetlbfs_privutils.so"

FILES_${PN}-dev = "${includedir}/*.h ${datadir}/${BPN}/ld.hugetlbfs \
		${datadir}/${BPN}/ld ${datadir}/${BPN}/ldscripts/* \
		"

RDEPENDS_${PN}-dev += "bash"

FILES_${PN}-dbg += "${libdir}/libhugetlbfs/tests/obj64/.debug ${libdir}/libhugetlbfs/tests/obj32/.debug"

SRC_URI[md5sum] = "70666447c81f4fb484fee94e40b1ea69"
SRC_URI[sha256sum] = "1a473ebc70e80d8c452f359b05bc45d1fb87e6e2425d216c06486e30acf86ed2"
