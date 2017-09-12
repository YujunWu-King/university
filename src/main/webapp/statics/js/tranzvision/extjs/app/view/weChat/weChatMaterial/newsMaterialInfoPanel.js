Ext.define('KitchenSink.view.weChat.weChatMaterial.newsMaterialInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'newsMaterialInfoPanel', 
	controller: 'newsMaterialController',
	currentRecord:{},
	requires: [
	    'Ext.data.*',
        'Ext.util.*',
        'KitchenSink.view.weChat.weChatMaterial.newsArticlesStore',
	    'KitchenSink.view.weChat.weChatMaterial.newsMaterialController'
	],
    title: "图文素材", 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',

	constructor: function (config) {
		 this.jgId = config.jgId;
		 this.wxAppId = config.wxAppId;
		 this.materialID = config.materialID;
		 this.actType = config.actType;
		 
		 this.callParent();
	},
	
	listeners: {
		beforerender: function(panel) {
			//渲染前加载样式
			var el = document.createElement('link');
			el.setAttribute('rel', 'stylesheet');
			el.setAttribute('type', 'text/css');
			el.setAttribute('href', TzUniversityContextPath+'/statics/css/website/news-material.css');
			TranzvisionMeikecityAdvanced.Boot.doc.head.appendChild(el);
		},
		afterrender: function(panel){
			var actType = panel.actType;
			var jgId = panel.jgId;
			var wxAppId = panel.wxAppId;
			var materialID = panel.materialID;
			
			var articesView = panel.down('dataview[reference=newsMaterialArticesView]');
			var articesViewStore = articesView.getStore();
			if(actType == "add"){
				var r = Ext.create('KitchenSink.view.weChat.weChatMaterial.newsArticlesModel', {
			        title : "",
			        thumb_media_id : "",
			        thumb_img_url : "",
			        author : "",
			        digest : "",
			        show_cover_pic : 1,
			        content : "",
			        content_source_url : "",			        
			        orderNum: 0,
			        isCurrArt: 'Y',
			        publishSta: 'N'
				});
				articesViewStore.insert(0, r);
				
				
				
				panel.currentRecord = articesViewStore.getAt(0);
				//articesView.updateLayout();
				panel.updateLayout();
			}else{
				var form = panel.child('form[reference=newsMaterialForm]').getForm();
				var tzParams = '{"ComID":"TZ_WX_SCGL_COM","PageID":"TZ_WX_TWSC_STD","OperateType":"QF","comParams":{"jgId":"'+jgId+'","wxAppId":"'+wxAppId+'","materialID":"'+materialID+'"}}';
                 //加载数据
                Ext.tzLoad(tzParams,function(responseData){
                 	form.setValues(responseData.formData);

	                var publishSta=responseData.formData.publishSta;
	                var revokeBtn=panel.down("button[name=revokeBtn]");
	                var addArticesBtn = panel.down("button[name=addArticesBtn]");
	                if(publishSta == "Y"){
	                    revokeBtn.setDisabled(false);
	                    addArticesBtn.setHidden(true)
	                }else{
	                    revokeBtn.setDisabled(true);
	                    addArticesBtn.setHidden(false)
	                }
                });
                 
                //加载文章 
                articesViewStore.tzStoreParams = '{"jgId":"'+jgId+'","wxAppId":"'+wxAppId+'","materialID":"'+materialID+'"}';
                articesViewStore.load({
                	callback: function(){
                		chooseMaterial(0);
                	}
                });                
			}
		}
	},
	
	
	initComponent: function () {
		var me = this;
		
		var store = new KitchenSink.view.weChat.weChatMaterial.newsArticlesStore();
		
		Ext.apply(this, {
		    items: [{
		        xtype: 'form',
		        reference: 'newsMaterialForm',
				layout: {
		            type: 'vbox',
		            align: 'stretch'
		        },
		        border: false,
		        bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
				
		        fieldDefaults: {
		            msgTarget: 'side',
		            labelWidth: 100,
		            labelStyle: 'font-weight:bold'
		        },
				
		        items: [{
		            xtype: 'hiddenfield', 
					name: 'materialID'
		        },{
		            xtype: 'hiddenfield', 
					name: 'mediaId'
		        },{
		            xtype: 'hiddenfield', 
					name: 'jgId',
					value: me.jgId
		        },{
		            xtype: 'hiddenfield', 
					name: 'wxAppId',
					value: me.wxAppId
		        },{
		            xtype: 'textfield',
		            fieldLabel: "素材名称", 
					name: 'name',
		            allowBlank: false
		        },{
		            xtype: 'textfield',
					fieldLabel: "备注信息",
					name: 'bzInfo',
					allowBlank: false
		        },{
		        	xtype: 'panel',
		        	style: 'margin-top: 10px',
		        	layout: {
		                type: 'hbox',
		                align: 'stretch'
		            },
		        	items:[{
		        		xtype: 'panel',
		        		width: 240,
		        		border: false,
		        		style: 'margin-right:10px',
		        		layout: {
	    		            type: 'vbox',
	    		            align: 'stretch'
	    		        },
		        		items: [{
		        			xtype: 'dataview',
		        			reference: 'newsMaterialArticesView',
			        		title: '图文消息预览',
			                store: store,
			        		frame        : true,
			        		//cls          : 'dd',
			        		itemSelector : 'dd',
			        		overItemCls  : 'artices-item-over',
			        		trackOver    : true,
			        		tpl : Ext.create('Ext.XTemplate',
                                '<tpl for=".">',
                                    '<dd>',
                                    	'<div class="artices-item artices-order{[values.orderNum == 0 ? "0" : ""]} {[values.isCurrArt == "Y" ? "current-item" : ""]}">',
    										'<div class="',
    											'<tpl if="this.isFristArt(values)">cover</tpl>',
    											'<tpl if="this.isFristArt(values) == false">thumb</tpl>',
    										'" style="background-image: url(\''+ TzUniversityContextPath  +'{thumb_img_url}\');"></div>',
    										'<div class="artices_title"><span>{[values.title == "" ? "标题" : values.title]}</span></div>',
    										'<div class="edit-mask">',
    											'<div class="action-btn">',
	    											'<a href="javascript:void(0);" class="btn-circle edit" onclick="chooseMaterial({orderNum})"></a>',
	    											'<tpl if="this.isFristArt(values) == false && this.isPublish(values) == false">',
	    												'<a href="javascript:void(0);" class="btn-circle delete" onclick="deleteMaterial({orderNum})"></a>',
    												'</tpl>',
    											'</div>',
    										'</div>',
        								'</div>',
                                    '</dd>',
                                '</tpl>',{
			                     isFristArt: function(values){
			                    	 return values.orderNum == 0;
			                     },
			                     isPublish: function(values){
			                    	 return values.publishSta == "Y";
			                     },
			        			 chooseMaterial: function(values){
			        				 console.log(values);
			        			 }
			                }),
			                listeners:{
					    		render:function(v) {
					    			//拖拽排序
					    			onDataViewRender(v);
						    	}
					    	},
		        		},{
		        			xtype: 'button',
                            text: '<font color="#c0c0c0" size="8" style="display: block;">+</font>',
                            name:'addArticesBtn',
                            height: 50,
                            style:'background-color:white;border: 1px dotted #d9dadc;margin-top:6px;',
                            handler: function(btn){
                            	var articesView = btn.findParentByType('panel').child('dataview');
                            	var articesViewStore = articesView.getStore();
                            	var count = articesViewStore.getCount();
                				var r = Ext.create('KitchenSink.view.weChat.weChatMaterial.newsArticlesModel', {
                			        title : "",
                			        thumb_media_id : "",
                			        thumb_img_url : "",
                			        author : "",
                			        digest : "",
                			        show_cover_pic : 1,
                			        content : "",
                			        content_source_url : "",			        
                			        orderNum: count,
                			        isCurrArt: '',
                			        publishSta: 'N'
                				});
                				articesViewStore.insert(count, r);
                				articesView.updateLayout();
                            }
		        		}]
		        	},{
		        		flex: 1,
	                	xtype: 'ueditor',
	                	name: 'content',
	                	model: 'newsMaterial',
	                	minWidth: 400,
	                	height: 500,
	                	zIndex: 999,
	                	panelXtype: 'newsMaterialInfoPanel', 
	                	//自定义按钮-插入素材图片执行方法
	                	insertMaterialImg: function(editor){
	                		var wxAppId = me.wxAppId;
	                		Ext.tzSetCompResourses("TZ_GD_WXMSG_COM");
	                        //是否有访问权限
	                        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WXMSG_COM"]["TZ_GD_SCGL_STD"];
	                        if( pageResSet == "" || pageResSet == undefined){
	                            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
	                            return;
	                        }
	                        //该功能对应的JS类
	                        var className = pageResSet["jsClassName"];
	                        if(className == "" || className == undefined){
	                            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_SCGL_STD，请检查配置。');
	                            return;
	                        }

	                        var contentPanel,cmp, className, ViewClass, clsProto;
	                        var themeName = Ext.themeName;

	                        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
	                        contentPanel.body.addCls('kitchensink-example');
	                        if(!Ext.ClassManager.isCreated(className)){
	                            Ext.syncRequire(className);
	                        }
	                        ViewClass = Ext.ClassManager.get(className);
	                        clsProto = ViewClass.prototype;

	                        Ext.syncRequire(className);
	                        ViewClass = Ext.ClassManager.get(className);
	                        
	                        var win = new ViewClass({
	                        	materialType: 'TP',
	                        	wxAppId: wxAppId,
	                        	callback: function(rec){
	                        		var mediaId = rec.get("mediaId");
	                        		var src = rec.get("src");
	                        		var mediaUrl = rec.get("mediaUrl");

	                        		var imgHtml = '<img src="'+ mediaUrl +'">';
	                        		editor.execCommand('insertHtml', imgHtml);
	                        	}
	                        });

	                       win.show(); 
	                	},
	                	listeners:{
                			change: function(field){
                				var newVal = field.getValue();
                				var currentRecord = me.currentRecord;
                				currentRecord.set("content", newVal);
                			}
                		}
	                },{
	                	type: 'form',
	                	width: 300,
	                	style: 'margin-left: 10px',
	                	layout: {
	    		            type: 'vbox',
	    		            align: 'stretch'
	    		        },
	                	items:[{
	                		xtype:'textfield',
	                		name:'title',
	                		emptyText: '标题，最多64字，必填',
	                		allowBlank: false,
	                		maxLength: 64,
	                		listeners:{
	                			change: function(field,newVal,oldVal){
	                				var currentRecord = me.currentRecord;
	                				currentRecord.set("title", newVal);
	                			}
	                		}
	                	},{
	                		xtype: 'textareafield',
	                		name: 'digest',
	                		emptyText: '描述，最多120字，选填',
	                		maxLength: 120,
	                		listeners:{
	                			change: function(field,newVal,oldVal){
	                				var currentRecord = me.currentRecord;
	                				currentRecord.set("digest", newVal);
	                			}
	                		}
	                	},{
	                		xtype:'textfield',
	                		name: 'author',
	                		emptyText: '作者，最多8字，选填',
	                		maxLength: 8,
	                		listeners:{
	                			change: function(field,newVal,oldVal){
	                				var currentRecord = me.currentRecord;
	                				currentRecord.set("author", newVal);
	                			}
	                		}
	                	},{
	                		xtype: 'button',
                            text: '<span style="font-size:14px;">选择封面图片</span>',
                            name:'chooseScBtn',
                            style:'background-color:green;height:40px;',
                            width: 160,
                            handler:'chooseCoverPic'
	                	},{
	                		xtype: 'component',
	                		html: '建议尺寸 900×500',
	                		padding: '10 0 0 0'
	                	},/*{
	                		xtype:'textfield',
	                		name: 'content_source_url',
	                		emptyText: '原文链接，输入http://',
	                		listeners:{
	                			change: function(field,newVal,oldVal){
	                				var currentRecord = me.currentRecord;
	                				currentRecord.set("content_source_url", newVal);
	                			}
	                		}
	                	},*/{	                			
                			xtype: 'combo',
                            labelWidth: 100,
                            editable: false,
                            fieldLabel: '发布状态',
                            name: 'publishSta',
                            mode: "remote",
                            ignoreChangesFlag:true,
                            preSubTpl: [
                               '<div id="{cmpId}-triggerWrap" data-ref="triggerWrap" style="border:0" class="{triggerWrapCls} {triggerWrapCls}-{ui}">',
                                   '<div id={cmpId}-inputWrap data-ref="inputWrap" class="{inputWrapCls} {inputWrapCls}-{ui}">'
                            ],
                            readOnly:true,
                            valueField: 'status',
                            displayField: 'statusDesc',
                            store: {
                                fields: ["status", "statusDesc"],
                                data: [
                                    {status: "Y", statusDesc: "已发布"},
                                    {status: "N", statusDesc: "未发布"}
                                ]
                            },
                            value:'N',
                            style: 'background:none; border-right: 0px solid;border-top: 0px solid;border-left: 0px solid;border-bottom: #000000 0px solid;'
	                	}]
		        	}]
		        }]
		    }]
		});
		
        this.callParent();
    },
		
    buttons: [{
		text: '发布',
		iconCls:"publish",
		name: 'publishBtn',
		handler: 'publishToWx'
	},{
		text: '撤销发布',
		iconCls:"revoke",
		name: 'revokeBtn',
		disabled: true,
		handler: 'revokeFromWx'
	},{
		text: '保存',
		iconCls:"save",
		name: 'saveBtn',
		handler: 'onSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		name: 'ensureBtn',
		handler: 'onEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: function(btn){
			var panel = btn.findParentByType('newsMaterialInfoPanel');
			if(panel) panel.close();
		}
	}]
});



