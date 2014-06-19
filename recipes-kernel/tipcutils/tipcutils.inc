#
# Copyright (C) 2013 Wind River Systems, Inc.
#
SUMMARY = "Transparent Inter Process Communication Protocol for Linux (TIPC)"

DESCRIPTION = "The Transparent Inter-Process Communication protocol \
allows applications in a clustered computer environment to communicate \
quickly and reliably with other applications, regardless of their \
location within the cluster."

HOMEPAGE = "http://tipc.sourceforge.net"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=16520&atid=116520"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://tipc-config/tipc-config.c;beginline=9;endline=31;md5=3c1c49db86295dce8dec0561a3c66439"

INC_PR = "r0"

inherit autotools

EXTRA_OEMAKE = "CC='${CC}'"
