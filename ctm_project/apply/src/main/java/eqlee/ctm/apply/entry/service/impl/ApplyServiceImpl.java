package eqlee.ctm.apply.entry.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.utils.DateUtil;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.entry.dao.ApplyMapper;
import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.entity.query.*;
import eqlee.ctm.apply.entry.entity.vo.ApplyVo;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.exception.ApplicationException;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.service.ILineService;
import eqlee.ctm.apply.price.entity.Price;
import eqlee.ctm.apply.price.service.IPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;


/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper, Apply> implements IApplyService {

    @Autowired
    private ILineService lineService;

    @Autowired
    private IPriceService priceService;

    private final String MONTH_PAY = "月结";

    private final String NOW_PAY = "现结";

    private final String AGENT_PAY = "面收";

    @Override
    public void insertApply(ApplyVo applyVo) {
        Line line = lineService.queryLineByName(applyVo.getLineName());
        IdGenerator idGenerator = new IdGenerator();
        LocalDate localDate = DateUtil.parseDate(applyVo.getOutDate());
        //查询该天的价格情况
        Price price = priceService.queryPrice(localDate);
        //算出总价格
        Double AdultPrice = price.getAdultPrice()*applyVo.getAdultNumber();
        Double BabyPrice = price.getBabyPrice()*applyVo.getBabyNumber();
        Double OldPrice = price.getOldPrice()*applyVo.getOldNumber();
        Double ChildPrice = price.getChildPrice()*applyVo.getChildNumber();
        Double AllPrice = AdultPrice + BabyPrice + OldPrice + ChildPrice;
        //算出总人数
        Integer AllNumber = applyVo.getAdultNumber() + applyVo.getChildNumber() + applyVo.getOldNumber() + applyVo.getBabyNumber();
        //生成报名单号
        String orderCode = idGenerator.getOrderCode();

        Company company = baseMapper.queryCompanyById(applyVo.getCompanyNameId());
        //装配实体类
        Apply apply = new Apply();
        apply.setId(idGenerator.getNumberId());
        apply.setAdultNumber(applyVo.getAdultNumber());
        apply.setBabyNumber(applyVo.getBabyNumber());
        apply.setChildNumber(applyVo.getChildNumber());
        apply.setOldNumber(applyVo.getOldNumber());
        apply.setAllNumber(AllNumber);
        apply.setAllPrice(AllPrice);
        apply.setApplyNo(orderCode);
        apply.setCompanyName(company.getCompanyName());
        apply.setCompanyUser(applyVo.getCompanyUser());
        apply.setContactName(applyVo.getContactName());
        apply.setContactTel(applyVo.getContactTel());
        apply.setPlace(applyVo.getPlace());
        apply.setRegion(line.getRegion());
        apply.setLineId(line.getId());
        apply.setOutDate(localDate);
        apply.setCreateUserId(applyVo.getCreateUserId());
        apply.setUpdateUserId(applyVo.getUpdateUserId());
        if (MONTH_PAY.equals(applyVo.getPayType())) {
            apply.setPayType(1);
        }
        if (NOW_PAY.equals(applyVo.getPayType())) {
            apply.setPayType(0);
        }
        if (AGENT_PAY.equals(applyVo.getPayType())) {
            apply.setPayType(2);
        }

        int insert = baseMapper.insert(apply);

        if (insert <= 0) {
            log.error("insert apply fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"报名失败");
        }
    }

    /**
     * 分页展示报名记录
     * <a>如果只有出发日期，只对出发日期进行条件查询</a>
     * <b>如果只有线路名，区域任意一种，对线路名，或区域进行模糊查询</b>
     * <c>默认参数都为空，对所有分页</c>
     * <d>对出发日期，线路名或者区域查询</d>
     * @param page
     * @param OutDate
     * @param LineNameOrRegion
     * @return
     */
    @Override
    public Page<ApplyQuery> listPageApply(Page<ApplyQuery> page,String OutDate, String LineNameOrRegion) {
        if (StringUtils.isBlank(OutDate) && StringUtils.isBlank(LineNameOrRegion) ) {
            //分页查询所有报名记录
            return baseMapper.listPageApply(page);
        }

        if (StringUtils.isBlank(LineNameOrRegion) && StringUtils.isNotBlank(OutDate)) {
            //分页及时间条件查询报名记录
            LocalDate localDate = DateUtil.parseDate(OutDate);
            return baseMapper.listPageApplyByDate(page,localDate);
        }

        if (StringUtils.isBlank(OutDate) && StringUtils.isNotBlank(LineNameOrRegion)) {
            //分页及线路名或区域模糊查询报名记录
            return baseMapper.listPageApplyByLine(page,LineNameOrRegion);
        }

        //分页及时间条件查询及线路名或区域模糊查询报名记录
        LocalDate localDate = DateUtil.parseDate(OutDate);
        return baseMapper.listPageApplyByAll(page,LineNameOrRegion,localDate);
    }

    /**
     * 修改报名表
     * @param updateInfo
     */
    @Override
    public void updateApply(ApplyUpdateInfo updateInfo) {
        Apply apply = new Apply();
        apply.setId(updateInfo.getId());
        apply.setContactName(updateInfo.getContactName());
        apply.setContactTel(updateInfo.getContactTel());
        apply.setPlace(updateInfo.getPlace());
        LocalDateTime now = LocalDateTime.now();
        apply.setRemark(now + "修改");
        int result = baseMapper.updateById(apply);

        if (result <= 0) {
            log.error("update apply fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改报名表失败");
        }
    }

    /**
     * 分页查询已报名的列表,可以根据订单号模糊查询
     *（默认出来本公司所有的数据）
     * @param page
     * @param ApplyNo
     * @param LineName
     * @return
     */
    @Override
    public Page<ApplyDoQuery> listPageDoApply(Page<ApplyDoQuery> page, String ApplyNo, String LineName) {
        if (StringUtils.isBlank(ApplyNo) || StringUtils.isBlank(LineName)) {
            //当订单号 和线路名 都为空时
            return baseMapper.listPageDoApply(page);
        }

        if (StringUtils.isBlank(LineName) || StringUtils.isNotBlank(ApplyNo)) {
            //当线路名为空
            return baseMapper.listPageDoApplyByNo(page,ApplyNo);
        }

        if (StringUtils.isBlank(ApplyNo) || StringUtils.isNotBlank(LineName)) {
            //当订单号为空
            return baseMapper.listPageDoApplyByLineName(page,LineName);
        }

        //根据订单号和线路名模糊查询分页数据
        return baseMapper.listPageDoApplyByNoWithLine(page,LineName,ApplyNo);
    }

    /**
     * 查询同一公司所有报名记录
     * 对时间进行条件查询
     * @param page
     * @param OutTime
     * @param CompanyName
     * @return
     */
    @Override
    public Page<ApplyCompanyQuery> listPageDoApply2Company(Page<ApplyCompanyQuery> page, String OutTime, String CompanyName) {
        if (StringUtils.isBlank(OutTime)) {
            //时间为空   查询全部
            return baseMapper.listPageDoApply2Company(page,CompanyName);
        }
        LocalDate localDate = DateUtil.parseDate(OutTime);
        return baseMapper.listPageDoApply2CompanyByTime(page,localDate,CompanyName);
    }

    /**
     * 取消报名表
     * @param Id
     */
    @Override
    public void cancelApply(Long Id) {
        Apply apply = new Apply();
        apply.setId(Id);
        apply.setIsCancel(true);
        LocalDateTime now = LocalDateTime.now();
        apply.setRemark(now + "取消");
        int result = baseMapper.updateById(apply);

        if (result <= 0) {
            log.error("cancel apply fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"取消报名表失败");
        }
    }

    /**
     * 修改审核状态
     * @param Id
     * @param Status
     */
    @Override
    public void updateExamineStatus(Long Id, Integer Status) {
        Apply apply = new Apply();
        apply.setId(Id);
        apply.setStatu(Status);
        int i = baseMapper.updateById(apply);

        if (i <= 0) {
            log.error("update examine status fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"报名表修改审核状态失败");
        }
    }

    /**
     * 查询所有报名表
     * @return
     */
    @Override
    public List<Apply> selectAllApply() {
        return baseMapper.selectList(null);
    }

    /**
     * 查询公司信息
     * @param Id
     * @return
     */
    @Override
    public Company queryCompany(Long Id) {
        return baseMapper.queryCompanyById(Id);
    }

    /**
     * 查询一条报名记录
     * @param Id
     * @return
     */
    @Override
    public Apply queryById(Long Id) {
        return baseMapper.selectById(Id);
    }


}
