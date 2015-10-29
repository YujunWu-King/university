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
        xtype: "toolbar",
        items: [{
            text: "新增",
            tooltip: "新增模板",
            iconCls: "add",
            handler: 'addBmbTpl'
        }]
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
                    dataIndex: 'activestated',
                    hidden: true
                },
                {
                    text: '有效状态',
                    dataIndex: 'activestatedesc',
                    sortable: true,
                    resizable: false,
                    width: 100

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
