package com.yq.entity.send.ctmuser;

import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/12
 * @Version 1.0
 */
@Data
public class UserVo {

    private String userName;

    private String password;

    private String name;

    private String phone;

    private Long companyId;

    private String companyName;

    private String roleName;

    private List<CityBo> city;

    /**
     * 0--外部公司 1--本公司
     */
    private Integer type;

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
