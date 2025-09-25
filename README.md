# Invoice Management Backend

This is the **backend service** for the Invoice Management application, built with **Spring Boot**.  
It provides APIs for managing invoices, clients, payments, quotes, and more.

---

## Features
- Create and manage **invoices, quotes, and clients**
- Automatic numbering of documents
- Payment tracking
- Credit notes (**avoirs**) support
- Reminder system for unpaid invoices
- API integration ready
- Secure authentication & authorization

---

## Requirements
- Java 17
- Maven 3+
- Spring Boot 3+
- Database MySQL

---

## Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/fakhreddine-jadib/Invoice-Management-Backend.git
   cd Invoice-Management-Backend

2. **Build the project**
   ```bash
   mvn clean install

3. **Run the application**
   ```bash
   mvn spring-boot:run

4. **The API will be available at**
   ```bash
   http://localhost:8080
