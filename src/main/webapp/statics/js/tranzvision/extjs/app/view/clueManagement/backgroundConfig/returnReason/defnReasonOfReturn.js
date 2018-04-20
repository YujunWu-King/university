Ext.define('KitchenSink.view.clueManagement.backgroundConfig.returnReason.defnReasonOfReturn', {
    extend: 'Ext.grid.Panel',
    title:"退回原因定义",
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.common.store.comboxStore',
        'KitchenSink.view.clueManagement.backgroundConfig.returnReason.defnReasonOfReturnStore',
        'KitchenSink.view.clueManagement.backgroundConfig.returnReason.defnReasonOfReturnController'
    ],
    xtype: 'defnChannel',
    style:"margin:8px",
    controller: 'XSGLTHYYController',
    header:false,
    frame: true,
    name:"defnReasonOfReturnGrid",
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    multiSelect: true,
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    viewConfig: {
        enableTextSelection: true
    },
    dockedItems:[{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:"查询数据",iconCls:"query",handler:"queryData"},
            {text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addData"},
            {text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editCheckedData"}
        ]
    },{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
            {
                text:"关闭",
                iconCls:"close",
                // handler: 'onGridPanelClose'
                handler: function(btn){
                    btn.findParentByType('panel').close();
                }
            }]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.clueManagement.backgroundConfig.returnReason.defnReasonOfReturnStore();
        /*    transValue = this.controller.transvalueCollection = this.controller.transValues();
        transValue.set("TZ_XSGL_STATUS",function(){
            var tzParams = '{"ComID":"TZ_THYY_XSGL_COM","PageID":"TZ_THYY_XSGL_STD","OperateType":"GS","comParams":{}}';
            Ext.tzLoad(tzParams,function(respData){
                store.tzStoreParams = '{"cfgSrhId": "TZ_THYY_XSGL_COM.TZ_THYY_XSGL_STD.TZ_THYY_XSGL_V","condition":{"TZ_THYY_ID-operator":"07","TZ_THYY_ID-value":"","SETID-operator":"07","SETID-value":"'+(respData.setid||"SHARE")+'","TZ_LABEL_DESC-operator":"07","TZ_LABEL_DESC-value":""}}';
                store.load();
            });
        });*/
        var statusStore = new KitchenSink.view.common.store.appTransStore("TZ_XSXS_DEFN");
        statusStore.load();
        Ext.apply(this,{
            store:store,
                columns: [{
                    text: '退回原因编号',
                    dataIndex: 'reasonID',
                    minWidth:150
                },{
                    text: '机构ID',
                    dataIndex: 'orgID',
                    width:110
                },{
                    text: '退回原因名称',
                    dataIndex: 'reason',
                    minWidth:100,
                    flex:1
                },{
                    text: '描述',
                    dataIndex: 'description',
                    minWidth:200,
                    flex:3
                },{
                    text: '状态',
                    dataIndex: 'status',
                    renderer: function (v,grid,record){
                        var x;
                        // var statusStore = transValue.get("TZ_XSGL_STATUS");
                        v = v?v:'N';//状态为空默认为无效
                        if((x = statusStore.find('TValue',v))>=0){
                            return statusStore.getAt(x).data.TSDesc;
                        }else{
                            return v;
                        }
                    }
                },{
                    // text:"操作",
                    align:'center',
                    menuDisabled: true,
                    sortable: false,
                    minWidth:80,
                    xtype: 'actioncolumn',
                    items:[
                        {iconCls:'edit',tooltip:'编辑',handler:'editCurrentData'}
                    ]
                }],
            bbar: {
                xtype: 'pagingtoolbar',
                store:store,
                pageSize: 10,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});