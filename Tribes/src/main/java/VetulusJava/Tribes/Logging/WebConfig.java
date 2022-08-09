package VetulusJava.Tribes.Logging;

import VetulusJava.Tribes.Services.LoggingService.ILoggingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = {"VetulusJava.Tribes.Logging"})
public class WebConfig implements WebMvcConfigurer {
    ILoggingService loggingService;

    public WebConfig(ILoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @Bean
    public RequestInterceptor getRequestInterceptor(ILoggingService loggingService) {
        RequestInterceptor interceptor = new RequestInterceptor(loggingService);
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getRequestInterceptor(loggingService));
    }
}