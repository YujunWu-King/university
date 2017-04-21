package com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 使用Xml生成word（面试）
 * 
 * @author 宋子成
 */
@Service("com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.MsXmlToWord")
public class MsXmlToWord {

	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	public MsXmlToWord() {

	}

	/*
	 * 返回值为 1：参数不全
	 */
	public String createWord(String TZ_CLASS_ID, String TZ_APPLY_PC_ID, String TZ_PWEI_OPRIDS)
			throws TzSystemException {

		// 检查参数的合法性
		if (StringUtils.isBlank(TZ_CLASS_ID) || StringUtils.isBlank(TZ_APPLY_PC_ID)
				|| StringUtils.isBlank(TZ_PWEI_OPRIDS)) {
			return "1";
		} else {
			
			String dq_oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			String Filepath = getSysHardCodeVal.getDownloadPath() + "/pydata/mspydata/"+dq_oprid+"/"
					+ new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/";
			
			//用于返回值
			String FilepathandName = request.getContextPath()+Filepath;
			Filepath = request.getServletContext().getRealPath(Filepath);

			// 检测文件夹是否存在，如果不存在，创建文件夹
			File dir = new File(Filepath);

			if (!dir.exists()) {
				// System.out.println("路径不存在");
				dir.mkdirs();
			} else {
				// System.out.println("路径存在");
			}

			// 添加"/" 后缀
			if (!StringUtils.endsWith(Filepath, "/")) {
				Filepath = Filepath + "/";
			}

			// 定义文件名称
			String fileName = "dc_pysj_" + new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()) + ".doc";
			FilepathandName = FilepathandName+fileName;
			// 拼装全路径,包含文件名
			String url = Filepath + fileName;
			
			// 调用方法，获取拼装的字符串
			String html = null;
			html = this.getHtmlStr(TZ_CLASS_ID, TZ_APPLY_PC_ID, TZ_PWEI_OPRIDS);

			// 返回值为 "1" 时为参数不全
			if ("1".equals(html)) {
				return "1";
			}

			// 定义读写器
			SAXReader saxReader = new SAXReader();
			// 定义空文档
			Document document = null;

			// 读字符串到document
			try {
				document = (Document) saxReader.read(new ByteArrayInputStream(html.getBytes("UTF-8")));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			OutputFormat format = OutputFormat.createPrettyPrint();
			/** 将document中的内容写入文件中 */
			XMLWriter writer = null;

			try {
				writer = new XMLWriter(new FileWriter(url), format);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				writer.write(document);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return FilepathandName;
		}
	}

	/**
	 * @param TZ_CLASS_ID
	 *            报考班级
	 * @param TZ_APPLY_PC_ID
	 *            报考批次
	 * @param TZ_PWEI_OPRIDS
	 *            需要打印的pw账号拼装的字符串，用“#”分隔
	 * @return 返回 拼装的 xml 字符串 
	 * 返回值：1、 参数不全
	 * @throws TzSystemException
	 */
	public String getHtmlStr(String TZ_CLASS_ID, String TZ_APPLY_PC_ID, String TZ_PWEI_OPRIDS)
			throws TzSystemException {

		// 用于返回的字符串;
		String html = "";

		if (StringUtils.isBlank(TZ_CLASS_ID) || StringUtils.isBlank(TZ_APPLY_PC_ID)
				|| StringUtils.isBlank(TZ_PWEI_OPRIDS)) {
			// 如果参数不全，返回空串;
			return "1";

		} else {

			// 获取班级批次信息;
			String TZ_CLASS_SQL = "SELECT TZ_CLASS_NAME,TZ_JG_ID,TZ_MSCJ_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID =?";
			Map<String, Object> TZ_CLASS_MAP = jdbcTemplate.queryForMap(TZ_CLASS_SQL, new Object[] { TZ_CLASS_ID });

			String TZ_CLASS_NAME = "";

			if (TZ_CLASS_MAP == null) {
			} else {
				TZ_CLASS_NAME = ((String) TZ_CLASS_MAP.get("TZ_CLASS_NAME") != null) ? (String) TZ_CLASS_MAP.get("TZ_CLASS_NAME") : "";
			}

			//获取批次名称
			String TZ_CLS_BATCH_SQL = "select TZ_BATCH_NAME from PS_TZ_CLS_BATCH_T where TZ_CLASS_ID = ? and TZ_BATCH_ID = ?";
			String 	TZ_BATCH_NAME = jdbcTemplate.queryForObject(TZ_CLS_BATCH_SQL, new Object[] { TZ_CLASS_ID,TZ_APPLY_PC_ID},"String");
			
			
			//拼装班级批次信息
			String class_pc = TZ_CLASS_NAME + "-" +((TZ_BATCH_NAME!=null)?TZ_BATCH_NAME:"");

			// 获取机构id;
			String TZ_JG_ID = "";
			// 根据班级获取机构 ，不再取当前机构 TZ_JG_ID =
			// tzLoginServiceImpl.getLoginedManagerOrgid(request);
			TZ_JG_ID = ((String) TZ_CLASS_MAP.get("TZ_JG_ID") != null) ? (String) TZ_CLASS_MAP.get("TZ_JG_ID") : "";

			// 获取面试评审成绩模型ID ;
			String TZ_MSCJ_SCOR_MD_ID = "";
			TZ_MSCJ_SCOR_MD_ID = ((String) TZ_CLASS_MAP.get("TZ_MSCJ_SCOR_MD_ID") != null) ? (String) TZ_CLASS_MAP.get("TZ_MSCJ_SCOR_MD_ID") : "";
			// 成绩模型树名称;
			String TREE_NAME_SQL = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_JG_ID = ? AND TZ_SCORE_MODAL_ID = ?";
			Map<String, Object> TREE_NAME_MAP = jdbcTemplate.queryForMap(TREE_NAME_SQL,
					new Object[] { TZ_JG_ID, TZ_MSCJ_SCOR_MD_ID });
			String TREE_NAME = "";
			if (TREE_NAME_MAP != null) {
				TREE_NAME = TREE_NAME_MAP.get("TREE_NAME") == null ? ""
						: String.valueOf(TREE_NAME_MAP.get("TREE_NAME"));
			}

			// 成绩扁平化;
			String tz_cj_bph_sql = "select TZ_SCORE_ITEM_ID,TZ_XS_MC from PS_TZ_CJ_BPH_TBL where TZ_JG_ID =? and TZ_SCORE_MODAL_ID= ?  and TZ_ITEM_S_TYPE = 'B' order by TZ_PX";
			List<Map<String, Object>> tz_cjbph_list = jdbcTemplate.queryForList(tz_cj_bph_sql,
					new Object[] { TZ_JG_ID, TZ_MSCJ_SCOR_MD_ID });
			// 扁平化每列的宽度
			int bph_lk = 11950;
			if(tz_cjbph_list.size()==0){
				//处理成绩扁平化没配置报错的问题;
			}else{
				 bph_lk = 11950 / tz_cjbph_list.size();	
			}

			/*
			 * System.out.println("----------"+bph_lk); for (Object cjbphObj :
			 * tz_cjbph_list) {
			 * 
			 * Map<String, Object> cjbphResult = (Map<String, Object>) cjbphObj;
			 * String TZ_SCORE_ITEM_ID = cjbphResult.get("TZ_SCORE_ITEM_ID") ==
			 * null ? "" : String.valueOf(cjbphResult.get("TZ_SCORE_ITEM_ID"));
			 * String TZ_XS_MC = cjbphResult.get("TZ_XS_MC") == null ? "" :
			 * String.valueOf(cjbphResult.get("TZ_XS_MC")); }
			 */

			// 获取当前日期
			String DQDATE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			// System.out.println("DQDATE=" + DQDATE);

			// 用于拼装所有的评委串;
			String html_pws = "";
			// 用于拼装单个评委串;
			String html_pw = "";

			// 评委grid 总的字符串;
			String html_pwgrids = "";
			// 循环评委功能;
			// 用于取评委组名称的sql
			String TZ_PWZ_NAME_SQL = "";
			// 存放查询的评委组对象
			Map<String, Object> TZ_PWZ_NAME_MAP = null;
			// 评委组名称
			String TZ_CLPS_GR_NAME = "";
			String[] arr = TZ_PWEI_OPRIDS.split("=");

			int pw_num = arr.length;

			for (int i = 0; i < arr.length; i++) {

				// 单个评委串
				html_pw = "";
				// 每次循环时先清空;
				html_pwgrids = "";

				// 获取评委组名称
				TZ_PWZ_NAME_SQL = "SELECT (SELECT TZ_CLPS_GR_NAME FROM PS_TZ_MSPS_GR_TBL WHERE TZ_CLPS_GR_ID = CP.TZ_PWEI_GRPID AND TZ_JG_ID=?) TZ_CLPS_GR_NAME FROM PS_TZ_MSPS_PW_TBL CP WHERE CP.TZ_CLASS_ID =? AND CP.TZ_APPLY_PC_ID =? AND CP.TZ_PWEI_OPRID=?";

				TZ_PWZ_NAME_MAP = jdbcTemplate.queryForMap(TZ_PWZ_NAME_SQL,
						new Object[] { TZ_JG_ID, TZ_CLASS_ID, TZ_APPLY_PC_ID, arr[i] });

				if (TZ_PWZ_NAME_MAP == null) {

					TZ_CLPS_GR_NAME = "";
				} else {
					TZ_CLPS_GR_NAME = TZ_PWZ_NAME_MAP.get("TZ_CLPS_GR_NAME") == null ? ""
							: String.valueOf(TZ_PWZ_NAME_MAP.get("TZ_CLPS_GR_NAME"));
				}
				
				// 处理每个评委考生的面试序号;
				String pw_ks_xh_sql = "select TZ_APP_INS_ID from PS_TZ_MP_PW_KS_TBL WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_PWEI_OPRID = ?";
				List<Map<String, Object>> pw_ks_xh_list = jdbcTemplate.queryForList(pw_ks_xh_sql,new Object[] {TZ_CLASS_ID,TZ_APPLY_PC_ID,arr[i]});
				Map<String, Object> pw_ks_xh_map =new HashMap();
				
				for(int k = 0;k<pw_ks_xh_list.size();k++){
					//System.out.println("TZ_APP_INS_ID="+TZ_APP_INS_ID);
					pw_ks_xh_map.put(pw_ks_xh_list.get(k).get("TZ_APP_INS_ID").toString(),k+1);
				}
				
				// 1、获取班级批次、评委信息;
				
				//评委账号修改为评委的登录账号2017-4-21;
				String TZ_PW_ZH_SQL = "";
				TZ_PW_ZH_SQL = "select TZ_DLZH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID =? and OPRID =?";
				// 存放查询的评委账号对象
				Map<String, Object> TZ_PW_ZH_MAP = null;
				TZ_PW_ZH_MAP = jdbcTemplate.queryForMap(TZ_PW_ZH_SQL,
						new Object[] { TZ_JG_ID,arr[i] });
				
				String TZ_PW_ZH = "";
				if (TZ_PW_ZH_MAP == null) {
					TZ_PW_ZH = "";
				} else {
					TZ_PW_ZH = TZ_PW_ZH_MAP.get("TZ_DLZH_ID") == null ? ""
							: String.valueOf(TZ_PW_ZH_MAP.get("TZ_DLZH_ID"));
				}
				//String[] pcpwarr = { class_pc, DQDATE, TZ_CLPS_GR_NAME, arr[i] };
				String[] pcpwarr = { class_pc, DQDATE, TZ_CLPS_GR_NAME, TZ_PW_ZH};
				
				String pc_pw_html = tzGDObject
						.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_PCINFO_HTML", pcpwarr);
				// 2、获取班级批次、评委信息下面的空行;
				String kh_html = tzGDObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_KH_HTML");

				// 3 、拼装grid
				// 3、1、获取grid 列头 的列宽度组 （面试序号、面试申请号、姓名、排名）-固定项;
				String pw_msxh_lk_html = tzGDObject
						.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_LK_HTML", "1000");
				String pw_mssqh_lk_html = tzGDObject
						.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_LK_HTML", "1242");
				String pw_name_lk_html = tzGDObject
						.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_LK_HTML", "993");
				String pw_pm_lk_html = tzGDObject
						.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_LK_HTML", "708");
				// 获取grid 列头 的列宽度组 （动态扁平化）;;
				String dt_bph_lk_html = "";
				for (int j = 0; j < tz_cjbph_list.size(); j++) {
					dt_bph_lk_html = dt_bph_lk_html + tzGDObject.getHTMLText(
							"HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_LK_HTML", bph_lk + "");
				}
				//列宽由：面试序号+面试申请号+姓名+排名+动态项 组成
				String grid_head_lks = pw_msxh_lk_html + pw_mssqh_lk_html + pw_name_lk_html + pw_pm_lk_html + dt_bph_lk_html;

				// 3、2、获取grid 列头 （面试序号、面试申请号、姓名、排名）-固定项;
				String pw_msxh_html = tzGDObject
						.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_TC_HTML", "面试序号");
				String pw_mssqh_html = tzGDObject
						.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_TC_HTML", "面试申请号");
				String pw_name_html = tzGDObject
						.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_TC_HTML", "姓名");
				String pw_pm_html = tzGDObject
						.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_TC_HTML", "排名");
				// 获取grid 列头 （动态添加列头处）;
				String dt_bph_html = "";
				for (Object cjbphObj : tz_cjbph_list) {
					Map<String, Object> cjbphResult = (Map<String, Object>) cjbphObj;
					String TZ_XS_MC = cjbphResult.get("TZ_XS_MC") == null ? ""
							: String.valueOf(cjbphResult.get("TZ_XS_MC"));
					dt_bph_html = dt_bph_html + tzGDObject.getHTMLText(
							"HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_TC_HTML", TZ_XS_MC);
				}
				//列：面试序号+面试申请号+姓名+排名+动态项 组成
				String grid_head_tcs = pw_msxh_html + pw_mssqh_html + pw_name_html + pw_pm_html + dt_bph_html;
				String grid_head_tr = tzGDObject.getHTMLText(
						"HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_TR_HTML", grid_head_tcs);

				// 动态读取评委考生数据 - 开始;

				// 循环评委考生;
				String pw_ks_sql = "SELECT TZ_APP_INS_ID,TZ_SCORE_INS_ID,TZ_KSH_PSPM FROM PS_TZ_MP_PW_KS_TBL where TZ_CLASS_ID =? AND TZ_APPLY_PC_ID = ? AND TZ_PWEI_OPRID=? order by TZ_KSH_PSPM";
				List<Map<String, Object>> pw_ks_list = jdbcTemplate.queryForList(pw_ks_sql,
						new Object[] { TZ_CLASS_ID, TZ_APPLY_PC_ID, arr[i] });

				// 记录当前评委的所有考生;
				String grid_pwks_trs = "";
				// 记录当前评委的单个考生;
				String grid_pwks_tr = "";

				for (Object cjpwksObj : pw_ks_list) {

					// 单个考生的字符串每次使用前清空
					grid_pwks_tr = "";

					Map<String, Object> cjpwksResult = (Map<String, Object>) cjpwksObj;
					String TZ_APP_INS_ID = cjpwksResult.get("TZ_APP_INS_ID") == null ? ""
							: String.valueOf(cjpwksResult.get("TZ_APP_INS_ID"));
					String TZ_SCORE_INS_ID = cjpwksResult.get("TZ_SCORE_INS_ID") == null ? ""
							: String.valueOf(cjpwksResult.get("TZ_SCORE_INS_ID"));
					String TZ_KSH_PSPM = cjpwksResult.get("TZ_KSH_PSPM") == null ? ""
							: String.valueOf(cjpwksResult.get("TZ_KSH_PSPM"));

					// a、获取grid 的列 （面试申请号、姓名、排名）-固定项;
					//取得OPRID ;
					String ksName = "";//考生姓名
					String ksMssqh = "";//考生面试申请号
					String OPRID = "";
					String OPRID_SQL = "SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? AND TZ_APP_INS_ID = ?";
					OPRID= jdbcTemplate.queryForObject(OPRID_SQL, new Object[]{TZ_CLASS_ID,TZ_APP_INS_ID},"String");
					
					//取得姓名、面试申请号
					String Name_Mssqh_SQL = "";
					Name_Mssqh_SQL = "SELECT TZ_REALNAME,TZ_MSH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID = ? AND OPRID = ?";
					Map<String, Object> Name_Mssqh_MAP = null;
					Name_Mssqh_MAP = jdbcTemplate.queryForMap(Name_Mssqh_SQL,new Object[] { TZ_JG_ID, OPRID});
					
					if (Name_Mssqh_MAP == null) {
					} else {
						ksName = Name_Mssqh_MAP.get("TZ_REALNAME") == null ? "": String.valueOf(Name_Mssqh_MAP.get("TZ_REALNAME"));
						ksMssqh = Name_Mssqh_MAP.get("TZ_MSH_ID") == null ? "": String.valueOf(Name_Mssqh_MAP.get("TZ_MSH_ID"));
					}
					//面试序号
					String pw_ks_xh_str = pw_ks_xh_map.get(TZ_APP_INS_ID).toString();
					String pw_ks_msxh_html = tzGDObject.getHTMLText(
							"HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_TC_HTML", pw_ks_xh_str);	
					//面试申请号
					String pw_ks_mssqh_html = tzGDObject.getHTMLText(
							"HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_TC_HTML", ksMssqh);
					//姓名
					String pw_ks_name_html = tzGDObject.getHTMLText(
							"HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_TC_HTML", ksName);
					//排名
					String pw_ks_pm_html = tzGDObject.getHTMLText(
							"HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_TC_HTML", TZ_KSH_PSPM);

					// b、获取gird的动态项（扁平化配置的项）
					String pw_ks_bph_html = "";
					for (Object cjbphObj : tz_cjbph_list) {

						// pw_ks_bph_html = "";

						Map<String, Object> cjbphResult = (Map<String, Object>) cjbphObj;
						// 获取成绩项id
						String TZ_SCORE_ITEM_ID = cjbphResult.get("TZ_SCORE_ITEM_ID") == null ? ""
								: String.valueOf(cjbphResult.get("TZ_SCORE_ITEM_ID"));
						// 根据成绩项id ， 查询 成绩
						String KS_SCORE_SQL = "SELECT TZ_SCORE_NUM,TZ_SCORE_PY_VALUE FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID =?";

						Map<String, Object> KS_SCORE_MAP = jdbcTemplate.queryForMap(KS_SCORE_SQL,
								new Object[] { TZ_SCORE_INS_ID, TZ_SCORE_ITEM_ID });

						// 拼装某个考生的所有动态项
						if (KS_SCORE_MAP != null) {

							// 取得分数及评议数据

							String TZ_SCORE_NUM = KS_SCORE_MAP.get("TZ_SCORE_NUM") == null ? ""
									: String.valueOf(KS_SCORE_MAP.get("TZ_SCORE_NUM"));
							String TZ_SCORE_PY_VALUE = KS_SCORE_MAP.get("TZ_SCORE_PY_VALUE") == null ? ""
									: String.valueOf(KS_SCORE_MAP.get("TZ_SCORE_PY_VALUE"));

							// 查询成绩项类型;
							String SCORE_ITEM_TYPE_SQL = "SELECT TZ_SCORE_ITEM_TYPE FROM PS_TZ_MODAL_DT_TBL WHERE TZ_JG_ID = ? AND TREE_NAME = ? AND TZ_SCORE_ITEM_ID = ?";
							Map<String, Object> SCORE_ITEM_TYPE_MAP = jdbcTemplate.queryForMap(SCORE_ITEM_TYPE_SQL,
									new Object[] { TZ_JG_ID, TREE_NAME, TZ_MSCJ_SCOR_MD_ID });
							String TZ_SCORE_ITEM_TYPE = "";
							if (SCORE_ITEM_TYPE_MAP != null) {
								TZ_SCORE_ITEM_TYPE = SCORE_ITEM_TYPE_MAP.get("TZ_SCORE_ITEM_TYPE") == null ? ""
										: String.valueOf(SCORE_ITEM_TYPE_MAP.get("TZ_SCORE_ITEM_TYPE"));
							}
							// 评语项和打分项分别处理
							if (StringUtils.equals(TZ_SCORE_ITEM_TYPE, "C")) {
								// "C" 为评语项
								pw_ks_bph_html = pw_ks_bph_html + tzGDObject.getHTMLText(
										"HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_TC_HTML",
										TZ_SCORE_PY_VALUE);
							} else {
								pw_ks_bph_html = pw_ks_bph_html + tzGDObject.getHTMLText(
										"HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_TC_HTML",
										TZ_SCORE_NUM);
							}
						} else {
							pw_ks_bph_html = pw_ks_bph_html + tzGDObject.getHTMLText(
									"HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_TC_HTML", "-");
						}

					}

					// 此处需要添加动态列 （所有列：面试序号+面试申请号+姓名+排名+动态项）
					grid_pwks_tr = tzGDObject.getHTMLText(
							"HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_TR_HTML",
							pw_ks_msxh_html + pw_ks_mssqh_html + pw_ks_name_html + pw_ks_pm_html + pw_ks_bph_html);
					grid_pwks_trs = grid_pwks_trs + grid_pwks_tr;

				}

				// 动态读取评委考生数据 - 结束;

				// 拼装gird 的行（列表的头部和考生行）
				String grid_trs = grid_head_tr + grid_pwks_trs;

				// 4、拼装grid ;
				/**
				 * grid_head_lks :需要动态获取 grid_head_tr :需要动态获取
				 */
				html_pwgrids = tzGDObject.getHTMLText(
						"HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_PW_STULIST_HTML", grid_head_lks, grid_trs);

				// 拼装单个评委串（批次信息 + 空行 + 考生列表grid）
				html_pw = pc_pw_html + kh_html + html_pwgrids;

				// 如果一个评委完成，另起一页
				String html_fy = tzGDObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_FY_HTML");
				if (pw_num == 1) {
					// 只有一个评委时不需要加另起一页;
				} else {
					if (i == (arr.length - 1)) {
						// 最后一页不需要加另起一页;
					} else {
						html_pw = html_pw + html_fy;
					}
				}

				// 拼装多个评委
				html_pws = html_pws + html_pw;
			}

			// 获取拼装的字符串
			html = tzGDObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_MS_PY_HTML", html_pws);
		}

		return html;
	}

}
