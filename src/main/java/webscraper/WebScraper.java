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

public class WebScraper {

    final private static WebClient webClient = new WebClient();
    final private static Logger LOG = LoggerFactory.getLogger(WebScraper.class);


    public static void main (String[] args) throws Exception {


        ArrayList<FourteenerRoute> fourteenerRoutes = new WebScraper().createListOfFourteeners();
        System.out.print(fourteenerRoutes);

    }

    public ArrayList<FourteenerRoute> createListOfFourteeners () throws Exception {

        HashSet<String> urlsSeen = new HashSet<>();

        ArrayList<FourteenerRoute> fourteenerRouteArrayList = new ArrayList<>();
        List<List<String>> allRouteIds = getAllRouteIdLists();

        for (List<String> routeIdsByMountain : allRouteIds) {
            routeIdsByMountain.forEach((routeId) -> {
                try {
                    fourteenerRouteArrayList.add(scrapeFourteener(routeId.substring(1), urlsSeen));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        return fourteenerRouteArrayList;

    }

    private FourteenerRoute scrapeFourteener (String url, HashSet<String> routesSeen) throws IOException {

        if (routesSeen.contains(url)) {
            LOG.info("Found duplicate entry of {}; did not log duplicate", url);
            return null;
        }

        routesSeen.add(url);
        FourteenerRoute resultFourteenerRoute = new FourteenerRoute();
        final HtmlPage page = webClient.getPage("https://www.14ers.com" + url);
        final HtmlDivision div = (HtmlDivision) page.getByXPath("//div[@class='BldHdr2 bold1']").get(0);
        final HtmlTable table = (HtmlTable) page.getByXPath("//table[@class='routestatsbox']").get(0);

        scrapeTotalGain(url, table, resultFourteenerRoute);
        scrapeRouteLength(url, table, resultFourteenerRoute);
        scrapeMountainAndRouteName(div, resultFourteenerRoute);
        scrapeSnowRouteOnly(div, resultFourteenerRoute);
        scrapeStandardRoute(div, resultFourteenerRoute);
        scrapeGrade(table, resultFourteenerRoute);
        scrapeTrailhead(table, resultFourteenerRoute);
        scrapeStartElevation(table, resultFourteenerRoute);
        scrapeSummitElevation(table, resultFourteenerRoute);
        scrapeExposure(table, resultFourteenerRoute);
        scrapeRockfallPotential(table, resultFourteenerRoute);
        scrapeRouteFinding(table, resultFourteenerRoute);
        scrapeCommitment(table, resultFourteenerRoute);
        scrapeFourWheelDriveAccessible(div, resultFourteenerRoute);


        return resultFourteenerRoute;


    }

    private static FourteenerRoute scrapeTotalGain(String url, HtmlTable table, FourteenerRoute resultFourteenerRoute) {

        try {
            final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(5);
            int totalGain = Utils.convertTotalGainIntoInteger(cell.asNormalizedText());
            if (totalGain == 0) {
                resultFourteenerRoute.setMultipleRoutes(true);
            } else {
                resultFourteenerRoute.setTotalGain(totalGain);
            }

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            LOG.warn("Unable to scrape {}", url);
            return null;
        }

        return resultFourteenerRoute;


    }

    private static FourteenerRoute scrapeRouteLength (String url, HtmlTable table, FourteenerRoute resultFourteenerRoute) {
        try {
            final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(6);
            double routeLength = Utils.convertRouteLengthIntoInteger(cell.asNormalizedText());
            if (routeLength == 0) {
                resultFourteenerRoute.setMultipleRoutes(true);
            } else {
                resultFourteenerRoute.setRouteLength(routeLength);
            }

            resultFourteenerRoute.setRouteLength(Utils.convertRouteLengthIntoInteger(cell.asNormalizedText()));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            LOG.warn("Unable to scrape {}", url);
            return null;
        }

        return resultFourteenerRoute;
    }

    private static void scrapeMountainAndRouteName (HtmlDivision div, FourteenerRoute currentFourteenerRoute) {

        String[] divArray0 = div.asNormalizedText().split("\n ");
        String[] divArray1 = div.asNormalizedText().split("\n");

        if (divArray1.length == 1 && divArray0.length == 1) {
            currentFourteenerRoute.setMountainName("Approach Route");
            currentFourteenerRoute.setRouteName(divArray1[0]);
        }

        else if (divArray0.length == 1 && divArray1.length == 2) {
            currentFourteenerRoute.setMountainName(divArray1[0]);
            currentFourteenerRoute.setRouteName(divArray1[1]);

        } else {
            currentFourteenerRoute.setMountainName(divArray0[0]);
            currentFourteenerRoute.setRouteName(divArray0[1]);

        }



    }

    private static void scrapeSnowRouteOnly (HtmlDivision div, FourteenerRoute currentFourteenerRoute) {
        currentFourteenerRoute.setSnowRouteOnly((boolean) div.getByXPath("//@src='/images/icon_snowcover_large.png'").get(0));
    }

    private static void scrapeStandardRoute(HtmlDivision div, FourteenerRoute resultFourteenerRoute) {
    }

    private static void scrapeGrade (HtmlTable table, FourteenerRoute currentFourteenerRoute) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(0);
        String gradeString = cell.asNormalizedText().split("\n")[0];
        currentFourteenerRoute.setGrade(Utils.convertGradeIntoMap(gradeString));

    }

    private static void scrapeTrailhead(HtmlTable table, FourteenerRoute currentFourteenerRoute) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(2);
        currentFourteenerRoute.setTrailhead(cell.asNormalizedText());
    }

    private static void scrapeSummitElevation(HtmlTable table, FourteenerRoute currentFourteenerRoute) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(4);
        currentFourteenerRoute.setSummitElevation(Utils.convertStartAndSummitElevationStringToInteger(cell.asNormalizedText()));
    }

    private static void scrapeStartElevation(HtmlTable table, FourteenerRoute currentFourteenerRoute) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(3);
        currentFourteenerRoute.setStartElevation(Utils.convertStartAndSummitElevationStringToInteger(cell.asNormalizedText()));

    }

    private static void scrapeExposure (HtmlTable table, FourteenerRoute currentFourteenerRoute) {
         final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(1);
        currentFourteenerRoute.setExposure(cell.asNormalizedText().split(": ")[1].split("\n")[0]);
    }

    private static void scrapeRockfallPotential (HtmlTable table, FourteenerRoute currentFourteenerRoute) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(1);
        String rockfallPotential = cell.asNormalizedText().split(": ")[2].split("\n")[0];
        currentFourteenerRoute.setRockfallPotential(rockfallPotential.substring(0, rockfallPotential.length() - 2));
    }

    private static void scrapeRouteFinding (HtmlTable table, FourteenerRoute currentFourteenerRoute) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(1);
        String routeFinding = cell.asNormalizedText().split(": ")[3].split("\n")[0];
        currentFourteenerRoute.setRouteFinding(routeFinding.substring(0, routeFinding.length() - 2));
    }

    private static void scrapeCommitment (HtmlTable table, FourteenerRoute currentFourteenerRoute) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath("//td[@class='data_box_cell2']").get(1);
        String commitment = cell.asNormalizedText().split(": ")[4].split("\n")[0];
        currentFourteenerRoute.setCommitment(commitment.substring(0, commitment.length() - 2));
    }

    private static void scrapeFourWheelDriveAccessible(HtmlDivision div, FourteenerRoute resultFourteenerRoute) {
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