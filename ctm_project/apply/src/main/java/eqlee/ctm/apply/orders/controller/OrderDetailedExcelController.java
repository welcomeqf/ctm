package eqlee.ctm.apply.orders.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.entity.exl.ContentExl;
import com.yq.entity.exl.OutInfoExl;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.DateUtil;
import com.yq.utils.ExcelUtils;
import com.yq.utils.FilesUtils;
import com.yq.utils.StringUtils;
import com.yq.vilidata.TimeData;
import com.yq.vilidata.query.TimeQuery;
import eqlee.ctm.apply.entry.entity.query.ApplyResultCountQuery;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.entity.bo.OrderBo;
import eqlee.ctm.apply.orders.entity.query.OrderDetailedQuery;
import eqlee.ctm.apply.orders.entity.query.OrderFinanceQuery;
import eqlee.ctm.apply.orders.service.IOrdersDetailedService;
import eqlee.ctm.apply.orders.service.IOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
         @ApiImplicitParam(name = "payType", value = "支付类型(null-全部，0-现结,2-月结)", required = false, dataType = "int", paramType = "path")
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

      List<OrderFinanceQuery> outInfo = ordersDetailedService.queryInOutInfo(orders.getId());

      Integer adultNumber = 0;
      Integer oldNumber = 0;
      Integer childNumber = 0;
      Integer babyNumber = 0;
      Integer allNumber = 0;
      Double allMoney = 0.0;
      Double msMoney = 0.0;

      //创建报表体
      List<List<String>> body = new ArrayList<>();
      for (OrderDetailedQuery query : list) {

         adultNumber += query.getAdultNumber();
         oldNumber += query.getOldNumber();
         childNumber += query.getChildNumber();
         babyNumber += query.getBabyNumber();
         allNumber += query.getAllNumber();
         allMoney += query.getAllPrice();
         msMoney += query.getMsPrice();

         List<String> bodyValue = new ArrayList<>();
         bodyValue.add(query.getCompanyName());
         bodyValue.add(DateUtil.formatDate(orders.getOutDate()));
         bodyValue.add(query.getLineName());
         bodyValue.add(query.getContactName());
         bodyValue.add(query.getContactTel());
         bodyValue.add(String.valueOf(query.getAdultNumber()));
         bodyValue.add(String.valueOf(query.getChildNumber()));
         bodyValue.add(String.valueOf(query.getOldNumber()));
         bodyValue.add(String.valueOf(query.getBabyNumber()));
         bodyValue.add(String.valueOf(query.getAllNumber()));
         bodyValue.add(String.valueOf(query.getAllPrice()));
         bodyValue.add(query.getPlace());
         bodyValue.add(query.getApplyRemark());
//         bodyValue.add(query.getGuideName());
         //将数据添加到报表体中
         body.add(bodyValue);
      }
      ContentExl contentExl = new ContentExl();
      contentExl.setNumber("成人:" + adultNumber + " 老人:" + oldNumber + " 小孩:" + childNumber + " 幼儿:" +babyNumber);
      contentExl.setAllNumber(allNumber);
      contentExl.setIncomeMoney(allMoney);

      //总支出
      Double allOutMoney = 0.0;

      //其他收入
      Double otherInMoney = 0.0;

      List<OutInfoExl> outList = new ArrayList<>();
      if (outInfo.size() > 0) {
         String finance = null;
         for (OrderFinanceQuery financeQuery : outInfo) {
            OutInfoExl exl = new OutInfoExl();
            finance = financeQuery.getFinanceName();
            exl.setOutName(financeQuery.getOutName());
            exl.setOutPrice(financeQuery.getOutPrice());
            outList.add(exl);
            allOutMoney += financeQuery.getOutPrice();
            otherInMoney = financeQuery.getOtherInPrice();
         }
         contentExl.setFinanceName(finance);

      }

      //总收入
      Double allInMoney = otherInMoney + msMoney + allMoney;
      contentExl.setAllPrice(allInMoney);

      //利润
      Double setMoney = allInMoney - allOutMoney;

      contentExl.setGuideName(orders.getGuideName());
      contentExl.setMsPrice(msMoney);
      contentExl.setOtherPrice(otherInMoney);

      contentExl.setSetPrice("本单利润: " + String.format("%.2f", setMoney));

      contentExl.setList(outList);

      String fileName = "导游选人列表统计.xls";
      Workbook excel = ExcelUtils.exp2Excel(body,contentExl);
      String fileStorePath = "exl";
      String path = FilesUtils.getPath(fileName,fileStorePath);
      ExcelUtils.outFile2(excel,path,response);
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
      head.add("导游名");
      head.add("车辆");
      head.add("区域");
      head.add("成人人数");
      head.add("小孩人数");
      head.add("老人人数");
      head.add("幼儿人数");
      head.add("总人数");
      head.add("代收金额");
      head.add("订单状态");
      //创建报表体
      List<List<String>> body = new ArrayList<>();
      Map<Integer,Double> map = new HashMap<>();
      Double aut = 0.0;
      Double ch = 0.0;
      Double old = 0.0;
      Double baby = 0.0;
      Double allNumber = 0.0;
      Double ms = 0.0;
      for (OrderBo query : list) {
         List<String> bodyValue = new ArrayList<>();
         bodyValue.add(query.getOrderNo());
         bodyValue.add(query.getOutDate());
         bodyValue.add(query.getLineName());
         bodyValue.add(query.getGuideName());
         if (StringUtils.isBlank(query.getCarNumber())) {
            bodyValue.add("未选车辆");
         } else {
            bodyValue.add(query.getCarNumber());
         }
         bodyValue.add(query.getRegion());
         bodyValue.add(String.valueOf(query.getAdultNumber()));
         bodyValue.add(String.valueOf(query.getChildNumber()));
         bodyValue.add(String.valueOf(query.getOldNumber()));
         bodyValue.add(String.valueOf(query.getBabyNumber()));
         bodyValue.add(String.valueOf(query.getAllNumber()));
         bodyValue.add(String.valueOf(query.getMsPrice()));
         if (query.getStatus() == null) {
            bodyValue.add("未交账");
         } else if (query.getStatus() == 1) {
            bodyValue.add("已通过");
         } else if (query.getStatus() == 2) {
            bodyValue.add("已拒绝");
         } else if (query.getStatus() == 0){
            bodyValue.add("已交账");
         }
         aut += query.getAdultNumber();
         ch += query.getChildNumber();
         baby += query.getBabyNumber();
         old += query.getChildNumber();
         allNumber += query.getAllNumber();
         ms += query.getMsPrice();
         //将数据添加到报表体中
         body.add(bodyValue);
      }
      map.put(6,aut);
      map.put(7,ch);
      map.put(8,old);
      map.put(9,baby);
      map.put(10,allNumber);
      map.put(11,ms);
      String fileName = "导游出行记录统计.xls";
      HSSFWorkbook excel = ExcelUtils.expExcel(head, body,map);
      String fileStorePath = "exl";
      String path = FilesUtils.getPath(fileName,fileStorePath);
      ExcelUtils.outFile(excel,path,response);
   }



}
