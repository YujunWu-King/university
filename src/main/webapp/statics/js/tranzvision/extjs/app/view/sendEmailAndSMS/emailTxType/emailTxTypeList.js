Ext.define('KitchenSink.view.sendEmailAndSMS.emailTxType.emailTxTypeList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.sendEmailAndSMS.emailTxType.emailTxTypeController',
		'KitchenSink.view.sendEmailAndSMS.emailTxType.emailTxTypeModel',
        'KitchenSink.view.sendEmailAndSMS.emailTxType.emailTxTypeStore'
    ],
    xtype: 'emailTxTypeList',
	controller: 'emailTxTypeController',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '退信类别管理',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:'saveTxType'},
					{minWidth:80,text:'确定',iconCls:"ensure",handler: 'ensureTxType'},
					{minWidth:80,text:'关闭',iconCls:"close",handler: 'closeTxTypeList'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'searchTxType'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addTxType'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editTxType'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteTxType'},"-",
		]
	}],
    initComponent: function () {
		var store = new KitchenSink.view.sendEmailAndSMS.emailTxType.emailTxTypeStore();
		
		var txTypeStore = new KitchenSink.view.common.store.appTransStore("TZ_EMLTX_TYPE");
		var yxxStore = new KitchenSink.view.common.store.appTransStore("TZ_IS_USED");
		
        Ext.apply(this, {
            columns: [{ 
                text: Ext.tzGetResourse('TZ_EMLTX_TYPE_COM.TZ_EMLTX_TYPE_STD.txTypeId','退信类别ID'),
                dataIndex: 'txTypeId',
                minWidth: 120,
                flex: 1
            },{ 
                text: Ext.tzGetResourse('TZ_EMLTX_TYPE_COM.TZ_EMLTX_TYPE_STD.txTypeName','退信类别名称'),
                dataIndex: 'txTypeName',
                minWidth: 120,
                flex: 1
            },{
                text: Ext.tzGetResourse('TZ_EMLTX_TYPE_COM.TZ_EMLTX_TYPE_STD.txType','退信类型'),
                dataIndex: 'txType',
                width: 100,
                renderer:function(value,metadata,record){
    				if(value == null || value==""){
    					return "";	
    				}
    				var index = txTypeStore.find('TValue',value);   
    				if(index!=-1){   
    					   return txTypeStore.getAt(index).data.TSDesc;   
    				}   
    				return value; 
    			}
            },{
                text: Ext.tzGetResourse('TZ_EMLTX_TYPE_COM.TZ_EMLTX_TYPE_STD.isValid','有效状态'),
                dataIndex: 'isValid',
                width: 100,
                renderer:function(value,metadata,record){
    				if(value == null || value==""){
    					return "";	
    				}
    				var index = yxxStore.find('TValue',value);   
    				if(index!=-1){   
    					   return yxxStore.getAt(index).data.TSDesc;   
    				}   
    				return value; 
    			}
            },{
              	menuDisabled: true,
                sortable: false,
			    width:50,
                xtype: 'actioncolumn',
			    items:[
				  {iconCls: 'edit',tooltip: '编辑',handler:'editCurrentTxType'},
				  {iconCls: 'remove',tooltip: '删除',handler:'delCurrentTxType'}
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