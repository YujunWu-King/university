Ext.define('KitchenSink.view.enrollProject.userMg.userBmSchView', {
	extend : 'Ext.panel.Panel',
	xtype : 'userBmSchView',
	reference : 'userBmSchView',
	controller : 'userMgController',
	requires : [ 
		'Ext.data.*', 
		'Ext.grid.*', 
		'Ext.util.*',
		'Ext.toolbar.Paging', 
		'Ext.ux.ProgressBarPager',
	],
	listeners : {
		resize : function(win) {
			win.doLayout();
		}
	},
	actType : '',
	title : '考生报名流程查看',
	bodyStyle : 'overflow-y:auto;overflow-x:hidden',
	
	items : [ {
		xtype : 'form',
		reference : 'userMgForm',
		name:'userMgForm',
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
				columnWidth : .15,
				xtype : "image",
				src : "",
				height : 193,
				width : 140,
				name : "titileImage"

			}, 
			{
				columnWidth : .85,
				bodyStyle : 'padding-left:30px',
				layout : {
					type : 'vbox',
					align : 'stretch',
					readOnly : true,
				},
				items : [ {
					xtype : 'textfield',					
					fieldLabel : '用户编号',
					name : 'OPRID',
					readOnly:true,
					fieldStyle : 'background:#F4F4F4'
				},{
					xtype : 'textfield',					
					fieldLabel : '报名表编号',
					name : 'APPID',
					readOnly:true,
					hidden:true,
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
				}, {
					xtype : 'textfield',
					fieldLabel : '创建日期时间',
					readOnly : true,
					name : 'zcTime',
					fieldStyle : 'background:#F4F4F4'
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
				items : [
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
						/*
						 * readOnly : true,
						 * fieldStyle : 'background:#F4F4F4'
						 */
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
							fieldLabel : '是否进入材料评审',
							maxLength:30,
							name : 'enterClps'							
						},{
							xtype : 'textfield',							
							fieldLabel : '材料评审结果',
							maxLength:254,
							name : 'clpsJg'							
						}, 
						{
							xtype : 'textfield',							
							fieldLabel : '是否有面试资格',
							maxLength:30,
							name : 'sfmsZg'							
						}
						/*{
							xtype : 'combobox',
							fieldLabel : '是否有面试资格',
							name : 'sfmsZg',
							valueField: 'TValue',
					        displayField: 'TLDesc',
					        store: new KitchenSink.view.common.store.appTransStore("TZ_SF_SALE")
						}*/ ]
					}, {
						xtype : 'fieldset',
						title : '面试',
						layout : {
							type : 'vbox',
							align : 'stretch'
						},
						items : [ {
							xtype : 'textfield',
							fieldLabel : '面试报到时间',
							maxLength:60,
							name : 'msbdSj'
						}, {
							xtype : 'textfield',
							fieldLabel : '面试报到地点',
							maxLength:60,
							name : 'msbdDd'
						}, 
						{
							xtype : 'textfield',
							fieldLabel : '面试结果',
							maxLength:254,
							name : 'msJg'
						}, 
						{
							xtype : 'textfield',
							fieldLabel : '面试结果标识',
							maxLength:30,
							name : 'msJgBz'
						}
						/*{
							xtype : 'combobox',
							fieldLabel : '面试结果标识',
							name : 'msJgBz',
							valueField: 'TValue',
					        displayField: 'TLDesc',
					        store: new KitchenSink.view.common.store.appTransStore("TZ_MSH_RESULT_FLG"),
						}*/ ]
					}, {
						xtype : 'fieldset',
						title : '联考报名',
						layout : {
							type : 'vbox',
							align : 'stretch'
						},
						items : [ {
							xtype : 'textfield',
							fieldLabel : '联考年份',
							maxLength:10,
							name : 'lkNf'
						}, {
							xtype : 'textfield',
							fieldLabel : '联考总分',
							maxLength:10,
							name : 'lkZf'
						}, {
							xtype : 'textfield',
							fieldLabel : '联考英语',
							maxLength:10,
							name : 'lkYy'
						}, {
							xtype : 'textfield',
							fieldLabel : '联考综合',
							maxLength:10,
							name : 'lkZh'
						}, {
							xtype : 'textfield',
							fieldLabel : '联考是否过线',
							maxLength:10,
							name : 'lkSfgx'
						}, {
							xtype : 'textfield',
							fieldLabel : '政治',
							maxLength:10,
							name : 'Zz'
						}, {
							xtype : 'textfield',
							fieldLabel : '英语听力',
							maxLength:10,
							name : 'yyTl'
						}, {
							xtype : 'textfield',
							fieldLabel : '政治听力是否过线',
							maxLength:10,
							name : 'zzTlSfGx'
						}, {
							xtype : 'textfield',
							fieldLabel : '是否预录取',
							maxLength:10,
							name : 'sfYlq'
						}, {
							xtype : 'textfield',
							fieldLabel : '联考考生编号',
							maxLength:30,
							name : 'lkksBh'
						}, {
							xtype : 'textfield',
							fieldLabel : '联考报名时学历校验状态',
							//maxLength:30,
							maxLength:254,
							name : 'lkbmsXlJyZt'
						}, {
							xtype : 'textfield',
							fieldLabel : '考生编号后四位',
							maxLength:10,
							name : 'ksbhHsw'
						}, {
							xtype : 'textfield',
							fieldLabel : '政治英语听力考试时间',
							//maxLength:60,
							maxLength:100,
							name : 'zzYyTlKsSj'
						}, {
							xtype : 'textfield',
							fieldLabel : '政治英语听力考试地点',
							//maxLength:60,
							maxLength:100,
							name : 'zzYyTlKsDd'
						}, {
							xtype : 'textfield',
							fieldLabel : '新生奖学金申请状态',
							maxLength:100,
							name : 'ssJxjSqZt'
						}, {
							xtype : 'textfield',
							fieldLabel : '政治听力考试备注',
							maxLength:254,
							name : 'zzTlKsBz'
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
							fieldLabel : '新生奖学金最终结果',
							maxLength:254,
							name : 'ssJxjZzJg'
						}, {
							xtype : 'textfield',
							fieldLabel : '学费总额参考',
							maxLength:254,
							name : 'xfZeCk'
						}, {
							xtype : 'textfield',
							fieldLabel : '学号',
							maxLength:20,
							name : 'xh'
						}, {
							xtype : 'textfield',
							fieldLabel : '培养协议接受',
							maxLength:10,
							name : 'pyXyJs'
						}, {
							xtype : 'textfield',
							fieldLabel : '工作证明接受',
							maxLength:10,
							name : 'zzZmJs'
						}, {
							xtype : 'textfield',
							fieldLabel : '分班结果',
							maxLength:254,
							name : 'fbJg'
						}, {
							xtype : 'textfield',
							fieldLabel : '经管邮箱',
							maxLength:70,
							name : 'jgYx'
						}, {
							xtype : 'textfield',
							fieldLabel : '邮箱初始密码',
							maxLength:30,
							name : 'yxCsMm'
						},{
							xtype : 'textfield',
							maxLength:200,
							fieldLabel : '备注',
							name : 'yxRemark'
						} ]
					} ]
				} ]
			} ]
		} ]
	} ],
	buttons : [ {
		text : '保存',
		iconCls : "save",
		handler : 'onFormSave2'
	}, {
		text : '确定',
		iconCls : "ensure",
		handler : 'onFormEnsure2'
	}, {
		text : '关闭',
		iconCls : "close",
		handler : 'onFormClose'
	} ]
});