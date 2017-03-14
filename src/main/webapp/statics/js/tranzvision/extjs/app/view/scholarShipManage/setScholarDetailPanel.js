Ext.define('KitchenSink.view.scholarShipManage.setScholarDetailPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'setScholarDetailPanel',
    controller:'scholarController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.scholarShipManage.scholarStore',
        'KitchenSink.view.scholarShipManage.scholarController'
    ], 
    title: '奖学金详情',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    actType: 'update',
    items: [
        {
            xtype: 'form',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            border: false,
          //  bodyPadding: 10,
            style:"margin:8px",
            bodyStyle: 'overflow-y:auto;overflow-x:hidden',
            fieldDefaults: {
                msgTarget: 'side',
                labelWidth: 110,
                labelStyle: 'font-weight:bold'
            },
            items: [
                {
                    xtype: 'textfield',
                    fieldLabel: '奖学金编号',
                    name: 'TZ_SCHLR_ID',
                    cls:'lanage_1',
                    readOnly:true
                }, 
                {
                    layout: {
                        type: 'column'
                    },
                    items: [
                      {  columnWidth: .2,
                          xtype: 'textfield',
                          fieldLabel: "问卷模板",
                          name: 'TZ_APP_TPL_ID',
                          readOnly:true,
                          cls:'lanage_1'
                      },{
                          columnWidth: .5,
                          xtype: 'displayfield',
                          hideLabel: true,
                          name: 'TZ_APP_TPL_MC',
                          style: 'margin-left:8px'
                          }
                    ]
                },
                {
                    xtype: 'textfield',
                    fieldLabel: '问卷编号',
                    name: 'TZ_DC_WJ_ID',
                    cls:'lanage_1',
                    readOnly:true,
                    hidden:true
                },{
                    xtype: 'textfield',
                    name: 'TZ_SCHLR_NAME',
                    fieldLabel:'奖学金名称',
                    cls:'lanage_1',
                    readOnly:true
                },{
                    xtype: 'combobox',
                    fieldLabel:  "问卷状态",
                    forceSelection: true,
                    valueField: 'TValue',
                    displayField: 'TSDesc',
                    editable:false,
                    name:'TZ_DC_WJ_ZT',
                    store: new KitchenSink.view.common.store.appTransStore("TZ_DC_WJ_ZT"),
                    queryMode: 'local',
                    readOnly:true,
                    cls:'lanage_1'
                }, {
                    xtype: 'datefield',
                    fieldLabel:  "开始日期",
                    format: 'Y-m-d',
                    editable:false,
                    name: 'TZ_DC_WJ_KSRQ'
                },
                {
                    xtype: 'timefield',
                    fieldLabel:  "开始时间",
                    format: 'H:i:s',
                    editable:false,
                    name: 'TZ_DC_WJ_KSSJ'
                },
                {
                    xtype: 'datefield',
                    fieldLabel: "结束日期",
                    format: 'Y-m-d',
                    editable:false,
                    name: 'TZ_DC_WJ_JSRQ'
                },
                {
                    xtype: 'timefield',
                    fieldLabel: "结束时间",
                    format: 'H:i:s',
                    editable:false,
                    name: 'TZ_DC_WJ_JSSJ'
                },/*{
                    xtype: 'combobox',
                    fieldLabel:  "有效状态",
                    forceSelection: true,
                    valueField: 'TValue',
                    displayField: 'TSDesc',
                    name:'TZ_JXJ_STATE',
                    store: new KitchenSink.view.common.store.appTransStore("TZ_JXJ_STATE"),
                    queryMode: 'local',
                    editable:false
                }*/,{
                    layout: {
                        type: 'column'
                    },
                    items: [
                       {
                            columnWidth:0.2,
                            xtype:'button',
                            text:'设置详情',
                            style:'margin-left:114px',
                            handler:'setWjdc'
                        }
                    ]
                },
                {
                    xtype:'label',
                    text:'请注意：如要进行问卷和听众设置，请点击‘设置详情’按钮。',
                    cls: 'lable_1',
                    style:'margin-top:2px;margin-left:115px'
                }
            ]
        }],
        buttons: [{
            text: '保存', 
            iconCls: "save",
            handler:'onSchlrDetailSave'
        },{
            text: '确定',
            iconCls: "ensure",
            handler:'onSchlrDetailSure'
        },{
            text: '关闭',
            iconCls: "close",
            handler:'onSchlrDetailClose'
            }
        ]
});







































