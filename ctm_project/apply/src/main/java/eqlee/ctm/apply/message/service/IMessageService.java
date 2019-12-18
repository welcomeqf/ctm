package eqlee.ctm.apply.message.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.message.entity.query.MsgInfoQuery;
import eqlee.ctm.apply.message.entity.query.MsgQuery;
import eqlee.ctm.apply.message.entity.vo.MsgAddVo;
import eqlee.ctm.apply.message.entity.vo.MsgCountVo;
import eqlee.ctm.apply.message.entity.vo.MsgVo;

import java.util.List;

/**
 * @author qf
 * @date 2019/11/13
 * @vesion 1.0
 **/
public interface IMessageService {

   /**
    * 增加消息记录
    * @param msgVo
    */
   void insertMsg(MsgVo msgVo);

   /**
    * 修改查看状态
    * @param id
    */
   void updateMsg(Integer id);

   /**
    *  根据消息类型查询所有未读信息
    * @param page
    * @param msgType
    * @param toId
    * @return
    */
   Page<MsgQuery> pageMsg(Page<MsgQuery> page, Integer msgType, Long toId);

   /**
    * 查询未读消息
    * @param msgType
    * @param toId
    * @return
    */
   List<MsgInfoQuery> listMsgInfo(Integer msgType, Long toId);

   /**
    * 查询消息条数
    * @param msgType
    * @param toId
    * @return
    */
   MsgCountVo queryCount(Integer msgType, Long toId, String msg);

   /**
    * 批量增加同一角色下所有用户的消息记录
    * @param msgAddVo
    */
   void addAllMsg(MsgAddVo msgAddVo);


   /**
    * 修改当前用户所有未读消息状态
    * @param toId
    */
   void updateUserAllMsg(Long toId);


}
