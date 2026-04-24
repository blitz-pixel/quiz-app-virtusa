# Quiz App (Virtusa)

A desktop Java Swing quiz application with two primary workflows:

- **Student flow** to take a timed multiple-choice quiz and view result analysis.
- **Admin flow** to add questions, view questions and review student quiz outcomes

The app uses JSON files for persistence and Maven for dependency/build management.

## Features

- Timed quiz with automatic submission when time ends (`30s × number of questions`).
- One-attempt enforcement per roll number.
- Automatic evaluation with:
  - total questions
  - correct/wrong count
  - percentage
  - performance analysis
- Admin login (credentials loaded from `config.properties`).
- Admin question creation UI (4 options + correct option selector).
- Admin dashboards:
  - all questions table
  - all students table with per-question correctness, score, and percentage
- JSON-backed question/user storage via Jackson.

## Tech Stack

- **Language:** Java
- **UI:** Swing
- **Build Tool:** Maven
- **Serialization:** Jackson Databind
- **Packaging:** Maven Assembly Plugin (`jar-with-dependencies`)

## Repository Structure

```text
quiz-app-virtusa/
├── src/main/java/com/example/
│   ├── Main.java
│   ├── model/          # Question, Users, QuizResult
│   ├── repository/     # JSON file loading/writing repositories
│   ├── service/        # Quiz, user, question business logic
│   └── ui/             # Swing UI panels and frame
├── src/main/resources/
│   ├── questions.json
│   ├── users.json
│   └── notes.txt
├── data/
│   └── questions.json
├── pom.xml
└── README.md
```

## Prerequisites

- **JDK 25** (project is configured with `maven.compiler.source/target/release = 25`)
- **Maven 3.9+**

## Setup
### Option 1: Run from Installer
1. Open the output-exe folder
2. Download the .rar file and extract it
3. Navigate to the extracted folder and run the .exe file

### Option 2 : Run manually

1. Clone the repository.
2. Ensure JDK 25 is active:
   ```bash
   java -version
   mvn -version
   ```
3. Create `config.properties` in:
   `src/main/resources/config.properties`
4. Add admin credentials in this format:
   ```properties
   username=your_admin_username
   password=your_admin_password
   ```

> `config.properties` is git-ignored by design.

#### Build

```bash
mvn clean package
```

Expected output artifact:

`target/QuizAppBuild-1-jar-with-dependencies.jar`

## Run (If cloned the repository else skip)

### Option 1: Run from IDE

Run:

`com.example.Main`

### Option 2: Run packaged JAR

```bash
java -jar target/QuizAppBuild-1-jar-with-dependencies.jar
```

## How to Use

### Student (Take Quiz tab)

1. Enter **Name** and **Roll Number**.
2. Click **Start Quiz**.
3. Navigate with **Previous** / **Next**.
4. Click **Submit** or wait for auto-submit on timer expiration.
5. View score summary and analysis popup.

### Admin (Admin tab)

1. Enter admin username/password.
2. Click **Login**.
3. Use one of:
   - **Add Question**
   - **View All Students**
   - **View All Questions**

## Data Files

- `src/main/resources/questions.json`  
  Quiz question bank used by the app.
- `src/main/resources/users.json`  
  Stores attempted quiz records and results.

### Question JSON shape

```json
{
  "questionText": "Question here",
  "options": ["A", "B", "C", "D"],
  "answerIndex": 2,
  "questionNumber": 1
}
```

### User JSON shape

```json
{
  "name": "Student Name",
  "rollno": 101,
  "questions": { "1": true, "2": false },
  "score": 1,
  "percentage": 50.0
}
```

## Build/CI

GitHub Actions workflow (`.github/workflows/build-mac.yml`) performs:

- Maven package build on macOS
- DMG generation via `jpackage`
- Installer artifact upload

## Known Limitations / Notes

- `MainFrame` currently initializes question/user file paths using hardcoded Windows paths.  
  For local portability, update paths in `src/main/java/com/example/ui/MainFrame.java` to your environment or resource-based loading.
- Admin login validation depends on `config.properties` being present in classpath resources.
- There are no automated unit tests in this repository currently.

## Troubleshooting

- **`No data file detected` or similar runtime exception**  
  Ensure JSON files exist and are valid in the expected location.
- **Admin login always fails**  
  Verify `src/main/resources/config.properties` exists and keys are exactly `username` and `password`.
- **Build fails due to Java version**  
  Switch to JDK 25 to match Maven compiler settings.

## AI Disclaimer

Portions of this project documentation and/or code may have been assisted by AI tools. All AI-assisted outputs should be reviewed, validated, and approved by human maintainers before production use.
