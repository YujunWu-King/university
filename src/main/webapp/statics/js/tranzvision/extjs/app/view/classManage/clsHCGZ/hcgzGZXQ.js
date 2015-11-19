Ext.define('KitchenSink.view.classManage.clsHCGZ.hcgzGZXQ', {
    extend: 'Ext.panel.Panel',
    xtype: 'hcgzGZXQPanel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.classManage.clsHCGZ.hcgzGZXQModel',
        'KitchenSink.view.classManage.clsHCGZ.hcgzGZXQStore',
        'KitchenSink.view.classManage.clsHCGZ.hcgzGZXQController'
    ],
    title:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZXQ_STD.panelTitle","规则详情"),
    controller: 'hcgzGZXQController',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;
            var grid = panel.child('grid');
            var formHeight = panel.child('form').getHeight();
            if(grid) grid.setHeight( height-buttonHeight-formHeight-16);
        }
    },
    initComponent: function (){
        var hcgzGZXQStore = new KitchenSink.view.classManage.clsHCGZ.hcgzGZXQStore();
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                border: false,
                bodyPadding: 8,
                fieldDefaults:{
                    labelWidth:80,
                    labelAlign:"left"
                },
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items:[{
                    xtype:"displayfield",
                    fieldLabel:"规则编号",
                    name:"hcgzId",
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true,
                    fieldLabel: Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZXQ_STD.hcgzName","规则名称"),
                    name: 'hcgzName'
                }]
            },{
                xtype: 'grid',
                columnLines: true,
                style:"margin:8px",
                viewConfig: {
                    enableTextSelection: true
                },
                header:false,
                frame: true,
                dockedItems:[{
                    xtype:"toolbar",
                    items:[{
                            text:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZXQ_STD.tbarAdd","新增"),
                            tooltip:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZXQ_STD.tbarTipAdd","新增"),
                            iconCls:"add",
                            handler:'addHCGZCls'
                        }
                    ]
                }],
                columns: [{
                    text:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZXQ_STD.hcgzClassId","班级编号") ,
                    dataIndex: 'hcgzClassId',
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZXQ_STD.hcgzClassName","班级名称"),
                    dataIndex: 'hcgzClassName',
                    flex:2
                },{
                    menuDisabled: true,
                    sortable: false,
                    header:'操作',
                    width:40,
                    xtype: 'actioncolumn',
                    items:[
                        {	iconCls: 'remove',tooltip: '删除',handler:'deleteHCGZCls'}
                    ]
                }],
                store: hcgzGZXQStore
            }]
        });
        this.callParent();
    },

    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onPanelSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onPanelEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onPanelClose'
    }]
});
