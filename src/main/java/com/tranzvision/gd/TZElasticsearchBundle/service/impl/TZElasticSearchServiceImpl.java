package com.tranzvision.gd.TZElasticsearchBundle.service.impl;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.mapper.Mapper.Null;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.elasticSearch.ElasticManager;
import com.tranzvision.gd.util.httpclient.HttpClientService;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.wechart.PostJsonDataUtil;

@Service("com.tranzvision.gd.TZElasticsearchBundle.service.impl.TZElasticSearchServiceImpl")
public class TZElasticSearchServiceImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery sqlQuery;
	
	
	public String tzOther(String operateType,String strParams,String[] errMsg) {
		String strRet="";

		try {
			//创建索引
			if("tzCreateIndex".equals(operateType)) {
				strRet = createIndex(strParams,errMsg);
			}
			
			//查询数据
			if("tzQueryArticle".equals(operateType)) {
				strRet = queryArticle(strParams,errMsg);
			}
			
			//全文检索写数据PUT
			if("tzPutData".equals(operateType)) {
				strRet = putData(strParams,errMsg);
			}
			//全文检索获取数据GET
			if("tzGetData".equals(operateType)) {
				strRet = getData(strParams,errMsg);
			}
			//全文检索删除数据DELETE
			if("tzDeleteData".equals(operateType)) {
				strRet = deleteData(strParams,errMsg);
			}
			//全文检索查询数据POST
			if("tzQueryData".equals(operateType)) {
				strRet =queryData(strParams,errMsg);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	public String createIndex(String strParams,String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			String sql = "SELECT A.TZ_ART_ID,A.TZ_ART_TITLE,A.TZ_ART_CONENT,date_format(A.TZ_START_DATE,'%Y-%m-%d') TZ_START_DATE,date_format(A.TZ_START_TIME,'%H:%i') TZ_START_TIME,B.TZ_ART_URL";
			sql += " FROM PS_TZ_ART_REC_TBL A,PS_TZ_LM_NR_GL_T B";
			sql += " WHERE A.TZ_ART_ID=B.TZ_ART_ID AND B.TZ_SITE_ID ='72'";
			
			List<Map<String, Object>> listData = sqlQuery.queryForList(sql);
			
			for(Map<String, Object> mapData : listData) {
				String TZ_ART_ID = mapData.get("TZ_ART_ID") == null ? "" : mapData.get("TZ_ART_ID").toString();
				String TZ_ART_TITLE = mapData.get("TZ_ART_TITLE") == null ? "" : mapData.get("TZ_ART_TITLE").toString();
				String TZ_ART_CONENT = mapData.get("TZ_ART_CONENT") == null ? "" : mapData.get("TZ_ART_CONENT").toString();
				String TZ_START_DATE = mapData.get("TZ_START_DATE") == null ? "2017-09-22" : mapData.get("TZ_START_DATE").toString();
				String TZ_START_TIME = mapData.get("TZ_START_TIME") == null ? "09:00" : mapData.get("TZ_START_TIME").toString();
				String TZ_ART_URL = mapData.get("TZ_ART_URL") == null ? "" : mapData.get("TZ_ART_URL").toString();
				
				if(!"".equals(TZ_ART_ID)) {
					Map<String,Object> mapParams = new HashMap<String,Object>();
					mapParams.put("TZ_ART_ID", TZ_ART_ID);
					mapParams.put("TZ_ART_TITLE", TZ_ART_TITLE);
					mapParams.put("TZ_ART_CONENT", TZ_ART_CONENT);
					mapParams.put("TZ_START_DATE", TZ_START_DATE);
					mapParams.put("TZ_START_TIME", TZ_START_TIME);
					mapParams.put("TZ_ART_URL", TZ_ART_URL);
					
					String url = "http://202.120.24.114:9200/cms/mba/" + TZ_ART_ID;
					
					String strCreate = ElasticManager.createIndex(url, mapParams);
					
					mapRet.put(TZ_ART_ID, strCreate);
				}
				
			}
			
			strRet = jacksonUtil.Map2json(mapRet);
					
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	public String queryArticle(String strParams,String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			
			String strHtml = "";
			
			jacksonUtil.json2Map(strParams);
			String searchText = jacksonUtil.getString("searchText");
			
			String strSearch = ElasticManager.queryArticle(searchText);
			if(strSearch!=null && !"".equals(strSearch)) {
				
				jacksonUtil.json2Map(strSearch);
			
				Map<String, Object> mapHits = jacksonUtil.getMap("hits");
				
				if(mapHits!=null) {
			
					String total = mapHits.get("total") == null ? "0" : mapHits.get("total").toString();
			
					List<Map<String, Object>> listResult = (List<Map<String, Object>>) mapHits.get("hits");
			
					for(Map<String, Object> mapResult : listResult) {
						Map<String, Object> mapSource = (Map<String, Object>) mapResult.get("_source");
				
						if(mapSource!=null) {
							String TZ_ART_ID = mapSource.get("TZ_ART_ID") == null ? "" : mapSource.get("TZ_ART_ID").toString();
							String TZ_ART_TITLE = mapSource.get("TZ_ART_TITLE") == null ? "" : mapSource.get("TZ_ART_TITLE").toString();
							String TZ_ART_CONENT = mapSource.get("TZ_ART_CONENT") == null ? "" : mapSource.get("TZ_ART_CONENT").toString();
							String TZ_ART_URL = mapSource.get("TZ_ART_URL") == null ? "" : mapSource.get("TZ_ART_URL").toString();
							String TZ_START_DATE = mapSource.get("TZ_START_DATE") == null ? "" : mapSource.get("TZ_START_DATE").toString();
							String TZ_START_TIME = mapSource.get("TZ_START_TIME") == null ? "" : mapSource.get("TZ_START_TIME").toString();
						
							strHtml += "<div style='margin:10px'><a href='"+ TZ_ART_URL +"' target='_blank'>" + TZ_ART_TITLE + "</a></div>"; 
						}
					}
			
					mapRet.put("total", total);
					mapRet.put("html", strHtml);
			
					strRet = jacksonUtil.Map2json(mapRet);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	public String putData(String strParams,String[] errMsg) {
		String strRet = "";
	
		try {			
			strRet = ElasticManager.putMovie();			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	public String getData(String strParams,String[] errMsg) {
		String strRet = "";
	
		try {			
			strRet = ElasticManager.getMovie();			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	public String deleteData(String strParams,String[] errMsg) {
		String strRet = "";
	
		try {			
			strRet = ElasticManager.deleteMovie();			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	public String queryData(String strParams,String[] errMsg) {
		String strRet = "";
		
		try {			
			strRet = ElasticManager.queryMovie();	
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*
	public String callElasticsearch(String strParams,String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			
			client = transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

			//创建索引
			String indexname = "movies";
		    String type = "movie";
			IndexResponse response = client.prepareIndex(indexname, type)
			            .execute()
			            .actionGet();
			 
		     //查询条件
		     QueryBuilder queryBuilder = queryBuilders.matchQuery("year", 1962);
		     
		     List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		     SearchResponse searchResponse = client.prepareSearch(indexname).setTypes(type)
		    	        .setQuery(queryBuilder)
		    	        .execute()
		    	        .actionGet();
	        SearchHits hits = searchResponse.getHits();
	        System.out.println("查询到记录数=" + hits.getTotalHits());
	        SearchHit[] searchHists = hits.getHits();
	        if(searchHists.length>0){
	            for(SearchHit hit:searchHists){
	                String title = (String)hit.getSource().get("title");
	                String director =  (String) hit.getSource().get("director");
	                Integer year =  (Integer) hit.getSource().get("year");
	                String genres = (String) hit.getSource().get("genres");
	                Map<String, Object> mapData = new HashMap<String,Object>();
	                mapData.put("title", title);
	                mapData.put("director", director);
	                mapData.put("year", year);
	                mapData.put("genres", genres);
	                list.add(mapData);
	            }
	        }
		    	            
		    	            
	         for(Map<String, Object> mapRoot : list){
	            String title = mapRoot.get("title") == null ? "" : mapRoot.get("title").toString();
	            System.out.println(title);
	         }		
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString(); 
		}
		
		return strRet;
	}
	*/
}
