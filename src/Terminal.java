import java.io.File;
<<<<<<< Updated upstream
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
=======
import java.io.IOException;
>>>>>>> Stashed changes
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.nio.file.Files; 

public class Terminal {
    private Parser parser;

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
<<<<<<< Updated upstream
    public void ls(List<String> args){
        Path currentPath = Paths.get("").toAbsolutePath();
        File currentDirectory = new File(currentPath.toString());
=======
    public void ls(String[] args){
        
        File currentDirectory = new File(System.getProperty("user.dir"));
>>>>>>> Stashed changes
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
    public void rmdir(String[] args){
        if (args.length == 1 && "*".equals(args[0])) {
            File currentDirectory = new File(System.getProperty("user.dir"));
            removeEmptyDirectories(currentDirectory);
        } else {
            System.out.println("Usage: rmdir *");
        }

    }
    public void removeEmptyDirectories(File dir){
        File[] subDirs=dir.listFiles(File::isDirectory);
        if(subDirs!=null){
            
            for(File subDir:subDirs){
                removeEmptyDirectories(subDir);
                if(subDir.listFiles().length==0){
                    subDir.delete();
                }else{
                    removeEmptyDirectories(subDir);
                }
            }
        }

    }
    //abdo
    public void touch(){

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
    public void cat(){

    }
    //abdo
    public void wc(){

    }


    public void chooseCommandAction() {
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
            default:
                System.out.println("Command not found: " + commandName);
                break;
        }
    }


}
