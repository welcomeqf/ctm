package eqlee.ctm.finance.settlement.entity.vo;

import eqlee.ctm.finance.settlement.entity.bo.OtherBo;
import eqlee.ctm.finance.settlement.entity.bo.OutComeParamInfo;
import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
@Data
public class FinanceVo {

    /**
     * 出发日期
     */
    private String outDate;

    /**
     * 线路名
     */
    private String lineName;

    /**
     * 车牌号
     */
    private String carNo;

    /**
     * 应到人数
     */
    private Integer allDoNumber;

    /**
     * 实到人数
     */
    private Integer trueAllNumber;

    /**
     * 实到成年人数
     */
    private Integer treeAdultNumber;

    /**
     * 实到幼儿人数
     */
    private Integer treeBabyNumber;

    /**
     * 实到老人人数
     */
    private Integer treeOldNumber;

    /**
     * 实到小孩人数
     */
    private Integer treeChildNumber;


    /**
     * 面收金额
     */
    private Double gaiMoney;

    /**
     * 其他收入
     */
    private Double otherInPrice;

    /**
     * 支出消费信息
     */
    private List<OutComeParamInfo> outList;

    /**
     * 该团总价格
     */
    private Double allPrice;

    /**
     * 月结金额
     */
    private Double monthPrice;

    private Boolean carType;

    /**
     * 0-待审核  1-已同意  2-以拒绝  3--第一次提交
     */
    private Integer status;


    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

}
