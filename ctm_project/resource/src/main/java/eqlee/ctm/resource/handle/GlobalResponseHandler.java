package eqlee.ctm.resource.handle;

import com.yq.anntation.IgnoreResponseAdvice;
import com.yq.data.Result;
import eqlee.ctm.resource.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局统一返回
 * @Author qf
 * @Date 2019/8/31
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice {


    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        if (methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class) ||
                aClass.isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        return true;
    }

    @Override
    public Result beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof ApplicationException) {
            ApplicationException exception = (ApplicationException) o;
            Result result = new Result();
            result.setCode(exception.getCode());
            result.setMsg(exception.getMsg());

            return result;
        }

        return Result.SUCCESS(o);
    }
}
