package eqlee.ctm.api.entity.vo;


import com.yq.jwt.entity.UserLoginQuery;
import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/11
 * @Version 1.0
 */
@Data
public class ResultVo {

    private String code;

    private String msg;

    private UserLoginQuery data;
}
