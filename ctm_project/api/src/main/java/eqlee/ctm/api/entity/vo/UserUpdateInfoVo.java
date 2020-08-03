package eqlee.ctm.api.entity.vo;

import eqlee.ctm.api.entity.bo.CityBo;
import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/10/17
 * @Version 1.0
 */
@Data
public class UserUpdateInfoVo {


    private Long id;

    /**
     * 中文名
     */
    private String cname;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 电话
     */
    private String tel;

    private String roleName;

    /**
     * 是否停用（false--正常  true--停用）
     */
    private Boolean stopped;

    /**
     * 城市
     */
    private List<CityBo> city;

    /**
     * 是否接收报名审核通知的人
     */
    private Boolean ApplyExamNotifier;

    /**
     * 是否接收账单审核通知的人
     */
    private Boolean BillExamNotifier;

    /**
     * 是否接收导游选人通知的人
     */
    private Boolean GuideSelectNotifier;

    /**
     * 是否接收同行审核通知的人
     */
    private Boolean PeerExamNotifier;
}
