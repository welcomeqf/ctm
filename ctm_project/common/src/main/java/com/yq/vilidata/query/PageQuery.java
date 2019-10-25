package com.yq.vilidata.query;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.List;

/**
 *
 * 分页的工具类
 * @Author qf
 * @Date 2019/10/15
 * @Version 1.0
 */
@Data
public class PageQuery<T> implements Serializable {

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页显示的条数
     */
    private Integer pageSize;

    /**
     * 总条数
     */
    private Integer tatolCount;

    /**
     * 总页数
     */
    private Integer tatolPage;

    /**
     * 数据
     */
    private List<T> list;
}
