#
# Copyright (C) 2012 Wind River Systems, Inc.
#
SRCREV="${AUTOREV}"
LOCALCOUNT = "0"

THISDIR := "${@os.path.dirname(d.getVar('FILE', True))}"

LIC_FILES_CHKSUM = "file://git/tools/kgit;beginline=5;endline=9;md5=d8d1d729a70cd5f52972f8884b80743d"

KERN_TOOLS_SRC = "${THISDIR}/../../git/windriver-kernel-tools.git;branch=WRLINUX_7_0;protocol=file"
SRC_URI = "git://${KERN_TOOLS_SRC}"

