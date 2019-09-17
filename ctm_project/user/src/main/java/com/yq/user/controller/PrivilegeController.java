package com.yq.user.controller;

import com.yq.constanct.CodeType;
import com.yq.user.entity.query.PrivilegeQuery;
import com.yq.user.entity.vo.ResultVo;
import com.yq.user.exception.ApplicationException;
import com.yq.user.service.IPrivilegeService;
import com.yq.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Author qf
 * @Date 2019/9/12
 * @Version 1.0
 */
@Api
@RestController
@Slf4j
@RequestMapping("/privilege")
public class PrivilegeController {

    @Autowired
    private IPrivilegeService privilegeService;

    @ApiOperation(value = "增加权限", notes = "增加权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "menuList", value = "所有菜单集合", required = true, dataType = "List", paramType = "path"),
    })
    @PostMapping("/insertPrivilege")
    @CrossOrigin
    public ResultVo insertPrivilege(@RequestBody PrivilegeQuery query) {
        if (StringUtils.isBlank(query.getRoleName()) || query.getMenuList().size() == 0) {
            log.error("param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        privilegeService.insertAllPrivilege(query.getRoleName(),query.getMenuList());
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }
}
