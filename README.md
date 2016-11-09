"# budevices_dd"

<hr/>
Note there is 2 ways we can run these tests:<br/>
1. Via the IDE: <br/>
    * JUnit : reports are no good<br/>
    - Maven : reports are slightly better<br/>
        - After running the tests we will need to execute the following commands:<br/>
            - mvn surefire-report:report-only<br/>
            - mvn site -DgenerateReports=false<br/>
2. Simply run one of the following commands below:<br/>

<hr/>
-- Example running the tests from command prompt

A. Open command prompt and navigate to project directory

NOTE: Your project path will be different<br/>
cd C:\Users\TPD_Auto\Desktop\Noor\AutomationProjects\JavaProject\MHRA_MDCM_DEVICES_DD

B. Run one of the commands below:

IE:<br/>
mvn clean test -Dtest=RunAllSmokeTest -Dcurrent.browser=ie -Dspring.profiles.active=mhradevicestest
<br/>
GC:<br/>
mvn clean test -Dtest=RunAllSmokeTest -Dcurrent.browser=gc -Dspring.profiles.active=mhradevicestest
<br/>
FF:<br/>
mvn clean test -Dtest=RunAllSmokeTest -Dcurrent.browser=ff -Dspring.profiles.active=mhradevicestest
<br/>
PhantomJS Headless :<br/>
mvn clean test -Dtest=RunAllSmokeTest -Dcurrent.browser=pjs -Dspring.profiles.active=mhradevicestest
<br/>

<hr/>
Run Specific Tests:<br/>

mvn clean test -Dtest=SmokeTestsBusiness#your_test_name -Dcurrent.browser=ie -Dspring.profiles.active=mhradevicestest
<br/>mvn clean test -Dtest=SmokeTestsBusiness#your_test_name1+your_test_name2+your_test_name3 -Dcurrent.browser=ie -Dspring.profiles.active=mhradevicestest
<br/>Example:
<br/>mvn clean test -Dtest=SmokeTestsBusiness#asAUserIShouldBeAbleToLoginAndLogout -Dcurrent.browser=ie -Dspring.profiles.active=mhradevicestest

<hr/>
C. Generate the reports<br/>
mvn surefire-report:report
 <br/>
mvn clean test surefire-report:report
 <br/>

Create Better Reports With These Commands: <br/>
# Run tests and generate .xml reports
mvn test
# Convert .xml reports into .html report, but without the CSS or images
mvn surefire-report:report-only
# Put the CSS and images where they need to be without the rest of the time-consuming stuff
mvn site -DgenerateReports=false

mvn site : is very slow
mvn surefire-report:report-only + mvn site -DgenerateReports=false = faster solution


