package eqlee.ctm.apply.guider.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.guider.entity.vo.ApplyVo;
import eqlee.ctm.apply.guider.entity.vo.GuiderCountNumber;
import eqlee.ctm.apply.guider.entity.vo.GuiderVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/23 0023
 * @Version 1.0
 */
@Component
public interface GuiderMapper {

     /**
      *  根据日期和线路查询
      * @param page
      * @param outDate
      * @param lineName
      * @return
      */
     Page<GuiderVo> guiderIndexByLineAndTime(Page<GuiderVo> page,
                                @Param("outDate") LocalDate outDate,
                                @Param("lineName") String lineName);

     /**
      *  根据日期查询
      * @param page
      * @param outDate
      * @return
      */
     Page<GuiderVo> guiderIndexByTime(Page<GuiderVo> page,
                                @Param("outDate") LocalDate outDate);

     /**
      *  根据线路查询
      * @param page
      * @param lineName
      * @return
      */
     Page<GuiderVo> guiderIndexByLine(Page<GuiderVo> page,
                                      @Param("lineName") String lineName);



     /**
      *  ----查询导游首页
      * @param page
      * @param outDate
      * @param region
      * @param cityList
      * @param list
      * @return
      */
     Page<GuiderVo> guiderIndex2(Page<GuiderVo> page,
                                 @Param("outDate") LocalDate outDate,
                                 @Param("region") String region,
                                 @Param("cityList") List<String> cityList,
                                 @Param("list") List<Long> list,
                                 @Param("selectNot") Integer selectNot);


     /**
      * 查询人数统计
      * @param outDate
      * @param region
      * @param cityList
      * @param list
      * @param type
      * @return
      */
     GuiderCountNumber queryCountNumberInfo2 (@Param("outDate") LocalDate outDate,
                                             @Param("region") String region,
                                              @Param("cityList") List<String> cityList,
                                              @Param("list") List<Long> list,
                                              @Param("type") Integer type);


     /**
      *   查询选人的信息
      * @param page
      * @param lineId
      * @param outDate
      * @return
      */
     Page<ApplyVo> applyIndex(Page<ApplyVo> page,
                              @Param("lineId") Long lineId,
                              @Param("outDate") LocalDate outDate);

     /**
      * 根据区域模糊查询要选人信息
      * @param page
      * @param lineId
      * @param outDate
      * @param region
      * @return
      */
     Page<ApplyVo> queryApplyPerson(Page<ApplyVo> page,
                                   @Param("lineId") Long lineId,
                                   @Param("outDate") LocalDate outDate,
                                   @Param("region") String region);
}
