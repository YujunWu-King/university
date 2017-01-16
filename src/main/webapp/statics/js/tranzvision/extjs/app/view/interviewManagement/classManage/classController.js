Ext.define('KitchenSink.view.interviewManagement.classManage.classController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.iterviewMgClass',
    interviewArrange:function(grid, rowIndex, colIndex){	
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MS_ARR_MG_COM"]["TZ_CHS_BATCH_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CHS_BATCH_STD，请检查配置。');
            return;
        }

        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;
		
		var win = this.lookupReference('msBatchListWindow');
		if (!win) {
				Ext.syncRequire(className);
				ViewClass = Ext.ClassManager.get(className);
				//新建类
			win = new ViewClass();
			this.getView().add(win);
		}
		var windowgrid = this.lookupReference('msBatchWindowGrid');
				
		var tzStoreParams = '{"classID":"'+classID+'"}';
		windowgrid.store.tzStoreParams = tzStoreParams;
		windowgrid.store.load();		

		win.show();

    },
    onPanelClose:function(btn){
        btn.up('panel').close();
    },
	chooseMsBatch:function(grid, rowIndex, colIndex){
        var batchWindow = grid.up('window');

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MS_ARR_MG_COM"]["TZ_MS_CAL_ARR_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MS_CAL_ARR_STD，请检查配置。');
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

        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;
		var batchID = record.data.batchID;
		//Ext.MessageBox.alert('提示', '班级编号：'+ classID + '面试批次编号：'+ batchID);

        cmp = new ViewClass();
        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();
            var panelgrid = panel.child('form').child('grid');
            
            /*
            panelgrid.getView().on('expandbody', function (rowNode, record, expandRow, eOpts){
                if(!record.get('moreInfo')){
                    var classID = record.get('classID');
                    var batchID = record.get('batchID');
                    var msJxNo = record.get('msJxNo');
                    var msOprId = record.get('msOprId');
                    var msOrderState = record.get('msOrderState');
                    //msOrderState="B";
                    if (msOrderState=='B'){
                        var moreInfo={"city":record.data.city,"country":record.data.country,"lxEmail":record.data.lxEmail,"timezone":record.data.timezone,"timezoneDiff":record.data.timezoneDiff,"localStartDate":record.data.localStartDate,"localFinishDate":record.data.localFinishDate};
                        record.set('moreInfo',moreInfo); 
                    }else{
                        record.set('moreInfo',"{}");
                    }
                }
            });

            panelgrid.on("cellclick",function( table,td, cellIndex, record, tr, rowIndex, e, eOpts ){
                if(panelgrid.columns[cellIndex-2]){
                    var removeMsArrInfoDataIndex = panelgrid.columns[cellIndex-2].dataIndex;
                    if ("removeMsArrInfo"==removeMsArrInfoDataIndex){
                        if (record.data.msOprId==""){
                            Ext.MessageBox.alert('提示', '当前时间段未安排考生.');
                        }else{
                            record.set("localStartTime","");
                            record.set("localFinishTime","");
                            record.set("skypeId","");
                            record.set("msClearOprId",record.data.msOprId);
                            record.set("msOprId","");
                            record.set("msOprName","");
                            record.set("msOrderState","");
                            record.set("msConfirmState","");
                            record.set("sort","");
                            record.set("releaseOrUndo","");
                            record.set('moreInfo',"{}");
                        }
                    }
                }
            });
			*/
            var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_CAL_ARR_STD","OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';
            Ext.tzLoad(tzParams,function(responseData){
            	console.log(responseData);
                form.setValues(responseData);
                var zl_grid = panel.down('grid[name=msjh_grid]');
				Params= '{"classID":"'+classID+'","batchID":"'+batchID+'"}';
				zl_grid.store.tzStoreParams = Params;
				zl_grid.store.load();
            });
        });

        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }

        batchWindow.hide();
    },
    //查询
    queryClassInfo:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_MS_ARR_MG_COM.TZ_MSGL_CLASS_STD.TZ_CLASS_OPR_V',
            condition:{
                TZ_DLZH_ID: TranzvisionMeikecityAdvanced.Boot.loginUserId,
                TZ_JG_ID: Ext.tzOrgID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
	//关闭窗口
	onWindowClose: function(btn){
		var win = btn.findParentByType("window");
		win.close();
	}
});
