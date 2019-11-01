package eqlee.ctm.api.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import eqlee.ctm.api.code.dao.PayInfoMapper;
import eqlee.ctm.api.code.entity.PayInfo;
import eqlee.ctm.api.code.entity.query.PayInfoQuery;
import eqlee.ctm.api.code.service.IPayInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author qf
 * @Date 2019/10/30
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PayInfoServiceImpl extends ServiceImpl<PayInfoMapper, PayInfo> implements IPayInfoService {


    /**
     * 增加支付信息
     * @param payInfo
     * @return
     */
    @Override
    public Integer insertPayInfo(PayInfo payInfo) {
        return baseMapper.insert(payInfo);
    }

    /**
     * 查询该用户的支付信息
     * @param userId
     * @return
     */
    @Override
    public PayInfoQuery queryPayInfo(Long userId) {
        LambdaQueryWrapper<PayInfo> wrapper = new LambdaQueryWrapper<PayInfo>()
                .eq(PayInfo::getUserId,userId);
        PayInfo payInfo = baseMapper.selectOne(wrapper);
        PayInfoQuery query = new PayInfoQuery();
        query.setOpenId(payInfo.getOpenId());
        return query;
    }

    /**
     * 查询该用户是否有数据
     * @param userId
     * @return
     */
    @Override
    public Boolean queryPay(Long userId) {
        LambdaQueryWrapper<PayInfo> wrapper = new LambdaQueryWrapper<PayInfo>()
                .eq(PayInfo::getUserId,userId);
        PayInfo payInfo = baseMapper.selectOne(wrapper);
        if (payInfo != null) {
            return true;
        }
        return false;
    }

    /**
     * 删除该用户的支付授权信息
     * @param id
     * @return
     */
    @Override
    public Integer deletePayInfo(Long id) {
        LambdaQueryWrapper<PayInfo> wrapper = new LambdaQueryWrapper<PayInfo>()
                .eq(PayInfo::getUserId,id);
        return baseMapper.delete(wrapper);
    }
}
