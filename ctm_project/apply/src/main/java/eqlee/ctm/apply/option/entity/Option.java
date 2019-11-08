package eqlee.ctm.apply.option.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/11/7
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@TableName("SystemOption")
public class Option extends Model<Option> {

    /**
     * id
     */
    private Long Id;

    /**
     * 城市类别
     */
    private Integer Type;

    /**
     * 城市名称
     */
    private String Name;

    /**
     * 排序
     */
    private Integer Sort;

    /**
     * 是否默认
     */
    private Boolean IsDefault;

    /**
     * 是否停用
     */
    private Boolean Stopped;

    /**
     * 是否删除
     */
    private Boolean Deleted;

    /**
     * 创建人
     */
    private Long CreateUserId;

    /**
     * 创建时间
     */
    private LocalDateTime CreateUserDate;
}
