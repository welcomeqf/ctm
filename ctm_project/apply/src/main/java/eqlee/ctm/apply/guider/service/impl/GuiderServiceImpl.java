package eqlee.ctm.apply.guider.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.CityJwtBo;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.DateUtil;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.guider.dao.GuiderMapper;
import eqlee.ctm.apply.guider.entity.vo.*;
import eqlee.ctm.apply.guider.service.IGuiderService;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.service.ILineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private LocalUser localUser;

    /**
     *  导游选人首页
     * @param page
     * @param outDate
     * @param lineNameList
     * @return
     */
    @Override
    public Map<String,Object> guiderIndex(Page<GuiderVo> page, String outDate, GuiderList lineNameList, String region) {

        LocalDate localDate = null;

        if (StringUtils.isNotBlank(outDate)) {
            localDate = DateUtil.parseDate(outDate);
        }

        if (StringUtils.isBlank(region)) {
            region = null;
        }

       UserLoginQuery user = localUser.getUser("用户信息");


       List<String> cityList = new ArrayList<>();

       if (user.getCity().size() > 0) {
          for (CityJwtBo bo : user.getCity()) {
             cityList.add(bo.getCity());
          }
       } else {
          cityList = null;
       }

       List<Long> list = new ArrayList<>();

       if (lineNameList.getList().size() > 0) {
          for (Long aLong : lineNameList.getList()) {
             list.add(aLong);
          }
       } else {
          list = null;
       }

       Page<GuiderVo> guiderVoPage1 = guiderMapper.guiderIndex2(page, localDate, region, cityList, list);

       GuiderCountNumber guiderCountNumber1 = guiderMapper.queryCountNumberInfo2(localDate, region,cityList,list);

       Map<String,Object> map = new HashMap<>();
       map.put("page",guiderVoPage1);
       map.put("number",guiderCountNumber1);
       return map;
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
