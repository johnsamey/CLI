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
    public String pwd() {
        return "Current directory: " + System.getProperty("user.dir");
    }

    //john
    public void cd(String[] args) {
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
