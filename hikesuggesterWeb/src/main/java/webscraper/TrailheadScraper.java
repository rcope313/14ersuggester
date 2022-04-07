package webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import database.models.ImmutableFetchedRoute;
import database.models.ImmutableFetchedTrailhead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrailheadScraper {
    final private WebClient webClient;
    final private static Logger LOG = LoggerFactory.getLogger(TrailheadScraper.class);
    final private static String FOURTNEERERS_URL = "https://www.14ers.com/php14ers";
    final private static String TRAILHEADS_URL = "https://www.14ers.com/php14ers/trailheadsmain.php";
    final private static String SINGLE_RANGE_DIV = "//div[@class='singlerange']//a/@href";
    final private static String TRAILHEAD_NAME_XPATH = "//*[@id='wrap']/div[3]/div";
    final private static String COORDINATES_NAME_XPATH = "//*[@id='wrap']/div[6]/div[2]/div[1]/a[1]";
    final private static String ROAD_DIFFICULTY_XPATH = "//*[@id='wrap']/div[6]/div[3]/span[1]";
    final private static String ROAD_DESCRIPTION_XPATH = "//*[@id='wrap']/div[6]/div[3]";
    final private static String WINTER_ACCESS_XPATH = "//*[@id='wrap']/div[6]/div[5]";

    public TrailheadScraper(WebClient webClient) {
        this.webClient = webClient;
    }

    public ArrayList<Optional<ImmutableFetchedTrailhead>> buildAllImmutableFetchedTrailheads() throws Exception {
        ArrayList<String> trailHeadUrls = getTrailHeadUrlList();
        ArrayList<Optional<ImmutableFetchedTrailhead>> trailHeads = new ArrayList<>();
        for (String trailheadUrl : trailHeadUrls) {
            try {
                trailHeads.add(scrapeImmutableFetchedTrailhead(FOURTNEERERS_URL + trailheadUrl));
                LOG.info("Able to scrape {}", trailheadUrl);
            } catch (Exception e) {
                LOG.warn("Unable to scrape" +  trailheadUrl + " by exception {}", e.getMessage());
            }
        }
        return trailHeads;
    }

    public Optional<ImmutableFetchedTrailhead> scrapeImmutableFetchedTrailhead(String url) throws Exception {
        final HtmlPage page = webClient.getPage(url);
        Optional<ImmutableFetchedTrailhead> trailhead = Optional.empty();
        try {
           trailhead = Optional.of(ImmutableFetchedTrailhead.builder()
                    .name(scrapeName(page))
                    .coordinates(scrapeCoordinates(page))
                    .roadDifficulty(scrapeRoadDifficulty(page))
                    .roadDescription(scrapeRoadDescription(page))
                    .winterAccess(scrapeWinterAccess(page))
                    .url(url)
                    .build());
            LOG.info("Able to scrape {}", url);
        } catch (Exception e) {
            LOG.warn("Unable to scrape" +  url + " by exception {}", e.getMessage());
        }
        return trailhead;
    }

    private static String scrapeName(HtmlPage page) {
        var div = (HtmlDivision) page.getByXPath(TRAILHEAD_NAME_XPATH).get(0);
        return FourteenerRouteScraper.updateWithCorrectSqlSyntax(div.asNormalizedText());
    }

    private static String scrapeCoordinates(HtmlPage page) {
        List<HtmlAnchor> anchorList = page.getByXPath(COORDINATES_NAME_XPATH);
        if (anchorList.size() == 1) {
            return anchorList.get(0).asNormalizedText();
        } else {
            return "";
        }
    }

    private static int scrapeRoadDifficulty(HtmlPage page) {
        List<HtmlSpan> spanList = page.getByXPath(ROAD_DIFFICULTY_XPATH);
        if (spanList.size() == 1) {
            return Integer.parseInt(spanList.get(0).asNormalizedText());
        } else {
            return 0;
        }
    }

    private static String scrapeRoadDescription(HtmlPage page) {
        List<HtmlDivision> divList = page.getByXPath(ROAD_DESCRIPTION_XPATH);
        if (divList.size() == 1) {
            return FourteenerRouteScraper.updateWithCorrectSqlSyntax(divList.get(0).asNormalizedText().split("\n")[1]);
        } else {
            return "";
        }
    }

    private static String scrapeWinterAccess(HtmlPage page) {
        List<HtmlDivision> divList = page.getByXPath(WINTER_ACCESS_XPATH);
        if (divList.size() == 1) {
            return FourteenerRouteScraper.updateWithCorrectSqlSyntax(divList.get(0).asNormalizedText());
        } else {
            return "";
        }
    }

    private ArrayList<String> getTrailHeadUrlList() throws Exception {
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
}
