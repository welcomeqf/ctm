package eqlee.ctm.apply.orders.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.DateUtil;
import com.yq.utils.ExcelUtils;
import com.yq.utils.FilesUtils;
import com.yq.vilidata.TimeData;
import com.yq.vilidata.query.TimeQuery;
import eqlee.ctm.apply.entry.entity.query.ApplyResultCountQuery;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.entity.bo.OrderBo;
import eqlee.ctm.apply.orders.entity.query.OrderDetailedQuery;
import eqlee.ctm.apply.orders.service.IOrdersDetailedService;
import eqlee.ctm.apply.orders.service.IOrdersService;
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

   @Autowired
   private IOrdersService ordersService;

   @ApiOperation(value = "导游人员表导出", notes = "导游人员表导出")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "payType", value = "支付类型(null-全部，0-现结,1-月结,2-面收)", required = false, dataType = "int", paramType = "path")
   })
   @GetMapping("/queryCount")
   @CheckToken
   @CrossOrigin
   public void orderExcel(HttpServletResponse response,
                          @RequestParam("payType") Integer payType,
                          @RequestParam("id") Long id) {

      if (id == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR);
      }

      //查询出需要导出的数据
      List<OrderDetailedQuery> list = ordersDetailedService.pageOrderDetailed2Type(id, payType);

      Orders orders = ordersService.queryOne(id);

      //创建报表数据头
      List<String> head = new ArrayList<>();
      head.add("出行日期");
      head.add("线路名");
      head.add("车牌号");
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
         bodyValue.add(DateUtil.formatDate(orders.getOutDate()));
         bodyValue.add(orders.getLineName());
         bodyValue.add(orders.getCarNumber());
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
         } else if (query.getPayType() == 1) {
            bodyValue.add("月结");
         } else {
            bodyValue.add("面收");
         }
         //将数据添加到报表体中
         body.add(bodyValue);
      }
      String fileName = "导游选人列表统计.xls";
      HSSFWorkbook excel = ExcelUtils.expExcel(head, body);
      String fileStorePath = "exl";
      String path = FilesUtils.getPath(fileName,fileStorePath);
      ExcelUtils.outFile(excel,path,response);
   }


   @ApiOperation(value = "管理分配首页导出", notes = "管理分配首页导出")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "lineName", value = "线路名", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "startDate", value = "开始时间", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "region", value = "区域模糊查询", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "endDate", value = "结束时间", required = false, dataType = "String", paramType = "path")
   })
   @GetMapping("/pageOrderExl")
   @CrossOrigin
   @CheckToken
   public void pageOrder(HttpServletResponse response,
                                  @RequestParam("endDate") String endDate,
                                  @RequestParam("lineName") String lineName,
                                  @RequestParam("startDate") String startDate,
                                  @RequestParam("region") String region,
                                  @RequestParam("current") Integer current,
                                  @RequestParam("size") Integer size) {

      if (current == null || size == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR);
      }
      Page<OrderBo> page = new Page<>(current, size);

      TimeQuery dateParam = TimeData.getDateParam(startDate, endDate);
      Page<OrderBo> orderBoPage = ordersDetailedService.pageOrder(page, dateParam.getStartTime(), dateParam.getEndTime(), lineName, region);

      List<OrderBo> list = orderBoPage.getRecords();

      //创建报表数据头
      List<String> head = new ArrayList<>();
      head.add("订单号");
      head.add("出行时间");
      head.add("线路名");
      head.add("区域");
      head.add("面收总额");
      head.add("成人人数");
      head.add("小孩人数");
      head.add("老人人数");
      head.add("幼儿人数");
      head.add("总人数");
      //创建报表体
      List<List<String>> body = new ArrayList<>();
      for (OrderBo query : list) {
         List<String> bodyValue = new ArrayList<>();
         bodyValue.add(query.getOrderNo());
         bodyValue.add(query.getOutDate());
         bodyValue.add(query.getLineName());
         bodyValue.add(query.getRegion());
         bodyValue.add(String.valueOf(query.getMsPrice()));
         bodyValue.add(String.valueOf(query.getAdultNumber()));
         bodyValue.add(String.valueOf(query.getChildNumber()));
         bodyValue.add(String.valueOf(query.getOldNumber()));
         bodyValue.add(String.valueOf(query.getBabyNumber()));
         bodyValue.add(String.valueOf(query.getAllNumber()));
         //将数据添加到报表体中
         body.add(bodyValue);
      }
      String fileName = "导游出行记录统计.xls";
      HSSFWorkbook excel = ExcelUtils.expExcel(head, body);
      String fileStorePath = "exl";
      String path = FilesUtils.getPath(fileName,fileStorePath);
      ExcelUtils.outFile(excel,path,response);
   }



}
