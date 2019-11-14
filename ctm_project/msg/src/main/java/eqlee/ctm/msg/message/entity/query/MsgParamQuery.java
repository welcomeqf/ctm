package eqlee.ctm.msg.message.entity.query;

import com.yq.data.PageUtils;
import lombok.Data;

/**
 * @author qf
 * @date 2019/11/13
 * @vesion 1.0
 **/
@Data
public class MsgParamQuery extends PageUtils {

   private Integer msgType;

   private Long toId;
}
