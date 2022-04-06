package webscraper;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class FourteenerRouteScraperTest {

    @Test
    public void itConvertsElevationIntoInteger() {
        String str1 = "1200 feet";
        String str2 = "1200 ft";
        String str3 = "100 feet if you hike the road from Winfield";
        String str4 = "100 (starting at the end of the 4WD road)\n14.50 miles (starting at the paved TH)";
        String str5 = "500 meters";
        String str6 = "10,000 feet";
        String str7 = "10000 feet";
        String str8 = "10 feet";
        assertThat(FourteenerRouteScraper.convertElevationIntoInteger(str1)).isEqualTo(1200);
        assertThat(FourteenerRouteScraper.convertElevationIntoInteger(str2)).isEqualTo(1200);
        assertThat(FourteenerRouteScraper.convertElevationIntoInteger(str3)).isEqualTo(100);
        assertThat(FourteenerRouteScraper.convertElevationIntoInteger(str4)).isEqualTo(0);
        assertThat(FourteenerRouteScraper.convertElevationIntoInteger(str5)).isEqualTo(0);
        assertThat(FourteenerRouteScraper.convertElevationIntoInteger(str6)).isEqualTo(10000);
        assertThat(FourteenerRouteScraper.convertElevationIntoInteger(str7)).isEqualTo(10000);
        assertThat(FourteenerRouteScraper.convertElevationIntoInteger(str8)).isEqualTo(10);
    }

    @Test
    public void itConvertsRouteLengthIntoDouble() {
        String str1 = "12.00 miles";
        String str2 = "12.00 mi";
        String str3 = "10.75 miles if you hike the road from Winfield";
        String str4 = "10.50 miles (starting at the end of the 4WD road)\n14.50 miles (starting at the paved TH)";
        String str5 = "5 kilometers";
        assertThat(FourteenerRouteScraper.convertRouteLengthIntoDouble(str1)).isEqualTo(12.0);
        assertThat(FourteenerRouteScraper.convertRouteLengthIntoDouble(str2)).isEqualTo(12.0);
        assertThat(FourteenerRouteScraper.convertRouteLengthIntoDouble(str3)).isEqualTo(10.75);
        assertThat(FourteenerRouteScraper.convertRouteLengthIntoDouble(str4)).isEqualTo(0);
        assertThat(FourteenerRouteScraper.convertRouteLengthIntoDouble(str5)).isEqualTo(0);
    }

    @Test
    public void itUpdatesStringWithCorrectSqlSyntax() {
        assertThat(FourteenerRouteScraper.updateWithCorrectSqlSyntax("")).isEqualTo("");
        assertThat(FourteenerRouteScraper.updateWithCorrectSqlSyntax("abc")).isEqualTo("abc");
        assertThat(FourteenerRouteScraper.updateWithCorrectSqlSyntax("ab'c")).isEqualTo("ab''c");
    }

}
