package com.osp.bttp.common.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;

@Component
public class TokenHelper {

    @Value("${spring.application.name}")
    private String APP_NAME;

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expires_in}")
    private long EXPIRES_IN;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getUsernameFromToken(String token) {
        String username = "";
        try {
            final Claims claims = this.getClaimsFromToken(token);
           
            Date validTo = claims.getExpiration();
            Date requestTime = new Date();

            if (validTo.after(requestTime)) {
                //username = claims.getSubject();
                username = getClaimFromToken(token, "username").toString();
            }

        } catch (Exception e) {
            username = null;
        }
        return username;
    }
    public String getDeviceIdFromToken(String accessToken) {
        String deviceId = "";
        try {
            final Claims claims = this.getClaimsFromToken(accessToken);
            deviceId = claims.get("deviceAppId").toString();
        } catch (Exception e) {
            deviceId = null;
        }
        return deviceId;
    }
    public Object getClaimFromToken(String token, Object key) {
        Object objValue;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            objValue = claims.get(key);
        } catch (Exception e) {
            objValue = null;
        }
        return objValue;
    }

    public String generateTokenRefreshToken(String username) {
        Map<String, Object> mapClaims = new HashMap<>();
        mapClaims.put("username", username);
        String jws = Jwts.builder()
                .setClaims(mapClaims)
                .setIssuer(APP_NAME)
                .setSubject(username)
                .setIssuedAt(generateCurrentDate())
                .setExpiration(generateExpirationDateRefreshToken())
                .signWith(key(SECRET + "_refresh"))
                .compact();
        return jws;
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parser().setSigningKey(SECRET + "_refresh").build().parseClaimsJws(refreshToken);
            return true;
        } catch (MalformedJwtException ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage());
        }
        return false;
    }

    public Object getClaimFromRefreshToken(String token, Object key) {
        Object objValue;
        try {
            Claims claims;
            try {
                claims = Jwts.parser()
                        .verifyWith((SecretKey) key(SECRET+"_refresh"))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
            } catch (Exception e) {
                claims = null;
            }
            objValue = claims.get(key);
        } catch (Exception e) {
            objValue = null;
        }
        return objValue;
    }




    public String generateToken(String username, Map<String, Object> mapClaims) {
//        System.out.println("generateToken|generateExpirationDate="+generateExpirationDate());
        mapClaims.put("username", username);
        String jws = Jwts.builder()
                .setClaims(mapClaims)
                .setIssuer(APP_NAME)
                .setSubject(username)
                .setIssuedAt(generateCurrentDate())
                .setExpiration(generateExpirationDate())
                .signWith(key(SECRET))
                .compact();
        return jws;
    }

    public Key key(String sc) {
        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(sc.getBytes()));
    }

    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith((SecretKey) key(SECRET))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

//    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser().setSigningKey(token).build().parseSignedClaims(token).getBody();
//    }

    public Date getExpiredDateFromToken(String token) {
        try {
            final Claims claims = this.getClaimsFromToken(token);
            return claims.getExpiration();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private long getCurrentTimeMillis() {
        return new Date().getTime();
    }

    private Date generateCurrentDate() {
        return new Date();
    }

    private Date generateExpirationDate() {
        Date date = new Date(getCurrentTimeMillis() + this.EXPIRES_IN*100 );
        return date;
    }

    private Date generateExpirationDateRefreshToken() {
        //time exp = 1 year
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, 1);
        Date date = cal.getTime();
        return date;
    }

}
