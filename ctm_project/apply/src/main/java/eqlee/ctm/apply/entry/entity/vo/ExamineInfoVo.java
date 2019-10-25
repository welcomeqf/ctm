package eqlee.ctm.apply.entry.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/15
 * @Version 1.0
 */
@Data
public class ExamineInfoVo {

    /**
     * 修改人
     */
    private String UpdateUserName;

    /**
     * 修改时间
     */
    private String UpdateDate;

    /**
     * 联系人
     */
    private String ConnectName;

    /**
     * 联系电话
     */
    private String ConnectTel;

    /**
     * 接送地
     */
    private String Place;
}
