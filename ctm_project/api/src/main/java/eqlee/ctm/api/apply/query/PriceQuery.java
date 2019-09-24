package eqlee.ctm.api.apply.query;

import com.yq.data.PageUtils;
import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/18
 * @Version 1.0
 */
@Data
public class PriceQuery extends PageUtils {

    private String OutDate;

    private String LineName;
}
