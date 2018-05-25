Ext.define('KitchenSink.view.clueManagement.backgroundConfig.consultType.defnConsultType', {
    extend: 'Ext.grid.Panel',
    title:"咨询类别定义",
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.common.store.comboxStore',
        'KitchenSink.view.clueManagement.backgroundConfig.consultType.defnConsultTypeStore',
        'KitchenSink.view.clueManagement.backgroundConfig.consultType.defnConsultTypeController'
    ],
    xtype: 'defnConsultType',
    style:"margin:8px",
    controller: 'defnConsultTypeController',
    header:false,
    frame: true,
    name:"consultType",
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
                handler: function(btn){
                    btn.findParentByType('panel').close();
                }
            }]
    }],
    initComponent: function () {
	
        var store = new KitchenSink.view.clueManagement.backgroundConfig.consultType.defnConsultTypeStore();
        /*var tzParams = '{"ComID":"TZ_XSXS_ZXLB_COM","PageID":"TZ_XSXS_ZXLB_STD","OperateType":"GS","comParams":{}}';
        Ext.tzLoad(tzParams,function(respData) {
            store.tzStoreParams = '{"cfgSrhId": "TZ_XSXS_ZXLB_COM.TZ_XSXS_ZXLB_STD.TZ_XSXS_ZXLB_V","condition":{"SETID-operator":"07","SETID-value":"'+(respData.setID||"SHARE")+'"}}';
            store.load();
        });*/
		

        var statusStore = new KitchenSink.view.common.store.appTransStore("TZ_XSXS_DEFN");
        statusStore.load();
        Ext.apply(this,{
            store:store,
            columns: [{
                text: '咨询类别编号',
                dataIndex: 'consultSortID',
                width:150
            },{
                text: '机构ID',
                dataIndex: 'orgID',
                width:110
            },{
                text: '咨询类别名称',
                dataIndex: 'consultSortName',
                minWidth:100,
                flex:1
            },{
                text: '描述',
                dataIndex: 'consultSortDesc',
                minWidth:200,
                flex:3
            },{
                text: '状态',
                dataIndex: 'consultSortStatus',
                // width:170,
                renderer: function (v,grid,record) {
                    var x;
                    v = v?v:'N';
                    if((x = statusStore.find('TValue',v))>=0){
                        return statusStore.getAt(x).data.TSDesc;
                    }
                }
            },{
                menuDisabled: true,
                sortable: false,
                // text:'操作',
                minWidth:80,
                align:'center',
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'edit',tooltip: '编辑',handler:'editCurrentData'}
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