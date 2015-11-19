Ext.define('KitchenSink.view.sendEmailAndSMS.smsTempletDef.smsTempletDef', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.sendEmailAndSMS.smsTempletDef.smsTempletController',
		'KitchenSink.view.sendEmailAndSMS.smsTempletDef.smsTempletModel',
        'KitchenSink.view.sendEmailAndSMS.smsTempletDef.smsTempletStore'
    ],
    xtype: 'smsTempletDef',	
	controller: 'smsTempletController',
	store: {
        type: 'smsTempletStore'
    },
    columnLines: true,    //显示纵向表格线
    selModel: {
        type: 'checkboxmodel'   //复选框选择模式
    },
	style:"margin:8px",
    multiSelect: true,     //设置在行选择模式下支持多选
    title: '机构短信模板管理',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:'saveSmsTemplet'},
					{minWidth:80,text:"确定",iconCls:"ensure",handler:'ensureSmsTemplet'},
					{minWidth:80,text:"关闭",iconCls:"close",handler:'closeSmsTemplet'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'searchComList'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addSmsTemplet'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editSelSmsTemplet'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteSelSmsTemplet'},'->',
			{
				xtype:'splitbutton',
				text:'更多操作',
				iconCls:  'list',
				glyph: 61,
				menu:[{
					text:Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_MG_STD.initDefaultTemp","初始化设置默认模版"),
					handler:'resetAllTemplet'
				}]
			}
		]
	}],
    initComponent: function () {
        var useFlagStore = new KitchenSink.view.common.store.appTransStore("TZ_USE_FLAG");
		var store = new KitchenSink.view.sendEmailAndSMS.smsTempletDef.smsTempletStore();
        Ext.apply(this, {
            columns: [
			{ 
                text: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_MG_STD.smstemporg","机构"),
                sortable: false,
                dataIndex: 'smstemporg',
				hidden:true
            },
			{ 
                text: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_MG_STD.smstempid","短信模板编号"),
                sortable: true,
                dataIndex: 'smstempid',
				width: 200
            },{
                text: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_MG_STD.smstempname","短信模板名称"),
                sortable: true,
                dataIndex: 'smstempname',
                width: 300
            },{
                text: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_MG_STD.restempid","元模版编号"),
                sortable: true,
                dataIndex: 'restempid',
                hidden:true
            },{
                text: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_MG_STD.restempname","模版类型"),
                sortable: true,
                dataIndex: 'restempname',
                width: 300
            },{
                text:Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_MG_STD.isuse","是否启用"),
                sortable: true,
                dataIndex: 'isuse',
                minWidth: 80,
                renderer:function(v) {
                   v = v == true ? "Y" : "N";
                    var rec = useFlagStore.find('TValue', v, 0, false, true, false);
                    if (rec > -1) {
                        return useFlagStore.getAt(rec).get("TSDesc");
                    } else {
                        return "";
                    }
                 }
                },{
              	menuDisabled: true,
                sortable: false,
			    width:50,
                xtype: 'actioncolumn',
			    items:[
				  {iconCls: 'edit',tooltip: '编辑',handler:'editSmsTemplet'},
				  {iconCls: 'remove',tooltip: '删除',handler:'deleteSmsTemplet'}
			   ]
            }],
			store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 20,
   				store: store,
                displayInfo: true,
				displayMsg: '显示{0}-{1}条，共{2}条',
				beforePageText: '第',
                afterPageText: '页/共{0}页',
				emptyMsg: '没有数据显示',
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
});