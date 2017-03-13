Ext.define('KitchenSink.view.interviewManagement.interviewArrange.expMsArrToExcelController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.expMsArrToExcelController',

    /**
     * 功能；请求AE导出
     * 刘阳阳  2015-12-14
     */
    exportEnsure: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        var selList = win.selList;
        var form = win.child("form").getForm();
        if (form.isValid()) {
            var appFormModalID = win.modalID;
            var excelTpl = form.getValues()['excelTpl'];
            var excelName = form.getValues()['excelName'];
            
            if(selList && selList.length>0){
            	var classID = selList[0].get("classID");
        		var batchID = selList[0].get("batchID");
            	
                var selListArr = [];
                for(var i=0;i<selList.length;i++){
                	selListArr.push(selList[i].get("msJxNo"));
                }

            	var tzParamsObj = {
            		ComID: "TZ_MS_ARR_MG_COM",
            		PageID: "TZ_MS_ARR_EXP_STD",
            		OperateType: "U",
            		comParams:{
            			add:[{
            				excelTpl: excelTpl,
            				appFormModalID: appFormModalID,
            				excelName: excelName,
            				classID: classID,
            				batchID: batchID,
            				selList: selListArr
            			}]
            		}
            	};
            	var tzParams = Ext.JSON.encode(tzParamsObj);
            	
                var tabPanel = win.lookupReference("exportExcelTabPanel");
                Ext.tzSubmit(tzParams,function(responseData){
                    tabPanel.setActiveTab(1);
                    tabPanel.child("grid").store.reload();
                },"",true,this);
            }else{
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.prompt","提示"), Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.noApplicantsSelected","没有选中记录，无法导出Excel"));
            }

        }
    },
    /**
     * 功能：可配置搜索
     * 刘阳阳  2015-12-11
     */
    queryExcel:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.TZ_GD_MSARRDC_V',
            condition:
            {
                TZ_DLZH_ID: TranzvisionMeikecityAdvanced.Boot.loginUserId
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    /**
     * 功能：删除
     * 刘阳阳  2015-12-16
     */
    deleteExcel:function(btn){
        var msArrExcelGrid = btn.up('grid');
        //选中行
        var selList = msArrExcelGrid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var msArrExcelGridStore =msArrExcelGrid.store;
                    msArrExcelGridStore.remove(selList);
                }
            },this);
        }
    },
    /**
     * 功能：下载导出文件
     * 刘阳阳  2015-12-11
     */
    downloadFile: function(grid, rowIndex){
        var store = grid.getStore();
        var record = store.getAt(rowIndex);
        var viewUrl = record.get("fileUrl");
        if(viewUrl.length>0){
            window.open(viewUrl, "download","status=no,menubar=yes,toolbar=no,location=no");
        }else{
            //do nothing
        }
    },
    /**
     * 功能：保存
     * 刘阳阳  2015-12-16
     */
    exportExcelWindowSave:function(btn){
        var msArrExcelGrid = btn.up('grid');
        var selectedRecs = msArrExcelGrid.store.getRemovedRecords();
        var selectedJson="";
        if (selectedRecs.length>0){
            for(var i=0;i<selectedRecs.length;i++){
                if(selectedJson == ""){
                    selectedJson = Ext.JSON.encode(selectedRecs[i].data);
                }else{
                    selectedJson = selectedJson + ','+Ext.JSON.encode(selectedRecs[i].data);
                }
            }

            if(selectedJson != ""){
                var comParams = '"delete":[' + selectedJson + "]";
            }
            //提交参数.
            var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_ARR_EXP_STD","OperateType":"U","comParams":{'+comParams+'}}';
            //保存数据
            Ext.tzSubmit(tzParams,function(responseData){
                if(responseData.result='success'){
                    //刷新grid
                    msArrExcelGrid.store.load();
                };
            },"",true,this);
        }else{
            Ext.tzShowToast('保存成功','提示','t','#ffffff');
        };
    },
	
    /**
     *  功能：关闭窗口
     *  刘阳阳  2015-12-11
     * @param btn
     */
    exportExcelWindowClose: function(btn){
        var win = btn.findParentByType("window");
        win.close();
    }
})