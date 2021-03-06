package eqlee.ctm.apply.entry.entity.query;

import com.yq.IBaseMapper.IBaseMapper;
import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/10/14
 * @Version 1.0
 */
@Data
public class ApplyDoExaQuery {

    private Long Id;

    /**
     * 报名单号
     */
    private String ApplyNo;

    /**
     * 出行日期
     */
    private String OutDate;

    /**
     * 线路名
     */
    private String LineName;

    /**
     * 成年人数
     */
    private Integer AdultNumber;

    /**
     * 幼儿人数
     */
    private Integer BabyNumber;

    /**
     * 老人人数
     */
    private Integer OldNumber;

    /**
     * 小孩人数
     */
    private Integer ChildNumber;

    /**
     * 同行公司名
     */
    private String CompanyName;

    /**
     * 同行代表人
     */
    private String CompanyUser;

    /**
     * (0默认false--未付款 1  true--已付款)
     */
    private Boolean IsPayment;

    /**
     * (0--待审核  1--通过   2--不通过)
     */
    private Integer Statu;

    /**
     * 是否取消(0默认false--正常  1  true--取消)
     */
    private Boolean IsCancel;

    /**
     * 审核类型（0--报名审核  1--取消报名审核）
     */
    private String ExamineType;

    /**
     * 0-待处理 1-通过 2-拒绝
     */
    private Integer ExamineResult;

    /**
     * 联系人
     */
    private String ContactName;

    /**
     * 联系电话
     */
    private String ContactTel;

    private Double AllPrice;

    /**
     * (0--正常报名  1-补录  2-包团)
     */
    private Integer Type;

    /**
     * 包团文件详情
     */
    private String applyPic;

    /**
     * 报名审核记录
     */
    private List<ApplyExamRecord> ExamineRecords;


}
