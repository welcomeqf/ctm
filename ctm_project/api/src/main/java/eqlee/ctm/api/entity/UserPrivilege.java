package eqlee.ctm.api.entity;

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
    private Long SystemRoleId;

    /**
     * 功能菜单ID
     */
    private Long SystemMenuId;
}
