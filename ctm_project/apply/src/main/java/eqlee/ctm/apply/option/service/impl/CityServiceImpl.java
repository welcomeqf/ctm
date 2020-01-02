package eqlee.ctm.apply.option.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import eqlee.ctm.apply.option.dao.CityMapper;
import eqlee.ctm.apply.option.entity.Option;
import eqlee.ctm.apply.option.service.ICityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/11/7
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CityServiceImpl extends ServiceImpl<CityMapper, Option> implements ICityService {


    /**
     * 增加城市
     * @param option
     */
    @Override
    public void insertCity(Option option) {
        int insert = baseMapper.insert(option);

        if (insert <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "增加城市失败");
        }
    }

    /**
     * 查询城市信息
     * @param cityName
     * @return
     */
    @Override
    public Option queryCity(String cityName) {
        LambdaQueryWrapper<Option> wrapper = new LambdaQueryWrapper<Option>()
                .eq(Option::getName,cityName);
        return baseMapper.selectOne(wrapper);
    }


    /**
     * 查询所有城市
     * @return
     */
    @Override
    public List<Option> queryAllCity() {
        return baseMapper.selectList(null);
    }
}
