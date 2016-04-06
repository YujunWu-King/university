Ext.syncRequire("KitchenSink.view.enrollProject.submitDtMdlMg.newDjzlXq");
Ext.define('KitchenSink.view.enrollProject.submitDtMdlMg.newDjzl', {
	extend: 'Ext.window.Window',
	//xtype: 'myBmbRegWindow1137',
	//controller:'smtDtMdlController',
	reference: 'myBmbRegWindow1137',
	title:Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.newTpl","新建模板"),
	closable: true,
	modal: true,
	autoScroll: true,
	bodyStyle: 'padding: 5px;',
	actType: 'add',
	items: [
		//new KitchenSink.view.enrollProject.submitDtMdlMg.newDjzlXq()
		{
			xtype: 'side-navigation-tabs_djzl',
		}
		],
	buttons: [{
		text: Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.ensure","确定"),
		iconCls: "ensure",
		handler: 'onNewEnsure_1'
	},
	{
		text:Ext.tzGetResourse("TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.close","关闭"),
		iconCls: "close",
		handler: 'onNewCloseZlgl'
	}],
	onNewCloseZlgl: function(btn) {
		var win = btn.findParentByType("window");
		win.destory();
	}
});
