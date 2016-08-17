Ext.define('KitchenSink.view.artTypeManagement.artTypeList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.artTypeManagement.artTypeController',
        'KitchenSink.view.artTypeManagement.artTypeModel',
		'KitchenSink.view.artTypeManagement.artTypeStore'
    ],
    xtype: 'artTypeList',	
	controller: 'artTypeController',
	store: {
        type: 'artTypeStore'
    },
    columnLines: true,    //显示纵向表格线
    selModel: {
        type: 'checkboxmodel'   //复选框选择模式
    },
	style:"margin:8px",
    multiSelect: true,     //设置在行选择模式下支持多选
    title: '内容类型管理',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:'saveArtType'},
					{minWidth:80,text:"确定",iconCls:"ensure",handler:'ensureArtType'},
					{minWidth:80,text:"关闭",iconCls:"close",handler:'closeArtType'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'searchArtTypeList'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addArtType'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editSelArtType'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteSelArtType'}
		]
	}],
    initComponent: function () {
        var useFlagStore = new KitchenSink.view.common.store.appTransStore("TZ_USE_FLAG");
		var store = new KitchenSink.view.artTypeManagement.artTypeStore();
        Ext.apply(this, {
            columns: [{
                text: '内容类型编号',
                sortable: true,
                dataIndex: 'artTypeId',
				width: 150
            },{
                text: '内容类型名称',
                sortable: true,
                dataIndex: 'artTypeName',
                width: 200,
				flex:1
            },{
                text: '是否启用',
                sortable: true,
                dataIndex: 'isused',
                minWidth: 50,
                renderer:function(v){
                    var rec = useFlagStore.find('TValue',v,0,false,true,false);
                    if(rec>-1){
                        return useFlagStore.getAt(rec).get("TSDesc");
                    }else{
                        return "";
                    }
                }
            },{
              	menuDisabled: true,
                sortable: false,
                align:'center',
			    width:60,
                xtype: 'actioncolumn',
			    items:[
				  {iconCls: 'edit',tooltip: '编辑',handler:'editArtType'},
				  {iconCls: 'remove',tooltip: '删除',handler:'deleteArtType'}
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