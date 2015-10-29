Ext.define('KitchenSink.view.siteManage.siteManage.area.typeManges.siteAreaTypeInfoPanel',{
    extend: 'Ext.window.Window',
    xtype: 'siteAreaTypeInfoPanel', 
	controller: 'typeInfo',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.siteManage.siteManage.area.sitestateStroe',
        'KitchenSink.view.siteManage.siteManage.area.typeManges.areaTypeInfoController'
	],
    title: '区域类型设置',
    reference:'siteAreaTypeInfoPanel',
    width:600,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'areaTypeForm',
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
            readOnly:true,
            fieldLabel: '站点模板编号',
			name: 'siteId',
        	},{
            xtype: 'hiddenfield',
            name: 'areatypeid',
            readOnly:true,
            fieldLabel: '区域类型编号'
        },{
            xtype: 'combobox',
            fieldLabel: '状态',
            emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			name: 'areatypestate',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_AREA_TYPE_STATE")
        },{
            xtype: 'textfield',
            fieldLabel: '区域类型名称',
			name: 'areatypename'
        },{
            xtype: 'combobox',
            fieldLabel: '区域类型标识',
            emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			name: 'areatypearea',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_AREA_TYPE")
        },{
            xtype: 'textfield',
            fieldLabel: '业务人员设置区域时的处理程序类',
            labelWidth: 250,
			name: 'areasetcode'
        },{
            xtype: 'textfield',
            fieldLabel: '区域代码展示的处理程序类',
            labelWidth: 250,
			name: 'areahtmlcode'
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
