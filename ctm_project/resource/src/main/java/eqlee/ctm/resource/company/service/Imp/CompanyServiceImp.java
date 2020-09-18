package eqlee.ctm.resource.company.service.Imp;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.CityJwtBo;
import com.yq.jwt.entity.PrivilegeMenuQuery;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.DateUtil;
import com.yq.utils.IdGenerator;
import com.yq.utils.SendUtils;
import com.yq.utils.StringUtils;
import eqlee.ctm.resource.company.dao.CompanyMapper;
import eqlee.ctm.resource.company.entity.Company;
import eqlee.ctm.resource.company.entity.query.*;
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
import java.util.ArrayList;
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

    @Autowired
    private SendUtils sendService;

    private final String MONTH_PAY = "月结";

    private final String NOW_PAY = "现结";

    private final String WITH_PAY = "面收";

    /**
     * 查询所用同行信息（不分页）
     * @return
     */
    @Override
    public List<Company> queryAllCompany(Integer ctype) {

        LambdaQueryWrapper<Company> lambdaQueryWrapper = new LambdaQueryWrapper<Company>()
              .eq(Company::getStopped,0)
              .eq(Company::getStatus,1);
        if(ctype != null && ctype == 1){
            //只留月结同行
            lambdaQueryWrapper = new LambdaQueryWrapper<Company>()
                    .eq(Company::getStopped,0)
                    .eq(Company::getPayMethod,3)
                    .eq(Company::getStatus,1);
        }
        List<Company> companies = baseMapper.selectList(lambdaQueryWrapper);
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

        LambdaQueryWrapper<Company> queryWrapper = new LambdaQueryWrapper<Company>()
              .eq(Company::getCompanyName,companyVo.getCompanyName())
              .or()
              .eq(Company::getCompanyFullName,companyVo.getCompanyFullName());
        Company company1 = baseMapper.selectOne(queryWrapper);

        if (company1 != null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "公司名不能重复");
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
        company.setChargeName(companyVo.getChargeName());
        company.setChargeTel(companyVo.getChargeTel());
        company.setFinanceName(companyVo.getFinanceName());
        company.setAddress(companyVo.getAddress());
        company.setFinanceTel(companyVo.getFinanceTel());
        company.setInsurance(companyVo.getInsurance());
        company.setBankCard(companyVo.getBankCard());
        company.setBusiness(companyVo.getBusiness());
        company.setLicence(companyVo.getLicence());
        company.setStatus(1);
        company.setCompanyPic(companyVo.getCompanyPic());
        company.setCompanyFullName(companyVo.getCompanyFullName());

        if (StringUtils.isNotBlank(companyVo.getStartDate())) {
            String startDate = companyVo.getStartDate() + " 00:00:00";
            company.setStartDate(DateUtil.parseDateTime(startDate));
        }

        if (StringUtils.isNotBlank(companyVo.getEndDate())) {
            String endDate = companyVo.getEndDate() + " 23:59:59";
            company.setEndDate(DateUtil.parseDateTime(endDate));
        }

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
    public void UpdateCompany(CompanyVo companyVo) {

        UserLoginQuery user = localUser.getUser("用户信息");

        Company company = new Company();
        company.setCompanyPic(companyVo.getCompanyPic());
        company.setLicence(companyVo.getLicence());
        company.setBusiness(companyVo.getBusiness());
        company.setBankCard(companyVo.getBankCard());
        company.setInsurance(companyVo.getInsurance());

        company.setCompanyNo(companyVo.getCompanyNo());

        company.setId(companyVo.getId());
        company.setCompanyName(companyVo.getCompanyName());

        if (StringUtils.isNotBlank(companyVo.getStartDate())) {
            String startDate = companyVo.getStartDate() + " 00:00:00";
            company.setStartDate(DateUtil.parseDateTime(startDate));
        }

        if (StringUtils.isNotBlank(companyVo.getEndDate())) {
            String endDate = companyVo.getEndDate() + " 23:59:59";
            company.setEndDate(DateUtil.parseDateTime(endDate));
        }

        company.setSxPrice(companyVo.getSxPrice());
        company.setCompanyNo(companyVo.getCompanyNo());
        company.setCreateUserId(user.getId());
        company.setUpdateUserId(user.getId());
        company.setChargeName(companyVo.getChargeName());
        company.setChargeTel(companyVo.getChargeTel());
        company.setFinanceName(companyVo.getFinanceName());
        company.setAddress(companyVo.getAddress());
        company.setFinanceTel(companyVo.getFinanceTel());
        company.setStatus(companyVo.getStatus());

        if (StringUtils.isNotBlank(companyVo.getCompanyFullName())) {
            company.setCompanyFullName(companyVo.getCompanyFullName());
        }

        if (NOW_PAY.equals(companyVo.getPayMethod())) {
            company.setPayMethod(1);
        }
        if (MONTH_PAY.equals(companyVo.getPayMethod())) {
            company.setPayMethod(2);
        }
        if (WITH_PAY.equals(companyVo.getPayMethod())) {
            company.setPayMethod(3);
        }

        //审核通过短信通知同行
        if(companyVo.getIsedit() == null && companyVo.getStatus() != null){
            /*
            *通过审核
            尊敬的王仁杰，您注册的阳光旅游系统账号已通过审核，登录账号：信柏商务，初始密码：注册手机号后6位，请用电脑登录网址：510766.com自行修改密码及增加子账号并妥善保管，可以用手机登录关联微信或电脑下载到桌面【阳光国旅】

            未通过审核
            尊敬的王仁杰，您注册的阳光旅游系统账号未通过审核，如有疑问可与我司联系，谢谢！【阳光国旅】
            */
            String msg = "";
            if(companyVo.getStatus() == 1){
                msg = "尊敬的" + companyVo.getChargeName() + "，您注册的阳光旅游系统账号已通过审核，登录账号：" + companyVo.getCompanyName() + "，初始密码：注册手机号后6位，请用电脑登录网址：510766.com自行修改密码及增加子账号并妥善保管，可以用手机登录关联微信或电脑下载到桌面【阳光国旅】";
            }else if(companyVo.getStatus() == 2){
                msg = "尊敬的" + companyVo.getChargeName() + "，您注册的阳光旅游系统账号未通过审核，如有疑问可与我司联系，谢谢！【阳光国旅】";
            }
            try{
                sendService.send(companyVo.getChargeTel(),msg);
            }catch (Exception ex){
            }

        }
//        System.out.print(company);
        List<Company> list = baseMapper.selectList(null);
        System.out.println("--------"+list);
        int update = baseMapper.updateById(company);
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
        if(newCompany.getStopped()) {
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
        return baseMapper.getCompanyPageByName(page,pageCompany.getName(),pageCompany.getOnlynew());
    }


    /**
     * 展示修改公司信息首页
     * @param Id
     */
    @Override
    public CompanyQueryVo UpdateCompanyIndex(Long Id) {
        CompanyQueryVo companyVo = new CompanyQueryVo();
        Company company = baseMapper.selectById(Id);

        if (company.getStartDate() != null) {
            companyVo.setStartDate(DateUtil.formatDate(company.getStartDate()));
        }

        if (company.getEndDate() != null) {
            companyVo.setEndDate(DateUtil.formatDate(company.getEndDate()));
        }
        companyVo.setCompanyName(company.getCompanyName());
        companyVo.setCompanyNo(company.getCompanyNo());
        companyVo.setCompanyPic(company.getCompanyPic());
        companyVo.setSxPrice(company.getSxPrice());
        companyVo.setId(Id);
        companyVo.setPayMethod(company.getPayMethod());
        companyVo.setStopped(company.getStopped());
        companyVo.setAddress(company.getAddress());
        companyVo.setBankCard(company.getBankCard());
        companyVo.setBusiness(company.getBusiness());
        companyVo.setChargeName(company.getChargeName());
        companyVo.setChargeTel(company.getChargeTel());
        companyVo.setFinanceName(company.getFinanceName());
        companyVo.setFinanceTel(company.getFinanceTel());
        companyVo.setInsurance(company.getInsurance());
        companyVo.setLicence(company.getLicence());
        companyVo.setStatus(company.getStatus());
        companyVo.setRemark(company.getRemark());
        companyVo.setCompanyFullName(company.getCompanyFullName());
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
        query.setCompanyFullName(company.getCompanyFullName());
        query.setId(user.getId());
        query.setCompanyNo(company.getCompanyNo());
        query.setSxPrice(company.getSxPrice());
        query.setAccount(user.getAccount());
        query.setCName(user.getCname());
        query.setRoleName(user.getRoleName());
        query.setTel(user.getTel());
        query.setOpenId(user.getOpenId());
        query.setWechatNickname(user.getWechatNickname());
        query.setWechatImage(user.getWechatImage());
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

    /**
     * 查询自己的信息以及该公司的信息资料
     * @return
     */
    @Override
    public CompanyAdminQuery queryAdminMeInfo() {
        UserLoginQuery user = localUser.getUser("用户信息");

        CompanyAdminQuery query = new CompanyAdminQuery();

        query.setId(user.getId());
        query.setAccount(user.getAccount());
        query.setCName(user.getCname());
        query.setRoleName(user.getRoleName());
        query.setTel(user.getTel());

        //查询公司信息
        Company company = baseMapper.selectById(user.getCompanyId());
        query.setCompanyName(company.getCompanyName());
        query.setCompanyNo(company.getCompanyNo());
        query.setCompanyPic(company.getCompanyPic());
        query.setStartDate(DateUtil.formatDate(company.getStartDate()));
        query.setEndDate(DateUtil.formatDate(company.getEndDate()));
        query.setPayMethod(company.getPayMethod());
        query.setSxPrice(company.getSxPrice());
        query.setCompanyFullName(company.getCompanyFullName());
        return query;
    }

    /**
     * 注册
     * @param companyVo
     */
    @Override
    public void registerCompany(CompanyVo companyVo) {

        LambdaQueryWrapper<Company> queryWrapper = new LambdaQueryWrapper<Company>()
                .eq(Company::getCompanyName,companyVo.getCompanyName())
                .or()
                .eq(Company::getCompanyFullName,companyVo.getCompanyFullName());
        Company company1 = baseMapper.selectOne(queryWrapper);

        if (company1 != null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "公司名不能重复");
        }

        IdGenerator idGenerator = new IdGenerator();
        Company company = new Company();
        company.setId(idGenerator.getNumberId());
        company.setCompanyName(companyVo.getCompanyName());
        company.setCompanyNo(companyVo.getCompanyNo());
        company.setChargeName(companyVo.getChargeName());
        company.setChargeTel(companyVo.getChargeTel());
        company.setFinanceName(companyVo.getFinanceName());
        company.setFinanceTel(companyVo.getFinanceTel());
        company.setInsurance(companyVo.getInsurance());
        company.setBankCard(companyVo.getBankCard());
        company.setBusiness(companyVo.getBusiness());
        company.setLicence(companyVo.getLicence());
        company.setAddress(companyVo.getAddress());
        company.setCompanyPic(companyVo.getCompanyPic());
        company.setCompanyFullName(companyVo.getCompanyFullName());

        int insert = baseMapper.insert(company);
        //通知后台接收同行注册审核人信息
        try{
            String jsonStr = sendService.queryNotifyAdminInfo("",4);
            List<UserOpenIdVm> notifyList = JSONObject.parseArray(jsonStr,  UserOpenIdVm.class);
            if(notifyList != null && !notifyList.isEmpty()){
                for(UserOpenIdVm vm : notifyList){
                    if(StringUtils.isNotBlank(vm.getOpenId())){
                        sendService.pushCompanyExamManage(vm.getOpenId(),company.getCompanyName(),company.getChargeName(),company.getChargeTel(),company.getId());
                    }
                }
            }

        }catch (Exception ex){
            throw new ApplicationException(CodeType.SERVICE_ERROR,ex.toString());
        }

        if (insert <= 0) {
            log.error("insert company fail");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加同行公司失败");
        }
    }

    @Override
    public CompanyCount companyCount() {
        Long id = 634337000551350272L;
        CompanyCount result = new CompanyCount();
        UserLoginQuery user = localUser.getUser("用户信息");



        for (PrivilegeMenuQuery query : user.getMenuList()) {
            if (id.equals(query.getMenuId())) {

                LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<Company>()
                        .eq(Company::getStatus,0);
                Integer integer = baseMapper.selectCount(wrapper);

                result.setCount(integer);
                return result;

            }
        }

        //
        result.setCount(99999);
        return result;
    }
}
