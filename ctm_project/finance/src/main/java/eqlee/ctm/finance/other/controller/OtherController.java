package eqlee.ctm.finance.other.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import eqlee.ctm.finance.other.entity.Other;
import eqlee.ctm.finance.other.entity.vo.OtherPageVo;
import eqlee.ctm.finance.other.entity.vo.OtherVo;
import eqlee.ctm.finance.other.service.IOtherService;
import eqlee.ctm.finance.settlement.entity.vo.ResultVo;
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
 * @date 2019/12/4
 * @vesion 1.0
 **/
@Slf4j
@Api("紧急通知API")
@RestController
@RequestMapping("/v1/other")
public class OtherController {

   @Autowired
   private IOtherService otherService;


   @ApiOperation(value = "增加/修改紧急通知", notes = "增加/修改紧急通知")
   @PostMapping("/addOther")
   @CrossOrigin
   @CheckToken
   public ResultVo addOther (@RequestBody OtherVo other) {
      if(StringUtils.isBlank(other.getOtherContent())) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR);
      }

      otherService.addOther(other);
      ResultVo resultVo = new ResultVo();
      resultVo.setResult("ok");
      return resultVo;
   }



   @ApiOperation(value = "删除紧急通知", notes = "删除紧急通知")
   @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/deleteOther")
   @CrossOrigin
   @CheckToken
   public ResultVo deleteOther (@RequestParam("id") Long id) {
      if(id == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR);
      }

      otherService.deleteOther(id);
      ResultVo resultVo = new ResultVo();
      resultVo.setResult("ok");
      return resultVo;
   }


   @ApiOperation(value = "查询紧急通知", notes = "查询紧急通知")
   @GetMapping("/queryOther")
   @CrossOrigin
   @CheckToken
   public List<OtherVo> queryOther () {
      return otherService.queryOther();
   }


   @ApiOperation(value = "查询一条紧急通知", notes = "查询一条紧急通知")
   @GetMapping("/queryOtherByFirst")
   @CrossOrigin
   @CheckToken
   public OtherVo queryOtherByFirst () {
      return otherService.queryOtherByFirst();
   }


   @ApiOperation(value = "分页查询所有信息", notes = "分页查询所有信息")
   @GetMapping("/queryPageOther")
   @CrossOrigin
   @CheckToken
   public Page<Other> queryPageOther (@RequestParam("current") Integer current,@RequestParam("size") Integer size) {

      if (current == null || size == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR, "分页参数不能为空");
      }

      OtherPageVo vo = new OtherPageVo();
      vo.setCurrent(current);
      vo.setSize(size);
      return otherService.queryPageOther(vo);
   }
}
