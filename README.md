# 👨‍💼  ATENEA_NOTIFICATIONS_BACKEND

It centralizes the sending and management of notifications from the RIDECI ecosystem,
allowing the mobility, institutional security, and administration modules to
send in-app messages and emails in a unified, reliable, and traceable manner to users.

## 👥 Developers
* Raquel Iveth Selma Alaya  
* Nestor David Lopez Castañeda  
* Juan Pablo Nieto Cortes  
* Carlos David Astudillo Castiblanco  
* Robinson Steven Nuñez Portela  


## 🏛️ Project Architecture

The **ATENEA Notifications Backend** has a decoupled hexagonal / clean architecture
that isolates the core notification logic from infrastructure and external providers:

* **🧠 Domain (Core)**: Contains the notification business rules  
  (creación, estados, plantillas, tipos de canal, idempotencia).

* **🎯 Ports (Interfaces)**: Interfaces that define what the domain can do  
  (enviar notificaciones, persistirlas, publicar eventos, consumir mensajes).

* **🔌 Adapters (Infrastructure)**: Implementations of the ports that connect the domain
  with specific technologies like databases, RabbitMQ, SMTP e integración con otros módulos.

The use of this architecture has the following benefits:

* ✅ **Separation of Concerns:** Distinct boundaries between logic and infrastructure.
* ✅ **Maintainability:** Easier to update or replace specific components (e.g., email provider).
* ✅ **Scalability:** Components can evolve independently (add new channels like push / SMS).
* ✅ **Testability:** The domain can be tested in isolation without database, RabbitMQ or SMTP.

## 📂 Clean - Hexagonal Structure

```bash
📂 ATENEA_NOTIFICATIONS_BACKEND
 ┣ 📂 src/
 ┃ ┣ 📂 main/
 ┃ ┃ ┣ 📂 java/
 ┃ ┃ ┃ ┗ 📂 edu/dosw/rideci/
 ┃ ┃ ┃   ┣ 📄 AteneaNotificationsBackEndApplication.java
 ┃ ┃ ┃   ┣ 📂 domain/
 ┃ ┃ ┃   ┃ ┣ 📂 model/            # 🧠 Domain models (Notification, Channel, Template, Recipient, etc.)
 ┃ ┃ ┃   ┃ ┣ 📂 service/          # 🧠 Domain services (notification creation, routing, policies)
 ┃ ┃ ┃   ┃ ┗ 📂 event/            # 🧠 Domain events (NotificationCreated, NotificationDelivered...)
 ┃ ┃ ┃   ┣ 📂 application/
 ┃ ┃ ┃   ┃ ┣ 📂 ports/
 ┃ ┃ ┃   ┃ ┃ ┣ 📂 input/          # 🎯 Input ports (use cases exposed to controllers / consumers)
 ┃ ┃ ┃   ┃ ┃ ┗ 📂 output/         # 🔌 Output ports (repositories, email sender, message broker)
 ┃ ┃ ┃   ┃ ┗ 📂 usecases/         # ⚙️ Use case implementations (send, list, read, retry...)
 ┃ ┃ ┃   ┣ 📂 infrastructure/
 ┃ ┃ ┃   ┃ ┗ 📂 adapters/
 ┃ ┃ ┃   ┃   ┣ 📂 input/
 ┃ ┃ ┃   ┃   ┃ ┣ 📂 controller/   # 🌐 Input adapters (REST controllers / test endpoints)
 ┃ ┃ ┃   ┃   ┃ ┗ 📂 listener/     # 📬 RabbitMQ listeners (event consumers)
 ┃ ┃ ┃   ┃   ┗ 📂 output/
 ┃ ┃ ┃   ┃     ┣ 📂 persistence/  # 🗄️ Output adapters (persistence, Mongo/Postgres repositories)
 ┃ ┃ ┃   ┃     ┗ 📂 email/        # ✉️ SMTP / email sender adapter
 ┃ ┃ ┗ 📂 resources/
 ┃ ┃   ┣ 📄 application.properties
 ┃ ┃   ┗ 📄 application-*.yml
 ┣ 📂 test/
 ┃ ┣ 📂 java/
 ┃ ┃ ┗ 📂 edu/dosw/rideci/AteneaNotificationsBackEndApplication/
 ┃ ┃   ┗ 📄 AteneaNotificationsBackEndApplicationTests.java
 ┣ 📂 docs/
 ┃ ┣ uml/
 ┃ ┃ ┣ DiagramaContexto.png
 ┃ ┃ ┣ DiagramaComponentesGeneral.png
 ┃ ┃ ┣ DiagramaComponentesEspecifico.png
 ┃ ┃ ┣ DiagramaCasosUso.png
 ┃ ┃ ┣ DiagramaClases.png
 ┃ ┃ ┗ DiagramaBasesDatos.png
 ┃ ┗ pdf/
 ┃   ┗ diagramaSecuencias.pdf
 ┣ 📄 docker-compose.yml
 ┣ 📄 Dockerfile
 ┣ 📄 pom.xml
 ┣ 📄 mvnw / mvnw.cmd
 ┗ 📄 README.md
```
---------
# 📡 API Endpoints

