package eqlee.ctm.finance.settlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import eqlee.ctm.finance.settlement.entity.query.*;
import eqlee.ctm.finance.settlement.entity.vo.ExaVo;
import eqlee.ctm.finance.settlement.entity.vo.FinanceVo;
import eqlee.ctm.finance.settlement.entity.vo.ResultVo;
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
            @ApiImplicitParam(name = "unpaidList", value = "未付款的联系人列表（以下是数组的具体字段）", required = true, dataType = "list", paramType = "path"),
            @ApiImplicitParam(name = "contectUserName", value = "联系人名字（这是数组的第一个字段）", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "contectUserTel", value = "联系电话", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "allNumber", value = "一个联系人代表的未付款代收总人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "allPrice", value = "一个联系人代表的未付款代收总价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "babyNumber", value = "一个联系人代表的幼儿人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "oldNumber", value = "一个联系人代表的老人人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "childNumber", value = "一个联系人代表的小孩人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "adultNumber", value = "一个联系人代表的成年人数（这是数组的最后一个字段）", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "unpaidNumber", value = "未付款人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "gaiMoney", value = "应收金额", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "trueMoney", value = "实收金额", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "ticketName", value = "门票名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "ticketPrice", value = "门票费用", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "lunchPrice", value = "午餐费用", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "parkingRatePrice", value = "停车费", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "rentCarPrice", value = "租车费用", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "guideSubsidy", value = "导游补助", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "driverSubsidy", value = "司机补助", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "allOutPrice", value = "总支出费用", required = true, dataType = "double", paramType = "path")
    })
    @PostMapping("/insertFinance")
    @CrossOrigin
    @CheckToken
    public ResultVo insertFinance(@RequestBody FinanceVo vo) {
        if (StringUtils.isBlank(vo.getOutDate()) || StringUtils.isBlank(vo.getLineName()) || StringUtils.isBlank(vo.getCarNo())
        || vo.getTrueAllNumber() == null || vo.getTreeAdultNumber() == null || vo.getTreeBabyNumber() == null || vo.getTreeOldNumber() == null
        || vo.getTreeChildNumber() == null || vo.getUnpaidNumber() == null || vo.getGaiMoney() == null
        || vo.getTrueMoney() == null || StringUtils.isBlank(vo.getTicketName()) || vo.getTicketPrice() == null || vo.getLunchPrice() == null
        || vo.getParkingRatePrice() == null || vo.getRentCarPrice() == null || vo.getGuideSubsidy() == null || vo.getDriverSubsidy() == null
        || vo.getAllOutPrice() == null) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
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
            @ApiImplicitParam(name = "type", value = "类型(未审核,已审核)", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/listExamine2Page")
    @CrossOrigin
    @CheckToken
    public Page<ExamineResultQuery> listExamine2Page (@RequestParam("current") Integer current,
                                                      @RequestParam("size") Integer size,
                                                      @RequestParam("GuestName") String guestName,
                                                      @RequestParam("type") String type) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        Page<ExamineResultQuery> page = new Page<>(current,size);

        return inFinanceService.listExamine2Page(page,guestName,type);
    }


    @ApiOperation(value = "展示该导游的审核详情", notes = "展示该导游的审核详情")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/listExamineDetailed")
    @CrossOrigin
    @CheckToken
    public ExamineDetailedQuery listExamineDetailed (@RequestParam("Id") Long Id) {
        if (Id == null) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        return inFinanceService.queryExamineDetailed(Id);
    }

    @ApiOperation(value = "展示导游个人记录", notes = "展示导游个人记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "exaType", value = "审核类型(待审核,或已通过,或已拒绝)", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/guestPage2Me")
    @CrossOrigin
    @CheckToken
    public Page<GuestResultQuery> guestPage2Me (@RequestParam("current") Integer current,
                                                @RequestParam("size") Integer size,
                                                @RequestParam("exaType") String exaType) {

        Page<GuestResultQuery> page = new Page<>(current,size);
        return inFinanceService.GuestPage2Me(page,exaType);
    }

    @ApiOperation(value = "财务审核同意或拒绝操作", notes = "财务审核同意或拒绝操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "outDate", value = "出发时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "guestId", value = "导游id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "(1-代表通过，2-拒绝)", required = true, dataType = "int", paramType = "path")
    })
    @PostMapping("/examineGuestResult")
    @CrossOrigin
    @CheckToken
    public ExaResultQuery examineGuestResult (@RequestBody ExaVo vo) {

     if (StringUtils.isBlank(vo.getOutDate()) || StringUtils.isBlank(vo.getLineName())
             || vo.getGuestId() == null || vo.getType() == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
     }

        ExaResultQuery query = inFinanceService.examineGuestResult(vo.getOutDate(), vo.getLineName(), vo.getGuestId(), vo.getType());
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


}
