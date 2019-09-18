package com.yq.data;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/18
 * @Version 1.0
 */
@Data
public abstract class PageUtils {

    private Integer current;
    private Integer size;
}
