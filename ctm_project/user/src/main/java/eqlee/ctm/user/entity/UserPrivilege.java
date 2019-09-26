package eqlee.ctm.user.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
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
@TableName("SystemRolePrivilege")
public class UserPrivilege extends Model<UserPrivilege> {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private Long SystemRoleId;

    /**
     * 功能菜单ID
     */
    private Long SystemMenuId;

    @Override
    protected Serializable pkVal() {
        return this.SystemRoleId;
    }


}
