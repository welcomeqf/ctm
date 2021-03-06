package eqlee.ctm.apply.orders.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.SendUtils;
import com.yq.vilidata.TimeData;
import com.yq.vilidata.query.TimeQuery;
import eqlee.ctm.apply.entry.entity.query.ApplyUpdateInfo;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import eqlee.ctm.apply.orders.entity.Vo.IncomeVo;
import eqlee.ctm.apply.orders.entity.bo.OrderBo;
import eqlee.ctm.apply.orders.entity.query.*;
import eqlee.ctm.apply.orders.service.IOrdersDetailedService;
import eqlee.ctm.apply.orders.service.IOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author qf
 * @Date 2019/10/17
 * @Version 1.0
 */
@Slf4j
@Api("订单详情API")
@RestController
@RequestMapping("/v1/app/orderDetailed")
public class OrderDetailedController {


    @Autowired
    private IOrdersDetailedService ordersDetailedService;

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private SendUtils sendService;



    @ApiOperation(value = "查询导游人员表",notes = "查询导游人员表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "payType", value = "支付类型(null-全部，0-现结,1-月结,2-面收)", required = false, dataType = "int", paramType = "path")
    })
    @GetMapping("/pageOrderDetailed2Type")
    @CrossOrigin
    @CheckToken
    public Map<String,Object> pageOrderDetailed2Type (@RequestParam("payType") Integer payType,
                                                      @RequestParam("id") Long id) {

        if (id == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        OrderDetailedFainllyQuery query = ordersService.getCarNumber(id);
        List<OrderDetailedQuery> list = ordersDetailedService.pageOrderDetailed2Type(id, payType);
        //收入接口整合进来
        IncomeVo income = ordersService.incomeCount(id);
        //支出接口整合进来
        List<OrderFinanceQuery> outInfo = ordersDetailedService.queryInOutInfo(id);
        OrderFinanceOutComeQuery outcome = null;
        List<Outcome2Vm> outList = new ArrayList<>();
        //财务名
        String caiName = null;
        //其他收入
        Double otherInMoney = 0.0;
        //备注
        String remark = null;
        for (OrderFinanceQuery financeQuery : outInfo) {
            Outcome2Vm oc = new Outcome2Vm();
            oc.setOutName(financeQuery.getOutName());
            oc.setOutPrice(financeQuery.getOutPrice());
            oc.setId(financeQuery.getId());
            oc.setPicture(financeQuery.getPicture());
            oc.setCreateUserId(financeQuery.getCreateUserId());
            oc.setCreateDate(financeQuery.getCreateDate());
            oc.setUpdateUserId(financeQuery.getUpdateUserId());
            oc.setUpdateDate(financeQuery.getUpdateDate());
            outList.add(oc);
            otherInMoney = (financeQuery.getOtherInPrice() == null ? 0.0d : financeQuery.getOtherInPrice());
            caiName = financeQuery.getFinanceName();
            remark = financeQuery.getRemark();
        }
        if(outInfo != null && !outInfo.isEmpty()){
            outcome = new OrderFinanceOutComeQuery();
            outcome.setOutList(outList);
            outcome.setOtherInPrice(otherInMoney);
            outcome.setCaiName(caiName);
            outcome.setRemark(remark);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("all", query);
        map.put("data", list);
        map.put("income", income);
        map.put("outcome", outcome);
        return map;
    }



    @ApiOperation(value = "管理分配首页",notes = "管理分配首页")
    @ApiImplicitParams({
          @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "lineName", value = "线路名", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "startDate", value = "开始时间", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "region", value = "区域模糊查询", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "endDate", value = "结束时间", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/pageOrder")
    @CrossOrigin
    @CheckToken
    public Page<OrderBo> pageOrder (@RequestParam("endDate") String endDate,
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
        return ordersDetailedService.pageOrder(page,dateParam.getStartTime(),dateParam.getEndTime(),lineName,region);
    }


    @ApiOperation(value = "交账结果",notes = "交账结果")
    @ApiImplicitParams({
          @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "lineName", value = "线路名", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "startDate", value = "开始时间", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "guideName", value = "导游名字", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "endDate", value = "结束时间", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/pageOrder2")
    @CrossOrigin
    @CheckToken
    public Page<OrderBo> pageOrder2 (@RequestParam("endDate") String endDate,
                                    @RequestParam("lineName") String lineName,
                                    @RequestParam("startDate") String startDate,
                                    @RequestParam("guideName") String guideName,
                                    @RequestParam("current") Integer current,
                                    @RequestParam("size") Integer size,
                                     @RequestParam("inStatus") Integer inStatus) {

        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        Page<OrderBo> page = new Page<>(current, size);

        TimeQuery dateParam = TimeData.getDateParam(startDate, endDate);
        return ordersDetailedService.pageOrder2(page,dateParam.getStartTime(),dateParam.getEndTime(),lineName,guideName,inStatus);
    }





    @ApiOperation(value = "查询是否已配车",notes = "查询是否已配车")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/queryOrderCarNo")
    @CrossOrigin
    @CheckToken
    public ResultVo queryOrderCarNo (@RequestParam("id") Long id) {
        if (id == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
        }
        OrderDetailedFainllyQuery query = ordersService.getCarNumber(id);
        ResultVo vo = new ResultVo();
        vo.setResult(query.getCarNo());
        return vo;
    }

    @ApiOperation(value = "管理员取消待交账账单",notes = "管理员取消待交账账单")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/cancelOrders")
    @CrossOrigin
    @CheckToken
    public ResultVo cancelOrders (@RequestParam("id") Long id) {
        if (id == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
        }
        ordersDetailedService.cancelOrders(id);

        ResultVo vo = new ResultVo();
        vo.setResult("OK");
        return vo;
    }


    @ApiOperation(value = "导游选人发送短信给顾客",notes = "导游选人发送短信给顾客")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("/sendMsgToCustomer")
    @CrossOrigin
    @CheckToken
    public ResultVo sendMsgToCustomer (@RequestParam("id") Long id) throws Exception {
        if (id == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
        }
        //获取当前订单全部选人记录如已发送过短信不再继续发送
        List<OrderDetailedQuery> list = ordersDetailedService.pageOrderDetailed2Type(id, null);
        List<ApplyUpdateInfo> applyIds = new ArrayList<>();
        for(OrderDetailedQuery detailed : list){
            if(detailed.getIsSend() == null || !detailed.getIsSend()){
                /*
                *(出行通知)尊敬的张三先生/女士，您订购的珠海一日游纯玩A线已成团，您的专属导游刘大顺(13825681876)稍后会联系您，请保持您的电话畅通!
                */
                String msg = "尊敬的" + detailed.getContactName()+"，您订的「"+ detailed.getLineName()+"」已成团" +
                        "，您的导游" + detailed.getGuideName() + "("+ detailed.getGuideTel() + ")稍后会联系您，请保持电话畅通！【珠港澳游】";
                Boolean result = sendService.send(detailed.getContactTel(),msg);
                if(result){
                    ApplyUpdateInfo model = new ApplyUpdateInfo();
                    model.setId(detailed.getApplyId());
                    applyIds.add(model);
                }
            }
        }
        //更新报名表短信通知状态
        if(!applyIds.isEmpty()){
            ordersDetailedService.updateApplySendStatu(applyIds);
        }
        ResultVo vo = new ResultVo();
        vo.setResult("OK");
        return vo;
    }

}
