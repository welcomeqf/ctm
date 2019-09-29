package eqlee.ctm.resource.car.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yq.constanct.CodeType;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.resource.car.dao.CarMapper;
import eqlee.ctm.resource.car.entity.Car;
import eqlee.ctm.resource.car.entity.Vo.CarUpdateVo;
import eqlee.ctm.resource.car.entity.Vo.CarVo;
import eqlee.ctm.resource.car.service.ICarService;
import eqlee.ctm.resource.exception.ApplicationException;
import eqlee.ctm.resource.jwt.contain.LocalUser;
import eqlee.ctm.resource.jwt.entity.UserLoginQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/18 0018
 * @Version 1.0
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class CarServiceImp extends ServiceImpl<CarMapper, Car> implements ICarService {

    @Autowired
    private LocalUser localUser;

    /**
     * 增加车辆
     * @param carVo
     */
    @Override
    public void addCar(CarVo carVo) {
        UserLoginQuery user = localUser.getUser("用户信息");
        Car car = new Car();
        IdGenerator idGenerator = new IdGenerator();
        car.setId(idGenerator.getNumberId());
        car.setRemark(carVo.getRemark());
        car.setCarName(carVo.getCarName());
        car.setCreateUserId(user.getId());
        car.setUpdateUserId(user.getId());
        LambdaQueryWrapper<Car> queryWrapper = new LambdaQueryWrapper<Car>()
                .eq(Car::getCarNo,carVo.getCarNo());
        Car one = baseMapper.selectOne(queryWrapper);
        if (one != null) {
            throw new ApplicationException(CodeType.SUCC_ERROR,"车牌号已被使用");
        }
        car.setCarNo(carVo.getCarNo());
        if(carVo.getStatu() != null) {
            car.setStatu(carVo.getStatu());
        }
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
    public void updateCar(CarUpdateVo carVo, Long Id) {
        UserLoginQuery user = localUser.getUser("用户信息");

        Car car = new Car();
        car.setUpdateUserId(user.getId());
        car.setId(Id);
        car.setRemark(carVo.getRemark());
        car.setCarName(carVo.getCarName());
        car.setCarNo(carVo.getCarNo());
        car.setStatu(carVo.getStatu());
        if(carVo.isStop()) {
            car.setIsStop(true);
        }
        else
            car.setIsStop(false);
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

        LambdaQueryWrapper<Car> queryWrapper = new LambdaQueryWrapper<Car>()
                .eq(Car::getIsCompany,true);
        Page<Car> page = new Page<Car>();
        page.setSize(size);
        page.setCurrent(current);
        baseMapper.selectPage(page,queryWrapper);
        return page;
    }


    /**
     * 车辆信息修改首页
     * @param Id
     * @return
     */
    @Override
    public Car updateCarIndex(Long Id) {
        return baseMapper.selectById(Id);
    }
}
