Ext.define('KitchenSink.view.enrollProject.proClassify.proClassifyController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.proClassifyController', 
	
	// 在最后一行新增
	onAddInLastRow: function(btn){
		var grid = btn.findParentByType("grid");
		var row = grid.getStore().getCount();
		
        var model = new KitchenSink.view.enrollProject.proClassify.proClassifyModel({
            proTypeId: 'NEXT',
            proTypeName: '',
            proTypeDesc: ''
			//isSaved: 'N'
        });

        grid.getStore().insert(row, model);
        grid.cellEditing.startEditByPosition({
            row: row,
            column: 2
        });
    },
	
	//批量删除
	onDeleteBat: function(btn){
		 //选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert(Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.prompt","提示"),Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.qxzyscdjl","请选择要删除的记录"));
			return;
	   }else{
		  Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.confirm","确认"),Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.nqdyscsxjlm","您确定要删除所选记录吗？") , function(btnId){
				if(btnId == 'yes'){					   
				   var store = this.getView().store;
				   store.remove(selList);
				}												  
			},this);    
	   }
	},
	
	//保存修改
	onSaveData: function(btn){
		var grid = this.getView();
		var isValid = true;
		var nameRepeat = false;

		var modifRecs = grid.store.getModifiedRecords();	
		for (var i=0; i<modifRecs.length; i++){
			//分类名称不能为空
			if (modifRecs[i].data.proTypeName == ""){
				isValid = false;
				break;
			}else {
				var loop = 0, sNum = 0, rowNum = 0;
				//用新增数据行比较，如果有两次相同则重复，重复行可能包含自己
				while (loop < 2){
					if(grid.getStore().find("proTypeName",modifRecs[i].data.proTypeName,rowNum,false,true,true) != -1){
						rowNum = grid.getStore().find("proTypeName",modifRecs[i].data.proTypeName,rowNum,false,true,true) + 1;
						sNum++;//相同数据行数量加1
					}
					loop++;
				}
				if (sNum == 2){
					nameRepeat = true;
					break;	
				}
			}	
		}
		
		if (isValid){
			if (nameRepeat){
				Ext.Msg.alert(Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.prompt","提示"),Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.xmlxmcbncf","项目类型名称不能重复！"));
			} else {
				var tzParams = this.getProClassifyParams();
				Ext.tzSubmit(tzParams,function(respData){
					grid.store.reload();
				},"",true,this);
			}
		} else {
			Ext.Msg.alert(Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.prompt","提示"),Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.xmlxmcbnwk","项目类型名称不能为空！"));
		}
	},
	
	
	getProClassifyParams: function(){
		var updateJson, delJson;
		var grid = this.getView();
		var num = 0;
		
		var tzParams = '{"ComID":"TZ_ZS_XMLBSZ_COM","PageID":"TZ_ZS_XMLBSZ_STD","OperateType":"U","comParams":{';
		//删除数据行
		var removeRecs = grid.store.getRemovedRecords();
		for (var i=0; i<removeRecs.length; i++){
			//删除行JSON报文
			if (i == 0){
				delJson = Ext.JSON.encode(removeRecs[i].data);
			} else {
				delJson += ',' + Ext.JSON.encode(removeRecs[i].data);
			}	
		}
		if (removeRecs.length > 0){
			delJson = '"delete": ['+ delJson +']';
			tzParams += delJson;
			num +=1;
		}
		
		//修改数据行
		var modifRecs = grid.store.getModifiedRecords();	
		for (var i=0; i<modifRecs.length; i++){
			//修改行JSON报文
			if (i == 0){
				updateJson = Ext.JSON.encode(modifRecs[i].data);
			} else {
				updateJson += ',' + Ext.JSON.encode(modifRecs[i].data);
			}
		}
		if (modifRecs.length > 0){
			if (num > 0){
				updateJson = ',"update":	['+ updateJson +']';
			} else {
				updateJson = '"update":	['+ updateJson +']';
			}
			tzParams += updateJson;
		}
		tzParams += '}}';
		return tzParams;
	},
	//可配置搜索
	searchProTypeList: function(btn){     //searchComList为各自搜索按钮的handler event;
        Ext.tzShowCFGSearch({            
           cfgSrhId: 'TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.TZ_PRJ_TYPE_T',
           condition:
            {
                "TZ_JG_ID": Ext.tzOrgID  //设置搜索字段的默认值，没有可以不设置condition;
            },            
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });    
    }
});