Ext.define('KitchenSink.view.common.cfgSearchWindow', {
  extend: 'Ext.window.Window',
  xtype: 'cfgSearchWindow', 
  title: '查询', 
  width: 500,
  //height: 620,
  minWidth: 650,
  minHeight: 250,
  //maxHeight: 600,
  resizable: true,
  modal: true,
  closeAction: 'destroy',
  ignoreChangesFlag: true,//让框架程序不要提示用户保存的属性设置
	bodyStyle: 'overflow-y:auto;overflow-x:hidden',
	requires:[
		'tranzvision.extension.grid.column.Link',
		'KitchenSink.view.common.store.promptStore',
		'KitchenSink.view.common.store.comboxStore'
	],
	listeners:{
		resize: function(win){
			win.doLayout();
		}
	},
	
	initComponent: function(){
		var me = this;	

    var tzParams = '{"ComID":"TZ_COMMON_CFG_COM","PageID":"TZ_COMMON_CFG_STD","OperateType":"QF","comParams":{"cfgSrhId":"'+this.cfgSrhId+'"}}';
    
    //搜索条件
		var conItems = [];
		//加载数据
		Ext.tzLoadAsync(tzParams,function(responseData){
			var formData = responseData.formData;
			for(var fieldName in formData) {
				//字段类型
				var fldType = formData[fieldName].fldType;
				//如果是下拉框,则初始化下拉框默认值;
				var downCondition = null;
				var downConditionStr = "";
				if(fldType == "06"){
					 downConditionStr = '{ "TZ_JG_ID":{ "value" : "EMBA", "operator":"01", "type":"01"}}';
					 downCondition = Ext.JSON.decode(downConditionStr);
				}
				//promptTable;
				var promptTable = formData[fieldName].promptTable;
				var promptTableFld = formData[fieldName].promptTableFld;
				var promptTableFldDesc = formData[fieldName].promptTableFldDesc;
				var translateFld = formData[fieldName].translateFld;
				var transCondition = {};
				if(translateFld != ""){
					var transConditionStr = '{ "TZ_ZHZJH_ID":{ "value" : "' + translateFld + '", "operator":"01", "type":"01"}}';
			  	transCondition = Ext.JSON.decode(transConditionStr);
			  }
				//操作符是否只读;
				var operatorReadOnly = formData[fieldName].operatorReadOnly;
				//搜索字段是否字段;
				var fldReadOnly = formData[fieldName].fldReadOnly; 
				if(fldReadOnly == true){
					//如果字段只读，则操作符一定只读;
					operatorReadOnly = fldReadOnly;
				}
				//是否隐藏;
				var fldHidden = formData[fieldName].fldHidden;  
				//搜索条件
				var condition = me.condition;
			
				var fieldValue = "";
				for(var defaultFieldName in condition) {
				
					if(fieldName ==defaultFieldName ){
						
						fieldValue = condition[defaultFieldName];
						break;
					}
				}
				//操作符
				//var operator = formData[fieldName].operator;
				//操作符描述
				/*
				var oprDesc = "";
				
				switch (operator)
				{
				   case '01':
					 	oprDesc = "等于";
				     break;
				   case '02':
				     oprDesc = "不等于";
				     break;
				   case '03':
				     oprDec = "大于";
				     break;
				   case '04':
				     oprDesc = "大于等于";
				     break;
				   case '05':
				     oprDesc = "小于";
				     break;
				   case '06':
				     oprDesc = "小于等于";
				     break;
				   case '07':
				     oprDesc = "包含";
				     break;
				   case '08':
				     oprDesc = "开始于";
				     break;
				   case '09':
				     oprDesc = "结束于";
				     break;
				   case '10':
				     oprDesc = "存在于";
				     break;
				   case '11':
				     oprDesc = "为空";
				     break;
				   case '12':
				     oprDesc = "不为空";
				     break;
				   default:
				     if(fldType == "01"){
				     	oprDesc = "开始于";
				     }else{
				     	oprDesc = "等于";
				     }			     
				}
				*/
				var typeField = {};
				switch (fldType)
				{
					case '01'://字符串
					   typeField = {
				        xtype: 'textfield',
				        columnWidth: 1,
				        hideEmptyLabel: true, 
								name: fieldName+'-value',
								value: fieldValue,
								hidden: fldHidden, 
								readOnly: fldReadOnly,
								listeners: {
	                 specialkey: function (textfield, e) {
	                    if (e.getKey() == Ext.EventObject.ENTER) {
	                       me.searchCfg();
	                    }
	                 }
	              }
				     };
					   break;
					 case '02'://数字
					   typeField = {
				        	xtype: 'numberfield',
				        	columnWidth: 1,
				        	hideEmptyLabel: true, 
				        	value: fieldValue,
				        	readOnly: fldReadOnly,
				        	hidden: fldHidden, 
									name: fieldName+'-value',
								listeners: {
	                 specialkey: function (textfield, e) {
	                    if (e.getKey() == Ext.EventObject.ENTER) {
	                       me.searchCfg();
	                    }
	                 }
	              }
				     };
					   break;
					 case '03'://日期
					   typeField = {
				        	xtype: 'datefield',
				        	columnWidth: 1,
				        	hideEmptyLabel: true,
				        	format: 'Y-m-d', 
				        	value: fieldValue,
				        	readOnly: fldReadOnly,
				        	hidden: fldHidden, 
									name: fieldName+'-value',
								listeners: {
	                 specialkey: function (textfield, e) {
	                    if (e.getKey() == Ext.EventObject.ENTER) {
	                       me.searchCfg();
	                    }
	                 }
	              }
				     };
					   break;
					 case '04'://时间
					   typeField = {
				        	xtype: 'timefield',
				        	columnWidth: 1,
				        	increment: 30,
				        	format: 'H:i',
				        	hideEmptyLabel: true,
				        	hidden: fldHidden, 
				        	value: fieldValue,
				        	readOnly: fldReadOnly,
									name: fieldName+'-value',
								listeners: {
	                 specialkey: function (textfield, e) {
	                    if (e.getKey() == Ext.EventObject.ENTER) {
	                       me.searchCfg();
	                    }
	                 }
	              }
				     };
					   break;
					 case '05'://prompt
					   typeField = {
					   	 	layout: {
									type: 'column',
								},
								//bodyStyle:'padding:0 0 10px 0',
								items:[
					   			{
										//columnWidth: 1,
										maxWidth:120,
										xtype: 'textfield',
										hideEmptyLabel: true, 
										name: fieldName+'-value',
										readOnly: fldReadOnly,
										hidden: fldHidden,
										value: fieldValue,
										triggers: {
											search: {
												cls: 'x-form-search-trigger',
												handler: function(fld){
													var thisName = fld.getName();
													var fldName = thisName.replace("-value","");
													var promptTable =formData[fldName].promptTable;
													var promptTableFld =formData[fldName].promptTableFld;
													var promptTableFldDesc =formData[fldName].promptTableFldDesc;
													var promptTableFldLabel = formData[fldName].promptTableFldLabel;
													var promptTableDescFldLabel = formData[fldName].promptTableDescFldLabel;
													var promptTableDefaultFld = formData[fldName].promptTableDefaultFld;
													me.pmtSearchScheduModel(fld,promptTable,promptTableFld,promptTableFldDesc,promptTableFldLabel,promptTableDescFldLabel,promptTableDefaultFld);
												}
											}
										},
								listeners: {
	                 specialkey: function (textfield, e) {
	                    if (e.getKey() == Ext.EventObject.ENTER) {
	                       me.searchCfg();
	                    }
	                 }
	              }
									}
									/*,{
										xtype:'button',
										//text:'放大镜',
										minWidth:35,
										maxWidth:35,
										iconCls: "query",
										text: fieldName,
										disabled: fldReadOnly,
										style:{
											marginLeft:'5px',
											marginRight:'10px'
										},
										handler: function(btn){
											var fldName = btn.getText();
											var promptTable =formData[fldName].promptTable;
											var promptTableFld =formData[fldName].promptTableFld;
											var promptTableFldDesc =formData[fldName].promptTableFldDesc;
											var promptTableFldLabel = formData[fldName].promptTableFldLabel;
											var promptTableDescFldLabel = formData[fldName].promptTableDescFldLabel;
											var promptTableDefaultFld = formData[fldName].promptTableDefaultFld;
											me.pmtSearchScheduModel(btn,promptTable,promptTableFld,promptTableFldDesc,promptTableFldLabel,promptTableDescFldLabel,promptTableDefaultFld);
										}
									}*/
									,{
										xtype: 'displayfield',
										hideLabel: true,
										maxWidth:180,
										name: fieldName+'-desc',
									}
								]	
				     };
					   break;
					 case '06'://下拉框
					   typeField = {
			            xtype: 'combobox',
			            hideEmptyLabel: true,
			            width: 357,
			            autoSelect: false,
			            store: new KitchenSink.view.common.store.comboxStore({
										recname: promptTable,
										condition: transCondition,
										result: promptTableFld+","+promptTableFldDesc
									}),
			            valueField: promptTableFld,
			            displayField: promptTableFldDesc,
			            typeAhead: true,
			            queryMode: 'remote',
									name: fieldName+'-value',
									value: fieldValue,
									readOnly: fldReadOnly,
									hidden: fldHidden,
									listeners:{
			               	beforequery: function (query) {
			               		//var downCondition = null;
												 var downConditionStr = "";
												 //downConditionStr = '{ "TZ_JG_ID":{ "value" : "EMBA", "operator":"01", "type":"01"}}';
												 //downCondition = Ext.JSON.decode(downConditionStr);
												 //delete query.combo.lastQuery;
												 var combo = query.combo;
												 var store = combo.store;
												 try{
												 		var TZ_ZHZJH_ID = store.condition.TZ_ZHZJH_ID;
												 }catch(e){
				                   var comboName = combo.getName( );
				                   var form = combo.findParentByType("form").getForm();;
				                   
				                   var fldName = comboName.replace("-value", "");
				                   var downTableDefaultFld = formData[fldName].promptTableDefaultFld;
				                   
				                   Ext.Array.forEach( downTableDefaultFld, function(defFld){
															var fldVal = form.findField(defFld["TZ_FILTER_GL_FLD"]+"-value").getValue();
															if(downConditionStr ==""){
																 downConditionStr ='"'+defFld["TZ_FILTER_GL_FLD"]+'":{"value": "'+fldVal+'", "operator":"01","type": "01"	}';
															}else{
																 downConditionStr =downConditionStr+',"'+defFld["TZ_FILTER_GL_FLD"]+'":{"value": "'+fldVal+'", "operator":"01","type": "01"	}';
															}
													 });
	
														if(downConditionStr ==""){
																downConditionStr="{}";
														}else{
															  delete combo.lastQuery;
															  downConditionStr = "{"+downConditionStr+"}";
														}
				
					                   var tzStoreParams = '{"OperateType":"COMBOX","recname":"'+store.recname+'","condition":'+downConditionStr+',"result":"'+store.result+'"}';
					                   store.tzStoreParams = tzStoreParams;
					                 
					                  // store.load();
					                }
			               	},
			               	specialkey: function (textfield, e) {
		                    if (e.getKey() == Ext.EventObject.ENTER) {
		                       me.searchCfg();
		                    }
	                 		}
			            }
			       };
					   break;
					 default:
					   typeField = {
				        	xtype: 'textfield',
				        	columnWidth: 1,
				        	hideEmptyLabel: true,
				        	value: fieldValue, 
				        	readOnly: fldReadOnly,
				        	hidden: fldHidden, 
									name: fieldName+'-value'
				     };
					
				}

	      var fieldItem = {
	       layout:'column',
	       bodyPadding: 5,
			   items:[{
			      xtype: 'combobox',
			      labelWidth: 120,
			      width: 250,
			      labelSeparator:'',
			      readOnly: operatorReadOnly,
			      hidden: fldHidden, 
			      hideLabel: fldHidden, 
			      editable:false,
			      fieldLabel:formData[fieldName].fldDesc,
			      store:{
               fields: [{name:'transId'},{name:'transDesc'}],
               data: formData[fieldName].operator
            },
            displayField: 'transDesc',
    				valueField: 'transId',
			   		value: formData[fieldName].operator[0].transId,
						name: fieldName+'-operator'
			   },typeField]
	      }

	      conItems.push(fieldItem);
	   
      }	
			
		});

			Ext.apply(this,{
					items: [{
				    xtype: 'form',
						layout: {
				       type: 'vbox',
				       align: 'stretch'
				    },
				    border: false,
				    bodyPadding: 10,
				    bodyStyle:'overflow-y:auto;overflow-x:hidden',
						fieldDefaults: {
				      msgTarget: 'side',
				      labelStyle: 'font-weight:bold'
				    }, 
				    dockedItems:[{
							xtype:"toolbar",
							items:["->",
								{
									xtype:'splitbutton',
									text:'更多操作',
									iconCls:  'list',
									glyph: 61,
									menu:[{
										text:'基本搜索',
										listeners:{
			               	click: function (bt, e, eOpts) {
			                  me.changeSearchModel(bt,true );
			               	}
			            	}
									},{
										text:'高级搜索',
										listeners:{
			               	click: function (bt, e, eOpts) {
			                  me.changeSearchModel(bt,false);
			               	}
			            	}
									}]
								}
							]
						}], 
				    items: conItems,
				    buttonAlign: 'left'
					}]
					,
				  buttons: [
				  /*{
						text: '搜索',
						iconCls:"ensure",
						handler: function(btn){
							//获取窗口
							var win = btn.findParentByType("window");
							//选中行
						  var selection = win.child("grid").getSelectionModel().getSelection();
						  //选中行长度
						  var checkLen = selection.length;
						  if(checkLen == 0){
								Ext.Msg.alert("提示","未选中记录");   
								return;
						  }
							//回调函数
							win.callback(selection);
							//修改密码信息表单
							var form = win.child("form").getForm();
							//重置表单
							form.reset();
							//关闭窗口
							win.close();
						}
					},
					*/
					{
							text: '搜索',
							iconCls:"search",
							handler: function(btn){
								me.searchCfg();
							}
					},{
							text: '清除',
							iconCls:"clean",
							handler: function(btn){
								//搜索信息表单
								var form = btn.findParentByType("window").child("form").getForm();
								//重置表单
								form.reset();
							}
					},
					{
						text: '关闭',
						iconCls:"close",
						handler: function(btn){
								//获取窗口
								var win = btn.findParentByType("window");
								//修改密码信息表单
								var form = win.child("form").getForm();
								//关闭窗口
								win.close();
						}
					}]
					
			});
		this.callParent();
	},
	searchCfg: function(){
	
		//搜索信息表单
		var form = this.child("form").getForm();
		if (form.isValid()) {
			//表单数据
			var formParams = form.getValues();
		
			var cfgSrhId = this.cfgSrhId;
						
			var tzSearchParams = '{"cfgSrhId":"'+cfgSrhId+'","condition":'+Ext.encode(formParams)+'}';
			
	
			//回调函数
			this.callback(tzSearchParams);
			//关闭窗口
			this.close();
		}

		//搜索条件
		//var condition = this.condition;
		//var srhconditions = condition.srhConFields;
		//for(var fieldName in srhconditions) {
		//	var fieldValue = formParams[fieldName+"-value"];
		//	srhconditions[fieldName].value = fieldValue;
		//}
		//搜索表或试图
		//var recname = this.recname;
		//交互参数
      //  store.tzStoreParams = '{"OperateType":"'+store.tzType+'","maxRow":"'+store.maxRow+'","recname":"'+recname+'","condition":'+Ext.encode(condition)+',"result":"'+store.srhresults+'"}';
	
	},
	changeSearchModel: function(bt,model){
		
	  var form = bt.findParentByType("form");
	  var baseForm = form.getForm();
	  baseForm.reset();
	  var comboboxArr = form.query( "combobox" );
		
		
	  Ext.Array.forEach( comboboxArr, function(cmbox){
	  	 var oprName = cmbox.getName();
			 var isOpreate =  oprName.indexOf("-operator");
			 if(isOpreate > 0 ){
		  	 var fldName = oprName.replace("-operator", "-value");
		  	 var fld = baseForm.findField(fldName);  
		  	 var fldReadOnly = fld.getEl().dom.getElementsByTagName("input")[0].getAttribute("readonly");
		  	 if(fldReadOnly=='readonly' ||fldReadOnly=='true'){
		  	 	
		  	 }else{
		  	 	  cmbox.setReadOnly(model);
		  	 }
	  	 }
	  });
	},
	pmtSearchScheduModel: function(btn,promptTable,promptTableFld,promptTableFldDesc,promptTableFldLabel, promptTableDescFldLabel,promptTableDefaultFld){
		var srhConFieldsStr = '{"'+promptTableFld+'":{ "desc":"'+promptTableFldLabel+'","operator":"07","type":"01"},"'+promptTableFldDesc+'":{"desc":"'+promptTableDescFldLabel+'","operator":"07","type":"01"}}';
		var srhConFields = Ext.JSON.decode(srhConFieldsStr);
		var presetFieldsStr = "";
		var form = btn.findParentByType("form").getForm();
		
		Ext.Array.forEach( promptTableDefaultFld, function(defFld){

			var fldVal = form.findField(defFld["TZ_FILTER_GL_FLD"]+"-value").getValue();
			if(presetFieldsStr ==""){
				 presetFieldsStr ='{"'+defFld["TZ_FILTER_GL_FLD"]+'":{"value": "'+fldVal+'","type": "01"	}}';
			}else{
				presetFieldsStr =presetFieldsStr+","+'{"'+defFld["TZ_FILTER_GL_FLD"]+'":{"value": "'+fldVal+'","type": "01"	}}';
			}
		});

	// presetFieldsStr ='{"TZ_JG_ID":{"value": "EMBA","type": "01"	}}';
		if(presetFieldsStr ==""){
			presetFieldsStr="{}";
		}
		
	  var presetFields = Ext.JSON.decode(presetFieldsStr);
	  var srhresultStr = '{"'+promptTableFld+'": "'+promptTableFldLabel+'","'+promptTableFldDesc+'": "'+promptTableDescFldLabel+'"	}';
		var srhresult = Ext.JSON.decode(srhresultStr);
		
		Ext.tzShowPromptSearch({
			recname: promptTable,
			searchDesc: '查询',
			maxRow:20,
			condition:{
				presetFields:presetFields,
				srhConFields:srhConFields
			/*
				presetFields:{
					TZ_JG_ID:{
						value: Ext.tzOrgID,
						type: '01'	
					}	
				},
				srhConFields:{
					TZ_ART_ID:{
						desc:'活动ID',
						operator:'07',
						type:'01'	
					},
					TZ_NACT_NAME:{
						desc:'活动名称',
						operator:'07',
						type:'01'		
					}	
				}	
				*/
			},
			srhresult: srhresult,
			/*
			srhresult:{
				TZ_ART_ID: '活动ID',
				TZ_NACT_NAME: '活动名称'	
			},
			*/
			multiselect: false,
			callback: function(selection){
				form.findField(promptTableFld+"-value").setValue(selection[0].data[promptTableFld]);
				form.findField(promptTableFld+"-desc").setValue(selection[0].data[promptTableFldDesc]);
			}
		});	
	},
	constructor: function (config) {
		//可配置搜索查询;
		this.cfgSrhId = config.cfgSrhId;

		//搜索条件
    this.condition = config.condition;
    

		//回调函数
		this.callback = config.callback;

		this.callParent();
	}
});
