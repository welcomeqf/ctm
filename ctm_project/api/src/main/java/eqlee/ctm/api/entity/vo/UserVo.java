package eqlee.ctm.api.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import eqlee.ctm.api.entity.bo.CityBo;
import lombok.Data;

import java.util.List;

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

    private String companyName;

    private String roleName;

    private List<CityBo> city;

    /**
     * 0--外部公司 1--本公司
     */
    private Integer type;

}
