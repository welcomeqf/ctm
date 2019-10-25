package eqlee.ctm.user.controller;

import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import eqlee.ctm.user.entity.query.PrivilegeQuery;
import eqlee.ctm.user.entity.vo.ResultVo;
import eqlee.ctm.user.service.IPrivilegeService;
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
@RequestMapping("v1/app/privilege")
public class PrivilegeController {

    @Autowired
    private IPrivilegeService privilegeService;

    @ApiOperation(value = "增加权限", notes = "增加权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "menuList", value = "所有菜单集合", required = true, dataType = "List", paramType = "path"),
            @ApiImplicitParam(name = "appId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/insertPrivilege")
    @CrossOrigin
    public ResultVo insertPrivilege(@RequestBody PrivilegeQuery query) {
        if (query.getRoleId() == null || query.getMenuList().size() == 0
            || StringUtils.isBlank(query.getAppId())) {
            log.error("param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        privilegeService.insertAllPrivilege(query.getRoleId(),query.getMenuList(),query.getAppId());
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }
}
