package eqlee.ctm.user.jwt.Interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import eqlee.ctm.user.jwt.JwtVerfy;
import eqlee.ctm.user.jwt.contain.LocalUser;
import eqlee.ctm.user.jwt.entity.PrivilegeMenuQuery;
import eqlee.ctm.user.jwt.entity.UserLoginQuery;
import eqlee.ctm.user.jwt.islogin.CheckToken;
import eqlee.ctm.user.jwt.islogin.LoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * @Author qf
 * @Date 2019/9/24
 * @Version 1.0
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private LocalUser localUser;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {

        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有LoginToken注释，有则跳过认证
        if (method.isAnnotationPresent(LoginToken.class)) {
            LoginToken loginToken = method.getAnnotation(LoginToken.class);
            if (loginToken.required()) {
                return true;
            }
        }

        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(CheckToken.class)) {
            CheckToken checkToken = method.getAnnotation(CheckToken.class);
            if (checkToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("无token,请重新登录");
                }
                // 获取 token 中的 user信息
                UserLoginQuery query = new UserLoginQuery();
                try {
                    DecodedJWT decode = JWT.decode(token);
                    query.setId(decode.getClaim("Id").asLong());
                    query.setAccount(decode.getClaim("UserName").asString());
                    query.setCName(decode.getClaim("CName").asString());
                    query.setTel(decode.getClaim("Tel").asString());
                    query.setCompanyId(decode.getClaim("CompanyId").asLong());
                    query.setMenuList(decode.getClaim("menuList").asList(PrivilegeMenuQuery.class));
                    query.setPassword(decode.getClaim("Password").asString());
                    query.setRoleName(decode.getClaim("roleName").asString());

                } catch (JWTDecodeException j) {
                    throw new RuntimeException("JWT解码失败");
                }
                localUser.setUser(query);
                Boolean verify = JwtVerfy.isVerify(token, query);
                if (!verify) {
                    throw new RuntimeException("身份验证错误");
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
