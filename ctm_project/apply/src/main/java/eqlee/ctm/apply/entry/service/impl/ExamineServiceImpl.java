package eqlee.ctm.apply.entry.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.IdGenerator;
import eqlee.ctm.apply.entry.dao.ExamineMapper;
import eqlee.ctm.apply.entry.entity.Examine;
import eqlee.ctm.apply.entry.entity.query.ApplyUpdateInfo;
import eqlee.ctm.apply.entry.entity.query.ExaMqAdminQuery;
import eqlee.ctm.apply.entry.entity.vo.*;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.entry.service.IExamineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    IdGenerator idGenerator = new IdGenerator();


    /**
     * 提交取消报名表的审核
     * @param ApplyId
     */
    @Override
    public void CancelExamine(Long ApplyId) {
        Examine examine = new Examine();
        examine.setId(idGenerator.getNumberId());
        examine.setApplyId(ApplyId);
        examine.setExamineType("1");

        UserLoginQuery user = localUser.getUser("用户信息");
        examine.setCreateUserId(user.getId());
        examine.setUpdateUserId(user.getId());

        int insert = baseMapper.insert(examine);

        if (insert <= 0) {
            log.error("insert cancel apply exa fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR, "提交取消报名审核失败");
        }

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
        //将修改的信息以json的形式装进备注字段
        UpdateInfoVo infoVo = new UpdateInfoVo();
        infoVo.setConnectName(examineVo.getConnectName());
        infoVo.setConnectTel(examineVo.getConnectTel());
        infoVo.setPlace(examineVo.getPlace());

        examine.setRemark(infoVo.toString());

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
    public void AdoptCancelExamine(Long ApplyId) {

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

        //改变报名表
        applyService.cancelApply(ApplyId);
    }

    /**
     * 不通过取消报名审核
     * @param ApplyId
     */
    @Override
    public void NotAdoptCancelExamine(Long ApplyId) {
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

    }


    /**
     * 不通过报名审核
     * @param ApplyId
     */
    @Override
    public void NotAdoptExamine(Long ApplyId) {
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

        //同步报名表的审核状态
        applyService.updateExamineStatus(ApplyId,2);
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
        return baseMapper.listUpdateInfo(vo);
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




}
