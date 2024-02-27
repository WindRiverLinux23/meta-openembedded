require python-django.inc
inherit setuptools3


SRC_URI[sha256sum] = "b1260ed381b10a11753c73444408e19869f3241fc45c985cd55a30177c789d13"

RDEPENDS:${PN} += "\
    ${PYTHON_PN}-sqlparse \
    ${PYTHON_PN}-asgiref \
"
