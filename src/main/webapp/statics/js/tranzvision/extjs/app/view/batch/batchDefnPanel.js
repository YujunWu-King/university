Ext.define('KitchenSink.view.batch.batchDefnPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'batchDefn', 
	controller: 'batchDefnMngController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*'
	],
    title: '进程定义',
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
			fieldLabel:Ext.tzGetResourse("TZ_BATCH_DEFN_COM.TZ_BATCH_DEFN_STD.orgId","归属机构"),
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
            fieldLabel: Ext.tzGetResourse("TZ_BATCH_DEFN_COM.TZ_BATCH_DEFN_STD.batchName","进程名称"),
			name: 'batchName',
			maxLength: 30,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_BATCH_DEFN_COM.TZ_BATCH_DEFN_STD.batchDecs","进程描述"),
			name: 'batchDecs',
			maxLength: 254,
            afterLabelTextTpl: [
                                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                            ],
            allowBlank: false
        },{
        	xtype: 'combobox',
			fieldLabel:Ext.tzGetResourse("TZ_BATCH_DEFN_COM.TZ_BATCH_DEFN_STD.yxptLx","运行平台类型"),
			editable:false,
			emptyText:'请选择',
			queryMode: 'remote',
			name: 'yxptLx',
			valueField: 'TValue',
			displayField: 'TSDesc',
			store: new KitchenSink.view.common.store.appTransStore("TZ_YXPT_LX")
        },{
        	xtype: 'textarea',
        	fieldLabel:Ext.tzGetResourse("TZ_BATCH_DEFN_COM.TZ_BATCH_DEFN_STD.beiZhu","备注信息"),
        	name: 'beiZhu',
        	maxLength: 254
        },{
        	xtype: 'textfield',
        	fieldLabel:Ext.tzGetResourse("TZ_BATCH_DEFN_COM.TZ_BATCH_DEFN_STD.javaClass","进程对应JavaClass"),
        	name: 'javaClass',
        	maxLength: 254,
            afterLabelTextTpl: [
                                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                            ],
            allowBlank: false
        },{
			layout: {
				type: 'column'
			},
			items:[{
				columnWidth:.45,
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_BATCH_DEFN_COM.TZ_BATCH_DEFN_STD.comId", "注册到组件"),
				name: 'comId',
				editable: false,
				triggers: {
					clear: {
						cls: 'x-form-clear-trigger',
						handler: 'clearPmtSearchCom'
					},
					search: {
						cls: 'x-form-search-trigger',
						handler: "pmtSearchCom"
					}
				}
			},{
				columnWidth:.55,
				xtype: 'displayfield',
				hideLabel: true,
				style:'margin-left:5px',
				name: 'comName'
			}]
        }]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onBatchDefnSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onBatchDefnEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onBatchDefnClose'
	}]
});
