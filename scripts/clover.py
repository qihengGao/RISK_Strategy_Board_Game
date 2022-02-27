import re
import sys

from bs4 import BeautifulSoup

totalCodeCoverage = None

try:
    soup = BeautifulSoup(open("./build/reports/clover/html/dashboard.html"), "html.parser")
    totalCodeCoverage = soup.find(
        string=re.compile("Code coverage")).parent.parent.parent.find(
        name="span", attrs={"class": "sortValue"}).contents
except:
    print("Can't find total code coverage in dashboard.html.")
    sys.exit(1)

try:
    totalCodeCoverage = float(totalCodeCoverage[0])
except:
    print("Can't convert Code coverage to float.")
    sys.exit(1)
print("TOTAL COVERAGE: %.2f%%" % (totalCodeCoverage * 100))
