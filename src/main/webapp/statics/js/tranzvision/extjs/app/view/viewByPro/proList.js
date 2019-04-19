Ext.define('KitchenSink.view.viewByPro.proList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.viewByPro.proListController',
		'KitchenSink.view.viewByPro.proListModel',
        'KitchenSink.view.viewByPro.proListStore'
    ],
    xtype: 'hardCode',
	controller: 'proList',
	store: {
        type: 'proListStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '按项目查看',
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
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveHardCodeInfos"},
            {minWidth:80,text:"确定",iconCls:"ensure",handler:"ensureHardCodeInfos"},
            {minWidth:80,text:"关闭",iconCls:"close",handler:"closeHardCodeInfos"}
        ]
		},{
		xtype:"toolbar",
		items:[
		    {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryHardCode'},/*"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addHardCode'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editHardCode'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteHardCode'}*/
		]
	}],
    initComponent: function () {  
	   var store = new KitchenSink.view.viewByPro.proListStore();
	   
	 //开通标识
		var openStore = new KitchenSink.view.common.store.appTransStore("TZ_XMGL_ISOPEN");
	   
        Ext.apply(this, {
            columns: [/*{
			 	xtype: 'rownumberer',
				text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.seq","序号")
			},*/{
                text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.projectId","项目编号"),
                sortable: true,
                dataIndex: 'projectId',
                minWidth: 130
				/*items:[{
					getText: function(v, meta, rec) {
						return v;
					},
					handler: "editProjectInfo"
				}],*/
            },{ 
				text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.projectName","项目名称"),
				dataIndex: 'projectName', 
				minWidth: 200,
				flex:1
            },{
                text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.projectType","项目类别"),
                sortable: true,
                dataIndex: 'projectType',
                minWidth: 130
            },{
				text: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.usedStatus","开通状态"),	
				/*
				xtype: 'checkcolumn',
				dataIndex: 'usedStatus',
				sortable: false,
				minWidth:100,
				listeners: {
					"beforecheckchange": function(){
						return false;
					}
				}
				*/
				dataIndex: 'usedStatus',
				minWidth:100,
				renderer: function(value,metadata,record){	
			 		var index = openStore.find('TValue',value);   
					if(index!=-1){   
						   return openStore.getAt(index).data.TSDesc;   
					}   
					return record.get('statusDesc');  
				} 
			},
			   {
				   xtype: 'actioncolumn',
				   menuDisabled: true,
				   sortable: false,
				   width:60,
				   align: 'center',
				   items:[
					   {
						   iconCls: 'edit',
						   tooltip: Ext.tzGetResourse("TZ_BY_PRO_COM.TZ_PRO_LIST_PAGE.edit","查看"),
						   handler: 'editClassInfo'
					   }
				   ]
			   }],
			store:store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				/*store: {
					type: 'hardCodeStore'
				},*/
				store:store,
                displayInfo: true,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
});
