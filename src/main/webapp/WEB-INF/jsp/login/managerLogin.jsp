<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="google" content="notranslate" />
<meta charset="utf-8">
<title>${TZ_JG_LOGIN_INFO}</title>
<script type="text/javascript">
	var TzUniversityContextPath = "${contextPath}";
</script>
<link rel="stylesheet" type="text/css"
	href="${contextPath}/statics/js/lib/extjs/packages/ext-theme-neptune/build/resources/ext-theme-neptune-all.css" />
<style type="text/css">
html, body {
	height: 95%;
}

body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

.x-panel-header-default {
	background-color: #b1193b;
}

.x-window-header-default-top {
	background-color: #b1193b;
}

.x-window-default {
	border-color: #b1193b;
}

.x-window-header-default {
	border-width: 0px;
}
</style>
<script type="text/javascript"
	src="${contextPath}/statics/js/tranzvision/util/checkExplorer.js"
	charset="utf-8"></script>
<script type="text/javascript"
	src="${contextPath}/statics/js/tranzvision/func/captcha.js"
	charset="utf-8"></script>
<script type="text/javascript" charset="UTF-8"
	src="${contextPath}/statics/js/lib/extjs/bootstrap.js"></script>
<script type='text/javascript'>
	var rightwidth = window.screen.availWidth;

	var topheight = window.screen.availHeight;

	rightwidth = rightwidth / 2 - 340 - 220;

	if (rightwidth <= 0) {

		rightwidth = 120;

	}

	if (topheight > 720) {

		topheight = (topheight - 720) / 2 + 60;

	} else {

		topheight = 60;

	}

	var jgHidden = false;

	var locationOrgId = "${locationOrgId}";

	var currentOrgId = "";

	if (locationOrgId != "") {
		jgHidden = true;
	}

	function BindEnter(obj)

	{

		if (obj.keyCode == 13)

		{

			var bt = Ext.getCmp("btSubmit");

			var fm = bt.findParentByType("form");

			var htmlCom = fm.down("component[name=errorMsg]");

			var form = fm.getForm();

			if (form.isValid()) {

				var orgIdField = form.findField("orgId");

				var orgId = orgIdField.getValue();

				if (orgIdField.isHidden() == true) {

					orgId = currentOrgId;

				}

				var userName = form.findField("userName").getValue();

				var password = form.findField("password").getValue();

				var yzm = form.findField("yzm").getValue();

				var tzLoginParams = {};

				tzLoginParams.ComID = "TZ_PT_LOGIN_COM";

				tzLoginParams.PageID = "TZ_PT_LOGIN_PAGE";

				tzLoginParams.OperateType = "HTML";

				tzLoginParams.comParams = {};

				tzLoginParams.comParams.validateType = "JgLogin";

				tzLoginParams.comParams.orgId = orgId;

				tzLoginParams.comParams.userName = userName;

				tzLoginParams.comParams.password = password;

				tzLoginParams.comParams.yzm = yzm;
				Ext.getBody().mask("数据提交中,请稍后......");
				Ext.Ajax
						.request({

							async : false,

							url : '${contextPath}/login/dologin',

							params : {

								"tzParams" : Ext.JSON.encode(tzLoginParams)

							},

							success : function(response) {

								var responseText = eval("("
										+ response.responseText + ")");

								if (responseText.success == "success") {

									var cpr = new Ext.state.CookieProvider();

									Ext.state.Manager.setProvider(cpr);

									cpr.set("orgId", form.findField("orgId")
											.getValue());

									cpr.set("userName", form.findField(
											"userName").getValue());

									window.location.href = "${contextPath}"
											+ responseText.indexUrl;

								} else {

									//Ext.Msg.alert("提示",responseText.error);
									Ext.getBody().unmask();
									Ext.getCmp("yzmpic")
											.setSrc(GenCaptchaUrl());

									form.findField("yzm").setValue("");

									if (responseText.success == "2") {

										form.findField("password").setValue("");

									}
									
									htmlCom.getEl().dom.innerHTML = "<div style='padding:5px;margin-top: -20px;'><img src='${contextPath}/statics/images/login/alert.png' />&nbsp;"
										+ responseText.error + "<div>";

								}

							}

						});

			} else {

				htmlCom.getEl().dom.innerHTML = "<div style='padding:5px;margin-top: -20px;'><img src='${contextPath}/statics/images/login/alert.png' />&nbsp;登录信息未填写完整或验证码错误<div>";

			}

		}

	}

	Ext
			.onReady(function() {
				 
				
				Ext.define('Altus.overrides.form.field.ComboBox',

				{

					override : 'Ext.form.field.ComboBox',

					checkChangeEvents : Ext.isIE ? [ 'change',
							'propertychange', 'keyup' ] : [ 'change', 'input',
							'textInput', 'keyup', 'dragdrop' ]

				});

				var cp = new Ext.state.CookieProvider();

				Ext.state.Manager.setProvider(cp);

				var cpUsername = cp.get("userName");

				var cpOrgId = cp.get("orgId");

				Ext.apply(Ext.form.field.VTypes, {

					codeValidator : function(value) {

						var flag = false;

						Ext.Ajax.request({

							async : false,

							url : '${contextPath}/captcha/checkqrcode',

							params : {

								"tzParams" : tzYzmParams

							},

							async : false,

							success : function(response) {

								var responseText = eval("("
										+ response.responseText + ")");

								if (responseText.success == "true") {

									flag = true;

								}

							}

						});

						return flag;

					},

					codeValidatorText : '输入的验证码不正确!'

				});

				Ext
						.create(
								'Ext.form.FormPanel',
								{

									title : '${TZ_JG_LOGIN_INFO}',

									width : 340,

									height : 350,

									border : false,

									frame : false,

									titleAlign : 'center',

									bodyPadding : 30,

									bodyStyle : 'border-width: 0px;',

									style : 'position:absolute; top:'
											+ topheight
											+ 'px; right:'
											+ rightwidth
											+ 'px; opacity:0.8;  filter: Alpha(Opacity=80);',

									fieldDefaults : {

										msgTarget : 'side',

										labelWidth : 70

									//          labelStyle: 'font-weight:bold'

									},

									renderTo : "tranzvision_loginDiv",

									items : [
											{

												xtype : 'combobox',

												width : 280,

												fieldLabel : '所属机构',

												store : {

													fields : [ {
														name : 'orgValue'
													}, {
														name : 'orgDesc'
													} ],

													autoLoad : true,
													/*
													data : [ {
														orgValue : 'Admin',
														orgDesc : '平台管理机构'
													}, {
														orgValue : 'YENCHING',
														orgDesc : '燕京学堂'
													}, {
														orgValue : 'GSM',
														orgDesc : '北大光华'
													} ]
													 */
													///*
													proxy : {

														type : 'ajax',

														url : "${contextPath}/login/getorgdata",

														reader : {

															type : 'json',

															rootProperty : 'org'

														}

													}
												//*/

												},

												displayField : 'orgDesc',

												valueField : 'orgValue',

												name : 'orgId',

												allowBlank : jgHidden,

												blankText : '请选择登录机构',

												hidden : false,

												editable : false,

												//forceSelection: true,

												listeners : {

													afterrender : function(cm,
															eOpts) {

														var fm = cm
																.findParentByType("form");

														var htmlCom = fm
																.down("component[name=errorMsg]");

														if (jgHidden == true) {

															Ext.Ajax
																	.request({

																		async : false,

																		url : '${contextPath}/login/checkorgstatus',
																		params : {
																			"orgid" : locationOrgId
																		},

																		success : function(
																				response) {

																			var responseText = eval("("
																					+ response.responseText
																					+ ")");

																			if (responseText.success == "true") {

																				currentOrgId = responseText.orgId;

																				cm
																						.hide();

																			} else if (responseText.success == "false") {

																			} else {

																			}

																		}

																	});

														} else {

															cm
																	.setValue(cpOrgId);

														}

													}

												}

											},
											{

												xtype : 'textfield',

												width : 280,

												fieldLabel : '用户名',

												margin : '20 0 0 0',

												name : 'userName',

												value : cpUsername,

												blankText : '请填写用户名',

												allowBlank : false,
												
												listeners : {

													change : function(cm,
															newVal, oldVal,
															eOpts) {

														var fm = cm
																.findParentByType("form");

														var htmlCom = fm
																.down("component[name=errorMsg]");

														htmlCom.getEl().dom.innerHTML = "<div style='padding:5px;margin-top: -20px;height:30px;'>&nbsp;<div>";

													}

												}

											},
											{

												width : 280,

												xtype : 'textfield',

												fieldLabel : '密码',

												margin : '20 0 0 0',

												name : 'password',

												blankText : '请填写密码',

												allowBlank : false,

												border : false,

												inputType : 'password',

												listeners : {

													change : function(cm,
															newVal, oldVal,
															eOpts) {

														var fm = cm
																.findParentByType("form");

														var htmlCom = fm
																.down("component[name=errorMsg]");

														htmlCom.getEl().dom.innerHTML = "<div style='padding:5px;margin-top: -20px;height:30px;'>&nbsp;<div>";

													}

												}

											},
											{

												layout : 'column',

												bodyStyle : 'border: 0; margin-top:20px; margin-bottom:30px;',

												//	margin: '20 0 0 0',

												frame : false,

												width : 280,

												height : 80,

												items : [
														{

															columnWidth : 0.7,

															xtype : 'textfield',

															fieldLabel : '验证码',

															name : 'yzm',

															blankText : '请填写验证码',

															allowBlank : false,

															//	validateOnChange: false,

															//	validateOnBlur: true,

															//	vtype: 'codeValidator',

															listeners : {

																change : function(
																		cm,
																		newVal,
																		oldVal,
																		eOpts) {

																	var fm = cm
																			.findParentByType("form");

																	var htmlCom = fm
																			.down("component[name=errorMsg]");

																	htmlCom
																			.getEl().dom.innerHTML = "<div style='padding:5px;margin-top: -20px;height:30px;'>&nbsp;<div>";

																}

															}

														},
														{

															columnWidth : 0.3,

															xtype : "image",

															src : GenCaptchaUrl(),

															name : "yzmpic",

															id : "yzmpic",

															listeners : {

																el : {

																	click : function() {

																		Ext
																				.getCmp(
																						"yzmpic")
																				.setSrc(
																						GenCaptchaUrl());

																	}

																}

															}

														} ]

											},
											{

												xtype : 'component',

												html : "<div style='padding:5px;margin-top: -20px;height:30px;'>&nbsp;<div>",

												name : 'errorMsg'

											},
											{

												xtype : 'button',

												text : '登 录',

												id : "btSubmit",

												padding : 10,

												width : 280,

												style : 'background: #b1193b; border-color: #b1193b;',

												handler : function(bt, e) {

													var fm = bt
															.findParentByType("form");

													var htmlCom = fm
															.down("component[name=errorMsg]");

													var form = fm.getForm();

													if (form.isValid()) {

														var orgIdField = form
																.findField("orgId");

														var orgId = orgIdField
																.getValue();

														if (orgIdField
																.isHidden() == true) {

															orgId = currentOrgId;

														}

														var userName = form
																.findField(
																		"userName")
																.getValue();

														var password = form
																.findField(
																		"password")
																.getValue();

														var yzm = form
																.findField(
																		"yzm")
																.getValue();

														//var tzLoginParams ='{"ComID":"TZ_PT_LOGIN_COM","PageID":"TZ_PT_LOGIN_PAGE","OperateType":"HTML","comParams":{"validateType":"JgLogin","orgId":"'+ orgId +'","userName":"'+userName+'","password":"'+password+'","yzm":"'+yzm+'"}}';

														var tzLoginParams = {};

														tzLoginParams.ComID = "TZ_PT_LOGIN_COM";

														tzLoginParams.PageID = "TZ_PT_LOGIN_PAGE";

														tzLoginParams.OperateType = "HTML";

														tzLoginParams.comParams = {};

														tzLoginParams.comParams.validateType = "JgLogin";

														tzLoginParams.comParams.orgId = orgId;

														tzLoginParams.comParams.userName = userName;

														tzLoginParams.comParams.password = password;

														tzLoginParams.comParams.yzm = yzm;
														
														//提交添加只读层;
														
														Ext.getBody().mask("数据提交中,请稍后......");
									                  	
														Ext.Ajax
																.request({

																	async : false,

																	url : '${contextPath}/login/dologin',

																	params : {

																		"tzParams" : Ext.JSON
																				.encode(tzLoginParams)

																	},

																	success : function(
																			response) {

																		var responseText = eval("("
																				+ response.responseText
																				+ ")");

																		if (responseText.success == "success") {

																			cp
																					.set(
																							"userName",
																							userName);

																			cp
																					.set(
																							"orgId",
																							orgId);
																			
																			window.location.href = "${contextPath}"
																					+ responseText.indexUrl;
																			
																		} else {
																			Ext.getBody().unmask();

																			Ext
																					.getCmp(
																							"yzmpic")
																					.setSrc(
																							GenCaptchaUrl());

																			form
																					.findField(
																							"yzm")
																					.setValue(
																							"");

																			if (responseText.success == "2") {

																				form
																						.findField(
																								"password")
																						.setValue(
																								"");

																			}

																			htmlCom
																					.getEl().dom.innerHTML = "<div style='padding:5px;margin-top: -20px;'><img src='${contextPath}/statics/images/login/alert.png' />&nbsp;"
																					+ responseText.error
																					+ "<div>";

																		}

																	}

																});
														
													
														
													} else {

														//Ext.Msg.alert("提示","登录信息未填写完整");		

														htmlCom.getEl().dom.innerHTML = "<div style='padding:5px;margin-top: -20px;'><img src='${contextPath}/statics/images/login/alert.png' />&nbsp;登录信息未填写完整<div>";

													}

												}

											} ]

								});

				//检查浏览器;

				var checkBZ = false;

				var explorer = window.navigator.userAgent;

				//ie; 

				if ("ActiveXObject" in window) {

					if (navigator.userAgent.indexOf("MSIE 6.0") > 0
							|| navigator.userAgent.indexOf("MSIE 7.0") > 0
							|| navigator.userAgent.indexOf("MSIE 8.0") > 0) {

					} else {

						if (document.documentMode) {

							checkBZ = true;

						}

					}

				}

				//firefox; 

				else if (explorer.indexOf("Firefox") >= 0) {

					checkBZ = true;

				}

				//Chrome;

				else if (explorer.indexOf("Chrome") >= 0) {

					checkBZ = true;

				}

				//Opera;

				else if (explorer.indexOf("Opera") >= 0) {

					checkBZ = true;

				}

				//Safari;

				else if (explorer.indexOf("Safari") >= 0) {

					checkBZ = true;

				}
				;

				if (!checkBZ) {

					alert("您所使用的浏览器版本过低，请选择IE9及以上或较新版本的Friefox、Chrome浏览器，推荐使用Chrome，可以得到最好的体验。如果您坚持使用当前浏览器，可能会发生未知错误。\nThe browser version you are using is too low, please choose IE9 above or a newer version of Firefox, Chrome.  It is recommended to use Chrome, and you can get the best experience.  If you stick with the current browser, may unknown error has occurred.");

				}

			});
</script>

</head>

<body
	style="background: #fff; background-image: url('${contextPath}${orgLoginBjImgUrl}'); background-repeat: no-repeat; background-position: center center;"
	onkeydown="BindEnter(event)">

	<div id="tranzvision_loginDiv" style="width: 100%; height: 100%"></div>

	<div
		style="position: absolute; bottom: 0px; width: 100%; background: #fff; text-align: center; padding-top: 20px;">

		${TZ_JG_LOGIN_COPR}</div>

</body>

</html>