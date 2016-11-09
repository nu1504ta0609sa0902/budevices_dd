package com.mhra.mdcm.devices.dd.appian;

import com.mhra.mdcm.devices.dd.appian._test.junit.smoke.SmokeTestsAuthorisedRep;
import com.mhra.mdcm.devices.dd.appian._test.junit.smoke.SmokeTestsBusiness;
import com.mhra.mdcm.devices.dd.appian._test.junit.smoke.SmokeTestsManufacturers;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({SmokeTestsBusiness.class} )
//@Suite.SuiteClasses({SmokeTestsBusiness.class, SmokeTestsManufacturers.class, SmokeTestsAuthorisedRep.class} )
//@Suite.SuiteClasses({SmokeTestsManufacturers.class, SmokeTestsAuthorisedRep.class} )
public class RunAllSmokeTest {
}
