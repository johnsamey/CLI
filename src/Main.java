import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
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
            terminal.chooseCommandAction();
            // commandHistory.add(input);

        }
        scanner.close();
    }
}