Ext.define('KitchenSink.view.clueManagement.clueManagement.viewOrAddLxReportWindow', {
    extend: 'Ext.window.Window',
    title: "联系报告",
    requires: [
        'Ext.data.*',
        'Ext.util.*',
        'KitchenSink.view.clueManagement.clueManagement.clueContactReportController'
    ],
    controller:"clueContactReportController",
    width: 1000,
    modal:true,
    minHeight: 200,
    maxHeight: 500,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    y: 100,
    layout: {
        type: 'fit'
    },
    
    constructor:function(config){
        this.clueID = config.clueID;
        this.callParent();
    },
    
    listeners:{
        afterrender:function(panel){
			var clueID = panel.clueID;
			
			Ext.tzSetCompResourses("TZ_XSXS_INFO_COM");
            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_CONNECT_RPT_STD"];
            if( pageResSet == "" || pageResSet == undefined){
                Ext.MessageBox.alert('提示', '您没有修改数据的权限');
                return;
            }
            //该功能对应的JS类
            var className = pageResSet["jsClassName"];
            if(className == "" || className == undefined){
                Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CONNECT_RPT_STD，请检查配置。');
                return;
            }
            var contentPanel, cmp, ViewClass, clsProto;

            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
            contentPanel.body.addCls('kitchensink-example');

            if(!Ext.ClassManager.isCreated(className)){
                Ext.syncRequire(className);
            }
            ViewClass = Ext.ClassManager.get(className);
			
            panel.add(new ViewClass({
                TZ_LEAD_ID:clueID,
                winFlag: 'Y'
            }));
        }
    },
    
    buttons: [{
        text: '关闭',
        iconCls:"close",
        handler: function(btn){
        	btn.findParentByType('window').close();
        }
    }]
});