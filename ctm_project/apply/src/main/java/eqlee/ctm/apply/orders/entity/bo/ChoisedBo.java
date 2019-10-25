package eqlee.ctm.apply.orders.entity.bo;

import eqlee.ctm.apply.orders.entity.Vo.ChoisedVo;
import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/10/11
 * @Version 1.0
 */
@Data
public class ChoisedBo {

    private List<ChoisedVo> choisedList;

    private String outDate;

    private String lineName;

    /**
     * 1--同意换人  2--不同意换人
     */
    private Integer type;

}
