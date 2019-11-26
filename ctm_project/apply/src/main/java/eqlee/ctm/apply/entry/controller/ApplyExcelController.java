package eqlee.ctm.apply.entry.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.ExcelUtils;
import eqlee.ctm.apply.entry.entity.query.ApplyCompanyQuery;
import eqlee.ctm.apply.entry.entity.query.ApplyMonthQuery;
import eqlee.ctm.apply.entry.entity.query.ApplyResultCountQuery;
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
import java.util.List;

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


   @ApiOperation(value = "同行报名记录统计", notes = "同行报名记录统计")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "outDate", value = "出发日期", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "lineName", value = "线路名称", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "payType", value = "(0--现结  1--月结 2--面收)", required = true, dataType = "int", paramType = "path")
   })
   @GetMapping("/count")
   @CheckToken
   @CrossOrigin
   public void applyExcel(HttpServletResponse response,
                            @RequestParam("current") Integer current,
                            @RequestParam("size") Integer size,
                            @RequestParam("lineName") String lineName,
                            @RequestParam("payType") Integer payType,
                            @RequestParam("outDate") String outDate){

      if (current == null || size == null || payType == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR);
      }

      Page<ApplyResultCountQuery> page = new Page<>(current,size);

      //查询出需要导出的数据
      Page<ApplyResultCountQuery> queryPage = applyService.pageResult2CountList(page,payType,outDate,lineName);

      //拿到集合数据
      List<ApplyResultCountQuery> list = queryPage.getRecords();

      //创建报表数据头
      List<String> head = new ArrayList<>();
      head.add("报名单号");
      head.add("出发日期");
      head.add("线路名");
      head.add("联系人");
      head.add("联系人电话");
      head.add("成人人数");
      head.add("老人人数");
      head.add("小孩人数");
      head.add("幼儿人数");
      head.add("总人数");
      head.add("结算价");
      //创建报表体
      List<List<String>> body = new ArrayList<>();
      for (ApplyResultCountQuery query : list) {
         List<String> bodyValue = new ArrayList<>();
         bodyValue.add(query.getApplyNo());
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
         //将数据添加到报表体中
         body.add(bodyValue);
      }
      String fileName = "同行报名记录统计.xls";
      HSSFWorkbook excel = ExcelUtils.expExcel(head,body);
      ExcelUtils.outFile(excel,"./"+fileName,response);
   }


   @ApiOperation(value = "分页查询财务月结的信息导出", notes = "分页查询财务月结的信息导出")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "outDate", value = "出行时间", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "type", value = "类型(0-查询全部 1-未付款 2-已付款)", required = false, dataType = "int", paramType = "path")
   })
   @GetMapping("/month")
   @CheckToken
   @CrossOrigin
   public void applyExcel(HttpServletResponse response,
                          @RequestParam("current") Integer current,
                          @RequestParam("size") Integer size,
                          @RequestParam("type") Integer type,
                          @RequestParam("outDate") String outDate){

      if (current == null || size == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR);
      }

      Page<ApplyMonthQuery> page = new Page<>(current,size);

      //查询出需要导出的数据
      Page<ApplyMonthQuery> queryPage = applyService.queryMonth2Apply(page,type,outDate);

      //拿到集合数据
      List<ApplyMonthQuery> list = queryPage.getRecords();

      //创建报表数据头
      List<String> head = new ArrayList<>();
      head.add("旅游线路");
      head.add("同行代表");
      head.add("旅客");
      head.add("出行日期");
      head.add("总人数");
      head.add("总费用");
      //创建报表体
      List<List<String>> body = new ArrayList<>();
      for (ApplyMonthQuery query : list) {
         List<String> bodyValue = new ArrayList<>();
         bodyValue.add(query.getLineName());
         bodyValue.add(query.getContactName());
         bodyValue.add(query.getOutDate());
         bodyValue.add(String.valueOf(query.getAllNumber()));
         bodyValue.add(String.valueOf(query.getAllPrice()));
         //将数据添加到报表体中
         body.add(bodyValue);
      }
      String fileName = "财务月结记录统计.xls";
      HSSFWorkbook excel = ExcelUtils.expExcel(head,body);
      ExcelUtils.outFile(excel,"./"+fileName,response);
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
                              @RequestParam("todayType") Integer todayType,
                              @RequestParam("companyUserId") Long companyUserId, HttpServletResponse response) {

      if (current == null || size == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
      }
      Page<ApplyCompanyQuery> page = new Page<>(current,size);
      UserLoginQuery user = localUser.getUser("用户信息");

      String admin = "运营人员";
      if (admin.equals(user.getRoleName())) {
         Page<ApplyCompanyQuery> applyList = applyService.pageAdmin2Apply(page, LineName, OutTime, applyTime, type, companyUserId,todayType);

         List<ApplyCompanyQuery> list = applyList.getRecords();

         //创建报表数据头
         List<String> head = new ArrayList<>();
         head.add("报名单号");
         head.add("报名日期");
         head.add("出行日期");
         head.add("线路名");
         head.add("客人名字");
         head.add("客人电话");
         head.add("门市价");
         head.add("结算价");
         head.add("总人数");
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
            bodyValue.add(String.valueOf(query.getMarketAllPrice()));
            bodyValue.add(String.valueOf(query.getAllPrice()));
            bodyValue.add(String.valueOf(query.getAllNumber()));
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
            //将数据添加到报表体中
            body.add(bodyValue);
         }
         String fileName = "我的报名记录统计.xls";
         HSSFWorkbook excel = ExcelUtils.expExcel(head,body);
         ExcelUtils.outFile(excel,"./"+fileName,response);
         return;
      }

      Page<ApplyCompanyQuery> pageList = applyService.pageMeApply(page, LineName, OutTime, applyTime, type,todayType);

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
      head.add("门市价");
      head.add("结算价");
      head.add("总人数");
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
         bodyValue.add(String.valueOf(query.getMarketAllPrice()));
         bodyValue.add(String.valueOf(query.getAllPrice()));
         bodyValue.add(String.valueOf(query.getAllNumber()));
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
         //将数据添加到报表体中
         body.add(bodyValue);
      }
      String fileName = "我的报名记录统计.xls";
      HSSFWorkbook excel = ExcelUtils.expExcel(head,body);
      ExcelUtils.outFile(excel,"./"+fileName,response);
   }
}
