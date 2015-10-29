Ext.define('KitchenSink.view.siteManage.siteManage.area.siteareaInfoPanel',{
    extend: 'Ext.window.Window',
    xtype: 'siteareaInfoPanel', 
	controller: 'siteAreaInfo1',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.siteManage.siteManage.area.sitestateStroe',
		'KitchenSink.view.siteManage.siteManage.area.areatypeStroe',
		'KitchenSink.view.siteManage.siteManage.area.areapositionStore',
		'KitchenSink.view.siteManage.siteManage.area.arealmStore',
		'KitchenSink.view.siteManage.siteManage.area.siteAreaController'
	],
    title: '区域设置',
    reference:'siteareaInfoPanel',
    width:600,
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
            readOnly:true,
            fieldLabel: '站点模板编号',
			name: 'siteId'
        	},{
            xtype: 'textfield',
            readOnly:true,
			hidden: true,
            fieldLabel: '区域编号',
			name: 'areaid',
			value: ' '
        	},{
            xtype: 'textfield',
            fieldLabel: '区域名称',
			name: 'areaname'
        },{
            xtype: 'combobox',
            fieldLabel: '状态',
            emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			name: 'areastate',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_AREA_STATE")
        },{
            xtype: 'combobox',
            fieldLabel: '区域类型',
			emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			name: 'areatypeid',
			valueField: 'TZ_AREA_TYPE_ID',
    		displayField: 'TZ_AREA_TYPE_NAME'
        }, {
            xtype: 'combobox',
            fieldLabel: '区域位置',
            emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			name: 'areaposition',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_AREA_POSITION")
        }, {
            xtype: 'numberfield',
            fieldLabel: '区域顺序号',
            minValue: 1,
			name: 'areasxh'
        }, {
            xtype: 'combobox',
            fieldLabel: '对应栏目',
			forceSelection: true,
			emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			name: 'arealm',
			valueField: 'TZ_COLU_ID',
    		displayField: 'TZ_COLU_NAME'
        }, {
            xtype: 'textareafield',  
            grow: true,
            height:100,
            fieldLabel: '区域源代码',
            preventScrollbars : false,
            name: 'areacode'
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
