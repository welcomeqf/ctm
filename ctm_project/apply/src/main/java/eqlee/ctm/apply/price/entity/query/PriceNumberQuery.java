package eqlee.ctm.apply.price.entity.query;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/22
 * @vesion 1.0
 **/
@Data
public class PriceNumberQuery {

   private Long id;

   private String outDate;

   private Double price;

   private Double marketPrice;
}
