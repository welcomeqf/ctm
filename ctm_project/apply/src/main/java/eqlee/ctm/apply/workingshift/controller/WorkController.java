package eqlee.ctm.apply.workingshift.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.carInfo.entity.query.CarInfoQuery;
import eqlee.ctm.apply.carInfo.entity.vo.CarInfoInsertVo;
import eqlee.ctm.apply.carInfo.service.ICarInfoService;
import eqlee.ctm.apply.line.entity.vo.LineVo;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import eqlee.ctm.apply.workingshift.entity.WorkingShift;
import eqlee.ctm.apply.workingshift.entity.vo.WorkingShiftVo;
import eqlee.ctm.apply.workingshift.service.IWorkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qf
 * @date 2020/1/15
 * @vesion 1.0
 **/
@Api
@Slf4j
@RestController
@RequestMapping("/v1/work")
public class WorkController {

   @Autowired
   private IWorkService workService;


   @ApiOperation(value = "获取班次分页列表",notes = "获取班次分页列表（分页）")
   @ApiImplicitParams({
           @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
           @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Integer", paramType = "path")
   })
   @GetMapping("/queryWorkingShiftPage")
   @CrossOrigin
   @CheckToken
   public Page<WorkingShift> queryAllWorkingShif(@RequestParam("current") Integer current,
                                         @RequestParam("size") Integer size) {
      if(current == null||size == null){
         throw new ApplicationException(CodeType.PARAMETER_ERROR,"当前页或者页面大小为空");
      }
      return workService.queryAllWorkingShif(current,size);
   }

   @ApiOperation(value = "添加班次",notes = "添加班次")
   @PostMapping("/addWorkingShift")
   @ApiImplicitParams({
           @ApiImplicitParam(name = "ShiftName", value = "班次名称", required = true, dataType = "String", paramType = "path"),
           @ApiImplicitParam(name = "Remark", value = "备注", required = true, dataType = "String", paramType = "path"),
   })
   @CrossOrigin
   @CheckToken
   public void addWorkingShift(@RequestBody WorkingShiftVo workingShiftVo) {
      if(StringUtils.isBlank(workingShiftVo.getShiftName())){
         throw new ApplicationException(CodeType.PARAMETER_ERROR,"班次名称不能为空");
      }
      workService.addWorkingShift(workingShiftVo);
   }

   @ApiOperation(value = "班次名称修改",notes = "班次名称修改")
   @PostMapping("/updateWorkingShift")
   @CrossOrigin
   @CheckToken
   public void updateWorkingShift(@RequestBody WorkingShiftVo workingShiftVo) {
      if(StringUtils.isBlank(workingShiftVo.getShiftName()) ||workingShiftVo.getId() == null){
         throw new ApplicationException(CodeType.PARAMETER_ERROR);
      }
      workService.updateWorkingShift(workingShiftVo);
   }

   @ApiOperation(value = "班次删除",notes = "班次删除")
   @GetMapping("/delete")
   @ApiImplicitParam(name = "Id",value = "班次Id",required = true,dataType = "Long",paramType = "path")
   @CrossOrigin
   @CheckToken
   public void deleteWorkingShift(@RequestParam("Id") Long Id) {
      if(Id == null){
         log.error("delete car param is null");
         throw new ApplicationException(CodeType.PARAMETER_ERROR,"删除班次Id为空");
      }
      workService.deleteWorkingShift(Id);
   }
}