For detailed documentation refer to our Swagger UI  
(Running locally at: `http://localhost:8080/swagger-ui.html`).

Core notification input & output

| Method | URI                               | Description                                                 | Request Body / Params                                       |
| :----- | :-------------------------------- | :---------------------------------------------------------- | :---------------------------------------------------------- |
| `POST` | `/notifications/email`            | Enviar una nueva notificación por correo electrónico        | `EmailNotificationRequest` (JSON en el cuerpo)             |
| `POST` | `/notifications/in-app`           | Crear una notificación in-app para un usuario o grupo       | `InAppNotificationRequest` (JSON en el cuerpo)             |
| `GET`  | `/notifications/users/{userId}`   | Listar notificaciones de un usuario (in-app)                | Path Variable: `userId`, Query Params: `status`, `type`, `page`, `size` (opcionales) |
| `PATCH`| `/notifications/{id}/read`        | Marcar notificación como leída                              | Path Variable: `id`, Query Param opcional: `readAt`        |
| `PATCH`| `/notifications/{id}/archive`     | Archivar notificación                                       | Path Variable: `id`                                        |
| `GET`  | `/notifications/{id}`             | Ver detalle de una notificación                             | Path Variable: `id`                                        |
| `GET`  | `/notifications`                  | Listar notificaciones con filtros                           | Query Params: `userId`, `channel`, `status`, `from`, `to`, `page`, `size` |
| `POST` | `/notifications/templates`        | Crear plantilla de notificación                             | Request Body: `NotificationTemplateDto`                    |
| `PUT`  | `/notifications/templates/{id}`   | Actualizar plantilla                                        | Path Variable: `id`, Request Body: `NotificationTemplateDto` |
| `GET`  | `/notifications/templates`        | Listar plantillas                                           | Query Params: `type`, `channel` (opcionales)               |
| `POST` | `/notifications/test/email`       | Enviar correo de prueba para validación SMTP                | Request Body: `TestEmailRequest`                           |
| `POST` | `/notifications/test/event`       | Publicar evento de prueba en RabbitMQ                       | Request Body: `TestEventRequest`                           |
| `GET`  | `/actuator/health`                | Health check del microservicio                              | -                                                          |

### 📟 HTTP Status Codes

Common status codes returned by the API.

| Code  | Status                  | Description                                                              |
| :---- | :---------------------- | :----------------------------------------------------------------------- |
| `200` | **OK**                  | Request processed successfully.                                          |
| `201` | **Created**             | Notification resource created successfully.                              |
| `202` | **Accepted**            | Notification accepted for async processing (e.g., queued in RabbitMQ).   |
| `400` | **Bad Request**         | Invalid payload or missing parameters.                                   |
| `401` | **Unauthorized**        | Missing or invalid JWT token.                                            |
| `404` | **Not Found**           | Notification, template or user does not exist.                           |
| `409` | **Conflict**            | Duplicate or already-processed event/notification.                       |
| `500` | **Internal Server Error** | Unexpected error (SMTP / broker / DB failure).                         |

# Input and Output Data

Data information per functionality (summary)

- **EmailNotificationRequest**  
  - `to`: lista de destinatarios (email)  
  - `subject`: asunto del correo  
  - `body`: contenido del mensaje (texto plano o HTML)  
  - `templateId` (opcional): identificador de plantilla  
  - `params` (opcional): mapa de parámetros para placeholders de la plantilla  
  - `metadata` (opcional): información adicional para trazabilidad (e.g. `correlationId`, `sourceModule`)  

