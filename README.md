## 💊 Prescription App (Interview Assessment)

A simple **web-based Spring Boot application** for generating, viewing,
and managing prescriptions. This project is part of an **interview
assessment**, showcasing backend development, MVC architecture, and REST
API consumption.

## 🧠 Tech Stack

**Backend Framework:** Spring Boot\
**Frontend Template:** Thymeleaf\
**Build Tool:** Maven\
**Database:** H2 (In-memory)\
**Language:** Java\
**API Integration:** RxNav REST API (for drug interaction checks)

## 🧩 Database Schema

The following ER diagram illustrates the relationship between the `users` and `prescriptions` tables:

![Database Schema](/assets/cmed-7-diagram.png)

## 📁 Project Structure

    PrescriptionManagement/
    │
    ├── src/main/java/com/example/prescription/
    │   ├── controller/        # Web controllers (PrescriptionController, etc.)
    │   ├── model/             # Entity classes (Prescription.java)
    │   ├── repository/        # Data access layer
    │   ├── service/           # Business logic layer
    │   ├── utils/             # Utility classes (e.g., DateFormatter, ApiResponseHandler)
    │   └── PrescriptionManagementApplication.java
    │
    ├── src/main/resources/
    │   ├── static/            # Static assets (CSS, JS, images)
    │   ├── templates/         # Thymeleaf HTML pages
    │   └── application.properties
    │
    └── pom.xml                # Maven dependencies

------------------------------------------------------------------------

## 🌐 REST API Used

The application integrates with **RxNav (NIH)** for drug interaction
data.

**Example endpoint:**

    https://rxnav.nlm.nih.gov/REST/interaction/interaction.json?rxcui=341248

⚠️ Note: The originally provided RXCUI (`34124`) no longer exists, so
`341248` is used instead for testing.

------------------------------------------------------------------------

## 🚀 Features Implemented

-   Secure login (no anonymous users)\
-   Prescription creation, listing, and deletion\
-   Integration with external drug interaction API\
-   Interactive frontend using Thymeleaf

------------------------------------------------------------------------
## 🛠️ Setup Instructions

1.  **Clone the repository**

    ``` bash
    git clone https://github.com/yourusername/prescription-system.git
    ```

2.  **Open in IDE** Open the folder in **IntelliJ IDEA** or **Spring
    Tool Suite**.

3.  **Run the application**

    ``` bash
    mvn spring-boot:run
    ```

4.  **Access the app** Open your browser and visit:

        http://localhost:8080

------------------------------------------------------------------------

## 📌 Note
This project is currently under development as part of an interview assessment, focusing on clean code structure, best practices, and maintainable architecture.

## 👩‍💻 Author

**Tabassum**\
Backend Developer \| CSE Graduate\
Passionate about Spring Boot, API Integration, and Full Stack
Development.
