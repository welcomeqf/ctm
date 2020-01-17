package eqlee.ctm.apply.carInfo.controller;

import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.carInfo.entity.query.CarInfoQuery;
import eqlee.ctm.apply.carInfo.entity.vo.CarInfoInsertVo;
import eqlee.ctm.apply.carInfo.service.ICarInfoService;
import eqlee.ctm.apply.line.entity.vo.LineVo;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
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
@RequestMapping("/v1/carInfo")
public class CarInfoController {

   @Autowired
   private ICarInfoService carInfoService;


   @ApiOperation(value = "查询该天没有出行的车辆", notes = "查询该天没有出行的车辆")
   @ApiImplicitParam(name = "orderId", value = "订单ID", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/queryCarInfo")
   @CrossOrigin
   @CheckToken
   public List<CarInfoQuery> queryCarInfo(@RequestParam("orderId") Long orderId) {
      if (orderId == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR);
      }
      return carInfoService.queryCarInfo(orderId);
   }
}
