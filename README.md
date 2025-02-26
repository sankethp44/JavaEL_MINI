# 🧩 Java Word Search Game

A simple **Java Swing** application where users search for words in a grid. The game randomly places words from a predefined list in various directions, and players select letters to match words.

---

## Features
* Randomly generates a new word search puzzle each time  
* Highlights selected letters and validates words  
* Tracks score based on words found  
* Simple and interactive **GUI** using Java Swing  
* Runs on any system with Java installed  

---

## Prerequisites

Before running this project, ensure you have:

- **Java JDK 17+** installed → [Download JDK](https://www.oracle.com/java/technologies/javase-downloads.html)
- **Eclipse IDE** (or IntelliJ, VS Code with Java support)
- **Git** (for cloning the repository)

---

## Getting Started

### 1. Download the Project

You can download the project files in the following way:   
`git clone https://github.com/sankethp44/JavaEL_MINI.git`   
`cd JavaEL_MINI`    
Or downlaod the ZIP from Github and extract it.


### 2. Open in Eclipse

1. Open Eclipse.
2. Go to **File** → **Import**.
3. Select **Existing Projects into Workspace**.
4. Click **Next**, then browse to the project folder.
5. Click **Finish**.

### 3. Run the Project

1. Open `WordSearch.java`.
2. Right-click on the file.
3. Select **Run As** → **Java Application**.

### 4. Run via Terminal (Alternative)

If you prefer running without an IDE, you can use the terminal with the following commands:

```bash
cd path/to/project
javac -d bin src/com/javacodeswing/WordSearch.java
java -cp bin com.javacodeswing.WordSearch
