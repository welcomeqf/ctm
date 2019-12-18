package eqlee.ctm.apply.entry.entity.bo;

import lombok.Data;

import java.util.List;

/**
 * @author qf
 * @date 2019/11/26
 * @vesion 1.0
 **/
@Data
public class PriceBo {

   private String startTime;

   private String endTime;

   private List<Integer> weekList;

   private String lineName;

   private Double adultPrice;

   private Double oldPrice;

   private Double babyPrice;

   private Double childPrice;

   private Double marketAdultPrice;

   private Double marketOldPrice;

   private Double marketBabyPrice;

   private Double marketChildPrice;

   private String remark;
}
