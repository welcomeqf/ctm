package eqlee.ctm.resource.system.controller;

import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.IdGenerator;
import eqlee.ctm.resource.system.entity.SystemConfig;
import eqlee.ctm.resource.system.entity.vo.*;
import eqlee.ctm.resource.system.service.ISystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qf
 * @date 2019/12/25
 * @vesion 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/v1/config")
public class ConfigController {

   @Autowired
   private ISystemConfigService systemConfigService;

   @PostMapping("/insertConfig")
   @CrossOrigin
   public void insertConfig () {

      SystemConfig config = new SystemConfig();
      IdGenerator idGenerator = new IdGenerator();
      config.setId(idGenerator.getNumberId());
      config.setDescription("公司合同");
      config.setNos("companyFile4");
      config.setValuess("");
      config.setIsPublic(true);
      config.setRemark("");
      config.setSort(13);
      config.setTypes(1);

      systemConfigService.insertConfig(config);

   }



   @GetMapping("/querySystemConfig")
   @CrossOrigin
   @CheckToken
   public List<SystemCompanyVo> querySystemConfig () {
      return systemConfigService.querySystemConfig();

   }


   @PostMapping("/allUpdateConfig")
   @CrossOrigin
   @CheckToken
   public ResultWithVo allUpdateConfig (@RequestBody SystemVo vo) {
      if (vo.getList().size() <= 0) {
         throw new ApplicationException(CodeType.PARAM_ERROR);
      }

      systemConfigService.allUpdateConfig(vo.getList());

      ResultWithVo result = new ResultWithVo();

      result.setResult("ok");

      return result;
   }


   /**
    * 同行查看的数据
    * @return
    */
   @GetMapping("/queryCompanyInfo")
   @CrossOrigin
   @CheckToken
   public List<SystemResultCompanyVo> queryCompanyInfo () {
      return systemConfigService.queryCompanyInfo();

   }



   /**
    * 公司文件
    * @return
    */
   @GetMapping("/queryFile")
   @CrossOrigin
   public SystemFileVo queryFile (@RequestParam("sort") Integer sort) {
      return systemConfigService.queryFile(sort);

   }
}
