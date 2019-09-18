package eqlee.ctm.apply.channle.service;

import eqlee.ctm.apply.channle.entity.Channel;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/18 0018
 * @Version 1.0
 */
public interface IChannelService {

    /**
     * 查询所有渠道列表
     * @return
     */
    List<Channel> queryAllChannel();

    /**
     * 增加渠道
     * @return
     */
    void addChannel(String ChannelType);

    /**
     * 修改渠道类型
     * @return
     */
   void updateChannel(Channel channel);

    /**
     * 删除渠道
     * @return
     */
    void deleteChannel(Long id);


    /**
     * 根渠道类型查询渠道列表据
     * @param ChannelType
     * @return
     */
    Channel selectChannelByType(String ChannelType);
}
