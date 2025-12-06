package com.zhy.management.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhy.common.result.Result;
import com.zhy.management.exception.BusinessException;
import com.zhy.management.mapper.MajorMapper;
import com.zhy.management.mapper.StudentMapper;
import com.zhy.management.service.MajorService;
import com.zhy.management.service.StudentService;
import com.zhy.model.dto.AddAndEditMajorDto;
import com.zhy.model.dto.MajorDto;
import com.zhy.model.entity.Major;
import com.zhy.model.entity.Student;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 专业管理service层实现类
 */
@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements MajorService {

    @Autowired
    private MajorMapper majorMapper;

    @Autowired
    private StudentService studentService;
    /**
     * 条件查询专业
     *
     * @param majorDto
     * @return
     */
    @Override
    public Page<Major> select(MajorDto majorDto) {
        //构造分页参数
        Page<Major> page = new Page<>(majorDto.getPageNum(), majorDto.getPageSize());
        //查询
        return page(page, Wrappers.<Major>lambdaQuery()
                .like(StringUtils.isNotBlank(majorDto.getName()), Major::getName, majorDto.getName())
                .like(StringUtils.isNotBlank(majorDto.getDepartment()), Major::getDepartment, majorDto.getDepartment())
                .eq(majorDto.getIsEnabled() != null, Major::getIsEnabled, majorDto.getIsEnabled())
                .orderByDesc(Major::getUpdateTime));
    }

    /**
     * 新增专业
     *
     * @param addAndEditMajorDto
     * @return
     */
    @Override
    public void insert(AddAndEditMajorDto addAndEditMajorDto) {
        if (!addAndEditMajorDto.getCode().matches("\\d{6}")) {
            throw new BusinessException("专业代码必须为6位数字");
        }
        //创建Major对象
        Major major = new Major();
        //属性拷贝
        BeanUtils.copyProperties(addAndEditMajorDto, major);
        //创建时间和修改时间设置为当前时间
        major.setCreateTime(LocalDateTime.now());
        major.setUpdateTime(LocalDateTime.now());
        //新增
        boolean save = save(major);
        if (!save) {
            throw new BusinessException("新增专业失败");
        }
    }

    /**
     * 根据id查询回显
     *
     * @param code
     * @return
     */
    @Override
    public Major selectById(String code) {
        if (StringUtils.isBlank(code)) {
            throw new BusinessException("请选择专业");
        }
        //查询
        Major major = getById(code);
        if (major == null) {
            throw new BusinessException("没有当前专业");
        }
        return major;
    }

    /**
     * 修改专业
     *
     * @param addAndEditMajorDto
     * @return
     */
    @Override
    public void updateById(AddAndEditMajorDto addAndEditMajorDto) {
        //创建专业对象
        Major major = new Major();
        //属性拷贝
        BeanUtils.copyProperties(addAndEditMajorDto, major);
        //修改时间
        major.setUpdateTime(LocalDateTime.now());
        //修改
        boolean b = updateById(major);
        if (!b) {
            throw new BusinessException("修改专业失败");
        }
    }

    /**
     * 启用停用专业
     *
     * @param code
     * @param status
     * @return
     */
    @Override
    public void startOrStop(String code, Integer status) {
        if (StringUtils.isBlank(code) || status == null) {
            throw new BusinessException("请选择要设置的楼栋");
        }
        //参数校验
        if(status != 0 && status != 1) {
            throw new BusinessException("只能设置启用或者停用");
        }

        //停用时，如果该专业下还有学生，不能停用
        if (status == 0) {
            //根据专业代码查询学生表是否有数据
            boolean exist = studentService.getStuByMajorCode(code);
            if (exist) {
                throw new BusinessException("该专业下还有学生，无法停用");
            }
        }

        //创建专业对象
        Major major = new Major();
        //添加参数
        major.setCode(code);
        major.setIsEnabled(status);
        //修改时间
        major.setUpdateTime(LocalDateTime.now());
        //修改
        boolean b = updateById(major);
        if (!b) {
            throw new BusinessException("设置专业状态失败");
        }
    }

    /**
     * 批量删除专业
     *
     * @param codes
     * @return
     */
    @Override
    @Transactional
    public void deleteBatch(List<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            throw new BusinessException("请选择需要删除的专业");
        }
        //去重，防止前端误传重复 ID
        List<String> uniqueIds = new ArrayList<>(new LinkedHashSet<>(codes));

        //根据专业代码-code查询是否有学生，如果有，不允许删除
        boolean exist = studentService.getStuByMajorCodes(uniqueIds);
        if (exist) {
            throw new BusinessException("存在学生关联的专业，无法删除");
        }
        //删除
        boolean b = removeBatchByIds(uniqueIds);
        if (!b) {
            throw new BusinessException("所选专业不存在或已被删除，请刷新后重试");
        }

    }

    /**
     * 查询全部启用专业
     *
     * @return
     */
    @Override
    public List<Major> selectEnabled() {
        //启用1  停用0
        List<Major> majors = list(Wrappers.<Major>lambdaQuery().eq(Major::getIsEnabled, 1));
        if (CollectionUtils.isEmpty(majors)) {
            throw new BusinessException("查询启用专业失败");
        }
        return majors;
    }


}
