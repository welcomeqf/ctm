package eqlee.ctm.apply.guider.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.guider.dao.GuiderMapper;
import eqlee.ctm.apply.guider.entity.vo.ApplyVo;
import eqlee.ctm.apply.guider.entity.vo.GuiderVo;
import eqlee.ctm.apply.guider.service.IGuiderService;
import eqlee.ctm.apply.line.service.ILineService;
import eqlee.ctm.apply.price.service.IPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private GuiderMapper guiderMapper;


    /**
     *
     * @param current
     * @param size
     * @return
     */
    @Override
    public Page<GuiderVo> guiderIndex(Integer current,Integer size,String outDate) {
        Page<GuiderVo> page = new Page<GuiderVo>();
        page.setCurrent(current);
        page.setSize(size);
        return guiderMapper.guiderIndex(page, outDate);
    }



    /**
     *
     * @param current
     * @param size
     * @param outDate
     * @param lineName
     * @return
     */
    @Override
    public Page<ApplyVo> applyIndex(Integer current, Integer size, String outDate, String lineName) {
        Page<ApplyVo> page = new Page<ApplyVo>();
        page.setCurrent(current);
        page.setSize(size);
        return guiderMapper.applyIndex(page, outDate,lineName);
    }

}
