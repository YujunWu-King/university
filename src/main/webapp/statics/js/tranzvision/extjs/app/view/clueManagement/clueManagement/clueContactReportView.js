Ext.define('KitchenSink.view.clueManagement.clueManagement.clueContactReportView', {
    extend: 'Ext.panel.Panel',
    xtype:'clueContactReportView',
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
    listeners:{
        afterrender:function(panel){
            panel.down("timeLine").controller.loadPointsView(this.TZ_LEAD_ID,panel.down("timeLine"));
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
        Ext.apply(this,config);
        this.callParent();
    }
});