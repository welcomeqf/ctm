package eqlee.ctm.api.img.controller;

import com.alibaba.fastjson.JSONObject;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import com.yq.utils.IdGenerator;
import eqlee.ctm.api.baidu.ueditor.ActionEnter;
import eqlee.ctm.api.img.entity.FileUtils;
import eqlee.ctm.api.img.entity.query.ImgServerResultQuery;
import io.jsonwebtoken.lang.Assert;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import eqlee.ctm.api.img.vilidata.ImgTokenData;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author qf
 * @date 2019/11/27
 * @vesion 1.0
 **/
@Slf4j
@Api("图片api")
@RestController
@RequestMapping("/v1/img")
public class ImgController {

   @Autowired
   private ImgTokenData tokenData;

   @Autowired
   private HttpClientUtils apiService;

   IdGenerator idGenerator = new IdGenerator();

   private final Object createFileLock = new Object();


   @RequestMapping("/ueditor")
   @CrossOrigin
   public void uploadimage(HttpServletRequest request, HttpServletResponse response, MultipartFile upfile) {

      response.setContentType("application/json");
      String rootPath = request.getSession().getServletContext().getRealPath("/");
      try {
         String exec = "";
         String actionType = request.getParameter("action");

         if("config".equals(actionType)){

            request.setCharacterEncoding( "utf-8" );
            exec = new ActionEnter(request, rootPath).exec();

         }else{
            // 做图片上传操作
            exec = uploadImage(upfile);
         }
         PrintWriter writer = response.getWriter();
         response.setCharacterEncoding("utf-8");
         writer.write(exec);
         writer.flush();
         writer.close();
      } catch (IOException e) {
      }
   }

   /**
    * 上传文件图片到本地工程
    * @param file
    * @return
    * @throws Exception
    */
   @PostMapping("/storeFile")
   @CrossOrigin
   public String storeFile(@RequestBody MultipartFile file) throws Exception{

      if (file == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR, "未上传文件");
      }

      String name = file.getOriginalFilename();
      String extraName = name.substring(name.lastIndexOf("."));

      synchronized (createFileLock) {
         //文件保存路径
         Path path;
         //图片名称
         String fileName;
         do {
            //图片名称赋值
            fileName = idGenerator.getShortOrderNo();
            //保存路径赋值
            path = FileUtils.name2Path(fileName);

            //循环条件就是 如果文件地址不存在的情况下
         } while (checkPathConflict(path));
         try {
            //创建文件夹
            Files.createDirectories(path.getParent());
            //复制文件到指定文件夹下面
//            Files.copy(file.getInputStream(), path);
            String newPath = path.toString() + extraName;
            File toFile = FileUtils.multipartFileToFile(file,newPath);
            return  "http://tmpimg.ctm.eqlee.com/"+ fileName.substring(0, 8) + "/"  + fileName + extraName;
         } catch (IOException e) {
            log.info("io err", e);
            throw new IllegalArgumentException("文件上传失败");
         }
      }
   }


   private boolean checkPathConflict(Path path) {
      return Files.exists(path);
   }

   @GetMapping("/download")
   @CrossOrigin
   public void download(String key, HttpServletResponse response) {
      Assert.hasText(key, "Invalid key");
      try {
         Files.copy(FileUtils.getFile(key), response.getOutputStream());
      } catch (IOException e) {
         e.printStackTrace();
         throw new IllegalArgumentException("下载发送错误");
      } catch (StringIndexOutOfBoundsException e) {
         throw new IllegalArgumentException("Invalid key!");
      }
   }

   /**
    * 富文本上传图片
    * @param file
    * @return
    */
   public String uploadImage(MultipartFile file) {
      JSONObject jsonResult;
      try {
         String fileName = file.getOriginalFilename();
         String extraName = fileName.substring(fileName.lastIndexOf("."));
         // 这里就是上传图片的具体逻辑了，原本是通过fastdfs来上传图片的，这里就简单生成个字符串假装上传成功了吧
//            String url = FastdfsUtil.upload(file.getBytes(), file.getOriginalFilename());
         String url = getImg(file);
         jsonResult = new JSONObject(FileUtils.resultMap("SUCCESS",url,file.getSize(),fileName,fileName,extraName));
      } catch (Exception e) {
         jsonResult = new JSONObject(FileUtils.resultMap("文件上传失败","",0,"","",""));
      }


      return jsonResult.toString();
   }


   @PostMapping("/getImg")
   @CrossOrigin
   public String getImg (@RequestBody MultipartFile file) throws Exception{

      if (file == null) {
         throw new ApplicationException(CodeType.PARAM_ERROR, "文件为空");
      }

      //获取token
      String imgMapToken = tokenData.getImgMapToken();
      String token = "Bearer " + imgMapToken;

//      String url = "http://img.eqlee.com/v1/UploadImage/Single";

//      String url = "http://ctm.wapi.eqlee.com/api/v1/img/storeFile";

      String url = "http://img.eqlee.com/v1/Upload";

      //上传文件
      HttpResult httpResult = apiService.filePost(file, token, url);

      if (httpResult.getCode() != 200) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "网络开小差了...");
      }

      ImgServerResultQuery query = JSONObject.parseObject(httpResult.getBody(), ImgServerResultQuery.class);

      if (query.getError()) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, query.getMsg());
      }

      List data = query.getData();

      return (String) data.get(0);
   }



}
