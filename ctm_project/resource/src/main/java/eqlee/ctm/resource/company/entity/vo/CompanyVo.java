package eqlee.ctm.resource.company.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author Claire
 * @Date 2019/9/17 0017
 * @Version 1.0
 */
@Data
public class CompanyVo {

    private Long Id;

    private String CompanyName;

    private String StartDate;

    private String endDate;

    private String PayMethod;

    private String Stopped;

}
