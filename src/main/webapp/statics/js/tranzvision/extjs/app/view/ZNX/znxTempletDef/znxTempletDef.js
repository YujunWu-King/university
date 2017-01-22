Ext.define('KitchenSink.view.ZNX.znxTempletDef.znxTempletDef', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.ZNX.znxTempletDef.znxTempletController',
        'KitchenSink.view.ZNX.znxTempletDef.znxTempletStore'
    ],
    xtype: 'znxTempletDef',	
	controller: 'znxTempletController',
	store: {
        type: 'znxTempletStore'
    },
    columnLines: true,    //显示纵向表格线
    selModel: {
        type: 'checkboxmodel'   //复选框选择模式
    },
	style:"margin:8px",
    multiSelect: true,     //设置在行选择模式下支持多选
    title: '站内信模板管理',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:'saveZnxTemplet'},
					{minWidth:80,text:"确定",iconCls:"ensure",handler:'ensureZnxTemplet'},
					{minWidth:80,text:"关闭",iconCls:"close",handler:'closeZnxTemplet'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'searchComList'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addZnxTemplet'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editSelZnxTemplet'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteSelZnxTemplet'},'->'
		]
	}],
    initComponent: function () {
        var useFlagStore = new KitchenSink.view.common.store.appTransStore("TZ_USE_FLAG");
		var store = new KitchenSink.view.ZNX.znxTempletDef.znxTempletStore();
        Ext.apply(this, {
            columns: [
			{ 
                text: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_MG_STD.znxtemporg","机构"),
                sortable: false,
                dataIndex: 'znxtemporg',
				hidden:true
            },
			{ 
                text: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_MG_STD.znxtempid","模板编号"),
                sortable: true,
                dataIndex: 'znxtempid',
				width: 200
            },{
                text: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_MG_STD.znxtempname","模板名称"),
                sortable: true,
                dataIndex: 'znxtempname',
                width: 500
            },{
                text: Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_MG_STD.restempid","元模版编号"),
                sortable: true,
                dataIndex: 'restempid',
                hidden:true
            },{
                text:Ext.tzGetResourse("TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_MG_STD.isuse","是否启用"),
                sortable: true,
                dataIndex: 'isuse',
                minWidth: 80,
                renderer:function(v){
                    v=v==true?"Y":"N";
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
                flex:1,
                xtype: 'actioncolumn',
                align:'center',
			    items:[
				  {iconCls: 'edit',tooltip: '编辑',handler:'editZnxTemplet'},
				  {iconCls: 'remove',tooltip: '删除',handler:'deleteZnxTemplet'}
			   ]
            }],
			store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 20,
   				store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
});