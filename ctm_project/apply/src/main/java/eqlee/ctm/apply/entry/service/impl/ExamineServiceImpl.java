package eqlee.ctm.apply.entry.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.entity.websocket.NettyType;
import com.yq.exception.ApplicationException;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.DateUtil;
import com.yq.utils.IdGenerator;
import com.yq.utils.SendUtils;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.entry.dao.ExamineMapper;
import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.entity.Examine;
import eqlee.ctm.apply.entry.entity.User;
import eqlee.ctm.apply.entry.entity.bo.ExamineUpdateVo;
import eqlee.ctm.apply.entry.entity.bo.UserAdminBo;
import eqlee.ctm.apply.entry.entity.query.*;
import eqlee.ctm.apply.entry.entity.vo.*;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.entry.service.IExamineService;
import eqlee.ctm.apply.entry.vilidata.HttpUtils;
import eqlee.ctm.apply.entry.vilidata.TokenData;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import eqlee.ctm.apply.message.entity.vo.MsgAddVo;
import eqlee.ctm.apply.message.entity.vo.MsgVo;
import eqlee.ctm.apply.message.service.IMessageService;
import eqlee.ctm.apply.orders.service.IOrderSubstitutService;
import eqlee.ctm.apply.orders.service.IOrdersDetailedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author qf
 * @Date 2019/9/20
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExamineServiceImpl extends ServiceImpl<ExamineMapper, Examine> implements IExamineService {

    @Autowired
    private IApplyService applyService;

    @Autowired
    private LocalUser localUser;

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private TokenData tokenData;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IOrdersDetailedService ordersDetailedService;

    @Autowired
    private IOrderSubstitutService orderSubstitutService;

    @Autowired
    private SendUtils sendService;

    IdGenerator idGenerator = new IdGenerator();

    /**
     * 提交取消报名表的审核
     * @param ApplyId
     */
    @Override
    public void CancelExamine(Long ApplyId) {
        UserLoginQuery user = localUser.getUser("用户信息");

        LambdaQueryWrapper<Examine> wrapper = new LambdaQueryWrapper<Examine>()
                .eq(Examine::getApplyId,ApplyId)
                .eq(Examine::getCreateUserId,user.getId())
                .eq(Examine::getExamineType,"1");
        Examine one = baseMapper.selectOne(wrapper);

        if (one != null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"不能重复取消,请等候管理人的审核结果");
        }

        Examine examine = new Examine();
        examine.setId(idGenerator.getNumberId());
        examine.setApplyId(ApplyId);
        examine.setExamineType("1");

        examine.setCreateUserId(user.getId());
        examine.setUpdateUserId(user.getId());

        int insert = baseMapper.insert(examine);

        if (insert <= 0) {
            log.error("insert cancel apply exa fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR, "提交取消报名审核失败");
        }

        //修改报名表的状态
        applyService.updateApplyCancel(1,ApplyId);


        //批量增加所有运营可见的消息提醒
        //查询所有管理员的id集合
//        List<Long> longList = new ArrayList<>();
//        List<UserAdminBo> idList = (List<UserAdminBo>) httpUtils.queryAllAdminInfo();
//
//        if (idList.size() == 0) {
//            throw new ApplicationException(CodeType.SERVICE_ERROR, "请将管理员的角色名设置为运营人员");
//        }
//
//        for (int i = 0; i <= idList.size()-1; i++) {
//            UserAdminBo bo = JSONObject.parseObject(String.valueOf(idList.get(i)),UserAdminBo.class);
//            longList.add(bo.getAdminId());
//        }
//
//        //批量增加所有运营审核的报名消息提醒
//        MsgAddVo msgVo = new MsgAddVo();
//        msgVo.setCreateId(user.getId());
//        msgVo.setMsgType(3);
//        msgVo.setMsg(NettyType.CANCEL_EXA.getMsg());
//        msgVo.setToId(longList);
//
//        //增加数据库
//        messageService.addAllMsg(msgVo);

        ordersDetailedService.updateCancelStatus(ApplyId,1);

    }

    /**
     * 提交修改报名表的审核
     * @param examineVo
     */
    @Override
    public void UpdateApplyExamine(ExamineVo examineVo) {
        Examine examine = new Examine();
        examine.setId(idGenerator.getNumberId());
        examine.setApplyId(examineVo.getApplyId());
        examine.setExamineType("2");
        examine.setExamineResult(1);
        //将修改之前的信息以json的形式装进备注字段
        ApplySeacherVo vo = applyService.queryById(examineVo.getApplyId());

        //如审核状态为已审核 则不允许再修改
        if (vo.getStatu() == 1) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "申请表已通过审核，不允许再修改，请联系管理员！");
        }
        UpdateInfoVo infoVo = new UpdateInfoVo();
        infoVo.setConnectName(vo.getContactName());
        infoVo.setConnectTel(vo.getContactTel());
        infoVo.setPlace(vo.getPlace());
        infoVo.setRemark(vo.getApplyRemark());

        examine.setRemark(JSON.toJSONString(infoVo));

        UserLoginQuery user = localUser.getUser("用户信息");
        examine.setCreateUserId(user.getId());
        examine.setUpdateUserId(user.getId());
        baseMapper.insert(examine);

        //同时修改报名表的审核状态
        //删除报名表
        applyService.deleteApply(examineVo.getApplyId());


        if (vo.getPayType() == 0) {
            //现结退款
            //获得token
            String payToken;
            try {
                payToken = tokenData.getPayMapToken();
            } catch (Exception e) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"获取token失败");
            }
            String tokenString = "Bearer " +payToken;

            if (vo.getPayInfo() == null) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "没有支付记录");
            }

            if (vo.getPayInfo() == 1) {
                //支付宝支付
                ResultRefundQuery aliRefund = httpUtils.aliRefund(vo.getApplyNo(), "拒绝报名", vo.getAllPrice(), tokenString);
                if (aliRefund.getError()) {
                    //退款失败
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"退款失败");
                }
                //退款成功
            }

            if (vo.getPayInfo() == 0) {
                //微信支付
                //申请退款
                ResultRefundQuery refund = httpUtils.refund(vo.getApplyNo(), vo.getAllPrice(), vo.getAllPrice(), tokenString);
                if (refund.getError()) {
                    //退款失败
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"退款失败");
                }
                //退款成功
            }
        }

        ApplyVo applyVo = new ApplyVo();
        applyVo.setMsPrice(examineVo.getMsPrice());
        applyVo.setSxPrice(examineVo.getSxPrice());
        applyVo.setCName(examineVo.getCName());
        applyVo.setAdultNumber(examineVo.getAdultNumber());
        applyVo.setAdultPrice(examineVo.getAdultPrice());
        applyVo.setApplyRemark(examineVo.getApplyRemark());
        applyVo.setBabyNumber(examineVo.getBabyNumber());
        applyVo.setBabyPrice(examineVo.getBabyPrice());
        applyVo.setChildNumber(examineVo.getChildNumber());
        applyVo.setChildPrice(examineVo.getChildPrice());
        applyVo.setCompanyNameId(examineVo.getCompanyNameId());
        applyVo.setCompanyUser(examineVo.getCompanyUser());
        applyVo.setCompanyUserId(examineVo.getCompanyUserId());
        applyVo.setContactName(examineVo.getContactName());
        applyVo.setContactTel(examineVo.getContactTel());
        applyVo.setLineName(examineVo.getLineName());
        applyVo.setMarketAllPrice(examineVo.getMarketAllPrice());
        applyVo.setOldNumber(examineVo.getOldNumber());
        applyVo.setOldPrice(examineVo.getOldPrice());
        applyVo.setOutDate(examineVo.getOutDate());
        applyVo.setPayType(examineVo.getPayType());
        applyVo.setPlace(examineVo.getPlace());
        applyVo.setType(examineVo.getType());

        applyVo.setUpOrInsert(1);
        applyVo.setApplyId(examineVo.getApplyId());
        applyVo.setApplyNo(examineVo.getApplyNo());
        //applyVo.setCreateUserId(examineVo.getCreateUserId());
        applyVo.setApplyPic(examineVo.getApplyPic());
        applyVo.setIcnumber(examineVo.getIcnumber());
        applyVo.setPlaceRegion(examineVo.getPlaceRegion());
        applyVo.setPlaceAddress(examineVo.getPlaceAddress());
        applyVo.setCreateUserId(vo.getCreateUserId());
        //保持申请表审核记录以及经手人信息 记录修改人信息
        applyVo.setUpdateUserId(examineVo.getUpdateUserId());
        applyVo.setStatu(vo.getStatu());

        applyService.insertApply(applyVo);
    }

    /**
     * 通过取消报名表的审核
     * @param ApplyId
     */
    @Override
    public ExaApplyResultQuery AdoptCancelExamine(Long ApplyId) {

        //判断该账号是什么支付
        ApplySeacherVo vo = applyService.queryById(ApplyId);

//        if (vo.getIsSelect()) {
//            throw new ApplicationException(CodeType.SERVICE_ERROR, "该单已经被导游选了,不能同意");
//        }

        LambdaQueryWrapper<Examine> queryWrapper = new LambdaQueryWrapper<Examine>()
                .eq(Examine::getApplyId,ApplyId)
                .eq(Examine::getExamineType,"1");
        Examine examine = new Examine();
        examine.setExamineResult(1);
        UserLoginQuery user = localUser.getUser("用户信息");
        examine.setUpdateUserId(user.getId());
        int update = baseMapper.update(examine, queryWrapper);

        if (update <= 0) {
            log.error("update cancel apply fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR, "通过取消报名审核失败");
        }

        //修改报名表的状态
        applyService.updateApplyCancel(0,ApplyId);

        //是否退款
        ExaApplyResultQuery query = new ExaApplyResultQuery();
        if (vo.getPayType() == 0) {
            //现结退款
            //获得token
            String payToken;
            try {
                payToken = tokenData.getPayMapToken();
            } catch (Exception e) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"获取token失败");
            }
            String tokenString = "Bearer " +payToken;
            //申请退款
            if (vo.getPayInfo() == null) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "没有支付记录");
            }
            //判断是什么支付
            if (vo.getPayInfo() == 1) {
                //支付宝支付
                ResultRefundQuery aliRefund = httpUtils.aliRefund(vo.getApplyNo(), "取消报名", vo.getAllPrice(), tokenString);
                if (aliRefund.getError()) {
                    //退款失败
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"退款失败");
                }
                //退款成功
                query.setType("退款成功");
            }
            if (vo.getPayInfo() == 0) {
                //微信支付
                ResultRefundQuery refund = httpUtils.refund(vo.getApplyNo(), vo.getAllPrice(), vo.getAllPrice(), tokenString);
                if (refund.getError()) {
                    //退款失败
                    throw new ApplicationException(CodeType.SERVICE_ERROR, refund.getMsg() != null && refund.getMsg() != "" ? refund.getMsg() : "退款失败");
                }
                //退款成功
                query.setType("退款成功");
            }
        }
        if (vo.getPayType() == 1) {
            query.setType("月结");
        }
        if (vo.getPayType() == 2) {
            query.setType("面收");
        }

        //增加一条通过报名的消息提醒记录
