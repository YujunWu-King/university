Ext.define('KitchenSink.view.tzLianxi.liuzh.lzhBugMg.bugUser', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.tzLianxi.liuzh.lzhBugMg.bugStore',
        'KitchenSink.view.tzLianxi.liuzh.lzhBugMg.bugController'
    ],
    controller:'bugController',
    stateful: true,
    collapsible: true,
    multiSelect: true,
    style:"margin:8px",
    height: 350,
    title: 'Bug管理',
    viewConfig: {
        enableTextSelection: true
    },
    initComponent: function () {
        var store = new KitchenSink.view.tzLianxi.liuzh.lzhBugMg.bugStore();
        Ext.apply(this,{
            dockedItems:[{
                xtype:'toolbar',
                items:[{text:'查询',  tooltip:'检索bug数据',iconCls:"query",handler:'searchBug'},"-",
                    {text: "查看",tooltip: "查看bug数据", iconCls:"view", handler: 'editBug'}]
            }],
            selModel: {
                type: 'checkboxmodel'
            },
            columns:[{
                width:1
            },{
                text:'编号',
                width:'15%',
                align:'center',
                dataIndex:'BugID'
            },{
                text:'说明',
                width:'25%',
                align:'center',
                dataIndex:'name'
            },{
                text:'责任人',
                width:'15%',
                align:'center',
                dataIndex:'responsableOprID'
            },{
                text:'期望解决日期',
                width:'20%',
                align:'center',
                dataIndex:'espectDate'
            },{
                text:'状态描述',
                width:'10%',
                align:'center',
                dataIndex:'status',
                flex:1
                // renderer:formatStatus
            }],
            store:store,
            bbar: {
                flex:1,
                xtype: 'pagingtoolbar',
                store:store,
                pageSize: 10,
                displayInfo: true,
                displayMsg: '显示{0}-{1}条，共{2}条',
                beforePageText: '第',
                afterPageText: '页/共{0}页',
                emptyMsg: '没有数据显示',
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});