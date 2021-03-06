package eqlee.ctm.resource.company.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.resource.company.entity.Company;
import eqlee.ctm.resource.company.entity.query.CompanyAdminQuery;
import eqlee.ctm.resource.company.entity.query.CompanyCount;
import eqlee.ctm.resource.company.entity.query.CompanyQuery;
import eqlee.ctm.resource.company.entity.query.PageCompanyQuery;
import eqlee.ctm.resource.company.entity.vo.CompanyIndexVo;
import eqlee.ctm.resource.company.entity.vo.CompanyQueryVo;
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
   List<Company> queryAllCompany(Integer ctype);

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
    void UpdateCompany(CompanyVo companyVo);
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
     * 根据公司名称得到同行列表（分页）
     * @param pageCompany
     * @return
     */
    Page<CompanyIndexVo> GetCompanyPageByName(PageCompanyQuery pageCompany);


    /**
     *  查询
     * @param Id
     */
    CompanyQueryVo UpdateCompanyIndex(Long Id);

    /**
     * 查询公司
     * @param id
     * @return
     */
    Company queryCompanyById (Long id);

      /**
       * 得到公司名
       * @return
       */
    CompanyQuery getCompanyName ();

   /**
    * 查询自己的信息以及公司的信息
    * @return
    */
    CompanyAdminQuery queryAdminMeInfo ();

   /**
    * 注册
    * @param vo
    */
   void registerCompany (CompanyVo vo);


    /**
     *  公司未注册统计
     * @return
     */
    CompanyCount companyCount();
}
