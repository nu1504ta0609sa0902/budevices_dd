package com.mhra.mdcm.devices.dd.appian;

import com.mhra.mdcm.devices.dd.appian._junit_smokes.smoke.SmokeTestsAuthorisedRep;
import com.mhra.mdcm.devices.dd.appian._junit_smokes.smoke.SmokeTestsBusiness;
import com.mhra.mdcm.devices.dd.appian._junit_smokes.smoke.SmokeTestsManufacturers;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({SmokeTestsBusiness.class, SmokeTestsManufacturers.class, SmokeTestsAuthorisedRep.class} )
public class RunAllSmokeTest {
}
