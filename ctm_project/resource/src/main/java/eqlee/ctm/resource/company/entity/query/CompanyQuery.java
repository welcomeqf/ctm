package eqlee.ctm.resource.company.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/24
 * @Version 1.0
 */
@Data
public class CompanyQuery {

    private String companyName;

    private Long id;

    /**
     * 用户名
     */
    private String account;

    /**
     * 中文名
     */
    private String cName;

    /**
     * 电话
     */
    private String tel;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 结算方式
     */
    private String payType;
}
