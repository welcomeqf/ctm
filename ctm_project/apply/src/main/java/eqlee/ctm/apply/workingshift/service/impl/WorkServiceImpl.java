package eqlee.ctm.apply.workingshift.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.IdGenerator;
import eqlee.ctm.apply.carInfo.dao.CarInfoMapper;
import eqlee.ctm.apply.carInfo.entity.CarInfo;
import eqlee.ctm.apply.carInfo.entity.query.CarInfoQuery;
import eqlee.ctm.apply.carInfo.entity.vo.CarInfoInsertVo;
import eqlee.ctm.apply.carInfo.entity.vo.DateVo;
import eqlee.ctm.apply.carInfo.service.ICarInfoService;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.service.IOrdersService;
import eqlee.ctm.apply.workingshift.dao.WorkMapper;
import eqlee.ctm.apply.workingshift.entity.WorkingShift;
import eqlee.ctm.apply.workingshift.entity.vo.WorkingShiftVo;
import eqlee.ctm.apply.workingshift.service.IWorkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qf
 * @date 2020/1/15
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkServiceImpl extends ServiceImpl<WorkMapper, WorkingShift> implements IWorkService {

   @Autowired
   private LocalUser localUser;

   /**
    * 查询车辆信息
    * @return
    */
   @Override
   public Page<WorkingShift> queryAllWorkingShif(Integer current, Integer size) {

      LambdaQueryWrapper<WorkingShift> queryWrapper = new LambdaQueryWrapper<WorkingShift>();
      Page<WorkingShift> page = new Page<WorkingShift>();
      page.setSize(size);
      page.setCurrent(current);
      baseMapper.selectPage(page,queryWrapper);
      return page;
   }

   /**
    * 增加班次
    */
   @Override
   public void addWorkingShift(WorkingShiftVo workingShiftVo) {
      UserLoginQuery user = localUser.getUser("用户信息");
      WorkingShift model = new WorkingShift();
      IdGenerator idGenerator = new IdGenerator();
      model.setId(idGenerator.getNumberId());
      model.setRemark(workingShiftVo.getRemark());
      model.setCreateUserId(user.getId());
      model.setUpdateUserId(user.getId());
      LambdaQueryWrapper<WorkingShift> queryWrapper = new LambdaQueryWrapper<WorkingShift>()
              .eq(WorkingShift::getShiftName,workingShiftVo.getShiftName());
      WorkingShift one = baseMapper.selectOne(queryWrapper);
      if (one != null) {
         throw new ApplicationException(CodeType.SUCC_ERROR,"已经添加过相同名称班次,请重新操作!");
      }
      model.setShiftName(workingShiftVo.getShiftName());
      int add = baseMapper.insert(model);
      if(add <= 0){
         throw new ApplicationException(CodeType.SERVICE_ERROR,"添加班次失败!");
      }
   }


   /**
    * 更新班次名称信息
    */
   @Override
   public void updateWorkingShift(WorkingShiftVo workingShiftVo) {
      UserLoginQuery user = localUser.getUser("用户信息");

      WorkingShift model = new WorkingShift();
      model.setUpdateUserId(user.getId());
      model.setId(workingShiftVo.getId());
      model.setRemark(workingShiftVo.getRemark());
      model.setShiftName(workingShiftVo.getShiftName());
      LambdaQueryWrapper<WorkingShift> queryWrapper = new LambdaQueryWrapper<WorkingShift>()
              .eq(WorkingShift::getShiftName,workingShiftVo.getShiftName());
      WorkingShift one = baseMapper.selectOne(queryWrapper);
      if (one != null && one.getId() != workingShiftVo.getId()) {
         throw new ApplicationException(CodeType.SUCC_ERROR,"系统已存在相同名称班次,请重新操作!");
      }
      int update = baseMapper.updateById(model);
      if(update <= 0){
         log.error("update car fail");
         throw new ApplicationException(CodeType.SERVICE_ERROR,"更新班次名称失败");
      }
   }

   /**
    * 根据Id删除
    * @param班次 id
    */
   @Override
   public void deleteWorkingShift(Long id) {
      int delete = baseMapper.deleteById(id);
      if(delete <= 0){
         log.error("delete car fail");
         throw new ApplicationException(CodeType.SERVICE_ERROR,"删除班次失败");
      }
   }

}
