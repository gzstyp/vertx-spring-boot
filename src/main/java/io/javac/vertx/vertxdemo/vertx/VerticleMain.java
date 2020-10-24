package io.javac.vertx.vertxdemo.vertx;

import io.javac.vertx.vertxdemo.annotation.RequestBlockingHandler;
import io.javac.vertx.vertxdemo.annotation.RequestBody;
import io.javac.vertx.vertxdemo.annotation.RequestMapping;
import io.javac.vertx.vertxdemo.config.SpringBootContext;
import io.javac.vertx.vertxdemo.handler.TokenCheckHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * 启动类
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/10/24 10:05
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Component
public class VerticleMain extends AbstractVerticle {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    TokenCheckHandler tokenCheckHandler;

    /**
     * Controller 所在的包
    */
    private final String controllerBasePackage[] = {"io.javac.vertx.vertxdemo.controller"};

    @Override
    public void start() throws Exception {
        super.start();
        //路由
        final Router router = Router.router(vertx);
        router.route().path("/api/*").handler(tokenCheckHandler);

        //注册Controller
        for (String packagePath : controllerBasePackage) {
            registerController(router, packagePath);
        }
        router.route().failureHandler(handler -> {
            handler.failure().printStackTrace();
        });
        //start listen port
        final HttpServer server = vertx.createHttpServer();
        server.requestHandler(router).listen(801, handler -> {
            System.out.println("vertx run prot : [{}] run state : [{}]," + 801 + "," + handler.succeeded());
        });
    }

    /**
     * register controller
    */
    private void registerController(@NotNull Router router, String packagePath) {
        if (SpringBootContext.getApplicationContext() == null) {
            System.out.println("SpringBoot application context is null register controller is fail");
            return;
        }
        try {
            final Resource[] resources = VerticleUtils.scannerControllerClass(packagePath, resourceLoader);
            final String separator = File.separator;
            for (final Resource resource : resources) {
                String absolutePath = resource.getFile().getAbsolutePath().replace(separator, ".");
                absolutePath = absolutePath.substring(absolutePath.indexOf(packagePath));
                absolutePath = absolutePath.replace(".class", "");
                if (StringUtils.isEmpty(absolutePath)) continue;
                //get class
                final Class<?> controllerClass = Class.forName(absolutePath);
                //from class get controller instance bean
                final Object controller = SpringBootContext.getApplicationContext().getBean(controllerClass);

                final RequestMapping classRequestMapping = controllerClass.getAnnotation(RequestMapping.class);
                //if controller class not have requestMapping annotation -> skip register
                if (classRequestMapping == null) continue;
                //register controller method
                registerControllerMethod(router, classRequestMapping, controllerClass, controller);
            }
        } catch (Exception ex) {
            System.out.println("registerController fail " + ex);
        }
    }

    /**
     * register controller method
     *
     * @param router              route
     * @param classRequestMapping controller requestMapping annotation
     * @param controllerClass     controller class
     * @param controller          controller instance
     */
    private void registerControllerMethod(@NotNull Router router, @NotNull RequestMapping classRequestMapping, @NotNull Class<?> controllerClass, @NotNull Object controller) {
        //获取控制器里的方法
        final Method[] controllerClassMethods = controllerClass.getMethods();
        Arrays.asList(controllerClassMethods).stream()
            .filter(method -> method.getAnnotation(RequestMapping.class) != null)
            .forEach(method -> {
                try {
                    RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
                    final String superPath = classRequestMapping.value()[0];
                    final String methodPath = methodRequestMapping.value()[0];
                    //if api path empty skip
                    if (StringUtils.isEmpty(superPath) || StringUtils.isEmpty(methodPath)) return;
                    final String url = VerticleUtils.buildApiPath(superPath, methodPath);
                    //build route
                    final Route route = VerticleUtils.buildRouterUrl(url, router, methodRequestMapping.method());
                    //run controller method get Handler object
                    final Handler<RoutingContext> methodHandler = (Handler<RoutingContext>) method.invoke(controller);
                    //register bodyAsJson handler
                    Optional.ofNullable(method.getAnnotation(RequestBody.class)).ifPresent(requestBody -> {
                        route.handler(BodyHandler.create());
                    });
                    //register controller mthod Handler object
                    RequestBlockingHandler requestBlockingHandler = Optional.ofNullable(method.getAnnotation(RequestBlockingHandler.class)).orElseGet(() -> controllerClass.getAnnotation(RequestBlockingHandler.class));
                    if (requestBlockingHandler != null) {
                        //register blocking handler
                        route.blockingHandler(methodHandler);
                    } else {
                        route.handler(methodHandler);
                    }
                    System.out.println("register controller -> [{}]  method -> [{}]  url -> [{}] " + controllerClass.getName() + method.getName() + url);
                } catch (Exception e) {
                    System.err.println("registerControllerMethod fail controller: [{}]  method: [{}]" + controllerClass + method.getName());
                }
            });
    }
}