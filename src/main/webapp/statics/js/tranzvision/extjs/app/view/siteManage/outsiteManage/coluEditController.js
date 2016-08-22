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
				form.findField("coluId").setValue('');
				form.findField("coluName").setValue('');
				form.findField("coluPath").setValue('');
				form.findField("coluState").setValue('');
				form.findField("coluTempletId").setValue('');
				form.findField("contentTypeId").setValue('');
				form.findField("coluTempletName").setValue('');
				form.findField("contentTypeName").setValue('');
				form.findField("coluUrl").setValue('');

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

			form.findField("coluId").setValue('');
			form.findField("coluName").setValue('');
			form.findField("coluPath").setValue('');
			form.findField("coluState").setValue('');
			form.findField("coluTempletId").setValue('');
			form.findField("contentTypeId").setValue('');
			form.findField("coluTempletName").setValue('');
			form.findField("contentTypeName").setValue('');
			form.findField("coluUrl").setValue('');

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
								siteId : siteId,
								coluTempletName : operateNode.data.coluTempletName,
								contentTypeName : operateNode.data.contentTypeName
							});
							form.findField("coluId").setReadOnly(true);
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
									form.setValues({
										menuId : pNode.data.id,
										menuName : pNode.data.text,
										menuYxState : pNode.data.menuYxState,
										comId : pNode.data.comId,
										bigImgId : pNode.data.bigImgId,
										smallImgId : pNode.data.smallImgId,
										helpId : pNode.data.helpId,
										NodeType : "",
										operateNode : "",
										rootNode : rootNode,
										comName : pNode.data.comName
									});
									form.findField("menuId").setReadOnly(true);
									panel.actType = "update";
									pNode.removeChild(thisNode);
									if (pNode.hasChildNodes() == false) {
										pNode.set('leaf',true);
									}
								}
							}, "",true, this);
						}
					}
		}, this);
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
	        				coluState : coluState,
	        				coluPath : coluPath,
	        				coluTempletId : coluTempletId,
	        				contentTypeId : contentTypeId,
	        				coluTempletName : coluTempletName,
	        				contentTypeName : contentTypeName,
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
	        				coluState : coluState,
	        				coluPath : coluPath,
	        				coluTempletId : coluTempletId,
	        				contentTypeId : contentTypeId,
	        				coluTempletName : coluTempletName,
	        				contentTypeName : contentTypeName,
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
	        			thisNode.set('coluState', coluState);
	        			thisNode.set('coluPath', coluPath);
	        			thisNode.set('coluTempletId', coluTempletId);
	        			thisNode.set('contentTypeId', contentTypeId);
	        			thisNode.set('coluTempletName', coluTempletName);
	        			thisNode.set('contentTypeName', contentTypeName);
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
	        				coluState : coluState,
	        				coluPath : coluPath,
	        				coluTempletId : coluTempletId,
	        				contentTypeId : contentTypeId,
	        				coluTempletName : coluTempletName,
	        				contentTypeName : contentTypeName,
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
	        				coluState : coluState,
	        				coluPath : coluPath,
	        				coluTempletId : coluTempletId,
	        				contentTypeId : contentTypeId,
	        				coluTempletName : coluTempletName,
	        				contentTypeName : contentTypeName,
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
	        			thisNode.set('coluState', coluState);
	        			thisNode.set('coluPath', coluPath);
	        			thisNode.set('coluTempletId', coluTempletId);
	        			thisNode.set('contentTypeId', contentTypeId);
	        			thisNode.set('coluTempletName', coluTempletName);
	        			thisNode.set('contentTypeName', contentTypeName);
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
    }
    
    
    
});