package eqlee.ctm.user.controller;

import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.utils.StringUtils;
import eqlee.ctm.user.entity.query.*;
import eqlee.ctm.user.entity.vo.MenuUpdateVo;
import eqlee.ctm.user.entity.vo.MenuVo;
import eqlee.ctm.user.entity.vo.MenuZiVo;
import eqlee.ctm.user.entity.vo.ResultVo;
import eqlee.ctm.user.service.IMenuService;
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
 * @Date 2019/9/11
 * @Version 1.0
 */
@Slf4j
@Api
@RestController
@RequestMapping("/v1/app/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;



    @ApiOperation(value = "查询所有权限", notes = "查询所有权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Id", value = "Id", required = false, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryMenu")
    @CrossOrigin
    public List<UserMenuQuery> queryMenu(@RequestParam("Id") Long Id, @RequestParam("AppId") String AppId) {
        return menuService.queryAllMenu(Id,AppId);
    }


    @ApiOperation(value = "查看权限", notes = "查看权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryAll")
    @CrossOrigin
    public List<WithQuery> queryAll (@RequestParam("AppId") String AppId, @RequestParam("roleId") Long roleId) {

        return menuService.queryAll(AppId,roleId);
    }

    @ApiOperation(value = "查看所有父权限", notes = "查看所有父权限")
    @GetMapping("/queryAllParent")
    @CrossOrigin
    public List<WithQuery> queryAllParent (@RequestParam("AppId") String AppId) {

        return menuService.queryAllParent(AppId);
    }

    @ApiOperation(value = "查看所有子角色权限", notes = "查看所有子角色权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/queryZiAll")
    @CrossOrigin
    public List<WithQuery> queryAll (@RequestBody MenuZiVo vo) {

        return menuService.queryZiAll(vo.getAppId(),vo.getRoleId(),vo.getList());
    }
}
