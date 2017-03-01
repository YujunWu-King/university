/**
 * 搜索考生panel
 * @param jsonObject
 * @param isFromDfPanel
 * @returns
 */
function createMainPageSearchKSPanel(jsonObject, isFromDfPanel){
	var showThisPanelHeader = true;
	if(isFromDfPanel!="undefined" && isFromDfPanel!=null) showThisPanelHeader = false;
	
	var MainPageSearchKSPanel = Ext.create("Ext.FormPanel",{
					title		: '搜索考生',
					header		: showThisPanelHeader,
					collapsible	: true,
					hideCollapseTool: false,
					overflowY	: 'hidden',
					//margins		: '5 0 5 5',
					bodyPadding	: 10,
					width		: 1058, 
					height		: 150,
					layout		: 'form',
					items		: [
									{
										xtype: "fieldcontainer",
										fieldLabel		: '搜索操作',
										hideLabel		: true,
										combineErrors	: false,
										//msgTarget		: 'side',
										layout			: 'hbox',
										height			: 30,
										defaults		: {hideLabel: false},
										items			: [
															{
																xtype     	: 'textfield',
																name      	: 'KSH_SEARCH_MSID',
																fieldLabel	: '请输入考生申请号',
																labelWidth	: 110,
																value		: '',
																enableKeyEvents	: true,
																margin		: '0 30 0 0',
																allowBlank	: true,
																width		: 300
															},
															{
																xtype     	: 'textfield',
																name      	: 'KSH_SEARCH_NAME',
																fieldLabel	: '或&nbsp;&nbsp;姓名',
																labelWidth	: 55,
																value		: '',
																enableKeyEvents	: true,
																margin		: '0 5 0 0',
																allowBlank	: true,
																width		: 250
															},
															{
																xtype		: 'hiddenfield',
																name		: 'KSH_BMBID',
																value		: ''
															},
															{
																xtype		: 'hiddenfield',
																name		: 'KSH_KSNAME',
																value		: ''
															},
															{
																xtype		: 'hiddenfield',
																name		: 'KSH_MSID',
																value		: ''
															}
														  ]
									},
									{
										xtype: "fieldcontainer",
										hidden			: false,
										fieldLabel		: '搜索结果',
										hideLabel		: true,
										combineErrors	: false,
										//msgTarget		: 'side',
										layout			: 'hbox',
										height			: 30,
										defaults		: {hideLabel: false},
										items			: [
															{
																xtype		: 'displayfield',
																name		: 'SearchKSResult',
																value		: '请输入考生申请号或姓名进行查找',
																width		: 1000
															}
														  ]
														  
									},
									{
										xtype: "fieldcontainer",
										fieldLabel		: '操作按钮',
										hideLabel		: true,
										combineErrors	: false,
										//msgTarget		: 'side',
										layout			: 'hbox',
										height			: 30,
										defaults		: {hideLabel: false},
										items			: [
															{
																xtype		: 'button',
																text		: '查&nbsp;&nbsp;找',
																width		: 100,
																height		: 25,
																margin		: '10 10 10 0',
																handler 	: function() {
																				if(MainPageSearchKSPanel.getForm().isValid()){
																					
																					var searchKSForm = MainPageSearchKSPanel.getForm();
																					
																					var searchMSID = Ext.String.trim(searchKSForm.findField('KSH_SEARCH_MSID').getValue());
																					var searchKSNM = Ext.String.trim(searchKSForm.findField('KSH_SEARCH_NAME').getValue());
																					searchKSForm.findField('KSH_SEARCH_MSID').setValue(searchMSID);
																					searchKSForm.findField('KSH_SEARCH_NAME').setValue(searchKSNM);
																					
																					searchKSForm.findField('KSH_BMBID').setValue('');
																					
																					if(searchMSID=='' && searchKSNM==''){
																						Ext.Msg.alert('提示','申请号或姓名至少输入一项！');
																						searchKSForm.findField('SearchKSResult').setValue('请输入考生申请号或姓名进行查找');
																						return;
																					}
																					
																					searchKSForm.findField('SearchKSResult').setValue('正在查找，请稍后');
																					
																					maskWindow();
																					
																					var searchKSFormValues = searchKSForm.getValues();
																					var editJson = '{"typeFlag":"SEARCH","data":'+Ext.JSON.encode(searchKSFormValues)+'}'
																					var tzParams='{"ComID":"TZ_PW_MSPS_COM","PageID":"TZ_MSPS_DF_STD","OperateType":"tzSearchExaminee","comParams":{'+editJson+'}}';
																					
																					 Ext.Ajax.request({
																	                        url : window.getAddDelOneKsDataUrl,
																	                        params : {tzParams:tzParams},
																	                        timeout : 60000,
																	                        async : false,
																	                        success : function(response,opts) {
																	                        	//返回值内容
																	                            var jsonText = response.responseText;
																	                            try
																	                            {
																	                                var jsonTextObject = Ext.util.JSON.decode(jsonText);
																	                                var comContent = jsonTextObject.comContent;
																	                                //判断服务器是否返回了正确的信息
																	                                if(comContent.result == 0) {
																	                                	//显示查询结果
																	                                	var searchResult = '<b>查询结果：</b>考生申请号【'+ comContent.mssqh +'】，姓名【'+ comContent.name +'】';
																										searchKSForm.findField("SearchKSResult").setValue(searchResult);
																										
																										searchKSForm.findField("KSH_BMBID").setValue(comContent.bmbId);
																										searchKSForm.findField("KSH_KSNAME").setValue(comContent.name);
																										searchKSForm.findField("KSH_MSID").setValue(comContent.mssqh);
																										
																	                                } else {
																	                                	Ext.Msg.alert('失败', comContent.resultMsg);
																										searchKSForm.findField('SearchKSResult').setValue('请输入考生申请号或姓名进行查找');
																	                                }
																	                            }
																	                            catch(e1){
																									alert('查询失败！请重试！多次失败请联系管理员！');
																									searchKSForm.findField('SearchKSResult').setValue('请输入考生申请号或姓名进行查找');
																								}
																	                        }
																					 });
																					
																					/* Normally we would submit the form to the server here and handle the response... */
																					/*
																					searchKSForm.submit({
																						clientValidation: false,
																						url: window.getAddDelOneKsDataUrl,
																						params: {
																									LanguageCd:'ZHS',
																									OperationType:'Search',
																									BaokaoFXID:jsonObject['ps_bkfx_id']
																								},
																						success: function(form, action) {
																							//unmask window
																							unmaskWindow();
																							
																							try{
																							
																								if(action.result.error_code=="0"){
																									//显示查询结果
																									var searchRstZJHM = Ext.String.trim(action.result.ps_ksh_zjhm);
																									searchRstZJHM = searchRstZJHM==''?'无':searchRstZJHM;
																									
																									//searchKSForm.findField("SearchKSResult").setValue('<b>查询结果：</b>姓名【'+ action.result.ps_ksh_xm +'】，考生申请号【'+ action.result.ps_ksh_msid +'】，证件编号【'+ searchRstZJHM +'】');
																									searchKSForm.findField("SearchKSResult").setValue('<b>查询结果：</b>考生申请号【'+ action.result.ps_ksh_msid +'】，姓名【'+ action.result.ps_ksh_xm +'】');
																									
																									searchKSForm.findField("KSH_BMBID").setValue(action.result.ps_ksh_bmbid);
																									searchKSForm.findField("KSH_KSNAME").setValue(action.result.ps_ksh_xm);
																									searchKSForm.findField("KSH_MSID").setValue(action.result.ps_ksh_msid);
																									
																								}else{
																									Ext.Msg.alert('失败', action.result.error_decription);
																									searchKSForm.findField('SearchKSResult').setValue('请输入考生申请号或姓名进行查找');
																								}
																							
																							}
																							catch(e1){
																								alert('查询失败！请重试！多次失败请联系管理员！');
																								searchKSForm.findField('SearchKSResult').setValue('请输入考生申请号或姓名进行查找');
																							}
																						
																							
																						},
																						failure: function(form, action) {
																							//unmask window
																							unmaskWindow();
																							searchKSForm.findField('SearchKSResult').setValue('请输入考生申请号或姓名进行查找');
																							
																							switch (action.failureType) {
																								case Ext.form.action.Action.CLIENT_INVALID:
																									Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
																									break;
																								case Ext.form.action.Action.CONNECT_FAILURE:
																									Ext.Msg.alert('Failure', 'Ajax communication failed');
																									break;
																								case Ext.form.action.Action.SERVER_INVALID:
																								   Ext.Msg.alert('Failure', action.result.msg);
																						   }
																						}
																					});
																					*/
																					

																				}
							
																		}
																
															},
															{
																xtype		: 'button',
																hidden		: false,
																text		: '进行评审',
																width		: 100,
																height		: 25,
																margin		: '10 10 10 0',
																handler 	: function() {
																				if(MainPageSearchKSPanel.getForm().isValid()){
																					var searchKSForm = MainPageSearchKSPanel.getForm();
																					
																					var searchKSBMBID = Ext.String.trim(searchKSForm.findField("KSH_BMBID").getValue());
																					var searchKSKSNAME = Ext.String.trim(searchKSForm.findField("KSH_KSNAME").getValue());
																					var searchKSMSID = Ext.String.trim(searchKSForm.findField("KSH_MSID").getValue());
																					
																					if(searchKSBMBID==""){
																						return;
																					}
																									
																					maskWindow();
																					
																					
																					var searchKSFormValues = searchKSForm.getValues();
																					var editJson = '{"typeFlag":"ADD","data":'+Ext.JSON.encode(formValues)+'}'
																					var tzParams='{"ComID":"TZ_PW_MSPS_COM","PageID":"TZ_MSPS_DF_STD","OperateType":"tzSearchExaminee","comParams":{'+editJson+'}}';
																					
																					Ext.Ajax.request({
																                        url : window.getAddDelOneKsDataUrl,
																                        params : {tzParams:tzParams},
																                        timeout : 60000,
																                        async : false,
																                        success : function(response,opts) {
																                        	//返回值内容
																                            var jsonText = response.responseText;
																                            
															                                var jsonTextObject = Ext.util.JSON.decode(jsonText);
															                                var comContent = jsonObject.comContent;
															                                //判断服务器是否返回了正确的信息															                              
															                                if(comContent.result == "0" || comContent.result == "2") {
															                                	//0-新抽取的考生 或者是 2-列表中已存在的考生，则直接跳转到评分页面
															                                	
															                                	//局部刷新本地数据并跳转到打分页面
																								if(window.KSINFO_JSON_DATA == null)
																								{
																									window.KSINFO_JSON_DATA = new Array();
																								}
																								
																								var tmpBmbID = searchKSBMBID;
																								KSINFO_JSON_DATA[tmpBmbID] = jsonObject;
														
																								var tzEObject = new tzEvaluateObject();
																								
																								tzEObject.baokaoDirectionID = jsonObject['applyBatchId'];
																								tzEObject.baokaoDirectionName = jsonObject['applyBatchName'];
																								tzEObject.baokaoYear = jsonObject['classStartYear'];
																								tzEObject.baokaoClassID = jsonObject['classId'];
																								tzEObject.baokaoClassName = jsonObject['className'];
																								tzEObject.applicantName = searchKSKSNAME;
																								tzEObject.applicantInterviewID = searchKSMSID;
																								tzEObject.applicantBaomingbiaoID = searchKSBMBID;
																								
																								//若是在打分页面，则刷新；否则要跳转到打分页面
																								if(showThisPanelHeader){
																									getPartBatchDataByBatchId(jsonObject['ps_bkfx_id'],loadApplicantData,tzEObject,'NXT');
																								}else{
																									getPartBatchDataByBatchId(jsonObject['ps_bkfx_id'],loadApplicantData,tzEObject,'NXT');
																								}
																								
															                                } else {
															                                	Ext.Msg.alert('失败', jsonObject.comContent.resultMsg);
															                                }
																                            
																                        }
																					});
																					
																					/* Normally we would submit the form to the server here and handle the response... */
																					/*
																					searchKSForm.submit({
																						clientValidation: false,
																						url: window.getAddDelOneKsDataUrl,
																						params: {
																									LanguageCd:'ZHS',
																									OperationType:'Add',
																									BaokaoFXID:jsonObject['ps_bkfx_id']
																								},
																						success: function(form, action) {
																							//unmask window
																							unmaskWindow();
																							
																							//try{
																								//0-新抽取的考生 或者是 2-列表中已存在的考生，则直接跳转到评分页面
																								if(action.result.error_code=="0" || action.result.error_decription=="2"){
																									
																									//var searchKSBMBID = Ext.string.trim(searchKSForm.fineField("KSH_BMBID").getValue());
																									
																									//局部刷新本地数据并跳转到打分页面
																									if(window.KSINFO_JSON_DATA == null)
																									{
																										window.KSINFO_JSON_DATA = new Array();
																									}
																									
																									var tmpBmbID = searchKSBMBID;
																									KSINFO_JSON_DATA[tmpBmbID] = jsonObject;
															
																									var tzEObject = new tzEvaluateObject();
																									
																									tzEObject.baokaoDirectionID = jsonObject['ps_bkfx_id'];
																									tzEObject.baokaoDirectionName = jsonObject['ps_bkfx_mc'];
																									tzEObject.baokaoYear = jsonObject['ps_baok_nf'];
																									tzEObject.baokaoBatch = jsonObject['ps_baok_pc'];
																									tzEObject.baokaoZhiyuan = jsonObject['ps_baok_zy'];
																									tzEObject.applicantName = searchKSKSNAME;
																									tzEObject.applicantInterviewID = searchKSMSID;
																									tzEObject.applicantBaomingbiaoID = searchKSBMBID;
																									
																									//获取新的局部数据，并使用局部数据刷新当前页面
																									
																									//若是在打分页面，则刷新；否则要跳转到打分页面
																									if(showThisPanelHeader){
																										getPartBatchDataByBatchId(jsonObject['ps_bkfx_id'],loadApplicantData,tzEObject,'NXT');
																									}else{
																										getPartBatchDataByBatchId(jsonObject['ps_bkfx_id'],loadApplicantData,tzEObject,'NXT');
																									}
																									
																								}else{
																									
																									Ext.Msg.alert('失败', action.result.error_decription);
																									
																								}
																							
																							//}
																							//catch(e1){
																							//	alert('操作失败，请重试！多次失败请联系管理员！');
																							//}
																						
																							
																						},
																						failure: function(form, action) {
																							//unmask window
																							unmaskWindow();
																							
																							switch (action.failureType) {
																								case Ext.form.action.Action.CLIENT_INVALID:
																									Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
																									break;
																								case Ext.form.action.Action.CONNECT_FAILURE:
																									Ext.Msg.alert('Failure', 'Ajax communication failed');
																									break;
																								case Ext.form.action.Action.SERVER_INVALID:
																								   Ext.Msg.alert('Failure', action.result.msg);
																						   }
																						}
																					});
																					*/
																				}
							
																		}
																
															}
														  ]
														  
									}
					
						]
				});	
	
	
	
	return MainPageSearchKSPanel;
}


