package eqlee.ctm.apply.orders.entity.Vo;

import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/10/11
 * @Version 1.0
 */
@Data
public class OrderWithVo {

    private List<OrderIndexVo> orderIndexVos;

    /**
     * 更换导游人的Id
     */
    private Long id;
}
