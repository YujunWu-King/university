Ext.define('KitchenSink.view.common.drExcel.drExcelLog', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.common.drExcel.drExcelController',
		'KitchenSink.view.common.drExcel.drExcelLogModel',
        'KitchenSink.view.common.drExcel.drExcelLogStore'
    ],
    xtype: 'drExcelLog',
    controller: 'drExcel',
	/*store: {
        type: 'drExcelLogStore'
    },*/
    columnLines: true,
    title: '导入日志记录',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    /*dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:"saveFilterInfos"}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query"},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addFilter'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteFilter'},"-",
			{text:"图片上传",tooltip:"图片上传处理",iconCls:"add",handler:"picClipping"},"-",
			{text:"导入Excel",tooltip:"导入Excel",iconCls:"save",handler:"importExcel"}
		]
	}],*/
    initComponent: function () {    
    	var store = new KitchenSink.view.common.drExcel.drExcelLogStore();
        Ext.apply(this, {
            columns: [{ 
                text: '组件编号',
                dataIndex: 'ProcessID',
				width: 140,
				hidden:true
            },{
                text: '日志流水号',
                sortable: true,
                dataIndex: 'logLsh',
                width: 150
            },{ 
                text: '时间',
                dataIndex: 'dateTime',
				width: 140
            },{
                text: '原始记录',
                sortable: true,
                dataIndex: 'ysJl',
                width: 160
            },{
                text: '日志内容描述',
                sortable: true,
				dataIndex: 'logDesc',
                minWidth: 160,
				flex:1
            }],
            store:store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store:store,
				/*store: {
					type: 'drExcelLogStore'
				},*/
                displayInfo: true,
				displayMsg: '显示{0}-{1}条，共{2}条',
				beforePageText: '第',
                afterPageText: '页/共{0}页',
				emptyMsg: '没有数据显示',
                plugins: new Ext.ux.ProgressBarPager()
           }
        });
		
        this.callParent();
    }
     
});
