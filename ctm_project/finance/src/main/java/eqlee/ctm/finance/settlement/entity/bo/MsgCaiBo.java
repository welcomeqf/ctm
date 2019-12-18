package eqlee.ctm.finance.settlement.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/13
 * @vesion 1.0
 **/
@Data
public class MsgCaiBo {

   private Long toId;

   private Long createId;

   private String msg;

   private Integer msgType;
}
