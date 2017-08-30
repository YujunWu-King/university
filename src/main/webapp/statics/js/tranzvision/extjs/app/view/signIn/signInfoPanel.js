Ext.define('KitchenSink.view.signIn.signInfoPanel', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.signIn.signInfoController',
       'KitchenSink.view.signIn.signStore'
       // 'tranzvision.extension.grid.Exporter'

    ],
    xtype: 'signInfoPanel',
    controller: 'signInfoController',
    reference:'signInfoPanel',
	selModel: {
       	type: 'checkboxmodel'
    },
    columnLines: true,

    style:"margin:8px",
    multiSelect: true,
    title: '签到信息',
    viewConfig: {
        enableTextSelection: true
     },
    header:false,
    frame: true,
    dockedItems:[{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
               {minWidth:80,text:"关闭",iconCls:"close",handler:"SignInfoWindowClose"},
               {minWidth:80,text:"保存",iconCls:"save",handler:"SignInfoWindowSave"}
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:'查询',iconCls:"query",handler:'selectSign'},'-',
            {text:'删除',tooltip:'删除',iconCls:"remove",handler:'deleteSign'}
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.signIn.signStore();
        Ext.apply(this, {
            columns: [
                //new Ext.grid.RowNumberer() , 列表序号
                {
                    text:"签到人",
                    sortable: true,
                    dataIndex: 'nickName',
                    width:200
                },
                {
                    text:"签到时间",
                    sortable: true,
                    align: 'center',
                    dataIndex: 'signTime',
                    width:200
                },
                {
                    text:"签到地点",
                    sortable: true,
                    dataIndex: 'ibeaconName',
                    width:400
                },
                {
                    text:"签到距离(米)",
                    sortable: true,
                    dataIndex: 'signAccuracy',
                    width:200
                },
                {
                    menuDisabled: true,
                    sortable: false,
                    text: '操作',
                    align: 'center',
                    xtype: 'actioncolumn',
                    width:60,
                    items:[
                        {iconCls:'remove',tooltip:'删除',handler:'deleteOneSign'}
                        ]
              
               }]
        , 
        store:store,
        bbar: {
            xtype: 'pagingtoolbar',
            pageSize: 30,
            store: store,
            displayInfo: true,
            displayMsg: '显示{0}-{1}条，共{2}条',
            beforePageText: '第',
            afterPageText: '页/共{0}页',
            emptyMsg: '没有数据显示',
            plugins: new Ext.ux.ProgressBarPager()}
        });
        this.callParent();
    }
});

