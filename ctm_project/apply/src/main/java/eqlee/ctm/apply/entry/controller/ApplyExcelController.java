package eqlee.ctm.apply.entry.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.ExcelUtils;
import com.yq.utils.FilesUtils;
import com.yq.utils.StringUtils;
import com.yq.vilidata.TimeData;
import com.yq.vilidata.query.TimeQuery;
import eqlee.ctm.apply.entry.entity.bo.ApplyCompanyInfo;
import eqlee.ctm.apply.entry.entity.bo.ApplyCountCaiBo;
import eqlee.ctm.apply.entry.entity.query.ApplyCompanyQuery;
import eqlee.ctm.apply.entry.entity.query.ApplyDoExaQuery;
import eqlee.ctm.apply.entry.entity.query.ApplyMonthQuery;
import eqlee.ctm.apply.entry.entity.query.ApplyResultCountQuery;
import eqlee.ctm.apply.entry.entity.vo.ApplyCountVo;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
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
@Api("报名poi导出")
@RestController
@RequestMapping("/v1/poi/applyExcel")
public class ApplyExcelController {


   @Autowired
   private IApplyService applyService;

   @Autowired
   private LocalUser localUser;


   @ApiOperation(value = "同行财务统计详情", notes = "同行财务统计详情")
   @GetMapping("/count")
   @CheckToken
   @CrossOrigin
   public void applyExcel(HttpServletResponse response,
                          @RequestParam("current") Integer current,
                          @RequestParam("size") Integer size,
                          @RequestParam("outDate") String outDate,
                          @RequestParam("companyName") String companyName,
                          @RequestParam("lineName") String lineName){

      if (current == null || size == null || StringUtils.isBlank(outDate) || StringUtils.isBlank(companyName)) {
         throw new ApplicationException(CodeType.PARAM_ERROR);
      }

      Page<ApplyCountVo> page = new Page<>(current,size);
      Page<ApplyCountVo> voPage = applyService.queryCountInfo(page, outDate, companyName, lineName);
      //拿到集合数据
      List<ApplyCountVo> list = voPage.getRecords();

      //创建报表数据头
      List<String> head = new ArrayList<>();
      head.add("报名单号");
      head.add("出行日期");
      head.add("旅游线路");
      head.add("联系人");
      head.add("联系电话");
      head.add("成人人数");
      head.add("小孩人数");
      head.add("老人人数");
      head.add("幼儿人数");
      head.add("总人数");
      head.add("月结总额");
      head.add("代收金额");
      head.add("实付金额");
      head.add("经手人");

      //创建报表体
      List<List<String>> body = new ArrayList<>();
      Map<Integer,Double> map = new HashMap<>();
      Double aut = 0.0;
      Double ch = 0.0;
      Double old = 0.0;
      Double baby = 0.0;
      Double allNumber = 0.0;
      Double month = 0.0;
      Double ms = 0.0;
      Double set = 0.0;
      for (ApplyCountVo query : list) {
         List<String> bodyValue = new ArrayList<>();
         bodyValue.add(query.getApplyNo());
         bodyValue.add(query.getOutDate());
         bodyValue.add(query.getLineName());
         bodyValue.add(query.getContactName());
         bodyValue.add(query.getContactTel());
         bodyValue.add(String.valueOf(query.getAdultNumber()));
         bodyValue.add(String.valueOf(query.getChildNumber()));
         bodyValue.add(String.valueOf(query.getOldNumber()));
         bodyValue.add(String.valueOf(query.getBabyNumber()));
         bodyValue.add(String.valueOf(query.getAllNumber()));
         bodyValue.add(String.valueOf(query.getAllPrice()));
         bodyValue.add(String.valueOf(query.getMsPrice()));
         bodyValue.add(String.valueOf(query.getAllPrice() - query.getMsPrice()));
         bodyValue.add(query.getCname());

         aut += query.getAdultNumber();
         ch += query.getChildNumber();
         baby += query.getBabyNumber();
         old += query.getChildNumber();
         allNumber += query.getAllNumber();
         month += query.getAllPrice();
         ms += query.getMsPrice();
         set += query.getAllPrice() - query.getMsPrice();

         //将数据添加到报表体中
         body.add(bodyValue);
      }
      map.put(5,aut);
      map.put(6,ch);
      map.put(7,old);
      map.put(8,baby);
      map.put(9,allNumber);
      map.put(10,month);
      map.put(11,ms);
      map.put(12,set);
      String fileName = "同行财务统计详情.xls";
      HSSFWorkbook excel = ExcelUtils.expExcel(head,body,map);
      String fileStorePath = "exl";
      String path = FilesUtils.getPath(fileName,fileStorePath);
      ExcelUtils.outFile(excel,path,response);
   }


