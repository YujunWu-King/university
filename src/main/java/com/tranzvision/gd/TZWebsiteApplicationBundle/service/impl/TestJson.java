package com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * 用于测试获取到的报名表JSON的解析
 * 
 * @author caoy
 *
 */
public class TestJson {

	public void Detail(String json, Long numAppInsId) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		// jacksonUtil.json2Map(strJsonData);

		Map<String, Object> mapAppData = jacksonUtil.parseJson2Map(json);

		if (mapAppData != null) {
			// this.delAppIns(numAppInsId);
			for (Entry<String, Object> entry : mapAppData.entrySet()) {

				// 解析第一层
				Map<String, Object> mapJsonItems = (Map<String, Object>) entry.getValue();
				String strClassName = "";
				if (mapJsonItems.containsKey("classname")) {
					strClassName = String.valueOf(mapJsonItems.get("classname"));
				}
				String strIsDoubleLine = "";
				if (mapJsonItems.containsKey("isDoubleLine")) {
					strIsDoubleLine = String.valueOf(mapJsonItems.get("isDoubleLine"));
				}
				String strIsSingleLine = "";
				if (mapJsonItems.containsKey("isSingleLine")) {
					strIsSingleLine = String.valueOf(mapJsonItems.get("isSingleLine"));
				}
				String strOthervalue = "";
				if (mapJsonItems.containsKey("othervalue")) {
					strOthervalue = String.valueOf(mapJsonItems.get("othervalue"));
				}
				String strItemIdLevel0 = "";
				if (mapJsonItems.containsKey("itemId")) {
					strItemIdLevel0 = String.valueOf(mapJsonItems.get("itemId"));
				}
				if (mapJsonItems.containsKey("children")) {

					// 分组框单独处理
					// if (strClassName.equals("LayoutControls")) {
					// List<?> mapChildrens1 = (ArrayList<?>)
					// mapJsonItems.get("children");
					// Map<String, Object> mapChildren1 = (Map<String, Object>)
					// mapChildrens1.get(0);
					// System.out.println("分组框children大小：" +
					// mapChildren1.size());
					// } else {
					List<Map<String, Object>> mapChildrens1 = (ArrayList<Map<String, Object>>) mapJsonItems
							.get("children");
					if ("Y".equals(strIsDoubleLine)) {
						if (strClassName.equals("LayoutControls")) {
							this.saveDhLineNum(strItemIdLevel0, numAppInsId,
									(short) ((Map<String, Object>) mapChildrens1.get(0)).size());
						} else {
							this.saveDhLineNum(strItemIdLevel0, numAppInsId, (short) mapChildrens1.size());
						}
						for (Object children1 : mapChildrens1) {
							// 多行容器
							Map<String, Object> mapChildren1 = (Map<String, Object>) children1;

							for (Entry<String, Object> entryChildren : mapChildren1.entrySet()) {
								Map<String, Object> mapJsonChildrenItems = (Map<String, Object>) entryChildren
										.getValue();
								String strItemIdLevel1 = "";
								if (mapJsonChildrenItems.containsKey("itemId")) {
									strItemIdLevel1 = String.valueOf(mapJsonChildrenItems.get("itemId"));
								}
								if (strClassName.equals("LayoutControls")) {
									System.out.println("分组框childrenID：" + strItemIdLevel1);
								}

								if (mapJsonChildrenItems.containsKey("children")) {
									// 多行容器下的子容器 modity by caoy
									// 解决分组框的某些组合控件的问题
									//// //System.out.println("111:" +
									// mapJsonChildrenItems.get("children"));
									List<Map<String, Object>> mapChildrens2 = null;
									try {
										mapChildrens2 = (ArrayList<Map<String, Object>>) mapJsonChildrenItems
												.get("children");
									} catch (Exception e) {
										mapChildrens2 = new ArrayList<Map<String, Object>>();
										Map<String, Object> cmap = (Map<String, Object>) mapJsonChildrenItems
												.get("children");
										Map<String, Object> ccmap = null;
										for (String key : cmap.keySet()) {
											ccmap = (Map<String, Object>) cmap.get(key);
											mapChildrens2.add(ccmap);
										}
										// System.out.println("分组框children的children："
										// + mapChildrens2.size());
									}

									// ////System.out.println("Size:" +
									// mapChildrens2.size());

									String strIsSingleLine2 = "";
									if (mapJsonChildrenItems.containsKey("isSingleLine")) {
										strIsSingleLine2 = String.valueOf(mapJsonChildrenItems.get("isSingleLine"));
									}
									if ("Y".equals(strIsSingleLine2)) {
										// 多行容器中的单行容器
										for (Object children2 : mapChildrens2) {
											Map<String, Object> mapChildren2 = (Map<String, Object>) children2;
											this.savePerXxxIns(strItemIdLevel0 + strItemIdLevel1, mapChildren2,
													numAppInsId);
										}
									} else {
										// 多行容器中的附件
										String strStorageType = "";
										if (mapJsonChildrenItems.containsKey("StorageType")) {
											strStorageType = mapJsonChildrenItems.get("StorageType") == null ? ""
													: String.valueOf(mapJsonChildrenItems.get("StorageType"));
											if ("F".equals(strStorageType)) {
												for (Object children2 : mapChildrens2) {
													Map<String, Object> mapChildren2 = (Map<String, Object>) children2;
													this.savePerAttrInfo(strItemIdLevel0 + strItemIdLevel1,
															mapChildren2, numAppInsId);
													String strIsHidden = "";
													if (mapJsonChildrenItems.containsKey("isHidden")) {
														strIsHidden = mapJsonChildrenItems.get("isHidden") == null ? ""
																: String.valueOf(mapJsonChildrenItems.get("isHidden"));
													}
													this.saveXxxHidden(numAppInsId, strItemIdLevel0 + strItemIdLevel1,
															strIsHidden);
												}
											}
										}
									}
								} else {
									// 多行容器中的单选框.复选框、一般字段
									String strStorageType = "";
									strStorageType = mapJsonChildrenItems.get("StorageType") == null ? ""
											: String.valueOf(mapJsonChildrenItems.get("StorageType"));
									if ("S".equals(strStorageType) || "L".equals(strStorageType)) {
										// 多行容器中的普通字段
										this.savePerXxxIns(strItemIdLevel0, mapJsonChildrenItems, numAppInsId);
									} else if ("D".equals(strStorageType)) {
										// 单选框或者复选框
										if (mapJsonChildrenItems.containsKey("option")) {
											Map<String, Object> mapOptions = (Map<String, Object>) mapJsonChildrenItems
													.get("option");
											for (Entry<String, Object> entryOption : mapOptions.entrySet()) {
												Map<String, Object> mapOption = (Map<String, Object>) entryOption
														.getValue();
												this.savePerXxxIns2(strItemIdLevel0 + strItemIdLevel1, "", mapOption,
														numAppInsId);
											}
										}
									} else if ("F".equals(strStorageType)) {
										// 推荐信附件信息和其他固定容器附件
										if (!"recommendletter".equals(strClassName)) {
											this.savePerAttrInfo(strItemIdLevel0 + strItemIdLevel1,
													mapJsonChildrenItems, numAppInsId);
										}
									}
								}
							}
						}
					} else if ("Y".equals(strIsSingleLine)) {
						// 如果是单行容器
						for (Object children1 : mapChildrens1) {
							Map<String, Object> mapChildren1 = (Map<String, Object>) children1;
							String strStorageType = "";
							if (mapChildren1.containsKey("StorageType")) {
								strStorageType = mapChildren1.get("StorageType") == null ? ""
										: String.valueOf(mapChildren1.get("StorageType"));
							}
							if ("S".equals(strStorageType) || "L".equals(strStorageType)) {
								this.savePerXxxIns(strItemIdLevel0, mapChildren1, numAppInsId);
							}
						}
					} else {
						// 如果是附件信息
						String strStorageType = "";
						if (mapJsonItems.containsKey("StorageType")) {
							strStorageType = mapJsonItems.get("StorageType") == null ? ""
									: String.valueOf(mapJsonItems.get("StorageType"));
						}
						if ("F".equals(strStorageType)) {
							for (Object children1 : mapChildrens1) {
								Map<String, Object> mapChildren1 = (Map<String, Object>) children1;
								this.savePerAttrInfo(strItemIdLevel0, mapChildren1, numAppInsId);
								String strIsHidden = "";
								if (mapJsonItems.containsKey("isHidden")) {
									strIsHidden = mapJsonItems.get("isHidden") == null ? ""
											: String.valueOf(mapJsonItems.get("isHidden"));
								}
								this.saveXxxHidden(numAppInsId, strItemIdLevel0, strIsHidden);
							}
						}
					}
					// }
				} else {
					// 没有Children节点
					String strStorageType = "";
					if (mapJsonItems.containsKey("StorageType")) {
						strStorageType = mapJsonItems.get("StorageType") == null ? ""
								: String.valueOf(mapJsonItems.get("StorageType"));
					}
					if ("D".equals(strStorageType)) {
						// 如果是多项框或者单选框
						if (mapJsonItems.containsKey("option")) {
							Map<String, Object> mapOptions = (Map<String, Object>) mapJsonItems.get("option");
							for (Entry<String, Object> entryOption : mapOptions.entrySet()) {
								Map<String, Object> mapOption = (Map<String, Object>) entryOption.getValue();
								this.savePerXxxIns2(strItemIdLevel0, "", mapOption, numAppInsId);
							}
						}
					} else if ("S".equals(strStorageType) || "L".equals(strStorageType)) {
						this.savePerXxxIns("", mapJsonItems, numAppInsId);
						if ("bmrPhoto".equals(strClassName)) {
							// this.saveBmrPhoto("" , mapJsonItems,
							// numAppInsId);
						}
					}
				}
			}
		}

	}

	private void saveDhLineNum(String strItemId, Long numAppInsId, short numLineDh) {
		System.out.println("保存多行容器的行数信息：TzXxxBh=" + strItemId + "              ,TzXxxLine=" + numLineDh);
	}

	private void savePerXxxIns2(String strParentItemId, String strOtherValue, Map<String, Object> xxxObject,
			Long numAppInsId) {

		String strIsChecked = "";
		if (xxxObject.containsKey("checked")) {
			strIsChecked = xxxObject.get("checked") == null ? "" : String.valueOf(xxxObject.get("checked"));
		}
		if (!"Y".equals(strIsChecked)) {
			strIsChecked = "N";
		}
		String strCode = "";
		if (xxxObject.containsKey("code")) {
			strCode = xxxObject.get("code") == null ? "" : String.valueOf(xxxObject.get("code"));
		}
		String strTxt = "";
		if (xxxObject.containsKey("txt")) {
			strTxt = xxxObject.get("txt") == null ? "" : String.valueOf(xxxObject.get("txt"));
		}
		if (xxxObject.containsKey("othervalue")) {
			strOtherValue = xxxObject.get("othervalue") == null ? "" : String.valueOf(xxxObject.get("othervalue"));
		}

		System.out.println("保存psTzAppDhccT信息：TzXxxBh=" + strParentItemId + "              ,TzIsChecked=" + strIsChecked
				+ "              ,TzXxxkxzMc=" + strCode + "              ,TzAppSText=" + strTxt
				+ "              ,TzKxxQtz=" + strOtherValue);

	}

	private void savePerXxxIns(String strParentItemId, Map<String, Object> xxxObject, Long numAppInsId) {

		String strItemId = "";
		if (xxxObject.containsKey("itemId")) {
			strItemId = String.valueOf(xxxObject.get("itemId"));
		}
		if (!"".equals(strParentItemId) && strParentItemId != null) {
			strItemId = strParentItemId + strItemId;
		}
		// 数据存储类型
		String strStorageType = "";
		// 存储值
		String strValueL = "";
		String strValueS = "";
		String strValue = "";
		// 控件类名称
		String strClassName = "";

		if (xxxObject.containsKey("StorageType")) {
			strStorageType = xxxObject.get("StorageType") == null ? "" : String.valueOf(xxxObject.get("StorageType"));
			if ("L".equals(strStorageType)) {
				strValueL = xxxObject.get("value") == null ? "" : String.valueOf(xxxObject.get("value"));
			} else {
				strValueS = xxxObject.get("value") == null ? "" : String.valueOf(xxxObject.get("value"));
				strValueL = xxxObject.get("wzsm") == null ? "" : String.valueOf(xxxObject.get("wzsm"));
			}
		}
		// 如果是推荐信title Start
		String sql = "";
		if (xxxObject.containsKey("classname")) {
			strClassName = xxxObject.get("classname") == null ? "" : String.valueOf(xxxObject.get("classname"));
			if ("RefferTitle".equals(strClassName)) {
			}
		}

		if (strValueS.length() > 254) {
			strValueS = strValueS.substring(0, 254);
		}

		System.out.println("保存psTzAppCcT信息：TzXxxBh=" + strItemId + "              ,TzAppSText=" + strValueS
				+ "              ,TzAppLText=" + strValueL);

		// 是否隐藏
		String strIsHidden = "";
		if (xxxObject.containsKey("isHidden")) {
			strIsHidden = xxxObject.get("isHidden") == null ? "" : String.valueOf(xxxObject.get("isHidden"));
			if ("".equals(strIsHidden)) {
				strIsHidden = "N";
			}
		} else {
			strIsHidden = "N";
		}
		this.saveXxxHidden(numAppInsId, strItemId, strIsHidden);
	}

	private void saveXxxHidden(Long numAppInsId, String strItemId, String strIsHidden) {
		System.out.println("保存psTzAppHiddenT信息：TzXxxBh=" + strItemId + "              ,TzIsHidden=" + strIsHidden);
	}

	private void savePerAttrInfo(String strParentItemId, Map<String, Object> xxxObject, Long numAppInsId) {

		String strSysFileName = "";
		if (xxxObject.containsKey("sysFileName")) {
			strSysFileName = xxxObject.get("sysFileName") == null ? "" : String.valueOf(xxxObject.get("sysFileName"));
		}
		String strUseFileName = "";
		if (xxxObject.containsKey("fileName")) {
			strUseFileName = xxxObject.get("fileName") == null ? "" : String.valueOf(xxxObject.get("fileName"));
		} else {
			if (xxxObject.containsKey("filename")) {
				strUseFileName = xxxObject.get("filename") == null ? "" : String.valueOf(xxxObject.get("filename"));
			}
		}
		String strOrderBy = "";
		if (xxxObject.containsKey("orderby")) {
			strOrderBy = xxxObject.get("orderby") == null ? "" : String.valueOf(xxxObject.get("orderby"));
		}
		int numOrderBy = 0;
		if ("".equals(strOrderBy) || strOrderBy == null) {
			numOrderBy = 0;
		} else {
			numOrderBy = Integer.parseInt(strOrderBy);
		}

		String strPath = "";
		if (xxxObject.containsKey("accessPath")) {
			strPath = xxxObject.get("accessPath") == null ? "" : String.valueOf(xxxObject.get("accessPath"));
		}

		System.out.println("保存PsTzFormAttT信息：TzXxxBh=" + strParentItemId + "              ,TzIndex=" + numOrderBy
				+ "              ,TzAccessPath=" + strPath + "              ,Attachsysfilename=" + strSysFileName
				+ "              ,Attachuserfile=" + strUseFileName);
	}

	public String readTxtFile(String filePath) {
		String s = "";
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// System.out.println(lineTxt);
					s = s + lineTxt;
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return s;
	}

	public void reinfo(String json) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> mapAppData = jacksonUtil.parseJson2Map(json);

		if (mapAppData != null) {
			Map<String, Object> mapJsonItems = null;
			List<String> itemName = new ArrayList<String>();

			for (Entry<String, Object> entry : mapAppData.entrySet()) {
				mapJsonItems = (Map<String, Object>) entry.getValue();
				String strClassName = "";
				if (mapJsonItems.containsKey("classname")) {
					strClassName = String.valueOf(mapJsonItems.get("classname"));
				}
				if (strClassName.equals("recommendInfo")) {
					if (mapJsonItems.containsKey("children")) {

						List<Map<String, Object>> mapChildrens = (ArrayList<Map<String, Object>>) mapJsonItems
								.get("children");

						Map<String, Object> cmap = (Map<String, Object>) mapChildrens.get(0);
						// System.out.println(cmap.toString());
						Map<String, Object> ccmap = null;

						List<Map<String, Object>> mapChildrens2 = new ArrayList<Map<String, Object>>();
						for (String key : cmap.keySet()) {
							ccmap = (Map<String, Object>) cmap.get(key);
							// System.out.println(ccmap.toString());
							mapChildrens2.add(ccmap);
						}

						for (Object children2 : mapChildrens2) {
							Map<String, Object> mapChildren2 = (Map<String, Object>) children2;
							String useby = String.valueOf(mapChildren2.get("useby"));
							System.out.println("useby:" + useby);
							if (useby.equals("Y")) {
								System.out.println("需要填写的项:" + String.valueOf(mapChildren2.get("itemId")));
								itemName.add(String.valueOf(mapChildren2.get("itemId")));
							}
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//TestJson ts = new TestJson();
		//String json = ts.readTxtFile("C:\\Users\\feifei\\Desktop\\json.json");
		//ts.Detail(json, new Long(9999));
		//String str = "123ABC456";
		//String strInsData =  "时间：2016年03月-至今 \n职位：大中国区翻新仪器业务经理\n工作职责：负责安捷伦大中国区官方翻新仪器业务\n工作业绩：FY16销售额$800K, FY17上半年$1.2M\n汇报关系：全球翻新仪器市场经理\n下属人数：0\n时间：2014年11月-2016年3月\n职位：政府项目经理\n工作职责：负责政府大项目招投标\n工作业绩：年销售额15M\n汇报关系：政府大客户团队经理\n下属人数：0\n时间：2009年5月-2014年11月\n职位：核磁共振销售，核磁共振销售经理，研究产品销售经理\n工作职责：负责核磁共振产品销售\n工作业绩：个人5-6M，团队20M。\n汇报关系：大中国区生命科学高级经理\n下属人数：4\n";
		String strInsData ="\\$$$\\\"!@#%^&*()+_-|/?";
		System.out.println(strInsData);
		System.out.println("--------------------------");
		Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
		Matcher mc = CRLF.matcher(strInsData);
		if (mc.find()) {
			strInsData = mc.replaceAll("\\\\n");
		}
		//strInsData = strInsData.replace("\\", "\\\\");
		//strInsData = strInsData.replace("$", "\\$");
		
		//System.out.println(strInsData);
		//System.out.println("--------------------------");
		//if (strInsData.contains("\\")) {
			// val = val.replace("\\", "\\\\");
		//}
		//if (strInsData.contains("$")) {
		strInsData = java.util.regex.Matcher.quoteReplacement(strInsData);
			// val = val.replace("$", "\\$");
		//}

		System.out.println(strInsData);
		System.out.println("--------------------------");
		strInsData = java.util.regex.Matcher.quoteReplacement(strInsData);
		System.out.println(strInsData);
		System.out.println("--------------------------");
//		JacksonUtil jacksonUtil = new JacksonUtil();
//		String val = "ABC\\%$\"{}[]=:";
//		Map<String, Object> jsonObject = new HashMap<String, Object>();
//		jsonObject.put("A", val);
//		val = jacksonUtil.Map2json(jsonObject);
//		System.out.println(val);
//		val = val.substring(6, val.length() - 2);
//		System.out.println(val);

		 //String strValue="!2";
		 //boolean isMatch = strValue.matches("^[0-9a-zA-Z\\-]*$");

		 //System.out.println(isMatch);

	}

}
