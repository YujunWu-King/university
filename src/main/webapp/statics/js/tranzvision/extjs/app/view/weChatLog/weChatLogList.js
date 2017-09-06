Ext.define('KitchenSink.view.weChatLog.weChatLogList', {
    extend: 'Ext.panel.Panel',
    xtype: 'weChatLogList',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.AdvancedVType',
        'KitchenSink.view.weChatLog.weChatLogListStore',
        'KitchenSink.view.weChatLog.weChatLogListController'
	],
    title: '微信服务号日志',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',
    controller: 'weChatLogListController',
    items: [{
        xtype: 'form',
        reference: 'weChatLogForm',
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
            fieldLabel: '机构ID',
			name: 'jgId',
            vtype:'toUppercase',
            maxLength: 30,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: '服务号ID',
			name: 'appId'
        }]
    },
        {
		xtype: 'grid',
        height:340,
        autoHeight:true,
		title: '服务号日志',
		frame: true,
		columnLines: true,
        dockedItems:{
            xtype:"toolbar",
            items:[
                {text:"查询",tooltip:"查询数据",iconCls:"query",handler:'queryPlstCom'},"-",
                {text:"查看",tooltip:"查看详细信息",iconCls:"view",handler:'getLogInfo'}
            ]
        },
        selModel: {
            type: 'checkboxmodel'
        },
		reference: 'weChatLogListGrid',
		style:"margin:10px",
		store: {
			type: 'weChatLogListStore'
		},
		columns: [{
            text: '机构',
            dataIndex: 'jgId',
            hidden:true
        },{
            text: '微信APPID',
            dataIndex: 'appId',
            hidden:true
        },{
            text: '序号',
            dataIndex: 'XH',
            hidden:true
        },{
            text: '类型',
            dataIndex: 'sendType',
        },{
			text: '发送时间',
			dataIndex: 'sendDTime',
			minWidth: 250
		},{
			text: '发送人',
			dataIndex: 'sendPSN',
			minWidth: 200,
			flex: 1
		},{
			text: '发送状态',
			dataIndex: 'sendState',
			minWidth: 200,
			flex: 1
		},{
            menuDisabled: true,
            sortable: false,
            align:'center',
            width:50,
            xtype: 'actioncolumn',
            items:[
                {iconCls: 'preview',tooltip: '查看',handler:'getLogInfo'}
                //,{iconCls: 'remove',tooltip: '删除',handler:'deleteCurrPlstCom'}
            ]
        }],
		bbar: {
			xtype: 'pagingtoolbar',
			pageSize: 5,
			reference: 'weChatLogListBar',
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
		handler: 'onPlstInfoEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onPlstInfoClose'
	}]
});
