Ext.define('KitchenSink.view.interviewManagement.interviewManage.msMgrBatchListWin', {
    extend: 'Ext.window.Window',
	reference: 'msMgrBatchListWin',
    xtype: 'msMgrBatchListWin',
	width: 600,
	height: 350,
	minWidth: 300,
	minHeight: 300,
    columnLines: true,
    title:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_CHSBAT_STD.winTitle","批次选择") ,
	layout: 'fit',
	resizable: true,
	modal: true,
	closeAction: 'hide',
	items: [{
				xtype: 'grid',
				height: 360, 
				//frame: true,
				name: 'msMgrBatchWinGrid',
				columnLines: true,
				
				reference: 'msMgrBatchWinGrid',
				store: {
						type: 'msMgrBatchStore'
				},
				//style:"margin:10px",
				columns: [{
					text: Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_CHSBAT_STD.rowNum","序号"),
					xtype: 'rownumberer',
					width:50
				},
				/*
				{
					text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_CHSBAT_STD.classID","班级编号"),
					dataIndex: 'classID',
					hidden:true
				},{
					text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_CHSBAT_STD.batchID","批次编号") ,
					dataIndex: 'batchID',
					hidden:true
				},
				*/
				{
					text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_CHSBAT_STD.batchName","批次名称") ,
					dataIndex: 'batchName',
					width:210
				},{
					text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_CHSBAT_STD.startDate","开始日期") ,
					dataIndex: 'startDate',
					width:110
				},{
					text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_CHSBAT_STD.endDate","结束日期"),
					dataIndex: 'endDate',
					width:110
				},
				{
					xtype:'linkcolumn',
					text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_CHSBAT_STD.linkEnsureTip","确定"),
					flex:1,
					sortable:false,
					items:[{
						text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_CHSBAT_STD.linkEnsureItemTip","确定"),
						handler: "chooseMsBatch",
						tooltip:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_CHSBAT_STD.linkEnsureItemTip","确定")
					}]
				}
				]
			}],
    buttons: [{
		text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_CHSBAT_STD.butClose","关闭") ,
		iconCls:"close",
		handler: 'onWindowClose'
	}]
});
