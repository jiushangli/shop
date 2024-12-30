package common.filter;



import com.alibaba.fastjson2.JSONObject;
import com.shop.vo.common.Result;
import com.shop.vo.common.ResultCodeEnum;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Slf4j
@Component
public class GateWayFilter implements Filter{

    private String gateWayKey = "kdajfopdifauediafejnafdifu-90q849qjeru4q90uq9rje";
    //这里用了硬编码，解决不了无启动类导致的类似Value注解无法装配，读取不到配置文件的问题
    //todo 解决此处硬编码


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, IOException {
        log.info("GateWayFilter: 校验请求是否来自GateWay");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String gateWay = httpServletRequest.getHeader("x-forward-from-gateway");
        if (gateWayKey.equals(gateWay)){
            //验证成功，来自网关
            log.info("验证成功，请求来自网关");
            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            //todo 验证失败，请求不来自网关，直接返回
            Result result = Result.build(null, ResultCodeEnum.GATEWAY_NO);
            byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);

            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write(bits);
            //指定编码，否则在浏览器中会中文乱码
            return;

        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
