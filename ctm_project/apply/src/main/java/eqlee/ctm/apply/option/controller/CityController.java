package eqlee.ctm.apply.option.controller;

import com.yq.jwt.islogin.CheckToken;
import eqlee.ctm.apply.option.entity.Option;
import eqlee.ctm.apply.option.service.ICityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author qf
 * @date 2019/12/27
 * @vesion 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/v1/city")
public class CityController {

   @Autowired
   private ICityService cityService;


   @CrossOrigin
   @GetMapping("/queryAllCity")
   @CheckToken
   public List<Option> queryAllCity () {
      return cityService.queryAllCity();
   }
}
