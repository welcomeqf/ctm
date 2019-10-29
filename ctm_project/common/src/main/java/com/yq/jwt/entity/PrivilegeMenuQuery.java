package com.yq.jwt.entity;

import lombok.Data;


/**
 * @Author qf
 * @Date 2019/9/19
 * @Version 1.0
 */
@Data
public class PrivilegeMenuQuery {

    private Long menuId;

    private String menuName;

    private String action;

    private String iconClass;

    private String iconColor;

    private Long parent;
}
