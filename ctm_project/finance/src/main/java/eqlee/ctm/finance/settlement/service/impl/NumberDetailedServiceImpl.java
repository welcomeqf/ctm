package eqlee.ctm.finance.settlement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import eqlee.ctm.finance.exception.ApplicationException;
import eqlee.ctm.finance.settlement.dao.NumberDetailedMapper;
import eqlee.ctm.finance.settlement.entity.NumberDetailed;
import eqlee.ctm.finance.settlement.entity.vo.ContectUserNumberVo;
import eqlee.ctm.finance.settlement.service.INumberDetailedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class NumberDetailedServiceImpl extends ServiceImpl<NumberDetailedMapper, NumberDetailed> implements INumberDetailedService {


    /**
     * 批量插入未付款代收人员信息
     * @param vo
     */
    @Override
    public void insertAllNumberDetailed(List<ContectUserNumberVo> vo) {

        Integer integer = baseMapper.insertNumberDetailed(vo);

        if (integer <= 0) {
            log.error("insert no pay person fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加未付款代付信息失败");
        }
    }
}
