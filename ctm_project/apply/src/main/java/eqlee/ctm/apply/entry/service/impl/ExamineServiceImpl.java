package eqlee.ctm.apply.entry.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.utils.IdGenerator;
import eqlee.ctm.apply.entry.dao.ExamineMapper;
import eqlee.ctm.apply.entry.entity.Examine;
import eqlee.ctm.apply.entry.entity.query.ApplyUpdateInfo;
import eqlee.ctm.apply.entry.entity.vo.ExamineVo;
import eqlee.ctm.apply.entry.entity.vo.UpdateInfoVo;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.entry.service.IExamineService;
import eqlee.ctm.apply.exception.ApplicationException;
import eqlee.ctm.apply.jwt.contain.LocalUser;
import eqlee.ctm.apply.jwt.entity.UserLoginQuery;
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
     * 增加一条取消报名表的审核记录
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
     * 增加一条修改报名表的审核记录
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
        Examine examine = new Examine();
        LambdaQueryWrapper<Examine> queryWrapper = new LambdaQueryWrapper<Examine>()
                .eq(Examine::getApplyId,ApplyId);
        examine.setExamineResult(1);
        UserLoginQuery user = localUser.getUser("用户信息");
        examine.setUpdateUserId(user.getId());
        int update = baseMapper.update(examine, queryWrapper);

        if (update <= 0) {
            log.error("cancel apply examine fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"审核失败，请重试");
        }

        //同时改变报名表的状态
        applyService.cancelApply(ApplyId);
    }

    /**
     * 通过修改报名表的审核
     * @param ApplyId
     */
    @Override
    public void AdoptUpdateExamine(Long ApplyId) {
        Examine examine = new Examine();
        LambdaQueryWrapper<Examine> queryWrapper = new LambdaQueryWrapper<Examine>()
                .eq(Examine::getApplyId,ApplyId);
        examine.setExamineResult(1);
        UserLoginQuery user = localUser.getUser("用户信息");
        examine.setUpdateUserId(user.getId());
        int update = baseMapper.update(examine, queryWrapper);

        if (update <= 0) {
            log.error("update apply examine fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"通过修改报名表失败,请重试");
        }

        Examine result = baseMapper.selectOne(queryWrapper);

        ApplyUpdateInfo info = JSONObject.parseObject(result.getRemark(), ApplyUpdateInfo.class);
        //修改报名表
        applyService.updateApply(info);
    }

    /**
     * 不通过审核
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


}
