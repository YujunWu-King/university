Ext.define('KitchenSink.view.clueManagement.clueManagement.clueBackWindow',{
    extend:'Ext.window.Window',
    xtype:'clueBackWindow',
    controller:'clueDealWithController',
    requires:[
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.clueManagement.clueManagement.clueDealWithController'
    ],
    title:'线索退回',
    reference:'clueBackWindow',
    width:500,
    y:10,
    modal:true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    fromType:'',
    constructor:function(obj){
        this.clueId = obj.clueId;
		this.chargeOprid = obj.chargeOprid;
		this.clueState = obj.clueState;
        this.backReasonData = obj.backReasonData;
        this.backPersonOprid = obj.backPersonOprid;
        this.backPersonName = obj.backPersonName;
        this.fromType = obj.fromType;
        this.callParent();
    },
    initComponent:function() {
        var store = Ext.create("Ext.data.Store", {
                fields: [
                    'TZ_THYY_ID,TZ_LABEL_NAME'
                ],
                data: this.backReasonData
            });
        Ext.apply(this,{
            items:[{
                xtype:'form',
                layout:{
                    type:'vbox',
                    align:'stretch'
                },
                border:false,
                bodyPadding:10,
                items:[{
                    xtype:'textfield',
                    name:'clueId',
                    value:this.clueId,
                    hidden:true
                },{
                    xtype:'textfield',
                    name:'chargeOpridPage',
                    value:this.chargeOprid,
                    hidden:true
                },{
                    xtype:'textfield',
                    name:'clueStatePage',
                    value:this.clueState,
                    hidden:true
                },{
                    xtype:'textfield',
                    name:'backPersonOprid',
                    value: this.backPersonOprid,
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel: '退回至',
                    name: 'backPersonName',
                    afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    value: this.backPersonName,
                    editable: false,
                    allowBlank: false,
                    triggers: {
                        clear: {
                            cls: 'x-form-clear-trigger',
                            handler: 'clearUser'
                        },
                        search: {
                            cls: 'x-form-search-trigger',
                            handler: "searchUser"
                        }
                    }
                },{
                    xtype: 'combo',
                    fieldLabel: '退回原因',
                    name: 'backReasonId',
                    afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    store: store,
                    autoShow: true,
                    valueField: 'TZ_THYY_ID',
                    displayField: 'TZ_LABEL_NAME',
                    queryMode: 'local',
                    editable: false,
                    allowBlank: false
                },{
					xtype: 'textfield',
					fieldLabel: '备注信息',
					//afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					maxLength: 254,
					name: 'demo'
				}]
            }]
        });
        this.callParent();
    },
    buttons:[{
        text:'确定',
        iconCls:'ensure',
        name:'clueBackReasonEnsureBtn',
        handler:'onClueBackReasonSave'
    },{
        text:'关闭',
        iconCls:'close',
        handler:'onClueDealWithClose'
    }]
});
