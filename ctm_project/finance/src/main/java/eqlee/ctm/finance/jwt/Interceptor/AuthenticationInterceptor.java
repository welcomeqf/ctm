package eqlee.ctm.finance.jwt.Interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.yq.constanct.CodeType;
import eqlee.ctm.finance.exception.ApplicationException;
import eqlee.ctm.finance.jwt.JwtVerfy;
import eqlee.ctm.finance.jwt.contain.LocalUser;
import eqlee.ctm.finance.jwt.entity.PrivilegeMenuQuery;
import eqlee.ctm.finance.jwt.entity.UserLoginQuery;
import eqlee.ctm.finance.jwt.islogin.CheckToken;
import eqlee.ctm.finance.jwt.islogin.LoginToken;
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
    private LocalUser user;

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
                    query.setId(JWT.decode(token).getClaim("Id").asLong());
                    query.setAccount(JWT.decode(token).getClaim("UserName").asString());
                    query.setCName(JWT.decode(token).getClaim("CName").asString());
                    query.setCompanyId(JWT.decode(token).getClaim("CompanyId").asLong());
                    query.setMenuList(JWT.decode(token).getClaim("menuList").asList(PrivilegeMenuQuery.class));
                    query.setPassword(JWT.decode(token).getClaim("Password").asString());
                    query.setRoleName(JWT.decode(token).getClaim("roleName").asString());
                    query.setTel(JWT.decode(token).getClaim("Tel").asString());

                } catch (JWTDecodeException j) {
                    throw new ApplicationException(CodeType.DATABASE_ERROR,"JWT解码失败");
                }
                user.setUser(query);
                Boolean verify = JwtVerfy.isVerify(token, query);
                if (!verify) {
                    throw new ApplicationException(CodeType.AUTHENTICATION_ERROR,"身份验证错误,请重新登录");
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
