package eqlee.ctm.resource.region.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.resource.company.entity.vo.CompanyVo;
import eqlee.ctm.resource.company.entity.vo.ResultVo;
import eqlee.ctm.resource.region.entity.Region;
import eqlee.ctm.resource.region.entity.query.RegionQuery;
import eqlee.ctm.resource.region.entity.query.RegionUpdateQuery;
import eqlee.ctm.resource.region.service.IRegionService;
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
 * @date 2019/11/20
 * @vesion 1.0
 **/
@Slf4j
@Api("区域API")
@RestController
@RequestMapping("/v1/region")
public class RegionController {

   @Autowired
   private IRegionService regionService;

   @Autowired
   private LocalUser localUser;

   IdGenerator idGenerator = new IdGenerator();

   @ApiOperation(value = "添加区域", notes = "添加区域")
   @ApiImplicitParam(name = "regionName", value = "区域名", required = true, dataType = "String", paramType = "path")
   @PostMapping("/addRegion")
   @CrossOrigin
   @CheckToken
   public ResultVo addRegion (@RequestBody RegionQuery regionQuery) {
      if(StringUtils.isBlank(regionQuery.getRegionName())) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR);
      }
      UserLoginQuery user = localUser.getUser("用户信息");
      Region region = new Region();
      region.setId(idGenerator.getNumberId());
      region.setRegionName(regionQuery.getRegionName());
      region.setCreateUserId(user.getId());
      region.setCreateUser(user.getCname());
      regionService.addRegion(region);

      ResultVo resultVo = new ResultVo();
      resultVo.setResult("ok");
      return resultVo;
   }


   @ApiOperation(value = "删除区域", notes = "删除区域")
   @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/deleteRegion")
   @CrossOrigin
   @CheckToken
   public ResultVo deleteRegion (@RequestParam("id") Long id) {
      if(id == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR);
      }
     regionService.deleteRegion(id);

      ResultVo resultVo = new ResultVo();
      resultVo.setResult("ok");
      return resultVo;
   }


   @ApiOperation(value = "查询区域", notes = "查询区域")
   @GetMapping("/queryRegion")
   @CrossOrigin
   @CheckToken
   public List<Region> queryRegion () {
      return regionService.queryRegion();
   }


   @ApiOperation(value = "修改区域", notes = "修改区域")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "regionName", value = "区域名", required = true, dataType = "String", paramType = "path")
   })
   @PostMapping("/updateRegion")
   @CrossOrigin
   @CheckToken
   public ResultVo updateRegion (@RequestBody RegionUpdateQuery regionQuery) {
      if(StringUtils.isBlank(regionQuery.getRegionName()) || regionQuery.getId() == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR);
      }

      regionService.updateRegion(regionQuery);

      ResultVo resultVo = new ResultVo();
      resultVo.setResult("ok");
      return resultVo;
   }
}
