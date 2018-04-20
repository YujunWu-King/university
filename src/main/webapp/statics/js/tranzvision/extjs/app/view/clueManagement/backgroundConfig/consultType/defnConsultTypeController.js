Ext.define('KitchenSink.view.clueManagement.backgroundConfig.consultType.defnConsultTypeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.defnConsultTypeController',
    queryData:function(btn){
        /*var tzParams = '{"ComID":"TZ_XSXS_ZXLB_COM","PageID":"TZ_XSXS_ZXLB_STD","OperateType":"GS","comParams":{}}';
        Ext.tzLoad(tzParams,function(respData){*/
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_XSXS_ZXLB_COM.TZ_XSXS_ZXLB_STD.TZ_XSXS_ZXLB_V',
            // condition:{"SETID":respData.setID },
            condition:{"TZ_JG_ID": Ext.tzOrgID },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    //添加线索类别
    addData:function(btn){
        var grid= btn.findParentByType("grid");
        this.showConsultTypeDetail(grid,"");
    },
    //编辑选中的行线索类别
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
            var consultSortID=rec.data.consultSortID;
            this.showConsultTypeDetail(grid,consultSortID);
        }
    },

    //编辑当前行的线索类别
    editCurrentData:function(btn,rowIndex){
        var grid= btn.findParentByType("grid"),
            rec = grid.getStore().getAt(rowIndex),
            consultSortID=rec.data.consultSortID;
        this.showConsultTypeDetail(grid,consultSortID);
    },
    //删除当前行的线索类别
    removeCurrentData:function(view,rowIndex){
        Ext.MessageBox.confirm('警告', '您确定要删除当前行的线索类别数据吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    //删除选中的所有线索类别
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
    showConsultTypeDetail:function(grid,consultSortID){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_ZXLB_COM"]["TZ_XSXS_ZXLB_D_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_XSXS_ZXLB_D_STD，请检查配置。');
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
        cmp.consultSortID=consultSortID;

        cmp.on('afterrender',function(panel){
            var form = panel.getForm();
            var tzParams = '{"ComID":"TZ_XSXS_ZXLB_COM","PageID":"TZ_XSXS_ZXLB_D_STD",' +
                '"OperateType":"QF","comParams":{"consultSortID":"'+consultSortID+'"}}';
            Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                if (formData.consultSortID==""){
                    formData.consultSortID = "NEXT";
                    formData.orgID = Ext.tzOrgID;
                    formData.consultSortStatus="Y";
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
    consultSortSave:function(btn){
        var form=btn.findParentByType('form').getForm();
        var formParams = form.getValues();

		if(form.isValid()) {
        	//更新操作参数
        	var comParams = "";
        	comParams = '"update":[' + Ext.JSON.encode(formParams) + "]";
        	var tzParams = '{"ComID":"TZ_XSXS_ZXLB_COM","PageID":"TZ_XSXS_ZXLB_D_STD","OperateType":"U","comParams":{'+comParams+'}}';
        	Ext.tzSubmit(tzParams,function(responseData) {
            	form.setValues(responseData);
            	Ext.getCmp('tranzvision-framework-content-panel').down("grid[name=consultType]").store.reload();
        	},"",true,this);
		}
    },
    save:function(btn){
        var grid=btn.findParentByType('grid');
        this.saveConsultTypeGrid(grid);
    },
    ensure:function(btn){
        var grid=btn.findParentByType('grid');
        this.saveConsultTypeGrid(grid);
        btn.findParentByType('panel').close();
    },
    saveConsultTypeGrid:function(grid){


        var   store=grid.getStore();
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
        var tzParams = '{"ComID":"TZ_XSXS_ZXLB_COM","PageID":"TZ_XSXS_ZXLB_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(responseData) {
            Ext.getCmp('tranzvision-framework-content-panel').down("grid[name=consultType]").store.reload();
        },"",true,this);


    },
    consultSortEnsure:function(btn){
        var form=btn.findParentByType('form').getForm();
        var formParams = form.getValues();

		if(form.isValid()) {
        	//更新操作参数
        	var comParams = "";
        	comParams = '"update":[' + Ext.JSON.encode(formParams) + "]";
        	var tzParams = '{"ComID":"TZ_XSXS_ZXLB_COM","PageID":"TZ_XSXS_ZXLB_D_STD","OperateType":"U","comParams":{'+comParams+'}}';
        	Ext.tzSubmit(tzParams,function(responseData) {
            	var panel = btn.findParentByType('panel');
            	panel.close();
                Ext.getCmp('tranzvision-framework-content-panel').down("grid[name=consultType]").store.reload();
        	},"",true,this);
		}
    }
});

