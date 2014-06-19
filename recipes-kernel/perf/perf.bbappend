#
# Copyright (C) 2013 Wind River Systems, Inc.
#

PACKAGECONFIG ??= ""

PACKAGECONFIG_append_x86 ??= "numactl"
PACKAGECONFIG_append_x86-64 ??= "numactl"

# enable,disable,depends,rdepends
#
PACKAGECONFIG[audit] = ",,audit,audit"
PACKAGECONFIG[numactl] = ",,numactl,numactl"
