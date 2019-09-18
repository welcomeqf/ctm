package eqlee.ctm.apply.line.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.utils.DateUtil;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.exception.ApplicationException;
import eqlee.ctm.apply.line.dao.LineMapper;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.entity.query.LineQuery;
import eqlee.ctm.apply.line.entity.vo.LineVo;
import eqlee.ctm.apply.line.service.ILineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        int insert = baseMapper.insert(line);

        if (insert <= 0) {
            log.error("insert line fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加线路失败");
        }
    }

    /**
     * 修改线路
     * @param line
     */
    @Override
    public void updateLine(Line line) {
        int i = baseMapper.updateById(line);

        if (i <= 0) {
            log.error("update line fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改线路失败");
        }
    }

    /**
     * 根据日期查询
     * @param dateTime
     * @return
     */
    @Override
    public List<LineQuery> listLine(String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return baseMapper.listAllLine();
        }
        //转换时间参数
        String StartTime = dateTime + " 00:00:00";
        String EndTime = dateTime + " 23:59:59";
        LocalDateTime startDate = DateUtil.parseDateTime(StartTime);
        LocalDateTime endDate = DateUtil.parseDateTime(EndTime);

        List<LineQuery> lineQueries = baseMapper.listLine(startDate, endDate);

        return lineQueries;
    }

    /**
     * 停用线路
     * @param id
     */
    @Override
    public void updateStatus(Long id) {
        Line line = new Line();
        line.setId(id);
        line.setStopped(true);
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
        Line line = new Line();
        line.setId(id);
        line.setStopped(false);
        int i = baseMapper.updateById(line);
        if (i <= 0) {
            log.error("start line fail.");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"启用线路失败");
        }
    }
}
