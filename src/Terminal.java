import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Terminal {
    private Parser parser;

    public Terminal() {
        this.parser = new Parser();
    }
    public Parser getParser() {
        return parser;
    }
    public void echo(String[] args) {
        System.out.println(String.join(" ", args));
    }
    public void pwd() {
        System.out.println("Current directory: " + System.getProperty("user.dir"));
    }

    //john
    public void cd(String[] args) {
        if (args.length == 0) {
            System.setProperty("user.dir", System.getProperty("user.home"));
            return;
        }

        String directoryPath = System.getProperty("user.dir");
        if (args[0].equals("..")) {
            File parentDirectory = new File(directoryPath).getParentFile();
            if (parentDirectory != null) {
                System.setProperty("user.dir", parentDirectory.getPath());
            } else {
                System.out.println("Already at the root directory");
            }
        } else {
            StringBuilder targetDirectory= new StringBuilder(args[0]);
            targetDirectory = new StringBuilder(args[0].replaceAll("^\"|\"$", ""));
            if (args.length>1) {
                for (int i = 1;i< args.length;i++) {
                    targetDirectory.append(" ").append(args[i].replaceAll("^\"|\"$", ""));
                }
            }
            File newDirectory = new File(directoryPath, targetDirectory.toString());
            if (!newDirectory.exists()) {
                newDirectory = new File(targetDirectory.toString());
            }

            if (newDirectory.exists() && newDirectory.isDirectory()) {
                System.setProperty("user.dir", newDirectory.getPath()+"\\");
            } else {
                System.out.println("Directory not found");
            }
        }
        System.out.println("Current Directory: " + System.getProperty("user.dir"));
    }

    //abdo ls and ls -r
    public void ls(String[] args){
        Path currentPath = Paths.get("").toAbsolutePath();
        File currentDirectory = new File(currentPath.toString());
        File[] files = currentDirectory.listFiles();
        if(args.length > 0 && "-r".equals(args[0])){
            Arrays.sort(files, (f1, f2) -> f2.getName().compareTo(f1.getName()));
        }else{
            Arrays.sort(files);
        }
        for (File file : files) {
                System.out.println(file.getName());
        }
    }
    //john
    public static void mkdir(String[] directories) {
        for (String dir : directories) {
            File file = new File(System.getProperty("user.dir") + File.separator + dir);
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
    public void rmdir(){

    }
    //john
    public void touch(){

    }
    //john cp and cp -r
    public void cp(){

    }
    //john
    public void rm(){

    }
    //abdo
    public void cat(){

    }
    //abdo
    public void wc(){

    }


    public void chooseCommandAction() {
        String commandName = parser.getCommandName();
        String[] args = parser.getArgs();

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
            case "exit":
                System.exit(0);
                break;
            default:
                System.out.println("Command not found: " + commandName);
                break;
        }
    }


}
