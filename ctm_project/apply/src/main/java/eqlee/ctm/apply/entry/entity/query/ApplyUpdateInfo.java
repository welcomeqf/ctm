package eqlee.ctm.apply.entry.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/20
 * @Version 1.0
 */
@Data
public class ApplyUpdateInfo {

    private Long Id;

    /**
     *联系人名字
     */
    private String ContactName;

    /**
     * 联系电话
     */
    private String ContactTel;

    /**
     * 接送地点
     */
    private String Place;

    private String remark;
}
