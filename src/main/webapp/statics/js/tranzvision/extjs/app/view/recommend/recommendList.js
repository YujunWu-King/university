Ext.define('KitchenSink.view.recommend.recommendList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.recommend.recommendListController',
        'KitchenSink.view.recommend.recommendListStore'
    ],
    xtype: 'recommendMg',
    controller: 'recommendMg',
	reference: 'recommendListGridPanal',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '推荐人管理',
    viewConfig: {
        enableTextSelection: true
    },
	plugins: {
		ptype: 'cellediting',
		pluginId: 'artHdListCellEditing'
	},
		header:false,
		frame: true,
   dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:[
				'->',{minWidth:80,text:"关闭",iconCls:"close",handler:"onComRegClose"}
			]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'cfgSearchAct'},"-",
		]
	}],
    initComponent: function () {   
    		var store = new KitchenSink.view.recommend.recommendListStore();    										
    		 
        Ext.apply(this, {
            columns: [{ 
                text: '班級ID',
                dataIndex: 'classId',
                hidden: true
            },{ 
                text: '班級名称',
                dataIndex: 'className',
				flex: 1
            },{
                text: '批次',
                sortable: true,
                dataIndex: 'batchName',
                flex: 1
            },{
                text: '入学年份',
                sortable: true,
                dataIndex: 'entranceYear',
                formatter: 'date("Y")',
                flex:1,
//            },{
//                text: '申请状态',
//                sortable: true,
//                dataIndex: 'applyState',
//                flex:1
            },{
            	text:'推荐人管理',
                xtype:'linkcolumn',
                menuDisabled:true,
                hideable:false,
                items:[{
                    getText:function(v, meta, rec) {
                        return this.text;
                    },
                    handler: "viewRecommend"
                }],
                flex:1
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
