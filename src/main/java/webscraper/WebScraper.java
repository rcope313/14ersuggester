package webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WebScraper {

    final WebClient webClient = new WebClient();

    public static void main (String[] args) throws Exception {
       System.out.print(new WebScraper().getAllRouteIdLists());
    }

    public List<List<String>> getAllRouteIdLists () throws Exception {

        List<List<String>> listOfListOfRouteIds = new ArrayList<>();

        getPeakIdList().forEach((peakId) -> {

            try {
                listOfListOfRouteIds.add(getARouteIdList(peakId));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        return listOfListOfRouteIds;


    }



    public List<String> getARouteIdList(String peakId) throws IOException {

        List<String> listOfRouteIds = new ArrayList<>();
        HashSet<String> routesSeen = new HashSet<>();

        String peakIdUrl = "https://www.14ers.com/routelist.php" + peakId;
        final HtmlPage page = webClient.getPage(peakIdUrl);
        final HtmlTable table = page.getHtmlElementById("routeResults");

        for (final HtmlTableRow row : table.getRows()) {
            row.getCells().forEach((cell) -> getARouteIdList(cell, listOfRouteIds, routesSeen));
        }

        return listOfRouteIds;


    }

    private void getARouteIdList(HtmlTableCell cell, List<String> listOfRouteIds, HashSet<String> routesSeen) {

        HtmlElement element = cell.querySelector("[href]");
        if (element != null) {
            String url = element.getAttribute("href");

            if (!routesSeen.contains(url) && url.startsWith("./route")) {
                routesSeen.add(url);
                listOfRouteIds.add(url);
            }


        }


    }

    public List<String> getPeakIdList() throws Exception {

        List<String> listOfPeakIds = new ArrayList<>();

            final HtmlPage page = webClient.getPage("https://www.14ers.com/php14ers/14ers.php");
            final HtmlTable table = page.getHtmlElementById("peakTable");

            for (final HtmlTableRow row : table.getRows()) {
                row.getCells().forEach((cell) -> getPeakIdList(cell, listOfPeakIds));
            }


        return listOfPeakIds;


    }

    private static void getPeakIdList(HtmlTableCell cell, List<String> listOfPeakIds) {

        HtmlElement element = cell.querySelector("[href]");
        if (element != null) {
            String url = element.getAttribute("href");
            if (url.startsWith("14")) {
                String[] resultString = url.split("14er.php?");
                listOfPeakIds.add(resultString[1]);
            }
        }
    }
}