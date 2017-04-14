//var cellEditing = Ext.create('Ext.grid.plugin.CellEditing');
    
Ext.define('KitchenSink.view.activity.activityInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'activityInfo', 
	controller: 'activityInfo',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.ux.DataView.DragSelector',
        'Ext.ux.DataView.LabelEditor' ,
        'Ext.ux.Ueditor',
	    'KitchenSink.view.activity.activityInfoController',
        'KitchenSink.view.activity.attachmentStore',
        'KitchenSink.view.activity.viewArtModel',
        'KitchenSink.view.activity.viewArtStore',
        'KitchenSink.view.activity.applyItemStore',
        'KitchenSink.view.activity.activityImageStore',
        'KitchenSink.view.activity.applyItemOptionsStore',
        'KitchenSink.view.activity.picStore',
        'KitchenSink.view.activity.SiteModel',
        'KitchenSink.view.activity.SiteStore',
        'KitchenSink.view.activity.ColuModel',
        'KitchenSink.view.activity.ColuStore'
	],
  title: '活动信息', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
	initComponent:function(){

	   var siteStore=new KitchenSink.view.activity.SiteStore();//从数据库加载到local
	   siteStore.load({
	       callback: function(records, options, success) {

	       }
	   });
	   
	   var coluStore = new KitchenSink.view.activity.ColuStore(); 
	   
	   var tagStore1 = new Ext.data.Store({
           fields:['tagName','tagId'],
           data:[]
       });
	   
	   
	 Ext.apply(this,{
    items: [{
        xtype: 'form',
        reference: 'activityForm',
				layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
				//heigth: 600,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 120,
            labelStyle: 'font-weight:bold'
        },
        items: [{
           xtype: 'textfield',
						name: 'activityId',
						hidden: true
        }/*,{
           xtype: 'textfield',
						name: 'siteId',
						hidden: true
        },{
           xtype: 'textfield',
						name: 'coluId',
						hidden: true
        },{
           xtype: 'textfield',
						name: 'saveImageAccessUrl',
						hidden: true
        },{
           xtype: 'textfield',
						name: 'saveAttachAccessUrl',
						hidden: true
        }*/,{
            xtype: 'textfield',
            fieldLabel: '活动名称',
						name: 'activityName',
						maxLength : 254,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        },{
           xtype: 'datefield',
            fieldLabel: '活动开始日期',
            format: 'Y-m-d',
						name: 'activityStartDate',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        },{
           xtype: 'timefield',
            fieldLabel: '活动开始时间',
            format: 'H:i',
						name: 'activityStartTime',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false,
            value: "08:30"
        },{
           xtype: 'datefield',
            fieldLabel: '活动结束日期',
            format: 'Y-m-d',
						name: 'activityEndDate',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        },{
           xtype: 'timefield',
            fieldLabel: '活动结束时间',
            format: 'H:i',
						name: 'activityEndTime',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false,
            value: "17:30"
        },{
           xtype: 'textfield',
            fieldLabel: '活动地点',
						name: 'activityPlace',
						maxLength: 256,
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        },{
            xtype: 'textfield',
            fieldLabel: '活动城市',
						name: 'activityCity',
						maxLength: 30,
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        },{
        	 xtype: 'combobox',
        	 fieldLabel: '活动类型',
        	 valueField: 'TValue',
           displayField: 'TLDesc',
           store: new KitchenSink.view.common.store.appTransStore("TZ_ART_TYPE"),
           queryMode: 'local',
    			 name: 'artType',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            value: 'A',
            allowBlank: false,
            listeners: {
							"change": function( cbox , newValue, oldValue){
								//手机号码
								var externalLink = cbox.findParentByType("form").getForm().findField("externalLink");
								if(newValue=="B"){
									externalLink.allowBlank = false;
								}else{
									externalLink.allowBlank = true;
								}
							}
						}
        },{
            xtype: 'textfield',
            fieldLabel: '外部引用链接',
						name: 'externalLink'
        },{
        	xtype: 'textfield',
        	fieldLabel: '栏目隐藏',
        	name: 'columsHide',
        	hidden: true
        }, {
            xtype: 'tagfield',
            fieldLabel:'站点',
            name: 'siteids',
            store: siteStore,
            valueField: 'siteId',
            displayField: 'siteName',
            filterPickList:true,
            createNewOnEnter: true,
            createNewOnBlur: true,
            queryMode: 'local',
            listeners:{
                'select': function(combo,record,index,eOpts)//匹配下拉值之后置空输入文字
                {
                    var me = this;
                    me.inputEl.dom.value = "";
                },
                "change": function( cbox , newValue, oldValue){
                	/*
                	console.log(cbox);
                	console.log(newValue);
                	console.log(oldValue);
                	console.log("begin");
                	for(var i=0;i<newValue.length;i++){
                		console.log(newValue[i]);
                	}
                	console.log("end");
                	*/
                	
                	var tzStoreParamsJson = {
                			"gridTyp":"COLU",
                			"sites": newValue
                	};
                	
                	coluStore.tzStoreParams = Ext.JSON.encode(tzStoreParamsJson);
                	coluStore.load({
             	       callback: function(records, options, success) {
             	    	 
             	    	 //查看现有站点有没有对应的选择的栏目值; 
             	    	 var form = cbox.findParentByType("form");
             	    	 var columsHide = form.getForm().findField("columsHide").getValue();
             	    	 if(columsHide != ""){
             	    		var columsHideList = columsHide.split(",");	
             	    		form.getForm().findField("columsHide").setValue("");
             	    		form.getForm().findField("colus").setValue(columsHideList);
             	    		var panel = cbox.findParentByType("activityInfo");
             	    		panel.commitChanges(panel);
             	    	 }else{
             	    		var colu = form.down("tagfield[name=colus]");
                	    	var coluValues = colu.getValue();
   	           	    	  	for (var i=0;i<coluValues.length;i++)
   		             	    {
   	           	    	  		var cValue = coluValues[i];
   	           	    	  		var bl = false;
   	           	    	  		for (var j=0;j<records.length;j++)
   	 	             	      	{	
   	           	    	  			var remValue = records[j].data.coluId;
   	           	    	  			if(cValue == remValue ){
   	           	    	  				bl = true;
   	           	    	  				break;
   	           	    	  			}
   	 	             	      	}
   	           	    	  		if(bl == false){
   	           	    	  			colu.removeValue ( cValue );
   	           	    	  		}
   		             	    }
             	    	 }
             	       }
             	   });
                }
            },
            afterLabelTextTpl: [
                                 '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                 ],
            allowBlank: false
        }, {
            xtype: 'tagfield',
            fieldLabel:'栏目',
            name: 'colus',
            store: coluStore,
            valueField: 'coluId',
            displayField: 'coluName',
            filterPickList:true,
            createNewOnEnter: true,
            createNewOnBlur: true,
            queryMode: 'local',
            listeners:{
                'select': function(combo,record,index,eOpts)//匹配下拉值之后置空输入文字
                {
                    var me = this;
                    me.inputEl.dom.value = "";
                }
            },
            afterLabelTextTpl: [
                                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                ],
           allowBlank: false
        },  
        {
        	xtype:'radiogroup',
        	fieldLabel:'发布对象',
        	items: [{
        		name: 'limit',
        		inputValue: 'A',
        		boxLabel: '无限制',
        		
        		checked: true,
        		listeners:{
        			"change":function(el,checked){
        				if(checked){
        					el.findParentByType("form").getForm().findField('AudList').hide();
        				}
        			}
        		}
        		},{
        		name: 'limit',
        		inputValue: 'B',
        		boxLabel: '听众',
                
        			listeners:{
            			"change":function(el,checked){
            				if(checked){
            					el.findParentByType("form").getForm().findField('AudList').show();
            				}
            			}
            		}
        		}]
        },        
        
        {   xtype:'tagfield',
            fieldLabel:'听众列表',
            name:'AudList',
            anyMatch:true,
            filterPickList: true,
            createNewOnEnter: true,
            createNewOnBlur: false,
            enableKeyEvents: true,
            ignoreChangesFlag:true,
            store: tagStore1,
            valueField: 'tagId',
            displayField: 'tagName',
            triggers: {
                search: {
                    cls: 'x-form-search-trigger',
                    handler: "searchListeners"
                }
            }
        },{
			xtype: 'fieldset',
			name: 'fbSet',
			layout: {
	            type: 'vbox',
	            align: 'stretch'
	        },
			title: '发布对象',
			items: [{
				items:[{
			       xtype: 'radio',
			       boxLabel: '无限制',
			       name: 'limit-abandon',
			       inputValue : 'A'
			    },{
			    	xtype: 'radio',
			    	boxLabel: '项目',
					name: 'limit-abandon',
					inputValue : 'B'
				}, {
                    xtype: 'tagfield',
                    //fieldLabel:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.tag","标签"),
                    name: 'projects',
                    store:new KitchenSink.view.common.store.comboxStore({
                        recname:'PS_TZ_PRJ_INF_T',
                        condition:{
                        	TZ_JG_ID:{
                                value:Ext.tzOrgID,
                                operator:'01',
                                type:'01'
                            },
                            TZ_IS_OPEN:{
                                value:"Y",
                                operator:'01',
                                type:'01'
                            }
                        },
                        result:'TZ_PRJ_ID,TZ_PRJ_NAME'
                    }),
                    valueField: 'TZ_PRJ_ID',
                    displayField: 'TZ_PRJ_NAME',
                    filterPickList:true,
                    createNewOnEnter: false,
                    createNewOnBlur: false,
                    queryMode: 'local',
                    listeners:{
                        'select': function(combo,record,index,eOpts)//匹配下拉值之后置空输入文字
                        {
                            var me = this;
                            me.inputEl.dom.value = "";
                        }
                    }
                }]
			}]
        },{
            //xtype: 'htmleditor',
            xtype: 'ueditor',
            fieldLabel: '内容',
            height: 300,
            zIndex: 900,
						name: 'contentInfo'
        },{
            xtype: 'hiddenfield',
						name: 'titleImageUrl'
						//id: 'titleImageUrl'
        },{
        	  xtype: 'tabpanel',
        	  frame: true,
        	  items:[{
        	  	title: "标题图",
        	  	layout: {
            		type: 'column'
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
										name: 'orguploadfile',
										buttonOnly:true,
										listeners:{
											change:function(file, value, eOpts){
												addAttach(file, value, "IMG");
											}
										}	
        					},{
        						columnWidth:.35,
            				xtype: 'button',
            				text: '删除',
										listeners:{
											click:function(bt, value, eOpts){
												deleteImage(bt, value, eOpts);
											}
										}
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
									name: 'titleImageTitle'
        				},{
            			xtype: 'textarea',
            			fieldLabel: '说明',
									name: 'titleImageDesc'
        				}]
							}]
        	  },{
				    		xtype : 'panel',
				    		title:'图片墙',
				    		autoHeight:true,
				    		tbar: [{
				    			xtype: 'form',
				    			items:[{
						            xtype: 'fileuploadfield',
						            buttonText: '上传图片',
									name: 'orguploadfile',
									buttonOnly:true,
									listeners:{
										change:function(file, value, eOpts){
											addAttach (file, value, "TPJ");
										}
									}
				    			}]
				    		}],
				    		items :[{
				    		 //id: 'picView',
				    			name: 'picView',
				        	xtype:'dataview',
				        	store: {
										type: 'picStore'
									},
						    	tpl:[
						    		'<tpl for=".">',
						    		'<div class="thumb-wrap" id="{index}">',
							        	'<div style="width:160px;height:113px;background:url('+ TzUniversityContextPath +'{sltUrl});background-size:100%">',
								        	'<img src="'+ TzUniversityContextPath +'/statics/images/tranzvision/bj.png" class="picEider" onMouseOver="getMouser(this)" onMouseOut="outMouser(this)" onclick="clickMouser_E({index})"/>',
								        	'<img src="'+ TzUniversityContextPath +'/statics/images/tranzvision/jh.png" class="picEider" onMouseOver="getMouser(this)" onMouseOut="outMouser(this)" onclick="clickMouser_D({index})"/>',
							        	'</div>',
							        	'<tpl if="caption.length &gt; 20"><marquee scrollamount=3 width: 100%">{caption}</marquee></tpl>',
							        	'<tpl if="caption.length <= 20"><span>{caption}</span></tpl>',
						        	'</div>',
						    		'</tpl>',
						    		'<div class="x-clear"></div>',
								],
						    	itemSelector: 'div.thumb-wrap',
						    	emptyText: 'No images available',
						    	style:{
						    		background:'#eee',
						    		border:'1px solid #000000',
						    		padding:'0 15px 25px 0'
						    	},
						    	listeners:{
						    		render:function(v) {
						    			onDataViewRender(v);
						    			setTimeout(function(){
						    				
						    			},200);
							    	},
							    	refresh:function(v){
							    		//if(v.store.getCount() > 0){
							    			//v.getNode(v.store.getCount()-1).innerHTML="";
						    				//v.getNode(v.store.getCount()-1).style.padding="6px";
						    				//v.getNode(v.store.getCount()-1).style.background="#eee";
						    				//v.getNode(v.store.getCount()-1).style.cursor="auto";
						    				//var src = v.store.getAt(v.store.getCount() - 1).get("src");
						    				//Ext.create('Ext.panel.Panel', {
						    				//	width:160,
												//height:132,
											  //  renderTo: v.getNode(v.store.getCount()-1),
											  //  items:[{
									    	//		xtype: 'form',
									    	//		layout: "hbox",
									    	//		items:[{
											  //          xtype: 'fileuploadfield',
											  //          margin:'-5 0 0 -11',
											  //          buttonText: '<div style="width:160px;height:135px;background:url(' + src + ');background-size:100%"></div>',
												//		name: 'picUpload',
												//		buttonOnly:true,
												//		listeners:{
												//			change:function(file, value, eOpts){
												//				picWallAdd(file, value, eOpts);
												//			}
												//		}
									    	//		}]
									    	//	}]
											//});
							    		//}
							    	}
							    	,itemmouseenter:function(view, record, item, index, e, eOpts){
							    	//	if(index != view.store.getCount()-1){
								    		item.getElementsByTagName("img")[0].style.display = "block";
								    		item.getElementsByTagName("img")[1].style.display = "block";
							    	//	}
							    	},
							    	itemmouseleave:function(view, record, item, index, e, eOpts){
							    	//	if(index != view.store.getCount()-1){
								    		item.getElementsByTagName("img")[0].style.display = "none";
								    		item.getElementsByTagName("img")[1].style.display = "none";
							    	//	}
							    	}
						    	}
							}]
				    },
        	  {
	        	  	title: "附件集",
								items: [{
									xtype: 'grid',
									height: 315, 
									frame: true,
									columnLines: true,
									//id: 'attachmentGrid',
									name: 'attachmentGrid',
									reference: 'attachmentGrid',
									style:"margin:10px",
									store: {
										type: 'attachmentStore'
									},
									selModel: {
										type: 'checkboxmodel'
   				 					},
									//dockedItems: [{
	                //	xtype: 'toolbar',
	                //	items:[
	                	//'->',
										//{
										//	xtype:'splitbutton',
										//	text:'更多操作',
										//	iconCls:  'list',
								  	//	glyph: 61,
										//	menu:[{
										//		text:'上传附件'
										//	}	
										//]
									//}]
									tbar: [{
    								xtype: 'form',
    								bodyStyle:'padding:10px 0px 0px 0px',
    								items:[{
		            			xtype: 'fileuploadfield',
		            			buttonText: '上传附件',
											name: 'orguploadfile',  
											buttonOnly:true,
											width: 88,
											listeners:{
												change:function(file, value, eOpts){
													addAttach(file, value, "ATTACHMENT");
												}
											}
    								}]
    							},"-", {iconCls: 'remove',text: '删除',tooltip:"删除选中的数据",handler: 'deleteArtAttenments'}],
									columns: [{ 
										text: '附件ID',
										dataIndex: 'attachmentID',
										sortable: false,
										hidden: true
									},{ 
										text: '附件url',
										dataIndex: 'attachmentUrl',
										sortable: false,
										hidden: true
									},{
										text: '附件名称',
										dataIndex: 'attachmentName',
										sortable: false,
										minWidth: 100,
										flex: 1,
										renderer: function(v){
											  var arr = v.split("|");
												return '<a href="'+TzUniversityContextPath+arr[1]+'" target="_blank">'+arr[0]+'</a>';
										}
									},{
										menuDisabled: true,
										sortable: false,
					   			 		width:60,
					   			 		xtype: 'actioncolumn',
					  			 	items:[
							  			{iconCls: 'remove',tooltip: '删除', handler: 'deleteArtAttenment'}
					   				]
									}]
									//,bbar: {
									//	xtype: 'pagingtoolbar',
									//	pageSize: 5,
									//	reference: 'attachmentToolBar',
									//	store: {
									//		type: 'attachmentStore'
									//	},
									//	displayInfo: true,
									//	displayMsg: '显示{0}-{1}条，共{2}条',
									//	beforePageText: '第',
									//	afterPageText: '页/共{0}页',
									//	emptyMsg: '没有数据显示',
									//	plugins: new Ext.ux.ProgressBarPager()
									//}
								}]	
        	  },
        	  {
        	  	title: "在线报名",
        	  	bodyPadding: 10,
			        layout: {
			        	  type: 'vbox',
			        	  align: 'stretch'
			        },
			        bodyStyle:'overflow-y:auto;overflow-x:hidden',
        			fieldDefaults: {
            		msgTarget: 'side',
            		labelWidth: 100,
            		labelStyle: 'font-weight:bold'
        			}, 
        	  	items: [
        	  	/*{
			            xtype: 'checkbox',
			            fieldLabel: '启用在线报名',
									name: 'enabledApply'
			        }*/{
						xtype: 'combobox',
						fieldLabel: '启用在线报名',
						valueField: 'TValue',
						displayField: 'TLDesc',
						store: new KitchenSink.view.common.store.appTransStore("TZ_QY_ZXBM"),
						queryMode: 'local',
						name: 'enabledApply'
					},{
           				xtype: 'datefield',
			            fieldLabel: '报名开始日期',
			            format: 'Y-m-d',
									name: 'applyStartDate'
        			},{
			           	xtype: 'timefield',
			            fieldLabel: '报名开始时间',
			            format: 'H:i',
									name: 'applyStartTime',
            			value: "08:30"
		        },{
		           	xtype: 'datefield',
		            fieldLabel: '报名结束日期',
		            format: 'Y-m-d',
								name: 'applyEndDate'
		        },{
		           	xtype: 'timefield',
		            fieldLabel: '报名结束时间',
		            format: 'H:i',
								name: 'applyEndTime',
            		value: "17:30"
		        },{
										xtype: 'numberfield',
										fieldLabel: '席位数',
										minValue: 0,
										name: 'applyNum',
										/*
										validator: function(val){
											var enabledApply = this.findParentByType('form').getForm().findField('enabledApply').getValue();
											if(enabledApply == "Y" && val <=0) return '请填写席位数且席位数不能为0';
											return true;	
										}*/
							},{
										xtype: 'fieldset',
										title: '席位信息显示模式',
										items: [{
											xtype: 'fieldcontainer',
										  defaultType: 'radio',
											items:[{
												boxLabel  : '不显示',
                    		name      : 'showModel',
                    		inputValue : '1',
                    		checked: true
											},{
												boxLabel  : '显示总席位数，已报名人数',
                    		name      : 'showModel',
                    		inputValue : '2'
											},{
												boxLabel  : '显示总席位数，已报名人数，且可以查看已报名人的姓名',
                    		name      : 'showModel',
                    		inputValue : '3'
											}]
										}]
							}
							//,{
							//	xtype:"toolbar",
							//	items:[
							//		{text:"新增",tooltip:"新增信息项",iconCls:"add"},"-",
							//		{text:"删除",tooltip:"删除选中的数据",iconCls:"remove"}
							//]}
							,{
								xtype: 'grid',
								height: 360, 
								frame: true,
								//id: 'applyItemGrid',
								name: 'applyItemGrid',
								dockedItems: [{
									xtype: 'toolbar',
									items: [{iconCls: 'add',text: '新增', tooltip:"新增信息项",handler: 'addApplyItem'
                    //handler: function() {
                		//	cellEditing.cancelEdit();

                			// Create a model instance
                		//	var r = Ext.create('KitchenSink.view.activity.applyItemModel', {
                    //			applyItemID: 'TZ_XXX_007',
									  //      applyItemNum: '',
									  //      applyItemName: '',
									  //      applyItemRequired: false,
									  //      applyItemType: '1'
                		//	});

                		//	Ext.getCmp('applyItemGrid').getStore().insert(0, r);
                		//	cellEditing.startEditByPosition({
        						//			row: 0,
       							//			column: 1
    								//	});
            				//}
                	},"-", 
                	{iconCls: 'remove',text: '删除',tooltip:"删除选中的数据",handler: 'deleteApplyItems'}
                	]
            		}],
								columnLines: true,
								selModel: {
       	 					type: 'checkboxmodel'
   				 			},
								reference: 'applyItemGrid',
								//style:"margin:10px",
								store: {
									type: 'applyItemStore'
								},
								//plugins: [cellEditing],
								plugins: {
        						ptype: 'cellediting',
        						pluginId: 'applyItemCellediting',
        						listeners: {
        							beforeedit: function( editor, context, eOpts ){
        								
        								 var appplyItenId = context.record.data.applyItemId;
        								 if(appplyItenId =="TZ_CYR_NAME" || appplyItenId =="TZ_ZY_EMAIL" || appplyItenId =="TZ_ZY_SJ"){
        								 		
        								 	  //Ext.Msg.alert("提示","【姓名】、【邮箱】、【手机】为不可修改项");
        								 	  //editor.cancelEdit();
        									 return false;
        								 }
        							}
        						},
        						clicksToEdit: 1
    						},
    						viewConfig: {
        					plugins: {
            				ptype: 'gridviewdragdrop',
            				dragText: '拖拽进行信息项的排序'
        					},
									listeners: {
									//	beforedrop: function(node, data, overModel, dropPosition, dropHandlers) {
										    // Defer the handling
									//	    Ext.MessageBox.confirm('Drop', 'Are you sure:'+data, function(btn){
									//	        if (btn === 'yes') {
									//	            dropHandlers.processDrop();
									//	        } else {
									//	            dropHandlers.cancelDrop();
									//	        }
									//	    });
									//	},
										drop: function(node, data, dropRec, dropPosition) {
											data.view.store.beginUpdate();
											var items = data.view.store.data.items;
											for(var i = 0;i< items.length;i++){
												items[i].set('applyItemNum',i+1);
											}
											data.view.store.endUpdate();
										}
									}
    						},
								columns: [/*{ 
									text: '活动Id',
									sortable: false,
									dataIndex: 'activityId',
						      hidden: true
								},{ 
									text: '信息项ID',
									sortable: false,
									dataIndex: 'applyItemId',
						      hidden: true
								},{ 
									text: '排序',
									sortable: false,
									dataIndex: 'applyItemNum',
						      hidden: true
								},*/{
									text: '信息项',
									sortable: false,
									dataIndex: 'applyItemName',
									sortable: false,
									flex: 1,
									editor: {
									xtype: 'textfield',
									allowBlank: false
									}
								},{
									text: '信息项英文',
									sortable: false,
									dataIndex: 'applyItemNameEng',
									sortable: false,
									flex: 1,
									editor: {
									xtype: 'textfield',
									allowBlank: false
									}
								},/*{
									text: '是否必填',
									dataIndex: 'applyItemRequired',
									sortable: false,
									xtype: 'checkcolumn',
									flex: 1,
									//editor: {
									//	xtype: 'checkbox'
										//},
										listeners: {
											checkchange: function( col, rowIndex, checked, eOpts ){
												var store = col.findParentByType("grid").store;
															var applyItemId = store.getAt(rowIndex).data.applyItemId;
															if(applyItemId =="TZ_CYR_NAME" || applyItemId =="TZ_ZY_EMAIL" ){
																  store.getAt( rowIndex ).set( "applyItemRequired" , true);
															Ext.Msg.alert("提示","【姓名】、【邮箱】为必填项");
															return;
														}	
											}
										}
								},*/{
									text: '是否必填',
									dataIndex: 'applyItemRequired',
									sortable: false,
									flex: 1,
									editor: {
									xtype: 'combobox',
									store:{
										fields: [{name:'applyItemRequiredValue'},{name:'applyItemRequiredDesc'}],
										data: [{applyItemRequiredValue: 'Y', applyItemRequiredDesc: '是'},{applyItemRequiredValue: 'N', applyItemRequiredDesc: '否'}]
									},
									displayField: 'applyItemRequiredDesc',
									valueField: 'applyItemRequiredValue'
									},
									renderer: function(v){
										if(v=='Y'){
											return "是";
										}else{
											return "否";
										}	
									} 
								},{
									text: '控件类型',
									dataIndex: 'applyItemType',
									sortable: false,
									flex: 1,
									editor: {
									xtype: 'combobox',
									store:{
										fields: [{name:'applyItemTypeValue'},{name:'applyItemTypeDesc'}],
										data: [{applyItemTypeValue: '1', applyItemTypeDesc: '文本框'},{applyItemTypeValue: '2', applyItemTypeDesc: '下拉框'}]
									},
									displayField: 'applyItemTypeDesc',
												valueField: 'applyItemTypeValue'
									},
									renderer: function(v){
										if(v=='2'){
											return "下拉框";
										}else{
											return "文本框";
										}	
									} 
								},/*{
									text: '下拉值设置',
									sortable: false,
									align: 'center',
									width: 120,
									xtype: 'actioncolumn',
									items:[
										{iconCls:'edit',tooltip: '设置',handler: 'editorApplyItemOptions'}
									]
									//renderer: function(){
									//		return '<a href="#" onclick="editorApplyItemOptions()" >设置</a>';
									//}
								},*/{
									text: '操作',
									menuDisabled: true,
									sortable: false,
									width:60,
									xtype: 'actioncolumn',
									items:[
										{iconCls:' edit',tooltip: '设置',handler: 'editorApplyItemOptions',
											isDisabled:function(view ,rowIndex ,colIndex ,item,record ){
												var applyItemType = record.get('applyItemType');
												if(applyItemType == "2"){
													return false;
												}else{
													return true;
												}
											},
											getClass: function(v, metadata , r,rowIndex ,colIndex ,store ){
												var applyItemType = store.getAt(rowIndex).get("applyItemType");
												if(applyItemType == "2"){
													return 'edit';
												}else{
													return ' edit';
												}
											}
										},
										{iconCls: ' remove',tooltip: '删除',handler: 'deleteApplyItem',
											isDisabled:function(view ,rowIndex ,colIndex ,item,record ){
												var applyItemId = record.get('applyItemId');
												if(applyItemId == "TZ_CYR_NAME" || applyItemId == "TZ_ZY_SJ" ||applyItemId == "TZ_ZY_EMAIL"){
													return true;
												}else{
													return false;
												}
											}
										}
									]
								}]
								//bbar: {
								//	xtype: 'pagingtoolbar',
								//	pageSize: 5,
								//	reference: 'applyItemToolBar',
								//	store: {
								//		type: 'applyItemStore'
								//	},
								//	displayInfo: true,
								//	displayMsg: '显示{0}-{1}条，共{2}条',
								//	beforePageText: '第',
								//	afterPageText: '页/共{0}页',
								//	emptyMsg: '没有数据显示',
								//	plugins: new Ext.ux.ProgressBarPager()
								//}
							}
        	  	]
        	  }]
        },{
			xtype: 'grid',
			height: 180, 
			frame: true,
			columnLines: false,
			//id: 'attachmentGrid',
			name: 'viewArtGrid',
			reference: 'viewArtGrid',
			style:"margin-top:10px",
			store: {
				type: 'viewArtStore'
			},
			columns: [{ 
				text: '站点id',
				dataIndex: 'siteId',
				sortable: false,
				hidden: true
			},{ 
				text: '栏目id',
				dataIndex: 'coluId',
				sortable: false,
				hidden: true
			},{ 
				text: '活动id',
				dataIndex: 'artId',
				sortable: false,
				hidden: true
			},{ 
				text: '发布站点-栏目',
				dataIndex: 'coluName',
				width: 250,
				sortable: false,
				hidden: false
			},{ 
				text: '发布类型',
				dataIndex: 'artPubType',
				width: 70,
				sortable: false,
				hidden: false
			},{ 
				text: '发布状态',
				dataIndex: 'publicState',
				width: 90,
				sortable: false,
				hidden: false
			},{ 
				text: '预览url',
				dataIndex: 'previewUrl',
				sortable: false,
				hidden: true
			},{
				text: '活动发布访问链接',
				dataIndex: 'publicUrl',
				sortable: false,
				minWidth: 200,
				flex: 1,
				renderer: function(v){
						return '<a href="'+v+'" target="_blank">'+v+'</a>';
				}
			},{
				menuDisabled: true,
				sortable: false,
			 	width:60,
			 	xtype: 'actioncolumn',
			 	items:[
			 	       {iconCls: 'view',tooltip: '预览', handler: 'viewArtContent'}
				]
			}]
				
        },{
           xtype: 'textfield',
           fieldLabel: '状态',
					 name: 'publishStatus',
					 hidden: true
        },{
           xtype: 'textfield',
           fieldLabel: '是否点击发布或撤销发布按钮',
					 name: 'publishClick',
					 hidden: true
        },{
           xtype: 'textfield',
           fieldLabel: '是否点击置顶按钮',
					 name: 'upArtClick',
					 hidden: true
        }/*,{
           xtype: 'displayfield',
           fieldLabel: '状态',
					 name: 'publishStatusDesc'
        },{
           xtype: 'displayfield', 
           fieldLabel: '页面访问URL',
					 name: 'publishUrl'
        }*/]
    }]
	});
	 this.callParent();
	},
    buttons: [{
			text: '发布',
			iconCls:"publish",
			handler: 'publishArt'
		},{
			text: '撤销发布',
			iconCls:"revoke",
			handler: 'unpublishArt'
		},{
			text: '置顶',
			iconCls:"top",
			handler: 'upArt'
		},{
			text: '预览',
			iconCls:"preview",
			handler: 'viewArt'
		},{
			text: '手机版预览',
			iconCls:"preview",
			handler: 'previewPhoneArt',
			hidden: true
		},{
			text: '保存',
			iconCls:"save",
			handler: 'onActivitySave'
		}, {
			text: '确定',
			iconCls:"ensure",
			handler: 'onActivityEnsure'
		}, {
			text: '关闭',
			iconCls:"close",
			handler: 'onActivityClose'
		}]
});


