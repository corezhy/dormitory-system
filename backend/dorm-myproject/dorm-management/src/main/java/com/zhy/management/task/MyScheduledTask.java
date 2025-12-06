package com.zhy.management.task;

import com.zhy.management.exception.BusinessException;
import com.zhy.management.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务处理业务类
 */
@Component
@Slf4j
public class MyScheduledTask {

    @Autowired
    private StudentService studentService;

    /**
     * 每天凌晨5点执行：删除已退宿学生
     */
    @Scheduled(cron = "0 0 5 * * ?")
    //@Scheduled(fixedDelay = 5000) 5s测试
    public void removeStuByStatusIsZero() {
        try {
            studentService.removeStuByStatusIsZero();
        } catch (Exception e) {
            log.error("定时删除已退宿学生失败", e);
            // 可选：报警、重试等
        }
    }



}
