package com.yq.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("SystemRole")
public class UserRole extends Model<UserRole> {

    private Long Id;

    /**
     * 角色名字
     */
    private String RoleName;

    /**
     * 公司ID
     */
    private Long CompanyId;

    /**
     * 是否停用(0--停用  1--正常（正在使用）  默认停用)
     */
    private Boolean Stopped;

    /**
     * 备注
     */
    private String Remark;

    /**
     * 是否已删除(0--未删除  1--已删除  默认0)
     */
    private Boolean Deleted;

}
