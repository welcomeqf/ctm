package eqlee.ctm.finance.settlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import eqlee.ctm.finance.settlement.entity.bo.FinanceCompanyBo;
import eqlee.ctm.finance.settlement.entity.bo.FinanceCompanyInfoBo;
import eqlee.ctm.finance.settlement.entity.bo.ResultBo;
import eqlee.ctm.finance.settlement.entity.query.*;
import eqlee.ctm.finance.settlement.entity.vo.*;
import eqlee.ctm.finance.settlement.service.IInFinanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author qf
 * @Date 2019/9/28
 * @Version 1.0
 */
@Slf4j
@Api("财务Api")
@RestController
@RequestMapping("/v1/app/settlement")
public class FinanceController {

    @Autowired
    private IInFinanceService inFinanceService;

    @ApiOperation(value = "提交导游支出收入消费", notes = "提交导游支出收入消费")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "outDate", value = "出行日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "carNo", value = "车牌号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "allDoNumber", value = "应到人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "trueAllNumber", value = "实到人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "treeAdultNumber", value = "实到成年人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "treeBabyNumber", value = "实到幼儿人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "treeOldNumber", value = "实到老人人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "treeChildNumber", value = "实到小孩人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "gaiMoney", value = "面收金额", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "otherInPrice", value = "其他收入", required = true, dataType = "Double", paramType = "path"),
            @ApiImplicitParam(name = "allPrice", value = "该团总价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "monthPrice", value = "月结金额", required = true, dataType = "double", paramType = "path")
    })
    @PostMapping("/insertFinance")
    @CrossOrigin
    @CheckToken
    public ResultVo insertFinance(@RequestBody FinanceVo vo) {
        if (StringUtils.isBlank(vo.getOutDate()) || vo.getTrueAllNumber() == null ||
              vo.getTreeAdultNumber() == null || vo.getTreeBabyNumber() == null || vo.getTreeOldNumber() == null
        || vo.getTreeChildNumber() == null || vo.getGaiMoney() == null || vo.getAllPrice() == null ||
              vo.getMonthPrice() == null || vo.getCarType() == null) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"参数不能为空");
        }

        if (StringUtils.isBlank(vo.getCarNo())) {
            throw new ApplicationException(CodeType.PARAM_ERROR, "您还未选车辆");
        }

        inFinanceService.insertFinance(vo);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("OK");
        return resultVo;
    }

    @ApiOperation(value = "分页查询所有财务审核记录", notes = "分页查询所有财务审核记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "GuestName", value = "导游名字", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出行日期", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名字", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "类型(0-未审核,1-已审核)", required = false, dataType = "int", paramType = "path")
    })
    @GetMapping("/listExamine2Page")
    @CrossOrigin
    @CheckToken
    public Page<ExamineResultQuery> listExamine2Page (@RequestParam("current") Integer current,
                                                      @RequestParam("size") Integer size,
                                                      @RequestParam("GuestName") String guestName,
                                                      @RequestParam("type") Integer type,
                                                      @RequestParam("outDate") String outDate,
                                                      @RequestParam("lineName") String lineName) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        Page<ExamineResultQuery> page = new Page<>(current,size);

        return inFinanceService.listExamine2Page(page,guestName,type,outDate,lineName);
    }


    @ApiOperation(value = "展示该导游的审核详情", notes = "展示该导游的审核详情")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/listExamineDetailed")
    @CrossOrigin
    @CheckToken
    public Map<String,Object> listExamineDetailed (@RequestParam("Id") Long Id) {
        if (Id == null) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        return inFinanceService.queryExamineDetailed(Id);
    }

    @ApiOperation(value = "展示导游个人记录", notes = "展示导游个人记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出行时间", required = false, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "exaType", value = "审核类型(待审核,或已通过,或已拒绝)", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/guestPage2Me")
    @CrossOrigin
    @CheckToken
    public Page<GuestResultQuery> guestPage2Me (@RequestParam("current") Integer current,
                                                @RequestParam("size") Integer size,
                                                @RequestParam("exaType") String exaType,
                                                @RequestParam("outDate") String outDate,
                                                @RequestParam("lineName") String lineName) {

        Page<GuestResultQuery> page = new Page<>(current,size);
        return inFinanceService.GuestPage2Me(page,exaType,outDate,lineName);
    }

    @ApiOperation(value = "财务审核同意或拒绝操作", notes = "财务审核同意或拒绝操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "(1-代表通过，2-拒绝)", required = true, dataType = "int", paramType = "path")
    })
    @PostMapping("/examineGuestResult")
    @CrossOrigin
    @CheckToken
    public ExaResultQuery examineGuestResult (@RequestBody ExaVo vo) {

     if (vo.getId() == null || vo.getType() == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
     }

        ExaResultQuery query = inFinanceService.examineGuestResult(vo.getId() , vo.getType());
        return query;
    }


    @ApiOperation(value = "展示所有财务审核的结果", notes = "展示所有财务审核的结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "outDate", value = "出发时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "guestId", value = "导游id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/pageExamine")
    @CrossOrigin
    @CheckToken
    public Page<ExamineInfoQuery> pageExamine (@RequestParam("outDate") String outDate,
                                               @RequestParam("lineName") String lineName,
                                               @RequestParam("guestId") Long guestId,
                                               @RequestParam("current") Integer current,
                                               @RequestParam("size") Integer size) {

        if (StringUtils.isBlank(outDate) || StringUtils.isBlank(lineName) || guestId == null || current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }

        Page<ExamineInfoQuery> page = new Page<>(current,size);
        return inFinanceService.pageExamine(page,outDate,lineName,guestId);
    }



    @ApiOperation(value = "统计财务的月结统计", notes = "统计财务的月结统计")
    @ApiImplicitParams({
          @ApiImplicitParam(name = "outDate", value = "出发时间（默认传第一天）", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "companyName", value = "公司名,同行账号,同行代表人姓名,同行代表人电话", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/pageCompanyCount")
    @CrossOrigin
    @CheckToken
    public Page<FinanceCompanyBo> pageCompanyCount (@RequestParam("outDate") String outDate,
                                                    @RequestParam("companyName") String companyName,
                                                    @RequestParam("current") Integer current,
                                                    @RequestParam("size") Integer size) {

        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        Page<FinanceCompanyBo> page = new Page<>(current,size);
        return inFinanceService.pageCompanyCount(page,outDate,companyName);
    }


    @ApiOperation(value = "月结统计详情", notes = "月结统计详情")
    @ApiImplicitParams({
          @ApiImplicitParam(name = "outDate", value = "出发时间（默认传第一天）", required = true, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "accountName", value = "同行代表人账号", required = true, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "lineName", value = "线路名", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/queryCompanyInfoCount")
    @CrossOrigin
    @CheckToken
    public Map<String,Object> queryCompanyInfoCount (@RequestParam("outDate") String outDate,
                                                     @RequestParam("accountName") String accountName,
                                                     @RequestParam("current") Integer current,
                                                     @RequestParam("size") Integer size,
                                                     @RequestParam("lineName") String lineName) {
        if (StringUtils.isBlank(outDate) || StringUtils.isBlank(accountName) || current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        Page<FinanceCompanyInfoBo> page = new Page<>(current,size);
        return inFinanceService.queryCompanyInfoCount (page,lineName,outDate,accountName);
    }



    @ApiOperation(value = "查询提交信息", notes = "查询提交信息")
    @ApiImplicitParam(name = "outDate", value = "出发时间", required = true, dataType = "String", paramType = "path")
    @GetMapping("/queryResult")
    @CrossOrigin
    @CheckToken
    public ResultBo queryResult (@RequestParam("outDate") String outDate) {

        if (StringUtils.isBlank(outDate)) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        return inFinanceService.queryResult(outDate);
    }

}
