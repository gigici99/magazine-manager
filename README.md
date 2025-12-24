
# 📚 Magazine Manager

**Magazine Manager** is an open-source project designed to provide companies worldwide with a **free solution for managing digital magazines**. The goal is to create a modern, scalable, and user-friendly platform.

---

## 🚀 Project Goals
- Deliver a **free magazine management system** for businesses of all sizes.
- Build a **scalable and modular architecture**, ready for microservices integration.
- Provide an **intuitive and responsive interface** for an excellent user experience.

---

## 🛠️ Tech Stack
The project leverages the following technologies:

### **Backend**
- **Java** with **Spring Boot** for a robust and secure REST API.
- Potential evolution toward **microservices architecture** for scalability and maintainability.

### **Frontend**
- **Vue.js** or **Angular** for a modern and reactive user interface.
- Communication with the backend via **REST APIs**.

---

## 📐 Architecture
- **Initial Monolithic Design** using Spring Boot.
- Future evolution toward **microservices** if required.
- **Database**: To be defined (e.g., PostgreSQL, MySQL).
- **Authentication**: JWT or OAuth2.

---

## ✅ Core Features (Roadmap)
- Create and manage digital magazines.
- User and role management.
- Upload and display multimedia content.
- Dashboard for analytics and reporting.

---

## 📦 Installation & Setup

### **Prerequisites**
- Java 17+
- Node.js 18+
- Maven
- (Optional) Docker for containerization


### **Backend Setup**
```bash
# Clone the repository
git clone https://github.com/<your-username>/magazine-manager.git

# Navigate to backend folder
cd magazine-manager/backend

# Build and run the Spring Boot application
mvn clean install
mvn spring-boot:run


# Navigate to frontend folder
cd magazine-manager/frontend

# Install dependencies
npm install

# Start development server
npm run serve

