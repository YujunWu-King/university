Ext.define('KitchenSink.view.interviewManagement.interviewArrange.MsjyTimeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.MsjyTimeController',
    //保存面试建议时间
    SaveMsjyTime: function(btn){
        var msjytimeWinForm = btn.up('window').child('form').getForm();
        var msjytimeWinFormRec = msjytimeWinForm.getFieldValues();
        var startJyTime = msjytimeWinFormRec["startJyTime"];
        var endJyTime = msjytimeWinFormRec["endJyTime"];
        if (startJyTime==null &&endJyTime!=null){
            Ext.Msg.alert("提示","请选择【开始时间】.");
            return;
        }else if (startJyTime!=null &&endJyTime==null){
            Ext.Msg.alert("提示","请选择【结束时间】.");
            return;
        }else if(startJyTime!=null &&endJyTime!=null){
            if(startJyTime>endJyTime){
                Ext.Msg.alert("提示","【开始时间】大于【结束时间】.");
                return;
            }
        }
        var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_JYSJ_STD","OperateType":"U","comParams":{"update":[{"startJyTime":"'+startJyTime+'","endJyTime":"'+endJyTime+'"}]}}';
        Ext.tzSubmit(tzParams,function(responseData){

        },"",true,this);
    },
    //确认面试建议时间
    SureMsjyTime:function(btn){
        this.SaveMsjyTime(btn);
        this.CloseMsjyTime(btn);
    },
    //关闭面试建议时间
    CloseMsjyTime: function(btn){
        var win = btn.findParentByType("window");
        win.close();
    }
});