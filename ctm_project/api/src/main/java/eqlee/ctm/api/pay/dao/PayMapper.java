package eqlee.ctm.api.pay.dao;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.api.pay.entity.Pay;
import eqlee.ctm.api.pay.entity.query.ApplyPayResultQuery;
import eqlee.ctm.api.pay.entity.query.PayResultQuery;
import eqlee.ctm.api.pay.entity.vo.GetApplyIdVo;
import org.springframework.stereotype.Component;

/**
 * @Author qf
 * @Date 2019/10/28
 * @Version 1.0
 */
@Component
public interface PayMapper extends IBaseMapper<Pay> {


    /**
     * 修改报名表状态
     * @param applyNo
     * @return
     */
    int updateApplyPayStatus (String applyNo);

    /**
     * 得到报名id
     * @param applyNo
     * @return
     */
    GetApplyIdVo getApplyId (String applyNo);

    /**
     * 删除该报名记录
     * @param id
     * @return
     */
    int deleteApplyInfo (Long id);

    /**
     * 删除审核记录
     * @param applyId
     * @return
     */
    int deleteExaInfo (Long applyId);

    /**
     * 查询支付结果
     * @param applyNo
     * @return
     */
    PayResultQuery queryPayResult(String applyNo);

    /**
     * 查询报名支付结果
     * @param applyNo
     * @return
     */
    ApplyPayResultQuery queryApplyResult (String applyNo);
}
