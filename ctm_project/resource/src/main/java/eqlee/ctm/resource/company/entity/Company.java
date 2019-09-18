package eqlee.ctm.resource.company.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.time.LocalDateTime;

/**
 * @Author Claire
 * @Date 2019/9/17 0017
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Company")
public class Company extends Model<Company> {

    private Long Id;

    /*
    公司名称
    */
    private String CompanyName;

    /*
    合同开始时间
    */
    private LocalDateTime StartDate;

    /*
    合同结束时间
    */
    private LocalDateTime endDate;

    /*
    支付方式(0--默认1--现结2--月结3--代收)
    */
    private int PayMethod;

    /*
    状态（0--正常 1--停用）
    */
    private boolean Stopped;

    /*
    备注
    */
    private String Remark;

    /*
   创建人
   */
    private Long CreateUserId;

    /*
  创建时间
  */
    private LocalDateTime CreateDate;

    /*
  修改人
  */
    private Long UpdateUserId;

    /*
  创建时间
  */
    private LocalDateTime UpdateDate;
}
