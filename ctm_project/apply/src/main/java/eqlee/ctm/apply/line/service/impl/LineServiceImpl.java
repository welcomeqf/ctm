package eqlee.ctm.apply.line.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.utils.IdGenerator;
import eqlee.ctm.apply.exception.ApplicationException;
import eqlee.ctm.apply.jwt.contain.LocalUser;
import eqlee.ctm.apply.jwt.entity.UserLoginQuery;
import eqlee.ctm.apply.line.dao.LineMapper;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.entity.query.LinePageQuery;
import eqlee.ctm.apply.line.entity.vo.LineUpdateVo;
import eqlee.ctm.apply.line.entity.vo.LineVo;
import eqlee.ctm.apply.line.service.ILineService;
import eqlee.ctm.apply.price.entity.Price;
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
public class LineServiceImpl extends ServiceImpl<LineMapper, Line> implements ILineService {

    @Autowired
    private LocalUser localUser;

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
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该线路名已被使用");
        }
        IdGenerator idGenerator = new IdGenerator();
        Line line = new Line();
        line.setId(idGenerator.getNumberId());
        line.setLineName(lineVo.getLineName());
        line.setInformation(lineVo.getInformation());
        line.setRegion(lineVo.getRegion());
        line.setMaxNumber(lineVo.getMaxNumber());
        line.setMinNumber(lineVo.getMinNumber());
        line.setTravelSituation(lineVo.getTravelSituation());

        //获取用户信息
        UserLoginQuery user = localUser.getUser("用户信息");
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
        line.setTravelSituation(vo.getTravelSituation());
        line.setUpdateUserId(user.getId());
        line.setId(Id);

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
    public Page<Line> listPageLine(LinePageQuery query) {
        Page<Line> page = new Page<>();
        page.setCurrent(query.getCurrent());
        page.setSize(query.getSize());

        LambdaQueryWrapper<Line> queryWrapper = new LambdaQueryWrapper<Line>()
                .orderByDesc(Line::getCreateDate);
        baseMapper.selectPage(page,queryWrapper);
        return page;
    }


    /**
     * 停用线路
     * @param id
     */
    @Override
    public void updateStatus(Long id) {
        //获取用户信息
        UserLoginQuery user = localUser.getUser("用户信息");

        Line line = new Line();
        line.setId(id);
        line.setStopped(true);
        line.setUpdateUserId(user.getId());
        int i = baseMapper.updateById(line);
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




}
