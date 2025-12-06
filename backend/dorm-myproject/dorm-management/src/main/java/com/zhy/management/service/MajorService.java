package com.zhy.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhy.common.result.Result;
import com.zhy.model.dto.AddAndEditMajorDto;
import com.zhy.model.dto.MajorDto;
import com.zhy.model.entity.Major;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * 专业管理service接口
 */
public interface MajorService extends IService<Major> {

    /**
     * 条件查询专业
     * @param majorDto
     * @return
     */
    public Page<Major> select(MajorDto majorDto);

    /**
     * 新增专业
     * @param addAndEditMajorDto
     * @return
     */
    public void insert(AddAndEditMajorDto addAndEditMajorDto);

    /**
     * 根据id查询回显
     * @param code
     * @return
     */
    public Major selectById(String code);

    /**
     * 修改专业
     * @param addAndEditMajorDto
     * @return
     */
    public void updateById(AddAndEditMajorDto addAndEditMajorDto);

    /**
     * 启用停用专业
     * @param code
     * @param status
     * @return
     */
    public void startOrStop(String code, Integer status);

    /**
     * 批量删除专业
     * @param codes
     * @return
     */
    public void deleteBatch(List<String> codes);

    /**
     * 查询全部启用专业
     * @return
     */
    public List<Major> selectEnabled();
}
