package eqlee.ctm.api.img.entity.query;

import lombok.Data;

import java.util.List;

/**
 * @author qf
 * @date 2019/12/12
 * @vesion 1.0
 **/
@Data
public class ImgServerResultQuery {

   private Boolean error;

   private Integer code;

   private String msg;

   private List data;
}
