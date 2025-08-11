package com.osp.bttp.common.config.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

/**
 * @author sangnk
 * @Created 31/10/2024 - 8:57 SA
 * @project = bttp
 * @_ Mô tả:
 */
@Slf4j
@Component
public class HttpLoggingFilter extends OncePerRequestFilter {
    private static String X_FORWARDED_FOR = "X-Forwarded-For";

    private static String PROXY_CLIENT_IP = "Proxy-Client-IP";

    private static String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

    private static String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";

    private static String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";

    private static final String UNKNOWN = "unknown";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        RepeatableContentCachingRequestWrapper requestWrapper = new RepeatableContentCachingRequestWrapper(request);
//        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
//
//        logRequest(requestWrapper);
        filterChain.doFilter(request, response);
//        logResponse(responseWrapper);
    }

    private void logRequest(RepeatableContentCachingRequestWrapper requestWrapper) throws IOException {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        String ip = getRemoteIpFrom(requestWrapper);
        String body = requestWrapper.readInputAndDuplicate();
        String param = requestWrapper.getQueryString();
        String url = requestWrapper.getRequestURI() + (param != null ? "?" + param : "");
        String token = requestWrapper.getHeader("Authorization");
        String bodyLog = body;
        log.info("Request : \n requestId: {} \n ip: {} \n url: {} \n token: {} \n body: {}", requestId, ip, url, token, bodyLog);


    }

    private void logResponse(ContentCachingResponseWrapper responseWrapper) throws IOException {
//        log.info("Response {}", new String(responseWrapper.getContentAsByteArray()));
//        String body = new String(responseWrapper.getContentAsByteArray());
//        if(body.length() > 500){
//            body = body.substring(0, 200);
//            log.info("Response : \n requestId: {} \n response: {}", MDC.get("requestId"), body);
//        }else {
//            log.info("Response : \n requestId: {} \n response: {}", MDC.get("requestId"), body);
//        }
//        MDC.clear();
//        responseWrapper.copyBodyToResponse();

        byte[] responseArray=responseWrapper.getContentAsByteArray();
        String responseStr=new String(responseArray,responseWrapper.getCharacterEncoding());
        if (responseStr.length() > 500) {
            responseStr = responseStr.substring(0, 500);
        }
        log.info("Response : \n requestId: {} \n response: {}", MDC.get("requestId"), responseStr);
        MDC.clear();
        responseWrapper.copyBodyToResponse();

    }

    public static String getRemoteIpFrom(HttpServletRequest request) {
        String ip = null;
        int tryCount = 1;

        try {
            while (!isIpFound(ip) && tryCount <= 6) {
                switch (tryCount) {
                    case 1:
                        ip = request.getHeader(X_FORWARDED_FOR);
                        break;
                    case 2:
                        ip = request.getHeader(PROXY_CLIENT_IP);
                        break;
                    case 3:
                        ip = request.getHeader(WL_PROXY_CLIENT_IP);
                        break;
                    case 4:
                        ip = request.getHeader(HTTP_CLIENT_IP);
                        break;
                    case 5:
                        ip = request.getHeader(HTTP_X_FORWARDED_FOR);
                        break;
                    default:
                        ip = request.getRemoteAddr();
                }

                tryCount++;
            }
        } catch (Exception e) {
//            LogManager.getLogger(TokenAuthenticationFilter.class).error(e.getMessage());
            log.error(e.getMessage());
        }
        return ip;
    }

    private static boolean isIpFound(String ip) {
        return ip != null && ip.length() > 0 && !UNKNOWN.equalsIgnoreCase(ip);
    }
}