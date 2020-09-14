package eqlee.ctm.apply.entry.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.entity.bo.*;
import eqlee.ctm.apply.entry.entity.query.*;
import eqlee.ctm.apply.entry.entity.vo.ApplyCountVo;
import eqlee.ctm.apply.entry.entity.vo.ApplyOpenIdVo;
import eqlee.ctm.apply.orders.entity.Vo.LongVo;
import eqlee.ctm.apply.sxpay.entity.PayInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
@Component
public interface ApplyMapper extends IBaseMapper<Apply> {


    //展示报名记录

    /**
     * 分页及时间条件查询及线路名或区域模糊查询报名记录
     * @param page
     * @param lineNameOrRegion
     * @param outDate
     * @return
     */
    Page<ApplyQuery> queryApplyInfo(Page<ApplyQuery> page,
                                    @Param("lineNameOrRegion") String lineNameOrRegion,
                                    @Param("outDate") LocalDate outDate,
                                    @Param("lineName") String lineName);


    //未审核数据

    /**
     *   ---
     *   分页查询已报名未审核的列表,可以根据出发时间模糊查询
     * @param page
     * @param outDate
     * @param lineName
     * @param type
     * @param start
     * @param end
     * @param exaStatus
     * @param list
     * @return
     */
    Page<ApplyDoExaQuery> queryAllExaInfo2 (Page<ApplyDoExaQuery> page,
                                          @Param("outDate") LocalDate outDate,
                                          @Param("lineName") Long lineName,
                                          @Param("type") Integer type,
                                          @Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          @Param("exaStatus") Integer exaStatus,
                                          @Param("list") List<String> list,
                                          @Param("outDateEnd") LocalDate outDateEnd);


    /**
     * 分页查询所有同一公司的报名记录
     * @param page
     * @param CompanyName
     * @return
     */
    Page<ApplyCompanyQuery> listPageDoApply2Company(Page<ApplyCompanyQuery> page,
                                                    @Param("CompanyName") String CompanyName);

    /**
     * 分页查询所有同一公司的报名记录
     * 通过出行时间进行条件选择
     * @param page
     * @param OutTime
     * @param CompanyName
     * @return
     */
    Page<ApplyCompanyQuery> listPageDoApply2CompanyByTime(Page<ApplyCompanyQuery> page,
                                                          @Param("OutTime") LocalDate OutTime,
                                                          @Param("CompanyName") String CompanyName);


    /**
     * 查询公司
     * @param Id
     * @return
     */
    Company queryCompanyById (Long Id);


    //我的报名记录

    /**
     * 我的报名记录(同行)
     * @param page
     * @param lineName
     * @param outTime
     * @param startDate
     * @param endDate
     * @param id
     * @param companyId
     * @param type
     * @param roadName
     * @return
     */
    Page<ApplyCompanyQuery> allCompanyList(Page<ApplyCompanyQuery> page,
                                           @Param("lineName") String lineName,
                                           @Param("outTime") LocalDate outTime,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate,
                                           @Param("id") Long id,
                                           @Param("companyId") Long companyId,
                                           @Param("type") Integer type,
                                           @Param("roadName") String roadName);

    /**
     * 我的报名记录(运营人员)
     * @param page
     * @param lineName
     * @param outTime
     * @param startDate
     * @param endDate
     * @param companyUserId
     * @param type
     * @return
     */
    Page<ApplyCompanyQuery> allCompanyAdminList(Page<ApplyCompanyQuery> page,
                                           @Param("lineName") String lineName,
                                           @Param("outTime") LocalDate outTime,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate,
                                           @Param("companyUserId") Long companyUserId,
                                           @Param("type") Integer type,
                                           @Param("roadName") String roadName);

    /**
     * 查询今日报名的数据
     * @param page
     * @param startDate
     * @param endDate
     * @param id
     * @return
     */
    Page<ApplyCompanyQuery> listToDayApply (Page<ApplyCompanyQuery> page,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate,
                                            @Param("id") Long id);

