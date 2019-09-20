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


    private String CompanyName;


    private LocalDateTime StartDate;


    private LocalDateTime endDate;


    private int PayMethod;


    private boolean Stopped;


    private String Remark;


    private Long CreateUserId;


    private LocalDateTime CreateDate;

    private Long UpdateUserId;


    private LocalDateTime UpdateDate;
}
