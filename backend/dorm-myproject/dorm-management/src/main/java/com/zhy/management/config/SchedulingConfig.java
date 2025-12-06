package com.zhy.management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * springTask自定义线程池
 */
@Configuration
public class SchedulingConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5); // 设置线程池大小
        taskScheduler.setThreadNamePrefix("scheduled-task-");
        taskScheduler.initialize();

        taskRegistrar.setTaskScheduler(taskScheduler);
    }
}