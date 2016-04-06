Ext.syncRequire("KitchenSink.view.template.proTemplate.newBmlcmbXq");
Ext.define('KitchenSink.view.template.proTemplate.newBmlcmb', {
	extend: 'Ext.window.Window',
    controller: 'proTemplate',
	//xtype: 'myBmbRegWindow1',
	reference: 'myBmbRegWindow1',
	title: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.xjmb","新建模板"),
	closable: true,
	modal: true,
	autoScroll: true,
	bodyStyle: 'padding: 5px;',
	actType: 'add',
	items: [
		//new KitchenSink.view.template.proTemplate.newBmlcmbXq()
		{
			xtype: 'side-navigation-tabs_bmlc',
		}
		],
	buttons: [{
		text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.ensure","确定"),
		iconCls: "ensure",
		handler: 'proonNewEnsure'
	},
	{
		text: Ext.tzGetResourse("TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMB_STD.close","关闭"),
		iconCls: "close",
		handler: 'onNewClose'
	}],
	onNewClose: function(btn) {
		var win = btn.findParentByType("window");
		win.destory();
	}
});
