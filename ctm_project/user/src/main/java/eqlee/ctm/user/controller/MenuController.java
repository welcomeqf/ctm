package eqlee.ctm.user.controller;

import eqlee.ctm.user.entity.query.UserMenuQuery;
import eqlee.ctm.user.entity.vo.MenuVo;
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
@RequestMapping("/v1/app/user/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @ApiOperation(value = "增加菜单", notes = "增加菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "MenuName", value = "菜单名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Parent", value = "父级ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "Action", value = "链接地址", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "IconClass", value = "图标", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/addMenu")
    @CrossOrigin
    public ResultVo addMenu(@RequestBody MenuVo vo) {

        menuService.addMenu(vo);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "查询所有权限", notes = "查询所有权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryMenu")
    @CrossOrigin
    public List<UserMenuQuery> queryMenu(@RequestParam("Id") Long Id, @RequestParam("AppId") String AppId) {
        return menuService.queryAllMenu(Id,AppId);
    }
}
