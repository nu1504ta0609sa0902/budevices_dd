"# budevices_dd"

-- Example running the tests from command prompt

1. Open command prompt and navigate to project directory

NOTE: Your project path will be different
cd C:\Users\TPD_Auto\Desktop\Noor\AutomationProjects\JavaProject\MHRA_MDCM_DEVICES_DD

2. Run one of the commands below:

IE:
mvn clean test -Dtest=RunAllSmokeTest -Dcurrent.browser=ie -Dspring.profiles.active=mhratest
GC:
mvn clean test -Dtest=RunAllSmokeTest -Dcurrent.browser=gc -Dspring.profiles.active=mhratest
FF:
mvn clean test -Dtest=RunAllSmokeTest -Dcurrent.browser=ff -Dspring.profiles.active=mhratest

