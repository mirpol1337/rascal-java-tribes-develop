package VetulusJava.Tribes.Services.LoggingService;

import VetulusJava.Tribes.Entities.Log;
import VetulusJava.Tribes.Exceptions.ErrorResponse;
import VetulusJava.Tribes.Repositories.ILogRepository;
import org.h2.message.DbException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LoggingService implements ILoggingService {

    ILogRepository logRepository;
    private Environment env;

    public LoggingService(ILogRepository logRepository, Environment env) {
        this.logRepository = logRepository;
        this.env = env;
    }

    public void logError(ErrorResponse error) {
        Log log = new Log();

        String logLvl = env.getProperty("TRIBES_LOG_LVL");
        log.loggingLvl = logLvl;
        log.created = LocalDateTime.now();
        log.errorStatus = error.getStatus();
        log.errorMessage= error.getMessage();
        saveLog(log);
    }

    public void log(HttpServletRequest request, HttpServletResponse response, int executionTime) {
        Log log = new Log();
        String logLvl = env.getProperty("TRIBES_LOG_LVL");
        if(logLvl.equals("error")){
            return;
        }
        log.loggingLvl = logLvl;
        log.created = LocalDateTime.now();
        log.executionTime = executionTime;
        log.requestMethod = request.getMethod();
        log.responseStatus = response.getStatus();
        if (logLvl.equals("debug")){
                log.requestString = getRequestString(request);
                log.responseString = getResponseString(response, "");
        }
        saveLog(log);
    }

    private void saveLog(Log log) {
        try{
            logRepository.save(log);
        }
        catch (DbException ex)
        {
            try {
                logToFile(log.toString());
            }
            catch (IOException e) {

            }
        }
    }

    private void logToFile(String log) throws IOException {
        File file = new File("logs.txt");
        if (file.createNewFile()) {
            this.logToFile("logs.txt created");
        }
        FileWriter writer = new FileWriter("logs.txt", true);
        writer.write(log + "\n");
        writer.close();
    }

    private String getRequestString(HttpServletRequest request) {
        var stringBuilder = new StringBuilder();
        var parameters = buildParametersMap(request);
        String body = null;
        try {
            body = request.getReader().lines().collect(Collectors.joining());
        } catch (Exception e) {
            body= "body can't be accessed";
        }

        stringBuilder.append("REQUEST ");
        stringBuilder.append("method=[").append(request.getMethod()).append("] ");
        stringBuilder.append("path=[").append(request.getRequestURI()).append("] ");
        stringBuilder.append("headers=[").append(buildHeadersMap(request)).append("] ");

        if (!parameters.isEmpty()) {
            stringBuilder.append("parameters=[").append(parameters).append("] ");
        }

        if (body != "") {
            stringBuilder.append("body=[" + body + "]");
        }

        return stringBuilder.toString();
    }

    private String getResponseString(HttpServletResponse response, Object body) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("RESPONSE ");
        stringBuilder.append("responseHeaders=[").append(buildHeadersMap(response)).append("] ");
        stringBuilder.append("responseBody=[").append(body).append("] ");

        return stringBuilder.toString();
    }

    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    private Map<String, String> buildHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    private Map<String, String> buildHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }

        return map;
    }

}