SUMMARY = "The implementation of SOME/IP"
DESCRIPTION = "The vsomeip stack implements the http://some-ip.com/ \
(Scalable service-Oriented MiddlewarE over IP (SOME/IP)) protocol."
HOMEPAGE = "https://github.com/COVESA/vsomeip"
SECTION = "net"

LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=815ca599c9df247a0c7f619bab123dad"

SRC_URI = "git://github.com/GENIVI/${BPN}.git;branch=master;protocol=https;name=vsomeip \
           file://0001-Fix-pkgconfig-dir-for-multilib.patch \
           file://0002-Install-example-configuration-files-to-etc-vsomeip.patch \
           file://0003-Do-not-build-external-gtest.patch \
           file://0004-Support-boost-1.76.patch \
           file://0005-Add-boost-1.78-support-so-that-vsomeip3-will-compile.patch \
           file://0006-Support-boost-1.82.patch \
           file://0007-Do-general-cleanup-of-warnings-that-gcc-12.1.1-fired.patch \
          "

SRCREV = "13f9c89ced6ffaeb1faf485152e27e1f40d234cd"

COMPATIBLE_HOST:mips = "null"
COMPATIBLE_HOST:mips64 = "null"
COMPATIBLE_HOST:powerpc = "null"
COMPATIBLE_HOST:libc-musl = 'null'

DEPENDS = "boost dlt-daemon googletest"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

EXTRA_OECMAKE = "-DINSTALL_LIB_DIR:PATH=${baselib} \
                 -DINSTALL_CMAKE_DIR:PATH=${baselib}/cmake/vsomeip3 \
                 -DCMAKE_CXX_STANDARD=14 \
                "

# For vsomeip-test
EXTRA_OECMAKE += "-DTEST_IP_MASTER=10.0.3.1 \
                  -DTEST_IP_SLAVE=10.0.3.2 \
                  -DTEST_IP_SLAVE_SECOND=10.0.3.3 \
                 "

RDEPENDS:${PN}-test += "bash"

do_compile:append() {
    cmake_runcmake_build --target examples
    cmake_runcmake_build --target build_tests
}

do_install:append() {
    install -d ${D}/opt/${PN}-test/examples
    install -m 0755 ${B}/examples/*-sample ${D}/opt/${PN}-test/examples
    install -d ${D}/opt/${PN}-test/examples/routingmanagerd
    install -m 0755 ${B}/examples/routingmanagerd/routingmanagerd \
        ${D}/opt/${PN}-test/examples/routingmanagerd

    install -d ${D}/opt/${PN}-test/test
    cp -f ${B}/test/*test* ${D}/opt/${PN}-test/test

    cp -rf ${S}/config ${D}/opt/${PN}-test
}

PACKAGES += "${PN}-test"

FILES:${PN}-dbg += " \
   /opt/${PN}-test/.debug/* \
   "
FILES:${PN}-test = " \
   /opt/${PN}-test \
   "
