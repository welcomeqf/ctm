package eqlee.ctm.resource.company.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.utils.IdGenerator;
import eqlee.ctm.resource.company.dao.CompanyMapper;
import eqlee.ctm.resource.company.entity.Company;
import eqlee.ctm.resource.company.entity.query.PageCompanyQuery;
import eqlee.ctm.resource.company.entity.vo.CompanyVo;
import eqlee.ctm.resource.company.service.ICompanyService;
import eqlee.ctm.resource.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/17 0017
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyServiceImp extends ServiceImpl<CompanyMapper,Company> implements ICompanyService {

    /**
     * 查询所用同行信息（不分页）
     * @return
     */
    @Override
    public List<Company> queryAllCompany() {

        List<Company> companies = baseMapper.selectList(null);
        return companies;
    }


    /**
     * 添加同行
     * @param companyVo
     */
    @Override
    public void addCompany(CompanyVo companyVo) {
        IdGenerator idGenerator = new IdGenerator();
        Company company = new Company();
        company.setId(idGenerator.getNumberId());
        company.setCompanyName(companyVo.getCompanyName());
        company.setStartDate(companyVo.getStartDate());
        company.setEndDate(companyVo.getEndDate());
        company.setPayMethod(companyVo.getPayMethod());
        int insert = baseMapper.insert(company);

        if (insert <= 0) {
            log.error("insert company fail");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加同行公司失败");
        }

    }


    /**
     * 根据公司Id删除同行信息
     * @param id
     */
    @Override
    public void deleteCompany(Long id)
    {
        int delete = baseMapper.deleteById(id);
        if (delete <= 0) {
            log.error("delete company fail");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"删除同行公司失败");
        }
    }


    /**
     * 更新同行信息
     * @param company
     */
    @Override
    public void UpdateCompany(Company company) {
      int update = baseMapper.updateById(company);
      if (update <= 0) {
            log.error("update company fail");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"更新同行公司失败");
      }
    }


    /**
     * 根据公司名称查询同行信息（不分页）
     * @param companyName
     * @return
     */
    @Override
    public List<Company> queryCompanyByCompanyName(String companyName) {

        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like("CompanyName",companyName);
        queryWrapper.like(true, "CompanyName", companyName);
        List<Company> companies = baseMapper.selectList(queryWrapper);
        return companies;
    }


    /**
     * 更新公司状态
     * @param id
     */
    @Override
    public void UpdateCompanyStopped(Long id) {
        Company company = new Company();
        Company newCompany = baseMapper.selectById(id);
        if(newCompany.isStopped())
            company.setStopped(false);
        else
            company.setStopped(true);
        company.setId(id);
        int update = baseMapper.updateById(company);
        if (update <= 0) {
            log.error("update stop fail");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改公司状态失败");
        }
    }


    /**
     * 查询所用同行信息（分页）
     * @param pageCompany
     * @return
     */
    @Override
    public Page<Company> GetCompanyPage(PageCompanyQuery pageCompany) {
        LambdaQueryWrapper<Company> queryWrapper = new LambdaQueryWrapper<Company>()
                .orderByDesc(Company::getCreateDate);

        Page<Company> page = new Page<Company>();
        page.setCurrent(pageCompany.getCurrent());
        baseMapper.selectPage(page, queryWrapper);
        return page;

    }


    /**
     * 根据公司名称查询公司信息（分页）
     * @param pageCompany
     * @return
     */
    @Override
    public Page<Company> GetCompanyPageByName(PageCompanyQuery pageCompany) {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(true, "CompanyName", pageCompany.getName());
//        LambdaQueryWrapper<Company> queryWrapper = new LambdaQueryWrapper<Company>()
//                .like(Company::getCompanyName,pageCompany.getName());
//        log.info(queryWrapper.toString());
        Page<Company> page = new Page<Company>();
        page.setCurrent(pageCompany.getCurrent());
        log.info(pageCompany.getName());
        baseMapper.selectPage(page, queryWrapper);
        return page;
    }
}
