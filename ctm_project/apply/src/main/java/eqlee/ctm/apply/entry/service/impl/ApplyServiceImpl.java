package eqlee.ctm.apply.entry.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.utils.IdGenerator;
import eqlee.ctm.apply.entry.dao.ApplyMapper;
import eqlee.ctm.apply.entry.entity.Apply;
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

    @Override
    public void insertApply(ApplyVo applyVo) {
        Line line = lineService.queryLineByName(applyVo.getLineName());
        IdGenerator idGenerator = new IdGenerator();
        //查询该天的价格情况
        Price price = priceService.queryPrice(applyVo.getOutDate());
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
        apply.setCompanyName(applyVo.getCompanyName());
        apply.setCompanyUser(applyVo.getCompanyUser());
        apply.setContactName(applyVo.getContactName());
        apply.setContactTel(applyVo.getContactTel());
        apply.setPlace(applyVo.getPlace());
        apply.setRegion(applyVo.getRegion());
        apply.setLineId(line.getId());
        apply.setPayType(applyVo.getPayType());

        int insert = baseMapper.insert(apply);

        if (insert <= 0) {
            log.error("insert apply fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"报名失败");
        }
    }
}
