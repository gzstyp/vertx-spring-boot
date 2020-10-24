package io.javac.vertx.vertxdemo.handler;

import io.javac.vertx.vertxdemo.model.respone.ResponeWrapper;
import io.javac.vertx.vertxdemo.vertx.VertxRespone;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 拦截器
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/10/24 9:14
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Component
public class TokenCheckHandler implements Handler<RoutingContext> {
    @Override
    public void handle(final RoutingContext context) {
        final HttpServerRequest request = context.request();
        final String accesstoken = request.getHeader("accesstoken");
        if (StringUtils.isEmpty(accesstoken)) {
            VertxRespone.build(context).respone(new ResponeWrapper(10002, null, "登录失效，请重新登录！"));
        } else {
            //继续下一个路由
            context.next();
        }
    }
}