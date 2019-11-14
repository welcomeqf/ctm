package eqlee.ctm.apply.entry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.entity.query.*;
import eqlee.ctm.apply.entry.entity.vo.ApplySeacherVo;
import eqlee.ctm.apply.entry.entity.vo.ApplyVo;

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
    Page<ApplyQuery> listPageApply(Page<ApplyQuery> page,String OutDate, String LineNameOrRegion);

    /**
     * 修改报名表
     * @param updateInfo
     */
    void updateApply(ApplyUpdateInfo updateInfo);

    /**
     * 分页查询已报名未审核的列表,可以根据出发时间模糊查询
     * @param page
     * @param OutDate
     * @param LineName
     * @param ApplyType
     * @return
     */
    Page<ApplyDoExaQuery> listPageDoApply(Page<ApplyDoExaQuery> page, String OutDate, String LineName, String ApplyType);


    /**
     * 分页查询已报名已审核的列表,可以根据出发时间模糊查询
     * @param page
     * @param OutDate
     * @param LineName
     * @param ApplyType
     * @return
     */
    Page<ApplyDoExaQuery> toListPageDoApply(Page<ApplyDoExaQuery> page, String OutDate, String LineName, String ApplyType);


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
     */
    void updateExamineStatus(Long Id, Integer Status);

    /**
     * 查询报名表
     * @return
     */
    List<Apply> selectAllApply();

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
     * 我的报名记录
     * @param page
     * @param lineName
     * @param outDate
     * @return
     */
    Page<ApplyCompanyQuery> page2MeApply (Page<ApplyCompanyQuery> page, String lineName, String outDate);


    /**
     * 根据报名表id修改导游选人状态
     * @param id
     */
    void updateGuestStatus (Long id);

    /**
     * 查询报名表
     * @param outDate
     * @param lineName
     * @return
     */
    List<Apply> queryApplyByTime (LocalDate outDate, String lineName);

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
    Page<ApplyMonthQuery> queryMonthApply (Page<ApplyMonthQuery> page,Integer type,String outDate);

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
     * @param payType
     * @param outDate
     * @param lineName
     * @return
     */
    Page<ApplyResultCountQuery> pageResultCountList (Page<ApplyResultCountQuery> page, Integer payType,String outDate, String lineName);


    /**
     * 返回待付款的支付信息
     * @param applyNo
     * @return
     */
    ApplyPayResultQuery queryPayInfo (String applyNo);

    /**
     * 查询同行报名审核以及取消审核条数
     * @return
     */
    ApplyReadCountQuery queryReadCount ();

    /**
     * 查询所有管理员Id
     * @return
     */
    List<Long> queryAdminIds ();



}
