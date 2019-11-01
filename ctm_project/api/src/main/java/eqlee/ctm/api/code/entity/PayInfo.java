package eqlee.ctm.api.code.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/10/30
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("PayInfo")
public class PayInfo extends Model<PayInfo> {

    private Long Id;

    /**
     * (0--微信支付 1--支付宝支付  2--银联)
     */
    private Integer Type;

    private String Code;

    private String OpenId;

    private Long UserId;

    private String Province;

    private String City;

    private String Area;

    private String HeadPortrait;

    private String Remark;

    private Long CreateUserId;

    private LocalDateTime CreateDate;

    private Long UpdateUserId;

    private LocalDateTime UpdateDate;
}
