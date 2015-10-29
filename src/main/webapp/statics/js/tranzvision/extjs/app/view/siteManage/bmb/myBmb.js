Ext.define('KitchenSink.view.template.bmb.myBmb', {
    extend: 'Ext.grid.Panel',
    requires: [	'Ext.data.*', 
				'Ext.grid.*', 
				'Ext.util.*', 
				'Ext.toolbar.Paging', 
				'Ext.ux.ProgressBarPager', 
				'KitchenSink.view.template.bmb.myBmbController', 
				'KitchenSink.view.template.bmb.myBmbModel', 
				'KitchenSink.view.template.bmb.myBmbStore'
			],
    xtype: 'myBmb',
    controller: 'myBmb',
    store: {
        type: 'myBmbStore'
    },
    columnLines: true,
    selModel: {
        //type: 'checkboxmodel'
    },
    style: "margin:8px",
    //multiSelect: true,
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
        Ext.apply(this, {
            columns: [{
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
                width: 60,
                xtype: 'actioncolumn',
                items: [{
                    iconCls: 'edit',
                    tooltip: '编辑',
                    handler: 'editBmbTpl'
                },
                {
                    iconCls: 'copy',
                    tooltip: '复制',
                    handler: 'copyBmbTpl'
                },
                {
                    iconCls: 'preview',
                    tooltip: '预览',
                    handler: 'previewBmbTpl'
                }]
            }],
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: {
                    type: 'myBmbStore'
                },
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
