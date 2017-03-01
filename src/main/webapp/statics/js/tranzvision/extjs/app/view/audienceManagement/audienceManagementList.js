Ext.define('KitchenSink.view.audienceManagement.audienceManagementList', {
    extend: 'Ext.grid.Panel',
  //  extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.audienceManagement.audienceManagementModel',
        'KitchenSink.view.audienceManagement.audienceManagementStore',
        'KitchenSink.view.audienceManagement.newAudWindowStore',
		'KitchenSink.view.audienceManagement.audienceManagementController'
		
    ],
    xtype: 'audienceManagementDa',
	controller: 'audienceManagementController',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
 //   multiSelect: true,
    title: '听众管理',
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
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editPageRegInfo"},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"deleteComRegInfos"}
		]
	}],
    initComponent: function () { 
	    //组件注册信息列表
    	
    	
	    var store = new KitchenSink.view.audienceManagement.audienceManagementStore();
	    
	    var orgid = Ext.tzOrgID;
	    
	//    console.log(orgid);
	//    var store = new KitchenSink.view.audienceManagement.newAudWindowStore();
        Ext.apply(this, {
            columns: [
                      
            { 
                text: '听众ID',
                hidden:true,
                dataIndex: 'audId',
				Width: 400,
				flex:1
            },{ 
                text: '听众描述',
                hidden:true,
                dataIndex: 'audMS',
				Width: 400,
				flex:1
            },{ 
                text: '听众来源',
                hidden:true,
                dataIndex: 'audLY',
				Width: 400,
				flex:1
            },{ 
                text: '听众SQL',
                hidden:true,
                dataIndex: 'audSQL',
				Width: 400,
				flex:1
            },{ 
                text: '听众名称',
                dataIndex: 'audName',
				Width: 400,
				flex:1
            },{
                text: '听众类型',
                sortable: true,
                dataIndex: 'audType',
                minWidth: 260,
                renderer:function(v) {
           		 if (v == '1') {
           			 return "动态听众";
           		 } else if (v == '2') {
           			 return "静态听众";
           		 }
           	 }
				//flex:1
            },
            {
                text: '有效状态',
                sortable: true,
                dataIndex: 'audStat',
                minWidth: 260,
                renderer:function(v) {
              		 if (v == '1') {
              			 return "有效";
              		 } else if (v == '2') {
              			 return "无效";
              		 }
              	 }
				//flex:1
            },
            
            {
               menuDisabled: true,
               sortable: false,
			   width:60,
               align:'center',
               xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '编辑',handler: 'editSelComRegInfo'},
				//  {iconCls: 'edit',tooltip: '编辑',handler: 'editPageRegInfoOne'},
			   	//  {iconCls: 'remove',tooltip: '删除',handler: 'deleteComRegInfo'}
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
