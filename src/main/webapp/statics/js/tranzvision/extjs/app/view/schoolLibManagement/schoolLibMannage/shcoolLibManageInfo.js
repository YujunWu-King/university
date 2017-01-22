Ext.define('KitchenSink.view.schoolLibManagement.schoolLibMannage.schoolLibManageInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'schoolMgInfo', 
	controller: 'schoolMgConter',
	actType:'',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
	    'KitchenSink.view.schoolLibManagement.schoolLibMannage.schoolLibManageController'
	],
    title: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.schoolLibInfo","院校库详情"), 
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'schoolInform',
				layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 130,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.orgId","机构代码"), 
						name: 'orgId',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.ZHSName","中文名称"),
						name: 'chinaName',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        }, {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.ENGName","英文名称"),
						name: 'engName',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        }, {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.country","国家"),
						name: 'country',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        }, {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.maindepartment","主管部门"),
						name: 'mainDeart',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        },
        {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.maindepartment","主管部门"),
						name: 'mainDeart',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        },
        {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.city","所在城市"),
						name:'city',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        },
        {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.Level","办学层次"),
						name: 'level',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        },
        {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.attribute","属性"),
						name: 'attriBute',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        },
        {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.dec","备注"),
						name: 'adddec',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        },
        {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.State","州"),
						name: 'state',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        },
        {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.type","类型"),
						name: 'type',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        },
        {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.hemisphere","所在半球"),
						name: 'hemiHere',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        }]
    }],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onAppClassSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onAppClassEsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onAppClassClose'
	}]
});
