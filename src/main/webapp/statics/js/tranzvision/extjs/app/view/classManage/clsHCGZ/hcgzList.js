Ext.define('KitchenSink.view.classManage.clsHCGZ.hcgzList', {
    extend: 'Ext.panel.Panel',
    xtype: 'hcgzListPanel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.classManage.clsHCGZ.hcgzListModel',
        'KitchenSink.view.classManage.clsHCGZ.hcgzListStore',
        'KitchenSink.view.classManage.clsHCGZ.hcgzListController',
        'tranzvision.extension.grid.column.Link'
    ],
    title:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.panelTitle","招生班级互斥规则"),
    controller: 'hcgzListController',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;
            var grid = panel.child('grid');
            if(grid) grid.setHeight( height-buttonHeight-16);
        }
    },
    initComponent: function (){
        var hcgzListStore = new KitchenSink.view.classManage.clsHCGZ.hcgzListStore();
        Ext.apply(this,{
            items: [{
                xtype: 'grid',
                columnLines: true,
                selModel: {
                    type: 'checkboxmodel'
                },
                style:"margin:8px",
                multiSelect: true,
                viewConfig: {
                    enableTextSelection: true
                },
                header:false,
                frame: true,
                dockedItems:[{
                    xtype:"toolbar",
                    items:[
                        {
                         text:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.tbarQuery","查询"),
                         tooltip:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.tbarTipQuery","查询数据"),
                         iconCls:"query",
                         handler:'queryHCGZ'
                         },{
                            text:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.tbarAdd","新增"),
                            tooltip:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.tbarTipAdd","新增"),
                            iconCls:"add",
                            handler:'addHCGZ'
                        },{
                            text:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.tbarDelete","删除"),
                            tooltip:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.tbarTipDelete","删除"),
                            iconCls:"delete",
                            handler:'deleteHCGZs'
                        }
                    ]
                }],
                columns: [{
                    text:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.hcgzId","规则编号") ,
                    dataIndex: 'hcgzId',
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.hcgzName","规则名称"),
                    dataIndex: 'hcgzName',
                    flex:2
                },{
                    xtype:'linkcolumn',
                    text:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.linkGZXQ","规则详情"),
                    width:100,
                    sortable:false,
                    items:[{
                        text:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.linkGZXQItem","规则详情"),
                        handler: "hcgzDetail",
                        tooltip:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.linkGZXQItemTip","规则详情")
                    }]
                }],
                store: hcgzListStore,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    store: hcgzListStore,
                    displayInfo: true,
                    displayMsg:"显示{0}-{1}条，共{2}条",
                    beforePageText:"第",
                    afterPageText:"页/共{0}页",
                    emptyMsg: "没有数据显示",
                    plugins: new Ext.ux.ProgressBarPager()
                }
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
