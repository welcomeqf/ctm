package eqlee.ctm.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import eqlee.ctm.user.entity.UserRole;
import eqlee.ctm.user.entity.vo.ResultVo;
import eqlee.ctm.user.entity.vo.RoleVo;
import eqlee.ctm.user.entity.vo.UserRoleVo;
import eqlee.ctm.user.exception.ApplicationException;
import eqlee.ctm.user.jwt.contain.LocalUser;
import eqlee.ctm.user.jwt.entity.UserLoginQuery;
import eqlee.ctm.user.jwt.islogin.CheckToken;
import eqlee.ctm.user.service.IRoleService;
import com.yq.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
@RequestMapping("/v1/app/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;


    @ApiOperation(value = "增加角色", notes = "增加角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "appId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/addRole")
    @CrossOrigin
    public ResultVo addRole(@RequestBody UserRoleVo roleVo) {
        if (StringUtils.isBlank(roleVo.getRoleName())) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        roleService.addRole(roleVo);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/{id}/{AppId}")
    @CrossOrigin
    public ResultVo deleteRole(@PathVariable("id") Long id,@PathVariable("AppId") String AppId) {
        if (id == null || StringUtils.isBlank(AppId)) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        roleService.deleteRole(id,AppId);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "查询角色", notes = "查询角色")
    @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    @GetMapping("/RoleInfo")
    @CrossOrigin
    public List<UserRole> getRole(@RequestParam("AppId") String AppId) {
        return roleService.queryAllRole(AppId);
    }


    @ApiOperation(value = "分页查询所有角色", notes = "分页查询所有角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryPageRole")
    @CrossOrigin
    public Page<UserRole> queryPageRole(@RequestParam("current") Integer current,
                                        @RequestParam("size") Integer size,@RequestParam("AppId") String AppId) {
        if (current == null || size == null || StringUtils.isBlank(AppId)) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"分页查询角色参数不能为空");
        }
        RoleVo roleVo = new RoleVo();
        roleVo.setCurrent(current);
        roleVo.setSize(size);
        return roleService.queryPageRole(roleVo);
    }
}
