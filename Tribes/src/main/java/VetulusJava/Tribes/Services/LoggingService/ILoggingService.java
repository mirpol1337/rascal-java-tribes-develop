package VetulusJava.Tribes.Services.LoggingService;

import VetulusJava.Tribes.Exceptions.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ILoggingService {
    void log(HttpServletRequest request, HttpServletResponse response, int executionTime);
    void logError(ErrorResponse error);
}
