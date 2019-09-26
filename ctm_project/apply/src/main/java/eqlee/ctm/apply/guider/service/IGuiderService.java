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
     * @param current
     * @param size
     * @param outDate
     * @return
     */
    Page<GuiderVo> guiderIndex(Integer current,Integer size,String outDate);


    /**
     *导游看到的报名表
     * @param current
     * @param size
     * @param outDate
     * @param lineName
     * @return
     */
    Page<ApplyVo>  applyIndex(Integer current,Integer size,String outDate,String lineName);

}
