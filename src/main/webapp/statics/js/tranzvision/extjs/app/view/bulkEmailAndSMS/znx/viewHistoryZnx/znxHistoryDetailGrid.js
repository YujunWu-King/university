
Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxHistoryDetailGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxHistoryDetailGridStore',
        'KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxHistoryPanelController',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.common.store.comboxStore'
    ],
    xtype: 'znxHistoryDetailGrid',
    columnLines: true,
    style:"margin:8px",
    title: '查看发送明细',
    header:false,
    frame: true,
    controller: 'znxHistoryPanelController',
    taskID:" ",
    dockedItems:[
        {
            xtype:"toolbar",
            dock:"bottom",
            ui:"footer",
            items:['->',
                {
                    minWidth:80,text:"关闭",iconCls:"close",handler: function(btn){
                    //获取窗口
                    var win = btn.findParentByType("znxHistoryDetailGrid");
                    //关闭窗口
                    win.close();
                }
                }]
        },{
            xtype:"toolbar",
            items:[
                {text:"查询",tooltip:"查询数据",iconCls:"query",handler:"queryemailHistoryDetail"}
            ]
        }/**,{
			xtype:"toolbar",
			items:[
				{
					text:"查询",tooltip:"查询数据",iconCls: "query",handler: function(btn){
								//获取窗口
								var win = btn.findParentByType("commonEmailAddressee");
								//关闭窗口
								win.close();
					}
				}
			]}***/
    ],
    initComponent: function () {
var sendStatusStore = new KitchenSink.view.common.store.appTransStore("TZ_FS_ZT");
        var znxHistoryDetailGridStore = new KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxHistoryDetailGridStore({
        	listeners:{
        		beforeload:function(store){
        			sendStatusStore.load();
        			}
        	}
        	});


        Ext.apply(this, {
            columns: [{
                text: '姓名',
                dataIndex: 'AddresseeName',
                width: 120
            }/*,{
                text: '邮箱',
                dataIndex: 'AddresseeEmail',
                minWidth: 120,
                flex: 1
            }*/,{
				text: '发送时间',
				dataIndex: 'sendTime',
				minWidth: 120,
				flex: 1	
			},{
                text: '状态',
                dataIndex: 'status',
                width: 100,
                    renderer: function (v,grid,record) {
                    var x;
                  
                    if((x = sendStatusStore.find('TValue',v))>=0){
                        return sendStatusStore.getAt(x).data.TSDesc;
                    }else{
                        return v;
                    }
                }
            },{
                header: '查看',
                xtype:'linkcolumn',
                sortable: false,
                value:'查看',
                minWidth: 80,
                handler:'viewEachZnxDetail'

            }],
            store: znxHistoryDetailGridStore,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: znxHistoryDetailGridStore,
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
//    constructor: function () {
//        //发送的模板ID;
////        this.tmpId = config.tmpId;
////        this.callParent();
//    },
////    listeners:{
////        resize: function(win){
////            win.doLayout();
////        }
////    }

});
