package eqlee.ctm.apply.entry.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/20
 * @Version 1.0
 */
@Data
public class ExamineVo {

    /**
     * 报名ID
     */
    private Long ApplyId;

    /**
     * 联系人
     */
    private String ConnectName;

    /**
     * 联系电话
     */
    private String ConnectTel;

    /**
     * 接送地点
     */
    private String Place;
}
