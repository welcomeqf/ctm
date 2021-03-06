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
public class UserRole {

    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long Id;

    /**
     * 角色名字
     */
    private String RoleName;

    /**
     * 公司ID
     */
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long CompanyId;

    /**
     * 是否停用(0--停用false  1--正常（正在使用）  默认停用)
     */
    private Boolean Stopped;

    /**
     * 0--默认  1--子用户设置
     */
    private Integer Statu;

    /**
     * 备注
     */
    private String Remark;

    /**
     * 是否已删除(0--未删除  1--已删除  默认0)
     */
    private Boolean Deleted;

}
