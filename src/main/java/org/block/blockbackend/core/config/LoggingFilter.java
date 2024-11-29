package org.block.blockbackend.core.config;

import jakarta.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class LoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            // 요청 URL과 메서드 로깅
            logger.info("Request URL: {}", httpRequest.getRequestURL());
            logger.info("HTTP Method: {}", httpRequest.getMethod());

            // 요청 헤더와 파라미터 로깅
            httpRequest.getHeaderNames().asIterator()
                    .forEachRemaining(header -> logger.info("Header [{}]: {}", header, httpRequest.getHeader(header)));

            httpRequest.getParameterMap().forEach((key, values) ->
                    logger.info("Parameter [{}]: {}", key, String.join(", ", values)));
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
