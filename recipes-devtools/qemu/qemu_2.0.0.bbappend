EXTRA_OECONF_append += " --enable-fdt"

# needed for libvirt
do_install_append () {
    cd ${D}${bindir}
    # kvm is the symlink of target qemu-system-${ARCH}
    arch=`echo ${TARGET_ARCH} | sed 's/i[456]86/i386/' | sed 's/powerpc/ppc/'`
    ln -sf qemu-system-${arch} kvm
    install -m 755 ${S}/scripts/kvm/kvm_stat ${D}${bindir}
    sed -i '1 s#/usr/bin/python#/usr/bin/env python#' ${D}${bindir}/kvm_stat
}
