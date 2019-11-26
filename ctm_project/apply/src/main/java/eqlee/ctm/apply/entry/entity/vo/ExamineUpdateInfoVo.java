package eqlee.ctm.apply.entry.entity.vo;

import com.yq.data.PageUtils;
import lombok.Data;


/**
 * @Author qf
 * @Date 2019/10/15
 * @Version 1.0
 */
@Data
public class ExamineUpdateInfoVo{


    private Long Id;

    /**
     * 修改人
     */
    private String UpdateUserName;

    /**
     * 修改时间
     */
    private String UpdateDate;

    private String ContactName;

    private String ContactTel;

    private String Place;

    /**
     * 修改内容
     */
    private String Remark;

    private String oldConnectName;

    /**
     * 联系电话
     */
    private String oldConnectTel;

    /**
     * 接送地点
     */
    private String oldPlace;

}
