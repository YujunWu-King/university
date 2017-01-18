Ext.define('KitchenSink.view.interviewManagement.classBatchChoose.msBatchListWindow', {
    extend: 'Ext.window.Window',
	reference: 'msBatchListWindow',
    xtype: 'msBatchListWindow',
	width: 600,
	height: 350,
	minWidth: 300,
	minHeight: 300,
    columnLines: true,
    title: '批次选择',
	layout: 'fit',
	resizable: false,
	modal: true,
	closeAction: 'hide',
	items: [{
				xtype: 'grid',
				height: 360, 
				//frame: true,
				name: 'msBatchWindowGrid',
				columnLines: true,
				
				reference: 'msBatchWindowGrid',
				store: {
					type: 'msBatchStore'
				},
				//style:"margin:10px",
				columns: [{
					text: '序号',
					xtype: 'rownumberer',
					width:50
				}/*,{
					text: '班级编号',
					dataIndex: 'classID',
					hidden:true
				},{
					text: '批次编号',
					dataIndex: 'batchID',
					hidden:true
				}*/,{
					text: '批次名称',
					dataIndex: 'batchName',
					width:210
				},{
					text: '开始日期',
					dataIndex: 'startDate',
					width:110
				},{
					text: '结束日期',
					dataIndex: 'endDate',
					width:110
				},
				{
					xtype:'linkcolumn',
					text:"确定",
					flex:1,
					sortable:false,
					items:[{
						text:"确定",
						handler: "chooseMsBatch",
						tooltip:"确定"
					}]
				}
				]
			}],
    buttons: [{
		text: '关闭',
		iconCls:"close",
		handler: 'onWindowClose'
	}]
});
