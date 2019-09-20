package eqlee.ctm.apply.entry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.entry.entity.query.ApplyCompanyQuery;
import eqlee.ctm.apply.entry.entity.query.ApplyDoQuery;
import eqlee.ctm.apply.entry.entity.query.ApplyQuery;
import eqlee.ctm.apply.entry.entity.query.ApplyUpdateInfo;
import eqlee.ctm.apply.entry.entity.vo.ApplyVo;


/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
public interface IApplyService {

    /**
     * 报名
     * @param applyVo
     */
    void insertApply(ApplyVo applyVo);


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
     * 分页查询已报名的列表,可以根据订单号模糊查询
     * @param page
     * @param ApplyNo
     * @param LineName
     * @return
     */
    Page<ApplyDoQuery> listPageDoApply(Page<ApplyDoQuery> page, String ApplyNo, String LineName);


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

}
