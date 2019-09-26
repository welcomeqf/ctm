package eqlee.ctm.user.entity.query;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/19
 * @Version 1.0
 */
@Data
public class UserMenuQuery {

    private Long Id;

    private String MenuName;

    private String Action;

    private String IconClass;
}
