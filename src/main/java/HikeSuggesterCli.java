import picocli.CommandLine;
import subcommands.CompareSubCommand;
import subcommands.QuerySubCommand;
import subcommands.SearchSubCommand;
import tester.SyntaxCommand;
import tester.TestCommand;

import java.util.concurrent.Callable;

@CommandLine.Command(
        subcommands =
                {CompareSubCommand.class, QuerySubCommand.class, SearchSubCommand.class, TestCommand.class, SyntaxCommand.class},
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