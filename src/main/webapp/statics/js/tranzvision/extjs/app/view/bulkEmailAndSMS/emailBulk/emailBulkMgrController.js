/**
 * 功能：邮件群发管理controller
 * 刘阳阳  2015-12-31
 */
Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkMgrController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.emailBulkMgrController',
    requires: [
        'Ext.grid.plugin.Clipboard'
    ],
    /**
     * 功能：查询
     * 刘阳阳  2015-12-31
     */
    queryEmlTsks:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.TZ_EMLQ_LIST_V',
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
     * 刘阳阳  2015-12-31
     */
    addEmlTsks: function(grid, rowIndex, colIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EMLQ_COM"]["TZ_EMLQ_DET_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MS_ARR_PRE_STD，请检查配置。');
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

            var senderStore = new KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetSenderStore();
            var receverStore = new KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetReceiverStore();
            var CCStore = new KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetCCStore();
            var EmlTmplStore = new KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetEmlTmplStore();
            var attaStore = new KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkAttaStore();
            var emlItemStore = new KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetEmlItemStore();

            var emlBkDtForm = panel.child('form');
			emlBkDtForm.down('button[reference=copyHistoryBtn]').setHidden(false);/*显示历史复制按钮*/

            emlBkDtForm.down('grid[reference=emlTmplItemGrid]').setStore(emlItemStore);
            emlBkDtForm.down('grid[reference=emlInfoItemGrid]').setStore(attaStore);

            var par = '{"emlQfId": "","queryID": "recever"}';
            receverStore.tzStoreParams=par;
			
			senderStore.load({
                callback: function (records, options, success) {
                    //emlBkDtForm.down('combobox[name=sender]').setStore(senderStore);
                }
            });
			emlBkDtForm.down('combobox[name=sender]').setStore(senderStore);
			
			receverStore.load({
				callback: function (records, options, success) {
					//emlBkDtForm.down('tagfield[reference=recever]').setStore(receverStore);
				}
			});
			emlBkDtForm.down('tagfield[reference=recever]').setStore(receverStore);

			CCStore.tzStoreParams = '{"emlQfId": "","queryID": "CC"}';
			CCStore.load({
				callback: function (records, options, success) {
					//emlBkDtForm.down('tagfield[reference=mailCC]').setStore(CCStore);
				}
			});
			emlBkDtForm.down('tagfield[reference=mailCC]').setStore(CCStore);
			
			EmlTmplStore.load({
				callback: function (records, options, success) {
					//emlBkDtForm.down('combobox[reference=emlTmpId]').setStore(EmlTmplStore);
				}
			});
			emlBkDtForm.down('combobox[reference=emlTmpId]').setStore(EmlTmplStore);

            var tzParams = '{"ComID":"TZ_EMLQ_COM","PageID":"TZ_EMLQ_DET_STD","OperateType":"getCreInfo","comParams":{}}';
            Ext.tzLoad(tzParams,function(responseData){
            	emlBkDtForm.down('radio[reference="sendModelNor"]').removeListener('change','norSend');
				emlBkDtForm.down('tagfield[reference="recever"]').removeListener('change','receverChange');

                emlBkDtForm.getForm().setValues(responseData);
                panel.BulkTaskId = emlBkDtForm.down('textfield[name=emlQfId]').getValue();

                par = '{"emlQfId": "'+responseData['emlQfId']+'","queryID": "atta"}';
                attaStore.tzStoreParams=par;
                attaStore.load();
                                             
                emlBkDtForm.down('radio[reference="sendModelNor"]').addListener('change','norSend');
				emlBkDtForm.down('tagfield[reference="recever"]').addListener('change','receverChange');
				
				emlBkDtForm.down('button[reference=setEmlTmpl]').disabled=true;
				emlBkDtForm.down('button[reference=setEmlTmpl]').addCls('disabled-button-color');
            });

            emlBkDtForm.child('toolbar').child('button[reference=clearAllBtn]').disabled=true;
            emlBkDtForm.child('toolbar').child('button[reference=clearAllBtn]').addCls('x-item-disabled x-btn-disabled');
			
            emlBkDtForm.down('checkbox[name=edmFlag]').setValue(true);
            emlBkDtForm.down('checkbox[name=qxdyFlag]').setValue(true);
            emlBkDtForm.down('checkbox[name=qypfFlag]').setValue(true);
            emlBkDtForm.down('textfield[name=fsslXs]').setVisible(true);
            emlBkDtForm.down('textfield[name=fsslXs]').setValue('500');
			
			emlBkDtForm.child('tagfield[reference=recever]').setEditable(false);
			emlBkDtForm.child('tagfield[reference=recever]').disabled=true;
			emlBkDtForm.down('tagfield[reference=recever]').addCls('readOnly-tagfield-BackgroundColor');
			
			emlBkDtForm.child('toolbar').child('button[reference=addAudienceBtn]').disabled=true;
			emlBkDtForm.child('toolbar').child('button[reference=addAudienceBtn]').addCls('x-item-disabled x-btn-disabled');
			
			emlBkDtForm.child('toolbar').child('button[reference=selectStuBtn]').disabled=true;
			emlBkDtForm.child('toolbar').child('button[reference=selectStuBtn]').addCls('x-item-disabled x-btn-disabled');
			
			emlBkDtForm.child('toolbar').child('button[reference=pasteFromExcelBtn]').disabled=true;
			emlBkDtForm.down('combobox[reference=emlTmpId]').disabled=true;
			emlBkDtForm.down('combobox[reference=emlTmpId]').addCls('readOnly-combox-BackgroundColor');
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
     * 刘阳阳  2015-12-31
     */
    editEmialTask: function(grid, rowIndex, colIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EMLQ_COM"]["TZ_EMLQ_DET_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MS_ARR_PRE_STD，请检查配置。');
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

        var emlBulkGridRecord = grid.store.getAt(rowIndex);
        var emlQfId = emlBulkGridRecord.data.emlQfId;

        cmp.on('afterrender',function(panel){
            var senderStore = new KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetSenderStore();
            var receverStore = new KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetReceiverStore();
            var CCStore = new KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetCCStore();
            var EmlTmplStore = new KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetEmlTmplStore();
            var attaStore = new KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkAttaStore();
            var emlItemStore = new KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetEmlItemStore();

            var emlBkDetForm = panel.child('form');

            emlBkDetForm.down('grid[reference=emlTmplItemGrid]').setStore(emlItemStore);
            emlBkDetForm.down('grid[reference=emlInfoItemGrid]').setStore(attaStore);

            var par = '{"emlQfId": "'+emlQfId+'","queryID": "recever"}';
            receverStore.tzStoreParams=par;

			senderStore.load({
                callback: function (records, options, success) {
                    //emlBkDetForm.down('combobox[name=sender]').setStore(senderStore);
                }
            });
			emlBkDetForm.down('combobox[name=sender]').setStore(senderStore);
			
			receverStore.load({
				callback: function (records, options, success) {
					//emlBkDetForm.down('tagfield[reference=recever]').setStore(receverStore);
				}
			});
			emlBkDetForm.down('tagfield[reference=recever]').setStore(receverStore);
			
			CCStore.tzStoreParams='{"emlQfId": "'+emlQfId+'","queryID": "CC"}';
			CCStore.load({
				callback: function (records, options, success) {
					//emlBkDetForm.down('tagfield[reference=mailCC]').setStore(CCStore);
				}
			});
			emlBkDetForm.down('tagfield[reference=mailCC]').setStore(CCStore);
			
			EmlTmplStore.load({
				callback: function (records, options, success) {
					//emlBkDetForm.down('combobox[reference=emlTmpId]').setStore(EmlTmplStore);
				}
			});
			emlBkDetForm.down('combobox[reference=emlTmpId]').setStore(EmlTmplStore);
			
			var tzParams = '{"ComID":"TZ_EMLQ_COM","PageID":"TZ_EMLQ_DET_STD","OperateType":"QF","comParams":{"emlQfId":"'+emlQfId+'"}}';
			Ext.tzLoad(tzParams,function(responseData){
				emlBkDetForm.down('radio[reference="sendModelNor"]').removeListener('change','norSend');
				emlBkDetForm.down('tagfield[reference="recever"]').removeListener('change','receverChange');

				emlBkDetForm.getForm().setValues(responseData);
				if(responseData.dsfsTime == ""){
					emlBkDetForm.down('timefield[name=dsfsTime]').setValue('00:00'); /*解决报时间无效错误*/		
				}
				panel.BulkTaskId = emlBkDetForm.down('textfield[name=emlQfId]').getValue();

				if (emlBkDetForm.down('radio[reference="sendModelExc"]').checked) {
					emlBkDetForm.down('button[reference=setEmlTmpl]').disabled=false;
					var tzParams = '{"ComID":"TZ_EMLQ_COM","PageID":"TZ_EMLQ_DET_STD","OperateType":"getEmlTmpItem","comParams":{"emlQfId":"'+emlQfId+'","emlTmpId":"'+responseData['emlTmpId']+'"}}';
					Ext.tzLoad(tzParams,function(responseData){
						emlItemStore.add(responseData['root']);
						emlItemStore.commitChanges();

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
				par = '{"emlQfId": "'+emlQfId+'","queryID": "atta"}';
				attaStore.tzStoreParams=par;
				attaStore.load();

				if(responseData['dsfsInfo']!=""){
					emlBkDetForm.down('displayfield[name=dsfsInfo]').setVisible(true);
				}
				emlBkDetForm.down('displayfield[name=creDt]').setVisible(true);

				if (responseData['rwzxZt']==""||responseData['rwzxZt']=="D"||responseData['rwzxZt']=="E"){
					if(responseData['recever']!=""){
						emlBkDetForm.down('tagfield[reference="recever"]').removeListener('change','receverChange');
						emlBkDetForm.down('tagfield[reference=recever]').setValue(responseData['recever']);
						emlBkDetForm.down('tagfield[reference="recever"]').addListener('change','receverChange');
						
						var arrRecever = responseData['recever'];
						var hasEmailAdd = false;
						for(var i=0; i<arrRecever.length; i++){
							var EmailReg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;	
							if (EmailReg.test(arrRecever[i])){
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
					}
					if(responseData['mailCC']!=""){
						emlBkDetForm.down('tagfield[reference=mailCC]').setValue(responseData['mailCC']);
					}
					if(responseData['sendModel']=="EXC"){
						emlBkDetForm.down('radio[reference=sendModelExc]').setValue(true);

						emlBkDetForm.child('tagfield[reference=recever]').setEditable(false);
						emlBkDetForm.child('tagfield[reference=recever]').disabled=true;
						emlBkDetForm.child('toolbar').child('button[reference=addAudienceBtn]').disabled=true;
						emlBkDetForm.child('toolbar').child('button[reference=addAudienceBtn]').addCls('x-item-disabled x-btn-disabled');
						
						emlBkDetForm.child('toolbar').child('button[reference=pasteFromExcelBtn]').disabled=true;
						
						emlBkDetForm.child('toolbar').child('button[reference=selectStuBtn]').disabled=true;
						emlBkDetForm.child('toolbar').child('button[reference=selectStuBtn]').addCls('x-item-disabled x-btn-disabled');
						
						emlBkDetForm.down('combobox[reference=emlTmpId]').disabled=true;
						emlBkDetForm.down('tagfield[reference=recever]').addCls('readOnly-tagfield-BackgroundColor');
						emlBkDetForm.down('combobox[reference=emlTmpId]').addCls('readOnly-combox-BackgroundColor');
					};
				}else{
					panel.down('button[reference=saveBtn]').setDisabled(true);
					panel.down('button[reference=sendBtn]').setDisabled(true);
					panel.getController().pageReadonly(emlBkDetForm);
				}

				if(responseData['rwzxZt']=="B"){
					panel.down('button[reference=revokeBtn]').setVisible(true);
				}
				emlBkDetForm.down('radio[reference="sendModelNor"]').addListener('change','norSend');
				emlBkDetForm.down('tagfield[reference="recever"]').addListener('change','receverChange');
				
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
     * 功能：查看邮件发送历史
     * 刘阳阳  2015-12-31
     * 王耀修改 2016-1-13
     */
    viewEmailHistory: function(grid, rowIndex, colIndex){
        // alert("查看邮件发送历史");
        var store = grid.store;
        var selRec = store.getAt(rowIndex);
        var emailID = selRec.get("emlQfId");
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
            console.log(panel)
            var store=panel.getStore();
            var tzStoreParams ='{"storeType":"history","emailID":"'+emailID+'"}';
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
     * 功能：查看邮件发送统计
     * 刘阳阳  2015-12-31
     * 李丹丹修改 2016-1-7
     */
    viewEmailCount: function(view, rowIndex){

        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var emailID = selRec.get("emlQfId");
        Ext.tzSetCompResourses("TZ_GK_EDM_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GK_EDM_COM"]["TZ_GK_EDM_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GK_EDM_STD，请检查配置。');
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
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        cmp=new ViewClass();
        cmp.on('afterrender',function(panel){
            //  console.log(panel);
            //组件注册表单信息;
            var form = panel.child('form').getForm();
            //var grid=panel.child('grid');
			var grid=panel.down('grid');
            //参数
            var tzParams = '{"ComID":"TZ_GK_EDM_COM","PageID":"TZ_GK_EDM_STD","OperateType":"QF","comParams":{"emailID":"'+emailID +'"}}';
            //加载数据
            /*Ext.tzLoad(tzParams,function(responseData){
                //组件注册信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                panel.htmlContent=formData.emailContent;
                var tzStoreParams = '{"emailID":"'+emailID+'"}';
                grid.store.tzStoreParams = tzStoreParams;
                grid.store.load();
            });*/
            Ext.tzLoadAsync(tzParams,function(responseData){
                var formData = responseData.formData;
                form.setValues(formData);
                panel.yjqfId = emailID;
                var tzStoreParams = '{"emailID":"'+emailID+'"}';
                grid.store.tzStoreParams = tzStoreParams;
                grid.store.load();
				
				//加载漏斗图
				var funnel = panel.down('component[name=funnelPic]');
				var dispalyFunnel = responseData.dispalyFunnel;
				if(dispalyFunnel == "Y"){
					var funenlDate = responseData.funenlDate;
					var data = [];
					for(var i=0;i<funenlDate.length;i++){
						data.push([funenlDate[i]["name"],funenlDate[i]["count"]]);	
					}
					var chart = new Highcharts.Chart({
						chart: {
							type: 'funnel',
							marginRight: 100,
							height:380,
							//renderTo: 'funnelPicContainer',
							renderTo:funnel.getEl().dom
						},
						title: {
							text: ''
						},
						credits: {
							enabled: false	
						},
						plotOptions: {
							funnel: {
								borderWidth: 1,
								center: ["45%", "47%"],
								cursor: 'pointer',
								height: '80%',
								width: '80%',
								dataLabels: {
									enabled: true,
									format: '{point.name}({point.y:,.0f})',
									color: 'black',
									softConnector: true,
									distance:10,
									style:{"color": "contrast", "fontSize": "8px", "fontWeight": "normal"}
								},
								neckWidth: '10%',
								neckHeight: '10%',
								tooltip: {
									headerFormat:'<span style="font-size: 10px">{point.key}</span>:',
									pointFormat:'{point.y}'
								}
							}
						},
						legend: {
							enabled: true
						},
						series: [{
							name: '数量',
							data: data
						}]
					});
				}else{
					funnel.hidden = true;
					grid.columnWidth = 1;	
				}
            });
        });
        var tab = contentPanel.add(cmp);
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
    }
});
