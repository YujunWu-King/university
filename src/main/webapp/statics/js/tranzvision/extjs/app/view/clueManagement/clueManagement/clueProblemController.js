Ext.define('KitchenSink.view.clueManagement.clueManagement.clueProblemController',{
    extend: 'Ext.app.ViewController',
    alias: 'controller.clueProblemController',
    //系统建议操作
    suggestClueProblem:function(btn) {
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var count = store.getCount();

        var tzParams = '{"ComID":"TZ_XSXS_ZSXS_COM","PageID":"TZ_XSXS_WTXS_STD","OperateType":"tzSystemSuggest",comParams:{}}';
        /*
        Ext.tzSubmit(tzParams,function(responseData){
            var clueData = responseData.clueData;
            for(var i=0;i<count;i++) {
                var clueId = store.getAt(i).get("clueId");

                for (var j=0;j<clueData.length;j++) {
                    var clueId_wt = clueData[j].clueId;
                    var suggestOperation_wt = clueData[j].suggestOperation;
                    var systemRuleTip_wt = clueData[j].systemRuleTip;

                    if(clueId==clueId_wt) {
                        store.getAt(i).set("suggestOperation",suggestOperation_wt);
                        store.getAt(i).set("systemRuleTip",systemRuleTip_wt);
                    }
                }
            }
        },"系统正在根据规则匹配，请稍等",true,this);
        */


        //等待信息
        var myMask = new Ext.LoadMask(
            {
                msg    : "系统正在根据规则匹配，请稍等......",
                target : Ext.getCmp('tranzvision-framework-content-panel')
            });

        myMask.show();

        var theFailureFn;
        try
        {
            theFailureFn = eval(failureFn);
        }
        catch(e)
        { }

        try
        {
            Ext.Ajax.request(
                {
                    url: Ext.tzGetGeneralURL(),
                    params:{tzParams: tzParams},
                    //timeout: 60000,
                    //async: false,
                    success: function(response, opts)
                    {
                        //返回值内容
                        var jsonText = response.responseText;
                        try
                        {
                            var jsonObject = Ext.util.JSON.decode(jsonText);
                            /*判断服务器是否返回了正确的信息*/
                            if(jsonObject.state.errcode == 0)
                            {
                                var clueData = jsonObject.comContent.clueData;
                                for(var i=0;i<count;i++) {
                                    var clueId = store.getAt(i).get("clueId");

                                    for (var j=0;j<clueData.length;j++) {
                                        var clueId_wt = clueData[j].clueId;
                                        var suggestOperation_wt = clueData[j].suggestOperation;
                                        var systemRuleTip_wt = clueData[j].systemRuleTip;

                                        if(clueId==clueId_wt) {
                                            store.getAt(i).set("suggestOperation",suggestOperation_wt);
                                            store.getAt(i).set("systemRuleTip",systemRuleTip_wt);
                                        }
                                    }
                                }
                            }
                            else if(jsonObject.state.timeout == true)
                            {
                                try
                                {
                                    if (typeof(theFailureFn) == "function")
                                    {
                                        theFailureFn(jsonObject.state);
                                    }

                                    if(Ext.getCmp("tranzvision_relogin_20150626") == undefined)
                                    {
                                        /*var win = new tranzvision.view.window.ReloginWindow();
                                         win.show();*/
                                        Ext.Msg.alert(TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00021"),TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00101"),function(optional){
                                            if(optional=="ok"){
                                                var org=Ext.tzOrgID.toLowerCase();
                                                window.location.href= TzUniversityContextPath+"/login/"+org;
                                            }
                                        });
                                    }
                                }
                                catch(e2)
                                {}
                            }
                            else
                            {
                                if (typeof(theFailureFn) == "function")
                                {
                                    theFailureFn(jsonObject.state);
                                }
                                TranzvisionMeikecityAdvanced.Boot.errorMessage(jsonObject.state.errdesc);
                            }
                        }
                        catch(e)
                        {
                            if (typeof(failureFn) == "function")
                            {
                                failureFn(jsonText);
                            }
                            TranzvisionMeikecityAdvanced.Boot.errorMessage(e.toString());
                        }
                    },
                    failure: function(response, opts)
                    {
                        //错误信息响应报文
                        try
                        {
                            var respText = Ext.util.JSON.decode(response.responseText);
                            if (typeof(failureFn) == "function")
                            {
                                failureFn(respText);
                            }
                            TranzvisionMeikecityAdvanced.Boot.errorMessage(respText.error);
                        }
                        catch(e2)
                        {
                            if (typeof(failureFn) == "function")
                            {
                                failureFn(response.responseText);
                            }
                            if(response.responseText == undefined || response.responseText == "")
                            {
                                TranzvisionMeikecityAdvanced.Boot.errorMessage(boot.getMessage("TZGD_FWINIT_00027"));
                            }
                            else
                            {
                                TranzvisionMeikecityAdvanced.Boot.errorMessage(response.responseText);
                            }
                        }
                    },
                    callback: function(opts,success,response)
                    {
                        myMask.hide();
                    }
                });
        }
        catch(e1)
        {
            TranzvisionMeikecityAdvanced.Boot.errorMessage(boot.getMessage("TZGD_FWINIT_00027"));
            myMask.hide();
        }

    },
    //选中线索确认
    ensureClueProblem:function(btn) {
        var grid = btn.findParentByType("grid");
        var store = grid.store;
        var gridRecord = store.getRange();

        var editJson="";

        for(var i=0;i<gridRecord.length;i++) {
            var isChecked = gridRecord[i].data.isChecked;
            var suggestOperation = gridRecord[i].data.suggestOperation;
            if(isChecked==true) {
                if(editJson==""){
                    editJson=Ext.JSON.encode(gridRecord[i].data);
                }else{
                    editJson=editJson+','+Ext.JSON.encode(gridRecord[i].data);
                }
            }
        }

        if(editJson=="") {
            Ext.Msg.alert("提示","您没有选中任何记录");
            return;
        } else {
            var comParams = '"update":[' + editJson + "]";

            var tzParams = '{"ComID":"TZ_XSXS_ZSXS_COM","PageID":"TZ_XSXS_WTXS_STD","OperateType":"U",comParams:{'+comParams+'}}';
            Ext.tzSubmit(tzParams,function(responseData){
                for(var i=0;i<gridRecord.length;i++) {
                    var isChecked = gridRecord[i].data.isChecked;
                    if(isChecked==true) {
                        var record = gridRecord[i];
                        record.dirty=false;
                        record.commit();
                    }
                }
            },"线索确认成功",false,this);
        }


    },
    //关闭
    closeClueProblem: function (btn) {
        this.getView().close();
    },
    //选择责任人
    searchCharge:function(btn) {
        var me = this,
            view = me.getView();

        Ext.tzSetCompResourses("TZ_XSXS_INFO_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_ZRR_XZ_STD"];
        if(pageResSet==""||pageResSet==undefined) {
            Ext.MessageBox.alert('提示','您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet['jsClassName'];
        if(className==""||className==undefined) {
            Ext.MessageBox.alert('提示','未找到该功能页面对应的JS类，页面ID为：TZ_ZRR_XZ_STD，请检查配置。');
            return;
        }

        var win = me.lookupReference("personChooseWindow");
        if(!win) {
            Ext.syncRequire(className);
            var ViewClass = Ext.ClassManager.get(className);

            win = new ViewClass(view);

            view.add(win);
            win.show();
        }
    }
});
