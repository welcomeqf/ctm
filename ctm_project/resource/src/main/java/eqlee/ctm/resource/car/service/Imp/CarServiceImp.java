package eqlee.ctm.resource.car.service.Imp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.resource.car.dao.CarMapper;
import eqlee.ctm.resource.car.entity.Car;
import eqlee.ctm.resource.car.entity.Vo.CarVo;
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

    /**
     * 增加车辆
     * @param carVo
     */
    @Override
    public void addCar(CarVo carVo) {
        Car car = new Car();
        IdGenerator idGenerator = new IdGenerator();
        car.setId(idGenerator.getNumberId());
        car.setRemark(carVo.getRemark());
        car.setCarName(carVo.getCarName());
        car.setCarNo(carVo.getCarNo());
        car.setStatu(Integer.parseInt(carVo.getStatu()));
        int add = baseMapper.insert(car);
        if(add <= 0){
            log.error("insert car fail");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加车辆失败");
        }
    }

    /**
     * 根据Id删除车辆
     * @param id
     */
    @Override
    public void deleteCar(Long id) {
        int delete = baseMapper.deleteById(id);
        if(delete <= 0){
            log.error("delete car fail");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"删除车辆失败");
        }
    }

    /**
     * 更新车辆信息
     * @param carVo
     */
    @Override
    public void updateCar(CarVo carVo) {

        Car car = new Car();
        car.setId(carVo.getId());
        car.setRemark(carVo.getRemark());
        car.setCarName(carVo.getCarName());
        car.setCarNo(carVo.getCarNo());
        car.setStatu(Integer.parseInt(carVo.getStatu()));
        int update = baseMapper.updateById(car);
        if(update <= 0){
            log.error("update car fail");
            throw new ApplicationException(CodeType.SERVICE_ERROR,"更新车辆失败");
        }
    }

    /**
     * 查询车辆信息
     * @return
     */
    @Override
    public Page<Car> queryAllCar(Integer current,Integer size) {
        Page<Car> page = new Page<Car>();
        page.setSize(size);
        page.setCurrent(current);
        baseMapper.selectPage(page,null);
        return page;
    }


    /**
     * 车辆信息修改首页
     * @param Id
     * @return
     */
    @Override
    public CarVo updateCarIndex(Long Id) {
        CarVo carVo = new CarVo();
        Car car = new Car();
        car = baseMapper.selectById(Id);
        carVo.setCarName(car.getCarName());
        carVo.setCarNo(carVo.getCarNo());
        carVo.setId(car.getId());
        if (car.getStatu() == 0) {
            carVo.setStatu("未出行");
        }
        if (car.getStatu() == 1) {
            carVo.setStatu("已出行");
        }
        if (car.getStatu() == 2) {
            carVo.setStatu("已报废");
        }
        carVo.setRemark(carVo.getRemark());
        return carVo;
    }
}
