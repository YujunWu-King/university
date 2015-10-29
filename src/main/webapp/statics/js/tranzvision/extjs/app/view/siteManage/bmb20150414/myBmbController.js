Ext.define('KitchenSink.view.template.bmb.myBmbController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.myBmb', 
	requires: [
		'KitchenSink.view.tab.SideNavigationTabs'
    ],
	isLoaded:false,
	items:[],
	tplid:"",
	editurl:"http://202.120.24.169:9550/index.html", //http://202.120.24.169:8050/psc/ACEMDEV/EMPLOYEE/CRM/s/WEBLIB_SURVEY.TZ_SETBUTTON.FieldFormula.IScript_SetTplPageTwo
	previewurl:"http://202.120.24.169:9550/index.html",//http://202.120.24.169:8050/psc/ACEMDEV/EMPLOYEE/CRM/s/WEBLIB_SURVEY.TZ_PREVIEW.FieldFormula.Iscript_AppForm_Dispatcher?mode=Y
    addBmbTpl: function() {
		
		var   me = this ,predefinetpl ='';
		if(!me.isLoaded){
			Ext.Ajax.request({
					url: '/tranzvision/kitchensink/app/view/template/bmb/predefinetpl.json',
					async:false,
					params: {
						usAccNum: 1
					},
					waitTitle : '请等待' ,  
					waitMsg: '正在加载中', 
					success: function(response){
						var resText = response.responseText;
						var responseData = Ext.JSON.decode(resText)
						for(var i in responseData){
							predefinetpl += '<div class="tplitem" onclick="mybmb_cj(this)" style="padding: 15px;cursor: pointer;border: 1px solid #eee;display: inline-table;margin: 5px;" data-id="'+responseData[i].tplid+'">'+responseData[i].tplname+'</div>';
						}			
						me.isLoaded = true;
					}
				});	

			this.items = [{
				itemId: 'add',
				title: '新建空白模板',
				glyph: 72,
				html:(function(){
					var divTmp = document.createElement("div");
					var txt = Ext.create('Ext.form.field.Text', {
						 fieldLabel: '模板名称',
						 labelStyle: 'font-weight:bold',
						 cls:'bmb_blank_text',
						 allowBlank: false
					}).render(divTmp)
					return divTmp.outerHTML;
				})()+'<div>空白模板</div>'
			},{
				itemId: 'predefine',
				title: '使用预定义模板',
				glyph: 117,
				html: (function(){
					var divTmp = document.createElement("div");
					var txt = Ext.create('Ext.form.field.Text', {
						 fieldLabel: '模板名称',
						 labelStyle: 'font-weight:bold',
						 cls:'bmb_predefine_text',
						 allowBlank: false
					}).render(divTmp)
					return divTmp.outerHTML;
				})() + '<div class="predefinetpllist">'+predefinetpl+"</div>"
			}]
		}
		var AttachmentScan = new Ext.window.Window( {
		
			title : '新建模板',

			closable: true,

			closeAction: 'hide',
			
			modal :true,
			
			autoScroll : true,

			bodyStyle: 'padding: 5px;',

			items: [new KitchenSink.view.tab.SideNavigationTabs({fields:me.items})],
			
			buttons:[

				new Ext.button.Button({

					text	: '确定',

					width	: 80,

					height	: 30,

					padding	: 5,

					handler : function() {
						var activeTab = AttachmentScan.items.items[0].getActiveTab(),id='';
						var tplName = Ext.get(activeTab.id).select('input').elements[0].value,tplId ="";
						if(activeTab.itemId == "predefine"){
							Ext.each(Ext.query(".tplitem"),function(i){
								if(this.style.backgroundColor=="rgb(173, 216, 230)"){
									tplId = this.getAttribute("data-id");
									return false;
								}
							});
						}else{
							tplId = "";
						}
						if(tplName){
							Ext.tzSubmit({"tzParams":JSON.stringify({"OperateType":"EJSON","tplId":tplId,"tplName":tplName,"ComID":"",action:"add"})},function(jsonObject){
								var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');	
								var tab = contentPanel.add({
									title: tplName,
									loader: {
										url:me.editurl+'?TZ_QSTN_TPL_ID='+jsonObject.TZ_QSTN_TPL_ID,
										contentType: 'html',
										loadMask: true
									}
								});
								var items = contentPanel.items.items;
								items[items.length-1].loader.load();
								contentPanel.setActiveTab(tab);
							},"",true,this);
						}
						AttachmentScan.close();

					}

				}),
				
				new Ext.button.Button({

					text	: '关闭',

					width	: 80,

					height	: 30,

					padding	: 5,

					handler : function() {

						AttachmentScan.close();

					}

				})
			]
		})	
		
		AttachmentScan.show();
		if(!window.mybmb_cj){
			window.mybmb_cj =function(el){
					Ext.each(Ext.query(".tplitem"),function(i){this.style.backgroundColor = null});
					el.style.backgroundColor="rgb(173, 216, 230)";
					var activeTab = AttachmentScan.items.items[0].getActiveTab();
					document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value  = el.innerText ;
			}
		}
		
    },
    editBmbTpl: function(gridViewObject,rowIndex) {
		var me =this;
		var tplid = gridViewObject.store.getAt(rowIndex).get("tplid");
		var tplname = gridViewObject.store.getAt(rowIndex).get("tplname");
		var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');	
		var tab = contentPanel.add({
			title: tplname,
			loader: {
				url:me.editurl+'?TZ_QSTN_TPL_ID='+tplid,
				contentType: 'html',
				loadMask: true
			}
		});
		var items = contentPanel.items.items;
		items[items.length-1].loader.load();  
		contentPanel.setActiveTab(tab);
    },
	copyBmbTpl: function(gridViewObject,rowIndex){
		this.tplid = gridViewObject.store.getAt(rowIndex).get("tplid");
		Ext.MessageBox.prompt('tplname', '请输入另存的模板名称:', this.showResultText, this);
	}, 
	previewBmbTpl: function(gridViewObject,rowIndex){
		var me = this;
		var tplid = gridViewObject.store.getAt(rowIndex).get("tplid");
		var tplname = gridViewObject.store.getAt(rowIndex).get("tplname");
		var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');	
		var tab = contentPanel.add({
			title: tplname,
			loader: {
				url:me.previewurl+'?TZ_QSTN_TPL_ID='+tplid,
				contentType: 'html',
				loadMask: true
			}
		});
		var items = contentPanel.items.items;
		items[items.length-1].loader.load();
		contentPanel.setActiveTab(tab);
	},
	showResultText: function(btn, text) {
		 //此处应先发请求保存
		 var me = this;
			Ext.tzSubmit({"tzParams":JSON.stringify({"OperateType":"EJSON","tplId":this.tplid||"","tplName":text,"ComID":"",action:"add"})},function(jsonObject){
				var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');	
				var tab = contentPanel.add({
					title: text,
					loader: {
						url:me.editurl+'?TZ_QSTN_TPL_ID='+this.tplid,
						contentType: 'html',
						loadMask: true
					}
				});
				var items = contentPanel.items.items;
				items[items.length-1].loader.load();
				contentPanel.setActiveTab(tab);
			},"",true,this);
	}
});