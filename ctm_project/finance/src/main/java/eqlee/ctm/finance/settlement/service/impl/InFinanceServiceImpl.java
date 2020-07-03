package eqlee.ctm.finance.settlement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.CityJwtBo;
import com.yq.jwt.entity.PrivilegeMenuQuery;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.DateUtil;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.finance.other.service.IOtherService;
import eqlee.ctm.finance.settlement.dao.InFinanceMapper;
import eqlee.ctm.finance.settlement.entity.Income;
import eqlee.ctm.finance.settlement.entity.Number;
import eqlee.ctm.finance.settlement.entity.Outcome2;
import eqlee.ctm.finance.settlement.entity.bo.*;
import eqlee.ctm.finance.settlement.entity.order.OrderQuery;
import eqlee.ctm.finance.settlement.entity.query.*;
import eqlee.ctm.finance.settlement.entity.vo.ExamineResultVo;
import eqlee.ctm.finance.settlement.entity.vo.FinanceVo;
import eqlee.ctm.finance.settlement.service.*;
import eqlee.ctm.finance.settlement.vilidata.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收入service
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class InFinanceServiceImpl extends ServiceImpl<InFinanceMapper, Income> implements IInFinanceService {

    @Autowired
    private LocalUser localUser;

    @Autowired
    private IOut2FinanceService  out2FinanceService;


    @Autowired
    private INumberService numberService;

    IdGenerator idGenerator = new IdGenerator();

    private final String EXA_DAI = "待审核";

    private final String EXA_YI = "已通过";

    private final String EXA_WEI = "已拒绝";

    /**
     * 增加导游消费情况
     * @param vo
     */
    @Override
    public void insertFinance(FinanceVo vo) {
        //获取用户信息
        UserLoginQuery user = localUser.getUser("用户信息");


        if (vo.getStatus() != 2 && vo.getStatus() != 3) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "提交失败，请求参数有误!");
        }

        //判断是否重复交账
        LambdaQueryWrapper<Income> wrapper = new LambdaQueryWrapper<Income>()
                .eq(Income::getOutDate,DateUtil.parseDate(vo.getOutDate()))
                .eq(Income::getCreateUserId,user.getId());
        Income income = baseMapper.selectOne(wrapper);
        if(income != null && vo.getStatus() == 3){
            throw new ApplicationException(CodeType.SERVICE_ERROR, "当前账单已提交，请勿重复提交！");
        }

        //判断是否第一次提交
        if (vo.getStatus() == 2) {
            //不是第一次提交


            if (income != null) {
                income.setLineName(vo.getLineName());
                income.setCarNo(vo.getCarNo());
                income.setStatus(0);
                income.setSettlementPrice(vo.getGaiMoney());
                income.setOtherInPrice(vo.getOtherInPrice());
                Double all = vo.getOtherInPrice() + vo.getGaiMoney() + vo.getAllPrice();
                income.setAllInPrice(all);
                int update = baseMapper.updateById(income);

                if (update <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "提交失败");
                }
            }else{
                throw new ApplicationException(CodeType.SERVICE_ERROR, "请使用原始交账人进行本操作！");
            }

            Number number = numberService.queryById(income.getNumberId());

            if (number != null) {
                number.setAllDoNumber(vo.getAllDoNumber());
                number.setTreeAdultNumber(vo.getTreeAdultNumber());
                number.setTreeBabyNumber(vo.getTreeBabyNumber());
                number.setTreeChildNumber(vo.getTreeChildNumber());
                number.setTreeOldNumber(vo.getTreeOldNumber());
                number.setTrueAllNumber(vo.getTrueAllNumber());
                number.setAllPrice(vo.getAllPrice());
                number.setMonthPrice(vo.getMonthPrice());

                numberService.updateNumber(number);
            }

            //支出表

