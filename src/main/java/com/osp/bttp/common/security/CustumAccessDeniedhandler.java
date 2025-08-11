//package com.osp.bttp.common.security;
//
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
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
//public class CustumAccessDeniedhandler implements AccessDeniedHandler {
//    // trả về trang 401 nếu sử dụng spring web mvc
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        // ObjectMapper mapper = new ObjectMapper();
//        // response.setContentType("application/json;charset=UTF-8");
//        // response.setStatus(403);
//        // response.getWriter().write(
//        //         mapper.writeValueAsString(new org.json.JSONObject()
//        //         .put("timestamp", System.currentTimeMillis())
//        //         .put("status", 403)
//        //         .put("message", "Access denied"))
//        // );
//        response.sendRedirect("401");
//    }
//}
