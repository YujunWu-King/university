Ext.define('KitchenSink.view.clueManagement.clueManagement.clueContactReport', {
    extend: 'Ext.panel.Panel',
    xtype:'clueContactReport',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.clueManagement.clueManagement.clueContactReportController',
        'KitchenSink.view.clueManagement.clueManagement.timeLine'
    ],
    bodyStyle:"overflow-x:hidden;overflow-y:auto",
//    dockedItems:[{
//        xtype:"toolbar",
//        style: 'box-shadow: 0 5px 5px -5px #888;',
//        items:[
//            {text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addReport"}
//        ]
//    }],
    listeners:{
        afterrender:function(panel){
            panel.down("timeLine").controller.loadPoints(this.TZ_LEAD_ID,panel.down("timeLine"));
			//panel.down("timeLine").setHidden(true);
        }
    },
    constructor:function(config){
        var controller = new KitchenSink.view.clueManagement.clueManagement.clueContactReportController();
        this.controller = controller;
        
        config.items = config.itmes||[{
                    xtype:"timeLine",
                    margin: 8,
                    controller:controller,
                    TZ_LEAD_ID:config.TZ_LEAD_ID||'1'
                }];
        
        config.dockedItems = [{
            xtype:"toolbar",
            style: (config.winFlag == "Y" ? 'box-shadow: 0 5px 5px -5px #888;' : ''),
            items:[
                {text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addReport"}
            ]
        }],
        
        Ext.apply(this,config);
        this.callParent();
    }
});