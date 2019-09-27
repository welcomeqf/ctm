package eqlee.ctm.finance.settlement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import eqlee.ctm.finance.exception.ApplicationException;
import eqlee.ctm.finance.settlement.dao.NumberMapper;
import eqlee.ctm.finance.settlement.entity.Number;
import eqlee.ctm.finance.settlement.service.INumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class NumberServiceImpl extends ServiceImpl<NumberMapper, Number> implements INumberService {

    /**
     * 增加人员数
     * @param number
     */
    @Override
    public void insertNumber(Number number) {
        int insert = baseMapper.insert(number);

        if (insert <= 0) {
            log.error("insert number fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加人员数失败");
        }
    }
}
