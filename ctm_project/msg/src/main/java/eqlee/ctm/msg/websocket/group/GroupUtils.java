package eqlee.ctm.msg.websocket.group;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qf
 * @date 2019/12/3
 * @vesion 1.0
 **/
@Component
public class GroupUtils {

   Map<Long,Channel> map = new ConcurrentHashMap<>();

   /**
    * 将id和channel管理
    * @param id
    * @param channel
    */
   public void put (Long id, Channel channel) {
      map.put(id, channel);
   }

   /**
    * 通过id得到通道
    * @param id
    * @return
    */
   public Channel getChannel (Long id) {
      return map.get(id);
   }

   /**
    * 通过id移除通道
    * @param id
    */
   public Channel removeById (Long id) {
      return map.remove(id);
   }

   /**
    * 查询当前连接的长度
    * @return
    */
   public int size () {
      return map.size();
   }

   /**
    * 移除所有连接
    * @param channel
    */
   public void removeByChannel (Channel channel) {

      Set<Map.Entry<Long, Channel>> entries = map.entrySet();

      for (Map.Entry<Long, Channel> entry : new HashSet<>(entries)) {
         if(entry.getValue() == channel){
            entries.remove(entry.getKey());
         }
      }

   }

}
