package subcommands;

import org.junit.Before;
import org.junit.Test;
import picocli.CommandLine;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class CompareSubCommandTest {
    int exitCode1, exitCode2;
    CommandLine cmd1, cmd2;
    CompareSubCommand mockApp1, mockApp2;

    @Before
    public void initData() {
        mockApp1 = mock(CompareSubCommand.class);
        cmd1 = new CommandLine(mockApp1);
        exitCode1 = cmd1.execute("-m1", "Pike's Peak", "-m2", "Long's Peak", "-r1", "Barr Trail", "-r2", "KeyHole Route");

        mockApp2 = mock(CompareSubCommand.class);
        cmd2 = new CommandLine(mockApp2);
        exitCode2 = cmd2.execute("--url", "https://www.14ers.com/route.php?route=elbe1", "https://www.14ers.com/route.php?route=mass1");
    }

    @Test
    public void acceptanceTest() {
        CommandLine cmd = new CommandLine(new CompareSubCommand());
        exitCode2 = cmd.execute("--url", "https://www.14ers.com/route.php?route=elbe1", "https://www.14ers.com/route.php?route=mass1");cmd2 = new CommandLine(mockApp2);
    }

    @Test
    public void itInitializesClassFields() {
        assertThat(mockApp1.routeName1).usingRecursiveComparison().isEqualTo(Optional.of("Barr Trail"));
        assertThat(mockApp1.routeName2).usingRecursiveComparison().isEqualTo(Optional.of("KeyHole Route"));
        assertThat(mockApp1.mountainName1).usingRecursiveComparison().isEqualTo(Optional.of("Pike's Peak"));
        assertThat(mockApp1.mountainName2).usingRecursiveComparison().isEqualTo(Optional.of("Long's Peak"));

        assertThat(mockApp2.routeUrls).usingRecursiveComparison().isEqualTo(new String[]{"https://www.14ers.com/route.php?route=elbe1", "https://www.14ers.com/route.php?route=mass1"});
    }
}
