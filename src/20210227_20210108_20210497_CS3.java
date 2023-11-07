import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

//          pwd
//        mkdir dir1 dir2
//        touch f1.txt
//        touch <current path>/f2.txt [full path]
//        ls > f1.txt
//        cat f1.txt
//        rmdir dir1
//        cp f1.txt f3.txt
//        cd dir2
//        touch f3.txt
//        ls
 class Parser {
    private String commandName;
    private List<String> args;
    


    public boolean parse(String input) {
        String[] tokens = input.split("(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$) ");
        if (tokens.length < 1) {
            return false;
        }
        commandName = tokens[0].replaceAll("^\"|\"$", "").toLowerCase();
        args = new ArrayList<>();
        for (int i = 1; i < tokens.length; i++) {
            args.add(tokens[i].replaceAll("^\"|\"$", ""));
        }
        return true;
    }

    public String getCommandName() {

        return commandName;
    }

    public List<String> getArgs() {

        return args;
    }
}

  class Terminal {
    private Parser parser;
    private static List<String> commandHistory=new ArrayList<>();

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
    
    public void touch(List<String> args)  {
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
                
                    try {
                        if (file.createNewFile()) {
                            System.out.println("File created: " + file.getAbsolutePath());
                        } else {
                            System.out.println("Failed to create file: " + file.getAbsolutePath());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }else{
            System.out.println("Incorrect number of arguments");
        }

    }

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
      public void cpR(String sourceDirName, String destinationDirName) {
          File sourceDir = new File(System.getProperty("user.dir") + File.separator + sourceDirName);
          File destinationDir = new File(System.getProperty("user.dir") + File.separator + destinationDirName);

          if (!sourceDir.exists() || !sourceDir.isDirectory()) {
              System.out.println("Source directory does not exist or is not a directory.");
              return;
          }

          if (!destinationDir.exists()) {
              if (!destinationDir.mkdir()) {
                  System.out.println("Failed to create destination directory.");
                  return;
              }
          }

          File[] files = sourceDir.listFiles();
          if (files != null) {
              for (File file : files) {
                  if (file.isDirectory()) {
                      File newDir = new File(destinationDir.getAbsolutePath() + File.separator + file.getName());
                      cpR(file.getPath(), newDir.getPath());
                  } else {
                      File newFile = new File(destinationDir.getAbsolutePath() + File.separator + file.getName());
                      try {
                          Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                      } catch (IOException e) {
                          System.out.println("Failed to copy file: " + e.getMessage());
                      }
                  }
              }
          }

          System.out.println("Directory copied successfully.");
      }
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
    
    public void cat(List<String> args) throws FileNotFoundException {

        if (args.size() < 1) {
            System.out.println("Insufficient number of arguments");
            return;
        }
        
        File fileOne = new File(System.getProperty("user.dir") + File.separator + args.get(0));;
        

        if (!fileOne.exists()) {
            System.out.println("File does not exist: " + fileOne.getAbsolutePath());
            return;
        }
        

        System.out.print(readFile(fileOne));

        if (args.size() == 2) {
            File fileTwo =new File(System.getProperty("user.dir") + File.separator + args.get(1));

            if (fileTwo.exists()) {

                System.out.print(readFile(fileTwo));
            } else {
                System.out.println();
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
    
    public void history(){
        for (String command : commandHistory) {
            System.out.println(command);
        }
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
            case "history":
                history();
                break;
            default:
                System.out.println("Command not found: " + commandName);
                break;
            
            
        }
    }
    
    
    
    public static void main(String[] args)  {
        Terminal terminal = new Terminal();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            File currentDirectory = new File(System.getProperty("user.dir"));
            System.out.print(currentDirectory.getPath() + "> ");
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }
            if (!terminal.getParser().parse(input)) {
                System.out.println("Invalid command");
                continue;
            }
            try {
                terminal.chooseCommandAction();
            } catch (IOException e) {
                e.printStackTrace();
            }
            commandHistory.add(input);

        }
        scanner.close();
    }
}

