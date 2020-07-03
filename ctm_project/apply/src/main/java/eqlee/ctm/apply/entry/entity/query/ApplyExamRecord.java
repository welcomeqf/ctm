package eqlee.ctm.apply.entry.entity.query;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2020/1/10
 * @vesion 1.0
 **/
@Data
public class ApplyExamRecord {

   /*
    *审核类型 0 报名审核 1 取消审核 2 修改记录
   */
   private String ExamineType;

   /*
    *审核结果 0 待审核 1 已审核 2 拒绝
    */
   private String ExamineResult;

   /**
    * 提交时间
    */
   private String CreateDate;

   /**
    * 审核人
    */
   private String ExamName;
}
