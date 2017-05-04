package com.mhra.mdcm.devices.dd.appian.utils.email;

import com.mhra.mdcm.devices.dd.appian.sort.SortByMessageNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author TPD_Auto
 */
public class GmailEmail {

    public static final Logger log = LoggerFactory.getLogger(GmailEmail.class);

    public static final String UNINVOICED_NOTIFICATIONS = "Uninvoiced Notifications";
    public static final String ANNUAL_INVOICED_NOTIFICATIONS = "Annual Notification Invoices";

    public static void main(String[] args) {
        String name = "ManufacturerRT01Test_2_5_";
        String body = GmailEmail.readMessageForSpecifiedOrganisations(15, 10, "Manufacturer Registration Request for " , name);
        log.warn(body);
    }


    /**
     *
     * Read message received in the last few minutes for the specified organisation
     * @param min
     * @param numberOfMessgesToCheck
     * @param subjectHeading
     * @param organisationName
     * @return
     */
    public static String readMessageForSpecifiedOrganisations(double min, int numberOfMessgesToCheck, String subjectHeading, String organisationName) {

        subjectHeading = subjectHeading + " " + organisationName;
        log.info("Waiting for email with heading : " + subjectHeading);
        String bodyText = null;
        Properties props = getEmailServerConfiguration();

        try {

            final String username = "mhra.uat@gmail.com";
            final String password = "MHRA1234";

            Session session = Session.getInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            Store store = session.getStore("imaps");
            store.connect("smtp.gmail.com", username, password);

            Folder inbox = store.getFolder("inbox");
            inbox.open(Folder.READ_ONLY);

            int messageCount = inbox.getMessageCount();
            Message[] messages = inbox.getMessages();
            List<Message> lom = Arrays.asList(messages);
            Collections.sort(lom, new SortByMessageNumber());
            messages = lom.toArray(new Message[lom.size()]);
            //log.warn("Number of messages : " + messages.length);


            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                Date sentDate = message.getSentDate();
                String subject = message.getSubject();
                //log.warn(subject);
                Address[] froms = message.getFrom();

                for (Address from : froms) {
                    String emailAddress = froms == null ? null : ((InternetAddress) from).getAddress();
                    if (emailAddress != null && (emailAddress.contains("appian") || emailAddress.contains("incessant"))) {

                        boolean isMessageReceivedToday = isMessageReceivedToday(subject, subjectHeading, sentDate);
                        //isMessageReceivedToday = true;
                        if ((isMessageReceivedToday && subject.contains(subjectHeading)) ) {

                            //If recent
                            boolean isRecent = receivedInLast(min, sentDate);
                            //isRecent = true;
                            if (isRecent && subject.toLowerCase().contains("manufacturer registration")) {
                                log.warn("---------------------------------");
                                log.warn("Recent email received : " + subject);
                                log.warn("---------------------------------");
                                String body = getTextFromMessage(message);
                                //log.warn("Body Text : " + body);

                                if(body.contains(organisationName)){
                                    log.info("Message RECEIVED");
                                    bodyText = body;
                                    break;
                                }
                            } else if (isRecent && subject.toLowerCase().contains("new account request")) {
                                log.warn("---------------------------------");
                                log.warn("Recent email received : " + subject);
                                log.warn("---------------------------------");
                                String body = getTextFromMessage(message);
                                //log.warn("Body Text : " + body);

                                if(body.contains(organisationName)){
                                    log.info("Message RECEIVED");
                                    bodyText = body;
                                    break;
                                }
                            }
                        } else {
                            //log.warn("Message is old or not relevant" );
                        }
                    }
                }

                if(i > numberOfMessgesToCheck || bodyText!=null){
                    //Most likely no emails received yet
                    break;
                }
            }

            //log.warn("Waiting for message to arrive ..... ");
            //log.warn("Total Messages : " + messageCount);
            inbox.close(true);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bodyText;

    }

    private static Properties getEmailServerConfiguration() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        return props;
    }

    /**
     * We are only interested in todays email
     *
     * @param subject
     * @param subjectHeading
     * @param sentDate       @return
     */
    private static boolean isMessageReceivedToday(String subject, String subjectHeading, Date sentDate) {

        if (subjectHeading == null || subjectHeading.equals("") || subjectHeading.contains("Annual Notification Invoices")) {
            return true;
        } else {
            String date = sentDate.toString();

            //Verify it was received today
            Calendar calendar = Calendar.getInstance();
            int dom = calendar.get(Calendar.DAY_OF_MONTH);
            String monthStr = new SimpleDateFormat("MMM").format(sentDate);
            String dayStr = new SimpleDateFormat("EEE").format(sentDate);
            String dayNumberStr = String.valueOf(dom);
            if(dom < 10){
                dayNumberStr = "0" + dayNumberStr;
            }

            if (date.contains(dayNumberStr) && date.contains(dayStr) && date.contains(monthStr))
                return true;
            else
                return false;
        }
    }

    /**
     * Check if email was received in the last X minuit
     *
     * @param i received in last few minuites
     * @param receivedDate
     * @return
     */
    private static boolean receivedInLast(double i, Date receivedDate) {
        long receivedTime = receivedDate.getTime();
        long currentTime = new Date().getTime();
        double time = i * 60 * 1000;

        if (receivedTime > (currentTime - time))
            return true;
        else
            return false;
    }


