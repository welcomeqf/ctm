package com.yq.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 创建文件夹工具类
 * @author qf
 * @date 2019/12/12
 * @vesion 1.0
 **/
public class FilesUtils {


   public static String getPath (String name, String fileStorePath) {
      //文件保存路径
      Path path;
      IdGenerator idGenerator = new IdGenerator();
      //图片名称
      String fileName;
      String newPath;
      do {
         //图片名称赋值
         fileName = idGenerator.getShortOrderNo();
         //保存路径赋值
         path = name2Path(fileName,name, fileStorePath);

         //循环条件就是 如果文件地址不存在的情况下
      } while (checkPathConflict(path));
      try {
         //创建文件夹
         Files.createDirectories(path.getParent());
         //复制文件到指定文件夹下面
//            Files.copy(file.getInputStream(), path);
         newPath = path.toString();
      } catch (IOException e) {
         throw new IllegalArgumentException("文件上传失败");
      }
      return  newPath;
   }

   private static boolean checkPathConflict(Path path) {
      return Files.exists(path);
   }

   public static Path name2Path(String fileName, String name, String fileStorePath) {
      return Paths.get(fileStorePath, fileName.substring(0, 8), name);
   }
}
