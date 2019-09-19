package eqlee.ctm.resource.company.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.resource.company.entity.Company;
import eqlee.ctm.resource.company.entity.query.PageCompanyQuery;
import eqlee.ctm.resource.company.entity.vo.CompanyVo;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/17 0017
 * @Version 1.0
 */
public interface ICompanyService {
    /**
     * 查询所有同行列表（不分页）
     * @return
     */
   List<Company> queryAllCompany();

    /**
     * 增加同行信息
     * @return
     */

    void addCompany(CompanyVo companyVo);
    /**
     * 删除同行信息
     * @return
     */
    void deleteCompany(Long id);
    /**
     * 修改同行信息
     * @return
     */
    void UpdateCompany(Company company);
    /**
     * 根据公司名查询(不分页)
     * @return
     */
    List<Company> queryCompanyByCompanyName(String companyName);

    /**
     * 修改状态
     * @return
     */
    public void UpdateCompanyStopped(Long id);

    /**
     *查询所有同行列表（分页）
     * @param pageCompany
     * @return
     */
    Page<Company> GetCompanyPage(PageCompanyQuery pageCompany);

    /**
     * 根据公司名称得到同行列表（分页）
     * @param pageCompany
     * @return
     */
    Page<Company> GetCompanyPageByName(PageCompanyQuery pageCompany);

}
