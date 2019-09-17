package com.yq.apply.entry.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Apply")
public class Apply extends Model<Apply> {

    private Long Id;

    private String ApplyNo;

    private LocalDateTime OutDate;


}
