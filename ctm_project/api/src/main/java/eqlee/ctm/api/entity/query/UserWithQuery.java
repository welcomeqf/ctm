package eqlee.ctm.api.entity.query;

import eqlee.ctm.api.entity.bo.CityBo;
import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/10/21
 * @Version 1.0
 */
@Data
public class UserWithQuery {

    private String userName;

    private String password;

    private String name;

    private String phone;

    private Long companyId;

    private String companyName;

    private String roleName;

    private List<CityBo> city;
}
