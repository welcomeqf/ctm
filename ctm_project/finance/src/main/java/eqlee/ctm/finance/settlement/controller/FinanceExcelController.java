package eqlee.ctm.finance.settlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.ExcelUtils;
import com.yq.utils.FilesUtils;
import com.yq.utils.StringUtils;
import eqlee.ctm.finance.settlement.entity.bo.FinanceCompanyBo;
import eqlee.ctm.finance.settlement.entity.bo.FinanceCompanyInfoBo;
import eqlee.ctm.finance.settlement.entity.query.ExamineResultQuery;
import eqlee.ctm.finance.settlement.entity.query.GuestResultQuery;
import eqlee.ctm.finance.settlement.service.IInFinanceService;
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
import java.util.Map;

/**
 * @Author qf
 * @Date 2019/11/8
 * @Version 1.0
 */
@Slf4j
@Api("财务excel控制层")
@RestController
@RequestMapping("/v1/poi/finance")
public class FinanceExcelController {


    @Autowired
    private IInFinanceService inFinanceService;


    @ApiOperation(value = "展示导游个人记录导出", notes = "展示导游个人记录导出")
    @ApiImplicitParams({
          @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "outDate", value = "出行时间", required = false, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "lineName", value = "线路名", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "exaType", value = "审核类型(待审核,或已通过,或已拒绝)", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/guestPage2MeExl")
    @CrossOrigin
    @CheckToken
    public void guestPage2Me (HttpServletResponse response,
                              @RequestParam("current") Integer current,
                              @RequestParam("size") Integer size,
                              @RequestParam("exaType") String exaType,
                              @RequestParam("outDate") String outDate,
                              @RequestParam("lineName") String lineName) {

        Page<GuestResultQuery> page = new Page<>(current,size);
        Page<GuestResultQuery> queryPage = inFinanceService.GuestPage2Me(page, exaType, outDate, lineName);


        //拿到集合数据
        List<GuestResultQuery> list = queryPage.getRecords();

        //创建报表数据头
        List<String> head = new ArrayList<>();
        head.add("出发日期");
        head.add("旅游线路");
        head.add("成人人数");
        head.add("小孩人数");
        head.add("老人人数");
        head.add("幼儿人数");
        head.add("收入金额");
        head.add("支出金额");
        head.add("结算金额");
        head.add("总人数");
        head.add("订单状态");
        //创建报表体
        List<List<String>> body = new ArrayList<>();
        for (GuestResultQuery query : list) {
            List<String> bodyValue = new ArrayList<>();
            bodyValue.add(query.getOutDate());
            bodyValue.add(query.getLineName());
            bodyValue.add(String.valueOf(query.getTreeAdultNumber()));
            bodyValue.add(String.valueOf(query.getTreeChildNumber()));
            bodyValue.add(String.valueOf(query.getTreeOldNumber()));
            bodyValue.add(String.valueOf(query.getTreeBabyNumber()));
            bodyValue.add(String.valueOf(query.getSubstitutionPrice()));
            bodyValue.add(String.valueOf(query.getAllOutPrice()));
            bodyValue.add(String.valueOf(query.getResultPrice()));
            bodyValue.add(String.valueOf(query.getTrueAllNumber()));
            if (query.getStatus() == 0) {
                bodyValue.add("待审核");
            } else if (query.getStatus() == 1) {
                bodyValue.add("已通过");
            } else {
                bodyValue.add("已拒绝");
            }
            //将数据添加到报表体中
            body.add(bodyValue);
        }
        String fileName = "导游统计记录.xls";
        HSSFWorkbook excel = ExcelUtils.expExcel(head,body);
        String fileStorePath = "exl";
        String path = FilesUtils.getPath(fileName,fileStorePath);
        ExcelUtils.outFile(excel,path,response);
    }



    @ApiOperation(value = "分页查询所有财务审核记录导出", notes = "分页查询所有财务审核记录导出")
    @ApiImplicitParams({
          @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "GuestName", value = "导游名字", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "outDate", value = "出行日期", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "lineName", value = "线路名字", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "type", value = "类型(0-未审核,1-已审核)", required = false, dataType = "int", paramType = "path")
    })
    @GetMapping("/exa")
    @CheckToken
    @CrossOrigin
    public void financeExcel(HttpServletResponse response,
                             @RequestParam("current") Integer current,
                             @RequestParam("size") Integer size,
                             @RequestParam("GuestName") String guestName,
                             @RequestParam("type") Integer type,
                             @RequestParam("outDate") String outDate,
                             @RequestParam("lineName") String lineName){

        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        Page<ExamineResultQuery> page = new Page<>(current,size);

        //查询出需要导出的数据
        Page<ExamineResultQuery> queryPage = inFinanceService.listExamine2Page(page,guestName,type,outDate,lineName);

        //拿到集合数据
        List<ExamineResultQuery> list = queryPage.getRecords();

        //创建报表数据头
        List<String> head = new ArrayList<>();
        head.add("出发日期");
        head.add("线路名");
        head.add("导游名字");
        head.add("导游电话");
        head.add("总支出费用");
        head.add("总收入");
        head.add("实际收入");
        //创建报表体
        List<List<String>> body = new ArrayList<>();
        for (ExamineResultQuery query : list) {
            List<String> bodyValue = new ArrayList<>();
            bodyValue.add(query.getOutDate());
            bodyValue.add(query.getLineName());
            bodyValue.add(query.getGuideName());
            bodyValue.add(query.getTel());
            bodyValue.add(String.valueOf(query.getAllOutPrice()));
            bodyValue.add(String.valueOf(query.getAllInPrice()));
            bodyValue.add(String.valueOf(query.getFinallyPrice()));
            //将数据添加到报表体中
            body.add(bodyValue);
        }
        String fileName = "财务审核统计.xls";
        HSSFWorkbook excel = ExcelUtils.expExcel(head,body);
        String fileStorePath = "exl";
        String path = FilesUtils.getPath(fileName,fileStorePath);
        ExcelUtils.outFile(excel,path,response);
    }



    @ApiOperation(value = "统计财务的月结统计导出", notes = "统计财务的月结统计导出")
    @ApiImplicitParams({
          @ApiImplicitParam(name = "outDate", value = "出发时间（默认传第一天）", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "companyName", value = "公司名,同行账号,同行代表人姓名,同行代表人电话", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/pageCompanyCountExl")
    @CrossOrigin
    @CheckToken
    public void pageCompanyCount (HttpServletResponse response,
                                  @RequestParam("outDate") String outDate,
                                  @RequestParam("companyName") String companyName,
                                  @RequestParam("current") Integer current,
                                  @RequestParam("size") Integer size) {

        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        Page<FinanceCompanyBo> page = new Page<>(current,size);
        Page<FinanceCompanyBo> boPage = inFinanceService.pageCompanyCount(page, outDate, companyName);

        //拿到集合数据
        List<FinanceCompanyBo> list = boPage.getRecords();

        //创建报表数据头
        List<String> head = new ArrayList<>();
        head.add("出发月份");
        head.add("公司编码");
        head.add("公司名");
        head.add("同行代表");
        head.add("同行代表电话");
        head.add("成人人数");
        head.add("老人人数");
        head.add("小孩人数");
        head.add("幼儿人数");
        head.add("总人数");
        head.add("月结金额");
        head.add("面收金额");
        head.add("应收金额");
        //创建报表体
        List<List<String>> body = new ArrayList<>();
        for (FinanceCompanyBo query : list) {
            List<String> bodyValue = new ArrayList<>();
            bodyValue.add(query.getOutDate());
            bodyValue.add(query.getCompanyNo());
            bodyValue.add(query.getCompanyName());
            bodyValue.add(query.getCompanyUserName());
            bodyValue.add(query.getCompanyUserTel());
            bodyValue.add(String.valueOf(query.getAdultNumber()));
            bodyValue.add(String.valueOf(query.getOldNumber()));
            bodyValue.add(String.valueOf(query.getChildNumber()));
            bodyValue.add(String.valueOf(query.getBabyNumber()));
            bodyValue.add(String.valueOf(query.getAllNumber()));
            bodyValue.add(String.valueOf(query.getMonthPrice()));
            bodyValue.add(String.valueOf(query.getMsPrice()));
            bodyValue.add(String.valueOf(query.getGaiPrice()));
            //将数据添加到报表体中
            body.add(bodyValue);
        }
        String fileName = "财务月结统计.xls";
        HSSFWorkbook excel = ExcelUtils.expExcel(head,body);
        String fileStorePath = "exl";
        String path = FilesUtils.getPath(fileName,fileStorePath);
        ExcelUtils.outFile(excel,path,response);
    }




    @ApiOperation(value = "月结统计详情导出", notes = "月结统计详情导出")
    @ApiImplicitParams({
          @ApiImplicitParam(name = "outDate", value = "出发时间（默认传第一天）", required = true, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "accountName", value = "同行代表人账号", required = true, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "lineName", value = "线路名", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/queryCompanyInfoCountExl")
    @CrossOrigin
    @CheckToken
    public void queryCompanyInfoCount (HttpServletResponse response,
                                       @RequestParam("outDate") String outDate,
                                       @RequestParam("accountName") String accountName,
                                       @RequestParam("current") Integer current,
                                       @RequestParam("size") Integer size,
                                       @RequestParam("lineName") String lineName) {
        if (StringUtils.isBlank(outDate) || StringUtils.isBlank(accountName) || current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        Page<FinanceCompanyInfoBo> page = new Page<>(current,size);
        Map<String, Object> map = inFinanceService.queryCompanyInfoCount(page, lineName, outDate, accountName);

        Page<FinanceCompanyInfoBo> boPage = (Page<FinanceCompanyInfoBo>) map.get("pageData");

        //拿到集合数据
        List<FinanceCompanyInfoBo> list = boPage.getRecords();

        //创建报表数据头
        List<String> head = new ArrayList<>();
        head.add("公司编号");
        head.add("公司名称");
        head.add("同行代表");
        head.add("出行日期");
        head.add("线路名");
        head.add("成人人数");
        head.add("老人人数");
        head.add("小孩人数");
        head.add("幼儿人数");
        head.add("总人数");
        head.add("月结金额");
        head.add("面收金额");
        head.add("总金额");
        //创建报表体
        List<List<String>> body = new ArrayList<>();
        for (FinanceCompanyInfoBo query : list) {
            List<String> bodyValue = new ArrayList<>();
            bodyValue.add(query.getCompanyNo());
            bodyValue.add(query.getCompanyName());
            bodyValue.add(query.getCompanyUserName());
            bodyValue.add(query.getOutDate());
            bodyValue.add(query.getLineName());
            bodyValue.add(String.valueOf(query.getAdultNumber()));
            bodyValue.add(String.valueOf(query.getOldNumber()));
            bodyValue.add(String.valueOf(query.getChildNumber()));
            bodyValue.add(String.valueOf(query.getBabyNumber()));
            bodyValue.add(String.valueOf(query.getAllNumber()));
            bodyValue.add(String.valueOf(query.getMonthPrice()));
            bodyValue.add(String.valueOf(query.getMsPrice()));
            bodyValue.add(String.valueOf(query.getPrice()));
            //将数据添加到报表体中
            body.add(bodyValue);
        }
        String fileName = "财务月结统计.xls";
        HSSFWorkbook excel = ExcelUtils.expExcel(head,body);
        String fileStorePath = "exl";
        String path = FilesUtils.getPath(fileName,fileStorePath);
        ExcelUtils.outFile(excel,path,response);
    }



}
