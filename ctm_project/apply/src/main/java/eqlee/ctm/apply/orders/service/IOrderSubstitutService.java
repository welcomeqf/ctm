package eqlee.ctm.apply.orders.service;

import eqlee.ctm.apply.entry.entity.query.ApplyNoReadCountQuery;
import eqlee.ctm.apply.orders.entity.OrderSubstitut;

import java.time.LocalDate;

/**
 * @Author qf
 * @Date 2019/10/21
 * @Version 1.0
 */
public interface IOrderSubstitutService {

    /**
     * 增加换人记录
     * @param orderSubstitut
     */
    void addOrderSubstitut (OrderSubstitut orderSubstitut);

    /**
     * 同意或拒绝换人
     * @param outDate
     * @param lineName
     * @param id
     * @param type
     */
    void apotSubstitution (LocalDate outDate, String lineName, Long id, Integer type);

    /**
     * 查询所有导游未审核的换人记录
     * @return
     */
    ApplyNoReadCountQuery queryGuideNoExaCount ();
}
