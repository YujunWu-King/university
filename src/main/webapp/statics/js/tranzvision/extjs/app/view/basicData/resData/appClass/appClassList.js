Ext.define('KitchenSink.view.basicData.resData.appClass.appClassList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.basicData.resData.appClass.appClassController',
        'KitchenSink.view.basicData.resData.appClass.appClassModel',
        'KitchenSink.view.basicData.resData.appClass.appClassStore'
    ],
    xtype: 'appClassList',
    controller: 'appClassController',
    store: {
        type: 'appClassStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style:"margin:8px",
    multiSelect: true,
    title: '应用程序类定义',
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveAppClass",name:'save'},
            {minWidth:80,text:"确定",iconCls:"ensure",handler:"saveAppClass",name:'ensure'},
            {minWidth:80,text:"关闭",iconCls:"close",handler:
                function(btn){
                    var grid = btn.findParentByType("grid");
                    grid.close();
                }
            }
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'cfgSearchAppCls'},"-",
            {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addAppClassDfn'},"-",
            {text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editAppClassDfn'},"-",
            {text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteAppClassDfns'}
        ]
    }],
    initComponent: function(){
        var store = new KitchenSink.view.basicData.resData.appClass.appClassStore();
        Ext.apply(this, {
            columns: [{
                text: '应用程序类ID',
                dataIndex: 'appClassId',
                minWidth: 400
            },{
                text: '类方法描述',
                sortable: true,
                dataIndex: 'appClassDesc',
                minWidth: 400,
                flex: 1
            },{
                menuDisabled: true,
                sortable: false,
                width:60,
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'edit',tooltip: '编辑',handler:'editSelAppClassDfn'},
                    {iconCls: 'remove',tooltip: '删除',handler:'deleteSelAppClassDfn'}
                ]
            }],
            store:store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store:store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});
