import java.util.Scanner;

public class Terminal {
    private Parser parser;

    public Terminal() {
        this.parser = new Parser();
    }
    public Parser getParser() {
        return parser;
    }
    public String pwd() {
        return "Current directory: " + System.getProperty("user.dir");
    }

    public void cd(String[] args) {
    }

    public void echo(String[] args) {
        System.out.println(String.join(" ", args));
    }
    public void chooseCommandAction() {
        String commandName = parser.getCommandName();
        String[] args = parser.getArgs();

        switch (commandName) {
            case "pwd":
                (pwd());
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
