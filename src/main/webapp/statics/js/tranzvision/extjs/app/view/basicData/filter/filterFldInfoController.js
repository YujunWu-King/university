Ext.define('KitchenSink.view.basicData.filter.filterFldInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.filterFldInfo', 
    addFld: function(btn) {
    	var formValues = this.getView().child("form").getForm().getValues();
    	var ComID = formValues['ComID'];
    	var PageID = formValues['PageID'];
    	var ViewMc = formValues['ViewMc'];
    	var FieldMc = formValues['FieldMc'];
        var fldGrid = this.getView().child("form").child("tabpanel").getActiveTab();
        var fldCellEditing = fldGrid.getPlugin('fldCellEditing');
        var fldStore =  fldGrid.getStore();
        fldCellEditing.cancelEdit();
        var r = Ext.create('KitchenSink.view.basicData.filter.fltprmFldModel', {
            ComID:ComID,
            PageID:PageID,
            ViewMc:ViewMc,
            FieldMc:FieldMc,
            orderNum: "NEXT",
            FieldGL: "请选择",
            fieldDesc: ""
        });

        fldStore.insert(0,r);
        fldCellEditing.startEditByPosition({row: 0,column: 6});
    },
    fieldChange:function(view,newValue,oldValue){
    	var store = this.getView().child("form").child("tabpanel").getActiveTab().getStore();
    	var oldRowIndex = store.find("FieldGL",oldValue);
		var newRowIndex = store.find("FieldGL",newValue);
		var index,fieldDesc;
		if(newRowIndex == -1 || this.getView().newDate == oldValue){
			index = view.store.find("TZ_FILTER_FLD",newValue);
			if(index == -1){
				fieldDesc = "";
			}else{
				fieldDesc = view.store.getAt(index).get("LONGNAME");
			}
			if(this.getView().newDate == oldValue){
				oldRowIndex = this.getView().changeNum;
			}
			var selRec = store.getAt(oldRowIndex);
			var r = Ext.create('KitchenSink.view.basicData.filter.fltprmFldModel', {
				orderNum:selRec.get("orderNum"),
	            ComID:selRec.get("ComID"),
	            PageID:selRec.get("PageID"),
	            ViewMc:selRec.get("ViewMc"),
	            FieldMc:selRec.get("FieldMc"),
	            FieldGL:newValue,
	            fieldDesc:fieldDesc
	        });
			store.removeAt(oldRowIndex);
			store.insert(oldRowIndex,r);
			this.getView().changeNum = "";
    		this.getView().newDate = "";
		}else{
			Ext.Msg.alert("提示","您选择的字段已经存在。");
			this.getView().changeNum = oldRowIndex;
    		this.getView().newDate = newValue;
			view.setValue(oldValue);
		}
    },
    comRender:function(com){
		var formValues = this.getView().child("form").getForm().getValues();
    	var ComID = formValues['ComID'];
    	var PageID = formValues['PageID'];
    	var ViewMc = formValues['ViewMc'];
    	var store = new KitchenSink.view.common.store.comboxStore({
			recname: 'TZ_FILPRM_FLD_V',
			condition:{
				TZ_COM_ID:{
					value:ComID,
					operator:"01",
					type:"01"
				},
				TZ_PAGE_ID:{
					value:PageID,
					operator:"01",
					type:"01"
				},
				TZ_VIEW_NAME:{
					value:ViewMc,
					operator:"01",
					type:"01"
				}
			},
			result:'TZ_FILTER_FLD,TZ_FILTER_FLD,LONGNAME'
		});
		com.setStore(store);
	},
    deleteFlds: function(btn){
        //资源信息列表
        var grid = this.getView().child("form").child("tabpanel").getActiveTab();
        //选中行
        var selList = grid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var store = grid.store;
                    store.remove(selList);
                }
            },this);
        }
    },
    onFilterInfoSave: function(btn){
			var from = this.getView().child("form").getForm();
			
			if (from.isValid()) {
				var actType = this.getView().actType;
				var comSiteParams = from.getValues();
				var ComID = comSiteParams['ComID'];
				var PageID = comSiteParams['PageID'];
				var ViewMc = comSiteParams['ViewMc'];
				var FieldMc = comSiteParams['FieldMc'];
				var comParams = "";
				
				if(comSiteParams["fldIsDown"]){
					comSiteParams["fldIsDown"] = "1";
				}else{
					comSiteParams["fldIsDown"] = "0";
				}
				if(comSiteParams["fldReadonly"]){
					comSiteParams["fldReadonly"] = "1";
				}else{
					comSiteParams["fldReadonly"] = "0";
				}
				if(comSiteParams["fldHide"]){
					comSiteParams["fldHide"] = "1";
				}else{
					comSiteParams["fldHide"] = "0";
				}		
				//列表信息
				var tabpanel = this.getView().child("form").child("tabpanel");
				var grid = tabpanel.getActiveTab();
				var grid1 = tabpanel.down('grid[name=searchFld]');
				var grid2 = tabpanel.down('grid[name=promptFld]');
				
				var queryID;
				/*
				if(grid.name == "searchFld"){
					queryID = "1";
				}
				if(grid.name == "promptFld"){
					queryID = "2";
				}
				*/
				
				var store1 = grid1.getStore();
				
				var editJson1="";
				var removeJson1="";
				//if(queryID == "1"){;
					for(var i =0;i<store1.getCount();i++){
						var rec1 = store1.getAt(i);
						var isOprt1 = rec1.get('isOprt');
						if(isOprt1 == "1"){
							var isQy = rec1.get('isQy');
							if(isQy != 1){
								Ext.Msg.alert("提示","未启用的字段不能设置为默认字段。");
								return;
							}
						}
					}
					//列表中修改的记录
					var mfRecs1 = store1.getModifiedRecords(); 
					for(var i=0;i<mfRecs1.length;i++){
						if(editJson1 == ""){
							editJson1 = Ext.JSON.encode(mfRecs1[i].data);
						}else{
							editJson1 = editJson1 + ','+Ext.JSON.encode(mfRecs1[i].data);
						}
					}
					//列表中删除的数据
					/*
					var removeRecs1 = store1.getRemovedRecords();
					for(var i=0;i<removeRecs1.length;i++){
						if(removeJson1 == ""){
							removeJson1 = Ext.JSON.encode(removeRecs1[i].data);
						}else{
							removeJson1 = removeJson1 + ','+Ext.JSON.encode(removeRecs1[i].data);
						}
					}
					*/
				//}
			
				var editJson2="";
				var removeJson2="";
				var store2 = grid2.getStore();
				//if(queryID == "2"){;
					for(var i =0;i<store2.getCount();i++){
						var rec2 = store2.getAt(i);
						var FieldGL = rec2.get('FieldGL');
						if(FieldGL == "" || FieldGL == "请选择"){
							Ext.Msg.alert("提示","请选择字段。");
							return;
						}
						if(editJson2 == ""){
							editJson2 = Ext.JSON.encode(rec2.data);
						}else{
							editJson2 = editJson2 + ','+Ext.JSON.encode(rec2.data);
						}
					}
					//列表中删除的数据
					var removeRecs2 = store2.getRemovedRecords();
					for(var i=0;i<removeRecs2.length;i++){
						if(removeJson2 == ""){
							removeJson2 = Ext.JSON.encode(removeRecs2[i].data);
						}else{
							removeJson2 = removeJson2 + ','+Ext.JSON.encode(removeRecs2[i].data);
						}
					}
				//}
			
				//修改json字符串
				//comParams = '"update":[{"update":' + Ext.JSON.encode(comSiteParams) + ',"queryID":"1","updateList":[' + editJson1 + '],"deleteList":[' + removeJson1 + ']}]';
				comParams = '"update":[{"update":' + Ext.JSON.encode(comSiteParams) + ',"updateList":{"data1":[' + editJson1 + '],"data2":['+ editJson2 + '],"data3":['+ removeJson2 + ']}}]';
				
				//提交参数
				var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_FILTER_FLD_STD","OperateType":"U","comParams":{'+comParams+'}}';
				
				Ext.tzSubmit(tzParams,function(){
					//var contentPanel;
					//contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
					//contentPanel.child("filterInfoPanel").child("grid").store.reload();
					store1.reload();
					store2.reload();
				},"",true,this);
				if(btn == "btn_Ensure"){
					this.getView().close();
				}
			}
    },
    onFilterInfoEnsure: function(){
		this.onFilterInfoSave("btn_Ensure");
    },
    onFilterInfoClose: function(){
		this.getView().close();
	},
	//放大镜搜索Prompt字段名称
	pmtSearchPromptFldTmp: function(btn){
		var form = this.getView().child("form").getForm();
		var comSiteParams = form.getValues();
		var promptTab = comSiteParams['promptTab'];
		
		if(promptTab == ""){
			Ext.Msg.alert("提示","请先填写Prompt表名称");
			return;
		}
				
		Ext.tzShowPromptSearch({
			recname: 'TZ_FIELDRECD_VW',
			searchDesc: '搜索Prompt字段名称',
			maxRow:20,
			condition:{
				presetFields:{
					RECNAME:{
						value:promptTab,
						type: '01'	
					}
				},
				srhConFields:{
					FIELDNAME:{
						desc:'字段名称',
						operator:'07',
						type:'01'		
					},
					LONGNAME:{
						desc:'字段描述',
						operator:'07',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				FIELDNAME: 'Prompt字段名称',
				LONGNAME: '字段描述'
			},
			multiselect: false,
			callback: function(selection){
				form.findField("promptFld").setValue(selection[0].data.FIELDNAME);
				//form.findField("ComIDName").setValue(selection[0].data.TZ_COM_MC);
			}
		});	
	},
	//放大镜搜索Prompt描述字段名称
	pmtSearchPromptDescTmp: function(btn){
		var form = this.getView().child("form").getForm();
		var comSiteParams = form.getValues();
		var promptTab = comSiteParams['promptTab'];
		
		if(promptTab == ""){
			Ext.Msg.alert("提示","请先填写Prompt表名称");
			return;
		}
		
		Ext.tzShowPromptSearch({
			recname: 'TZ_FIELDRECD_VW',
			searchDesc: '搜索Prompt字段名称',
			maxRow:20,
			condition:{
				presetFields:{
					RECNAME:{
						value:promptTab,
						type: '01'	
					}
				},
				srhConFields:{
					FIELDNAME:{
						desc:'字段名称',
						operator:'07',
						type:'01'		
					},
					LONGNAME:{
						desc:'字段描述',
						operator:'07',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				FIELDNAME: 'Prompt描述字段名称',
				LONGNAME: '字段描述'
			},
			multiselect: false,
			callback: function(selection){
				form.findField("promptDesc").setValue(selection[0].data.FIELDNAME);
				//form.findField("ComIDName").setValue(selection[0].data.TZ_COM_MC);
			}
		});	
	}
});