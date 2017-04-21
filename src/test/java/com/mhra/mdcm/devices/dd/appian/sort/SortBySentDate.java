package com.mhra.mdcm.devices.dd.appian.sort;

import javax.mail.Message;
import java.util.Comparator;

/**
 * Created by TPD_Auto on 28/07/2016.
 */
public class SortBySentDate implements Comparator<Message> {
    @Override
    public int compare(Message o1, Message o2) {
        try {
            long to1 = o1.getSentDate().getTime();
            long to2 = o2.getSentDate().getTime();

            if(to1 > to2)
                return 1;
            else if(to1 <= to2)
                return 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
