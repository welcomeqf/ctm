package eqlee.ctm.apply.entry.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
@Data
public class User {

    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long Id;

    /**
     * 中文名
     */
    private String CName;

}
