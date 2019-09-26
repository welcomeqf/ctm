package eqlee.ctm.user.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/23
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("SecutictySign")
public class Sign extends Model<Sign> {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一（相当于设备ID）
     */
    private Long Id;

    /**
     * AppId 需要AppId才能访问
     */
    private String AppId;

    /**
     * 签名
     */
    private String MySig;

    /**
     * 密钥
     */
    private String PrivateKey;

    /**
     * 公钥
     */
    private String PublicKey;

    /**
     * 用户信息（通常取用户名）
     */
    private String Information;

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
