package eqlee.ctm.user.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("SystemMenu")
public class UserMenu extends Model<UserMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    private Long Id;

    /**
     * 父级ID
     */
    private Long Parent;

    /**
     * 菜单名称
     */
    private String MenuName;

    /**
     * 链接地址
     */
    private String Action;

    /**
     * 排序
     */
    private Integer Sort;

    /**
     * 图标CLASS
     */
    private String IconClass;

    /**
     * 图标颜色
     */
    private String IconColor;

    /**
     * 是否为系统菜单（默认0--为系统菜单  1--不为系统菜单）
     */
    private Boolean Systemic;

    /**
     * 是否停用（默认0--停用  1--正常（正在使用））
     */
    private Boolean Stopped;

    @TableField(exist = false)
    private Boolean start;


    @Override
    protected Serializable pkVal() {
        return this.Id;
    }
}
