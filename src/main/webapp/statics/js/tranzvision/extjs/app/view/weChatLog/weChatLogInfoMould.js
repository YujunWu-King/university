Ext.define('KitchenSink.view.weChatLog.weChatLogInfoMould', {
    extend: 'Ext.window.Window',
    xtype: 'weChatLogInfo',
    reference: 'weChatLogInfo',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.AdvancedVType',
        'KitchenSink.view.weChatLog.weChatLogInfoModel',
        'KitchenSink.view.weChatLog.weChatLogInfoStore'
	],
    title: '微信服务号日志',
    bodyStyle:'overflow-y:auto;overflow-x:hidden;padding-top:10px',
    actType: 'update',
    width: 650,
//    y:10,
//    autoScroll：true,
    minWidth: 400,
    minHeight: 400,
    maxHeight: 600,
    resizable: true,
    modal:true,
    listeners:{
        resize: function(win){
            win.doLayout();
        }
    },
    viewConfig: {
        enableTextSelection: true
    },
    items: [{
        xtype: 'form',
        reference: 'weChatLogInfoForm',
        layout: {
//            type: 'vbox',
//            align: 'stretch',
            type:'table',
        	columns:2
        },
        fieldDefaults: {
            msgTarget: 'side',
            labelStyle: 'font-weight:bold'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
        	xtype: 'combo',
        	fieldLabel: '发送类型',
            name: 'sendTpye',
            readOnly:true,
            fieldStyle:'background:#F4F4F4',
            editable : false,
            valueField: 'TValue',
            editable:false,
            displayField: 'TLDesc',
            store: new KitchenSink.view.common.store.appTransStore("TZ_SEND_TYPE"),
            queryMode: 'local'	 
        },
        {
            xtype: 'textfield',
            fieldLabel: '发送人',
            name: 'sendPsn',
            readOnly:true,
            fieldStyle:'background:#F4F4F4'
        },
        {
            xtype: 'textfield',
            fieldLabel: '发送时间',
            name: 'sendDTime',
            readOnly:true,
            fieldStyle:'background:#F4F4F4'
        },
//        {
//            xtype: 'textfield',
//            fieldLabel: '发送状态',
//            name: 'sendState',
//            readOnly:true,
//            cls:'lanage_1'
//        },
//        {
//            xtype: 'textfield',
//            fieldLabel: '成功时间',
//            name: 's_DT',
//            readOnly:true,
//            cls:'lanage_1'
//        },
//        {
//            xtype: 'textfield',
//            fieldLabel: '发送总数',
//            name: 's_total',
//            readOnly:true,
//            cls:'lanage_1'
//        },
//        {
//            xtype: 'textfield',
//            fieldLabel: '过滤数',
//            name: 's_fiter',
//            readOnly:true,
//            cls:'lanage_1'
//        },
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
//       {
//            xtype: 'textfield',
//            fieldLabel: '发送内容',
//            name: 'content',
//            readOnly:true,
//            cls:'lanage_1'
//        },
//        {
//            xtype: 'textfield',
//            fieldLabel: '素材编号',
//            name: 'mediaId',
//            readOnly:true,
//            cls:'lanage_1'
//        }
        ]
    },
        {
		xtype: 'grid',
//        height:340,
        autoHeight:true,
		title: '群发用户',
		frame: true,
		columnLines: true,
		reference: 'weChatLogInfoGrid',
		style:"margin:10px",
		store: {
			type: 'weChatLogInfoStore'
		},
		columns: [{
			text: 'open_id',
			dataIndex: 'openId',
			minWidth: 100,
			flex: 1
		},{
			text: '昵称',
			dataIndex: 'nickName',
			minWidth: 50,
			flex: 1
		},{
			text: '消息内容',
			dataIndex: 'sendState',
			minWidth: 200,
			flex: 1
		},{
			text: '发送状态',
			dataIndex: 'content',
			minWidth: 50,
			flex: 1
		}
		],
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
    buttons: [/*{
		text: '确定',
		iconCls:"ensure",
		handler: 'onLogInfoEnsure'
	},*/ {
		 text: '关闭',
	        iconCls:"close",
	        handler: function(btn){
	            //获取窗口
	            var win = btn.findParentByType("window");
	            var form = win.child("form").getForm();
	            //关闭窗口
	            win.close();
	        }
	}]
});
