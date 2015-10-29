Ext.define('KitchenSink.view.interviewManagement.interviewManage.interviewMgrController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.interviewMgrController',
    //查询
    searchComList: function(btn){     //searchComList为各自搜索按钮的handler event;
        alert("---");
        var clsform = this.getView().down('form').getForm().getFieldValues();
        alert(clsformrec);
        alert(clsformrec.classID);
        var classID = clsformrec.classID;

        var batchID = clsformrec.batchID;
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.TZ_MS_ITWMGRL_V',
            condition:
            {
                "TZ_CLASS_ID": classID,          //设置搜索字段的默认值，没有可以不设置condition;
                "TZ_BATCH_ID": batchID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },

    //保存
    onPanelSave: function(){
        //面试信息列表
        var grid = this.getView().down("grid");

        //面试信息数据
        var store = grid.getStore();

        //提交参数
        var tzParams = this.getItwMgrSubmitParams();

        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        });
    },
    //确定
    onPanelConfirm: function(){
        //面试信息列表
        var grid = this.getView().down("grid");
        //面试信息数据
        var store = grid.getStore();
        //提交参数
        var tzParams = this.getItwMgrSubmitParams();
        var comView = this.getView();
        if(tzParams!=""){
            Ext.tzSubmit(tzParams,function(){
                comView.close();
            });
        }else{
            comView.close();
        }
    },
    //关闭
    onPanelClose: function(){
        this.getView().close();
    },

    //获取提交参数
    getItwMgrSubmitParams:function(){
        //更新操作参数
        var comParams = "";

        //修改json字符串
        var editJson = "";

        //面试信息列表
        var grid = this.getView().down("grid");

        //面试信息数据
        var store = grid.getStore();

        //修改记录
        var mfRecs = store.getModifiedRecords();
        var tagCellEditing = grid.getPlugin('tagCellEditing');

        for(var i=0;i<mfRecs.length;i++){
            if(editJson == ""){
                editJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }
        }

        if(editJson != "")comParams = '"update":[' + editJson + "]";

        var tzParams = '{"ComID":"TZ_MS_MGR_COM","PageID":"TZ_MS_IVWMGR_STD","OperateType":"U","comParams":{'+comParams+'}}';

        return tzParams;
    }
});
