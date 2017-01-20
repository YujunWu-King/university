
Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxHistoryPanelController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.znxHistoryPanelController',
    viewHistoryDetail: function(grid, rowIndex, colIndex){
            // alert("查看邮件发送历史");
            var store = grid.store;
            var selRec = store.getAt(rowIndex);

           var emailID = selRec.get("emlQfId");//群发定义ID
        var taskID=selRec.get("taskID");//任务ID；
            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZNXQ_VIEWTY_COM"]["TZ_ZNXQ_VWTY_D_STD"];
            if( pageResSet == "" || pageResSet == undefined){
                Ext.MessageBox.alert('提示', '您没有修改数据的权限');
                return;
            }
            //该功能对应的JS类
            var className = pageResSet["jsClassName"];
            if(className == "" || className == undefined){
                Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZNXQ_VWTY_D_STD，请检查配置。');
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
                panel.taskID=taskID;
                var store=panel.getStore();
                var tzStoreParams = '{"cfgSrhId": "TZ_ZNXQ_VIEWTY_COM.TZ_ZNXQ_VWTY_D_STD.TZ_ZNX_QFHIS_VW","condition":{"TZ_EML_SMS_TASK_ID-operator": "01","TZ_EML_SMS_TASK_ID-value":"'+taskID+'"}}';
              //  var tzStoreParams ='{"storeType":"historyDetail","emailID":"'+emailID+'","taskID":"'+taskID+'"}';
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
        viewEachZnxDetail:function(grid, rowIndex, colIndex){
        var store = grid.store;
        var selRec = store.getAt(rowIndex);

        var rwsl_ID=selRec.get("rwslID");//任务实例ID
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZNXQ_VIEWTY_COM"]["TZ_ZNXQ_VW_E_D_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZNXQ_VW_E_D_STD，请检查配置。');
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
            var form = panel.child('form').getForm();
            var tzParams = '{"ComID":"TZ_ZNXQ_VIEWTY_COM","PageID":"TZ_ZNXQ_VW_E_D_STD","OperateType":"viewEachZnx","comParams":{"type":"viewEachZnx","rwsl_ID":"'+rwsl_ID+'"}}';
            Ext.tzLoad(tzParams,function(responseData){
                    var formData = responseData.formData;
                    form.setValues(formData);
                    var htmlCom = panel.down("component[name=znxContentHtml]");
                    htmlCom.getEl().update(formData.znxContentHtml);
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
	onClickNumber: function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var taskId = rec.data.taskID;
		
		var sendStatus = "";
		switch(colIndex){
			case 1:
				sendStatus = "";
				break;
			case 2:
				sendStatus = "SUC";
				break;
			case 3:
				sendStatus = "FAIL";
				break;
			case 4:
				sendStatus = "RPT";
				break;
		}
		
		var contentPanel,cmp, className, ViewClass;
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');
		
		className = 'KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxSendMemberWindow';
		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}
		ViewClass = Ext.ClassManager.get(className);
		var config = {
				taskId: taskId,
				sendStatus: sendStatus
			}
		var win = new ViewClass(config);
		win.show();	
	},
	reSendFailTask: function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var taskId = rec.data.taskID;
		var znxQfId = rec.data.znxQfId;
		
		var tzParams = '{"ComID":"TZ_ZNXQ_VIEWTY_COM","PageID":"TZ_ZNXQ_VW_E_D_STD","OperateType":"U","comParams":{"update":[{"znxQfId":"'+ znxQfId +'","taskId":"'+ taskId +'"}]}}';
		Ext.tzLoad(tzParams,function(responseData){
		});
		Ext.Msg.alert("提示","失败任务已发送");	
	},
    queryemailHistoryDetail:function(btn){
        var taskID=btn.findParentByType("grid").taskID;
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_ZNXQ_VIEWTY_COM.TZ_ZNXQ_VWTY_D_STD.TZ_ZNX_QFHIS_VW',
            condition :{TZ_EML_SMS_TASK_ID:taskID},
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    }
});
