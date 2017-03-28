Ext.define('KitchenSink.view.enrollProject.userMg.userMgInfoPanel', {
	extend : 'Ext.panel.Panel',
	xtype : 'userMgInfoPanel',
	controller : 'userMgController',
	requires : [ 'Ext.data.*', 'Ext.grid.*', 'Ext.util.*',
			'Ext.toolbar.Paging', 'Ext.ux.ProgressBarPager', ],
	listeners : {
		resize : function(win) {
			win.doLayout();
		}
	},
	actType : '',
	title : '考生信息',
	bodyStyle : 'overflow-y:auto;overflow-x:hidden',
	items : [ {
		xtype : 'form',
		reference : 'userMgForm',
		layout : {
			type : 'vbox',
			align : 'stretch'
		},
		border : false,
		bodyPadding : 10,
		bodyStyle : 'overflow-y:auto;overflow-x:hidden',

		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 110,
			labelStyle : 'font-weight:bold'
		},
		items : [ {
			layout : {
				type : 'column'
			},
			items : [ {
				columnWidth : .2,
				xtype : "image",
				src : "",
				height : 300,
				width : 217,
				name : "titileImage"

			}, {
				columnWidth : .8,
				bodyStyle : 'padding-left:30px',
				layout : {
					type : 'vbox',
					align : 'stretch'
				},
				items : [ {
					xtype : 'textfield',
					readOnly : true,
					fieldLabel : '用户编号',
					name : 'OPRID',
					fieldStyle : 'background:#F4F4F4'
				}, {
					xtype : 'textfield',
					fieldLabel : '面试申请号',
					readOnly : true,
					name : 'msSqh',
					fieldStyle : 'background:#F4F4F4'
				}, {
					xtype : 'textfield',
					fieldLabel : '姓名',
					readOnly : true,
					name : 'userName',
					fieldStyle : 'background:#F4F4F4'
				}, {
					xtype : 'textfield',
					fieldLabel : '性别',
					readOnly : true,
					name : 'userSex',
					fieldStyle : 'background:#F4F4F4'
				}, {
					xtype : 'textfield',
					fieldLabel : '邮箱',
					readOnly : true,
					name : 'userEmail',
					fieldStyle : 'background:#F4F4F4'
				}, {
					xtype : 'textfield',
					fieldLabel : '手机',
					readOnly : true,
					name : 'userPhone',
					fieldStyle : 'background:#F4F4F4'
				}/*
					 * ,{ xtype: 'textfield', fieldLabel: '账号激活状态',
					 * readOnly:true, name: 'jihuoZt',
					 * fieldStyle:'background:#F4F4F4' }
					 */, {
					xtype : 'combo',
					fieldLabel : '账号激活状态',
					name : 'jihuoZt',
					emptyText : '请选择',
					queryMode : 'remote',
					editable : false,
					valueField : 'TZ_ZHZ_ID',
					displayField : 'TZ_ZHZ_DMS',
					store : new KitchenSink.view.common.store.comboxStore({
						recname : 'TZ_PT_ZHZXX_TBL',
						condition : {
							TZ_ZHZJH_ID : {
								value : "TZ_JIHUO_ZT",
								operator : "01",
								type : "01"
							},
							TZ_EFF_STATUS : {
								value : "A",
								operator : "01",
								type : "01"
							}
						},
						result : 'TZ_ZHZ_ID,TZ_ZHZ_DMS'
					})
				}, {
					xtype : 'combo',
					fieldLabel : '账号锁定状态',
					name : 'acctlock',
					emptyText : '请选择',
					queryMode : 'remote',
					editable : false,
					valueField : 'TZ_ZHZ_ID',
					displayField : 'TZ_ZHZ_DMS',
					store : new KitchenSink.view.common.store.comboxStore({
						recname : 'TZ_PT_ZHZXX_TBL',
						condition : {
							TZ_ZHZJH_ID : {
								value : "ACCTLOCK",
								operator : "01",
								type : "01"
							},
							TZ_EFF_STATUS : {
								value : "A",
								operator : "01",
								type : "01"
							}
						},
						result : 'TZ_ZHZ_ID,TZ_ZHZ_DMS'
					})
				}, {
					xtype : 'textfield',
					fieldLabel : '创建日期时间',
					readOnly : true,
					name : 'zcTime',
					fieldStyle : 'background:#F4F4F4'
				}, {
					xtype : 'radiogroup',
					fieldLabel : '黑名单用户',
					// readOnly:true,
					name : 'blackNames',
					fieldStyle : 'background:#F4F4F4',
					columns : 6,
					items : [ {
						boxLabel : "是",
						name : "blackName",
						inputValue : "Y",
					// readOnly : true
					}, {
						boxLabel : "否",
						name : "blackName",
						inputValue : "N",
					// readOnly : true
					} ]
				}, {
					xtype : 'radiogroup',
					fieldLabel : '允许继续申请',
					// readOnly:true,
					name : 'allowApplys',
					fieldStyle : 'background:#F4F4F4',
					columns : 6,
					items : [ {
						boxLabel : "是",
						name : "allowApply",
						inputValue : "Y",
					// readOnly : true
					}, {
						boxLabel : "否",
						name : "allowApply",
						inputValue : "N",
					// readOnly : true
					} ]
				}, {
					xtype : 'textarea',
					fieldLabel : '备注',
					name : 'beizhu',
				// fieldStyle:'background:#F4F4F4'
				}, {
					xtype : 'hiddenfield',
					name : 'titleImageUrl'
				} ]
			}, {
				xtype : 'tabpanel',
				frame : true,
				columnWidth : 1,
				bodyStyle : 'padding-top:10px',
				layout : {
					type : 'vbox',
					align : 'stretch'
				},
				items : [ {
					title : '个人信息',
					xtype : 'form',
					name : 'userInfoForm',
					layout : {
						type : 'vbox',
						align : 'stretch'
					},
					// frame: true,
					border : false,
					bodyPadding : 10,
					// margin:10,
					bodyStyle : 'overflow-y:auto;overflow-x:hidden',
					fieldDefaults : {
						msgTarget : 'side',
						labelWidth : 120,
						labelStyle : 'font-weight:bold'
					},
					items : [
						{
							xtype : 'datefield',
							fieldLabel : '出生日期',
							name : 'birthdate',
                            columnWidth: 1,                            
                            hideEmptyLabel: true,
                            format: 'Y-m-d'
						},
						{
							xtype : 'textfield',
							fieldLabel : '联系电话',
							name : 'zyPhone',
						},
						{
							xtype : 'combobox',
							fieldLabel : '证件类型',
							name : 'nationType',
							editable:false,
							valueField: 'TValue',
					        displayField: 'TLDesc',
					        store: new KitchenSink.view.common.store.appTransStore("TZ_NATION_ID_TYPE"),
					        queryMode: 'local',
						},
						{
							xtype : 'textfield',
							fieldLabel : '证件号码',
							name : 'nationId',
						},
						{
							xtype : 'textfield',
							fieldLabel : '常驻省市',
							name : 'lenProvince',
						},
						{
							xtype : 'textfield',
							fieldLabel : '考生编号',
							name : 'kshNo',
						},
						{
							xtype : 'combobox',
							fieldLabel : '是否有海外学历',
							editable:false,
							valueField: 'TValue',
					        displayField: 'TLDesc',
					        store: new KitchenSink.view.common.store.appTransStore("TZ_SF_SALE"),
							name : 'isHaiwXuel',
						},
						{
							xtype : 'combobox',
							fieldLabel : '申请的专业',
							name : 'appMajor',
							valueField: 'TValue',
							editable:false,
					        displayField: 'TLDesc',
					        store: new KitchenSink.view.common.store.appTransStore("TZ_MBAX_ZHUANY"),
					        queryMode: 'local',
						},						
						{
							xtype : 'textfield',
							fieldLabel : '紧急联系人',
							name : 'jjlxr',
						},
						{
							xtype : 'combobox',
							fieldLabel : '紧急联系人性别',
							editable:false,
							valueField: 'TValue',
					        displayField: 'TLDesc',
					        store: new KitchenSink.view.common.store.appTransStore("TZ_GENDER"),
							name : 'jjlxrSex',
						},
						{
							xtype : 'textfield',
							fieldLabel : '紧急联系人电话',
							name : 'jjlxrPhone',
						}
					]
				},
				{
					title : '教育经历与外语水平',
					xtype : 'form',
					name : 'eduInfoForm',
					layout : {
						type : 'vbox',
						align : 'stretch'
					},
					// frame: true,
					border : false,
					bodyPadding : 10,
					// margin:10,
					bodyStyle : 'overflow-y:auto;overflow-x:hidden',
					fieldDefaults : {
						msgTarget : 'side',
						labelWidth : 120,
						labelStyle : 'font-weight:bold'
					},
					items : [
						{
							xtype : 'combobox',
							fieldLabel : '最高学历',
							name : 'highXueli',
							editable:false,
							valueField: 'TValue',
					        displayField: 'TLDesc',
					        store: new KitchenSink.view.common.store.appTransStore("TZ_NATION_ID_TYPE"),
					        queryMode: 'local',
						},
						{
							xtype : 'combobox',
							fieldLabel : '最高学位',
							name : 'highXuewei',
							editable:false,
							valueField: 'TValue',
					        displayField: 'TLDesc',
					        store: new KitchenSink.view.common.store.appTransStore("TZ_NATION_ID_TYPE"),
					        queryMode: 'local',
						},
						{
							xtype : 'combobox',
							fieldLabel : '本专科院校',
							name : 'college',
							editable:false,
							valueField: 'TValue',
					        displayField: 'TLDesc',
					        store: new KitchenSink.view.common.store.appTransStore("TZ_NATION_ID_TYPE"),
					        queryMode: 'local',
						},
						{
							xtype : 'textfield',
							fieldLabel : '院校匪类',
							name : 'collegeFenl',
						},
						{
							xtype : 'textfield',
							fieldLabel : '毕业时间',
							name : 'biyDate',
						},
						{
							xtype : 'combobox',
							fieldLabel : '学习类型',
							name : 'learnType',
							editable:false,
							valueField: 'TValue',
					        displayField: 'TLDesc',
					        store: new KitchenSink.view.common.store.appTransStore("TZ_NATION_ID_TYPE"),
					        queryMode: 'local',
						},
						{
							xtype : 'fieldset',
							title : '外语水平',
							layout : {
								type : 'vbox',
								align : 'stretch'
							},
							items : [ {
								xtype : 'textfield',
								labelWidth:110,
								fieldLabel : '英语水平',
								name : 'engShuip'
							}, {
								xtype : 'textfield',
								fieldLabel : 'GMAT总成绩',
								name : 'GmatScore',								
							},
							{
								xtype : 'textfield',		
								labelWidth:110,
								fieldLabel : 'GMAT考试时间',
								name : 'GmatKshDtime'
							}]
						},
						{
							xtype: 'grid',
							height: 180, 
							frame: true,
							columnLines: false,
							title: '毕业院校',
							name: 'viewEduGrid',
							reference: 'viewEduGrid',
							style:"margin-top:10px",
							dockedItems:[{
								xtype:"toolbar",
								items:[									
									{text:"新增",tooltip:"新增数据",iconCls:"add"},"-",
									{text:"编辑",tooltip:"编辑数据",iconCls:"edit"},"-",
									{text:"删除",tooltip:"删除选中的数据",iconCls:"remove"}
								]
							}],
							columns: [{ 
								text: '毕业院校',
								dataIndex: 'biyCollege',
								sortable: false
							},{ 
								text: '其他毕业院校',
								dataIndex: 'othbiyCollege',
								sortable: false						
							},{ 
								text: '毕业时间',
								dataIndex: 'biyDate',
								sortable: false
							},{ 
								text: '国家',
								dataIndex: 'biyCountry',
								width: 250,
								sortable: false
							},{ 
								text: '学历',
								dataIndex: 'biyXueli',
								width: 70,
								sortable: false
							},{ 
								text: '学位',
								dataIndex: 'biyXuewei',
								width: 90,
								sortable: false
							},{ 
								text: '专业类别',
								dataIndex: 'biyMajorType',
								sortable: false
							},{
								text: '专业',
								dataIndex: 'biyMajor',
								sortable: false
							}]
								
				        }
					]
				},
				{
					title : '职业背景',
					xtype : 'form',
					name : 'workInfoForm',
					layout : {
						type : 'vbox',
						align : 'stretch'
					},
					// frame: true,
					border : false,
					bodyPadding : 10,
					// margin:10,
					bodyStyle : 'overflow-y:auto;overflow-x:hidden',
					fieldDefaults : {
						msgTarget : 'side',
						labelWidth : 160,
						labelStyle : 'font-weight:bold'
					},
					items : [
						{
							xtype : 'numberfield',
							fieldLabel : '全职工作经验（年）',
							name : 'quanWorkExp',
							minValue:0,
							maxValue:99
						},
						{
							xtype : 'numberfield',
							fieldLabel : '管理工作经验（年）',
							name : 'mgrWorkExp',
							minValue:0,
							maxValue:99
						},
						{
							xtype : 'fieldset',
							title : '职业背景',
							layout : {
								type : 'vbox',
								align : 'stretch'
							},
							fieldDefaults : {								
								labelWidth : 150
							},
							items : [ {
								xtype : 'textfield',
								fieldLabel : '公司名称',
								name : 'workCompanyName'
							},
							{
								xtype : 'datefield',
								fieldLabel : '在本公司开始工作时间',
								name : 'workStartDt',
	                            columnWidth: 1,
	                            hideEmptyLabel: true,
	                            format: 'Y-m-d'
							},
							{
								xtype : 'combobox',
								fieldLabel : '单位性质',
								name : 'workCompanyXinz',
								editable:false,
								valueField: 'TValue',
						        displayField: 'TLDesc',
						        store: new KitchenSink.view.common.store.appTransStore("TZ_NATION_ID_TYPE"),
						        queryMode: 'local',
							},
							{
								xtype : 'combobox',
								fieldLabel : '行业类别',
								name : 'workCompanyHylb',
								editable:false,
								valueField: 'TValue',
						        displayField: 'TLDesc',
						        store: new KitchenSink.view.common.store.appTransStore("TZ_MBA_ZS_HYLB"),
						        queryMode: 'local',
							},
							{
								xtype : 'textfield',
								fieldLabel : '所在部门',
								name : 'workCompanyDept'
							},
							{
								xtype : 'combobox',
								fieldLabel : '工作职能',
								name : 'workCompanyZhin',
								editable:false,
								valueField: 'TValue',
						        displayField: 'TLDesc',
						        store: new KitchenSink.view.common.store.appTransStore("TZ_MBA_ZS_GWZN"),
						        queryMode: 'local',
							},
							{
								xtype : 'textfield',
								fieldLabel : '工作职位',
								name : 'workCompanyPosition'
							},
							{
								xtype : 'combobox',
								fieldLabel : '职务类型',
								name : 'workPosiType',
								editable:false,
								valueField: 'TValue',
						        displayField: 'TLDesc',
						        store: new KitchenSink.view.common.store.appTransStore("TZ_MBA_ZW_LX"),
						        queryMode: 'local',
							},{
								xtype : 'numberfield',
								fieldLabel : '目前年收入（万）',
								name : 'currentYearShr',
								minValue:1
							},{
								xtype : 'numberfield',
								fieldLabel : '下属员工数（人）',
								name : 'ownerPersn',
								minValue:0
							},
							{
								xtype : 'combobox',
								fieldLabel : '工作所在省市',
								editable:false,
								name : 'workProvince',
								valueField: 'TValue',
						        displayField: 'TLDesc',
						        store: new KitchenSink.view.common.store.appTransStore("TZ_MBA_ZW_LX"),
						        queryMode: 'local',
							},
							{
								xtype : 'combobox',
								fieldLabel : '是否自主创业',
								name : 'workIsChy',
								editable:false,
								valueField: 'TValue',
						        displayField: 'TLDesc',
						        store: new KitchenSink.view.common.store.appTransStore("TZ_SF_SALE"),
						        queryMode: 'local',
							}]
						},
						{
							xtype : 'textareafield',
							fieldLabel : '其他工作经历',
							name : 'otherWorkExp',
							height:100
						}
					]
				},
				{
					title : '创业经历',
					xtype : 'form',
					name : 'chyInfoForm',
					layout : {
						type : 'vbox',
						align : 'stretch'
					},
					// frame: true,
					border : false,
					bodyPadding : 10,
					// margin:10,
					bodyStyle : 'overflow-y:auto;overflow-x:hidden',
					fieldDefaults : {
						msgTarget : 'side',
						labelWidth : 160,
						labelStyle : 'font-weight:bold'
					},
					items : [						
						{
							xtype : 'fieldset',
							title : '创业经历',
							layout : {
								type : 'vbox',
								align : 'stretch'
							},
							fieldDefaults : {								
								labelWidth : 150
							},
							items : [ {
								xtype : 'textfield',
								fieldLabel : '公司名称',
								name : 'chyCompanyName'
							},							
							{
								xtype : 'combobox',
								fieldLabel : '单位性质',
								editable:false,
								name : 'chyCompanyXinz',
								valueField: 'TValue',
						        displayField: 'TLDesc',
						        store: new KitchenSink.view.common.store.appTransStore("TZ_NATION_ID_TYPE"),
						        queryMode: 'local',
							},
							{
								xtype : 'combobox',
								fieldLabel : '行业类别',
								editable:false,
								name : 'chyCompanyHylb',
								valueField: 'TValue',
						        displayField: 'TLDesc',
						        store: new KitchenSink.view.common.store.appTransStore("TZ_MBA_ZS_HYLB"),
						        queryMode: 'local',
							},
							{
								xtype : 'textfield',
								fieldLabel : '所在部门',
								name : 'chyCompanyDept'
							},
							{
								xtype : 'combobox',
								fieldLabel : '工作职能',
								name : 'chyCompanyZhin',
								editable:false,
								valueField: 'TValue',
						        displayField: 'TLDesc',
						        store: new KitchenSink.view.common.store.appTransStore("TZ_MBA_ZS_GWZN"),
						        queryMode: 'local',
							},
							{
								xtype : 'textfield',
								fieldLabel : '工作职位',
								name : 'chyCompanyPosition'
							},
							{
								xtype : 'combobox',
								fieldLabel : '职务类型',
								name : 'chyPosiType',
								editable:false,
								valueField: 'TValue',
						        displayField: 'TLDesc',
						        store: new KitchenSink.view.common.store.appTransStore("TZ_MBA_ZW_LX"),
						        queryMode: 'local',
							},
							{
								xtype : 'numberfield',
								fieldLabel : '下属员工数（人）',
								name : 'chyOwnerPersn',
								minValue:0
							},
							{
								xtype : 'combobox',
								fieldLabel : '工作所在省市',
								editable:false,
								name : 'chyProvince',
								valueField: 'TValue',
						        displayField: 'TLDesc',
						        store: new KitchenSink.view.common.store.appTransStore("TZ_MBA_ZW_LX"),
						        queryMode: 'local',
							}]
						},
						{
							xtype : 'textareafield',
							fieldLabel : '其他创业经历',
							name : 'otherChyExp',
							height:100
						}
					]
				},
				{
					title : '申请材料',
					xtype : 'form',
					name : 'appClInfoForm',
					layout : {
						type : 'vbox',
						align : 'stretch'
					},
					// frame: true,
					border : false,
					bodyPadding : '0 10 10 10',
					// margin:10,
					bodyStyle : 'overflow-y:auto;overflow-x:hidden',
					fieldDefaults : {
						msgTarget : 'side',
						labelWidth : 120,
						labelStyle : 'font-weight:bold'
					},
					items : [						
						{
							xtype: 'grid',
							height: 180, 
							frame: true,
							columnLines: false,
							name: 'viewAppGrid',
							reference: 'viewAppGrid',
							style:"margin-top:10px",					
							columns: [{ 
								text: '报考方向',
								dataIndex: 'appInfo',
								sortable: false
							},
							{ 
								text: '提交状态',
								dataIndex: 'appSubStatus',
								sortable: false
							},
							{ 
								text: '申请单编号',
								dataIndex: 'applyInfoNo',
								sortable: false						
							},
							{
								   xtype: 'actioncolumn',
								   text: '操作',	
					               menuDisabled: true,
								   menuText: '操作',
					               sortable: false,
								   align: 'center',
								   items:[
									   {text: '查看报名表',iconCls: 'preview',tooltip: '查看报名表'},
									   {text: '打印',iconCls: 'print',tooltip: '打印'},
									   {text: '报名流程',iconCls: 'edit',tooltip: '报名流程'},
									   {text: '查看',iconCls: 'view',tooltip: '查看'}
								   ]
					        }]
								
				        }
					]
				},
				{
					title : '考生导入信息',
					xtype : 'form',
					name : 'ksdrInfoForm',
					layout : {
						type : 'vbox',
						align : 'stretch'
					},
					// frame: true,
					border : false,
					bodyPadding : 10,
					// margin:10,
					bodyStyle : 'overflow-y:auto;overflow-x:hidden',
					fieldDefaults : {
						msgTarget : 'side',
						labelWidth : 200,
						labelStyle : 'font-weight:bold'
					},
					items : [ {
						xtype : 'fieldset',
						title : '材料评审',
						layout : {
							type : 'vbox',
							align : 'stretch'
						},
						items : [ {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '材料评审结果',
							name : 'clpsJg',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '是否有面试资格',
							name : 'sfmsZg',
							fieldStyle : 'background:#F4F4F4'
						} ]
					}, {
						xtype : 'fieldset',
						title : '面试',
						layout : {
							type : 'vbox',
							align : 'stretch'
						},
						items : [ {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '面试报到时间',
							name : 'msbdSj',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '面试报到地点',
							name : 'msbdDd',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '面试结果',
							name : 'msJg',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '面试结果标识',
							name : 'msJgBz',
							fieldStyle : 'background:#F4F4F4'
						} ]
					}, {
						xtype : 'fieldset',
						title : '联考报名',
						layout : {
							type : 'vbox',
							align : 'stretch'
						},
						items : [ {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '联考年份',
							name : 'lkNf',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '联考总分',
							name : 'lkZf',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '联考英语',
							name : 'lkYy',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '联考综合',
							name : 'lkZh',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '联考是否过线',
							name : 'lkSfgx',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '政治',
							name : 'Zz',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '英语听力',
							name : 'yyTl',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '政治听力是否过线',
							name : 'zzTlSfGx',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '是否预录取',
							name : 'sfYlq',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '联考考生编号',
							name : 'lkksBh',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '联考报名时学历校验状态',
							name : 'lkbmsXlJyZt',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '考生编号后四位',
							name : 'ksbhHsw',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '政治英语听力考试时间',
							name : 'zzYyTlKsSj',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '政治英语听力考试地点',
							name : 'zzYyTlKsDd',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '新生奖学金申请状态',
							name : 'ssJxjSqZt',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '政治听力考试备注',
							name : 'zzTlKsBz',
							fieldStyle : 'background:#F4F4F4'
						} ]
					}, {
						xtype : 'fieldset',
						title : '预录取',
						layout : {
							type : 'vbox',
							align : 'stretch'
						},
						items : [ {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '新生奖学金最终结果',
							name : 'ssJxjZzJg',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '学费总额参考',
							name : 'xfZeCk',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '学号',
							name : 'xh',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '培养协议接受',
							name : 'pyXyJs',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '工作证明接受',
							name : 'zzZmJs',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '分班结果',
							name : 'fbJg',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '经管邮箱',
							name : 'jgYx',
							fieldStyle : 'background:#F4F4F4'
						}, {
							xtype : 'textfield',
							readOnly : true,
							fieldLabel : '邮箱初始密码',
							name : 'yxCsMm',
							fieldStyle : 'background:#F4F4F4'
						} ]
					} ]
				} ]
			} ]
		} ]
	} ],
	buttons : [ {
		text : '保存',
		iconCls : "save",
		handler : 'onFormSave'
	}, {
		text : '确定',
		iconCls : "ensure",
		handler : 'onFormEnsure'
	}, {
		text : '关闭',
		iconCls : "close",
		handler : 'onFormClose'
	} ]
});