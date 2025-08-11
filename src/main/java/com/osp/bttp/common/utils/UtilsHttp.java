package com.osp.bttp.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.exception.Result;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Service
public class UtilsHttp {

    public void handleResponse(int status, Result result, HttpServletResponse response, Exception exception) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(status);
        Map<String ,Object > rsp =new HashMap<>();
        rsp.put("timestamp", System.currentTimeMillis());
        rsp.put("status", status);
        rsp.put("message", exception.getMessage());
        String body = mapper.writeValueAsString(new ApiResponseV1<>(false, result.getCode(), rsp, result));
        response.getWriter().write(body);
        response.getWriter().flush();
        response.getWriter().close();
    }


    public static void getAllParamsReq(HttpServletRequest request, HttpServletResponse response) {
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];
            }
        }
    }

    public static HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }


    public static String getToken(HttpServletRequest request) {
        String accessToken = request.getHeader(Constants.HEADER_FIELD.AUTHORIZATION);
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            return accessToken.substring(7);
        }
        return null;
    }

    public static String getSecToken(HttpServletRequest request) {
        String accessToken = request.getHeader(Constants.HEADER_FIELD.AUTHORIZATION);
        if (accessToken != null && accessToken.startsWith("Basic ")) {
            return accessToken.substring(6);
        }
        return null;
    }

    public static String getIpClient(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }

}
