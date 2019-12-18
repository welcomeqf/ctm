package eqlee.ctm.apply.guider.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.utils.DateUtil;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.guider.dao.GuiderMapper;
import eqlee.ctm.apply.guider.entity.vo.ApplyVo;
import eqlee.ctm.apply.guider.entity.vo.GuiderVo;
import eqlee.ctm.apply.guider.service.IGuiderService;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.service.ILineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Autowired
    private ILineService lineService;

    /**
     *  导游选人首页
     * @param page
     * @param outDate
     * @param lineName
     * @return
     */
    @Override
    public Page<GuiderVo> guiderIndex(Page<GuiderVo> page,String outDate,String lineName, String region) {

        if (StringUtils.isBlank(lineName)) {
            lineName = null;
        }

        LocalDate localDate = null;

        if (StringUtils.isNotBlank(outDate)) {
            localDate = DateUtil.parseDate(outDate);
        }

        if (StringUtils.isBlank(region)) {
            region = null;
        }
        return guiderMapper.guiderIndex(page,localDate,lineName,region);
    }



    /**
     *  导游选人
     * @param current
     * @param size
     * @param outDate
     * @param lineName
     * @return
     */
    @Override
    public Page<ApplyVo> applyIndex(Integer current, Integer size, String lineName, String outDate, String region) {
        Page<ApplyVo> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        LocalDate localDate = DateUtil.parseDate(outDate);

        Line line = lineService.queryLineByName(lineName);

        if (line == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该线路有可能已被删除");
        }

        if (StringUtils.isBlank(region)) {
            return guiderMapper.applyIndex(page, line.getId(),localDate);
        }

        //通过区域进行摔选
        return guiderMapper.queryApplyPerson(page,line.getId(),localDate,region);
    }

}
