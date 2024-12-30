package common.annotation;

import com.alibaba.fastjson2.JSONObject;
import com.shop.vo.common.Result;
import com.shop.vo.common.ResultCodeEnum;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class UserIdMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //从请求头中获取userId进行装配
        log.info("UserIdMethodArgumentResolver, 从请求头中获取userId进行装配");
        String userId = webRequest.getHeader("userId");
        if (userId != null){
            log.info("userId 注入成功");
            return Long.parseLong(userId);
        }else {
            log.info("userId为空 返回用户未登录");
            HttpServletResponse httpServletResponse = webRequest.getNativeResponse(HttpServletResponse.class);
            Result result = Result.build(null, ResultCodeEnum.GATEWAY_NO);
            byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
            httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write(bits);
            return null;
        }
    }
}