   @ApiOperation(value = "我的报名记录导出", notes = "我的报名记录导出")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "OutTime", value = "出行时间", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "applyTime", value = "报名时间", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "LineName", value = "线路名模糊查询", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "type", value = "(0-已取消)", required = false, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "todayType", value = "(1-今天已报名 2-今天出行)", required = false, dataType = "int", paramType = "path")
   })
   @GetMapping("/page2MeExl")
   @CheckToken
   @CrossOrigin
   public void page2MeExl(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                              @RequestParam("OutTime") String OutTime, @RequestParam("LineName") String LineName,
                              @RequestParam("applyTime") String applyTime, @RequestParam("type") Integer type,
                              @RequestParam("todayType") Integer todayType,@RequestParam("roadName") String roadName,
                              @RequestParam("companyUserId") Long companyUserId, HttpServletResponse response) {

      if (current == null || size == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
      }
      Page<ApplyCompanyQuery> page = new Page<>(current,size);
      UserLoginQuery user = localUser.getUser("用户信息");

      String admin = "运营人员";
      String admin1 = "超级管理员";
      String admin2 = "管理员";
      Map<Integer,Double> map = new HashMap<>();
      Double aut = 0.0;
      Double ch = 0.0;
      Double old = 0.0;
      Double baby = 0.0;
      Double allNumber = 0.0;
      Double set = 0.0;
      Double men = 0.0;
      if (admin.equals(user.getRoleName()) || admin1.equals(user.getRoleName()) || admin2.equals(user.getRoleName())) {
         Page<ApplyCompanyQuery> applyList = applyService.pageAdmin2Apply(page, LineName, OutTime, applyTime, type, companyUserId,todayType,roadName);

         List<ApplyCompanyQuery> list = applyList.getRecords();

         //创建报表数据头
         List<String> head = new ArrayList<>();
         head.add("报名单号");
         head.add("出行日期");
         head.add("线路名");
         head.add("同行");
         head.add("客人名字");
         head.add("客人电话");
         head.add("成人人数");
         head.add("老人人数");
         head.add("小孩人数");
         head.add("幼儿人数");
         head.add("总人数");
         head.add("结算价");
         head.add("门市价");
         head.add("接送地");
         head.add("经手人");
         head.add("审核状态");
         //创建报表体
         List<List<String>> body = new ArrayList<>();
         for (ApplyCompanyQuery query : list) {
            List<String> bodyValue = new ArrayList<>();
            bodyValue.add(query.getApplyNo());
            bodyValue.add(query.getOutDate());
            bodyValue.add(query.getLineName());
            bodyValue.add(query.getCompanyName());
            bodyValue.add(query.getContactName());
            bodyValue.add(query.getContactTel());
            bodyValue.add(String.valueOf(query.getAdultNumber()));
            bodyValue.add(String.valueOf(query.getOldNumber()));
            bodyValue.add(String.valueOf(query.getChildNumber()));
            bodyValue.add(String.valueOf(query.getBabyNumber()));
            bodyValue.add(String.valueOf(query.getAllNumber()));
            bodyValue.add(String.valueOf(query.getAllPrice()));
            bodyValue.add(String.valueOf(query.getMarketAllPrice()));
            bodyValue.add(query.getPlace());
            bodyValue.add(query.getCName());
            if (query.getIsCancel()) {
               bodyValue.add("已取消");
            } else {
               //没有取消 判断是否付款
               if (!query.getIsPayment()) {
                  bodyValue.add("未付款");
               } else {
                  //装状态
                  if (query.getStatu() == 0) {
                     bodyValue.add("待审核");
                  } else if (query.getStatu() == 1) {
                     bodyValue.add("报名通过");
                  } else {
                     bodyValue.add("不通过");
                  }
               }

            }

            aut += query.getAdultNumber();
            ch += query.getChildNumber();
            baby += query.getBabyNumber();
            old += query.getChildNumber();
            allNumber += query.getAllNumber();
            set += query.getAllPrice();
            men += query.getMarketAllPrice();

            //将数据添加到报表体中
            body.add(bodyValue);
         }
         map.put(6,aut);
         map.put(7,ch);
         map.put(8,old);
         map.put(9,baby);
         map.put(10,allNumber);
         map.put(11,set);
         map.put(12,men);
         String fileName = "我的报名记录统计.xls";
         HSSFWorkbook excel = ExcelUtils.expExcel(head,body,map);
         String fileStorePath = "exl";
         String path = FilesUtils.getPath(fileName,fileStorePath);
         ExcelUtils.outFile(excel,path,response);
         return;
      }

      Page<ApplyCompanyQuery> pageList = applyService.pageMeApply(page, LineName, OutTime, applyTime, type,todayType,roadName);

      //同行的数据
      List<ApplyCompanyQuery> list = pageList.getRecords();


      //创建报表数据头
      List<String> head = new ArrayList<>();
      head.add("报名单号");
      head.add("报名日期");
      head.add("出行日期");
      head.add("线路名");
      head.add("客人名字");
      head.add("客人电话");
      head.add("成人人数");
      head.add("老人人数");
      head.add("小孩人数");
      head.add("幼儿人数");
      head.add("总人数");
      head.add("结算价");
      head.add("门市价");
      head.add("接送地");
      head.add("经手人");
      head.add("审核状态");
      //创建报表体
      List<List<String>> body = new ArrayList<>();
      for (ApplyCompanyQuery query : list) {
         List<String> bodyValue = new ArrayList<>();
         bodyValue.add(query.getApplyNo());
         bodyValue.add(query.getCreateDate());
         bodyValue.add(query.getOutDate());
         bodyValue.add(query.getLineName());
         bodyValue.add(query.getContactName());
         bodyValue.add(query.getContactTel());
         bodyValue.add(String.valueOf(query.getAdultNumber()));
         bodyValue.add(String.valueOf(query.getOldNumber()));
         bodyValue.add(String.valueOf(query.getChildNumber()));
         bodyValue.add(String.valueOf(query.getBabyNumber()));
         bodyValue.add(String.valueOf(query.getAllNumber()));
         bodyValue.add(String.valueOf(query.getAllPrice()));
         bodyValue.add(String.valueOf(query.getMarketAllPrice()));
         bodyValue.add(query.getPlace());
         bodyValue.add(query.getCName());
         if (query.getIsCancel()) {
            bodyValue.add("已取消");
         } else {
            //没有取消 判断是否付款
            if (!query.getIsPayment()) {
               bodyValue.add("未付款");
            } else {
               //装状态
               if (query.getStatu() == 0) {
                  bodyValue.add("待审核");
               } else if (query.getStatu() == 1) {
                  bodyValue.add("报名通过");
               } else {
                  bodyValue.add("不通过");
               }
            }

         }

         aut += query.getAdultNumber();
         ch += query.getChildNumber();
         baby += query.getBabyNumber();
         old += query.getChildNumber();
         allNumber += query.getAllNumber();
         set += query.getAllPrice();
         men += query.getMarketAllPrice();
         //将数据添加到报表体中
         body.add(bodyValue);
      }
      map.put(6,aut);
      map.put(7,ch);
      map.put(8,old);
      map.put(9,baby);
      map.put(10,allNumber);
      map.put(11,set);
      map.put(12,men);
      String fileName = "我的报名记录统计.xls";
      HSSFWorkbook excel = ExcelUtils.expExcel(head,body,map);
      String fileStorePath = "exl";
      String path = FilesUtils.getPath(fileName,fileStorePath);
      ExcelUtils.outFile(excel,path,response);
   }


   @ApiOperation(value = "财务月结统计导出", notes = "财务月结统计导出")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "time", value = "查询年份", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "companyId", value = "同行id", required = false, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "caiType", value = "(0-未确认 1-已确认)", required = false, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "type", value = "(0-未支付 1-已支付)", required = false, dataType = "int", paramType = "path")
   })
   @GetMapping("/queryCaiMonthCountPoi")
   @CrossOrigin
   @CheckToken
   public void queryCaiMonthCount(@RequestParam("current") Integer current,
                                                         @RequestParam("size") Integer size,
                                                         @RequestParam("time") String time,
                                                         @RequestParam("type") Integer type,
                                                         @RequestParam("caiType") Integer caiType,
                                                         @RequestParam("companyId") Long companyId,
                                                         HttpServletResponse response) {


      if (current == null || size == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR);
      }

      Page<ApplyResultCountQuery> page = new Page<>(current, size);


      Page<ApplyResultCountQuery> queryPage = applyService.pageResultAdminCountList(page, time, type, caiType, companyId);

      List<ApplyResultCountQuery> list = queryPage.getRecords();

      //创建报表数据头
      List<String> head = new ArrayList<>();
      head.add("出行年月");
      head.add("公司名称");
      head.add("成人人数");
      head.add("小孩人数");
      head.add("老人人数");
      head.add("幼儿人数");
      head.add("总人数");
      head.add("月结总额");
      head.add("代收金额");
      head.add("应收金额");
      head.add("支付状态");
      head.add("确认状态");

      List<List<String>> body = new ArrayList<>();
      Map<Integer,Double> map = new HashMap<>();
      Double aut = 0.0;
      Double ch = 0.0;
      Double old = 0.0;
      Double baby = 0.0;
      Double allNumber = 0.0;
      Double month = 0.0;
      Double ms = 0.0;
      Double set = 0.0;
      for (ApplyResultCountQuery query : list) {
         List<String> bodyValue = new ArrayList<>();
         bodyValue.add(query.getYear() + "-" + query.getMonth());
         bodyValue.add(query.getCompanyName());
         bodyValue.add(String.valueOf(query.getAdultNumber()));
         bodyValue.add(String.valueOf(query.getChildNumber()));
         bodyValue.add(String.valueOf(query.getOldNumber()));
         bodyValue.add(String.valueOf(query.getBabyNumber()));
         bodyValue.add(String.valueOf(query.getAllNumber()));
         bodyValue.add(String.valueOf(query.getAllPrice()));
         bodyValue.add(String.valueOf(query.getMsprice()));
         bodyValue.add(String.valueOf(query.getAllPrice() - query.getMsprice()));

         if (query.getSxType() == 0) {
            bodyValue.add("未支付");
         }else {
            bodyValue.add("已支付");
         }

         if (query.getCaiType() == 0) {
            bodyValue.add("待确认");
         } else {
            bodyValue.add("已确认");
         }

         aut += query.getAdultNumber();
         ch += query.getChildNumber();
         baby += query.getBabyNumber();
         old += query.getOldNumber();
         allNumber += query.getAllNumber();
         month += query.getAllPrice();
         ms += query.getMsprice();
         set += query.getAllPrice() - query.getMsprice();

         //将数据添加到报表体中
         body.add(bodyValue);
      }

      map.put(2,aut);
      map.put(3,ch);
      map.put(4,old);
      map.put(5,baby);
      map.put(6,allNumber);
      map.put(7,month);
      map.put(8,ms);
      map.put(9,set);

      String fileName = "财务月结统计.xls";
      HSSFWorkbook excel = ExcelUtils.expExcel(head,body,map);
      String fileStorePath = "exl";
      String path = FilesUtils.getPath(fileName,fileStorePath);
      ExcelUtils.outFile(excel,path,response);
   }

   @ApiOperation(value = "财务月结统计详情导出", notes = "财务月结统计详情导出")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "outDate", value = "出行年月", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "companyName", value = "公司", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "lineName", value = "线路名", required = false, dataType = "String", paramType = "path")
   })
   @GetMapping("/queryCountInfo2Poi")
   @CrossOrigin
   @CheckToken
   public void queryCountInfo2(@RequestParam("current") Integer current,
                               @RequestParam("size") Integer size,
                               @RequestParam("outDate") String outDate,
                               @RequestParam("companyName") String companyName,
                               @RequestParam("lineName") String lineName,
                               HttpServletResponse response) {

      if (StringUtils.isBlank(outDate) || StringUtils.isBlank(companyName)) {
         throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
      }

      Page<ApplyCountVo> page = new Page<>(current,size);
      Page<ApplyCountCaiBo> boPage = applyService.queryCountInfo2(page, outDate, companyName, lineName);

      List<ApplyCountCaiBo> list = boPage.getRecords();

      //创建报表数据头
      List<String> head = new ArrayList<>();
      head.add("出行日期");
      head.add("线路名称");
      head.add("同行代表");
      head.add("同行电话");
      head.add("联系人");
      head.add("联系电话");
      head.add("成人人数");
      head.add("小孩人数");
      head.add("老人人数");
      head.add("幼儿人数");
      head.add("总人数");
      head.add("月结总额");
      head.add("代收金额");
      head.add("应收金额");

      List<List<String>> body = new ArrayList<>();

      Map<Integer,Double> map = new HashMap<>();
      Double aut = 0.0;
      Double ch = 0.0;
      Double old = 0.0;
      Double baby = 0.0;
      Double allNumber = 0.0;
      Double month = 0.0;
      Double ms = 0.0;
      Double set = 0.0;
      for (ApplyCountCaiBo query : list) {
         List<String> bodyValue = new ArrayList<>();
         bodyValue.add(query.getOutDate());
         bodyValue.add(query.getLineName());
         bodyValue.add(query.getCname());
         bodyValue.add(String.valueOf(query.getCompanyDbTel()));
         bodyValue.add(query.getContactName());
         bodyValue.add(String.valueOf(query.getContactTel()));
         bodyValue.add(String.valueOf(query.getAdultNumber()));
         bodyValue.add(String.valueOf(query.getChildNumber()));
         bodyValue.add(String.valueOf(query.getOldNumber()));
         bodyValue.add(String.valueOf(query.getBabyNumber()));
         bodyValue.add(String.valueOf(query.getAllNumber()));
         bodyValue.add(String.valueOf(query.getAllPrice()));
         bodyValue.add(String.valueOf(query.getMsPrice()));
         bodyValue.add(String.valueOf(query.getAllPrice() - query.getMsPrice()));

         aut += query.getAdultNumber();
         ch += query.getChildNumber();
         baby += query.getBabyNumber();
         old += query.getOldNumber();
         allNumber += query.getAllNumber();

         month += query.getAllPrice();
         ms += query.getMsPrice();
         set += query.getAllPrice() - query.getMsPrice();

         //将数据添加到报表体中
         body.add(bodyValue);
      }

      map.put(6,aut);
      map.put(7,ch);
      map.put(8,old);
      map.put(9,baby);
      map.put(10,allNumber);
      map.put(11,month);
      map.put(12,ms);
      map.put(13,set);

      String fileName = "月结详情.xls";
      HSSFWorkbook excel = ExcelUtils.expExcel(head,body,map);
      String fileStorePath = "exl";
      String path = FilesUtils.getPath(fileName,fileStorePath);
      ExcelUtils.outFile(excel,path,response);
   }


   @ApiOperation(value = "查询统计的详情导出", notes = "查询统计的详情导出")
   @GetMapping("/queryCountInfoPoi")
   @CrossOrigin
   @CheckToken
   public void queryCountInfo(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                              @RequestParam("OutDate") String OutDate, @RequestParam("LineName") Long LineName,
                              @RequestParam("type") Integer type,@RequestParam("applyDate") String applyDate,
                              @RequestParam("exaStatus") Integer exaStatus, HttpServletResponse response) {

      if (current == null || size == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
      }
      Page<ApplyDoExaQuery> page = new Page<>(current,size);

      Page<ApplyDoExaQuery> queryPage = applyService.listPageDo2Apply(page, OutDate, LineName, type, applyDate, exaStatus);

      List<ApplyDoExaQuery> list = queryPage.getRecords();

      //创建报表数据头
      List<String> head = new ArrayList<>();
      head.add("订单号");
      head.add("同行");
      head.add("出行日期");
      head.add("旅游线路");
      head.add("联系人");
      head.add("联系电话");
      head.add("成人人数");
      head.add("小孩人数");
      head.add("老人人数");
      head.add("幼儿人数");
      head.add("总人数");
      head.add("结算金额");
      head.add("报名状态");
      head.add("订单状态");
      List<List<String>> body = new ArrayList<>();

      Map<Integer,Double> map = new HashMap<>();
      Double aut = 0.0;
      Double ch = 0.0;
      Double old = 0.0;
      Double baby = 0.0;
      Double allNumber = 0.0;
      Double set = 0.0;
      for (ApplyDoExaQuery query : list) {
         List<String> bodyValue = new ArrayList<>();
         bodyValue.add(query.getApplyNo());
         bodyValue.add(query.getCompanyName());
         bodyValue.add(query.getOutDate());
         bodyValue.add(query.getLineName());
         bodyValue.add(query.getContactName());
         bodyValue.add(String.valueOf(query.getContactTel()));
         bodyValue.add(String.valueOf(query.getAdultNumber()));
         bodyValue.add(String.valueOf(query.getChildNumber()));
         bodyValue.add(String.valueOf(query.getOldNumber()));
         bodyValue.add(String.valueOf(query.getBabyNumber()));
         bodyValue.add(String.valueOf(query.getAdultNumber() + query.getChildNumber() + query.getOldNumber() + query.getBabyNumber()));
         bodyValue.add(String.valueOf(query.getAllPrice()));
         if ("0".equals(query.getExamineType())) {
            bodyValue.add("报名申请");
         } else {
            bodyValue.add("取消申请");
         }

         if (query.getExamineResult() == 0) {
            bodyValue.add("申请中");
         } else if (query.getExamineResult() == 1) {
            bodyValue.add("已通过");
         } else {
            bodyValue.add("已拒绝");
         }

         aut += query.getAdultNumber();
         ch += query.getChildNumber();
         baby += query.getBabyNumber();
         old += query.getOldNumber();
         allNumber += query.getAdultNumber() + query.getChildNumber() + query.getOldNumber() + query.getBabyNumber();
         set += query.getAllPrice();

         //将数据添加到报表体中
         body.add(bodyValue);
      }

      map.put(6,aut);
      map.put(7,ch);
      map.put(8,old);
      map.put(9,baby);
      map.put(10,allNumber);
      map.put(11,set);

      String fileName = "报名审核.xls";
      HSSFWorkbook excel = ExcelUtils.expExcel(head,body,map);
      String fileStorePath = "exl";
      String path = FilesUtils.getPath(fileName,fileStorePath);
      ExcelUtils.outFile(excel,path,response);
   }

}
