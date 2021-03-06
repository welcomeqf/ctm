package eqlee.ctm.apply.line.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Line")
public class Line extends Model<Line> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long Id;

    /**
     * 线路名
     */
    private String LineName;

    /**
     * 线路简介
     */
    private String Information;

    /**
     * 区域
     */
    private String Region;

    /**
     * 出游天数（几日游）
     */
    private Integer TravelSituation;

    /**
     * 最大人数
     */
    private Integer MaxNumber;

    /**
     * 最小人数
     */
    private Integer MinNumber;

    /**
     * 城市Id
     */
    private Long CityId;

    /**
     * 城市
     */
    private String City;

    /**
     * 图片
     */
    private String PicturePath;

    /**
     * 是否停用0默认false-正常 1-禁用true
     */
    private Boolean Stopped;

    /**
     * 备注
     */
    private String Remark;

    /**
     * 线路样式(暂存颜色)
     */
    private String Style;

    /**
     * 创建人
     */
    private Long CreateUserId;

    /**
     * 创建时间
     */
    private LocalDateTime CreateDate;

    /**
     * 修改人
     */
    private Long UpdateUserId;

    /**
     * 修改时间
     */
    private LocalDateTime UpdateDate;

    /**
     * 排序
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Integer sort;

    @Override
    protected Serializable pkVal() {
        return this.Id;
    }
}
