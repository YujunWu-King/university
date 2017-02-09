Ext.define('KitchenSink.view.bulkEmailAndSMS.EDM.EDMSetController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.EDMSetController',
    /*关闭*/
    onEDMSetClose:function(){
        this.getView().close()
    },
    /*查看收件人*/
    viewReceiver:function(btn){
        var form=btn.findParentByType("form").getForm();
        var emailID=form.findField("emailID").getValue();
        //ÊÇ·ñÓÐ·ÃÎÊÈ¨ÏÞ
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GK_EDM_COM"]["TZ_EDM_VIEWRY_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_EDM_VIEWRY_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, ViewClass, clsProto;
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
        }
        var win = this.lookupReference('EDMViewReceiverWin');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            var grid = win.child('grid');
            this.getView().add(win);
        }
         var grid = win.child('grid');
         var tzStoreParams = '{"emailID":"'+emailID+'"}';

         grid.store.tzStoreParams = tzStoreParams;
         grid.store.load();
         win.show();
    },
    /*运行退信引擎*/
    runTuiXinYq:function(btn){
        var form=btn.findParentByType("form").getForm();
        var emailID=form.findField("emailID").getValue();
        btn.setDisabled(true);
        var tzParams = '{"ComID":"TZ_GK_EDM_COM","PageID":"TZ_GK_EDM_STD","OperateType":"U","comParams":{"update":[{"emailID":"'+emailID+'"}]}}';
        Ext.tzLoad(tzParams,function(responseData){

        });
    },
    /*查看指标项详情*/
    viewZhiBiaoItemNum:function(view,rowindex){
        var col0=[
             {
             text: '姓名',
             dataIndex: 'personName',
             width: 150
             },
            {
                text: '收信邮箱',
                dataIndex: 'sendEmail',
                width: 250
            },
            {
                text: '发送时间',
                dataIndex: 'sendTime',
                flex:1
            }];
        var col2=[
            {
                text: '退信邮箱',
                dataIndex: 'refusedEmail',
                width: 250
            },
            {
                text: '退信时间',
                dataIndex: 'refusedTime',
                width: 200
            },
            {
                text: '退信原因',
                dataIndex: 'refusedReason',
                flex:1
            },{
				xtype: 'linkcolumn',
				text: '查看退信邮件',
                dataIndex: 'mailContent',
                width: 90,   
				flex: 1,             
				items:[{
					getText: function(v, meta, rec) {
						return '查看';
					},
					handler: function(grid,rowIndex,colIndex){
						var rec = grid.getStore().getAt(rowIndex);
						var mailContent = rec.data.mailContent;	
						
						var Panel=Ext.create('Ext.form.Panel', {  
							bodyPadding: 10,  
							bodyStyle:'overflow-y:auto;overflow-x:hidden',
							layout: {
								type: 'vbox',
								align: 'stretch'
							},
							items: [{
								xtype:'component',
								hideLabel: true,	
								html:[mailContent],
							}]
						});
						
						imgWin = Ext.widget('window', {
							 title: '退信邮件内容',
							 closeAction: 'hide',
							 width: 900,
							 minWidth: 800,
							 height: 500,
							 minHeight: 400,
							 resizable: true,
							 modal: true,
							 autoScroll: true,
							 items:Panel,
							 dockedItems:[{
									xtype:"toolbar",
									dock:"bottom",
									ui:"footer",
									items:['->',
										{minWidth:80,text:'关闭',iconCls:"close",handler: function (btn)
										{   var win = btn.findParentByType("window");
											win.close();
										}}]
								}]
						});
						imgWin.show();
					}
				}]
			}];
        var col4=[
            {
                text: '邮箱',
                dataIndex: 'openEmail',
                width: 250
            },{
                text:'IP地址',
                dataIndex:'ipAddr',
                width:250
            },
            {
                text: '打开时间',
                dataIndex: 'openTime',
                flex:1
            }];
        var col5=[
            {
                text: '邮箱',
                dataIndex: 'openEmail',
                flex: 1
            }];
        var col6=[
            {
                text: '邮箱',
                dataIndex: 'clickEmail',
                width: 250
            },{
                text: '点击目标网址',
                dataIndex: 'clickWebsite',
                width:250
            },
            {
                text: '点击时间',
                dataIndex: 'clickTime',
                flex:1
            }];
        var col7=[
            {
                text: '邮箱',
                dataIndex: 'clickEmail',
                width:300
            },
           {
                text: '点击目标网址',
                dataIndex: 'clickWebsite',
                flex:1
            }];
        var col8= [
            {
                text: '退订邮箱',
                dataIndex: 'backEmail',
                width: 300
            },
           {
                text: '退订渠道',
                dataIndex: 'backChannel',
                flex:1
            }]

        //var form=view.findParentByType("grid").findParentByType("panel").child("form").getForm();
		var form=view.findParentByType("grid").up("EDMSetInfo").child("form").getForm();
        /*获得邮件群发批次ID*/
        var emailID=form.findField("emailID").getValue();
        var store=view.findParentByType("grid").store;
        var ZhiBiaoDesc=store.getAt(rowindex).get("zhiBiaoDesc");
        //console.log(ZhiBiaoDesc);
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GK_EDM_COM"]["TZ_EDM_CKITEM_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_EDM_VIEWITEM_STD，请检查配置。');
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
        }
        var win = this.lookupReference('EDMZhiBiaoWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            if (ZhiBiaoDesc=="FSZS"){
                win = new ViewClass(col0);
		        win.setTitle("发送总数");
            }
	    if (ZhiBiaoDesc=="FSCGS"){
                win = new ViewClass(col0);
		        win.setTitle("发送成功数");
            }
            if (ZhiBiaoDesc=="YTS"){
                win = new ViewClass(col2);
		        win.setTitle("硬退数");
            }
	     if (ZhiBiaoDesc=="RTS"){
                win = new ViewClass(col2);
		        win.setTitle("软退数");
            }
            if (ZhiBiaoDesc=="ZDKS"){
                win = new ViewClass(col4);
		        win.setTitle("总打开数");
            }
	    if (ZhiBiaoDesc=="JDKS"){
                win = new ViewClass(col5);
		        win.setTitle("净打开数");
            }
            if (ZhiBiaoDesc=="ZDJS"){
                win = new ViewClass(col6);
		        win.setTitle("总点击数");
            }
	    if (ZhiBiaoDesc=="JDJS"){
                win = new ViewClass(col7);
		        win.setTitle("净点击数");
            }
            if (ZhiBiaoDesc=="TDS"){
                win = new ViewClass(col8);
		        win.setTitle("退订数");
            }
            this.getView().add(win);
        }
        var grid=win.child("grid");
        //发送总数
        win.yjqfId=emailID;
        if(ZhiBiaoDesc=="FSZS"){
            win.type="FSZS";
            grid.store.tzStoreParams='{"cfgSrhId":"TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_FSZS_VW","condition":{"TZ_MLSM_QFPC_ID-operator": "01","TZ_MLSM_QFPC_ID-value": "'+emailID+'"}}';
            grid.store.reload();
        }
        //发送成功数
        if(ZhiBiaoDesc=="FSCGS"){
            win.type="FSCGS";
            grid.store.tzStoreParams='{"cfgSrhId":"TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_FSCG_VW","condition":{"TZ_MLSM_QFPC_ID-operator": "01","TZ_MLSM_QFPC_ID-value": "'+emailID+'"}}';
            grid.store.reload();
        }
        //硬退数
        if(ZhiBiaoDesc=="YTS"){
            win.type="YTS";
            grid.store.tzStoreParams='{"cfgSrhId":"TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_YT_VW","condition":{"TZ_MLSM_QFPC_ID-operator": "01","TZ_MLSM_QFPC_ID-value": "'+emailID+'"}}';
            grid.store.reload();
        }
        //软退数
        if(ZhiBiaoDesc=="RTS"){
            win.type="RTS";
            grid.store.tzStoreParams='{"cfgSrhId":"TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_RT_VW","condition":{"TZ_MLSM_QFPC_ID-operator": "01","TZ_MLSM_QFPC_ID-value": "'+emailID+'"}}';
            grid.store.reload();
        }
        //总打开数
        if(ZhiBiaoDesc=="ZDKS"){
            win.type="ZDKS";
            grid.store.tzStoreParams='{"cfgSrhId":"TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_ZDK_VW","condition":{"TZ_MLSM_QFPC_ID-operator": "01","TZ_MLSM_QFPC_ID-value": "'+emailID+'"}}';
            grid.store.reload();
        }
        //净打开数
        if(ZhiBiaoDesc=="JDKS"){
            win.type="JDKS";
            grid.store.tzStoreParams='{"cfgSrhId":"TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_JDK_VW","condition":{"TZ_MLSM_QFPC_ID-operator": "01","TZ_MLSM_QFPC_ID-value": "'+emailID+'"}}';
            grid.store.reload();
        }
        //总点击数
        if(ZhiBiaoDesc=="ZDJS"){
            win.type="ZDJS";
            grid.store.tzStoreParams='{"cfgSrhId":"TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_ZDJ_VW","condition":{"TZ_MLSM_QFPC_ID-operator": "01","TZ_MLSM_QFPC_ID-value": "'+emailID+'"}}';
            grid.store.reload();
        }
        //净点击数
        if(ZhiBiaoDesc=="JDJS"){
            win.type="JDJS";
            grid.store.tzStoreParams='{"cfgSrhId":"TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_JDJ_VW","condition":{"TZ_MLSM_QFPC_ID-operator": "01","TZ_MLSM_QFPC_ID-value": "'+emailID+'"}}';
            grid.store.reload();
        }
        //退订数
        if(ZhiBiaoDesc=="TDS"){
            win.type="TDS";
            grid.store.tzStoreParams='{"cfgSrhId":"TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_TD_VW","condition":{"TZ_MLSM_QFPC_ID-operator": "01","TZ_MLSM_QFPC_ID-value": "'+emailID+'"}}';
            grid.store.reload();
        }
        win.show();
    },
    //可配置搜索
    findEDMItem:function(btn) {
        var type = btn.findParentByType("window").type;
        var yjqfId = btn.findParentByType("window").yjqfId;
  
        if (type == "FSZS") {
            Ext.tzShowCFGSearch({
                cfgSrhId: 'TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_FSZS_VW',
                condition: {
                    "TZ_MLSM_QFPC_ID": yjqfId         //设置搜索字段的默认值，没有可以不设置condition;
                },
                callback: function (seachCfg) {
                    var store = btn.findParentByType("window").child('grid').store;
                    store.tzStoreParams = seachCfg;
                    store.load();
                }
            });
        }
        if (type == "FSCGS") {
            Ext.tzShowCFGSearch({
                cfgSrhId: 'TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_FSCG_VW',
                condition: {
                    "TZ_MLSM_QFPC_ID": yjqfId
                },
                callback: function (seachCfg) {
                    var store = btn.findParentByType("window").child('grid').store;
                    store.tzStoreParams = seachCfg;
                    store.load();
                }
            });
        }
        //硬退数
        if (type == "YTS") {
            Ext.tzShowCFGSearch({
                cfgSrhId: 'TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_YT_VW',
                condition: {
                    "TZ_MLSM_QFPC_ID": yjqfId
                },
                callback: function (seachCfg) {
                    var store = btn.findParentByType("window").child('grid').store;
                    store.tzStoreParams = seachCfg;
                    store.load();
                }
            });
        }
        //软退数
        if (type == "RTS") {
            Ext.tzShowCFGSearch({
                cfgSrhId: 'TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_RT_VW',
                condition: {
                    "TZ_MLSM_QFPC_ID": yjqfId
                },
                callback: function (seachCfg) {
                    var store = btn.findParentByType("window").child('grid').store;
                    store.tzStoreParams = seachCfg;
                    store.load();
                }
            });
        }
        //总打开数
        if (type == "ZDKS") {
            Ext.tzShowCFGSearch({
                cfgSrhId: 'TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_ZDK_VW',
                condition: {
                    "TZ_MLSM_QFPC_ID": yjqfId
                },
                callback: function (seachCfg) {
                    var store = btn.findParentByType("window").child('grid').store;
                    store.tzStoreParams = seachCfg;
                    store.load();
                }
            });
        }
        //净打开数
        if (type == "JDKS") {
            Ext.tzShowCFGSearch({
                cfgSrhId: 'TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_JDK_VW',
                condition: {
                    "TZ_MLSM_QFPC_ID": yjqfId
                },
                callback: function (seachCfg) {
                    var store = btn.findParentByType("window").child('grid').store;
                    store.tzStoreParams = seachCfg;
                    store.load();
                }
            });
        }
        //总点击
        if (type == "ZDJS") {
            Ext.tzShowCFGSearch({
                cfgSrhId: 'TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_ZDJ_VW',
                condition: {
                    "TZ_MLSM_QFPC_ID": yjqfId
                },
                callback: function (seachCfg) {
                    var store = btn.findParentByType("window").child('grid').store;
                    store.tzStoreParams = seachCfg;
                    store.load();
                }
            });
        }
        //净点击
        if (type == "JDJS") {
            Ext.tzShowCFGSearch({
                cfgSrhId: 'TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_JDJ_VW',
                condition: {
                    "TZ_MLSM_QFPC_ID": yjqfId
                },
                callback: function (seachCfg) {
                    var store = btn.findParentByType("window").child('grid').store;
                    store.tzStoreParams = seachCfg;
                    store.load();
                }
            });
        }
        //退订数
        if (type == "TDS") {
            Ext.tzShowCFGSearch({
                cfgSrhId: 'TZ_GK_EDM_COM.TZ_EDM_CKITEM_STD.TZ_YJQF_TD_VW',
                condition: {
                    "TZ_MLSM_QFPC_ID": yjqfId
                },
                callback: function (seachCfg) {
                    var store = btn.findParentByType("window").child('grid').store;
                    store.tzStoreParams = seachCfg;
                    store.load();
                }
            });
        }
      },
    //查看退信情况
    viewTxQk:function(btn){
        var form=btn.findParentByType("form").getForm();
        var emailID=form.findField("emailID").getValue();
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GK_EDM_COM"]["TZ_VIEW_TDRZ_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GK_EDM_COM.TZ_VIEW_TDRZ_STD.prompt","提示"),Ext.tzGetResourse("TZ_GK_EDM_COM.TZ_VIEW_TDRZ_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GK_EDM_COM.TZ_VIEW_TDRZ_STD.prompt","提示"),Ext.tzGetResourse("TZ_GK_EDM_COM.TZ_VIEW_TDRZ_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('EDMTxWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
        win.yjqfId=emailID;
        var grid = win.child('grid');
        grid.store.tzStoreParams='{"cfgSrhId":"TZ_GK_EDM_COM.TZ_VIEW_TDRZ_STD.TZ_YJQFTXRZ_VW","condition":{"TZ_MLSM_QFPC_ID-operator": "01","TZ_MLSM_QFPC_ID-value": "'+emailID+'"}}';
        grid.store.reload();
        win.show();
  },
    //查看退信详情--查询
    txRzQuery:function(btn){
        var yjqfId = btn.findParentByType("window").yjqfId;
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_GK_EDM_COM.TZ_VIEW_TDRZ_STD.TZ_YJQFTXRZ_VW',
            condition: {
                "TZ_MLSM_QFPC_ID": yjqfId
            },
            callback: function (seachCfg) {
                var store = btn.findParentByType("window").child('grid').store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    //查看退信详情--关闭
    txRzClose:function(btn){
       var win=btn.findParentByType("window");
        win.close();
   },
   //查看退信详情-下载退信日志
    downloadTzRz:function(grid,rowIndex){
        var store = grid.getStore();
        var record = store.getAt(rowIndex);
        var AEId=record.get("AEId");
        var emailId=grid.findParentByType("window").yjqfId;
        if(AEId!=""&&emailId!=""){
            var tzParams='{"ComID":"TZ_GK_EDM_COM","PageID":"TZ_EDM_TD_STD","OperateType":"QF","comParams":{"yjqfId":"'+emailId+'","AEId":"'+AEId+'"}}';
            Ext.tzLoad(tzParams,function(responseData){
                if(responseData.fileUrl){
                    window.open(TzUniversityContextPath + responseData.fileUrl, "download","status=no,menubar=yes,toolbar=no,location=no");
                  //  window.location.href=responseData.fileUrl;
                }
            });
        }else{
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GK_EDM_COM.TZ_EDM_TD_STD.prompt","提示"), Ext.tzGetResourse("TZ_GK_EDM_COM.TZ_EDM_TD_STD.zbdrz","找不到日志"));
        }
    }
});