//        MsgVo msgVo = new MsgVo();
//        msgVo.setCreateId(user.getId());
//        msgVo.setMsgType(1);
//        msgVo.setMsg(NettyType.AGGRE_CANCEL_EXA.getMsg());
//
//        msgVo.setToId(vo.getCreateUserId());
//
//        messageService.insertMsg(msgVo);

        //改变报名表
        applyService.cancelApply(ApplyId);

        //通过
        ordersDetailedService.updateCancelStatus(ApplyId,2);
        return query;
    }

    /**
     * 不通过取消报名审核
     * @param ApplyId
     * @param exaRemark
     * @return
     */
    @Override
    public ExaApplyResultQuery NotAdoptCancelExamine(Long ApplyId, String exaRemark) {
        LambdaQueryWrapper<Examine> queryWrapper = new LambdaQueryWrapper<Examine>()
                .eq(Examine::getApplyId,ApplyId)
                .eq(Examine::getExamineType,"1");
        Examine examine = new Examine();
        examine.setExamineResult(2);
        examine.setExaRemark(exaRemark);
        UserLoginQuery user = localUser.getUser("用户信息");
        examine.setUpdateUserId(user.getId());
        int update = baseMapper.update(examine, queryWrapper);

        if (update <= 0) {
            log.error("update cancel apply fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR, "不通过取消报名的审核记录-失败");
        }


        //修改报名表的状态
        applyService.updateApplyCancel(2,ApplyId);

        //增加一条不通过取消报名的消息提醒记录
//        ApplySeacherVo vo = applyService.queryById(ApplyId);
//        MsgVo msgVo = new MsgVo();
//        msgVo.setCreateId(user.getId());
//        msgVo.setMsgType(2);
//        msgVo.setMsg(NettyType.NOAGGRE_CANCEL_EXA.getMsg());
//
//        msgVo.setToId(vo.getCreateUserId());
//
//        messageService.insertMsg(msgVo);

        ordersDetailedService.updateCancelStatus(ApplyId,0);

        ExaApplyResultQuery query = new ExaApplyResultQuery();
        query.setType("ok");
        return query;
    }


    /**
     * 不通过报名审核
     * @param ApplyId
     * @param exaRemark
     * @return
     */
    @Override
    public ExaApplyResultQuery NotAdoptExamine(Long ApplyId, String exaRemark) {
        Examine examine = new Examine();
        examine.setExamineResult(2);
        LambdaQueryWrapper<Examine> queryWrapper = new LambdaQueryWrapper<Examine>()
                .eq(Examine::getApplyId,ApplyId)
                .eq(Examine::getExamineType,"0");
        UserLoginQuery user = localUser.getUser("用户信息");
        examine.setUpdateUserId(user.getId());
        examine.setExaRemark(exaRemark);
        int update = baseMapper.update(examine, queryWrapper);

        if (update <= 0) {
            log.error("not examine fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"不通过审核失败，请重试");
        }

        //判断该账号是什么支付
        ApplySeacherVo vo = applyService.queryById(ApplyId);
        //是否退款
        //是否退款
        ExaApplyResultQuery query = new ExaApplyResultQuery();
        if (vo.getPayType() == 0) {
            //现结退款
            //获得token
            String payToken;
            try {
                payToken = tokenData.getPayMapToken();
            } catch (Exception e) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"获取token失败");
            }
            String tokenString = "Bearer " +payToken;

            if (vo.getPayInfo() == null) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "没有支付记录");
            }

            if (vo.getPayInfo() == 1) {
                //支付宝支付
                ResultRefundQuery aliRefund = httpUtils.aliRefund(vo.getApplyNo(), "拒绝报名", vo.getAllPrice(), tokenString);
                if (aliRefund.getError()) {
                    //退款失败
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"退款失败");
                }
                //退款成功
                query.setType("退款成功");
            }

            if (vo.getPayInfo() == 0) {
                //微信支付
                //申请退款
                ResultRefundQuery refund = httpUtils.refund(vo.getApplyNo(), vo.getAllPrice(), vo.getAllPrice(), tokenString);
                if (refund.getError()) {
                    //退款失败
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"退款失败");
                }
                //退款成功
                query.setType("退款成功");
            }
        }
        if (vo.getPayType() == 1) {
            query.setType("月结");
        }
        if (vo.getPayType() == 2) {
            query.setType("面收");
        }

        //增加一条不通过报名的消息提醒记录
