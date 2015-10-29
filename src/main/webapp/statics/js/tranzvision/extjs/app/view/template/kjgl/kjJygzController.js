Ext.define('KitchenSink.view.template.kjgl.kjJygzController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.kjJygzController',

    pmtSearchJygz: function(btn){
        var form = this.getView().child("form").getForm();
        Ext.tzShowPromptSearch({
            recname: 'TZ_JYGZ_DY_T',
            searchDesc: '校验规则',
            maxRow:20,
            condition:{
                presetFields:{
                    TZ_EFFEXP_ZT:{
                        value: "Y",
                        type: '01'
                    }
                },
                srhConFields:{
                    TZ_JYGZ_ID:{
                        desc:'校验规则ID',
                        operator:'01',
                        type:'01'
                    },
                    TZ_JYGZ_MC:{
                        desc:'校验规则名称',
                        operator:'01',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_JYGZ_ID: '校验规则ID',
                TZ_JYGZ_MC: '校验规则名称'
            },
            multiselect: false,
            callback: function(selection){
                form.findField("kjJygzID").setValue(selection[0].data.TZ_JYGZ_ID);
                form.findField("kjJygzMx").setValue(selection[0].data.TZ_JYGZ_MC);
            }
        });
    },
    shuaxinKjJygz:function(){
        //刷新数据,取得当前活动页面
        var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab();
        //targetStore = activeTab.down("grid[name=materialsReviewApplicantsGrid]").getStore()
        //取得控件id
        var formkj = activeTab.child('form').getForm();
        var kjID = formkj.getValues()['kjID'];
        //获得控件校验规则store
        var kjJygzStore = activeTab.child('tabpanel').down('grid[name=kjJygzGrid]').store;
        tzStoreParams = '{"kjID":"'+kjID+'","queryType":"JYGZ"}';
        kjJygzStore.tzStoreParams = tzStoreParams;
        kjJygzStore.load();
    },
    sureKjJygz: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        var form = win.child("form").getForm();

        if(!form.isValid()){
            return false;
        }
        //获取数据;
        var formParams = form.getValues();
        //Params
        var tzParams = '{"ComID":"TZ_KJGL_COM","PageID":"TZ_KJJYGZ_INFO_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
        Ext.tzSubmit(tzParams,function(response){
            win.close();

            //刷新数据,取得当前活动页面
            var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab();
            //targetStore = activeTab.down("grid[name=materialsReviewApplicantsGrid]").getStore()
            //取得控件id
            var formkj = activeTab.child('form').getForm();
            var kjID = formkj.getValues()['kjID'];
            //获得控件校验规则store
            var kjJygzStore = activeTab.child('tabpanel').down('grid[name=kjJygzGrid]').store;
            tzStoreParams = '{"kjID":"'+kjID+'","queryType":"JYGZ"}';
            kjJygzStore.tzStoreParams = tzStoreParams;
            kjJygzStore.load();

        },"",false,this);
    },
    doSaveKjJygz:function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        var form = win.child("form").getForm();
        if(!form.isValid()){
            return false;
        }
        //form datas
        var formParams = form.getValues();
        //Params
        var tzParams = '{"ComID":"TZ_KJGL_COM","PageID":"TZ_KJJYGZ_INFO_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
        Ext.tzSubmit(tzParams,function(response){

        },"",false,this);
    },
    closeKjJygz: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        //关闭窗口
        win.close();
        this.shuaxinKjJygz();
    }
});