import java.util.Arrays;

public class Parser {
    private String commandName;
    private String[] args;

    public boolean parse(String input) {
        String[] tokens = input.split(" ");
        if (tokens.length < 1) {
            return false;
        }
        commandName = tokens[0].toLowerCase();
        args = Arrays.copyOfRange(tokens, 1, tokens.length);
        return true;
    }

    public String getCommandName() {

        return commandName;
    }

    public String[] getArgs() {

        return args;
    }
}
