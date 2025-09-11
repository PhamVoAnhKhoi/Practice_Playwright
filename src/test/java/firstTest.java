import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class firstTest {

    @Test
    public void helloWorld(){
        String message = "Hello World";
        assertThat(message).isEqualTo("Hello World");
    }
}
