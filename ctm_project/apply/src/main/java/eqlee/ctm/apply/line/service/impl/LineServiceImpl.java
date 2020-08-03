package eqlee.ctm.apply.line.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.CityJwtBo;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.line.dao.LineMapper;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.entity.query.LinePageQuery;
import eqlee.ctm.apply.line.entity.query.LineSeacherQuery;
import eqlee.ctm.apply.line.entity.vo.LineInfomationVo;
import eqlee.ctm.apply.line.entity.vo.LineUpdateVo;
import eqlee.ctm.apply.line.entity.vo.LineVo;
import eqlee.ctm.apply.line.service.ILineService;
import eqlee.ctm.apply.option.entity.Option;
import eqlee.ctm.apply.option.service.ICityService;
import eqlee.ctm.apply.orders.entity.OrderDetailed;
import eqlee.ctm.apply.price.entity.Price;
import eqlee.ctm.apply.price.service.IPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class LineServiceImpl extends ServiceImpl<LineMapper, Line> implements ILineService {

    @Autowired
    private LocalUser localUser;

    @Autowired
    private ICityService cityService;

    @Autowired
    private IPriceService priceService;

    @Autowired
    private IApplyService applyService;

    /**
     * 根据线路名查询线路
     * @param LineName
     * @return
     */
    @Override
    public Line queryLineByName(String LineName) {
        LambdaQueryWrapper<Line> queryWrapper = new LambdaQueryWrapper<Line>()
                .eq(Line::getLineName,LineName);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 增加线路
     * @param lineVo
     */
    @Override
    public synchronized void insertLine(LineVo lineVo) {
        LambdaQueryWrapper<Line> queryWrapper = new LambdaQueryWrapper<Line>()
                .eq(Line::getLineName,lineVo.getLineName());
        Line line1 = baseMapper.selectOne(queryWrapper);
        if (line1 != null) {
            throw new ApplicationException(CodeType.SUCC_ERROR,"该线路名已被使用");
        }

        IdGenerator idGenerator = new IdGenerator();
        //获取用户信息
        UserLoginQuery user = localUser.getUser("用户信息");

        Line line = new Line();
        //查询城市信息
        Option city = cityService.queryCity(lineVo.getCity());

        //如果该城市不存在就添加
        if (city == null) {
            //添加城市信息
            Option option = new Option();
            Long cityId = idGenerator.getNumberId();
            option.setId(cityId);
            option.setName(lineVo.getCity());
            option.setCreateUserId(user.getId());
            cityService.insertCity(option);
            line.setCityId(cityId);
        }

        //如果该城市存在就取出城市id
        if (city != null) {
            line.setCityId(city.getId());
        }

        line.setId(idGenerator.getNumberId());
        line.setLineName(lineVo.getLineName());
        line.setCity(lineVo.getCity());
        line.setPicturePath(lineVo.getPicturePath());
        line.setInformation(lineVo.getInformation());
        line.setRegion(lineVo.getRegion());
        line.setMaxNumber(lineVo.getMaxNumber());
        line.setMinNumber(lineVo.getMinNumber());
        line.setTravelSituation(lineVo.getTravelSituation());

        line.setCreateUserId(user.getId());
        line.setUpdateUserId(user.getId());
        int insert = baseMapper.insert(line);

        if (insert <= 0) {
            log.error("insert line fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加线路失败");
        }

    }

    /**
     * 修改线路
     * @param vo
     */
    @Override
    public void updateLine(LineUpdateVo vo, Long Id) {

        if (Id == 658822140488843264L) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "该线路不能修改");
        }

        //获取用户信息
        UserLoginQuery user = localUser.getUser("用户信息");

        Line line = new Line();

        line.setMinNumber(vo.getMinNumber());
        line.setMaxNumber(vo.getMaxNumber());
        line.setStopped(vo.getStopped());
        line.setRegion(vo.getRegion());
        line.setInformation(vo.getInformation());
        line.setLineName(vo.getLineName());
        line.setRemark(vo.getRemark());
        line.setCity(vo.getCity());
        line.setPicturePath(vo.getPicturePath());
        line.setTravelSituation(vo.getTravelSituation());
        line.setUpdateUserId(user.getId());
        line.setId(Id);
        line.setSort(vo.getSort());

        int i = baseMapper.updateById(line);

        if (i <= 0) {
            log.error("update line fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改线路失败");
        }
    }

    /**
     * 分页查询所有线路
     * @param query
     * @return
     */
    @Override
    public Page<LineSeacherQuery> listPageLine(Page<LineSeacherQuery> query, String lineName) {
        if (StringUtils.isNotBlank(lineName)) {
            return baseMapper.queryLine2PageAndName(query,lineName);
        }

        return baseMapper.queryLine2Page(query);
    }

    /**
     * 分页查询所有可报名线路
     * @param query
     * @param lineName
     * @return
     */
    @Override
    public Page<LineSeacherQuery> pageLine(Page<LineSeacherQuery> query, String lineName) {
        if (StringUtils.isBlank(lineName)) {
            //线路名为空
            return baseMapper.queryLine(query);
        }
        //根据线路名查询可报名线路
        return baseMapper.queryLineAndName(query,lineName);
    }


    /**
     * 停用线路
     * @param id
     */
    @Override
    public void updateStatus(Long id) {
        //获取用户信息
        UserLoginQuery user = localUser.getUser("用户信息");
        LambdaQueryWrapper<Line> wrapper = new LambdaQueryWrapper<Line>()
                .eq(Line::getId,id);
        Line one = baseMapper.selectOne(wrapper);
        Line line = new Line();
        line.setId(id);
        line.setStopped(true);
        line.setSort(one.getSort());
        line.setUpdateUserId(user.getId());
        int i = baseMapper.update(line, wrapper);
        //int i = baseMapper.updateById(line);
        if (i <= 0) {
            log.error("stop line fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"停用线路失败");
        }
    }

    /**
     * 启用线路
     * @param id
     */
    @Override
    public void updateNormal(Long id) {
        //获取用户信息
        UserLoginQuery user = localUser.getUser("用户信息");

        Line line = new Line();
        line.setId(id);
        line.setStopped(false);
        line.setUpdateUserId(user.getId());
        int i = baseMapper.updateById(line);
        if (i <= 0) {
            log.error("start line fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"启用线路失败");
        }
    }

    /**|
     * 根据Id查询一条线路
     * @param Id
     * @return
     */
    @Override
    public Line queryOneLine(Long Id) {
        return baseMapper.selectById(Id);
    }

    /**
     * 删除线路
     * @param Id
     */
    @Override
    public void deleteLine(Long Id) {

        if (Id == 658822140488843264L) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "该线路不能删除");
        }

        //设置价格，报名后的线路都不能删除
        List<Price> list = priceService.queryPriceByLineNameId(Id);

        if (list.size() > 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "该线路正在使用不能被删除");
        }

        List<Apply> applies = applyService.queryApplyByLineId(Id);

        if (applies.size() > 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "该线路正在使用不能被删除");
        }

        int delete = baseMapper.deleteById(Id);

        if (delete <= 0) {
            log.error("delete line fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"删除线路失败");
        }
    }

    /**
     * 查询所有线路
     * @return
     */
    @Override
    public List<Line> listAllLine() {

        return baseMapper.queryOrderbyLine();
    }

    @Override
    public List<Line> queryAllLine() {
        LambdaQueryWrapper<Line> wrapper = new LambdaQueryWrapper<Line>()
              .orderByDesc(Line::getCreateDate);
        return baseMapper.selectList(wrapper);
    }


    /**
     * 查询图文
     * @param id
     * @return
     */
    @Override
    public LineInfomationVo queryContent(Long id) {
        Line line = baseMapper.selectById(id);
        LineInfomationVo vo = new LineInfomationVo();
        vo.setContent(line.getInformation());
        return vo;
    }


    /**
     * 查询线路集合
     * @param list
     * @return
     */
    @Override
    public List<Line> queryByIdList(List<Long> list) {
        return baseMapper.selectBatchIds(list);
    }


    /**
     * 查询当前城市的所有线路
     * @return
     */
    @Override
    public List<Line> queryLocalCityLine() {
        UserLoginQuery user = localUser.getUser("用户信息");

        List<String> list = new ArrayList<>();
        if (user.getCity().size() > 0) {
            for (CityJwtBo bo : user.getCity()) {
                list.add(bo.getCity());
            }
        }


        return baseMapper.queryLocalCity (list);



    }

    /**
     * 修改线路
     * @param list
     */
    @Override
    public void updateLineSort(List<LineUpdateVo> list) {
        for(LineUpdateVo lineUpdateVo : list){
            Line line = new Line();
            line.setId(lineUpdateVo.getId());
            line.setSort(lineUpdateVo.getSort());
            baseMapper.updateById(line);
        }
    }

}
