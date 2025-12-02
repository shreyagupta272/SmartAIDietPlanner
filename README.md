# 📘 Diet Plan Builder — Angular + Spring Boot + Groq AI

A full-stack application that allows fitness trainers to:

✔ Create personalized multi-day meal plans  
✔ Generate complete meal plans with AI (Groq models)  
✔ Edit meals day-by-day using a clean card-based UI  
✔ Download the final plan as a PDF  
✔ Store client details and meal plans across pages  
✔ Run entirely locally or deploy anywhere

---

## 🚀 Tech Stack

### Frontend
- **Angular 19** (Standalone Components)
- **HTML2PDF.js** for PDF generation
- Responsive card-based UI for meal planning

### Backend
- **Spring Boot 3**
- **WebFlux** (`WebClient`) for Groq API calls
- **Jackson** `ObjectMapper` for JSON → Java mapping

### AI
- **Groq API**
- OpenAI-compatible Chat Completions endpoint

---

## 🧩 Features

### 🔹 Client Setup
Trainers enter:
- Name
- Age
- Gender
- Goal
- Activity Level

### 🔹 Meal Planner Workspace
- Add unlimited days (Day 1, Day 2...)
- Each day contains:
  - Breakfast
  - Snacks
  - Lunch
  - Dinner
- Each meal has:
  - Time
  - Items (multiline text)

### 🔹 AI-Generated Meal Plan
One click → generates a full meal plan using Groq LLM.

### 🔹 PDF Download
Instantly export the entire plan as a professionally formatted PDF.

---

## 📦 Project Structure

```
diet-plan-builder/
│
├── frontend/ (Angular)
│   ├── src/app/pages/client-setup/
│   ├── src/app/pages/plan-builder/
│   ├── src/app/services/
│   └── ... 
│
└── backend/ (Spring Boot)
    ├── src/main/java/.../controller/
    ├── src/main/java/.../service/
    ├── src/main/java/.../model/
    ├── src/main/java/.../config/
    └── ...
```

---

## 🔧 Setup Instructions

### 1️⃣ Clone the Repo

```bash
git clone https://github.com/yourname/diet-plan-builder.git
cd diet-plan-builder
```

---

### 🖥️ Frontend Setup (Angular)

```bash
cd frontend
npm install
ng serve
```

Your Angular app will run at:  
👉 **http://localhost:4200**

---

### 🖥️ Backend Setup (Spring Boot)

#### Install Dependencies

```bash
cd backend
mvn clean install
```

#### Configure the Groq API Key (IMPORTANT)

⚠️ **You must not store your key in GitHub.**

Use an environment variable:

**macOS/Linux:**
```bash
export GROQ_API_KEY="groq-xxxxx"
```

**Windows:**
```cmd
setx GROQ_API_KEY "groq-xxxxx"
```

#### Backend `application.properties`:

```properties
groq.api.key=${GROQ_API_KEY}
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```

#### Start the backend:

```bash
mvn spring-boot:run
```

Backend runs at:  
👉 **http://localhost:8080**

---

## 🤖 AI Integration (Groq)

The backend calls:

```
POST https://api.groq.com/openai/v1/chat/completions
```

using the selected model (e.g., `"groq/llama3-8b"`, `"groq/llama3-70b"` depending on your account).

Prompts are structured so the model responds with:

```json
{
  "dayPlans": [
    {
      "id": 1,
      "name": "Day 1 · Monday",
      "subtitle": "Balanced veg · ~1600 kcal",
      "meals": [
        ...
      ]
    }
  ]
}
```

The Angular UI instantly updates with this AI-generated plan.

---

## 📄 PDF Export

Frontend uses **html2pdf.js** to convert the full plan layout into a downloadable A4 PDF.

Everything you see in the UI = captured in the PDF.

---

## 👨‍💻 Author

**Shreya Gupta**  
GitHub: [@shreyagypta272](https://github.com/shreyagupta272)
