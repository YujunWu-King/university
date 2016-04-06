Ext.syncRequire("KitchenSink.view.tab.SideNavigationTabs");
Ext.define('KitchenSink.view.template.bmb.myBmbRegWindow', {
	extend: 'Ext.window.Window',
	xtype: 'myBmbRegWindow',
	reference: 'myBmbRegWindow',
	title: '新建模板',
	closable: true,
	closeAction: 'hide',
	modal: true,
	autoScroll: true,
	bodyStyle: 'padding: 5px;',
	actType: 'add',
	items: [
//	        new KitchenSink.view.tab.SideNavigationTabs()
	        {
	        	xtype:'side-navigation-tabs'
	        }
	        ],
	buttons: [{
		text: '确定',
		iconCls: "ensure",
		handler: 'onBmbRegEnsure'
	},
		{
			text: '关闭',
			iconCls: "close",
			handler: 'onBmbRegClose'
		}]
});