function chooseMaterial(rowNum){
	var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
	var panel = contentPanel.getActiveTab();
	if(panel.getXType() == "newsMaterialInfoPanel"){
		var articesView = panel.down('dataview[reference=newsMaterialArticesView]');
		var articesViewStore = articesView.getStore();
		
		//去除当前标识
		articesViewStore.each(function(rec) {   
	       rec.set("isCurrArt","N");
		});  
		
		var record = articesViewStore.getAt(rowNum);
		record.set("isCurrArt","Y");
		panel.currentRecord = record;
		
		var title = record.get('title');
		var content = record.get('content');
		var digest = record.get('digest');
		var author = record.get('author');
		
		var form = panel.down('form[reference=newsMaterialForm]').getForm();
		form.setValues({
			title: title,
			content: content,
			digest: digest,
			author: author
		});
	}
}


function deleteMaterial(rowNum){
	var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
	var panel = contentPanel.getActiveTab();
	if(panel.getXType() == "newsMaterialInfoPanel"){
		var articesView = panel.down('dataview[reference=newsMaterialArticesView]');
		var articesViewStore = articesView.getStore();
		var selList = articesViewStore.getAt(rowNum);
		var isCurrArt = selList.get("isCurrArt");
		
		articesViewStore.remove(selList);		
		if(isCurrArt == "Y"){
			chooseMaterial(0);
		}
		refreshArticesStore(articesViewStore);
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
	            if(idx > -1 && idx < v.store.getCount()){
	            	draglast = v.getRecord(sourceEl);
	            	v.getStore().remove(dragData.record);
		            v.getStore().insert(idx, [dragData.record]);
		            return true;
	            }

            return false;
        },
        afterDragDrop:function(target,e,id){
        	//更新排序序号
        	refreshArticesStore(v.getStore());
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


function refreshArticesStore(store){
	for(var i=0; i<store.getCount(); i++){
		store.getAt(i).set("orderNum", i);
	}
}