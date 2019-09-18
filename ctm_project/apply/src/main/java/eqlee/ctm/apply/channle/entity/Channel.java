package eqlee.ctm.apply.channle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/18
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Channel")
public class Channel {


    private Long Id;

    /*
   渠道类型
   */
    private String ChannelType;

    /**
     * 是否停用(0默认--正常  1--停用)
     */
    private Boolean Stopped;

    /*
   备注
   */
    private String Remark;

    /*
   创建人
   */
    private Long CreateUserId;

    /*
   创建时间
   */
    private LocalDateTime CreateDate;

    /*
   修改人
   */
    private Long UpdateUserId;

    /*
   修改时间
   */
    private LocalDateTime UpdateDate;
}
