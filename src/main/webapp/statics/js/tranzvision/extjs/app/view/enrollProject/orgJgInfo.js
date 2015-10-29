Ext.define('KitchenSink.view.orgmgmt.orgJgInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'orgJgInfo',
	controller: 'orgMg',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.orgmgmt.orgController',
        'KitchenSink.view.orgmgmt.orgInfoStore'
	],
    title: '机构信息', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'update',//默认修改
    listeners:{
        afterrender: function(panel){

            var form = panel.child('form').getForm();

            var tzParams = '{"ComID":"TZ_GD_ORGGL_COM","PageID":"TZ_GD_ORGDEF_STD","OperateType":"QF","comParams":{}}';

            Ext.tzLoad (tzParams,function(responseData){
                //组件注册信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                form.findField("orgId").setReadOnly(true);
            });
        }
    },
    items: [{
        xtype: 'form',
        reference: 'orgInfoForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		//heigth: 600,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'textfield',
            //fieldLabel: '机构编号',
			fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.orgId","机构编号"),
			name: 'orgId',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            //fieldLabel: '机构名称',
			fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.orgName","机构名称"),
			name: 'orgName'
        }, {
            xtype: 'combobox',
            //fieldLabel: '有效状态',
			fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.stateStore","有效状态"),
			forceSelection: true,
            valueField: 'TValue',
            displayField: 'TSDesc',
			store: new KitchenSink.view.common.store.appTransStore("TZ_ORG_EFF_STATE"),
            typeAhead: true,
            queryMode: 'local',
			name: 'orgYxState',
            value:'Y'
        }, {
            xtype: 'textarea',
            //fieldLabel: '备注',
			fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.orgBeizhu","备注"),
			name: 'orgBeizhu'
        },/* {
            html:'<br><a href="#initOrgInfo">机构初始化设置</a><br><br>'
           // handler:'initOrg'
        }, */{
            xtype: 'textfield',
            //fieldLabel: '联系人',
			fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.orgLxrName","联系人"),
			name: 'orgLxrName'
        }, {
            xtype: 'textfield',
            //fieldLabel: '联系人电话',
			fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.orgLxrPhone","联系人电话"),
			name: 'orgLxrPhone'
        }, {
            xtype: 'textfield',
            //fieldLabel: '联系人邮箱',
			fieldLabel: Ext.tzGetResourse("TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.orgLxrEmail","联系人邮箱"),
			name: 'orgLxrEmail',
			vtype: 'email'
        }]
    }],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onJgdFormSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onJgdFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});
