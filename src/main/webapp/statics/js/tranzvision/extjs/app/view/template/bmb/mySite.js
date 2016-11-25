Ext.define('KitchenSink.view.template.bmb.mySite', {
    extend: 'Ext.window.Window',
    xtype: 'mySite',
    controller: 'myBmb',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.bmb.myBmbController'
	],
    title: '选择招生站点', 
    reference:'mySite',
    width:800,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	//actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'sitetempAccountForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        // frame: true,
        border: false,
        bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		buttonAlign: 'center',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 120,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'hiddenfield',
            //xtype: 'textfield',
            readOnly:true,
            fieldLabel: '报名表模板编号',
			name: 'tplId'
        },{
        	xtype: 'combobox',
        	fieldLabel: '站点',
        	emptyText:'请选择',
        	queryMode: 'remote',
        	editable:false,
        	name: 'siteId',
        	valueField: 'siteId',
        	displayField: 'siteName'
        }]
    }],
    buttons: [{
		text: '确定',
		iconCls:"ensure",
		handler: 'onFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});