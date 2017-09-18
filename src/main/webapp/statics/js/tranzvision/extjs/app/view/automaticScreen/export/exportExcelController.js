Ext.define('KitchenSink.view.automaticScreen.export.exportExcelController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.exportExcelController', 
	
    //确认导出
	ensureExport: function(btn){
		var win = btn.findParentByType('exportExcelWindow');
		var form = btn.findParentByType('form');
		var formRec = form.getForm().getValues();
		
		if(form.isValid()){
			var exportObj = win.exportObj;
			exportObj.comParams.fileName = formRec.FileName;
			
			var jsonData = Ext.JSON.encode(exportObj);
			Ext.tzSubmit(jsonData,function(respDate){
				var tabPanel = win.lookupReference("packageTabPanel");
				tabPanel.child('grid').getStore().reload();
		        tabPanel.setActiveTab(1);
		        
			},"导出自动初筛结果成功",true,this);
		}
	},
	
	//搜索历史导出记录
	exportQuery: function(btn){
		var win = btn.findParentByType('exportExcelWindow');
		var classId = win.classId;
		var batchId = win.batchId;
		var classBatch = classId+"-"+batchId;
		
		Ext.tzShowCFGSearch({            
           cfgSrhId: 'TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.TZ_ZDCS_DC_V', 
		   condition:
            {
                "TZ_DLZH_ID": TranzvisionMeikecityAdvanced.Boot.loginUserId,
                "TZ_CLASS_BATCH": classBatch
            },   
           callback: function(seachCfg){
	        	var searchObj = eval('(' + seachCfg + ')');
	        	searchObj.type="expHistory";
	
	        	seachCfg = Ext.JSON.encode(searchObj);

                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });   
	},
	
	//删除导出记录
	exportDelete: function(btn){
        var me=this;
        var win = btn.findParentByType("window");
        var tabPanel = win.lookupReference("packageTabPanel");
        //选中行
        var selList = tabPanel.child("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示", "请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm("确认", "您确定要删除所选记录吗?", function(btnId){
                if(btnId == 'yes'){
                    var store = btn.findParentByType('grid').store;
                    store.remove(selList);
                }
            },this);
        }
    },
	
    //下载导出结果
	downloadFile: function(grid, rowIndex){
        var store = grid.getStore();
        var record = store.getAt(rowIndex);
        console.log(record);
        var procInsId=record.get("procInsId");

        if(procInsId != ""){
        	 var tzParams = '{"ComID":"TZ_AUTO_SCREEN_COM","PageID":"TZ_AUTO_SCREEN_STD","OperateType":"DOWNLOAD","comParams":{"procInsId":"'+ procInsId +'"}}';
             //保存数据
             Ext.tzLoad(tzParams,function(respData){
                 var filePath = respData.filePath;
                 //window.open(filePath, "download","status=no,menubar=yes,toolbar=no,location=no");
                 if(filePath == ""){
                	 Ext.Msg.alert("提示","文件不存在");
                 }else{
                	 window.location.href=filePath;
                 }
             });
        	
        }else{
            Ext.MessageBox.alert("提示", "找不到附件");
        }
    },
    
    //保存删除
    exportGridSave: function(btn){
        //组件注册信息列表
         var grid = btn.findParentByType("grid");
         //组件注册信息数据
         var store = grid.getStore();
         //删除json字符串
         var removeJson = "";
         //删除记录
         var removeRecs = store.getRemovedRecords();

         for(var i=0;i<removeRecs.length;i++){
             if(removeJson == ""){
                 removeJson = Ext.JSON.encode(removeRecs[i].data);
             }else{
                 removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
             }
         }
         var comParams ="";
         if(removeJson != ""){
              comParams = '"delete":[' + removeJson + "]";
         }

         //提交参数
         var tzParams = '{"ComID":"TZ_AUTO_SCREEN_COM","PageID":"TZ_AUTO_SCREEN_STD","OperateType":"U","comParams":{'+comParams+'}}';
         //保存数据
         Ext.tzSubmit(tzParams,function(){
             store.reload();
         },"",true,this);
     }
});