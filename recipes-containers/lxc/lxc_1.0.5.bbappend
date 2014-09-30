# Fix configure.ac:82: error: possibly undefined macro: AC_MSG_ERROR
inherit pkgconfig

# For aarch64
COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|aarch64.*|powerpc.*|mips.*)-linux'
