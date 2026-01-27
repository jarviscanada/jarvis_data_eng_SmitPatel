package ca.jrvs.apps.grep.main.java.ca.jrvs.apps.grep;


import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class JavaGrepImp implements JavaGrep {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private String regex;
    private String rootPath;
    private String outFile;
    private Pattern pattern;

    public static void main(String[] args){
        if(args.length != 3){
            throw new IllegalArgumentException("USAGE: JavaGrepImp regex rootpath outFile");
        }

        // Logger config
        BasicConfigurator.configure();

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try{
            javaGrepImp.process();
        }
        catch (Exception e){
            javaGrepImp.logger.error("Error: Unable to process ",e );
        }
        javaGrepImp.logger.error("Error : Unable to process the file.");
    }



    @Override
    public void process() throws IOException {
        if (rootPath == null || regex == null || outFile == null) {
            throw new IllegalStateException("regex, rootPath, and outFile is null");
        }

        List<String> matchedLines = new ArrayList<>();

        logger.info("Found {} files under {}", rootPath, rootPath);

        listFiles(getRootPath())
                .stream()
                .forEach(file -> {
                    readLines(file)
                    .stream()
                    .filter(this::containsPattern)
                    .forEach(matchedLines::add);
                }

        );

        writeToFile(matchedLines);
        logger.info("Wrote {} matched lines to {}", matchedLines.size(), outFile);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        try (Stream<Path> paths = Files.walk(Paths.get(rootDir))) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            this.logger.error("Unable to list files", ex);
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> readLines(File inputFile) {
        if (inputFile == null || !inputFile.isFile()) {
            throw new IllegalArgumentException("inputFile is not a valid file: " + inputFile);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            return reader
                    .lines()
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            this.logger.error("Unable to read lines from file: " + inputFile, ex);
        }

        return Collections.emptyList();
    }

    @Override
    public boolean containsPattern(String line) {
        return pattern.matcher(line).find();
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        if(this.getOutFile() == null) {
            throw new IllegalArgumentException("OutFile does not exit");
        }

        File file = new File(this.getOutFile());

        try (BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(file)))){
            lines
            .stream()
            .forEach(line -> {
                try {
                    writer.write(line);
                    writer.newLine();
                } catch (IOException ex) {
                     this.logger.error("Unable to write the file: ", line, ex);
                    }
            });
        }
    }

    @Override
    public String getRootPath() {
        return this.rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return this.regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public String getOutFile() {
        return this.outFile;
    }

    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }
}
