package eqlee.ctm.api.img.entity.query;

import lombok.Data;

/**
 * @author qf
 * @date 2019/11/27
 * @vesion 1.0
 **/
@Data
public class ImgQuery {

   private String tokenType;

   private String token;

   private Long expiresAt;
}
