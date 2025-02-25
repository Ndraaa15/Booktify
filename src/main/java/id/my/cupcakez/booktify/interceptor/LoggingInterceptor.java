package id.my.cupcakez.booktify.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LogManager.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        UUID requestId = UUID.randomUUID();

        logger.info("Request URI: {} | Method: {} | Start Time: {} | IP: {} | Request ID: {} ", request.getRequestURI(), request.getMethod(), startTime, request.getRemoteAddr(), requestId);

        MDC.put("requestStartTime", String.valueOf(startTime));
        MDC.put("requestId", requestId.toString());
        MDC.put("requestUri", request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception{

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long endTime = System.currentTimeMillis();
        long duration = endTime - Long.parseLong(MDC.get("requestStartTime"));

        logger.info("Response Status: {} | Request URI: {} | Duration: {} ms | Request ID: {}", response.getStatus(), request.getRequestURI(), duration, MDC.get("requestId"));

        MDC.clear();
    }
}
