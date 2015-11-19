Ext.define('KitchenSink.view.interviewManagement.interviewManage.classManagement', {
    extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.interviewManagement.interviewManage.classModel',
        'KitchenSink.view.interviewManagement.interviewManage.classStore',
        'KitchenSink.view.interviewManagement.interviewManage.classController',
        'tranzvision.extension.grid.column.Link',
		'KitchenSink.view.interviewManagement.interviewManage.msBatchModel',
        'KitchenSink.view.interviewManagement.interviewManage.msBatchStore',
        'KitchenSink.view.interviewManagement.interviewManage.interviewMgrStore'
    ],
    xtype: 'msMgrClassManagement',
    title:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MGR_CLS_STD.panelTitle","面试结果管理"),
    controller: 'interviewClassMgr',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;/*button height plus panel body padding*/
            var grid = panel.child('grid');
            if(grid) grid.setHeight( height -buttonHeight -16);
        }
    },

    initComponent: function () {
        var store = new KitchenSink.view.interviewManagement.interviewManage.classStore();
        Ext.apply(this, {
            items: [{
                xtype: 'grid',
                columnLines: true,
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
                            text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MGR_CLS_STD.tbarQuery","查询"),
                            tooltip:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MGR_CLS_STD.tbarTipQuery","查询数据"),
                            iconCls:"query",
                            handler:'queryClassInfo'
                        },
                    ]
                }],
                columns: [{
                    text: Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MGR_CLS_STD.rowNum","序号"),
                    xtype: 'rownumberer',
                    width:50
                },
                    {
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MGR_CLS_STD.className","班级名称") ,
                        dataIndex: 'className',
                        //width:280
                        flex:3
                    },{
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MGR_CLS_STD.projectName","所属项目"),
                        dataIndex: 'projectName',
                        //width:160
                        flex:2
                    },{
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MGR_CLS_STD.projectType","项目类别"),
                        dataIndex: 'projectType',
                        width:110
                    },{
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MGR_CLS_STD.applicantsNumber","报名人数") ,
                        dataIndex: 'applicantsNumber',
                        width:110
                    },{
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MGR_CLS_STD.noauditNumber","未审核人数"),
                        dataIndex: 'noauditNumber',
                        width:140,
                        renderer: function(val){
                            if(val>0){
                                return '<span  style="color: #ED0048;" >'+val+'</span>';
                            }else{
                                return val;
                            }
                        }
                    },{
                        xtype:'linkcolumn',
                        text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MGR_CLS_STD.linkMSGL","面试结果管理"),
                        width:125,
                        sortable:false,
                        items:[{
                            text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MGR_CLS_STD.linkMSGLItem","面试结果管理"),
                            handler: "interviewManage",
                            tooltip:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MGR_CLS_STD.linkMSGLItemTip","面试结果管理")
                        }]
                    }],
                store: store,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    store: store,
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
    buttons: [ {
        text: '关闭',
        iconCls:"close",
        handler: 'onPanelClose'
    }]
});

