package eqlee.ctm.api.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.utils.DateUtil;
import eqlee.ctm.api.pay.dao.PayMapper;
import eqlee.ctm.api.pay.entity.Pay;
import eqlee.ctm.api.pay.entity.PayResult;
import eqlee.ctm.api.pay.entity.query.ApplyPayResultQuery;
import eqlee.ctm.api.pay.entity.query.MonthPayResultQuery;
import eqlee.ctm.api.pay.entity.query.PayResultQuery;
import eqlee.ctm.api.pay.entity.query.ResultQuery;
import eqlee.ctm.api.pay.entity.vo.GetApplyIdVo;
import eqlee.ctm.api.pay.service.IPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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
    public synchronized PayResult insertPayInfo(Pay pay) {

        LambdaQueryWrapper<Pay> wrapper = new LambdaQueryWrapper<Pay>()
              .eq(Pay::getApplyNo,pay.getApplyNo())
              .eq(Pay::getThirdPartyNumber,pay.getThirdPartyNumber());

        Pay one = baseMapper.selectOne(wrapper);
        PayResult result = new PayResult();
        if (one != null) {
            return null;
        }

        int insert = baseMapper.insert(pay);
        if (insert <= 0) {
            result.setResult("增加支付信息失败");
        } else {
            result.setResult("ok");
        }

        //修改报名表的支付状态


        return result;
    }

    /**
     * 修改报名表支付状态
     * @param applyNo
     * @param payInfo
     */
    @Override
    public void updateApplyPayStatus(String applyNo, Integer payInfo) {
        baseMapper.updateApplyPayStatus(applyNo,payInfo);
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
        List<PayResultQuery> list = baseMapper.queryPayResult(applyNo);

        if (list.size() == 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"请重新支付");
        }

        ResultQuery result = new ResultQuery();
        for (PayResultQuery payResult : list) {
            result.setId(payResult.getId());
            result.setApplyNo(payResult.getApplyNo());
            result.setPayDate(payResult.getPayDate());
            result.setThirdPartyNumber(payResult.getThirdPartyNumber());

            if (payResult.getPayStatus() == 0 || payResult.getPayStatus() == 2) {
                result.setPayStatus("支付失败");
            } else {
                result.setPayStatus("支付成功");
            }
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

    @Override
    public ResultQuery queryMonthResult(String applyNo) {
        List<PayResultQuery> list = baseMapper.queryPayResult(applyNo);

        if (list.size() == 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"请重新支付");
        }

        ResultQuery result = new ResultQuery();
        for (PayResultQuery payResult : list) {
            result.setId(payResult.getId());
            result.setApplyNo(payResult.getApplyNo());
            result.setPayDate(payResult.getPayDate());
            result.setThirdPartyNumber(payResult.getThirdPartyNumber());

            if (payResult.getPayStatus() == 0 || payResult.getPayStatus() == 2) {
                result.setPayStatus("支付失败");
            } else {
                result.setPayStatus("支付成功");
            }
        }
        return result;
    }


    /**
     * 查询支付后的凭证
     * @param monthNo
     * @return
     */
    @Override
    public MonthPayResultQuery queryMonthPayResult(String monthNo) {

        LambdaQueryWrapper<Pay> wrapper = new LambdaQueryWrapper<Pay>()
              .eq(Pay::getApplyNo,monthNo);

        Pay pay = baseMapper.selectOne(wrapper);

        if (pay == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "您可能未支付");
        }

        MonthPayResultQuery query = new MonthPayResultQuery();
        query.setPayDate(DateUtil.formatDateTime(pay.getPayDate()));
        query.setFilePath(pay.getPayPhone());
        if (pay.getPayType() == 0) {
            query.setPayType("微信支付");
        } else if (pay.getPayType() == 1) {
            query.setPayType("支付宝支付");
        } else if (pay.getPayType() == 2) {
            query.setPayType("转账支付");
        }
        query.setThNo(pay.getThirdPartyNumber());
        query.setMonthNo(monthNo);
        query.setPayMoney(pay.getPayMoney());
        query.setPayStatus(pay.getPayStatu());
        return query;
    }

    @Override
    public void upMonthStatus(String startDate, String companyName) {

        LocalDate satrt = DateUtil.parseDate(startDate);

        LocalDate end = satrt.plusMonths(1).minusDays(1);

        Integer integer = baseMapper.upSxTypeStatus(satrt, end, companyName);

        if (integer <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
        }
    }

    /**
     * 获取报名申请表基本信息
     * @param applyNo
     */
    @Override
    public ApplyPayResultQuery queryApplyResult(String applyNo) {
        ApplyPayResultQuery query = baseMapper.queryApplyResult(applyNo);
        return  query;
    }
}
