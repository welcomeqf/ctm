package eqlee.ctm.apply.orders.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.ExcelUtils;
import eqlee.ctm.apply.entry.entity.query.ApplyResultCountQuery;
import eqlee.ctm.apply.orders.entity.query.OrderDetailedQuery;
import eqlee.ctm.apply.orders.service.IOrdersDetailedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qf
 * @date 2019/11/18
 * @vesion 1.0
 **/
@Slf4j
@Api("导游poi_Api")
@RestController
@RequestMapping("/v1/poi/orderExcel")
public class OrderDetailedExcelController {


   @Autowired
   private IOrdersDetailedService ordersDetailedService;

   @ApiOperation(value = "查询导游人员表",notes = "查询导游人员表")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "outDate", value = "出行日期", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "payType", value = "支付类型(现结,月结,面收)", required = false, dataType = "String", paramType = "path")
   })
   @GetMapping("/queryCount")
   @CheckToken
   @CrossOrigin
   public void orderExcel(HttpServletResponse response,
                            @RequestParam("current") Integer current,
                            @RequestParam("size") Integer size,
                            @RequestParam("lineName") String lineName,
                            @RequestParam("payType") String payType,
                            @RequestParam("outDate") String outDate){

      if (current == null || size == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR);
      }

      Page<OrderDetailedQuery> page = new Page<>(current,size);

      //查询出需要导出的数据
      Page<OrderDetailedQuery> queryPage = ordersDetailedService.pageOrderDetailed2Type(page, payType, lineName, outDate);

      //拿到集合数据
      List<OrderDetailedQuery> list = queryPage.getRecords();

      //创建报表数据头
      List<String> head = new ArrayList<>();
      head.add("订单号");
      head.add("联系人");
      head.add("联系电话");
      head.add("成人人数");
      head.add("小孩人数");
      head.add("老人人数");
      head.add("幼儿人数");
      head.add("总人数");
      head.add("接送地");
      head.add("支付方式");
      //创建报表体
      List<List<String>> body = new ArrayList<>();
      for (OrderDetailedQuery query : list) {
         List<String> bodyValue = new ArrayList<>();
         bodyValue.add(query.getOrderNo());
         bodyValue.add(query.getContactName());
         bodyValue.add(query.getContactTel());
         bodyValue.add(String.valueOf(query.getAdultNumber()));
         bodyValue.add(String.valueOf(query.getChildNumber()));
         bodyValue.add(String.valueOf(query.getOldNumber()));
         bodyValue.add(String.valueOf(query.getBabyNumber()));
         bodyValue.add(String.valueOf(query.getAllNumber()));
         bodyValue.add(query.getPlace());
         if (query.getPayType() == 0) {
            bodyValue.add("现结");
         }else if (query.getPayType() == 1) {
            bodyValue.add("月结");
         }else {
            bodyValue.add("面收");
         }
         //将数据添加到报表体中
         body.add(bodyValue);
      }
      String fileName = "导游选人列表统计.xls";
      HSSFWorkbook excel = ExcelUtils.expExcel(head,body);
      ExcelUtils.outFile(excel,"./"+fileName,response);
   }
}
