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
//            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_TJR_MANAGER_COM.TZ_TJR_DETAIL_STD.prompt","提示"),Ext.tzGetResourse("TZ_TJR_MANAGER_COM.TZ_TJR_DETAIL_STD.nmyqx","您没有权限"));
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
//            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_TJR_MANAGER_COM.TZ_TJR_DETAIL_STD.prompt","提示"), Ext.tzGetResourse("TZ_TJR_MANAGER_COM.TZ_TJR_DETAIL_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_TJR_DETAIL_STD，请检查配置。');
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

	onComRegClose: function(btn){
		//关闭窗口
		this.getView().close();
	},
});
