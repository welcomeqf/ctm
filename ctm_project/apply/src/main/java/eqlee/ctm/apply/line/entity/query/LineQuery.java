package eqlee.ctm.apply.line.entity.query;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
@Data
public class LineQuery {

    private LocalDateTime OutDate;

    private String LineName;

    private String Region;

    private String TravelSituation;

    /**
     * 是否停用0默认false-正常 1-禁用true
     */
    private Boolean Stopped;
}
