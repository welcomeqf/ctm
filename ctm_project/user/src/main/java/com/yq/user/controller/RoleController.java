package com.yq.user.controller;

import com.yq.constanct.CodeType;
import com.yq.user.entity.UserRole;
import com.yq.user.entity.vo.ResultVo;
import com.yq.user.exception.ApplicationException;
import com.yq.user.service.IRoleService;
import com.yq.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
@RestController
@Slf4j
@Api
@RequestMapping("/v1/app/user/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @ApiOperation(value = "增加角色", notes = "增加角色")
    @ApiImplicitParam(name = "RoleName", value = "角色名称", required = true, dataType = "String", paramType = "path")
    @PostMapping("/addRole")
    @CrossOrigin
    public ResultVo addRole(@RequestBody UserRole role) {
        if (StringUtils.isBlank(role.getRoleName())) {
            log.error("param not is null.");
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        roleService.addRole(role);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "path")
    @GetMapping("/deleteRole")
    @CrossOrigin
    public ResultVo deleteRole(@RequestParam("id") Long id) {
        if (id == null) {
            log.error("param not is null.");
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        roleService.deleteRole(id);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "查询角色", notes = "查询角色")
    @GetMapping("/RoleInfo")
    @CrossOrigin
    public List<UserRole> getRole() {
        return roleService.queryAllRole();
    }
}
