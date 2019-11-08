package eqlee.ctm.finance.settlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.ExcelUtils;
import eqlee.ctm.finance.settlement.entity.query.ExamineResultQuery;
import eqlee.ctm.finance.settlement.service.IInFinanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

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

    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "GuestName", value = "导游名字", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "类型(未审核,已审核)", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/exa")
    @CrossOrigin
    public void financeExcel(HttpServletResponse response,
                             @RequestParam("current") Integer current,
                             @RequestParam("size") Integer size,
                             @RequestParam("GuestName") String guestName,
                             @RequestParam("type") String type){

        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        Page<ExamineResultQuery> page = new Page<>(current,size);

        //查询出需要导出的数据
        Page<ExamineResultQuery> queryPage = inFinanceService.listExamine2Page(page, guestName, type);

        //拿到集合数据
        List<ExamineResultQuery> list = queryPage.getRecords();

        //创建报表数据头
        List<String> head = new ArrayList<>();
        head.add("出发日期");
        head.add("线路名");
        head.add("导游名字");
        head.add("导游电话");
        head.add("总支出费用");
        head.add("应得收入");
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
            bodyValue.add(String.valueOf(query.getInPrice()));
            bodyValue.add(String.valueOf(query.getFinallyPrice()));
            //将数据添加到报表体中
            body.add(bodyValue);
        }
        String fileName = "财务审核统计.xls";
        HSSFWorkbook excel = ExcelUtils.expExcel(head,body);
        ExcelUtils.outFile(excel,"./"+fileName,response);
    }
}
