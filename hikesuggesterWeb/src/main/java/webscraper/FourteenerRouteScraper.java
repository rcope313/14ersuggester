package webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import database.models.ImmutableFetchedRoute;
import org.assertj.core.util.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FourteenerRouteScraper {
    final private WebClient webClient;
    final private static Logger LOG = LoggerFactory.getLogger(FourteenerRouteScraper.class);
    final private static String FOURTEENER_URL = "https://www.14ers.com";
    final private static String PEAK_TABLE_URL = "https://www.14ers.com/php14ers/14ers.php";
    final private static String ROUTE_LIST_URL = "https://www.14ers.com/routelist.php";
    final private static String ROUTE_RESULTS = "routeResults";
    final private static String PEAK_TABLE = "peakTable";
    final private static String HEADER_DIV = "//div[@class='BldHdr2 bold1']";
    final private static String ROUTE_STATS_TABLE = "//table[@class='routestatsbox'";
    final private static String ROUTE_STATS_CELL = "//td[@class='data_box_cell2']";
    final private static String SNOW_IMAGE = "//@src='/images/icon_snowcover_large.png'";
    final private static String STANDARD_ROUTE_ALT_IMAGE = "//@alt='standard'";

    public FourteenerRouteScraper(WebClient webClient) {
        this.webClient = webClient;
    }

    public ArrayList<ImmutableFetchedRoute> buildAllImmutableFetchedRoutes() throws Exception {
        HashSet<String> urlsSeen = new HashSet<>();
        ArrayList<ImmutableFetchedRoute> fourteenerRouteArrayList = new ArrayList<>();
        List<List<String>> allRouteIds = getListOfAllRouteIds();
        for (List<String> routeIdsByMountain : allRouteIds) {
            for (String routeId : routeIdsByMountain) {
                try {
                    String routeUrl = routeId.substring(1);
                    if (!urlsSeen.contains(routeUrl)) {
                        fourteenerRouteArrayList.add(scrapeImmutableFetchedRoute(routeUrl));
                    }
                    urlsSeen.add(routeUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.warn("Unable to scrape {}", routeId);
                    System.out.print("unable to scrape" + routeId);
                }
            }
        }
        return fourteenerRouteArrayList;
    }

    public ImmutableFetchedRoute scrapeImmutableFetchedRoute(String url) throws IOException {
        final String fullUrl = FOURTEENER_URL + url;
        final HtmlPage page = webClient.getPage(fullUrl);
        final HtmlDivision div = (HtmlDivision) page.getByXPath(HEADER_DIV).get(0);
        final HtmlTable table = (HtmlTable) page.getByXPath(ROUTE_STATS_TABLE).get(0);

        return ImmutableFetchedRoute.builder()
                .mountainName(scrapeMountainName(div))
                .routeName(scrapeRouteName(div))
                .isSnowRoute(scrapeSnowRouteOnly(div))
                .isStandardRoute(scrapeStandardRoute(div))
                .grade(scrapeGrade(table))
                .gradeQuality(scrapeGradeQuality(table))
                .trailhead(scrapeTrailhead(table))
                .startElevation(scrapeStartElevation(table))
                .summitElevation(scrapeSummitElevation(table))
                .totalGain(scrapeTotalGain(table))
                .routeLength(scrapeRouteLength(table))
                .exposure(scrapeExposure(table))
                .rockfallPotential(scrapeRockfallPotential(table))
                .routeFinding(scrapeRouteFinding(table))
                .commitment(scrapeCommitment(table))
                .hasMultipleRoutes(scrapeHasMultipleRoutes(table))
                .url(fullUrl)
                .build();
    }

    private List<List<String>> getListOfAllRouteIds() throws Exception {
        List<List<String>> listOfListOfRouteIds = new ArrayList<>();
        getListOfPeakIds().forEach((peakId) -> {
            try {
                listOfListOfRouteIds.add(getListOfRouteIDsOfPeakId(peakId));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return listOfListOfRouteIds;
    }

    private List<String> getListOfRouteIDsOfPeakId(String peakId) throws IOException {
        List<String> listOfRouteIds = new ArrayList<>();
        HashSet<String> routesSeen = new HashSet<>();
        String peakIdUrl = ROUTE_LIST_URL + peakId;
        final HtmlPage page = webClient.getPage(peakIdUrl);
        final HtmlTable table = page.getHtmlElementById(ROUTE_RESULTS);
        for (final HtmlTableRow row : table.getRows()) {
            row.getCells().forEach((cell) -> getListOfRouteIDsOfPeakId(cell, listOfRouteIds, routesSeen));
        }
        return listOfRouteIds;
    }

    private static void getListOfRouteIDsOfPeakId(HtmlTableCell cell, List<String> listOfRouteIds, HashSet<String> routesSeen) {
        HtmlElement element = cell.querySelector("[href]");
        if (element != null) {
            String url = element.getAttribute("href");
            if (!routesSeen.contains(url) && url.startsWith("./route")) {
                routesSeen.add(url);
                listOfRouteIds.add(url);
            }
        }
    }

    private List<String> getListOfPeakIds() throws Exception {
        List<String> listOfPeakIds = new ArrayList<>();
        final HtmlPage page = webClient.getPage(PEAK_TABLE_URL);
        final HtmlTable table = page.getHtmlElementById(PEAK_TABLE);
        for (final HtmlTableRow row : table.getRows()) {
            row.getCells().forEach((cell) -> getListOfPeakIds(cell, listOfPeakIds));
        }
        return listOfPeakIds;
    }

    private static void getListOfPeakIds(HtmlTableCell cell, List<String> listOfPeakIds) {
        HtmlElement element = cell.querySelector("[href]");
        if (element != null) {
            String url = element.getAttribute("href");
            if (url.startsWith("14")) {
                String[] resultString = url.split("14er.php?");
                listOfPeakIds.add(resultString[1]);
            }
        }
    }

    private static boolean scrapeHasMultipleRoutes(HtmlTable table) {
        final HtmlTableDataCell cellTotalGain = (HtmlTableDataCell) table.getByXPath(ROUTE_STATS_CELL).get(5);
        final HtmlTableDataCell cellRouteLength = (HtmlTableDataCell) table.getByXPath(ROUTE_STATS_CELL).get(6);
        int totalGain = convertElevationIntoInteger(cellTotalGain.asNormalizedText());
        double routeLength = convertRouteLengthIntoDouble(cellRouteLength.asNormalizedText());
        return totalGain == 0 || routeLength == 0;
    }

    private static int scrapeTotalGain(HtmlTable table) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath(ROUTE_STATS_CELL).get(5);
        return convertElevationIntoInteger(cell.asNormalizedText());
    }

    private static double scrapeRouteLength(HtmlTable table) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath(ROUTE_STATS_CELL).get(6);
        return convertRouteLengthIntoDouble(cell.asNormalizedText());
    }

    private static String scrapeMountainName(HtmlDivision div) {
        String mountainName;
        String[] divArray0 = div.asNormalizedText().split("\n ");
        String[] divArray1 = div.asNormalizedText().split("\n");
        if (divArray1.length == 1 && divArray0.length == 1) {
            mountainName = "Approach Route";
        }
        else if (divArray0.length == 1 && divArray1.length == 2) {
            mountainName =  divArray1[0];
        } else {
            mountainName = divArray0[0];
        }
        return updateWithCorrectSqlSyntax(mountainName);
    }

    private static String scrapeRouteName(HtmlDivision div) {
        String routeName;
        String[] divArray0 = div.asNormalizedText().split("\n ");
        String[] divArray1 = div.asNormalizedText().split("\n");
        if (divArray1.length == 1 && divArray0.length == 1) {
            routeName = divArray1[0];
        } else {
            routeName = divArray0[1];
        }
        return updateWithCorrectSqlSyntax(routeName);
    }

    private static boolean scrapeSnowRouteOnly(HtmlDivision div) {
        return (boolean) div.getByXPath(SNOW_IMAGE).get(0);
    }

    private static boolean scrapeStandardRoute(HtmlDivision div) {
        return (boolean) div.getByXPath(STANDARD_ROUTE_ALT_IMAGE).get(0);
    }

    private static int scrapeGrade(HtmlTable table) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath(ROUTE_STATS_CELL).get(0);
        String gradeString = cell.asNormalizedText().split("\n")[0];
        if (gradeString.split(" ")[1].equals("Difficult") || gradeString.split(" ")[1].equals("Easy")) {
            return Integer.parseInt(gradeString.split(" ")[3]);
        } else {
            return Integer.parseInt(gradeString.split(" ")[2]);
        }

    }

    private static String scrapeGradeQuality(HtmlTable table) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath(ROUTE_STATS_CELL).get(0);
        String gradeString = cell.asNormalizedText().split("\n")[0];
        if (gradeString.split(" ")[1].equals("Difficult")) {
            return "Difficult";
        }
        else if (gradeString.split(" ")[1].equals("Easy")) {
            return "Easy";
        } else {
            return "";
        }
    }

    private static String scrapeTrailhead(HtmlTable table) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath(ROUTE_STATS_CELL).get(2);
        return updateWithCorrectSqlSyntax(cell.asNormalizedText() + " Trailhead");
    }

    private static int scrapeSummitElevation(HtmlTable table) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath(ROUTE_STATS_CELL).get(4);
        return convertElevationIntoInteger(cell.asNormalizedText());
    }

    private static int scrapeStartElevation(HtmlTable table) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath(ROUTE_STATS_CELL).get(3);
        return convertElevationIntoInteger(cell.asNormalizedText());
    }

    private static String scrapeExposure(HtmlTable table) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath(ROUTE_STATS_CELL).get(1);
        return cell.asNormalizedText().split(": ")[1].split("\n")[0];
    }

    private static String scrapeRockfallPotential(HtmlTable table) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath(ROUTE_STATS_CELL).get(1);
        String rockfallPotential = cell.asNormalizedText().split(": ")[2].split("\n")[0];
        return rockfallPotential.substring(0, rockfallPotential.length() - 2);
    }

    private static String scrapeRouteFinding(HtmlTable table) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath(ROUTE_STATS_CELL).get(1);
        String routeFinding = cell.asNormalizedText().split(": ")[3].split("\n")[0];
        return routeFinding.substring(0, routeFinding.length() - 2);
    }

    private static String scrapeCommitment(HtmlTable table) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) table.getByXPath(ROUTE_STATS_CELL).get(1);
        String commitment = cell.asNormalizedText().split(": ")[4].split("\n")[0];
        return commitment.substring(0, commitment.length() - 2);
    }

    @VisibleForTesting
    static int convertElevationIntoInteger(String str) {
        if (str.split("\n").length > 1) {
            return 0;
        }
        if (str.contains("feet")) {
            return convertElevationIntoInteger(str.split(" feet"));
        } else if (str.contains("ft")){
            return convertElevationIntoInteger(str.split(" ft"));
        } else {
            return 0;
        }
    }

    private static int convertElevationIntoInteger(String[] strArray) {
        char[] charArray = strArray[0].toCharArray();
        int decimalPlace = 1, elevation = 0;

        for (int idx = charArray.length-1; idx >= 0; idx--) {
            if (charArray[idx] >= 48 && charArray[idx] <= 57) {
                elevation += decimalPlace * Character.getNumericValue(charArray[idx]);
                decimalPlace = decimalPlace * 10;
            }
        }
        return elevation;
    }

    @VisibleForTesting
    static double convertRouteLengthIntoDouble(String str) {
        if (str.split("\n").length > 1) {
            return 0;
        }
         if (str.contains("miles")) {
            return Double.parseDouble(str.split(" miles")[0]);
        } else if (str.contains("mi")){
            return Double.parseDouble(str.split(" mi")[0]);
        } else {
            return 0;
        }
    }

    static String updateWithCorrectSqlSyntax(String str) {
        if (str.contains("'")) {
            return insertApostrophe(str);
        } else {
            return str;
        }
    }

    private static String insertApostrophe(String str) {
        StringBuilder resultString = new StringBuilder();
        for (Character c : str.toCharArray()) {
            if (39 == c) {
                resultString.append("''");
            } else {
                resultString.append(c);
            }
        }
        return resultString.toString();
    }
}
