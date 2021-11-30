package utility;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class UtilityTest {

    @Test
    public void withAssertions_examples() {

        // assertThat methods come from WithAssertions - no static import needed
        assertThat(33).isEqualTo(33);
        assertThat("Frodo").isEqualTo("Frodo").isNotEqualTo("Frodon");

    }
}
