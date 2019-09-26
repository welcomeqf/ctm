package eqlee.ctm.apply.entry.entity.query;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author qf
 * @Date 2019/9/20
 * @Version 1.0
 */
@Data
public class ApplyCompanyQuery {

    private Long Id;

    /**
     * 报名单号
     */
    private String ApplyNo;

    /**
     * 出行日期
     */
    private LocalDate OutDate;

    /**
     * 线路名
     */
    private String LineName;

    /**
     * 总人数
     */
    private Integer AllNumber;

    /**
     *联系人名字
     */
    private String ContactName;

    /**
     * 联系电话
     */
    private String ContactTel;

    /**
     * (0-待审核  1-通过  2--不通过)
     */
    private Integer Statu;
}