/**
 * 获取局部数据信息的函数
 * 
 * @param batchId
 * @param callBackFunction
 * @param applicantObject
 * @param operationType
 * @param tipMessage
 * @returns
 */
function getPartBatchDataByBatchId(batchId,callBackFunction,applicantObject,operationType,tipMessage)
{
	Ext.Ajax.request(
					{
						url:window.getBatchDataUrl,
						method:'POST',
						timeout:10000,
						params: {
									LanguageCd:'ZHS',
									BaokaoFXID:batchId,
									RequestDataType:'S',
									MaxRowCount:1000,
									StartRowNumber:1,
									MoreRowsFlag:'N'
								},
						success:function(response)
						{
							var jsonObject = null;
							
							try
							{
								jsonObject = Ext.JSON.decode(response.responseText);
								
								if(jsonObject.error_code != '0')
								{
									//unmask window
									unmaskWindow();
								
									loadSuccess = false;
									alert('刷新当前评审批次[' + getBatchNameById(batchId) + ']数据时发生错误：' + jsonObject.error_decription + '[错误码：' + jsonObject.error_code + ']。');
								}
								else
								{
									/*缓存当前局部刷新数据*/
									window.batchJSONArray[batchId]['ps_gaiy_info'] = jsonObject['ps_gaiy_info'];
									window.batchJSONArray[batchId]['ps_data_cy'] = jsonObject['ps_data_cy'];
									window.batchJSONArray[batchId]['ps_data_fb'] = jsonObject['ps_data_fb'];
									window.batchJSONArray[batchId]['ps_data_kslb'] = jsonObject['ps_data_kslb'];
									window.batchJSONArray[batchId]['ps_kslb_submtall'] = jsonObject['ps_kslb_submtall'];
									
									
									/*获取新的局部数据，并使用局部数据刷新当前批次评审主页面数据*/
									refreshBatchDataByBatchId(jsonObject,'ps_ksh_bmbid',applicantObject.applicantBaomingbiaoID);
									
									//回调指定函数
									if(operationType == 'NXT')
									{//因为获取下一个考生而产生的回调，该回调将导致当前页面切换到指定考生面试评审主页面
										callBackFunction(applicantObject);
									}
									else
									{
										//unmask window
										unmaskWindow();
									}
									
									
									if(tipMessage != null && tipMessage != '' && tipMessage != 'undefined')
									{
										alert(tipMessage);
									}
								}
							}
							catch(e1)
							{
								loadSuccess = false;
								if(window.evaluateSystemDebugFlag == 'Y')
								{
									alert('刷新当前评审批次[' + batchId + ']数据时发生错误，请与系统管理员联系：错误的JSON数据[' + e1.description + ']' + response.responseText);
									var mytmpWindow = window.open("about:blank");
									mytmpWindow.document.body.innerHTML = response.responseText;
								}
								else
								{
									alert('刷新当前评审批次[' + getBatchNameById(batchId) + ']数据时发生错误，请与系统管理员联系：错误的JSON数据[' + e1.description + ']。');
								}
							}
						},
						failure:function(response)
						{
							loadSuccess = false;
							if(window.evaluateSystemDebugFlag == 'Y')
							{
								alert('刷新当前评审批次[' + batchId + ']数据时发生错误，请与系统管理员联系：' + response.responseText);
							}
							else
							{
								alert('刷新当前评审批次[' + getBatchNameById(batchId) + ']数据时发生错误，请与系统管理员联系。');
							}
						}
					}
					);
}