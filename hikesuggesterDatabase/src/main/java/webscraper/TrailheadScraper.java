package webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import database.models.ImmutableFetchedTrailhead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class TrailheadScraper {
    final private static WebClient webClient = new WebClient();
    final private static Logger LOG = LoggerFactory.getLogger(TrailheadScraper.class);
    final private static String DIV_PAGE_TILE = "//div[@class='pagetitle']";
    final private static String DIV_STATS_BOX = "//div[@class='statsbox']";
    final private static String FOURTNEERERS_URL = "https://www.14ers.com/php14ers";
    final private static String TRAILHEADS_URL = "https://www.14ers.com/php14ers/trailheadsmain.php";
    final private static String SINGLE_RANGE_DIV = "//div[@class='singlerange']//a/@href";

    public static ArrayList<ImmutableFetchedTrailhead> buildAllImmutableFetchedTrailheads() throws Exception {
        ArrayList<String> trailHeadUrls = getTrailHeadUrlList();
        ArrayList<ImmutableFetchedTrailhead> trailHeads = new ArrayList<>();
        for (String trailheadUrl : trailHeadUrls) {
            try {
                trailHeads.add(scrapeImmutableFetchedTrailhead(FOURTNEERERS_URL + trailheadUrl));
            } catch (Exception e) {
                LOG.warn("Unable to scrape " + trailheadUrl);
            }
        }
        return trailHeads;
    }

    public static ImmutableFetchedTrailhead scrapeImmutableFetchedTrailhead(String url) throws Exception {
        final HtmlPage page = webClient.getPage(url);
        final HtmlDivision pageTitle = (HtmlDivision) page.getByXPath(DIV_PAGE_TILE).get(0);
        final HtmlDivision statsBox = (HtmlDivision) page.getByXPath(DIV_STATS_BOX).get(0);
        final String[] statsBoxAsNormalizedText = statsBox.asNormalizedText().split("\n");
        return ImmutableFetchedTrailhead.builder()
                .name(scrapeName(pageTitle))
                .coordinates(scrapeCoordinates(statsBoxAsNormalizedText))
                .roadDifficulty(scrapeRoadDifficulty(statsBoxAsNormalizedText))
                .roadDescription(scrapeRoadDescription(statsBoxAsNormalizedText))
                .winterAccess(scrapeWinterAccess(statsBoxAsNormalizedText))
                .url(url)
                .build();
    }

    private static String scrapeName(HtmlDivision pageTitle) {
        return updateWithCorrectSqlSyntax(pageTitle.asNormalizedText());
    }

    private static String scrapeCoordinates(String[] statsBoxAsNormalizedText) {
        if (statsBoxContainsAllFields(statsBoxAsNormalizedText)) {
            return convertCoordinatesPhraseToCoordinates(statsBoxAsNormalizedText[2]);
        } else {
            return "";
        }
    }

    private static int scrapeRoadDifficulty(String[] statsBoxAsNormalizedText) {
        if (statsBoxContainsAllFields(statsBoxAsNormalizedText)) {
            return convertRoadDifficultyPhraseToInt(statsBoxAsNormalizedText[5]);
        } else {
            return convertRoadDifficultyPhraseToInt(statsBoxAsNormalizedText[3]);
        }
    }

    private static String scrapeRoadDescription(String[] statsBoxAsNormalizedText) {
        return updateWithCorrectSqlSyntax(statsBoxAsNormalizedText[6]);
    }

    private static String scrapeWinterAccess(String[] statsBoxAsNormalizedText) {
        if (statsBoxContainsAllFields(statsBoxAsNormalizedText)) {
            return updateWithCorrectSqlSyntax(statsBoxAsNormalizedText[10]);
        } else {
            return updateWithCorrectSqlSyntax(statsBoxAsNormalizedText[18]);
        }
    }

    private static boolean statsBoxContainsAllFields(String[] statsBoxAsNormalizedText) {
        return statsBoxAsNormalizedText.length == 11;
    }

    private static ArrayList<String> getTrailHeadUrlList() throws Exception {
        ArrayList<String> trailHeadUrls = new ArrayList<>();
        final HtmlPage page = webClient.getPage(TRAILHEADS_URL);
        final List<DomAttr> domList = page.getByXPath(SINGLE_RANGE_DIV);
        domList.forEach((domAttr) -> filterDomAttrValue(domAttr, trailHeadUrls));
        return trailHeadUrls;
    }

    private static void filterDomAttrValue(DomAttr domAttr, ArrayList<String> trailheadUrls) {
        if (domAttr.getValue().length() < 35) {
            trailheadUrls.add(domAttr.getValue().substring(1));
        }
    }

    private static String convertCoordinatesPhraseToCoordinates (String coordinatesPhrase) {
        return coordinatesPhrase.split(": ")[1].trim();
    }

    private static int convertRoadDifficultyPhraseToInt(String roadDifficultyPhrase) {
        return Integer.parseInt(roadDifficultyPhrase.split(" ")[1]);
    }

    private static String updateWithCorrectSqlSyntax(String str) {
        if (str.contains("'")) {
            return insertApostrophe(insertApostrophe(str));
        } else {
            return str;
        }
    }

    private static String insertApostrophe (String str) {
        String resultString = "";
        for (Character c : str.toCharArray()) {
            if (c.charValue() == 39) {
                resultString = resultString + "''";
            } else {
                resultString = resultString + c;
            }
        }
        return resultString;
    }
}
