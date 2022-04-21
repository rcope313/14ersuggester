import picocli.CommandLine;
import subcommands.CompareSubCommand;
import subcommands.SearchSubCommand;
import subcommands.SuggestSubCommand;

import java.util.concurrent.Callable;

@CommandLine.Command(
        subcommands = {CompareSubCommand.class, SearchSubCommand.class, SuggestSubCommand.class},
        name = "Hike Suggester",
        mixinStandardHelpOptions = true,
        version = "1.0")
public class HikeSuggesterCli implements Callable<Integer> {

    @Override
    public Integer call() { // business logic
        return 123;
    }

    public static void main(String... args) { // bootstrap the application
        CommandLine cmd = new CommandLine(new HikeSuggesterCli());
        int exitCode = cmd.execute(args);
        var result = cmd.getExecutionResult();
    }
}