//            List<OutComeInfoBo> list = new ArrayList<>();
//            List<OutComeInfoBo> addList = new ArrayList<>();
            if (vo.getOutList().size() > 0) {
                for (OutComeParamInfo info : vo.getOutList()) {

                    if (info.getId() == null) {
                        OutComeInfoBo bo = new OutComeInfoBo();
                        //增加
                        bo.setId(idGenerator.getNumberId());
                        bo.setIncomeId(income.getId());
                        bo.setOutName(info.getOutName());
                        bo.setOutPrice(info.getOutPrice());
                        bo.setPicture(info.getPicture());
                        bo.setCreateUserId(user.getId());
                        bo.setUpdateUserId(user.getId());
                        out2FinanceService.insertOut(bo);
                    }

                    if (info.getId() != null) {
                        OutComeInfoBo bo = new OutComeInfoBo();
                        //修改
                        bo.setId(info.getId());
                        bo.setOutName(info.getOutName());
                        bo.setOutPrice(info.getOutPrice());
                        bo.setPicture(info.getPicture());
                        out2FinanceService.upOut2Info(bo);
                    }

                }
//                //批量修改数据库
//                out2FinanceService.updateOut2Info(list);
            }


        }

        if (vo.getStatus() == 3) {
            //第一次提交

            //装配收入表
            income = new Income();
            long numberId = idGenerator.getNumberId();
            income.setId(numberId);
            income.setCreateUserId(user.getId());
            income.setGuideTel(user.getTel());
            income.setGuideName(user.getCname());
            income.setOrderId(vo.getOrderId());

            if (vo.getOrderId() != null) {
                OrderQuery query = baseMapper.queryOrderCity(vo.getOrderId());
                income.setCity(query.getCity());
            }

            income.setLineName(vo.getLineName());
            income.setCarNo(vo.getCarNo());
            //加入到人数表以及人数明细表
            Number number = new Number();
            Long id = idGenerator.getNumberId();
            number.setId(id);
            number.setCreateUserId(user.getId());
            number.setAllDoNumber(vo.getAllDoNumber());
            number.setTreeAdultNumber(vo.getTreeAdultNumber());
            number.setTreeBabyNumber(vo.getTreeBabyNumber());
            number.setTreeChildNumber(vo.getTreeChildNumber());
            number.setTreeOldNumber(vo.getTreeOldNumber());
            number.setTrueAllNumber(vo.getTrueAllNumber());
            number.setUpdateUserId(user.getId());

            //增加--
            number.setAllPrice(vo.getAllPrice());
            number.setMonthPrice(vo.getMonthPrice());
            //增加人数表
            numberService.insertNumber(number);

            //

            income.setOutDate(DateUtil.parseDate(vo.getOutDate()));
            //结算价(面收金额)
            income.setSettlementPrice(vo.getGaiMoney());
            income.setUpdateUserId(user.getId());
            income.setNumberId(id);
            income.setOtherInPrice(vo.getOtherInPrice());

//        Double price = 0.0;
//        for (OtherBo bo : vo.getOtherInPrice()) {
//            price = price + bo.getPrice();
//        }
            //总收入
            Double all = vo.getOtherInPrice() + vo.getGaiMoney() + vo.getAllPrice();
            income.setAllInPrice(all);

            int insert = baseMapper.insert(income);

            if (insert<= 0) {
                log.error("insert income fail.");
                throw new ApplicationException(CodeType.SERVICE_ERROR,"提交收入表失败");
            }

            //装配其他收入表

//        if (vo.getOtherInPrice().size() > 0) {
//            List<OtherUpdateVo> list = new ArrayList<>();
//            for (OtherBo otherBo : vo.getOtherInPrice()) {
//                OtherUpdateVo updateVo = new OtherUpdateVo();
//                updateVo.setId(otherBo.getId());
//                updateVo.setOtherPrice(otherBo.getPrice());
//                updateVo.setIncomeId(numberId);
//                //updateOther
//                list.add(updateVo);
//            }
//            otherService.updateOther(list);
//        }

            //装配支出表
            List<OutComeInfoBo> list = new ArrayList<>();
            if (vo.getOutList().size() > 0) {
                for (OutComeParamInfo info : vo.getOutList()) {
                    OutComeInfoBo bo = new OutComeInfoBo();
                    bo.setId(idGenerator.getNumberId());
                    bo.setIncomeId(numberId);
                    bo.setOutName(info.getOutName());
                    bo.setOutPrice(info.getOutPrice());
                    bo.setPicture(info.getPicture());
                    bo.setCreateUserId(user.getId());
                    bo.setUpdateUserId(user.getId());
                    list.add(bo);
                }

                //批量增加数据库
                out2FinanceService.insertOut2Info(list);
            }


            //标记该订单已完成
            int i = baseMapper.updateIsFinash(DateUtil.parseDate(vo.getOutDate()), user.getId(), LocalDateTime.now());

            if (i <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"请使用当前订单导游账号登陆操作！");
            }

        }




