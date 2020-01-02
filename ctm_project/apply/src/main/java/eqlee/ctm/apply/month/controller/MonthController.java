package eqlee.ctm.apply.month.controller;

import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.month.entity.vo.MonthParamVo;
import eqlee.ctm.apply.month.entity.vo.MonthVo;
import eqlee.ctm.apply.month.service.IMonthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author qf
 * @date 2019/12/27
 * @vesion 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/v1/month")
public class MonthController {

   @Autowired
   private IMonthService monthService;

   @PostMapping("/insertMonth")
   @CrossOrigin
   @CheckToken
   public MonthVo insertMonth (@RequestBody MonthParamVo vo) {

      if (vo.getMonthPrice() == null || StringUtils.isBlank(vo.getStartDate())) {
         throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
      }

      return monthService.insertMonth(vo);
   }



   @GetMapping("/queryInfo")
   @CrossOrigin
   @CheckToken
   public MonthVo queryInfo (@RequestParam("id") Long id) {

      if (id == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
      }

      return monthService.queryInfo(id);
   }



}
