Ext.define('KitchenSink.view.clueManagement.backgroundConfig.closeReason.defnReasonOfCloseController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.XSGLGBYYController',
    queryData:function(btn){
        /*var tzParams = '{"ComID":"TZ_GBYY_XSGL_COM","PageID":"TZ_GBYY_XSGL_STD","OperateType":"GS","comParams":{}}';
        Ext.tzLoad(tzParams,function(respData){*/
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_GBYY_XSGL_COM.TZ_GBYY_XSGL_STD.TZ_GBYY_XSGL_V',
            // condition:{"SETID":respData.setid||"SHARE"},
            condition:{"TZ_JG_ID": Ext.tzOrgID },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    //添加关闭原因
    addData:function(btn){
        var grid= btn.findParentByType("grid");
        this.showCloseReasonDetail(grid,"");
    },
    //编辑选中行的关闭原因
    editCheckedData:function(btn){
        var grid= btn.findParentByType("grid");
        var selList =  grid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要编辑的记录");
            return;
        }
        if (checkLen>=2) {
            Ext.Msg.alert("提示", "请您仅选择一条记录进行编辑");
            return;
        } else{
            //获取选中行的线索类别ID
            var rec=selList[0];
            var reasonID=rec.data.reasonID
            this.showCloseReasonDetail(grid,reasonID);
        }
    },
    //编辑当前行的关闭原因
    editCurrentData:function(btn,rowIndex){
        var grid= btn.findParentByType("grid"),
            rec = grid.getStore().getAt(rowIndex),
            reasonID=rec.data.reasonID;
        this.showCloseReasonDetail(grid,reasonID);
    },
    //删除当前行的关闭原因
    removeCurrentData:function(view,rowIndex){
        Ext.MessageBox.confirm('警告', '您确定要删除当前行的关闭原因数据吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.store;
                store.removeAt(rowIndex);
            }
        },this);
    },
  //删除选中的关闭原因
    deleteCheckedData:function(btn){
        //选中行
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        var resSetStore =  btn.findParentByType("grid").store;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    resSetStore.remove(selList);
                }

            },"",true,this);
        }
    },
    showCloseReasonDetail:function(grid,reasonID){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GBYY_XSGL_COM"]["TZ_GBYY_MODIF_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GBYY_MODIF_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
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
        cmp.reasonID=reasonID;
        cmp.on('afterrender',function(panel){
        	// panel.getForm().findField('reasonID').setValue('NEXT');
            // panel.getForm().findField('setID').setValue(respData.setid||"SHARE");
        	var form = panel.getForm();
        	var tzParams = '{"ComID":"TZ_GBYY_XSGL_COM","PageID":"TZ_GBYY_XSGL_STD",' +
    						'"OperateType":"QF","comParams":{"reasonID":"'+reasonID+'"}}';
            Ext.tzLoad(tzParams,function(respData){
            	var formData = respData.formData;
                if (formData.reasonID==""){
                    formData.reasonID = "NEXT";
                    formData.orgID = Ext.tzOrgID;
                    formData.status="Y";
                }
                /*if (formData.setID==""){
                    formData.setID = "SHARE";
                }*/
                form.setValues(formData);
            });
        });
        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    
    save:function(btn){
        var grid=btn.findParentByType('grid');
        this.savCloseReasonGrid(grid);
    },
    ensure:function(btn){
        var grid=btn.findParentByType('grid');
        this.savCloseReasonGrid(grid);
        btn.findParentByType('panel').close();
    },
    
    savCloseReasonGrid:function(grid,isClose){
        var store=grid.getStore();
        //修改了的grid记录;
        var editRecs = store.getModifiedRecords();
        //删除了的grid记录;
        var removeRecs = store.getRemovedRecords();
        var editJson="",removeJson="";
        for(var i=0;i<editRecs.length;i++){
            if(editJson == ""){
                editJson = Ext.JSON.encode(editRecs[i].data);
            }else{
                editJson = editJson + ','+Ext.JSON.encode(editRecs[i].data);
            }
        }
        for(var j=0;j<removeRecs.length;j++){
            if(removeJson == ""){
                removeJson = Ext.JSON.encode(removeRecs[j].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[j].data);
            }
        }

        //更新操作参数
        var comParams = "";

        comParams = '"update":[' +editJson  + "]"+ ',"delete":[' + removeJson + "]";
        var tzParams = '{"ComID":"TZ_GBYY_XSGL_COM","PageID":"TZ_GBYY_XSGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(responseData) {
        // var panel = btn.findParentByType('panel');
        // panel.close();
        },"",true,this);
    },
    
    onModifiedSave:function(btn){
        var form = btn.findParentByType("form").getForm();
        var formParams = form.getValues();
        if(form.isValid()) {
        	// 更新操作参数
            // var comParams = "";
            // comParams = '"update":[' + Ext.JSON.encode(formParams) + "]";
            // var tzParams = '{"ComID":"TZ_GBYY_XSGL_COM","PageID":"TZ_GBYY_XSGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
            var tzParams = '{"ComID":"TZ_GBYY_XSGL_COM","PageID":"TZ_GBYY_XSGL_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
            Ext.tzSubmit(tzParams,function(responseData){
                form.setValues(responseData);
                Ext.getCmp('tranzvision-framework-content-panel').down("grid[name =defnReasonOfCloseGrid]").store.reload();
            },"",true,this);
        }
    },
    onModifiedEnsurn:function(btn){
        var form = btn.findParentByType("form").getForm();
        var formParams = form.getValues();
        if(form.isValid()) {
        	// 更新操作参数
            // var comParams = "";
            // comParams = '"update":[' + Ext.JSON.encode(formParams) + "]";
            // var tzParams = '{"ComID":"TZ_GBYY_XSGL_COM","PageID":"TZ_GBYY_XSGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
            var tzParams = '{"ComID":"TZ_GBYY_XSGL_COM","PageID":"TZ_GBYY_XSGL_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
            Ext.tzSubmit(tzParams,function(responseData){
            Ext.getCmp('tranzvision-framework-content-panel').down("grid[name =defnReasonOfCloseGrid]").store.reload();
            btn.findParentByType('panel').close();
            },"",true,this);
	}
    },
    onGridPanelClose:function(btn){
        btn.findParentByType("panel").close();
    }
});