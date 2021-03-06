package eqlee.ctm.api.pay.dao;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.api.pay.entity.Pay;
import eqlee.ctm.api.pay.entity.query.ApplyPayResultQuery;
import eqlee.ctm.api.pay.entity.query.PayResultQuery;
import eqlee.ctm.api.pay.entity.vo.GetApplyIdVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

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
     * @param payInfo
     */
    void updateApplyPayStatus (@Param("applyNo") String applyNo,
                               @Param("payInfo") Integer payInfo);

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
    List<PayResultQuery> queryPayResult(String applyNo);

    /**
     * 查询报名支付结果
     * @param applyNo
     * @return
     */
    ApplyPayResultQuery queryApplyResult (String applyNo);


    /**
     * 修改报名表支付状态（转账）
     * @param start
     * @param end
     * @param companyName
     * @return
     */
    Integer upSxTypeStatus (@Param("start") LocalDate start,
                            @Param("end") LocalDate end,
                            @Param("companyName") String companyName);
}
