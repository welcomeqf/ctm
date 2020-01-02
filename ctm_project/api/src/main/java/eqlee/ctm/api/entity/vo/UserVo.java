package eqlee.ctm.api.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/12
 * @Version 1.0
 */
@Data
public class UserVo {

    private String userName;

    private String password;

    private String name;

    private String phone;

    private Long companyId;

    private String roleName;

    private String city;

}
