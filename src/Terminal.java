import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Terminal {
    private Parser parser;
    // private List<String> commandHistory;

    public Terminal() {
        this.parser = new Parser();
    }
    public Parser getParser() {
        return parser;
    }
    public void echo(List<String> args) {
        System.out.println(String.join(" ", args));
    }
    public void pwd() {
        System.out.println("Current directory: " + System.getProperty("user.dir"));
    }

    //john
    public void cd(List<String> args) {
        if (args.size() == 0) {
            System.setProperty("user.dir", System.getProperty("user.home"));
            return;
        }

        String directoryPath = System.getProperty("user.dir");
        if (args.get(0).equals("..")) {
            File parentDirectory = new File(directoryPath).getParentFile();
            if (parentDirectory != null) {
                System.setProperty("user.dir", parentDirectory.getPath());
            } else {
                System.out.println("Already at the root directory");
            }
        } else {
            StringBuilder targetDirectory = new StringBuilder(args.get(0));
            targetDirectory = new StringBuilder(args.get(0).replaceAll("^\"|\"$", ""));
            if (args.size() > 1) {
                for (int i = 1; i < args.size(); i++) {
                    targetDirectory.append(" ").append(args.get(i).replaceAll("^\"|\"$", ""));
                }
            }
            File newDirectory = new File(directoryPath, targetDirectory.toString());
            if (!newDirectory.exists()) {
                newDirectory = new File(targetDirectory.toString());
            }
            if (newDirectory.exists() && newDirectory.isDirectory()) {
                System.setProperty("user.dir", newDirectory.getPath() + "\\");
            } else {
                System.out.println("Directory not found");
            }
        }
    }

    //abdo ls and ls -r
    public void ls(List<String> args){
        
        File currentDirectory = new File(System.getProperty("user.dir"));
        File[] files = currentDirectory.listFiles();
        if(args.size() > 0 && "-r".equals(args.get(0))){
            Arrays.sort(files, (f1, f2) -> f2.getName().compareTo(f1.getName()));
        }else{
            Arrays.sort(files);
        }
        for (File file : files) {
                System.out.println(file.getName());
        }
    }
    //john
    public void mkdir(List<String> directories) {
        for (String dir : directories) {
            String targetDirectory = dir.replaceAll("^\"|\"$", "");
            File file;
            if (targetDirectory.contains(File.separator)) {
                file = new File(targetDirectory);
            } else {
                file = new File(System.getProperty("user.dir") + File.separator + targetDirectory);
            }
            if (file.exists()) {
                System.out.println("Directory already exists: " + file.getAbsolutePath());
            } else {
                if (file.mkdirs()) {
                    System.out.println("Directory created: " + file.getAbsolutePath());
                } else {
                    System.out.println("Failed to create directory: " + file.getAbsolutePath());
                }
            }
        }
    }
    //abdo
    public void rmdir(List<String> args) {
        if (args.size() == 1) {
            if ("*".equals(args.get(0))) {
                File currentDirectory = new File(System.getProperty("user.dir"));
                File[] subDirectories = currentDirectory.listFiles(File::isDirectory);
    
                for (File subDirectory : subDirectories) {
                    if (subDirectory.listFiles().length == 0) {
                        if (subDirectory.delete()) {
                            System.out.println("Directory removed: " + subDirectory.getAbsolutePath());
                        } else {
                            System.out.println("Failed to remove directory: " + subDirectory.getAbsolutePath());
                        }
                    } else {
                        System.out.println("Directory is not empty: " + subDirectory.getAbsolutePath());
                    }
                }
            } else {
                String path = args.get(0);
                File file;
                if (new File(path).isAbsolute()) {
                    file = new File(path);
                } else {
                    file = new File(System.getProperty("user.dir") + File.separator + path);
                }
                if (file.exists() && file.isDirectory()) {
                    if (file.delete()) {
                        System.out.println("Directory removed: " + file.getAbsolutePath());
                    } else {
                        System.out.println("Failed to remove directory: " + file.getAbsolutePath());
                    }
                } else {
                    System.out.println("Directory does not exist: " + file.getAbsolutePath());
                }
            }
        } else {
            System.out.println("Too many arguments");
        }
    }
    
    //abdo
    public void touch(List<String> args) throws IOException{
        if(args.size()==1){
            String path = args.get(0);
            File file;
            if (new File(path).isAbsolute()) {
                file = new File(path);
            } else {
                file = new File(System.getProperty("user.dir") + File.separator + path);
            }
            if (file.exists()) {
                System.out.println("File already exists: " + file.getAbsolutePath());
            } else {
                
                    if (file.createNewFile()) {
                        System.out.println("File created: " + file.getAbsolutePath());
                    } else {
                        System.out.println("Failed to create file: " + file.getAbsolutePath());
                    }
                }
        }else{
            System.out.println("Incorrect number of arguments");
        }

    }

    //john cp and cp -r
    public void cp(List<String> Files) {
        if (Files.size() == 2 ) {
            File sourceFile = new File(System.getProperty("user.dir") + File.separator + Files.get(0));
            File destinationFile = new File(System.getProperty("user.dir") + File.separator + Files.get(1));

            try (Scanner scanner = new Scanner(sourceFile);
                 PrintWriter printWriter = new PrintWriter(destinationFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    printWriter.println(line);
                }
                System.out.println("File copied successfully.");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (Files.size() == 3 && Objects.equals(Files.get(0), "-r")){
            String sourceDirName = Files.get(1);
            String destinationDirName = Files.get(2);
            cpR(sourceDirName, destinationDirName);
        } else {
            System.out.println("Two many argument");
        }
    }
    public static void cpR(String sourceDirName, String destinationDirName) {
        File sourceDir = new File( System.getProperty("user.dir")+File.separator +sourceDirName);
        File destinationDir = new File( System.getProperty("user.dir")+File.separator +destinationDirName);

        if (!destinationDir.exists()) {
            destinationDir.mkdir();
        }

        File[] files = sourceDir.listFiles();
        if (files != null) {
            for (File file : files) {
                String sourcePath = sourceDir.toString();
                String destinationPath = destinationDir.toString();
                if (file.isDirectory()) {
                    cpR(sourcePath, destinationPath);
                } else {
                    try {
                        Path source = Path.of(sourcePath);
                        Path destination = Path.of(destinationPath);
                        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    //john
    public void rm(String fileName) {
        File file = new File(System.getProperty("user.dir") + File.separator + fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("File removed: " + file.getAbsolutePath());
            } else {
                System.out.println("Failed to remove file: " + file.getAbsolutePath());
            }
        } else {
            System.out.println("File does not exist or is not a regular file: " + file.getAbsolutePath());
        }
    }
    //abdo
    public void cat(List<String> args) throws FileNotFoundException {

        if (args.size() < 1) {
            System.out.println("Insufficient number of arguments");
            return;
        }

        String pathOne = args.get(0);
        File fileOne = new File(pathOne);

        if (!fileOne.exists()) {
            System.out.println("File does not exist: " + fileOne.getAbsolutePath());
            return;
        }
        

        System.out.print(readFile(fileOne));

        if (args.size() == 2) {
            String pathTwo = args.get(1);
            File fileTwo = new File(pathTwo);

            if (fileTwo.exists()) {

                System.out.print(readFile(fileTwo));
            } else {
                System.out.println("Second file does not exist: " + fileTwo.getAbsolutePath());
            }
        }
        System.out.println();

    }

    private String readFile(File file) throws FileNotFoundException {
        String output ="";
        Scanner myReader = new Scanner(file);
        while (myReader.hasNextLine()) {
            output+=myReader.nextLine();
            if(myReader.hasNextLine()){
                output+="\n";
            }
        }
        myReader.close();
        return output;
    }
    
    //abdo
    public void wc(List<String> args){
       if (args.size() != 1) {
            System.out.println("Incorrect number of arguments");
            return;
        }
        File currentDirectory = new File(System.getProperty("user.dir"));
        File filetoRead = new File(currentDirectory.getAbsolutePath() + File.separator + args.get(0));



        if (!filetoRead.exists()) {
            System.out.println("File does not exist: " + filetoRead.getAbsolutePath());
            return;
        }

        try {
            Printcount(filetoRead);
        } catch (FileNotFoundException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

    }
    private void Printcount(File file)throws FileNotFoundException{
        int lines = 0;
        int words = 0;
        int characters = 0;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lines++;
                words += line.split("\\s+").length;
                characters += line.length();
            }
        }

        System.out.println(lines + " " + words + " " + characters + " " + file.getName());
    }

    public void chooseCommandAction() throws IOException {
        String commandName = parser.getCommandName();
        List<String> args = parser.getArgs();

        switch (commandName) {
            case "pwd":
                pwd();
                break;
            case "cd":
                cd(args);
                break;
            case "echo":
                echo(args);
                break;
            case "ls":
                ls(args);
                break;
            case "mkdir":
                mkdir(args);
                break;
            case "rm":
                rm(args.get(0));
                break;
            case "cp":
                cp(args);
                break;
            case "exit":
                System.exit(0);
                break;
            case "rmdir":
                rmdir(args);
                break;
            case "touch":
                touch(args);
                break;
            case "cat":
                cat(args);
                break;
            case "wc":
                wc(args);
                break;
            default:
                System.out.println("Command not found: " + commandName);
                break;
            
            
        }
    }


}
