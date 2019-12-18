package eqlee.ctm.api.img.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/10
 * @vesion 1.0
 **/
@Data
public class JsonResultVo {

   private String state;

   private String original;

   private String title;

   private Integer size;

   private String type;

   private String url;
}
