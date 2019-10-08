package com.yq.jwt.Interceptor;

import com.yq.jwt.entity.UserLoginQuery;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @Author qf
 * @Date 2019/10/1
 * @Version 1.0
 */
public class JwtParseUtil {

    /**
     * Token的解密
     * @param token 加密后的token
     * @param user  用户的对象
     * @return
     */
    public static Claims parseJWT(String token, UserLoginQuery user) {
        //签名秘钥，和生成的签名的秘钥一模一样
        String key = user.getPassword();

        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(key)
                //设置需要解析的jwt
                .parseClaimsJws(token).getBody();
        return claims;
    }
}
