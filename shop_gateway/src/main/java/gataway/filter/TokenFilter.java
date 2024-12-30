package gataway.filter;


import com.alibaba.fastjson2.JSONObject;
import com.shop.vo.common.Result;
import com.shop.vo.common.ResultCodeEnum;
import gataway.common.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
@Order(-1)
public class TokenFilter implements GlobalFilter {

    @Value("${jwt.key}")
    private String key;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取头部token
        ServerHttpRequest request = exchange.getRequest();
        List<String> tokens = request.getHeaders().get("token");
        String path = request.getURI().getPath();

        //没有token
        if (tokens == null){
            //从路径验证是否不需要登录
            if (antPathMatcher.match("/api/**/auth/**", path)){//需要登录但是没有登录
                log.info("需要登录但是没有登录");
                return out(exchange.getResponse(), ResultCodeEnum.LOGIN_AUTH);
                //test
//                return chain.filter(exchange);

            }else {
                log.info("测试login不需要校验token");
                return chain.filter(exchange);
            }
        }else {
            String token = tokens.get(0);
            //解析token
            Long aLong = JWTUtils.verifyJWT(token, key);
            if (aLong == -1L){
                //解析失败
                log.info("token解析失败， 疑似在被攻击");
                return out(exchange.getResponse(), ResultCodeEnum.LOGIN_AUTH);

            }else {
                //成功解析
                //将userId添加到请求头
                ServerHttpRequest mutableRequst = request.mutate()
                        .headers(httpHeaders -> {
                            httpHeaders.add("userId", String.valueOf(aLong));
                        })
                        .build();
                log.info("解析成功, {}", aLong);
                return chain.filter(exchange.mutate().request(mutableRequst).build());
            }
        }

    }

    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
        Result result = Result.build(null, resultCodeEnum);
        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }


}
