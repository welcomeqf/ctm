package eqlee.ctm.apply.workingshift.dao;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.carInfo.entity.CarInfo;
import eqlee.ctm.apply.carInfo.entity.query.CarInfoQuery;
import eqlee.ctm.apply.carInfo.entity.vo.DateVo;
import eqlee.ctm.apply.workingshift.entity.WorkingShift;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * @author qf
 * @date 2020/1/15
 * @vesion 1.0
 **/
@Component
public interface WorkMapper extends IBaseMapper<WorkingShift> {


   /**
    * 查询该天未出行的车辆
    * @param list
    * @return
    */
   List<CarInfoQuery> queryCarInfo(List<DateVo> list);
}
