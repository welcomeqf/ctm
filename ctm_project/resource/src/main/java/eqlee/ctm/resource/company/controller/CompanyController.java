package eqlee.ctm.resource.company.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.utils.IdGenerator;
import eqlee.ctm.resource.company.entity.Company;
import eqlee.ctm.resource.company.entity.query.PageCompanyQuery;
import eqlee.ctm.resource.company.entity.vo.CompanyVo;
import eqlee.ctm.resource.company.entity.vo.ResultVo;
import eqlee.ctm.resource.company.service.ICompanyService;
import eqlee.ctm.resource.exception.ApplicationException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/17 0017
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/v1/app/resource/company")
public class CompanyController {

    @Autowired
    private ICompanyService companyService;



    @ApiOperation(value = "同行列表", notes = "同行列表展示")
    @GetMapping("/QueryCompany")
    @CrossOrigin
    public Page<Company> queryCompany(@RequestParam("current") Integer current) {

        PageCompanyQuery pageCompany = new PageCompanyQuery();
        pageCompany.setCurrent(current);
//        companyService.queryAllCompany();
//        return  companyService.queryAllCompany();
        return companyService.GetCompanyPage(pageCompany);
    }


    @ApiOperation(value = "同行信息修改", notes = "同行信息修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "CompanyName", value = "公司名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "StartDate", value = "合同开始时间", required = true, dataType = "LocalDateTime", paramType = "path"),
            @ApiImplicitParam(name = "endDate", value = "合同结束时间", required = true, dataType = "LocalDateTime", paramType = "path"),
            @ApiImplicitParam(name = "PayMethod", value = "支付方式", required = true, dataType = "Int", paramType = "path"),
            @ApiImplicitParam(name = "Stopped", value = "状态", required = true, dataType = "Boolen", paramType = "path")
    })
    @PostMapping("/UpdateCompany")
    @CrossOrigin
    public ResultVo UpdateCompany (@RequestBody Company company) {
        if(company==null) {
            log.error("update company param is null");
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"更新公司信息为空");
        }
        companyService.UpdateCompany(company);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }



    @ApiOperation(value = "同行信息删除", notes = "同行信息删除")
    @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/DeleteCompany")
    @CrossOrigin
    public ResultVo DeleteCompany (@RequestParam("id") Long id) {

        if(id==null) {
            log.error("delete company param is null");
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"删除公司Id为空");
        }
        companyService.deleteCompany(id);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }


    @ApiOperation(value = "同行列表", notes = "由公司名查询的公司列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CompanyName", value = "公司名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping("/queryCompanyByName")
    @CrossOrigin
    public Page<Company> queryCompanyByCompanyName (@RequestParam("CompanyName") String name,@RequestParam("current") Integer current) {

        if(name==null) {
            log.error("queryCompanyByName company param is null");
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"由公司名称查询列表的公司名称为空");
        }
        PageCompanyQuery pageCompany = new PageCompanyQuery();
        pageCompany.setCurrent(current);
        pageCompany.setName(name);
        return  companyService.GetCompanyPageByName(pageCompany);
    }


    @ApiOperation(value = "添加", notes = "添加同行信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CompanyName", value = "公司名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "StartDate", value = "合同开始时间", required = true, dataType = "LocalDateTime", paramType = "path"),
            @ApiImplicitParam(name = "endDate", value = "合同结束时间", required = true, dataType = "LocalDateTime", paramType = "path"),
            @ApiImplicitParam(name = "PayMethod", value = "支付方式", required = true, dataType = "Int", paramType = "path"),
    })
    @PostMapping("/addCompany")
    @CrossOrigin
    //@RequestBody CompanyVo companyVo
    public ResultVo addCompany () {

        CompanyVo company = new CompanyVo();
        company.setCompanyName("Claire");
        if(company==null) {
            log.error("add company param is null.");
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"增加公司参数不能为空.");
        }

        companyService.addCompany(company);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }
    @ApiOperation(value = "同行状态", notes = "同行状态修改")
    @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/UpdateCompanyStop")
    @CrossOrigin
    public ResultVo UpdateCompanyStop (@RequestParam("id") Long id) {

        if(id==null) {
            log.error("update companystop param is null");
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"修改公司状态Id为空");
        }
        companyService.UpdateCompanyStopped(id);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }
}
