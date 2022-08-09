package VetulusJava.Tribes.Logging;

import VetulusJava.Tribes.Services.LoggingService.ILoggingService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestInterceptor implements HandlerInterceptor {
    private long startTime;
    private long endTime;
    private int executionTime;
    ILoggingService loggingService;
    HttpServletRequest request;

    public RequestInterceptor(ILoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        startTime = System.currentTimeMillis();
        this.request = request;
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) {
        endTime = System.currentTimeMillis();
        executionTime = (int) (endTime - startTime);
        loggingService.log(this.request, response, executionTime);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
    }
}