package eqlee.ctm.resource.car.service;

import eqlee.ctm.resource.car.entity.Car;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/18 0018
 * @Version 1.0
 */
public interface ICarService {
    void addCar(Car car);
    void deleteCar(Long id);
    void updateCar(Car car);
    List<Car> queryAllCar();

}
