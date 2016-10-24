Ext.define('KitchenSink.view.siteManage.outsiteManage.menuEditPanel',{
	extend : 'Ext.panel.Panel',
	xtype : 'menuEdit',
	controller : 'menuEdit',
	requires : ['Ext.data.*',
	            'Ext.util.*',
	            'KitchenSink.view.siteManage.outsiteManage.menuEditController',
	            'KitchenSink.view.siteManage.outsiteManage.menuTreeStore',
	            'KitchenSink.view.siteManage.outsiteManage.menuStore',
	            'Ext.data.TreeStore' ],
	title : '站点菜单管理',
	width : 640,
	layout : 'border',
	viewModel : true,
	actType : 'update',
	thisMenuId : 'NEXT',

	initComponent : function() {
		me = this;
		this.items = [{
			title : '站点菜单树',
			region : 'west',
			xtype : 'treepanel',
			width : 300,
			split : true,
			collapsible : true,
			autoScroll : true,
			lines : true,
			rootVisible : true,
			store : new KitchenSink.view.siteManage.outsiteManage.menuTreeStore({
				siteId : me.siteId
			}),
			listeners : {
				afterrender : function(thisTree) {
				},
				itemclick : function(view, record,item, index, e, eOpts) {
					var form = view.findParentByType("menuEdit").child("form").getForm();
					form.setValues({
						menuId : record.data.id,
						menuName : record.data.text,
						menuPath : record.data.menuPath,
						menuState : record.data.menuState,
						menuTempletId : record.data.menuTempletId,
						menuTempletName : record.data.menuTempletName,
						menuPageName : record.data.menuPageName,
						menuType : record.data.menuType,
						menuXH : record.data.menuXH,
						menuStyle : record.data.menuStyle,
						isDefault : record.data.isDefault,
						defaultPage : record.data.defaultPage,
						NodeType : record.data.NodeType,
						operateNode : record.data.operateNode,
						rootNode : record.data.rootNode,
						menuShow : record.data.menuShow,
						siteId : me.siteId
					});
					form.findField("menuId").setReadOnly(true);
					form.findField("menuId").addCls('lanage_1'); 
					form.findField("menuPath").setReadOnly(true);
					form.findField("menuPath").addCls('lanage_1'); 
					form.findField("menuType").setReadOnly(true);
					form.findField("menuType").addCls('lanage_1'); 
					
					// 如果是BOOK类型隐藏menuPageName隐藏是否默认页面，显示路径
					// A:PAGE  B:BOOK
					if (record.data.menuType == "B") {
						//form.findField("menuPageName").hide();
						form.findField("isDefault").hide();
						form.findField("menuPath").show();
						form.findField("defaultPage").show();
					
						//form.findField("menuStyle").show();
					} else {
						//form.findField("menuPageName").show();
						form.findField("isDefault").show();
						form.findField("menuPath").hide();
						form.findField("defaultPage").hide();
					
						//form.findField("menuStyle").hide();
					}
					view.findParentByType("menuEdit").actType = "update";
					
					/*var grid = view.findParentByType("menuEdit").child('grid');
					var tzStoreParams = '{"menuId":"' + record.data.id + '"}';
					grid.store.tzStoreParams = tzStoreParams;
					grid.store.load(); */
				}
			}
		},{
			/*xtype: 'panel',
            region: 'center', 
            frame: true,
            title: '菜单',
            reference: 'menuPanel',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            border: false,
            scrollable:true,
            //collapsible : true,
			autoScroll : true,
            bodyStyle: 'overflow-y:auto;overflow-x:hidden',

            fieldDefaults: {
                msgTarget: 'side',
                labelWidth:120,
                labelStyle: 'font-weight:bold'
            },*/
			//items: [{ 
				region : 'center',
				frame : true,
				xtype : 'form',
				reference : 'menuEditForm',
				layout : {
					type : 'vbox',
					align : 'stretch'
				},
				border : false,
				bodyPadding : 10,
				bodyStyle : 'overflow-y:auto;overflow-x:hidden',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 120,
					labelStyle : 'font-weight:bold'
				},
				items : [{
					xtype : "toolbar",
					items : [{
						text : "插入同级节点",
						iconCls : 'siblingnode',
						tooltip : "插入同级节点",
						handler : 'inserMenuItem'
					},"-",{
						text : "插入子节点",
						iconCls : 'childnode',
						tooltip : "插入子节点",
						handler : 'inserChildMenuItem'
					},"-",{
						text : "删除",
						iconCls : 'remove',
						tooltip : "删除",
						handler : 'removeMenuItem'
					}]
				},{
					xtype : 'textfield',
					fieldLabel : '菜单编号',
					name : 'menuId',
					//allowBlank : false,
					value : 'NEXT'
				},{
					// 站点ID
					xtype : 'textfield',
					name : 'siteId',
					hidden : true
				},{
		            xtype: 'textfield',
					name: 'saveAttachAccessUrl',
					hidden: true
		        },
				{
		            xtype: 'textfield',
					name: 'saveImageAccessUrl',
					hidden: true
		        },{
					xtype : 'textfield',
					fieldLabel : '菜单名称',
					name : 'menuName',
					beforeLabelTextTpl : [ '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>' ],
					allowBlank : false
				},{
					xtype : 'textfield',
					fieldLabel : '顺序',
					name : 'menuXH',
					beforeLabelTextTpl : [ '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>' ],
					allowBlank : false
				},{
					xtype : 'combobox',
					fieldLabel : '有效状态',
					forceSelection : true,
					allowBlank : false,
					valueField : 'TValue',
					displayField : 'TSDesc',
					store : new KitchenSink.view.common.store.appTransStore("TZ_YXX"),
					typeAhead : true,
					queryMode : 'local',
					name : 'menuState'
				},{
					xtype : 'combobox',
					fieldLabel : '菜单类型',
					forceSelection : true,
					allowBlank : false,
					valueField : 'TValue',
					displayField : 'TSDesc',
					//allowBlank : false,
					store : new KitchenSink.view.common.store.appTransStore("TZ_ZDCD_LX"),
					typeAhead : true,
					queryMode : 'local',
					name : 'menuType',
					listeners : {
						select : function(combo,record,index){	
							form= combo.findParentByType("form").getForm();
							// 如果是BOOK类型隐藏menuPageName隐藏是否默认页面，显示路径
							if (combo.getValue() == "B") {
								//form.findField("menuPageName").hide();
								form.findField("isDefault").hide();
								form.findField("menuPath").show();
								form.findField("defaultPage").show();	
								form.findField("menuStyle").show();
							} else {
								//form.findField("menuPageName").show();
								form.findField("isDefault").show();
								form.findField("menuPath").hide();
								form.findField("defaultPage").hide();
								form.findField("menuStyle").hide();
							}
						}
					}
				},{
					xtype : 'textfield',
					fieldLabel : '页面名称',
					name : 'menuPageName',
					//allowBlank : false
				},{
					xtype : 'textfield',
					fieldLabel : '菜单样式',
					name : 'menuStyle'
				},{
					xtype : 'textfield',
					fieldLabel : '菜单路径',
					name : 'menuPath'
				},{
					xtype: 'checkboxfield',
					fieldLabel  : '是否默认首页',
					name : 'isDefault',
					inputValue: 'Y'
				},{
					xtype: 'checkboxfield',
					fieldLabel  : '是否显示',
					inputValue: 'Y',
					name : 'menuShow'
				}
				,{
					layout : {
						type : 'column'
					},
					items : [{
						columnWidth : .55,
						xtype : 'textfield',
						fieldLabel : '菜单模板',
						name : 'menuTempletId',
						editable : false,
						triggers : {
							clear : {
								cls : 'x-form-clear-trigger',
								handler : 'clearPmtSearchTemplet'
							},
							search : {
								cls : 'x-form-search-trigger',
								handler : "pmtSearchTemplet"
							}
						}
					},{
						columnWidth : .25,
						xtype : 'displayfield',
						hideLabel : true,
						style : 'margin-left:5px',
						name : 'menuTempletName'
					},{
						//columnWidth : .20,
						xtype: 'displayfield',
						fieldLabel  : '默认首页',
						name : 'defaultPage'
					}]
				}
				,{
					// 插入同级节点还是子节点,Y:表示同级节点，'N'表示子节点;
					xtype : 'textfield',
					name : 'NodeType',
					hidden : true
				},{
					// 插入同级节点或子节点是在哪个节点上操作的;
					xtype : 'textfield',
					name : 'operateNode',
					hidden : true
				}, {
					// 跟节点;
					xtype : 'textfield',
					name : 'rootNode',
					hidden : true
<<<<<<< HEAD
				},
				
				
				{
		        	  xtype: 'tabpanel',
		        	  frame: true,
		        	  items:[{
		        	  	title: "标题图",
		        	  	layout: {
		            		type: 'column',
		        			},
		        	  	items:[{
								    columnWidth:.2,
								    bodyStyle:'padding:10px',  
									  layout: {
									  	type: 'vbox',
		           			 	align: 'stretch'
									  },
										items: [{
		            			xtype: "image",  
											src: ""	,
											name: "titileImage"
											//id: "titileImage"
		        				},{
		            			layout: {
									  		type: 'column'
									  	},
									  	bodyStyle:'padding:10px 0 0 0',
									  	xtype: 'form',  
											items: [{
												columnWidth:.65,
		            				xtype: "fileuploadfield",  
												buttonText: '上传',
												//name: 'picUpload',
												name: 'websitefile',
												
												buttonOnly:true,
												listeners:{
													change:function(file, value, eOpts){
														addAttach(file, value, "IMG");
													}
												}
		        					},{
		        						columnWidth:.35,
		            				xtype: 'button',
		            				text: '删除'/*,
												listeners:{
													click:function(bt, value, eOpts){
														deleteImage(bt, value, eOpts);
													}
												}*/
		        					}]
		        				}]
									},{
										columnWidth:.8,
										bodyStyle:'padding:10px 10px 10px 30px',  
									  layout: {
									  	type: 'vbox',
		           			 	align: 'stretch'
									  },
										items: [{
		            			xtype: 'textfield',
		            			fieldLabel: '标题',
		            			maxLength :100,
								name: 'titleImageTitle'
		        				},{
		            			xtype: 'textarea',
		            			fieldLabel: '说明',
		            			maxLength :254,
								name: 'titleImageDesc'
		        				}]
									}]
		        	  }]
					/*xtype : 'textfield',
					fieldLabel  : '测试',
					name : 'rootNode'*/
					
				}
				
				
				
				],
            	
            //}],
				
				
				
				
=======
				}],
>>>>>>> d2d10ecc9a7f5041ab9d8a60e6bf9b4db9962362
				listeners : {
					afterrender : function(thisForm) {
					}
				}
			}/*,{
				xtype: 'grid',
				title: '子菜单列表',
				frame: true,
				columnLines: true,
				multiSelect: true,
				height:350,
				flex: 1,
				reference: 'menuEditGrid',
				style:"margin:0 10px 10px 10px",
				store: {
					type: 'menuStore'
				},
				columns: [{
	        		text: '菜单编号',
					dataIndex: 'menuId',
					hidden: true,
					width: 100
				},{
					text: '菜单名称',
					dataIndex: 'menuName',
					minWidth: 5,
					flex: 1
				},{
					text: '菜单类型',
					dataIndex: 'menuType',
					width: 100
				},{
					text: '菜单循序',
					dataIndex: 'menuXH',
					width: 100
				}],
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 5,
					listeners:{
						afterrender: function(pbar){
							var grid = pbar.findParentByType("grid");
							pbar.setStore(grid.store);
						}
					},
					plugins: new Ext.ux.ProgressBarPager()
				}
			}
		]}*/
		];
		this.callParent();
	},
	listeners : {
		afterrender : function(panel) {
			var thisTree = panel.child("treepanel");
			var treeStore = thisTree.getStore();
			var rootNode = treeStore.getRoot();
			thisTree.getSelectionModel().select(rootNode);

			
			//var refs = me.getReferences(),
			//	menuPanel = refs.menuPanel;
			//var form =menuPanel.child("form").getForm();
			var form = panel.child("form").getForm();
			form.setValues({
				menuId : rootNode.data.id,
				menuName : rootNode.data.text,
				menuPath : rootNode.data.menuPath,
				menuState : rootNode.data.menuState,
				menuTempletId : rootNode.data.menuTempletId,
				menuPageName : rootNode.data.menuPageName,
				menuType : rootNode.data.menuType,
				isDefault : rootNode.data.isDefault,
				menuXH : rootNode.data.menuXH,
				menuStyle : rootNode.data.menuStyle,
				defaultPage : rootNode.data.defaultPage,
				NodeType : rootNode.data.NodeType,
				operateNode : rootNode.data.operateNode,
				rootNode : rootNode.data.rootNode,
				siteId : me.siteId,
				menuShow:rootNode.data.mennuShow,
				menuTempletName : rootNode.data.menuTempletName
			});
			form.findField("menuId").setReadOnly(true);
			form.findField("menuId").addCls('lanage_1'); 

			form.findField("menuPath").setReadOnly(true);
			form.findField("menuPath").addCls('lanage_1');
			form.findField("menuType").setReadOnly(true);
			form.findField("menuType").addCls('lanage_1'); 
			
			// 如果是BOOK类型隐藏menuPageName隐藏是否默认页面，显示路径
			if (rootNode.data.menuType == "B") {
				//form.findField("menuPageName").hide();
				form.findField("isDefault").hide();
				form.findField("menuPath").show();
				form.findField("defaultPage").show();
				//form.findField("menuStyle").show();
			} else {
				//form.findField("menuPageName").show();
				form.findField("isDefault").show();
				form.findField("menuPath").hide();
				form.findField("defaultPage").hide();
				//form.findField("menuStyle").hide();
			}
			
			/*var grid = panel.child("grid");
			//var grid =menuPanel.child("grid");
			var tzStoreParams = '{"menuId":"' + rootNode.data.id + '"}';
			grid.store.tzStoreParams = tzStoreParams;
			grid.store.load(); */
		}
	},
	buttons : [{
		text : '生成一级菜单',
		iconCls : "save",
		handler : 'cerateTopMenu'
	}, {
		text : '生成本级菜单',
		iconCls : "save",
		handler : 'createThisMenu'
	}, {
		text : '保存',
		iconCls : "save",
		handler : 'onFormSave'
	}, {
		text : '确定',
		iconCls : "ensure",
		handler : 'onFormEnsure'
	}, {
		text : '关闭',
		iconCls : "close",
		handler : 'onFormClose'
	}],
	constructor : function(config) {
		// 机构主菜单ID;
		this.siteId = config.siteId;
		this.callParent();
	}
});

