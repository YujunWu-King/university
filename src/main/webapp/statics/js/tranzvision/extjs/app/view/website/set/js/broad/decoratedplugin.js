/*设置动态js文件加载地址*/


Ext.Loader.setConfig({
	enabled:true,
	paths:{
	  'KitchenSink': TzUniversityContextPath + '/statics/js/tranzvision/extjs/app',
	  'Ext.ux': TzUniversityContextPath + '/statics/js/lib/extjs/ux',
	  'tranzvision.extension': TzUniversityContextPath + '/statics/js/tranzvision/extjs/extension'
	}
});


Ext.require(['Ext.grid.*', 'Ext.window.Window', 'Ext.container.Viewport', 'Ext.layout.container.Border', 'Ext.state.*', 'Ext.data.*', 'Ext.grid.plugin.BufferedRenderer','Ext.ux.colorpick.Field']);

var urlBegin = TzUniversityContextPath + "/dispatcher";
var urlPreview = TzUniversityContextPath + "/preview";
var urlDecorate = TzUniversityContextPath + "/decorate";
var editEnrollFieldComponentID = "_A0000041";
/*网站首页个人信息展示选择页面*/
var editPersonInfoComponentID="_A0000085";
/*
function getQueryString(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) return unescape(r[2]); return null; 
} 
*/
function FindData(value,direct){

	var rgbArray = new Array(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255);
	var hexArray = new Array("00","01","02","03","04","05","06","07","08","09","0A","0B","0C","0D","0E","0F","10","11","12","13","14","15","16","17","18","19","1A","1B","1C","1D","1E","1F","20","21","22","23","24","25","26","27","28","29","2A","2B","2C","2D","2E","2F","30","31","32","33","34","35","36","37","38","39","3A","3B","3C","3D","3E","3F","40","41","42","43","44","45","46","47","48","49","4A","4B","4C","4D","4E","4F","50","51","52","53","54","55","56","57","58","59","5A","5B","5C","5D","5E","5F","60","61","62","63","64","65","66","67","68","69","6A","6B","6C","6D","6E","6F","70","71","72","73","74","75","76","77","78","79","7A","7B","7C","7D","7E","7F","80","81","82","83","84","85","86","87","88","89","8A","8B","8C","8D","8E","8F","90","91","92","93","94","95","96","97","98","99","9A","9B","9C","9D","9E","9F","A0","A1","A2","A3","A4","A5","A6","A7","A8","A9","AA","AB","AC","AD","AE","AF","B0","B1","B2","B3","B4","B5","B6","B7","B8","B9","BA","BB","BC","BD","BE","BF","C0","C1","C2","C3","C4","C5","C6","C7","C8","C9","CA","CB","CC","CD","CE","CF","D0","D1","D2","D3","D4","D5","D6","D7","D8","D9","DA","DB","DC","DD","DE","DF","E0","E1","E2","E3","E4","E5","E6","E7","E8","E9","EA","EB","EC","ED","EE","EF","F0","F1","F2","F3","F4","F5","F6","F7","F8","F9","FA","FB","FC","FD","FE","FF");
	var index;
	if (direct =="L")
	{
		for (var i = 0 ; i<rgbArray.length; i++ )
		{
			if (rgbArray[i] ==value)
			{
				index=i;
				
			}
		}
	return hexArray[index];
	}else{
		//alert(value ==hexArray[51] );
		for (var j = 0 ;  j<hexArray.length;j++ )
		{
			if (hexArray[j] ==value)
			{
				index=j;
			}
		}
	return rgbArray[index];
	}
}



Ext.define('Ext.panel.EditWinPanel', {
	extend: 'Ext.form.Panel',
	xtype: 'form',
	border: false,
	width: 740,
	bodyStyle: 'padding-left:10px;padding-top:10px;padding-right:10px;',
	layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
	
	fieldDefaults: {
                    labelWidth: 80
              
                },
	items: [],
	buttons: [
	{
		text: '保存',
		handler: function() {
			var comParams = '"update":[{"typeFlag":"save","data":'+Ext.JSON.encode(editPanel.getForm().getValues())+'}]';
			var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_AREA_SETED_STD","OperateType":"U","comParams":{'+comParams+'}}';
			
			/*如果页面页面代码为空，则表示没有改变过表单值，无需提交*/
			if (! editPanel.getForm().findField('bodyCode').getValue())
			{
				//查看是不是报名中心;
				var areaType = editPanel.getForm().findField('areaType').getValue();
				var bmbClassShow = editPanel.getForm().findField('bmbClassShow');
				if(areaType=="AC" && bmbClassShow != null){
					var tzParamsGz = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_SITEI_CLSGZ_STD","OperateType":"SHOWGZ","comParams":'+Ext.JSON.encode(editPanel.getForm().getValues())+'}';
					
					Ext.Ajax.request({
						url: urlBegin,
						params: {
							tzParams:tzParamsGz
						},
						method : 'POST',    
						 success: function(response){
							var obj = Ext.util.JSON.decode(response.responseText);
							if(obj.comContent.success==true){
								Ext.toast({
									html: '已保存！',
									closable: false,
									align: 't',
									style: "background-color:white",
									slideInDuration: 400,
									minWidth: 400
								});
							}else{
								Ext.Msg.show({
													title: '提示信息',
													msg: '保存失败',
													buttons: Ext.Msg.OK,
													icon: Ext.Msg.WARNING,
													buttonText: {
														ok: "关闭"
													}
												});
								
							}
						},
						failure: function() {
							Ext.Msg.show({
								title: '提示信息',
								msg: "保存失败！",
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.WARNING,
								buttonText: {
									ok: "关闭"
								}
							});
						}
					});
				}else{
					Ext.Msg.show({
						title: '提示信息',
						msg: '没有要提交的数据！',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING,
						buttonText: {
							ok: "确定"
						}
					});
				}
				return;
			}
			
			Ext.Ajax.request({
				url: urlBegin,
				params: {
					tzParams:tzParams
				},
				method : 'POST',    
				 success: function(response){
					var obj = Ext.util.JSON.decode(response.responseText);
					if(obj.comContent.success==true){
						Ext.toast({
							html: '已保存！',
							closable: false,
							align: 't',
							style: "background-color:white",
							slideInDuration: 400,
							minWidth: 400
						});
					}else{
						Ext.Msg.show({
											title: '提示信息',
											msg: '保存失败',
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.WARNING,
											buttonText: {
												ok: "关闭"
											}
										});
						
					}
				},
				failure: function() {
					Ext.Msg.show({
						title: '提示信息',
						msg: "保存失败！",
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING,
						buttonText: {
							ok: "关闭"
						}
					});
				}
			});
		}
	},
	{
		text: '返回',
		handler: function() {
			this.up('form').up("window").close();
		}
	}]
	 
});

Ext.define('ForumThread', {
	extend: 'Ext.data.Model',
	fields: ['title', 'forumtitle', 'forumid', 'username', {
		name: 'replycount',
		type: 'int'
	},
	{
		name: 'lastpost',
		mapping: 'lastpost',
		type: 'date',
		dateFormat: 'timestamp'
	},
	'lastposter', 'excerpt', 'threadid'],
	idProperty: 'threadid'
});


