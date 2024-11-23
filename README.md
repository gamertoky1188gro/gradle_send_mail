# Send Mail Project

This repository hosts the **Send Mail Project**, which sets up a server for sending emails via a REST API. The project includes an OAuth 2.0 authentication system and several API endpoints to manage email requests.

👉 **Project Link:** [Send Mail Project](https://github.com/gamertoky1188gro/gradle_send_mail)

---

## How It Works

1. **Authentication**  
   - When you run the project, it starts a server and provides a URL for authentication.  
   - You need to authenticate using one of the test emails configured in the Google Cloud Console (GCC) OAuth 2.0 credentials.  

   **Note:**  
   If you'd like to test this project, please provide me with your email so I can add it to the list of test emails.  

   **Why Only Test Emails?**  
   Moving this project to production to support all users is technically possible, but it involves significant challenges. If you're curious about these challenges, feel free to contact me.

2. **Post-Authentication**  
   - After completing the authentication process, the server shuts down automatically, or you can stop it manually.  
   - Restart the server to access the API endpoints for sending email requests.

---

## API Endpoints

### **Endpoint**: `POST /api/data`  
Logs incoming JSON data and sends an email with the specified details.  

#### Request Body Parameters
| Parameter         | Type     | Description                                                                 |
|--------------------|----------|-----------------------------------------------------------------------------|
| `id`              | Integer  | A unique identifier for the request.                                        |
| `value`           | String   | A custom value associated with the request.                                 |
| `fromEmail`       | String   | Sender's email address (required).                                          |
| `toEmail`         | String   | Recipient's email address (required).                                       |
| `attachmentPath`  | String   | File path for an optional attachment.                                       |
| `messageSubject`  | String   | Subject of the email (required).                                            |
| `bodyText`        | String   | Text content of the email body (required).                                  |
| `htmlBody`        | String   | HTML content of the email body (optional).                                  |
| `msgType`         | String   | Type of message (`plain`, `html`, etc.)—determines formatting.              |

#### Response Structure
| Field     | Type     | Description                                  |
|-----------|----------|----------------------------------------------|
| `status`  | String   | Status of the request (`success`, `failure`). |
| `message` | String   | Additional information about the request.    |

---

## Features

- **OAuth 2.0 Authentication**: Secured access with test email accounts.
- **Email Sending**: Supports attachments, plain text, and HTML content.
- **Cross-Origin Resource Sharing (CORS)**: Enabled for all origins (`*`).
- **Error Handling**: Comprehensive error messages for messaging, I/O, or security issues.

---

## Known Issues & Feedback

- **Testing Limitation**: Only test email accounts can authenticate. Moving to production is complex; contact me for details.  
- **Manual Restart**: The server needs to be restarted after each authentication.  

If you encounter any problems or have suggestions, please raise an issue or contact me. I’ll address them as soon as possible.

---

Thank you for checking out this project! 😊
