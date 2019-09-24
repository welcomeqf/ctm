package eqlee.ctm.apply.guider.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import eqlee.ctm.apply.guider.dao.GuiderMapper;
import eqlee.ctm.apply.guider.entity.vo.GuiderVo;
import eqlee.ctm.apply.guider.service.IGuiderService;
import eqlee.ctm.apply.line.service.ILineService;
import eqlee.ctm.apply.price.entity.vo.PriceVo;
import eqlee.ctm.apply.price.service.IPriceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Claire
 * @Date 2019/9/23 0023
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GuiderServiceImpl implements IGuiderService {

    @Autowired
    private ILineService lineService;

    @Autowired
    private IPriceService priceService;

    @Autowired
    private GuiderMapper guiderMapper;


    /**
     *
     * @param current
     * @param size
     * @return
     */
    @Override
    public Page<GuiderVo> guiderIndex(Integer current,Integer size) {


        Page<GuiderVo> page = new Page<GuiderVo>();
        page.setCurrent(current);
        page.setSize(size);
        return guiderMapper.guiderIndex(page);
    }
}
