package eqlee.ctm.resource.company.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.resource.company.dao.CompanyMapper;
import eqlee.ctm.resource.company.entity.Company;
import eqlee.ctm.resource.company.entity.query.PageCompanyQuery;
import eqlee.ctm.resource.company.entity.vo.CompanyVo;
import eqlee.ctm.resource.company.service.ICompanyService;
import eqlee.ctm.resource.exception.ApplicationException;
import eqlee.ctm.resource.jwt.contain.LocalUser;
import eqlee.ctm.resource.jwt.entity.UserLoginQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Autowired
    private LocalUser localUser;

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
        UserLoginQuery user = localUser.getUser("用户信息");

        IdGenerator idGenerator = new IdGenerator();
        Company company = new Company();
        company.setId(idGenerator.getNumberId());
        company.setCompanyName(companyVo.getCompanyName());
        company.setCreateUserId(user.getId());
        company.setUpdateUserId(user.getId());
        company.setStartDate(LocalDateTime.parse(companyVo.getStartDate()));
        company.setEndDate(LocalDateTime.parse(companyVo.getEndDate()));
        company.setPayMethod(Integer.parseInt(companyVo.getPayMethod()));
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
     * @param companyVo
     */
    @Override
    public void UpdateCompany(CompanyVo companyVo) {
        UserLoginQuery user = localUser.getUser("用户信息");

        Company company = new Company();
        company.setId(companyVo.getId());
        company.setCompanyName(companyVo.getCompanyName());
        company.setStartDate(LocalDateTime.parse(companyVo.getStartDate()));
        company.setEndDate(LocalDateTime.parse(companyVo.getEndDate()));
        company.setUpdateUserId(user.getId());
        if(companyVo.getStopped() == "0"){
            company.setStopped(false);
        }
        if(companyVo.getStopped() == "1"){
            company.setStopped(true);
        }
        company.setPayMethod(Integer.parseInt(companyVo.getPayMethod()));

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
        queryWrapper.like(true, "CompanyName", companyName);
        List<Company> companies = baseMapper.selectList(queryWrapper);
        return companies;
    }


    /**
     * 更新公司状态
     * @param id
     */
    @Override
    public synchronized void UpdateCompanyStopped(Long id) {
        UserLoginQuery user = localUser.getUser("用户信息");
        Company company = new Company();
        Company newCompany = baseMapper.selectById(id);
        if(newCompany.isStopped())
            company.setStopped(false);
        else
            company.setStopped(true);
        company.setId(id);
        company.setUpdateUserId(user.getId());
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
        page.setSize(pageCompany.getSize());
        baseMapper.selectPage(page, queryWrapper);
        return page;
    }


    /**
     * 修改公司信息首页
     * @param Id
     */
    @Override
    public CompanyVo UpdateCompanyIndex(Long Id) {
        CompanyVo companyVo = new CompanyVo();
        Company company = new Company();
        company = baseMapper.selectById(Id);
        companyVo.setStartDate(company.getStartDate().toString());
        companyVo.setEndDate(company.getEndDate().toString());
        companyVo.setCompanyName(company.getCompanyName());
        companyVo.setId(Id);
        if (company.getPayMethod() == 0) {
            companyVo.setPayMethod("默认");
        }
        if (company.getPayMethod() == 0) {
            companyVo.setPayMethod("现结");
        }
        if (company.getPayMethod() == 0) {
            companyVo.setPayMethod("月结");
        }
        if (company.getPayMethod() == 0) {
            companyVo.setPayMethod("代收");
        }
        if (company.isStopped()){
            companyVo.setStopped("停用");
        }
        if (!company.isStopped()){
            companyVo.setStopped("正常");
        }
        return companyVo;
    }
}
