# WR Does not use the lttng modules package
LTTNGMODULES = ""

SYSTEMTAP_mips64 = ""
VALGRIND_mips64 = ""
VALGRIND_powerpc = ""

PACKAGES += "${PN}-lttng"

RDEPENDS_${PN}-lttng = "\
    ${LTTNGUST} \
    ${LTTNGTOOLS} \
    ${LTTNGMODULES} \
    ${BABELTRACE} \
    "
