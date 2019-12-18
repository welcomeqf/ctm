package eqlee.ctm.api.img.entity;

import com.alibaba.fastjson.JSONObject;
import com.yq.utils.IdGenerator;
import io.jsonwebtoken.lang.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @auther qf
 * @date 2019/12/9
 * @vesion 1.0
 **/
public class FileUtils {


   /**
    * MultipartFile 转 File
    *
    * @param file
    * @throws Exception
    */
   public static File multipartFileToFile(MultipartFile file, String path) throws Exception {

      File toFile = null;
      if (file.equals("") || file.getSize() <= 0) {
         file = null;
      } else {
         InputStream ins = null;
         ins = file.getInputStream();
         toFile = new File(path);
         inputStreamToFile(ins, toFile);
         ins.close();
      }
      return toFile;
   }

   //获取流文件
   private static void inputStreamToFile(InputStream ins, File file) {
      try {
         OutputStream os = new FileOutputStream(file);
         int bytesRead = 0;
         byte[] buffer = new byte[8192];
         while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
         }
         os.close();
         ins.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   /**
    * 删除本地临时文件
    * @param file
    */
   public static void delteTempFile(File file) {
      if (file != null) {
         File del = new File(file.toURI());
         del.delete();
      }
   }




   public static Path getFile(String fileName) {
      //获取文件保存路径
      Path path = name2Path(fileName);
      Assert.isTrue(Files.exists(path), "Invalid Filename " + fileName);
      return path;
   }

   public static Path name2Path(String fileName) {
      String fileStorePath = "ctmImg";
      return Paths.get(fileStorePath, fileName.substring(0, 8), fileName);
   }

   public static Map<String,Object> resultMap(String state, String url, long size, String title, String original, String type){
      Map<String ,Object> result = new HashMap<>();
      result.put("state",state);
      result.put("original",original);
      result.put("size",size);
      result.put("title",title);
      result.put("type",type);
      result.put("url", url);
      return result;
   }




}
