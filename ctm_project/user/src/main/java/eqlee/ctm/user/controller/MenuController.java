package eqlee.ctm.user.controller;

import eqlee.ctm.user.entity.UserMenu;
import eqlee.ctm.user.entity.vo.ResultVo;
import eqlee.ctm.user.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @ApiImplicitParam(name = "MenuName", value = "菜单名称", required = true, dataType = "String", paramType = "path")
    @PostMapping("/addMenu")
    @CrossOrigin
    public ResultVo addMenu(@RequestBody UserMenu userMenu) {

        menuService.addMenu(userMenu);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }
}
