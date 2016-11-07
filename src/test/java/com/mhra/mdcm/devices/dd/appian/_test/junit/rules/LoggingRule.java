package com.mhra.mdcm.devices.dd.appian._test.junit.rules;

import com.mhra.mdcm.devices.dd.appian.pageobjects._Page;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by TPD_Auto on 07/11/2016.
 */
public class LoggingRule implements TestRule {

    public final Logger log = LoggerFactory.getLogger(LoggingRule.class);
    @Override
    public Statement apply(final Statement stmt, final Description desc) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                log.info("Tests : "+ desc.getMethodName());
                stmt.evaluate();
            }
        };
    }
}
