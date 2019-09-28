package eqlee.ctm.finance.settlement.controller;

import com.yq.constanct.CodeType;
import com.yq.utils.StringUtils;
import eqlee.ctm.finance.exception.ApplicationException;
import eqlee.ctm.finance.jwt.islogin.CheckToken;
import eqlee.ctm.finance.settlement.entity.vo.FinanceVo;
import eqlee.ctm.finance.settlement.entity.vo.ResultVo;
import eqlee.ctm.finance.settlement.service.IInFinanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "申请报名", notes = "申请报名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "OutDate", value = "出行日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "CarNo", value = "车牌号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "TrueAllNumber", value = "实到人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "TreeAdultNumber", value = "实到成年人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "TreeBabyNumber", value = "实到幼儿人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "TreeOldNumber", value = "实到老人人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "TreeChildNumber", value = "实到小孩人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "UnpaidList", value = "未付款的联系人列表", required = true, dataType = "list", paramType = "path"),
            @ApiImplicitParam(name = "UnpaidNumber", value = "未付款人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "GaiMoney", value = "应收金额", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "TrueMoney", value = "实收金额", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "TicketName", value = "门票名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "TicketPrice", value = "门票价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "LunchPrice", value = "午餐费用", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "ParkingRatePrice", value = "停车费", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "RentCarPrice", value = "租车费用", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "GuideSubsidy", value = "导游补助", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "DriverSubsidy", value = "司机补助", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "AllOutPrice", value = "总支出费用", required = true, dataType = "double", paramType = "path")
    })
    @PostMapping("/insertFinance")
    @CrossOrigin
    @CheckToken
    public ResultVo insertFinance(@RequestBody FinanceVo vo) {
        if (StringUtils.isBlank(vo.getOutDate()) || StringUtils.isBlank(vo.getLineName()) || StringUtils.isBlank(vo.getCarNo())
        || vo.getTrueAllNumber() == null || vo.getTreeAdultNumber() == null || vo.getTreeBabyNumber() == null || vo.getTreeOldNumber() == null
        || vo.getTreeChildNumber() == null || vo.getUnpaidList().size() == 0 || vo.getUnpaidNumber() == null || vo.getGaiMoney() == null
        || vo.getTrueMoney() == null || StringUtils.isBlank(vo.getTicketName()) || vo.getTicketPrice() == null || vo.getLunchPrice() == null
        || vo.getParkingRatePrice() == null || vo.getRentCarPrice() == null || vo.getGuideSubsidy() == null || vo.getDriverSubsidy() == null
        || vo.getAllOutPrice() == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        inFinanceService.insertFinance(vo);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("OK");
        return resultVo;
    }

}
