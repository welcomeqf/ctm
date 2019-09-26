package eqlee.ctm.apply.entry.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.entity.query.ApplyCompanyQuery;
import eqlee.ctm.apply.entry.entity.query.ApplyDoQuery;
import eqlee.ctm.apply.entry.entity.query.ApplyQuery;
import eqlee.ctm.apply.entry.entity.query.Company;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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
    Page<ApplyDoQuery> listPageDoApply(Page<ApplyDoQuery> page);

    /**
     * 根据线路名模糊查询
     * @param page
     * @param LineName
     * @return
     */
    Page<ApplyDoQuery> listPageDoApplyByLineName(Page<ApplyDoQuery> page, @Param("LineName") String LineName);

    /**
     * 根据订单号模糊查询
     * @param page
     * @param ApplyNo
     * @return
     */
    Page<ApplyDoQuery> listPageDoApplyByNo(Page<ApplyDoQuery> page, @Param("ApplyNo") String ApplyNo);
    /**
     * 分页查询所有已报名的数据
     * 对订单号和线路名模糊查询
     * @param page
     * @param LineName
     * @param ApplyNo
     * @return
     */
    Page<ApplyDoQuery> listPageDoApplyByNoWithLine(Page<ApplyDoQuery> page,
                                                @Param("LineName") String LineName,
                                                @Param("ApplyNo") String ApplyNo);

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
}
