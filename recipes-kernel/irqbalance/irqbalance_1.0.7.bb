#
# Copyright (C) 2013 Wind River Systems, Inc.
#

require irqbalance.inc

PR = "r1"

SRC_URI[md5sum] = "09cb0ab81ab4f3401a7ff5dabc158a1e"
SRC_URI[sha256sum] = "fed1bb405654be8eda40961667bbe75033667600d828b34419c25a1282e81127"

SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/irqbalance/irqbalance-1.0.7.tar.bz2/09cb0ab81ab4f3401a7ff5dabc158a1e/irqbalance-1.0.7.tar.bz2 \
           file://add-initscript.patch \
           file://irqbalance-Add-status-and-reload-commands.patch \
           file://fix-configure-libcap-ng.patch \
           file://irqbalanced.service \
          "
