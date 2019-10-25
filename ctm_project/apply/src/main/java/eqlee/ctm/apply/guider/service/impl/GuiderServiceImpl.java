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
    public Page<GuiderVo> guiderIndex(Page<GuiderVo> page,String outDate,String lineName) {

        if (StringUtils.isBlank(outDate) && StringUtils.isBlank(lineName)) {
            //日期和线路名为空
            return guiderMapper.guiderIndex(page);
        }

        if (StringUtils.isBlank(outDate) && StringUtils.isNotBlank(lineName)) {
            //根据线路名查询
            return guiderMapper.guiderIndexByLine(page,lineName);
        }

        if (StringUtils.isNotBlank(outDate) && StringUtils.isBlank(lineName)) {
            //根据日期查询
            LocalDate localDate = DateUtil.parseDate(outDate);
            return guiderMapper.guiderIndexByTime(page,localDate);
        }

        LocalDate localDate = DateUtil.parseDate(outDate);
        return guiderMapper.guiderIndexByLineAndTime(page,localDate,lineName);
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
    public Page<ApplyVo> applyIndex(Integer current, Integer size, String lineName, String outDate) {
        Page<ApplyVo> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        LocalDate localDate = DateUtil.parseDate(outDate);

        Line line = lineService.queryLineByName(lineName);

        if (line == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该线路有可能已被删除");
        }

        return guiderMapper.applyIndex(page, line.getId(),localDate);
    }

}
