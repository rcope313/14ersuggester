package webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import models.FourteenerRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utility.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FourteenerRouteScraper {

    final private static WebClient webClient = new WebClient();
    final private static Logger LOG = LoggerFactory.getLogger(FourteenerRouteScraper.class);


    public static void main (String[] args) throws Exception {

        ArrayList<FourteenerRoute> fourteenerRoutes = new FourteenerRouteScraper().createListOfFourteenerRoutes();
        System.out.print(fourteenerRoutes);

    }

    public static ArrayList<FourteenerRoute> createListOfFourteenerRoutes() throws Exception {

        HashSet<String> urlsSeen = new HashSet<>();

        ArrayList<FourteenerRoute> fourteenerRouteArrayList = new ArrayList<>();
        List<List<String>> allRouteIds = getAllRouteIdLists();
        int idx = 0;

        for (List<String> routeIdsByMountain : allRouteIds) {

            for (String routeId : routeIdsByMountain) {
                try {
                    fourteenerRouteArrayList.add(scrapeFourteener(routeId.substring(1), urlsSeen, idx));
                    idx++;
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.warn("Unable to scrape {}", routeId);
                    System.out.print("unable to scrape" + routeId);

                }
            }
        }

        return fourteenerRouteArrayList;

    }

    public static FourteenerRoute scrapeFourteener(String url) throws IOException {

        FourteenerRoute resultFourteenerRoute = new FourteenerRoute();
        resultFourteenerRoute.setUrl("https://www.14ers.com" + url);
        resultFourteenerRoute.setFourteenerRouteId(0);

        final HtmlPage page = webClient.getPage("https://www.14ers.com" + url);
        final HtmlDivision div = (HtmlDivision) page.getByXPath("//div[@class='BldHdr2 bold1']").get(0);
        final HtmlTable table = (HtmlTable) page.getByXPath("//table[@class='routestatsbox']").get(0);

        scrapeTotalGain(url, table, resultFourteenerRoute);
        scrapeRouteLength(url, table, resultFourteenerRoute);
        scrapeMountainAndRouteName(div, resultFourteenerRoute);
        scrapeSnowRouteOnly(div, resultFourteenerRoute);
        scrapeStandardRoute(div, resultFourteenerRoute);
        scrapeGradeQuality(table, resultFourteenerRoute);
        scrapeTrailhead(table, resultFourteenerRoute);
        scrapeStartElevation(table, resultFourteenerRoute);
        scrapeSummitElevation(table, resultFourteenerRoute);
        scrapeExposure(table, resultFourteenerRoute);
        scrapeRockfallPotential(table, resultFourteenerRoute);
        scrapeRouteFinding(table, resultFourteenerRoute);
        scrapeCommitment(table, resultFourteenerRoute);



        return resultFourteenerRoute;


    }


    private static FourteenerRoute scrapeFourteener (String url, HashSet<String> routesSeen, int idx) throws IOException {

        if (routesSeen.contains(url)) {
            return null;
        }

        routesSeen.add(url);
        FourteenerRoute resultFourteenerRoute = new FourteenerRoute();
        resultFourteenerRoute.setUrl("https://www.14ers.com" + url);
        resultFourteenerRoute.setFourteenerRouteId(idx);

        final HtmlPage page = webClient.getPage("https://www.14ers.com" + url);
        final HtmlDivision div = (HtmlDivision) page.getByXPath("//div[@class='BldHdr2 bold1']").get(0);
        final HtmlTable table = (HtmlTable) page.getByXPath("//table[@class='routestatsbox']").get(0);

        scrapeTotalGain(url, table, resultFourteenerRoute);
        scrapeRouteLength(url, table, resultFourteenerRoute);
        scrapeMountainAndRouteName(div, resultFourteenerRoute);
        scrapeSnowRouteOnly(div, resultFourteenerRoute);
        scrapeStandardRoute(div, resultFourteenerRoute);
        scrapeGradeQuality(table, resultFourteenerRoute);
        scrapeTrailhead(table, resultFourteenerRoute);
        scrapeStartElevation(table, resultFourteenerRoute);
        scrapeSummitElevation(table, resultFourteenerRoute);
        scrapeExposure(table, resultFourteenerRoute);
        scrapeRockfallPotential(table, resultFourteenerRoute);
        scrapeRouteFinding(table, resultFourteenerRoute);
        scrapeCommitment(table, resultFourteenerRoute);



        return resultFourteenerRoute;


    }

    private static void  scrapeTotalGain(String url, HtmlTable table, FourteenerRoute resultFourteenerRoute) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(5);
        int totalGain = Utils.convertTotalGainIntoInteger(cell.asNormalizedText());

        if (totalGain == 0) {
            resultFourteenerRoute.setHasMultipleRoutes(true);
        } else {
            resultFourteenerRoute.setTotalGain(totalGain);
        }


    }

    private static void scrapeRouteLength (String url, HtmlTable table, FourteenerRoute resultFourteenerRoute) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(6);
        double routeLength = Utils.convertRouteLengthIntoInteger(cell.asNormalizedText());

        if (routeLength == 0) {
            resultFourteenerRoute.setHasMultipleRoutes(true);
        } else {
            resultFourteenerRoute.setRouteLength(routeLength);
        }

        resultFourteenerRoute.setRouteLength(Utils.convertRouteLengthIntoInteger(cell.asNormalizedText()));



    }

    private static void scrapeMountainAndRouteName (HtmlDivision div, FourteenerRoute resultFourteenerRoute) {

        String[] divArray0 = div.asNormalizedText().split("\n ");
        String[] divArray1 = div.asNormalizedText().split("\n");

        if (divArray1.length == 1 && divArray0.length == 1) {
            resultFourteenerRoute.setMountainName("Approach Route");
            resultFourteenerRoute.setRouteName(divArray1[0]);
        }

        else if (divArray0.length == 1 && divArray1.length == 2) {
            resultFourteenerRoute.setMountainName(divArray1[0]);
            resultFourteenerRoute.setRouteName(divArray1[1]);

        } else {
            resultFourteenerRoute.setMountainName(divArray0[0]);
            resultFourteenerRoute.setRouteName(divArray0[1]);

        }



    }

    private static void scrapeSnowRouteOnly (HtmlDivision div, FourteenerRoute resultFourteenerRoute) {
        resultFourteenerRoute.setSnowRoute((boolean) div.getByXPath("//@src='/images/icon_snowcover_large.png'").get(0));
        
    }

    private static void scrapeStandardRoute(HtmlDivision div, FourteenerRoute resultFourteenerRoute) {
        resultFourteenerRoute.setStandardRoute((boolean) div.getByXPath("//@alt='standard'").get(0));

    }

    private static void scrapeGradeQuality(HtmlTable table, FourteenerRoute resultFourteenerRoute) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(0);
        String gradeString = cell.asNormalizedText().split("\n")[0];
        resultFourteenerRoute.setGradeQuality(Utils.convertStringIntoGradeQuality(gradeString));

    }

    private static void scrapeTrailhead(HtmlTable table, FourteenerRoute resultFourteenerRoute) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(2);
        resultFourteenerRoute.setTrailhead(cell.asNormalizedText() + " Trailhead");
    }

    private static void scrapeSummitElevation(HtmlTable table, FourteenerRoute resultFourteenerRoute) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(4);
        resultFourteenerRoute.setSummitElevation(Utils.convertStartAndSummitElevationStringToInteger(cell.asNormalizedText()));
    }

    private static void scrapeStartElevation(HtmlTable table, FourteenerRoute resultFourteenerRoute) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(3);
        resultFourteenerRoute.setStartElevation(Utils.convertStartAndSummitElevationStringToInteger(cell.asNormalizedText()));

    }

    private static void scrapeExposure (HtmlTable table, FourteenerRoute resultFourteenerRoute) {
         final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(1);
        resultFourteenerRoute.setExposure(cell.asNormalizedText().split(": ")[1].split("\n")[0]);
    }

    private static void scrapeRockfallPotential (HtmlTable table, FourteenerRoute resultFourteenerRoute) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(1);
        String rockfallPotential = cell.asNormalizedText().split(": ")[2].split("\n")[0];
        resultFourteenerRoute.setRockfallPotential(rockfallPotential.substring(0, rockfallPotential.length() - 2));
    }

    private static void scrapeRouteFinding (HtmlTable table, FourteenerRoute resultFourteenerRoute) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(1);
        String routeFinding = cell.asNormalizedText().split(": ")[3].split("\n")[0];
        resultFourteenerRoute.setRouteFinding(routeFinding.substring(0, routeFinding.length() - 2));
    }

    private static void scrapeCommitment (HtmlTable table, FourteenerRoute resultFourteenerRoute) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(1);
        String commitment = cell.asNormalizedText().split(": ")[4].split("\n")[0];
        resultFourteenerRoute.setCommitment(commitment.substring(0, commitment.length() - 2));
    }




    public static List<List<String>> getAllRouteIdLists () throws Exception {

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

    public static List<String> getARouteIdList(String peakId) throws IOException {

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

    private static void getARouteIdList(HtmlTableCell cell, List<String> listOfRouteIds, HashSet<String> routesSeen) {

        HtmlElement element = cell.querySelector("[href]");
        if (element != null) {
            String url = element.getAttribute("href");

            if (!routesSeen.contains(url) && url.startsWith("./route")) {
                routesSeen.add(url);
                listOfRouteIds.add(url);
            }


        }


    }

    public static List<String> getPeakIdList() throws Exception {

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