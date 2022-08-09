package VetulusJava.Tribes.Services;


import VetulusJava.Tribes.Entities.Log;
import VetulusJava.Tribes.Exceptions.ErrorResponse;
import VetulusJava.Tribes.Repositories.ILogRepository;
import VetulusJava.Tribes.Services.LoggingService.ILoggingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class LoggingServiceTest {

    @Autowired
    ILoggingService loggingService;

    @Autowired
    ILogRepository logRepository;

    @Test
    public void logErrorTest(){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, "Test message");
        loggingService.logError(errorResponse);
        var actualLogs = logRepository.findAll();
        boolean correctLogExists = false;
        for (Log log: actualLogs){
            if (log.errorStatus.equals(HttpStatus.CONFLICT) && log.errorMessage.equals("Test message")){
                correctLogExists = true;
            }
        }
        assertEquals(true, correctLogExists);
    }

    @Test
    public void LogTest(){
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        loggingService.log(mockRequest, mockResponse, 123456);
        var actualLogs = logRepository.findAll();
        boolean correctLogExists = false;
        for (Log log: actualLogs){
            if (log.executionTime == 123456){
                correctLogExists = true;
            }
        }
        assertEquals(true, correctLogExists);
    }

}
