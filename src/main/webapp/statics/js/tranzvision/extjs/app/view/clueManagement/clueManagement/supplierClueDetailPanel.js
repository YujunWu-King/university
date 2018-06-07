Ext.define('KitchenSink.view.clueManagement.clueManagement.supplierClueDetailPanel',{
    extend:'Ext.panel.Panel',
    xtype:'supplierClueDetailPanel',
    reference:'supplierClueDetailPanel',
    requires:[
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'KitchenSink.view.common.store.comboxStore',
        'KitchenSink.view.clueManagement.clueManagement.supplierClueController'
    ],
    controller:'supplierClueController',
    
    title:'线索详情',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    
    initComponent:function() {
        var me=this;
        var backReasonData = me.backReasonData,
            closeReasonData = me.closeReasonData,
            customerNameData = me.customerNameData,
            companyNameData = me.companyNameData;


        var dynamicStore = {
            //退回原因
            backReasonStore: Ext.create("Ext.data.Store", {
                fields: [
                    'TZ_THYY_ID,TZ_LABEL_NAME'
                ],
                data: backReasonData
            }),
            //关闭原因
            closeReasonStore:Ext.create("Ext.data.Store", {
                fields: [
                    'TZ_GBYY_ID,TZ_LABEL_NAME'
                ],
                data: closeReasonData
            }),
            //姓名
            customerNameStore:Ext.create("Ext.data.Store",{
                fields:[
                    "TZ_KH_OPRID","TZ_REALNAME","TZ_DESCR_254"
                ],
                data:customerNameData
            }),
            //公司
            companyNameStore:Ext.create("Ext.data.Store",{
                fields:[
                    "TZ_COMP_CNAME"
                ],
                data:companyNameData
            })
        };

        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'clueDetailForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                bodyStyle: 'overflow-y:auto;overflow-x:hidden',
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype:'hiddenfield',
                    fieldLabel:'线索ID',
                    name:'clueId'
                },{
                    xtype: 'combobox',
                    fieldLabel: "状态",
                    forceSelection: true,
                    valueField: 'TValue',
                    displayField: 'TSDesc',
                    name:'clueState',
                    store: new KitchenSink.view.common.store.appTransStore("TZ_LEAD_STATUS"),
                    editable : false,
                    readOnly:true,
                    queryMode: 'local',
                    fieldStyle:'background:#F4F4F4',
                    value: 'A',
                },{
                    xtype:'combobox',
                    fieldLabel:'退回原因',
                    afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    name:'backReasonId',
                    store: dynamicStore.backReasonStore,
                    autoShow: true,
                    valueField: 'TZ_THYY_ID',
                    displayField: 'TZ_LABEL_NAME',
                    queryMode: 'local',
                    editable: false,
                    readOnly:true,
                    fieldStyle:'background:#F4F4F4'
                },{
                    xtype:'combobox',
                    fieldLabel:'关闭原因',
                    afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    name:'closeReasonId',
                    store: dynamicStore.closeReasonStore,
                    autoShow: true,
                    valueField: 'TZ_GBYY_ID',
                    displayField: 'TZ_LABEL_NAME',
                    queryMode: 'local',
                    editable: false,
                    readOnly:true,
                    fieldStyle:'background:#F4F4F4'
                },{
                    xtype:'datefield',
                    fieldLabel:'建议跟进日期',
                    afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    name: 'contactDate',
                    format:'Y-m-d',
                    readOnly:true,
                    fieldStyle:'background:#F4F4F4'
                },{
                    xtype: 'combo',
                    fieldLabel: '客户姓名',
                    maxLength: 30,
                    name: 'cusName',
                    afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    store: dynamicStore.customerNameStore,
                    typeAhead: true,
                    autoShow: true,
                    valueField: 'TZ_REALNAME',
                    displayField: 'TZ_DESCR_254',
                    queryMode: 'local',
                    allowBlank: false,
                    displayTpl: Ext.create('Ext.XTemplate',
                        '<tpl for=".">',
                        '{TZ_REALNAME}',
                        '</tpl>'
                    ),
                    listeners:{
                        select:function(combo,record){
                            var form = this.findParentByType('form').getForm();

                            //显示值为姓名
                            var selectValue=this.getValue();
                            var selectDisplay = this.getRawValue();
                            this.setRawValue(selectValue);

                            var cusOprid = record.get("TZ_KH_OPRID");

                            //带入相关信息
                            var tzParamsHis = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"tzGetCustomerInfo","comParams":{"cusOprid":"'+cusOprid+'","cusName":"'+selectValue+'","nameCompany":"'+selectDisplay+'"}}';
                            Ext.tzLoad(tzParamsHis,function(responseData){
                                form.findField('cusMobile').setValue(responseData.mobile);
                                form.findField('companyName').setRawValue(responseData.company);
                                form.findField('position').setValue(responseData.position);
                                form.findField('phone').setValue(responseData.phone);
                            });
                        }
                    }
                },{
                    xtype: 'textfield',
                    fieldLabel: '手机',
                    maxLength: 25,
                    //afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    //allowBlank:false,
                    name: 'cusMobile'
                },{
                	xtype: 'textfield',
                    fieldLabel: '邮箱',
                    maxLength: 100,
                    name: 'cusEmail'
                },{
                    xtype: 'combo',
                    fieldLabel: '公司',
                    maxLength: 50,
                    name: 'companyName',
                    store: dynamicStore.companyNameStore,
                    typeAhead: true,
                    autoShow: true,
                    valueField: 'TZ_COMP_CNAME',
                    displayField: 'TZ_COMP_CNAME',
                    queryMode: 'local',
                    displayTpl: Ext.create('Ext.XTemplate',
                        '<tpl for=".">',
                        '{TZ_COMP_CNAME}',
                        '</tpl>'
                    )
                },{
                    xtype: 'textfield',
                    fieldLabel: '职务',
                    maxLength: 50,
                    name: 'position'
                },{
                    xtype: 'textfield',
                    fieldLabel: '电话',
                    maxLength: 24,
                    name: 'phone'
                },{
                    xtype: 'combobox',
                    fieldLabel: "报考状态",
                    forceSelection: true,
                    valueField: 'TValue',
                    displayField: 'TSDesc',
                    name:'bkStatus',
                    store: new KitchenSink.view.common.store.appTransStore("TZ_LEAD_BMB_STATUS"),
                    editable : false,
                    readOnly:true,
                    queryMode: 'local',
                    fieldStyle:'background:#F4F4F4',
                    value:'A',
                },{
                    xtype: 'textareafield',
                    grow: true,
                    fieldLabel: '备注',
                    name: 'memo'
                }]
            }],
            buttons: [{
                text: '保存',
                iconCls: "save",
                name: 'saveBtn',
                handler: 'saveClue'
            },{
                text: '确定',
                iconCls: "ensure",
                name: 'ensureBtn',
                handler: 'saveClue'
            },  {
                text: '关闭',
                iconCls: "close",
                handler: function(btn){
                	btn.findParentByType('supplierClueDetailPanel').close();
                }
            }]
        });
        this.callParent();
    },
    constructor:function(config){
        this.backReasonData = config.backReasonData;
        this.closeReasonData = config.closeReasonData;

        this.customerNameData = config.customerNameData;
        this.companyNameData = config.companyNameData;
        
        this.actType = config.actType;
        this.clueID = config.clueID;
        this.reloadClueGrid = config.reloadClueGrid;
        
        this.callParent();
    }
});
