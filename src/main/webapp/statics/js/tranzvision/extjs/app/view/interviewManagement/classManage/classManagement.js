Ext.define('KitchenSink.view.interviewManagement.classManage.classManagement', {
    extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.interviewManagement.classManage.classModel',
        'KitchenSink.view.interviewManagement.classManage.classStore',
        'KitchenSink.view.interviewManagement.classManage.classController',
        'tranzvision.extension.grid.column.Link',
		'KitchenSink.view.interviewManagement.classBatchChoose.msBatchModel',
        'KitchenSink.view.interviewManagement.classBatchChoose.msBatchStore'
    ],
    xtype: 'classManagement',
    controller: 'iterviewMgClass',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    title:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSGL_CLASS_STD.panelTitle",'班级管理'),
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;/*button height plus panel body padding*/
            var grid = panel.child('grid');
            if(grid) grid.setHeight( height -buttonHeight -16);
        }
    },

    initComponent: function () {
        var store = new KitchenSink.view.interviewManagement.classManage.classStore();
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
                        {text:"查询",tooltip:"查询数据",iconCls:"query",handler:'queryClassInfo'}
                    ]
                }],
                columns: [{
                    text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSGL_CLASS_STD.rownumberer",'序号'),
                    xtype: 'rownumberer',
                    width:50
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSGL_CLASS_STD.className",'班级名称'),
                    dataIndex: 'className',
                    minwidth:250,
                    flex:2
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSGL_CLASS_STD.projectName", '所属项目'),
                    dataIndex: 'projectName',
                    minwidth:110,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSGL_CLASS_STD.projectType",'项目类别'),
                    dataIndex: 'projectType',
                    minwidth:110,
                    flex:1
                },{
                    text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSGL_CLASS_STD.applicantsNumber",'报名人数'),
                    dataIndex: 'applicantsNumber',
                    minwidth:110,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSGL_CLASS_STD.noauditNumber","未审核人数"),
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
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSGL_CLASS_STD.interviewArrange","面试时间安排"),
                    width:130,
                    sortable:false,
                    items:[{
                        text:"面试时间安排",
                        handler: "interviewArrange",
                        tooltip:"面试时间安排"
                    }]
                }],
                store: store,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    store: store,
                    displayInfo: true,
                    /*
                    displayMsg: '显示{0}-{1}条，共{2}条',
                    beforePageText: '第',
                    afterPageText: '页/共{0}页',
                    emptyMsg: '没有数据显示',
                    */
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

