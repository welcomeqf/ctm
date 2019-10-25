package eqlee.ctm.apply.channle.controller;

import eqlee.ctm.apply.channle.service.IChannelService;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author qf
 * @Date 2019/10/25
 * @Version 1.0
 */
@RestController
@RequestMapping("/v1/Channle")
public class ChannleController {

    @Autowired
    private IChannelService channelService;

    @PostMapping("/insertChannle")
    public ResultVo insertChannle () {

        channelService.addChannel("直连");
        ResultVo vo = new ResultVo();
        vo.setResult("Ok");
        return vo;
    }
}
