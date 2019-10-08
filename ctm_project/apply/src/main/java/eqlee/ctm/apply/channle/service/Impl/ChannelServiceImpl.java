package eqlee.ctm.apply.channle.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.utils.IdGenerator;
import eqlee.ctm.apply.channle.dao.ChannleMapper;
import eqlee.ctm.apply.channle.entity.Channel;
import eqlee.ctm.apply.channle.service.IChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/18 0018
 * @Version 1.0
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ChannelServiceImpl extends ServiceImpl<ChannleMapper,Channel>implements IChannelService {


    /**
     * 查询所有渠道
     * @return
     */
    @Override
    public List<Channel> queryAllChannel() {
        return baseMapper.selectList(null);
    }


    /**
     * 增加渠道
     * @param ChannelType
     */
    @Override
    public void addChannel(String ChannelType) {
        IdGenerator idGenerator = new IdGenerator();
        Channel channel = new Channel();
        channel.setId(idGenerator.getNumberId());
        channel.setChannelType(ChannelType);
        int add = baseMapper.insert(channel);
        if(add<=0){
            log.error("addChannel fail");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加渠道失败");
        }
    }


    /**
     * 更新渠道信息
     * @param channel
     */
    @Override
    public void updateChannel(Channel channel) {
        int update = baseMapper.updateById(channel);
        if(update<=0){
            log.error("updateChannel fail");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"更新渠道失败");
        }

    }


    /**
     * 根据id删除渠道
     * @param id
     */
    @Override
    public void deleteChannel(Long id) {
        int delete = baseMapper.deleteById(id);
        if(delete<=0){
            log.error("deleteChannel fail");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"删除渠道失败");
        }
    }


    /**
     * 通过渠道类型选择渠道
     * @param ChannelType
     * @return
     */
    @Override
    public Channel selectChannelByType(String ChannelType) {
        LambdaQueryWrapper<Channel> queryWrapper = new LambdaQueryWrapper<Channel>()
                .eq(Channel::getChannelType,ChannelType);
        return baseMapper.selectOne(queryWrapper);
    }
}
