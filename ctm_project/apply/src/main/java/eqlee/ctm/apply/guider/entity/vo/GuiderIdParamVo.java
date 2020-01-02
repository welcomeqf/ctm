package eqlee.ctm.apply.guider.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/25
 * @vesion 1.0
 **/
@Data
public class GuiderIdParamVo {

   private Integer current;

   private Integer size;

   private String outDate;

   private GuiderList lineNameList;

   private String region;
}
