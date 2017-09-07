Ext.define('KitchenSink.view.weChat.weChatUser.weChatTagInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.weChatTagInfoController',
    //删除
    deleteTagInfo: function (btn) {
        var me = this,
            view = me.getView(),
            listWin = view.listWin;

        Ext.MessageBox.confirm("确认","该标签下人员将使用该标签属性，是否确认删除?",function(btnId){
            if(btnId=="yes") {
                var form = view.child("form").getForm();
                var formParams = form.getValues();
                var formJson = Ext.JSON.encode(formParams);

                var comParams = '"delete":['+formJson+']';
                var tzParams = '{"ComID":"TZ_WX_USER_COM","PageID":"TZ_WX_TAGXX_STD","OperateType":"U","comParams":{'+comParams+'}}';
                Ext.tzSubmit(tzParams,function(responseData) {
                    if(responseData.errcode!="0" && responseData.errmsg!="") {
                        Ext.MessageBox.alert("提示",responseData.errmsg);
                        return;
                    } else {
                        view.ignoreChangesFlag = true;
                        view.close();
                        //刷新标签列表页面
                        listWin.down("grid").getStore().load();
                    }
                },"",true,this);
            }
        });

    },
    //保存、确定
    saveTagInfo:function(btn) {
        var me = this,
            view = me.getView(),
            actType = view.actType,
            listWin = view.listWin;

        var form = view.child("form").getForm();
        if(form.isValid()) {
            var formParams = form.getValues();
            var formJson = Ext.JSON.encode(formParams);

            var comParams = "";
            if(actType=="add") {
                comParams = '"add":['+formJson+']';
            } else {
                comParams = '"update":['+formJson+']';
            }
            var tzParams = '{"ComID":"TZ_WX_USER_COM","PageID":"TZ_WX_TAGXX_STD","OperateType":"U","comParams":{'+comParams+'}}';
            Ext.tzSubmit(tzParams,function(responseData) {
                if(responseData.errcode!="0" && responseData.errmsg!="") {
                    Ext.MessageBox.alert("提示",responseData.errmsg);
                    return;
                } else {
                    if (actType == "add") {
                        view.actType = "update";
                    }
                    if (btn.name == "ensureTagInfoBtn") {
                        view.close();
                    }
                    //刷新标签列表页面
                    listWin.down("grid").getStore().load();
                }
            },"",true,this);
        }
    },
    //关闭
    closeTagInfo:function() {
        this.getView().close();
    }
});