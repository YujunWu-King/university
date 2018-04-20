Ext.define('KitchenSink.view.clueManagement.clueManagement.clueCloseWindow',{
    extend:'Ext.window.Window',
    xtype:'clueCloseWindow',
    controller:'clueDealWithController',
    requires:[
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.clueManagement.clueManagement.clueDealWithController'
    ],
    title:'线索关闭',
    reference:'clueCloseWindow',
    width:500,
    y:10,
    modal:true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    fromType:'',
    constructor:function(obj){
        this.clueId = obj.clueId;
		this.chargeOprid = obj.chargeOprid;
		this.clueState = obj.clueState;
        this.closeReasonData = obj.closeReasonData;
        this.fromType = obj.fromType;
        this.callParent();
    },
    initComponent:function() {
        var store =  Ext.create("Ext.data.Store", {
            fields: [
                'TZ_GBYY_ID,TZ_LABEL_NAME'
            ],
            data: this.closeReasonData
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
                    name:'clueId',
                    value:this.clueId,
                    hidden:true
                },{
                    xtype: 'combo',
                    fieldLabel: '关闭原因',
                    name: 'closeReasonId',
                    afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    store: store,
                    autoShow: true,
                    valueField: 'TZ_GBYY_ID',
                    displayField: 'TZ_LABEL_NAME',
                    queryMode: 'local',
                    editable: false,
                    allowBlank: false
                },{
					xtype: 'textfield',
					fieldLabel: '备注信息',
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
        name:'clueCloseReasonEnsureBtn',
        handler:'onClueCloseReasonSave'
    },{
        text:'关闭',
        iconCls:'close',
        handler:'onClueDealWithClose'
    }]
});
