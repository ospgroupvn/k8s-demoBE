//package com.osp.bttp.common.security;
//
//import com.osp.bttp.common.exception.Result;
//import com.osp.bttp.common.utils.UtilsHttp;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * TODO: write you class description here
// *
// * @author
// */
//
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    @Autowired
//    private UtilsHttp utilsHttp;
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        // ObjectMapper mapper = new ObjectMapper();
//        // response.setContentType("application/json;charset=UTF-8");
//        // response.setStatus(401);
//        // response.getWriter().write(
//        //         mapper.writeValueAsString(new org.json.JSONObject()
//        //                 .put("timestamp", System.currentTimeMillis())
//        //                 .put("status", 401)
//        //                 .put("message", "Access denied"))
//        // );
//        utilsHttp.handleResponse(HttpServletResponse.SC_UNAUTHORIZED, Result.UNAUTHORIZED, response, authException);
//    }
//}
