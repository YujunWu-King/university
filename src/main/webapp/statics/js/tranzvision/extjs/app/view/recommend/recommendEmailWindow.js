Ext.define('KitchenSink.view.recommend.recommendEmailWindow', {
    extend: 'Ext.window.Window',
	reference: 'recommendWindow',
    xtype: 'recommendWindow',
	width: 600,
	height: 350,
	minWidth: 300,
	minHeight: 300,
	controller:'recommendCon',
    columnLines: true,
    title: '邮件发送史',
	layout: 'fit',
	resizable: false,
	modal: true,
	closeAction: 'hide',
	items: [{
				xtype: 'grid',
				height: 360, 
				name: 'recommendWindowGrid',
				columnLines: true,
				
				reference: 'recommendWindowGrid',
				store: {
					type: 'recommendEmailStore'
				},
				columns: [{
					text: '序号',
					xtype: 'rownumberer',
					width:50
				},{
					text: '收件人邮箱',
					dataIndex: 'email',
					minWidth: 120,
					flex:1
				},{
					text: '发送日期',
					dataIndex: 'sendDate',
					minWidth: 120,
					flex: 1	
				},{
					text: '发送状态',
					dataIndex: 'sendState',
					width:110
				},
				]
			}],
    buttons: [{
		text: '关闭',
		iconCls:"close",
		handler: 'onWindowClose'
	}]
});