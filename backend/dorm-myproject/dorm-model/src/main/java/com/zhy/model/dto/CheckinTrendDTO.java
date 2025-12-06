package com.zhy.model.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * 入住人数时间趋势统计Dto
 */
@Data
public class CheckinTrendDTO {
    private String rangeType;  // day, week, month, quarter, year, custom
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
}
