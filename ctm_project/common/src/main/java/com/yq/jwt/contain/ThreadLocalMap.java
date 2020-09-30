package com.yq.jwt.contain;

import com.yq.jwt.entity.UserLoginQuery;

/**
 * @author qf
 * @date 2020/9/30
 * @vesion 1.0
 **/
public class ThreadLocalMap {

   private static ThreadLocal<UserLoginQuery> threadLocal = new ThreadLocal<>();

   public static UserLoginQuery get () {
      return threadLocal.get();
   }

   public static void set (UserLoginQuery query) {
      threadLocal.set(query);
   }

   public static void remove () {
      threadLocal.remove();
   }
}
