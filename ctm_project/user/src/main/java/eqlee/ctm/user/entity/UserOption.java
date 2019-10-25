package eqlee.ctm.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/10/23
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("SystemOption")
public class UserOption extends Model<UserOption> {

    private Long Id;

    private Long RoleId;

    private Long MenuId;

    private Boolean Started;

    private Integer Type;

    private String Name;

    private Integer Sort;

    private Boolean IsDefault;

    private Boolean Stopped;

    private Boolean Deleted;

    private Long CreateUserId;

    private LocalDateTime CreateUserDate;
}
