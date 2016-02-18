Ext.define('KitchenSink.view.basicData.resData.message.messageInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'messageInfo',
	controller: 'msgInfoController',
    reference:'messageInfo',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.basicData.resData.message.msgInfoController',
        'KitchenSink.view.basicData.resData.message.messageInfoStore'
	],
    title: '消息集合定义',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    lanageType:TranzvisionMeikecityAdvanced.Boot.language,//语言类型，默认中文
	orgId:'ADMIN',
    items: [{
        xtype: 'form',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding:10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 120,
            labelStyle: 'font-weight:bold'
        },
        items: [{
			xtype: 'textfield',
			fieldLabel: '消息集合编号',
			name: 'msgSetID',
            allowBlank:false,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ]
		},{
			xtype: 'textfield',
			fieldLabel: '消息集合描述',
			name: 'msgSetDesc'
		}
         ]
	},
        {
            xtype: 'grid',
            height:350,
            title: '消息信息列表',
            frame: true,
            reference:'msgInfoListGrid',
            style:'margin:10px',
            columnLines: true,
            multiSelect: true,
            selModel: {
                type: 'checkboxmodel'
            },
            store: {
                type: 'messageInfoStore'
            },
            dockedItems: [{
                xtype:"toolbar",
                items: [
                		{text:"查询",tooltip:"查询数据",iconCls:"query",handler:'queryMsg'},'-',
                    {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addMsgDefine'},'-',
                    {text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editMsgDefine'},'-',
                    {text:"删除",tooltip:"删除数据",iconCls:"remove",handler:'delMsgDefine'},'-',
					{
						xtype:'combo',
						forceSelection: true,
						editable: false,
						valueField: 'TZ_JG_ID',
						displayField: 'TZ_JG_NAME',
						queryMode: 'local',
						name: 'orgId',
						width: 180,
						value: 'ADMIN',
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
						listeners:{
							change: function(thisComb, newValue, oldValue, eOpts ){
								var panel = thisComb.up("messageInfo");
								panel.orgId = newValue;

								if(panel.actType == "add"){
									return;/*如果是新增未保存，不刷新消息定义Grid*/
								}
								var form = panel.child("form").getForm();
								var msgSetID = form.findField("msgSetID").getValue();
								var msgLanage = panel.lanageType;
								var grid = panel.child('grid');
                                var tzStoreParams = '{"cfgSrhId": "TZ_GD_MESSAGE_COM.TZ_GD_MSGINFO_STD.TZ_PT_XXDY_VW","condition":{"TZ_XXJH_ID-operator": "01","TZ_XXJH_ID-value": "'+msgSetID+'","TZ_LANGUAGE_ID-operator": "01","TZ_LANGUAGE_ID-value": "'+msgLanage+'","TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+newValue+'"}}';
								grid.store.tzStoreParams = tzStoreParams;
								grid.store.reload();
							}
						}
					},'->',
                    {
                        xtype:'splitbutton',
                        text:'更多操作',
                        iconCls:'list',
                        glyph: 61,
                        menu:[
                                {
                                    text:'切换语言',
                                    iconCls:"switch ",
                                    handler:'changeLanguage'
                                },{
                                    text:'同步资源',
                                    iconCls:"sync",
                                    handler:'synchrLanguage'
                                }
                            ]
                    }]
            }],
            columns: [{
                text: '消息集合编号',
                dataIndex: 'msgSetID',
                hidden:true
            },{
                text: '消息编号',
                dataIndex: 'messageId',
                width: 200
            },{
                text: '消息文本',
                dataIndex: 'messageTest',
                minWidth:150,
                flex:1
            },{
                text: '语言',
                dataIndex: 'messageLanguage',
                width: 150
            },{
                menuDisabled: true,
                sortable: false,
                width:60,
                align:'center',
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'edit',tooltip: '编辑',handler:'editCurrentMsg'},
                    {iconCls: 'remove',tooltip: '删除',handler:'deleteCurrentMsg'}
                ]
            }],
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
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
		text: '保存',
		iconCls:"save",
		handler: 'onMsgInfoSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onMsgInfoEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onMsgInfoClose'
	}]
});
