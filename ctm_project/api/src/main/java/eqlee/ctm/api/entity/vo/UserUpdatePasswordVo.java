package eqlee.ctm.api.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/26
 * @Version 1.0
 */
@Data
public class UserUpdatePasswordVo {

    private Long Id;

    private String userName;

    private String password;

    private String tel;

    private String AppId;
}