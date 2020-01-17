package eqlee.ctm.apply.carInfo.dao;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.apply.carInfo.entity.CarInfo;
import eqlee.ctm.apply.carInfo.entity.query.CarInfoQuery;
import eqlee.ctm.apply.carInfo.entity.vo.DateVo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * @author qf
 * @date 2020/1/15
 * @vesion 1.0
 **/
@Component
public interface CarInfoMapper extends IBaseMapper<CarInfo> {


   /**
    * 查询该天未出行的车辆
    * @param list
    * @return
    */
   List<CarInfoQuery> queryCarInfo (List<DateVo> list);
}
