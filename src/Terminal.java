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

            String homeDirectory = System.getProperty("user.home");
            System.setProperty("user.dir", homeDirectory);
        } else {
            if (args[0].equals("..")) {

                String currentPath = System.getProperty("user.dir");
                String parent = new File(currentPath).getParent();
                if (parent != null) {
                    System.setProperty("user.dir", parent);
                } else {
                    System.out.println("Already at the root directory");
                }
            } else {
                String newPath = args[0];
                File file = new File(newPath);
                if (file.exists() && file.isDirectory()) {
                    System.out.println(file.getPath()+"\\");
                    System.setProperty("user.dir", file.getPath()+"\\");
                } else {
                    // Search for the directory if the complete path is not provided

                    File currentDirectory = new File(System.getProperty("user.dir"));

                    File[] matchingDirectories = currentDirectory.listFiles(
                            pathname -> pathname.isDirectory() && pathname.getName().equals(args[0])
                    );
                    if (matchingDirectories != null && matchingDirectories.length > 0) {
                        System.setProperty("user.dir", matchingDirectories[0].getPath()+"\\");
                    } else {
                        System.out.println("Directory not found");
                    }
                }
            }
        }
    }

    //abdo ls and ls -r
    public void ls(String[] args){}
    //john
    public static void mkdir(String[] directories) {
        for (String dir : directories) {
            File file = new File(dir);
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
