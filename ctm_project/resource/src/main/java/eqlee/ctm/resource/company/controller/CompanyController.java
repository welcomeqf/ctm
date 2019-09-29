package eqlee.ctm.resource.company.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.resource.company.entity.Company;
import eqlee.ctm.resource.company.entity.query.PageCompanyQuery;
import eqlee.ctm.resource.company.entity.vo.CompanyQueryVo;
import eqlee.ctm.resource.company.entity.vo.CompanyVo;
import eqlee.ctm.resource.company.entity.vo.ResultVo;
import eqlee.ctm.resource.company.service.ICompanyService;
import eqlee.ctm.resource.exception.ApplicationException;
import eqlee.ctm.resource.jwt.islogin.CheckToken;
import io.swagger.annotations.*;
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
@Api("公司Api")
@Slf4j
@RestController
@RequestMapping("/v1/app/company")
public class CompanyController {

    @Autowired
    private ICompanyService companyService;



    @ApiOperation(value = "同行列表", notes = "同行列表展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "页面大小", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping("/queryCompany")
    @CrossOrigin
    @CheckToken
    public Page<Company> queryCompany(@RequestParam("current") Integer current,@RequestParam("size") Integer size) {
        if(current == null||size == null){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"当前页或者页面大小为空");
        }
        PageCompanyQuery pageCompany = new PageCompanyQuery();
        pageCompany.setCurrent(current);
        pageCompany.setSize(size);
        return companyService.GetCompanyPage(pageCompany);
    }


    @ApiOperation(value = "展示同行信息修改首页", notes = "展示同行信息修改首页")
    @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/CompanyIndex")
    @CrossOrigin
    @CheckToken
    public CompanyQueryVo CompanyIndex (@RequestParam("Id") Long Id) {
        if(Id == null){
            log.error("update company param is null");
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return companyService.UpdateCompanyIndex(Id);
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
    @PutMapping("/updateCompany/{Id}")
    @CrossOrigin
    @CheckToken
    public ResultVo updateCompany (@PathVariable("Id") Long id, @RequestBody CompanyVo companyVo) {
        if(id == null || StringUtils.isBlank(companyVo.getPayMethod())
        || StringUtils.isBlank(companyVo.getEndDate())|| StringUtils.isBlank(companyVo.getStartDate())
        || StringUtils.isBlank(companyVo.getCompanyName())){
            log.error("update company param is null");
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        companyService.UpdateCompany(id,companyVo);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }




    @ApiOperation(value = "同行信息删除", notes = "同行信息删除")
    @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping("/deleteCompany/{Id}")
    @CrossOrigin
    @CheckToken
    public ResultVo deleteCompany (@PathVariable("Id") Long Id) {

        if(Id == null) {
            log.error("delete company param is null");
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"删除公司Id为空");
        }
        companyService.deleteCompany(Id);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }


    @ApiOperation(value = "同行列表", notes = "由公司名查询的公司列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyName", value = "公司名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping("/queryCompanyByName")
    @CrossOrigin
    @CheckToken
    public Page<Company> queryCompanyByCompanyName (@RequestParam("size") Integer size,@RequestParam("companyName") String companyName,
                                                    @RequestParam("current") Integer current) {

        if(current == null||size == null||StringUtils.isBlank(companyName)){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"当前页或者页面大小为空");
        }
        PageCompanyQuery pageCompany = new PageCompanyQuery();
        pageCompany.setCurrent(current);
        pageCompany.setSize(size);
        pageCompany.setName(companyName);
        return  companyService.GetCompanyPageByName(pageCompany);
    }


    @ApiOperation(value = "添加", notes = "添加同行信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyName", value = "公司名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "startDate", value = "合同开始时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endDate", value = "合同结束时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "payMethod", value = "支付方式", required = true, dataType = "String", paramType = "path"),
    })
    @PostMapping("/addCompany")
    @CrossOrigin
    @CheckToken
    public ResultVo addCompany (@RequestBody CompanyVo companyVo) {

        if(StringUtils.isBlank(companyVo.getPayMethod())
                || StringUtils.isBlank(companyVo.getEndDate())|| StringUtils.isBlank(companyVo.getStartDate())
                || StringUtils.isBlank(companyVo.getCompanyName())) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"增加公司参数不能为空.");
        }

        companyService.addCompany(companyVo);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }



    @ApiOperation(value = "同行状态", notes = "同行状态修改")
    @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    @PutMapping("/Stu/{Id}")
    @CrossOrigin
    @CheckToken
    public ResultVo UpdateCompanyStop (@PathVariable("Id") Long Id) {
        if(Id==null) {
            log.error("update company stop param is null");
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"修改公司状态Id为空");
        }
        companyService.UpdateCompanyStopped(Id);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }
}
