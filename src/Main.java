import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter command: ");
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