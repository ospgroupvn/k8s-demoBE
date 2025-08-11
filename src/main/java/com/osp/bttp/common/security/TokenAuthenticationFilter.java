package com.osp.bttp.common.security;


import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.common.utils.UtilsHttp;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.repository.db3.AccUserRepository;
import com.osp.bttp.dao.service.AccUserService;
import com.osp.bttp.dao.service.GroupService;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * TODO: write you class description here
 *
 * @author
 */
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter implements Ordered {

    private static String AUTHORIZATION = "Authorization";

    private static String AUTHENTICATION_TYPE_BASIC = "Basic";

    private static String X_AUTH_TOKEN = "X-AUTH-TOKEN";

    private static String WWW_Authenticate = "WWW-Authenticate";

    private static String X_FORWARDED_FOR = "X-Forwarded-For";

    private static String PROXY_CLIENT_IP = "Proxy-Client-IP";

    private static String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

    private static String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";

    private static String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";

    private static final String UNKNOWN = "unknown";


    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private AccUserRepository accUserRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private AccUserService accUserService;

    @Autowired
    private RedisTemplate<Serializable, Object> redisTemplate;
    @Value("${redis.enable}")
    private boolean redisEnable;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE - 1;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        boolean check = StringUtils.startsWithAny(path, Constants.NO_TOKEN_WHITELIST);
        return check;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        log.info("URL: " + request.getRequestURI());
        String accessToken = UtilsHttp.getToken(request);
        if (accessToken != null) {
            String username = tokenHelper.getUsernameFromToken(accessToken);
            Date expiredToken = tokenHelper.getExpiredDateFromToken(accessToken);
            if (!H.isTrue(expiredToken) || expiredToken.getTime() - new Date().getTime() <= 0) {
                response.setStatus(419);
                return;
            }
            String userType = tokenHelper.getClaimFromToken(accessToken, "usertype") + "";

            if (StringUtils.isBlank(username)) {
                throw new AccessDeniedException("Invalid token");
            }
            Optional<AccUser> khUserOptian = accUserRepository.findByUsernameNew(username);
            if (!khUserOptian.isPresent() || !khUserOptian.get().getStatus().equals(Constants.ACCOUNT_OK)) {
                response.setStatus(401);
                return;
            }
            AccUser khUser = khUserOptian.get();
            if (khUser != null) {
                if (khUser.isEnabled()) {
                    List<GrantedAuthority> authorities = new ArrayList<>();


                    // Kiểm tra sự tồn tại của khóa trong Redis cache
                    String cacheKey = Constants.PARAMETER.REDIS_KEY_AUTHORITY_ALL + username;
                    //check has key and value.size > 0
                    if ( redisEnable && redisTemplate.hasKey(cacheKey) && ((List<String>) redisTemplate.opsForValue().get(cacheKey)).size() > 0) {
                        List<String> list = (List<String>) redisTemplate.opsForValue().get(cacheKey);
                        if (list != null && list.size() > 0) {
                            for (String authority : list) {
                                authorities.add(new SimpleGrantedAuthority(authority));
                            }
                        }
                    } else {
                        List<String> list = this.groupService.loadListAuthorityOfUserByUserIdAll(khUser.getId());
                        if (list != null && list.size() > 0) {
                            for (String authority : list) {
                                authorities.add(new SimpleGrantedAuthority(authority));
                            }
                        }
                        if ( redisEnable ) redisTemplate.opsForValue().set(cacheKey, list);
                        if ( redisEnable ) redisTemplate.expire(cacheKey, 10, TimeUnit.MINUTES);
                    }



                    khUser.setGrantedAuths(authorities);
                    TokenBasedAuthentication authentication = new TokenBasedAuthentication(khUser, true);
                    authentication.setToken(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
//                                throw new AccessDeniedException("Invalid token");
                    response.setStatus(200);
                    String messageJson = "{\"message\":\"The account has been delete or disable\",\"code\":\"150\",\"data\":null}";
                    PrintWriter out = response.getWriter();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    out.print(messageJson);
                    out.flush();
                    return;
                }
            } else {
                response.setStatus(200);
                String messageJson = "{\"message\":\"Tài khoản không tồn tại\",\"code\":\"150\",\"data\":null}";
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(messageJson);
                out.flush();
                return;
            }
        } else {
//            throw new AccessDeniedException("No token input");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("No token input");
            return;
        }

        chain.doFilter(request, response);
    }

    //--------------------------------------------HELPER----------------------------------------------------------------
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
