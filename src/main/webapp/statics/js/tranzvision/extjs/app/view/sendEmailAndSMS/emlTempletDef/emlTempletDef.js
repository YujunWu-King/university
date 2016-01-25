Ext.define('KitchenSink.view.sendEmailAndSMS.emlTempletDef.emlTempletDef', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.sendEmailAndSMS.emlTempletDef.emlTempletController',
        'KitchenSink.view.sendEmailAndSMS.emlTempletDef.emlTempletStore'
    ],
    xtype: 'emlTempletDef',	
	controller: 'emlTempletController',
	store: {
        type: 'emlTempletStore'
    },
    columnLines: true,    //显示纵向表格线
    selModel: {
        type: 'checkboxmodel'   //复选框选择模式
    },
	style:"margin:8px",
    multiSelect: true,     //设置在行选择模式下支持多选
    title: '机构邮件模板管理',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:'saveEmlTemplet'},
					{minWidth:80,text:"确定",iconCls:"ensure",handler:'ensureEmlTemplet'},
					{minWidth:80,text:"关闭",iconCls:"close",handler:'closeEmlTemplet'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'searchComList'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addEmlTemplet'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editSelEmlTemplet'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteSelEmlTemplet'},'->',
			{
				xtype:'splitbutton',
				text:'更多操作',
				iconCls:  'list',
				glyph: 61,
				menu:[{
					text:'初始化设置默认模版',
					handler:'resetAllTemplet'
				}]
			}
		]
	}],
    initComponent: function () {
        var useFlagStore = new KitchenSink.view.common.store.appTransStore("TZ_USE_FLAG");
		var store = new KitchenSink.view.sendEmailAndSMS.emlTempletDef.emlTempletStore();
        Ext.apply(this, {
            columns: [
			{ 
                text: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_MG_STD.emltemporg","机构"),
                sortable: false,
                dataIndex: 'emltemporg',
				hidden:true
            },
			{ 
                text: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_MG_STD.emltempid","邮件模板编号"),
                sortable: true,
                dataIndex: 'emltempid',
				width: 180
            },{
                text: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_MG_STD.emltempname","邮件模板名称"),
                sortable: true,
                dataIndex: 'emltempname',
                width: 280
            },{
                text: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_MG_STD.restempid","元模版编号"),
                sortable: true,
                dataIndex: 'restempid',
                hidden:true
            },{
                text: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_MG_STD.restempname","模板类型"),
                sortable: true,
                dataIndex: 'restempname',
                width: 280
            },{
                text:Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_MG_STD.isuse","是否启用"),
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
				  {iconCls: 'edit',tooltip: '编辑',handler:'editEmlTemplet'},
				  {iconCls: 'remove',tooltip: '删除',handler:'deleteEmlTemplet'}
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