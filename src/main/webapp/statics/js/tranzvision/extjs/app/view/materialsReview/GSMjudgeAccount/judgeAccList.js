Ext.define('KitchenSink.view.materialsReview.GSMjudgeAccount.judgeAccList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.materialsReview.GSMjudgeAccount.judgeAccStore',
		'KitchenSink.view.materialsReview.GSMjudgeAccount.judgeAccController',
		'KitchenSink.view.materialsReview.GSMjudgeAccount.judgeAccInfoWin',
		'KitchenSink.view.materialsReview.GSMjudgeAccount.judgeTypeStore'
    ],
    xtype: 'GSMjudgeAccountMg',
	controller: 'GSMjudgeAccController',
    columnLines: true,
	style:"margin:8px",
	selModel: {
       	type: 'checkboxmodel'
    },
    multiSelect: false,
    title: '评委账号管理',
	header:false,
	frame: true,
    dockedItems:[
		{
			xtype:"toolbar",
			items:[
				{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchJudgeAcc"},'-',
				{text:"新增",tooltip:"新增数据",iconCls: "add",handler:"addNewJudgeAcc"},'-',
				{text:"编辑",tooltip:"编辑数据",iconCls: "edit",handler:"editSelJudgeAcc"},'-',
				{text:"删除",tooltip:"批量删除数据",iconCls: "remove",handler:"deleteJudgeBat"}
			]
		},
		{
			xtype:"toolbar",
			dock:"bottom",
			ui:"footer",
			items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:"onSave"}]
		}
	],
    initComponent: function () {    
		var store = new KitchenSink.view.materialsReview.GSMjudgeAccount.judgeAccStore();
		
        Ext.apply(this, {
           columns: [{ 
                text: '评委账号',
                sortable: true,
                dataIndex: 'accountNo',
				width: 100
            },{
                text: '评委姓名',
                sortable: true,
                dataIndex: 'judgeName',
               width: 150

            },{
                text: '评委类型',
                sortable: true,
                dataIndex: 'judTypeDesc',
                minWidth: 150,
				flex: 1
            },{
               text: '评委手机',
               sortable: true,
               dataIndex: 'judPhoneNumber',
               minWidth: 150,
               flex: 1
           },{
               text: '评委邮箱',
               sortable: true,
               dataIndex: 'judEmail',
               minWidth: 150,
               flex: 1
           },{
               text: 'OA系统账号',
               sortable: true,
               dataIndex: 'judOAID',
               minWidth: 150,
               flex: 1
           },{
               text: '评委所属系',
               sortable: true,
               dataIndex: 'judDepart',
               minWidth: 150,
               flex: 1
           },{
               text: '操作',
			   menuDisabled: true,
               sortable: false,
			   draggable:false,
			   width:80,
			   align: 'center',
               xtype: 'actioncolumn',
			   items:[
			   			{
							text: '编辑',
							iconCls: 'edit',
							tooltip: '编辑',
                			handler: "editCurrRow"
						},
					  	{
							text: '删除',
							iconCls: 'remove',
							tooltip: '删除',
                    		handler: "deleteCurrRow"
						}
			   ]
             }],
			store:store,
			bbar: {
				xtype: 'pagingtoolbar',
				pageSize: 10,
				store:store,
				/*
				displayInfo: true,
				displayMsg: '显示{0}-{1}条，共{2}条',
				beforePageText: '第',
				afterPageText: '页/共{0}页',
				emptyMsg: '没有数据显示',
				*/
				plugins: new Ext.ux.ProgressBarPager()
			}	 
        });
		
        this.callParent();
    }
});



