package eqlee.ctm.finance.settlement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@TableName("Outcome")
public class Outcome extends Model<Outcome> {

    private static final long serialVersionUID = 1L;

    private Long Id;

    /**
     * 收入Id
     */
    private Long IncomeId;

    /**
     * 门票名
     */
    private String TicketName;

    /**
     * 门票价格
     */
    private Double TicketPrice;

    /**
     * 午餐费用
     */
    private Double LunchPrice;

    /**
     * 停车费
     */
    private Double ParkingRatePrice;

    /**
     * 车牌号
     */
    private String CarNo;

    /**
     * 租车费用
     */
    private Double RentCarPrice;

    /**
     * 导游补助
     */
    private Double GuideSubsidy;

    /**
     * 司机补助
     */
    private Double DriverSubsidy;

    /**
     * 总支出费用
     */
    private Double AllOutPrice;

    /**
     * 备注
     */
    private String Remark;

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

    @Override
    protected Serializable pkVal() {
        return this.Id;
    }
}
