
Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsHistoryDetailGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsHistoryDetailGridStore',
        'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsHistoryPanelController',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.common.store.comboxStore'
    ],
    xtype: 'SmsHistoryDetailGrid',
    controller: 'SmsHistoryPanelController',
    columnLines: true,
    style:"margin:8px",
    title: '查看发送明细',
    header:false,
    frame: true,
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
                    var win = btn.findParentByType("SmsHistoryDetailGrid");
                    //关闭窗口
                    win.close();
                }
                }]
        },{
            xtype:"toolbar",
            items:[
                {text:"查询",tooltip:"查询数据",iconCls:"query",handler:"querysmsHistoryDetail"}
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

        var SmsHistoryDetailGridStore = new KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsHistoryDetailGridStore({
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
                minWidth: 90,
                flex: 1
            },{
                text: '手机号',
                dataIndex: 'AddresseePhone',
                minWidth: 90,
                flex: 1
            },{
                text: '发送时间',
                dataIndex: 'sendTime',
                minWidth: 120,
                flex: 1
            },{
                text: '状态',
                dataIndex: 'status',
                minWidth: 120,
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
                minWidth: 150,
                handler:'viewEachSmsDetail'

            }],
            store: SmsHistoryDetailGridStore,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: SmsHistoryDetailGridStore,
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
