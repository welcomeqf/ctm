package eqlee.ctm.apply.message.controller;

import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.message.entity.vo.MsgConfigVo;
import eqlee.ctm.apply.message.entity.vo.ResultVo;
import eqlee.ctm.apply.message.service.IMessageConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author qf
 * @date 2019/11/13
 * @vesion 1.0
 **/
@Slf4j
@Api("消息配置Api")
@RestController
@RequestMapping("/v1/config")
public class MsgConfigController {

   @Autowired
   private IMessageConfigService configService;

   @ApiOperation(value = "增加消息记录", notes = "增加消息记录")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "msgTitle", value = "消息类型名称", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "msgContent", value = "消息内容", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "msgType", value = "消息类型", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "msgRemark", value = "消息具体配置", required = true, dataType = "String", paramType = "path")
   })
   @PostMapping("/insertMsgConfig")
   @CrossOrigin
   public ResultVo insertMsgConfig(@RequestBody MsgConfigVo vo) {
      if (vo.getMsgType() == null || StringUtils.isBlank(vo.getMsgContent()) || StringUtils.isBlank(vo.getMsgRemark())
      || StringUtils.isBlank(vo.getMsgTitle())) {
         throw new ApplicationException(CodeType.PARAM_ERROR);
      }

      configService.insertMsgConfig(vo);
      ResultVo resultVo = new ResultVo();
      resultVo.setResult("ok");
      return resultVo;
   }
}
