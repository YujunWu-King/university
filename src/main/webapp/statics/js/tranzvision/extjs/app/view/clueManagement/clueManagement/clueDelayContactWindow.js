Ext.define('KitchenSink.view.clueManagement.clueManagement.clueDelayContactWindow',{
    extend:'Ext.window.Window',
    xtype:'clueDelayContactWindow',
    controller:'clueDealWithController',
    requires:[
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.clueManagement.clueManagement.clueDealWithController'
    ],
    title:'线索延迟联系',
    reference:'clueDelayContactWindow',
    width:500,
    y:10,
    modal:true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    fromType:'',
    constructor:function(obj){
        this.clueId = obj.clueId;
		this.chargeOprid = obj.chargeOprid;
		this.clueState = obj.clueState;
        this.fromType = obj.fromType;
        this.callParent();
    },
    initComponent:function() {
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
                    xtype: 'datefield',
                    fieldLabel: '建议跟进日期',
                    afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                	allowBlank:false,
					name: 'contactDate',
                    format:'Y-m-d'
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
        name:'clueDelayContactEnsureBtn',
        handler:'onClueDelayContactSave'
    },{
        text:'关闭',
        iconCls:'close',
        handler:'onClueDealWithClose'
    }]
});
