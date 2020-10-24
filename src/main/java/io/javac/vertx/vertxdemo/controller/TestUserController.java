package io.javac.vertx.vertxdemo.controller;

import io.javac.vertx.vertxdemo.annotation.RequestBlockingHandler;
import io.javac.vertx.vertxdemo.annotation.RequestMapping;
import io.javac.vertx.vertxdemo.base.ControllerHandler;
import io.javac.vertx.vertxdemo.enums.RequestMethod;
import io.javac.vertx.vertxdemo.model.respone.ResponeWrapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/user")
@Component
public class TestUserController {

    // http://127.0.0.1:801/api/user/userInfo
    @RequestMapping("userInfo")
    public ControllerHandler userInfo() {
        return vertxRequest -> {
            Map<String, String> map = new HashMap<>();
            map.put("name", "大白");
            map.put("age", "18");
            vertxRequest.buildVertxRespone().responeSuccess(map);
        };
    }

    // http://127.0.0.1:801/api/user/findGirlFriend
    @RequestBlockingHandler
    @RequestMapping("findGirlFriend")
    public ControllerHandler findGirlFriend() {
        return vertxRequest -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            vertxRequest.buildVertxRespone().respone(new ResponeWrapper(10001, null, "girl friend not found"));
        };
    }


    // http://127.0.0.1:801/api/user/userLogin?username=admin&password=666666
    @RequestMapping(value = "userLogin", method = RequestMethod.POST)
    public ControllerHandler userLogin() {
        return vertxRequest -> {
            String username = vertxRequest.getParam("username").orElse("not found");
            String password = vertxRequest.getParam("password").orElse("not found");
            Map<String, String> map = new HashMap<>();
            map.put("username", username);
            map.put("password", password);
            vertxRequest.buildVertxRespone().responeSuccess(map);
        };
    }
}