package com.tranzvision.gd.util.wechart;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.httpclient.HttpClientService;

/**
 * 微信素材相关操作API
 * @author zhanglang
 *
 */
public class TzWxMaterialUtil {
	//新增其他类型素材
	private static final String tz_addMaterial_url = "https://api.weixin.qq.com/cgi-bin/material/add_material";
	
	//上传图文消息内的图片获取URL
	private static final String tz_uploadimg_url = "https://api.weixin.qq.com/cgi-bin/media/uploadimg";
	
	//新增永久图文素材
	private static final String tz_materialAddNews_url = "https://api.weixin.qq.com/cgi-bin/material/add_news";
	
	//修改永久图文素材
	private static final String tz_materialUpdateNews_url = "https://api.weixin.qq.com/cgi-bin/material/update_news";
	
	//删除永久素材
	private static final String tz_deleteMaterial_url = "https://api.weixin.qq.com/cgi-bin/material/del_material";
	
	//获取素材列表
	private static final String tz_batchGetMaterial_url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material";
	
	//获取永久素材
	private static final String tz_getMaterial_url = "https://api.weixin.qq.com/cgi-bin/material/get_material";
	
	
	
	/**
	 * 上传材料到微信服务器
	 * @param path	
	 * @param file
	 * @param videoDescr  {"title":"","introduction":""},title-视频素材的标题,introduction-视频素材的描述
	 * @return
	 * @throws TzException 
	 */
	private static String connectHttpsByPostForm(String path, File file, Map<String,Object> videoDescr) throws TzException{
		JacksonUtil jacksonUtil = new JacksonUtil();
		String result = null;
		
		try{
			boolean isVideo = false;
			String video_description = "";
			if(videoDescr != null && videoDescr.containsKey("title") && videoDescr.containsKey("introduction")){
				isVideo = true;
				video_description = jacksonUtil.Map2json(videoDescr);
			}

			long filelength = file.length();
			String fileName=file.getName();
			
			URL url = new URL(path);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false); // post方式不能使用缓存
			// 设置请求头信息
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			
			// 设置边界,这里的boundary是http协议里面的分割符，不懂的可惜百度(http 协议 boundary)，这里boundary 可以是任意的值(111,2222)都行
			String BOUNDARY = "----------" + System.currentTimeMillis();
			con.setRequestProperty("Content-Type","multipart/form-data; boundary=" + BOUNDARY);
			
			
			// 请求正文信息
			// 第一部分：
			StringBuilder sb = new StringBuilder();
			
			//这块是上传video是必须的参数，你们可以在这里根据文件类型做if/else 判断
			if(isVideo){
				sb.append("--"); // 必须多两道线
				sb.append(BOUNDARY);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data;name=\"description\" \r\n\r\n");
				sb.append(video_description+"\r\n");
			}
			
			
			sb.append("--"); // 必须多两道线
			sb.append(BOUNDARY);
			sb.append("\r\n");
			//这里是media参数相关的信息，这里是否能分开下我没有试，感兴趣的可以试试
			sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + fileName + "\";filelength=\"" + filelength + "\" \r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			
			System.out.println(sb.toString());
			
			byte[] head = sb.toString().getBytes("utf-8");
			// 获得输出流
			OutputStream out = new DataOutputStream(con.getOutputStream());
			// 输出表头
			out.write(head);
			
			// 文件正文部分
			// 把文件已流文件的方式 推入到url中
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			
			// 结尾部分，这里结尾表示整体的参数的结尾，结尾要用"--"作为结束，这些都是http协议的规定
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
			out.write(foot);
			out.flush();
			out.close();
			
