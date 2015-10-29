Ext.define('KitchenSink.view.bugManagement.bugWhitelist.bugWhiteList', {
    extend: 'Ext.panel.Panel',
    xtype: 'bugWhileList',
    controller: 'bugWhileListControl',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bugManagement.bugWhitelist.bugWhileListControl',
        'KitchenSink.view.bugManagement.bugWhitelist.bugWhiteListStore'
    ],
    title:'白名单管理',
    columnLines: true,
    viewConfig: {
        enableTextSelection: true
    },
    header: false,
    frame: true,
    style: "margin:8px",
    layout: {
        type: 'fit'
    },
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    initComponent:function() {
        var store = new KitchenSink.view.bugManagement.bugWhitelist.bugWhiteListStore();
        Ext.apply(this,{
            items:[{
                xtype: "grid",
                minHeight: 200,
                columnLines: true,
                autoHeight: true,
                frame: true,
                style:'border:0',
                selModel: {
                    type: 'checkboxmodel'
                },
                bodyStyle: 'overflow-y:auto;overflow-x:hidden;,border:0px',
                dockedItems: [
                    {
                        xtype: "toolbar",
                        items: [
                            /*{text: "查询", iconCls: "search", tooltip: "搜索人员", handler: "searchPeople"},*/
                            {text: "新增", iconCls: "add", tooltip: "新增人员", handler: "addPeople"},
                            {text: "删除", iconCls: "remove", tooltip: "删除人员", handler: "removePeoples"}
                        ]
                    }
                ],
                store: store,
                columns: [{
                    text: '登录账号',
                    dataIndex: 'account',
                    minWidth: 100,
                    flex: 1
                }, {
                    text: '人员姓名',
                    dataIndex: 'realName',
                    minWidth: 100,
                    flex: 1
                },{
                    xtype : 'checkcolumn',
                    text:'启用',
                    dataIndex:'isWhite',
                    maxWidth:60,
                    align:'center'
                },{
                    text:"操作",
                    align:'center',
                    menuDisabled: true,
                    sortable: false,
                    minWidth:100,
                    xtype: 'actioncolumn',
                    items:[
                        {iconCls: 'remove',tooltip: '删除',handler:'removePeople'}
                    ]
                }],
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    store: store,
                    plugins: new Ext.ux.ProgressBarPager()
                }
            }]
        });
        this.callParent();
    },
    buttons:[{
        text: '保存',
        iconCls:"save",
        handler: 'onListSave'
    }/*, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onListEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onListClose'
    }*/]
});