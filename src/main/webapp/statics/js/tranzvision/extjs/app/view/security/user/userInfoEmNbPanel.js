﻿Ext.define('KitchenSink.view.security.user.userInfoEmNbPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'userAccInfo', 
    controller: 'userEmNbMg',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.security.user.userRoleModel',
		'KitchenSink.view.common.store.comboxStore',
        'KitchenSink.view.security.user.userRoleEmNbStore'
	],
	listeners:{
		resize: function(win){
			win.doLayout();
		}
	},
	actType: '',	
    title: 'EMBA用户定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
		xtype: 'form',
		reference: 'userAccountForm',
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		bodyPadding: 10,
		//heigth: 600,
		bodyStyle: 'overflow-y:auto;overflow-x:hidden',

		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: 110,
			labelStyle: 'font-weight:bold'
		},

		items: [{
			xtype: 'textfield',
			fieldLabel: '登录账号',
			// fieldLabel:Ext.tzGetResourse("TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHXX_STD.usAccNum","登录账号"),
			name: 'usAccNum',
			maxLength: 30,
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false,
			validateOnChange: true,
			validateOnBlur: true,
			validator: function (value) {
				if (/.*[\u4e00-\u9fa5]+.*$/.test(value)) {
					return "不能包含中文";
				}
				return true;
			}
		}, {
			xtype: 'textfield',
			fieldLabel: '用户名称',
			// fieldLabel:Ext.tzGetResourse("TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHXX_STD.usName","用户名称"),
			maxLength: 150,
			name: 'usName',
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false
		}, {
			xtype: 'textfield',
			fieldLabel: '手机号码',
			// fieldLabel:Ext.tzGetResourse("TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHXX_STD.mobile","手机号码"),
			maxLength: 25,
			name: 'mobile'
		}, {
			xtype: 'textfield',
			fieldLabel: '电子邮箱',
			// fieldLabel:Ext.tzGetResourse("TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHXX_STD.email","电子邮箱"),
			name: 'email',
			maxLength: 70,
			vtype: 'email'
		}, {
			xtype: 'combobox',
			fieldLabel: '激活状态',
			// fieldLabel: Ext.tzGetResourse("TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHXX_STD.jhState","激活状态"),
			editable: false,
			hidden: true,
			emptyText: '请选择',
			queryMode: 'remote',
			name: 'jhState',
			valueField: 'TValue',
			displayField: 'TSDesc',
			store: new KitchenSink.view.common.store.appTransStore("TZ_JIHUO_ZT")
		}, {
			xtype: 'combobox',
			fieldLabel: Ext.tzGetResourse("TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHXX_STD.jhMethod", "激活方式"),
			editable: false,
			hidden: true,
			emptyText: '请选择',
			queryMode: 'remote',
			name: 'jhMethod',
			valueField: 'TValue',
			displayField: 'TSDesc',
			store: new KitchenSink.view.common.store.appTransStore("TZ_JIHUO_FS")
		}, {
			xtype: 'combobox',
			fieldLabel: '账号类型',
			// fieldLabel: Ext.tzGetResourse("TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHXX_STD.rylx","账号类型"),
			editable: false,
			hidden: true,
			emptyText: '请选择',
			queryMode: 'remote',
			name: 'rylx',
			valueField: 'TValue',
			displayField: 'TSDesc',
			store: new KitchenSink.view.common.store.appTransStore("TZ_RYLX"),
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			allowBlank: false,
			listeners: {
				afterrender: function (combox) {
					//当前登录人机构id
					//combox.readOnly = true;
				}
			}

		}, {
			xtype: 'combobox',
			fieldLabel: Ext.tzGetResourse("TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHXX_STD.acctLock", "锁定账号"),
			editable: false,
			hidden: true,
			emptyText: '请选择',
			queryMode: 'remote',
			name: 'acctLock',
			valueField: 'TValue',
			displayField: 'TSDesc',
			store: new KitchenSink.view.common.store.appTransStore("ACCTLOCK")
		}, {
			xtype: 'textfield',
			fieldLabel: '账号密码',
			// fieldLabel:Ext.tzGetResourse("TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHXX_STD.password","账号密码"),
			name: 'password',
			inputType: 'password',
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			minLength: 6,
			maxLength: 32,
			allowBlank: false
		}, {
			xtype: 'textfield',
			fieldLabel: '确认密码',
			// fieldLabel:Ext.tzGetResourse("TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHXX_STD.reptPassword","确认密码"),
			name: 'reptPassword',
			inputType: 'password',
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			minLength: 6,
			maxLength: 32,
			allowBlank: false,
			validator: function (v) {
				var form = this.findParentByType("form").getForm();
				var pwd = form.findField("password").getValue();
				if (v != "" && pwd != "") {
					if (v == pwd) {
						return true;
					} else {
						return "输入的密码不一致";
					}
				} else {
					return true;
				}
			}
		}, {
			xtype: 'textfield',
			fieldLabel: '机构编号',
			// fieldLabel:Ext.tzGetResourse("TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHXX_STD.orgNo","机构编号"),
			hidden: true,
			name: 'orgNo'
		}, {
			xtype: 'combobox',
			fieldLabel: '机构名称',
			// fieldLabel:Ext.tzGetResourse("TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHXX_STD.userOrg","机构名称"),
			forceSelection: true,
			editable: false,
			hidden: true,
			store: new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_JG_BASE_T',
				/*condition:{
				 "TZ_JG_EFF_STA":{
				 "value":"Y",
				 "operator":"01",
				 "type":"01"
				 }
				 },*/
				condition: {
					TZ_JG_EFF_STA: {
						value: "Y",
						operator: "01",
						type: "01"
					}
				},
				result: 'TZ_JG_ID,TZ_JG_NAME'
			}),
			valueField: 'TZ_JG_ID',
			displayField: 'TZ_JG_NAME',
			//typeAhead: true,
			queryMode: 'remote',
			name: 'orgId',
			afterLabelTextTpl: [
				'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
			],
			listeners: {
				afterrender: function (combox) {
					//当前登录人机构id
					combox.readOnly = true;
					//console.log(combox.findRecordByValue(value));
				},
				change: function (thisComb, newValue, oldValue, eOpts) {
					//用户角色信息列表
					var grid = thisComb.findParentByType('userAccInfo').child('grid');
					//用户账号信息表单
					var form = thisComb.findParentByType('form').getForm();
					//账号
					var usAccNum = form.findField("usAccNum").getValue();

					var tzStoreParams = '{"usAccNum":"' + usAccNum + '","orgId":"' + newValue + '"}';
					grid.store.tzStoreParams = tzStoreParams;
					grid.store.load();
				}
			},
			allowBlank: false
		}, {
			xtype: 'tagfield',
			fieldLabel: '主管地区',
			name: 'zgArea',
			anyMatch: true,
			filterPickList: true,
			createNewOnEnter: false,
			createNewOnBlur: false,
			enableKeyEvents: true,
			ignoreChangesFlag: true,
			store: new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_XSXS_DQBQ_T',
				condition: {
					TZ_LABEL_STATUS: {
						value: "Y",
						operator: "01",
						type: "01"
					},
					TZ_JG_ID: {
						value: Ext.tzOrgID,
						operator: "01",
						type: "01"
					}
				},
				result: 'TZ_LABEL_NAME,TZ_LABEL_DESC'
			}),
			valueField: 'TZ_LABEL_NAME',
			displayField: 'TZ_LABEL_DESC',
			queryMode: 'local',
			listeners: {
				'select': function (combo, record, index, eOpts)//匹配下拉值之后置空输入文字
				{
					var me = this;
					me.inputEl.dom.value = "";
				}
			}
			/*triggers: {
			 search: {
			 cls: 'x-form-search-trigger',
			 handler: "searchListeners"
			 }
			 }*/
		}, {
			xtype: 'tagfield',
			fieldLabel: '自主开发地区',
			name: 'kfArea',
			anyMatch: true,
			filterPickList: true,
			createNewOnEnter: false,
			createNewOnBlur: false,
			enableKeyEvents: true,
			ignoreChangesFlag: true,
			store: new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_XSXS_DQBQ_T',
				condition: {
					TZ_LABEL_STATUS: {
						value: "Y",
						operator: "01",
						type: "01"
					},
					TZ_JG_ID: {
						value: Ext.tzOrgID,
						operator: "01",
						type: "01"
					}
				},
				result: 'TZ_LABEL_NAME,TZ_LABEL_DESC'
			}),
			valueField: 'TZ_LABEL_NAME',
			displayField: 'TZ_LABEL_DESC',
			queryMode: 'local',
			listeners: {
				'select': function (combo, record, index, eOpts)//匹配下拉值之后置空输入文字
				{
					var me = this;
					me.inputEl.dom.value = "";
				}
			}
			/*triggers: {
			 search: {
			 cls: 'x-form-search-trigger',
			 handler: "searchListeners"
			 }
			 }*/
		}, {
			xtype: 'textfield',
			//fieldLabel: '用户名称',
			//fieldLabel:Ext.tzGetResourse("TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHXX_STD.originOrgId","用户原机构"),
			hidden: true,
			name: 'originOrgId'
		}, {
			xtype: 'combobox',
			fieldLabel: '职员类型',
			editable: false,
			emptyText: '请选择',
			queryMode: 'remote',
			name: 'staffType',
			valueField: 'TValue',
			displayField: 'TSDesc',
			store: new KitchenSink.view.common.store.appTransStore("TZ_STAFF_TYPE")
		}]
	},{
    	xtype: 'grid', 
		height: 400, 
		title: '角色列表',
		frame: true,
		columnLines: true,
		reference: 'userRoleGrid',
		style:"margin:10px",
		store: {
			type: 'userRoleEmNbStore'
		},
		columns: [{ 
			text: '角色编号',
			// text:Ext.tzGetResourse("TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHXX_STD.roleID","角色编号"),
			dataIndex: 'roleID',
			hidden: true
		},{
			text: '角色名称',
			// text:Ext.tzGetResourse("TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHXX_STD.roleName","角色名称"),
			dataIndex: 'roleName',
			minWidth: 100,
			flex: 1
		},{
			xtype : 'checkcolumn',
			text: '选中角色',
			// text:Ext.tzGetResourse("TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHXX_STD.isRole","选中角色"),
			dataIndex: 'isRole',
			width: 100
		}]
		//,			
		//bbar: {
		//	xtype: 'pagingtoolbar',
		//	pageSize: 10,
			/*store: {
				type: 'userRoleNbStore'
			},*/
		/*	listeners:{
				afterrender: function(pbar){
					var grid = pbar.findParentByType("grid");
					pbar.setStore(grid.store);
				}
			},
			displayInfo: true,
			displayMsg: '显示{0}-{1}条，共{2}条',
			beforePageText: '第',
			afterPageText: '页/共{0}页',
			emptyMsg: '没有数据显示',
			plugins: new Ext.ux.ProgressBarPager()
		}	*/
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onFormSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});
