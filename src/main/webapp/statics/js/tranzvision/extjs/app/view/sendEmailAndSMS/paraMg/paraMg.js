Ext.define('KitchenSink.view.sendEmailAndSMS.paraMg.paraMg', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.sendEmailAndSMS.paraMg.paraMgController',
		'KitchenSink.view.sendEmailAndSMS.paraMg.paraModel',
        'KitchenSink.view.sendEmailAndSMS.paraMg.paraStore'
    ],
    xtype: 'paraMg',	
	controller: 'paraMgController',
	store: {
        type: 'paraStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '函件参数管理',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:'saveParaInfo'},
					{minWidth:80,text:'确定',iconCls:"ensure",handler: 'ensureParaInfo'},
					{minWidth:80,text:'关闭',iconCls:"close",handler: 'closeParaInfo'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'searchComList'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addParaInfo'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editSelParaInfo'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteSelParaInfo'},"-",
		]
	}],
    initComponent: function () {    
		var store = new KitchenSink.view.sendEmailAndSMS.paraMg.paraStore();
        Ext.apply(this, {
            columns: [{ 
                text: Ext.tzGetResourse('TZ_PARA_MG_COM.TZ_PARA_MG_STD.paraid','参数编号'),
                sortable: true,
                dataIndex: 'paraid',
				width: 300
            },{
                text: Ext.tzGetResourse('TZ_PARA_MG_COM.TZ_PARA_MG_STD.chaname','中文名称'),
                sortable: true,
                dataIndex: 'chaname',
                minWidth: 200,
				flex:1
            },{
                text: Ext.tzGetResourse('TZ_PARA_MG_COM.TZ_PARA_MG_STD.datatype','数据类型'),
                sortable: true,
                dataIndex: 'datatype',
                minWidth: 100,
            },{
              	menuDisabled: true,
                sortable: false,
			    width:50,
                xtype: 'actioncolumn',
			    items:[
				  {iconCls: 'edit',tooltip: '编辑',handler:'editParaInfo'},
				  {iconCls: 'remove',tooltip: '删除',handler:'deleteParaInfo'}
			   ]
            }],
			store: store,
            bbar: {
                xtype: 'pagingtoolbar',
				store: store,
                pageSize: 10,
                displayInfo: true,
				/*
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