package com.dotop.smartwater.project.server.water;


import com.dotop.smartwater.project.module.api.revenue.IPaymentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @program: project-parent
 * @description:

 * @create: 2019-12-16 09:37
 **/
@Component
public class AfterServiceStarted implements ApplicationRunner {
    /**
     * 会在服务启动完成后立即执行(监听队列)
     */
    @Autowired
    private IPaymentFactory iPaymentFactory;

    @Override
    public void run(ApplicationArguments args){
        Thread t = new Thread(() -> {
            //监听redis队列
            iPaymentFactory.receive();
        });
        t.setName("监听redis队列线程");
        t.start();
    }
}
