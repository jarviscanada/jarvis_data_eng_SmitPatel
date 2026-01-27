# JavaGrep

## Introduction
JavaGrep is a lightweight, cross-platform command-line tool inspired by the Linux `grep` command. It allows users to search for lines matching a given regular expression in one or multiple files or directories and write the matching lines to an output file. The project leverages **Core Java**, **Lambda and Stream APIs**, **SLF4J** for logging, and **JUnit 5** for testing. Maven is used for project management, IntelliJ IDEA is the development IDE, and Docker is used for containerized deployment.

---

## Quick Start

### Building the App
1. Clone the project and navigate to the project directory:
```bash
cd core_java/grep
```
2. Choose the implementation class in `pom.xml`:
```xml
<mainClass>ca.jrvs.apps.grep.{JavaGrepImp or JavaGrepLambdaImp}</mainClass>
```
3. Build the project with Maven:
```bash
mvn clean package
```

### Running the App
#### Using the Jar
```bash
java -jar target/grep-1.0-SNAPSHOT.jar <regex> <rootDir> <outputFile>
```
Or directly specifying the class:
```bash
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.{JavaGrepImp or JavaGrepLambdaImp} <regex> <rootDir> <outputFile>
```

#### Using Docker
1. Build the Docker image:
```bash
docker build -t grep:local .
```
2. Run the program in the container:
```bash
docker run --rm \
  -v "$(pwd)"/data:/data \
  -v "$(pwd)"/out:/out \
  grep:local <regex> <rootDir> <outputFile>
```

---

## Implementation
### Pseudocode
```
matchedLines = []
for file in listFilesRecursively(rootDir)
    for line in readLines(file)
        if containsPattern(line)
            matchedLines.add(line)
writeToFile(matchedLines)
```

### Details
- **File Traversal:** Recursive using `java.io.File`
- **File Reading:** `BufferedReader` with `FileReader`
- **Regex Matching:** `Pattern` and `Matcher`
- **File Writing:** `BufferedWriter` with `FileWriter` (can be UTF-8 encoded for consistency)
- **Logging:** SLF4J with Log4j backend
- **Streams and Lambda:** Used in the Lambda implementation to reduce memory footprint for large files

---

## Performance Considerations
The default implementation collects all lines into a list, which may cause heap memory issues for large files. The Lambda/Stream version handles data line-by-line to reduce memory usage.

---

## Testing
- JUnit 5 is used for unit testing.
- Tests cover file listing, reading, matching, writing, and the end-to-end `process()` method.
- Temporary directories and files are used for tests, which are automatically cleaned up after execution.
- `shakespeare.txt` is used as a sample input for testing.

---

## Deployment
- **Jar:** Build and distribute the shaded JAR with Maven.
- **Docker:** Containerized deployment for portability; base image is `eclipse-temurin:8-jdk-alpine`.

---

## Improvements
1. Explicitly use **UTF-8 encoding** for reading/writing to ensure consistent behavior across systems.
2. Improve **error handling** and maintain API consistency, e.g., clarify which methods throw checked exceptions.
3. Expand **test coverage** to include more scenarios and edge cases.
4. Optimize performance further for very large files using streaming and avoiding collecting all data in memory.