    /**
     * 今日出行
     * @param page
     * @param outDate
     * @param id
     * @return
     */
    Page<ApplyCompanyQuery> listToDayOut (Page<ApplyCompanyQuery> page,
                                          @Param("outDate") LocalDate outDate,
                                          @Param("id") Long id);

    /**
     * 查询支付信息
     * @param userId
     * @return
     */
    ApplyOpenIdVo queryPayInfo (Long userId);


    //月结统计

    /**
     * 分页查询月结的信息（优化）
     * @param page
     * @param outDate
     * @param type
     * @return
     */
    Page<ApplyMonthQuery> queryAllMonth(Page<ApplyMonthQuery> page,
                                        @Param("outDate") LocalDate outDate,
                                        @Param("type") Integer type);




    //以下是同行月结现结账单

    /**
     * 查询同行月结现结面收账单
     * @param page
     * @param start
     * @param end
     * @param type
     * @param id
     * @param companyId
     * @return
     */
    Page<ApplyResultCountQuery> queryCompanyResultCount (Page<ApplyResultCountQuery> page,
                                                         @Param("start") LocalDate start,
                                                         @Param("end") LocalDate end,
                                                         @Param("type") Integer type,
                                                         @Param("caiType") Integer caiType,
                                                         @Param("id") Long id,
                                                         @Param("companyId") Long companyId,
                                                         @Param("payType") Integer payType);

    /**
     * 管理员月结现结账单核算
     * @param page
     * @param start
     * @param end
     * @param type
     * @param caiType
     * @param companyUserId
     * @return
     */
    Page<ApplyResultCountQuery> queryCompanyAdminResultCount (Page<ApplyResultCountQuery> page,
                                                         @Param("year") String year,
                                                         @Param("month") String month,
                                                         @Param("type") Integer type,
                                                         @Param("caiType") Integer caiType,
                                                         @Param("payType") Integer payType,
                                                         @Param("companyUserId") Long companyUserId);

    /**
     * 批量回收订单
     * @param list
     * @return
     */
    Integer updateAllApplyStatus (List<ApplyScheQuery> list);

    /**
     * 批量修改导游选人的状态
     * @param list
     * @return
     */
    Integer updateAllApply (List<ApplyGuideBo> list);

    /**
     * 查询公司信息
     * @param id
     * @return
     */
    ApplyCompanyBo queryApplyCompanyInfo (Long id);

    /**
     * 查询同行核算统计金额数据
     * @param id
     * @return
     */
    ApplyBo queryApplyCountInfo (Long id);


    /**
     *  查询同行核算统计
     * @param id
     * @param payType
     * @return
     */
    ApplyBo queryApplyCount (@Param("id") Long id,
                             @Param("payType") Integer payType);

    /**
     * 查询当前月使用的金额
     * @param id
     * @return
     */
    List<Apply> queryAllPriceToApply (Long id);

    /**
     * 查询支付信息
     * @param applyNo
     * @return
     */
    ApplyDoExaInfo queryApplyDoExaInfo (String applyNo);


    /**
     * 查询统计的详情
     * @param page
     * @param start
     * @param end
     * @param companyName
     * @param lineIdList
     * @return
     */
    Page<ApplyCountVo> queryCountInfo(@Param("page") Page<ApplyCountVo> page,
                                      @Param("start") LocalDate start,
                                      @Param("end") LocalDate end,
                                      @Param("companyName") String companyName,
                                      @Param("lineIdList") List<String> lineIdList,
                                      @Param("payType") Integer payType);


    /**
     * 查询统计的详情
     * @param page
     * @param start
     * @param end
     * @param companyName
     * @param lineName
     * @return
     */
    Page<ApplyCountCaiBo> queryCountInfo2(@Param("page") Page<ApplyCountVo> page,
                                          @Param("start") LocalDate start,
                                          @Param("end") LocalDate end,
                                          @Param("companyName") String companyName,
                                          @Param("lineName") String lineName,
                                          @Param("payType") Integer payType);

    int queryExamineCount();

    ApplyExaCountQuery queryExamineCount2();



}
