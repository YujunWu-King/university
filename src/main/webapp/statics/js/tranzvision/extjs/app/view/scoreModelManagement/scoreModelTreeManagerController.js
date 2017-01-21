Ext.define('KitchenSink.view.scoreModelManagement.scoreModelTreeManagerController', {
	extend: 'Ext.app.ViewController',
	alias: 'controller.scoreModelTreeManagerController',
	
	
	viewScoreModelTreeNode: function(config){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SCORE_MOD_COM"]["TZ_TREE_NODE_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.prompt","提示"), Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.prompt","提示"),Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}

		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}
		ViewClass = Ext.ClassManager.get(className);
		clsProto = ViewClass.prototype;

		var win = new ViewClass(config,function(){
			//回调函数
		});
		
		var treeName = config.treeName;
		var form = win.child('form').getForm();
		//新增时默认成绩项类型为A
		if(config.actType == "A"){
			form.setValues({itemType: "A",treeName: treeName});
		}else{
			var OpeItemId = config.OperatorItemId;//操作树节点
			var tzParams = '{"ComID":"TZ_SCORE_MOD_COM","PageID":"TZ_TREE_NODE_STD","OperateType":"QF","comParams":{"treeName":"'+treeName+'","OpeItemId":"'+OpeItemId+'"}}';
			Ext.tzLoad(tzParams,function(respData){
				console.log(respData);
				//form.setValues(respData);
			});
		}
		
		win.show();
	},
	
	
	/**
	 * 插入兄弟节点
	 * 参数：actType：A-新增（包括插入子节点、插入同级节点），U-编辑；
	 *		insertType：A-插入同级节点，B-插入子节点；
	 */
	insertBrotherNode: function(btn){
		var treeManager = btn.findParentByType("scoreModelTreeManager");
		var treeName = treeManager.treeName;
		var selectTreeNode = treeManager.selectTreeNode;
		
		if(selectTreeNode){
			if(selectTreeNode.root){
				//如果是根节点，不可以插入兄弟节点
				Ext.Msg.alert("提示","根节点不能插入同级节点");
			}else{
				var config = {
						treeName: treeName,
						actType: "A",
						insertType: 'A',
						OperatorItemId: selectTreeNode.itemId
					}
				this.viewScoreModelTreeNode(config);
			}
		}else{
			Ext.Msg.alert("提示","请先选择树节点");
		}
	},
	
	/**
	 * 插入子节点
	 * 参数：actType：A-新增（包括插入子节点、插入同级节点），U-编辑；
	 *		insertType：A-插入同级节点，B-插入子节点；
	 */
	insertChildNode: function(btn){
		var treeManager = btn.findParentByType("scoreModelTreeManager");
		var treeName = treeManager.treeName;
		var selectTreeNode = treeManager.selectTreeNode;
		
		if(selectTreeNode){
			var config = {
					treeName: treeName,
					actType: "A",
					insertType: 'B',
					OperatorItemId: selectTreeNode.itemId
				}
			this.viewScoreModelTreeNode(config);
		}else{
			Ext.Msg.alert("提示","请先选择树节点");
		}
	},
	
	/**
	 * 编辑节点
	 * 参数：actType：A-新增（包括插入子节点、插入同级节点），U-编辑；
	 *		insertType：A-插入同级节点，B-插入子节点；
	 */
	editNode: function(btn){
		var treeManager = btn.findParentByType("scoreModelTreeManager");
		var treeName = treeManager.treeName;
		var selectTreeNode = treeManager.selectTreeNode;
		
		if(selectTreeNode){
			var config = {
					treeName: treeName,
					actType: "U",
					insertType: '',
					OperatorItemId: selectTreeNode.itemId
				}
			this.viewScoreModelTreeNode(config);
		}else{
			Ext.Msg.alert("提示","请先选择树节点");
		}
	},
	
	
	/**
	 * 删除节点
	 */
	removeNode:function(btn){
		var treeManager = btn.findParentByType("scoreModelTreeManager");
		var treeName = treeManager.treeName;
		var selectTreeNode = treeManager.selectTreeNode;
		
		if(selectTreeNode){
			
		}else{
			Ext.Msg.alert("提示","请先选择树节点");
		}
	},
	
	
	
	onScoreModelSave: function(btn){
		
	},
	
	
	onScoreModelEnsure: function(btn){
		
	},
	
	onScoreModelClose: function(btn){
		btn.findParentByType("scoreModelTreeNodeWin").close();
	}
});