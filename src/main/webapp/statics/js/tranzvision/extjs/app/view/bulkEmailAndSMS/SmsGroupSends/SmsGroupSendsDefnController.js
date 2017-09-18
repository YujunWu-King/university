/**
 * 功能：邮件群发定义controller
 */
Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsDefnController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.SmsGroupSendsDefnController',

    /**
     * 功能：一般发送
     */
    norSend:function (t, newValue, oldValue, eOpts) {			
        if (newValue == true) {
            Ext.MessageBox.confirm('提示', '切换发送模式将会清除【收件人】【短信模版】【短信内容】等信息，您确定要切换吗?', function(btnId){
                if(btnId == 'yes'){
                    t.findParentByType('form').child('tagfield[reference=receverTagField]').setEditable(true);
                    t.findParentByType('form').child('tagfield[reference=receverTagField]').disabled = false;
                    t.findParentByType('form').child('toolbar[reference=receverToolbar]').child('button[reference=addAudienceBtn]').disabled = false;
                    t.findParentByType('form').child('toolbar[reference=receverToolbar]').child('button[reference=addAudienceBtn]').removeCls('x-item-disabled x-btn-disabled');
                    t.findParentByType('form').child('toolbar[reference=receverToolbar]').child('button[reference=pasteFromExcelBtn]').disabled=false;
					t.findParentByType('form').child('toolbar[reference=receverToolbar]').child('button[reference=selectStuBtn]').disabled=false;
					t.findParentByType('form').child('toolbar[reference=receverToolbar]').child('button[reference=selectStuBtn]').removeCls('x-item-disabled x-btn-disabled');
					
                    t.findParentByType('form').down('combobox[reference=smsTmpId]').disabled = false;
                    t.findParentByType('form').down('tagfield[reference=receverTagField]').removeCls('readOnly-tagfield-BackgroundColor');
                    t.findParentByType('form').down('combobox[reference=smsTmpId]').removeCls('readOnly-combox-BackgroundColor');
                    this.changeSendModelClearData(t);
                }else{
                    t.setValue(false);
                    t.findParentByType('form').down('radio[reference=sendModelExc]').removeListener('change','excSend');
                    t.findParentByType('form').down('radio[reference=sendModelExc]').setValue(true);
                    t.findParentByType('form').down('radio[reference=sendModelExc]').addListener('change','excSend');
                }
            },this);
        }
    },
    /**
     * 功能：导入Excel发送
     * 刘阳阳  2016-01-15
     */
    excSend:function (t, newValue, oldValue, eOpts) {
        if (newValue == true) {
            Ext.MessageBox.confirm('提示', '切换发送模式将会清除【收件人】【短信模版】【短信内容】等信息，您确定要切换吗?', function(btnId){
                if (btnId == 'yes') {
                    t.findParentByType('form').child('tagfield[reference=receverTagField]').setEditable(false);
                    t.findParentByType('form').child('tagfield[reference=receverTagField]').disabled=true;
                    t.findParentByType('form').child('toolbar[reference=receverToolbar]').child('button[reference=addAudienceBtn]').disabled=true;
                    t.findParentByType('form').child('toolbar[reference=receverToolbar]').child('button[reference=addAudienceBtn]').addCls('x-item-disabled x-btn-disabled');
                    t.findParentByType('form').child('toolbar[reference=receverToolbar]').child('button[reference=pasteFromExcelBtn]').disabled=true;
					
					t.findParentByType('form').child('toolbar[reference=receverToolbar]').child('button[reference=selectStuBtn]').disabled=true;
					t.findParentByType('form').child('toolbar[reference=receverToolbar]').child('button[reference=selectStuBtn]').addCls('x-item-disabled x-btn-disabled');
					
                    t.findParentByType('form').down('combobox[reference=smsTmpId]').disabled=true;
                    t.findParentByType('form').down('tagfield[reference=receverTagField]').addCls('readOnly-tagfield-BackgroundColor');
                    t.findParentByType('form').down('combobox[reference=smsTmpId]').addCls('readOnly-combox-BackgroundColor');
                    this.changeSendModelClearData(t);
                }else{
                    t.setValue(false);
                    t.findParentByType('form').down('radio[reference=sendModelNor]').removeListener('change','norSend');
                    t.findParentByType('form').down('radio[reference=sendModelNor]').setValue(true);
                    t.findParentByType('form').down('radio[reference=sendModelNor]').addListener('change','norSend');
                }
            },this)
        }
    },
    /**
     * 功能：切换发送模式清除数据
     * 刘阳阳 2016-01-15
     */
    changeSendModelClearData:function(t){
        var form = t.findParentByType('form');
        var formdata = {
            "recever":"",
            "smsTmpId":"",
            "smsCont":""
        };
        form.getForm().setValues(formdata);
        form.down('grid[reference=smsTmplItemGrid]').store.removeAll();
        form.down('button[reference=setSmsTmpl]').disabled=true;
		form.down('button[reference=setSmsTmpl]').addCls('disabled-button-color');
        t.findParentByType('form').child('tagfield[reference=receverTagField]').clearValue();
    },
    /*==========================================+
     ++ 功能描述：导入Excel							+
     ++ 张浪，2016-01-14							+
     +==========================================*/
    importFromExcel: function(btn){
        var form = btn.findParentByType("form");
        var picID = form.getForm().findField("smsQfId").getValue();

        var className = 'KitchenSink.view.bulkEmailAndSMS.ImportExcel.smsEmlImportExcelWindow';
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        var ViewClass = Ext.ClassManager.get(className);
        var win = new ViewClass({
            sendPicId: picID,
            sendType:'SMS',
            callback: function(receverArr,smsQm,smsContent,paramItemZwfArr){
                //设置收件人
                form.down("tagfield[reference=receverTagField]").addValue(receverArr);
                //设置短信签名
                //form.getForm().findField("smsQm").setValue(smsQm);
                //设置短信内容
                form.getForm().findField("smsCont").setValue(smsContent);
                //设置信息项占位符
                form.down('grid[reference=smsTmplItemGrid]').store.removeAll(true);
                form.down('grid[reference=smsTmplItemGrid]').store.add(paramItemZwfArr);

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
    /**
     * 功能：收件人Change
     * 刘阳阳  2016-01-15
     */
    receverChange:function(field,newValue, oldValue){
        var smsBkDetForm = field.findParentByType('form');
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
            if(!smsBkDetForm.down('fieldset[reference=sendModelSet]').child('radio[reference=sendModelExc]').checked){
                smsBkDetForm.down('combobox[reference=smsTmpId]').disabled=false;
                smsBkDetForm.down('combobox[reference=smsTmpId]').removeCls('readOnly-combox-BackgroundColor');
            }
            */
        }else{
            if(newValue.length>oldValue.length){
                var newinput= arrNewValue[arrNewValue.length-1];
                if (newinput!="") {
                    var SMSReg = /^1\d{10}$/;
                    if (!SMSReg.test(newinput)){
                        var config = {
                            title: "提示",
                            msg: "请输入正确的手机号码.",
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
            if(!smsBkDetForm.down('fieldset[reference=sendModelSet]').child('radio[reference=sendModelExc]').checked){
				/*
                if(smsBkDetForm.down('combobox[reference=smsTmpId]').value==""||smsBkDetForm.down('combobox[reference=smsTmpId]').value==null){
                    smsBkDetForm.down('combobox[reference=smsTmpId]').disabled=true;
                    smsBkDetForm.down('combobox[reference=smsTmpId]').addCls('readOnly-combox-BackgroundColor');
                }
				*/
            	/*
				var hasPhoneAdd = false;
				for(var i=0; i<arrNewValue.length; i++){
					var phoneReg = /^1\d{10}$/;	
					if (phoneReg.test(arrNewValue[i])){
						hasPhoneAdd = true;
						break;
					}
				}
				
				if(hasPhoneAdd) {
					smsBkDetForm.down('combobox[reference=smsTmpId]').disabled=true;
                    smsBkDetForm.down('combobox[reference=smsTmpId]').addCls('readOnly-combox-BackgroundColor');	
				}else{
					smsBkDetForm.down('combobox[reference=smsTmpId]').disabled=false;
                    smsBkDetForm.down('combobox[reference=smsTmpId]').removeCls('readOnly-combox-BackgroundColor');
				}
				*/
            }
        }
    },
	
	/**
     * 功能：选择教职员
     * 张浪  2016-08-18
     */
    /*
	selectStaff: function(btn){
		var smsBkDetForm = btn.findParentByType('smsGroupDet').child('form');
		var receverField = smsBkDetForm.child('tagfield[reference="receverTagField"]');
		var arrAddData = [];
		
		Ext.tzShowPersonnelSelector({
			selModel: 'M', 
			callback: function(personInfoArr){
				for(var i=0; i<personInfoArr.length; i++){
					var phoneNum = personInfoArr[i].mobile;
					var PhoneNumReg = /^1\d{10}$/;
					if (PhoneNumReg.test(phoneNum)){
						arrAddData.push(phoneNum);
					}
				}
				if(arrAddData.length>0) receverField.addValue(arrAddData);
			}	
		});
	},
	*/
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
		var smsBkDetForm = btn.findParentByType('smsGroupDet').child('form');
		var receverField = smsBkDetForm.child('tagfield[reference="receverTagField"]');
		var arrAddData = [];
		
		for(var i=0; i<selList.length; i++){
			var phoneNum = selList[i].data.phone;
			var PhoneNumReg = /^1\d{10}$/;
			if (PhoneNumReg.test(phoneNum)){
				arrAddData.push(phoneNum);
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
                if (selection.length>0){
                    for(j=0;j<selection.length;j++){
                        addAudirec="";
                        addAudirec = {"id":selection[j].data.TZ_AUD_ID,"desc":selection[j].data.TZ_AUD_NAME};
                        arrAddAudience.push(addAudirec);
                        arrAddAudiValue.push(selection[j].data.TZ_AUD_ID);
                    };
                    var SmsGroupDetForm = btn.findParentByType('smsGroupDet').child('form');
                    var storereceive=SmsGroupDetForm.child('tagfield[reference="receverTagField"]').getStore();
                    storereceive.add(arrAddAudience);
                    SmsGroupDetForm.down('tagfield[reference="receverTagField"]').removeListener('change','receverChange');
                    SmsGroupDetForm.child('tagfield[reference="receverTagField"]').addValue(arrAddAudiValue);
                    SmsGroupDetForm.down('tagfield[reference="receverTagField"]').addListener('change','receverChange');

                    SmsGroupDetForm.child('toolbar[reference=receverToolbar]').child('button[reference=clearAllBtn]').disabled=false;
                    SmsGroupDetForm.child('toolbar[reference=receverToolbar]').child('button[reference=clearAllBtn]').removeCls('x-item-disabled x-btn-disabled');
                }
            }
        })
    },
    /**
     * 功能：清除所有
     * 刘阳阳 2016-01-05
     */
    clearAll:function(btn){
        var SmsGroupDetForm = btn.findParentByType('form');
        if(SmsGroupDetForm.down('radio[reference=sendModelExc]').checked){
            var smsQfId=SmsGroupDetForm.down('textfield[name="smsQfId"]').value;
            var tzParams = '{"ComID":"TZ_SMSQ_COM","PageID":"TZ_SMSQ_DET_STD","OperateType":"clearAll","comParams":{"smsQfId":"'+smsQfId+'"}}';
            Ext.tzLoad(tzParams,function(responseData){
                SmsGroupDetForm.getForm().setValues(responseData);
            });

            SmsGroupDetForm.down('grid[reference=smsTmplItemGrid]').store.removeAll(true);
        }else{
            SmsGroupDetForm.down('tagfield[reference=receverTagField]').store.removeAll(true);
            SmsGroupDetForm.down('tagfield[reference=receverTagField]').setValue("");
        }
        btn.addCls('x-item-disabled x-btn-disabled');
    },
    /**
     * 功能：从Excel粘贴
     * 刘阳阳  2015-01-05
     */
    pasteFromExcel:function(btn){
        var className='KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.pasteFromExcelWinSMS';
        Ext.syncRequire(className);
        ViewClass = Ext.ClassManager.get(className);
        var win = new ViewClass();
        this.getView().add(win);
        win.show();
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
            var SMSReg = /^1\d{10}$/;
			var phoneNum = Ext.String.trim(columnsData[i]);
            if (!SMSReg.test(phoneNum) && phoneNum != ""){
                isLeagalFlag="Y";
                irLeagalStr=i;
                break;
            }else{
                if(phoneNum != "") arrAddData.push(phoneNum);
            };
        }
        if (isLeagalFlag=="Y"){
            Ext.Msg.alert("提示","请输入手机号码.(第"+(irLeagalStr+1)+"行数据不合法.)");
        }else{
            var emailBulkDetForm = btn.findParentByType('smsGroupDet').child('form');
            emailBulkDetForm.child('tagfield[reference="receverTagField"]').addValue(arrAddData);
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
     * 功能：设置短信模版
     * 刘阳阳  2016-01-07
     */
    setSmsTmpl:function(btn){
        Ext.tzSetCompResourses("TZ_SMS_TMPL_MG_COM");

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SMS_TMPL_MG_COM"]["TZ_SMS_TMPL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_SMS_TMPL_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        //   className = 'KitchenSink.view.sendEmailAndSMS.emailServer.emailServerInfo';
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

        var smsTmpId = btn.findParentByType('form').down('combobox[reference=smsTmpId]').getValue();

        cmp.on('afterrender',function(panel){
            //组件注册表单信息;
            var form = panel.child('form').getForm();
            form.findField("smstempid").setReadOnly(true);
            form.findField("smstemporg").setReadOnly(true);
            form.findField("smstempid").addCls("lanage_1");;
            form.findField("smstemporg").addCls("lanage_1");

            //加载短信模版信息
            var tzParams = '{"ComID":"TZ_SMSQ_COM","PageID":"TZ_SMSQ_DET_STD","OperateType":"getSmsMetaTmpInfo","comParams":{"smsTmpId":"'+smsTmpId+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                var smstemporg=responseData['smstemporg'];
                var metaempid=responseData['metaempid'];
                tzParams ='{"ComID":"TZ_SMS_TMPL_MG_COM","PageID":"TZ_SMS_TMPL_STD","OperateType":"QF","comParams":{"smstempid":"'+smsTmpId+'","smstemporg":"'+smstemporg+'"}}';
                Ext.tzLoad(tzParams,function(responseData){
                    form.setValues(responseData);
                    //信息项数据
                    var	smsTmplItemGrid = panel.down('grid[name=smsTmplItemGrid]');
                    var tzStoreParamsItem = "{'restempid':'"+metaempid+"'}";
                    smsTmplItemGrid.store.tzStoreParams = tzStoreParamsItem;
                    smsTmplItemGrid.store.load();
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
    insertSmsitem: function(grid,rowIndex, colIndex){
        var smsbkdefForm = grid.findParentByType('form');
        var rec = grid.getStore().getAt(rowIndex);
        var parainfoitem = rec.get('parainfoitem');

        var form = this.getView().child("form").getForm();
        var dom = form.findField("smsCont").inputEl.dom;
        this.insertAtCursor(dom,parainfoitem);

        var smscont = smsbkdefForm.down('textareafield[name=smsCont]').value;
        smsbkdefForm.down('textareafield[name=smsCont]').setValue(smscont);
    },
    insertAtCursor: function(myField, myValue) {
        //IE support
        if (document.selection) {
            myField.focus();
            sel = document.selection.createRange();
            sel.text = myValue;
            sel.select();
        }
        //MOZILLA/NETSCAPE support
        else if (myField.selectionStart || myField.selectionStart == '0') {

            var startPos = myField.selectionStart;
            var endPos = myField.selectionEnd;
            // save scrollTop before insert www.keleyi.com
            var restoreTop = myField.scrollTop;
            myField.value = myField.value.substring(0, startPos) + myValue + myField.value.substring(endPos, myField.value.length);
            if (restoreTop > 0) {
                myField.scrollTop = restoreTop;
            }
            myField.focus();
            myField.selectionStart = startPos + myValue.length;
            myField.selectionEnd = startPos + myValue.length;
        } else {
            myField.value += myValue;
            myField.focus();
        }
    },
    /**
     * 功能：获取提交参数
     */
    getSubmitPar:function(btn){
        var smsbkdefPanel = btn.up('panel');
        var smsbkdefForm = smsbkdefPanel.child('form');
        var formdata =Ext.JSON.encode(smsbkdefForm.getValues());
        var formrec = smsbkdefForm.getForm().getFieldValues();
        var msgInfo="";
		if(formrec["smsQfDesc"]==""||formrec["smsQfDesc"]==null){
            Ext.Msg.alert("提示","请填写群发任务名称.");
            return;
        }
        var receverValue=smsbkdefForm.child('tagfield[reference="receverTagField"]').value;
        if(!receverValue||receverValue==""){
            Ext.Msg.alert("提示","收件人不能为空.");
            return;
        };
        if(smsbkdefForm.down('checkbox[reference=tsfsFlag]').checked){
            var tsfsPhone=smsbkdefForm.down('textfield[name=tsfsPhone]').value;
            if(tsfsPhone==""){
                Ext.Msg.alert("提示","同时发送给手机不能为空.");
                return;
            }else{
				/*
                var SMSReg = /^1\d{10}$/;
                if(!SMSReg.test(tsfsPhone)){
                    Ext.Msg.alert("提示","同时发送给手机格式不正确.");
                    return;
                }*/
            }
        };
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
            msgInfo='短信已存至草稿箱中，将会于 '+Ext.Date.format(formrec['dsfsDate'], 'Y-m-d  ')+Ext.Date.format(formrec['dsfsTime'], 'H:i')+' 发送';
        }
        var othInfo = '{"crePer":"' + formrec["crePer"] + '","dept":"' + formrec["dept"] + '","setId":"' + formrec["setId"] + '","recever":"' + receverValue + '"}';
        var comParams = "";
        comParams = '"formdata":' + formdata + ',"othInfo":' + othInfo + '';

        var dsfadt = Ext.Date.format(formrec['dsfsDate'], 'Y-m-d')+"  "+Ext.Date.format(formrec['dsfsTime'], 'H:i');
        comParams=comParams+"&"+formrec["dsfsFlag"]+"&"+msgInfo+"&"+dsfadt;

        return comParams;
    },
    /**
     * 功能：保存
     * 刘阳阳  2016-01-07
     */
    onPanelSave:function(btn){
        var strRet=this.getSubmitPar(btn);
             arrRet = strRet.split('&');
        var smsbkdefForm = btn.up('panel').child('form');
		/*表单验证*/
		if(!smsbkdefForm.getForm().isValid()) return;
        var tzParams = '{"ComID":"TZ_SMSQ_COM","PageID":"TZ_SMSQ_DET_STD","OperateType":"save","comParams":{'+arrRet[0]+'}}';
        Ext.tzSubmit(tzParams,function(responseData){
            smsbkdefForm.getForm().setValues(responseData);
            smsbkdefForm.down('displayfield[name=creDt]').setVisible(true);
            if(arrRet[1]!="false"){
                var dsfsInfo=arrRet[3];
                dsfsInfo='<span style="color:red">短信将会于'+dsfsInfo+'发送</span>';
                smsbkdefForm.down('displayfield[name=dsfsInfo]').setValue(dsfsInfo);
                smsbkdefForm.down('displayfield[name=dsfsInfo]').setVisible(true);
            }else{
                smsbkdefForm.down('displayfield[name=dsfsInfo]').setVisible(false);
            }
        },arrRet[2],true,this);

        var arrSmsBkMgrPanel=Ext.ComponentQuery.query("SmsGroupSendsMg");
        for(var i=0;i<arrSmsBkMgrPanel.length;i++){
            arrSmsBkMgrPanel[i].down('grid').store.load();
        }
    },
    /**
     * 功能：发送
     * 刘阳阳  2016-01-18
     */
    sendSms:function(btn){
		var me = this;
        var strRet=this.getSubmitPar(btn),
            arrRet = strRet.split('&');
        if(arrRet[2]==""){
            arrRet[2]="群发任务已提交.";
        }
		var smsbkdefPanel = btn.up('panel');
        var smsbkdefForm = smsbkdefPanel.child('form');
		/*表单验证*/
		if(!smsbkdefForm.getForm().isValid()) return;
        Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.ensure","确认"),
            Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_DET_STD.ensureSendDesc","确认要发送短信?"), function(btnId) {
                if (btnId == 'yes') {
                    var tzParams = '{"ComID":"TZ_SMSQ_COM","PageID":"TZ_SMSQ_DET_STD","OperateType":"sendSms","comParams":{' + arrRet[0] + '}}';
                    Ext.tzSubmit(tzParams, function (responseData) {
                        smsbkdefForm.getForm().setValues(responseData);
                        smsbkdefForm.down('displayfield[name=creDt]').setVisible(true);
                        if(arrRet[1]!="false"){
                            var dsfsInfo=arrRet[3];
                            dsfsInfo='<span style="color:red">短信将会于'+dsfsInfo+'发送</span>';
                            smsbkdefForm.down('displayfield[name=dsfsInfo]').setValue(dsfsInfo);
                            smsbkdefForm.down('displayfield[name=dsfsInfo]').setVisible(true);
                        }else{
                            smsbkdefForm.down('displayfield[name=dsfsInfo]').setVisible(false);
                        }
                        if (responseData['rwzxZt']==""||responseData['rwzxZt']=="C"||responseData['rwzxZt']=="D") {

                        } else {
                            smsbkdefPanel.down('button[reference=saveBtn]').setDisabled(true);
                            smsbkdefPanel.down('button[reference=sendBtn]').setDisabled(true);
                            if (responseData['rwzxZt'] == "B") {
                                smsbkdefPanel.down('button[reference=revokeBtn]').setVisible(true);
                            }
                            smsbkdefPanel.getController().pageReadonly(smsbkdefForm);
                        }
                        if(responseData['rwzxZt']=="C"||responseData['rwzxZt']=="D"){
                            smsbkdefPanel.down('button[reference=viewHisBtn]').setDisabled(false);
                        }

						if(!smsbkdefForm.down('button[reference=copyHistoryBtn]').hidden){
							/*隐藏复制历史任务*/
							smsbkdefForm.down('button[reference=copyHistoryBtn]').setHidden(true);
						}
						
						/*重置表单修改状态*/
						var savedObject = me && me.getView && (typeof me.getView === "function") && me.getView();
						savedObject = savedObject || me;
						if(savedObject && savedObject.commitChanges && (typeof savedObject.commitChanges === "function")){
							savedObject.commitChanges(savedObject);
						}
                    }, arrRet[2], true, this);

                    var arrSmsBkMgrPanel=Ext.ComponentQuery.query("SmsGroupSendsMg");
                    for(var i=0;i<arrSmsBkMgrPanel.length;i++){
                        arrSmsBkMgrPanel[i].down('grid').store.load();
                    }
                }
            }
        );
    },
    /**
     * 功能：只读页面
     * 刘阳阳  2016-01-18
     */
    pageReadonly:function(form){
        form.down('textfield[name=smsQfDesc]').setReadOnly(true);
        form.down('textfield[name=smsQfDesc]').addCls('readOnly-combox-BackgroundColor');
        form.down('radio[reference=sendModelNor]').setReadOnly(true);
        form.down('radio[reference=sendModelExc]').setReadOnly(true);
        if(form.down('radio[reference=sendModelExc]').checked){
            form.down('button[reference=impExc]').disabled=true;
            form.down('button[reference=impExc]').addCls('x-item-disabled x-btn-disabled');
        };
        form.down('tagfield[reference=receverTagField]').setEditable(false);
        form.down('tagfield[reference=receverTagField]').disabled=true;
        form.down('tagfield[reference=receverTagField]').addCls('readOnly-tagfield-BackgroundColor');
        form.down('toolbar[reference=receverToolbar]').child('button[reference=addAudienceBtn]').disabled=true;
        form.down('toolbar[reference=receverToolbar]').child('button[reference=addAudienceBtn]').addCls('x-item-disabled x-btn-disabled');
        form.down('toolbar[reference=receverToolbar]').child('button[reference=clearAllBtn]').disabled=true;
        form.down('toolbar[reference=receverToolbar]').child('button[reference=clearAllBtn]').addCls('x-item-disabled x-btn-disabled');
        form.down('toolbar[reference=receverToolbar]').child('button[reference=pasteFromExcelBtn]').disabled=true;
		form.down('toolbar[reference=receverToolbar]').child('button[reference=selectStuBtn]').disabled=true;
		form.down('toolbar[reference=receverToolbar]').child('button[reference=selectStuBtn]').addCls('x-item-disabled x-btn-disabled');
		
        form.down('checkbox[name=tsfsFlag]').setReadOnly(true);
        form.down('textfield[name=tsfsPhone]').setReadOnly(true);
        form.down('textfield[name=tsfsPhone]').addCls('readOnly-combox-BackgroundColor');
        form.down('combobox[reference=smsTmpId]').disabled=true;
        form.down('combobox[reference=smsTmpId]').addCls('readOnly-combox-BackgroundColor');
        if(form.down('combobox[reference=smsTmpId]').getValue()!=""){
            form.down('button[reference=setSmsTmpl]').disabled=true;
            form.down('button[reference=setSmsTmpl]').addCls('x-item-disabled x-btn-disabled');
        };
        form.down('textfield[name=smsQm]').setReadOnly(true);
        form.down('textfield[name=smsQm]').addCls('readOnly-combox-BackgroundColor');
        form.down('textareafield[name=smsCont]').setReadOnly(true);
        form.down('textareafield[name=smsCont]').addCls('readOnly-textarea-BackgroundColor');
        form.down('grid[name=smsTmplItemGrid]').setDisabled(true);
        form.down('checkbox[name=dsfsFlag]').setReadOnly(true);
        if(form.down('checkbox[name=dsfsFlag]').checked){
            form.down('datefield[name=dsfsDate]').setReadOnly(true);
            form.down('datefield[name=dsfsDate]').addCls('readOnly-combox-BackgroundColor');
            form.down('timefield[name=dsfsTime]').setReadOnly(true);
            form.down('timefield[name=dsfsTime]').addCls('readOnly-combox-BackgroundColor');
        };
        /*
		form.down('checkbox[name=transmitFlag]').setReadOnly(true);
		if(form.down('checkbox[name=transmitFlag]').checked){
			form.down('tagfield[reference=transPhoneNumsTagField]').setEditable(false);
			form.down('tagfield[reference=transPhoneNumsTagField]').disabled=true;
			form.down('tagfield[reference=transPhoneNumsTagField]').addCls('readOnly-tagfield-BackgroundColor');
        };
        */
    },
    /**
     * 功能：控制页面字段
     */
    pageFiledsDisControl:function(form){
        form.down('textfield[name=smsQfDesc]').setReadOnly(false);
        form.down('textfield[name=smsQfDesc]').removeCls('readOnly-combox-BackgroundColor');
        form.down('radio[reference=sendModelNor]').setReadOnly(false);
        form.down('radio[reference=sendModelExc]').setReadOnly(false);
        if(form.down('radio[reference=sendModelExc]').checked){
            form.down('button[reference=impExc]').disabled=false;
            form.down('button[reference=impExc]').removeCls('x-item-disabled x-btn-disabled');
        };
        if (!form.down('radio[reference=sendModelExc]').checked){
            form.down('toolbar[reference=receverToolbar]').child('button[reference=addAudienceBtn]').disabled=false;
            form.down('toolbar[reference=receverToolbar]').child('button[reference=addAudienceBtn]').removeCls('x-item-disabled x-btn-disabled');
            if(form.down('combobox[reference=smsTmpId]').getValue()==""||form.down('combobox[reference=smsTmpId]').getValue()==null){
                form.down('tagfield[reference=receverTagField]').setEditable(true);
                form.down('tagfield[reference=receverTagField]').disabled=false;
                form.down('tagfield[reference=receverTagField]').removeCls('readOnly-tagfield-BackgroundColor');
				/*
                form.down('toolbar[reference=receverToolbar]').child('button[reference=pasteFromExcelBtn]').disabled=true;
				
				form.down('toolbar[reference=receverToolbar]').child('button[reference=selectStuBtn]').disabled=true;
				form.down('toolbar[reference=receverToolbar]').child('button[reference=selectStuBtn]').addCls('x-item-disabled x-btn-disabled');
				*/
            }
        }
        if(form.down('tagfield[reference=receverTagField]').getValue!=""){
            form.down('toolbar[reference=receverToolbar]').child('button[reference=clearAllBtn]').disabled=false;
            form.down('toolbar[reference=receverToolbar]').child('button[reference=clearAllBtn]').removeCls('x-item-disabled x-btn-disabled');
        }
        form.down('checkbox[name=tsfsFlag]').setReadOnly(false);
        form.down('textfield[name=tsfsPhone]').setReadOnly(false);
        form.down('textfield[name=tsfsPhone]').removeCls('readOnly-combox-BackgroundColor');
        if (!form.down('radio[reference=sendModelExc]').checked&&form.down('tagfield[reference=receverTagField]').getValue=="") {
            form.down('combobox[reference=smsTmpId]').disabled = false;
            form.down('combobox[reference=smsTmpId]').removeCls('readOnly-combox-BackgroundColor');
        }
        if(form.down('combobox[reference=smsTmpId]').getValue()=="" || form.down('combobox[reference=smsTmpId]').getValue()== null){
			form.down('button[reference=setSmsTmpl]').disabled=true;
            form.down('button[reference=setSmsTmpl]').addCls('disabled-button-color');
		}else{
			form.down('button[reference=setSmsTmpl]').disabled=false;
            form.down('button[reference=setSmsTmpl]').removeCls('disabled-button-color');
        };
        form.down('textfield[name=smsQm]').setReadOnly(false);
        form.down('textfield[name=smsQm]').removeCls('readOnly-combox-BackgroundColor');
        form.down('textareafield[name=smsCont]').setReadOnly(false);
        form.down('textareafield[name=smsCont]').removeCls('readOnly-textarea-BackgroundColor');
        form.down('grid[name=smsTmplItemGrid]').setDisabled(false);
        form.down('checkbox[name=dsfsFlag]').setReadOnly(false);
        if(form.down('checkbox[name=dsfsFlag]').checked){
            form.down('datefield[name=dsfsDate]').setReadOnly(false);
            form.down('datefield[name=dsfsDate]').removeCls('readOnly-combox-BackgroundColor');
            form.down('timefield[name=dsfsTime]').setReadOnly(false);
            form.down('timefield[name=dsfsTime]').removeCls('readOnly-combox-BackgroundColor');
        };
    },
    /**
     * 功能：中断发送
     * 刘阳阳 2015-01-12
     */
    interSend:function(btn){
        var smsbkdefPanel = btn.up('panel');
        var smsbkdefForm = smsbkdefPanel.child('form');
        var formrec = smsbkdefForm.getForm().getFieldValues();
        var comParams = '"smsQfId":'+formrec['smsQfId']+'';
        var tzParams = '{"ComID":"TZ_SMSQ_COM","PageID":"TZ_SMSQ_DET_STD","OperateType":"revoke","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(responseData){
            smsbkdefPanel.getController().pageFiledsDisControl(smsbkdefForm);
            smsbkdefPanel.down('button[reference=revokeBtn]').setVisible(false);
            smsbkdefPanel.down('button[reference=saveBtn]').setDisabled(false);
            smsbkdefPanel.down('button[reference=sendBtn]').setDisabled(false);
        },"",true,this);
    },
    /**
     * 功能：关闭
     * 刘阳阳  2015-12-31
     */
    onPanelClose: function(btn){
        var win = btn.findParentByType("panel");
        win.close();
    },

    /*王耀:预览发送邮件
     2016.1.12 */
    /*王耀:预览发送邮件
     2016.1.12 */

    preViewSms:function(btn){
        var form = btn.findParentByType("panel").child("form");
        var sendPcId=form.getForm().findField("smsQfId").getValue();//群发任务ID

        //手动输入的邮箱
        var keyInputEmail=form.getForm().findField("receverOrigin").getValue(),
            audIDTotal=form.down('tagfield[reference="receverTagField"]').getValue();//添加听众
        if(audIDTotal=="" ||audIDTotal==undefined ){
            Ext.MessageBox.alert('提示', '收件人为空');
            return;
        }
        var ShiJiEmail="";
        var Audience="";
        var AudienceEmail="";
        var AudienceOprID="";
        //获取发送模式，若为true则是一般发送，false则是导入excel发送;
        var sendType=form.getForm().findField("sendModel").getValue();
        if (sendType==true) {
            //如果为true则是一般发送，分为邮箱和听众;
            var SMSReg = /^1\d{10}$/;//校验是否为手机号
            for (var i = 0; i < audIDTotal.length; i++) {
                if (SMSReg.test(audIDTotal[i])) {
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

            var tzTotalAudienceParams ='{"ComID":"TZ_SMSQ_PREVIEW_COM","PageID":"TZ_SMSQ_VIEW_STD",' +
                '"OperateType":"checkSmsAudience","comParams":{"type":"checkSmsAudience","totalAudience":"'+Audience+'"}}';

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
       var tsfsFlag = form.getForm().findField("tsfsFlag").getValue();
       if(tsfsFlag){
	        var tsfsEmail=form.getForm().findField("tsfsPhone").getValue();
	        if (tsfsEmail!=""){
				tsfsEmail = tsfsEmail.replace(/;/g,',');
				ShiJiEmail=ShiJiEmail+','+tsfsEmail
			}
       }

        var emlTmpId=form.getForm().findField("smsTmpId").getValue(),//邮件模板
            emailTheme=form.getForm().findField("smsQm").getValue(),//邮件签名
            emailModal=form.getForm().findField("smsTmpId").getValue(),//邮件模板
        // emailContent=form.getForm().findField("emlCont").getValue();//邮件内容
            emailContent= Ext.util.Format.htmlEncode(form.getForm().findField("smsCont").getValue());
        //是否有访问权限
        Ext.tzSetCompResourses("TZ_SMSQ_PREVIEW_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SMSQ_PREVIEW_COM"]["TZ_SMSQ_VIEW_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_SMSQ_VIEW_STD，请检查配置。');
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
            var formParams={"type":"previewSMS",
                "sendPcId":sendPcId,
                "sendType":sendType,
                "viewNumber":"1",
                "keyInputEmail":ShiJiEmail,
                "audIDTotal":Audience,
                "emlTmpId":emlTmpId,
                "emailTheme":emailTheme,
                "emailModal":emailModal,
                "emailContent":emailContent}

            var tzParams = '{"ComID":"TZ_SMSQ_PREVIEW_COM","PageID":"TZ_SMSQ_VIEW_STD","OperateType":"previewSMS","comParams":'+Ext.JSON.encode(formParams)+'}';
            Ext.tzLoad(tzParams,function(responseData){
                    var formData = responseData.formData;
                    formData.configuration = Ext.encode(configuration);
                    form.setValues(formData);
                    var htmlCom = panel.down("component[name=SmsContentHtml]");
                    htmlCom.getEl().update(Ext.util.Format.htmlDecode(formData.SmsContent));
                    htmlCom.updateLayout();
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
    viewSendHistory: function(btn){

        var form = btn.findParentByType("panel").child("form");
        var smsQfId=form.getForm().findField("smsQfId").getValue();//群发任务ID

        Ext.tzSetCompResourses("TZ_SMSQ_VIEWTY_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SMSQ_VIEWTY_COM"]["TZ_SMSQ_VIEWTY_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_SMSQ_VIEWTY_STD，请检查配置。');
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
//        var configuration ={
//            //模板ID;
//            "senderEmail": senderEmail,
//            "keyInputEmail": ShiJiEmail,
//            "audIDTotal": Audience,
//            "emailModal":emailModal,
//            "emailTheme": emailTheme,
//            "emailContent": emailContent
//
//        }
        cmp = new ViewClass();

        cmp.on('afterrender',function(panel){

            var store=panel.getStore();
            var tzStoreParams ='{"storeType":"history","smsQfID":"'+smsQfId+'"}';
            store.tzStoreParams = tzStoreParams;
            store.load({

            });
//            var tzParams = '{"ComID":"TZ_EMLQ_PREVIEW_COM","PageID":"TZ_EMLQ_VIEW_STD","OperateType":"previewSms","comParams":{"type":"previewSms","viewNumber":"1","senderEmail":"'+senderEmail+'","keyInputEmail":"'+ShiJiEmail+'","audIDTotal":"'+Audience+'","emailTheme":"'+emailTheme+'","emailModal":"'+emailModal+'","emailContent":"'+emailContent+'"}}';
//            var tzParams ="";
//            Ext.tzLoad(tzParams,function(responseData){
//
//                }
//            )
        });
        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
	/***复制历史任务内容到当前任务***/
	copyHistoryData: function(btn){
		var className='KitchenSink.view.bulkEmailAndSMS.copyHistory.copyFromHistoryWin';
		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}
		ViewClass = Ext.ClassManager.get(className);
		var config = {
			taskType:"SMS"	
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
		hisGridStore.tzStoreParams = Ext.JSON.encode({"queryID": "myHistoryRw","taskType":"SMS","searchText":searchContent});
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
		
		var smsPanel = this.getView();
		var SmsGroupForm = smsPanel.child("form");
		
		var currSmsQfId = smsPanel.BulkTaskId;
		
		var par = '{"smsQfId": "'+qfRwId+'","queryID": "recever"}';
		var receverStore = SmsGroupForm.down("tagfield[reference=receverTagField]").getStore();
        receverStore.tzStoreParams=par;
		receverStore.load();
			
		var tzParams = '{"ComID":"TZ_SMSQ_COM","PageID":"TZ_SMSQ_DET_STD","OperateType":"getHistoryRwInfo","comParams":{"smsQfId":"'+qfRwId+'","currSmsQfId":"'+currSmsQfId+'"}}';
		Ext.tzLoad(tzParams,function(responseData){
			SmsGroupForm.down('radio[reference="sendModelNor"]').removeListener('change','norSend');
			SmsGroupForm.down('radio[reference="sendModelExc"]').removeListener('change','excSend');
			SmsGroupForm.down('tagfield[reference="receverTagField"]').removeListener('change','receverChange');
			SmsGroupForm.getForm().setValues(responseData);

			smsPanel.BulkTaskId = SmsGroupForm.down('textfield[name=smsQfId]').getValue();
			if (SmsGroupForm.down('radio[reference="sendModelExc"]').checked) {
				SmsGroupForm.down('button[reference=setSmsTmpl]').disabled=false;

				var tzParams = '{"ComID":"TZ_SMSQ_COM","PageID":"TZ_SMSQ_DET_STD","OperateType":"getSmsTmpItem","comParams":{"smsQfId":"'+qfRwId+'","SmsTmpId":"'+responseData['smsTmpId']+'"}}';
				Ext.tzLoad(tzParams,function(resData){
					var SmsItemGrid = SmsGroupForm.down("grid[reference=smsTmplItemGrid]");
					var SmsItemStore = SmsItemGrid.getStore();
					
					Ext.suspendLayouts();
					SmsItemStore.suspendEvents();
					
					SmsItemStore.removeAll(true);
					SmsItemStore.add(resData['root']);
					SmsItemStore.commitChanges();
					
					SmsItemStore.resumeEvents();
					SmsItemGrid.reconfigure(SmsItemStore);
					Ext.resumeLayouts(true);

					var userAgent = navigator.userAgent;
					if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1) {
						var copyItemsDom = document.getElementsByName("itememlCopy");
						for (var i=0;i<copyItemsDom.length;i++)
						{
							$(copyItemsDom[i]).zclip({
								beforeCopy:function(){
									var itemHtml = this.parentNode.parentNode.parentNode.innerHTML;
									var itemFirstCharPositon = itemHtml.indexOf("[");
									var itemLastCharPositon = itemHtml.indexOf("]");
									var itemPara = itemHtml.slice(itemFirstCharPositon,itemLastCharPositon+1);
									SmsGroupForm.down('textfield[name="copyfield"]').setValue(itemPara);
								},
								copy:function(){
									return SmsGroupForm.down('textfield[name="copyfield"]').getValue();
								}
							});
						}
					}
				});
			};
			
			if(responseData['recever'].length>0){
				SmsGroupForm.down('tagfield[reference=receverTagField]').setValue(responseData['recever']);
			    var len = responseData['recever'].length;
			    for(var ind=0;ind<len;ind++){
					var phoneNum = responseData['recever'][ind];
					var SMSReg = /^1\d{10}$/;
					if (SMSReg.test(phoneNum)){
						SmsGroupForm.down('combobox[reference=smsTmpId]').disabled=true;
						SmsGroupForm.down('combobox[reference=smsTmpId]').addCls('readOnly-combox-BackgroundColor');
					}
			   }
			}

			SmsGroupForm.down('radio[reference="sendModelNor"]').addListener('change','norSend');
			SmsGroupForm.down('radio[reference="sendModelExc"]').addListener('change','excSend');
			SmsGroupForm.down('tagfield[reference="receverTagField"]').addListener('change','receverChange');

			histortWin.close();
		});
	}
});
