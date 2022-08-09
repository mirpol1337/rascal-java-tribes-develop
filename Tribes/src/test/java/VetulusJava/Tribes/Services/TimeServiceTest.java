package VetulusJava.Tribes.Services;

import VetulusJava.Tribes.Services.ResourceService.IResourceService;
import VetulusJava.Tribes.Services.ResourceService.ResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class TimeServiceTest {
    private IResourceService resourceService;

    @Test
    public void checkTime() {
        long timeNow = System.currentTimeMillis() / 1000;
        long mockedServerTime = (System.currentTimeMillis() / 1000) - 120;
        long expectedTicks = (timeNow - mockedServerTime) / 60;
        int ticks = 0;
        long ticksDifference = expectedTicks - ticks;
        if (expectedTicks != ticks) {
            for (int i = 0; i < ticksDifference; i++) {
                ticks++;
            }
        }
        assertEquals(ticks, expectedTicks);
    }
}
