package eqlee.ctm.resource.company.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.DateUtil;
import com.yq.utils.IdGenerator;
import eqlee.ctm.resource.company.dao.CompanyMapper;
import eqlee.ctm.resource.company.entity.Company;
import eqlee.ctm.resource.company.entity.query.PageCompanyQuery;
import eqlee.ctm.resource.company.entity.vo.CompanyQueryVo;
import eqlee.ctm.resource.company.entity.vo.CompanyVo;
import eqlee.ctm.resource.company.service.ICompanyService;
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

    private final String MONTH_PAY = "月结";

    private final String NOW_PAY = "现结";

    private final String WITH_PAY = "面收";

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
        if (NOW_PAY.equals(companyVo.getPayMethod())) {
            company.setPayMethod(1);
        }
        if (MONTH_PAY.equals(companyVo.getPayMethod())) {
            company.setPayMethod(2);
        }
        if (WITH_PAY.equals(companyVo.getPayMethod())) {
            company.setPayMethod(3);
        }
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

    @Override
    public void UpdateCompany(Long Id, CompanyVo companyVo) {
        UserLoginQuery user = localUser.getUser("用户信息");

        Company company = new Company();
        company.setId(Id);
        company.setCompanyName(companyVo.getCompanyName());
        company.setStartDate(LocalDateTime.parse(companyVo.getStartDate()));
        company.setEndDate(LocalDateTime.parse(companyVo.getEndDate()));
        company.setUpdateUserId(user.getId());
        if("停用".equals(companyVo.getStopped())){
            company.setStopped(true);
        }else {
            company.setStopped(false);
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
        if(newCompany.isStopped()) {
            company.setStopped(false);
        } else {
            company.setStopped(true);
        }
        company.setId(id);
        company.setUpdateUserId(user.getId());
        int update = baseMapper.updateById(company);
        if (update <= 0) {
            log.error("update stop fail");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改公司状态失败");
        }
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
     * 展示修改公司信息首页
     * @param Id
     */
    @Override
    public CompanyQueryVo UpdateCompanyIndex(Long Id) {
        CompanyQueryVo companyVo = new CompanyQueryVo();
        Company company = baseMapper.selectById(Id);
        companyVo.setStartDate(company.getStartDate().toString());
        companyVo.setEndDate(DateUtil.formatDate(company.getEndDate()));
        companyVo.setCompanyName(company.getCompanyName());
        companyVo.setId(Id);
        if (company.getPayMethod() == 1) {
            companyVo.setPayMethod(NOW_PAY);
        }
        if (company.getPayMethod() == 2) {
            companyVo.setPayMethod(MONTH_PAY);
        }
        if (company.getPayMethod() == 3) {
            companyVo.setPayMethod(WITH_PAY);
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
