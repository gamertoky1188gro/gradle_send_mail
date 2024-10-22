package com.send.mail.server.gamertoky1188;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.mail.MessagingException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") 
public class MyController {

    // POST: Log incoming JSON data to the console and send an email
    @PostMapping("/data")
    public Response createData(@RequestBody DataRequest request) {
        // Log the incoming JSON data to the console
        System.out.println("Received POST request: ID = " + request.getId() + ", Value = " + request.getValue());

        // Prepare email parameters based on the incoming request
        String fromEmail = request.getFromEmail();
        String toEmail = request.getToEmail();
        String attachmentPath = request.getAttachmentPath();
        String messageSubject = request.getMessageSubject();
        String bodyText = request.getBodyText();
        String htmlBody = request.getHtmlBody();
        String msgType = request.getMsgType();

        // Null checks
        if (fromEmail == null || toEmail == null || messageSubject == null || bodyText == null) {
            return new Response("failure", "Invalid input: 'fromEmail', 'toEmail', 'messageSubject', and 'bodyText' are required.");
        }

        try {
            // Call the method to send the email
            SendEmailWithAttachment.sendEmail(fromEmail, toEmail, attachmentPath, messageSubject, bodyText, htmlBody, msgType);
            return new Response("success", "Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
            return new Response("failure", "Failed to send email due to messaging error.");
        } catch (IOException e) {
            e.printStackTrace();
            return new Response("failure", "Failed to send email due to I/O error.");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return new Response("failure", "Failed to send email due to security error.");
        }
    }

    // Response class for JSON response structure
    static class Response {
        private String status;
        private String message;

        public Response(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

    // DataRequest class to map incoming JSON
    static class DataRequest {
        private Integer id;
        private String value;
        private String fromEmail;
        private String toEmail;
        private String attachmentPath;
        private String messageSubject;
        private String bodyText;
        private String htmlBody;
        private String msgType;

        // Getters and Setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getFromEmail() {
            return fromEmail;
        }

        public void setFromEmail(String fromEmail) {
            this.fromEmail = fromEmail;
        }

        public String getToEmail() {
            return toEmail;
        }

        public void setToEmail(String toEmail) {
            this.toEmail = toEmail;
        }

        public String getAttachmentPath() {
            return attachmentPath;
        }

        public void setAttachmentPath(String attachmentPath) {
            this.attachmentPath = attachmentPath;
        }

        public String getMessageSubject() {
            return messageSubject;
        }

        public void setMessageSubject(String messageSubject) {
            this.messageSubject = messageSubject;
        }

        public String getBodyText() {
            return bodyText;
        }

        public void setBodyText(String bodyText) {
            this.bodyText = bodyText;
        }

        public String getHtmlBody() {
            return htmlBody;
        }

        public void setHtmlBody(String htmlBody) {
            this.htmlBody = htmlBody;
        }

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }
    }
}
