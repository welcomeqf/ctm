package eqlee.ctm.apply.guider.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
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

        Long lineId1 = null;
        Long lineId2 = null;
        Long lineId3 = null;
        Long lineId4 = null;
        Long lineId5 = null;
        Long lineId6 = null;
        Long lineId7 = null;


        if (lineNameList.getList() != null) {
           if (lineNameList.getList().size() != 0) {
              for (int i =0; i < lineNameList.getList().size(); i++) {
                 lineId1 = lineNameList.getList().get(0);

                 if (lineNameList.getList().size() >= 2) {
                    lineId2 = lineNameList.getList().get(1);
                 }

                 if (lineNameList.getList().size() >= 3) {
                    lineId3 = lineNameList.getList().get(2);
                 }

                 if (lineNameList.getList().size() >= 4) {
                    lineId4 = lineNameList.getList().get(3);
                 }

                 if (lineNameList.getList().size() >= 5) {
                    lineId5 = lineNameList.getList().get(4);
                 }

                 if (lineNameList.getList().size() >= 6) {
                    lineId6 = lineNameList.getList().get(5);
                 }

                 if (lineNameList.getList().size() >= 7) {
                    lineId6 = lineNameList.getList().get(6);
                 }
              }
           }
        }



        LocalDate localDate = null;

        if (StringUtils.isNotBlank(outDate)) {
            localDate = DateUtil.parseDate(outDate);
        }

        if (StringUtils.isBlank(region)) {
            region = null;
        }

       UserLoginQuery user = localUser.getUser("用户信息");

       String city = null;
        if (user.getCity() != null) {
           city = user.getCity();
        }

       Page<GuiderVo> guiderVoPage = guiderMapper.guiderIndex(page, localDate, region, city, lineId1, lineId2, lineId3, lineId4, lineId5, lineId6, lineId7);

       GuiderCountNumber guiderCountNumber = guiderMapper.queryCountNumberInfo(localDate, region, city, lineId1, lineId2, lineId3, lineId4, lineId5, lineId6, lineId7);

       Map<String,Object> map = new HashMap<>();
       map.put("page",guiderVoPage);
       map.put("number",guiderCountNumber);
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
