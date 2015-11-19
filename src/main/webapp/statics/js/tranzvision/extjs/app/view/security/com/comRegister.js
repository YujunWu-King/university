Ext.define('KitchenSink.view.security.com.comRegister', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.security.com.comModel',
        'KitchenSink.view.security.com.comStore',
		'KitchenSink.view.security.com.comRegController'
    ],
    xtype: 'comReg',
	controller: 'comReg',
	/*store: {
        type: 'comStore'
    },*/
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '功能组件注册',
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
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveComRegInfos"},
            {minWidth:80,text:'确定',iconCls:"ensure",handler: 'ensureComRegInfos'},
            {minWidth:80,text:'关闭',iconCls:"close",handler: 'closeComRegInfos'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchComList"},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addComRegInfo"},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editComRegInfo"},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"deleteComRegInfos"}
		]
	}],
    initComponent: function () { 
	    //组件注册信息列表
	    var store = new KitchenSink.view.security.com.comStore();
        Ext.apply(this, {
            columns: [{ 
                text: '组件ID',
                dataIndex: 'comID',
				width: 240
            },{
                text: '组件名称',
                sortable: true,
                dataIndex: 'comName',
                minWidth: 100,
				flex:1
            },{
               menuDisabled: true,
               sortable: false,
			   width:60,
               align:'center',
               xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '编辑',handler: 'editSelComRegInfo'},
			   	  {iconCls: 'remove',tooltip: '删除',handler: 'deleteComRegInfo'}
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
