---

# 📋 Task Manager Android App Using Kotlin

This project is part of a **Task Manager application**, built with a **Kotlin-based Android frontend** and a **Node.js + Express + TypeScript backend**.
The backend provides a RESTful API to manage tasks — allowing users to **create**, **read**, **update**, and **delete** (CRUD) their tasks. The data is persisted using a PostgreSQL database running in Docker.

---

## 🖥️ Backend Overview

The backend is structured with:

* **Node.js** for server runtime
* **Express** for the API framework
* **TypeScript** for type-safe JavaScript
* **Docker + docker-compose** for containerized development
* **PostgreSQL** for the database
* **TypeORM** (if you're using it) or other migration tools for DB schema

---

## 📦 Setup & Installation

### 🗄️ Prepare the Database

1. **Create the `.env` file**
   Copy the `.env.example` file and rename it to `.env`. Adjust the environment variables as needed.

   ```bash
   cp .env.example .env
   ```

2. **Build and start Docker containers**

   From the root project directory:

   ```bash
   cd my-api
   docker-compose up --build
   ```

3. **Run the database migration**

   After the containers are up:

   ```bash
   npm run migration
   ```

---

## 🚀 How to Run the Backend

1. Navigate to the backend directory:

   ```bash
   cd my-api
   ```

2. Install development dependencies:

   ```bash
   npm install --save-dev ts-node-dev typescript @types/node @types/express
   ```

3. Start the backend in development mode:

   ```bash
   npm run dev
   ```

---

## 🔧 Scripts

* `npm run dev`: Start the backend server with live reloading.
* `npm run migration`: Run the database migrations.

---
