package eqlee.ctm.apply.entry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.entity.bo.*;
import eqlee.ctm.apply.entry.entity.query.*;
import eqlee.ctm.apply.entry.entity.vo.ApplyCountVo;
import eqlee.ctm.apply.entry.entity.vo.ApplyOpenIdVo;
import eqlee.ctm.apply.entry.entity.vo.ApplySeacherVo;
import eqlee.ctm.apply.entry.entity.vo.ApplyVo;
import eqlee.ctm.apply.orders.entity.Vo.LongVo;
import eqlee.ctm.apply.sxpay.entity.PayInfo;

import java.time.LocalDate;
import java.util.List;


/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
public interface IApplyService {

    /**
     * 报名
     * @param applyVo
     * @return
     */
    ApplyPayResultQuery insertApply(ApplyVo applyVo);

    /**
     * 补录
     * @param applyVo
     * @return
     */
    ApplyPayResultQuery insertWithApply(ApplyVo applyVo);

    /**
     * 分页展示报名记录
     * <a>如果只有出发日期，只对出发日期进行条件查询</a>
     * <b>如果只有线路名，区域任意一种，对线路名，或区域进行模糊查询</b>
     * <c>默认参数都为空，对所有分页</c>
     * <d>对出发日期，线路名或者区域查询</d>
     * @param page
     * @param OutDate
     * @param LineNameOrRegion
     * @return
     */
    Page<ApplyQuery> listPage2Apply (Page<ApplyQuery> page,String OutDate, String LineNameOrRegion, String lineName);

    /**
     * 删除报名表
     * @param id
     */
    void deleteApply (Long id);

    /**
     * 分页查询已报名未审核的列表,可以根据出发时间模糊查询
     * 优化
     * @param page
     * @param outDate
     * @param lineName
     * @param type
     * @return
     */
    Page<ApplyDoExaQuery> listPageDo2Apply(Page<ApplyDoExaQuery> page, String outDate, Long lineName, Integer type, String applyDate, Integer exaStatus);


    /**
     * 分页查询所有同行的列表，可以根据出行时间进行条件查询
     * @param page
     * @param OutTime
     * @param CompanyName
     * @return
     */
    Page<ApplyCompanyQuery> listPageDoApply2Company(Page<ApplyCompanyQuery> page, String OutTime, String CompanyName);

    /**
     * 取消报名表
     * @param Id
     */
    void cancelApply(Long Id);

    /**
     * 修改审核状态
     * @param Id
     * @param Status
     * @param type
     */
    void updateExamineStatus(Long Id, Integer Status, Integer type);

    /**
     * 根据id列表查询报名表
     * @param list
     * @return
     */
    List<Apply> listApply (List<Long> list);


    /**
     * 查询公司信息
     * @param Id
     * @return
     */
    Company queryCompany (Long Id);

    /**
     * 查询一条报名记录
     * @param Id
     * @return
     */
    ApplySeacherVo queryById(Long Id);


    /**
     * 我的报名记录(同行)
     * @param page
     * @param lineName
     * @param outDate
     * @param applyTime
     * @param type
     * @param todayType
     * @return
     */
    Page<ApplyCompanyQuery> pageMeApply (Page<ApplyCompanyQuery> page, String lineName, String outDate, String applyTime, Integer type,Integer todayType,String roadName);

    /**
     * 我的报名记录(运营人员)
     * @param page
     * @param lineName
     * @param outDate
     * @param applyTime
     * @param type
     * @param companyUserId
     * @param todayType
     * @return
     */
    Page<ApplyCompanyQuery> pageAdmin2Apply (Page<ApplyCompanyQuery> page, String lineName, String outDate, String applyTime, Integer type, Long companyUserId,Integer todayType,String roadName);


    /**
     * 根据报名表id修改导游选人状态
     * @param id
     */
    void updateGuestStatus (Long id);

    /**
     * 批量修改导游选人的状态
     * @param list
     */
    void updateAllGuestStatus (List<ApplyGuideBo> list);


    /**
     * 分页查询月结的信息
     * type==0 默认
     * type==1 未付款
     * type==2 已付款
     * @param page
     * @param type
     * @param outDate
     * @return
     */
    Page<ApplyMonthQuery> queryMonth2Apply (Page<ApplyMonthQuery> page,Integer type,String outDate);

    /**
     * 修改付款状态
     * @param id
     */
    void updateMonthType (Long id);

    /**
     * 根据订单号回收报名表
     * @param applyNo
     */
    void dopApply (String applyNo);

    /**
     * 回收所有订单
     */
    void dopAllApply ();


    /**
     * 同行月结现结统计
     * @param page
     * @param time
     * @param type
     * @param caiType
     * @return
     */
    Page<ApplyResultCountQuery> pageResult2CountList (Page<ApplyResultCountQuery> page, String time, Integer type, Integer caiType);

    /**
     * 管理员月结现结统计查询
     * @param page
     * @param time
     * @param type
     * @param caiType
     * @param companyUserId
     * @return
     */
    Page<ApplyResultCountQuery> pageResultAdminCountList (Page<ApplyResultCountQuery> page, String time, Integer type, Integer caiType, Long companyUserId);

    /**
     * 返回待付款的支付信息
     * @param applyNo
     * @return
     */
    ApplyPayResultQuery queryPayInfo (String applyNo);


    /**
     *  统计同行当月的数据
     * @return
     */
    ApplyCountBo queryApplyCount ();

    /**
     * 跟剧线路ID查询报名表
     * @param lineId
     * @return
     */
    List<Apply> queryApplyByLineId (Long lineId);

    /**
     * 需改取消状态
     * @param type
     * @param id
     */
    void updateApplyCancel(Integer type, Long id);

    /**
     * 查询一些支付信息
     * 以及报名的一些信息
     * @param applyNo
     * @return
     */
    ApplyDoExaInfo queryApplyDoExaInfo (String applyNo);

    /**
     * 修改传的月份的授信金额状态
     * @param s
     */
    void updateSxPriceStatus (String s);

    /**
     *  查询auto
     * @return
     */
    ApplyOpenIdVo queryAuto (Long id);

    /**
     *  查询统计的详情
     * @param page
     * @param outDate
     * @param companyName
     * @param lineName
     * @return
     */
    Page<ApplyCountVo> queryCountInfo (Page<ApplyCountVo> page,String outDate, String companyName, String lineName);


    /**
     *  查询财务统计的详情
     * @param page
     * @param outDate
     * @param companyName
     * @param lineName
     * @return
     */
    Page<ApplyCountCaiBo> queryCountInfo2 (Page<ApplyCountVo> page, String outDate, String companyName, String lineName);

    /**
     *  查询公司信息和支付情况确认情况
     * @param id
     * @return
     */
    ApplyCompanyInfo queryInfo (Long id);

    /**
     *   财务确认
     * @param outDate
     * @param companyName
     */
    void upCaiMonthCount (String outDate, String companyName);

    /**
     *  查询待审核条数
     * @return
     */
    ApplyExaCountQuery queryCount ();

}
