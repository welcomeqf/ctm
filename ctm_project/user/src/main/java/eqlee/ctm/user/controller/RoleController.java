package eqlee.ctm.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.jwt.islogin.CheckToken;
import eqlee.ctm.user.entity.Sign;
import eqlee.ctm.user.entity.UserRole;
import eqlee.ctm.user.entity.query.RoleAddQuery;
import eqlee.ctm.user.entity.vo.ResultVo;
import eqlee.ctm.user.entity.vo.RoleUpdateVo;
import eqlee.ctm.user.entity.vo.RoleVo;
import eqlee.ctm.user.entity.vo.UserRoleVo;
import eqlee.ctm.user.service.IRoleService;
import com.yq.utils.StringUtils;
import eqlee.ctm.user.service.ISignService;
import eqlee.ctm.user.vilidata.DataUtils;
import eqlee.ctm.user.vilidata.SignData;
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


    @Autowired
    private ISignService signService;


    @ApiOperation(value = "增加角色", notes = "增加角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "appId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/addRole")
    @CrossOrigin
    public ResultVo addRole(@RequestBody RoleAddQuery roleVo) {
        if (StringUtils.isBlank(roleVo.getRoleName()) || StringUtils.isBlank(roleVo.getAppId())) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        roleService.addRole(roleVo);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "增加子角色", notes = "增加子角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "appId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/addZiRole")
    @CrossOrigin
    public ResultVo addZiRole(@RequestBody RoleAddQuery roleVo) {
        if (StringUtils.isBlank(roleVo.getRoleName()) || StringUtils.isBlank(roleVo.getAppId())) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        roleService.addZiRole(roleVo);
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

    @ApiOperation(value = "修改角色", notes = "修改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "roleName", value = "角色名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "stopped", value = "是否停用", required = true, dataType = "Boolean", paramType = "path")
    })
    @PostMapping("/updateRole")
    @CrossOrigin
    public ResultVo updateRole(@RequestBody RoleUpdateVo vo) {
        if (vo.getId() == null || StringUtils.isBlank(vo.getAppId()) || StringUtils.isBlank(vo.getRoleName())) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }


        //验证签名
        Sign sign = signService.queryOne(vo.getAppId());
        Boolean result = null;
        try {
            result = SignData.getResult(DataUtils.getDcodeing(vo.getAppId()), DataUtils.getDcodeing(sign.getInformation()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!result) {
            throw new ApplicationException(CodeType.AUTHENTICATION_ERROR);
        }

        UserRole role = new UserRole();
        role.setId(vo.getId());
        role.setRoleName(vo.getRoleName());
        role.setStopped(vo.getStopped());
        roleService.updateRole(role);
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
    @CheckToken
    public Page<UserRole> queryPageRole(@RequestParam("current") Integer current,
                                        @RequestParam("size") Integer size,@RequestParam("AppId") String AppId) {
        if (current == null || size == null || StringUtils.isBlank(AppId)) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"分页查询角色参数不能为空");
        }
        RoleVo roleVo = new RoleVo();
        roleVo.setCurrent(current);
        roleVo.setSize(size);
        roleVo.setAppId(AppId);
        return roleService.queryPageRole(roleVo);
    }


    @ApiOperation(value = "分页查询所有子角色", notes = "分页查询所有子角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "companyId", value = "公司id", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("/queryZiPageRole")
    @CrossOrigin
    @CheckToken
    public Page<UserRole> queryZiPageRole(@RequestParam("current") Integer current,
                                        @RequestParam("size") Integer size,
                                          @RequestParam("AppId") String AppId,
                                          @RequestParam("companyId") Long companyId) {
        if (current == null || size == null || StringUtils.isBlank(AppId)) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"分页查询子角色参数不能为空");
        }
        RoleVo roleVo = new RoleVo();
        roleVo.setCurrent(current);
        roleVo.setSize(size);
        roleVo.setAppId(AppId);
        roleVo.setCompanyId(companyId);
        return roleService.queryZiPageRole(roleVo);
    }
}
