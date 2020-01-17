package eqlee.ctm.report.statisticline.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.ExcelUtils;
import com.yq.utils.FilesUtils;
import com.yq.utils.StringUtils;
import com.yq.vilidata.TimeData;
import com.yq.vilidata.query.TimeQuery;
import eqlee.ctm.report.statisticline.entity.vo.PersonCountVo;
import eqlee.ctm.report.statisticline.entity.vo.PriceCountVo;
import eqlee.ctm.report.statisticline.service.IStatisticLineService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qf
 * @date 2019/11/19
 * @vesion 1.0
 **/
@Slf4j
@Api("报表导出api")
@RestController
@RequestMapping("/v1/poi")
public class StatisticEcxController {

   @Autowired
   private IStatisticLineService statisticLineService;


   @ApiOperation(value = "金额统计报表导出", notes = "金额统计报表导出")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String", paramType = "path")
   })
   @GetMapping("/priceExcel")
   @CrossOrigin
   @CheckToken
   public void priceExcel(HttpServletResponse response,
                            @RequestParam("startTime") String startTime,
                            @RequestParam("endTime") String endTime){

      TimeQuery query = TimeData.getParam(startTime, endTime);
      List<PriceCountVo> list = statisticLineService.selectPriceByTime(query.getStartTime(), query.getEndTime());

      //创建报表数据头
      List<String> head = new ArrayList<>();
      head.add("开始时间");
      head.add("结束时间");
      head.add("城市");
      head.add("总金额");
      //创建报表体
      List<List<String>> body = new ArrayList<>();
      Map<Integer,Double> map = new HashMap<>();
      Double all = 0.0;
      for (PriceCountVo vo : list) {
         List<String> bodyValue = new ArrayList<>();
         bodyValue.add(query.getStartTime());
         bodyValue.add(query.getEndTime());
         bodyValue.add(vo.getCity());
         bodyValue.add(String.valueOf(vo.getAllPrice()));
         //将数据添加到报表体中

         all += vo.getAllPrice();

         body.add(bodyValue);
      }
      map.put(3,all);
      String fileName = "金额统计报表导出.xls";
      HSSFWorkbook excel = ExcelUtils.expExcel(head,body,map);
      String fileStorePath = "exl";
      String path = FilesUtils.getPath(fileName,fileStorePath);
      ExcelUtils.outFile(excel,path,response);
   }


   @ApiOperation(value = "报名人数报表导出", notes = "报名人数报表导出")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String", paramType = "path")
   })
   @GetMapping("/personExcel")
   @CheckToken
   @CrossOrigin
   public void personExcel(HttpServletResponse response,
                          @RequestParam("startTime") String startTime,
                          @RequestParam("endTime") String endTime){

      TimeQuery query = TimeData.getParam(startTime, endTime);
      List<PersonCountVo> list = statisticLineService.selectCountByTime(query.getStartTime(), query.getEndTime());

      //创建报表数据头
      List<String> head = new ArrayList<>();
      head.add("开始时间");
      head.add("结束时间");
      head.add("城市");
      head.add("总人数");
      //创建报表体
      List<List<String>> body = new ArrayList<>();
      Map<Integer,Double> map = new HashMap<>();
      Double all = 0.0;
      for (PersonCountVo vo : list) {
         List<String> bodyValue = new ArrayList<>();
         bodyValue.add(query.getStartTime());
         bodyValue.add(query.getEndTime());
         bodyValue.add(vo.getCity());
         bodyValue.add(String.valueOf(vo.getAllPersonCount()));
         //将数据添加到报表体中
         all += vo.getAllPersonCount();
         body.add(bodyValue);
      }
      map.put(3,all);
      String fileName = "报名人数报表.xls";
      HSSFWorkbook excel = ExcelUtils.expExcel(head,body,map);
      String fileStorePath = "exl";
      String path = FilesUtils.getPath(fileName,fileStorePath);
      ExcelUtils.outFile(excel,path,response);
   }
}
