Ext.define('KitchenSink.view.weakPwdManage.weakPwdList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.weakPwdManage.weakPwdModel',
        'KitchenSink.view.weakPwdManage.weakPwdStore',
		'KitchenSink.view.weakPwdManage.weakPwdController'
    ],
    xtype: 'weakPwdList',
	controller: 'weakPwdController',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '弱密码口令列表',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveWeakPwdList"},
            {minWidth:80,text:'确定',iconCls:"ensure",handler: 'ensureWeakPwdList'},
            {minWidth:80,text:'关闭',iconCls:"close",handler: 'closeWeakPwdList'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchWeakPwdList"},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addWeakPwdList"},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editWeakPwdLists"},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"deleteWeakPwdLists"}
		]
	}],
    initComponent: function () { 
	    var store = new KitchenSink.view.weakPwdManage.weakPwdStore();
        Ext.apply(this, {
        	id:'weakPwdGrid',
        	reference: 'weakPwdGrid',
            columns: [{ 
                text: '弱密码ID',
                sortable: true,
                dataIndex: 'TZ_PWD_ID',
                flex:0.5
            },{
                text: '弱密码',
                sortable: true,
                dataIndex: 'TZ_PWD_VAL',
				flex:1
            },{
               menuDisabled: true,
               sortable: false,
			   width:60,
               align:'center',
               xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '编辑',handler: 'editWeakPwdList'},
			   	  {iconCls: 'remove',tooltip: '删除',handler: 'deleteWeakPwdList'}
			   ]
            }],
			store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
});
