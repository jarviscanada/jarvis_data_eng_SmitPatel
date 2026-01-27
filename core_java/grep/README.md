# JavaGrep

## Overview
**JavaGrep** is a lightweight, cross-platform command-line tool written in Java. It recursively traverses a directory tree, reads lines from each file, filters them using a user-provided regular expression, and writes the matching lines to an output file.

The project uses **Java 8+ features** like streams and lambdas, **JUnit 5** for testing, and **SLF4J with Log4j** for logging. Maven is used as the build tool, and the project can be packaged as an executable JAR or run via Docker.

---

## Features
- Recursively search files in a directory
- Supports Java regular expressions
- Writes matching lines to an output file
- Unit-tested with JUnit 5
- Command-line interface for easy usage
- Logging with SLF4J + Log4j

---

## Project Structure
```
grep/
├── data/                 # Sample input files
├── src/
│   ├── main/java/ca/jrvs/apps/grep/JavaGrep.java          # Interface
│   └── main/java/ca/jrvs/apps/grep/JavaGrepImp.java       # Implementation
│   └── test/java/ca/jrvs/apps/grep/JavaGrepImpTest.java   # JUnit tests
├── pom.xml               # Maven build configuration
└── README.md
```

---

## Quick Start

### 1. Build with Maven
```bash
mvn clean package
```
This will produce a **shaded (fat) JAR** in the `target/` directory:

```
target/JavaGrep-1.0.jar
```

---

### 2. Run from Command Line
```bash
java -jar target/JavaGrep-1.0.jar "<regex>" <root-directory> <output-file>
```

**Example:**
```bash
java -jar target/JavaGrep-1.0.jar ".*Romeo.*Juliet.*" ./data ./output.txt
```

---

### 3. Run with Docker
```bash
docker run --rm \
  -v /path/to/data:/data \
  -v /path/to/output:/output \
  your-docker-image-name ".*Romeo.*Juliet.*" /data /output/output.txt
```

---

## Implementation Details
- **File Traversal:** Recursive using `java.io.File` API  
- **File Reading:** `BufferedReader` wrapped around `FileReader`  
- **Regex Matching:** `Pattern` and `Matcher`  
- **File Writing:** `BufferedWriter` with `FileWriter`  
- **Logging:** SLF4J with Log4j backend  
- **Testing:** JUnit 5 with `@TempDir` for temporary file handling  

---

### Pseudocode
```
matchedLines = []
for file in listFilesRecursively(rootDir)
    for line in readLines(file)
        if containsPattern(line)
            matchedLines.add(line)
writeToFile(matchedLines)
```

---

## Testing
- Unit tests cover:
  - Listing files
  - Reading file lines
  - Regex matching
  - Writing output
  - End-to-end `process()` method

- Example: `JavaGrepImpTest` creates temporary directories and files, runs `process()`, and verifies output.

---

## Limitations & Improvements
1. **Large File Handling:** Current implementation reads all lines into memory. Can be improved using streaming without collecting lists.  
2. **Regex Documentation:** Add examples for common patterns.  
3. **Configurable Output:** Add JSON/CSV output for integration with other tools.  
4. **Performance Testing:** Add tests for large file trees and track I/O performance.

---

## Dependencies
- Java 8+
- Maven
- SLF4J 1.7.28+ (with Log4j)
- JUnit 5

---

## Author
Smit Patel

