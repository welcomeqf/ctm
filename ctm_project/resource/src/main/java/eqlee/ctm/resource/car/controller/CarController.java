package eqlee.ctm.resource.car.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.utils.StringUtils;
import eqlee.ctm.resource.car.entity.Car;
import eqlee.ctm.resource.car.entity.Vo.CarUpdateVo;
import eqlee.ctm.resource.car.entity.Vo.CarVo;
import eqlee.ctm.resource.car.service.ICarService;
import eqlee.ctm.resource.company.entity.vo.ResultVo;
import eqlee.ctm.resource.exception.ApplicationException;
import eqlee.ctm.resource.jwt.contain.LocalUser;
import eqlee.ctm.resource.jwt.islogin.CheckToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * @Author Claire
 * @Date 2019/9/18 0018
 * @Version 1.0
 */
@Api("车辆管理Api")
@Slf4j
@RestController
@RequestMapping("/v1/app/car")
public class CarController {

    @Autowired
    private ICarService carService;

    @ApiOperation(value = "车辆列表(只展示本公司车辆）",notes = "车辆列表展示（分页）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping("/queryCarPage")
    @CrossOrigin
    @CheckToken
    public Page<Car> queryAllCar(@RequestParam("current") Integer current,
                                 @RequestParam("size") Integer size) {
        if(current == null||size == null){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"当前页或者页面大小为空");
        }
        return carService.queryAllCar(current,size);
    }



    @ApiOperation(value = "车辆删除",notes = "车辆删除")
    @DeleteMapping("/delete/{Id}")
    @ApiImplicitParam(name = "Id",value = "车辆Id",required = true,dataType = "Long",paramType = "path")
    @CrossOrigin
    @CheckToken
    public void deleteCar(@PathVariable("Id") Long Id) {
        if(Id == null){
            log.error("delete car param is null");
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"删除车辆的Id为空");
        }
        carService.deleteCar(Id);
    }



    @ApiOperation(value = "车辆增加",notes = "车辆增加")
    @PostMapping("/addCar")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carName", value = "车辆名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "carNo", value = "车牌号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "statu", value = "状态", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "remark", value = "备注", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "isStop", value = "是否停用", required = true, dataType = "Boolean", paramType = "path")
    })
    @CrossOrigin
    @CheckToken
    public void addCar(@RequestBody CarVo carVo) {
        if(StringUtils.isBlank(carVo.getCarNo()) ||StringUtils.isBlank(carVo.getCarName())){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"增加车辆信息为空");
        }
        carService.addCar(carVo);
    }



    @ApiOperation(value = "查询车辆修改页首页",notes = "查询车辆修改页首页")
    @GetMapping("/CarDetail")
    @ApiImplicitParam(name = "Id", value = "车辆Id", required = true, dataType = "Long", paramType = "path")
    @CrossOrigin
    @CheckToken
    public Car CarDetail(@RequestParam("Id") Long Id) {
        if(Id == null){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"修改车辆首页的Id不能为空");
        }
        return carService.updateCarIndex(Id);
    }





    @ApiOperation(value = "车辆修改",notes = "车辆修改")
    @PostMapping("/updateCar")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Id", value = "车辆Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "carName", value = "车辆名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "carNo", value = "车辆号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "statu", value = "状态", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "remark", value = "备注", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "isStop", value = "是否启用", required = true, dataType = "boolean", paramType = "path")
    })
    @CrossOrigin
    @CheckToken
    public void updateCar(@RequestBody CarUpdateVo carUpdateVo) {
        if(StringUtils.isBlank(carUpdateVo.getCarNo()) ||StringUtils.isBlank(carUpdateVo.getCarName())||carUpdateVo.getId() == null){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        carService.updateCar(carUpdateVo,carUpdateVo.getId());
    }


}
