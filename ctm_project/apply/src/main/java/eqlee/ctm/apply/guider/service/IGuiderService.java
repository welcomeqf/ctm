package eqlee.ctm.apply.guider.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.guider.entity.vo.ApplyVo;
import eqlee.ctm.apply.guider.entity.vo.GuiderVo;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/23 0023
 * @Version 1.0
 */
public interface IGuiderService {

    /**
     * 导游首页展示
     * @param page
     * @param outDate
     * @param lineName
     * @param region
     * @return
     */
    Page<GuiderVo> guiderIndex(Page<GuiderVo> page,String outDate,String lineName, String region);


    /**
     * 导游看到的报名表
     * @param current
     * @param size
     * @param lineName
     * @param outDate
     * @param region
     * @return
     */
    Page<ApplyVo>  applyIndex(Integer current,Integer size,String lineName, String outDate, String region);

}
