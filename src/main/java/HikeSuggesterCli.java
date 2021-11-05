
import picocli.CommandLine;
import subcommands.Compare;
import subcommands.Group;
import subcommands.Query;
import subcommands.Search;

import java.util.concurrent.Callable;

@CommandLine.Command(
        subcommands = {Compare.class, Group.class, Query.class, Search.class},
        name = "Hike Suggester",
        mixinStandardHelpOptions = true,
        version = "1.0")

class HikeSuggesterCli implements Callable<Integer> {

    @Override
    public Integer call() { // business logic
        return 123;
    }

    public static void main(String... args) { // bootstrap the application
        System.exit(new CommandLine(new HikeSuggesterCli()).execute(args));
    }
}