//        MsgVo msgVo = new MsgVo();
//        msgVo.setCreateId(user.getId());
//        msgVo.setMsgType(2);
//        msgVo.setMsg(NettyType.NOAGGRE_EXA.getMsg());
//
//        msgVo.setToId(vo.getCreateUserId());
//
//        messageService.insertMsg(msgVo);

        //同步报名表的审核状态
        applyService.updateExamineStatus(ApplyId,2,0);
        try{
            //获取同行openid根据账号 报名审核结果通知同行
            String jsonStr = sendService.queryNotifyAdminInfo(vo.getCompanyName(),5);
            List<UserOpenIdVm> notifyList = JSONObject.parseArray(jsonStr,  UserOpenIdVm.class);
            if(notifyList != null && !notifyList.isEmpty()){
                for(UserOpenIdVm vm : notifyList){
                    if(StringUtils.isNotBlank(vm.getOpenId())){
                        sendService.pushApplyExamPeer(vm.getOpenId(),vo.getContactName(),vo.getContactTel(),DateUtil.formatDateTime(LocalDateTime.now()),"已拒绝");
                    }
                }
            }

        }catch (Exception ex){}

        return query;
    }

    /**
     * 通过报名表的审核
     * @param ApplyId
     */
    @Override
    public void doptExamine(Long ApplyId,String OpenId) {
        Examine examine = new Examine();
        examine.setExamineResult(1);
        LambdaQueryWrapper<Examine> queryWrapper = new LambdaQueryWrapper<Examine>()
                .eq(Examine::getApplyId,ApplyId)
                .eq(Examine::getExamineType,"0");
        UserLoginQuery user = localUser.getUser("用户信息");
        examine.setUpdateUserId(user.getId());
        int update = baseMapper.update(examine, queryWrapper);

        if (update <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"通过审核失败，请重试");
        }

        ApplySeacherVo query = applyService.queryById(ApplyId);

