Ext.define('KitchenSink.view.distributionTable.distributionMgList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.distributionTable.distributionMgStore',
		'KitchenSink.view.distributionTable.distributionMgController',
    ],
    xtype: 'distributionMgList',
	controller: 'distributionMgController',
	viewConfig: {markDirty: false},
    columnLines: true,
	style:"margin:8px",
    title: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_GL_STD.distributionManagement","分布对照表管理"),
	header:false,
	frame: true,
	selModel:{
		type: 'rowmodel'
	},
	dockedItems:[{xtype:"toolbar",dock:"bottom",ui:"footer",items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:'saveList'},{minWidth:80,text:"确定",iconCls:"ensure",handler:'ensureList'},{minWidth:80,text:"关闭",iconCls:"close",handler:'onGridPanelClose'}]},
		{
			xtype:"toolbar",
			items:[
				{text:Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_GL_STD.query","查询"),iconCls: "query",handler:"cfgSearchDistribution"},'-',
				{text:Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_GL_STD.add","新增"),iconCls: "add",handler:"addDistribution"}/*,'-',
				{text:Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_GL_STD.edit","编辑"),iconCls:"edit",handler:'editSelScoreModel'}*/
			]
		}
	],

    initComponent: function () {
		var store = new KitchenSink.view.distributionTable.distributionMgStore();

        Ext.apply(this, {
           columns: [{
       			text : '序号',
       			dataIndex : 'id',
       			xtype : 'rownumberer',
       			width : 50,
       			align : 'center'
           },{
                text: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_GL_STD.distrId","分布对照表编号"),
                sortable: true,
                dataIndex: 'distrId',
                width: 130,
                minWidth: 130,
                flex:1
            },{ 
				text: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_GL_STD.distrName","分布对照表名称"),
				dataIndex: 'distrName', 
				minWidth: 200,
				flex:2
            },{
				   xtype: 'actioncolumn',
				   menuDisabled: true,
				   sortable: false,
				   width:60,
				   align: 'center',
				   items:[
					   {
						   iconCls: 'edit',
						   tooltip: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_GL_STD.edit","编辑"),
						   handler: 'editSelData'
					   },{
						   iconCls: 'delete',
						   tooltip: Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_GL_STD.delete","删除"),
						   handler: 'deleteSelData'
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

