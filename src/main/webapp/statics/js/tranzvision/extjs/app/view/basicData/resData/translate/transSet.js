Ext.define('KitchenSink.view.basicData.resData.translate.transSet', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.basicData.resData.translate.transModel',
        'KitchenSink.view.basicData.resData.translate.transStore',
		'KitchenSink.view.basicData.resData.translate.transSetController'
		//'KitchenSink.view.basicData.resData.translate.transDefineController'
    ],
    xtype: 'tranSetMg',
	controller: 'tranSetMg',
	store: {
        type: 'transStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '转换值集合信息列表',
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
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveTranslates"},
            {minWidth:80,text:"确定",iconCls:"ensure",handler:"ensureTranslates"},
            {minWidth:80,text:"关闭",iconCls:"close",handler:"closeTranslates"}
        ]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls:"query",handler:'queryTranslate'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addTranslate'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editTranslate'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteTranslates'}
		]
	}],
    initComponent: function () { 
	   var store = new KitchenSink.view.basicData.resData.translate.transStore();
        Ext.apply(this, {
            columns: [{ 
                text: '转换值集合编号',
                dataIndex: 'transSetID',
				width: 180
            },{
                text: '描述',
				dataIndex: 'transSetDesc',
                minWidth: 160,
				flex:1
            },{
               menuDisabled: true,
               sortable: false,
			   width:60,
               align:'center',
               xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '编辑',handler:'editSelTranslate'},
			   	  {iconCls: 'remove',tooltip: '删除',handler:'deleteSelTranslate'}
			   ]
            }],
			store:store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				/*store: {
					type: 'transStore'
				},*/
				store:store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
});
