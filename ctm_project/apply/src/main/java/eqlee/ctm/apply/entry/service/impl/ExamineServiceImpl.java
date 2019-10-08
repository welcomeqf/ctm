package eqlee.ctm.apply.entry.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.IdGenerator;
import eqlee.ctm.apply.entry.dao.ExamineMapper;
import eqlee.ctm.apply.entry.entity.Examine;
import eqlee.ctm.apply.entry.entity.query.ApplyUpdateInfo;
import eqlee.ctm.apply.entry.entity.vo.ExamineVo;
import eqlee.ctm.apply.entry.entity.vo.UpdateInfoVo;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.entry.service.IExamineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        examine.setExamineType("取消报名表");
        baseMapper.insert(examine);

        UserLoginQuery user = localUser.getUser("用户信息");
        examine.setCreateUserId(user.getId());
        examine.setUpdateUserId(user.getId());

        //同时修改报名表的审核状态
        applyService.updateExamineStatus(ApplyId,0);
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
        examine.setExamineType("修改报名表");
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
        applyService.updateExamineStatus(examineVo.getApplyId(),0);
    }

    /**
     * 通过取消报名表的审核
     * @param ApplyId
     */
    @Override
    public void AdoptCancelExamine(Long ApplyId) {

        //改变报名表
        applyService.cancelApply(ApplyId);
    }

    /**
     * 通过修改报名表的审核
     * @param ApplyId
     */
    @Override
    public void AdoptUpdateExamine(Long ApplyId) {
        LambdaQueryWrapper<Examine> queryWrapper = new LambdaQueryWrapper<Examine>()
                .eq(Examine::getApplyId,ApplyId);

        Examine result = baseMapper.selectOne(queryWrapper);

        ApplyUpdateInfo info = JSONObject.parseObject(result.getRemark(), ApplyUpdateInfo.class);
        //修改报名表
        applyService.updateApply(info);
    }

    /**
     * 不通过报名审核
     * @param ApplyId
     */
    @Override
    public void NotAdoptExamine(Long ApplyId) {
        Examine examine = new Examine();
        examine.setExamineType("报名审核");
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
        examine.setExamineType("报名审核");
        examine.setExamineResult(1);
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
        applyService.updateExamineStatus(ApplyId,1);
    }


}
