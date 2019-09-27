package eqlee.ctm.finance.settlement.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
@Data
public class OrderDetailedVo {

    /**
     * 成人人数
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




}