//        List<UserIdBo> idList = (List<UserIdBo>) httpUtils.queryAllAdminInfo();
//
//        if (idList.size() == 0) {
//            throw new ApplicationException(CodeType.SERVICE_ERROR, "请将财务的角色名设置为财务");
//        }
//
//        for (int j = 0; j <= idList.size()-1; j++) {
//            UserIdBo bo = JSONObject.parseObject(String.valueOf(idList.get(j)),UserIdBo.class);
//
//            //消息提醒财务审核
//            //增加消息提醒的记录
//            Integer integer = baseMapper.insertMsg(bo.getAdminId(),user.getId(),NettyType.CAI_EXA.getMsg(),3);
//
//            if (integer <= 0) {
//                throw new ApplicationException(CodeType.SERVICE_ERROR, "提交有误");
//            }
//        }


    }

    /**
     * 查询支出信息
     * @param orderId
     * @return
     */
    @Override
    public ResultBo queryResult(Long orderId) {

        LambdaQueryWrapper<Income> wrapper = new LambdaQueryWrapper<Income>()
              .eq(Income::getOrderId,orderId);
        Income income = baseMapper.selectOne(wrapper);

        if (income == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "您还未提交账单");
        }

        ResultBo vo = new ResultBo();
        vo.setOtherInPrice(income.getOtherInPrice());

        List<Outcome2> outcome2s = out2FinanceService.queryOut(income.getId());

        vo.setOutList(outcome2s);

        vo.setCaiName(income.getCaiName());
        vo.setRemark(income.getRemark());

        return vo;
    }

    /**
     * 分页查询所有财务审核
     * @param page
     * @param guideName
     * @param type
     * @param outDate
     * @return
     */
    @Override
    public Page<ExamineResultQuery> listExamine2Page(Page<ExamineResultQuery> page, String guideName, Integer type, String outDate,String orderNo,String outDateEnd) {

        LocalDate outTime = null;
        LocalDate outTimeEnd = null;

        if (StringUtils.isNotBlank(outDate)) {
            outTime = DateUtil.parseDate(outDate);
        }

        if (StringUtils.isNotBlank(outDateEnd)) {
            outTimeEnd = DateUtil.parseDate(outDateEnd);
        }

        if (StringUtils.isBlank(guideName)) {
            guideName = null;
        }


        return baseMapper.listPageExa (page,guideName,type,outTime,orderNo,outTimeEnd);

    }

    /**
     * 展示该导游的审核详情信息
     * @param Id
     * @return
     */
    @Override
    public Map<String,Object> queryExamineDetailed(Long Id) {


        ExamineResultVo vo = baseMapper.listExamineDetailed(Id);

        Map<String,Object> map = new HashMap<>();

        map.put("obj",vo);

        return map;
    }

    /**
     * 展示导游的个人记录
     * @param page
     * @param exaType
     * @param outDate
     * @param lineName
     * @return
     */
    @Override
    public Page<GuestResultQuery> GuestPage2Me(Page<GuestResultQuery> page, String exaType, String outDate, String lineName) {

        UserLoginQuery user = localUser.getUser("用户信息");

        if (StringUtils.isBlank(exaType)) {
            exaType = null;
        }

        int exa = 3;
        if (StringUtils.isNotBlank(exaType)) {
            if (!EXA_DAI.equals(exaType) && !EXA_WEI.equals(exaType) && !EXA_YI.equals(exaType)) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"审核类型输入有误");
            }

            if (EXA_DAI.equals(exaType)) {
                exa = 0;
            }

            if (EXA_YI.equals(exaType)) {
                exa = 1;
            }

            if (EXA_WEI.equals(exaType)) {
                exa = 2;
            }
        }

        if (StringUtils.isBlank(lineName)) {
            lineName = null;
        }

        LocalDate outTime = null;
        if (StringUtils.isNotBlank(outDate)) {
            outTime = DateUtil.parseDate(outDate);
        }

        Long id = null;
        if ("导游".equals(user.getRoleName())) {
            id = user.getId();
        } else {
            id = null;
        }

        List<String> list = new ArrayList<>();

        if (user.getCity().size() > 0) {
            for (CityJwtBo bo : user.getCity()) {
                list.add(bo.getCity());
            }
        } else {
            list = null;
        }

        return baseMapper.pageGuest2Me(page, exa, outTime, lineName,id,list);
    }

    /**
     * 财务同意或拒绝审核
     * @param id
     * @param type
     * @return
     */
    @Override
    public ExaResultQuery examineGuestResult(Long id, Integer type, String caiName, String remark) {

        if (type != 1 && type != 2) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"type参数只能是1或2");
        }

        LocalDateTime time = LocalDateTime.now();

        ExaResultQuery query = new ExaResultQuery();
        if (type == 1) {
            //同意
            int result = baseMapper.examineGuestResult(id, time,caiName,remark);


            if (result <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"操作失败");
            }

            //增加消息提醒的记录
