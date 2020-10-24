package io.javac.vertx.vertxdemo.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javac.vertx.vertxdemo.model.respone.ResponeWrapper;
import io.javac.vertx.vertxdemo.utils.JsonUtils;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

/**
 * 客户端响应
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/10/24 9:32
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public class VertxRespone {

    private final RoutingContext routingContext;

    private VertxRespone(final RoutingContext routingContext) {
        this.routingContext = routingContext;
    }

    public static VertxRespone build(RoutingContext routingContext) {
        return new VertxRespone(routingContext);
    }

    public void respone(final ResponeWrapper responeWrapper) {
        final HttpServerResponse response = routingContext.response();
        response.putHeader("Content-Type","text/json;charset=utf-8");
        try {
            // 转换为JSON 字符串
            response.end(JsonUtils.objectToJson(responeWrapper));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void responeSuccess(Object data) {
        respone(new ResponeWrapper(0, data, "操作成功"));
    }

    public void responeState(boolean state) {
        if (state) {
            respone(ResponeWrapper.RESPONE_SUCCESS);
        } else {
            respone(ResponeWrapper.RESPONE_FAIL);
        }
    }
}