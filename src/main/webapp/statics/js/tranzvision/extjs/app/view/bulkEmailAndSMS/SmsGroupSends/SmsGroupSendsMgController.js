/*===========================================================
 + 功能：短信群发管理controller
 + 开发人：张浪
 + 时间：2016-01-11
 *===========================================================*/
Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsMgController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.SmsGroupSendsMgController',

    /**
     * 功能：查询
     */
    querySmsGroupSends:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_SMSQ_COM.TZ_SMSQ_MGR_STD.TZ_SMSQ_LIST_V',
			condition:
            {
                "TZ_JG_ID": Ext.tzOrgID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    /**
     * 功能：新增
     */
    addSmsGroupSends: function(btn){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SMSQ_COM"]["TZ_SMSQ_DET_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_SMSQ_DET_STD，请检查配置。');
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
            var SmsGroupForm = panel.child('form');
            var receverStore = new KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsReceiverStore();
            var SmsTmplStore = new KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsSmsTmplStore();
            var SmsItemStore = new KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsSmsItemStore();
			var TransmitStore = new KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsTransmitStore();

            SmsGroupForm.down('grid[reference=smsTmplItemGrid]').setStore(SmsItemStore);
			SmsGroupForm.down('button[reference=copyHistoryBtn]').setHidden(false);/*显示历史复制按钮*/

            var par = '{"smsQfId": "","queryID": "recever"}';
            receverStore.tzStoreParams=par;
            receverStore.load({
                callback: function (records, options, success) {
                    SmsGroupForm.down('tagfield[reference=receverTagField]').setStore(receverStore);
                }
            });
			//SmsGroupForm.down('tagfield[reference=receverTagField]').setStore(receverStore);
			
            
			par="";
			SmsTmplStore.load({
				callback: function (records, options, success) {
					SmsGroupForm.down('combobox[reference=smsTmpId]').setStore(SmsTmplStore);
				}
			});
			//SmsGroupForm.down('combobox[reference=smsTmpId]').setStore(SmsTmplStore);
			
			/*
			TransmitStore.tzStoreParams = '{"smsQfId": "","queryID": "transmit"}';
			TransmitStore.load({
				callback: function(records, options, success){
					//SmsGroupForm.down('tagfield[reference=transPhoneNumsTagField]').setStore(TransmitStore);	
				}	
			});
			SmsGroupForm.down('tagfield[reference=transPhoneNumsTagField]').setStore(TransmitStore);	
*/
			
            var tzParams = '{"ComID":"TZ_SMSQ_COM","PageID":"TZ_SMSQ_DET_STD","OperateType":"getCreInfo","comParams":{}}';
            Ext.tzLoad(tzParams,function(responseData){
            	var smsForm = panel.child("form");
            	smsForm.down('tagfield[reference="receverTagField"]').removeListener('change','receverChange');
            	
                smsForm.getForm().setValues(responseData);
                panel.BulkTaskId = smsForm.down('textfield[name=smsQfId]').getValue();
                smsForm.down('tagfield[reference="receverTagField"]').addListener('change','receverChange');

                smsForm.child('toolbar[reference=receverToolbar]').child('button[reference=clearAllBtn]').disabled=true;
                smsForm.child('toolbar[reference=receverToolbar]').child('button[reference=clearAllBtn]').addCls('x-item-disabled x-btn-disabled');
				
				
				smsForm.child('tagfield[reference=receverTagField]').setEditable(false);
				smsForm.child('tagfield[reference=receverTagField]').disabled=true;
				smsForm.child('toolbar[reference=receverToolbar]').child('button[reference=addAudienceBtn]').disabled=true;
				smsForm.child('toolbar[reference=receverToolbar]').child('button[reference=addAudienceBtn]').addCls('x-item-disabled x-btn-disabled');
				smsForm.child('toolbar[reference=receverToolbar]').child('button[reference=pasteFromExcelBtn]').disabled=true;
				smsForm.child('toolbar[reference=receverToolbar]').child('button[reference=selectStuBtn]').disabled=true;
				smsForm.child('toolbar[reference=receverToolbar]').child('button[reference=selectStuBtn]').addCls('x-item-disabled x-btn-disabled');
				
				smsForm.down('combobox[reference=smsTmpId]').disabled=true;
				smsForm.down('tagfield[reference=receverTagField]').addCls('readOnly-tagfield-BackgroundColor');
				smsForm.down('combobox[reference=smsTmpId]').addCls('readOnly-combox-BackgroundColor');
				
				smsForm.down('button[reference=setSmsTmpl]').disabled = true;
				smsForm.down('button[reference=setSmsTmpl]').addCls('disabled-button-color');
				
				panel.commitChanges(panel);
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
     * 功能：编辑
     */
    editSmsTask: function(grid, rowIndex, colIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SMSQ_COM"]["TZ_SMSQ_DET_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_SMSQ_DET_STD，请检查配置。');
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

        var smsBulkGridRecord = grid.store.getAt(rowIndex);
        var smsQfId = smsBulkGridRecord.data.smsQfId;

        cmp.on('afterrender',function(panel){
            var SmsGroupForm = panel.child('form');
            var receverStore = new KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsReceiverStore();
            var SmsTmplStore = new KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsSmsTmplStore();
            var SmsItemStore = new KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsSmsItemStore();
			var TransmitStore = new KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsTransmitStore();

            SmsGroupForm.down('grid[reference=smsTmplItemGrid]').setStore(SmsItemStore);

            var myMask = new Ext.LoadMask({
                msg    : '加载中...',
                target : Ext.getCmp('tranzvision-framework-content-panel')
            });

            myMask.show();
			/*
			TransmitStore.tzStoreParams = '{"smsQfId": "'+smsQfId+'","queryID": "transmit"}';
			TransmitStore.load({
				callback: function(){
					//SmsGroupForm.down('tagfield[reference=transPhoneNumsTagField]').setStore(TransmitStore);	
				}	
			});
			SmsGroupForm.down('tagfield[reference=transPhoneNumsTagField]').setStore(TransmitStore);	
*/
            var par = '{"smsQfId": "'+smsQfId+'","queryID": "recever"}';
            receverStore.tzStoreParams=par;
            receverStore.load({
                callback: function (records, options, success) {
                    SmsGroupForm.down('tagfield[reference=receverTagField]').setStore(receverStore);
                    SmsTmplStore.load({
                        callback: function (records, options, success) {
                            SmsGroupForm.down('combobox[reference=smsTmpId]').setStore(SmsTmplStore);
                            var tzParams = '{"ComID":"TZ_SMSQ_COM","PageID":"TZ_SMSQ_DET_STD","OperateType":"QF","comParams":{"smsQfId":"'+smsQfId+'"}}';
                            Ext.tzLoad(tzParams,function(responseData){
                                SmsGroupForm.down('radio[reference="sendModelNor"]').removeListener('change','norSend');
                                SmsGroupForm.down('tagfield[reference="receverTagField"]').removeListener('change','receverChange');
                                SmsGroupForm.getForm().setValues(responseData);
								if(responseData.dsfsTime == ""){
									SmsGroupForm.down('timefield[name=dsfsTime]').setValue('00:00'); /*解决报时间无效错误*/		
								}
								
                                panel.BulkTaskId = SmsGroupForm.down('textfield[name=smsQfId]').getValue();
                                //if (responseData['smsTmpId']!=""||SmsGroupForm.down('radio[reference="sendModelExc"]').checked) {
                                if (SmsGroupForm.down('radio[reference="sendModelExc"]').checked) {
                                    SmsGroupForm.down('button[reference=setSmsTmpl]').disabled=false;

                                    var tzParams = '{"ComID":"TZ_SMSQ_COM","PageID":"TZ_SMSQ_DET_STD","OperateType":"getSmsTmpItem","comParams":{"smsQfId":"'+smsQfId+'","SmsTmpId":"'+responseData['smsTmpId']+'"}}';
                                    Ext.tzLoad(tzParams,function(responseData){
                                        SmsItemStore.add(responseData['root']);
                                        SmsItemStore.commitChanges();

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
                                                        //form.findField("copyfield").setValue(itemPara);
                                                        SmsGroupForm.down('textfield[name="copyfield"]').setValue(itemPara);
                                                    },
                                                    copy:function(){
                                                        //return form.findField("copyfield").getValue();
                                                        return SmsGroupForm.down('textfield[name="copyfield"]').getValue();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                };

                                if(responseData['dsfsInfo']!=""){
                                    SmsGroupForm.down('displayfield[name=dsfsInfo]').setVisible(true);
                                }
                                SmsGroupForm.down('displayfield[name=creDt]').setVisible(true);
                                if (responseData['rwzxZt']==""||responseData['rwzxZt']=="D"||responseData['rwzxZt']=="E"){
                                    if(responseData['recever'].length>0){
                                        SmsGroupForm.down('tagfield[reference="receverTagField"]').removeListener('change','receverChange');
                                        SmsGroupForm.down('tagfield[reference=receverTagField]').setValue(responseData['recever']);
                                        SmsGroupForm.down('tagfield[reference="receverTagField"]').addListener('change','receverChange');


									    var arrRecever = responseData['recever'];
									    /*
                                       	var hasPhoneAdd = false;
										for(var i=0; i<arrRecever.length; i++){
											var phoneReg = /^1\d{10}$/;	
											if (phoneReg.test(arrRecever[i])){
												hasPhoneAdd = true;
												break;
											}
										}
										if(hasPhoneAdd) {
											SmsGroupForm.down('combobox[reference=smsTmpId]').disabled=true;
											SmsGroupForm.down('combobox[reference=smsTmpId]').addCls('readOnly-combox-BackgroundColor');	
										}else{
											SmsGroupForm.down('combobox[reference=smsTmpId]').disabled=false;
											SmsGroupForm.down('combobox[reference=smsTmpId]').removeCls('readOnly-combox-BackgroundColor');
										}
										*/
                                    }
                                    if(responseData['sendModel']=="EXC"){
                                        SmsGroupForm.down('radio[reference=sendModelExc]').setValue(true);

                                        SmsGroupForm.child('tagfield[reference=receverTagField]').setEditable(false);
                                        SmsGroupForm.child('tagfield[reference=receverTagField]').disabled=true;
                                        SmsGroupForm.child('toolbar[reference=receverToolbar]').child('button[reference=addAudienceBtn]').disabled=true;
										SmsGroupForm.child('toolbar[reference=receverToolbar]').child('button[reference=addAudienceBtn]').addCls('x-item-disabled x-btn-disabled');
                                        SmsGroupForm.child('toolbar[reference=receverToolbar]').child('button[reference=pasteFromExcelBtn]').disabled=true;
										SmsGroupForm.child('toolbar[reference=receverToolbar]').child('button[reference=selectStuBtn]').disabled=true;
										SmsGroupForm.child('toolbar[reference=receverToolbar]').child('button[reference=selectStuBtn]').addCls('x-item-disabled x-btn-disabled');
										
                                        SmsGroupForm.down('combobox[reference=smsTmpId]').disabled=true;
                                        SmsGroupForm.down('tagfield[reference=receverTagField]').addCls('readOnly-tagfield-BackgroundColor');
                                        SmsGroupForm.down('combobox[reference=smsTmpId]').addCls('readOnly-combox-BackgroundColor');
                                    }else{
                                    	SmsGroupForm.down('combobox[reference=smsTmpId]').disabled=false;
										SmsGroupForm.down('combobox[reference=smsTmpId]').removeCls('readOnly-combox-BackgroundColor');
                                    };
                                }else{
                                    panel.down('button[reference=saveBtn]').setDisabled(true);
                                    panel.down('button[reference=sendBtn]').setDisabled(true);
                                    panel.getController().pageReadonly(SmsGroupForm);
                                }
                                if(responseData['rwzxZt']=="B"){
                                    panel.down('button[reference=revokeBtn]').setVisible(true);
                                }
								/*
                                if(responseData['rwzxZt']=="C"||responseData['rwzxZt']=="D"){
                                    panel.down('button[reference=viewHisBtn]').setDisabled(false);
                                }
								*/
                                SmsGroupForm.down('radio[reference="sendModelNor"]').addListener('change','norSend');
                                SmsGroupForm.down('tagfield[reference="receverTagField"]').addListener('change','receverChange');
                                
                                panel.commitChanges(panel);
                                myMask.hide();
                            });
                        }
                    });
                }
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
     * 功能：查看邮件发送历史
     * 刘阳阳  2015-12-31
     * 王耀修改 2016-1-13
     */
    viewSmsHistory: function(grid, rowIndex, colIndex){
        // alert("查看邮件发送历史");
        var store = grid.store;
        var selRec = store.getAt(rowIndex);
        var smsQfId = selRec.get("smsQfId");
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
    /**
     * 功能：关闭
     */
    onPanelClose: function(btn){
        var win = btn.findParentByType("panel");
        win.close();
    }
});