//            Integer integer = baseMapper.insertMsg(income.getCreateUserId(),user.getId(),NettyType.AGGRE_CAI_EXA.getMsg(),3);
//
//            if (integer <= 0) {
//                throw new ApplicationException(CodeType.SERVICE_ERROR, "增加有误");
//            }
            //修改订单表的状态 为已完成
            baseMapper.updateOrderOver(id);
            query.setExaResult(1);
            return query;
        }

        //拒绝
        int result = baseMapper.examineResult(id, time,caiName,remark);

        //修改订单表的状态
        baseMapper.updateOrder (id);


        if (result <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"操作失败");
        }

        //增加消息提醒的记录
        //财务拒绝的记录
//        Integer integer = baseMapper.insertMsg(income.getCreateUserId(),user.getId(),NettyType.AGGRE_CAI_NO_EXA.getMsg(),3);
//
//        if (integer <= 0) {
//            throw new ApplicationException(CodeType.SERVICE_ERROR, "增加有误");
//        }

        query.setExaResult(2);
        return query;
    }

    /**
     * 展示所有财务审核的结果
     * @param page
     * @param guideId
     * @return
     */
    @Override
    public Page<ExamineInfoQuery> pageExamine(Page<ExamineInfoQuery> page,String outDate, String lineName, Long guideId) {
        LocalDate date = DateUtil.parseDate(outDate);

        return baseMapper.pageExamine(page,date,lineName,guideId);
    }

    /**
     * 财务的月结统计
     * @param outDate
     * @param companyName
     * @return
     */
    @Override
    public Page<FinanceCompanyBo> pageCompanyCount(Page<FinanceCompanyBo> page,String outDate, String companyName) {
        LocalDate start = null;
        LocalDate end = null;
        if (StringUtils.isNotBlank(outDate)) {
            start = DateUtil.parseDate(outDate);
            //获取当月最后一天
            LocalDate date = start.plusMonths(1);
            end = date.minusDays(1);
        }

        if (StringUtils.isBlank(companyName)) {
            companyName = null;
        }

        return baseMapper.pageFinanceCompany (page,start,end,companyName);
    }

    /**
     * 月结统计 详情
     * @param page
     * @param lineName
     * @param outDate
     * @param accountName
     * @return
     */
    @Override
    public Map<String,Object> queryCompanyInfoCount(Page<FinanceCompanyInfoBo> page, String lineName, String outDate, String accountName) {
        LocalDate start = null;
        LocalDate end = null;
        if (StringUtils.isNotBlank(outDate)) {
            start = DateUtil.parseDate(outDate);
            //获取当月最后一天
            LocalDate date = start.plusMonths(1);
            end = date.minusDays(1);
        }

        if (StringUtils.isBlank(lineName)) {
            lineName = null;
        }


        Page<FinanceCompanyInfoBo> pageList = baseMapper.queryCompanyInfoCount(page, lineName, start, end, accountName);


        CompanyMonthBo bo = new CompanyMonthBo();

        for (FinanceCompanyInfoBo list : pageList.getRecords()) {
            bo.setCompanyNo(list.getCompanyNo());
            bo.setCompanyName(list.getCompanyName());
            bo.setCompanyUserName(list.getCompanyUserName());
            break;
        }

        Map<String,Object> map = new HashMap<>();
        map.put("pageData",pageList);
        map.put("sameData",bo);

        return map;
    }

    @Override
    public IncomeCount incomeCount() {
        Long id = 634338791976337408L;
        IncomeCount result = new IncomeCount();
        UserLoginQuery user = localUser.getUser("用户信息");

        List<String> list = new ArrayList<>();

        for (CityJwtBo bo : user.getCity()) {
            list.add(bo.getCity());
        }

        for (PrivilegeMenuQuery query : user.getMenuList()) {
            if (id.equals(query.getMenuId())) {

                LambdaQueryWrapper<Income> wrapper = new LambdaQueryWrapper<Income>()
                        .eq(Income::getStatus,0)
                        .in(Income::getCity,list);
                Integer integer = baseMapper.selectCount(wrapper);

                result.setCount(integer);
                return result;

            }
        }

        //
        result.setCount(99999);
        return result;
    }


}
