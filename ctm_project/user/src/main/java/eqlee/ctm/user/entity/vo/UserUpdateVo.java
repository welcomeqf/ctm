package eqlee.ctm.user.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/26
 * @Version 1.0
 */
@Data
public class UserUpdateVo {

    private Long Id;

    /**
     * 中文名
     */
    private String CName;


    /**
     * 个人图像路径
     */
    private String SelfImagePath;

    /**
     * 电话
     */
    private String Tel;

    /**
     * 公司ID
     */
    private Long CompanyId;

    private String AppId;
}
