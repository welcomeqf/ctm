package eqlee.ctm.apply.entry.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.httpclient.HttpClientUtils;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.IdGenerator;
import eqlee.ctm.apply.entry.dao.ExamineMapper;
import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.entity.Examine;
import eqlee.ctm.apply.entry.entity.bo.ExamineUpdateVo;
import eqlee.ctm.apply.entry.entity.bo.UserAdminBo;
import eqlee.ctm.apply.entry.entity.query.*;
import eqlee.ctm.apply.entry.entity.vo.*;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.entry.service.IExamineService;
import eqlee.ctm.apply.entry.vilidata.HttpUtils;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import eqlee.ctm.apply.orders.service.IOrderSubstitutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private IOrderSubstitutService orderSubstitutService;

    IdGenerator idGenerator = new IdGenerator();

    /**
     * 以下是消息记录中的msg
     */
    private final String CANCEL_EXA = "取消报名审核";

    private final String CANCEL_DO = "取消报名";

    private final String APPLY_DO = "报名申请";


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

        //批量增加所有运营可见的消息提醒
        //查询所有管理员的id集合
//        List<Long> longList = new ArrayList<>();
//        List<UserAdminBo> idList = (List<UserAdminBo>) httpUtils.queryAllAdminInfo();
//
//        if (idList.size() == 0) {
//            throw new ApplicationException(CodeType.SERVICE_ERROR, "请将管理员的角色名设置为运营人员");
//        }
//
//        for (UserAdminBo bo : idList) {
//            longList.add(bo.getAdminId());
//        }
//
//        //批量增加所有运营审核的报名消息提醒
//        MsgAddVo msgVo = new MsgAddVo();
//        msgVo.setCreateId(user.getId());
//        msgVo.setMsgType(3);
//        msgVo.setMsg(CANCEL_EXA);
//        msgVo.setToId(longList);
//
//        httpUtils.addAllMsg(msgVo);

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

        UpdateInfoVo infoVo = new UpdateInfoVo();
        infoVo.setConnectName(vo.getContactName());
        infoVo.setConnectTel(vo.getContactTel());
        infoVo.setPlace(vo.getPlace());

        examine.setRemark(JSON.toJSONString(infoVo));

        UserLoginQuery user = localUser.getUser("用户信息");
        examine.setCreateUserId(user.getId());
        examine.setUpdateUserId(user.getId());
        baseMapper.insert(examine);

        //同时修改报名表的审核状态
        ApplyUpdateInfo info = new ApplyUpdateInfo();
        info.setId(examineVo.getApplyId());
        info.setContactName(examineVo.getConnectName());
        info.setContactTel(examineVo.getConnectTel());
        info.setPlace(examineVo.getPlace());
        //修改报名表
        applyService.updateApply(info);
    }

    /**
     * 通过取消报名表的审核
     * @param ApplyId
     */
    @Override
    public ExaApplyResultQuery AdoptCancelExamine(Long ApplyId) {

        //判断该账号是什么支付
        ApplySeacherVo vo = applyService.queryById(ApplyId);

        if (vo.getIsSelect()) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "该单已经被导游选了,不能同意");
        }

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

        //是否退款
        ExaApplyResultQuery query = new ExaApplyResultQuery();
        if (vo.getPayType() == 0) {
            //现结退款
            //获得token
            String payToken = null;
            try {
                payToken = httpUtils.getPayToken();
            } catch (Exception e) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"获取token失败");
            }
            String tokenString = "Bearer " +payToken;
            //申请退款
            ResultRefundQuery refund = httpUtils.refund(vo.getApplyNo(), vo.getAllPrice(), vo.getAllPrice(), tokenString);
            if (refund.getError()) {
                //退款失败
                throw new ApplicationException(CodeType.SERVICE_ERROR,"退款失败");
            }
            //退款成功
            query.setType("退款成功");
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
//        msgVo.setMsg(CANCEL_DO);
//        msgVo.setToId(vo.getCreateUserId());
//
//        httpUtils.addMsg(msgVo);

        //改变报名表
        applyService.cancelApply(ApplyId);
        return query;
    }

    /**
     * 不通过取消报名审核
     * @param ApplyId
     */
    @Override
    public ExaApplyResultQuery NotAdoptCancelExamine(Long ApplyId) {
        LambdaQueryWrapper<Examine> queryWrapper = new LambdaQueryWrapper<Examine>()
                .eq(Examine::getApplyId,ApplyId)
                .eq(Examine::getExamineType,"1");
        Examine examine = new Examine();
        examine.setExamineResult(2);
        UserLoginQuery user = localUser.getUser("用户信息");
        examine.setUpdateUserId(user.getId());
        int update = baseMapper.update(examine, queryWrapper);

        if (update <= 0) {
            log.error("update cancel apply fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR, "不通过取消报名的审核记录-失败");
        }

        //增加一条不通过取消报名的消息提醒记录
//        ApplySeacherVo vo = applyService.queryById(ApplyId);
//        MsgVo msgVo = new MsgVo();
//        msgVo.setCreateId(user.getId());
//        msgVo.setMsgType(2);
//        msgVo.setMsg(CANCEL_DO);
//        msgVo.setToId(vo.getCreateUserId());
//
//        httpUtils.addMsg(msgVo);

        ExaApplyResultQuery query = new ExaApplyResultQuery();
        query.setType("ok");
        return query;
    }


    /**
     * 不通过报名审核
     * @param ApplyId
     */
    @Override
    public ExaApplyResultQuery NotAdoptExamine(Long ApplyId) {
        Examine examine = new Examine();
        examine.setExamineResult(2);
        LambdaQueryWrapper<Examine> queryWrapper = new LambdaQueryWrapper<Examine>()
                .eq(Examine::getApplyId,ApplyId);
        UserLoginQuery user = localUser.getUser("用户信息");
        examine.setUpdateUserId(user.getId());
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
            String payToken = null;
            try {
                payToken = httpUtils.getPayToken();
            } catch (Exception e) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"获取token失败");
            }
            String tokenString = "Bearer " +payToken;
            //申请退款
            ResultRefundQuery refund = httpUtils.refund(vo.getApplyNo(), vo.getAllPrice(), vo.getAllPrice(), tokenString);
            if (refund.getError()) {
                //退款失败
                throw new ApplicationException(CodeType.SERVICE_ERROR,"退款失败");
            }
            //退款成功
            query.setType("退款成功");
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
//        msgVo.setMsg(APPLY_DO);
//        msgVo.setToId(vo.getCreateUserId());
//
//        httpUtils.addMsg(msgVo);

        //同步报名表的审核状态
        applyService.updateExamineStatus(ApplyId,2);
        return query;
    }

    /**
     * 通过报名表的审核
     * @param ApplyId
     */
    @Override
    public void doptExamine(Long ApplyId) {
        Examine examine = new Examine();
        examine.setExamineResult(1);
        LambdaQueryWrapper<Examine> queryWrapper = new LambdaQueryWrapper<Examine>()
                .eq(Examine::getApplyId,ApplyId);
        UserLoginQuery user = localUser.getUser("用户信息");
        examine.setUpdateUserId(user.getId());
        int update = baseMapper.update(examine, queryWrapper);

        if (update <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"通过审核失败，请重试");
        }

        ApplySeacherVo query = applyService.queryById(ApplyId);

        //增加一条通过报名的消息提醒记录
//        MsgVo vo = new MsgVo();
//        vo.setCreateId(user.getId());
//        vo.setMsgType(1);
//        vo.setMsg(APPLY_DO);
//        vo.setToId(query.getCreateUserId());
//
//        httpUtils.addMsg(vo);

        //同步报名表的审核状态
        applyService.updateExamineStatus(ApplyId,1);
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

        Page<ExamineUpdateInfoVo> list = baseMapper.listUpdateInfo(vo);

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


}
