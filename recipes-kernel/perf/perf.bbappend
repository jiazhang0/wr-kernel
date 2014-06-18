#
# Copyright (C) 2013 Wind River Systems, Inc.
#

PERF_FEATURES_ENABLE_append_x86 ??= " numactl"
PERF_FEATURES_ENABLE_append_x86-64 ??= "numactl"

AUDIT_DEPENDS = "${@perf_feature_enabled('audit', 'audit', '',d)}"
NUMACTL_DEPENDS = "${@perf_feature_enabled('numactl', 'numactl', '',d)}"

DEPENDS += "${AUDIT_DEPENDS} ${NUMACTL_DEPENDS}"

AUDIT_DEFINES = "${@perf_feature_enabled('audit', '', 'NO_LIBAUDIT=1', d)}"
NUMACTL_DEFINES = "${@perf_feature_enabled('numactl', '', 'NO_LIBNUMA=1', d)}"

EXTRA_OEMAKE += "${AUDIT_DEFINES} ${NUMACTL_DEFINES}"
