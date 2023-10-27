import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        Path currentPath = Paths.get("").toAbsolutePath();
        System.out.println("Current directory: " +currentPath.toString());
    }

    //john
    public void cd(String[] args) {
        if (args.length == 0) {
            String homeDirectory = System.getProperty("user.home");
            System.setProperty("user.dir", homeDirectory);
        } else if (args.length == 1) {
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
                    System.setProperty("user.dir", file.getAbsolutePath());
                } else {
                    System.out.println("Invalid path");
                }
            }
        } else {
            System.out.println("Invalid arguments for cd command");
        }
    }
    //abdo ls and ls -r
    public void ls(){

    }
    //john
    public void mkdir(){

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
            case "exit":
                System.exit(0);
                break;
            default:
                System.out.println("Command not found: " + commandName);
                break;
        }
    }


}
