package eqlee.ctm.api.pay.controller;

import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.api.pay.entity.Pay;
import eqlee.ctm.api.pay.entity.PayResult;
import eqlee.ctm.api.pay.entity.bo.MonthPhonePayBo;
import eqlee.ctm.api.pay.entity.query.MonthPayResultQuery;
import eqlee.ctm.api.pay.service.IPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2019/12/27
 * @vesion 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/v1/monthPay")
public class MonthPayController {


   @Autowired
   private IPayService payService;


   /**
    * 上传转账凭证
    */
   @PostMapping("/monthPhonePay")
   @CrossOrigin
   @CheckToken
   public PayResult monthPhonePay (@RequestBody MonthPhonePayBo bo) {

      if (bo.getMonthPrice() == null || StringUtils.isBlank(bo.getFilePath()) || StringUtils.isBlank(bo.getMonthNo())) {
         throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
      }

      IdGenerator idGenerator = new IdGenerator();
      Pay pay = new Pay();
      pay.setId(idGenerator.getNumberId());
      pay.setPayType(2);
      pay.setPayPhone(bo.getFilePath());
      pay.setPayDate(LocalDateTime.now());
      pay.setApplyNo(bo.getMonthNo());
      pay.setPayMoney(bo.getMonthPrice());

      payService.upMonthStatus(bo.getStartDate(),bo.getCompanyName());
      PayResult result = payService.insertPayInfo(pay);

      return result;

   }

   @GetMapping("/queryMonthPayResult")
   @CrossOrigin
   @CheckToken
   public MonthPayResultQuery queryMonthPayResult (String monthNo) {
      if (StringUtils.isBlank(monthNo)) {
         throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
      }
      return payService.queryMonthPayResult(monthNo);
   }
}