- **InAppNotificationRequest**  
  - `userId`: identificador del usuario destinatario  
  - `title`: título de la notificación  
  - `message`: mensaje principal a mostrar en la UI  
  - `priority` (opcional): prioridad (LOW, NORMAL, HIGH)  
  - `type` (opcional): tipo de notificación (SECURITY, TRIP, SYSTEM, etc.)  
  - `expiresAt` (opcional): fecha de expiración  
  - `metadata` (opcional): información complementaria para el frontend (links, actions, etc.)  

- **NotificationTemplateDto**  
  - `id` (opcional en creación): identificador de la plantilla  
  - `name`: nombre lógico de la plantilla  
  - `channel`: canal asociado (EMAIL / IN_APP)  
  - `language`: idioma (es-CO, en-US, etc.)  
  - `subject` (para email): asunto con placeholders  
  - `body`: cuerpo del mensaje con placeholders  
  - `enabled`: indicador de si la plantilla está activa  

- **NotificationResponse**  
  - `id`: identificador de la notificación  
  - `userId` (si aplica): usuario destinatario  
  - `channel`: canal usado (EMAIL / IN_APP)  
  - `status`: estado actual (CREATED, SENT, READ, ARCHIVED, FAILED, etc.)  
  - `createdAt`: fecha de creación  
  - `sentAt` (opcional): fecha de envío  
  - `readAt` (opcional): fecha de lectura  
  - `archivedAt` (opcional): fecha de archivado  
  - `correlationId` (opcional): id para trazabilidad cross-microservicio  

- **PageResponse\<NotificationResponse\>**  
  - `content`: lista de notificaciones  
  - `page`: número de página  
  - `size`: tamaño de página  
  - `totalElements`: total de registros  
  - `totalPages`: total de páginas  

---

# 🔗 Connections with other Microservices

This module does not work alone. It interacts with the RideCi Ecosystem via REST APIs and Message Brokers:

1. **Travel Management / Nemesis Module**  
   - Sends trip-related events (created, started, finished, canceled).  
   - The notifications module generates email and in-app messages for passengers and drivers:
     - Trip creation confirmation.
     - Trip start reminders.
     - Trip completion notifications and optional surveys.

2. **Administration Module (ATENEA_ADMINISTRATION_BACKEND)**  
   - Publishes events when:
     - Drivers are approved or rejected.
     - Users are suspended, blocked, or reactivated.
     - Security reports are registered.  
   - The notifications module sends messages to users and administrators to inform decisions and statuses.

3. **Auth / Users Module**  
   - Handles:
     - Password recovery (password reset).
     - Email verification.
     - Suspicious login alerts or credential change notifications.  
   - The notifications module sends emails with temporary codes or signed links.

4. **Email Provider / SMTP (Office 365 or others)**  
   - External channel for delivering transactional emails.  
   - Configured through `spring.mail.*` properties.  
   - Supports TLS/STARTTLS and authenticated delivery.

5. **API Gateway / Edge Service**  
   - Exposure layer for the public notification endpoints.  
   - Responsible for authentication, authorization, and rate limiting before routing requests to the backend.

6. **Monitoring / Logging Stack** (optional)  
   - Integrated with observability tools (e.g., centralized logs, metrics, dashboards) to:
     - Track notification volume.
     - Measure delivery time.
     - Monitor per-channel error rates.

Asynchronous communication is handled primarily via **RabbitMQ**, decoupling business event generation from final notification delivery and enabling retries and load balancing across consumers.

---

# Technologies

The following technologies were used to build and deploy this module:

### Backend & Core

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

---

