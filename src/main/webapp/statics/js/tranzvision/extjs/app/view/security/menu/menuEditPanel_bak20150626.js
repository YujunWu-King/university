Ext.apply(Ext.form.field.VTypes, {  
  munuEditNameValidator:  function(value,field) {  
  var flag = false;  
  var actType = field.findParentByType("menuEdit").actType;

  Ext.Ajax.request({
     async: false,
     url: Ext.tzGetGeneralURL,
     params: {
     	 "tzParams":'{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENULIST_STD","OperateType":"HTML","comParams":{"menuId":"'+ value +'","actType":"'+ actType +'"}}'
     },
     async:false,  
		 success: function(response){
				var responseText = eval( "(" + response.responseText + ")" );
				if(responseText.success == "true"){
					flag = true;
				}
			}
	});
  return flag;  
  },  
  munuEditNameValidatorText: '菜单节点编号已经被使用'
});

Ext.define('KitchenSink.view.security.menu.menuEditPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'menuEdit',
		controller: 'menuEdit',
		requires: [
	    'Ext.data.*',
      'Ext.util.*',
	    'KitchenSink.view.security.menu.menuEditController',
	    'KitchenSink.view.security.menu.menuTreeStore',
      'Ext.data.TreeStore'
		],
    title: '机构功能菜单管理',
    width: 640,
    layout: 'border',
    viewModel: true,
    actType: 'update',
    initComponent: function() {
    	  me = this;
        this.items = [
            {
                //columnWidth: 0.3,
                //margin: "10 5 0 0",
                title: '功能菜单树',
                region:'west',
                xtype: 'treepanel',
                width: 300,
                split: true,
                collapsible: true,
              // height: 400,
                autoScroll : true,
                lines: true,
								rootVisible: true,
								//store: 'menuTreeStore',
								//store: Ext.StoreMgr.get('menuTreeStore'),
								//store: Ext.StoreMgr.get('navigation'),
								store: new KitchenSink.view.security.menu.menuTreeStore({menuId: me.menuId}),
								viewConfig: {
								    plugins: { 
								    	ptype: 'treeviewdragdrop' 
								    },
								    listeners: {
								    	drop: function( node, data, overModel, dropPosition, eOpts) {
								    		var treepanel = me.down("treepanel");
								    		var selList = treepanel.getSelectionModel().getSelection( ) ;
								    		if (selList.length == 1){
								    			var operateNode = selList[0];
								    									    		
									    		//console.log(node);
									    		//console.log(data);
									    		//console.log(overModel);
									    		//console.log(dropPosition);
									    		
									    		var prevNode = operateNode.previousSibling;
									    		var parentNode = operateNode.parentNode;
									    		
									    		var nodeId, prevNodeId, parentNodeId;
									    		nodeId = operateNode.data.nodeId;
									    		parentNodeId = parentNode.data.nodeId;
									    		if (!prevNode && typeof(prevNode)!="undefined"){  
													   prevNodeId = ""; 
													}else{
														 prevNodeId = prevNode.data.nodeId;
													}
													
									    		if(dropPosition == "before"){
									    			 console.log(nodeId +"-->"+ prevNodeId +"-->"+ parentNodeId);
									    		} 
									    		
									    		if(dropPosition == "after"){
									    			 console.log(nodeId +"-->"+ prevNodeId +"-->"+ parentNodeId);
									    		} 
									    		
									    		if(dropPosition == "append"){
									    			  console.log(nodeId +"-->"+ prevNodeId +"-->"+ parentNodeId);
									    		} 
									    		
									    		 var tzParams = '{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENUXX_STD","OperateType":"HTML","comParams":{"nodeId":"'+nodeId+'","prevNodeId":"'+prevNodeId+'","parentNodeId":"'+parentNodeId+'"}}';
									    		 Ext.Ajax.request({
															 async: false,
													     url: Ext.tzGetGeneralURL,
													     params: {
													        "tzParams": tzParams
													     },
													     async:false,  
															 success: function(response){
															 		console.log("-->");
															 }
														});
								    		}
								    		
								    	}
								    }
								},
	        			listeners : {  
	            		//beforeitemexpand : function(node,eOpts){
	            			//try{
							            			 	// var tzParams ='{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENUXX_STD","OperateType":"QF","comParams":{"typeFlag":"NODE","menuId":"'+ node.data.nodeId+'"}}';               	
							                		//	this.getStore().setProxy({
																	//			type: 'ajax',
																	//			url:  '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_ZSGL_D.TZ_ZSGL.FieldFormula.IScript_TZ_ZSGL?tzParams='+tzParams,
																	//			reader: {
																	//				type : 'json', 
																	//				rootProperty: 'comContent.root.menuTree'
																	//			}
																	//	});
											//this.getStore().getProxy( ).extraParams.nodeId =  node.data.nodeId;
	                	
	                //}catch( e){
	                		
	                //	}
	            		//},
	            		afterrender: function( thisTree ){
            			 
	            			 /*
	            			 form.setValues({
	            			 			"menuId": rootNode.id,
													"menuName": rootNode.text,
													"menuYxState": rootNode.menuYxState,
													"comId": rootNode.comId,
													"bigImgId": rootNode.bigImgId,
													"smallImgId": rootNode.smallImgId,
													"helpId": rootNode.helpId,
												  "NodeType": rootNode.NodeType,
													"operateNode": rootNode.operateNode,
												  "rootNode": me.menuId,
												  "comName": rootNode.comName
	            			 });
	            			 */
	            			 
	            			 
	            			// var pNode = treeStore.getNodeById( "TZLX_WY_BUGMG" );
	            			// pNode.insertChild(0,{
	            			// 		"id": "11111111111",
										//		"nodeId": "22222222",
										//		"text": "333333",
										//		"leaf": true
	            			// });
	            			// pNode.leaf = false;
	            			// pNode.expand();
	            		},
	            		itemclick: function( view , record, item, index, e, eOpts ){			 
						            			 
						         var form = view.findParentByType("menuEdit").child("form").getForm();
						         form.setValues({
						            menuId: record.data.id,
												menuName: record.data.text,
												menuYxState: record.data.menuYxState,
												comId: record.data.comId,
												bigImgId: record.data.bigImgId,
												smallImgId: record.data.smallImgId,
												helpId: record.data.helpId,
												NodeType: record.data.NodeType,
												operateNode: record.data.operateNode,
												rootNode: me.menuId,
												comName: record.data.comName
						         });
						         form.findField("menuId").setReadOnly(true); 
						         view.findParentByType("menuEdit").actType = "update"; 
	            			/*
	            			  var form = view.findParentByType("menuEdit").child("form").getForm();
	            			  var rootNode = form.findField("rootNode").getValue();
	            			  var tzParams = '{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENUXX_STD","OperateType":"QF","comParams":{"typeFlag":"FORM","menuId":"'+record.data.nodeId+'"}}';
	            			  Ext.tzLoad(tzParams,function(responseData){
                				//菜单信息数据
           					     var formData = responseData.formData;
                				 form.setValues(formData);
                				 form.findField("rootNode").setValue(rootNode);
         						  });
         						  */
	            		}
	            	} 
            }, {
               // columnWidth: 0.7,
                //margin: "10 0 0 5",
                region: 'center', 
                frame: true,
                //title: 'Framed Panel',
               // glyph: 117,
                xtype: 'form',
                reference: 'menuEditForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                //height: 400,
                bodyStyle: 'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth:120,
                    labelStyle: 'font-weight:bold'
                },

                items: [{
                    xtype:"toolbar",
                    items:[
                        {text:"插入同级节点",tooltip:"插入同级节点",handler: 'inserMenueItem'},"-",
                        {text:"插入子节点",tooltip:"插入子节点",handler: 'inserChildMenueItem'},"-",
                        {text:"删除",tooltip:"删除",handler: 'removeMenueItem'}
                    ]},
                    {
                        xtype: 'textfield',
                        //fieldLabel: '菜单节点编号',
                        fieldLabel: Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENUXX_STD.menuId", "菜单节点编号"),
                        name: 'menuId',
                        beforeLabelTextTpl: [
                            '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                        ],
                        allowBlank: false,
                        validateOnChange: false,
												validateOnBlur: true,
                        vtype: 'munuEditNameValidator'
                    },
                    {
                        xtype: 'textfield',
                        //fieldLabel: '菜单名称',
                        fieldLabel: Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENUXX_STD.menuName", "菜单名称"),
                        name: 'menuName',
                        beforeLabelTextTpl: [
                            '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                        ],
                        allowBlank: false
                    },
                    {
                        xtype: 'combobox',
                        //fieldLabel: '有效状态',
                        fieldLabel: Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENUXX_STD.menuYxState", "有效状态"),
                        forceSelection: true,
                        valueField: 'TValue',
                        displayField: 'TSDesc',
                        store: new KitchenSink.view.common.store.appTransStore("TZ_YXX"),
                        typeAhead: true,
                        queryMode: 'local',
                        name: 'menuYxState'

                    },{
											layout: {
												type: 'column',
											},
											items:[{
												columnWidth:.55,
												xtype: 'textfield',
												fieldLabel: Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENUXX_STD.comId", "菜单对应组件ID"),
												name: 'comId',
												editable: false,
												triggers: {
													clear: {
                            cls: 'x-form-clear-trigger',
                            handler: 'clearPmtSearchCom'
                        	},
								          search: {
								             cls: 'x-form-search-trigger',
								             handler: "pmtSearchCom"
								          }
								        }
											},{
												columnWidth:.45,
												xtype: 'displayfield',
												hideLabel: true,
												style:'margin-left:5px',
												name: 'comName'
											}]
								    },
                    {
                        xtype: 'textfield',
                        //fieldLabel: '菜单大图标ID',
                        fieldLabel: Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENUXX_STD.bigImgId", "菜单大图标ID"),
                        name: 'bigImgId'
                    },
                    {
                        xtype: 'textfield',
                        //fieldLabel: '菜单小图标ID',
                        fieldLabel: Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENUXX_STD.smallImgId", "菜单小图标ID"),
                        name: 'smallImgId'
                    },
                    {
                        xtype: 'textfield',
                        //fieldLabel: '帮助信息内容ID',
                        fieldLabel: Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENUXX_STD.helpId", "帮助信息内容ID"),
                        name: 'helpId'
                    },
                    {
                    	//插入同级节点还是子节点,Y:表示同级节点，'N'表示子节点;
                        xtype: 'textfield',
                        name: 'NodeType',
                        hidden: true
                    },
                    {
                    	//插入同级节点或子节点是在哪个节点上操作的;
                        xtype: 'textfield',
                        name: 'operateNode',
                        hidden: true
                    },
                    {
                    	//跟节点;
                        xtype: 'textfield',
                        name: 'rootNode',
                        hidden: true
                    }
                ],
                listeners : {
                	afterrender: function( thisForm ){

	            		}
                }
            }
        ];

        this.callParent();
    },
    listeners : {
        afterrender: function( panel ){
        	 var thisTree = panel.child("treepanel");
           var treeStore = thisTree.getStore();
	         var rootNode = treeStore.getNodeById( me.menuId );
	         thisTree.getSelectionModel().select(rootNode);
 			 
	         var form = panel.child("form").getForm();
	         form.setValues({
	            menuId: rootNode.data.id,
							menuName: rootNode.data.text,
							menuYxState: rootNode.data.menuYxState,
							comId: rootNode.data.comId,
							bigImgId: rootNode.data.bigImgId,
							smallImgId: rootNode.data.smallImgId,
							helpId: rootNode.data.helpId,
							NodeType: rootNode.data.NodeType,
							operateNode: rootNode.data.operateNode,
							rootNode: me.menuId,
							comName: rootNode.data.comName
	         });
	         form.findField("menuId").setReadOnly(true);              		
        }
    },

    buttons: [{
        text: '复制菜单到当前机构',
        iconCls:"save",
        handler: 'copyFuncMenu'
    },{
        text: '保存',
        iconCls:"save",
        handler: 'onFormSave'
    }, {
			text: '确定',
			iconCls:"ensure",
			handler: 'onFormEnsure'
		}, {
			text: '关闭',
			iconCls:"close",
			handler: 'onFormClose'
		}],
		constructor: function (config) {
			//机构主菜单ID;
			this.menuId = config.menuId;

			this.callParent();
		}
});
