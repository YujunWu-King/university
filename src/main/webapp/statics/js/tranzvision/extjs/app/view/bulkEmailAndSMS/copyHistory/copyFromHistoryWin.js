Ext.define('KitchenSink.view.bulkEmailAndSMS.copyHistory.copyFromHistoryWin', {
    extend: 'Ext.window.Window',
    reference: 'copyFromHistoryWin',
    xtype: 'copyFromHistoryWin',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bulkEmailAndSMS.copyHistory.copyFromHistoryStore'
    ],
    width: 800,
    minHeight:350,
    maxHeight:600,
    ignoreChangesFlag: true,
    style:"margin:8px",
    title: '我创建的历史短信任务',
    layout: 'fit',
    resizable: false,
    modal: true,
	constructor: function (config) {
		this.taskType = config.taskType;
		this.callParent();
	},
    initComponent: function () {
		var tType = this.taskType;
		var store = new KitchenSink.view.bulkEmailAndSMS.copyHistory.copyFromHistoryStore(tType);
		var columns = [];
		
		columns.push({
				text: "序号",
				width: 60,
				align:'center',
				xtype: 'rownumberer'
			},{
				text: "群发任务名称",
				dataIndex: 'qfRwName',
				width: 200,
				flex:1	
			});
		if(tType=="SMS"){
			this.title="我创建的历史短信任务";
		}else{
			this.title="我创建的历史邮件任务";
			columns.push({
				text: "邮件主题",
				dataIndex: 'emlTheme',
				width: 120,
				flex:1	
			});
		}
		
		columns.push(/*{
				text: "所属部门",
				dataIndex: 'deptDesc',
				width: 100,
			},*/{
				text: "发送模式",
				dataIndex: 'sendModal',
				width: 120,
				renderer: function(val){
					if(val == "NOR"){
						return "一般发送";	
					}else{
						return "导入Excel发送";	
					}	
				}	
			},{
				text: "创建时间",
				dataIndex: 'createDttm',
				width: 140,
				flex:1	
			});
		
        Ext.apply(this, {
            items: [{
				xtype: 'grid',
                columnLines: true,
				selModel: {
					selType : 'checkboxmodel',
  					mode : 'SINGLE'
				},
				viewConfig: {
					enableTextSelection: true
				},
				store:store,
				columns: columns,
				dockedItems:[{
					xtype:"toolbar",
					items:[{
						xtype:"textfield",
						reference: 'searchHistoryContent',
						emptyText: '请输入任务名称搜索...',
						width:250
					},{text:"搜索",iconCls: "query",handler: "searchHistoryList"}]
				 }],
				 bbar: {
					 xtype: 'pagingtoolbar',
					 pageSize: 10,
					 listeners:{
						 afterrender: function(pbar){
							 var grid = pbar.findParentByType("grid");
							 pbar.setStore(grid.store);
						 }
					 },
				 	 plugins: new Ext.ux.ProgressBarPager()
				}
			}]
        });
        this.callParent();
    },
    buttons: [ {
        text: '确定',
        iconCls:"ensure",
        handler: 'onSelectHistory'
    },{
        text: '关闭',
        iconCls:"close",
        handler: 'onWinClose'
    }]
});


