package eqlee.ctm.apply.entry.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.entity.query.*;
import eqlee.ctm.apply.entry.entity.vo.ApplyOpenIdVo;
import eqlee.ctm.apply.orders.entity.Vo.LongVo;
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

    /**
     * 默认查询所有报名表记录
     * @param page
     * @return
     */
    Page<ApplyQuery> listPageApply(Page<ApplyQuery> page);

    /**
     * 分页及时间条件查询报名记录
     * @param page
     * @param OutDate
     * @return
     */
    Page<ApplyQuery> listPageApplyByDate(Page<ApplyQuery> page,
                                         @Param("OutDate") LocalDate OutDate);

    /**
     * 分页及线路名或区域模糊查询报名记录
     * @param page
     * @param LineNameOrRegion
     * @return
     */
    Page<ApplyQuery> listPageApplyByLine(Page<ApplyQuery> page,
                                         @Param("LineNameOrRegion") String LineNameOrRegion);



    /**
     * 分页及时间条件查询及线路名或区域模糊查询报名记录
     * @param page
     * @param LineNameOrRegion
     * @param OutDate
     * @return
     */
    Page<ApplyQuery> listPageApplyByAll(Page<ApplyQuery> page,
                                        @Param("LineNameOrRegion") String LineNameOrRegion,
                                        @Param("OutDate") LocalDate OutDate);

    /**
     * 分页查询所有已报名的数据
     * @param page
     * @return
     */
    Page<ApplyDoExaQuery> listPageDoApply(Page<ApplyDoExaQuery> page);

    /**
     * 根据线路名模糊查询
     * @param page
     * @param LineName
     * @return
     */
    Page<ApplyDoExaQuery> listPageDoApplyByLineName(Page<ApplyDoExaQuery> page, @Param("LineName") String LineName);

    /**
     * 根据出发时间模糊查询
     * @param page
     * @param outDate
     * @return
     */
    Page<ApplyDoExaQuery> listPageDoApplyByNo(Page<ApplyDoExaQuery> page, @Param("outDate") LocalDate outDate);
    /**
     * 分页查询所有已报名的数据
     * 对订单号和线路名模糊查询
     * @param page
     * @param LineName
     * @param outDate
     * @return
     */
    Page<ApplyDoExaQuery> listPageDoApplyByNoWithLine(Page<ApplyDoExaQuery> page,
                                                @Param("LineName") String LineName,
                                                @Param("outDate") LocalDate outDate);

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


    /**
     * 以下是审核完成的报名记录
     */

    /**
     * 分页查询所有已报名的数据
     * @param page
     * @return
     */
    Page<ApplyDoExaQuery> toListPageDoApply(Page<ApplyDoExaQuery> page);

    /**
     * 根据线路名模糊查询
     * @param page
     * @param LineName
     * @return
     */
    Page<ApplyDoExaQuery> toListPageDoApplyByLineName(Page<ApplyDoExaQuery> page, @Param("LineName") String LineName);

    /**
     * 根据出发时间模糊查询
     * @param page
     * @param outDate
     * @return
     */
    Page<ApplyDoExaQuery> toListPageDoApplyByNo(Page<ApplyDoExaQuery> page, @Param("outDate") LocalDate outDate);
    /**
     * 分页查询所有已报名的数据
     * 对订单号和线路名模糊查询
     * @param page
     * @param LineName
     * @param outDate
     * @return
     */
    Page<ApplyDoExaQuery> toListPageDoApplyByNoWithLine(Page<ApplyDoExaQuery> page,
                                                   @Param("LineName") String LineName,
                                                   @Param("outDate") LocalDate outDate);


    /**
     * 分页查询所有已报名加类型的数据
     * @param page
     * @param ApplyType
     * @return
     */
    Page<ApplyDoExaQuery> toListPageDoExa(Page<ApplyDoExaQuery> page,
                                          @Param("ApplyType") String ApplyType);

    /**
     * 根据线路名加类型模糊查询
     * @param page
     * @param LineName
     * @param ApplyType
     * @return
     */
    Page<ApplyDoExaQuery> toListPageDoExaByLineName(Page<ApplyDoExaQuery> page,
                                                    @Param("LineName") String LineName,
                                                    @Param("ApplyType") String ApplyType);

    /**
     * 根据出发时间加类型模糊查询
     * @param page
     * @param outDate
     * @param ApplyType
     * @return
     */
    Page<ApplyDoExaQuery> toListPageDoExaByNo(Page<ApplyDoExaQuery> page,
                                              @Param("outDate") LocalDate outDate,
                                              @Param("ApplyType") String ApplyType);
    /**
     * 分页查询所有已报名的数据
     * 对订单号和线路名加类型模糊查询
     * @param page
     * @param LineName
     * @param outDate
     * @param ApplyType
     * @return
     */
    Page<ApplyDoExaQuery> toListPageDoExaByNoWithLine(Page<ApplyDoExaQuery> page,
                                                        @Param("LineName") String LineName,
                                                        @Param("outDate") LocalDate outDate,
                                                      @Param("ApplyType") String ApplyType);


    //以下是取消报名审核记录的未审核的记录

    /**
     * 分页查询所有已报名的数据
     * @param page
     * @return
     */
    Page<ApplyDoExaQuery> listPageNotCancel(Page<ApplyDoExaQuery> page);

    /**
     * 根据线路名模糊查询
     * @param page
     * @param LineName
     * @return
     */
    Page<ApplyDoExaQuery> listPageNotCancelByLineName(Page<ApplyDoExaQuery> page, @Param("LineName") String LineName);

    /**
     * 根据出发时间模糊查询
     * @param page
     * @param outDate
     * @return
     */
    Page<ApplyDoExaQuery> listPageNotCancelByTime(Page<ApplyDoExaQuery> page, @Param("outDate") LocalDate outDate);
    /**
     * 分页查询所有已报名的数据
     * 对订单号和线路名模糊查询
     * @param page
     * @param LineName
     * @param outDate
     * @return
     */
    Page<ApplyDoExaQuery> listPageNotCancelByTimeWithLine(Page<ApplyDoExaQuery> page,
                                                   @Param("LineName") String LineName,
                                                   @Param("outDate") LocalDate outDate);

    //我的报名记录
    /**
     * 分页查询我的报名记录
     * @param page
     * @return
     */
    Page<ApplyCompanyQuery> listPageDoApply2Me(Page<ApplyCompanyQuery> page,
                                               @Param("id") Long id);

    /**
     * 分页查询我的报名记录
     * 通过出行时间进行条件选择
     * @param page
     * @param OutDate
     * @return
     */
    Page<ApplyCompanyQuery> listPageDoApply2MeByTime(Page<ApplyCompanyQuery> page,
                                                          @Param("OutDate") LocalDate OutDate,
                                                     @Param("id") Long id);

    /**
     * 分页查询我的报名记录
     * 通过线路名进行条件选择
     * @param page
     * @param lineName
     * @return
     */
    Page<ApplyCompanyQuery> listPageDoApply2MeByName(Page<ApplyCompanyQuery> page,
                                                  @Param("lineName") String lineName,
                                                     @Param("id") Long id);

    /**
     * 分页查询我的报名记录
     * 通过出行时间和线路名进行条件选择
     * @param page
     * @param lineName
     * @param OutTime
     * @return
     */
    Page<ApplyCompanyQuery> listPageDoApply2MeByNameAndTime(Page<ApplyCompanyQuery> page,
                                                        @Param("lineName") String lineName,
                                                         @Param("OutTime") LocalDate OutTime,
                                                            @Param("id") Long id);


    /**
     * 分页查询我的报名记录
     * @param page
     * @return
     */
    Page<ApplyCompanyQuery> listPageDoApply2MeCancel(Page<ApplyCompanyQuery> page,
                                               @Param("id") Long id);

    /**
     * 分页查询我的报名记录
     * 通过出行时间进行条件选择
     * @param page
     * @param OutDate
     * @return
     */
    Page<ApplyCompanyQuery> listPageDoApply2MeByTimeCancel(Page<ApplyCompanyQuery> page,
                                                     @Param("OutDate") LocalDate OutDate,
                                                     @Param("id") Long id);

    /**
     * 分页查询我的报名记录
     * 通过线路名进行条件选择
     * @param page
     * @param lineName
     * @return
     */
    Page<ApplyCompanyQuery> listPageDoApply2MeByNameCancel(Page<ApplyCompanyQuery> page,
                                                     @Param("lineName") String lineName,
                                                     @Param("id") Long id);

    /**
     * 分页查询我的报名记录
     * 通过出行时间和线路名进行条件选择
     * @param page
     * @param lineName
     * @param OutTime
     * @return
     */
    Page<ApplyCompanyQuery> listPageDoApply2MeByNameAndTimeCancel(Page<ApplyCompanyQuery> page,
                                                            @Param("lineName") String lineName,
                                                            @Param("OutTime") LocalDate OutTime,
                                                            @Param("id") Long id);


    //根据报名日期查询
    /**
     * 分页查询我的报名记录
     * 通过出行时间进行条件选择
     * @param page
     * @param startDate
     * @param endDate
     * @return
     */
    Page<ApplyCompanyQuery> listPageDoApply2MeByTimeWithAT(Page<ApplyCompanyQuery> page,
                                                     @Param("startDate") LocalDateTime startDate,
                                                     @Param("endDate") LocalDateTime endDate,
                                                     @Param("id") Long id);


    /**
     * 分页查询我的报名记录
     * 通过出行时间和线路名进行条件选择
     * @param page
     * @param startDate
     * @param endDate
     * @return
     */
    Page<ApplyCompanyQuery> listPageDoApply2MeByNameAndTimeWithAT(Page<ApplyCompanyQuery> page,
                                                            @Param("lineName") String lineName,
                                                            @Param("startDate") LocalDateTime startDate,
                                                            @Param("endDate") LocalDateTime endDate,
                                                            @Param("id") Long id);



    /**
     * 分页查询我的报名记录
     * 通过出行时间进行条件选择
     * @param page
     * @param startDate
     * @param endDate
     * @return
     */
    Page<ApplyCompanyQuery> listPageDoApply2MeByTimeCancelWithAT(Page<ApplyCompanyQuery> page,
                                                                 @Param("startDate") LocalDateTime startDate,
                                                                 @Param("endDate") LocalDateTime endDate,
                                                                 @Param("id") Long id);


    /**
     * 分页查询我的报名记录
     * 通过出行时间和线路名进行条件选择
     * @param page
     * @param lineName
     * @param startDate
     * @param endDate
     * @return
     */
    Page<ApplyCompanyQuery> listPageDoApply2MeByNameAndTimeCancelWithAT(Page<ApplyCompanyQuery> page,
                                                              @Param("lineName") String lineName,
                                                              @Param("startDate") LocalDateTime startDate,
                                                              @Param("endDate") LocalDateTime endDate,
                                                              @Param("id") Long id);

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


    //运营查询我的报名记录的数据

    /**
     * 分页查询我的报名记录
     * @param page
     * @return
     */
    Page<ApplyCompanyQuery> listAdminDoApply2Me(Page<ApplyCompanyQuery> page);

    /**
     * 分页查询我的报名记录
     * 通过出行时间进行条件选择
     * @param page
     * @param OutDate
     * @return
     */
    Page<ApplyCompanyQuery> listAdminDoApply2MeByTime(Page<ApplyCompanyQuery> page,
                                                     @Param("OutDate") LocalDate OutDate);

    /**
     * 分页查询我的报名记录
     * 通过线路名进行条件选择
     * @param page
     * @param lineName
     * @return
     */
    Page<ApplyCompanyQuery> listAdminDoApply2MeByName(Page<ApplyCompanyQuery> page,
                                                     @Param("lineName") String lineName);

    /**
     * 分页查询我的报名记录
     * 通过出行时间和线路名进行条件选择
     * @param page
     * @param lineName
     * @param OutTime
     * @return
     */
    Page<ApplyCompanyQuery> listAdminDoApply2MeByNameAndTime(Page<ApplyCompanyQuery> page,
                                                            @Param("lineName") String lineName,
                                                            @Param("OutTime") LocalDate OutTime);


    /**
     * 分页查询我的报名记录
     * @param page
     * @return
     */
    Page<ApplyCompanyQuery> listAdminDoApply2MeCancel(Page<ApplyCompanyQuery> page);

    /**
     * 分页查询我的报名记录
     * 通过出行时间进行条件选择
     * @param page
     * @param OutDate
     * @return
     */
    Page<ApplyCompanyQuery> listAdminDoApply2MeByTimeCancel(Page<ApplyCompanyQuery> page,
                                                           @Param("OutDate") LocalDate OutDate);

    /**
     * 分页查询我的报名记录
     * 通过线路名进行条件选择
     * @param page
     * @param lineName
     * @return
     */
    Page<ApplyCompanyQuery> listAdminDoApply2MeByNameCancel(Page<ApplyCompanyQuery> page,
                                                           @Param("lineName") String lineName);

    /**
     * 分页查询我的报名记录
     * 通过出行时间和线路名进行条件选择
     * @param page
     * @param lineName
     * @param OutTime
     * @return
     */
    Page<ApplyCompanyQuery> listAdminDoApply2MeByNameAndTimeCancel(Page<ApplyCompanyQuery> page,
                                                                  @Param("lineName") String lineName,
                                                                  @Param("OutTime") LocalDate OutTime);


    //根据报名日期查询
    /**
     * 分页查询我的报名记录
     * 通过出行时间进行条件选择
     * @param page
     * @param startDate
     * @param endDate
     * @return
     */
    Page<ApplyCompanyQuery> listAdminDoApply2MeByTimeWithAT(Page<ApplyCompanyQuery> page,
                                                           @Param("startDate") LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate);


    /**
     * 分页查询我的报名记录
     * 通过出行时间和线路名进行条件选择
     * @param page
     * @param startDate
     * @param endDate
     * @return
     */
    Page<ApplyCompanyQuery> listAdminDoApply2MeByNameAndTimeWithAT(Page<ApplyCompanyQuery> page,
                                                                  @Param("lineName") String lineName,
                                                                  @Param("startDate") LocalDateTime startDate,
                                                                  @Param("endDate") LocalDateTime endDate);



    /**
     * 分页查询我的报名记录
     * 通过出行时间进行条件选择
     * @param page
     * @param startDate
     * @param endDate
     * @return
     */
    Page<ApplyCompanyQuery> listAdminDoApply2MeByTimeCancelWithAT(Page<ApplyCompanyQuery> page,
                                                                 @Param("startDate") LocalDateTime startDate,
                                                                 @Param("endDate") LocalDateTime endDate);


    /**
     * 分页查询我的报名记录
     * 通过出行时间和线路名进行条件选择
     * @param page
     * @param lineName
     * @param startDate
     * @param endDate
     * @return
     */
    Page<ApplyCompanyQuery> listAdminDoApply2MeByNameAndTimeCancelWithAT(Page<ApplyCompanyQuery> page,
                                                                        @Param("lineName") String lineName,
                                                                        @Param("startDate") LocalDateTime startDate,
                                                                        @Param("endDate") LocalDateTime endDate);

    /**
     * 查询全部
     * @param page
     * @return
     */
    Page<ApplyMonthQuery> queryMonthApply(Page<ApplyMonthQuery> page);


    /**
     * 根据出行日期查询全部
     * @param page
     * @param outDate
     * @return
     */
    Page<ApplyMonthQuery> queryMonthApplyByTime(Page<ApplyMonthQuery> page,
                                                @Param("outDate") LocalDate outDate);

    /**
     * 查询未付款信息
     * @param page
     * @return
     */
    Page<ApplyMonthQuery> queryMonthWeiApply(Page<ApplyMonthQuery> page);

    /**
     * 根据出行日期查询未付款信息
     * @param page
     * @param outDate
     * @return
     */
    Page<ApplyMonthQuery> queryMonthWeiApplyByTime(Page<ApplyMonthQuery> page,
                                                   @Param("outDate") LocalDate outDate);


    /**
     * 查询已付款信息
     * @param page
     * @return
     */
    Page<ApplyMonthQuery> queryMonthYiApply(Page<ApplyMonthQuery> page);

    /**
     * 根据出行日期查询已付款信息
     * @param page
     * @param outDate
     * @return
     */
    Page<ApplyMonthQuery> queryMonthYiApplyByTime(Page<ApplyMonthQuery> page,
                                                  @Param("outDate") LocalDate outDate);




    //以下是同行月结现结账单

    /**
     * 查询所有月结(现结)账单
     * @param page
     * @param payType
     * @return
     */
    Page<ApplyResultCountQuery> pageResult (Page<ApplyResultCountQuery> page,
                                            @Param("payType") Integer payType);

    /**
     * 根据线路名模糊查询现结(月结)账单
     * @param page
     * @param payType
     * @param lineName
     * @return
     */
    Page<ApplyResultCountQuery> pageResultByLineName (Page<ApplyResultCountQuery> page,
                                                      @Param("payType") Integer payType,
                                                      @Param("lineName") String lineName);

    /**
     * 根据出发时间查询现结(月结)账单
     * @param page
     * @param payType
     * @param outDate
     * @return
     */
    Page<ApplyResultCountQuery> pageResultByOutDate (Page<ApplyResultCountQuery> page,
                                                     @Param("payType") Integer payType,
                                                     @Param("outDate") LocalDate outDate);


    /**
     * 根据出发时间和线路名模糊查询现结(月结)账单
     * @param page
     * @param payType
     * @param outDate
     * @param lineName
     * @return
     */
    Page<ApplyResultCountQuery> pageResultByTimeAndName (Page<ApplyResultCountQuery> page,
                                                         @Param("payType") Integer payType,
                                                         @Param("outDate") LocalDate outDate,
                                                         @Param("lineName") String lineName);


    /**
     * 批量回收订单
     * @param list
     * @return
     */
    Integer updateAllApplyStatus (List<ApplyScheQuery> list);

    /**
     * 查询所有管理人的id
     * @param roleName
     * @return
     */
    List<Long> queryAllAdmin (String roleName);

    /**
     * 批量修改导游选人的状态
     * @param list
     * @return
     */
    Integer updateAllApply (List<LongVo> list);
}
