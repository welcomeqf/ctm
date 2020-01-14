package eqlee.ctm.api.entity.vo;

import eqlee.ctm.api.entity.bo.CityBo;
import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/10/17
 * @Version 1.0
 */
@Data
public class UserUpdateInfoVo {


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

    private String roleName;

    /**
     * 是否停用（false--正常  true--停用）
     */
    private Boolean stopped;

    /**
     * 城市
     */
    private List<CityBo> city;
}
