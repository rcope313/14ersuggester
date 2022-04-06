package subcommands;

import org.junit.Before;
import org.junit.Test;
import picocli.CommandLine;
import java.util.Optional;
import java.util.logging.Level;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class SuggestSubCommandTest {
    int exitCode1, exitCode2, exitCode3, exitCode4, exitCode5, exitCode6, exitCode7, exitCode8, exitCode9;
    CommandLine cmd1, cmd2, cmd3, cmd4, cmd5, cmd6, cmd7, cmd8, cmd9;
    SuggestSubCommand mockApp1, mockApp2, mockApp3, mockApp4, mockApp5, mockApp6, mockApp7, mockApp8, mockApp9;

    @Before
    public void initData() {
        mockApp1 = mock(SuggestSubCommand.class);
        cmd1 = new CommandLine(mockApp1);
        exitCode1 = cmd1.execute();

        mockApp2 = mock(SuggestSubCommand.class);
        cmd2 = new CommandLine(mockApp2);
        exitCode2 = cmd2.execute("--m", "Mt. Elbert", "--m", "Longs Peak", "--m", "Mt. Antero", "--st");

        mockApp3 = mock(SuggestSubCommand.class);
        cmd3 = new CommandLine(mockApp3);
        exitCode3 = cmd3.execute( "--m", "Mt. Elbert", "--m", "Longs Peak", "--m", "Mt. Antero", "--tg", "5000", "--rl", "4.5");

        mockApp4 = mock(SuggestSubCommand.class);
        cmd4 = new CommandLine(mockApp4);
        exitCode4 = cmd4.execute("--sn", "--r", "Box Creek Couloirs", "--r", "South Face");

        mockApp5 = mock(SuggestSubCommand.class);
        cmd5 = new CommandLine(mockApp5);
        exitCode5 = cmd5.execute("--ste", "8000", "--sue", "14000");

        mockApp6 = mock(SuggestSubCommand.class);
        cmd6 = new CommandLine(mockApp6);
        exitCode6 = cmd6.execute("-exposure", "low", "-routefinding", "low", "-rockfallpotential", "low", "-commitment", "low");

        mockApp7 = mock(SuggestSubCommand.class);
        cmd7 = new CommandLine(mockApp7);
        exitCode7 = cmd7.execute("--g", "1", "--g", "2", "--g", "3", "--gq", "Easy", "--gq", "Difficult");

        mockApp8 = mock(SuggestSubCommand.class);
        cmd8 = new CommandLine(mockApp8);
        exitCode8 = cmd8.execute( "-roaddifficulties", "1", "-roaddifficulties", "2", "-roaddifficulties", "3" );

        mockApp9 = mock(SuggestSubCommand.class);
        cmd9 = new CommandLine(mockApp9);
        exitCode9 = cmd9.execute("-routeurls", "https://www.14ers.com/route.php?route=elbe1", "-routeurls", "https://www.14ers.com/route.php?route=elbe2", "-trailheadurls", "/trailheadsview.php?thparm=sw01", "-trailheadurls", "/trailheadsview.php?thparm=sw03");
    }

    @Test
    public void acceptanceTest() {

        CommandLine cmd = new CommandLine(new SearchSubCommand());
        int exitCode = cmd.execute();
    }

    @Test
    public void itInitializesClassFields() {
        assertThat(mockApp2.mountainNames).usingRecursiveComparison().isEqualTo(new String[]{"Mt. Elbert", "Longs Peak", "Mt. Antero"});
        assertThat(mockApp2.isStandardRoute).usingRecursiveComparison().isEqualTo(Optional.of(true));

        assertThat(mockApp3.mountainNames).usingRecursiveComparison().isEqualTo(new String[]{"Mt. Elbert", "Longs Peak", "Mt. Antero"});
        assertThat(mockApp3.totalGain).usingRecursiveComparison().isEqualTo(Optional.of(5000));
        assertThat(mockApp3.routeLength).usingRecursiveComparison().isEqualTo(Optional.of(4.5));

        assertThat(mockApp4.routeNames).usingRecursiveComparison().isEqualTo(new String[]{"Box Creek Couloirs", "South Face"});
        assertThat(mockApp4.isSnowRoute).usingRecursiveComparison().isEqualTo(Optional.of(true));

        assertThat(mockApp5.startElevation).usingRecursiveComparison().isEqualTo(Optional.of(8000));
        assertThat(mockApp5.summitElevation).usingRecursiveComparison().isEqualTo(Optional.of(14000));

        assertThat(mockApp6.exposure).usingRecursiveComparison().isEqualTo(Optional.of("low"));
        assertThat(mockApp6.routeFinding).usingRecursiveComparison().isEqualTo(Optional.of("low"));
        assertThat(mockApp6.rockfallPotential).usingRecursiveComparison().isEqualTo(Optional.of("low"));
        assertThat(mockApp6.commitment).usingRecursiveComparison().isEqualTo(Optional.of("low"));

        assertThat(mockApp7.grades).usingRecursiveComparison().isEqualTo(new int[]{1,2,3});
        assertThat(mockApp7.gradeQualities).usingRecursiveComparison().isEqualTo(new String[]{"Easy", "Difficult"});

        assertThat(mockApp8.roadDifficulties).usingRecursiveComparison().isEqualTo(new int[]{1,2,3});

        assertThat(mockApp9.trailheadUrls).usingRecursiveComparison().isEqualTo(new String[]{"/trailheadsview.php?thparm=sw01", "/trailheadsview.php?thparm=sw03"});
        assertThat(mockApp9.routeUrls).usingRecursiveComparison().isEqualTo(new String[]{"https://www.14ers.com/route.php?route=elbe1", "https://www.14ers.com/route.php?route=elbe2"});
    }
}
