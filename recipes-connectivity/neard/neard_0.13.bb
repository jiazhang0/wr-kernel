#
# Copyright (C) 2013 Wind River Systems, Inc.
#

require neard.inc

SRC_URI = "git://git.kernel.org/pub/scm/network/nfc/neard.git \
           file://neard.in \
           file://neard.service.in \
           file://add_dependence.patch \
          "

S = "${WORKDIR}/git"
SRCREV = "b2e1b68046b472d545dea570f7be9fa25dabbd20"
PV = "0.13+git${SRCPV}"
