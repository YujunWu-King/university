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
		var processingStatus = new KitchenSink.view.common.store.appTransStore("TZ_AE_STATUS");
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
                        dataIndex: 'AEState',
                        width: 200,
                        renderer:function(v) {
                            if (v == '1') {
                                return "取消";
                            } else if (v == '10') {
                                return "未完成";
                            }else if(v=='2'){
                                return "删除";
                            }else if(v=='3'){
                                return "错误";
                            }else if(v=='4'){
                             return "搁置";
                            }else if (v == '5') {
                                return "已排队";
                            }else if(v=='6'){
                                return "已启动";
                            }else if(v=='7'){
                                return "正在处理";
                            }else if(v=='8'){
                                return '已取消';
                            }else if(v=='9'){
                               return '成功';
                            }
                        }
                    },
                       /* renderer:function(v){
                            if(v){
                               var rec = processingStatus.find('TValue',v,0,false,true,false);
                                console.log(v,rec);
                               if(rec>-1){
                                return processingStatus.getAt(rec).get("TSDesc");
                                }else{
                                    return "";
                                }
                            }else{
                                return "";
                            }
                        }*/
                    {
                        xtype:'actioncolumn',
                        text:"下载日志",
                        flex:1,
                        items:[
                            {
                                iconCls:'download',
                                sortable:false,
                                handler: "downloadTzRz"
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
