package eqlee.ctm.resource.car.entity.Vo;

import lombok.Data;

/**
 * @Author Claire
 * @Date 2019/9/28 0028
 * @Version 1.0
 */
@Data
public class CarUpdateVo {
    private String carNo;

    private String carName;

    private String remark;


    private Integer statu;

    private boolean isStop;
}
