Ext.define('KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletDef', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletController',
        'KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletStore'
    ],
    xtype: 'resTempletDef',	
	controller: 'resTempletController',
	store: {
        type: 'resTempletStore'
    },
    columnLines: true,    //显示纵向表格线
    selModel: {
        type: 'checkboxmodel'   //复选框选择模式
    },
	style:"margin:8px",
    multiSelect: true,     //设置在行选择模式下支持多选
    title: '函件元模板管理',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:'saveResTemplet'},
					{minWidth:80,text:"确定",iconCls:"ensure",handler:'ensureResTemplet'},
					{minWidth:80,text:"关闭",iconCls:"close",handler:'closeResTemplet'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'searchComList'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addResTemplet'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editSelResTemplet'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteSelResTemplet'}
		]
	}],
    initComponent: function () {
        var useFlagStore = new KitchenSink.view.common.store.appTransStore("TZ_USE_FLAG");
		var store = new KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletStore();
        Ext.apply(this, {
            columns: [{ 
                text: '元模板编号',
                sortable: true,
                dataIndex: 'restempid',
				width: 100
            },{
                text: '元模板名称',
                sortable: true,
                dataIndex: 'restempname',
                width: 200,
				flex:1
            },{
                text: '所属机构',
                dataIndex: 'restemporg',
				hidden:true
            },{
                text: '所属机构',
                sortable: true,
                dataIndex: 'orgname',
                width: 150
            },{
                text: '是否启用',
                sortable: true,
                dataIndex: 'isneed',
                minWidth: 50,
                renderer:function(v){
                    v=v?"Y":"N";
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
				  {iconCls: 'edit',tooltip: '编辑',handler:'editResTemplet'},
				  {iconCls: 'remove',tooltip: '删除',handler:'deleteResTemplet'}
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