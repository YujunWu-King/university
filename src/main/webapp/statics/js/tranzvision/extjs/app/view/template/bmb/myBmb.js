Ext.define('KitchenSink.view.template.bmb.myBmb', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.bmb.myBmbController',
        'KitchenSink.view.template.bmb.myBmbStore'
    ],
    xtype: 'myBmb',
    controller: 'myBmb',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style: "margin:8px",
    multiSelect: true,
    title: '我的报名表模板',
    viewConfig: {
        enableTextSelection: true
    },
    header: false,
    frame: true,
    dockedItems: [{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',{minWidth:80,text:"关闭",iconCls:"close",handler:'onPanelClose'}]
    },{
        xtype: "toolbar",
        items: [{
            text:"查询",
            tooltip:'查询',
            iconCls:'query',
            handler:'queryBmbTpl'
        },{
            text: "新增",
            tooltip: "新增",
            iconCls: "add",
            handler: 'addBmbTpl'
        },
            {text:"编辑",tooltip:"编辑",iconCls:"edit",handler:'editBmbTpl'}]
    }],
    initComponent: function() {
        var store = new KitchenSink.view.template.bmb.myBmbStore();
        Ext.apply(this, {
            columns: [
                {
                    text: '模板ID',
                    dataIndex: 'tplid',
                    hidden: true
                },
                {
                    text: '模板名称',
                    dataIndex: 'tplname',
                    sortable: true,
                    resizable: true,
                    flex: 1
                },
                {
                    text: '有效状态',
                    dataIndex: 'activestate',
                    renderer:function(v){
                        if(v=='Y'){
                            return '生效';
                        }else{
                            return '失效';
                        }
                    }
                },
                {
                    menuDisabled: true,
                    sortable: false,
                    width: 80,
                    xtype: 'actioncolumn',
                    text: '操作',
                    menuText: '操作',
                    align: 'center',
                    items: [
                        {
                            iconCls: 'edit',
                            tooltip: '编辑',
                            handler: 'onBmbTplEdit'
                        },
                        {
                            iconCls: 'copy',
                            tooltip: '复制',
                            handler: 'onBmbTplCopy'
                        },
                        {
                            iconCls: 'preview',
                            tooltip: '预览',
                            handler: 'onBmbTplPreview'
                        },
                        {
                            iconCls: 'set',
                            tooltip: '设置管理权限',
                            handler: 'onBmbTplSet'
                        },
                        {
                            iconCls: 'print',
                            tooltip: '打印设置',
                            handler: 'onBmbPrintSet'
                        },
                        {
                            iconCls: 'print',
                            tooltip: 'PDF打印模板设置',
                            handler: 'onPdfPrintSet'
                        }
                    ]
                }],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});
