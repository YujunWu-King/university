Ext.define('KitchenSink.view.bulkEmailAndSMS.searchStu.searchStuWin', {
    extend: 'Ext.window.Window',
    reference: 'searchStuWin',
    xtype: 'searchStuWin',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bulkEmailAndSMS.searchStu.searchStuStore'
    ],
    width: 800,
    minHeight:350,
    maxHeight:600,
    ignoreChangesFlag: true,
    style:"margin:8px",
    title: '搜索考生',
    layout: 'fit',
    resizable: false,
    modal: true,
	constructor: function (config) {
		this.taskType = config.taskType;
		this.callParent();
	},
    initComponent: function () {
		var tType = this.taskType;
		var store = new KitchenSink.view.bulkEmailAndSMS.searchStu.searchStuStore(tType);
		var columns = [];
		
		columns.push({
				text: "序号",
				width: 70,
				align:'center',
				xtype: 'rownumberer'
			},{
				text: "机构",
				dataIndex: 'jgId',
				hidden: true	
			},{
				text: "oprid",
				dataIndex: 'oprId',
				hidden: true	
			},{
				text: "姓名",
				dataIndex: 'oprName',
				width: 100
			},{
				text: "手机",
				dataIndex: 'phone',
				width: 150
			},{
				text: "邮箱",
				dataIndex: 'email',
				width: 150
			},{
				text: "班级",
				dataIndex: 'className',
				width: 200,
				flex:1	
			});

		/*
		columns.push({
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
		*/
        Ext.apply(this, {
            items: [{
				xtype: 'grid',
                columnLines: true,
				selModel: {
					selType : 'checkboxmodel',
  					mode : 'MULTI'
				},
				viewConfig: {
					enableTextSelection: true
				},
				store:store,
				columns: columns,
				dockedItems:[{
					xtype:"toolbar",
					items:[{
                        text:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.tbarQuery","查询"),
                        tooltip:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.tbarTipQuery","查询数据"),
                        iconCls:"query",
                        handler:'searchStuList'
                    }]
				 }],
				 bbar: {
					 xtype: 'pagingtoolbar',
					 pageSize: 20,
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
        handler: 'selectStu'
    },{
        text: '关闭',
        iconCls:"close",
        handler: 'onWinClose'
    }]
});


