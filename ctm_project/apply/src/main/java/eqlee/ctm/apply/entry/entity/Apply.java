package eqlee.ctm.apply.entry.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Apply")
public class Apply extends Model<Apply> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
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
     * 总价格
     */
    private Double AllPrice;

    /**
     * 门市价
     */
    private Double MarketAllPrice;

    /**
     * 线路ID  哪条线路
     */
    private Long LineId;

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
     * 总人数
     */
    private Integer AllNumber;

    /**
     * 同行id
     */
    private Long CompanyId;

    /**
     * 同行公司名
     */
    private String CompanyName;

    /**
     * 同行代表人账号
     */
    private String CompanyUser;

    /**
     * 同行代表人名字
     */
    private String CName;

    /**
     * 同行代表人电话
     */
    private String CompanyTel;

    /**
     * 省（备用）
     */
    private String Province;

    /**
     * 市（备用）
     */
    private String City;

    /**
     * 区域
     */
    private String Region;

    /**
     * 支付类型(0--现结  1--月结 2--代付)默认0
     */
    private Integer PayType;

    /**
     * (0默认false--未付款 1  true--已付款)
     */
    private Boolean IsPayment;

    /**
     * 备注
     */
    private String Remark;

    /**
     * 是否取消(0默认false--正常  1  true--取消)
     */
    private Boolean IsCancel;

    /**
     * 导游是否选人（0--未选 1--已选）
     */
    private Boolean IsSelect;

    /**
     * 创建人
     */
    private Long CreateUserId;

    /**
     * 创建时间
     */
    private LocalDateTime CreateDate;

    /**
     * 修改人
     */
    private Long UpdateUserId;

    /**
     * 修改时间
     */
    private LocalDateTime UpdateDate;

    /**
     * (0-待审核  1-通过  2--不通过)
     */
    private Integer Statu;

    /**
     * 过期时间
     */
    private Integer ExpreDate;

    /**
     * 面收金额
     */
    private Double MsPrice;

    /**
     * 报名备注
     */
    private String ApplyRemark;

    /**
     * 0--微信  1--支付宝
     */
    private Integer PayInfo;

    /**
     * 0--正常  1--申请中
     */
    private Integer CancelInfo;

    /**
     * 0--正常报名  1--补录 2--包团
     */
    private Integer Type;

    /**
     * (0--正常  1--已结算)
     * 续授信金额将之前的报名单为1
     */
    private Integer SxType;

    /**
     * 0--财务未确认  1--财务已确认
     */
    private Integer CaiType;

    /**
     * 成人价格
     */
    private Double AdultPrice;

    /**
     * 幼儿价格
     */
    private Double BabyPrice;

    /**
     * 老人价格
     */
    private Double OldPrice;

    /**
     * 小孩价格
     */
    private Double ChildPrice;

    /**
     * 包团详情
     */
    private String ApplyPic;

    /**
     * 导游
     */
    private String GuideName;

    /**
     * 导游电话
     */
    private String GuideTel;

    /**
     *  身份证信息（选填）
     */
    private String icnumber;

    /**
     * 接送地
     */
    private String PlaceRegion;

    /**
     * 区域
     */
    private String PlaceAddress;

    /**
     * 是否发过通知(0默认false--没有  1  true--已发)
     */
    private Boolean IsSend;

    @Override
    protected Serializable pkVal() {
        return this.Id;
    }

}
