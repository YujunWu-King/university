package com.tranzvision.gd.TZBzScoreMathBundle.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

@Service("com.tranzvision.gd.TZBzScoreMathBundle.service.impl.TzBzScoreMathBasetoListServiceImpl")
public class TzBzScoreMathBasetoListServiceImpl extends FrameworkImpl {

	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();

		mapRet.put("total", 0);
		// 获取当前登陆人机构ID
		// String Orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();

		ArrayList<String[]> list = new ArrayList<String[]>();
		ArrayList<String[]> list_pw = new ArrayList<String[]>();
		jacksonUtil.json2Map(comParams);

		String[] actData = null;
		// 操作数据;
		// JSONArray jsonArray = null;
		List<Map<String, Object>> jsonArray = null;
		int num1 = 0;
		System.out.println(jacksonUtil.containsKey("add"));
		if (jacksonUtil.containsKey("add")) {
			jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("add");
			// System.out.println(jacksonUtil.Map2json(jsonArray));
			if (jsonArray != null && jsonArray.size() > 0) {
				actData = new String[jsonArray.size()];
				for (num1 = 0; num1 < jsonArray.size(); num1++) {
					actData[num1] = jacksonUtil.Map2json(jsonArray.get(num1));
					System.out.println(actData[num1]);
				}

			}
		}

		try {
			int num = 0;
			String xmid = "";
			String xmName = "";
			String teamID = "";
			String judgeUser = "";
			String rawscore = "";
			String judgescore = "";
			String fg = ",";
			String key = "";
			String value = "";
			String value1 = "";
			String key_pw = "";
			String value_pw = "";
			String value_pw_1 = "";
			String find_key = "";
			Map<String, String> map = new HashMap<String, String>();
			Map<String, String> map_pw = new HashMap<String, String>();
			String[] aa = null;
			String[] bb = null;
			int i = 0;
			int i_two = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strForm = actData[num];
				System.out.println(strForm);
				jacksonUtil.json2Map(strForm);
				// 主题 ID;

				xmid = jacksonUtil.getString("xmid");
				xmName = jacksonUtil.getString("xmName");
				teamID = jacksonUtil.getString("teamID");
				judgeUser = jacksonUtil.getString("judgeUser");
				rawscore = jacksonUtil.getString("rawscore");
				judgescore = jacksonUtil.getString("judgescore");
				// 合并同一个学生的信息成一列
				key = xmid + fg + xmName + fg + teamID;
				value = xmid + fg + xmName + fg + teamID + fg + judgeUser + fg + rawscore + fg + judgescore;
				value1 = judgeUser + fg + rawscore + fg + judgescore;
				if (map.get(key) == null) {
					map.put(key, value);
				} else {
					value = map.get(key).toString() + fg + value1;
					map.replace(key, value);
				}
				// 合并某一评委的评分信息
				key_pw = teamID + fg + judgeUser;
				value_pw = "9999999999999" + fg + rawscore;
				value_pw_1 = rawscore;
				if (map_pw.get(key_pw) == null) {
					map_pw.put(key_pw, value_pw);

				} else {
					map_pw.replace(key_pw, map_pw.get(key_pw).toString() + fg + value_pw_1);

				}

			}
			for (Map.Entry<String, String> entry : map.entrySet()) {
				aa = entry.getValue().split(",");
				list.add(aa);
				// System.out.println("key= " + entry.getKey() + " and value= "
				// + entry.getValue());
			}
			for (Map.Entry<String, String> entry : map_pw.entrySet()) {
				bb = entry.getValue().split(",");
				list_pw.add(bb);
				// System.out.println("key= " + entry.getKey() + " and value= "
				// + entry.getValue());

			}
			for (i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				String JgID = rowList[0];

				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("xmid", rowList[0]);
				mapList.put("xmName", rowList[1]);
				mapList.put("teamID", rowList[2]);
				//
				for (i_two = 1; i_two <= (rowList.length - 3); i_two++) {
					System.out.println(3 * i_two + ";" + rowList.length);

					if (3 * i_two >= rowList.length) {
						mapList.put("judge_" + String.valueOf(i_two) + "_id", null);

					} else {
						mapList.put("judge_" + String.valueOf(i_two) + "_id", rowList[3 * i_two]);
					}
					if ((3 * i_two + 1) >= rowList.length) {
						mapList.put("judge_" + String.valueOf(i_two) + "_score", null);
						mapList.put("judge_" + String.valueOf(i_two) + "_rank", null);
						mapList.put("judge_" + String.valueOf(i_two) + "_num", null);
					} else {
						mapList.put("judge_" + String.valueOf(i_two) + "_score", rowList[3 * i_two + 1]);
						find_key = rowList[2] + fg + rowList[3 * i_two];
						bb = map_pw.get(find_key).split(",");
						mapList.put("judge_" + String.valueOf(i_two) + "_rank",
								this.checkpeRank(bb, rowList[3 * i_two + 1]));
						mapList.put("judge_" + String.valueOf(i_two) + "_num", String.valueOf(bb.length - 1));
					}

				}

				listData.add(mapList);

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
			// TODO: handle exception
		}
		mapRet.replace("total", 0);
		mapRet.replace("root", listData);
		return jacksonUtil.Map2json(mapRet);

	}

	/***
	 * 对字符串数组进行转换再排名 返回 排名
	 * 
	 * @param arrs
	 * @param score
	 * @return
	 */
	public String checkpeRank(String[] arrs, String score) {
		float[] ints = new float[arrs.length];
		for (int i = 0; i < arrs.length; i++) {
			ints[i] = Float.parseFloat(arrs[i]);
		}
		Arrays.sort(ints);
		int j = 2;
		while (!Float.valueOf(score).equals(ints[ints.length - j])) {
			j++;
		}
		return String.valueOf(j - 1);

	}

}
