Ext.define('KitchenSink.view.weChatLog.weChatLogInfo', {
    extend: 'Ext.window.Window',
    xtype: 'weChatLogInfo',
//	requires: [
//	    'Ext.data.*',
//        'Ext.grid.*',
//        'Ext.util.*',
//        'Ext.toolbar.Paging',
//        'Ext.ux.ProgressBarPager',
//        'KitchenSink.AdvancedVType',
//        'KitchenSink.view.weChatLog.weChatLogInfoStore',
//        'KitchenSink.view.weChatLog.weChatLogListController'
//	],
    title: '微信服务号日志',
    width: 600,
    height: 520,
    minWidth: 300,
    minHeight: 380,
    layout: 'fit',
    resizable: true,
    modal: true,
    closeAction: 'destroy',
	actType: 'add',
    controller: 'weChatLogListController',
    items: [{
        xtype: 'form',
        reference: 'weChatInfoForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		//heigth: 600,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
        	xtype: 'textfield',
        	fieldLabel: '发送类型',
            name: 'sendTpye',
            readOnly:true,
            cls:'lanage_1'
        },
        {
            xtype: 'textfield',
            fieldLabel: '发送人',
            name: 'sendPsn',
            readOnly:true,
            cls:'lanage_1'
        },
        {
            xtype: 'textfield',
            fieldLabel: '发送时间',
            name: 'sendDTime',
            readOnly:true,
            cls:'lanage_1'
        },
        {
            xtype: 'textfield',
            fieldLabel: '发送状态',
            name: 'sendState',
            readOnly:true,
            cls:'lanage_1'
        },
        {
            xtype: 'textfield',
            fieldLabel: '成功时间',
            name: 's_DT',
            readOnly:true,
            cls:'lanage_1'
        },
        {
            xtype: 'textfield',
            fieldLabel: '发送总数',
            name: 's_total',
            readOnly:true,
            cls:'lanage_1'
        },
        {
            xtype: 'textfield',
            fieldLabel: '过滤数',
            name: 's_fiter',
            readOnly:true,
            cls:'lanage_1'
        },
        {
            xtype: 'textfield',
            fieldLabel: '成功数',
            name: 's_suceuss',
            readOnly:true,
            cls:'lanage_1'
        },
        {
            xtype: 'textfield',
            fieldLabel: '失败数',
            name: 's_fail',
            readOnly:true,
            cls:'lanage_1'
        },
       {
            xtype: 'checkboxfield',
            fieldLabel: '发送内容',
            name: 'context',
            hideLabel:true
        }]
    },
        {
		xtype: 'grid',
        height:340,
        autoHeight:true,
		title: '群发用户',
		frame: true,
		columnLines: true,
//        dockedItems:{
//            xtype:"toolbar",
//            items:[
//                {text:"查询",tooltip:"查询数据",iconCls:"query",handler:'queryIn'},"-",
//                {text:"查看",tooltip:"查看详细信息",iconCls:"view",handler:'viewLogInfo'}
//            ]
//        },
//        selModel: {
//            type: 'checkboxmodel'
//        },
		reference: 'weChatLogInfoGrid',
		style:"margin:10px",
		store: {
			type: 'weChatLogInfoStore'
		},
		columns: [{
			text: 'open_id',
			dataIndex: 'openId',
			minWidth: 250,
			flex: 1
		},{
			text: '昵称',
			dataIndex: 'nickName',
			minWidth: 200,
			flex: 1
		},{
            menuDisabled: true,
            sortable: false,
            align:'center',
            width:50,
            xtype: 'actioncolumn',
            items:[
                //{iconCls: 'preview',tooltip: '查看',handler:'viewLogInfo'}
                //,{iconCls: 'remove',tooltip: '删除',handler:'deleteCurrPlstCom'}
            ]
        }],
		bbar: {
			xtype: 'pagingtoolbar',
			pageSize: 5,
			reference: 'weChatLogInfoBar',
            listeners:{
                afterrender: function(pbar){
                    var grid = pbar.findParentByType("grid");
                    pbar.setStore(grid.store);
                }
            },
			plugins: new Ext.ux.ProgressBarPager()
		}
	}],
    buttons: [{
		text: '确定',
		iconCls:"ensure",
		handler: 'onLogInfoEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onLogInfoClose'
	}]
});
