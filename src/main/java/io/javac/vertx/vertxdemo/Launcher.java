package io.javac.vertx.vertxdemo;

import io.javac.vertx.vertxdemo.config.SpringBootContext;
import io.javac.vertx.vertxdemo.vertx.VerticleMain;
import io.vertx.core.Vertx;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Launcher{

    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }

    /**
     * 监听SpringBoot 启动完毕 开始部署Vertx
     *
     * @param event
    */
    @EventListener
    public void deployVertx(final ApplicationReadyEvent event) {
        final ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        SpringBootContext.setApplicationContext(applicationContext);
        final VerticleMain verticleMain = applicationContext.getBean(VerticleMain.class);
        final Vertx vertx = Vertx.vertx();
        //部署vertx
        vertx.deployVerticle(verticleMain, handler -> {
            System.out.println("vertx deploy state [{}]" + handler.succeeded());
        });
    }
}