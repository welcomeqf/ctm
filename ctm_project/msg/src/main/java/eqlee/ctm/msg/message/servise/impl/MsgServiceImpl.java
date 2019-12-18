package eqlee.ctm.msg.message.servise.impl;

import eqlee.ctm.msg.message.dao.MsgMapper;
import eqlee.ctm.msg.message.entity.MsgIdList;
import eqlee.ctm.msg.message.entity.MsgResult;
import eqlee.ctm.msg.message.servise.IMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qf
 * @date 2019/12/13
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MsgServiceImpl implements IMsgService {

   @Autowired
   private MsgMapper msgMapper;

   @Override
   public List<MsgResult> queryMsg() {
      return msgMapper.queryMsg();
   }

   @Override
   public void updateMsgStatus(int id) {
      msgMapper.updateMsgStatus(id);
   }
}