function deleteImage( bt, e, eOpts){
	bt.findParentByType("tabpanel").down('image[name=titileImage]').setSrc("");
	bt.findParentByType("form").findParentByType("form").down('hiddenfield[name=titleImageUrl]').setValue("");
}


function addAttach(file, value, attachmentType){
	
	var form = file.findParentByType("form").getForm();
	
	if(value != ""){
		if(attachmentType=="IMG" || attachmentType=="TPJ"){ 
			var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
			if(fix.toLowerCase() != "jpg" && fix.toLowerCase() != "jpeg" && fix.toLowerCase() != "png" && fix.toLowerCase() != "gif" && fix.toLowerCase() != "bmp"){
				Ext.MessageBox.alert("提示","请上传jpg|jpeg|png|gif|bmp格式的图片。");
				form.reset();
				return;
			};	
		}
	
		//如果是附件则存在在附件的url中，如果是图片在存放在图片的url中;
		var dateStr = Ext.Date.format(new Date(), 'Ymd');
		   
		
		var upUrl = "";
		if(attachmentType=="ATTACHMENT"){ 
			//upUrl = file.findParentByType("activityInfo").child("form").getForm().findField("saveAttachAccessUrl").getValue();
			if(upUrl==""){
				//Ext.Msg.alert("错误","未定义上传附件的路径，请与管理员联系");
				//return;
				upUrl = TzUniversityContextPath + '/UpdServlet?filePath=activity';
			}else{
				/*
				if(upUrl.length == (upUrl.lastIndexOf("/")+1)){
				   upUrl = TzUniversityContextPath + '/UpdServlet?filePath='+upUrl+dateStr;
				}else{
				   upUrl = TzUniversityContextPath + '/UpdServlet?filePath='+upUrl+"/"+dateStr;
				}
				*/
				upUrl = TzUniversityContextPath + '/UpdServlet?filePath=activity';
			}
		}else{
			//upUrl = file.findParentByType("activityInfo").child("form").getForm().findField("saveImageAccessUrl").getValue();
			if(upUrl==""){
				//Ext.Msg.alert("错误","未定义上传图片的路径，请与管理员联系");
				//return;
				upUrl = TzUniversityContextPath + '/UpdServlet?filePath=activity';
			}else{
				/*
				if(upUrl.length == (upUrl.lastIndexOf("/")+1)){
				   upUrl = TzUniversityContextPath + '/UpdServlet?filePath='+upUrl+dateStr;
				}else{
				   upUrl = TzUniversityContextPath + '/UpdServlet?filePath='+upUrl+"/"+dateStr;
				}
				*/
				upUrl = TzUniversityContextPath + '/UpdServlet?filePath=activity';
			}
		}
		
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
				
				tzParams = '{"ComID":"TZ_HD_MANAGER_COM","PageID":"TZ_HD_INFO_STD","OperateType":"HTML","comParams":' + tzParams +'}';
				
				Ext.Ajax.request({
				    //url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_ATT_D.TZ_GD_ATT_FILE.FieldFormula.Iscript_AddArtAttach',
				    url: Ext.tzGetGeneralURL,
				    params: {
				        tzParams: tzParams
				    },
				    success: function(response){
				    	var responseText = eval( "(" + response.responseText + ")" );
				        if(responseText.success == 0){
				        	
				        	var accessPath = action.result.msg.accessPath;
				        	var sltPath = action.result.msg.accessPath;
				        	if(accessPath.length == (accessPath.lastIndexOf("/")+1)){
				        		accessPath = accessPath + action.result.msg.sysFileName;
				        		//sltPath = sltPath + "MINI_"+action.result.msg.sysFileName;
				        		sltPath = sltPath + responseText.minPicSysFileName;
				        	}else{
				        		accessPath = accessPath + "/" + action.result.msg.sysFileName;
				        		//sltPath = sltPath+ "/" + "MINI_"+action.result.msg.sysFileName;
				        		sltPath = sltPath+ "/" + responseText.minPicSysFileName;
				        	}
				        	
				        	
				        	//viewStore.reload();
				        	if(attachmentType=="IMG"){ 
				        		//Ext.getCmp( "titileImage").setSrc(TzUniversityContextPath + accessPath);
				        		//Ext.getCmp( "titleImageUrl").setValue(accessPath);
				        		file.findParentByType("tabpanel").down('image[name=titileImage]').setSrc(TzUniversityContextPath + accessPath);
				        		file.findParentByType("form").findParentByType("form").down('hiddenfield[name=titleImageUrl]').setValue(accessPath);
				        		//Ext.ComponentQuery.query('image[name=titileImage]')[0].setSrc(TzUniversityContextPath + accessPath);
										//Ext.ComponentQuery.query('hiddenfield[name=titleImageUrl]')[0].setValue(accessPath);
				        	
				  					
				  				}
				  				
				  				if(attachmentType=="ATTACHMENT"){ 
										//var applyItemGrid = this.lookupReference('attachmentGrid');
										var applyItemGrid = file.findParentByType("grid")
										var r = Ext.create('KitchenSink.view.activity.attachmentModel', {
											"attachmentID": action.result.msg.sysFileName,
											"attachmentName": "<a href='"+accessPath+"' target='_blank'>"+action.result.msg.filename+"</a>",
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




function onDataViewRender(v){
	var dragData = null;
	var draglast = null;
	var dragZone = new Ext.dd.DragZone(v.getEl(), {
        getDragData: function(e) {
        	var idx,record;
            var sourceEl = e.getTarget(v.itemSelector, 10);
            if (sourceEl) {
            	idx = v.indexOf(sourceEl);
            	//if(idx == v.store.getCount()-1){return null;}
            	record = v.getRecord(sourceEl);
	        	dragData = {
	        		index:idx,
	        		record:record
	        	};
                d = sourceEl.cloneNode(true);
                d.id = Ext.id();
                return {
                    ddel: d,
                    sourceEl: sourceEl,
                    repairXY: Ext.fly(sourceEl).getXY(),
                    sourceStore: v.store,
                    draggedRecord: v.getRecord(sourceEl)
                }
            }
        },
        onDrag:function(e){
        	var idx;
        	var sourceEl = e.getTarget(v.itemSelector,10);
            idx = v.indexOf(sourceEl);
           // if(dragData.index < v.store.getCount()-1){
	            if(idx > -1 && idx < v.store.getCount()){
	            	draglast = v.getRecord(sourceEl);
	            	v.getStore().remove(dragData.record);
		            v.getStore().insert(idx, [dragData.record]);
		            return true;
	            }
          //  }

            return false;
        },
        afterDragDrop:function(target,e,id){
        
        	refreshPicStore(v.getStore());
        	v.refresh();
        	/*
        	var sourceEl = e.getTarget(v.itemSelector,10);
          idx = v.indexOf(sourceEl);
        	if(idx > -1 && idx != v.store.getCount()-1){
        		if(dragData.index != idx){
	        		var file = v.store.getAt(idx).get("src");
	        		file = file.substring(file.lastIndexOf("/") + 1,file.length);
	        		var order1 = dragData.index + 1;
	        		var order2 = idx + 1;
	            var tzParams = '{"file":"' + file + '","order1":' + order1 + ',"order2":' + order2 + '}';
							Ext.Ajax.request({
					    	url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_ATT_D.TZ_GD_ATT_FILE.FieldFormula.Iscript_drag',
					    	params: {
					        tzParams: tzParams
					    	},
					    	success: function(response){
					    		var responseText = eval( "(" + response.responseText + ")" );
					        if(responseText.success == 0){
					        //	v.store.reload();
					        }else{
					        	Ext.MessageBox.alert("错误", responseText.message);
					        	return false;
					        }
					    	}
							});
        		}
        		
          }*/
            return true;
        },
        getRepairXY: function(e) {
        	return draglast.repairXY;
        }
    });
	dragZone.proxy.el.child(".x-dd-drag-ghost").addCls("transparent_class").removeCls("x-dd-drag-ghost");
	dragZone.proxy.el.child(".x-dd-drop-icon").setDisplayed(false);
	var dropZone = new Ext.dd.DropZone(v.getEl(), {

    });
}
function getMouser(doc){
	doc.style.border = "1px solid #eee";
}
function outMouser(doc){
	doc.style.border = "none";
}
function clickMouser_D(vor){
	
	Ext.MessageBox.confirm('确认', '您确定要删除该图片吗?', function(btnId){
		if(btnId == 'yes'){
			var picViewCom = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab( ).down('dataview[name=picView]');		
     
			picStore = picViewCom.getStore();
			var file = picStore.getAt(vor - 1).get("src");
			picStore.remove(picStore.getAt(vor - 1));
			
			refreshPicStore(picStore);
			picViewCom.refresh();
			
			//var file = Ext.getCmp("picView").store.getAt(vor - 1).get("src");
			/*
	    file = file.substring(file.lastIndexOf("/") + 1,file.length);
			var tzParams = '{"file":"' + file + '","order":' + vor + '}';
			Ext.Ajax.request({
			    url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_ATT_D.TZ_GD_ATT_FILE.FieldFormula.Iscript_deletePic',
			    params: {
			        tzParams: tzParams
			    },
			    success: function(response){
			    	var responseText = eval( "(" + response.responseText + ")" );
			        if(responseText.success == 0){
			        	//Ext.getCmp("picView").getStore().reload();
			        	//picViewCom.getStore().reload();
			        }else{
			        	Ext.MessageBox.alert("错误", responseText.message);        
			        }
			    }
			});
			*/
		}												  
	});
}
function clickMouser_E(vor){
	className = 'KitchenSink.view.activity.picEdit';
	Ext.syncRequire(className);
	ViewClass = Ext.ClassManager.get(className);
    //新建类
  win = new ViewClass();
	var picViewCom = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab( ).down('dataview[name=picView]');		
	//var picViewCom = Ext.getCmp("picView");
 	win.on('afterrender',function(panel){
		var form = panel.child('form').getForm();
		
		var formData = picViewCom.store.getAt(vor - 1);
		//var formData = Ext.getCmp("picView").store.getAt(vor - 1);
    	var file = formData.get("src");
    	var sysFileName = file.substring(file.lastIndexOf("/") + 1,file.length);
    	form.setValues({caption:formData.get("caption")});
    //	form.setValues({index:formData.get("index")});
    	form.setValues({picURL:formData.get("picURL")});
			form.setValues({sysFileName:sysFileName});
	});
		
   // Ext.getCmp("picView").findParentByType("activityInfo").add(win);
  picViewCom.findParentByType("activityInfo").add(win);
  win.show();
}


function refreshPicStore(picStore){
	var i = 0;
	for(i=0; i<picStore.getCount(); i++){
		picStore.getAt(i).data.index = i + 1;
	}
}

/*
function picWallAdd(file, value, eOpts){
	if(value != ""){
		var form = file.findParentByType("form").getForm();
		var picViewCom = Ext.ComponentQuery.query('dataview[name=picView]')[0];
		form.submit({
			url: TzUniversityContextPath + '/UpdServlet?filePath=/linkfile/FileUpLoad/imagesWall',
			waitMsg: '图片正在上传，请耐心等待....',
			success: function (form, action) {
				//var viewStore = Ext.getCmp("picView").store;
				var viewStore = picViewCom.store;
				var tzParams = '{"order":' + viewStore.getCount() + ',"data":' + Ext.JSON.encode(action.result.msg) + '}';
				Ext.Ajax.request({
				    url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_ATT_D.TZ_GD_ATT_FILE.FieldFormula.Iscript_attUp',
				    params: {
				        tzParams: tzParams
				    },
				    success: function(response){
				    	var responseText = eval( "(" + response.responseText + ")" );
				        if(responseText.success == 0){
				        	viewStore.reload();
				        }else{
				        	Ext.MessageBox.alert("错误", responseText.message);        
				        }
				    }
				});
				//重置表单
				form.reset();
			},
			failure: function (form, action) {
				Ext.MessageBox.alert("错误", action.result.msg);
			}
		});
	}
}
*/
