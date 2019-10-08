package eqlee.ctm.finance.settlement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import eqlee.ctm.finance.settlement.dao.OutFinanceMapper;
import eqlee.ctm.finance.settlement.entity.Outcome;
import eqlee.ctm.finance.settlement.service.IOutFinanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支出Service
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OutFinanceServiceImpl extends ServiceImpl<OutFinanceMapper, Outcome> implements IOutFinanceService {


    /**
     * 提交支出表
     * @param outcome
     */
    @Override
    public void insertOutFinance(Outcome outcome) {
        int insert = baseMapper.insert(outcome);

        if (insert <= 0) {
            log.error("insert outcome fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"提交失败");
        }
    }
}
