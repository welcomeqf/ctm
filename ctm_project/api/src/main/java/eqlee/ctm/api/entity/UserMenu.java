package eqlee.ctm.api.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
@Data
public class UserMenu {

    /**
     * 菜单ID
     */
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long Id;

    /**
     * 父级ID
     */
    @JSONField(serializeUsing= ToStringSerializer.class)
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
}
