/**
 * 功能：邮件群发定义controller
 * 刘阳阳  2015-12-31
 */
Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.emailBulkDetController',
    /**
     * 功能：一般发送
     * 刘阳阳  2015-01-07
     */
    norSend:function (t, newValue, oldValue, eOpts) {
        if (newValue == true) {
			var emlForm = t.findParentByType('form');
            Ext.MessageBox.confirm('提示', '切换发送模式将会清除【收件人】【邮件模版】【邮件主题】【邮件内容】等信息，您确定要切换吗?', function(btnId){
                if(btnId == 'yes'){
                    emlForm.child('tagfield[reference=recever]').setEditable(true);
                    emlForm.child('tagfield[reference=recever]').disabled = false;
                    emlForm.child('toolbar').child('button[reference=addAudienceBtn]').disabled = false;
                    emlForm.child('toolbar').child('button[reference=addAudienceBtn]').removeCls('x-item-disabled x-btn-disabled');
					
					emlForm.child('toolbar').child('button[reference=selectStuBtn]').disabled=false;
					emlForm.child('toolbar').child('button[reference=selectStuBtn]').removeCls('x-item-disabled x-btn-disabled');
					
                    emlForm.child('toolbar').child('button[reference=pasteFromExcelBtn]').disabled=false;
                    emlForm.down('combobox[reference=emlTmpId]').disabled = false;
                    emlForm.down('tagfield[reference=recever]').removeCls('readOnly-tagfield-BackgroundColor');
                    emlForm.down('combobox[reference=emlTmpId]').removeCls('readOnly-combox-BackgroundColor');
					
					emlForm.down('button[reference=setEmlTmpl]').disabled=true;
					emlForm.down('button[reference=setEmlTmpl]').addCls('disabled-button-color');
					
                    this.changeSendModelClearData(t);
                }else{
                    t.setValue(false);
                    emlForm.down('radio[reference=sendModelExc]').removeListener('change','excSend');
                    emlForm.down('radio[reference=sendModelExc]').setValue(true);
                    emlForm.down('radio[reference=sendModelExc]').addListener('change','excSend');
                }
            },this);
        }
    },
    /**
     * 功能：导入Excel发送
     * 刘阳阳  2015-01-07
     */
    excSend:function (t, newValue, oldValue, eOpts) {
        if (newValue == true) {
			var emlForm = t.findParentByType('form');
            Ext.MessageBox.confirm('提示', '切换发送模式将会清除【收件人】【邮件模版】【邮件主题】【邮件内容】等信息，您确定要切换吗?', function(btnId) {
                if (btnId == 'yes') {
                    emlForm.child('tagfield[reference=recever]').setEditable(false);
                    emlForm.child('tagfield[reference=recever]').disabled=true;
                    emlForm.child('toolbar').child('button[reference=addAudienceBtn]').disabled=true;
                    emlForm.child('toolbar').child('button[reference=addAudienceBtn]').addCls('x-item-disabled x-btn-disabled');
					
					emlForm.child('toolbar').child('button[reference=selectStuBtn]').disabled=true;
					emlForm.child('toolbar').child('button[reference=selectStuBtn]').addCls('x-item-disabled x-btn-disabled');
					
                    emlForm.child('toolbar').child('button[reference=pasteFromExcelBtn]').disabled=true;
                    emlForm.down('combobox[reference=emlTmpId]').disabled=true;
                    emlForm.down('tagfield[reference=recever]').addCls('readOnly-tagfield-BackgroundColor');
                    emlForm.down('combobox[reference=emlTmpId]').addCls('readOnly-combox-BackgroundColor');
					
					emlForm.down('button[reference=setEmlTmpl]').disabled=true;
					emlForm.down('button[reference=setEmlTmpl]').addCls('disabled-button-color');
					
                    this.changeSendModelClearData(t);
                }else{
                    t.setValue(false);
                    emlForm.down('radio[reference=sendModelNor]').removeListener('change','norSend');
                    emlForm.down('radio[reference=sendModelNor]').setValue(true);
                    emlForm.down('radio[reference=sendModelNor]').addListener('change','norSend');
                }
            },this)
        }
    },
    /**
     * 功能：切换发送模式清除数据
     * 刘阳阳  2016-01-07
     */
    changeSendModelClearData:function(t){
        var form = t.findParentByType('form');
        var formdata = {
            "recever":"",
            "emlTmpId":"",
            "emlSubj":"",
            "emlCont":""
        };
        form.getForm().setValues(formdata);
        form.down('grid[reference=emlTmplItemGrid]').store.removeAll();
        form.down('button[reference=setEmlTmpl]').disabled=true;
		form.down('button[reference=setEmlTmpl]').addCls('disabled-button-color');
        t.findParentByType('form').child('tagfield[reference=recever]').clearValue();
    },
    /**
     * 功能：收件人Change
     * 刘阳阳  2016-01-11
     */
    receverChange:function(field,newValue, oldValue){
    	
        var emlBkDetForm = field.findParentByType('form');
        var newValue=newValue+"";
        var oldValue=oldValue+"";
        
        newValue = newValue.replace(/(^\s*)|(\s*$)/g, "");
        
        while (newValue.indexOf("，") >= 0){
        	 newValue = newValue.replace("，", ",");
        }

        //把所有空格也作为分隔符;
        var values = "";
        var newValueData = newValue.split(" ");
        if(newValueData.length > 0){
	        for(var i = 0; i < newValueData.length; i++){
	        	  var val = newValueData[i].replace(/(^\s*)|(\s*$)/g, "");
	        	  if(val != ""){
	        	  	 if(values != ""){
	        	  	 		values = values + "," + val;
	        	  	 }else{
	        	  	 		values = val;
	        	  	 }
	        	  	 
	        	  }
	        }
	        newValue = values;
        }
        
        field.setValue(newValue);
        var arrNewValue = newValue.split(',');
        var arrOldValue = oldValue.split(',');
        //处理 清除所有 按钮
        if(newValue==""){
        	/*
            if(!emlBkDetForm.child('fieldset[reference=sendModelSet]').child('radio[reference=sendModelExc]').checked){
                emlBkDetForm.down('combobox[reference=emlTmpId]').disabled=false;
                emlBkDetForm.down('combobox[reference=emlTmpId]').removeCls('readOnly-combox-BackgroundColor');
            }
            */
        }else{
            if(newValue.length>oldValue.length){
                var newinput= arrNewValue[arrNewValue.length-1];
                if (newinput!="") {
                    var EmlReg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
                    if (!EmlReg.test(newinput)){
                        var config = {
                            title: "提示",
                            msg: "请输入正确的邮箱地址.",
                            width: 300,
                            closable: true,
                            buttons: Ext.MessageBox.OK
                        };
                        Ext.MessageBox.show(config);
                        field.setValue(oldValue);
                        return;
                    }
                }
            }

            if(!emlBkDetForm.child('fieldset[reference=sendModelSet]').child('radio[reference=sendModelExc]').checked){
				/*
                if(emlBkDetForm.down('combobox[reference=emlTmpId]').value==""||emlBkDetForm.down('combobox[reference=emlTmpId]').value==null){
                    emlBkDetForm.down('combobox[reference=emlTmpId]').disabled=true;
                    emlBkDetForm.down('combobox[reference=emlTmpId]').addCls('readOnly-combox-BackgroundColor');
                }
				*/
            	
            	/*
				var hasEmailAdd = false;
				for(var i=0; i<arrNewValue.length; i++){
					var EmailReg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;	
					if (EmailReg.test(arrNewValue[i])){
						hasEmailAdd = true;
						break;
					}
				}
				
				if(hasEmailAdd) {
					emlBkDetForm.down('combobox[reference=emlTmpId]').disabled=true;
                    emlBkDetForm.down('combobox[reference=emlTmpId]').addCls('readOnly-combox-BackgroundColor');	
				}else{
					emlBkDetForm.down('combobox[reference=emlTmpId]').disabled=false;
                    emlBkDetForm.down('combobox[reference=emlTmpId]').removeCls('readOnly-combox-BackgroundColor');
				}
				*/
            }
        }
    },
	/**
     * 功能：添加教职员
     * 张浪  2016-08-22
     */
	/*
	selectStaff: function(btn){
		var emlBkDetForm = btn.findParentByType('emailBulkDet').child('form');
		var receverField = emlBkDetForm.child('tagfield[reference="recever"]');
		var arrAddData = [];
		
		Ext.tzShowPersonnelSelector({
			selModel: 'M', 
			callback: function(personInfoArr){
				for(var i=0; i<personInfoArr.length; i++){
					var email = personInfoArr[i].email;
					var EmlReg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
					if (EmlReg.test(email)){
						arrAddData.push(email);
					}
				}
				if(arrAddData.length>0) receverField.addValue(arrAddData);
			}	
		});
		
	},*/
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
    	
    	var stuWin = btn.findParentByType("searchStuWin");
		var stuGrid = stuWin.child("grid");
		var selList = stuGrid.getSelectionModel().getSelection();
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择考生！");   
			return;
		}
		var emlBkDetForm = btn.findParentByType('emailBulkDet').child('form');
		var receverField = emlBkDetForm.child('tagfield[reference="recever"]');
		var arrAddData = [];
		
		for(var i=0; i<selList.length; i++){
			var email = selList[i].data.email;
			var EmlReg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			if (EmlReg.test(email)){
				arrAddData.push(email);
			}
		}
		if(arrAddData.length>0) receverField.addValue(arrAddData);
		this.onWinClose(btn);
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
                    TZ_AUD_NAME:{
                        desc:'听众名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_AUD_ID:'听众ID',
                TZ_AUD_NAME: '听众名称',
                //TZ_ORG_CODE:'所属部门',
                ROW_ADDED_DTTM:'创建时间'
                // ROW_LASTMANT_DTTM:'修改时间'
            },
            multiselect: true,
            callback: function(selection){
                if(selection.length>0){
                    for(j=0;j<selection.length;j++){
                        addAudirec="";
                        addAudirec = {"id":selection[j].data.TZ_AUD_ID,"desc":selection[j].data.TZ_AUD_NAME};
                        arrAddAudience.push(addAudirec);
                        arrAddAudiValue.push(selection[j].data.TZ_AUD_ID);
                    };
                    var emailBulkDetForm = btn.findParentByType('emailBulkDet').child('form');
                    var storereceive=emailBulkDetForm.child('tagfield[reference="recever"]').getStore();
                    storereceive.add(arrAddAudience);
                    emailBulkDetForm.down('tagfield[reference="recever"]').removeListener('change','receverChange');
                    emailBulkDetForm.child('tagfield[reference="recever"]').addValue(arrAddAudiValue);
                    emailBulkDetForm.down('tagfield[reference="recever"]').addListener('change','receverChange');

                    emailBulkDetForm.down('button[reference=clearAllBtn]').disabled=false;
                    emailBulkDetForm.down('button[reference=clearAllBtn]').removeCls('x-item-disabled x-btn-disabled')
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
        if(emlBkDetForm.down('radio[reference=sendModelExc]').checked){
            var emlQfId=btn.findParentByType('form').down('textfield[name="emlQfId"]').value;
            var tzParams = '{"ComID":"TZ_EMLQ_COM","PageID":"TZ_EMLQ_DET_STD","OperateType":"clearAll","comParams":{"emlQfId":"'+emlQfId+'"}}';
            Ext.tzLoad(tzParams,function(responseData){
                emlBkDetForm.getForm().setValues(responseData);
            });

            emlBkDetForm.down('grid[reference=emlTmplItemGrid]').store.removeAll(true);
        }else{
            emlBkDetForm.child('tagfield[reference=recever]').store.removeAll(true);
            emlBkDetForm.child('tagfield[reference=recever]').setValue("");
        }
        btn.addCls('x-item-disabled x-btn-disabled');
    },
    /**
     * 功能：从Excel粘贴
     * 刘阳阳  2015-01-05
     */
    pasteFromExcel:function(btn){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EMLQ_COM"]["TZ_EMLQ_PFEXC_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }

        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_EMLQ_PFEXC_STD，请检查配置。');
            return;
        }

        var win = this.lookupReference('pasteFromExcelWin');

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        win.show();
    },
    /**
     * 功能：设置邮件模版
     * 刘阳阳  2016-01-07
     */
    setEmlTmpl:function(btn){
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

        var emlTmpId = btn.findParentByType('form').down('combobox[reference=emlTmpId]').getValue();

        cmp.on('afterrender',function(panel){
            //组件注册表单信息;
            var form = panel.child('form').getForm();
            form.findField("emltempid").setReadOnly(true);
            form.findField("emltemporg").setReadOnly(true);
            form.findField("emltempid").addCls("lanage_1");
            form.findField("emltemporg").addCls("lanage_1");

            //加载邮件模版信息
            var tzParams = '{"ComID":"TZ_EMLQ_COM","PageID":"TZ_EMLQ_DET_STD","OperateType":"getEmlMetaTmpInfo","comParams":{"emlTmpId":"'+emlTmpId+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                var emltemporg=responseData['emltemporg'];
                var metaempid=responseData['metaempid'];
                tzParams = '{"ComID":"TZ_EML_TMPL_MG_COM","PageID":"TZ_EML_TMPL_STD","OperateType":"QF","comParams":{"emltempid":"'+emlTmpId+'","emltemporg":"'+emltemporg+'"}}';
                Ext.tzLoad(tzParams,function(responseData){
                    form.setValues(responseData);
                    //信息项数据
                    var	emlTmplItemGrid = panel.down('grid[name=emlTmplItemGrid]');
                    var tzStoreParamsItem = "{'restempid':'"+metaempid+"'}";
                    emlTmplItemGrid.store.tzStoreParams = tzStoreParamsItem;
                    emlTmplItemGrid.store.load();
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
        var objEmlContentHtml = form.findField("emlCont");
        var dom = form.findField("emlCont").getEl();
        var editorId = dom.id + "-ueditor";
        UE.getEditor(editorId).execCommand("inserthtml",parainfoitem);

        var emlcont = emlbkdefForm.down('ueditor[name=emlCont]').value;
        emlbkdefForm.down('ueditor[name=emlCont]').setValue(emlcont);
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
                        var applyItemGrid = file.findParentByType("grid[reference=emlInfoItemGrid]");
                        var emlBBkDetFormRec = applyItemGrid.findParentByType("form[reference=emlBulkDetForm]").getForm().getFieldValues();
                        var r = Ext.create('KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkAttaModel', {
                            "emlQfId":emlBBkDetFormRec['emlQfId'] ,
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
        var store = btn.findParentByType("grid[reference=emlInfoItemGrid]").getStore();
        var selList = btn.findParentByType("grid[reference=emlInfoItemGrid]").getSelectionModel().getSelection();
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
		if(formrec["emlQfDesc"]==""||formrec["emlQfDesc"]==null){
            Ext.Msg.alert("提示","请填写群发任务名称.");
            return;
        }
        if(formrec["sender"]==""||formrec["sender"]==null){
            Ext.Msg.alert("提示","请选择发件人.");
            return;
        }
        if(!emlbkdefForm.child('tagfield[reference="recever"]').value||emlbkdefForm.child('tagfield[reference="recever"]').value==""){
            Ext.Msg.alert("提示","收件人不能为空.");
            return;
        };
        if(emlbkdefForm.down('checkbox[reference=tsfsFlag]').checked){
            var tsfsEmail=emlbkdefForm.down('textfield[name=tsfsEmail]').value;
            if(tsfsEmail==""){
                Ext.Msg.alert("提示","同时发送给邮箱不能为空.");
                return;
            }else{
				/*
                var EmlReg =/^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
                if(!EmlReg.test(tsfsEmail)){
                    Ext.Msg.alert("提示","同时发送给邮箱格式不正确.");
                    return;
                }
				*/
            }
        };
        if(formrec["emlSubj"]==""){
            Ext.Msg.alert("提示","邮件主题不能为空.");
            return;
        }
        if(formrec["qypfFlag"]){
            var numreg = /^[0-9]*$/i;
            var val = formrec['fsslXs'];
            var bolFlag = numreg.test(val)&&(val>0?true:false);
            if(!bolFlag){
                Ext.Msg.alert("提示","每小时发送邮件数必须为大于0的正整数.");
                return;
            }
        }
        if(formrec["dsfsFlag"]){
            if(Ext.Date.format(formrec['dsfsDate'], 'Ymd')==Ext.Date.format(new Date(), 'Ymd')){
                if(Ext.Date.format(formrec['dsfsTime'], 'Hi')<=Ext.Date.format(new Date(), 'Hi')){
                    Ext.Msg.alert("提示","定时发送日期时间必须在当前时间之后.");
                    return;
                }
            }else if(Ext.Date.format(formrec['dsfsDate'], 'Ymd')<Ext.Date.format(new Date(), 'Ymd')){
                Ext.Msg.alert("提示","定时发送日期时间必须在当前时间之后.");
                return;
            }
            msgInfo='邮件已存至草稿箱中，将会于 '+Ext.Date.format(formrec['dsfsDate'], 'Y-m-d  ')+Ext.Date.format(formrec['dsfsTime'], 'H:i')+' 发送';
        }
        var othInfo = '{"crePer":"'+formrec["crePer"]+'","dept":"'+formrec["dept"]+'","setId":"'+formrec["setId"]+'","recever":"'+emlbkdefForm.child('tagfield[reference="recever"]').value+'","mailCC":"'+emlbkdefForm.child('tagfield[reference="mailCC"]').value+'"}';
        var attaGrid,
            attaAllRecs,attaDelRecs,attaNewAtta,
            attaList="",attaDelLit="",attaNewList="",
            comParams="";
        attaGrid =emlbkdefForm.down("grid[reference=emlInfoItemGrid]");
        if (formrec['emlQfId']=="NEXT"){
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

        var tzParams = '{"ComID":"TZ_EMLQ_COM","PageID":"TZ_EMLQ_DET_STD","OperateType":"save","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(responseData){
            emlbkdefForm.getForm().setValues(responseData);
            emlbkdefForm.down('displayfield[name=creDt]').setVisible(true);

            if(formrec["dsfsFlag"]){
                var dsfsInfo=Ext.Date.format(formrec['dsfsDate'], 'Y-m-d')+"  "+Ext.Date.format(formrec['dsfsTime'], 'H:i');
                dsfsInfo='<span style="color:red">邮件将会于'+dsfsInfo+'发送</span>';
                emlbkdefForm.down('displayfield[name=dsfsInfo]').setValue(dsfsInfo);
                emlbkdefForm.down('displayfield[name=dsfsInfo]').setVisible(true);
            }else{
                emlbkdefForm.down('displayfield[name=dsfsInfo]').setVisible(false);
            }

            var par = '{"emlQfId": "'+responseData['emlQfId']+'","queryID": "atta"}';
            attaGrid.store.tzStoreParams=par;
            attaGrid.store.load();
        },msgInfo,true,this);

        var arrEmlBkMgrPanel=Ext.ComponentQuery.query("emailBulkMgr");
        for(var i=0;i<arrEmlBkMgrPanel.length;i++){
            arrEmlBkMgrPanel[i].down('grid').store.load();
        }
    },
    /**
     * 功能：发送
     * 刘阳阳  2016-01-12
     */
    sendEmail:function(btn){
		var me = this;
		var emlbkdefPanel = btn.up('panel');
        var emlbkdefForm = emlbkdefPanel.child('form');
		/*表单验证*/
		if(!emlbkdefForm.getForm().isValid()) return;
        Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.ensure","确认"),
            Ext.tzGetResourse("TZ_COMMON_EMAIL_COM.TZ_COM_EMAIL_STD.ensureSendDesc","确认要发送邮件?"), function(btnId) {
                if (btnId == 'yes') {
                    var formdata = Ext.JSON.encode(emlbkdefForm.getValues());
                    var formrec = emlbkdefForm.getForm().getFieldValues();
                    var msgInfo = "";
                    if(formrec["sender"]==""||formrec["sender"]==null){
                        Ext.Msg.alert("提示","请选择发件人.");
                        return;
                    }
                    if(!emlbkdefForm.child('tagfield[reference="recever"]').value||emlbkdefForm.child('tagfield[reference="recever"]').value==""){
                        Ext.Msg.alert("提示","收件人不能为空.");
                        return;
                    };
                    if(emlbkdefForm.down('checkbox[reference=tsfsFlag]').checked){
                        var tsfsEmail=emlbkdefForm.down('textfield[name=tsfsEmail]').value;
                        if(tsfsEmail==""){
                            Ext.Msg.alert("提示","同时发送给邮箱不能为空.");
                            return;
                        }else{
							/*
                            var EmlReg =/^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
                            if(!EmlReg.test(tsfsEmail)){
                                Ext.Msg.alert("提示","同时发送给邮箱格式不正确.");
                                return;
                            }*/
                        }
                    };
                    if(formrec["qypfFlag"]){
                        var numreg = /^[0-9]*$/i;
                        var val = formrec['fsslXs'];
                        var bolFlag = numreg.test(val)&&(val>0?true:false);
                        if(!bolFlag){
                            Ext.Msg.alert("提示","每小时发送邮件数必须为大于0的正整数.");
                            return;
                        }
                    }
                    msgInfo='群发任务已提交.';
                    if (formrec["dsfsFlag"]) {
                        if ((Ext.Date.format(formrec['dsfsDate'], 'Ymd') >= Ext.Date.format(new Date(), 'Ymd')) &&
                            (Ext.Date.format(formrec['dsfsTime'], 'Hi') >= Ext.Date.format(new Date(), 'Hi'))) {
                            msgInfo = '邮件已存至草稿箱中，将会于 ' + Ext.Date.format(formrec['dsfsDate'], 'Y-m-d  ') + Ext.Date.format(formrec['dsfsTime'], 'H:i') + ' 发送';
                        } else {
                            Ext.Msg.alert("提示", "定时发送日期时间必须在当前时间之后.");
                            return;
                        }
                    };
                    var othInfo = '{"crePer":"' + formrec["crePer"] + '","dept":"' + formrec["dept"] + '","setId":"' + formrec["setId"] + '","recever":"' + emlbkdefForm.child('tagfield[reference="recever"]').value + '","mailCC":"' + emlbkdefForm.child('tagfield[reference="mailCC"]').value + '"}';
                    //alert(formrec['emlQfId']);
                    var attaGrid,
                        attaAllRecs, attaDelRecs, attaNewAtta,
                        attaList = "", attaDelLit = "", attaNewList = "",
                        comParams = "";
                    attaGrid = emlbkdefForm.down("grid[reference=emlInfoItemGrid]");
                    if (formrec['emlQfId'] == "NEXT") {
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

                    var tzParams = '{"ComID":"TZ_EMLQ_COM","PageID":"TZ_EMLQ_DET_STD","OperateType":"sendEml","comParams":{' + comParams + '}}';
                    Ext.tzSubmit(tzParams, function (responseData) {
                        emlbkdefForm.getForm().setValues(responseData);
                        emlbkdefForm.down('displayfield[name=creDt]').setVisible(true);

                        if (formrec["dsfsFlag"]) {
                            var dsfsInfo = Ext.Date.format(formrec['dsfsDate'], 'Y-m-d') + "  " + Ext.Date.format(formrec['dsfsTime'], 'H:i');
                            dsfsInfo = '<span style="color:red">邮件将会于' + dsfsInfo + '发送</span>';
                            emlbkdefForm.down('displayfield[name=dsfsInfo]').setValue(dsfsInfo);
                            emlbkdefForm.down('displayfield[name=dsfsInfo]').setVisible(true);
                        } else {
                            emlbkdefForm.down('displayfield[name=dsfsInfo]').setVisible(false);
                        }

                        if (responseData['rwzxZt']==""||responseData['rwzxZt']=="D"||responseData['rwzxZt']=="E") {

                        } else {
                            emlbkdefPanel.down('button[reference=saveBtn]').setDisabled(true);
                            emlbkdefPanel.down('button[reference=sendBtn]').setDisabled(true);
							/*
                            if (responseData['rwzxZt'] == "B") {
                                emlbkdefPanel.down('button[reference=revokeBtn]').setVisible(true);
                            }
							*/
							emlbkdefPanel.down('button[reference=revokeBtn]').setVisible(true);
                            emlbkdefPanel.getController().pageReadonly(emlbkdefForm);
                        }
						/*
                        if(responseData['rwzxZt']=="C"||responseData['rwzxZt']=="D"){
                            emlbkdefPanel.down('button[reference=viewHisBtn]').setDisabled(false);
                        }
						*/
                        var par = '{"emlQfId": "' + responseData['emlQfId'] + '","queryID": "atta"}';
                        attaGrid.store.tzStoreParams = par;
                        attaGrid.store.load();
						
						if(!emlbkdefForm.down('button[reference=copyHistoryBtn]').hidden){
							/*隐藏复制历史任务*/
							emlbkdefForm.down('button[reference=copyHistoryBtn]').setHidden(true);
						}
						
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
        var comParams = '"emlQfId":'+formrec['emlQfId']+'';
        var tzParams = '{"ComID":"TZ_EMLQ_COM","PageID":"TZ_EMLQ_DET_STD","OperateType":"revoke","comParams":{'+comParams+'}}';
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
        var emailID=form.getForm().findField("emlQfId").getValue();//群发任务ID

        Ext.tzSetCompResourses("TZ_EMLQ_VIEWTY_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EMLQ_VIEWTY_COM"]["TZ_EMLQ_VIEWTY_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_EMLQ_VIEWTY_STD，请检查配置。');
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
            var tzStoreParams ='{"storeType":"history","emailID":"'+emailID+'"}';
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
     * 功能：从excel粘贴 确定
     * 刘阳阳  2016-01-11
     */
    onWinEnsure:function(btn){
        //解析粘贴Excel数据
        var excelText = btn.findParentByType("window").down('textarea[name=excelText]').getValue();
        if(!excelText||excelText.replace(/(^\s*)|(\s*$)/g, "")==""){
            Ext.MessageBox.show({
                title: '提示',
                msg: '请粘贴Excel数据!',
                buttons: Ext.Msg.OK,
                scope: this,
                icon:  Ext.Msg.WARNING});
            return;
        };
        var columnsData = excelText.split("\n");//获取每行数据
        var isLeagalFlag="N";
        var irLeagalStr="";
        var arrAddData=[];
        for(var i = 0;i<columnsData.length;i++){
            //if(columnsData[i].replace(/(^\s*)|(\s*$)/g, "")=="") continue;
            var EmlReg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			var emailAddr = Ext.String.trim(columnsData[i]);
            if (!EmlReg.test(emailAddr) && emailAddr != ""){
                isLeagalFlag="Y";
                irLeagalStr=i;
                break;
            }else{
				if(columnsData[i] != "") arrAddData.push(emailAddr);
            };
        }
        if (isLeagalFlag=="Y"){
            Ext.Msg.alert("提示","请输入正确的邮箱地址.(第"+(irLeagalStr+1)+"行数据不合法.)");
        }else{
            var emailBulkDetForm = btn.findParentByType('emailBulkDet').child('form');
            emailBulkDetForm.child('tagfield[reference="recever"]').addValue(arrAddData);
            this.onWinClose(btn);
        }
    },
    /**
     * 功能：从excel粘贴 关闭
     * 刘阳阳  2016-01-11
     */
    onWinClose:function(btn){
        btn.findParentByType("window").close();
    },
    /**
     * 功能：页面只读
     */
    pageReadonly:function(form){
        form.down('textfield[name=emlQfDesc]').setReadOnly(true);
        form.down('textfield[name=emlQfDesc]').addCls('readOnly-combox-BackgroundColor');
        form.down('combobox[name=sender]').setReadOnly(true);
        form.down('combobox[name=sender]').addCls('readOnly-combox-BackgroundColor');
        form.down('radio[reference=sendModelNor]').setReadOnly(true);
        form.down('radio[reference=sendModelExc]').setReadOnly(true);
        if(form.down('radio[reference=sendModelExc]').checked){
            form.down('button[reference=impExc]').disabled=true;
            form.down('button[reference=impExc]').addCls('x-item-disabled x-btn-disabled');
        };
        form.down('tagfield[reference=recever]').setEditable(false);
        form.down('tagfield[reference=recever]').disabled=true;
        form.down('tagfield[reference=recever]').addCls('readOnly-tagfield-BackgroundColor');
        form.down('toolbar').child('button[reference=addAudienceBtn]').disabled=true;
        form.down('toolbar').child('button[reference=addAudienceBtn]').addCls('x-item-disabled x-btn-disabled');
		
		form.down('toolbar').child('button[reference=selectStuBtn]').disabled=true;
		form.down('toolbar').child('button[reference=selectStuBtn]').addCls('x-item-disabled x-btn-disabled');
						
        form.down('toolbar').child('button[reference=clearAllBtn]').disabled=true;
        form.down('toolbar').child('button[reference=clearAllBtn]').addCls('x-item-disabled x-btn-disabled');
        form.down('toolbar').child('button[reference=pasteFromExcelBtn]').disabled=true;
        form.down('checkbox[name=tsfsFlag]').setReadOnly(true);
        form.down('textfield[name=tsfsEmail]').setReadOnly(true);
        form.down('textfield[name=tsfsEmail]').addCls('readOnly-combox-BackgroundColor');
        form.down('tagfield[reference=mailCC]').setEditable(false);
        form.down('tagfield[reference=mailCC]').disabled=true;
        form.down('tagfield[reference=mailCC]').addCls('readOnly-tagfield-BackgroundColor');
        form.down('combobox[reference=emlTmpId]').disabled=true;
        form.down('combobox[reference=emlTmpId]').addCls('readOnly-combox-BackgroundColor');
        if(form.down('combobox[reference=emlTmpId]').getValue()!=""){
            form.down('button[reference=setEmlTmpl]').disabled=true;
            form.down('button[reference=setEmlTmpl]').addCls('disabled-button-color');
        };
        form.down('textfield[name=emlSubj]').setReadOnly(true);
        form.down('textfield[name=emlSubj]').addCls('readOnly-combox-BackgroundColor');
        //form.down('ueditor[name=emlCont]').setReadOnly(true);
        form.down('ueditor[name=emlCont]').addCls('readOnly-tagfield-BackgroundColor');
        form.down('grid[name=emlTmplItemGrid]').setDisabled(true);
        form.down('grid[name=emlInfoItemGrid]').setDisabled(true);
        form.down('checkbox[name=edmFlag]').setReadOnly(true);
        form.down('checkbox[name=qxdyFlag]').setReadOnly(true);
        form.down('checkbox[name=qypfFlag]').setReadOnly(true);
        if(form.down('checkbox[name=qypfFlag]').checked){
            form.down('textfield[name=fsslXs]').setReadOnly(true);
            form.down('textfield[name=fsslXs]').addCls('readOnly-combox-BackgroundColor');
        };
        form.down('checkbox[name=dsfsFlag]').setReadOnly(true);
        if(form.down('checkbox[name=dsfsFlag]').checked){
            form.down('datefield[name=dsfsDate]').setReadOnly(true);
            form.down('datefield[name=dsfsDate]').addCls('readOnly-combox-BackgroundColor');
            form.down('timefield[name=dsfsTime]').setReadOnly(true);
            form.down('timefield[name=dsfsTime]').addCls('readOnly-combox-BackgroundColor');
        };
        form.down('checkbox[name=qzfsFlag]').setReadOnly(true);
    },
    /**
     * 功能：控制页面字段
     */
    pageFiledsDisControl:function(form){
        form.down('textfield[name=emlQfDesc]').setReadOnly(false);
        form.down('textfield[name=emlQfDesc]').removeCls('readOnly-combox-BackgroundColor');
        form.down('combobox[name=sender]').setReadOnly(false);
        form.down('combobox[name=sender]').removeCls('readOnly-combox-BackgroundColor');
        form.down('radio[reference=sendModelNor]').setReadOnly(false);
        form.down('radio[reference=sendModelExc]').setReadOnly(false);
        if(form.down('radio[reference=sendModelExc]').checked){
            form.down('button[reference=impExc]').disabled=false;
            form.down('button[reference=impExc]').removeCls('x-item-disabled x-btn-disabled');
        };
        if (!form.down('radio[reference=sendModelExc]').checked){
			//console.log(form.down('toolbar').child('button[reference=pasteFromExcelBtn]').disabled);
            form.down('toolbar').child('button[reference=addAudienceBtn]').disabled=false;
            form.down('toolbar').child('button[reference=addAudienceBtn]').removeCls('x-item-disabled x-btn-disabled');
			
            if(form.down('combobox[reference=emlTmpId]').getValue()==""||form.down('combobox[reference=emlTmpId]').getValue()==null){
                form.down('tagfield[reference=recever]').setEditable(true);
                form.down('tagfield[reference=recever]').disabled=false;
                form.down('tagfield[reference=recever]').removeCls('readOnly-tagfield-BackgroundColor');
                form.down('toolbar').child('button[reference=pasteFromExcelBtn]').disabled=false;
				
				form.down('toolbar').child('button[reference=selectStuBtn]').disabled=false;
            	form.down('toolbar').child('button[reference=selectStuBtn]').removeCls('x-item-disabled x-btn-disabled');
            }
        }
        if(form.down('tagfield[reference=recever]').getValue!=""){
            form.down('toolbar').child('button[reference=clearAllBtn]').disabled=false;
            form.down('toolbar').child('button[reference=clearAllBtn]').removeCls('x-item-disabled x-btn-disabled');
        }
        form.down('checkbox[name=tsfsFlag]').setReadOnly(false);
        form.down('textfield[name=tsfsEmail]').setReadOnly(false);
        form.down('textfield[name=tsfsEmail]').removeCls('readOnly-combox-BackgroundColor');
        form.down('tagfield[reference=mailCC]').setEditable(true);
        form.down('tagfield[reference=mailCC]').disabled=false;
        form.down('tagfield[reference=mailCC]').removeCls('readOnly-tagfield-BackgroundColor');
        if (!form.down('radio[reference=sendModelExc]').checked&&form.down('tagfield[reference=recever]').getValue=="") {
            form.down('combobox[reference=emlTmpId]').disabled = false;
            form.down('combobox[reference=emlTmpId]').removeCls('readOnly-combox-BackgroundColor');
        }
        if(form.down('combobox[reference=emlTmpId]').getValue()=="" || form.down('combobox[reference=emlTmpId]').getValue()== null){
			form.down('button[reference=setEmlTmpl]').disabled=true;
			form.down('button[reference=setEmlTmpl]').addCls('disabled-button-color');
        }else{
			form.down('button[reference=setEmlTmpl]').disabled=false;
            form.down('button[reference=setEmlTmpl]').removeCls('disabled-button-color');
		}
        form.down('textfield[name=emlSubj]').setReadOnly(false);
        form.down('textfield[name=emlSubj]').removeCls('readOnly-combox-BackgroundColor');
        //form.down('ueditor[name=emlCont]').setReadOnly(false);
        form.down('ueditor[name=emlCont]').removeCls('readOnly-tagfield-BackgroundColor');
        form.down('grid[name=emlTmplItemGrid]').setDisabled(false);
        form.down('grid[name=emlInfoItemGrid]').setDisabled(false);
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
        form.down('checkbox[name=qzfsFlag]').setReadOnly(false);
    },
	/*==========================================+
	++ 功能描述：导入Excel							+
	++ 张浪，2016-01-14							+
	+==========================================*/
	importFromExcel: function(btn){
		var form = btn.findParentByType("form");
		var picID = form.getForm().findField("emlQfId").getValue();

		var className = 'KitchenSink.view.bulkEmailAndSMS.ImportExcel.smsEmlImportExcelWindow';
		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}
		var ViewClass = Ext.ClassManager.get(className);
		var win = new ViewClass({
			sendPicId: picID,
			sendType:'MAL',
			callback: function(receverArr,emailSubject,emalContent,paramItemZwfArr){
				var viewReceverArr = receverArr.slice(0,20);
				//设置收件人
				form.down("tagfield[reference=recever]").addValue(viewReceverArr);
				//设置邮件主题
				//if(emailSubject != "") form.getForm().findField("emlSubj").setValue(emailSubject);
				//设置邮件内容
				if(emalContent != "") form.getForm().findField("emlCont").setValue(emalContent);
				//设置信息项占位符
				form.down('grid[reference=emlTmplItemGrid]').store.removeAll(true);
                form.down('grid[reference=emlTmplItemGrid]').store.add(paramItemZwfArr);

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
			}
		});
		win.show();
	},
    /*王耀:预览发送邮件
     2016.1.12 */
    /*王耀:预览发送邮件
     2016.1.12 */

    preViewEmail:function(btn){
        var form = btn.findParentByType("panel").child("form");
        var sendPcId=form.getForm().findField("emlQfId").getValue();//群发任务ID
        var senderEmail=form.getForm().findField("sender").getValue();//发件人邮箱
        //手动输入的邮箱
        var keyInputEmail=form.getForm().findField("receverOrigin").getValue(),
            audIDTotal=form.down('tagfield[reference="recever"]').getValue();//添加听众
        if(audIDTotal=="" ||audIDTotal==undefined ){
            Ext.MessageBox.alert('提示', '收件人为空');
            return;
        }

        var ShiJiEmail="";
        var Audience="";
        var AudienceEmail="",AudienceOprID="";
        //获取发送模式，若为true则是一般发送，false则是导入excel发送;
        var sendType=form.getForm().findField("sendModel").getValue();
        if (sendType==true) {
            //如果为true则是一般发送，分为邮箱和听众;
            var EmlReg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;//校验是否为邮箱
            for (var i = 0; i < audIDTotal.length; i++) {
                if (EmlReg.test(audIDTotal[i])) {
                    if (ShiJiEmail == "") {
                        ShiJiEmail = audIDTotal[i];
                    } else {
                        ShiJiEmail = ShiJiEmail + ',' + audIDTotal[i];
                    }
                }
                else {
                    if (Audience == "") {
                        Audience = audIDTotal[i];
                    } else {
                        Audience = Audience + ',' + audIDTotal[i];
                    }
                }

            }
            var tzTotalAudienceParams ='{"ComID":"TZ_EMLQ_PREVIEW_COM","PageID":"TZ_EMLQ_VIEW_STD",' +
                '"OperateType":"checkEmailAudience","comParams":{"type":"checkEmailAudience","totalAudience":"'+Audience+'"}}';
            
            Ext.tzLoadAsync(tzTotalAudienceParams,function(respData){
                Audience=respData.totalAudienceID;
                AudienceEmail=respData.totalAudienceEmail;
                AudienceOprID=respData.totalAudienceOprID;


            });
        }
        //否则即为导入excel发送，不考虑听众情况;
        else{
            ShiJiEmail=audIDTotal;
            Audience="";
        }
        //添加同时发送
        var tsfsFlag=form.getForm().findField("tsfsFlag").getValue();
        if(tsfsFlag){
        	var tsfsEmail=form.getForm().findField("tsfsEmail").getValue();
            if (tsfsEmail!=""){
    			tsfsEmail = tsfsEmail.replace(/;/g,',');
    			ShiJiEmail=ShiJiEmail+','+tsfsEmail
    		}
        }
        

        var emlTmpId=form.getForm().findField("emlTmpId").getValue(),//邮件模板
            emailTheme=form.getForm().findField("emlSubj").getValue(),//邮件主题
            emailModal=form.getForm().findField("emlTmpId").getValue(),//邮件模板
        // emailContent=form.getForm().findField("emlCont").getValue();//邮件内容
            emailContent= Ext.util.Format.htmlEncode(form.getForm().findField("emlCont").getValue());
        //是否有访问权限
        Ext.tzSetCompResourses("TZ_EMLQ_PREVIEW_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EMLQ_PREVIEW_COM"]["TZ_EMLQ_VIEW_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_EMLQ_VIEW_STD，请检查配置。');
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
        var configuration ={
            "sendPcId":sendPcId,
            //模板ID;
            "senderEmail": senderEmail,
            "keyInputEmail": ShiJiEmail,
            "audIDTotal": Audience,
            "emlTmpId":emlTmpId,
            "emailModal":emailModal,
            "emailTheme": emailTheme,
            "emailContent": emailContent,
            "sendType":sendType,
            "AudienceEmail":AudienceEmail,
            "AudienceOprID":AudienceOprID
        }

        cmp = new ViewClass();

        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();
            //var tzParams = '{"ComID":"TZ_EMLQ_PREVIEW_COM","PageID":"TZ_EMLQ_VIEW_STD","OperateType":"previewEmail","comParams":{"type":"previewEmail","sendPcId":"'+sendPcId+'","sendType":"'+sendType+'","viewNumber":"1","senderEmail":"'+senderEmail+'","keyInputEmail":"'+ShiJiEmail+'","audIDTotal":"'+Audience+'", "emlTmpId":"'+emlTmpId+'","emailTheme":"'+emailTheme+'","emailModal":"'+emailModal+'","emailContent":"'+emailContent+'"}}';
            var tzParamsJson = {
            		"ComID":"TZ_EMLQ_PREVIEW_COM",
            		"PageID":"TZ_EMLQ_VIEW_STD",
            		"OperateType":"previewEmail",
            		"comParams":{
            			"type":"previewEmail",
            			"sendPcId":sendPcId,
            			"sendType":sendType,
            			"viewNumber":"1",
            			"senderEmail":senderEmail,
            			"keyInputEmail":ShiJiEmail,
            			"audIDTotal":Audience,
            			"emlTmpId":emlTmpId,
            			"emailTheme":emailTheme,
            			"emailModal":emailModal,
            			"emailContent":emailContent
            		}
            };
            var tzParams = Ext.JSON.encode(tzParamsJson);
            Ext.tzLoad(tzParams,function(responseData){
                    var formData = responseData.formData;
                    formData.configuration = Ext.encode(configuration);
                    form.setValues(formData);
                    var htmlCom = panel.down("component[name=emailContentHtml]");
                    htmlCom.getEl().update(Ext.util.Format.htmlDecode(formData.emailContent));
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
		var emlQfId = form.findField('emlQfId').getValue(),
			sendModel = form.findField('sendModel').getValue();
		var config;
		
		if(sendModel==true){
			//一般发送	
			config = {"emlQfId":emlQfId, "sendModel":"NOR"}
		}else{
			//Excel导入发送
			config = {"emlQfId":emlQfId, "sendModel":"EXC"}
		}
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EMLQ_COM"]["TZ_EMLQ_SJR_STD"];
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
		
		var currEmlQfId = emlPanel.BulkTaskId;
		
		var receverStore = emlBkDetForm.down("tagfield[reference=recever]").getStore();
        receverStore.tzStoreParams = '{"emlQfId": "'+qfRwId+'","queryID": "recever"}';
		receverStore.load();
		
		var CCStore = emlBkDetForm.down('tagfield[reference=mailCC]').getStore();
		CCStore.tzStoreParams='{"emlQfId": "'+qfRwId+'","queryID": "CC"}';
		CCStore.load();
		
		var attaStore = emlBkDetForm.down("grid[reference=emlInfoItemGrid]").getStore();
		attaStore.tzStoreParams = '{"emlQfId": "'+qfRwId+'","queryID": "atta"}';
		attaStore.load();
		
		var tzParams = '{"ComID":"TZ_EMLQ_COM","PageID":"TZ_EMLQ_DET_STD","OperateType":"getHistoryRwInfo","comParams":{"emlQfId":"'+qfRwId+'","currEmlQfId":"'+currEmlQfId+'"}}';
		Ext.tzLoad(tzParams,function(responseData){
			emlBkDetForm.down('radio[reference="sendModelNor"]').removeListener('change','norSend');
			emlBkDetForm.down('radio[reference="sendModelExc"]').removeListener('change','excSend');
			emlBkDetForm.down('tagfield[reference="recever"]').removeListener('change','receverChange');

			emlBkDetForm.getForm().setValues(responseData);
			if (emlBkDetForm.down('radio[reference="sendModelExc"]').checked) {
				emlBkDetForm.down('button[reference=setEmlTmpl]').disabled=false;
				var tzParams = '{"ComID":"TZ_EMLQ_COM","PageID":"TZ_EMLQ_DET_STD","OperateType":"getEmlTmpItem","comParams":{"emlQfId":"'+qfRwId+'","emlTmpId":"'+responseData['emlTmpId']+'"}}';
				Ext.tzLoad(tzParams,function(resData){
					var EmlItemGrid = emlBkDetForm.down("grid[reference=emlTmplItemGrid]");
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
						emlBkDetForm.down('combobox[reference=emlTmpId]').disabled=true;
						emlBkDetForm.down('combobox[reference=emlTmpId]').addCls('readOnly-combox-BackgroundColor');
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
