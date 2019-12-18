package eqlee.ctm.msg.message.servise;

import eqlee.ctm.msg.message.entity.MsgIdList;
import eqlee.ctm.msg.message.entity.MsgResult;

import java.util.List;

/**
 * @author qf
 * @date 2019/12/13
 * @vesion 1.0
 **/
public interface IMsgService {


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
