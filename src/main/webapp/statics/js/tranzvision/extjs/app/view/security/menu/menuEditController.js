Ext.define('KitchenSink.view.security.menu.menuEditController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.menuEdit',
    requires:
    [
      'KitchenSink.view.common.changeDataLanguage',
      'KitchenSink.view.common.synMultiLanguageData'
    ],

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
	         var menuName = form.findField("menuName").getValue();
	         var menuYxState = form.findField("menuYxState").getValue();
	         var comId = form.findField("comId").getValue();
	         var comName = form.findField("comName").getValue();
	         var bigImgId = form.findField("bigImgId").getValue();
	         var smallImgId = form.findField("smallImgId").getValue();
	         var helpId = form.findField("helpId").getValue();
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
					          text: menuName,
		                nodeId: menuId,
		                id: menuId,
		                menuYxState: menuYxState,
		                comId: comId,
										bigImgId: bigImgId,
										smallImgId: smallImgId,
										helpId: helpId,
										NodeType: "",
										operateNode: "",
										rootNode: rootNode,
										comName: comName,
										leaf: true
					        });
					        
					        brotherNode = treepanelStore.getNodeById( menuId );
					        
					        treepanel.getSelectionModel().select(brotherNode);
							 }
							 
							 // 添加子节点;
							 if(NodeType == "N"){
							 		operateNode.insertChild(0,{
					          text: menuName,
		                nodeId: menuId,
		                id: menuId,
		                menuYxState: menuYxState,
		                comId: comId,
										bigImgId: bigImgId,
										smallImgId: smallImgId,
										helpId: helpId,
										NodeType: "",
										operateNode: "",
										rootNode: rootNode,
										comName: comName,
										leaf: true
					        });
					        operateNode.leaf = false;
					        operateNode.expand();
					        
					        childNode = treepanelStore.getNodeById( menuId );
					        
					        treepanel.getSelectionModel().select(childNode);
		
							 }
							 // 保存当前节点;
							 if(NodeType == ""){
							 	  	var thisNode = treepanelStore.getNodeById(menuId);
							 	  				 	  
										thisNode.set('text', menuName);
							 	    thisNode.set('nodeId', menuId);
							 	    thisNode.set('id', menuId);
							 			thisNode.set('menuYxState', menuYxState);
							 	    thisNode.set('comId', comId);
							 	    thisNode.set('bigImgId', bigImgId);
							 	    thisNode.set('smallImgId', smallImgId);
							 	    thisNode.set('helpId', helpId);
							 	    thisNode.set('NodeType', "");
							 	    thisNode.set('operateNode', "");
							 	    thisNode.set('rootNode', rootNode);
							 	    thisNode.set('comName', comName);
							 	    
					          //thisNode.data.text = menuName;
		                //thisNode.data.nodeId = menuId;
		                //thisNode.data.id= menuId;
		                //thisNode.data.menuYxState= menuYxState;
		                //thisNode.data.comId= comId;
										//thisNode.data.bigImgId= bigImgId;
										//thisNode.data.smallImgId= smallImgId;
										//thisNode.data.helpId= helpId;
										//thisNode.data.NodeType= "";
										//thisNode.data.operateNode= "";
										//thisNode.data.rootNode= rootNode;
										//thisNode.data.comName = comName;
										//treepanelStore.reload({node:thisNode});
										//treepanelStore.reload();
							 }
							 
							 form.findField("NodeType").setValue("");
							 form.findField("operateNode").setValue("");
							 form.findField("menuId").setReadOnly(true);
                             form.findField("menuId").addCls('lanage_1');
                 view.actType = "update";
                 
                 view.commitChanges(view);
          	 }else{
          	 	  Ext.Msg.alert("提示","保存失败");
          	 }
          },"",true,this);
        }
        /*
        var form = this.getView().child("form").getForm();
        var treepanelStore = this.getView().child("treepanel").getStore();
        if (form.isValid()) {
            //获取组件注册信息参数
            var tzParams = this.getOrgMenuInfoParams();
            var orgView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                orgView.actType = "update";
                form.findField("menuId").setReadOnly(true);
                treepanelStore.getProxy().extraParams.nodeId = "";
                treepanelStore.load();
            },"",true,this);
        }
        */
    },
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
	         var menuName = form.findField("menuName").getValue();
	         var menuYxState = form.findField("menuYxState").getValue();
	         var comId = form.findField("comId").getValue();
	         var comName = form.findField("comName").getValue();
	         var bigImgId = form.findField("bigImgId").getValue();
	         var smallImgId = form.findField("smallImgId").getValue();
	         var helpId = form.findField("helpId").getValue();
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
					          text: menuName,
		                nodeId: menuId,
		                id: menuId,
		                menuYxState: menuYxState,
		                comId: comId,
										bigImgId: bigImgId,
										smallImgId: smallImgId,
										helpId: helpId,
										NodeType: "",
										operateNode: "",
										rootNode: rootNode,
										comName: comName,
										leaf: true
					        });
					        
					        brotherNode = treepanelStore.getNodeById( menuId );
					        
					        treepanel.getSelectionModel().select(brotherNode);
							 }
							 
							 // 添加子节点;
							 if(NodeType == "N"){
							 		operateNode.insertChild(0,{
					          text: menuName,
		                nodeId: menuId,
		                id: menuId,
		                menuYxState: menuYxState,
		                comId: comId,
										bigImgId: bigImgId,
										smallImgId: smallImgId,
										helpId: helpId,
										NodeType: "",
										operateNode: "",
										rootNode: rootNode,
										comName: comName,
										leaf: true
					        });
					        operateNode.leaf = false;
					        operateNode.expand();
					        
					        childNode = treepanelStore.getNodeById( menuId );
					        
					        treepanel.getSelectionModel().select(childNode);
		
							 }
							 // 保存当前节点;
							 if(NodeType == ""){
							 	  	var thisNode = treepanelStore.getNodeById(menuId);
							 	  				 	  
										thisNode.set('text', menuName);
							 	    thisNode.set('nodeId', menuId);
							 	    thisNode.set('id', menuId);
							 			thisNode.set('menuYxState', menuYxState);
							 	    thisNode.set('comId', comId);
							 	    thisNode.set('bigImgId', bigImgId);
							 	    thisNode.set('smallImgId', smallImgId);
							 	    thisNode.set('helpId', helpId);
							 	    thisNode.set('NodeType', "");
							 	    thisNode.set('operateNode', "");
							 	    thisNode.set('rootNode', rootNode);
							 	    thisNode.set('comName', comName);
							 	    
					          //thisNode.data.text = menuName;
		                //thisNode.data.nodeId = menuId;
		                //thisNode.data.id= menuId;
		                //thisNode.data.menuYxState= menuYxState;
		                //thisNode.data.comId= comId;
										//thisNode.data.bigImgId= bigImgId;
										//thisNode.data.smallImgId= smallImgId;
										//thisNode.data.helpId= helpId;
										//thisNode.data.NodeType= "";
										//thisNode.data.operateNode= "";
										//thisNode.data.rootNode= rootNode;
										//thisNode.data.comName = comName;
										//treepanelStore.reload({node:thisNode});
										//treepanelStore.reload();
							 }
							 
							 form.findField("NodeType").setValue("");
							 form.findField("operateNode").setValue("");
							 form.findField("menuId").setReadOnly(true); 
							 view.actType = "update";
							 view.commitChanges(view);
							 
							 view.close();
          	 }else{
          	 	  Ext.Msg.alert("提示","保存失败");
          	 }
          },"",true,this);
        }
    },
		onFormClose: function(){
			this.getView().close();
		},
		inserMenueItem: function(bt,eOpts){
			 var actType = bt.findParentByType("menuEdit").actType;
			 if(actType=="update"){
			 		 var form = bt.findParentByType("form").getForm();
					 var menuId = form.findField("menuId").getValue();
					 var rootNode = form.findField("rootNode").getValue();
					 if(menuId ==rootNode){
					 	  Ext.Msg.alert("提示","不可插入根节点的同级节点");
					 }else{
                         form.findField("menuName").setValue('');
                         form.findField("menuId").setValue('');
                         form.findField("menuYxState").setValue('');
                         form.findField("comId").setValue('');
                         form.findField("comName").setValue('');
                         form.findField("bigImgId").setValue('');
                         form.findField("smallImgId").setValue('');
                         form.findField("helpId").setValue('');

						 form.findField("NodeType").setValue("Y");
						 form.findField("operateNode").setValue(menuId);
						 form.findField("rootNode").setValue(rootNode);
						 form.findField("menuId").setReadOnly(false);
                         form.findField("menuId").removeCls('lanage_1');
						 bt.findParentByType("menuEdit").actType = "add";
					 }
			 }else{
			 		Ext.Msg.alert("提示","请先保存当前节点才能添加该节点的同级节点");
			 }
			 
		},
		inserChildMenueItem: function(bt,eOpts){
			 var actType = bt.findParentByType("menuEdit").actType;
			 if(actType=="update"){
				 var form = bt.findParentByType("form").getForm();
				 var menuId = form.findField("menuId").getValue();
				 var rootNode = form.findField("rootNode").getValue();

                 form.findField("menuName").setValue('');
                 form.findField("menuId").setValue('');
                 form.findField("menuYxState").setValue('');
                 form.findField("comId").setValue('');
                 form.findField("comName").setValue('');
                 form.findField("bigImgId").setValue('');
                 form.findField("smallImgId").setValue('');
                 form.findField("helpId").setValue('');

				 form.findField("NodeType").setValue("N");
				 form.findField("operateNode").setValue(menuId);
				 form.findField("rootNode").setValue(rootNode);
				 form.findField("menuId").setReadOnly(false);
                 form.findField("menuId").removeCls('lanage_1');
				 bt.findParentByType("menuEdit").actType = "add";
			 }else{
				 Ext.Msg.alert("提示","请先保存当前节点才能添加该节点的子节点");
			 }
		},
		removeMenueItem: function(bt,eOpts){
			var panel = bt.findParentByType("menuEdit");
			var actType = panel.actType;

			var form = panel.child("form").getForm();
      var treepanel = panel.child("treepanel");
      var treepanelStore = treepanel.getStore();
        
			var menuId = form.findField("menuId").getValue();
			var rootNode = form.findField("rootNode").getValue();
			var operateNodeId = form.findField("operateNode").getValue();
			var operateNode = treepanelStore.getNodeById(operateNodeId);
			
			Ext.Msg.confirm("确认","是否确认删除当前节点及其子节点",function(confm){
				if(confm == 'yes'){
					if(actType=="add"){
						 //treepanel.getSelectionModel().select(operateNode);
						 form.setValues({
						            menuId: operateNode.data.id,
												menuName: operateNode.data.text,
												menuYxState: operateNode.data.menuYxState,
												comId: operateNode.data.comId,
												bigImgId: operateNode.data.bigImgId,
												smallImgId: operateNode.data.smallImgId,
												helpId: operateNode.data.helpId,
												NodeType: "",
												operateNode: "",
												rootNode: rootNode,
												comName: operateNode.data.comName
						 });
						 form.findField("menuId").setReadOnly(true); 
						 panel.actType = "update"; 
						
						/****
						 var tzParams = '{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENUXX_STD","OperateType":"QF","comParams":{"typeFlag":"FORM","menuId":"'+operateNode+'"}}';
						 Ext.tzLoad(tzParams,function(responseData){
					     //菜单信息数据
					     var formData = responseData.formData;
					     form.setValues(formData);
					     form.findField("rootNode").setValue(rootNode);
					     bt.findParentByType("menuEdit").actType ="update";
					   });
					   ****/
					}else{
							var tzParams = this.getOrgMenuInfoDeleteParams(form);
				      Ext.tzSubmit(tzParams,function(responseData){
				      	 if(rootNode == menuId){
				      	 	   panel.close();
				      	 }else{
										var thisNode = treepanelStore.getNodeById(menuId);
						  			 var pNode = thisNode.parentNode;
										 treepanel.getSelectionModel().select(pNode);
										 form.setValues({
										            menuId: pNode.data.id,
																menuName: pNode.data.text,
																menuYxState: pNode.data.menuYxState,
																comId: pNode.data.comId,
																bigImgId: pNode.data.bigImgId,
																smallImgId: pNode.data.smallImgId,
																helpId: pNode.data.helpId,
																NodeType: "",
																operateNode: "",
																rootNode: rootNode,
																comName: pNode.data.comName
										 });
										 form.findField("menuId").setReadOnly(true); 
										 panel.actType = "update"; 
				
										 //thisNode.removeAll();
										 pNode.removeChild(thisNode);
										 if (pNode.hasChildNodes( ) == false){
										 		pNode.set('leaf', true);
										 }
				      	 }
				         
				      },"",true,this);
						 
						 
						
					/*	
						var treepanelStore = bt.findParentByType("menuEdit").child("treepanel").getStore();
						
						var tzParams = this.getOrgMenuInfoDeleteParams(form);
			      Ext.tzSubmit(tzParams,function(responseData){
			      	 if(rootNode == menuId){
			      	 	   bt.findParentByType("menuEdit").close();
			      	 }else{
			      	 	   form.findField("menuId").setReadOnly(true);
					         treepanelStore.getProxy().extraParams.nodeId = "";
					         treepanelStore.load();
					         
					         var formData = responseData.formData;
				           form.setValues(formData);
				           form.findField("rootNode").setValue(rootNode);
				           bt.findParentByType("menuEdit").actType ="update";
					        // var tzParams = '{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENUXX_STD","OperateType":"QF","comParams":{"typeFlag":"PARENTFORM","menuId":"'+menuId+'"}}';
					        //   Ext.tzLoad(tzParams,function(responseData){
				             //菜单信息数据
				          // 	 var formData = responseData.formData;
				          //   form.setValues(formData);
				          //   form.findField("rootNode").setValue(rootNode);
				     
				    			//});
			      	 }
			         
			      },"",true,this);
			      */
			    }
	      }
			},this);
			
		},
    getOrgMenuInfoParams: function(){
        //功能菜单表单
        var form = this.getView().child("form").getForm();
        //功能菜单标志
        var actType = this.getView().actType;
        //获取数据语言代码
        var dataLanguage = this.getView().dataLanguage;
        //更新操作参数
        var comParams = "";
        //新增
        /*
        if(actType == "add"){
            comParams = '"add":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
        }
        //修改json字符串
        var editJson = "";
        if(actType == "update"){
            editJson = '{"data":'+Ext.JSON.encode(form.getValues())+'}';
        }


        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }
 				*/
  			comParams = '"add":[{"data":'+Ext.JSON.encode(form.getValues())+',"dataLanguage":"' + dataLanguage + '","synchronous":false}]';
        //提交参数
       	var tzParams = '{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENUXX_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    getOrgMenuInfoDeleteParams: function(form){
        //功能菜单表单
        //var form = this.getView().child("form").getForm();
        //功能菜单标志
        //var actType = this.getView().actType;
        //获取数据语言代码
        var dataLanguage = this.getView().dataLanguage;
        //删除参数
        var comParams = "";
        
  			comParams = '"delete":[{"data":'+Ext.JSON.encode(form.getValues())+',"dataLanguage":"' + dataLanguage + '","synchronous":false}]';
        //提交参数
       	var tzParams = '{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENUXX_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    copyFuncMenu: function() {
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			var menuView = this.getView();
			var menuForm = menuView.child("form").getForm();
			var menuId = menuForm.findField("rootNode").getValue();
			
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_MENU_COM"]["TZ_AQ_MENUCOPY_STD"];
			if( pageResSet == "" || typeof(pageResSet) == "undefined" ){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || typeof(className) == "undefined"  ){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AQ_MENUCOPY_STD，请检查配置。');
				return;
			}
			
			var win = this.lookupReference('securityMenuInfoSourcePanel');
	        
	    if (!win) {
				Ext.syncRequire(className);
				ViewClass = Ext.ClassManager.get(className);
			  //新建类
	      win = new ViewClass();
	     
	      this.getView().add(win);
	      var form = win.child("form").getForm();
  			form.findField("menuOrg").setValue(menuId);

  			var me = this;
  			win.on('close',function( panel, eOpts ){
  				 var confirmBz =  panel.child("form").getForm().findField("confirmBz").getValue();
	      	 if(confirmBz != "Y"){
	      	  	return;
	      	 }
	      	 
	      	 //功能菜单树表单信息;
            var form = menuView.child('form').getForm();
            form.findField("menuId").setReadOnly(true);
            //treepanel
            var treepanel = menuView.child('treepanel');
            

            treepanel.reconfigure( new KitchenSink.view.security.menu.menuTreeStore({menuId: menuId,dataLanguage: menuView.dataLanguage}));
            me.setDefaultMenoNode(treepanel.getStore().getRoot());
            
	      	 /**************
	      	 //功能菜单树表单信息;
            var form = menuView.child('form').getForm();
            form.findField("menuId").setReadOnly(true);
            //treepanel
            var treepanel = menuView.child('treepanel');
            
           var tzParams ='{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENUXX_STD","OperateType":"QF","comParams":{"typeFlag":"TREE","menuId":"'+menuId+'"}}';
						treepanel.getStore().setProxy({
									type: 'ajax',
									url:  '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_ZSGL_D.TZ_ZSGL.FieldFormula.IScript_TZ_ZSGL?tzParams='+tzParams,
									//url:  '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_BJXZQ.TZ_BJXZQ.FieldFormula.IScript_test',
									reader: {
										type : 'json', 
										rootProperty: 'comContent.root.menuTree'
									}
							});
						treepanel.getStore().load();			
            //参数
            
            var tzParams = '{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENUXX_STD","OperateType":"QF","comParams":{"typeFlag":"FORM","menuId":"'+menuId+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
            	//菜单那信息数据
            	var formData = responseData.formData;
                
            	form.setValues(formData);
            });
	      	 ***************/
  			});
	    }
      
       win.show();
    },
    pmtSearchCom: function(btn){
			var form = this.getView().child("form").getForm();
			Ext.tzShowPromptSearch({
				recname: 'TZ_AQ_COMZC_TBL',
				searchDesc: '搜索组件',
				maxRow:20,
				condition:{
					srhConFields:{
						TZ_COM_ID:{
							desc:'组件ID',
							operator:'07',
							type:'01'	
						},
						TZ_COM_MC:{
							desc:'组件名称',
							operator:'07',
							type:'01'		
						}	
					}	
				},
				srhresult:{
					TZ_COM_ID: '组件ID',
					TZ_COM_MC: '组件名称'	
				},
				multiselect: false,
				callback: function(selection){
					form.findField("comId").setValue(selection[0].data.TZ_COM_ID);
					form.findField("comName").setValue(selection[0].data.TZ_COM_MC);
				}
			});	
		},
		clearPmtSearchCom: function(btn){
			var form = this.getView().child("form").getForm();
			form.findField("comId").setValue("");
			form.findField("comName").setValue("");
			
		},


		setDefaultMenoNode: function(node)
		{
		  var view = this.getView();
		  var form = view.child("form").getForm();
		  form.setValues({
		                   menuId: node.data.id,
		                   menuName: node.data.text,
		                   menuYxState: node.data.menuYxState,
		                   comId: node.data.comId,
		                   bigImgId: node.data.bigImgId,
		                   smallImgId: node.data.smallImgId,
		                   helpId: node.data.helpId,
		                   NodeType: node.data.NodeType,
		                   operateNode: node.data.operateNode,
		                   rootNode: view.menuId,
		                   comName: node.data.comName
		                 });
		  form.findField("menuId").setReadOnly(true); 
		  view.actType = "update";
		},


		changeLanguage: function(btn)
		{
		  var me = this;
		  var myView = this.getView();

		  //先判断当前是否已修改且尚未保存
		  if(myView.isChangesCommited(myView) === false)
		  {
		    Ext.MessageBox.show(
		    {
		      title: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00014"),
		      width: 500,
		      msg: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00073"),
		      buttons: Ext.Msg.YESNO,
		      buttonText:
		      {
		        yes: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00074"),
		        no: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00019")
		      },
		      icon: Ext.Msg.WARNING,
		      fn: function(btnClicked)
		      {
		        if(btnClicked === "yes")
		        {
		          //继续切换数据语言
		          me.private_changeLanguage(btn,myView);
		        }
		      }
		    });
		  }
		  else
		  {
		    //继续切换数据语言
		    me.private_changeLanguage(btn,myView);
		  }
		},
		private_changeLanguage: function(btn,vObject)
		{
		  var me = this;
		  var myView = vObject;

		  //弹出语言选择框
		  var win = this.lookupReference('changeDataLanguage');
		  if(!win)
		  {
		    win = new KitchenSink.view.common.changeDataLanguage();
		    win.ensureHandler = function(language)
		    {
		      myView.dataLanguage = language;


		      var form = myView.child('form').getForm();
		      form.findField("menuId").setReadOnly(true);


		      var menuForm = myView.child("form").getForm();
		      var menuId = menuForm.findField("rootNode").getValue();


		      var treepanel = myView.child('treepanel');
		      treepanel.reconfigure(new KitchenSink.view.security.menu.menuTreeStore({menuId: menuId,dataLanguage: myView.dataLanguage}));
		      me.setDefaultMenoNode(treepanel.getStore().getRoot());
		    }
		    this.getView().add(win);
		  }

		  var form = win.child("form").getForm();
		  form.reset();
		  form.setValues({msgLanage:myView.dataLanguage});
		  win.show();
		},


		synchrLanguage: function(btn)
		{
		  var myView = this.getView();


		  //弹出多语言资源数据同步选择框
		  var win = this.lookupReference('synMultiLanguageData');
		  if(!win)
		  {
		    win = new KitchenSink.view.common.synMultiLanguageData();
		    win.ensureHandler = function(sLanguage,tLanguage)
		    {
		      var comParams = '"add":[{"menuId":"' + myView.menuId + '","sDataLanguage":"' + sLanguage + '","tDataLanguage":"' + tLanguage + '","synchronous":true}]';
		      var tzParams = '{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENUXX_STD","OperateType":"U","comParams":{'+comParams+'}}';
		      Ext.tzSubmit(tzParams,null,TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00081"));
		    }
		    this.getView().add(win);
		  }

		  var form = win.child("form").getForm();
		  form.reset();
		  form.setValues({sourceLanage:myView.dataLanguage});
		  win.show();
		}
});