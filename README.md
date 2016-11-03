"# budevices_dd"

-- Example running the tests from command prompt

1. Open command prompt and navigate to project directory

NOTE: Your project path will be different<br/>
cd C:\Users\TPD_Auto\Desktop\Noor\AutomationProjects\JavaProject\MHRA_MDCM_DEVICES_DD

2. Run one of the commands below:

IE:<br/>
mvn clean test -Dtest=RunAllSmokeTest -Dcurrent.browser=ie -Dspring.profiles.active=mhratest<br/>
GC:<br/>
mvn clean test -Dtest=RunAllSmokeTest -Dcurrent.browser=gc -Dspring.profiles.active=mhratest<br/>
FF:<br/>
mvn clean test -Dtest=RunAllSmokeTest -Dcurrent.browser=ff -Dspring.profiles.active=mhratest<br/>