//
//    /*
//    * This method checks for content-type
//    * based on which, it processes and
//    * fetches the content of the message
//    */
//
//    public static void writePart(Part p) throws Exception {
//        //if (p instanceof Message)
//        //Call methos writeEnvelope
//        writeEnvelope((Message) p);
//
//        if (p.isMimeType("multipart/*")) {
//            log.warn("----------------------------");
//            log.warn("CONTENT-TYPE: " + p.getContentType());
//            log.warn("This is a Multipart");
//            log.warn("---------------------------");
//            Multipart mp = (Multipart) p.getContent();
//            int count = mp.getCount();
//            for (int i = 0; i < count; i++)
//                writePart(mp.getBodyPart(i));
//
//        } else {
//
//            Object o = p.getContent();
//            if (o instanceof InputStream) {
//                log.warn("This is just an input stream");
//                log.warn("---------------------------");
//                InputStream is = (InputStream) o;
//
//                BufferedReader br = null;
//                StringBuilder sb = new StringBuilder();
//                String line;
//                try {
//                    int lineNumber = 0;
//                    br = new BufferedReader(new InputStreamReader(is));
//                    while ((line = br.readLine()) != null) {
//                        //Ignore first line
//                        if (lineNumber > 0) {
//                            sb.append(line + "\n");
//                            //Invoice invoice = new Invoice(line);
//                            //if (!listOfInvoices.contains(invoice))
//                            //listOfInvoices.add(invoice);
//                        }
//                        lineNumber++;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (br != null) {
//                        try {
//                            br.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                log.warn(sb.toString());
//            }
//        }
//    }
//
//
//    public static void writePartPDF(Part p) throws Exception {
//
//
//        if (p.isMimeType("multipart/*")) {
//
//            refusalForEmailContent = new StringBuilder();
//            log.warn("----------------------------");
//            log.warn("CONTENT-TYPE: " + p.getContentType());
//            log.warn("This is a Multipart");
//            log.warn("---------------------------");
//            Multipart mp = (Multipart) p.getContent();
//            int count = mp.getCount();
//            for (int i = 0; i < count; i++) {
//                try {
//                    BodyPart bodyPart = mp.getBodyPart(i);
//                    InputStream is = bodyPart.getInputStream();
//                    PDDocument document = null;
//                    document = PDDocument.load(is);
//                    document.getClass();
//                    if (!document.isEncrypted()) {
//                        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
//                        stripper.setSortByPosition(true);
//                        PDFTextStripper Tstripper = new PDFTextStripper();
//                        String st = Tstripper.getText(document);
//                        //log.warn("Text:" + st);
//                        refusalForEmailContent.append(st);
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//        } else {
//
//            Object o = p.getContent();
//            if (o instanceof InputStream) {
//                log.warn("This is just an input stream");
//                log.warn("---------------------------");
//                InputStream is = (InputStream) o;
//
//                BufferedReader br = null;
//                StringBuilder sb = new StringBuilder();
//                String line;
//                try {
//                    int lineNumber = 0;
//                    br = new BufferedReader(new InputStreamReader(is));
//                    while ((line = br.readLine()) != null) {
//                        //Ignore first line
//                        //if (lineNumber > 0) {
//                        sb.append(line + "\n");
//                        //Invoice invoice = new Invoice(line);
//                        //if (!listOfInvoices.contains(invoice))
//                        //listOfInvoices.add(invoice);
//                        //}
//                        lineNumber++;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (br != null) {
//                        try {
//                            br.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                log.warn(sb.toString());
//            }
//        }
//    }
//
//    /*
//
//    * This method would print FROM,TO and SUBJECT of the message
//
//    */
//
//    public static void writeEnvelope(Message m) throws Exception {
//        log.warn("This is the message envelope");
//        log.warn("---------------------------");
//        Address[] a;
//        // FROM
//        if ((a = m.getFrom()) != null) {
//            for (int j = 0; j < a.length; j++)
//                log.warn("FROM: " + a[j].toString());
//        }
//
//        // TO
//        if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
//            for (int j = 0; j < a.length; j++)
//                log.warn("TO: " + a[j].toString());
//        }
//
//        // SUBJECT
//        if (m.getSubject() != null)
//            log.warn("SUBJECT: " + m.getSubject());
//
//
//        //Content
//
//        log.warn("Text: " + m.getContent().toString());
//        String message = getTextFromMessage(m);
//        log.warn(message);
//    }


    private static String getTextFromMessage(Message message) throws Exception {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private static String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws Exception{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }
}