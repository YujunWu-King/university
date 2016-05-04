Ext.define('KitchenSink.view.batch.batchServerDefnPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'batchDefn', 
	controller: 'batchServerDefnMngController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*'
	],
    title: '进程服务器定义',
	bodyStyle:'overflow-y:auto;overflow-x:hidden', 
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'batchDefnForm',
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
            labelWidth: 125,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'combobox',
			fieldLabel:Ext.tzGetResourse("TZ_BATCH_SERVER_COM.TZ_BATCH_SERVD_STD.orgId","归属机构"),
			forceSelection: true,
			editable: false,
			store: new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_JG_BASE_T',
				condition:{
					TZ_JG_EFF_STA:{
						value:"Y",
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_JG_ID,TZ_JG_NAME'
			}),
            valueField: 'TZ_JG_ID',
            displayField: 'TZ_JG_NAME',
            //typeAhead: true,
            queryMode: 'remote',
			name: 'orgId',
			afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        },{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_BATCH_SERVER_COM.TZ_BATCH_SERVD_STD.batchServerName","进程服务器名称"),
			name: 'batchServerName',
			maxLength: 30,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_BATCH_SERVER_COM.TZ_BATCH_SERVD_STD.batchServerIP","服务器IP地址"),
			name: 'batchServerIP',
			maxLength: 20,
            afterLabelTextTpl: [
                                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_BATCH_SERVER_COM.TZ_BATCH_SERVD_STD.batchServerDesc","进程服务器描述"),
			name: 'batchServerDesc',
			maxLength: 254
        },{
        	xtype: 'combobox',
			fieldLabel:Ext.tzGetResourse("TZ_BATCH_SERVER_COM.TZ_BATCH_SERVD_STD.czxtLx","操作系统类型"),
			editable:false,
			emptyText:'请选择',
			queryMode: 'remote',
			name: 'czxtLx',
			valueField: 'TValue',
			displayField: 'TSDesc',
			store: new KitchenSink.view.common.store.appTransStore("TZ_CZXT_LX")
        }, {
            xtype: 'numberfield',
            fieldLabel: Ext.tzGetResourse("TZ_BATCH_SERVER_COM.TZ_BATCH_SERVD_STD.rwxhJg","任务循环读取间隔时间"),
			name: 'rwxhJg',
			minValue: 0,
			maxLength: 10,
			value: 0
        }, {
            xtype: 'numberfield',
            fieldLabel: Ext.tzGetResourse("TZ_BATCH_SERVER_COM.TZ_BATCH_SERVD_STD.zdbxRws","任务循环读取间隔时间"),
			name: 'zdbxRws',
			minValue: 0,
			maxLength: 10,
			value: 0
        },{
        	xtype: 'textarea',
        	fieldLabel:Ext.tzGetResourse("TZ_BATCH_SERVER_COM.TZ_BATCH_SERVD_STD.beiZhu","备注信息"),
        	name: 'beiZhu',
        	maxLength: 254
        },{
			layout: {
				type: 'column'
			},
			items:[{
				columnWidth:.45,
				xtype: 'combobox',
				fieldLabel: Ext.tzGetResourse("TZ_BATCH_SERVER_COM.TZ_BATCH_SERVD_STD.yxzt", "运行状态"),
				name: 'yxzt',
				store: {
					fields: ['yxztValue', 'yxztDesc'],
					data : [
					        {"yxztValue":"STOPPED", "yxztDesc":"已停止"},
					        {"yxztValue":"STOPPING", "yxztDesc":"停止中"},
					        {"yxztValue":"RUNNING", "yxztDesc":"运行中"},
					        {"yxztValue":"STARTING", "yxztDesc":"启动中"}
					]
				},
				readOnly: true,
				displayField: 'yxztDesc',
			    valueField: 'yxztValue'
			},{
				columnWidth:.02,
				html: "&nbsp;&nbsp;&nbsp;&nbsp;"
			},{
				text: '启动',
				columnWidth:.10,
				xtype: 'button',
				iconCls:"save",
				handler: 'startBatchServer'
			},{
				columnWidth:.02,
				html: "&nbsp;&nbsp;&nbsp;&nbsp;"
			},{
				text: '停止',
				columnWidth:.10,
				xtype: 'button',
				iconCls:"save",
				handler: 'stopBatchServer'
			}]
        }]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onBatchServerDefnSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onBatchServerDefnEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onBatchServerDefnClose'
	}]
});
