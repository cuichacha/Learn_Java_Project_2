package com.itheima.admin.gateway.filter;

import com.itheima.admin.gateway.utils.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author cuichacha
 * @date 1/25/21 19:55
 */
@Component
@Log4j2
public class AuthorizeFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 登录请求直接放行
        String path = request.getURI().getPath();
        if (path.contains("/login/in")) {
            return chain.filter(exchange);
        }
        // 通过请求头获取token
        String token = request.getHeaders().getFirst("token");
        // token不存在就直接返回错误信息
        if (StringUtils.isEmpty(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        // 判断token是否合法
        Claims claimsBody = AppJwtUtil.getClaimsBody(token);
        int result = AppJwtUtil.verifyToken(claimsBody);
        if(result == 0 || result == -1){
            Integer id = (Integer) claimsBody.get("id");
            log.info("find userid:{} from uri:{}",id,request.getURI());
            //重新设置token到header中
                ServerHttpRequest serverHttpRequest = request.mutate().headers(httpHeaders -> {
                    httpHeaders.add("userId", id + "");
                }).build();
                exchange.mutate().request(serverHttpRequest).build();
                return chain.filter(exchange);
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
