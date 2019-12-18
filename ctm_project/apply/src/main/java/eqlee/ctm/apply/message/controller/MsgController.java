package eqlee.ctm.apply.message.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.message.entity.query.MsgInfoQuery;
import eqlee.ctm.apply.message.entity.query.MsgQuery;
import eqlee.ctm.apply.message.entity.vo.*;
import eqlee.ctm.apply.message.service.IMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qf
 * @date 2019/11/13
 * @vesion 1.0
 **/
@Slf4j
@Api("消息Api")
@RestController
@RequestMapping("/v1/msg")
public class MsgController {

   @Autowired
   private IMessageService messageService;

   @ApiOperation(value = "增加消息记录", notes = "增加消息记录")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "createId", value = "发送人", required = false, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "toId", value = "接收人", required = true, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "msgType", value = "消息类型", required = true, dataType = "int", paramType = "path")
   })
   @PostMapping("/insertMsg")
   @CrossOrigin
   public ResultVo insertMsg(@RequestBody MsgVo msgVo) {
      if (msgVo.getToId() == null || msgVo.getMsgType() == null || StringUtils.isBlank(msgVo.getMsg())) {
         throw new ApplicationException(CodeType.PARAM_ERROR);
      }

      messageService.insertMsg(msgVo);
      ResultVo resultVo = new ResultVo();
      resultVo.setResult("ok");
      return resultVo;
   }


   @ApiOperation(value = "批量增加同一角色下所有用户的消息记录", notes = "批量增加同一角色下所有用户的消息记录")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "createId", value = "发送人", required = false, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "toId", value = "接收人集合", required = true, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "msgType", value = "消息类型", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "msg", value = "消息名称", required = true, dataType = "String", paramType = "path")
   })
   @PostMapping("/addAllMsg")
   @CrossOrigin
   public ResultVo addAllMsg(@RequestBody MsgAddVo msgVo) {
      if (msgVo.getToId() == null || msgVo.getMsgType() == null || StringUtils.isBlank(msgVo.getMsg())) {
         throw new ApplicationException(CodeType.PARAM_ERROR);
      }

      messageService.addAllMsg(msgVo);
      ResultVo resultVo = new ResultVo();
      resultVo.setResult("ok");
      return resultVo;
   }


   @ApiOperation(value = "修改查看状态", notes = "修改查看状态")
   @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "path")
   @PostMapping("/updateMsg")
   @CrossOrigin
   public ResultVo updateMsg(@RequestBody MsgUpdateVo vo) {
      if (vo.getId() == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR);
      }

      messageService.updateMsg(vo.getId());
      ResultVo resultVo = new ResultVo();
      resultVo.setResult("ok");
      return resultVo;
   }


   @ApiOperation(value = "根据消息类型查询所有未读信息", notes = "根据消息类型查询所有未读信息")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "msgType", value = "消息类型", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "toId", value = "接收人id", required = true, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int", paramType = "path")
   })
   @GetMapping("/pageMsg")
   @CrossOrigin
   public Page<MsgQuery> pageMsg(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                                 @RequestParam("toId") Long toId, @RequestParam("msgType") Integer msgType) {
      if (current == null || size == null || toId == null || msgType == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR);
      }

      Page<MsgQuery> page = new Page<>(current,size);
      return messageService.pageMsg(page,msgType,toId);
   }


   @ApiOperation(value = "查询未读消息", notes = "查询未读消息")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "msgType", value = "消息类型", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "toId", value = "接收人id", required = true, dataType = "Long", paramType = "path")
   })
   @GetMapping("/listMsgInfo")
   @CrossOrigin
   public List<MsgInfoQuery> listMsgInfo(@RequestParam("toId") Long toId, @RequestParam("msgType") Integer msgType) {
      if (toId == null || msgType == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR);
      }

      return messageService.listMsgInfo(msgType,toId);
   }

   @ApiOperation(value = "查询未读消息条数", notes = "查询未读消息条数")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "msgType", value = "消息类型", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "toId", value = "接收人id", required = true, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "msg", value = "消息名称", required = true, dataType = "String", paramType = "path")
   })
   @GetMapping("/queryCount")
   @CrossOrigin
   public MsgCountVo queryCount(@RequestParam("toId") Long toId,
                                @RequestParam("msgType") Integer msgType,
                                @RequestParam("msg") String msg) {
      if (toId == null || msgType == null || StringUtils.isBlank(msg)) {
         throw new ApplicationException(CodeType.PARAM_ERROR);
      }

      return messageService.queryCount(msgType,toId,msg);
   }


   @ApiOperation(value = "修改当前用户所有未读消息状态", notes = "修改当前用户所有未读消息状态")
   @ApiImplicitParam(name = "toId", value = "接收人id", required = true, dataType = "Long", paramType = "path")
   @PostMapping("/updateUserAllMsg")
   @CrossOrigin
   public ResultVo updateUserAllMsg(@RequestBody MsgUpdateAllVo vo) {
      if (vo.getToId() == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR);
      }

      messageService.updateUserAllMsg(vo.getToId());
      ResultVo resultVo = new ResultVo();
      resultVo.setResult("ok");
      return resultVo;
   }
}
