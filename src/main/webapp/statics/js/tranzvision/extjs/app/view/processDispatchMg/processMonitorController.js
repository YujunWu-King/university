Ext.define('KitchenSink.view.processDispatchMg.processMonitorController', {
    extend: 'Ext.app.ViewController',
    requires: ['Ext.ux.IFrame'],
    alias: 'controller.processMonitorCon',

    //可配置搜索
    cfgSearchAct: function (btn) {
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_JC_DISPATCH_COM.TZ_MONITOR_LIST.TZ_JC_MONITOR_VW',
            condition: {},
            callback: function (seachCfg) {
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    //放大镜搜索ComID
    pmtSearchCycleTmp: function(btn){
        var form = btn.findParentByType("window").child("form").getForm();
        Ext.tzShowPromptSearch({
            recname: 'TZ_XUNH_DEFN_T',
            searchDesc: '搜索循环信息',
            maxRow:20,
            condition:{
                presetFields:{

                },
                srhConFields:{
                    TZ_XH_MC:{
                        desc:'循环名称',
                        operator:'07',
                        type:'01'
                    },
                    TZ_XH_QZBDS:{
                        desc:'循环表达式',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_XH_MC: '循环名称',
                TZ_XH_QZBDS: '循环表达式'
            },
            multiselect: false,
            callback: function(selection){
                form.findField("cycleExpression").setValue(selection[0].data.TZ_XH_QZBDS);
                //form.findField("ComIDName").setValue(selection[0].data.TZ_COM_MC);
            }
        });
    },
    viewProcessMonitor: function () {

        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if (checkLen == 0) {
            Ext.Msg.alert("提示", "请选择一条要修改的记录");
            return;
        } else if (checkLen > 1) {
            Ext.Msg.alert("提示", "只能选择一条要修改的记录");
            return;
        }
        var processInstance = selList[0].get("processInstance");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_JC_DISPATCH_COM"]["TZ_INSTANCE_INFO"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_INSTANCE_INFO，请检查配置。');
            return;
        }
        var win = this.lookupReference('processRunInfoWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            var form = win.child('form').getForm();

            this.getView().add(win);
        }
        var tzParams = '{"ComID":"TZ_JC_DISPATCH_COM","PageID":"TZ_INSTANCE_INFO","OperateType":"QF","comParams":{"orgId":"'+ Ext.tzOrgID + '","processInstance":"' + processInstance + '"}}';
        // 加载数据
        Ext.tzLoad(tzParams, function (responseData) {
            var formData = responseData.formData;
            form.setValues(formData);
        });
        win.show();
    },

    deleteProcessInstance:function () {
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.MessageBox.alert("提示", "您没有选中任何记录");
            return;
        }else{
            Ext.MessageBox.confirm("确认", "您确定要删除所选记录吗?", function(btnId){
                if(btnId == 'yes'){
                    var tagStore = this.getView().store;
                    tagStore.remove(selList);
                }
            },this);
        }
    },

    viewMonitor:function (view,rowIndex) {

        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var processInstance = selRec.get("processInstance");

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_JC_DISPATCH_COM"]["TZ_INSTANCE_INFO"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_INSTANCE_INFO，请检查配置。');
            return;
        }
        var win = this.lookupReference('processRunInfoWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            var form = win.child('form').getForm();

            this.getView().add(win);
        }
        var tzParams = '{"ComID":"TZ_JC_DISPATCH_COM","PageID":"TZ_INSTANCE_INFO","OperateType":"QF","comParams":{"processInstance":"'+ processInstance + '","orgId":"' + Ext.tzOrgID + '"}}';
        // 加载数据
        Ext.tzLoad(tzParams, function (responseData) {
            var formData = responseData.formData;
            form.setValues(formData);
        });
        win.show();

    },
    startInstanceBL:function (view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var orgId = selRec.get("orgId");
        var processInstance = selRec.get("processInstance");
        var processServerName = selRec.get("processServerName");
        var tzParams = '{"ComID":"TZ_JC_DISPATCH_COM","PageID":"TZ_MONITOR_LIST","OperateType":"startProcess","comParams":{"orgId":"'+orgId+ '","processServerName":"' + processServerName +'","processInstance":"'+ processInstance +'"}}';
        Ext.tzLoad(tzParams,function(responseData){

        	if(responseData.status == "failed"){
        		
        		store.reload();
        		Ext.MessageBox.alert("提示", "启动失败，进程服务器未启动！");
        	}else if(responseData.status == "startfailed"){
        		
        		store.reload();
        		Ext.MessageBox.alert("提示", "启动失败，进程状态不处于可启动状态！");
        	}else if(responseData.status == "open"){
        		
        		store.reload();
        		Ext.MessageBox.alert("提示", "进程状态已处于启动状态,请不要重复开启！");
        	}else{
        		
        		store.reload();
        		Ext.MessageBox.alert("提示", "进程实例已启动！");
        	}
            
            
        });
    },
    stopInstanceBL:function (view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);

        var orgId = selRec.get("orgId");
        var processInstance = selRec.get("processInstance");
        var tzParams = '{"ComID":"TZ_JC_DISPATCH_COM","PageID":"TZ_MONITOR_LIST","OperateType":"stopProcess","comParams":{"orgId":"'+orgId+'","processInstance":"'+ processInstance +'"}}';
        Ext.tzLoad(tzParams,function(responseData){
        	
        	if(responseData.status == "failed"){
        		
        		store.reload();
                Ext.MessageBox.alert("提示", "关闭失败，进程状态不处于可关闭状态！")
        	}else if(responseData.status == "close"){
        		
        		store.reload();
                Ext.MessageBox.alert("提示", "进程状态已处于关闭状态,请不要重复关闭！")
        	}else{
        		
                store.reload();
                Ext.MessageBox.alert("提示", "进程实例已关闭！")
        	}

        });
    },
    deleteInstanceBL:function (view, rowIndex) {
        Ext.MessageBox.confirm("确认", "您确定要删除所选记录吗?", function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    saveProcessInstance:function (btn) {
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var removeJson = "";
        var removeRecs = store.getRemovedRecords();
        for(var i=0;i<removeRecs.length;i++){
            if(removeJson == ""){
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
            }
        }
        var comParams = "";
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
        }

        //提交参数
        var tzParams = '{"ComID":"TZ_JC_DISPATCH_COM","PageID":"TZ_MONITOR_LIST","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(responseData){
            
            if(responseData.status == "failed"){
            	
            	store.reload();
            	Ext.MessageBox.alert("提示", "STARTED、RUNNING、STOPPING三个状态不可以删除！")
            	
            }else{
            	
            }
        },"",true,this);
    },
    ensureProcessInstance:function (btn) {
        this.saveProcessInstance(btn);
        this.view.close();
    },
    onComRegClose: function (btn) {
        //关闭窗口
        this.getView().close();
    }
});
