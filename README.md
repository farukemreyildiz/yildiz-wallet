# 💰 Wallet Service (Spring Boot & Event-Driven)

A high-performance digital wallet application implemented with Spring Boot and event-driven architecture, focusing on internal system design rather than high-level payment gateways.

### 🏗️ Project Pipeline
This project builds a complete transaction pipeline from scratch including:
* **Database connection management** (PostgreSQL)
* **Caching layer integration** (Redis)
* **Asynchronous messaging** (Kafka)
* **Event-driven transaction handling**
* **Infrastructure orchestration** (Docker Compose)
* **Logging and debugging**

---

## ✨ Features

### 🗄️ Database & Persistence
* **PostgreSQL 15 Integration:** Primary storage for wallet and user data.
* **Connection Pooling:** Optimized DB access via HikariCP.
* **Hibernate DDL-Auto:** Automatic schema management and updates.
* **Custom Dialect Support:** Fine-tuned for PostgreSQL-specific data types.

### ⚡ Caching (Redis)
* **Spring Data Redis:** High-speed balance retrieval.
* **Memory-level Data Storage:** Reducing DB load for frequent queries.
* **Automatic Cache Synchronization:** Keeping balance data consistent.

### 📨 Messaging (Apache Kafka)
* **KRaft Mode:** Modern Kafka setup without Zookeeper dependencies.
* **Custom Producers/Consumers:** Manual implementation of transaction events.
* **String Serialization:** Explicit handling of key and value message types.
* **Asynchronous Processing:** Non-blocking money transfer operations.

### 🐳 Infrastructure & Deployment
* **Dockerized Services:** One-click startup for DB, Redis, and Kafka.
* **Port Isolation:** Configured on **5433** to avoid local service conflicts.
* **Persistent Volumes:** Data safety across container restarts.

---

## 🛠️ Technologies
* **Java 21** (Modern language features)
* **Spring Boot 4.0.3** (Microservices framework)
* **PostgreSQL** (SQL persistence)
* **Redis** (NoSQL caching)
* **Apache Kafka** (Event streaming)
* **Docker & Docker Compose** (Containerization)

---
