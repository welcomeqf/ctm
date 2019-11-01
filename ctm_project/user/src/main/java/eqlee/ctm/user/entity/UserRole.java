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
@TableName("SystemRole")
public class UserRole extends Model<UserRole> {

    private static final long serialVersionUID = 1L;

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
     * 是否停用(0--正常（正在使用）false  1--停用)
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

    /**
     * 操作人
     */
    private Long CreateUserId;

    @Override
    protected Serializable pkVal() {
        return this.Id;
    }

}
