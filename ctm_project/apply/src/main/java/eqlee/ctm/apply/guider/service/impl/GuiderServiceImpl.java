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
    public Map<String,Object> guiderIndex(Page<GuiderVo> page, String outDate, GuiderList lineNameList, String region, Integer selectNot,Long orderId,String cityName) {

        LocalDate localDate = null;

        if (StringUtils.isNotBlank(outDate)) {
            localDate = DateUtil.parseDate(outDate);
        }

        if (StringUtils.isBlank(region)) {
            region = null;
        }

       UserLoginQuery user = localUser.getUser();


       List<String> cityList = new ArrayList<>();

       if (user.getCity().size() > 0 && StringUtils.isEmpty(cityName)) {
          for (CityJwtBo bo : user.getCity()) {
             cityList.add(bo.getCity());
          }
       }
       else if(StringUtils.isNotEmpty(cityName)){
           cityList.addAll(java.util.Arrays.asList(cityName.split("\\,")));
       }
       else {
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

       Page<GuiderVo> guiderVoPage1 = guiderMapper.guiderIndex2(page, localDate, region, cityList, list, selectNot);

       GuiderCountNumber guiderCountNumber1 = guiderMapper.queryCountNumberInfo2(localDate, region,cityList,list,0, selectNot);

       GuiderCountNumber guiderCountNumber2 = guiderMapper.queryCountNumberInfo2(localDate, region,cityList,list,1, selectNot);

       if (guiderCountNumber1 == null) {
          guiderCountNumber1 = new GuiderCountNumber();
          guiderCountNumber1.setNumberCount(0);
       }

       if (guiderCountNumber2 == null) {
          guiderCountNumber2 = new GuiderCountNumber();
          guiderCountNumber1.setTotalNumber(0);
          guiderCountNumber1.setOldNumber(0);
          guiderCountNumber1.setChildNumber(0);
          guiderCountNumber1.setBabyNumber(0);
          guiderCountNumber1.setAdultNumber(0);
       }

       guiderCountNumber1.setTotalNumber(guiderCountNumber2.getNumberCount());
       guiderCountNumber1.setOldNumber(guiderCountNumber2.getOldNumber());
       guiderCountNumber1.setChildNumber(guiderCountNumber2.getChildNumber());
       guiderCountNumber1.setBabyNumber(guiderCountNumber2.getBabyNumber());
       guiderCountNumber1.setAdultNumber(guiderCountNumber2.getAdultNumber());

       Map<String,Object> map = new HashMap<>();
       map.put("page",guiderVoPage1);
       map.put("number",guiderCountNumber1);
       //如果有传订单id则表示为导澈再选人进入的给出导游已选人记录
       if(orderId != null){
           //UserLoginQuery users = localUser.getUser("用户信息");
           GuiderCountNumber guiderCountNumber3 = guiderMapper.querySelectNumber(orderId);
           map.put("selectNumber",guiderCountNumber3);
       }
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
