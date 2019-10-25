package eqlee.ctm.api.resource;

import com.yq.jwt.islogin.CheckToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;



/**
 * @Author qf
 * @Date 2019/9/24
 * @Version 1.0
 */
@Slf4j
@Api("公司Api--9091:resource")
@RestController
@RequestMapping("/v1/app/company")
public class CompanyApiController {

    @Value("${api.userIp}")
    private String Ip;

    @Value("${resourceApi.port}")
    private String port;

    @Value("${resourceApi.path}")
    private String path;


    private final Integer Status = 200;

    @ApiOperation(value = "展示同行信息修改首页--9091:resource", notes = "展示同行信息修改首页--9091:resource")
    @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/CompanyIndex")
    public Object CompanyIndex () {

        return null;
    }


    @ApiOperation(value = "同行信息修改", notes = "同行信息修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "companyName", value = "公司名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "startDate", value = "合同开始时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endDate", value = "合同结束时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "payMethod", value = "支付方式", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "stopped", value = "状态", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/updateCompany")
    public Object updateCompany () {

        return null;
    }



    @ApiOperation(value = "同行信息删除", notes = "同行信息删除")
    @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping("/deleteCompany/{Id}")
    public Object deleteCompany () {

        return null;
    }


    @ApiOperation(value = "同行列表", notes = "由公司名查询的公司列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyName", value = "公司名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping("/queryCompanyByName")
    public Object queryCompanyByCompanyName () {

        return null;
    }

    @ApiOperation(value = "添加", notes = "添加同行信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyName", value = "公司名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "startDate", value = "合同开始时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endDate", value = "合同结束时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "payMethod", value = "支付方式", required = true, dataType = "String", paramType = "path"),
    })
    @PostMapping("/addCompany")
    public Object addCompany () {

        return null;
    }


    @ApiOperation(value = "同行状态修改", notes = "同行状态修改")
    @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/Stu/{Id}")
    public Object UpdateCompanyStop () {

        return null;
    }

    @ApiOperation(value = "根据id查询公司", notes = "根据id查询公司")
    @ApiImplicitParam(name = "id", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/queryCompanyById")
    public Object queryCompanyById () {

        return null;
    }

    @ApiOperation(value = "返回个人信息", notes = "返回个人信息")
    @GetMapping("/get")
    public Object queryAllMenu() {

        return null;
    }
}
