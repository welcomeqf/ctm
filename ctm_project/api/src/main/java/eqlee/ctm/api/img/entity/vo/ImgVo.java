package eqlee.ctm.api.img.entity.vo;

import eqlee.ctm.api.img.entity.query.ImgQuery;
import lombok.Data;

/**
 * @author qf
 * @date 2019/11/27
 * @vesion 1.0
 **/
@Data
public class ImgVo {

   private Boolean error;

   private Integer code;

   private String msg;

   private ImgQuery data;
}
