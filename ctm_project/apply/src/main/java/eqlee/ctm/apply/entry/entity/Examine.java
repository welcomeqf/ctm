package eqlee.ctm.apply.entry.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/20
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Examine")
public class Examine extends Model<Examine> {

    private static final long serialVersionUID = 1L;

    private Long Id;

    /**
     * 报名表ID
     */
    private Long ApplyId;

    /**
     * 审核类型(0--报名审核  1--取消审核 2-修改报名表)
     */
    private String ExamineType;

    /**
     * (0--待审核  1--通过  2--不通过)
     */
    private Integer ExamineResult;

    /**
     * 备注
     */
    private String Remark;

    /**
     * 创建人
     */
    private Long CreateUserId;

    /**
     * 创建时间
     */
    private LocalDateTime CreateDate;

    /**
     * 修改人
     */
    private Long UpdateUserId;

    /**
     * 修改时间
     */
    private LocalDateTime UpdateDate;

    @Override
    protected Serializable pkVal() {
        return this.Id;
    }
}
