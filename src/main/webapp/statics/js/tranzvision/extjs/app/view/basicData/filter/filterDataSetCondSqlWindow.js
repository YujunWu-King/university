Ext.define('KitchenSink.view.basicData.filter.filterDataSetCondSqlWindow', {
    extend: 'Ext.window.Window',
    requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
	],
    xtype: 'filterWindow', 
    title: 'SQL显示预览', 
	reference: 'filterDataSetCondSqlWindow',
    y:20,
    width: 740,
    modal:true,
    layout: 'fit',
    resizable: true,
	items: [{
		xtype: 'form',	
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
        bodyPadding: 10,
		//heigth: 600,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		defaults: {
			msgTarget: 'side',
			labelWidth: 90,
			labelStyle: 'font-weight:bold'
		},
		items: [{
			xtype: 'textarea',
			fieldLabel: '',
			height:300,
			name: 'previewsql',
			readOnly:true
		}]
	}],
    buttons: [{
		text: '关闭',
		iconCls:"close",
		handler: 'onFilterDataSetCondSqlWinClose'
	}]
});
