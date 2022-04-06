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
import java.util.Optional;

public class FourteenerRouteScraper {
    final private WebClient webClient;
    final private static Logger LOG = LoggerFactory.getLogger(FourteenerRouteScraper.class);
    final private static String PEAK_TABLE_URL = "https://www.14ers.com/php14ers/14ers.php";
    final private static String ROUTE_LIST_URL = "https://www.14ers.com/routelist.php";
    final private static String ROUTE_RESULTS = "routeResults";
    final private static String PEAK_TABLE = "peakTable";
    final private static String HEADER_DIV = "//div[@class='BldHdr2 bold1']";
    final private static String SNOW_IMAGE = "//@src='/images/icon_snowcover_large.png'";
    final private static String STANDARD_ROUTE_ALT_IMAGE = "//@alt='standard'";
    final private static String MOUNTAIN_NAME_XPATH = "//*[@id='wrap']/div[6]/span/text()";
    final private static String ROUTE_NAME_XPATH = "//*[@id='wrap']/div[6]/text()";
    final private static String TOTAL_GAIN_XPATH = "//*[@id='statsColumn1']/table/tbody/tr[6]/td[2]";
    final private static String ROUTE_LENGTH_XPATH = "//*[@id='statsColumn1']/table/tbody/tr[7]/td[2]";
    final private static String START_ELEVATION_XPATH = "//*[@id='statsColumn1']/table/tbody/tr[4]/td[2]";
    final private static String SUMMIT_ELEVATION_XPATH = "//*[@id='statsColumn1']/table/tbody/tr[5]/td[2]";
    final private static String GRADE_XPATH= "//*[@id='statsColumn1']/table/tbody/tr[1]/td[2]/div/span";
    final private static String TRAILHEAD_XPATH = "//*[@id='statsColumn1']/table/tbody/tr[3]/td[2]/a";
    final private static String EXPOSURE_XPATH = "//*[@id='statsColumn1']/table/tbody/tr[2]/td[2]/span[1]";
    final private static String ROCKFALL_POTENTIAL_XPATH = "//*[@id='statsColumn1']/table/tbody/tr[2]/td[2]/span[2]";
    final private static String ROUTE_FINDING_XPATH = "//*[@id='statsColumn1']/table/tbody/tr[2]/td[2]/span[3]";
    final private static String COMMITMENT_XPATH = "//*[@id='statsColumn1']/table/tbody/tr[2]/td[2]/span[4]";

    public FourteenerRouteScraper(WebClient webClient) {
        this.webClient = webClient;
    }

    public static void main(String[] args) throws IOException {
        FourteenerRouteScraper scraper = new FourteenerRouteScraper(new WebClient());
        System.out.print(scraper.scrapeImmutableFetchedRoute("https://www.14ers.com/route.php?route=elbe1"));
    }

    public ArrayList<Optional<ImmutableFetchedRoute>> buildAllImmutableFetchedRoutes() throws Exception {
        HashSet<String> urlsSeen = new HashSet<>();
        ArrayList<Optional<ImmutableFetchedRoute>> fourteenerRouteArrayList = new ArrayList<>();
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

    public Optional<ImmutableFetchedRoute> scrapeImmutableFetchedRoute(String url) throws IOException {
        final HtmlPage page = webClient.getPage(url);
        final HtmlDivision div = (HtmlDivision) page.getByXPath(HEADER_DIV).get(0);
        Optional<ImmutableFetchedRoute> route = Optional.empty();

        try {
            route = Optional.of(ImmutableFetchedRoute.builder()
                    .mountainName(scrapeMountainName(page))
                    .routeName(scrapeRouteName(page))
                    .isSnowRoute(scrapeSnowRouteOnly(div))
                    .isStandardRoute(scrapeStandardRoute(div))
                    .grade(scrapeGrade(page))
                    .gradeQuality(scrapeGradeQuality(page))
                    .trailhead(scrapeTrailhead(page))
                    .startElevation(scrapeStartElevation(page))
                    .summitElevation(scrapeSummitElevation(page))
                    .totalGain(scrapeTotalGain(page))
                    .routeLength(scrapeRouteLength(page))
                    .exposure(scrapeExposure(page))
                    .rockfallPotential(scrapeRockfallPotential(page))
                    .routeFinding(scrapeRouteFinding(page))
                    .commitment(scrapeCommitment(page))
                    .hasMultipleRoutes(scrapeHasMultipleRoutes(page))
                    .url(url)
                    .build());
            LOG.info("Able to scrape {}", url);
        } catch (Exception e){
            LOG.warn("Unable to scrape {}", url);
        }
        return route;
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

    private static boolean scrapeHasMultipleRoutes(HtmlPage page) {
        final HtmlTableDataCell cellTotalGain = (HtmlTableDataCell) page.getByXPath(TOTAL_GAIN_XPATH).get(0);
        final HtmlTableDataCell cellRouteLength = (HtmlTableDataCell) page.getByXPath(ROUTE_LENGTH_XPATH).get(0);
        int totalGain = convertElevationIntoInteger(cellTotalGain.asNormalizedText());
        double routeLength = convertRouteLengthIntoDouble(cellRouteLength.asNormalizedText());
        return totalGain == 0 || routeLength == 0;
    }

    private static int scrapeTotalGain(HtmlPage page) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) page.getByXPath(TOTAL_GAIN_XPATH).get(0);
        return convertElevationIntoInteger(cell.asNormalizedText());
    }

