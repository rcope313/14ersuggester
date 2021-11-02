package webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import models.Trailhead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class TrailheadScraper {

    final private static WebClient webClient = new WebClient();
    final private static Logger LOG = LoggerFactory.getLogger(TrailheadScraper.class);

    public static void main (String[] args) throws Exception {

        var trailheads = new TrailheadScraper().createListOfTrailheads();
        System.out.print(trailheads);


    }

    public ArrayList<Trailhead> createListOfTrailheads() throws Exception {

        ArrayList<String> trailHeadUrls = getTrailHeadUrlList();
        ArrayList<Trailhead> trailHeads = new ArrayList<>();
        int idx = 0;


        for (String url : trailHeadUrls) {

            try {
                trailHeads.add(new TrailheadScraper().scrapeTrailhead(url, idx));
                idx ++;
            } catch (Exception e) {
                LOG.warn("Unable to scrape " + url);
            }

        }


        return trailHeads;

    }

    private Trailhead scrapeTrailhead (String url, int idx) throws Exception {

        Trailhead resultTrailhead = new Trailhead();
        resultTrailhead.setPrimaryKey(idx);
        resultTrailhead.setUrl(url);


        final HtmlPage page = webClient.getPage("https://www.14ers.com/php14ers" + url);
        final HtmlDivision pageTitle = (HtmlDivision) page.getByXPath("//div[@class='pagetitle']").get(0);
        final HtmlDivision statsBox = (HtmlDivision) page.getByXPath("//div[@class='statsbox']").get(0);
        final String[] statsBoxAsNormalizedText = statsBox.asNormalizedText().split("\n");

        return scrapeTrailhead(statsBoxAsNormalizedText, pageTitle, resultTrailhead);
    }

    private Trailhead scrapeTrailhead(String[] statsBoxAsNormalizedText, HtmlDivision pageTitle, Trailhead resultTrailhead) {

        scrapeName(pageTitle.asNormalizedText(), resultTrailhead);

        if (statsBoxAsNormalizedText.length == 11) {
            scrapeCoordinates(statsBoxAsNormalizedText[2], resultTrailhead);
            scrapeRoadDifficulty(statsBoxAsNormalizedText[5], resultTrailhead);
            scrapeRoadDescription(statsBoxAsNormalizedText[6], resultTrailhead);
            scrapeWinterAccess(statsBoxAsNormalizedText[10], resultTrailhead);

        } else if (statsBoxAsNormalizedText.length == 9) {
            resultTrailhead.setCoordinates("");
            scrapeRoadDifficulty(statsBoxAsNormalizedText[3], resultTrailhead);
            scrapeRoadDescription(statsBoxAsNormalizedText[6], resultTrailhead);
            scrapeWinterAccess(statsBoxAsNormalizedText[8], resultTrailhead);


        }

        return resultTrailhead;

    }

    private void scrapeName(String str, Trailhead resultTrailhead) {
        resultTrailhead.setName(str);
    }

    private void scrapeCoordinates(String str, Trailhead resultTrailhead) {
        resultTrailhead.setCoordinates(Utils.convertCoordinatesPhraseToCoordinates(str));
    }

    private void scrapeRoadDifficulty(String str, Trailhead resultTrailhead) {
        resultTrailhead.setRoadDifficulty(Utils.convertRoadDifficultyPhraseToInt(str));
    }

    private void scrapeRoadDescription(String str, Trailhead resultTrailhead) {
        resultTrailhead.setRoadDescription(str);
    }

    private void scrapeWinterAccess(String str, Trailhead resultTrailhead) {
        resultTrailhead.setWinterAccess(str);
    }

    private ArrayList<String> getTrailHeadUrlList() throws Exception {

        ArrayList<String> trailHeadUrls = new ArrayList<>();

        final HtmlPage page = webClient.getPage("https://www.14ers.com/php14ers/trailheadsmain.php");
        final List<DomAttr> domList = page.getByXPath("//div[@class='singlerange']//a/@href");

        domList.forEach((domAttr) -> filterDomAttrValue(domAttr, trailHeadUrls));

        return trailHeadUrls;

    }

    private static void filterDomAttrValue (DomAttr domAttr, ArrayList<String> trailheadUrls) {
        if (domAttr.getValue().length() < 35) {
            trailheadUrls.add(domAttr.getValue().substring(1));
        }
    }

}
