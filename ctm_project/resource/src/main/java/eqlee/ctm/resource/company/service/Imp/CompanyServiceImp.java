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
import eqlee.ctm.resource.company.entity.query.CompanyQuery;
import eqlee.ctm.resource.company.entity.query.PageCompanyQuery;
import eqlee.ctm.resource.company.entity.vo.CompanyIndexVo;
import eqlee.ctm.resource.company.entity.vo.CompanyQueryVo;
import eqlee.ctm.resource.company.entity.vo.CompanyVo;
import eqlee.ctm.resource.company.service.ICompanyService;
import eqlee.ctm.resource.company.vilidata.HttpUtils;
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

    @Autowired
    private HttpUtils apiService;

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
        LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<Company>()
              .eq(Company::getCompanyNo,companyVo.getCompanyNo());
        Company one = baseMapper.selectOne(wrapper);

        if (one != null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "该编号已存在");
        }

        UserLoginQuery user = localUser.getUser("用户信息");

        IdGenerator idGenerator = new IdGenerator();
        Company company = new Company();
        company.setId(idGenerator.getNumberId());
        company.setCompanyName(companyVo.getCompanyName());
        company.setCreateUserId(user.getId());
        company.setUpdateUserId(user.getId());
        company.setSxPrice(companyVo.getSxPrice());
        company.setCompanyNo(companyVo.getCompanyNo());
        company.setCompanyPic(companyVo.getCompanyPic());
        company.setStopped(companyVo.getStopped());
        String startDate = companyVo.getStartDate() + " 00:00:00";
        String endDate = companyVo.getEndDate() + " 23:59:59";
        company.setStartDate(DateUtil.parseDateTime(startDate));
        company.setEndDate(DateUtil.parseDateTime(endDate));
        if (NOW_PAY.equals(companyVo.getPayMethod())) {
            company.setPayMethod(1);
        }
        if (MONTH_PAY.equals(companyVo.getPayMethod())) {
            company.setPayMethod(2);
        }
        if (WITH_PAY.equals(companyVo.getPayMethod())) {
            company.setPayMethod(3);
        }

        if (!NOW_PAY.equals(companyVo.getPayMethod()) && !MONTH_PAY.equals(companyVo.getPayMethod()) && !WITH_PAY.equals(companyVo.getPayMethod())) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "结算类型有误");
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
    public void deleteCompany(Long id) {
        //先删除该公司对应的子角色
        //删除该公司下的所有用户
        try {
            apiService.deleteUserAndRole(id);
        } catch (Exception e) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "删除失败");
        }

        int delete = baseMapper.deleteById(id);
        if (delete <= 0) {
            log.error("delete company fail");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"删除公司失败");
        }
    }

    @Override
    public void UpdateCompany(Long Id, CompanyVo companyVo) {

        LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<Company>()
              .eq(Company::getCompanyNo,companyVo.getCompanyNo());
        Company one = baseMapper.selectOne(wrapper);

        if (one != null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "该编号已存在");
        }

        UserLoginQuery user = localUser.getUser("用户信息");

        Company company = new Company();
        company.setId(Id);
        company.setCompanyName(companyVo.getCompanyName());
        String startDate = companyVo.getStartDate() + " 00:00:00";
        String endDate = companyVo.getEndDate() + " 23:59:59";
        company.setStartDate(DateUtil.parseDateTime(startDate));
        company.setEndDate(DateUtil.parseDateTime(endDate));
        company.setSxPrice(companyVo.getSxPrice());
        company.setCompanyNo(companyVo.getCompanyNo());
        company.setCompanyPic(companyVo.getCompanyPic());
        company.setUpdateUserId(user.getId());
        if(companyVo.getStopped()){
            company.setStopped(true);
        }else {
            company.setStopped(false);
        }

        if (NOW_PAY.equals(companyVo.getPayMethod())) {
            company.setPayMethod(1);
        }
        if (MONTH_PAY.equals(companyVo.getPayMethod())) {
            company.setPayMethod(2);
        }
        if (WITH_PAY.equals(companyVo.getPayMethod())) {
            company.setPayMethod(3);
        }else {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "结算类型有误");
        }

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
    public Page<CompanyIndexVo> GetCompanyPageByName(PageCompanyQuery pageCompany) {
        Page<CompanyIndexVo> page = new Page<CompanyIndexVo>();
        page.setCurrent(pageCompany.getCurrent());
        page.setSize(pageCompany.getSize());
        return baseMapper.getCompanyPageByName(page,pageCompany.getName());
    }


    /**
     * 展示修改公司信息首页
     * @param Id
     */
    @Override
    public CompanyQueryVo UpdateCompanyIndex(Long Id) {
        CompanyQueryVo companyVo = new CompanyQueryVo();
        Company company = baseMapper.selectById(Id);
        companyVo.setStartDate(DateUtil.formatDateTime(company.getStartDate()));
        companyVo.setEndDate(DateUtil.formatDateTime(company.getEndDate()));
        companyVo.setCompanyName(company.getCompanyName());
        companyVo.setCompanyNo(company.getCompanyNo());
        companyVo.setCompanyPic(company.getCompanyPic());
        companyVo.setSxPrice(company.getSxPrice());
        companyVo.setId(Id);
        companyVo.setPayMethod(company.getPayMethod());
        if (company.isStopped()){
            companyVo.setStopped(true);
        }
        if (!company.isStopped()){
            companyVo.setStopped(false);
        }
        return companyVo;
    }

    /**
     * 查询公司
     * @param id
     * @return
     */
    @Override
    public Company queryCompanyById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 得到公司名
     * @return
     */
    @Override
    public CompanyQuery getCompanyName() {
        UserLoginQuery user = localUser.getUser("用户信息");

        Company company = baseMapper.selectById(user.getCompanyId());

        if (company == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该公司尚未签合同");
        }
        CompanyQuery query = new CompanyQuery();
        query.setCompanyName(company.getCompanyName());
        query.setId(user.getId());
        query.setCompanyNo(company.getCompanyNo());
        query.setSxPrice(company.getSxPrice());
        query.setAccount(user.getAccount());
        query.setCName(user.getCname());
        query.setRoleName(user.getRoleName());
        query.setTel(user.getTel());
        if (company.getPayMethod() == 1) {
            query.setPayType(NOW_PAY);
        } else if (company.getPayMethod() == 2) {
            query.setPayType(MONTH_PAY);
        } else if (company.getPayMethod() == 3) {
            query.setPayType(WITH_PAY);
        } else {
            query.setPayType("请选择");
        }
        return query;
    }
}
