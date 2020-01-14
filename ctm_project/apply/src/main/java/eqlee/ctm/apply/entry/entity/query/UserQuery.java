package eqlee.ctm.apply.entry.entity.query;


import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/10
 * @Version 1.0
 */
@Data
public class UserQuery {


    /**
     * 中文名
     */
    private String cname;

    /**
     * 电话
     */
    private String companyTel;

}
