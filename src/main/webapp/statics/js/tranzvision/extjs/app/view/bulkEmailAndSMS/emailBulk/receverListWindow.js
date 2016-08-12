Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.receverListWindow', {
    extend: 'Ext.window.Window',
	reference: 'receverListWindow',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.bulkEmailAndSMS.emailBulk.receverListStore'
    ],
	title:'查看收件人' ,
    width: 650,
    height: 400,
    layout: 'fit',
    resizable: true,
    modal: true,
	constructor: function (config) {
		this.emlQfId = config.emlQfId;
		this.sendModel = config.sendModel;
		
		this.callParent();	
	},
	initComponent: function () {
		var store = new KitchenSink.view.bulkEmailAndSMS.emailBulk.receverListStore({
				emlQfId : this.emlQfId,
				sendModel : this.sendModel
			});
		
		Ext.apply(this, {
			items: [{
				xtype: 'grid',
				columnLines: true,
				store: store,
				columns: [{
					xtype:'rownumberer'	
				},{
					text: '收件人邮箱',
					dataIndex: 'email',
					minWidth: 130,
					flex: 1	
				}],
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 50,
					listeners:{
						afterrender: function(pbar){
							var grid = pbar.findParentByType("grid");
							pbar.setStore(grid.store);
						}
					},
					reference: 'adminUserToolBar',
					plugins: new Ext.ux.ProgressBarPager()
				}
			}],
		});
        this.callParent();
    },
	buttons: [{
		text: '关闭',
		iconCls:"close",
		handler: 'onWinClose'
	}]
});
