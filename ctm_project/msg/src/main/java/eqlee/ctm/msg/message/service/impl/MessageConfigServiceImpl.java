package eqlee.ctm.msg.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import eqlee.ctm.msg.message.dao.MessageConfigMapper;
import eqlee.ctm.msg.message.entity.MessageConfig;
import eqlee.ctm.msg.message.entity.vo.MsgConfigVo;
import eqlee.ctm.msg.message.service.IMessageConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qf
 * @date 2019/11/13
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MessageConfigServiceImpl extends ServiceImpl<MessageConfigMapper, MessageConfig> implements IMessageConfigService {


   /**
    * 增加消息配置
    * @param msgConfigVo
    */
   @Override
   public synchronized void insertMsgConfig(MsgConfigVo msgConfigVo) {

      LambdaQueryWrapper<MessageConfig> wrapper = new LambdaQueryWrapper<MessageConfig>()
            .eq(MessageConfig::getMsgType,msgConfigVo.getMsgType());
      MessageConfig messageConfig = baseMapper.selectOne(wrapper);

      if (messageConfig != null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "该类型已经存在");
      }

      MessageConfig config = new MessageConfig();
      config.setMsgContent(msgConfigVo.getMsgContent());
      config.setMsgRemark(msgConfigVo.getMsgRemark());
      config.setMsgTitle(msgConfigVo.getMsgTitle());
      config.setMsgType(msgConfigVo.getMsgType());

      int insert = baseMapper.insert(config);

      if (insert <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "增加失败");
      }
   }

   /**
    * 查询消息配置
    * @param msgType
    * @return
    */
   @Override
   public MsgConfigVo queryMsgConfig(Integer msgType) {
      LambdaQueryWrapper<MessageConfig> wrapper = new LambdaQueryWrapper<MessageConfig>()
            .eq(MessageConfig::getMsgType,msgType);
      MessageConfig messageConfig = baseMapper.selectOne(wrapper);

      MsgConfigVo vo = new MsgConfigVo();
      vo.setMsgContent(messageConfig.getMsgContent());
      vo.setMsgRemark(messageConfig.getMsgRemark());
      vo.setMsgTitle(messageConfig.getMsgTitle());
      vo.setMsgType(messageConfig.getMsgType());
      return vo;
   }
}
