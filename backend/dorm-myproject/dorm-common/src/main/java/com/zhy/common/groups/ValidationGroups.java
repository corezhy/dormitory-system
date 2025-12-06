package com.zhy.common.groups;

/**
 * 分组校验接口
 */
public interface ValidationGroups {
    interface Insert {}  // 新增时使用
    interface Update {}  // 编辑时使用（可选）
}