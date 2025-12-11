# 👨‍💼 ATENEA_NOTIFICATIONS_BACKEND

It centralizes the sending and management of notifications from the
RIDECI ecosystem, allowing the mobility, institutional security, and
administration modules to send in-app messages and emails in a unified,
reliable, and traceable manner to users.

## 👥 Developers

-   Raquel Iveth Selma Alaya\
-   Nestor David Lopez Castañeda\
-   Juan Pablo Nieto Cortes\
-   Carlos David Astudillo Castiblanco\
-   Robinson Steven Nuñez Portela

------------------------------------------------------------------------

# 🏛️ Project Architecture

The ATENEA Notifications Backend has a decoupled **hexagonal / clean
architecture** that isolates the core notification logic from
infrastructure and external providers:

### 🧠 Domain (Core)

Business rules: creation, states, templates, channels, idempotency.

### 🎯 Ports (Interfaces)

Defines what the domain can do: send notifications, persist them,
publish events, consume messages.

### 🔌 Adapters (Infrastructure)

Implementation of ports: databases, RabbitMQ, SMTP, integrations.

### ✅ Benefits

-   Clear **separation of concerns**
-   **Maintainable** and replaceable components
-   **Scalable** (expand to SMS, push, WhatsApp, etc.)
-   **Testable** without infrastructure

------------------------------------------------------------------------

# 📂 Clean - Hexagonal Structure

    📂 ATENEA_NOTIFICATIONS_BACKEND
    ┣ 📂 src/
    ┃ ┣ 📂 main/
    ┃ ┃ ┣ 📂 java/
    ┃ ┃ ┃ ┗ 📂 edu/dosw/rideci/
    ┃ ┃ ┃   ┣ AteneaNotificationsBackEndApplication.java
    ┃ ┃ ┃   ┣ 📂 domain/
    ┃ ┃ ┃   ┃ ┣ 📂 model/
    ┃ ┃ ┃   ┃ ┣ 📂 service/
    ┃ ┃ ┃   ┃ ┗ 📂 event/
    ┃ ┃ ┃   ┣ 📂 application/
    ┃ ┃ ┃   ┃ ┣ 📂 ports/
    ┃ ┃ ┃   ┃ ┃ ┣ 📂 input/
    ┃ ┃ ┃   ┃ ┃ ┗ 📂 output/
    ┃ ┃ ┃   ┃ ┗ 📂 usecases/
    ┃ ┃ ┃   ┣ 📂 infrastructure/
    ┃ ┃ ┃   ┃ ┗ 📂 adapters/
    ┃ ┃ ┃   ┃   ┣ 📂 input/
    ┃ ┃ ┃   ┃   ┃ ┣ 📂 controller/
    ┃ ┃ ┃   ┃   ┃ ┗ 📂 listener/
    ┃ ┃ ┃   ┃   ┗ 📂 output/
    ┃ ┃ ┃   ┃     ┣ 📂 persistence/
    ┃ ┃ ┃   ┃     ┗ 📂 email/
    ┃ ┃ ┗ 📂 resources/
    ┃ ┃   ┣ application.properties
    ┃ ┃   ┗ application-*.yml
    ┣ 📂 test/
    ┣ 📂 docs/
    ┃ ┣ uml/
    ┃ ┗ pdf/
    ┣ docker-compose.yml
    ┣ Dockerfile
    ┣ pom.xml
    ┗ README.md

------------------------------------------------------------------------

# 📡 API Endpoints

Swagger UI: `http://localhost:8080/swagger-ui.html`

Method   URI                             Description
  -------- ------------------------------- ----------------------------
POST     /notifications/email            Send email notification
POST     /notifications/in-app           Create in-app notification
GET      /notifications/users/{userId}   List user notifications
PATCH    /notifications/{id}/read        Mark as read
PATCH    /notifications/{id}/archive     Archive notification
GET      /notifications/{id}             Notification detail
GET      /notifications                  Filter notifications
POST     /notifications/templates        Create template
PUT      /notifications/templates/{id}   Update template
GET      /notifications/templates        List templates
POST     /notifications/test/email       Test SMTP
POST     /notifications/test/event       Test RabbitMQ
GET      /actuator/health                Health check

------------------------------------------------------------------------

# 📟 HTTP Status Codes

Code   Status
  ------ -----------------------
200    OK
201    Created
202    Accepted
400    Bad Request
401    Unauthorized
404    Not Found
409    Conflict
500    Internal Server Error

------------------------------------------------------------------------

# 📑 Input & Output Data

### **EmailNotificationRequest**

-   to
-   subject
-   body
-   templateId?
-   params?
-   metadata?

### **InAppNotificationRequest**

-   userId
-   title
-   message
-   priority?
-   type?
-   expiresAt?
-   metadata?

### **NotificationTemplateDto**

-   id?
-   name
-   channel
-   language
-   subject
-   body
-   enabled

### **NotificationResponse**

-   id, userId?, channel, status, timestamps...

### **PageResponse**

-   content, page, size, totalElements, totalPages

------------------------------------------------------------------------

# 🔗 Connections with other Microservices

### **Travel Management / Nemesis Module**

Trip events → emails & in-app messages.

### **Administration Module**

Driver approvals, blocks, reports → notifications.

### **Auth / Users Module**

Password recovery, email verification.

### **Email Provider / SMTP**

Office 365 or similar.

### **API Gateway**

Authentication and routing.

### **Monitoring / Logging Stack**

Metrics, dashboards, logs.

------------------------------------------------------------------------

# 🛠️ Technologies

### Backend

-   Java\
-   Spring Boot\
-   Maven

### Messaging & Email

-   RabbitMQ\
-   Microsoft 365 SMTP

### DevOps

-   Docker\
-   Kubernetes\
-   Railway\
-   Vercel

### CI/CD

-   GitHub Actions\
-   SonarQube\
-   JaCoCo

### Documentation

-   Swagger\
-   Postman

### Design & Management

-   Figma\
-   Jira\
-   Slack

------------------------------------------------------------------------

# 🌿 Branches Strategy (Gitflow)

Branch       Purpose
  ------------ -------------------
main         Stable production
develop      Main development
feature/\*   New features
release/\*   Pre-production
hotfix/\*    Urgent fixes

### Naming

-   feature/email-notifications\
-   feature/rabbitmq-integration\
-   release/v1.0.0\
-   hotfix/securityPatch

### Commit Format

    type(scope): short description

------------------------------------------------------------------------

# 🚀 Getting Started

## Requirements

-   Java 17\
-   Maven 3.x\
-   Docker\
-   Port 8080

## Clone Repository

    git clone https://github.com/RIDECI/ATENEA_NOTIFICATIONS_BACKEND.git
    cd ATENEA_NOTIFICATIONS_BACKEND

## Dockerize

    docker compose up -d

## Install Dependencies

    mvn clean install
    mvn clean compile

## Run

    mvn spring-boot:run

------------------------------------------------------------------------

# 🌎 RIDECI --- Connecting the community safely and sustainably.