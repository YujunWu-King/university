Ext.define('KitchenSink.view.bulkEmailAndSMS.EDM.EDMZhiBiaoWindow', {
    extend: 'Ext.window.Window',
    xtype: 'EDMZhiBiaoWindow',
    reference:'EDMZhiBiaoWindow',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bulkEmailAndSMS.EDM.EDMZhiBiaoModel',
        'KitchenSink.view.bulkEmailAndSMS.EDM.EDMSetController',
        'KitchenSink.view.bulkEmailAndSMS.EDM.EDMZhiBiaoStore'
    ],
    modal: true,
    layout: 'fit',
    resizable: true,
    width:800,
    height:400,
    yjqfId:'',//邮件群发ID
    type:'',//指标项类型
    initComponent: function () {
        var store = new KitchenSink.view.bulkEmailAndSMS.EDM.EDMZhiBiaoStore();
        Ext.apply(this, {
            items: [
                {   xtype: 'grid',
                    columnLines: true,
                    viewConfig: {
                        enableTextSelection: true
                    },
                    store:store,
                    columns: this.myColumn,
                    dockedItems:[{
                        xtype:"toolbar",
                        dock:"bottom",
                        ui:"footer",
                        items:['->',
                            {minWidth:80,text:'关闭',iconCls:"close",handler: function (btn)
                            {   var win = btn.findParentByType("window");
                                win.close();
                            }}]
                        },{ xtype:"toolbar",items:[ {text:"查询",iconCls: "query",handler:"findEDMItem"}]
                      }],

                     bbar: {
                     xtype: 'pagingtoolbar',
                     pageSize: 5,
                     listeners:{
                     afterrender: function(pbar){
                         var grid = pbar.findParentByType("grid");
                         pbar.setStore(grid.store);
                       }
                     },
                     plugins: new Ext.ux.ProgressBarPager()
                      }
              }
            ]
        });
        this.callParent();
    },
    constructor:function(col){
        this.myColumn = col;
        this.callParent();
    }
});
