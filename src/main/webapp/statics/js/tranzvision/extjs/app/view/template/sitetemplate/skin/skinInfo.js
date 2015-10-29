Ext.define('KitchenSink.view.template.sitetemplate.skin.skinInfo', {
 extend: 'Ext.panel.Panel',
 xtype: 'skinInfo', 
 controller: 'siteSkinInfo',
 	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
	    'KitchenSink.view.template.sitetemplate.skin.siteSkinController',//保存
	    'KitchenSink.view.template.sitetemplate.skin.attachmentModel',
		'KitchenSink.view.template.sitetemplate.skin.skinRoleModel',//图片列表json格式
		'KitchenSink.view.template.sitetemplate.skin.skinstate',
        'KitchenSink.view.template.sitetemplate.skin.skinPictureStore'//图片列表json路径
	],
    title: '站点皮肤设置', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
	items:[
	{
        xtype: 'form',
        reference: 'skinAccountForm',//查看表单时cmp.on引用
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
            items:[{
            	xtype: 'hiddenfield',
            	readOnly:true,
            	fieldLabel: '站点模板编号',
				name: 'siteId'
        	},{
            	xtype: 'hiddenfield',
            	readOnly:true,
            	fieldLabel: '皮肤编号',
				name: 'skinId',
				value: ''
        	},{
            	xtype: 'combobox',
            	queryMode: 'remote',
            	editable:false,
            	emptyText:'请选择',
            	fieldLabel: '状态',
            	valueField: 'TValue',
            	displayField:'TSDesc',
				name: 'skinStatus',
				store: new KitchenSink.view.common.store.appTransStore("TZ_SKIN_STATE")
				/*
				listeners: {
				  	afterrender: function(skinStatus){
						Ext.tzLoad('{"OperateType":"TV","fieldName":"TZ_SKIN_STATE"}',function(response){
							skinStatus.setStore(new Ext.data.Store({		
								fields: ['TValue','TSDesc','TLDesc'],
								data:response.TZ_SKIN_STATE
							}));
						});
					}
				}*/
            },{
	            xtype: 'textfield',
	            fieldLabel: '皮肤名称',
				name: 'skinName'
            },{
	            xtype: 'textarea',
	            fieldLabel: '皮肤样式源码',
	            labelSeparator:':',//分隔符
	            labelWindth:300,
	            height: 200,
				name: 'skinCode'
            }],
	},{
		xtype: 'grid',
		height: 315, 
		title: '皮肤效果图片集',
		frame: true,
		columnLines: true,
		name: 'applyItemGrid',
		reference: 'applyItemGrid',
		style:"margin:10px",
		store: {
			type: 'skinPictureStore'
		},
		plugins: {
			ptype: 'cellediting',
			pluginId: 'applyItemCellediting',
		},
		viewConfig: {
			plugins: {
			ptype: 'gridviewdragdrop',
			dragText: '拖拽进行信息项的排序'
			},
			listeners: {
					drop: function(node, data, dropRec, dropPosition) {
						data.view.store.beginUpdate();
						var items = data.view.store.data.items;
						for(var i = 0;i< items.length;i++){
							items[i].set('imgxh',i+1);
						}
						data.view.store.endUpdate();
					}
				}
		},
		columns: [{ 
			text: '图片ID',
			dataIndex: 'imgid',
            sortable: false,
            hidden: true
		},{
			text: '图片顺序',
			dataIndex: 'imgxh',
			sortable: true,
			minWidth: 80,
			flex: 1,
			hidden: true
		},{
			text: '图片名称',
			dataIndex: 'imgname',
			minWidth: 80,
			flex: 1,
			editor: {
        		xtype: 'textfield',
        		allowBlank: false
    		}
		},{
			text: '图片访问地址',
			dataIndex: 'imgurl',
			minWidth: 80,
			flex: 1
		},{
			text: '操作',
           	menuDisabled: true,
           	sortable: false,
		   	width:100,
           	xtype: 'actioncolumn',
		   	items:[
				{iconCls: 'remove',tooltip: '删除',handler: 'deleteSitePic'}
		   	]
        }],
		bbar: {
			xtype: 'pagingtoolbar',
			pageSize: 5,
			/*
			reference: 'skinRoleToolBar',
			store: {
				type: 'skinPictureStore'
			},*/
			listeners:{
				afterrender: function(pbar){
					var grid = pbar.findParentByType("grid");
					pbar.setStore(grid.store);
				}
			},
			displayInfo: true,
			displayMsg: '显示{0}-{1}条，共{2}条',
			beforePageText: '第',
			afterPageText: '页/共{0}页',
			emptyMsg: '没有数据显示',
			plugins: new Ext.ux.ProgressBarPager()
		}
        },{
			xtype: 'form',
			layout: 'hbox',
			width:'100%',
			height:'100%',
			defaults:{
				margin:'0 0 0 20px'
			},
			items:[{
				xtype:'button',
				text:'预览图片集',
				margin:'0 0 0 700px',
				listeners:{
					click:function(file, value, eOpts ){
						
					}
				}
			},{
	            xtype: 'fileuploadfield',
	            name: 'menutypeimg',
	            buttonText: '上传图片',
	            //msgTarget: 'side',
	            buttonOnly:true,
				listeners:{
					change:function(file, value, eOpts ){
						if(value != ""){
							var form = file.findParentByType("form").getForm();
							//获取该类
							var panel = file.findParentByType("skinInfo");
							if(panel.actType == "update"){
								//获取后缀
								var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
								if(fix.toLowerCase() == "jpg" || fix.toLowerCase() == "png" || fix.toLowerCase() == "gif" || fix.toLowerCase() == "bmp" || fix.toLowerCase() == "ico"){
									form.submit({
										url: '/UpdServlet?filePath=/linkfile/sysImages',
										waitMsg: '图片正在上传，请耐心等待....',
										success: function (form, action) {
											var message = action.result.msg;
											var path = message.accessPath;
											var sysFileName = message.sysFileName;
											if(path.charAt(path.length - 1) == '/'){
												path = path + sysFileName;
											}else{
												path = path + "/" + sysFileName;
											}
											var comSiteParams = panel.child("form").getForm().getValues();
											//站点模板id
											var siteId = comSiteParams["siteId"];
											//皮肤id
											var skinId = comSiteParams["skinId"];
											
											var comParams = '{"siteId":"' + siteId + '","skinId":"' + skinId + '","name":"' + action.result.msg.filename +'","path":"' + path + '"}';
											var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_GD_ZDPFPIC_STD","OperateType":"EJSON","comParams":'+comParams+'}';
											Ext.Ajax.request({
											    //url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_ATT_D.TZ_GD_ATT_FILE.FieldFormula.Iscript_SkinPicAttUp',
												url: Ext.tzGetGeneralURL,
											    params: {
											        tzParams: tzParams
											    },
											    success: function(response){
											    	var responseText = eval( "(" + response.responseText + ")" );
											        if(responseText.comContent.success == 0){
											        	var applyItemGrid =  file.findParentByType("form").previousSibling();
											        	/*var r = Ext.create('KitchenSink.view.template.sitetemplate.skin.attachmentModel', {
														"imgid": action.result.msg.sysFileName,
														"imgxh": 0,
														"imgname": "<a href='"+action.result.msg.accessPath+"/"+action.result.msg.sysFileName+"' target='_blank'>"+action.result.msg.filename+"</a>",
														"imgurl": action.result.msg.accessPath+"/"+action.result.msg.sysFileName
														});
											
														applyItemGrid.store.insert(0,r);*/
														var tzStoreParams = '{"siteId":"'+siteId+'","skinId":"'+skinId+'"}';
														applyItemGrid.store.tzStoreParams = tzStoreParams;
														applyItemGrid.store.reload();
											        }else{
											        	Ext.MessageBox.alert("错误", responseText.comContent.message);        
											        }
											    }
											});
											//重置表单
											form.reset();
										},
										failure: function (form, action) {
											//重置表单
											form.reset();
											Ext.MessageBox.alert("错误", action.result.msg);
										}
									});
								}else{
									//重置表单
									form.reset();
									Ext.MessageBox.alert("提示", "请上传jpg|png|gif|bmp|ico格式的图片。");
								}
							}else{
								//重置表单
								form.reset();
								Ext.MessageBox.alert("提示", "请先保存菜单类型。");
							}
						}
					}
				}
	        }]
		}],//页面itemend
        
	    buttons: [{
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
	    }]
});