package eqlee.ctm.resource.jwt;

import eqlee.ctm.resource.jwt.entity.UserLoginQuery;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @Author qf
 * @Date 2019/9/25
 * @Version 1.0
 */
public class JwtVerfy {

    /**
     * 校验token
     * 在这里可以使用官方的校验，我这里校验的是token中携带的密码于数据库一致的话就校验通过
     * @param token
     * @param user
     * @return
     */
    public static Boolean isVerify(String token, UserLoginQuery user) {
        //签名秘钥，和生成的签名的秘钥一模一样
        String key = user.getPassword();

        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(key)
                //设置需要解析的jwt
                .parseClaimsJws(token).getBody();

        String Id = "Id";

        if (claims.get(Id).equals(user.getId())) {
            return true;
        }

        return false;
    }
}