function addAttach(file, value, attachmentType){
	//Ext.Msg.alert('提示','1122');
	var form = file.findParentByType("form").getForm();
	
	if(value != ""){
		if(attachmentType=="IMG" || attachmentType=="TPJ"){ 
			var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
			//Ext.Msg.alert('提示',fix);
			if(fix.toLowerCase() != "jpg" && fix.toLowerCase() != "jpeg" && fix.toLowerCase() != "png" && fix.toLowerCase() != "gif" && fix.toLowerCase() != "bmp"){
				Ext.MessageBox.alert("提示","请上传jpg|jpeg|png|gif|bmp格式的图片。");
				form.reset();
				return;
				};	
		}
	
		//如果是附件则存在在附件的url中，如果是图片在存放在图片的url中;
		//var dateStr = Ext.Date.format(new Date(), 'Ymd');      
		
		var upUrl = "";
		
		var siteId = file.findParentByType("menuEdit").child("form").getForm().findField("siteId").getValue();
		//Ext.Msg.alert('提示',siteId);
		if(siteId==""){
			Ext.Msg.alert("错误","不存在站点，请先为该机构新建站点");
			return;
		}
		
		if(attachmentType=="ATTACHMENT"){ 
			
			upUrl = file.findParentByType("menuEdit").child("form").getForm().findField("saveAttachAccessUrl").getValue();
			//Ext.Msg.alert('提示',upUrl);
			
			if(upUrl==""){
				Ext.Msg.alert("错误","未定义上传附件的路径，请与管理员联系");
				return;
			}
		}else{
			
			upUrl = file.findParentByType("menuEdit").child("form").getForm().findField("saveImageAccessUrl").getValue();
			alert(upUrl);
			if(upUrl==""){
				Ext.Msg.alert("错误","未定义上传图片的路径，请与管理员联系");
				return;
			}
		}
		
		upUrl = TzUniversityContextPath + '/UpdWebServlet?siteid='+siteId+'&filePath='+upUrl;
		
		
		var myMask = new Ext.LoadMask({
	    msg    : '加载中...',
	    target : Ext.getCmp('tranzvision-framework-content-panel')
		});
		
		myMask.show();
		
		form.submit({
			url: upUrl,
			//waitMsg: '图片正在上传，请耐心等待....',
			success: function (form, action) {
				var tzParams; 
			  var picViewCom;
			
			  if(attachmentType=="TPJ"){
			  	picViewCom = file.findParentByType("tabpanel").down('dataview[name=picView]');
			  	tzParams = '{"order":' + picViewCom.getStore().getCount() + ',"attachmentType":"'+attachmentType+'","data":' + Ext.JSON.encode(action.result.msg) + '}';
			  }else{
			  	tzParams = '{"attachmentType":"' + attachmentType + '","data":' + Ext.JSON.encode(action.result.msg) + '}';
			  }
			  
			  tzParams = '{"ComID":"TZ_CONTENT_MG_COM","PageID":"TZ_CONTENT_INF_STD","OperateType":"HTML","comParams":' + tzParams +'}';
				
				Ext.Ajax.request({
				    url: Ext.tzGetGeneralURL,
				    params: {
				        tzParams: tzParams
				    },
				    success: function(response){
				    	var responseText = eval( "(" + response.responseText + ")" );
				        if(responseText.success == 0){
				        	//viewStore.reload();
				        	var accessPath = action.result.msg.accessPath;
				        	var sltPath = action.result.msg.accessPath;
				        	if(accessPath.length == (accessPath.lastIndexOf("/")+1)){
				        		accessPath = accessPath + action.result.msg.sysFileName;
				        		sltPath = TzUniversityContextPath + sltPath + responseText.minPicSysFileName;
				        		// sltPath = sltPath + "MINI_"+action.result.msg.sysFileName;
				        	}else{
				        		accessPath = accessPath + "/" + action.result.msg.sysFileName;
				        	// 	sltPath = sltPath+ "/" + "MINI_"+action.result.msg.sysFileName;
				        		sltPath = TzUniversityContextPath + sltPath+ "/" + responseText.minPicSysFileName;
				        	}
				        			
				        	if(attachmentType=="IMG"){ 
				        		file.findParentByType("tabpanel").down('image[name=titileImage]').setSrc(TzUniversityContextPath + accessPath);
				        		file.findParentByType("form").findParentByType("form").down('hiddenfield[name=titleImageUrl]').setValue(accessPath);
				  				}
				  				
				  				if(attachmentType=="ATTACHMENT"){ 
										//var applyItemGrid = this.lookupReference('attachmentGrid');
										var applyItemGrid = file.findParentByType("grid")
										var r = Ext.create('KitchenSink.view.activity.attachmentModel', {
											"attachmentID": action.result.msg.sysFileName,
											"attachmentName": "<a href='" + TzUniversityContextPath + accessPath+"' target='_blank'>"+action.result.msg.filename+"</a>",
											"attachmentUrl": accessPath,
    								});
    	 							applyItemGrid.store.insert(0,r);
				  				}
				  				
				  				if(attachmentType=="TPJ"){
				  					 
				  					  var viewStore = picViewCom.store;
				  					  var picsCount = viewStore.getCount();
				  				
				  					  var r = Ext.create('KitchenSink.view.activity.picModel', {
    											"sysFileName": action.result.msg.sysFileName ,
													"index": picsCount+1,
    											"src": accessPath,
    											"caption": action.result.msg.filename,
    											"picURL": "",
    											"sltUrl": sltPath
    									});

    									viewStore.insert(picsCount ,r);
    									viewStore.loadData(r,true);
				  					 // Ext.Msg.alert("",Ext.JSON.encode(action.result.msg));
				  				}
				        }else{
				        	Ext.Msg.alert("提示", responseText.message);
				        }
				    },
				    failure: function (response) {
								Ext.MessageBox.alert("错误", "上传失败");
						}
				});
				//重置表单
				myMask.hide();
				form.reset();
			},
			failure: function (form, action) {
				myMask.hide();
				Ext.MessageBox.alert("错误", action.result.msg);
			}
		});
		
		
		
	}
}
