package eqlee.ctm.msg.message.dao;

import eqlee.ctm.msg.message.entity.MsgIdList;
import eqlee.ctm.msg.message.entity.MsgResult;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qf
 * @date 2019/12/13
 * @vesion 1.0
 **/
@Component
public interface MsgMapper {

   /**
    * 查询所有未读消息
    * @return
    */
   List<MsgResult> queryMsg ();

   /**
    * 批量修改状态
    * @param id
    */
   void updateMsgStatus (int id);
}
