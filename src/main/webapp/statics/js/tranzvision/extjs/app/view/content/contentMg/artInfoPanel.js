Ext.define('KitchenSink.view.content.contentMg.artInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'artInfo', 
		controller: 'artInfo',
		requires: [
	    	'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.ux.DataView.DragSelector',
        'Ext.ux.DataView.LabelEditor' ,
	    	'KitchenSink.view.content.contentMg.artInfoController',
				'KitchenSink.view.content.contentMg.artAttachmentModel',
        'KitchenSink.view.content.contentMg.artAttachmentStore',
        'KitchenSink.view.content.contentMg.artPicStore'
	],
  title: '内容详情', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'artInfoForm',
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
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
        items: [{
           xtype: 'textfield',
						name: 'artId',
						hidden: true
        },{
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
        },{
            xtype: 'textfield',
            fieldLabel: '文章标题',
						name: 'artTitle',
						maxLength : 254,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        },{
        	 xtype: 'combobox',
        	 fieldLabel: '文章类型',
        	 valueField: 'TValue',
           displayField: 'TSDesc',
           store: new KitchenSink.view.common.store.appTransStore("TZ_ART_TYPE"),
    			 //store: {
    			 //	  fields: ['trans', 'transDesc'],
   				//		data : [
    			 //			{"trans":"A", "transDesc":"自建文章"},
        	//			{"trans":"B", "transDesc":"外部引用"}
        	//		]
    			// },
           queryMode: 'local',
           value: 'A',
    			// displayField: 'transDesc',
    			// valueField: 'trans',
    			 name: 'artType',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
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
            xtype: 'ueditor',
            fieldLabel: '内容',
            zIndex: 900,
						name: 'contentInfo'
        },{
			xtype: 'fieldset',
			layout: {
	            type: 'vbox',
	            align: 'stretch'
	        },
			title: '发布对象',
			items: [{
				items:[{
			       xtype: 'radio',
			       boxLabel: '无限制',
			       name: 'limit',
			       inputValue : 'A'
			    },{
			    	xtype: 'radio',
			    	boxLabel: '项目',
					name: 'limit',
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
           xtype: 'textfield',
            fieldLabel: '发布者',
            maxLength :100,
						name: 'artFbz'
        },{
            xtype: 'textfield',
            fieldLabel: '发布者部门',
            maxLength :200,
						name: 'artFbBm'
        }
        /*
         ,{
           xtype: 'datefield',
            fieldLabel: '开始日期',
            format: 'Y-m-d',
						name: 'startDate',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        },{
           xtype: 'timefield',
            fieldLabel: '开始时间',
            format: 'H:i',
						name: 'startTime',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        },{
           xtype: 'datefield',
            fieldLabel: '结束日期',
            format: 'Y-m-d',
						name: 'endDate',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        },{
           xtype: 'timefield',
            fieldLabel: '结束时间',
            format: 'H:i',
						name: 'endTime',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }
        */
        ,{
            xtype: 'textfield',
            fieldLabel: '发布时间',
						name: 'artNewsDT',
						validateOnChange: false,
						validateOnBlur: true,
						validator: function(v){
							 if(v == ""){
							   return true;	
							 }else{
							 	  var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2})$/;
 							    var r = v.match(reg);
 							    if(r == null){
							 			return "请正确的日期时间格式,如：2015-09-01 12:00";
							 		}else{
							 			return true;
							 		}
							 }
						}
        },{
							xtype:'component',
							hideLabel: true,
							style:'color:#ff0000',
							margin: '0 0 10 105',
							html:'发布时间由系统自动产生，可修改，时间格式示例：2015-01-01 10:10'
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
            			maxLength :100,
									name: 'titleImageTitle'
        				},{
            			xtype: 'textarea',
            			fieldLabel: '说明',
            			maxLength :254,
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
									//name: 'picUpload',
						            name: 'websitefile',
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
										type: 'artPicStore'
									},
						    	tpl:[
						    		'<tpl for=".">',
						    		'<div class="thumb-wrap" id="{index}">',
							        	'<div style="width:160px;height:113px;background:url({sltUrl});background-size:100%">',
								        	'<img src="'+TzUniversityContextPath+'/statics/images/tranzvision/bj.png" class="picEider" onMouseOver="getMouser(this)" onMouseOut="outMouser(this)" onclick="clickMouser_Editor({index})"/>',
								        	'<img src="'+TzUniversityContextPath+'/statics/images/tranzvision/jh.png" class="picEider" onMouseOver="getMouser(this)" onMouseOut="outMouser(this)" onclick="clickMouser_D({index})"/>',
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
							    	itemmouseenter:function(view, record, item, index, e, eOpts){
								    		item.getElementsByTagName("img")[0].style.display = "block";
								    		item.getElementsByTagName("img")[1].style.display = "block";
							    	},
							    	itemmouseleave:function(view, record, item, index, e, eOpts){
								    		item.getElementsByTagName("img")[0].style.display = "none";
								    		item.getElementsByTagName("img")[1].style.display = "none";
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
										type: 'artAttachmentStore'
									},
									selModel: {
       	 						type: 'checkboxmodel'
   				 				},
									tbar: [{
    								xtype: 'form',
    								bodyStyle:'padding:10px 0px 0px 0px',
    								items:[{
		            			xtype: 'fileuploadfield',
		            			buttonText: '上传附件',
											//name: 'attachmentUpload', 
		            						name: 'websitefile',
											buttonOnly:true,
											width: 88,
											listeners:{
												change:function(file, value, eOpts){
													addAttach(file, value, "ATTACHMENT");
												}
											}
    								}]
    							},"-", 
                	{iconCls: 'remove',text: '删除',tooltip:"删除选中的数据",handler: 'deleteArtAttenments'}],
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
												return '<a href="'+arr[1]+'" target="_blank">'+arr[0]+'</a>';
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
								}]	
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
        },{
           xtype: 'displayfield',
           fieldLabel: '状态',
					 name: 'publishStatusDesc'
        },{
           xtype: 'displayfield', 
           fieldLabel: '页面访问URL',
					 name: 'publishUrl'
        }]
    }],
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
			text: '保存',
			iconCls:"save",
			handler: 'onArtSave'
		}, {
			text: '确定',
			iconCls:"ensure",
			handler: 'onArtEnsure'
		}, {
			text: '关闭',
			iconCls:"close",
			handler: 'onArtClose'
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
		//var dateStr = Ext.Date.format(new Date(), 'Ymd');      
		
		var upUrl = "";
		var siteId = file.findParentByType("artInfo").child("form").getForm().findField("siteId").getValue();
		if(siteId==""){
			Ext.Msg.alert("错误","不存在站点，请先为该机构新建站点");
			return;
		}
		
		if(attachmentType=="ATTACHMENT"){ 
			upUrl = file.findParentByType("artInfo").child("form").getForm().findField("saveAttachAccessUrl").getValue();
			if(upUrl==""){
				Ext.Msg.alert("错误","未定义上传附件的路径，请与管理员联系");
				return;
			}
		}else{
			upUrl = file.findParentByType("artInfo").child("form").getForm().findField("saveImageAccessUrl").getValue();
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

		}												  
	});
}
function clickMouser_Editor(vor){
	className = 'KitchenSink.view.content.contentMg.picEdit';
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
		
   // Ext.getCmp("picView").findParentByType("artInfo").add(win);
  picViewCom.findParentByType("artInfo").add(win);
  win.show();
}


function refreshPicStore(picStore){
	var i = 0;
	for(i=0; i<picStore.getCount(); i++){
		picStore.getAt(i).data.index = i + 1;
	}
}
