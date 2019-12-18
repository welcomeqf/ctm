package com.yq.entity.websocket;

/**
 * @author qf
 * @date 2019/12/13
 * @vesion 1.0
 **/
public enum NettyType {

   PERSON_EXA(6,"您有一条新的换人消息"),

   CAI_EXA(7,"您有一条新的财务审核记录"),

   CANCEL_EXA(8,"您有一条新的取消报名审核记录"),

   MSG_EXA(9,"您有一条新的报名审核记录"),

   AGGRE_EXA(10,"恭喜您,您的报名审核已通过"),

   NOAGGRE_EXA(11, "很遗憾,您的报名审核未通过"),

   AGGRE_CANCEL_EXA(12,"恭喜您,您的取消报名审核已通过"),

   NOAGGRE_CANCEL_EXA(13, "很遗憾,您的取消报名审核未通过"),

   AGGRE_PERSON_EXA(14, "很遗憾,您的换人申请未通过"),

   AGGRE_NO_PERSON_EXA(15, "很开心,您的换人申请已通过"),

   AGGRE_CAI_EXA(16, "很开心,您的财务审核已通过"),

   AGGRE_CAI_NO_EXA(16, "很遗憾,您的财务审核未通过");



   private int code;
   private String msg;

   NettyType(int code, String msg) {
      this.code = code;
      this.msg = msg;
   }

   public int getCode(){
      return code;
   }

   public void setCode(int code){
      this.code = code;
   }

   public String getMsg(){
      return msg;
   }

   public void setMsg(String msg){
      this.msg = msg;
   }

}
