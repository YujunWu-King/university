Ext.define('KitchenSink.view.clueManagement.clueManagement.enrollmentClueSmsHistory', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.clueManagement.clueManagement.enrollmentClueSmsHistoryStore',
        'KitchenSink.view.clueManagement.clueManagement.enrollmentClueController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'SmsHistoryPanel',
    columnLines: true,
    style:"margin:8px",
    title: '查看短信发送历史',
    header:false,
    frame: true,
    controller: 'enrollmentClueController',
    dockedItems:[
        {
            xtype:"toolbar",
            dock:"bottom",
            ui:"footer",
            items:['->',
                {
                    minWidth:80,text:"关闭",iconCls:"close",handler: function(btn){
                    //获取窗口
                    var win = btn.findParentByType("enrollmentClueSmsHistory");
                    //关闭窗口
                    win.close();
                }
                }]
        }
    ],
    initComponent: function () {

    	var enrollmentClueSmsHistoryStore = new KitchenSink.view.clueManagement.clueManagement.enrollmentClueSmsHistoryStore();

        Ext.apply(this, {
            columns: [{
                text: '发送状态',
                dataIndex: 'status',
                minWidth: 200,
                flex: 1
            },{
                text: '发送对象',
                dataIndex: 'phone',
                minWidth: 200,
                flex: 1
            },/*{
                text: '操作人',
                dataIndex: 'operator',
                minWidth: 200,
                flex: 1
            },*/{
                text: '发送时间',
                dataIndex: 'sendDt',
                minWidth: 200,
                flex: 1	
			},{
               menuDisabled: true,
               sortable: false,
			   width:60,
               align:'center',
               xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '查看发送历史正文',handler: 'editZw'}
			   ]
            }],
           store: enrollmentClueSmsHistoryStore,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: enrollmentClueSmsHistoryStore,
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
