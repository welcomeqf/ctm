package eqlee.ctm.resource.company.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import eqlee.ctm.resource.company.entity.Company;
import eqlee.ctm.resource.company.entity.query.CompanyAdminQuery;
import eqlee.ctm.resource.company.entity.query.CompanyCount;
import eqlee.ctm.resource.company.entity.query.CompanyQuery;
import eqlee.ctm.resource.company.entity.query.PageCompanyQuery;
import eqlee.ctm.resource.company.entity.vo.CompanyIndexVo;
import eqlee.ctm.resource.company.entity.vo.CompanyQueryVo;
import eqlee.ctm.resource.company.entity.vo.CompanyVo;
import eqlee.ctm.resource.company.entity.vo.ResultVo;
import eqlee.ctm.resource.company.service.ICompanyService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    @ApiOperation(value = "展示同行信息修改首页", notes = "展示同行信息修改首页")
    @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/CompanyIndex")
    @CrossOrigin
    @CheckToken
    public CompanyQueryVo CompanyIndex (@RequestParam("Id") Long Id) {
        if(Id == null){
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
            @ApiImplicitParam(name = "payMethod", value = "支付方式", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "companyNo", value = "公司编号", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "companyPic", value = "合同图片", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "chargeName", value = "负责人名字", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "chargeTel", value = "负责人电话", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "financeName", value = "财务名字", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "financeTel", value = "财务电话", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "business", value = "营业执照图片", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "licence", value = "许可证", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "insurance", value = "保险证", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "bankCard", value = "银行卡", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "status", value = "0-待审核 1-通过 2-拒绝", required = true, dataType = "Integer", paramType = "path"),
    })
    @PostMapping("/updateCompany")
    @CrossOrigin
    @CheckToken
    public ResultVo updateCompany (@RequestBody CompanyVo companyVo) {
        if(companyVo.getId() == null ||  StringUtils.isBlank(companyVo.getCompanyName())){
            log.error("update company param is null");
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        companyService.UpdateCompany(companyVo);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }




    @ApiOperation(value = "同行信息删除", notes = "同行信息删除")
    @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/deleteCompany/{Id}")
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
            @ApiImplicitParam(name = "companyName", value = "公司名称", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "onlynew", value = "是否过滤已审核通过主账户", required = false, dataType = "Integer", paramType = "path")
    })
    @GetMapping("/queryCompanyByName")
    @CrossOrigin
    @CheckToken
    public Page<CompanyIndexVo> queryCompanyByCompanyName (@RequestParam("size") Integer size, @RequestParam("companyName") String companyName,
                                                           @RequestParam("current") Integer current,@RequestParam("onlynew") Integer onlynew) {

        if(current == null||size == null){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"当前页或者页面大小为空");
        }
        PageCompanyQuery pageCompany = new PageCompanyQuery();
        pageCompany.setCurrent(current);
        pageCompany.setSize(size);
        pageCompany.setName(companyName);
        pageCompany.setOnlynew(onlynew);
        return companyService.GetCompanyPageByName(pageCompany);
    }


    @ApiOperation(value = "添加", notes = "添加同行信息")
    @ApiImplicitParams({
          @ApiImplicitParam(name = "companyName", value = "公司名称", required = true, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "startDate", value = "合同开始时间", required = true, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "endDate", value = "合同结束时间", required = true, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "payMethod", value = "支付方式", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "companyNo", value = "公司编号", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "companyPic", value = "合同图片", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "chargeName", value = "负责人名字", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "chargeTel", value = "负责人电话", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "financeName", value = "财务名字", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "financeTel", value = "财务电话", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "business", value = "营业执照图片", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "licence", value = "许可证", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "insurance", value = "保险证", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "bankCard", value = "银行卡", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "sxPrice", value = "授信金额", required = true, dataType = "Integer", paramType = "path"),
    })
    @PostMapping("/addCompany")
    @CrossOrigin
    @CheckToken
    public ResultVo addCompany (@RequestBody CompanyVo companyVo) {
        if(companyVo.getPayMethod() == null || StringUtils.isBlank(companyVo.getCompanyName()) ||
              StringUtils.isBlank(companyVo.getChargeName()) || StringUtils.isBlank(companyVo.getCompanyFullName())) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"增加公司参数不能为空.");
        }
        companyService.addCompany(companyVo);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }



    @ApiOperation(value = "同行状态修改", notes = "同行状态修改")
    @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/Stu/{Id}")
    @CrossOrigin
    @CheckToken
    public ResultVo pdateCompanyStop (@PathVariable("Id") Long Id) {
        if(Id==null) {
            log.error("update company stop param is null");
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"修改公司状态Id为空");
        }
        companyService.UpdateCompanyStopped(Id);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }


    @ApiOperation(value = "根据id查询公司", notes = "根据id查询公司")
    @ApiImplicitParam(name = "id", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/queryCompanyById")
    @CrossOrigin
    @CheckToken
    public Company queryCompanyById (@RequestParam("id") Long id) {
        if(id == null){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return companyService.queryCompanyById(id);
    }

    @ApiOperation(value = "返回个人信息", notes = "返回个人信息")
    @GetMapping("/get")
    @CrossOrigin
    @CheckToken
    public CompanyQuery queryAllMenu(){
        return companyService.getCompanyName();

    }

    @ApiOperation(value = "返回同行未审核条数", notes = "返回同行未审核条数")
    @GetMapping("/companyCount")
    @CrossOrigin
    @CheckToken
    public CompanyCount companyCount(){
        return companyService.companyCount();
    }

    @ApiOperation(value = "查询所有同行", notes = "查询所有同行")
    @ApiImplicitParam(name = "ctype", value = "是否过滤现结用户", required = false, dataType = "Integer", paramType = "path")
    @GetMapping("/getCompany")
    @CrossOrigin
    @CheckToken
    public List<Company> getCompany(@RequestParam("ctype") Integer ctype){
        return companyService.queryAllCompany(ctype);
    }


    @ApiOperation(value = "查询自己的信息以及公司的信息", notes = "查询自己的信息以及公司的信息")
    @GetMapping("/queryAdminMeInfo")
    @CrossOrigin
    @CheckToken
    public CompanyAdminQuery queryAdminMeInfo(){
        return companyService.queryAdminMeInfo();

    }

    @ApiOperation(value = "注册", notes = "注册")
    @ApiImplicitParams({
          @ApiImplicitParam(name = "companyName", value = "公司名称", required = true, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "startDate", value = "合同开始时间", required = true, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "endDate", value = "合同结束时间", required = true, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "payMethod", value = "支付方式", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "companyNo", value = "公司编号", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "companyPic", value = "合同图片", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "chargeName", value = "负责人名字", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "chargeTel", value = "负责人电话", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "financeName", value = "财务名字", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "financeTel", value = "财务电话", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "business", value = "营业执照图片", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "licence", value = "许可证", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "insurance", value = "保险证", required = true, dataType = "Integer", paramType = "path"),
          @ApiImplicitParam(name = "bankCard", value = "银行卡", required = true, dataType = "Integer", paramType = "path"),
    })
    @PostMapping("/registerCompany")
    @CrossOrigin
    public ResultVo registerCompany (@RequestBody CompanyVo companyVo) {
        if(StringUtils.isBlank(companyVo.getCompanyName()) || StringUtils.isBlank(companyVo.getChargeName())
              || StringUtils.isBlank(companyVo.getAddress())) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"所填信息不能为空.");
        }
        companyService.registerCompany(companyVo);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }
}
