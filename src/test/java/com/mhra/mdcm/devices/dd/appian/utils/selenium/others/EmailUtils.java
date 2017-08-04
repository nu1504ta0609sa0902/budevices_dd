package com.mhra.mdcm.devices.dd.appian.utils.selenium.others;

import com.mhra.mdcm.devices.dd.appian.utils.email.GmailEmail;
import com.mhra.mdcm.devices.dd.appian.utils.selenium.page.WaitUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;

/**
 * Created by TPD_Auto on 04/08/2017.
 */
public class EmailUtils {

    public static boolean verifyEmailReceived(String accountNameOrReference, String subjectHeading){
        boolean foundMessage = false;
        String messageBody = null;
        int attempt = 0;
        do {
            messageBody = GmailEmail.getMessageReceivedWithHeadingAndIdentifier(7, 10, subjectHeading, accountNameOrReference);

            //Break from loop if invoices read from the email server
            if (messageBody!=null) {
                break;
            } else {
                //Wait for 10 seconds and try again, Thread.sleep required because this is checking email
                WaitUtils.nativeWaitInSeconds(10);
            }
            attempt++;
        } while (!foundMessage && attempt < 15);

        return messageBody!=null && messageBody.contains(accountNameOrReference);
    }
}
