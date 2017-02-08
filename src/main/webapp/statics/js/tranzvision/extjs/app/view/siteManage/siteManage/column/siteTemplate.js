Ext.define('KitchenSink.view.siteManage.siteManage.column.siteTemplate', {
    extend: 'Ext.window.Window',
    xtype: 'siteTemplate', 
	controller: 'siteLMInfoI',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		//'KitchenSink.view.siteManage.siteManage.column.column',
		//'KitchenSink.view.siteManage.siteManage.column.LMtemplate',
		//'KitchenSink.view.siteManage.siteManage.column.contentTy',
		//'KitchenSink.view.siteManage.siteManage.column.contentTe',
		'KitchenSink.view.siteManage.siteManage.column.siteLMController'
	],
    title: '栏目设置',
    reference:'siteTemplate',
    width:500,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
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
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'hiddenfield',
            fieldLabel: '站点ID',
			name: 'siteId'
        },{
            xtype: 'hiddenfield',
            fieldLabel: '栏目ID',
			name: 'lm_id'
        },{
            xtype: 'textfield',
            fieldLabel: '栏目名称',
			name: 'lm_name'
        },{
            xtype: 'combobox',
            fieldLabel: '栏目类型',
			name: 'lm_lx',
			queryMode: 'remote',
            editable:false,
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_ZDLM_LX")
    		
        },{
            xtype: 'combobox',
            fieldLabel: '栏目模板',
			name: 'lm_mb',
			queryMode: 'remote',
            editable:false,
			valueField: 'TZ_TEMP_ID',
    		displayField: 'TZ_TEMP_NAME'
        },{
           	xtype: 'combo',
            fieldLabel: "内容类型",
			name: 'lm_nrlx',
			emptyText:"请选择...",
            queryMode: 'remote',
            editable:false,
			valueField: 'TZ_ART_TYPE_ID',
    		displayField: 'TZ_ART_TYPE_NAME',
			store:new KitchenSink.view.common.store.comboxStore({
				recname: 'PS_TZ_ART_TYPE_T',
				condition:{
					
					IS_ENABLED_FLG:{
						value: 'Y',
							operator:"01",
							type:"01"
					}
				},
				result:'TZ_ART_TYPE_ID,TZ_ART_TYPE_NAME'
			})

        },/*{
            xtype: 'combobox',
            fieldLabel: '内容类型',
			//forceSelection: true,
            valueField: 'TValue',
            displayField: 'TSDesc',
            editable:false,
            queryMode: 'remote',
			name: 'lm_nrlx',
			store: new KitchenSink.view.common.store.appTransStore("TZ_ZD_NRLX")
			
        },*/{
            xtype: 'combobox',
            fieldLabel: '内容模板',
			name: 'lm_nrmb',
			queryMode: 'remote',
            editable:false,
			//valueField: 'TValue',
    		//displayField: 'TSDesc',
    		//store: new KitchenSink.view.common.store.appTransStore("TZ_CONT_TEMP")
    		valueField: 'TZ_TEMP_ID',
    		displayField: 'TZ_TEMP_NAME'
        },{
            xtype: 'combobox',
            fieldLabel: '有效状态',
			//forceSelection: true,
            valueField: 'TValue',
            displayField: 'TSDesc',
            editable:false,
            queryMode: 'remote',
			name: 'lm_yxzt',
			store: new KitchenSink.view.common.store.appTransStore("TZ_COLUMN_STATE")
			
        }]
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
