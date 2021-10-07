package webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import java.util.ArrayList;
import java.util.List;

public class WebScraper {

    public static void main(String[] args) throws Exception {

        try (final WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage("https://www.14ers.com/php14ers/14ers.php");
            final HtmlTable table = page.getHtmlElementById("peakTable");
            
            List<String> listOfRouteIds = new ArrayList<>();
            for (final HtmlTableRow row : table.getRows()) {
                row.getCells().forEach((cell) -> getPeakId(cell, listOfRouteIds));
            }

            System.out.print(listOfRouteIds);


        }
    }

    private static void getPeakId(HtmlTableCell cell, List<String> listOfRouteIds) {

        HtmlElement element = cell.querySelector("[href]");
        if (element != null) {
            String url = element.getAttribute("href");
            if (url.startsWith("14")) {
                String[] resultString = url.split("14er.php?");
                listOfRouteIds.add(resultString[1]);
            }
        }
    }
}