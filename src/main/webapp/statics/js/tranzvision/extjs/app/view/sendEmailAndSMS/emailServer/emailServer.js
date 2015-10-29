Ext.define('KitchenSink.view.sendEmailAndSMS.emailServer.emailServer', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.sendEmailAndSMS.emailServer.emailServerController',
		'KitchenSink.view.sendEmailAndSMS.emailServer.emailServerModel',
        'KitchenSink.view.sendEmailAndSMS.emailServer.emailServerStore'
    ],
    xtype: 'emailSet',
	controller: 'emailServerMg',
	store: {
        type: 'emailServerStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '邮件参数设置',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:'saveEmailServer'},
					{minWidth:80,text:'确定',iconCls:"ensure",handler: 'ensureEmailServer'},
					{minWidth:80,text:'关闭',iconCls:"close",handler: 'closeEmailServer'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'searchComList'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addEmailServer'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editSelEmailServer'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteSelEmailServer'},"-",
		]
	}],
    initComponent: function () {
		var store = new KitchenSink.view.sendEmailAndSMS.emailServer.emailServerStore();
        Ext.apply(this, {
            //store: 'Companies',
            columns: [{ 
                text: Ext.tzGetResourse('TZ_EMLSER_MG_COM.TZ_EMLSER_MG_STD.emailservid','邮箱服务器编号'),
                sortable: true,
                dataIndex: 'emailservid',
				width: 80,
				hidden:true
            },{ 
                text: Ext.tzGetResourse('TZ_EMLSER_MG_COM.TZ_EMLSER_MG_STD.emailaddr','电子邮箱'),
                sortable: true,
                dataIndex: 'emailaddr',
				width: 300
            },{
                text: Ext.tzGetResourse('TZ_EMLSER_MG_COM.TZ_EMLSER_MG_STD.emailorg','机构ID'),
                sortable: true,
                dataIndex: 'emailorg',
				hidden:true
            },{
                text: Ext.tzGetResourse('TZ_EMLSER_MG_COM.TZ_EMLSER_MG_STD.emailorgName','机构'),
                sortable: true,
                dataIndex: 'emailorgName',
                width: 150
            },{
                text: Ext.tzGetResourse('TZ_EMLSER_MG_COM.TZ_EMLSER_MG_STD.chnsname','中文简称'),
                sortable: true,
                dataIndex: 'chnsname',
                width: 300
            },{
                text: Ext.tzGetResourse('TZ_EMLSER_MG_COM.TZ_EMLSER_MG_STD.emlalias','邮箱别名'),
                sortable: true,
                dataIndex: 'emlalias',
                minWidth: 120,
				flex:1
            },{
              	menuDisabled: true,
                sortable: false,
			    width:50,
                xtype: 'actioncolumn',
			    items:[
				  {iconCls: 'edit',tooltip: '编辑',handler:'editEmailServer'},
				  {iconCls: 'remove',tooltip: '删除',handler:'deleteEmailServer'}
			   ]
            }],
			store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				store: store,
				/*
                displayInfo: true,
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