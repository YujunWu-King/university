Ext.define('KitchenSink.view.sendEmailAndSMS.emailTxType.txRuleDefnList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.sendEmailAndSMS.emailTxType.txRuleDefnController',
		'KitchenSink.view.sendEmailAndSMS.emailTxType.txRuleModel',
        'KitchenSink.view.sendEmailAndSMS.emailTxType.txRuleStore'
    ],
    xtype: 'txRuleDefnList',
	controller: 'txRuleDefnController',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '退信条件定义',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:'saveTxRule'},
					{minWidth:80,text:'确定',iconCls:"ensure",handler: 'ensureTxRule'},
					{minWidth:80,text:'关闭',iconCls:"close",handler: 'closeTxRuleList'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'searchTxRule'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addTxRule'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editTxRule'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteTxRule'},"-",
		]
	}],
    initComponent: function () {
		var store = new KitchenSink.view.sendEmailAndSMS.emailTxType.txRuleStore();
		
		var matchTypeStore = new KitchenSink.view.common.store.appTransStore("TZ_TX_MATCH_TYPE");
		var yxxStore = new KitchenSink.view.common.store.appTransStore("TZ_IS_USED");
		
        Ext.apply(this, {
            columns: [{ 
                text: Ext.tzGetResourse('TZ_EMLTX_RULE_COM.TZ_EMLTX_RULE_STD.ruleId','退信条件ID'),
                dataIndex: 'ruleId',
                minWidth: 120,
                flex: 1
            },{ 
                text: Ext.tzGetResourse('TZ_EMLTX_RULE_COM.TZ_EMLTX_RULE_STD.ruleName','退信条件名称'),
                dataIndex: 'ruleName',
                minWidth: 120,
                flex: 1
            },{
                text: Ext.tzGetResourse('TZ_EMLTX_RULE_COM.TZ_EMLTX_RULE_STD.compareType','匹配类型'),
                dataIndex: 'compareType',
                width: 100,
                renderer:function(value,metadata,record){
    				if(value == null || value==""){
    					return "";	
    				}
    				var index = matchTypeStore.find('TValue',value);   
    				if(index!=-1){   
    					   return matchTypeStore.getAt(index).data.TSDesc;   
    				}   
    				return value; 
    			}
            },{
                text: Ext.tzGetResourse('TZ_EMLTX_RULE_COM.TZ_EMLTX_RULE_STD.status','有效状态'),
                dataIndex: 'status',
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
                text: Ext.tzGetResourse('TZ_EMLTX_RULE_COM.TZ_EMLTX_RULE_STD.keyword','匹配关键字'),
                dataIndex: 'keyword',
                minWidth: 140,
                flex: 2
            },{
              	menuDisabled: true,
                sortable: false,
			    width:50,
                xtype: 'actioncolumn',
			    items:[
				  {iconCls: 'edit',tooltip: '编辑',handler:'editCurrentTxRule'},
				  {iconCls: 'remove',tooltip: '删除',handler:'delCurrentTxRule'}
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