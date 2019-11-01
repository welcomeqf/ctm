package eqlee.ctm.api.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import eqlee.ctm.api.pay.dao.PayMapper;
import eqlee.ctm.api.pay.entity.Pay;
import eqlee.ctm.api.pay.entity.PayResult;
import eqlee.ctm.api.pay.entity.query.ApplyPayResultQuery;
import eqlee.ctm.api.pay.entity.query.PayResultQuery;
import eqlee.ctm.api.pay.entity.query.ResultQuery;
import eqlee.ctm.api.pay.entity.vo.GetApplyIdVo;
import eqlee.ctm.api.pay.service.IPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author qf
 * @Date 2019/10/28
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PayServiceImpl extends ServiceImpl<PayMapper, Pay> implements IPayService {


    /**
     * 增加支付信息
     * @param pay
     */
    @Override
    public PayResult insertPayInfo(Pay pay) {
        int insert = baseMapper.insert(pay);
        PayResult result = new PayResult();
        if (insert <= 0) {
            result.setResult("增加支付信息失败");
        } else {
            result.setResult("ok");
        }

        return result;
    }

    /**
     * 修改报名表支付状态
     * @param applyNo
     */
    @Override
    public void updateApplyPayStatus(String applyNo) {
        baseMapper.updateApplyPayStatus(applyNo);
    }

    /**
     * 支付失败删除该报名记录并删除审核记录
     * @param applyNo
     * @return
     */
    @Override
    public PayResult deleteApplyInfo(String applyNo) {
        //先得到该报名单的报名ID
        GetApplyIdVo applyId = baseMapper.getApplyId(applyNo);

        //删除审核表的审核记录
        int exaInfo = baseMapper.deleteExaInfo(applyId.getApplyId());

        PayResult result = new PayResult();
        if (exaInfo <= 0) {
            result.setResult("删除审核记录失败");
            return result;
        }

        int applyInfo = baseMapper.deleteApplyInfo(applyId.getApplyId());

        if (applyInfo <= 0) {
            result.setResult("删除报名记录失败");
            return result;
        }

        result.setResult("OK");
        return result;
    }

    /**
     * 查询支付结果
     * @param applyNo
     * @return
     */
    @Override
    public ResultQuery queryPayResult(String applyNo) {
        PayResultQuery payResult = baseMapper.queryPayResult(applyNo);

        if (payResult == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"请重新支付");
        }

        ResultQuery result = new ResultQuery();
        result.setId(payResult.getId());
        result.setApplyNo(payResult.getApplyNo());
        result.setPayDate(payResult.getPayDate());
        result.setThirdPartyNumber(payResult.getThirdPartyNumber());

        if (payResult.getPayStatus() == 0 || payResult.getPayStatus() == 2) {
            result.setPayStatus("支付失败");
        } else {
            result.setPayStatus("支付成功");
        }

        //查询报名表字段
        ApplyPayResultQuery query = baseMapper.queryApplyResult(applyNo);

        if (query == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "订单号传入有误");
        }

        result.setMoney(query.getMoney());

        if (query.getApplyStatus()) {
            result.setApplyStatus("更新报名表付款成功");
        } else {
            result.setApplyStatus("更新报名表付款失败");
        }

        return result;
    }
}
