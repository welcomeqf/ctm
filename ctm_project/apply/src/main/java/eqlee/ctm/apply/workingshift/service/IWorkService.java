package eqlee.ctm.apply.workingshift.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.carInfo.entity.query.CarInfoQuery;
import eqlee.ctm.apply.carInfo.entity.vo.CarInfoInsertVo;
import eqlee.ctm.apply.workingshift.entity.WorkingShift;
import eqlee.ctm.apply.workingshift.entity.vo.WorkingShiftVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author qf
 * @date 2020/1/15
 * @vesion 1.0
 **/
public interface IWorkService {


   /**
    * 获取班次分页列表
    */
   Page<WorkingShift> queryAllWorkingShif(Integer current, Integer size);

   void addWorkingShift(WorkingShiftVo workingShiftVo);

   void updateWorkingShift(WorkingShiftVo workingShiftVo);

   void deleteWorkingShift(Long Id);


}