			// 定义BufferedReader输入流来读取URL的响应
			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = null;
			String line = null;
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			
			result = buffer.toString();
			
		}catch(Exception e){
			throw new TzException("上传材料到微信服务器失败。"+e.getMessage());
		}
		
		return result;
	}
	
	
	

	/**
	 * 新增其他类型永久素材
	 * 
	 * @param accessToken
	 * @param file  上传的文件
	 * @param type 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
	 * @param title  上传类型为video的参数，视频素材的标题。其他类型传空即可
	 * @param introduction 上传类型为video的参数，视频素材的描述。其他类型传空即可
	 * @throws TzException 
	 * 
	 */
	public static Map<String,Object> addOtherMaterial(String accessToken,File file, String type, String title,String introduction) throws TzException {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try {
			if(!file.exists()){
				throw new TzException("素材文件不存在");
			}
			
			Map<String,Object> videoDescrMap = null;
			//这块是用来处理如果上传的类型是video的类型的
			if("video".equals(type)){
				if(title == null || "".equals(title) || introduction == null || "".equals(introduction)){
					throw new TzException("上传视频参数不能为空（标题和描述）");
				}
				
				videoDescrMap = new HashMap<String,Object>();
				videoDescrMap.put("title", title);
				videoDescrMap.put("introduction", introduction);
			}

			// 拼装请求地址
			String uploadMediaUrl = tz_addMaterial_url+"?access_token="+accessToken+"&type="+type;

			String result = TzWxMaterialUtil.connectHttpsByPostForm(uploadMediaUrl, file, videoDescrMap);
			jacksonUtil.json2Map(result);
			
			returnMap = jacksonUtil.getMap();
		} catch (Exception e) {
			throw new TzException(e.getMessage());
		}
		
		return returnMap;
	}
	
	
	
	/**
	 * 上传图文消息内的图片获取URL(本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。图片仅支持jpg/png格式，大小必须在1MB以下) 
	 * @param accessToken
	 * @param file
	 * @throws TzException 
	 */
	public static Map<String,Object> uploadMaterialContentImages(String accessToken,File file) throws TzException{
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try{
			if(!file.exists()){
				throw new TzException("图片文件不存在");
			}
			String fileName=file.getName();  
            String suffix=fileName.substring(fileName.lastIndexOf("."),fileName.length()); 
            if(!suffix.toLowerCase().equals("jpg") && !suffix.toLowerCase().equals("png")){
            	throw new TzException("图片格式不正确，仅支持jpg/png格式");
            }
            
			//上传图片素材      
	        String uploadImgUrl = tz_uploadimg_url+"?access_token="+accessToken;  
	        
	        String result = TzWxMaterialUtil.connectHttpsByPostForm(uploadImgUrl, file, null);
	        jacksonUtil.json2Map(result);
			
			returnMap = jacksonUtil.getMap();
		} catch (Exception e) {
			throw new TzException(e.getMessage());
		}
		
		return returnMap;
	}
	
	
	
	/**
	 * 新增图文素材
	 * @param access_token
	 * @param articleList
	 * [
		   {
		       "title": TITLE,								//标题
		       "thumb_media_id": THUMB_MEDIA_ID,			//图文消息的封面图片素材id（必须是永久mediaID）
		       "author": AUTHOR,							//作者
		       "digest": DIGEST,							//图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空。如果本字段为没有填写，则默认抓取正文前64个字。
		       "show_cover_pic": SHOW_COVER_PIC(0 / 1),		//是否显示封面，0为false，即不显示，1为true，即显示
		       "content": CONTENT,							//图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS,涉及图片url必须来源"上传图文消息内的图片获取URL"接口获取。外部图片url将被过滤。
		       "content_source_url": CONTENT_SOURCE_URL		//图文消息的原文地址，即点击“阅读原文”后的URL
		    },
		    ......
	    ]
	 * @return	
	  	{
		  "media_id":MEDIA_ID
		}
	 * @throws TzException
	 */
	public static String addMaterialNews(String access_token, List<Map<String,Object>> articleList) throws TzException{
		String returnStr = "";
		try{
			if(articleList == null || articleList.size() <=0){
				throw new TzException("新增图文素材参数不正确");
			}
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("articles", articleList);
			
			String materialAddNewsUrl = tz_materialAddNews_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(materialAddNewsUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	/**
	 * 修改永久图文素材
	 * @param access_token
	 * @param media_id								//要修改的图文消息的id
	 * @param index									//要更新的文章在图文消息中的位置（多图文消息时，此字段才有意义），第一篇为0
	 * @param articlesMap
	 * {
	      "title": TITLE,							//标题
	      "thumb_media_id": THUMB_MEDIA_ID,			//图文消息的封面图片素材id（必须是永久mediaID）
	      "author": AUTHOR,							//作者
	      "digest": DIGEST,							//图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
	      "show_cover_pic": SHOW_COVER_PIC(0 / 1),	//是否显示封面，0为false，即不显示，1为true，即显示
	      "content": CONTENT,						//图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS
	      "content_source_url": CONTENT_SOURCE_URL	//图文消息的原文地址，即点击“阅读原文”后的URL
	   }
	 * @return
	   	{
		 "errcode": ERRCODE,	//正确时errcode的值应为0。
		 "errmsg": ERRMSG
		}
	 * @throws TzException
	 */
	public static String updateMaterialNews(String access_token, String media_id, int index, Map<String,Object> articlesMap) throws TzException{
		String returnStr = "";
		try{
			if(articlesMap == null){
				throw new TzException("图文素材内容不能为空");
			}
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("media_id", media_id);
			paramsMap.put("index", index);
			paramsMap.put("articles", articlesMap);
			
			String materialUpdateNewsUrl = tz_materialUpdateNews_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(materialUpdateNewsUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	/**
	 * 删除永久素材
	 * @param access_token 
	 * @param media_id
	 * @return
	 	{
		 "errcode": ERRCODE,	//正确时errcode的值应为0。
		 "errmsg": ERRMSG
		}
	 * @throws TzException
	 */
	public static String deleteMaterial(String access_token, String media_id) throws TzException{
		String returnStr = "";
		try{
			if(media_id == null || "".equals(media_id)){
				throw new TzException("永久素材media_id不能为空");
			}
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("media_id", media_id);
			
			String deleteMaterialUrl = tz_deleteMaterial_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(deleteMaterialUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	/**
	 * 获取素材列表
	 * @param access_token
	 * @param type				//素材的类型，图片（image）、视频（video）、语音 （voice）、图文（news）
	 * @param offset			//从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
	 * @param count				//返回素材的数量，取值在1到20之间
	 * @return
	 	永久图文消息素材列表的响应如下：
		{
		  "total_count": TOTAL_COUNT,				//该类型的素材的总数
		  "item_count": ITEM_COUNT,					//本次调用获取的素材的数量
		  "item": [{
		      "media_id": MEDIA_ID,
		      "content": {
		          "news_item": [{
		              "title": TITLE,
		              "thumb_media_id": THUMB_MEDIA_ID,
		              "show_cover_pic": SHOW_COVER_PIC(0 / 1),
		              "author": AUTHOR,
		              "digest": DIGEST,
		              "content": CONTENT,
		              "url": URL,
		              "content_source_url": CONTETN_SOURCE_URL
		          },
		          //多图文消息会在此处有多篇文章
		          ]
		       },
		       "update_time": UPDATE_TIME
		   },
		   //可能有多个图文消息item结构
		 ]
		}
		其他类型（图片、语音、视频）的返回如下：
		{
		  "total_count": TOTAL_COUNT,
		  "item_count": ITEM_COUNT,
		  "item": [{
		      "media_id": MEDIA_ID,
		      "name": NAME,
		      "update_time": UPDATE_TIME,
		      "url":URL
		  },
		  //可能会有多个素材
		  ]
		}
	 * @throws TzException
	 */
	public static String batchGetMaterial(String access_token, String type, int offset, int count) throws TzException{
		String returnStr = "";
		try{
			if(!"image".equals(type) && !"video".equals(type) 
					&& !"voice".equals(type) && !"news".equals(type)){
				throw new TzException("图文素材类型不正确，图片（image）、视频（video）、语音 （voice）、图文（news）");
			}
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("type", type);
			paramsMap.put("offset", offset);
			paramsMap.put("count", count);
			
			String batchGetMaterialUrl = tz_batchGetMaterial_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(batchGetMaterialUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	/**
	 * 获取永久素材
	 * @param access_token
	 * @param media_id
	 * @return
	 	图文素材:
		{
		"news_item":
			[
			    {
			    "title":TITLE,
			    "thumb_media_id"::THUMB_MEDIA_ID,
			    "show_cover_pic":SHOW_COVER_PIC(0/1),
			    "author":AUTHOR,
			    "digest":DIGEST,
			    "content":CONTENT,
			    "url":URL,
			    "content_source_url":CONTENT_SOURCE_URL
			    },
			    //多图文消息有多篇文章
			 ]
		}
		视频消息素材：
		{
			 "title":TITLE,
			 "description":DESCRIPTION,
			 "down_url":DOWN_URL,
		}
		其他类型的素材消息，则响应的直接为素材的内容，开发者可以自行保存为文件
	 * @throws TzException
	 */
	public static String getMaterial(String access_token, String media_id) throws TzException{
		String returnStr = "";
		try{
			if(media_id == null || "".equals(media_id)){
				throw new TzException("永久素材media_id不能为空");
			}
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("media_id", media_id);
			
			String getMaterialUrl = tz_getMaterial_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(getMaterialUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
}
