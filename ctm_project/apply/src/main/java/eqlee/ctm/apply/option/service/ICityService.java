package eqlee.ctm.apply.option.service;

import eqlee.ctm.apply.option.entity.Option;

/**
 * @Author qf
 * @Date 2019/11/7
 * @Version 1.0
 */
public interface ICityService {

    /**
     * 增加城市
     * @param option
     */
    void insertCity (Option option);


    /**
     * 根据城市查询信息
     * @param cityName
     * @return
     */
    Option queryCity (String cityName);
}
