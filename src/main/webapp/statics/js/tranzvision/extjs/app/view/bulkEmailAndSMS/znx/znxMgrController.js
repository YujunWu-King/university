/**
 * 功能：邮件群发管理controller
 * 刘阳阳  2015-12-31
 */
Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.znxMgrController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.znxMgrController',
    requires: [
        'Ext.grid.plugin.Clipboard'
    ],
    /**
     * 功能：查询
     * 刘阳阳  2015-12-31
     */
    queryZnxTsks:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_ZNX_GL_COM.TZ_ZNX_GL_STD.TZ_ZNXQF_LIST_V',
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
    addZnxTsks: function(grid, rowIndex, colIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZNX_GL_COM"]["TZ_ZNX_DET_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZNX_DET_STD，请检查配置。');
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
            var receverStore = new KitchenSink.view.bulkEmailAndSMS.znx.znxDetReceiverStore();
            var ZnxTmplStore = new KitchenSink.view.bulkEmailAndSMS.znx.znxDetZnxTmplStore();
            var attaStore = new KitchenSink.view.bulkEmailAndSMS.znx.znxAttaStore();
            var znxItemStore = new KitchenSink.view.bulkEmailAndSMS.znx.znxDetZnxItemStore();

            var znxDtForm = panel.child('form');
			//znxDtForm.down('button[reference=copyHistoryBtn]').setHidden(false);/*显示历史复制按钮*/

            znxDtForm.down('grid[reference=znxTmplItemGrid]').setStore(znxItemStore);
            znxDtForm.down('grid[reference=znxInfoItemGrid]').setStore(attaStore);
            
            /*
            var par = '{"znxQfId": "","queryID": "recever"}';
            receverStore.tzStoreParams=par;
		
			receverStore.load({
				callback: function (records, options, success) {
					//znxDtForm.down('tagfield[reference=recever]').setStore(receverStore);
				}
			});
			*/
			znxDtForm.down('tagfield[reference=recever]').setStore(receverStore);
			
			ZnxTmplStore.load({
				callback: function (records, options, success) {
					znxDtForm.down('combobox[reference=znxTmpId]').setStore(ZnxTmplStore);
					
					var tzParams = '{"ComID":"TZ_ZNX_GL_COM","PageID":"TZ_ZNX_DET_STD","OperateType":"getCreInfo","comParams":{}}';
		            Ext.tzLoad(tzParams,function(responseData){
		            	//znxDtForm.down('tagfield[reference="recever"]').removeListener('change','receverChange');
		    	
		            	znxDtForm.getForm().setValues(responseData);
		                panel.BulkTaskId = znxDtForm.down('textfield[name=znxQfId]').getValue();

		                par = '{"znxQfId": "'+responseData['znxQfId']+'","queryID": "atta"}';
		                attaStore.tzStoreParams=par;
		                attaStore.load();
		                                             
		                //znxDtForm.down('tagfield[reference="recever"]').addListener('change','receverChange');
						
		                znxDtForm.down('button[reference=setZnxTmpl]').disabled=true;
		                znxDtForm.down('button[reference=setZnxTmpl]').addCls('disabled-button-color');
		                
		                panel.commitChanges(panel);
		            });
				}
			});
			//znxDtForm.down('combobox[reference=znxTmpId]').setStore(ZnxTmplStore);
			/*
            var tzParams = '{"ComID":"TZ_ZNX_GL_COM","PageID":"TZ_ZNX_DET_STD","OperateType":"getCreInfo","comParams":{}}';
            Ext.tzLoad(tzParams,function(responseData){
            	//znxDtForm.down('tagfield[reference="recever"]').removeListener('change','receverChange');
    	
            	znxDtForm.getForm().setValues(responseData);
                panel.BulkTaskId = znxDtForm.down('textfield[name=znxQfId]').getValue();

                par = '{"znxQfId": "'+responseData['znxQfId']+'","queryID": "atta"}';
                attaStore.tzStoreParams=par;
                attaStore.load();
                                             
                //znxDtForm.down('tagfield[reference="recever"]').addListener('change','receverChange');
				
                znxDtForm.down('button[reference=setZnxTmpl]').disabled=true;
                znxDtForm.down('button[reference=setZnxTmpl]').addCls('disabled-button-color');
            });
			 */
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
    editZnxTask: function(grid, rowIndex, colIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZNX_GL_COM"]["TZ_ZNX_DET_STD"];
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
        var znxQfId = emlBulkGridRecord.data.znxQfId;

        cmp.on('afterrender',function(panel){  
            var receverStore = new KitchenSink.view.bulkEmailAndSMS.znx.znxDetReceiverStore();
            var ZnxTmplStore = new KitchenSink.view.bulkEmailAndSMS.znx.znxDetZnxTmplStore();
            var attaStore = new KitchenSink.view.bulkEmailAndSMS.znx.znxAttaStore();
            var znxItemStore = new KitchenSink.view.bulkEmailAndSMS.znx.znxDetZnxItemStore();


            var emlBkDetForm = panel.child('form');

            emlBkDetForm.down('grid[reference=znxTmplItemGrid]').setStore(znxItemStore);
            emlBkDetForm.down('grid[reference=znxInfoItemGrid]').setStore(attaStore);

            var par = '{"znxQfId": "'+znxQfId+'","queryID": "recever"}';
            receverStore.tzStoreParams=par;
			
			receverStore.load({
				callback: function (records, options, success) {
					//emlBkDetForm.down('tagfield[reference=recever]').setStore(receverStore);
				}
			});
			
			emlBkDetForm.down('tagfield[reference=recever]').setStore(receverStore);
			
			ZnxTmplStore.load({
				callback: function (records, options, success) {
					emlBkDetForm.down('combobox[reference=znxTmpId]').setStore(ZnxTmplStore);
					
					var tzParams = '{"ComID":"TZ_ZNX_GL_COM","PageID":"TZ_ZNX_DET_STD","OperateType":"QF","comParams":{"znxQfId":"'+znxQfId+'"}}';
					Ext.tzLoad(tzParams,function(responseData){
						emlBkDetForm.getForm().setValues(responseData);
						
						panel.BulkTaskId = emlBkDetForm.down('textfield[name=znxQfId]').getValue();

						/*
							emlBkDetForm.down('button[reference=setZnxTmpl]').disabled=false;
							var tzParams = '{"ComID":"TZ_ZNX_GL_COM","PageID":"TZ_ZNX_DET_STD","OperateType":"getZnxTmpItem","comParams":{"znxQfId":"'+znxQfId+'","znxTmpId":"'+responseData['znxTmpId']+'"}}';
							Ext.tzLoad(tzParams,function(responseData){
								znxItemStore.add(responseData['root']);
								znxItemStore.commitChanges();

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
						*/
						par = '{"znxQfId": "'+znxQfId+'","queryID": "atta"}';
						attaStore.tzStoreParams=par;
						attaStore.load();
		/*
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
									emlBkDetForm.down('combobox[reference=znxTmpId]').disabled=true;
									emlBkDetForm.down('combobox[reference=znxTmpId]').addCls('readOnly-combox-BackgroundColor');	
								}else{
									emlBkDetForm.down('combobox[reference=znxTmpId]').disabled=false;
									emlBkDetForm.down('combobox[reference=znxTmpId]').removeCls('readOnly-combox-BackgroundColor');
								}
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
								
								emlBkDetForm.down('combobox[reference=znxTmpId]').disabled=true;
								emlBkDetForm.down('tagfield[reference=recever]').addCls('readOnly-tagfield-BackgroundColor');
								emlBkDetForm.down('combobox[reference=znxTmpId]').addCls('readOnly-combox-BackgroundColor');
							};
						}else{
							panel.down('button[reference=saveBtn]').setDisabled(true);
							panel.down('button[reference=sendBtn]').setDisabled(true);
							panel.getController().pageReadonly(emlBkDetForm);
						}

						if(responseData['rwzxZt']=="B"){
							panel.down('button[reference=revokeBtn]').setVisible(true);
						}
		*/
						panel.commitChanges(panel);
					});
				}
			});
			//emlBkDetForm.down('combobox[reference=znxTmpId]').setStore(ZnxTmplStore);
			
			
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
    viewZnxHistory: function(grid, rowIndex, colIndex){
        // alert("查看邮件发送历史");
        var store = grid.store;
        var selRec = store.getAt(rowIndex);
        var znxQfId = selRec.get("znxQfId");
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

    }
});
