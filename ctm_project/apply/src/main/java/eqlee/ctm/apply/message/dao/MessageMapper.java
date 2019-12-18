package eqlee.ctm.apply.message.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.message.entity.Message;
import eqlee.ctm.apply.message.entity.query.MsgQuery;
import eqlee.ctm.apply.message.entity.vo.MsgVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qf
 * @date 2019/11/13
 * @vesion 1.0
 **/
@Component
public interface MessageMapper extends IBaseMapper<Message> {

   /**
    * 根据消息类型查询所有未读信息
    * @param page
    * @param msgType
    * @param toId
    * @return
    */
   Page<MsgQuery> pageMsg(Page<MsgQuery> page,
                          @Param("msgType") Integer msgType,
                          @Param("toId") Long toId);

   /**
    * 批量增加同一角色下的所有用户
    * @param list
    * @return
    */
   Integer addAllMsg(List<MsgVo> list);
}
