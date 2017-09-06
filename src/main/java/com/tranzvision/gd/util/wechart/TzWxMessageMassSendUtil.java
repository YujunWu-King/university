package com.tranzvision.gd.util.wechart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.httpclient.HttpClientService;

/**
 * 微信公众号消息群发
 * @author zhanglang
 *
 */
public class TzWxMessageMassSendUtil {

	//上传图文消息素材
	private static final String tz_uploadnews_url = "https://api.weixin.qq.com/cgi-bin/media/uploadnews";
	
	//上传视频
	private static final String tz_uploadvideo_url = "https://api.weixin.qq.com/cgi-bin/media/uploadvideo";
	
	//根据标签进行群发
	private static final String tz_msssSendByTag_url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall";
	
	//根据标签进行群发
	private static final String tz_msssSendByOpenid_url = "https://api.weixin.qq.com/cgi-bin/message/mass/send";
	
	//查询群发消息发送状态
	private static final String tz_getSendStatus_url = "https://api.weixin.qq.com/cgi-bin/message/mass/get";
	
	
	
	/**
	 * 上传图文消息素材
	 * @param access_token
	 * @param articleList
	 * 	[
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
		   "type":"news",									//媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb），图文消息（news）	
		   "media_id":"CsEf3ldqkAYJAUfUJ54vq6-aYyUiQ",		//媒体文件/图文消息上传后获取的唯一标识
		   "created_at":1391857799							//媒体文件上传时间
		}
		错误时微信会返回错误码等信息
	 * @throws TzException
	 */
	public static String uploadNews(String access_token, List<Map<String,Object>> articleList) throws TzException{
		String returnStr = "";
		try{
			if(articleList == null || articleList.size() <=0){
				throw new TzException("上传图文素材参数不正确");
			}
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("articles", articleList);
			
			String uploadnewsUrl = tz_uploadnews_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(uploadnewsUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	
	
	
	/**
	 * 根据标签进行群发
	 * @param access_token
	 * @param mediaIdOrContent		用于群发的消息的media_id或文本消息
	 * @param msgtype				群发的消息类型，图文消息为mpnews，文本消息为text，语音为voice，音乐为music，图片为image，视频为video，卡券为wxcard
	 * @param tagId					群发到的标签的tag_id，参加用户管理中用户分组接口，若is_to_all值为true，可不填写tag_id
	 * @param title					消息的标题（仅视频）
	 * @param description			消息的描述（仅视频）
	 * @return
	  	{
		   "errcode":0,									//错误码
		   "errmsg":"send job submission success",		//错误信息
		   "msg_id":34182, 								//消息发送任务的ID
		   "msg_data_id": 206227730						//消息的数据ID，该字段只有在群发图文消息时，才会出现。可以用于在图文分析数据接口中，获取到对应的图文消息的数据，是图文分析数据接口中的msgid字段中的前半部分，详见图文分析数据接口中的msgid字段的介绍。
		}
	 * @throws TzException
	 */
	public static String messageMassSendByTag(String access_token, String mediaIdOrContent, String msgtype, String tagId, String title, String description) throws TzException{
		String returnStr = "";
		try{
			if(!"mpnews".equals(msgtype) 
					&& !"text".equals(msgtype) 
					&& !"voice".equals(msgtype) 
					&& !"image".equals(msgtype) 
					&& !"mpvideo".equals(msgtype) 
					&& !"wxcard".equals(msgtype)){
				throw new TzException("消息类型不正确");
			}
			
			//如果是发送视频
			if("mpvideo".equals(msgtype)){
				JacksonUtil jacksonUtil = new JacksonUtil();
				String uploadvideoUrl = tz_uploadvideo_url + "?access_token=" + access_token;
				
				Map<String, Object> videoParamsMap = new HashMap<String, Object>();
				videoParamsMap.put("media_id", mediaIdOrContent);
				videoParamsMap.put("title", title);
				videoParamsMap.put("description", description);
				
				//http请求
				HttpClientService HttpClientService = new HttpClientService(uploadvideoUrl,"POST",videoParamsMap,"UTF-8");
				String returnStr2 = HttpClientService.sendRequest();
				jacksonUtil.json2Map(returnStr2);
				
				if(jacksonUtil.containsKey("media_id")){
					mediaIdOrContent = jacksonUtil.getString("media_id");
				}else{
					throw new TzException("上传视频失败");
				}
			}
			

			Map<String, Object> filterMap = new HashMap<String, Object>();
			filterMap.put("is_to_all", false);
			filterMap.put("tag_id", tagId);
			
			Map<String, Object> contentMap = new HashMap<String, Object>();
			if("text".equals(msgtype)){
				contentMap.put("content", mediaIdOrContent);
			}else if("wxcard".equals(msgtype)){
				contentMap.put("card_id", mediaIdOrContent);
			}else{
				contentMap.put("media_id", mediaIdOrContent);
			}
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("msgtype", msgtype);
			paramsMap.put("filter", filterMap);
			paramsMap.put(msgtype, contentMap);
			if("mpnews".equals(msgtype)){
				paramsMap.put("send_ignore_reprint", 1);
			}

			String msssSendByTagUrl = tz_msssSendByTag_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(msssSendByTagUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	
	
	/**
	 * 根据OpenID列表群发
	 * @param access_token
	 * @param mediaIdOrContent			//用于群发的消息的media_id或文本消息
	 * @param msgtype					//群发的消息类型，图文消息为mpnews，文本消息为text，语音为voice，音乐为music，图片为image，视频为video，卡券为wxcard
	 * @param openIds					//群发用户OPENID数组
	 * @param title						//消息的标题（仅视频）
	 * @param description				//消息的描述（仅视频）
	 * @return
	 	{
		   "errcode":0,									//错误码
		   "errmsg":"send job submission success",		//错误信息
		   "msg_id":34182, 								//消息发送任务的ID
		   "msg_data_id": 206227730						//消息的数据ID，该字段只有在群发图文消息时，才会出现。可以用于在图文分析数据接口中，获取到对应的图文消息的数据，是图文分析数据接口中的msgid字段中的前半部分，详见图文分析数据接口中的msgid字段的介绍。
		}
	 * @throws TzException
	 */
	public static String messageMassSendByOpenid(String access_token, String mediaIdOrContent, String msgtype, String[] openIds , String title, String description) throws TzException{
		String returnStr = "";
		try{
			if(!"mpnews".equals(msgtype) 
					&& !"text".equals(msgtype) 
					&& !"voice".equals(msgtype) 
					&& !"image".equals(msgtype) 
					&& !"mpvideo".equals(msgtype) 
					&& !"wxcard".equals(msgtype)){
				throw new TzException("消息类型不正确");
			}
			
			//如果是发送视频
			if("mpvideo".equals(msgtype)){
				JacksonUtil jacksonUtil = new JacksonUtil();
				String uploadvideoUrl = tz_uploadvideo_url + "?access_token=" + access_token;
				
				Map<String, Object> videoParamsMap = new HashMap<String, Object>();
				videoParamsMap.put("media_id", mediaIdOrContent);
				videoParamsMap.put("title", title);
				videoParamsMap.put("description", description);
				
				//http请求
				HttpClientService HttpClientService = new HttpClientService(uploadvideoUrl,"POST",videoParamsMap,"UTF-8");
				String returnStr2 = HttpClientService.sendRequest();
				jacksonUtil.json2Map(returnStr2);
				
				if(jacksonUtil.containsKey("media_id")){
					mediaIdOrContent = jacksonUtil.getString("media_id");
				}else{
					throw new TzException("上传视频失败");
				}
			}
			
			Map<String, Object> contentMap = new HashMap<String, Object>();
			if("text".equals(msgtype)){
				contentMap.put("content", mediaIdOrContent);
			}else if("wxcard".equals(msgtype)){
				contentMap.put("card_id", mediaIdOrContent);
			}else{
				contentMap.put("media_id", mediaIdOrContent);
			}
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("msgtype", msgtype);
			paramsMap.put("touser", openIds);
			paramsMap.put(msgtype, contentMap);
			if("mpnews".equals(msgtype)){
				paramsMap.put("send_ignore_reprint", 1);
			}

			String msssSendByOpenidUrl = tz_msssSendByOpenid_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(msssSendByOpenidUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	
	/**
	 * 查询群发消息发送状态
	 * @param access_token
	 * @param msg_id						//群发消息后返回的消息id
	 * @return
	    {
		     "msg_id":201053012,	
		     "msg_status":"SEND_SUCCESS"	//消息发送后的状态，SEND_SUCCESS表示发送成功
		}
	 * @throws TzException
	 */
	public static String getMassSendStatus(String access_token, String msg_id) throws TzException{
		String returnStr = "";
		try{
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("msg_id", msg_id);
			
			String getSendStatusUrl = tz_getSendStatus_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(getSendStatusUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
}
