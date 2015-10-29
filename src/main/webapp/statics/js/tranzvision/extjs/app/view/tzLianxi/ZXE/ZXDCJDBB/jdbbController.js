/**
 * Created by carmen on 2015/9/7.
 */
Ext.define('KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.jdbbController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.jdbb',

    onJdbbInfoClose:function(){
    this.getView().close();
}


   /* getJDBBData:function(obj,rowIndex){
       // var jygzID;
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZXDC_JDBB_COM"]["TZ_ZXDC_JDBB_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZXDC_JDBB_STD，请检查配置。');
            return;
        }
        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className.toString());
        clsProto = ViewClass.prototype;

        if (clsProto.themes) {
            clsProto.themeInfo = clsProto.themes[themeName];

            if (themeName === 'gray') {
                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
            } else if (themeName !== 'neptune' && themeName !== 'classic') {
                if (themeName === 'crisp-touch') {
                    clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
                }
                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
            }
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }

        cmp = new ViewClass();
        //操作类型设置为查询
        //cmp.actType = "query";

        cmp.on('afterrender',function(panel){

            var form = panel.child('form').getForm();
            form.findField("onlinedcId").setReadOnly(true);
            form.findField("onlinedcId").addCls("lanage_1");

            //参数
            var tzParams = '{"ComID":"TZ_ZXDC_JDBB_COM ","PageID":"TZ_ZXDC_JDBB_STD","OperateType":"QF","comParams":{"onlinedcId":"' + onlinedcId + '"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                var formData = responseData.formData;
                form.setValues(formData);
            });

            tzParams = '{"ComID":"TZ_ZXDC_JDBB_COM ","PageID":"TZ_ZXDC_JDBB_STD","OperateType":"WCBL","comParams":{"onlinedcId":"' +onlinedcId + '"}}';
            Ext.tzLoad(tzParams,function(responseData){

                var chart = panel.down('chart[name=chart1]');
                Params= '{"onlinedcId":"' + onlinedcId + '"}';
                chart.store.tzStoreParams = Params;
                chart.store.reload();
            });

            tzParams = '{"ComID":"TZ_ZXDC_JDBB_COM ","PageID":"TZ_ZXDC_JDBB_STD","OperateType":"WEEK","comParams":{"onlinedcId":"' +onlinedcId + '"}}';
            Ext.tzLoad(tzParams,function(responseData){

                var chart = panel.down('chart[name=chart2]');
                Params= '{"onlinedcId":"' + onlinedcId + '"}';
                chart.store.tzStoreParams = Params;
                chart.store.reload();
            });


        });
        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    }*/



})