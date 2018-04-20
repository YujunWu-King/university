Ext.define('KitchenSink.view.clueManagement.backgroundConfig.areaLabel.defnAreaLabel', {
    extend: 'Ext.grid.Panel',
    title:"地区标签定义",
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.clueManagement.backgroundConfig.areaLabel.defnAreaLabelStore',
        'KitchenSink.view.common.store.comboxStore',
        'KitchenSink.view.clueManagement.backgroundConfig.areaLabel.defnAreaLabelController'
    ],
    xtype: 'defnAreaLabel',
    style:"margin:8px",
    controller: 'defnAreaLabelController',
    header:false,
    name:"defnAreaLabel",
    frame: true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    multiSelect: true,
    columnLines: true,
    viewConfig: {
        enableTextSelection: true
    },
    selModel: {
        type: 'checkboxmodel'
    },
    dockedItems:[{xtype:"toolbar",dock:"bottom",ui:"footer",items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:'save'},{minWidth:80,text:"确定",iconCls:"ensure",handler:'ensure'},{minWidth:80,text:"关闭",iconCls:"close",handler: function(btn){
        btn.findParentByType('panel').close();
    }}]},{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:"查询数据",iconCls:"query",handler:"queryData"},
            {text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addData"},
            {text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editCheckedData"},
            {text:"删除",tooltip:"编辑数据",iconCls:"remove",handler:"deleteCheckedData"}
        ]
    }/*,{
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
    }*/],
    initComponent: function () {
        var store = new KitchenSink.view.clueManagement.backgroundConfig.areaLabel.defnAreaLabelStore();

        var statusStore = new KitchenSink.view.common.store.appTransStore("TZ_XSXS_DEFN");
        statusStore.load();
        Ext.apply(this,{
            store:store,
            columns: [/*{
                text: '常用短语编号',
                dataIndex: 'commonPhraseID',
                width:150
            },*/{
                text: '机构ID',
                dataIndex: 'orgID',
                hidden: true,
                width:110
            },{
                text: '地区码',
                dataIndex: 'areaLabelName',
                minWidth:100,
                flex:1
            },{
                text: '描述',
                dataIndex: 'areaLabelDesc',
                minWidth:200,
                flex:3
            },{
                text: '状态',
                dataIndex: 'areaLabelStatus',
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
                    {iconCls: 'edit',tooltip: '编辑',handler:'editCurrentData'},
                    {iconCls: 'remove',tooltip: '删除',handler:'removeCurrentData'}
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