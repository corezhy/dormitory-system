package com.zhy.model.vo;

import lombok.Data;
import java.util.List;

/**
 * 入住人数时间趋势统计VO
 */
@Data
public class CheckinTrendVO {
    private List<String> labels;  // 时间标签，如 ["2024-01", "2024-02"]
    private List<Integer> values; // 对应的人数，如 [45, 52]
    private Integer total;        // 总数
    
    public CheckinTrendVO() {
    }
    
    public CheckinTrendVO(List<String> labels, List<Integer> values) {
        this.labels = labels;
        this.values = values;
        this.total = values.stream().mapToInt(Integer::intValue).sum();
    }
}