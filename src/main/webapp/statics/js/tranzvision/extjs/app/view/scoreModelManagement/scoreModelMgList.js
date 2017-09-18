Ext.define('KitchenSink.view.scoreModelManagement.scoreModelMgList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.scoreModelManagement.scoreModelMgStore',
		'KitchenSink.view.scoreModelManagement.scoreModelMgController',
		'tranzvision.extension.grid.column.Link',
		'KitchenSink.view.scoreModelManagement.copyScoreModelWin'
    ],
    xtype: 'scoreModelMgList',
	controller: 'scoreModelMgController',
	viewConfig: {markDirty: false},
    columnLines: true,
	style:"margin:8px",
    title: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.scoreModalManagement","成绩模型管理"),
	header:false,
	frame: true,
	selModel:{
		type: 'checkboxmodel'
	},
	dockedItems:[
		{
			xtype:"toolbar",
			items:[
				{text:Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.query","查询"),iconCls: "query",handler:"cfgSearchScoreModel"},'-',
				{text:Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.add","新增"),iconCls: "add",handler:"addNewScoreModel"},'-',
				{text:Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.edit","编辑"),iconCls:"edit",handler:'editSelScoreModel'}
				/*,'-',{text:'测试自动初筛',iconCls:"set",handler:'testAutoScreen'}*/
			]
		},
		{
			xtype:"toolbar",
			dock:"bottom",
			ui:"footer",
			items:['->',
				{
					minWidth:80,
					text:Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.close","关闭"),
					iconCls:"close",
					handler: 'onGridPanelClose'}]
		}
	],

    initComponent: function () {
		var store = new KitchenSink.view.scoreModelManagement.scoreModelMgStore();

        Ext.apply(this, {
           columns: [{
                text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.modeId","成绩模型ID"),
                sortable: true,
                dataIndex: 'modeId',
                width: 130,
                minWidth: 130,
                flex:1
            },{ 
				text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.modeDesc","描述"),
				dataIndex: 'modeDesc', 
				minWidth: 200,
				flex:2
            },{
                text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.treeName","树名称"),
                sortable: true,
                dataIndex: 'treeName',
                width: 130,
                minWidth: 130,
                flex:1
            },{
                text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.status","状态"),
                sortable: true,
                dataIndex: 'status',
                width: 120,
                minWidth: 120
            },{
				   xtype: 'actioncolumn',
				   menuDisabled: true,
				   sortable: false,
				   width:60,
				   align: 'center',
				   items:[
					   {
						   iconCls: 'edit',
						   tooltip: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.edit","编辑"),
						   handler: 'editScoreModel'
					   },{
						   iconCls: 'copy',
						   tooltip: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.copy","复制"),
						   handler: 'copyScoreModel'
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

