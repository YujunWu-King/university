Ext.define('KitchenSink.view.recommend.recommendListController', {
    extend: 'Ext.app.ViewController',
    requires:['Ext.ux.IFrame'],
    alias: 'controller.recommendMg', 

	//可配置搜索
	cfgSearchAct: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_TJR_MANAGER_COM.TZ_TJR_INFO_STD.TZ_GD_TJRCFG_VW',
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
	
	//查看推荐人
	viewRecommend: function(grid, rowIndex, colIndex){
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_TJR_MANAGER_COM"]["TZ_TJR_DETAIL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_TJR_MANAGER_COM.TZ_TJR_DETAIL_STD.prompt","提示"),Ext.tzGetResourse("TZ_TJR_MANAGER_COM.TZ_TJR_DETAIL_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_TJR_MANAGER_COM.TZ_TJR_DETAIL_STD.prompt","提示"), Ext.tzGetResourse("TZ_TJR_MANAGER_COM.TZ_TJR_DETAIL_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }

        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classId;
        var batchID = record.data.batchId;

        var initData=[];
        var stuGridColorSortFilterOptions=[];/*考生类别的过滤器数据*/
        var orgColorSortStore = new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_ORG_COLOR_V',
            condition:{
                TZ_JG_ID:{
                    value:Ext.tzOrgID,
                    operator:'01',
                    type:'01'
                }},
            result:'TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE',
            listeners:{
                load:function( store, records, successful, eOpts){
                    for(var i=0;i<records.length;i++){
                        initData.push(records[i].data);
                        stuGridColorSortFilterOptions.push([records[i].data.TZ_COLOR_SORT_ID,records[i].data.TZ_COLOR_NAME]);
                    };
                    cmp = new ViewClass({
                            orgColorSortStore:orgColorSortStore ,
                            initData:initData,
                            stuGridColorSortFilterOptions:stuGridColorSortFilterOptions,
                            classID:classID
                        }
                    );
                    cmp.on('afterrender',function(panel){
                        var form = panel.child('form').getForm();
                        var panelGrid = panel.child('grid');
//                        panelGrid.getView().on('expandbody', function (rowNode, record, expandRow, eOpts){
//                            if(!record.get('moreInfo')){
//                                var appInsID = record.get('appInsID');
//                                var tzExpandParams = '{"ComID":"TZ_TJR_MANAGER_COM","PageID":"TZ_TJR_DETAIL_STD","OperateType":"tzLoadExpandData","comParams":{"classID":"'+classID+'"}}';
//                                Ext.tzLoad(tzExpandParams,function(respData){
//                                        if(panelGrid.getStore().getModifiedRecords().length>0){
//                                            record.set('moreInfo',respData);
//                                        }else{
//                                            record.set('moreInfo',respData);
//                                            panelGrid.getStore().commitChanges( );
//                                        }
//                                    },panelGrid
//                                );
//                            }
//                        });
                        var tzParams = '{"ComID":"TZ_TJR_MANAGER_COM","PageID":"TZ_TJR_DETAIL_STD","OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';
                        Ext.tzLoad(tzParams,function(respData){
                            var formData = respData.formData;
                            form.setValues(formData);
//                            var tzStoreParams = '{"classID":"'+classID+'","batchID":"'+batchID+'"}';
//                            panelGrid.store.tzStoreParams = tzStoreParams;
//                            panelGrid.store.load();
//                            panelGrid.store.tzStoreParams = '{"cfgSrhId":"TZ_TJR_MANAGER_COM.TZ_TJR_DETAIL_STD.TZ_GD_TJRINF_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "' + classID + '","TZ_BATCH_ID-operator": "01","TZ_BATCH_ID-value": "' + batchID + '"}}';
//                            panelGrid.store.load();
                        });
                    });

                    tab = contentPanel.add(cmp);

                    contentPanel.setActiveTab(tab);

                    Ext.resumeLayouts(true);

                    if (cmp.floating) {
                        cmp.show();
                    }
                }
            }
        })
	},

	//查看报名表
    viewApplicationForm: function(grid, rowIndex,colIndex){
        Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_APP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }

        var store = grid.getStore();
        var record = store.getAt(rowIndex);
        var classID=record.get("classId");
        var oprID=record.get("oprID");
        var appInsID=record.get("appInsID");
        
        if(appInsID!=""){
            var tzParams='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+appInsID+'"}}';
            var viewUrl =Ext.tzGetGeneralURL()+"?tzParams="+encodeURIComponent(tzParams);
            var mask ;
            var win = new Ext.Window({
                name:'applicationFormWindow',
                title : Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.viewApplicationForm","查看报名表"),
                maximized : true,
                controller:'recommendMg',
                classID :classID,
                oprID :oprID,
                appInsID : appInsID,
                gridRecord:record,
                width : Ext.getBody().width,
                height : Ext.getBody().height,
                autoScroll : true,
                border:false,
                bodyBorder : false,
                isTopContainer : true,
                modal : true,
                resizable : false,
                items:[
                    new Ext.ux.IFrame({
                        xtype: 'iframepanel',
                        layout: 'fit',
                        style : "border:0px none;scrollbar:true",
                        border: false,
                        src : viewUrl,
                        height : "100%",
                        width : "100%"
                    })
                ],
                buttons: [ {
                    text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.audit","审批"),
                    iconCls:"send",
                    handler: "auditApplicationForm"
                },
                    {
                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
                        iconCls:"close",
                        handler: function(){
                            win.close();
                        }
                    }]
            })
            win.show();
        }else{
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.cantFindAppForm","找不到该报名人的报名表"));
        }
    },	
	onComRegClose: function(btn){
		//关闭窗口
		this.getView().close();
	},
});
