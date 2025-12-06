package com.zhy.management.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计宿舍Id出现的count次（方便批量删除时同宿舍空闲床铺只加一次）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomUpdate {
    private Long roomId;
    private Integer count;
}