package com.yq.entity.exl;

import lombok.Data;

/**
 * @author qf
 * @date 2020/1/13
 * @vesion 1.0
 **/
@Data
public class OutInfoExl {

   private Long id;

   /**
    * 支出消费名字
    */
   private String outName;

   /**
    * 支出金额
    */
   private Double outPrice;

   /**
    * 支出消费凭证
    */
   private String picture;
}
