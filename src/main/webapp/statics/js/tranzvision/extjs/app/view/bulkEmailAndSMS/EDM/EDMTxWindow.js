Ext.define('KitchenSink.view.bulkEmailAndSMS.EDM.EDMTxWindow', {
    extend: 'Ext.window.Window',
    xtype: 'EDMTxWindow',
    reference:'EDMTxWindow',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bulkEmailAndSMS.EDM.EDMTxStore',
        'KitchenSink.view.bulkEmailAndSMS.EDM.EDMSetController'
	],
    modal:true,
    title: "",
    layout:'fit',
    height: 400,
    width: 800,
    title:'退信引擎运行状态',
    yjqfId:'',
    initComponent: function(){
		var listStore = new KitchenSink.view.bulkEmailAndSMS.EDM.EDMTxStore();
        Ext.apply(this,{
            items: [ {
                xtype: 'grid',
                autoHeight: true,
                minHeight:200,
                columnLines: true,
                store: listStore,
                dockedItems: [{
                    xtype: "toolbar",
                    items: [
                        {text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.query","查询"),  iconCls: "query",handler:"txRzQuery"}
                    ]
                },{
                    xtype:"toolbar",
                    dock:"bottom",
                    ui:"footer",
                    items:['->',
                        {minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),iconCls:"close",handler:'txRzClose'}]
                }],
                columns: [
                    {
                        text:'实例ID',
                        dataIndex:'AEId',
                        width:150
                    },
                    {   text: "运行退信引擎时间",
                        dataIndex: 'txAETime',
                        width:300
                    },
                    {
                        text:"运行状态",
                        dataIndex: 'ProStaDesc',
                        width: 200,
                    },{
                        xtype:'actioncolumn',
                        text:"下载日志",
                        flex:1,
                        items:[
                            {
                                iconCls:' download',
                                sortable:false,
                                handler: "downloadTzRz",
                                isDisabled:function(view ,rowIndex ,colIndex ,item,record ){
									var AEState = record.get('AEState');
									if(AEState == "SUCCEEDED"){
										return false;
									}else{
										return true;
									}
								}
                            }
                        ]
                    }
                ],
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 5,
                    listeners: {
                        afterrender: function (pbar) {
                            var grid = pbar.findParentByType("grid");
                            pbar.setStore(grid.store);
                        }
                    },
                    plugins: new Ext.ux.ProgressBarPager()
                }
            }]
        });
        this.callParent();
    }
});
