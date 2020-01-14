package eqlee.ctm.apply.line.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/28
 * @Version 1.0
 */
@Data
public class LineUpdateVo {

    private Long id;

    private String lineName;

    private String information;

    /**
     * 图片路径
     */
    private String picturePath;

    private String region;

    private Integer travelSituation;

    private Integer maxNumber;

    private Integer minNumber;

    private Boolean stopped;

    private String city;

    private String remark;

}
