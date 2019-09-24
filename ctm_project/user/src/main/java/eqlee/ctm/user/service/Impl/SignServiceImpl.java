package eqlee.ctm.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import eqlee.ctm.user.dao.SignMapper;
import eqlee.ctm.user.entity.Sign;
import eqlee.ctm.user.exception.ApplicationException;
import eqlee.ctm.user.service.ISignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author qf
 * @Date 2019/9/23
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SignServiceImpl extends ServiceImpl<SignMapper, Sign> implements ISignService {


    /**
     * 增加一条签名记录
     * @param sign
     */
    @Override
    public synchronized void insertSign(Sign sign) {
        LambdaQueryWrapper<Sign> queryWrapper = new LambdaQueryWrapper<Sign>()
                .eq(Sign::getAppId,sign.getAppId());
        Sign one = baseMapper.selectOne(queryWrapper);

        if (one != null) {
            sign.setAppId(null);
            int insert = baseMapper.insert(sign);

            if (insert <= 0) {
                log.error("insert sign fail.");
                throw new ApplicationException(CodeType.SERVICE_ERROR,"增加签名记录失败");
            }
        }

        int insert = baseMapper.insert(sign);

        if (insert <= 0) {
            log.error("insert sign fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加签名记录失败");
        }
    }


    /**
     * 查询该条记录
     * @param AppId
     * @return
     */
    @Override
    public Sign queryOne(String AppId) {

        LambdaQueryWrapper<Sign> queryWrapper = new LambdaQueryWrapper<Sign>()
                .eq(Sign::getAppId,AppId);
        return baseMapper.selectOne(queryWrapper);
    }
}
