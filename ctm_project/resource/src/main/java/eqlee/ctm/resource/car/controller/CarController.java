package eqlee.ctm.resource.car.controller;

import com.yq.constanct.CodeType;
import com.yq.utils.IdGenerator;
import eqlee.ctm.resource.car.entity.Car;
import eqlee.ctm.resource.car.service.ICarService;
import eqlee.ctm.resource.exception.ApplicationException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.JdkIdGenerator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/18 0018
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/v1/app/resource/car")
public class CarController {

    @Autowired
    ICarService carService;

    @ApiOperation(value = "车辆列表",notes = "车辆列表展示")
    @GetMapping("/QueryCar")
    @CrossOrigin
    public List<Car> queryAllCar()
    {
        return carService.queryAllCar();
    }

    @ApiOperation(value = "车辆删除",notes = "车辆删除")
    @GetMapping("/DeleteCar")
    @ApiImplicitParam(name = "Id",value = "车辆Id",required = true,dataType = "Long",paramType = "path")
    @CrossOrigin
    public void Delete(Long id)
    {
        if(id==null){
            log.error("delete car param is null");
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"删除公司Id为空");
        }
        carService.deleteCar(id);
    }

    @ApiOperation(value = "车辆增加",notes = "车辆增加")
    @GetMapping("/AddCar")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Id", value = "车辆Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "CarName", value = "车辆名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "CarNo", value = "车辆号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Statu", value = "状态", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Remark", value = "备注", required = true, dataType = "Integer", paramType = "path")
    })
    @CrossOrigin
    public void addCar(Car car)
    {
        if(car==null){
            log.error("add car param is null");
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"增加公司信息为空");
        }
        carService.addCar(car);
    }
    @ApiOperation(value = "车辆修改",notes = "车辆修改")
    @GetMapping("/UpdateCar")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Id", value = "车辆Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "CarName", value = "车辆名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "CarNo", value = "车辆号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Statu", value = "状态", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Remark", value = "备注", required = true, dataType = "Integer", paramType = "path")
    })
    @CrossOrigin
    public void deleteCar(Car car)
    {
        if(car==null){
            log.error("update car param is null");
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"修改公司信息为空");
        }
        carService.updateCar(car);
    }
}
