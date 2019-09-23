package eqlee.ctm.api.vilidate;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/22
 * @Version 1.0
 */
@Data
public class BuildVo {

    private Integer id;
    private String name;
    private Integer parentId;
    private String type;
    private String AppId;
}
