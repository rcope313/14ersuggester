package tester;

import picocli.CommandLine;
import java.util.Arrays;

@CommandLine.Command(name = "test", mixinStandardHelpOptions = true)
public class TestCommand implements Runnable{

    @CommandLine.Option(names = {"--m", "--mountainnames"}, split = ", ",
            description = "Search by name of mountain. If searching by more than one mountain name, split list with ', '")
    public String[] mountainNames;
    @CommandLine.Option(names = {"-v", "--verbose"},
            description = "Verbose. Result output will yield all table columns, rather than just those specified in query")
    public boolean verbose = false;
    @CommandLine.Option(names = {"-q", "--query"}, interactive = true,
            description = "Write MySql query. Tables name: hikesuggester.fourtneer_routes, hikesuggester.trailheads")
    public String query;
    @CommandLine.Parameters(index = "0", description = "First argument, ice cream flavor")
    String iceCreamFlavor;
    @CommandLine.Parameters(index = "1", description = "Second argument, ice cream quantity")
    int iceCreamQuantity;

    @Override
    public void run() {
        displayMountainNames();
        isVerbose();
        displayQuery();
        displayIceCreamArguments();
        System.out.print("Your test subcommand works!");

    }


    /*
    MUST be split with specific regex. List is contained within double quotes.
     */
    public void displayMountainNames () {

        if (mountainNames != null) {
            Arrays.stream(mountainNames).forEach((mountainName) -> System.out.print(mountainName + "\n"));
        }
    }

    /*

     */
    public void isVerbose () {
        if (verbose) {
            System.out.print("You're shit is verbose!!! :) \n");

        } else {
            System.out.print("You're shit isn't verbose :/ \n");
        }

    }
    /*

     */
    public void displayQuery () {

        if (query != null) {

            System.out.print("Nice query!! \n");
            System.out.print(query);
        }

    }

    /*

     */
    public void displayIceCreamArguments () {
        System.out.print("Your ice cream arguments worked! \n");
        System.out.print(iceCreamFlavor + " ");
        System.out.print(iceCreamQuantity + "\n");

    }

}
