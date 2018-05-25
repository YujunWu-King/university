Ext.define('KitchenSink.view.clueManagement.clueManagement.clueAssignResponsibleWindow',{
    extend:'Ext.window.Window',
    xtype:'clueAssignResponsibleWindow',
    controller:'clueDealWithController',
    requires:[
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.clueManagement.clueManagement.clueDealWithController'
    ],
    title:'线索责任人',
    reference:'clueAssignResponsibleWindow',
    width:500,
    y:10,
    modal:true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    fromType:'',
    constructor:function(obj){
        this.clueIdSelect = obj.clueIdSelect;
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
                    name:'clueIdSelect',
                    value:this.clueIdSelect,
                    hidden:true
                },{
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
                    name:'fromType',
                    value:this.fromType,
                    hidden:true
                },{
                    xtype:'textfield',
                    name:'chargeOprid',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel: '责任人',
                    afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    name: 'chargeName',
                    editable:false,
                    triggers: {
                        search: {
                            cls: 'x-form-search-trigger',
                            handler: "searchUser"
                        }
                    },
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
        name:'clueAssignEnsureBtn',
        handler:'onClueAssignSave'
    },{
        text:'关闭',
        iconCls:'close',
        handler:'onClueDealWithClose'
    }]
});

