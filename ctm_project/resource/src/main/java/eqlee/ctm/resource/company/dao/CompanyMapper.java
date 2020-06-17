package eqlee.ctm.resource.company.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.resource.company.entity.Company;
import eqlee.ctm.resource.company.entity.query.PageCompanyQuery;
import eqlee.ctm.resource.company.entity.vo.CompanyIndexVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @Author Claire
 * @Date 2019/9/17 0017
 * @Version 1.0
 */
@Component
public interface CompanyMapper extends IBaseMapper<Company> {




    Page<CompanyIndexVo> getCompanyPageByName(Page<CompanyIndexVo>page,
                                               @Param("companyName") String companyName,@Param("onlynew") Integer onlynew);


    /**
     * 删除该公司旗下的所有子角色
     * @param id
     * @return
     */
    int deleteCompanyRole (Long id);

    /**
     * 删除该公司下的所有用户
     * @param id
     * @return
     */
    int deleteUser (Long id);
}
