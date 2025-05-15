##📋 Task Manager Android App Using Kotlin
This project is part of a Task Manager application, built with a Kotlin-based Android frontend and a Node.js + Express + TypeScript backend.
The backend provides a RESTful API to manage tasks — allowing users to create, read, update, and delete (CRUD) their tasks. The data is persisted using a PostgreSQL database running in Docker.

## 📦 Setup & Installation

### 🗄️ Prepare the Database

1. **Create the `.env` file**
   Copy the `.env.example` file and rename it to `.env`. Adjust the values as needed.

   ```bash
   cp .env.example .env
   ```

2. **Build and start Docker containers**

   From the project root:

   ```bash
   cd my-api
   docker-compose up --build
   ```

3. **Run the database migration**

   Once Docker is running:

   ```bash
   npm run migration
   ```

---

## 🚀 How to Run the Backend

1. Navigate to the project folder:

   ```bash
   cd my-api
   ```

2. Install dependencies:

   ```bash
   npm install --save-dev ts-node-dev typescript @types/node @types/express
   ```

3. Start the development server:

   ```bash
   npm run dev
   ```

---


## 🛠 Scripts

* `npm run dev`: Start the backend in development mode.
* `npm run migration`: Run database migrations.

---