Ext.define('SimpleTasks.view.lists.ContextMenu', {
	extend: 'Ext.menu.Menu',
	xtype: 'listsContextMenu',
	items: [{
		text: 'New List',
		iconCls: 'tasks-new-list',
		id: 'new-list-item'
	},
	{
		text: 'New Folder',
		iconCls: 'tasks-new-folder',
		id: 'new-folder-item'
	},
	{
		text: 'Delete',
		iconCls: 'tasks-delete-folder',
		id: 'delete-folder-item'
	}]

});
var editPanel;
Ext.onReady(function() {


	var store = Ext.create('Ext.data.TreeStore', {
		root: {
			expanded: true,
			children: [{
				text: "个人信息",
				leaf: true
			},
			{
				text: "新闻通知",
				leaf: true
			},
			{
				text: "招生信息",
				leaf: true
			},
			{
				text: "活动预告",
				leaf: true
			}]
		}
	});

	Ext.define('Ext.from.Panel', {
		extend: 'Ext.tree.Panel',
		width: 200,
		height: 350,
		store: store,
		rootVisible: true

	});

	var tabs = new Ext.Panel({

		region: 'center',
		//距离top、right、bottom、left边界的距离,单位为像素  
		margins: '3 3 3 0',
		activeTab: 0,
		width: 200,
		autoScroll: true,
		items: [{
			html: '内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容'
		}]
	});

	var panel = new Ext.Panel({

		region: 'west',
		split: true,
		width: 200,
		//True表示为面板是可收缩的，并自动渲染一个展开/收缩的轮换按钮在头部工具条  
		//collapsible : true,  
		margins: '3 0 3 3',
		cmargins: '3 3 3 3'

	});

	var panel2 = new Ext.tree.Panel({
		region: 'west',
		split: true,
		width: 200,
		width: 200,
		height: 350,
		store: store,
		rootVisible: false,
		listeners: {
			itemclick: function(view, record, item, index, e, eOpts) {
				alert(123);
			}
		}
	});




	/*var addWindow = Ext.widget('window', {
				title:'添加区域',
                closable : true,  
				border : false,   
				width: 650,
				height: 450,
				autoHeight: true,
            	autoWidth:true,
				modal: true,
				resizable: true,
				layout : 'border',
               	items:[]
            });
	*/

	var addWindow = Ext.widget('window', {
		title: '添加区域',
		closeAction: 'hide',
		width: 760,
		height: 450,
		autoHeight: true,
		autoWidth: true,
		layout: 'border',
		resizable: true,
		modal: true,
		closable: true,
		items: []
	});

	var editWindow = Ext.widget('window', {
		title: '编辑区域',
		closeAction: 'hide',
		width: 760,
		autoHeight: true,
		autoWidth: true,
		layout: 'vbox',
		resizable: false,
		modal: true,
		closable: true,
		items: []
	});


/*菜单数据*/
/*
var urlParams=getQueryString("tzParams");

urlParams = Ext.util.JSON.decode(urlParams);

var siteid=urlParams.comParams.siteId;
*/
var siteid = Ext.get("siteid").getValue();

var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_MENU_SETED_STD","OperateType":"QF","comParams":{"siteId":"'+siteid+'"}}';

var menuStore = Ext.create('Ext.data.TreeStore', {
		proxy: {
			type: 'ajax',
			url: urlBegin + '?tzParams='+tzParams,
			reader: {
				root: 'comContent.root.children'
			}
		}
	});
	
var coluDataStore = new Ext.create('Ext.data.Store', {
				model: new Ext.data.Model({
					fields: [{
						name: 'value',
						mapping: 'value',
						type: 'string'
					},
					{
						name: 'text',
						mapping: 'text',
						type: 'string'
					},
					{
						name: 'select',
						mapping: 'select',
						type: 'string'
					}]
				}),
				proxy: {
					type: 'ajax',
					url: urlBegin + '?tzParams={"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_COLU_JSON_STD","OperateType":"QF","comParams":{"siteId":"'+siteid+'","menuId":"","type":"all"}}',
					reader: {
						type: 'json',
						root: 'comContent'
					}
				},
				autoLoad: true,
				remoteSort: true
			});

var BeforNode;
/*菜单左侧面板*/
	var menuTree = new Ext.tree.Panel({
		id:"menuTree",
		region: 'west',
		//enableDD: true,
		lines: false,
		split: true,
		width: 250,
		height: 760,
		store: menuStore,
		rootVisible: false,
		viewConfig: {
			plugins: {
				ptype: 'gridviewdragdrop',
				//enableDrag: true,
				//enableDrop: true
				dragText: '拖拽进行选项的排序'

			},
			listeners: {
					/*
					排序不再受到任何限制
					beforedrop: function(node, data, overModel, dropPosition, dropHandlers) {

						var targetOrder = overModel.data.order;
						if(targetOrder < 7){
							dropHandlers.cancelDrop();
						}
						if(data.records[0].data.order < 7){
							dropHandlers.cancelDrop();
						}
					},*/
					drop: function(node, data, dropRec, dropPosition) {
						
						var items = data.view.store.data.items;
						var array = [];
						for (var i = 0; i < items.length; i++) {
							if (items[i].id !="add")
							{
								array[i] = items[i].id;
							}

						}

						/*
						 var urlParams=getQueryString("tzParams");

						 urlParams = Ext.util.JSON.decode(urlParams);

						 var siteid=urlParams.comParams.siteId;
						*/
						 var siteid = Ext.get("siteid").getValue();
						 var comParams = '"update":[{"typeFlag":"sort","data":{"siteId":"'+siteid+'","menus":"'+array+'"}}]';
						 var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_MENU_EDITED_STD","OperateType":"U","comParams":{'+comParams+'}}';
						 Ext.Ajax.request({
							url: urlBegin,
							params: {
							tzParams:tzParams
							},
							method : 'POST',    
							success: function(response){
								var obj = Ext.util.JSON.decode(response.responseText);
								if(obj.state.errcode=="0"){

								var menuStore2 = Ext.create('Ext.data.TreeStore', {
										root:obj.comContent.root
									});

									Ext.getCmp("menuTree").reconfigure(menuStore2);
									Ext.get("letf_menu").child("div").dom.innerHTML=obj.comContent.divHtml;
								}
							},
							failure: function() {

							}
						});
					}
				}
		},
		listeners: {
			itemcontextmenu:function(thisNode, record, item, index, e, eOpts){
				var nodemenu=new Ext.menu.Menu({
					items: [
					{
						iconCls: 'remove',
						text: '删除菜单',
						hidden: true,
						handler:function(){
						/*
						var urlParams=getQueryString("tzParams");

						urlParams = Ext.util.JSON.decode(urlParams);

						var siteid=urlParams.comParams.siteId;
						*/
						var siteid = Ext.get("siteid").getValue();
						var comParams = '"update":[{"typeFlag":"delete","data":{"siteId":"'+siteid+'","menuId":"'+record.id+'"}}]';
						var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_MENU_EDITED_STD","OperateType":"U","comParams":{'+comParams+'}}';
						Ext.Ajax.request({
							url: urlBegin,
							params: {
							tzParams:tzParams
							},
							method : 'POST',    
							success: function(response){
							var obj = Ext.util.JSON.decode(response.responseText);
							if(obj.state.errcode=="0"){

								var menuStore2 = Ext.create('Ext.data.TreeStore', {
									root:obj.comContent.root
								});

								Ext.getCmp("menuTree").reconfigure(menuStore2);

								//Ext.getCmp("menuForm").getForm().reset();

								var formData = obj.data.data;

								Ext.getCmp("menuForm").getForm().setValues(formData);	

								Ext.get("letf_menu").child("div").dom.innerHTML=obj.comContent.divHtml;

								Ext.toast({
									html: '已删除！',
									closable: false,
									align: 't',
									style: "background-color:white",
									slideInDuration: 400,
									minWidth: 400
								});
							}else{
								Ext.Msg.show({
											title: '提示信息',
											msg: obj.state.errdesc,
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.WARNING,
											buttonText: {
												ok: "关闭"
											}
										});
								}
							},
							failure: function() {
								Ext.Msg.show({
								title: '提示信息',
								msg: "删除失败!",
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.WARNING,
								buttonText: {
									ok: "关闭"
								}
								});
							}
						});

						}
					}]
				});

				e.preventDefault();
				e.stopEvent();
				nodemenu.showAt(e.getX(), e.getY());
			 },
			 itemclick: function(view, record, item, index, e, eOpts) {
				/*
				 var urlParams=getQueryString("tzParams");

				 urlParams = Ext.util.JSON.decode(urlParams);

				 var siteid=urlParams.comParams.siteId;
				*/
				var siteid = Ext.get("siteid").getValue();
				 var menuid= record.id;

				if (menuid=="add")
				{
					Ext.getCmp("menuForm").getForm().findField('menuColu').setReadOnly(false);
					Ext.getCmp("menuForm").getForm().findField('menuTypeId').setReadOnly(false);
					Ext.getCmp("menuForm").getForm().findField('menuId').setValue("add");
				}else{
					Ext.getCmp("menuForm").getForm().findField('menuId').setValue(menuid);
				}
				
				var menuId = Ext.getCmp("menuForm").getForm().findField('menuId').getValue();

				 var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_MENU_EDITED_STD","OperateType":"QF","comParams":{"siteId":"'+siteid+'","menuId":"'+menuId+'"}}';
				 
				 Ext.Ajax.request({
						url: urlBegin,
						params: {
								tzParams: tzParams
						},
						success: function(response, opts) {

							var responseData = Ext.JSON.decode(response.responseText);

							var formData = responseData.comContent.data;
							
							var thisForm = view.up("window").down("form").getForm();
							thisForm.setValues(formData);

							var showmenutypeimgpath = "";
							if(formData.menutypeimg!=""){
								showmenutypeimgpath = TzUniversityContextPath + formData.menutypeimg;
							}
							
							var showmenunowimgpath = "";
							if(formData.menunowimg!=""){
								showmenunowimgpath = TzUniversityContextPath + formData.menunowimg;
							}
							
							view.up("window").down("form").down('form[name=imgForm]').child("image").setSrc(showmenutypeimgpath);
							view.up("window").down("form").down('form[name=imgForm2]').child("image").setSrc(showmenunowimgpath);
							
							var menuColuSelect = thisForm.findField('menuColu');
							var addColuBtns = Ext.getCmp('coluBtnGroup');
							var addColuBtn = Ext.getCmp('addcolubtn');
							var addURLFields = Ext.getCmp('linkURL');
							var menuLink = thisForm.findField('menuLink');
							var linkTarget = thisForm.findField('linkTarget');
							var menuTypeId = thisForm.findField('menuTypeId');
							addURLFields.hide();
							if(formData.addcolu=="Y"){
								menuColuSelect.show();
								addColuBtns.show();
							}else if(formData.addcolu=="URL"){
								addURLFields.show();
								menuColuSelect.hide();
								addColuBtns.hide();
							}else{
								menuColuSelect.hide();
								addColuBtns.hide();
							}
							
							/*设置是否可编辑*/
							//console.log(formData);
							if (formData.menuEdit=="N")
							{
								menuTypeId.setReadOnly(true);
								menuColuSelect.setReadOnly(true);
								addColuBtns.hide();
								menuLink.setReadOnly(true);
								linkTarget.setReadOnly(true);
							}else{
								menuTypeId.setReadOnly(false);
								menuColuSelect.setReadOnly(false);
								menuLink.setReadOnly(false);
								linkTarget.setReadOnly(false);
							}
						},
						failure: function(response, opts) {
						
						}
				  });

            },
			  /*
			itemmove: function(thisNode, oldParent, newParent, index, eOpts) {

			  var urlParams=getQueryString("tzParams");

			 urlParams = Ext.util.JSON.decode(urlParams);

			 var siteid=urlParams.comParams.siteId;

			  var comParams = '"update":[{"typeFlag":"sort","data":{"siteId":"'+siteid+'","menuId1":"'+thisNode.data.id+'","menuId2":"'+BeforNode+'"}}]';
			  var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_MENU_EDITED_STD","OperateType":"U","comParams":{'+comParams+'}}';
					Ext.Ajax.request({
							url: urlBegin,
							params: {
							tzParams:tzParams
							},
							method : 'POST',    
							success: function(response){
								var obj = Ext.util.JSON.decode(response.responseText);
								if(obj.state.errcode=="0"){

								var menuStore2 = Ext.create('Ext.data.TreeStore', {
										root:obj.comContent.root
									});

									Ext.getCmp("menuTree").reconfigure(menuStore2);
									Ext.get("letf_menu").dom.innerHTML=obj.comContent.divHtml;
								}
							},
							failure: function() {

							}
						});
				
			},
			*/
			beforeitemmouseenter:function(thisView, record, item, index, e, eOpts ){
				BeforNode=record.id;
			}
		}
	});


/*菜单右侧面板*/
	var menuForm = new Ext.form.Panel({
		id:'menuForm',
		region: 'center',
		border: false,
		bodyStyle: 'padding-left:10px;padding-top:10px;padding-right:10px;',
		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: 80
		},
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		items: [{
			fieldLabel: "菜单名称",
			name: "menuName",
			width: 80,
			xtype: "textfield"
		},
		{
			xtype: "combo",
			name: "menuTypeId",
			fieldLabel: "功能类型",
			store: new Ext.create('Ext.data.Store', {
				model: new Ext.data.Model({
					fields: [{
						name: 'value',
						mapping: 'value',
						type: 'string'
					},
					{
						name: 'text',
						mapping: 'text',
						type: 'string'
					},
					{
						name: 'select',
						mapping: 'select',
						type: 'string'
					}]
				}),
				proxy: {
					type: 'ajax',
					url: urlBegin + '?tzParams={"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_MENU_JSON_STD","OperateType":"QF","comParams":{"siteId":"'+siteid+'"}}',
					reader: {
						type: 'json',
						root: 'comContent'
					}
				},
				autoLoad: true,
				remoteSort: true
			}),
			displayField: 'text',
			valueField: 'value',
			listeners: {
				select: function(combo, selected) {
					//console.log(combo);
					//console.log(selected);
					var menuDescrValue = selected.data.typedescr;
					var menuTypeImg = selected.data.menutypeimg;
					var menuNowImg = selected.data.menunowimg;

					var thisForm = this.up("form").getForm();
					thisForm.findField('menuDescr').setValue(menuDescrValue);
					
					if(menuTypeImg!=""){
						menuTypeImg = TzUniversityContextPath + menuTypeImg;
					}
					
					if(menuNowImg!=""){
						menuNowImg = TzUniversityContextPath + menuNowImg;
					}
					
					Ext.getCmp("menuForm").down('form[name=imgForm]').child("image").setSrc(menuTypeImg);
					Ext.getCmp("menuForm").down('form[name=imgForm2]').child("image").setSrc(menuNowImg);

					var menuColuSelect = thisForm.findField('menuColu');
					var addColuBtns = Ext.getCmp('coluBtnGroup');
					var addURLFields = Ext.getCmp('linkURL');
					menuColuSelect.clearValue();
					addURLFields.hide();
					if(selected.data.addcolu=="Y"){
						menuColuSelect.show();
						addColuBtns.show();
					}else if(selected.data.addcolu=="URL"){
						addURLFields.show();
						menuColuSelect.hide();
						addColuBtns.hide();
					}else{
						menuColuSelect.hide();
						addColuBtns.hide();
					}
					
				}
			}
		},
		{
			xtype: "combo",
			name: "menuColu",
			fieldLabel: "对应栏目",
			hidden: true,
			store: coluDataStore,
			displayField: 'text',
			valueField: 'value'
		},
		{
			xtype: 'fieldcontainer',
			id: 'linkURL',
			hidden: true,
			items: [{
					xtype: 'textfield',
					name: 'menuLink',
					fieldLabel: '外部网址',
					width: 553
				},
				{
					xtype: "combo",
					name: "linkTarget",
					fieldLabel: "打开方式",
					width: 553,
					store: Ext.create('Ext.data.Store', {
								fields: ['value', 'text'],
								data : [
									{"value":"_blank", "text":"新开窗口"},
									{"value":"_self", "text":"当前窗口"},
								]
							}
					),
					displayField: 'text',
					valueField: 'value',
					queryMode: 'local'
				}]
		},
		{
			xtype: 'fieldcontainer',
			id: 'coluBtnGroup',
			hidden: true,
			items: [{
				xtype: 'button',
				text: '指定/更改对应栏目',
				margin: '0 0 0 230',
				hidden: true,
				handler:function(e){
				
				var menuId = this.up("form").getForm().findField('menuId').getValue();
			/*
				var urlParams=getQueryString("tzParams");

				 urlParams = Ext.util.JSON.decode(urlParams);

				var siteid=urlParams.comParams.siteId;
			*/
			var siteid = Ext.get("siteid").getValue();
				var colugridStore=new Ext.create('Ext.data.Store', {
							model: new Ext.data.Model({
							fields: [{
									name: 'value',
									mapping: 'value',
									type: 'string'
								},
								{
									name: 'text',
									mapping: 'text',
									type: 'string'
								}
								]
							}),
					     proxy: {
						 type: 'ajax',
						 url: urlBegin + '?tzParams={"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_COLU_JSON_STD","OperateType":"QF","comParams":{"siteId":"'+siteid+'","menuId":"'+menuId+'","type":"some"}}',
						reader: {
							type: 'json',
							root: 'comContent'
						}
					},
					autoLoad: true,
					remoteSort: true
					});

				var coluGrid = Ext.widget('window', {
					title: '指定/更改对应栏目',
					closeAction: 'hide',
					width: 220,
					border:false,
					//frame: true,
					//height:400,
					autoHeight: true,
					autoWidth: true,
					layout: 'form',
					resizable: true,
					modal: true,
					closable: true,
					items: [{
						xtype: 'grid',
						hideHeaders: true,
						columnLines: false,
						minHeight:100,
						/*selModel: {
							type: 'checkboxmodel'
						},*/
						multiSelect: false,
						selModel: Ext.create("Ext.selection.CheckboxModel", {
						injectCheckbox: 0,//checkbox位于哪一列，默认值为0
						mode: "single",//multi,simple,single；默认为多选multi
						checkOnly: false,//如果值为true，则只用点击checkbox列才能选中此条记录
						allowDeselect: true,//如果值true，并且mode值为单选（single）时，可以通过点击checkbox取消对其的选择
						enableKeyNav: true
						}),
						store: colugridStore,
						columns: [{
						hidden:true,
						dataIndex: 'value',
						flex: 1,
						sortable: false
						},{
						dataIndex: 'text',
						flex: 1,
						sortable: false
						}]
						
					}],
					buttons:[{
								text: '确认',
								iconCls: 'ensure',
								scale: 'small',
								handler: function() {
									var grid = this.up("window").down("grid");
									var records = grid.getSelectionModel().getSelection();
									if (records[0])
									{
										/*
										 var urlParams=getQueryString("tzParams");

										 urlParams = Ext.util.JSON.decode(urlParams);

										 var siteid=urlParams.comParams.siteId;
										*/
										var siteid = Ext.get("siteid").getValue();
										 var comParams = '"update":[{"typeFlag":"changeColu","data":{"siteId":"'+siteid+'","menuId":"'+menuId+'","coluId":"'+records[0].data.value+'"}}]';
										 var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_MENU_EDITED_STD","OperateType":"U","comParams":{'+comParams+'}}';
										 Ext.Ajax.request({
											url: urlBegin,
											params: {
											tzParams:tzParams
											},
											method : 'POST',    
											success: function(response){
											var obj = Ext.util.JSON.decode(response.responseText);
											if(obj.comContent.success==true){
											   Ext.getCmp("menuForm").getForm().findField('menuColu').setValue(records[0].data.value);
											}
										},
											failure: function() {

											}
										});

										this.up("window").close();
									}
									else
									{
										Ext.Msg.show({
										title: '提示信息',
										msg: "未选择栏目!",
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.WARNING,
										buttonText: {
											ok: "关闭"
										}
										});
									}
									
								}
							},{
								text: '返回',
								iconCls: 'close',
								scale: 'small',
								handler: function() {
									this.up("window").close();
								}
							}]	
				});
			coluGrid.show();

		   // coluGrid.down("grid").reconfigure(colugridStore);

				}
			},
			{
				xtype: 'button',
				text: '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;创建一个新栏目&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;',
				id:  'addcolubtn',
				margin: '0 0 0 550',
				handler:function(e){
					/*
					var urlParams=getQueryString("tzParams");

					urlParams = Ext.util.JSON.decode(urlParams);

					var siteid=urlParams.comParams.siteId;
					*/
					var menuTypeId = this.up("form").getForm().findField('menuTypeId').getValue();
					var siteid = Ext.get("siteid").getValue();
					var coluForm = Ext.widget('window', {
					title: '创建一个新栏目',
					closeAction: 'hide',
					width: 330,
					border:false,
					//frame: true,
					//height:400,
					autoHeight: true,
					autoWidth: true,
					layout: 'form',
					resizable: true,
					modal: true,
					closable: true,
					items: [{
						xtype:'form',
						layout:'vbox',
						items:{
							xtype: 'textfield',
							fieldLabel: '栏目名称',
							name: 'coluName',
							allowBlank: false
						},
						buttons:null
					}],
					buttons:[{
								text: '确认',
								iconCls: 'ensure',
								scale: 'small',
								handler:function(){

									var thisform = this.up("window").down("form").getForm();
									var coluName = thisform.findField('coluName').getValue();

									if (coluName)
									{
										/*
										var urlParams=getQueryString("tzParams");

										 urlParams = Ext.util.JSON.decode(urlParams);

										 var siteid=urlParams.comParams.siteId;
									    */
										var siteid = Ext.get("siteid").getValue();
										 var comParams = '"update":[{"typeFlag":"addColu","data":{"siteId":"'+siteid+'","menuTypeId":"'+menuTypeId+'","coluName":"'+coluName+'"}}]';

										 var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_MENU_EDITED_STD","OperateType":"U","comParams":{'+comParams+'}}';
										 Ext.Ajax.request({
											url: urlBegin,
											params: {
											tzParams:tzParams
											},
											method : 'POST',    
											success: function(response){
											var obj = Ext.util.JSON.decode(response.responseText);
											
											coluDataStore.reload();
											
											if(obj.comContent.success==true){
											   Ext.toast({
													html: '创建完成！',
													closable: false,
													align: 't',
													style: "background-color:white",
													slideInDuration: 400,
													minWidth: 400
												});
											  Ext.getCmp("menuForm").getForm().findField('menuColu').setValue(obj.comContent.data.colu_id);
											}
										},
											failure: function() {

											}
										});

										this.up("window").close();

									}
									else{
										Ext.Msg.show({
										title: '提示信息',
										msg: "栏目名称不能为空!",
										buttons: Ext.Msg.OK,
										icon: Ext.Msg.WARNING,
										buttonText: {
											ok: "关闭"
										}
										});
									}

								}
					},{
								text: '返回',
								iconCls: 'close',
								scale: 'small',
								handler:function(){
									coluForm.close();
								}
								}]
					});
				
				coluForm.show();
				}
			}]
		},
		{
			xtype: 'form',
			buttons:null,
			name:'imgForm',
			layout: 'hbox',
			bodyStyle: 'padding-left:0px;padding-top:10px;padding-right:10px;',
			items:[{
				margin:'0 0 0 0',		
				xtype:'label',
				html:'<span>菜单默认图标:</span>'
			},{
				xtype:'image',
				width:24,
				height:30,
				border:1,
				style: {
				    borderColor: '#eee'
				},
				margin:'0 0 0 8',
				src:''
			},{
				xtype:'button',
				margin:'0 0 0 20',
				text:'删除',
				listeners:{
					click:function(file, value, eOpts ){
						file.previousSibling().setSrc("");
					}
				}
			},{
	            xtype: 'fileuploadfield',
	            name: 'orguploadfile',
				margin:'0 0 0 20',
	            buttonText: '上传',
	            //msgTarget: 'side',
	            buttonOnly:true,
				listeners:{
					change:function(file, value, eOpts ){
						if(value != ""){
							var form = file.findParentByType("form").getForm();
							//获取该类

	
								//获取后缀
								var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
								if(fix.toLowerCase() == "jpg" || fix.toLowerCase() == "jpeg" || fix.toLowerCase() == "png" || fix.toLowerCase() == "gif" || fix.toLowerCase() == "bmp" || fix.toLowerCase() == "ico"){
									form.submit({
										url: TzUniversityContextPath + '/UpdServlet?filePath=website',
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
											/*
											var formP=(file.findParentByType("form").findParentByType("form"));
											var comSiteParams = formP.getForm().getValues();
												
											//站点模板id
											var siteId = comSiteParams["siteId"];
										
											//菜单类型id
											var menutypeid = comSiteParams["menutypeid"];
												
											var comParams = '{"siteId":"' + siteId + '","menutypeid":"' + menutypeid + '","path":"' + path + '"}';
											var tzParams = '{"ComID":"TZ_GD_ZDGL_COM","PageID":"TZ_ZD_CDLXSZ_STD","OperateType":"EJSON","comParams":'+comParams+'}';
											Ext.Ajax.request({
											    //url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_ATT_D.TZ_GD_ATT_FILE.FieldFormula.Iscript_menuIAttUp',
											    url: Ext.tzGetGeneralURL,
											    params: {
											        tzParams: tzParams
											    },
											    success: function(response){
											    	var responseText = eval( "(" + response.responseText + ")" );
											        if(responseText.comContent.success == 0){
											        	file.previousSibling().previousSibling().setSrc(TzUniversityContextPath + path);
											        }else{
											        	Ext.MessageBox.alert("错误", responseText.comContent.msg);        
											        }
											    }
											});
											*/
											file.previousSibling().previousSibling().setSrc(TzUniversityContextPath + path);
											//重置表单
											//form.reset();
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
						}
					}
				}
	        }]
			
		},{
			xtype: 'form',
			name:'imgForm2',
			buttons:null,
			layout: 'hbox',
			bodyStyle: 'padding-left:0px;padding-top:10px;padding-right:10px;',
			items:[{
				margin:'0 0 0 0',		
				xtype:'label',
				html:'<span>菜单激活图标:</span>'
			},{
				xtype:'image',
				width:24,
				height:30,
				border:1,
				style: {
				    borderColor: "#eee",
					backgroundColor: "#c1ddf1"
				},
				margin:'0 0 0 8',
				src:''
			},{
				xtype:'button',
				margin:'0 0 0 20',
				text:'删除',
				listeners:{
					click:function(file, value, eOpts ){
						file.previousSibling().setSrc("");
					}
				}
			},{
	            xtype: 'fileuploadfield',
				margin:'0 0 0 20',
	            name: 'orguploadfile',
	            buttonText: '上传',
	            buttonOnly:true,
				listeners:{
					change:function(file, value, eOpts ){
						if(value != ""){
							var form = file.findParentByType("form").getForm();
							//获取该类
							
								//获取后缀
								var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
								if(fix.toLowerCase() == "jpg" || fix.toLowerCase() == "jpeg" || fix.toLowerCase() == "png" || fix.toLowerCase() == "gif" || fix.toLowerCase() == "bmp" || fix.toLowerCase() == "ico"){
									form.submit({
										url: TzUniversityContextPath + '/UpdServlet?filePath=website',
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
											/*
											var comSiteParams = panel.child("form").getForm().getValues();
											//站点模板id
										
											var siteId = comSiteParams["siteId"];
											
											//菜单类型id
											var menutypeid = comSiteParams["menutypeid"];
											var comParams = '{"siteId":"' + siteId + '","menutypeid":"' + menutypeid + '","path":"' + path + '"}';
											var tzParams = '{"ComID":"TZ_GD_ZDGL_COM","PageID":"TZ_ZD_CDLXSZ_STD","OperateType":"EJSON","comParams":'+comParams+'}';
											Ext.Ajax.request({
											    //url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_ATT_D.TZ_GD_ATT_FILE.FieldFormula.Iscript_menuIAttUp',
											    url: Ext.tzGetGeneralURL,
											    params: {
											        tzParams: tzParams
											    },
											    success: function(response){
											    	var responseText = eval( "(" + response.responseText + ")" );
											        if(responseText.comContent.success == 0){
											        	file.previousSibling().previousSibling().setSrc(TzUniversityContextPath + path);
											        }else{
											        	Ext.MessageBox.alert("错误", responseText.comContent.msg);        
											        }
											    }
											});
											*/
											file.previousSibling().previousSibling().setSrc(TzUniversityContextPath + path);		
											//重置表单
											//form.reset();
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
						}
					}
				}
	        }]
			
		},{
			xtype: 'displayfield',
			name: 'menuDescr',
			fieldLabel: '功能说明',
			value:'这里是菜单的功能说明文字',
			height:300,
			width:570
		},
		{
			xtype: 'hiddenfield',
			name: 'siteId'
		},
		{
			xtype: 'hiddenfield',
			name: 'menuId'
		},
		{
			xtype: 'hiddenfield',
			name: 'menuDel'
		},
		{
			xtype: 'hiddenfield',
			name: 'menuEdit'
		},
		{
			xtype: 'hiddenfield',
			name: 'submittype'
		}],
		buttons: [{
			text: '删除',
			iconCls: 'close',
			handler: function() {
				var menuName = this.up("form").getForm().findField('menuName').getValue();
				Ext.Msg.show({
					title:'提示信息',
					message: '您确定要删除菜单【'+ menuName +'】吗？',
					buttons: Ext.Msg.YESNO,
					icon: Ext.Msg.QUESTION,
					buttonText: {
						yes: "确定",
						no: "取消"
					},
					fn: function(btn) {
						if (btn === 'yes') {
							/*
							var urlParams=getQueryString("tzParams");

							urlParams = Ext.util.JSON.decode(urlParams);

							var siteid=urlParams.comParams.siteId;
							*/
							var siteid = Ext.get("siteid").getValue();
							var menuId = Ext.getCmp("menuForm").getForm().findField('menuId').getValue();

							var comParams = '"update":[{"typeFlag":"delete","data":{"siteId":"'+siteid+'","menuId":"'+menuId+'"}}]';
							var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_MENU_EDITED_STD","OperateType":"U","comParams":{'+comParams+'}}';
							Ext.Ajax.request({
								url: urlBegin,
								params: {
								tzParams:tzParams
								},
								method : 'POST',    
								success: function(response){
									var obj = Ext.util.JSON.decode(response.responseText);
									if(obj.state.errcode=="0"){

										var menuStore2 = Ext.create('Ext.data.TreeStore', {
											root:obj.comContent.root
										});

										Ext.getCmp("menuTree").reconfigure(menuStore2);

										//Ext.getCmp("menuForm").getForm().reset();

										var formData = obj.data.data;

										thisForm = Ext.getCmp("menuForm").getForm();
										thisForm.setValues(formData);

										Ext.getCmp("menuForm").down('form[name=imgForm]').child("image").setSrc(TzUniversityContextPath + formData.menutypeimg);
								        Ext.getCmp("menuForm").down('form[name=imgForm2]').child("image").setSrc(TzUniversityContextPath + formData.menunowimg);
							
										var menuColuSelect = thisForm.findField('menuColu');
										var addColuBtns = Ext.getCmp('coluBtnGroup');
										var addColuBtn = Ext.getCmp('addcolubtn');
										var addURLFields = Ext.getCmp('linkURL');
										var menuLink = thisForm.findField('menuLink');
										var linkTarget = thisForm.findField('linkTarget');
										var menuTypeId = thisForm.findField('menuTypeId');
										addURLFields.hide();
										if(formData.addcolu=="Y"){
											menuColuSelect.show();
											addColuBtns.show();
										}else if(formData.addcolu=="URL"){
											addURLFields.show();
											menuColuSelect.hide();
											addColuBtns.hide();
										}else{
											menuColuSelect.hide();
											addColuBtns.hide();
										}
										
										/*设置是否可编辑*/
										//console.log(formData);
										if (formData.menuEdit=="N")
										{
											menuTypeId.setReadOnly(true);
											menuColuSelect.setReadOnly(true);
											addColuBtns.hide();
											menuLink.setReadOnly(true);
											linkTarget.setReadOnly(true);
										}else{
											menuTypeId.setReadOnly(false);
											menuColuSelect.setReadOnly(false);
											menuLink.setReadOnly(false);
											linkTarget.setReadOnly(false);
										}
										
										Ext.get("letf_menu").child("div").dom.innerHTML=obj.comContent.divHtml;

										Ext.toast({
											html: '已删除！',
											closable: false,
											align: 't',
											style: "background-color:white",
											slideInDuration: 400,
											minWidth: 400
										});
									}else{
										Ext.Msg.show({
													title: '提示信息',
													msg: obj.state.errdesc,
													buttons: Ext.Msg.OK,
													icon: Ext.Msg.WARNING,
													buttonText: {
														ok: "关闭"
													}
												});
									}
								},
								failure: function() {
									Ext.Msg.show({
									title: '提示信息',
									msg: "删除失败！",
									buttons: Ext.Msg.OK,
									icon: Ext.Msg.WARNING,
									buttonText: {
										ok: "关闭"
									}
									});
								}
							});
						} else if (btn === 'no') {
							//console.log('No pressed');
						} else {
							//console.log('Cancel pressed');
						} 
					}
				});
			
			}
		},
		{
			text: '保存',
			iconCls: 'save',
			handler: function() {
		var src = this.up('form').down('form[name=imgForm]').child("image").src;
		var srcNow=	this.up('form').down('form[name=imgForm2]').child("image").src;
		//表单数据
		var formParams = this.up('form').getForm().getValues();
		formParams["menutypeimg"] = src;
		formParams["menunowimg"] = srcNow;

			var comParams = '"update":[{"typeFlag":"save","data":'+Ext.JSON.encode(formParams)+'}]';
			var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_MENU_SETED_STD","OperateType":"U","comParams":{'+comParams+'}}';

			Ext.Ajax.request({
				url: urlBegin,
				params: {
					tzParams:tzParams
				},
				method : 'POST',    
				 success: function(response){
					var obj = Ext.util.JSON.decode(response.responseText);
					if(obj.state.errcode=="0"){
						var menuStore2 = Ext.create('Ext.data.TreeStore', {
									root:obj.comContent.root
								});
						Ext.getCmp("menuTree").reconfigure(menuStore2);

						Ext.getCmp("menuForm").getForm().findField('menuId').setValue(obj.menuId);
						Ext.get("letf_menu").child("div").dom.innerHTML=obj.comContent.divHtml;
						Ext.toast({
								html: '保存成功！',
								closable: false,
								align: 't',
								style: "background-color:white",
								slideInDuration: 400,
								minWidth: 400
							});
					}else{
						Ext.Msg.show({
											title: '保存失败',
											msg: obj.state.errdesc,
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.WARNING,
											buttonText: {
												ok: "关闭"
											}
										});
					}
				},
				failure: function() {
					Ext.Msg.show({
						title: '提示信息',
						msg: "保存失败！",
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING,
						buttonText: {
							ok: "关闭"
						}
					});
				}
			});
			
			}
		},
		{
			text: '确定',
			iconCls: 'ensure',
			handler: function() {

			var src = this.up('form').down('form[name=imgForm]').child("image").src;
		var srcNow=	this.up('form').down('form[name=imgForm2]').child("image").src;
		//表单数据
		var formParams = this.up('form').getForm().getValues();
		formParams["menutypeimg"] = src;
		formParams["menunowimg"] = srcNow;

			var comParams = '"update":[{"typeFlag":"save","data":'+Ext.JSON.encode(formParams)+'}]';
			var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_MENU_SETED_STD","OperateType":"U","comParams":{'+comParams+'}}';

			Ext.Ajax.request({
				url: urlBegin,
				params: {
					tzParams:tzParams
				},
				method : 'POST',    
				 success: function(response){
					var obj = Ext.util.JSON.decode(response.responseText);
					if(obj.state.errcode=="0"){
						var menuStore2 = Ext.create('Ext.data.TreeStore', {
									root:obj.comContent.root
								});
						Ext.getCmp("menuTree").reconfigure(menuStore2);
						
						Ext.getCmp("menuForm").getForm().findField('menuId').setValue(obj.menuId);
						Ext.get("letf_menu").child("div").dom.innerHTML=obj.comContent.divHtml;

						Ext.toast({
								html: '保存成功！',
								closable: false,
								align: 't',
								style: "background-color:white",
								slideInDuration: 400,
								minWidth: 400
							});
					}else{
						Ext.Msg.show({
											title: '保存失败',
											msg: obj.state.errdesc,
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.WARNING,
											buttonText: {
												ok: "关闭"
											}
										});
					}
				},
				failure: function() {
					Ext.Msg.show({
						title: '提示信息',
						msg: "保存失败！",
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING,
						buttonText: {
							ok: "关闭"
						}
					});
				}
			});
			this.up('form').up("window").close();

			}
		},
		{
			text: '关闭',
			iconCls: 'close',
			handler: function() {
				this.up('form').up("window").close();
			}
		}],
		listeners: {
			boxready:function(thisCmp,layout, eOpts ){

			/*
			  var urlParams=getQueryString("tzParams");

			 urlParams = Ext.util.JSON.decode(urlParams);

			 var siteid=urlParams.comParams.siteId;
			*/
			var siteid = Ext.get("siteid").getValue();
			 var menuid= "";

			 var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_MENU_EDITED_STD","OperateType":"QF","comParams":{"siteId":"'+siteid+'","menuId":"'+menuid+'"}}';
			 
			 Ext.Ajax.request({
					url: urlBegin,
					params: {
							tzParams: tzParams
					},
					success: function(response, opts) {

						var responseData = Ext.JSON.decode(response.responseText);

						var formData = responseData.comContent.data;
						
						var thisForm = thisCmp.getForm();
						thisForm.setValues(formData);

						thisCmp.down('form[name=imgForm]').child("image").setSrc(TzUniversityContextPath + formData.menutypeimg);
						thisCmp.down('form[name=imgForm2]').child("image").setSrc(TzUniversityContextPath + formData.menunowimg);
						
						var menuColuSelect = thisForm.findField('menuColu');
						var addColuBtns = Ext.getCmp('coluBtnGroup');
						var addColuBtn = Ext.getCmp('addcolubtn');
						var addURLFields = Ext.getCmp('linkURL');
						var menuLink = thisForm.findField('menuLink');
						var linkTarget = thisForm.findField('linkTarget');
						var menuTypeId = thisForm.findField('menuTypeId');
						addURLFields.hide();
						if(formData.addcolu=="Y"){
							menuColuSelect.show();
							addColuBtns.show();
						}else if(formData.addcolu=="URL"){
							addURLFields.show();
							menuColuSelect.hide();
							addColuBtns.hide();
						}else{
							menuColuSelect.hide();
							addColuBtns.hide();
						}
						
						/*设置是否可编辑*/
						//console.log(formData);
						if (formData.menuEdit=="N")
						{
							menuTypeId.setReadOnly(true);
							menuColuSelect.setReadOnly(true);
							addColuBtns.hide();
							menuLink.setReadOnly(true);
							linkTarget.setReadOnly(true);
						}else{
							menuTypeId.setReadOnly(false);
							menuColuSelect.setReadOnly(false);
							menuLink.setReadOnly(false);
							linkTarget.setReadOnly(false);
						}
					},
					failure: function(response, opts) {
					
					}
			  });

			}
		}
	});


	/*编辑菜单时的弹出窗口*/
	var menuWindow = Ext.widget('window', {
		title: '编辑区域',
		closeAction: 'hide',
		width: 1024,
		height: 760,
		autoHeight: true,
		autoWidth: true,
		layout: 'border',
		resizable: false,
		modal: true,
		closable: true,
		items: [menuTree, menuForm]
	});

	var xp = Ext.get(document).getScrollLeft() + 150;
	var yp = Ext.get(document).getScrollTop() + 200;
	editPanel = Ext.create('Ext.panel.EditWinPanel');

	Ext.define("ElBindEvent", {

		bindAdd: function() {
			var me = this;

			Ext.each(Ext.query(".ds-bar-add"),
			function(cel, index) {
				var cel2 = Ext.get(cel);
				cel2.on('click',
				function(e) {

					var pel2 = cel2.parent().parent().parent();

					if (Ext.isEmpty(pel2)) {

					} else {
						
						if (!addWindow) {
							
							addWindow.setPosition(xp, yp);
							addWindow.show();
						} else {
							
/*
var urlParams=getQueryString("tzParams");
urlParams = Ext.util.JSON.decode(urlParams);

var siteid=urlParams.comParams.siteId;
*/
var siteid = Ext.get("siteid").getValue();
var areaZone=Ext.get(pel2).getAttribute("area-postion");

if (!areaZone)
{
	areaZone="";
}

var tzParams = '{"ComID":"TZ_AREA_ADD_COM","PageID":"TZ_AREA_ADD_STD","OperateType":"QF","comParams":{"typeflg":"all","siteId":"'+siteid+'","areaZone":"'+areaZone+'"}}';

var store = Ext.create('Ext.data.Store', {
	pageSize: 50,
	model: 'ForumThread',
	remoteSort: true,
	autoLoad: true,
	proxy: {
		type: 'ajax',
		//url: '/tranzvision/kitchensink/app/view/website/set/json/areaData.json',
		url: urlBegin + '?tzParams='+tzParams,
		reader: {
			//root: 'topics',
			root: 'comContent.topics'
		},
		// sends single sort as multi parameter
		simpleSortMode: true
	},
	sorters: [{
		property: 'lastpost',
		direction: 'DESC'
	}]
});

function renderImage(value, p, record) {
	//return Ext.String.format('<img width="100px" height="50px" src=/{0}>', record.data.areaImg);
	return Ext.String.format('<span style="font-size:20px;font-weight:600;">{0}</span>','&middot;');
}

function renderTopic(value, p, record) {
	return Ext.String.format('<b>{0}</b>{1}', value, record.data.areaDesc, record.getId(), record.data.forumid);
}

function renderLast(value, p, r) {
	return Ext.String.format('{0}<br/>by {1}', Ext.Date.dateFormat(value, 'M j, Y, g:i a'), r.get('lastposter'));
}

function renderAddButton(value, p, record){
	return Ext.String.format('<button class="addareabtn x-btn x-unselectable x-btn-default-small" style="min-width: 75px; height:30px; right: auto; margin: 0; color: #fff;text-align: center;">{0}</button>', '选&nbsp;&nbsp;择');
}

var gridPanel = Ext.create('Ext.grid.Panel', {
	id:"gridPanel",
	region: 'center',
	store: store,
	hideHeaders: true,
	columns: [{
		dataIndex: 'areaImg',
		width: 20,
		renderer: renderImage,
		sortable: false
	},
	{
		id: 'topic',
		dataIndex: 'areaName',
		flex: 1,
		renderer: renderTopic,
		sortable: false
	},
	{
		/*menuDisabled: true,*/
		/*xtype: 'actioncolumn',*/
		dataIndex: 'doSelect',
		renderer: renderAddButton,
		css: "height:50px;",
		sortable: false
		/*
		items: [{
			xtype: 'button',
			text: '添加',
			handler: function(grid, rowIndex, colIndex) {
				var urlParams=getQueryString("tzParams");
				urlParams = Ext.util.JSON.decode(urlParams);
				var siteid=urlParams.comParams.siteId;

				var areaId;
				var rec = grid.getStore().getAt(rowIndex);
				areaId= rec.get('areaId');
				var tzParams = '{"ComID":"TZ_AREA_ADD_COM","PageID":"TZ_AREA_ADD_STD","OperateType":"QF","comParams":{"typeflg":"add","siteId":"'+siteid+'","areaId":"'+areaId+'"}}';
				
				Ext.Ajax.request({
							url: urlBegin,
							params: {
							tzParams:tzParams
							},
							method : 'POST',    
							success: function(response){
							var obj = Ext.util.JSON.decode(response.responseText);
								pel2 = pel2.insertHtml('afterEnd',obj.comContent,true);
								var innerHtml = pel2.dom.innerHTML;
								var scripts = pel2.dom.getElementsByTagName("script");
								if (scripts)
								{
									var script;
									for (var i = 0; i < scripts.length; i++) {
										var script = scripts[0].text.replace(/(^\s*)|(\s*$)/g, "");
										eval(script);
									}
								}
								pel2.update(innerHtml);
								me.bindBasic();
								addWindow.close();
							}
				});

			}
		}]*/
	}]
});
gridPanel.on("cellclick", function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts){
    var addareabtn = e.getTarget('.addareabtn');
    if (addareabtn) {
      //console.log("click addareabtn");
	  /*
	    var urlParams=getQueryString("tzParams");
		urlParams = Ext.util.JSON.decode(urlParams);
		var siteid=urlParams.comParams.siteId;
		*/
	var siteid = Ext.get("siteid").getValue();
		var areaId;
		var rec = grid.getStore().getAt(rowIndex);
		areaId= rec.get('areaId');
		var tzParams = '{"ComID":"TZ_AREA_ADD_COM","PageID":"TZ_AREA_ADD_STD","OperateType":"QF","comParams":{"typeflg":"add","siteId":"'+siteid+'","areaId":"'+areaId+'"}}';
		
		Ext.Ajax.request({
					url: urlBegin,
					params: {
					tzParams:tzParams
					},
					method : 'POST',    
					success: function(response){

					var obj = Ext.util.JSON.decode(response.responseText);
							
					 pel2 = pel2.insertHtml('afterEnd',obj.comContent);

					// pel2 = Ext.DomHelper.insertHtml('afterEnd',pel2.dom,obj.comContent);
					
						/*var innerHtml = pel2.innerHTML;
						//var innerHtml = pel2.innerHTML;

						var scripts = pel2.getElementsByTagName("script");

						if (scripts)
						{
							var script;
							for (var i = 0; i < scripts.length; i++) {
								var script = scripts[0].text.replace(/(^\s*)|(\s*$)/g, "");
								eval(script);
							}
						}
						*/
						var i = 0 ;
						
						while (i < obj.comContent.length)
						{
							var begin = (obj.comContent).indexOf("<script>",i);
							var end;
							if (begin>0)
							{
								end = (obj.comContent).indexOf("</script>",begin);
								var script =  (obj.comContent).substring(begin,end+8);
								eval(script);
								i =end+8;
							}else{
								break;
							}
						
						}
						

						//pel2.update(innerHtml);
						me.bindBasic();
						addWindow.close();
					}
		});
    }
});
Ext.getCmp("gridPanel").reconfigure(store);



							addWindow.items.removeAll();
							//addWindow.add(panel2, gridPanel);
							addWindow.add(gridPanel);

							xp = Ext.get(document).getScrollLeft() + 300;
							//yp = Ext.get(document).getScrollTop() + 200;
							yp = Ext.get(document).getScrollTop() + (document.body.clientHeight/2 - addWindow.height/2);
							yp = yp>0?yp:(Ext.get(document).getScrollTop() + 50);
							addWindow.setPosition(xp, yp);
							addWindow.show();
						}
						
						me.bindBasic();
						return;
					}
				});
			});
		},
		bindPoint:function(){
			var me = this;
			Ext.each(Ext.query(".baracts"),
			function(cel, index) {
				var cel2 = Ext.get(cel);
				cel2.on('dblclick',
				function(e) {

					var pel2 = cel2.parent();

					if (Ext.isEmpty(pel2)) {

					} else {
						
						if (Ext.get(pel2).getAttribute("id") == "registe-area")
						{
							

							window.open( TzUniversityContextPath + "/index#"+ Ext.get("jgid").getValue() + editEnrollFieldComponentID);
							return;
						}

						if (Ext.get(pel2).getAttribute("id") == "letf_menu") {

							if (!menuWindow) {
								//console.log('bindPoint1:xp'+xp+',yp'+yp);
								menuWindow.setPosition(xp, yp);
								menuWindow.show();
							} else {

								
								//menuWindow.items.removeAll();
								//menuWindow.removeAll();
								//menuWindow.add(menuTree, menuForm);
								xp = Ext.get(document).getScrollLeft() + 300;
								yp = Ext.get(document).getScrollTop() + (document.body.clientHeight/2 - menuWindow.height/2);
								yp = yp>0?yp:(Ext.get(document).getScrollTop() + 50);
								//console.log('bindPoint2:xp'+xp+',yp'+yp);
								menuWindow.setPosition(xp, yp);
								menuWindow.show();
							}

						} else {
							if (!editWindow) {
								
								editWindow.setPosition(xp, yp);
								editWindow.show();
							} else {
/*
								var urlParams=getQueryString("tzParams");

									urlParams = Ext.util.JSON.decode(urlParams);

								var siteid=urlParams.comParams.siteId;
*/
								var siteid = Ext.get("siteid").getValue();
								var areaid=	Ext.get(pel2).getAttribute("area-id");

								if (!areaid)
								{
									areaid="";
								}
								var areaZone=Ext.get(pel2).getAttribute("area-postion");
								if (!areaZone)
								{
									areaZone="";
								}
								var areaType=Ext.get(pel2).getAttribute("area-type");
								if (!areaType)
								{
									areaType="";
								}
								var areaClass = Ext.get(pel2).getAttribute("class");
								if (!areaClass)
								{
									areaClass="";
								}

								var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_AREA_SETED_STD","OperateType":"QF","comParams":{"siteId":"'+siteid+'","areaId":"'+areaid+'","areaZone":"'+areaZone+'","areaType":"'+areaType+'","areaClass":"'+areaClass+'"}}';
								Ext.Ajax.request({
									url: urlBegin,
									params: {
										tzParams: tzParams
									},
									success: function(response, opts) {
										
										var obj = Ext.util.JSON.decode(response.responseText);
										 obj = obj.comContent;

										if (obj.success) {
											Ext.get(pel2).set({"area-id":obj.areaId});
											var itemsContainer = Ext.create('Ext.container.Container', {
												layout:{type:'vbox',align: 'stretch'},
												items: []
											});

											Ext.each(obj.items.container,
											function(el, index) {
												var fieldContainer = Ext.create('Ext.container.Container', {
													layout:{type:'vbox',align: 'stretch'},
													items: []
												});

												Ext.each(el.items,
												function(ell, indexx) {
													switch (ell.xtype) {
													case 'checkbox':
														var field = Ext.create('Ext.form.Checkbox', {
															labelStyle : "text-align:center;width:80;",
															fieldLabel: ell.fieldLabel,
															
															name: ell.name,
															inputValue: ell.inputValue,
															//选中的值
															uncheckedValue: ell.uncheckedValue

														});
														fieldContainer.items.add(field);
														break;
													case 'checkboxgroup':
														var field = Ext.create('Ext.form.CheckboxGroup', {
															labelStyle : "text-align:center;width:80;",
															fieldLabel: ell.fieldLabel,
															columns: ell.columns,
															items: ell.items
														});
														fieldContainer.items.add(field);
														break;
													case 'combo':
														var field = Ext.create('Ext.form.ComboBox', {
															labelStyle : "text-align:center;width:80;",
															fieldLabel: ell.fieldLabel,
															name: ell.name,
															store: new Ext.data.SimpleStore({
																fields: ell.fields,
																data: ell.data
															}),
															displayField: ell.displayField,
															valueField: ell.valueField,
															value:ell.value,
															readOnly:ell.readOnly,
															listeners:ell.listeners
														});
														fieldContainer.items.add(field);
														break;
													case 'displayfield':
														var field = Ext.create('Ext.form.DisplayField', {
															labelStyle : "text-align:center;width:80;",
															fieldLabel: ell.fieldLabel,
															name: ell.name,
															value:ell.value
														});
														fieldContainer.items.add(field);
														break;
													case 'datefield':
														var field = Ext.create('Ext.form.DateField', {
															labelStyle : "text-align:center;width:80;",
															fieldLabel: ell.fieldLabel,
															name: ell.name,
															minValue: ell.minValue,
															//选中的值
															maxValue: ell.maxValue,
															format: ell.format
														});
														fieldContainer.items.add(field);
														break;
													case 'timefield':
														var field = Ext.create('Ext.form.TimeField', {
															labelStyle : "text-align:center;width:80;",
															fieldLabel: ell.fieldLabel,
															name: ell.name,
															minValue: ell.minValue,
															//选中的值
															maxValue: ell.maxValue,
															increment: ell.increment,
															format: ell.format
														});
														fieldContainer.items.add(field);
														break;

													case 'numberfield':
														var field = Ext.create('Ext.form.NumberField', {
															labelStyle : "text-align:center;width:80;",
															fieldLabel: ell.fieldLabel,
															name: ell.name,
															minValue: ell.minValue,
															//选中的值
															maxValue: ell.maxValue
														});
														fieldContainer.items.add(field);
														break;
													case 'textfield':
														var field = Ext.create('Ext.form.TextField', {
															labelStyle : "text-align:center;width:80;",
															fieldLabel: ell.fieldLabel,
															name: ell.name,
															value:ell.value,
															fieldStyle:ell.fieldStyle,
															listeners:ell.listeners
														});
														fieldContainer.items.add(field);
														break;
													case 'radio':
														var field = Ext.create('Ext.form.Radio', {
															labelStyle : "text-align:center;width:80;",
															fieldLabel: ell.fieldLabel,
															name: ell.name,
															inputValue: ell.inputValue,
															//选中的值
															uncheckedValue: ell.uncheckedValue
														});
														fieldContainer.items.add(field);
														break;
													case 'radiogroup':
														var field = Ext.create('Ext.form.RadioGroup', {
															labelStyle : "text-align:center;width:80;",
															fieldLabel: ell.fieldLabel,
															items: ell.items
														});
														fieldContainer.items.add(field);
														break;
													case 'textarea':
														var field = Ext.create('Ext.form.TextArea', {
															labelStyle : "text-align:center;width:80;",
															fieldLabel: ell.fieldLabel,
															grow: ell.grow,
															name: ell.name
														});
														fieldContainer.items.add(field);
														break;
													case 'colorpicker':
														var field = Ext.create('Ext.picker.Color', {
															labelStyle : "text-align:center;width:80;",
															fieldLabel: ell.fieldLabel,
															name: ell.name,
															hidden: false,
															style:ell.style,
															listeners: ell.listeners
														});
														fieldContainer.items.add(field);
														break;
													case 'filefield':
														var field = Ext.create('Ext.form.File', {
															labelStyle : "text-align:center;width:80;",
															fieldLabel: ell.fieldLabel,
															name: ell.name,
															listeners:ell.listeners,
															buttonText: ell.buttonText
														});
														fieldContainer.items.add(field);
														break;
													case 'sliderfield':
														var field = Ext.create('Ext.slider.Single', {
															labelStyle : "text-align:center;width:80;",
															width: ell.width,
															minValue: ell.minValue,
															maxValue: ell.maxValue,
															style:ell.style,
															useTips:false,
															listeners:ell.listeners
														});
														fieldContainer.items.add(field);
														break;
													case 'fieldcontainer':
														var field = Ext.create('Ext.form.FieldContainer', {
															labelStyle : "text-align:center;width:80;",
															fieldLabel: ell.fieldLabel,
															layout:'hbox',
															items: ell.items,
															listeners:ell.listeners
														});
														fieldContainer.items.add(field);
														break;
													case 'box':
														var field = Ext.create('Ext.Img', {
															itemId:ell.itemId,
															width:ell.width,
															height:ell.height
														});
														fieldContainer.items.add(field);
														break;
													case 'hiddenfield':
														var field = Ext.create('Ext.form.Hidden', {
															labelStyle : "text-align:center;width:80;",
															name: ell.name,
															value:ell.value
														});
														fieldContainer.items.add(field);
														break;
														
													case 'ueditor':
														var field = Ext.create('Ext.ux.Ueditor', {
															labelStyle : "text-align:center;width:80;",
															name: ell.name,
															fieldLabel: ell.fieldLabel,
															height:ell.height,
															width:ell.width,
															listeners:ell.listeners
														});
														fieldContainer.items.add(field);
														break;
														
														case 'colorfield':
															var field = Ext.create('Ext.ux.colorpick.Field', {
																labelStyle : "text-align:center;width:80;",
																fieldLabel: ell.fieldLabel,
																id: ell.id,
																name: ell.name,
																//bind: '{color}',
																listeners: ell.listeners
															});
															fieldContainer.items.add(field);
															isColorPicker = true;
															break;
														case 'colorselector':
															var field = Ext.create('Ext.ux.colorpick.Selector', {
																hidden: ell.hidden,
																flex: ell.flex
																/*bind: {
																	value: '{color}',
																	visible: '{full}'
																}*/
															});
															fieldContainer.items.add(field);
															break;
														case 'displayfield':
															var field = Ext.create('Ext.form.field.Display', {
																fieldLabel: ell.fieldLabel,
																labelStyle : "text-align:center;width:80;",
																value: ell.value
															});
															fieldContainer.items.add(field);
															break;
														case 'image':
															var field = Ext.create('Ext.Img', {
																style: ell.style,
																itemId: ell.itemId,
																maxHeight: ell.maxHeight,
																maxWidth: ell.maxWidth,
																listeners: ell.listeners
															});
															fieldContainer.items.add(field);
															break;
													
													}			
												});
												itemsContainer.items.add(fieldContainer);
											});
										var itemsTabpanel;
											if (obj.items.tabpanel)
											{
												 itemsTabpanel = Ext.create('Ext.tab.Panel', {
												plain: true,
												activeTab: 0,
												items: []
											});
											Ext.each(obj.items.tabpanel,
											function(el, index) {

												var fieldTabpanel = Ext.create('Ext.panel.Panel', {
													title: el.title,
													items: []
												});
												Ext.each(el.items,
												function(ell, indexx) {
													switch (ell.xtype) {
													case 'combo':
														var field = Ext.create('Ext.form.ComboBox', {
															fieldLabel: ell.fieldLabel,
															name: ell.name,
															store: new Ext.data.SimpleStore({
																fields: ell.fields,
																data: ell.data
															}),
															displayField: ell.displayField,
															valueField: ell.valueField
														});
														fieldTabpanel.items.add(field);
														break;
													}
												});
												itemsTabpanel.add(fieldTabpanel);
											});

											}
											
											editPanel.removeAll();
											//editPanel.items.removeAll();
											editPanel.items.add(itemsContainer);
											editPanel.add(itemsTabpanel);
					
											

											editWindow.items.removeAll();
											editWindow.add(editPanel);
											
											xp = Ext.get(document).getScrollLeft() + 300;
											yp = Ext.get(document).getScrollTop() + (document.body.clientHeight/2 - editWindow.height/2);
											yp = yp>0?yp:(Ext.get(document).getScrollTop() + 50);
											editWindow.setPosition(xp, yp);

											editWindow.show();
										}

									},
									failure: function(response, opts) {
										

										Ext.Msg.show({
											title: '提示信息',
											msg: '获取数据失败！',
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.WARNING,
											buttonText: {
												ok: "确定"
											}
										});

									}
								});
							}
						}
						
						me.bindBasic();
						return ;
					}
				});
			});
		},
		bindEdit: function(){
			var me = this;
			Ext.each(Ext.query(".ds-bar-edit"),
			function(cel, index) {
				var cel2 = Ext.get(cel);
				cel2.on('click',
				function(e) {

					var pel2 = cel2.parent().parent();

					if (Ext.isEmpty(pel2)) {

					} else {
						/*信息注册区域编辑*/
						if (Ext.get(pel2).getAttribute("id") == "registe-area")
						{
							
							//window.open(TzUniversityContextPath + "/index#"+ Ext.get("jgid").getValue() + editEnrollFieldComponentID);
							window.open(TzUniversityContextPath + "/index?siteid="+Ext.get("siteid").getValue()+"#"+ Ext.get("jgid").getValue() + "_A00000201");
							return;
						}

                        /*个人信息显示区域 编辑部分--------------待做*/
                        if(Ext.get(pel2).getAttribute("id") == "perInfo"){
                            //window.open(TzUniversityContextPath + "/index#"+ Ext.get("jgid").getValue() + editPersonInfoComponentID);
                        	window.open(TzUniversityContextPath + "/index?siteid="+Ext.get("siteid").getValue()+"#"+ Ext.get("jgid").getValue() + "_A00000201");
                        	return;
                        }

						if (Ext.get(pel2).getAttribute("id") == "letf_menu") {

							if (!menuWindow) {
								//console.log('bindEdit1:xp'+xp+',yp'+yp);
								menuWindow.setPosition(xp, yp);
								menuWindow.show();
							} else {
								
								
								//menuWindow.items.removeAll();
								//menuWindow.removeAll();
								//menuWindow.add(menuTree, menuForm);
								xp = Ext.get(document).getScrollLeft() + 150;
								//console.log(menuWindow);
								yp = Ext.get(document).getScrollTop() + (document.body.clientHeight/2 - menuWindow.height/2);
								yp = yp>0?yp:(Ext.get(document).getScrollTop() + 50);
								//console.log('bindEdit2:xp'+xp+',yp'+yp);
								menuWindow.setPosition(xp, yp);
								menuWindow.show();
							}

						} else {
							if (!editWindow) {
								
								editWindow.setPosition(xp, yp);
								editWindow.show();
							} else {
								var isColorPicker = false;
								/*
								var urlParams=getQueryString("tzParams");

									urlParams = Ext.util.JSON.decode(urlParams);

								var siteid=urlParams.comParams.siteId;
*/
								var siteid = Ext.get("siteid").getValue();
								var areaid=	Ext.get(pel2).getAttribute("area-id");

								if (!areaid)
								{
									areaid="";
								}
								var areaZone=Ext.get(pel2).getAttribute("area-postion");
								if (!areaZone)
								{
									areaZone="";
								}
								var areaType=Ext.get(pel2).getAttribute("area-type");
								if (!areaType)
								{
									areaType="";
								}
								var areaClass = Ext.get(pel2).getAttribute("class");
								if (!areaClass)
								{
									areaClass="";
								}

							var eleid=Ext.get(pel2).getAttribute("id");

								var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_AREA_SETED_STD","OperateType":"QF","comParams":{"siteId":"'+siteid+'","areaId":"'+areaid+'","areaZone":"'+areaZone+'","areaType":"'+areaType+'","areaClass":"'+areaClass+'","eleid":"'+eleid+'"}}';
								Ext.Ajax.request({
									url: urlBegin,
									params: {
										tzParams: tzParams
									},
									success: function(response, opts) {
										
										var obj = Ext.util.JSON.decode(response.responseText);
										 obj = obj.comContent;

										if (obj.success) {
											Ext.get(pel2).set({"area-id":obj.areaId});
											var itemsContainer = Ext.create('Ext.container.Container', {
												layout:{type:'vbox',align: 'stretch'},
												items: []
											});

											Ext.each(obj.items.container,
											function(el, index) {
												var fieldContainer = Ext.create('Ext.container.Container', {
													layout:{type:'vbox',align: 'stretch'},
													items: []
												});

												Ext.each(el.items,
												function(ell, indexx) {
													switch (ell.xtype) {
														case 'checkbox':
															var field = Ext.create('Ext.form.Checkbox', {
																labelStyle : "text-align:center;width:80;",
																fieldLabel: ell.fieldLabel,
																
																name: ell.name,
																inputValue: ell.inputValue,
																//选中的值
																uncheckedValue: ell.uncheckedValue

															});
															fieldContainer.items.add(field);
															break;
														case 'checkboxgroup':
															var field = Ext.create('Ext.form.CheckboxGroup', {
																labelStyle : "text-align:center;width:80;",
																fieldLabel: ell.fieldLabel,
																columns: ell.columns,
																items: ell.items
															});
															fieldContainer.items.add(field);
															break;
														case 'combo':
															var field = Ext.create('Ext.form.ComboBox', {
																labelStyle : "text-align:center;width:80;",
																fieldLabel: ell.fieldLabel,
																name: ell.name,
																store: new Ext.data.SimpleStore({
																	fields: ell.fields,
																	data: ell.data
																}),
																displayField: ell.displayField,
																valueField: ell.valueField,
																value:ell.value,
																readOnly:ell.readOnly,
																listeners:ell.listeners,
																hidden:ell.id=="_selectColu"?(ell._areaType=="XL"?false:true):false
															});
															fieldContainer.items.add(field);
															break;
														case 'displayfield':
															var field = Ext.create('Ext.form.DisplayField', {
																labelStyle : "text-align:center;width:80;",
																fieldLabel: ell.fieldLabel,
																name: ell.name,
																value:ell.value
															});
															fieldContainer.items.add(field);
															break;
														case 'datefield':
															var field = Ext.create('Ext.form.DateField', {
																labelStyle : "text-align:center;width:80;",
																fieldLabel: ell.fieldLabel,
																name: ell.name,
																minValue: ell.minValue,
																//选中的值
																maxValue: ell.maxValue,
																format: ell.format
															});
															fieldContainer.items.add(field);
															break;
														case 'timefield':
															var field = Ext.create('Ext.form.TimeField', {
																labelStyle : "text-align:center;width:80;",
																fieldLabel: ell.fieldLabel,
																name: ell.name,
																minValue: ell.minValue,
																//选中的值
																maxValue: ell.maxValue,
																increment: ell.increment,
																format: ell.format
															});
															fieldContainer.items.add(field);
															break;

														case 'numberfield':
															var field = Ext.create('Ext.form.NumberField', {
																labelStyle : "text-align:center;width:80;",
																fieldLabel: ell.fieldLabel,
																name: ell.name,
																minValue: ell.minValue,
																//选中的值
																maxValue: ell.maxValue
															});
															fieldContainer.items.add(field);
															break;
														case 'textfield':
															var field = Ext.create('Ext.form.TextField', {
																labelStyle : "text-align:center;width:80;",
																fieldLabel: ell.fieldLabel,
																name: ell.name,
																value:ell.value,
																fieldStyle:ell.fieldStyle,
																listeners:ell.listeners
															});
															fieldContainer.items.add(field);
															break;
														case 'radio':
															var field = Ext.create('Ext.form.Radio', {
																labelStyle : "text-align:center;width:80;",
																fieldLabel: ell.fieldLabel,
																name: ell.name,
																inputValue: ell.inputValue,
																//选中的值
																uncheckedValue: ell.uncheckedValue
															});
															fieldContainer.items.add(field);
															break;
														case 'radiogroup':
															var field = Ext.create('Ext.form.RadioGroup', {
																labelStyle : "text-align:center;width:80;",
																fieldLabel: ell.fieldLabel,
																items: ell.items
															});
															fieldContainer.items.add(field);
															break;
														case 'textarea':
															var field = Ext.create('Ext.form.TextArea', {
																labelStyle : "text-align:center;width:80;",
																fieldLabel: ell.fieldLabel,
																grow: ell.grow,
																name: ell.name
															});
															fieldContainer.items.add(field);
															break;
														case 'colorpicker':
															var field = Ext.create('Ext.picker.Color', {
																labelStyle : "text-align:center;width:80;",
																fieldLabel: ell.fieldLabel,
																name: ell.name,
																hidden: false,
																style:ell.style,
																listeners: ell.listeners
															});
															fieldContainer.items.add(field);
															break;
														case 'filefield':
															var field = Ext.create('Ext.form.File', {
																labelStyle : "text-align:center;width:80;",
																fieldLabel: ell.fieldLabel,
																name: ell.name,
																listeners:ell.listeners,
																buttonText: ell.buttonText
															});
															fieldContainer.items.add(field);
															break;
														case 'sliderfield':
															var field = Ext.create('Ext.slider.Single', {
																labelStyle : "text-align:center;width:80;",
																width: ell.width,
																minValue: ell.minValue,
																maxValue: ell.maxValue,
																style:ell.style,
																useTips:false,
																listeners:ell.listeners
															});
															fieldContainer.items.add(field);
															break;
														case 'fieldcontainer':
															var field = Ext.create('Ext.form.FieldContainer', {
																labelStyle : "text-align:center;width:80;",
																fieldLabel: ell.fieldLabel,
																layout:'hbox',
																items: ell.items,
																listeners:ell.listeners
															});
															fieldContainer.items.add(field);
															break;
														case 'box':
															var field = Ext.create('Ext.Img', {
																itemId:ell.itemId,
																width:ell.width,
																height:ell.height
															});
															fieldContainer.items.add(field);
															break;
														case 'hiddenfield':
															var field = Ext.create('Ext.form.Hidden', {
																labelStyle : "text-align:center;width:80;",
																name: ell.name,
																value:ell.value
															});
															fieldContainer.items.add(field);
															break;
															
														case 'ueditor':
															var field = Ext.create('Ext.ux.Ueditor', {
																labelStyle : "text-align:center;width:80;",
																name: ell.name,
																fieldLabel: ell.fieldLabel,
																height:ell.height,
																width:ell.width,
																listeners:ell.listeners
															});
															fieldContainer.items.add(field);
															break;
														case 'colorfield':
															var field = Ext.create('Ext.ux.colorpick.Field', {
																labelStyle : "text-align:center;width:80;",
																fieldLabel: ell.fieldLabel,
																id: ell.id,
																name: ell.name,
																//bind: '{color}',
																listeners: ell.listeners
															});
															fieldContainer.items.add(field);
															isColorPicker = true;
															break;
														case 'colorselector':
															var field = Ext.create('Ext.ux.colorpick.Selector', {
																hidden: ell.hidden,
																flex: ell.flex
																/*bind: {
																	value: '{color}',
																	visible: '{full}'
																}*/
															});
															fieldContainer.items.add(field);
															break;
														case 'displayfield':
															var field = Ext.create('Ext.form.field.Display', {
																fieldLabel: ell.fieldLabel,
																labelStyle : "text-align:center;width:80;",
																value: ell.value
															});
															fieldContainer.items.add(field);
															break;
														case 'image':
															var field = Ext.create('Ext.Img', {
																style: ell.style,
																itemId: ell.itemId,
																maxHeight: ell.maxHeight,
																maxWidth: ell.maxWidth,
																listeners: ell.listeners
															});
															fieldContainer.items.add(field);
															break;
													}			
												});
												itemsContainer.items.add(fieldContainer);
											});
										var itemsTabpanel;
											if (obj.items.tabpanel)
											{
												 itemsTabpanel = Ext.create('Ext.tab.Panel', {
												plain: true,
												activeTab: 0,
												items: []
											});
											Ext.each(obj.items.tabpanel,
											function(el, index) {

												var fieldTabpanel = Ext.create('Ext.panel.Panel', {
													title: el.title,
													items: []
												});
												Ext.each(el.items,
												function(ell, indexx) {
													switch (ell.xtype) {
													case 'combo':
														var field = Ext.create('Ext.form.ComboBox', {
															fieldLabel: ell.fieldLabel,
															name: ell.name,
															store: new Ext.data.SimpleStore({
																fields: ell.fields,
																data: ell.data
															}),
															displayField: ell.displayField,
															valueField: ell.valueField
														});
														fieldTabpanel.items.add(field);
														break;
													}
												});
												itemsTabpanel.add(fieldTabpanel);
											});

											}
											
											editPanel.removeAll();
											
											//editPanel.viewModel = {data:{color: '#0f0',full: false}};
											
											//editPanel.items.removeAll();
											editPanel.items.add(itemsContainer);
											editPanel.add(itemsTabpanel);
					

											editWindow.items.removeAll();
											editWindow.add(editPanel);
											
											xp = Ext.get(document).getScrollLeft() + 300;
											//yp = Ext.get(document).getScrollTop() + 200;
											//console.log(editWindow);
											yp = Ext.get(document).getScrollTop() + (document.body.clientHeight/2 - editWindow.height/2);
											//console.log('yp='+yp);
											yp = yp>0?yp:(Ext.get(document).getScrollTop() + 50);
											editWindow.setPosition(xp, yp);

											editWindow.show();
											
											
										}

									},
									failure: function(response, opts) {
										

										Ext.Msg.show({
											title: '提示信息',
											msg: '获取数据失败！',
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.WARNING,
											buttonText: {
												ok: "确定"
											}
										});

									}
								});
							}
						}
						
						me.bindBasic();
						return ;
					}
				});
			});
		},
		bindDel: function() {
			var me = this;
			Ext.each(Ext.query(".ds-bar-del"),
			function(cel, index) {
				var cel2 = Ext.get(cel);
				cel2.on('click',
				function(e) {
					var pel2 = cel2.parent().parent();

					if (Ext.isEmpty(pel2)) {

					} else {
						pel2.remove();
						me.bindBasic();
					}
				});
			});
		},
		bindCopy: function() {
			var me = this;
			Ext.each(Ext.query(".ds-bar-copy"),
			function(cel, index) {
				var cel2 = Ext.get(cel);
				cel2.on('click',
				function(e) {
					var pel2 = cel2.parent().parent().parent();

					if (Ext.isEmpty(pel2)) {

					} else {
						pel2.insertHtml('afterEnd', pel2.dom.outerHTML.replace('id="ext-element', 'deprecated="ext-element', "gi"),true);
								var innerHtml = pel2.dom.innerHTML;
								var scripts = pel2.dom.getElementsByTagName("script");
								if (scripts)
								{
									var script;
									for (var i = 0; i < scripts.length; i++) {
										var script = scripts[0].text.replace(/(^\s*)|(\s*$)/g, "");
										eval(script);
									}
								}
								pel2.update(innerHtml);
						me.bindBasic();
					}
				});
			});
		},
		bindUp: function() {
			var me = this;
			Ext.each(Ext.query(".ds-bar-moveup"),
			function(cel, index) {
				var cel2 = Ext.get(cel);
				cel2.on('click',
				function(e) {
					var pel2 = cel2.parent().parent();

					if (Ext.isEmpty(pel2)) {

					} else {
						if (pel2.prev()) {
							pel2.prev().insertHtml('beforeBegin', pel2.dom.outerHTML.replace('id="ext-element', 'deprecated="ext-element', "gi"));
							pel2.remove();
							me.bindBasic();
						}
					}
				});
			});
		},
		bindDown: function() {
			var me = this;
			Ext.each(Ext.query(".ds-bar-movedown"),
			function(cel, index) {
				var cel2 = Ext.get(cel);
				cel2.on('click',
				function(e) {
					var pel2 = cel2.parent().parent();

					if (Ext.isEmpty(pel2)) {

					} else {
						if (pel2.next()) {
							pel2.next().insertHtml('afterEnd', pel2.dom.outerHTML.replace('id="ext-element', 'deprecated="ext-element', "gi"));
							pel2.remove();
							me.bindBasic();
						}
					}
				});
			});

		},
		btpreview: function() {
			/*
			var urlParams=getQueryString("tzParams");

			urlParams = Ext.util.JSON.decode(urlParams);

			var siteid=urlParams.comParams.siteId;
*/
			var siteid = Ext.get("siteid").getValue();
			var pagetype = Ext.get("pagetype").getValue();

			var comId="";

			var pageId="";

			var classId="";

			var openPreviewUrl = urlPreview;
			
			if (pagetype=="homepage")
			{
				comId="TZ_HOME_SETED_COM";
				pageId="TZ_HOME_SETED_STD";
				classId="homePage";
				openPreviewUrl = openPreviewUrl + "/index";
			}

			if (pagetype=="loginpage")
			{
				comId="TZ_SITEI_SETED_COM";
				pageId="TZ_SET_LOGINP_STD";
				classId="login";
				openPreviewUrl = openPreviewUrl + "/login";
			}

			if (pagetype=="enrollpage")
			{
				comId="TZ_SITEI_SETED_COM";
				pageId="TZ_SET_ENROLLP_STD";
			}
			//window.open(urlBegin + '?tzParams={"ComID":"'+comId+'","PageID":"'+pageId+'","OperateType":"HTML","comParams":{"siteId":"'+siteid+'","oprate":"P","pagetype":"'+pagetype+'"}}');

			window.open(openPreviewUrl + '/'+ TZ_GD_LOGIN_SITEI_ORG_CODE.toLowerCase()  +'/'+ siteid);
		
		},
		btsave: function() {
			
			var pagetype = Ext.get("pagetype").getValue();
			if(pagetype=="loginpage"){
				var captchaUrl = $("#yzmImg").attr("src");
				$("#yzmImg").attr("src","");
				var bodyCode = Ext.get("all").dom.outerHTML;
				$("#yzmImg").attr("src",captchaUrl);
			}else{
				var bodyCode = Ext.get("all").dom.outerHTML;
			}
			
			var savecontent ="";

			if (pagetype=="loginpage")
			{
				//savecontent =  "<body style=\"background:#fff\" onkeydown=\"BindEnter(event)\">" + bodyCode + "</body>";
				savecontent =  "<body style=\"background:#fff\">" + bodyCode + "</body>";
			}
			else{
				savecontent =  "<body style=\"background:#fff\">" + bodyCode + "</body>";
			}

			savecontent = savecontent.replace(new RegExp(/<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi), "");
			
		    savecontent=savecontent.replace(new RegExp(/(id=\"ext-element-[0-9]*\")/g), "");
			savecontent=Ext.JSON.encode(savecontent);

			var areaArr ="";
			Ext.each(Ext.query(".assdiv"),function(cel, index) {
					var cel2 = Ext.get(cel);
/*
					var urlParams=getQueryString("tzParams");

					urlParams = Ext.util.JSON.decode(urlParams);

					var siteid=urlParams.comParams.siteId;
*/
					var siteid = Ext.get("siteid").getValue();
					var areaid=	cel2.getAttribute("area-id");

					if (!areaid)
						{
							areaid="";
						}
					var areaZone=cel2.getAttribute("area-postion");
					if (!areaZone)
						{
							areaZone="";
						}
					var areaType=cel2.getAttribute("area-type");
					if (!areaType)
						{
							areaType="";
					}

					var areaCode=cel2.dom.outerHTML;
					if (!areaCode)
					{
						areaCode="";
					}else
					{
						areaCode.replace(new RegExp(/(id=\"ext-element-[0-9]*\")/g), "");
						areaCode=Ext.JSON.encode(areaCode);
					}
				
					if (areaArr=="")
					{
						areaArr='{"siteId":"'+siteid+'","areaId":"'+areaid+'","areaZone":"'+areaZone+'","areaType":"'+areaType+'","areaCode":'+areaCode+'}';
					}else{
						areaArr=areaArr+","+'{"siteId":"'+siteid+'","areaId":"'+areaid+'","areaZone":"'+areaZone+'","areaType":"'+areaType+'","areaCode":'+areaCode+'}';
					}
			});
			areaArr ="["+areaArr+"]";

	

			var comParams = '"update":[{"typeFlag":"save","data":{"siteId":"'+siteid+'","savecontent":'+savecontent+',"pagetype":"'+pagetype+'","dataArea":'+areaArr+',"sitePageBundle":"TZSitePageBroadStyleBundle"}}]';

			var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_SITEI_SETED_STD","OperateType":"U","comParams":{'+comParams+'}}';

			Ext.Ajax.request({
				url: urlBegin,
				method: 'post',
				params: {
					tzParams: tzParams
				},
				success: function(response, options) {
					var result = Ext.util.JSON.decode(response.responseText);
					if (result.comContent.success==true)
					{
						Ext.toast({
							html: '已保存！',
							closable: false,
							align: 't',
							style: "background-color:white",
							slideInDuration: 400,
							minWidth: 400
						});
					}
					
				},
				failure: function() {
					Ext.Msg.show({
						title: '提示信息',
						msg: "保存失败！",
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING,
						buttonText: {
							ok: "关闭"
						}
					});
				}
			});

		},
		btrelease: function() {
			
			var pagetype = Ext.get("pagetype").getValue();
			if(pagetype=="loginpage"){
				var captchaUrl = $("#yzmImg").attr("src");
				$("#yzmImg").attr("src","");
				var bodyCode = Ext.get("all").dom.outerHTML;
				$("#yzmImg").attr("src",captchaUrl);
			}else{
				var bodyCode = Ext.get("all").dom.outerHTML;
			}
			
			var releasecontent ="";
			
			
			 if (pagetype=="loginpage")
			{
				releasecontent =   "<body style=\"background:#fff\">" + bodyCode + "</body>";
			}
			else{
				releasecontent =   "<body>" + bodyCode + "</body>";
			}
			
			releasecontent = releasecontent.replace(new RegExp(/<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi), "");

			 releasecontent=releasecontent.replace(new RegExp(/(id=\"ext-element-[0-9]*\")/g), "");
			 releasecontent=Ext.JSON.encode(releasecontent);
	
			var areaArr ="";
			Ext.each(Ext.query(".assdiv"),function(cel, index) {
					var cel2 = Ext.get(cel);
/*
					var urlParams=getQueryString("tzParams");

					urlParams = Ext.util.JSON.decode(urlParams);

					var siteid=urlParams.comParams.siteId;
*/
					var siteid = Ext.get("siteid").getValue();
					var areaid=	cel2.getAttribute("area-id");

					if (!areaid)
						{
							areaid="";
						}
					var areaZone=cel2.getAttribute("area-postion");
					if (!areaZone)
						{
							areaZone="";
						}
					var areaType=cel2.getAttribute("area-type");
					if (!areaType)
						{
							areaType="";
					}

					var areaCode=cel2.dom.outerHTML;
					if (!areaCode)
					{
						areaCode="";
					}else
					{
						areaCode.replace(new RegExp(/(id=\"ext-element-[0-9]*\")/g), "");
						areaCode=Ext.JSON.encode(areaCode);
					}
				
					if (areaArr=="")
					{
						areaArr='{"siteId":"'+siteid+'","areaId":"'+areaid+'","areaZone":"'+areaZone+'","areaType":"'+areaType+'","areaCode":'+areaCode+'}';
					}else{
						areaArr=areaArr+","+'{"siteId":"'+siteid+'","areaId":"'+areaid+'","areaZone":"'+areaZone+'","areaType":"'+areaType+'","areaCode":'+areaCode+'}';
					}
			});
			areaArr ="["+areaArr+"]";

			var pagetype = Ext.get("pagetype").getValue();

			var comParams = '"update":[{"typeFlag":"release","data":{"siteId":"'+siteid+'","releasecontent":'+releasecontent+',"pagetype":"'+pagetype+'","dataArea":'+areaArr+',"sitePageBundle":"TZSitePageBroadStyleBundle"}}]';

			var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_SITEI_SETED_STD","OperateType":"U","comParams":{'+comParams+'}}';

			Ext.Ajax.request({
				url: urlBegin,
				method: 'post',
				
				params: {
					tzParams: tzParams
				},
				success: function(response, options) {
					var result = Ext.util.JSON.decode(response.responseText);
					if (result.comContent.success==true)
					{
						Ext.toast({
							html: '已发布！',
							closable: false,
							align: 't',
							style: "background-color:white",
							slideInDuration: 400,
							minWidth: 400
						});
					}
				},
				failure: function() {
					Ext.Msg.show({
						title: '提示信息',
						msg: "发布失败！",
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING,
						buttonText: {
							ok: "关闭"
						}
					});
				}
			});

		},
		bindBasic: function() {
			var me = this;
			Ext.each(Ext.query(".assdiv"),
			function(el, index) {
				var el2 = Ext.get(el);
				el2.hover(function() {
					if (Ext.isEmpty(el2.child('div.baracts'))) {
						el2.insertHtml('beforeEnd', '<div class="baracts"><a href="javascript:void(0)" class="ds-bar-edit"></a><a href="javascript:void(0)" class="ds-bar-moveup"></a><a href="javascript:void(0)" class="ds-bar-movedown"></a><a href="javascript:void(0)" class="ds-bar-del"></a><div class="bbars"><a href="javascript:void(0)" class="ds-bar-add"></a></div></div>');
						if (el2.getAttribute("area-add") == "false") {
							//el2.child('div.baracts .ds-bar-add').addClsOnOver("ds-bar-add-false");
							el2.child('div.baracts .ds-bar-add').remove();
						} else {
							me.bindAdd();
						}

						if (el2.getAttribute("area-edit") == "false") {
							//el2.child('div.baracts .ds-bar-edit').addClsOnOver("ds-bar-edit-false");
							el2.child('div.baracts .ds-bar-edit').remove();
						} else {
							me.bindEdit();
							me.bindPoint();
						}
						/* 复制不在使用了，全部去除*/
						/*
						if (el2.getAttribute("area-copy") == "false") {
							el2.child('div.baracts .ds-bar-copy').addClsOnOver("ds-bar-copy-false");
						} else {
							me.bindCopy();
						}
						*/

						if (el2.getAttribute("area-del") == "false") {
							//el2.child('div.baracts .ds-bar-del').addClsOnOver("ds-bar-del-false");
							el2.child('div.baracts .ds-bar-del').remove();
						} else if (Ext.isEmpty(el2.next()) && Ext.isEmpty(el2.prev())) {
							//el2.child('div.baracts .ds-bar-del').addClsOnOver("ds-bar-del-false");
							el2.child('div.baracts .ds-bar-del').remove();
						} else {
							me.bindDel();
						}

						if (el2.getAttribute("area-moveup") == "false") {
							//el2.child('div.baracts .ds-bar-moveup').addClsOnOver("ds-bar-moveup-false");
							el2.child('div.baracts .ds-bar-moveup').remove();
						} else {
							me.bindUp();
						}

						if (el2.getAttribute("area-movedown") == "false") {
							//el2.child('div.baracts .ds-bar-movedown').addClsOnOver("ds-bar-movedown-false");
							el2.child('div.baracts .ds-bar-movedown').remove();
						} else {
							me.bindDown();
						}

					}
				},
				function() {
					if (Ext.isEmpty(el2.child('div.baracts'))) {

					} else {

						el2.child('div.baracts').remove();
					}
				});
			});
		},
		init: function() {
			var me = this;
			/*
			Ext.get("sdkbarsetui").on("click",
				function(e) {
					var urlParams=getQueryString("tzParams");

					urlParams = Ext.util.JSON.decode(urlParams);

					var siteid=urlParams.comParams.siteId;

					var styleWin=Ext.create("KitchenSink.view.website.set.js.styleWindow");
					styleWin.show();
				}
			);
			//*/
			Ext.get("sdkbarhome").on("click",
			function(e) {
				/*
				var urlParams=getQueryString("tzParams");

				urlParams = Ext.util.JSON.decode(urlParams);

				var siteid=urlParams.comParams.siteId;
*/
				var siteid = Ext.get("siteid").getValue();
				//window.location.href='?tzParams={"ComID":"TZ_HOME_SETED_COM","PageID":"TZ_HOME_SETED_STD","OperateType":"HTML","comParams":{"siteId":"'+siteid+'","oprate":"D"}}';
				window.location.href= urlDecorate + "/index/" + TZ_GD_LOGIN_SITEI_ORG_CODE.toLowerCase() + "/" + siteid;
			});

			Ext.get("sdkbarlogin").on("click",
			function(e) {
				/*
				var urlParams=getQueryString("tzParams");

				urlParams = Ext.util.JSON.decode(urlParams);

				var siteid=urlParams.comParams.siteId;
				*/
				var siteid = Ext.get("siteid").getValue();
				//window.location.href='?tzParams={"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_SET_LOGINP_STD","OperateType":"HTML","comParams":{"siteId":"'+siteid+'","oprate":"D"}}';
				window.location.href= urlDecorate + "/login/" + TZ_GD_LOGIN_SITEI_ORG_CODE.toLowerCase() + "/" + siteid;
			});

			/*
			Ext.get("sdkbarregiste").on("click",
			function(e) {
				var urlParams=getQueryString("tzParams");

				urlParams = Ext.util.JSON.decode(urlParams);

				var siteid=urlParams.comParams.siteId;

				window.location.href='?tzParams={"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_SET_ENROLLP_STD","OperateType":"HTML","comParams":{"siteId":"'+siteid+'","oprate":"D"}}';
			});
			//*/


			Ext.get("sdkbarpreview").on("click",
			function(e) {
				me.btpreview();
			});

			Ext.get("sdkbarsave").on("click",
			function(e) {
				me.btsave();
			});

			Ext.get("sdkbarrelease").on("click",
			function(e) {
				me.btrelease();
			});

			me.bindBasic();
		}
	});
	new ElBindEvent().init();
});