//        增加一条通过报名的消息提醒记录
//        MsgVo vo = new MsgVo();
//        vo.setCreateId(user.getId());
//        vo.setMsgType(1);
//        vo.setMsg(NettyType.AGGRE_EXA.getMsg());
//
//        vo.setToId(query.getCreateUserId());
////
//        messageService.insertMsg(vo);

        //同步报名表的审核状态
        applyService.updateExamineStatus(ApplyId,1,0);

        try{
            //通知导游选人微信公众号
            if(StringUtils.isNotBlank(OpenId)){
                sendService.pushGuideSelect(OpenId,query.getLineName(),query.getContactName(),query.getContactTel());
            }
            //获取同行openid根据账号 通知报名审核结果
            String jsonStr = sendService.queryNotifyAdminInfo(query.getCompanyName(),5);
            List<UserOpenIdVm> notifyList = JSONObject.parseArray(jsonStr,  UserOpenIdVm.class);
            if(notifyList != null && !notifyList.isEmpty()){
                for(UserOpenIdVm vm : notifyList){
                    if(StringUtils.isNotBlank(vm.getOpenId())){
                        sendService.pushApplyExamPeer(vm.getOpenId(),query.getContactName(),query.getContactTel(),DateUtil.formatDateTime(LocalDateTime.now()),"已通过");
                    }
                }
            }

        }catch (Exception ex){}
    }

    /**
     * 增加审核记录
     * @param vo
     */
    @Override
    public void insertExamine(ExamineAddVo vo) {
        Examine examine = new Examine();
        examine.setId(idGenerator.getNumberId());
        examine.setExamineType(vo.getExamineType());
        examine.setApplyId(vo.getApplyId());
        UserLoginQuery user = localUser.getUser("用户信息");
        examine.setCreateUserId(user.getId());
        examine.setUpdateUserId(user.getId());
        int insert = baseMapper.insert(examine);

        if (insert <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加审核记录失败");
        }
    }

    /**
     * 展示修改列表
     * @param vo
     * @return
     */
    @Override
    public Page<ExamineUpdateInfoVo> listUpdateInfo(Page<ExamineUpdateInfoVo> vo) {

        UserLoginQuery user = localUser.getUser("用户信息");
        Long id = null;
        if ("同行".equals(user.getRoleName())) {
            //查看自己的数据
            id = user.getId();
        }

        Page<ExamineUpdateInfoVo> list = baseMapper.listUpdateInfo(vo,id);

        List<ExamineUpdateInfoVo> voList = list.getRecords();

        for (ExamineUpdateInfoVo infoVo : voList) {

            UpdateInfoVo updateInfoVo = JSONObject.parseObject(infoVo.getRemark(), UpdateInfoVo.class);

            infoVo.setOldConnectName(updateInfoVo.getConnectName());
            infoVo.setOldConnectTel(updateInfoVo.getConnectTel());
            infoVo.setOldPlace(updateInfoVo.getPlace());
        }

        list.setRecords(voList);

        return list;
    }

    /**
     * 查询修改记录详情
     * @param Id
     * @return
     */
    @Override
    public ExamineInfoVo queryUpdateInfo(Long Id) {
        ExamineUpdateInfoVo vo = baseMapper.queryUpdateInfo(Id);
        ExamineInfoVo result = new ExamineInfoVo();
        result.setUpdateUserName(vo.getUpdateUserName());
        result.setUpdateDate(vo.getUpdateDate());

        UpdateInfoVo infoVo = JSONObject.parseObject(vo.getRemark(), UpdateInfoVo.class);

        result.setConnectName(infoVo.getConnectName());
        result.setConnectTel(infoVo.getConnectTel());
        result.setPlace(infoVo.getPlace());

        return result;
    }

    /**
     * 查询未读的条数
     * @param toId
     * @param msgType
     * @param msg
     * @return
     */
    @Override
    public ApplyNoReadCountQuery queryNoReadCount(Long toId, Integer msgType, String msg) {
        ApplyNoReadCountQuery count = (ApplyNoReadCountQuery) httpUtils.queryCount(toId, msgType, msg);
        return count;
    }

    /**
     * 修改当前用户的所有未读消息状态
     * @return
     */
    @Override
    public ResultVo updateLocalMsgStatus() {
        UserLoginQuery user = localUser.getUser("用户信息");

        httpUtils.updateUserAllMsg(user.getId());
        ResultVo vo = new ResultVo();
        vo.setResult("ok");
        return vo;
    }

    /**
     * 查询所有未审核信息
     * @return
     */
    @Override
    public ApplyNoReadCountQuery queryAllNoExaCount() {
        UserLoginQuery user = localUser.getUser("用户信息");

        ApplyNoReadCountQuery query = new ApplyNoReadCountQuery();

        LambdaQueryWrapper<Examine> wrapper = new LambdaQueryWrapper<Examine>()
              .eq(Examine::getExamineResult,0);

        if ("运营人员".equals(user.getRoleName())) {
            Integer count = baseMapper.selectCount(wrapper);

            if (count >= 99) {
                query.setCount(99);
            }

            return query;
        }

        if ("导游".equals(user.getRoleName())) {
            return orderSubstitutService.queryGuideNoExaCount();
        }

        if ("财务".equals(user.getRoleName())) {
            Integer integer = baseMapper.queryNoExaCount();
            query.setCount(integer);

            return query;
        }
        //同行
        query.setCount(9999);
        return query;
    }

    /**
     * 返回备注
     * @param examineType
     * @param applyId
     * @return
     */
    @Override
    public ResultVo queryRemark(String examineType, Long applyId) {

        LambdaQueryWrapper<Examine> wrapper = new LambdaQueryWrapper<Examine>()
              .eq(Examine::getApplyId,applyId)
              .eq(Examine::getExamineType,examineType);
        Examine examine = baseMapper.selectOne(wrapper);

        ResultVo vo = new ResultVo();
        vo.setResult(examine.getExaRemark());
        return vo;
    }


    /**
     * 获取申请单审核记录
     * @param applyId
     * @return
     */
    @Override
    public List<ApplyExamRecord> queryExamRecord(Long applyId) throws Exception {

        LambdaQueryWrapper<Examine> wrapper = new LambdaQueryWrapper<Examine>()
                .eq(Examine::getApplyId,applyId)
                .ne(Examine::getExamineType,"2");
        List<Examine> examineList = baseMapper.selectList(wrapper);

        //获取审核人ids
        List<Long> ids = examineList.stream().map(Examine::getUpdateUserId).collect(Collectors.toList());
        String jsonStr = httpUtils.queryListByIds(ids);
        List<User> userList = JSONObject.parseArray(jsonStr,  User.class);
        //List<User> userList = tempList.stream().map(json -> JSONObject.toJavaObject(json, User.class)).collect(Collectors.toList());
        List<ApplyExamRecord> list = new ArrayList<>();
        if(examineList != null && !examineList.isEmpty()){
            for(Examine examine : examineList){
                ApplyExamRecord vm = new ApplyExamRecord();
                vm.setExamineType("0".equals(examine.getExamineType())? "报名审核" : "取消审核");
                vm.setExamineResult(examine.getExamineResult() == 0 ? "待审核" : examine.getExamineResult() == 1  ? "已通过" : "已拒绝");
                vm.setCreateDate(DateUtil.formatDateTime(examine.getCreateDate()));
                vm.setExamName("-");
                if(examine.getExamineResult() != 0){
                    List<User> ulist = userList.stream().filter(item -> item.getId().equals(examine.getUpdateUserId())).collect(Collectors.toList());
                    if (ulist != null && !ulist.isEmpty()) {
                        // 存在
                        User user =  ulist.get(0);
                        vm.setExamName(user.getCName());
                    }
                }
                list.add(vm);
            }
        }
        return list;
    }



}
