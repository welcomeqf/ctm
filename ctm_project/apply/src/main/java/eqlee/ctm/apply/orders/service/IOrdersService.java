package eqlee.ctm.apply.orders.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.apply.guider.entity.vo.ApplyVo;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.entity.Vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author Claire
 * @Date 2019/9/24 0024
 * @Version 1.0
 */
public interface IOrdersService {
    /**
     * 导游选人后保存
     *
     * @param applyVoList
     */
    void saveApply(List<LongVo> applyVoList);


    /**
     * 查看导游已选人情况
     *
     * @param page
     * @param LineName
     * @param OutDate
     * @return
     */
    Page<OrderIndexVo> ChoisedIndex(Page<OrderIndexVo> page, String LineName, String OutDate);


    /**
     * 由线路名，出发时间，导游姓名得到车牌号
     *
     * @return
     */
    String getCarNumber(String LineName, String OutDate);


    /**
     * 更换导游
     *
     * @param orderIndexVos
     * @param Id
     */
    void updateApply(List<OrderIndexVo> orderIndexVos, Long Id);


    /**
     * 保存车辆信息
     *
     * @param LineName
     * @param OutDate
     * @param CarNumber
     */
    void save(String LineName, String OutDate, String CarNumber);




    /**
     * 导游换人消息展示
     *
     * @return
     */
    List<ChangedVo> waiteChangeIndex();


    /**
     * 接受换人
     * @param choisedList
     */
    void sureChoised(List<ChoisedVo> choisedList);


    /**
     * 拒绝换人
     * @param choisedList
     */
    void denyChoised(List<ChoisedVo> choisedList);


    /**
     * 换人拒绝列表查询
     * @param LineName
     * @param OutDate
     * @return
     */
    List<ChangedVo> denyChoisedindex(String LineName,String OutDate);



    /**
     * 导游收入统计
     * @param LineName
     * @param OutDate
     * @return
     */
    IncomeVo IncomeCount(String LineName,String OutDate);



    /**
     * 未付款人列表
     * @param ContactTel
     * @return
     */
    UnpaidInformationVo unpaidInformation(String ContactTel,String LineName,String OutDate);
}
