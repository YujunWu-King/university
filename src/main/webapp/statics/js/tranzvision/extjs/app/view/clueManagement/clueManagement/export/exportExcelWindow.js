Ext.define('KitchenSink.view.clueManagement.clueManagement.export.exportExcelWindow',{
    extend:'Ext.window.Window',
    xtype:'exportExcelWindow',
    controller:'exportExcelController',
    requires:[
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.clueManagement.clueManagement.export.exportExcelStore',
        'KitchenSink.view.clueManagement.clueManagement.export.exportExcelController'
    ],
    modal:true,
    header:false,
    minHeight:200,
    maxHeight:400,
    resizable:false,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    constructor:function(obj) {
        this.exportType = obj.exportType;
        this.exportObj = obj.exportObj;
        this.hiddenPage = false;
        if(obj.type=="download") {
            this.hiddenPage = true;
        }
        this.callParent();
    },
    initComponent:function() {
        var me = this;

        var store = new KitchenSink.view.clueManagement.clueManagement.export.exportExcelStore(this.exportType);

        Ext.apply(this,{
            items:[{
                xtype:'form',
                reference:'exportForm',
                layout:{
                    type:'vbox',
                    align:'stretch'
                },
                border:false,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',
                fieldDefaults:{
                    msgTarget:'side',
                    labelStyle:'font-weight:bold'
                },
                items:[{
                    xtype:'tabpanel',
                    reference:'packageTabPanel',
                    activeTab:0,
                    frame:false,
                    header:false,
                    width:650,
                    minHeight:200,
                    maxHeight:400,
                    resizeTabs:true,
                    defaults:{
                        autoScroll:false
                    },
                    listeners:{
                        tabchange:function(tp,p) {
                            if(p.reference=='downloadGrid') {
                                this.doLayout();
                            }
                        }
                    },
                    items:[{
                        xtype:'form',
                        title:'招生线索数据导出',
                        frame:false,
                        minHeight:150,
                        autoHeight:true,
                        reference:'exportInfoForm',
                        border:false,
                        hidden:me.hiddenPage,
                        bodyPadding:10,
                        buttons:[{
                            text:'关闭',
                            iconCls:'close',
                            handler:function(btn) {
                                var win = btn.findParentByType("exportExcelWindow");
                                win.close();
                            }
                        }],
                        items:[{
                            xtype:'label',
                            text:'请输入导出文件名',
                            style:{
                                marginTop:'10px',
                                marginBottom:'5px',
                                display:'block'
                            }
                        },{
                            layout:{
                                type:'column'
                            },
                            items:[{
                                xtype:'textfield',
                                name:'FileName',
                                allowBlank:false,
                                columnWidth:.8
                            },{
                                xtype:"button",
                                width:120,
                                style:'margin-left:8px',
                                text: "确认导出",
                                labelAlign: 'right',
                                buttonAlign: 'left',
                                columnWidth:.2,
                                handler: 'ensureExport'
                            }]
                        }]
                    },{
                        title: "招生线索数据导出结果",
                        xtype: 'grid',
                        autoHeight: true,
                        frame:false,
                        minHeight:200,
                        columnLines: true,
                        reference: 'downloadGrid',
                        multiSelect: true,
                        store: store,
                        selModel: {
                            type: 'checkboxmodel'
                        },
                        dockedItems: [{
                            xtype: "toolbar",
                            items: [
                                {
                                    text: "查询",
                                    iconCls: "query",
                                    handler:"exportQuery"
                                },
                                '-',
                                {
                                    text: "删除",
                                    iconCls: "remove",
                                    handler:"exportDelete"
                                }
                            ]
                        },{
                            xtype:"toolbar",
                            dock:"bottom",
                            ui:"footer",
                            items:['->',
                                {
                                    minWidth:80,
                                    text: "保存",
                                    iconCls:"save",
                                    handler:'exportGridSave'
                                },{
                                    minWidth:80,
                                    text: "关闭",
                                    iconCls:"close",
                                    handler: function(btn){
                                        var win = btn.findParentByType('exportExcelWindow');
                                        win.close();
                                    }
                                }]
                        }],

                        columns: [
                            {text: "文件名称",
                                dataIndex: 'fileName',
                                minWidth:120,
                                flex:1
                            },
                            {
                                text: "操作人",
                                dataIndex: 'czPerName',
                                width: 100
                            },
                            {
                                text: "导出时间",
                                dataIndex: 'bgTime',
                                width: 160
                            },
                            {
                                text: "状态",
                                dataIndex: 'procStaDescr',
                                width: 100
                            },
                            {
                                xtype:'actioncolumn',
                                header: "下载",
                                align:'center',
                                width:60,
                                items:[{
                                    iconCls:'download',
                                    sortable:false,
                                    handler: "downloadFile",
                                    isDisabled:function(view ,rowIndex ,colIndex ,item ,record ){
                                        if(record.get("procState")=="SUCCEEDED"){
                                            return false;
                                        }else{
                                            return true;
                                        };
                                    }
                                }]
                            }
                        ],
                        bbar: {
                            xtype: 'pagingtoolbar',
                            pageSize: 5,
                            listeners: {
                                afterrender: function (pbar) {
                                    var grid = pbar.findParentByType("grid");
                                    pbar.setStore(grid.store);
                                }
                            },
                            plugins: new Ext.ux.ProgressBarPager()
                        }
                    }]
                }]
            }]
        });
        this.callParent();
    }
});