### Messaging & Email
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![Microsoft 365](https://img.shields.io/badge/Office365-EB3C00?style=for-the-badge&logo=microsoftoutlook&logoColor=white)

---

### DevOps & Infrastructure
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/kubernetes-%23326ce5.svg?style=for-the-badge&logo=kubernetes&logoColor=white)
![Railway](https://img.shields.io/badge/Railway-131415?style=for-the-badge&logo=railway&logoColor=white)
![Vercel](https://img.shields.io/badge/vercel-%23000000.svg?style=for-the-badge&logo=vercel&logoColor=white)

---

### CI/CD & Quality Assurance
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)
![SonarQube](https://img.shields.io/badge/SonarQube-4E9BCD?style=for-the-badge&logo=sonarqube&logoColor=white)
![JaCoCo](https://img.shields.io/badge/JaCoCo-Coverage-green?style=for-the-badge)

---

### Documentation & Testing
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)

---

### Design
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)

---

### Comunication & Project Management
![Jira](https://img.shields.io/badge/jira-%230A0FFF.svg?style=for-the-badge&logo=jira&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white) 

# 🌿 Branches Strategy & Structure

This module follows a strict branching strategy based on Gitflow to ensure ordered versioning, code quality and continuous integration.

| **Branch**                | **Purpose**                                      | **Receive of**           | **Sent to**        | **Notes**                                      |
| ------------------------- | ----------------------------------------------- | ------------------------ | ------------------ | ---------------------------------------------- |
| `main`                    | 🏁 Stable code for preproduction or production  | `release/*`, `hotfix/*`  | 🚀 Production      | 🔐 Protected with PR and successful CI         |
| `develop`                 | 🧪 Main developing branch                       | `feature/*`              | `release/*`        | 🔄 Base for continuous integration/deployment  |
| `feature/*`               | ✨ New features or refactors                    | `develop`                | `develop`          | 🧹 Deleted after merge into `develop`          |
| `release/*`               | 📦 Release preparation & final polish           | `develop`                | `main`, `develop`  | 🧪 Final QA. No new features here.             |
| `bugfix/*` or `hotfix/*`  | 🛠️ Critical fixes for production               | `main`                   | `main`, `develop`  | ⚡ Urgent patches. Highest priority            |

---

# 🏷️ Naming Conventions

## 🌿 Branch Naming

### ✨ Feature Branches

Used for new features or non-critical improvements.

**Format:**

`feature/[shortDescription]`

**Examples:**

- `feature/email-notifications`
- `feature/in-app-notifications`
- `feature/rabbitmq-integration`
- `feature/password-recovery-flow`

**Rules:**
* 🧩 **Case:** strictly *camelCase* (lowercase with hyphens).
* ✍️ **Descriptive:** Short and meaningful description.
---
### 📦 Release Branches
Used for preparing a new production release. Follows [Semantic Versioning](https://semver.org/).

**Format:**
`release/v[major].[minor].[patch]`

**Examples:**
- `release/v1.0.0`
- `release/v1.1.0-beta`
---

### 🚑 Hotfix Branches
Used for urgent fixes in the production environment.

**Format:**
`hotfix/[shortDescription]`

**Examples:**
- `hotfix/fixTokenExpiration`
- `hotfix/securityPatch`

---

## 📝 Commit Message Guidelines

We follow the **[Conventional Commits](https://www.conventionalcommits.org/)** specification.

### 🧱 Standard Format

```text
<type>(<scope>): <short description>
```

# 📐 System Architecture & Design

This section provides a visual representation of the module's architecture ilustrating the base diagrams to show the application structure and components flow.

### 🧩 Context Diagram
----
### 🧩 Specific Components Diagram
---
### 🧩 Use Cases Diagram
---
#### 🧩 Class Diagram
---
### 🧩 Sequence Diagrams
---
### 🧩 Specific Deploy Diagram
---
### 🧩 General Component Diagram

---

# 🚀 Getting Started

### Requesitos
- Java 17
- Maven 3.X
- Docker + Docker Compose
- Puerto disponiblo 8080

### Clone & open repository

`git clone https://github.com/RIDECI/ATENEA_NOTIFICATIONS_BACKEND.git`

`cd ATENEA_NOTIFICATIONS_BACKEND`

### Dockerize the project

Dockerize before compile the project avoid configuration issues and ensure environment consistency.

``` bash
docker compose up -d
```


### Install dependencies & compile project

Download dependencies and compile the source code.

``` bash
mvn clean install
```

``` bash
mvn clean compile
```

### To run the project
Start the Spring Boot server

``` bash
mvn spring-boot:run
```

---
RIDECI — Connecting the community to move safely, affordably, and sustainably.
