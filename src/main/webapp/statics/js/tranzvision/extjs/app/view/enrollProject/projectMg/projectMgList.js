Ext.define('KitchenSink.view.enrollProject.projectMg.projectMgList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.enrollProject.projectMg.projectMgStore',
		'KitchenSink.view.enrollProject.projectMg.projectMgController',
		'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'projectMg',
	controller: 'projectMgController',
	viewConfig: {markDirty: false},
    columnLines: true,
	style:"margin:8px",
    title: Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.projectManagement","项目管理"),
	header:false,
	frame: true,
	selModel:{
		type: 'checkboxmodel'
	},
	dockedItems:[
		{
			xtype:"toolbar",
			items:[
				{text:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.query","查询"),iconCls: "query",handler:"cfgSearchProject"},'-',
				{text:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.add","新增"),iconCls: "add",handler:"addNewProject"},'-',
				{text:Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.edit","编辑"),iconCls:"edit",handler:'editOptProject'}
			]
		},
		{
			xtype:"toolbar",
			dock:"bottom",
			ui:"footer",
			items:['->',
				{
					minWidth:80,
					text:Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_PROMG_STD.close","关闭"),
					iconCls:"close",
					handler: 'onComRegClose'}]
		}
	],

    initComponent: function () {
		var store = new KitchenSink.view.enrollProject.projectMg.projectMgStore();
		//开通标识
		var openStore = new KitchenSink.view.common.store.appTransStore("TZ_XMGL_ISOPEN");
		
		this.cellEditing = new Ext.grid.plugin.CellEditing({
            clicksToEdit: 1
        });
		
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
						   tooltip: Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.edit","编辑"),
						   handler: 'editProjectInfo'
					   }
				   ]
			   }],
			
			store:store,
			bbar: {
				xtype: 'pagingtoolbar',
				pageSize: 10,
				store:store,
				plugins: new Ext.ux.ProgressBarPager()
			}
        });
		
        this.callParent();
    }
});

