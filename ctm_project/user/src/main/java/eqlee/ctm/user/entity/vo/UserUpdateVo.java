package eqlee.ctm.user.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/26
 * @Version 1.0
 */
@Data
public class UserUpdateVo {


    private Long id;

    /**
     * 中文名
     */
    private String cname;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 电话
     */
    private String tel;

    /**
     * 签名ID
     */
    private String appId;

    /**
     * 是否停用（false--正常  true--停用）
     */
    private Boolean stopped;
}
