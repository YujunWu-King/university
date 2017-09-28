Ext.define('KitchenSink.view.weChatLog.weChatLogInfo', {
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
    y:-10,
//    autoScroll：true,
    minWidth: 650,
    minHeight: 300,
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
        	columns:2,
        },
        fieldDefaults: {
            msgTarget: 'side',
            labelStyle: 'font-weight:bold'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
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
            queryMode: 'local',
            colspan: 2
        },
        {
            xtype: 'displayfield',
            fieldLabel: '发送人',
            name: 'sendPsn',
            readOnly:true,
            columnWidth: 1,
            width: 300,
        },
        {
            xtype: 'displayfield',
            fieldLabel: '发送时间',
            name: 'sendDTime',
            readOnly:true,
            columnWidth: 1,
            width: 300,
        },
        {
            xtype: 'displayfield',
            fieldLabel: '发送状态',
            name: 'sendState',
            readOnly:true,
            columnWidth: 1,
            width: 300,
        },
        {
        	xtype: 'displayfield',
            fieldLabel: '成功时间',
            name: 's_DT',
            readOnly:true,
            columnWidth: 1,
            width: 300,
            //format: 'Y-m-d'
        },
        {
            xtype: 'displayfield',
            fieldLabel: '发送总数',
            name: 's_total',
            readOnly:true,
            columnWidth: 1,
            width: 300,
        },
        {
        	columnWidth: 0.5,
            xtype: 'displayfield',
            fieldLabel: '过滤数',
            name: 's_fiter',
            readOnly:true,  
            columnWidth: 1,
            width: 300,
        },
        {
        	columnWidth: 0.5,
            xtype: 'displayfield',
            fieldLabel: '成功数',
            name: 's_suceuss',
            readOnly:true,
            columnWidth: 1,
            width: 300,

        },
        {
        	columnWidth: 0.5,
            xtype: 'displayfield',
            fieldLabel: '失败数',
            name: 's_fail',
            readOnly:true,
            columnWidth: 1,
            width: 300,
        },
       {
            xtype: 'textarea',
            fieldLabel: '发送内容',
            name: 'content',
            readOnly:true,
            width: 600,
            colspan: 2,
        },
        {
            xtype: 'displayfield',
            fieldLabel: '素材编号',
            name: 'mediaId',
            readOnly:true,
            colspan: 2,
            hidden:true
//        },{
//        	xtype: 'textfield',
//            fieldLabel: '图片路径',
//            name: 'filePath',
//            hidden:true
//        },{
//            xtype: "image",
//            src: ""	,
//            name: "titileImage",
//            height: 86, 
//            style:'margin-left:105px;'
        }
        ]
    },
//    {
//        xtype: "image",
//        src: ""	,
//        name: "titileImage",
//        height:186, 
//        style:'margin-left:105px;'
//    },
    
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
			minWidth: 250,
			flex: 1
		},{
			text: '昵称',
			dataIndex: 'nickName',
			minWidth: 200,
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
