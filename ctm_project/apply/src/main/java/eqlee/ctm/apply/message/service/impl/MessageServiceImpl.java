package eqlee.ctm.apply.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.utils.DateUtil;
import eqlee.ctm.apply.message.dao.MessageMapper;
import eqlee.ctm.apply.message.entity.Message;
import eqlee.ctm.apply.message.entity.query.MsgInfoQuery;
import eqlee.ctm.apply.message.entity.query.MsgQuery;
import eqlee.ctm.apply.message.entity.vo.MsgAddVo;
import eqlee.ctm.apply.message.entity.vo.MsgConfigVo;
import eqlee.ctm.apply.message.entity.vo.MsgCountVo;
import eqlee.ctm.apply.message.entity.vo.MsgVo;
import eqlee.ctm.apply.message.service.IMessageConfigService;
import eqlee.ctm.apply.message.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qf
 * @date 2019/11/13
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {


   @Autowired
   private IMessageConfigService configService;

   /**
    * 增加消息记录
    * @param msgVo
    */
   @Override
   public void insertMsg(MsgVo msgVo) {
      Message message = new Message();
      message.setToId(msgVo.getToId());
      message.setCreateId(message.getCreateId());
      message.setMsgType(msgVo.getMsgType());
      message.setMsg(msgVo.getMsg());
      int insert = baseMapper.insert(message);

      if (insert <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "增加失败");
      }
   }

   /**
    * 修改查看状态
    * @param id
    */
   @Override
   public void updateMsg(Integer id) {
      Message message = new Message();
      message.setId(id);
      message.setIsRead(true);
      LocalDateTime now = LocalDateTime.now();
      message.setReadDate(now);

      int update = baseMapper.updateById(message);

      if (update <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
      }
   }

   /**
    * 根据消息类型查询所有未读信息
    * @param msgType
    * @return
    */
   @Override
   public Page<MsgQuery> pageMsg(Page<MsgQuery> page, Integer msgType, Long toId) {
      return baseMapper.pageMsg(page, msgType, toId);
   }

   /**
    * 查询所有未读消息详情
    * @param msgType
    * @param toId
    * @return
    */
   @Override
   public List<MsgInfoQuery> listMsgInfo(Integer msgType, Long toId) {
      //查询所有未读同一类型的数据
      LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<Message>()
            .eq(Message::getMsgType,msgType)
            .eq(Message::getToId,toId)
            .eq(Message::getIsRead,0)
            .orderByDesc(Message::getCreateDate);
      List<Message> messages = baseMapper.selectList(wrapper);

      MsgConfigVo vo = configService.queryMsgConfig(msgType);

      List<MsgInfoQuery> list = new ArrayList<>();
      for (Message message : messages) {
         //装配MsgInfoQuery
         MsgInfoQuery query = new MsgInfoQuery();
         query.setId(message.getId());
         String date = DateUtil.formatDateTime(message.getCreateDate());
         String readDate = DateUtil.formatDateTime(message.getReadDate());
         query.setCreateDate(date);
         query.setCreateId(message.getCreateId());
         query.setMsgContent(vo.getMsgContent());
         query.setMsgRemark(vo.getMsgRemark());
         query.setMsgTitle(vo.getMsgTitle());
         query.setReadDate(readDate);
         query.setToId(message.getToId());
         query.setMsg(message.getMsg());
         list.add(query);
      }

      return list;
   }


   /**
    * 查询消息条数
    * @param msgType
    * @param toId
    * @return
    */
   @Override
   public MsgCountVo queryCount(Integer msgType, Long toId, String msg) {

      //查询所有未读同一类型的数据
      LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<Message>()
            .eq(Message::getMsgType,msgType)
            .eq(Message::getToId,toId)
            .eq(Message::getIsRead,0)
            .eq(Message::getMsg,msg);

      Integer integer = baseMapper.selectCount(wrapper);

      MsgCountVo vo = new MsgCountVo();

      if (integer >= 99) {
         integer = 99;
      }

      vo.setCount(integer);

      return vo;
   }

   /**
    * 批量增加同一角色下所有用户的消息记录
    * @param msgAddVo
    */
   @Override
   public void addAllMsg(MsgAddVo msgAddVo) {

      List<MsgVo> list = new ArrayList<>();

      for (Long toId : msgAddVo.getToId()) {
         //装配所有消息
         MsgVo vo = new MsgVo();
         vo.setCreateId(msgAddVo.getCreateId());
         vo.setMsg(msgAddVo.getMsg());
         vo.setMsgType(msgAddVo.getMsgType());
         vo.setToId(toId);
         list.add(vo);
      }

      //批量增加
      Integer integer = baseMapper.addAllMsg(list);

      if (integer <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "批量添加失败");
      }

   }

   /**
    * 修改当前用户所有未读消息状态
    * @param toId
    */
   @Override
   public void updateUserAllMsg(Long toId) {
      LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<Message>()
            .eq(Message::getToId,toId)
            .eq(Message::getIsRead,0);

      Message message = new Message();
      message.setIsRead(true);
      LocalDateTime now = LocalDateTime.now();
      message.setReadDate(now);
      int update = baseMapper.update(message, wrapper);

      if (update <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "修改当前用户未读状态失败");
      }
   }
}
