/**
 * 功能：站内信定义controller
 * 
 */
Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.znxDetController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.znxDetController',
    /*添加考生*/
    addStruData: function(btn){
		var className='KitchenSink.view.bulkEmailAndSMS.searchStu.searchStuWin';
		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}
		ViewClass = Ext.ClassManager.get(className);
		var config = {
			taskType:"MAL"	
			}
		var win = new ViewClass(config);
		this.getView().add(win);
		win.show();	
	},
	/*搜索考生*/
	searchStuList: function(btn){
		/*
		var stuWin = btn.findParentByType("searchStuWin");
		var stuGrid = stuWin.child("grid");
		var searchContent = stuGrid.down("textfield[reference=searchStuContent]").getValue();
		var stuGridStore = stuGrid.getStore();
		stuGridStore.tzStoreParams = Ext.JSON.encode({"queryID": "searchStu","taskType":"MAL","searchText":searchContent});
		stuGridStore.reload();
		*/
		Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_EMLSMS_STU_COM.TZ_EMLSMS_STU_STD.TZ_QFKSXX_VW',
			condition:
            {
                "TZ_JG_ID": Ext.tzOrgID
				/*,"SETID":Ext.tzSetID*/
            },    
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
	},
    selectStu: function(btn){
    	var arrAddAudience=[];
        var addAudirec;
    	var stuWin = btn.findParentByType("searchStuWin");
		var stuGrid = stuWin.child("grid");
		var selList = stuGrid.getSelectionModel().getSelection();
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择考生！");   
			return;
		}
		var znxBulkDetForm = btn.findParentByType('znxBulkDet').child('form');
		var receverField = znxBulkDetForm.child('tagfield[reference="recever"]');
		var arrAddData = [];
		
		for(var i=0; i<selList.length; i++){
			addAudirec = {"id":selList[i].data.oprId,"desc":selList[i].data.oprName};
            arrAddAudience.push(addAudirec);
			var oprId = selList[i].data.oprId;
			arrAddData.push(oprId);
		}
         
		if(arrAddData.length>0){
			var storereceive = receverField.getStore();
			storereceive.add(arrAddAudience);
			receverField.addValue(arrAddData);
		}
		this.onWinClose(btn);
	},
	onWinClose:function(btn){
        btn.findParentByType("window").close();
    },
    /**
     * 功能：添加听众
     * 刘阳阳  2016-01-05
     */
    addAudience:function(btn){
        var arrAddAudience=[];
        var addAudirec;
        var arrAddAudiValue=[];
        Ext.tzShowPromptSearch({
            recname: 'TZ_AUDIENCE_VW',
            searchDesc: '选择听众',
            maxRow:50,
            condition:{
                presetFields:{
                	TZ_JG_ID:{
                        value: Ext.tzOrgID,
                        type: '01'
                    }
                },
                srhConFields:{
                	TZ_AUD_NAM:{
                        desc:'听众名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_AUD_ID:'听众ID',
                TZ_AUD_NAM: '听众名称',
                //TZ_ORG_CODE:'所属部门',
                ROW_ADDED_DTTM:'创建时间'
                // ROW_LASTMANT_DTTM:'修改时间'
            },
            multiselect: true,
            callback: function(selection){
                if(selection.length>0){
                    for(j=0;j<selection.length;j++){
                        addAudirec="";
                        addAudirec = {"id":selection[j].data.TZ_AUD_ID,"desc":selection[j].data.TZ_AUD_NAM};
                        arrAddAudience.push(addAudirec);
                        arrAddAudiValue.push(selection[j].data.TZ_AUD_ID);
                    };
                    var znxBulkDetForm = btn.findParentByType('znxBulkDet').child('form');
                    var storereceive=znxBulkDetForm.child('tagfield[reference="recever"]').getStore();
                    storereceive.add(arrAddAudience);
                    //znxBulkDetForm.down('tagfield[reference="recever"]').removeListener('change','receverChange');
                    znxBulkDetForm.child('tagfield[reference="recever"]').addValue(arrAddAudiValue);
                    //znxBulkDetForm.down('tagfield[reference="recever"]').addListener('change','receverChange');

                    znxBulkDetForm.down('button[reference=clearAllBtn]').disabled=false;
                    znxBulkDetForm.down('button[reference=clearAllBtn]').removeCls('x-item-disabled x-btn-disabled')
                }
            }
        })
    },
    /**
     * 功能：清除所有
     * 刘阳阳 2016-01-05
     */
    clearAll:function(btn){
        var emlBkDetForm = btn.findParentByType('form');
        
        //emlBkDetForm.child('tagfield[reference=recever]').store.removeAll(true);
        emlBkDetForm.child('tagfield[reference=recever]').setValue("");
    },
    /**
     * 功能：设置邮件模版
     * 刘阳阳  2016-01-07
     */
    setZnxTmpl:function(btn){
        Ext.tzSetCompResourses("TZ_EML_TMPL_MG_COM");

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EML_TMPL_MG_COM"]["TZ_EML_TMPL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_EML_TMPL_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

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
        //操作类型设置为更新
        cmp.actType = "update";

        var znxTmpId = btn.findParentByType('form').down('combobox[reference=znxTmpId]').getValue();

        cmp.on('afterrender',function(panel){
            //组件注册表单信息;
            var form = panel.child('form').getForm();
            form.findField("emltempid").setReadOnly(true);
            form.findField("emltemporg").setReadOnly(true);
            form.findField("emltempid").addCls("lanage_1");
            form.findField("emltemporg").addCls("lanage_1");

            //加载邮件模版信息
            var tzParams = '{"ComID":"TZ_ZNX_GL_COM","PageID":"TZ_ZNX_DET_STD","OperateType":"getEmlMetaTmpInfo","comParams":{"znxTmpId":"'+znxTmpId+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                var emltemporg=responseData['emltemporg'];
                var metaempid=responseData['metaempid'];
                tzParams = '{"ComID":"TZ_EML_TMPL_MG_COM","PageID":"TZ_EML_TMPL_STD","OperateType":"QF","comParams":{"emltempid":"'+znxTmpId+'","emltemporg":"'+emltemporg+'"}}';
                Ext.tzLoad(tzParams,function(responseData){
                    form.setValues(responseData);
                    //信息项数据
                    var	znxTmplItemGrid = panel.down('grid[name=znxTmplItemGrid]');
                    var tzStoreParamsItem = "{'restempid':'"+metaempid+"'}";
                    znxTmplItemGrid.store.tzStoreParams = tzStoreParamsItem;
                    znxTmplItemGrid.store.load();
                    panel.commitChanges(panel);
                });
            });
        });

        tab = contentPanel.add(cmp);

        tab.on(Ext.tzTabOn(tab,this.getView(),cmp));

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    /**
     * 功能：插入信息项
     * 刘阳阳 2016-01-05
     */
    insertemlitem: function(grid,rowIndex, colIndex){
        var emlbkdefForm = grid.findParentByType('form');
        var rec = grid.getStore().getAt(rowIndex);
        var parainfoitem = rec.get('parainfoitem');

        var form = this.getView().child("form").getForm();
        //var objEmlContentHtml = form.findField("znxCont");
        var dom = form.findField("znxCont").getEl();
        var editorId = dom.id + "-ueditor";
        UE.getEditor(editorId).execCommand("inserthtml",parainfoitem);

        var znxCont = emlbkdefForm.down('ueditor[name=znxCont]').value;
        emlbkdefForm.down('ueditor[name=znxCont]').setValue(znxCont);
    },
    /**
     * 功能：上传附件
     * 刘阳阳  2016-01-07
     */
    addAttachment : function(file, value, attachmentType){
        attachmentType = 'ATTACHMENT';
        var form = file.findParentByType("form").getForm();
        if(value != ""){
            //如果是附件则存在在附件的url中，如果是图片在存放在图片的url中;
            var dateStr = Ext.Date.format(new Date(), 'Ymd');
            var upUrl = "";

            if(attachmentType=="ATTACHMENT"){
                //upUrl = "/linkfile/FileUpLoad/attachment/EmailAndSMSBulk/";
            	upUrl = "EmailAndSMSBulk";
                if(upUrl==""){
                    Ext.Msg.alert("错误","未定义上传附件的路径，请与管理员联系");
                    return;
                }else{
                	upUrl = TzUniversityContextPath + '/UpdServlet?filePath=' + upUrl;
                	/*
                    if(upUrl.length == (upUrl.lastIndexOf("/")+1)){
                        upUrl = '/UpdServlet?filePath='+upUrl+dateStr;
                    }else{
                        upUrl = '/UpdServlet?filePath='+upUrl+"/"+dateStr;
                    }
                    */
                }
            }
            var myMask = new Ext.LoadMask({
                msg    : '加载中...',
                target : Ext.getCmp('tranzvision-framework-content-panel')
            });

            myMask.show();

            form.submit({
                //url: '/UpdServlet?filePath=/linkfile/FileUpLoad/imagesWall',
                url: upUrl,
                waitMsg: '正在上传，请耐心等待....',
                success: function (form, action) {
                    var tzParams;
                    var picViewCom;

                    var accessPath = action.result.msg.accessPath;
                    //var path = action.result.msg.path;
                    var path = action.result.msg.accessPath;
                    
                    if(path.length == (path.lastIndexOf("/")+1)){
                        path = path + action.result.msg.sysFileName;
                    }else{
                        path = path + "/" + action.result.msg.sysFileName;
                    }
                    if(accessPath.length == (accessPath.lastIndexOf("/")+1)){
                        accessPath = TzUniversityContextPath + accessPath + action.result.msg.sysFileName;
                    }else{
                        accessPath = TzUniversityContextPath + accessPath + "/" + action.result.msg.sysFileName;
                    }

                    if(attachmentType=="ATTACHMENT"){
                        //var applyItemGrid = this.lookupReference('attachmentGrid');
                        var applyItemGrid = file.findParentByType("grid[reference=znxInfoItemGrid]");
                        var emlBBkDetFormRec = applyItemGrid.findParentByType("form[reference=znxDetForm]").getForm().getFieldValues();
                        var r = Ext.create('KitchenSink.view.bulkEmailAndSMS.znx.znxAttaModel', {
                            "znxQfId":emlBBkDetFormRec['znxQfId'] ,
                            "attaID": 'NEXT',
                            "attaName": action.result.msg.filename,
                            "path":path,
                            "attaUrl": accessPath
                        });
                        applyItemGrid.store.insert(0,r);
                    }
                    //重置表单
                    myMask.hide();
                    form.reset();
                },
                failure: function (form, action) {
                    myMask.hide();
                    Ext.MessageBox.alert("错误", action.result.msg);
                }
            });
        }
    },
    /**
     * 功能：删除附件
     * 刘阳阳  2016-01-07
     */
    deleteArtAttenments: function(btn,rowIndex){
        //选中行
        var store = btn.findParentByType("grid[reference=znxInfoItemGrid]").getStore();
        var selList = btn.findParentByType("grid[reference=znxInfoItemGrid]").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(rowIndex.toString().match(/^\d+$/)){
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    store.removeAt(rowIndex);
                }
            },this);
        }else {
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
    },
    /**
     * 功能：保存
     * 刘阳阳  2016-01-07
     */
    onPanelSave:function(btn){
        var emlbkdefPanel = btn.up('panel');
        var emlbkdefForm = emlbkdefPanel.child('form');
		/*表单验证*/
		if(!emlbkdefForm.getForm().isValid()) return;
        var formdata =Ext.JSON.encode(emlbkdefForm.getValues());
        var formrec = emlbkdefForm.getForm().getFieldValues();
        var msgInfo="";
		if(formrec["znxQfDesc"]==""||formrec["znxQfDesc"]==null){
            Ext.Msg.alert("提示","请填写群发任务名称.");
            return;
        }
        if(!emlbkdefForm.child('tagfield[reference="recever"]').value||emlbkdefForm.child('tagfield[reference="recever"]').value==""){
            Ext.Msg.alert("提示","收件人不能为空.");
            return;
        };
        if(formrec["znxSubj"]==""){
            Ext.Msg.alert("提示","邮件主题不能为空.");
            return;
        }

        var othInfo = '{"crePer":"'+formrec["crePer"]+'","dept":"'+formrec["dept"]+'","setId":"'+formrec["setId"]+'","recever":"'+emlbkdefForm.child('tagfield[reference="recever"]').value+'"}';
        var attaGrid,
            attaAllRecs,attaDelRecs,attaNewAtta,
            attaList="",attaDelLit="",attaNewList="",
            comParams="";
        attaGrid =emlbkdefForm.down("grid[reference=znxInfoItemGrid]");
        if (formrec['znxQfId']=="NEXT"){
            attaAllRecs = attaGrid.store.getRange();
            for(var i=0;i<attaAllRecs.length;i++){
                if(attaList == ""){
                    attaList = Ext.JSON.encode(attaAllRecs[i].data);
                }else{
                    attaList = attaList + ','+Ext.JSON.encode(attaAllRecs[i].data);
                }
            };

            comParams = '"formdata":'+formdata+',"othInfo":'+othInfo+',"attaList":['+attaList+']';
        }else{
            attaDelRecs=attaGrid.store.getRemovedRecords();
            attaNewAtta=attaGrid.store.getNewRecords();
            for(var i=0;i<attaDelRecs.length;i++){
                if(attaDelLit == ""){
                    attaDelLit = Ext.JSON.encode(attaDelRecs[i].data);
                }else{
                    attaDelLit = attaDelLit + ','+Ext.JSON.encode(attaDelRecs[i].data);
                }
            };
            for(var i=0;i<attaNewAtta.length;i++){
                if(attaNewList == ""){
                    attaNewList = Ext.JSON.encode(attaNewAtta[i].data);
                }else{
                    attaNewList = attaNewList + ','+Ext.JSON.encode(attaNewAtta[i].data);
                }
            };

            comParams = '"formdata":'+formdata+',"othInfo":'+othInfo+',"attaDelLit":['+attaDelLit+'],"attaNewList":['+attaNewList+']';
        }

        var tzParams = '{"ComID":"TZ_ZNX_GL_COM","PageID":"TZ_ZNX_DET_STD","OperateType":"save","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(responseData){
            emlbkdefForm.getForm().setValues(responseData);
            //emlbkdefForm.down('displayfield[name=creDt]').setVisible(true);

            var par = '{"znxQfId": "'+responseData['znxQfId']+'","queryID": "atta"}';
            attaGrid.store.tzStoreParams=par;
            attaGrid.store.load();
        },msgInfo,true,this);

        var arrEmlBkMgrPanel=Ext.ComponentQuery.query("znxMgr");
        for(var i=0;i<arrEmlBkMgrPanel.length;i++){
            arrEmlBkMgrPanel[i].down('grid').store.load();
        }
    },
    /**
     * 功能：发送
     * 刘阳阳  2016-01-12
     */
    sendZnx:function(btn){
		var me = this;
		var emlbkdefPanel = btn.up('panel');
        var emlbkdefForm = emlbkdefPanel.child('form');
		/*表单验证*/
		if(!emlbkdefForm.getForm().isValid()) return;
        Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.ensure","确认"),
            Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_DET_STD.ensureSendDesc","确认要发送站内信?"), function(btnId) {
                if (btnId == 'yes') {
                    var formdata = Ext.JSON.encode(emlbkdefForm.getValues());
                    var formrec = emlbkdefForm.getForm().getFieldValues();
                    var msgInfo = "";
                    /*
                    if(!emlbkdefForm.child('tagfield[reference="recever"]').value||emlbkdefForm.child('tagfield[reference="recever"]').value==""){
                        Ext.Msg.alert("提示","收件人不能为空.");
                        return;
                    };
                    */
                    
                    if(formrec["znxQfDesc"]==""||formrec["znxQfDesc"]==null){
                        Ext.Msg.alert("提示","请填写群发任务名称.");
                        return;
                    }
                    if(!emlbkdefForm.child('tagfield[reference="recever"]').value||emlbkdefForm.child('tagfield[reference="recever"]').value==""){
                        Ext.Msg.alert("提示","收件人不能为空.");
                        return;
                    };
                    if(formrec["znxSubj"]==""){
                        Ext.Msg.alert("提示","邮件主题不能为空.");
                        return;
                    }
                    
                    msgInfo='群发任务已提交.';
                    
                    var othInfo = '{"crePer":"' + formrec["crePer"] + '","dept":"' + formrec["dept"] + '","setId":"' + formrec["setId"] + '","recever":"' + emlbkdefForm.child('tagfield[reference="recever"]').value + '"}';
                    //alert(formrec['znxQfId']);
                    var attaGrid,
                        attaAllRecs, attaDelRecs, attaNewAtta,
                        attaList = "", attaDelLit = "", attaNewList = "",
                        comParams = "";
                    attaGrid = emlbkdefForm.down("grid[reference=znxInfoItemGrid]");
                    if (formrec['znxQfId'] == "NEXT") {
                        attaAllRecs = attaGrid.store.getRange();
                        for (var i = 0; i < attaAllRecs.length; i++) {
                            if (attaList == "") {
                                attaList = Ext.JSON.encode(attaAllRecs[i].data);
                            } else {
                                attaList = attaList + ',' + Ext.JSON.encode(attaAllRecs[i].data);
                            }
                        }
                        ;

                        comParams = '"formdata":' + formdata + ',"othInfo":' + othInfo + ',"attaList":[' + attaList + ']';
                    } else {
                        attaDelRecs = attaGrid.store.getRemovedRecords();
                        attaNewAtta = attaGrid.store.getNewRecords();
                        for (var i = 0; i < attaDelRecs.length; i++) {
                            if (attaDelLit == "") {
                                attaDelLit = Ext.JSON.encode(attaDelRecs[i].data);
                            } else {
                                attaDelLit = attaDelLit + ',' + Ext.JSON.encode(attaDelRecs[i].data);
                            }
                        }

                        for (var i = 0; i < attaNewAtta.length; i++) {
                            if (attaNewList == "") {
                                attaNewList = Ext.JSON.encode(attaNewAtta[i].data);
                            } else {
                                attaNewList = attaNewList + ',' + Ext.JSON.encode(attaNewAtta[i].data);
                            }
                        }

                        comParams = '"formdata":' + formdata + ',"othInfo":' + othInfo + ',"attaDelLit":[' + attaDelLit + '],"attaNewList":[' + attaNewList + ']';
                    }

                    var tzParams = '{"ComID":"TZ_ZNX_GL_COM","PageID":"TZ_ZNX_DET_STD","OperateType":"sendZnx","comParams":{' + comParams + '}}';
                    Ext.tzSubmit(tzParams, function (responseData) {
                        emlbkdefForm.getForm().setValues(responseData);
                        //emlbkdefForm.down('displayfield[name=creDt]').setVisible(true);

                      
                        if (responseData['rwzxZt']==""||responseData['rwzxZt']=="D"||responseData['rwzxZt']=="E") {

                        } else {
                            emlbkdefPanel.down('button[reference=saveBtn]').setDisabled(true);
                            emlbkdefPanel.down('button[reference=sendBtn]').setDisabled(true);
							emlbkdefPanel.down('button[reference=revokeBtn]').setVisible(true);
                            emlbkdefPanel.getController().pageReadonly(emlbkdefForm);
                        }
                        
                        
                        var par = '{"znxQfId": "' + responseData['znxQfId'] + '","queryID": "atta"}';
                        attaGrid.store.tzStoreParams = par;
                        attaGrid.store.load();
						
                        /*
						if(!emlbkdefForm.down('button[reference=copyHistoryBtn]').hidden){
							
							emlbkdefForm.down('button[reference=copyHistoryBtn]').setHidden(true);
						}*/
						
						/*重置表单修改状态*/
						var savedObject = me && me.getView && (typeof me.getView === "function") && me.getView();
						savedObject = savedObject || me;
						if(savedObject && savedObject.commitChanges && (typeof savedObject.commitChanges === "function")){
							savedObject.commitChanges(savedObject);
						}
                    }, msgInfo, true, this);

                    var arrEmlBkMgrPanel=Ext.ComponentQuery.query("emailBulkMgr");
                    for(var i=0;i<arrEmlBkMgrPanel.length;i++){
                        arrEmlBkMgrPanel[i].down('grid').store.load();
                    }
                }
            }
        );

    },
    /**
     * 功能：中断发送
     * 刘阳阳 2015-01-12
     */
    interSend:function(btn){
        var emlbkdefPanel = btn.up('panel');
        var emlbkdefForm = emlbkdefPanel.child('form');
        var formrec = emlbkdefForm.getForm().getFieldValues();
        var comParams = '"znxQfId":'+formrec['znxQfId']+'';
        var tzParams = '{"ComID":"TZ_ZNX_GL_COM","PageID":"TZ_ZNX_DET_STD","OperateType":"revoke","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(responseData){
            //emlbkdefForm.setDisabled(false);
            emlbkdefPanel.getController().pageFiledsDisControl(emlbkdefForm);
            emlbkdefPanel.down('button[reference=revokeBtn]').setVisible(false);
            emlbkdefPanel.down('button[reference=saveBtn]').setDisabled(false);
            emlbkdefPanel.down('button[reference=sendBtn]').setDisabled(false);
        },"",true,this);
    },
    /**
     * 功能：查看发送历史
     */
    viewSendHistory:function(btn){
        var form = btn.findParentByType("panel").child("form");
        var znxQfId=form.getForm().findField("znxQfId").getValue();//群发任务ID

        Ext.tzSetCompResourses("TZ_ZNXQ_VIEWTY_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZNXQ_VIEWTY_COM"]["TZ_ZNXQ_VIEWTY_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZNXQ_VIEWTY_STD，请检查配置。');
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

        cmp.on('afterrender',function(panel){

            var store=panel.getStore();
            var tzStoreParams ='{"storeType":"history","znxQfId":"'+znxQfId+'"}';
            store.tzStoreParams = tzStoreParams;
            store.load({
            });

        });
        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }

    },
    /**
     * 功能：关闭
     * 刘阳阳  2015-12-31
     */
    onPanelClose: function(btn){
        var win = btn.findParentByType("panel");
        win.close();
    },
    /**
     * 功能：页面只读
     */
    pageReadonly:function(form){
        form.down('textfield[name=znxQfDesc]').setReadOnly(true);
        form.down('textfield[name=znxQfDesc]').addCls('readOnly-combox-BackgroundColor');
        form.down('tagfield[reference=recever]').setEditable(false);
        form.down('tagfield[reference=recever]').disabled=true;
        form.down('tagfield[reference=recever]').addCls('readOnly-tagfield-BackgroundColor');
        form.down('toolbar').child('button[reference=addAudienceBtn]').disabled=true;
        form.down('toolbar').child('button[reference=addAudienceBtn]').addCls('x-item-disabled x-btn-disabled');
		
		form.down('toolbar').child('button[reference=selectStuBtn]').disabled=true;
		form.down('toolbar').child('button[reference=selectStuBtn]').addCls('x-item-disabled x-btn-disabled');
						
        form.down('toolbar').child('button[reference=clearAllBtn]').disabled=true;
        form.down('toolbar').child('button[reference=clearAllBtn]').addCls('x-item-disabled x-btn-disabled');

        form.down('combobox[reference=znxTmpId]').disabled=true;
        form.down('combobox[reference=znxTmpId]').addCls('readOnly-combox-BackgroundColor');
        if(form.down('combobox[reference=znxTmpId]').getValue()!=""){
            form.down('button[reference=setZnxTmpl]').disabled=true;
            form.down('button[reference=setZnxTmpl]').addCls('disabled-button-color');
        };
        form.down('textfield[name=znxSubj]').setReadOnly(true);
        form.down('textfield[name=znxSubj]').addCls('readOnly-combox-BackgroundColor');
        //form.down('ueditor[name=znxCont]').setReadOnly(true);
        form.down('ueditor[name=znxCont]').addCls('readOnly-tagfield-BackgroundColor');
        form.down('grid[name=znxTmplItemGrid]').setDisabled(true);
        form.down('grid[name=znxInfoItemGrid]').setDisabled(true);
    },
    /**
     * 功能：控制页面字段
     */
    pageFiledsDisControl:function(form){
        form.down('textfield[name=znxQfDesc]').setReadOnly(false);
        form.down('textfield[name=znxQfDesc]').removeCls('readOnly-combox-BackgroundColor');
        //form.down('combobox[name=sender]').setReadOnly(false);
        //form.down('combobox[name=sender]').removeCls('readOnly-combox-BackgroundColor');
        //form.down('radio[reference=sendModelNor]').setReadOnly(false);
        //form.down('radio[reference=sendModelExc]').setReadOnly(false);
        //if(form.down('radio[reference=sendModelExc]').checked){
        //    form.down('button[reference=impExc]').disabled=false;
        //    form.down('button[reference=impExc]').removeCls('x-item-disabled x-btn-disabled');
        //};
        /*
        if (!form.down('radio[reference=sendModelExc]').checked){
			//console.log(form.down('toolbar').child('button[reference=pasteFromExcelBtn]').disabled);
            form.down('toolbar').child('button[reference=addAudienceBtn]').disabled=false;
            form.down('toolbar').child('button[reference=addAudienceBtn]').removeCls('x-item-disabled x-btn-disabled');
			
            if(form.down('combobox[reference=znxTmpId]').getValue()==""||form.down('combobox[reference=znxTmpId]').getValue()==null){
                form.down('tagfield[reference=recever]').setEditable(true);
                form.down('tagfield[reference=recever]').disabled=false;
                form.down('tagfield[reference=recever]').removeCls('readOnly-tagfield-BackgroundColor');
                form.down('toolbar').child('button[reference=pasteFromExcelBtn]').disabled=false;
				
				form.down('toolbar').child('button[reference=selectStuBtn]').disabled=false;
            	form.down('toolbar').child('button[reference=selectStuBtn]').removeCls('x-item-disabled x-btn-disabled');
            }
        }
        */
        if(form.down('tagfield[reference=recever]').getValue!=""){
            form.down('toolbar').child('button[reference=clearAllBtn]').disabled=false;
            form.down('toolbar').child('button[reference=clearAllBtn]').removeCls('x-item-disabled x-btn-disabled');
        }
        /*
        form.down('checkbox[name=tsfsFlag]').setReadOnly(false);
        form.down('textfield[name=tsfsEmail]').setReadOnly(false);
        form.down('textfield[name=tsfsEmail]').removeCls('readOnly-combox-BackgroundColor');
        form.down('tagfield[reference=mailCC]').setEditable(true);
        form.down('tagfield[reference=mailCC]').disabled=false;
        form.down('tagfield[reference=mailCC]').removeCls('readOnly-tagfield-BackgroundColor');
        if (!form.down('radio[reference=sendModelExc]').checked&&form.down('tagfield[reference=recever]').getValue=="") {
            form.down('combobox[reference=znxTmpId]').disabled = false;
            form.down('combobox[reference=znxTmpId]').removeCls('readOnly-combox-BackgroundColor');
        }
        */
        if(form.down('combobox[reference=znxTmpId]').getValue()=="" || form.down('combobox[reference=znxTmpId]').getValue()== null){
			form.down('button[reference=setZnxTmpl]').disabled=true;
			form.down('button[reference=setZnxTmpl]').addCls('disabled-button-color');
        }else{
			form.down('button[reference=setZnxTmpl]').disabled=false;
            form.down('button[reference=setZnxTmpl]').removeCls('disabled-button-color');
		}
        form.down('textfield[name=znxSubj]').setReadOnly(false);
        form.down('textfield[name=znxSubj]').removeCls('readOnly-combox-BackgroundColor');
        //form.down('ueditor[name=znxCont]').setReadOnly(false);
        form.down('ueditor[name=znxCont]').removeCls('readOnly-tagfield-BackgroundColor');
        form.down('grid[name=znxTmplItemGrid]').setDisabled(false);
        form.down('grid[name=znxInfoItemGrid]').setDisabled(false);
        /*
        form.down('checkbox[name=edmFlag]').setReadOnly(false);
        form.down('checkbox[name=qxdyFlag]').setReadOnly(false);
        form.down('checkbox[name=qypfFlag]').setReadOnly(false);
        if(form.down('checkbox[name=qypfFlag]').checked){
            form.down('textfield[name=fsslXs]').setReadOnly(false);
            form.down('textfield[name=fsslXs]').removeCls('readOnly-combox-BackgroundColor');
        };
        form.down('checkbox[name=dsfsFlag]').setReadOnly(false);
        if(form.down('checkbox[name=dsfsFlag]').checked){
            form.down('datefield[name=dsfsDate]').setReadOnly(false);
            form.down('datefield[name=dsfsDate]').removeCls('readOnly-combox-BackgroundColor');
            form.down('timefield[name=dsfsTime]').setReadOnly(false);
            form.down('timefield[name=dsfsTime]').removeCls('readOnly-combox-BackgroundColor');
        };
        */
        //form.down('checkbox[name=qzfsFlag]').setReadOnly(false);
    },
    /*王耀:预览发送邮件
     2016.1.12 */
    /*王耀:预览发送邮件
     2016.1.12 */

    preViewZnx:function(btn){
        var form = btn.findParentByType("panel").child("form");
        var sendPcId=form.getForm().findField("znxQfId").getValue();//群发任务ID
        //var senderEmail=form.getForm().findField("sender").getValue();//发件人邮箱
        //手动输入的邮箱
        //var keyInputEmail=form.getForm().findField("receverOrigin").getValue();
        var audIDTotal=form.down('tagfield[reference="recever"]').getValue();//添加听众
        if(audIDTotal=="" ||audIDTotal==undefined ){
            Ext.MessageBox.alert('提示', '收件人为空');
            return;
        }

        var ShiJiEmail="";
        var Audience="";
        var AudienceEmail="",AudienceOprID="";
        
        for (var i = 0; i < audIDTotal.length; i++) {
        	 if (ShiJiEmail == "") {
                 ShiJiEmail = audIDTotal[i];
             } else {
                 ShiJiEmail = ShiJiEmail + ',' + audIDTotal[i];
             }
        }


        var tzTotalAudienceParams ='{"ComID":"TZ_ZNXQ_PREVIEW_COM","PageID":"TZ_ZNXQ_VIEW_STD",' +
                '"OperateType":"checkEmailAudience","comParams":{"type":"checkZnxAudience","totalAudience":"'+ShiJiEmail+'"}}';
            
        Ext.tzLoadAsync(tzTotalAudienceParams,function(respData){
                AudienceOprID=respData.totalAudienceOprID;


        });

        var znxTmpId=form.getForm().findField("znxTmpId").getValue(),//站内信模板
        	znxlTheme=form.getForm().findField("znxSubj").getValue(),//站内信主题
        	znxContent= form.getForm().findField("znxCont").getValue();
        //是否有访问权限
        Ext.tzSetCompResourses("TZ_ZNXQ_PREVIEW_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZNXQ_PREVIEW_COM"]["TZ_ZNXQ_VIEW_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZNXQ_VIEW_STD，请检查配置。');
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
        /*
        var configuration ={
            "sendPcId":sendPcId,
            //模板ID;
            //"senderEmail": senderEmail,
            //"keyInputEmail": ShiJiEmail,
            //"audIDTotal": Audience,
            "znxTmpId":znxTmpId,
            //"emailModal":emailModal,
            "znxTheme": znxlTheme,
            "znxContent": znxContent,
            //"sendType":sendType,
            //"AudienceEmail":AudienceEmail,
            "AudienceOprID":AudienceOprID
        }
*/
        cmp = new ViewClass();

        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();
            var tzParamsJson = {
            		"ComID":"TZ_ZNXQ_PREVIEW_COM",
            		"PageID":"TZ_ZNXQ_VIEW_STD",
            		"OperateType":"previewZnx",
            		"comParams":{
            			"type":"previewZnx",
            			"viewNumber":"1",
            			"AudienceOprID":AudienceOprID, 
            			"znxTmpId":znxTmpId,
            			"znxlTheme":znxlTheme,
            			"znxContent":znxContent
            		}
            };
            var tzParams = Ext.JSON.encode(tzParamsJson); 
            Ext.tzLoad(tzParams,function(responseData){
                    var formData = responseData.formData;
                    //formData.configuration = Ext.encode(configuration);
                    form.setValues(formData);
                    var htmlCom = panel.down("component[name=znxContentHtml]");
                    htmlCom.getEl().update(Ext.util.Format.htmlDecode(formData.znxContentHtml));
                }
            )
        });
        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
	findCmpParent : function(ele){
        //根据当前DOM节点，向上查找最近的包含EXT节点对象的节点并返回该EXT节点对象
        if(ele){
            while(!Ext.getCmp(ele.parentNode.id)){
                ele = ele.parentNode;
            }
            return Ext.getCmp(ele.parentNode.id);
        }else{
            return false;
        }
    },
	viewMoreReceverForm: function(btn){
		var form = this.findCmpParent(btn.target).findParentByType('form').getForm();
		var znxQfId = form.findField('znxQfId').getValue(),
			sendModel = form.findField('sendModel').getValue();
		var config;
		
		if(sendModel==true){
			//一般发送	
			config = {"znxQfId":znxQfId, "sendModel":"NOR"}
		}else{
			//Excel导入发送
			config = {"znxQfId":znxQfId, "sendModel":"EXC"}
		}
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZNX_GL_COM"]["TZ_EMLQ_SJR_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_EMLQ_SJR_STD，请检查配置。');
			return;
		}

		Ext.syncRequire(className);
		ViewClass = Ext.ClassManager.get(className);
		//新建类
		win = new ViewClass(config);
		this.getView().add(win);

		win.show();
	},
	
	/***复制历史任务内容到当前任务***/
	copyHistoryData: function(btn){
		var className='KitchenSink.view.bulkEmailAndSMS.copyHistory.copyFromHistoryWin';
		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}
		ViewClass = Ext.ClassManager.get(className);
		var config = {
			taskType:"MAL"	
			}
		var win = new ViewClass(config);
		this.getView().add(win);
		win.show();	
	},
	/*搜索历史任务*/
	searchHistoryList: function(btn){
		var histortWin = btn.findParentByType("copyFromHistoryWin");
		var hisGrid = histortWin.child("grid");
		var searchContent = hisGrid.down("textfield[reference=searchHistoryContent]").getValue();
		var hisGridStore = hisGrid.getStore();
		hisGridStore.tzStoreParams = Ext.JSON.encode({"queryID": "myHistoryRw","taskType":"MAL","searchText":searchContent});
		hisGridStore.reload();
	},
	
	onSelectHistory: function(btn){
		var histortWin = btn.findParentByType("copyFromHistoryWin");
		var hisGrid = histortWin.child("grid");
		var selList = hisGrid.getSelectionModel().getSelection();
		var checkLen = selList.length;
		if(checkLen != 1){
			Ext.Msg.alert("提示","请选择一条需要复制的历史任务记录！");   
			return;
		}
		var qfRwId = selList[0].get("qfRwId");
		
		var emlPanel = this.getView();
		var emlBkDetForm = emlPanel.child("form");
		
		var currznxQfId = emlPanel.BulkTaskId;
		
		var receverStore = emlBkDetForm.down("tagfield[reference=recever]").getStore();
        receverStore.tzStoreParams = '{"znxQfId": "'+qfRwId+'","queryID": "recever"}';
		receverStore.load();
		
		var CCStore = emlBkDetForm.down('tagfield[reference=mailCC]').getStore();
		CCStore.tzStoreParams='{"znxQfId": "'+qfRwId+'","queryID": "CC"}';
		CCStore.load();
		
		var attaStore = emlBkDetForm.down("grid[reference=znxInfoItemGrid]").getStore();
		attaStore.tzStoreParams = '{"znxQfId": "'+qfRwId+'","queryID": "atta"}';
		attaStore.load();
		
		var tzParams = '{"ComID":"TZ_ZNX_GL_COM","PageID":"TZ_ZNX_DET_STD","OperateType":"getHistoryRwInfo","comParams":{"znxQfId":"'+qfRwId+'","currznxQfId":"'+currznxQfId+'"}}';
		Ext.tzLoad(tzParams,function(responseData){
			emlBkDetForm.down('radio[reference="sendModelNor"]').removeListener('change','norSend');
			emlBkDetForm.down('radio[reference="sendModelExc"]').removeListener('change','excSend');
			emlBkDetForm.down('tagfield[reference="recever"]').removeListener('change','receverChange');

			emlBkDetForm.getForm().setValues(responseData);
			if (emlBkDetForm.down('radio[reference="sendModelExc"]').checked) {
				emlBkDetForm.down('button[reference=setZnxTmpl]').disabled=false;
				var tzParams = '{"ComID":"TZ_ZNX_GL_COM","PageID":"TZ_ZNX_DET_STD","OperateType":"getEmlTmpItem","comParams":{"znxQfId":"'+qfRwId+'","znxTmpId":"'+responseData['znxTmpId']+'"}}';
				Ext.tzLoad(tzParams,function(resData){
					var EmlItemGrid = emlBkDetForm.down("grid[reference=znxTmplItemGrid]");
					var emlItemStore = EmlItemGrid.getStore();
					
					Ext.suspendLayouts();
					emlItemStore.suspendEvents();
					
					emlItemStore.removeAll(true);
					emlItemStore.add(resData['root']);
					emlItemStore.commitChanges();
					
					emlItemStore.resumeEvents();
					EmlItemGrid.reconfigure(emlItemStore);
					Ext.resumeLayouts(true);

					var userAgent = navigator.userAgent;
					if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1) {
						var copyItemsDom = document.getElementsByName("itememlCopy");
						for (var i = 0; i < copyItemsDom.length; i++) {
							$(copyItemsDom[i]).zclip({
								beforeCopy: function () {
									var itemHtml = this.parentNode.parentNode.parentNode.innerHTML;
									var itemFirstCharPositon = itemHtml.indexOf("[");
									var itemLastCharPositon = itemHtml.indexOf("]");
									var itemPara = itemHtml.slice(itemFirstCharPositon, itemLastCharPositon + 1);
									emlBkDetForm.down('textfield[name=copyfield]').setValue(itemPara);
								},
								copy: function () {
									return emlBkDetForm.down('textfield[name=copyfield]').getValue();
								}
							});
						}
					}
				});
			};

			if(responseData['recever'].length>0){
				emlBkDetForm.down('tagfield[reference=recever]').setValue(responseData['recever']);
				var len = responseData['recever'].length;
				for(var ind=0;ind<len;ind++){
					var emailAddr = responseData['recever'][ind];
					var emailReg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
					if (emailReg.test(emailAddr)){
						emlBkDetForm.down('combobox[reference=znxTmpId]').disabled=true;
						emlBkDetForm.down('combobox[reference=znxTmpId]').addCls('readOnly-combox-BackgroundColor');
					}
			   }
			}

			emlBkDetForm.down('radio[reference="sendModelNor"]').addListener('change','norSend');
			emlBkDetForm.down('radio[reference="sendModelExc"]').addListener('change','excSend');
			emlBkDetForm.down('tagfield[reference="recever"]').addListener('change','receverChange');
			
			histortWin.close();
		});
	}
});
