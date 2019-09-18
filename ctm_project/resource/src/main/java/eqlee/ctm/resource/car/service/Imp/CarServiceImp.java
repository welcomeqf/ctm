package eqlee.ctm.resource.car.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.utils.IdGenerator;
import eqlee.ctm.resource.car.dao.CarMapper;
import eqlee.ctm.resource.car.entity.Car;
import eqlee.ctm.resource.car.service.ICarService;
import eqlee.ctm.resource.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/18 0018
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CarServiceImp extends ServiceImpl<CarMapper, Car>implements ICarService {
    @Override
    public void addCar(Car car) {
        IdGenerator idGenerator = new IdGenerator();
        car.setId(idGenerator.getNumberId());
        int add = baseMapper.insert(car);
        if(add<=0){
            log.error("insert car fail");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加车辆失败");
        }
    }

    @Override
    public void deleteCar(Long id) {
        int delete = baseMapper.deleteById(id);
        if(delete<=0){
            log.error("delete car fail");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"删除车辆失败");
        }
    }

    @Override
    public void updateCar(Car car) {
        int update = baseMapper.updateById(car);
        if(update<=0){
            log.error("update car fail");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"更新车辆失败");
        }
    }

    @Override
    public List<Car> queryAllCar() {
        return baseMapper.selectList(null);
    }
}
