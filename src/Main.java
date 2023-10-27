import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String currentDirectory = System.getProperty("user.dir");
            System.out.print(currentDirectory + "> ");
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }
            if (!terminal.getParser().parse(input)) {
                System.out.println("Invalid command");
                continue;
            }
            terminal.chooseCommandAction();
        }
    }
}