Ext.define('KitchenSink.view.siteManage.outsiteManage.menuEditController',{
	extend : 'Ext.app.ViewController',
	alias : 'controller.menuEdit',
	requires : [ 'KitchenSink.view.common.changeDataLanguage','KitchenSink.view.common.synMultiLanguageData' ],
	
	//插入同级节点
	inserMenuItem : function(bt, eOpts) {
		var actType = bt.findParentByType("menuEdit").actType;
		if (actType == "update") {
			var form = bt.findParentByType("form").getForm();
			var menuId = form.findField("menuId").getValue();
			var rootNode = form.findField("rootNode").getValue();
			
			if (menuId == rootNode) {
				Ext.Msg.alert("提示", "不可插入根节点的同级节点");
			} else {
				form.findField("menuPath").setReadOnly(false);
				form.findField("menuPath").removeCls('lanage_1'); 
				
				form.findField("menuType").setReadOnly(false);
				form.findField("menuType").removeCls('lanage_1'); 
				
				
				form.findField("menuPath").hide();
				//form.findField("menuPageName").hide();
				form.findField("isDefault").hide();
				form.findField("defaultPage").hide();

				form.findField("menuId").setValue('');
				form.findField("menuName").setValue('');
				form.findField("menuPath").setValue('');
				form.findField("menuState").setValue('');
				form.findField("menuType").setValue('');
				form.findField("menuTempletId").setValue('');
				form.findField("menuTempletName").setValue('');
				form.findField("menuPageName").setValue('');
				form.findField("isDefault").setValue('');
				form.findField("menuXH").setValue('');
				form.findField("defaultPage").setValue('');
				

				form.findField("NodeType").setValue("Y");
				form.findField("operateNode").setValue(menuId);
				form.findField("rootNode").setValue(rootNode);

				bt.findParentByType("menuEdit").actType = "add";
			}
		} else {
			Ext.Msg.alert("提示", "请先保存当前节点才能添加该节点的同级节点");
		}
	},
	
	//插入子节点
	inserChildMenuItem : function(bt, eOpts) {
		var actType = bt.findParentByType("menuEdit").actType;
		if (actType == "update") {
			
			
			var form = bt.findParentByType("form").getForm();
			var menuType = form.findField("menuType").getValue();
			
			if (menuType == "B") {
				var menuId = form.findField("menuId").getValue();
				var rootNode = form.findField("rootNode").getValue();
				form.findField("menuPath").setReadOnly(false);
				form.findField("menuPath").removeCls('lanage_1'); 
				form.findField("menuType").setReadOnly(false);
				form.findField("menuType").removeCls('lanage_1'); 

			
				form.findField("menuPath").hide();
				//form.findField("menuPageName").hide();
				form.findField("isDefault").hide();
				form.findField("defaultPage").hide();
				

				form.findField("menuId").setValue('');
				form.findField("menuXH").setValue('');
				form.findField("menuName").setValue('');
				form.findField("menuPath").setValue('');
				form.findField("menuState").setValue('');
				form.findField("menuType").setValue('');
				form.findField("menuTempletId").setValue('');
				form.findField("menuTempletName").setValue('');
				form.findField("menuPageName").setValue('');
				form.findField("isDefault").setValue('');
				form.findField("defaultPage").setValue('');

				form.findField("NodeType").setValue("N");
				form.findField("operateNode").setValue(menuId);
				form.findField("rootNode").setValue(rootNode);
				bt.findParentByType("menuEdit").actType = "add";
			} else {
				Ext.Msg.alert("提示", "类型为PAGE的菜单不能添加子节点");
			}
		} else {
			Ext.Msg.alert("提示", "请先保存当前节点才能添加该节点的子节点");
		}
	},

	//删除节点
	removeMenuItem : function(bt, eOpts) {
		var panel = bt.findParentByType("menuEdit");
		var actType = panel.actType;

		var form = panel.child("form").getForm();
		var treepanel = panel.child("treepanel");
		var treepanelStore = treepanel.getStore();

		var menuId = form.findField("menuId").getValue();
		var siteId = form.findField("siteId").getValue();
		var rootNode = form.findField("rootNode").getValue();
		var operateNodeId = form.findField("operateNode").getValue();
		var operateNode = treepanelStore.getNodeById(operateNodeId);
		
		if (menuId == rootNode) {
			Ext.Msg.alert("提示", "不可删除根节点");
		} else {
			Ext.Msg.confirm("确认","是否确认删除当前节点及其子节点",
				function(confm) {
					if (confm == 'yes') {
						if (actType == "add") {
							form.setValues({
								menuId : operateNode.data.id,
								menuName : operateNode.data.text,
								menuPath : operateNode.data.menuPath,
								menuState : operateNode.data.menuState,
								menuType : operateNode.data.menuType,
								menuTempletId : operateNode.data.menuTempletId,
								menuPageName : operateNode.data.menuPageName,
								menuXH : operateNode.data.menuXH,
								isDefault : operateNode.data.isDefault,
								defaultPage : operateNode.data.defaultPage,
								NodeType : "",
								operateNode : "",
								rootNode : rootNode,
								siteId : siteId,
								menuTempletName : operateNode.data.menuTempletName
							});
							
							if (operateNode.data.menuType == "B") {
								//form.findField("menuPageName").hide();
								form.findField("isDefault").hide();
								form.findField("menuPath").show();
								form.findField("defaultPage").show();
							} else {
								//form.findField("menuPageName").show();
								form.findField("isDefault").show();
								form.findField("menuPath").hide();
								form.findField("defaultPage").hide();
							}
							form.findField("menuId").setReadOnly(true);
							form.findField("menuPath").setReadOnly(true);
							form.findField("menuId").addCls('lanage_1');
							form.findField("menuPath").addCls('lanage_1');
							form.findField("menuType").setReadOnly(true);
							form.findField("menuType").addCls('lanage_1');
							panel.actType = "update";
						} else {
							var tzParams = this.getOrgMenuInfoDeleteParams(form);
							Ext.tzSubmit(tzParams,function(responseData) {
								if (rootNode == menuId) {
									panel.close();
								} else {
									var thisNode = treepanelStore.getNodeById(menuId);
									var pNode = thisNode.parentNode;
									treepanel.getSelectionModel().select(pNode);
									var defaultPage=pNode.data.defaultPage;
									//Ext.Msg.alert("提示defaultPage", responseData.defaultPage);
									if(responseData.defaultPage=="Y"){
										defaultPage = "";
									}
									pNode.set('defaultPage',defaultPage);
									//Ext.Msg.alert("提示", defaultPage);
									form.setValues({
										menuId : pNode.data.id,
										menuName : pNode.data.text,
										menuPath : pNode.data.menuPath,
										menuState : pNode.data.menuState,
										menuType : pNode.data.menuType,
										menuTempletId : pNode.data.menuTempletId,
										menuPageName : pNode.data.menuPageName,
										isDefault : pNode.data.isDefault,
										menuXH : pNode.data.menuXH,
										defaultPage : defaultPage,
										NodeType : "",
										operateNode : "",
										rootNode : rootNode,
										siteId : siteId,
										menuTempletName : pNode.data.menuTempletName
									});
									
									if (pNode.data.menuType == "B") {
										//form.findField("menuPageName").hide();
										form.findField("isDefault").hide();
										form.findField("menuPath").show();
										form.findField("defaultPage").show();
									} else {
										//form.findField("menuPageName").show();
										form.findField("isDefault").show();
										form.findField("menuPath").hide();
										form.findField("defaultPage").hide();
									}
									form.findField("menuId").setReadOnly(true);
									form.findField("menuPath").setReadOnly(true);
									form.findField("menuId").addCls('lanage_1');
									form.findField("menuPath").addCls('lanage_1');
									form.findField("menuType").setReadOnly(true);
									form.findField("menuType").addCls('lanage_1');
									panel.actType = "update";
									pNode.removeChild(thisNode);
									if (pNode.hasChildNodes() == false) {
										pNode.set('leaf',true);
									}
								}
							}, "",true, this);
						}
					}
			}, this)
		};
	},
	
	//构造删除节点 提交到后台的JSON
	getOrgMenuInfoDeleteParams: function(form){
        //删除参数
		var comParams =  '"delete":[{"data":'+Ext.JSON.encode(form.getValues())+',"synchronous":false}]';
        //提交参数
       	var tzParams = '{"ComID":"TZ_GD_WWZDGL_COM","PageID":"TZ_GD_WWCDGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    
    //保存
    onFormSave: function(){
        //功能菜单项表单
    	var view = this.getView();
        var form = view.child("form").getForm();
        var treepanel = view.child("treepanel");
        var treepanelStore = treepanel.getStore();
        if (form.isValid()) {
           //得到在哪个节点上操作;
        	form.findField("menuId").setReadOnly(true); 
	         
	        var menuId = form.findField("menuId").getValue();
	        var siteId = form.findField("siteId").getValue();
	        var menuName = form.findField("menuName").getValue();
	        var menuPath = form.findField("menuPath").getValue();
	        var menuState = form.findField("menuState").getValue();
	        var menuType = form.findField("menuType").getValue();
	        var menuTempletId = form.findField("menuTempletId").getValue();
	        var menuTempletName = form.findField("menuTempletName").getValue();
	        var menuPageName = form.findField("menuPageName").getValue();
	        var isDefault = form.findField("isDefault").getValue();
	        var defaultPage = form.findField("defaultPage").getValue();
	        var menuXH = form.findField("menuXH").getValue();
	        
	        var NodeType = form.findField("NodeType").getValue();
	        var rootNode = form.findField("rootNode").getValue();
	        
	        //Ext.Msg.alert("提示isDefault", isDefault);
	        
	        //校验判断
	        if (menuType =="A") {
	        	if (menuPageName == null || menuPageName == undefined || menuPageName == "") { 
	        		Ext.Msg.alert("提示", "菜单类型为PAGE时页面名称必须填写");
	        		return;
	        	} 
	        }
	        
	        if (menuPath != null && menuPath != undefined && menuPath != "") { 
	        	var fdStart = menuPath.toLowerCase().indexOf("/");
        		if (fdStart != 0){
        			Ext.Msg.alert("提示", "菜单路径格式错误，必须以/开头");
        			return;
        		}
	        }
          
	        var operateNodeId = form.findField("operateNode").getValue();
	        var operateNode = treepanelStore.getNodeById(operateNodeId);
					 
	        var tzParams = this.getOrgMenuInfoParams();
	        var orgView = this.getView();

	        Ext.tzSubmit(tzParams,function(responseData){
	        	if(responseData.success=="true"){
	        		
	        		var childnodes;
	        		var childnode;
	        		
	        		 // 添加同级节点;
	        		if(NodeType == "Y"){
	        			var pNode = operateNode.parentNode;
	        			var operateNodeIndex = pNode.indexOfId(operateNodeId);
	        			pNode.set('defaultPage', responseData.defaultPage);
	        			
	        			//如果是选择默认页面，那么父节点下的其他节点 就需要修改成非默认
	        			if (isDefault ==true) {
	        				 childnodes = pNode.childNodes; 
	        	             for(var i=0;i<childnodes.length;i++){  //从节点中取出子节点依次遍历
	        	                 childnode = childnodes[i];
	        	                 childnode.set('isDefault', false);
	        	             }
	        			}
	        			
	        			pNode.insertChild(operateNodeIndex + 1,{
	        				text: menuName,
	        				nodeId: responseData.newMenuID,
	        				id: responseData.newMenuID,
	        				menuState : menuState,
	        				menuPath : menuPath,
	        				menuTempletId : menuTempletId,
	        				menuTempletName : menuTempletName,
	        				menuPageName : menuPageName,
	        				menuType : menuType,
	        				defaultPage : defaultPage,
	        				menuXH : menuXH,
	        				isDefault : isDefault,
	        				NodeType: "",
	        				operateNode: "",
	        				rootNode: rootNode,
	        				leaf: true
	        			});
	        			brotherNode = treepanelStore.getNodeById( responseData.newMenuID );
	        			treepanel.getSelectionModel().select(brotherNode);
	        		}
							 
	        		// 添加子节点;
	        		if(NodeType == "N"){
	        			operateNode.set('defaultPage', responseData.defaultPage);
	        			//如果是选择默认页面，那么父节点下的其他节点 就需要修改成非默认
	        			if (isDefault ==true) {
	        				 childnodes = operateNode.childNodes; 
	        	             for(var i=0;i<childnodes.length;i++){  //从节点中取出子节点依次遍历
	        	                 childnode = childnodes[i];
	        	                 childnode.set('isDefault', false);
	        	             }
	        			}
	        			
	        			operateNode.insertChild(0,{
	        				text: menuName,
	        				nodeId: responseData.newMenuID,
	        				id: responseData.newMenuID,
	        				menuState : menuState,
	        				menuPath : menuPath,
	        				menuTempletId : menuTempletId,
	        				menuTempletName : menuTempletName,
	        				menuPageName : menuPageName,
	        				menuType : menuType,
	        				menuXH : menuXH,
	        				isDefault : isDefault,
	        				defaultPage : defaultPage,
	        				NodeType: "",
	        				operateNode: "",
	        				rootNode: rootNode,
	        				leaf: true
	        			});
	        			operateNode.leaf = false;
	        			operateNode.expand();
					        
	        			childNode = treepanelStore.getNodeById( responseData.newMenuID );
					        
	        			treepanel.getSelectionModel().select(childNode);
		
	        		}
	        		 // 保存当前节点;
	        		if(NodeType == ""){
	        			//修改父节点的defaultPage
	        			var thisNode = treepanelStore.getNodeById(menuId);
	        			var pNode = thisNode.parentNode;
	        			//Ext.Msg.alert("提示",responseData.defaultPage);
	        			pNode.set('defaultPage', responseData.defaultPage);
	        			
	        			//如果是选择默认页面，那么父节点下的其他节点 就需要修改成非默认
	        			//Ext.Msg.alert("提示", isDefault);
	        			if (isDefault ==true) {
	        				childnodes = pNode.childNodes; 
	        	             for(var i=0;i<childnodes.length;i++){  //从节点中取出子节点依次遍历
	        	                 childnode = childnodes[i];
	        	                 childnode.set('isDefault', false);
	        	             }
	        			}
	        			
	        			
	        			thisNode.set('text', menuName);
	        			thisNode.set('nodeId', menuId);
	        			thisNode.set('id', menuId);
	        			thisNode.set('menuState', menuState);
	        			thisNode.set('menuPath', menuPath);
	        			thisNode.set('menuTempletId', menuTempletId);
	        			thisNode.set('menuTempletName', menuTempletName);
	        			thisNode.set('menuPageName', menuPageName);
	        			thisNode.set('menuType', menuType);
	        			thisNode.set('isDefault', isDefault);
	        			thisNode.set('defaultPage',defaultPage);
	        			thisNode.set('menuXH',menuXH);
	        			
	        			
	        			
	        			
	        			thisNode.set('NodeType', "");
	        			thisNode.set('operateNode', "");
	        			thisNode.set('rootNode', rootNode);
	        			
	        		}
	        		form.findField("NodeType").setValue("");
	        		form.findField("operateNode").setValue("");
	        		form.findField("menuId").setReadOnly(true);
	        		form.findField("menuId").addCls('lanage_1');
	        		form.findField("menuType").setReadOnly(true);
					form.findField("menuType").addCls('lanage_1');
					form.findField("menuPath").setReadOnly(true);
					form.findField("menuPath").addCls('lanage_1');
	        		view.actType = "update";
                 
	        		view.commitChanges(view);
	        	}else{
          	 	  Ext.Msg.alert("提示","保存失败");
	        	}
	        },"",true,this);
        }
    },
    
    
    //确定
    onFormEnsure: function(){
        //功能菜单项表单
    	var view = this.getView();
        var form = view.child("form").getForm();
        var treepanel = view.child("treepanel");
        var treepanelStore = treepanel.getStore();
        if (form.isValid()) {
           //得到在哪个节点上操作;
        	form.findField("menuId").setReadOnly(true); 
	         
	        var menuId = form.findField("menuId").getValue();
	        var siteId = form.findField("siteId").getValue();
	        var menuName = form.findField("menuName").getValue();
	        var menuPath = form.findField("menuPath").getValue();
	        var menuState = form.findField("menuState").getValue();
	        var menuType = form.findField("menuType").getValue();
	        var menuTempletId = form.findField("menuTempletId").getValue();
	        var menuTempletName = form.findField("menuTempletName").getValue();
	        var menuPageName = form.findField("menuPageName").getValue();
	        var isDefault = form.findField("isDefault").getValue();
	        var defaultPage = form.findField("defaultPage").getValue();
	        var NodeType = form.findField("NodeType").getValue();
	        var rootNode = form.findField("rootNode").getValue();
	        var menuXH = form.findField("menuXH").getValue();
	        
	        var operateNodeId = form.findField("operateNode").getValue();
	        var operateNode = treepanelStore.getNodeById(operateNodeId);
					 
	        var tzParams = this.getOrgMenuInfoParams();
	        var orgView = this.getView();

	        Ext.tzSubmit(tzParams,function(responseData){
	        	if(responseData.success=="true"){
	        		 // 添加同级节点;
	        		if(NodeType == "Y"){
	        			var pNode = operateNode.parentNode;
	        			var operateNodeIndex = pNode.indexOfId(operateNodeId);
	        			pNode.set('defaultPage', responseData.defaultPage);
	        			pNode.insertChild(operateNodeIndex + 1,{
	        				text: menuName,
	        				nodeId: responseData.newMenuID,
	        				id: responseData.newMenuID,
	        				menuState : menuState,
	        				menuPath : menuPath,
	        				menuTempletId : menuTempletId,
	        				menuTempletName : menuTempletName,
	        				menuPageName : menuPageName,
	        				menuType : menuType,
	        				isDefault : isDefault,
	        				menuXH : menuXH,
	        				defaultPage : defaultPage,
	        				NodeType: "",
	        				operateNode: "",
	        				rootNode: rootNode,
	        				leaf: true
	        			});
	        			brotherNode = treepanelStore.getNodeById( responseData.newMenuID );
	        			treepanel.getSelectionModel().select(brotherNode);
	        		}
							 
	        		// 添加子节点;
	        		if(NodeType == "N"){
	        			operateNode.set('defaultPage', responseData.defaultPage);
	        			operateNode.insertChild(0,{
	        				text: menuName,
	        				nodeId: responseData.newMenuID,
	        				id: responseData.newMenuID,
	        				menuState : menuState,
	        				menuPath : menuPath,
	        				menuTempletId : menuTempletId,
	        				menuTempletName : menuTempletName,
	        				menuPageName : menuPageName,
	        				menuType : menuType,
	        				menuXH : menuXH,
	        				isDefault : isDefault,
	        				defaultPage : defaultPage,
	        				NodeType: "",
	        				operateNode: "",
	        				rootNode: rootNode,
	        				leaf: true
	        			});
	        			operateNode.leaf = false;
	        			operateNode.expand();
					        
	        			childNode = treepanelStore.getNodeById( responseData.newMenuID );
					        
	        			treepanel.getSelectionModel().select(childNode);
		
	        		}
	        		 // 保存当前节点;
	        		if(NodeType == ""){
	        			var thisNode = treepanelStore.getNodeById(menuId);
	        			thisNode.set('text', menuName);
	        			thisNode.set('nodeId', menuId);
	        			thisNode.set('id', menuId);
	        			thisNode.set('menuState', menuState);
	        			thisNode.set('menuPath', menuPath);
	        			thisNode.set('menuTempletId', menuTempletId);
	        			thisNode.set('menuTempletName', menuTempletName);
	        			thisNode.set('menuPageName', menuPageName);
	        			thisNode.set('menuType', menuType);
	        			thisNode.set('isDefault', isDefault);
	        			thisNode.set('menuXH', menuXH);
	        			thisNode.set('defaultPage', defaultPage);
	        			thisNode.set('NodeType', "");
	        			thisNode.set('operateNode', "");
	        			thisNode.set('rootNode', rootNode);
	        			//修改父节点的defaultPage
	        			var pNode = thisNode.parentNode;
	        			//Ext.Msg.alert("提示",responseData.defaultPage);
	        			pNode.set('defaultPage', responseData.defaultPage);
	        		}
	        		form.findField("NodeType").setValue("");
	        		form.findField("operateNode").setValue("");
	        		form.findField("menuId").setReadOnly(true);
	        		form.findField("menuId").addCls('lanage_1');
	        		form.findField("menuType").setReadOnly(true);
					form.findField("menuType").addCls('lanage_1');
					form.findField("menuPath").setReadOnly(true);
					form.findField("menuPath").addCls('lanage_1');
	        		view.actType = "update";
	        		view.close();
	        	}else{
          	 	  Ext.Msg.alert("提示","保存失败");
	        	}
	        },"",true,this);
        }
    },
		
    //关闭页面
    onFormClose: function(){
    	this.getView().close();
    },
    
    //构造保存提交到后台的JSON
    getOrgMenuInfoParams: function(){
        //功能菜单表单
        var form = this.getView().child("form").getForm();
        //功能菜单标志
        var actType = this.getView().actType;

        //更新操作参数
        var comParams = "";
        //新增
        comParams = '"add":[{"data":'+Ext.JSON.encode(form.getValues())+',"synchronous":false}]';
        //提交参数
       	var tzParams = '{"ComID":"TZ_GD_WWZDGL_COM","PageID":"TZ_GD_WWCDGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    
    //放大镜 查询 站点模版
    pmtSearchTemplet: function(btn){
		var form = this.getView().child("form").getForm();
		var comSiteParams = form.getValues();
		var siteId = comSiteParams['siteId'];
		Ext.tzShowPromptSearch({
			recname: 'TZ_SITEI_TEMP_T',
			searchDesc: '搜索模版',
			maxRow:20,
			condition:{
				presetFields:{
					TZ_SITEI_ID:{
						value:siteId,
						type: '01'	
					}
				},
				srhConFields:{
					TZ_TEMP_ID:{
						desc:'模版ID',
						operator:'07',
						type:'01'	
					},
					TZ_TEMP_NAME:{
						desc:'模版名称',
						operator:'07',
						type:'01'		
					}
				}	
			},
			srhresult:{
				TZ_TEMP_ID: '模版ID',
				TZ_TEMP_NAME: '模版名称'	
			},
			multiselect: false,
			callback: function(selection){
				form.findField("menuTempletId").setValue(selection[0].data.TZ_TEMP_ID);
				form.findField("menuTempletName").setValue(selection[0].data.TZ_TEMP_NAME);
			}
		});	
	},
	clearPmtSearchTemplet: function(btn){
		var form = this.getView().child("form").getForm();
		form.findField("menuTempletId").setValue("");
		form.findField("menuTempletName").setValue("");
		
	}
	
    
});