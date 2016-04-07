Ext.define('KitchenSink.view.classManage.clsAttr.clsattrController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.clsattrController', 
	//添加一行数据 
    addData: function(btn) {
		var Grid = btn.findParentByType("grid");
        var clsAttrCellEditing = Grid.getPlugin('clsAttrCellEditing');
        var clsAttrStore =  Grid.getStore();
        var rowCount = clsAttrStore.getCount();
            //console.log(applyItemGrid);
        clsAttrCellEditing.cancelEdit();
            // Create a model instance
            var r = Ext.create('KitchenSink.view.classManage.clsAttr.clsattrModel', {
                attrSeq:rowCount+1,
                attrValue: "NEXT",
				attrDesc:"",
                attrType: "L",
				attrTypeDesc: "下拉框",
				attrEnabled: "true"
            });

        clsAttrStore.insert(rowCount,r);
        clsAttrCellEditing.startEdit(r, 1);	
    },
	 addOneData: function(view,rowIndex) {
        var Grid = view.findParentByType("grid");
        var clsAttrCellEditing = Grid.getPlugin('clsAttrCellEditing');
        var clsAttrStore =  Grid.getStore();
        var rowCount = rowIndex+1;
        //console.log(applyItemGrid);
        clsAttrCellEditing.cancelEdit();
        // Create a model instance
         var r = Ext.create('KitchenSink.view.classManage.clsAttr.clsattrModel', {
                attrSeq:rowCount+1,
                attrValue: "NEXT",
				attrDesc:"",
                attrType: "L",
				attrTypeDesc: "下拉框",
				attrEnabled: "true"
            });

        clsAttrStore.insert(rowCount,r);
        clsAttrCellEditing.startEdit(r, 1);
    },
	//添加属性-新开页面；
    addAttrPage: function(btn) {
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BJSX_COM"]["TZ_GD_BJSX_NEW_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		//var _bj_id=this.getView().class_id;
		//var _lc_id=this.getView().bmlc_id;
        var count = btn.findParentByType("grid").getStore().getCount();
		var win = this.lookupReference('addAttrWindow');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            this.getView().add(win);
        }
		var form = win.child('form').getForm();
		form.findField('attrSeq').setValue(count+1);
		
        win.show();
    },
	//删除个性化属性 字段
	deleteData: function(view, rowIndex){
		Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗") , function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	//删除个性化属性 字段
	deleteDatas: function(){
	   //选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗"), function(btnId){
				if(btnId == 'yes'){					   
				   var store = this.getView().store;
				   store.remove(selList);
				}												  
			},this);   
	   }
	},
	//删除个性化设置 下拉框类型 下拉值
	deleteItem: function(view, rowIndex){
		Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗") , function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	//删除个性化设置 下拉框类型 下拉值
	deleteItems: function(){
	   //选中行
	   var setDropdownWindowGrid = this.lookupReference('setDropdownWindowGrid');
	   var selList = setDropdownWindowGrid.getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxzyscdjl","请选择要删除的记录") );
			return;
	   }else{
			Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗") , function(btnId){
				if(btnId == 'yes'){					   
				   var store = setDropdownWindowGrid.store;
				   store.remove(selList);
				}												  
			},this);   
	   }

	},
	addDDList: function(btn) {
		var Grid = btn.findParentByType("grid");
        var attrItemCellEditing = Grid.getPlugin('attrItemCellEditing');
        var attrItemStore =  Grid.getStore();
        var rowCount = attrItemStore.getCount() + 1;
        
            console.log(rowCount);
        attrItemCellEditing.cancelEdit();
            // Create a model instance
            var r = Ext.create('KitchenSink.view.classManage.clsAttr.setDropdownModel', {
                attrValue:"",
                attrDropDownId: "",
				attrDropDownDesc:"",
				attrDDEnabled: "Y",
				attrOrder:rowCount
            });

        attrItemStore.insert(rowCount,r);
        attrItemCellEditing.startEdit(r, 1);	
    },
	//个性化属性 下拉框类型 下拉值
	addDDItem:function(view,rowIndex){
	
        var Grid = view.findParentByType("grid");
        var attrItemCellEditing = Grid.getPlugin('attrItemCellEditing');
        var attrItemStore =  Grid.getStore();
        var rowCount = rowIndex+1;
        //console.log(applyItemGrid);
        attrItemCellEditing.cancelEdit();
        // Create a model instance
         var r = Ext.create('KitchenSink.view.classManage.clsAttr.setDropdownModel', {
                attrValue:"",
                attrDropDownId: "",
				attrDropDownDesc:"",
				attrDDEnabled: "Y"
            });

        attrItemStore.insert(rowCount,r);
        attrItemCellEditing.startEdit(r, 1);
	
	},
	//设置 下拉列表
	setDDlist:function(view,rowIndex){
	
		var grid =view.findParentByType("grid");
	
		var record=view.findParentByType("grid").store.getAt(rowIndex);
		
		var attrType=record.get("attrType");
		var attrValue=record.get("attrValue");
		
		if (attrType=="L"){
			//下拉框 类型的才能够设置			
			//console.log(attrType+attrOrgId+attrValue);
			
			var rec = grid.getStore().getAt(rowIndex);
			var applyItemId = rec.data.applyItemId;
						
			if(this.getView().actType == "add"){
				Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxbcdqymxx","请先保存当前页面信息"));
				return;
			}		
			
			//var activityId = this.getView().child("form").getForm().findField("activityId").getValue();
			
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BJSX_COM"]["TZ_GD_SETLIST_STD"];
			if( pageResSet == "" || typeof(pageResSet) == "undefined" ){
				Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || typeof(className) == "undefined"  ){
				Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
				return;
			}
			
			var win = this.lookupReference('setDropdownWindow');
			
			if (!win) {
					//className = 'KitchenSink.view.activity.applyOptionsWindow';
					Ext.syncRequire(className);
					ViewClass = Ext.ClassManager.get(className);
					//新建类
				win = new ViewClass();
				this.getView().add(win);
			}
			
			//操作类型设置为更新
			//win.actType = "update";
			//参数
			//var tzParams = '{"ComID":"TZ_HD_MANAGER_COM","PageID":"TZ_HD_OPTIONS_STD","OperateType":"QF","comParams":{"activityId":"'+activityId+'"}}';
			var form = win.child("form").getForm();
			form.reset();
			form.setValues({"attrType":attrType,"attrValue":attrValue});
			
			grid = this.lookupReference('setDropdownWindowGrid');
					//console.log(grid);
			//var tzStoreParams = "{'attrType':'"+attrType+"','attrValue':'"+attrValue+"'}";
			var tzStoreParams = '{"attrType":"'+attrType+'","attrValue":"'+attrValue+'"}';
			grid.store.tzStoreParams = tzStoreParams;
			grid.store.load();		
			
			grid.attrEnabledStore=new KitchenSink.view.common.store.appTransStore("TZ_ATTR_ENABLED");
			win.show();
			
		
		}
	},
	saveClsAttrInfo:function(btn){
		//console.log("test");
        //更新操作参数
        var comParams = "";
        //修改json字符串
        var editJson = "";
        //信息列表
        var grid = btn.findParentByType("grid");
        //信息数据
        var store = grid.getStore();
        //修改记录
        var mfRecs = store.getModifiedRecords();
        var clsAttrCellEditing = grid.getPlugin('clsAttrCellEditing');
        for(var i=0;i<mfRecs.length;i++){
            /*标签名称不能为空*/
            var attrValueLength=mfRecs[i].get("attrValue").length;
            if(attrValueLength>18){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.sxzbndy18gzf","属性值不能大于18个字符"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            clsAttrCellEditing.startEdit(mfRecs[i], 2);
                        }
                    }
                )
                return;
            }else{
                if(mfRecs[i].get("attrValue")==""){
                    if(attrValueLength<1){
                        Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.sxzbnwk","属性值不能为空"),
                            function(e){
                                if(e == "ok"|| e == "OK" || e == "确定"){
                                    clsAttrCellEditing.startEdit(mfRecs[i], 2);
                                }
                            }
                        )
                        return;
                    }
                }
            }

            //记录查重
            var attrValue = mfRecs[i].get("attrValue");
            var attrDesc = mfRecs[i].get("attrDesc");
            var attrValueCount =0;
            var attrDescCount =0;

			if (attrDesc==""){
				 Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.sxzbnwk","属性值不能为空"),
                            function(e){
                                if(e == "ok"|| e == "OK" || e == "确定"){
                                    clsAttrCellEditing.startEdit(mfRecs[i], 2);
                                }
                            }
                        )
                 return;
			}
				
            var recIndex = store.findBy(function(record,id){
                if(record.get("attrValue")==attrValue){
                    attrValueCount++;
                    if(attrValueCount>1){
                        return true;
                    }
                };
                if(record.get("attrDesc")==attrDesc){
                    attrDescCount++;
                    if(attrDescCount>1){
                        return true;
                    }
                };
            },0);

            /*if(attrValueCount>1){
                Ext.MessageBox.alert('提示', '属性值出现重复！',
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            clsAttrCellEditing.startEdit(mfRecs[i], 2);
                        }
                    }
                )
                return;
            }else if(attrDescCount>1){
                Ext.MessageBox.alert('提示', '属性名称出现重复！',
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            clsAttrCellEditing.startEdit(mfRecs[i], 3);
                        }
                    }
                )
                return;
            }*/
			if(attrDescCount>1){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.sxmccxcf","属性名称出现重复"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            clsAttrCellEditing.startEdit(mfRecs[i], 3);
                        }
                    }
                )
                return;
            }
            if(editJson == ""){
                editJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }
        }
        if(editJson != "")comParams = '"update":[' + editJson + "]";

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
        if(removeJson != ""){
            if(comParams == ""){
                comParams = '"delete":[' + removeJson + "]";
            }else{
                comParams = comParams + ',"delete":[' + removeJson + "]";
            }
        }

        //提交参数
        var tzParams = '{"ComID":"TZ_GD_BJSX_COM","PageID":"TZ_GD_BJSX_STD","OperateType":"U","comParams":{'+comParams+'}}';
			
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
	},
	onreCheck:function(btn){
		//var grid = btn.findParentByType("setDropdownWindow").child("grid");
		var grid = btn.findParentByType("setDropdownWindow").child("form").child("grid");

        //信息数据
        var store = grid.getStore();
        //修改记录
        //var mfRecs = store.getModifiedRecords();
		var mfRecs = store.data;
        var attrItemCellEditing = grid.getPlugin('attrItemCellEditing');
		//console.log(attrItemCellEditing.getstore().getAt(1).data);
        for(var i=0;i<store.data.length;i++){
			//console.log(store.getAt(i).data);
			
            //var attrValueLength=mfRecs[i].get("attrDropDownId").length;
            //记录查重
            var attrDropDownId = store.getAt(i).get("attrDropDownId");
            var attrDropDownDesc = store.getAt(i).get("attrDropDownDesc");
            
			if (attrDropDownId==""){
				Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_SETLIST_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_SETLIST_STD.xlznotblank","下拉值不能为空"),function(e){
						if(e == "ok"|| e == "OK" || e == "确定"){
							attrItemCellEditing.startEdit(store.getAt(i), 1);
						}
				    }
				)
				return false;
			}
			
			if (attrDropDownDesc==""){
				Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_SETLIST_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_SETLIST_STD.xlzmsnotblank","下拉值描述不能为空"),function(e){
						if(e == "ok"|| e == "OK" || e == "确定"){
						    attrItemCellEditing.startEdit(store.getAt(i), 2);
						}
				    }
				)
				return;
			}
			
            var attrDropItemCount =0;
            var attrDropItemDescCount =0;

            var recIndex = store.findBy(function(record,id){
                if(record.get("attrDropDownId")==attrDropDownId){
                    attrDropItemCount++;
                    if(attrDropItemCount>1){
                        return true;
                    }
                };
                if(record.get("attrDropDownDesc")==attrDropDownDesc){
                    attrDropItemDescCount++;
                    if(attrDropItemDescCount>1){
                        return true;
                    }
                };
            },0);

            if(attrDropItemCount>1){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.xlzcxcf","下拉值出现重复！"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                           // attrItemCellEditing.startEdit(mfRecs[i], 2);
                        }
                    }
                )
                return false;
            }else if(attrDropItemDescCount>1){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.xlmccxcf","下拉名称出现重复！"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                         //   attrItemCellEditing.startEdit(mfRecs[i], 3);
                        }
                    }
                )
                return false;
            }
		}
	},
	//设置下拉值 窗口 保存
	onAttrItemSave: function(btn){
		var bool=this.onreCheck(btn);
		if (bool!=false) {
			//获取窗口
			var win = btn.findParentByType("window");
			//页面注册信息表单
			var form = win.child("form").getForm();
			if (form.isValid()) {
				/*保存页面注册信息*/
				var tzParams = this.getItmeOptionsParams(win);

				Ext.tzSubmit(tzParams,function(responseData){

				},"",true,this);
				
			}
		}
	},	
	//确定
	onAttrItemEnsure: function(btn){
		var bool=this.onreCheck(btn);
		if (bool!=false) {
		
			//获取窗口
			var win = btn.findParentByType("window");
			//页面注册信息表单
			var form = win.child("form").getForm();
			if (form.isValid()) {
				/*保存页面注册信息*/
				var tzParams = this.getItmeOptionsParams(win);

				Ext.tzSubmit(tzParams,function(responseData){
					//重置表单
					form.reset();
					//关闭窗口
					win.close();
				},"",true,this);
			}	
		}	
	},

	getItmeOptionsParams: function(win){
		 //活动信息表单
		var form = win.child("form").getForm();
		
		//属性ID;
		var attrValue = form.findField("attrValue").getValue();

		
		//更新操作参数
		var comParams = "";
		
		//选项grid;
		//var grid = Ext.getCmp('applyItemOptionsGrid');
		var grid = win.down('grid[name=setDropdownWindowGrid]');
		//报名信息项选项数据
		var store = grid.getStore();
	
		//修改记录
		var editJson="";
		var mfRecs = store.getModifiedRecords(); 
		for(var i=0;i<mfRecs.length;i++){
			if(editJson == ""){
				editJson = '{"typeFlag":"ACTOPTIONS","attrValue":"'+attrValue+'","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}else{
				editJson = editJson + ',{"typeFlag":"ACTOPTIONS","attrValue":"'+attrValue+'","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}
		}
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		
		//删除json字符串
		var removeJson = "";
		//删除记录
		var removeRecs = store.getRemovedRecords();
		for(var i=0;i<removeRecs.length;i++){
			//console.log(removeRecs[i].data.applyItemId);
			if(removeJson == ""){
				removeJson = '{"typeFlag":"ACTOPTIONS","attrValue":"'+attrValue+'","data":'+Ext.JSON.encode(removeRecs[i].data)+'}';
			}else{
				removeJson = removeJson +',{"typeFlag":"ACTOPTIONS","attrValue":"'+attrValue+'","data":'+Ext.JSON.encode(removeRecs[i].data)+'}';
	  	}
		}
		if(removeJson != ""){
			if(comParams == ""){
				comParams = '"delete":[' + removeJson + "]";
			}else{
				comParams = comParams + ',"delete":[' + removeJson + "]";
			}
		}
		//提交参数
		
		var tzParams = '{"ComID":"TZ_GD_BJSX_COM","PageID":"TZ_GD_SETLIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
		console.log(tzParams);
        return tzParams;
        
	},
	currAttrEdit: function(view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        //ID
        var attrValue = selRec.get("attrValue");
        //显示资源集合信息编辑页面
        this.editAttrByID(attrValue);
    },
    attrEdit: function() {
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxzytyxgdjl","请选择一条要修改的记录"));
            return;
        }else if(checkLen >1){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.znxzytyxgdjl","只能选择一条要修改的记录"));
            return;
        }
        var attrValue = selList[0].get("attrValue");
        this.editAttrByID(attrValue);
    },
	editAttrByID:function(attrValue){
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BJSX_COM"]["TZ_GD_BJSX_NEW_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
            // </debug>
        }
		
		/*
        cmp = new ViewClass();
        //操作类型设置为更新
		cmp.actType = "update";
		cmp.on('afterrender',function(panel){
			//消息集合定义表单;
			var form = panel.child('form').getForm();
			//form.findField("msgSetID").setReadOnly(true);
			//form.findField("msgSetID").addCls("lanage_1");
			var grid = panel.child('grid');
			var tzParams = '{"ComID":"TZ_GD_BJSX_COM","PageID":"TZ_GD_BJSX_NEW_STD","OperateType":"QF","comParams":{"attrValue":"'+attrValue+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				var formData = responseData.formData;
				form.setValues(formData);
			});
		});
		cmp.show();
		
		*/
		var win = this.lookupReference('addAttrWindow');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
			var form = win.child('form').getForm();

            this.getView().add(win);
        }
		var tzParams = '{"ComID":"TZ_GD_BJSX_COM","PageID":"TZ_GD_BJSX_NEW_STD","OperateType":"QF","comParams":{"attrValue":"'+attrValue+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				var formData = responseData.formData;
				form.setValues(formData);
			});
		
        win.show();
	
    },
	
	onComRegClose: function(btn){
		//关闭窗口
		this.getView().close();
	},
	/*
	onPageRegSave: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			//保存页面注册信息
			this.savePageRegInfo(win);
		}
	},
	onPageRegEnsure: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			//保存页面注册信息
			this.savePageRegInfo(win);
			//重置表单
			form.reset();
			//关闭窗口
			win.close();
		}	
	},
	*/
	ensureClsAttrInfo: function(btn){

		this.saveClsAttrInfo(btn);
		
		this.getView().close();

	},
	//关闭窗口,用户下拉列表值页面
	onPageClose: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		win.close();
	}
	

});