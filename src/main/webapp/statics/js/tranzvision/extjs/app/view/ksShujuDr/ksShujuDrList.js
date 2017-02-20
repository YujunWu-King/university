Ext.define('KitchenSink.view.ksShujuDr.ksShujuDrList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.ksShujuDr.ksShujuDrController',
        'KitchenSink.view.ksShujuDr.ksShujuDrStore'
    ],
   // alilas: 'widget.importTplList',
    controller: 'ksShujuDrController',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style:"margin:8px",
    multiSelect: true,
    title: '考生数据导入',
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
            {minWidth:80,text:"关闭",iconCls:"close",handler:
                function(btn){
                    var grid = btn.findParentByType("grid");
                    grid.close();
                }
            }
        ]
    }],
    initComponent: function(){
        var store = new KitchenSink.view.ksShujuDr.ksShujuDrStore();
        Ext.apply(this, {
            columns: [{
                text: '模板编号',
                dataIndex: 'tplId',
                width: 150
            },{
                text: '模板名称',
                dataIndex: 'tplName',
                minWidth: 200,
                flex: 1
            },{
                menuDisabled: true,
                sortable: false,
                width:30,
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'import',tooltip: '导入',handler:'import'}
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
