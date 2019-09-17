package com.yq.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {
    /**
     * 密钥
     */
    private static final String secret = "Svpu9MogFMk4NRpN1ZMrJNaXAOckyGegdWDDimoGuFYoPuxnJtIlFKpW9bFPdAvV";

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private static String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + (3600 * 24 * 7 * 1000));
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 生成令牌
     *
     * @param token token
     * @return 令牌
     */
    public static String generateToken(String token) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put("sub", token);
        claims.put("created", new Date());
        return generateToken(claims);
    }

    /**
     * 从令牌中获取token
     *
     * @param token 令牌
     * @return token
     */
    public static String getTokenFromJWT(String token) {
        String val;
        try {
            Claims claims = getClaimsFromToken(token);
            val = claims.getSubject();
        } catch (Exception e) {
            val = null;
        }
        return val;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public static Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取过期时间
     *
     * @param token
     * @return
     */
    public static Date getExpiration(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public static String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put("created", new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

}
