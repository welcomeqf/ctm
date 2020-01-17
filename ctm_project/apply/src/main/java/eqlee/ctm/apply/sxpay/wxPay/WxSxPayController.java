package eqlee.ctm.apply.sxpay.wxPay;


import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.entry.vilidata.HttpUtils;
import eqlee.ctm.apply.month.service.IMonthService;
import eqlee.ctm.apply.sxpay.entity.PayInfo;
import eqlee.ctm.apply.sxpay.entity.SucVo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 月结支付的回调controller
 * @author qf
 * @date 2019/12/24
 * @vesion 1.0
 **/
@Slf4j
@Api
@RestController
@RequestMapping("/sx")
public class WxSxPayController {

   @Autowired
   private IApplyService applyService;

   @Autowired
   private HttpUtils apiService;

   @Autowired
   private IMonthService monthService;


   /**
    * 微信支付完成调用此接口
    * @param vo
    * @return
    */
   @PostMapping("/sucFail")
   @CrossOrigin
   public void sucFail (@RequestBody SucVo vo) {
      if ("SUCCESS".equals(vo.getMessage())) {
         //支付成功
         applyService.updateSxPriceStatus (vo.getSelfParameter());

         PayInfo info = new PayInfo();
         info.setMessage(vo.getMessage());
         info.setPayOrderSerialNumber(vo.getPayOrderSerialNumber());
         info.setThirdPayOrderId(vo.getThirdPayOrderId());
         info.setPayStatus(1);
         info.setPayType(0);
         apiService.insertPayInfo(info);

         //修改月结表的支付状态
         monthService.updateMonthStatus(vo.getPayOrderSerialNumber());
      }
      //支付失败
      PayInfo info = new PayInfo();
      info.setMessage(vo.getMessage());
      info.setPayOrderSerialNumber(vo.getPayOrderSerialNumber());
      info.setThirdPayOrderId(vo.getThirdPayOrderId());
      info.setPayStatus(2);
      info.setPayType(0);
      apiService.insertPayInfo(info);
   }

   /**
    * 支付宝支付完成调用此接口
    * @param vo
    * @return
    */
   @PostMapping("/sucAliFail")
   @CrossOrigin
   public void sucAliFail (@RequestBody SucVo vo) {
      if ("SUCCESS".equals(vo.getMessage())) {
         //支付成功
         applyService.updateSxPriceStatus (vo.getSelfParameter());

         PayInfo info = new PayInfo();
         info.setMessage(vo.getMessage());
         info.setPayOrderSerialNumber(vo.getPayOrderSerialNumber());
         info.setThirdPayOrderId(vo.getThirdPayOrderId());
         info.setPayStatus(1);
         info.setPayType(1);
         apiService.insertPayInfo(info);

         //修改月结表的支付状态
         monthService.updateMonthStatus(vo.getPayOrderSerialNumber());
      }
      //支付失败
      PayInfo info = new PayInfo();
      info.setMessage(vo.getMessage());
      info.setPayOrderSerialNumber(vo.getPayOrderSerialNumber());
      info.setThirdPayOrderId(vo.getThirdPayOrderId());
      info.setPayStatus(2);
      info.setPayType(1);
      apiService.insertPayInfo(info);

   }
}
