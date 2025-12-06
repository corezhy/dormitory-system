package com.zhy.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhy.model.entity.Major;
import org.apache.ibatis.annotations.Mapper;

/**
 * 专业管理mapper层
 */
@Mapper
public interface MajorMapper extends BaseMapper<Major> {
}
