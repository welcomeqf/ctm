package eqlee.ctm.resource.company.entity.query;

import com.yq.data.PageUtils;
import lombok.Data;

/**
 * @Author Claire
 * @Date 2019/9/18 0018
 * @Version 1.0
 */
@Data
public class PageCompanyQuery extends PageUtils {
    private String name;

    /**
     * 1 过滤已审核通过同行公司
     */
    private Integer onlynew;
}
