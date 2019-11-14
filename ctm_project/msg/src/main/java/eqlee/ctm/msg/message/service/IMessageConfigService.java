package eqlee.ctm.msg.message.service;

import eqlee.ctm.msg.message.entity.vo.MsgConfigVo;

/**
 * @author qf
 * @date 2019/11/13
 * @vesion 1.0
 **/
public interface IMessageConfigService {


   /**
    * 增加消息配置
    * @param msgConfigVo
    */
   void insertMsgConfig (MsgConfigVo msgConfigVo);

   /**
    * 查询具体的消息配置
    * @param msgType
    * @return
    */
   MsgConfigVo queryMsgConfig (Integer msgType);
}
