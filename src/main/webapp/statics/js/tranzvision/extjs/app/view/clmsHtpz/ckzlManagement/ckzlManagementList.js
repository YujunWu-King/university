Ext.define('KitchenSink.view.clmsHtpz.ckzlManagement.ckzlManagementList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.clmsHtpz.ckzlManagement.ckzlManagementModel',
        'KitchenSink.view.clmsHtpz.ckzlManagement.ckzlManagementStore',
		'KitchenSink.view.clmsHtpz.ckzlManagement.ckzlManagementController'
	//	'KitchenSink.view.audienceManagement.newAudWindowStore',
		
    ],
    xtype: 'ckzlManagementList',
	controller: 'ckzlManagementController',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
 //   multiSelect: true,
    title: '参考资料类型管理',
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
		//	{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addPageRegInfo"},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editComRegInfo"},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"deleteComRegInfos"}
		]
	}],
    initComponent: function () { 
	    //组件注册信息列表
    	
    	
	    var store = new KitchenSink.view.clmsHtpz.ckzlManagement.ckzlManagementStore();
	    		      
	    
	    var orgid = Ext.tzOrgID;
	    

        Ext.apply(this, {
            columns: [{ 
                text: '参考资料编号',
                dataIndex: 'ckzlid',
				Width: 400,
				flex:1
            },{
                text: '参考资料名称',
                dataIndex: 'ckzlName',
				Width: 400,
				flex:1,
                minWidth: 260
            },{
                text: 'java类',
                dataIndex: 'java',
				Width: 400,
				flex:1,
                minWidth: 260
            },{
               menuDisabled: true,
               sortable: false,
			   width:60,
               align:'center',
               xtype: 'actioncolumn',
			   items:[
				//  {iconCls: 'edit',tooltip: '编辑',handler: 'editSelComRegInfo'},
				  {iconCls: 'edit',tooltip: '编辑',handler: 'editPageRegInfoOne'},
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