    private static double scrapeRouteLength(HtmlPage page) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) page.getByXPath(ROUTE_LENGTH_XPATH).get(0);
        return convertRouteLengthIntoDouble(cell.asNormalizedText());
    }

    private static String scrapeMountainName(HtmlPage page) {
        DomText text = (DomText) page.getByXPath(MOUNTAIN_NAME_XPATH).get(0);
        return updateWithCorrectSqlSyntax(text.asNormalizedText());
    }

    private static String scrapeRouteName(HtmlPage page) {
        DomText text = (DomText) page.getByXPath(ROUTE_NAME_XPATH).get(0);
        return updateWithCorrectSqlSyntax(text.asNormalizedText());
    }

    private static boolean scrapeSnowRouteOnly(HtmlDivision div) {
        return (boolean) div.getByXPath(SNOW_IMAGE).get(0);
    }

    private static boolean scrapeStandardRoute(HtmlDivision div) {
        return (boolean) div.getByXPath(STANDARD_ROUTE_ALT_IMAGE).get(0);
    }

    private static int scrapeGrade(HtmlPage page) {
        HtmlSpan var = (HtmlSpan) page.getByXPath(GRADE_XPATH).get(0);
        String gradeString = var.asNormalizedText().split("\n")[0];
        if (gradeString.split(" ")[1].equals("Difficult") || gradeString.split(" ")[1].equals("Easy")) {
            return Integer.parseInt(gradeString.split(" ")[3]);
        } else {
            return Integer.parseInt(gradeString.split(" ")[2]);
        }
    }

    private static String scrapeGradeQuality(HtmlPage page) {
        HtmlSpan var = (HtmlSpan) page.getByXPath(GRADE_XPATH).get(0);
        String gradeString = var.asNormalizedText().split("\n")[0];
        if (gradeString.split(" ")[1].equals("Difficult")) {
            return "Difficult";
        }
        else if (gradeString.split(" ")[1].equals("Easy")) {
            return "Easy";
        } else {
            return "";
        }
    }

    private static String scrapeTrailhead(HtmlPage page) {
        final HtmlAnchor anchor = (HtmlAnchor) page.getByXPath(TRAILHEAD_XPATH).get(0);
        return updateWithCorrectSqlSyntax(anchor.asNormalizedText() + " Trailhead");
    }

    private static int scrapeSummitElevation(HtmlPage page) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) page.getByXPath(SUMMIT_ELEVATION_XPATH).get(0);
        return convertElevationIntoInteger(cell.asNormalizedText());
    }

    private static int scrapeStartElevation(HtmlPage page) {
        final HtmlTableDataCell cell = (HtmlTableDataCell) page.getByXPath(START_ELEVATION_XPATH).get(0);
        return convertElevationIntoInteger(cell.asNormalizedText());
    }

    private static String scrapeExposure(HtmlPage page) {
        final HtmlSpan span = (HtmlSpan) page.getByXPath(EXPOSURE_XPATH).get(0);
        return span.asNormalizedText();
    }

    private static String scrapeRockfallPotential(HtmlPage page) {
        final HtmlSpan span = (HtmlSpan) page.getByXPath(ROCKFALL_POTENTIAL_XPATH).get(0);
        return span.asNormalizedText();
    }

    private static String scrapeRouteFinding(HtmlPage page) {
        final HtmlSpan span = (HtmlSpan) page.getByXPath(ROUTE_FINDING_XPATH).get(0);
        return span.asNormalizedText();
    }

    private static String scrapeCommitment(HtmlPage page) {
        final HtmlSpan span = (HtmlSpan) page.getByXPath(COMMITMENT_XPATH).get(0);
        return span.asNormalizedText();
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
