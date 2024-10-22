package com.send.mail.server.gamertoky1188;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Properties;

public class SendEmailWithAttachment {

    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    // Change the path below to the location where you saved your credentials file
    private static final String CREDENTIALS_FILE_PATH = "G:\\gcc o2.0 nsa\\credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new FileReader(CREDENTIALS_FILE_PATH));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, Collections.singleton(GmailScopes.GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Sends an email from the user's mailbox to its recipient using the Gmail API.
     *
     * @param fromEmailAddress Email address to appear in the from: header.
     * @param toEmailAddress   Email address of the recipient.
     * @param attachmentPath    Path to the attachment file.
     * @throws MessagingException if a wrongly formatted address is encountered.
     * @throws IOException if service account credentials file not found.
     */
    public static Message sendEmail(String fromEmailAddress, String toEmailAddress, String attachmentPath, String MessageSubject, String BodyText, String HtmlBody, String msgType)
            throws MessagingException, IOException, GeneralSecurityException {
        // Authorize and create the Gmail API client
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credentials = getCredentials(HTTP_TRANSPORT);
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();

		// Set the read timeout (e.g., 5 minutes)
		HTTP_TRANSPORT.createRequestFactory((request) -> request.setReadTimeout(5 * 60 * 1000));



        // Create the email content
        String messageSubject = MessageSubject;
        String bodyText = BodyText;
        String htmlBody = HtmlBody;

        // Create a MimeMessage using the javax.mail API
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(fromEmailAddress));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(toEmailAddress));
        email.setSubject(messageSubject);
        // Set the email type category correctly
        email.addHeader("X-Gmail-Labels", "CATEGORY-" + msgType.toUpperCase());

        MimeMultipart multipart = new MimeMultipart();

        // Create MimeBodyPart for the text
        MimeBodyPart textPart = null;
        if (!(bodyText == "")) {
            textPart = new MimeBodyPart();
            textPart.setText(bodyText);
            multipart.addBodyPart(textPart);
        }
        MimeBodyPart htmlPart = null;

        if (htmlBody != null && !htmlBody.isEmpty()) {
			// Create MimeBodyPart for the HTML
			htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlBody, "text/html");
			multipart.addBodyPart(htmlPart);
		}


        // Create MimeBodyPart for the attachment
        MimeBodyPart attachmentPart = new MimeBodyPart();
        if (!(attachmentPath == "")) {
            multipart.addBodyPart(attachmentPart);
            attachmentPart.attachFile(new File(attachmentPath));
        }

        // Combine parts into a Multipart


        email.setContent(multipart);

        // Encode and wrap the MIME message into a Gmail message
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);

        Message message = new Message();
        message.setRaw(encodedEmail);

        // Send the email
        message = service.users().messages().send("me", message).execute();

        System.out.println("Message ID: " + message.getId());
        System.out.println(message.toPrettyString());
        return message;
    }
}
