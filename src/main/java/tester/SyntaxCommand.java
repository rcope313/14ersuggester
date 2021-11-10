package tester;

import picocli.CommandLine;

@CommandLine.Command(name = "syntax", mixinStandardHelpOptions = true)
public class SyntaxCommand implements Runnable {


    @Override
    public void run() {
        Object[][] table = new String[4][];
        table[0] = new String[] { "Pie", "2.2", "12" };
        table[1] = new String[] { "Cracker", "4", "15" };
        table[2] = new String[] { "Pop tarts", "1", "4" };
        table[3] = new String[] { "Sun Chips", "5", "2" };

        String itemId = CommandLine.Help.Ansi.AUTO.string("@|bold,green,underline Items#|@");

        System.out.format("%-15s%-15s%-15s%-15s\n", "ItemId", "Item", "Price", "Quantity");
        for (int i = 0; i < table.length; i++) {
            Object[] row = table[i];
            System.out.format("%-15d%-15s%-15s%-15s\n", i, row[0], row[1], row[2]);
        }

    }
}
