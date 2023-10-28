import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
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
