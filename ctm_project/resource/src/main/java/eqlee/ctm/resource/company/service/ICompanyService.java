package eqlee.ctm.resource.company.service;

import eqlee.ctm.resource.company.entity.Company;
import eqlee.ctm.resource.company.entity.vo.CompanyVo;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/17 0017
 * @Version 1.0
 */
public interface ICompanyService {
    /**
     * 查询所有同行列表
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
     * 根据公司名查询
     * @return
     */
    List<Company> queryCompanyByCompanyName(String companyName);

    /**
     * 修改状态
     * @return
     */
    public void UpdateCompanyStopped(Long id);

}
