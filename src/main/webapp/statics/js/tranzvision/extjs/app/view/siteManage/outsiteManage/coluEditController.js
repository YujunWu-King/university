Ext.define('KitchenSink.view.siteManage.outsiteManage.coluEditController',{
	extend : 'Ext.app.ViewController',
	alias : 'controller.coluEdit',
	requires : [ 'KitchenSink.view.common.changeDataLanguage','KitchenSink.view.common.synMultiLanguageData' ],
	
	//插入同级节点
	inserColuItem : function(bt, eOpts) {
		var actType = bt.findParentByType("coluEdit").actType;
		if (actType == "update") {
			var form = bt.findParentByType("form").getForm();
			var coluId = form.findField("coluId").getValue();
			var rootNode = form.findField("rootNode").getValue();
			
			if (coluId == rootNode) {
				Ext.Msg.alert("提示", "不可插入根节点的同级节点");
			} else {
				form.findField("coluPath").setReadOnly(false);
				form.findField("coluPath").removeCls('lanage_1'); 
				
				form.findField("coluTempletId").hide();
				form.findField("coluTempletName").hide();
				form.findField("contentTypeId").hide();
				form.findField("contentTypeName").hide();
				form.findField("coluUrl").hide();
				form.findField("coluId").setValue('');
				form.findField("coluName").setValue('');
				form.findField("coluPath").setValue('');
				form.findField("coluState").setValue('');
				form.findField("coluType").setValue('');
				form.findField("coluTempletId").setValue('');
				form.findField("contentTypeId").setValue('');
				form.findField("coluTempletName").setValue('');
				form.findField("contentTypeName").setValue('');
				form.findField("coluUrl").setValue('');
				form.findField("coluAbout").setValue('');
				form.findField("NodeType").setValue("Y");
				form.findField("operateNode").setValue(coluId);
				form.findField("rootNode").setValue(rootNode);

				bt.findParentByType("coluEdit").actType = "add";
			}
		} else {
			Ext.Msg.alert("提示", "请先保存当前节点才能添加该节点的同级节点");
		}
	},
	
	//插入子节点
	inserChildColuItem : function(bt, eOpts) {
		var actType = bt.findParentByType("coluEdit").actType;
		if (actType == "update") {
			
			var form = bt.findParentByType("form").getForm();
			var coluId = form.findField("coluId").getValue();
			var rootNode = form.findField("rootNode").getValue();
			form.findField("coluPath").setReadOnly(false);
			form.findField("coluPath").removeCls('lanage_1'); 
			
			form.findField("coluTempletId").hide();
			form.findField("coluTempletName").hide();
			form.findField("contentTypeId").hide();
			form.findField("contentTypeName").hide();
			form.findField("coluUrl").hide();
			
			form.findField("coluId").setValue('');
			form.findField("coluName").setValue('');
			form.findField("coluPath").setValue('');
			form.findField("coluState").setValue('');
			form.findField("coluType").setValue('');
			form.findField("coluTempletId").setValue('');
			form.findField("contentTypeId").setValue('');
			form.findField("coluTempletName").setValue('');
			form.findField("contentTypeName").setValue('');
			form.findField("coluUrl").setValue('');
			form.findField("coluAbout").setValue('');

			form.findField("NodeType").setValue("N");
			form.findField("operateNode").setValue(coluId);
			form.findField("rootNode").setValue(rootNode);
			bt.findParentByType("coluEdit").actType = "add";
		} else {
			Ext.Msg.alert("提示", "请先保存当前节点才能添加该节点的子节点");
		}
	},

	//删除节点
	removeColuItem : function(bt, eOpts) {
		var panel = bt.findParentByType("coluEdit");
		var actType = panel.actType;

		var form = panel.child("form").getForm();
		var treepanel = panel.child("treepanel");
		var treepanelStore = treepanel.getStore();

		var coluId = form.findField("coluId").getValue();
		var siteId = form.findField("siteId").getValue();
		var rootNode = form.findField("rootNode").getValue();
		var operateNodeId = form.findField("operateNode").getValue();
		var operateNode = treepanelStore.getNodeById(operateNodeId);
		
		if (coluId == rootNode) {
			Ext.Msg.alert("提示", "不可删除根节点");
		} else {
			Ext.Msg.confirm("确认","是否确认删除当前节点及其子节点",
				function(confm) {
					if (confm == 'yes') {
						if (actType == "add") {
							form.setValues({
								coluId : operateNode.data.id,
								coluName : operateNode.data.text,
								coluPath : operateNode.data.coluPath,
								coluState : operateNode.data.coluState,
								coluTempletId : operateNode.data.coluTempletId,
								contentTypeId : operateNode.data.contentTypeId,
								coluUrl : operateNode.data.coluUrl,
								NodeType : "",
								operateNode : "",
								rootNode : rootNode,
								siteId : operateNode.data.siteId,
								coluTempletName : operateNode.data.coluTempletName,
								contentTypeName : operateNode.data.contentTypeName,
								coluAbout : operateNode.data.coluAbout
							});
							form.findField("coluId").setReadOnly(true);
							form.findField("coluPath").setReadOnly(true);
							form.findField("coluId").addCls('lanage_1');
							form.findField("coluPath").addCls('lanage_1');
							panel.actType = "update";
						} else {
							var tzParams = this.getOrgMenuInfoDeleteParams(form);
							Ext.tzSubmit(tzParams,function(responseData) {
								if (rootNode == coluId) {
									panel.close();
								} else {
									var thisNode = treepanelStore.getNodeById(coluId);
									var pNode = thisNode.parentNode;
									treepanel.getSelectionModel().select(pNode);
									form.setValues({
										coluId : pNode.data.id,
										coluName : pNode.data.text,
										coluPath : pNode.data.coluPath,
										coluState : pNode.data.coluState,
										coluTempletId : pNode.data.coluTempletId,
										contentTypeId : pNode.data.contentTypeId,
										coluUrl : pNode.data.coluUrl,
										NodeType : "",
										operateNode : "",
										rootNode : rootNode,
										siteId : pNode.data.siteId,
										coluTempletName : pNode.data.coluTempletName,
										contentTypeName : pNode.data.contentTypeName,
										coluAbout : pNode.data.coluAbout
									});
									form.findField("coluId").setReadOnly(true);
									form.findField("coluPath").setReadOnly(true);
									form.findField("coluId").addCls('lanage_1');
									form.findField("coluPath").addCls('lanage_1');
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
       	var tzParams = '{"ComID":"TZ_GD_WWZDGL_COM","PageID":"TZ_GD_WWLMGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
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
        	form.findField("coluId").setReadOnly(true); 
	         
	        var coluId = form.findField("coluId").getValue();
	        var siteId = form.findField("siteId").getValue();
	        var coluName = form.findField("coluName").getValue();
	        var coluPath = form.findField("coluPath").getValue();
	        var coluState = form.findField("coluState").getValue();
	        var coluType = form.findField("coluType").getValue();
	        var coluTempletId = form.findField("coluTempletId").getValue();
	        var contentTypeId = form.findField("contentTypeId").getValue();
	        var coluTempletName = form.findField("coluTempletName").getValue();
	        var contentTypeName = form.findField("contentTypeName").getValue();
	        var coluUrl = form.findField("coluUrl").getValue();
	        var NodeType = form.findField("NodeType").getValue();
	        var rootNode = form.findField("rootNode").getValue();
	        var coluAbout = form.findField("coluAbout").getValue();
	        
	        //校验判断
	        if (coluType =="C") {
	        	if (coluUrl == null || coluUrl == undefined || coluUrl == "") { 
	        		Ext.Msg.alert("提示", "栏目类型为跳转时外部链接必须填写");
	        		return;
	        	} else {
	        		var fdStart = coluUrl.toLowerCase().indexOf("http");
	        		if (fdStart != 0){
	        			Ext.Msg.alert("提示", "外部链接格式错误，必须以http开头");
	        			return;
	        		}
	        	}
	        }
	        
	        if (coluPath != null && coluPath != undefined && coluPath != "") { 
	        	var fdStart = coluPath.toLowerCase().indexOf("/");
        		if (fdStart != 0){
        			Ext.Msg.alert("提示", "栏目路径格式错误，必须以/开头");
        			return;
        		}
	        }
          
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
	        			pNode.insertChild(operateNodeIndex + 1,{
	        				text: coluName,
	        				nodeId: responseData.newColuID,
	        				id: responseData.newColuID,
	        				siteId : siteId,
	        				coluState : coluState,
	        				coluPath : coluPath,
	        				coluTempletId : coluTempletId,
	        				contentTypeId : contentTypeId,
	        				coluTempletName : coluTempletName,
	        				contentTypeName : contentTypeName,
	        				coluAbout : coluAbout,
	        				coluUrl : coluUrl,
	        				coluType : coluType,
	        				NodeType: "",
	        				operateNode: "",
	        				rootNode: rootNode,
	        				leaf: true
	        			});
	        			brotherNode = treepanelStore.getNodeById( responseData.newColuID );
	        			treepanel.getSelectionModel().select(brotherNode);
	        			form.findField("coluId").setValue(responseData.newColuID);
	        		}
							 
	        		// 添加子节点;
	        		if(NodeType == "N"){
	        			operateNode.insertChild(0,{
	        				text: coluName,
	        				nodeId: responseData.newColuID,
	        				id: responseData.newColuID,
	        				siteId : siteId,
	        				coluState : coluState,
	        				coluPath : coluPath,
	        				coluTempletId : coluTempletId,
	        				contentTypeId : contentTypeId,
	        				coluTempletName : coluTempletName,
	        				contentTypeName : contentTypeName,
	        				coluAbout : coluAbout,
	        				coluUrl : coluUrl,
	        				coluType : coluType,
	        				NodeType: "",
	        				operateNode: "",
	        				rootNode: rootNode,
	        				leaf: true
	        			});
	        			operateNode.leaf = false;
	        			operateNode.expand();
					        
	        			childNode = treepanelStore.getNodeById( responseData.newColuID );
					        
	        			treepanel.getSelectionModel().select(childNode);
	        			form.findField("coluId").setValue(responseData.newColuID);
		
	        		}
	        		 // 保存当前节点;
	        		if(NodeType == ""){
	        			var thisNode = treepanelStore.getNodeById(coluId);
	        			thisNode.set('text', coluName);
	        			thisNode.set('nodeId', coluId);
	        			thisNode.set('id', coluId);
	        			thisNode.set('siteId', siteId);
	        			thisNode.set('coluState', coluState);
	        			thisNode.set('coluPath', coluPath);
	        			thisNode.set('coluTempletId', coluTempletId);
	        			thisNode.set('contentTypeId', contentTypeId);
	        			thisNode.set('coluTempletName', coluTempletName);
	        			thisNode.set('contentTypeName', contentTypeName);
	        			thisNode.set('coluAbout', coluAbout);
	        			thisNode.set('coluUrl', coluUrl);
	        			thisNode.set('coluType', coluType);
	        			thisNode.set('NodeType', "");
	        			thisNode.set('operateNode', "");
	        			thisNode.set('rootNode', rootNode);
	        		}
	        		form.findField("NodeType").setValue("");
	        		form.findField("operateNode").setValue("");
	        		form.findField("coluId").setReadOnly(true);
	        		form.findField("coluId").addCls('lanage_1');
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
        	form.findField("coluId").setReadOnly(true); 
	         
	        var coluId = form.findField("coluId").getValue();
	        var siteId = form.findField("siteId").getValue();
	        var coluName = form.findField("coluName").getValue();
	        var coluPath = form.findField("coluPath").getValue();
	        var coluState = form.findField("coluState").getValue();
	        var coluType = form.findField("coluType").getValue();
	        var coluTempletId = form.findField("coluTempletId").getValue();
	        var contentTypeId = form.findField("contentTypeId").getValue();
	        var coluTempletName = form.findField("coluTempletName").getValue();
	        var contentTypeName = form.findField("contentTypeName").getValue();
	        var coluUrl = form.findField("coluUrl").getValue();
	        var NodeType = form.findField("NodeType").getValue();
	        var rootNode = form.findField("rootNode").getValue();
	        var coluAbout = form.findField("coluAbout").getValue();
           
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
	        			pNode.insertChild(operateNodeIndex + 1,{
	        				text: coluName,
	        				nodeId: responseData.newColuID,
	        				id: responseData.newColuID,
	        				siteId : siteId,
	        				coluState : coluState,
	        				coluPath : coluPath,
	        				coluTempletId : coluTempletId,
	        				contentTypeId : contentTypeId,
	        				coluTempletName : coluTempletName,
	        				contentTypeName : contentTypeName,
	        				coluAbout : coluAbout,
	        				coluUrl : coluUrl,
	        				coluType : coluType,
	        				NodeType: "",
	        				operateNode: "",
	        				rootNode: rootNode,
	        				leaf: true
	        			});
	        			brotherNode = treepanelStore.getNodeById( responseData.newColuID );
	        			treepanel.getSelectionModel().select(brotherNode);
	        		}
							 
	        		// 添加子节点;
	        		if(NodeType == "N"){
	        			operateNode.insertChild(0,{
	        				text: coluName,
	        				nodeId: responseData.newColuID,
	        				id: responseData.newColuID,
	        				siteId : siteId,
	        				coluState : coluState,
	        				coluPath : coluPath,
	        				coluTempletId : coluTempletId,
	        				contentTypeId : contentTypeId,
	        				coluTempletName : coluTempletName,
	        				contentTypeName : contentTypeName,
	        				coluAbout : coluAbout,
	        				coluUrl : coluUrl,
	        				coluType : coluType,
	        				NodeType: "",
	        				operateNode: "",
	        				rootNode: rootNode,
	        				leaf: true
	        			});
	        			operateNode.leaf = false;
	        			operateNode.expand();
					        
	        			childNode = treepanelStore.getNodeById( responseData.newColuID );
					        
	        			treepanel.getSelectionModel().select(childNode);
		
	        		}
	        		 // 保存当前节点;
	        		if(NodeType == ""){
	        			var thisNode = treepanelStore.getNodeById(coluId);
	        			thisNode.set('text', coluName);
	        			thisNode.set('nodeId', coluId);
	        			thisNode.set('id', coluId);
	        			thisNode.set('siteId', siteId);
	        			thisNode.set('coluState', coluState);
	        			thisNode.set('coluPath', coluPath);
	        			thisNode.set('coluTempletId', coluTempletId);
	        			thisNode.set('contentTypeId', contentTypeId);
	        			thisNode.set('coluTempletName', coluTempletName);
	        			thisNode.set('contentTypeName', contentTypeName);
	        			thisNode.set('coluAbout', coluAbout);
	        			thisNode.set('coluUrl', coluUrl);
	        			thisNode.set('coluType', coluType);
	        			thisNode.set('NodeType', "");
	        			thisNode.set('operateNode', "");
	        			thisNode.set('rootNode', rootNode);
	        		}
	        		form.findField("NodeType").setValue("");
	        		form.findField("operateNode").setValue("");
	        		form.findField("coluId").setReadOnly(true);
	        		form.findField("coluId").addCls('lanage_1');
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
       	var tzParams = '{"ComID":"TZ_GD_WWZDGL_COM","PageID":"TZ_GD_WWLMGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
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
				form.findField("coluTempletId").setValue(selection[0].data.TZ_TEMP_ID);
				form.findField("coluTempletName").setValue(selection[0].data.TZ_TEMP_NAME);
			}
		});	
	},
	clearPmtSearchTemplet: function(btn){
		var form = this.getView().child("form").getForm();
		form.findField("coluTempletId").setValue("");
		form.findField("coluTempletName").setValue("");
		
	},
	
	//放大镜 查询 内容类型
	pmtSearchCtype: function(btn){
		var form = this.getView().child("form").getForm();
		Ext.tzShowPromptSearch({
			recname: 'TZ_ART_TYPE_T',
			searchDesc: '搜索内容类型',
			maxRow:20,
			condition:{
				srhConFields:{
					TZ_ART_TYPE_ID:{
						desc:'内容类型ID',
						operator:'07',
						type:'01'	
					},
					TZ_ART_TYPE_NAME:{
						desc:'内容类型名称',
						operator:'07',
						type:'01'		
					}
				}	
			},
			srhresult:{
				TZ_ART_TYPE_ID: '内容类型ID',
				TZ_ART_TYPE_NAME: '内容类型名称'	
			},
			multiselect: false,
			callback: function(selection){
				form.findField("contentTypeId").setValue(selection[0].data.TZ_ART_TYPE_ID);
				form.findField("contentTypeName").setValue(selection[0].data.TZ_ART_TYPE_NAME);
			}
		});	
	},
	clearPmtSearchCtype: function(btn){
		var form = this.getView().child("form").getForm();
		form.findField("contentTypeId").setValue("");
		form.findField("contentTypeName").setValue("");

	}



});