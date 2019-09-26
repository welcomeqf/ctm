package eqlee.ctm.user.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;



/**
 * @Author qf
 * @Date 2019/9/23
 * @Version 1.0
 */
@Data
public class MenuVo {

    private String MenuName;

    private String AppId;

    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long Parent;

    private String Action;

    private String IconClass;
}
