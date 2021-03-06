Ext.define('KitchenSink.view.processDispatchMg.processLogsPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'processLogsPanel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.processDispatchMg.processLogsStore'
    ],
    title: '进程运行日志',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    listeners: {
        resize: function(panel, width, height, oldWidth, oldHeight, eOpts) {
        var buttonHeight = 42; //button height plus panel body padding
        var formHeight = 30;
        var formPadding = 20;
        var grid = panel.child('grid[name=loggrid]');
        grid.setHeight(height - formHeight - buttonHeight - formPadding);
        }
        },
    items:[{
        xtype: 'grid',
        frame: true,
        height: 500,
        columnLines: true,
        selModel: {
            type: 'checkboxmodel'
        },
        name:'loggrid',
        multiSelect: true,
        style:"margin:10px",
        store: {
            type: 'processLogsStore'
        },
        columns: [{
            text: '序号',
            dataIndex: 'orderNum'
        },{
            text: '日志级别',
            dataIndex: 'logLevel',
            width: 200,
            flex: 1
        },{
            text: '日期时间',
            dataIndex: 'dateTime',
            minWidth: 200,
            flex: 1
        },{
            text: '日志详细内容',
            dataIndex: 'logDetail',
            minWidth: 200,
            flex: 1
        }
        ],
        bbar: {
            xtype: 'pagingtoolbar',
            pageSize: 5,
            listeners:{
                afterrender: function(pbar){
                    var grid = pbar.findParentByType("grid");
                    pbar.setStore(grid.store);
                }
            },
            displayInfo: true,
            displayMsg: '显示{0}-{1}条，共{2}条',
            beforePageText: '第',
            afterPageText: '页/共{0}页',
            emptyMsg: '没有数据显示',
            plugins: new Ext.ux.ProgressBarPager()
        }
    }],
    buttons: [{
        text: '关闭',
        iconCls: "close",
        handler: function (btn) {
        	var panel = btn.findParentByType("panel");
        	panel.close();
        }
    }],
});
