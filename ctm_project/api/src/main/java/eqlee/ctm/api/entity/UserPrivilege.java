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
public class UserPrivilege {

    /**
     * 角色ID
     */
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long SystemRoleId;

    /**
     * 功能菜单ID
     */
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long SystemMenuId;
}
