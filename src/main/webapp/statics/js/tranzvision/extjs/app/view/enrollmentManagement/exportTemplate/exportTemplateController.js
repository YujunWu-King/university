Ext.define('KitchenSink.view.enrollmentManagement.exportTemplate.exportTemplateController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.exportTemplate',
    queryExportTemplate:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_BMGL_DCMB_COM.TZ_DCMB_LIST_STD.TZ_EXPORT_TMP_V',
            condition:{
                TZ_JG_ID:Ext.tzOrgID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    addTpl:function(btn){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_DCMB_COM"]["TZ_DCMB_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_DCMB_INFO_STD，请检查配置。');
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

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    editTpl:function(obj,rowIndex){
       var tplID,modalID;
       if(obj.name=="toolbarEdit"){
           var selList = this.getView().getSelectionModel().getSelection();
           var checkLen = selList.length;
           if(checkLen == 0){
               Ext.Msg.alert("提示","请选择一条要修改的记录");
               return;
           }else if(checkLen >1){
               Ext.Msg.alert("提示","只能选择一条要修改的记录");
               return;
           }
           tplID = selList[0].get("tplID");
           modalID=selList[0].get("modalID");
       }else{
            var store = obj.findParentByType("grid").store;
            var selRec = store.getAt(rowIndex);
            tplID = selRec.get("tplID");
            modalID = selRec.get("modalID");
        }

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_DCMB_COM"]["TZ_DCMB_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_DCMB_INFO_STD，请检查配置。');
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

        //操作类型设置为更新
        cmp.actType = "update";

        cmp.on('afterrender',function(panel){

            var form = panel.child('form').getForm();
            form.findField("tplID").setReadOnly(true);
            form.findField("tplID").addCls("lanage_1");
            var grid = panel.child('grid');
            //参数
            var tzParams = '{"ComID":"TZ_BMGL_DCMB_COM","PageID":"TZ_DCMB_INFO_STD","OperateType":"QF","comParams":{"tplID":"'+tplID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                var formData = responseData.formData;
                form.setValues(formData);
                var tzStoreParams = '{"tplID":"'+tplID+'","modalID":"'+modalID+'"}';
                grid.store.tzStoreParams = tzStoreParams;
                grid.store.load();
            });

        });
        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    selectAppModalID:function(trigger){
        Ext.tzShowPromptSearch({
            recname: 'TZ_APPTPL_DY_T',
            searchDesc: '选择报名表模板',
            maxRow:20,
            TZ_EFFEXP_ZT:"",
            condition:{
                presetFields:
                    {
                        TZ_JG_ID:{
                            value:Ext.tzOrgID,
                            type:'01'
                        },
                        TZ_EFFEXP_ZT:{
                            value:'Y',
                            type:'01'
                        }
                 },
                srhConFields:{
                    TZ_APP_TPL_ID:{
                        desc:'报名表模板ID',
                        operator:'07',
                        type:'01'
                    },
                    TZ_APP_TPL_MC:{
                        desc:'报名表模板名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_APP_TPL_ID: '报名表模板ID',
                TZ_APP_TPL_MC: '报名表模板名称'
            },
            multiselect: false,
            callback: function(selection){
                var modalID = selection[0].data.TZ_APP_TPL_ID;
                var modalName = selection[0].data.TZ_APP_TPL_MC;
                var form = trigger.findParentByType("form").getForm();
                form.findField("modalID").setValue(modalID);
                form.findField("modalName").setValue(modalName);
            }
        })
    },
    loadAppFormModalFields:function(btn){
        var actType = this.getView().actType;
        var form = this.getView().child('form').getForm();
        var modalID = form.findField("modalID").getValue();
        var exportTplID = form.findField("tplID").getValue();

        if(actType=="add"){
            Ext.Msg.alert("提示","请先保存导出模板信息再加载报名表模板字段。");
            return;
        }

        if(modalID==""||modalID==null){
            Ext.Msg.alert("提示","请先选择报名表模板ID再加载报名表模板字段。");
            form.findField("modalID").focus();
            return;
        }

        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        if(store.getCount( )>0){
            Ext.MessageBox.confirm('确认', '字段定义列表中已有数据，确定重新加载报名表模板字段吗？', function(btnId){
                if(btnId == 'yes'){
                    store.removeAll();
                    this.loadFieldsStore(exportTplID,modalID,store);
                }
            },this);
        }else{
            this.loadFieldsStore(exportTplID,modalID,store);
        }
		/*提交保存，保存页面数据
		
		if (form.isValid() && sava_flag) {
            var tzParams = this.getTplInfoParams2();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                comView.actType = "update";
                form.findField("tplID").setReadOnly(true);
                form.findField("tplID").addCls("lanage_1");
            },"",true,this);
        }else{
			alert("不保存");
		}*/
    },
   loadFieldsStore:function(exportTplID,modalID,store,btn){
       var tzParams = '{"ComID":"TZ_BMGL_DCMB_COM","PageID":"TZ_DCMB_INFO_STD","OperateType":"loadAppFormFields","comParams":{"modalID":"'+modalID+'","tplID":"'+exportTplID+'"}}';
       //加载数据
       Ext.tzLoad(tzParams,function(responseData){
           var fieldList = responseData.root;
           if(fieldList==null||fieldList.length==0){
               Ext.Msg.alert("提示","选择的报名表模板中没有字段。");
               return;
           }
           var seq=0;
           for(var i=0;i<fieldList.length;i++){
               seq+=10;
               var model = new KitchenSink.view.enrollmentManagement.exportTemplate.fieldModel({
                   tplID:exportTplID,
                   fieldID:fieldList[i].infoID,
                   fieldName:fieldList[i].infoName,
                   appClass:"",
                   fieldSeq:seq,
                   separator:'',
                   columnWidth:100,
                   filter:"string",
                   appFormField:[{
                       appFormFieldSeq:1,
                       appFormField:fieldList[i].infoID,
                       appFormFieldName:fieldList[i].infoName,
                       codeTable:"",
                       codeTableName:""
                   }]
               });
               store.add(model);
           }
       });
   },
    exportFieldSet:function(grid,rowIndex){
        var tplType = grid.findParentByType('exportTemplateInfo').child('form').getForm().findField('tplType').getValue();
        var rec = grid.getStore().getAt(rowIndex);
        var win = this.lookupReference('exportFieldSetWindow');
        if (!win) {
            //新建类
            win=new KitchenSink.view.enrollmentManagement.exportTemplate.exportFieldSetWindow();
            this.getView().add(win);
        }

        //加载数据
        var winForm =  win.child('form').getForm();
        var winGrid = win.child('grid');
        if(tplType!='1'){
            win.down('fieldset[name=fieldAttribute]').setHidden(true);
        }
        winForm.setValues({
            fieldName:rec.get("fieldName"),
            separator:rec.get("separator"),
            columnWidth:rec.get("columnWidth"),
            filter:rec.get("filter")
        });
        win.record=rec;
        win.modalID=this.getView().child('form').getForm().findField('modalID').getValue();
        var gridData = rec.get("appFormField");
        winGrid.store.loadData(gridData);
        win.show();
    },
    addField:function(btn){
        var actType = this.getView().actType;
        var form = this.getView().child('form').getForm();
        var modalID = form.findField("modalID").getValue();
        var tplID = form.findField("tplID").getValue();

        if(actType=="add"){
            Ext.Msg.alert("提示","请先保存导出模板信息再加载报名表模板字段。");
            return;
        }

        if(modalID==""||modalID.length<1){
            Ext.MessageBox.alert('提示', '您没有配置报名表模板编号，无法新增模板字段。');
            return;
        }
        Ext.tzShowPromptSearch({
            recname: 'TZ_FORM_FIELD_V',
            searchDesc: '新增模板字段',
            maxRow:20,
            TZ_EFFEXP_ZT:"",
            condition:{
                presetFields:
                {
                    TZ_APP_TPL_ID:{
                        value:modalID,
                        type:'01'
                    }
                },
                srhConFields:{
                    TZ_XXX_BH:{
                        desc:'信息项编号',
                        operator:'07',
                        type:'01'
                    },
                    TZ_XXX_MC:{
                        desc:'信息项名称',
                        operator:'07',
                        type:'01'
                    },
                    TZ_COM_LMC:{
                        desc:'控件类名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_XXX_BH: '信息项编号',
                TZ_XXX_MC: '信息项名称',
                TZ_COM_LMC:'控件类名称'
            },
            multiselect: true,
            callback: function(selection){
                var store = btn.findParentByType('grid').store;
                for(var i=0;i<selection.length;i++){
                    var tplField = selection[i].data.TZ_XXX_BH;
                    var tplFieldDesc = selection[i].data.TZ_XXX_MC;

                    if(store.find('fieldID',tplField,0,false,true,true)==-1){
                        var model = new KitchenSink.view.enrollmentManagement.exportTemplate.fieldModel({
                            tplID:tplID,
                            fieldID:tplField,
                            fieldName:tplFieldDesc,
                            appClass:"",
                            fieldSeq:0,
                            separator:'',
                            columnWidth:100,
                            filter:"string",
                            appFormField:[{
                                appFormFieldSeq:1,
                                appFormField:tplField,
                                appFormFieldName:tplFieldDesc,
                                codeTable:""
                            }]
                        });
                        store.add(model);
                    }
                }
            }
        })
    },
    addAppClass:function(btn){
        var actType = this.getView().actType;
        var form = this.getView().child('form').getForm();
        var modalID = form.findField("modalID").getValue();
        var tplID = form.findField("tplID").getValue();

        if(actType=="add"){
            Ext.Msg.alert("提示","请先保存导出模板信息再添加应用程序类。");
            return;
        }

        if(modalID==""||modalID.length<1){
            Ext.MessageBox.alert('提示', '您没有配置报名表模板编号，无法添加应用程序类。');
            return;
        }
        Ext.tzShowPromptSearch({
            recname: 'TZ_APPCLS_TBL',
            searchDesc: '添加应用程序类',
            maxRow:20,
            condition:{
                srhConFields:{
                	TZ_APPCLS_ID :{
                        desc:'应用程序类',
                        operator:'07',
                        type:'01'
                    },
                    TZ_DESCR100:{
                        desc:'描述',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
            	TZ_APPCLS_ID: '应用程序类',
            	TZ_DESCR100: '描述'
            },
            multiselect: true,
            callback: function(selection){
                var store = btn.findParentByType('grid').store;
                for(var i=0;i<selection.length;i++){
                	var appClass = selection[i].data.TZ_APPCLS_ID;
                    var tplField = "APPCLS_"+appClass;
                    var tplFieldDesc = selection[i].data.TZ_DESCR100;

                    if(store.find('fieldID',tplField,0,false,true,true)==-1){
                        var model = new KitchenSink.view.enrollmentManagement.exportTemplate.fieldModel({
                            tplID:tplID,
                            fieldID:tplField,
                            fieldName:tplFieldDesc,
                            appClass:appClass,
                            fieldSeq:0,
                            separator:'',
                            columnWidth:100,
                            filter:"string",
                            appFormField:[]
                        });
                        store.add(model);
                    }
                }
            }
        })
    },
    onTplInfoSave:function(btn){
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            var tzParams = this.getTplInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                comView.actType = "update";
                form.findField("tplID").setReadOnly(true);
                form.findField("tplID").addCls("lanage_1");
            },"",true,this);
        }
    },
    onTplInfoEnsure:function(btn){
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            var tzParams = this.getTplInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                //关闭窗口
                comView.close();
            },"",true,this);
        }
    },
	getTplInfoParams2: function(){
		/*该方法不处理删除的数据，供加载字段时使用，防止重新加载的字段在保存的时候又被删除*/
        var form = this.getView().child("form").getForm();
        //表单数据
        var formParams = form.getValues();

        //组件信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":[{"typeFlag":"TPL","data":'+Ext.JSON.encode(formParams)+'}]';
        }
        //修改json字符串
        var editJson = "";
        if(actType == "update"){
            editJson = '{"typeFlag":"TPL","data":'+Ext.JSON.encode(formParams)+'}';
        }

        //字段列表
        var grid = this.getView().child("grid");
        var store = grid.getStore();
        
        //grid修改json字符串
        var editRecs = store.getModifiedRecords();
        for(var i=0;i<editRecs.length;i++){
            if(editJson == ""){
                editJson = '{"typeFlag":"FIELD","data":'+Ext.JSON.encode(editRecs[i].data)+'}';
            }else{
                editJson = editJson + ','+'{"typeFlag":"FIELD","data":'+Ext.JSON.encode(editRecs[i].data)+'}';
            }
        }
        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_BMGL_DCMB_COM","PageID":"TZ_DCMB_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
        store.commitChanges();
        return tzParams;
    },
    getTplInfoParams: function(){
        var form = this.getView().child("form").getForm();
        //表单数据
        var formParams = form.getValues();
		
        //组件信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":[{"typeFlag":"TPL","data":'+Ext.JSON.encode(formParams)+'}]';
        }
        //修改json字符串
        var editJson = "";
        if(actType == "update"){
            editJson = '{"typeFlag":"TPL","data":'+Ext.JSON.encode(formParams)+'}';
        }

        //字段列表
        var grid = this.getView().child("grid");
        var store = grid.getStore();
        //删除json字符串
        var removeJson = "";
        //删除记录
        var removeRecs = store.getRemovedRecords();
		var removeRecsFiled = "";
		var updateRecsFiled = "";
		var deleteflag = true;
        for(var i=0;i<removeRecs.length;i++){
			deleteflag = true;
			removeRecsFiled = removeRecs[i].get("fieldID");
			/*是否是需要删除的字段*/
			var editRecs1 = store.getModifiedRecords();
			for(var m=0;m<editRecs1.length;m++){
				updateRecsFiled = editRecs1[m].get("fieldID");
				if(removeRecsFiled == updateRecsFiled){
					deleteflag = false;
					break;
				}
			}
			if(deleteflag){
				if(removeJson == ""){
					removeJson = Ext.JSON.encode(removeRecs[i].data);
				}else{
					removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
				}
			} 
        }
        if(removeJson != ""){
            if(comParams == ""){
                comParams = '"delete":[' + removeJson + "]";
            }else{
                comParams = comParams + ',"delete":[' + removeJson + "]";
            }
        };

        //grid修改json字符串
        var editRecs = store.getModifiedRecords();
        for(var i=0;i<editRecs.length;i++){
            if(editJson == ""){
                editJson = '{"typeFlag":"FIELD","data":'+Ext.JSON.encode(editRecs[i].data)+'}';
            }else{
                editJson = editJson + ','+'{"typeFlag":"FIELD","data":'+Ext.JSON.encode(editRecs[i].data)+'}';
            }
        }
        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_BMGL_DCMB_COM","PageID":"TZ_DCMB_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
        store.commitChanges();
        return tzParams;
    },
    onTplInfoClose:function(){
        this.getView().close();
    },
    removeField:function(grid, rowIndex){
        var store = grid.getStore();
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                store.removeAt(rowIndex);
            }
        },this);
    },
    addAppFormField:function(btn){
        var win  = btn.findParentByType('exportFieldSetWindow');
        var modalID= win.modalID;
        if(modalID==""||modalID.length<1){
            Ext.MessageBox.alert('提示', '您没有配置报名表模板编号，无法新增报名表模板字段。');
            return;
        }
        Ext.tzShowPromptSearch({
            recname: 'TZ_TEMP_FIELD_V',
            searchDesc: '选择报名表字段',
            maxRow:20,
            TZ_EFFEXP_ZT:"",
            condition:{
                presetFields:
                {
                    TZ_APP_TPL_ID:{
                        value:modalID,
                        type:'01'
                    }
                },
                srhConFields:{
                    TZ_XXX_BH:{
                        desc:'信息项编号',
                        operator:'07',
                        type:'01'
                    },
                    TZ_XXX_MC:{
                        desc:'信息项名称',
                        operator:'07',
                        type:'01'
                    },
                    TZ_COM_LMC:{
                        desc:'控件类名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_XXX_BH: '信息项编号',
                TZ_XXX_MC: '信息项名称',
                TZ_COM_LMC:'控件类名称'
            },
            multiselect: true,
            callback: function(selection){
                var store = btn.findParentByType('grid').store;
                for(var i=0;i<selection.length;i++){
                    var appFormField = selection[i].data.TZ_XXX_BH;
                    var appFormFieldName = selection[i].data.TZ_XXX_MC;

                    if(store.find('appFormField',appFormField)==-1){
                        store.add({
                            appFormField:appFormField,
                            appFormFieldName:appFormFieldName,
                            codeTable:"",
                            appClass:"",
                            codeTableName:"",
                            appFormFieldSeq:store.getCount()+1
                        })
                    }
                };

            }
        })
    },
    selectAppFormField:function(trigger){
        var modalID= trigger.findParentByType('exportFieldSetWindow').modalID;
        if(modalID==""||modalID.length<1){
            Ext.MessageBox.alert('提示', '您没有配置报名表模板编号，无法选择报名表模板字段。');
            return;
        }
        Ext.tzShowPromptSearch({
            recname: 'TZ_TEMP_FIELD_V',
            searchDesc: '选择报名表字段',
            maxRow:20,
            TZ_EFFEXP_ZT:"",
            condition:{
                presetFields:
                {
                    TZ_APP_TPL_ID:{
                        value:modalID,
                        type:'01'
                    }
                },
                srhConFields:{
                    TZ_XXX_BH:{
                        desc:'信息项编号',
                        operator:'07',
                        type:'01'
                    },
                    TZ_XXX_MC:{
                        desc:'信息项名称',
                        operator:'07',
                        type:'01'
                    },
                    TZ_COM_LMC:{
                        desc:'控件类名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_XXX_BH: '信息项编号',
                TZ_XXX_MC: '信息项名称',
                TZ_COM_LMC:'控件类名称'
            },
            multiselect: false,
            callback: function(selection){
                var grid = trigger.findParentByType('grid');
                var store = grid.store;
                var appFormField = selection[0].data.TZ_XXX_BH;
                var appFormFieldName = selection[0].data.TZ_XXX_MC;

                if(store.find('appFormField',appFormField,0,false,false,true)==-1){
                    var record = grid.getSelection()[0];
                    record.set("appFormField",appFormField);
                    record.set("appFormFieldName",appFormFieldName);
                }else{
                    Ext.Msg.alert("提示","信息项编号为“"+appFormField+"”的报名表字段值已经存在");
                    return false;
                }

            }
        })
    },
    selectAppFormCodeTable:function(trigger){
        Ext.tzShowPromptSearch({
            recname: 'TZ_PT_ZHZJH_TBL',
            searchDesc: '选择码表类别',
            maxRow:20,
            TZ_EFFEXP_ZT:"",
            condition:{
                presetFields:
                {},
                srhConFields:{
                    TZ_ZHZJH_ID:{
                        desc:'码表类别',
                        operator:'07',
                        type:'01'
                    },
                    TZ_ZHZJH_MS:{
                        desc:'码表描述',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_ZHZJH_ID: '码表类别',
                TZ_ZHZJH_MS: '码表描述'
            },
            multiselect: false,
            callback: function(selection){
                var grid = trigger.findParentByType('grid');
                var store = grid.store;
                var codeTable = selection[0].data.TZ_ZHZJH_ID;
                var codeTableName = selection[0].data.TZ_ZHZJH_MS;

                var record = grid.getSelection()[0];
                record.set("codeTable",codeTable);
                record.set("codeTableName",codeTableName);
            }
        })
    },
    deleteTpl:function(obj,rowIndex){
        //选中行
        var store = obj.findParentByType('grid').getStore();
        if(rowIndex.toString().match(/^\d+$/)){
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    store.removeAt(rowIndex);
                }
            },this);
        }else {
            var selList = obj.findParentByType('grid').getSelectionModel().getSelection();
            //选中行长度
            var checkLen = selList.length;
            if(checkLen == 0){
                Ext.Msg.alert("提示","请选择要删除的记录");
                return;
            }else{
                Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                    if(btnId == 'yes'){
                        store.remove(selList);
                    }
                },this);
            }

        }
    }
});

