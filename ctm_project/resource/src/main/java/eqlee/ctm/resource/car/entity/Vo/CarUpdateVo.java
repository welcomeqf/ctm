package eqlee.ctm.resource.car.entity.Vo;

import lombok.Data;

/**
 * @Author Claire
 * @Date 2019/9/28 0028
 * @Version 1.0
 */
@Data
public class CarUpdateVo {
    private String CarNo;

    private String CarName;

    private String Remark;


    private Integer Statu;

    private boolean IsStop;
}
