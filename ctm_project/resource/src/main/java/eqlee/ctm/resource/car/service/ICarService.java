package eqlee.ctm.resource.car.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.resource.car.entity.Car;
import eqlee.ctm.resource.car.entity.Vo.CarVo;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/18 0018
 * @Version 1.0
 */
public interface ICarService {
    /**
     * 增加车辆
     * @param carVo
     */
    void addCar(CarVo carVo);

    /**
     * 删除车辆
     * @param id
     */
    void deleteCar(Long id);

    /**
     * 更新车辆
     * @param carVo
     */
    void updateCar(CarVo carVo , Long Id);

    /**
     * 查询车辆列表
     * @param current
     * @param size
     * @return
     */
    Page<Car> queryAllCar(Integer current,Integer size);

    /**
     * 展示
     * @param Id
     * @return
     */
    Car updateCarIndex(Long Id);

}
