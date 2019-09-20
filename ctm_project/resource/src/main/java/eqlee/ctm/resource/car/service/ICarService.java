package eqlee.ctm.resource.car.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.resource.car.entity.Car;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/18 0018
 * @Version 1.0
 */
public interface ICarService {
    /**
     * 增加车辆
     * @param car
     */
    void addCar(Car car);

    /**
     * 删除车辆
     * @param id
     */
    void deleteCar(Long id);

    /**
     * 更新车辆
     * @param car
     */
    void updateCar(Car car);

    /**
     * 查询车辆列表
     * @return
     */
    Page<Car> queryAllCar(Integer current,Integer size);

}
