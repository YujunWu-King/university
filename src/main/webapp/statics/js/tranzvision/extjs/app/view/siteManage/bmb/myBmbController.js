Ext.define('KitchenSink.view.template.bmb.myBmbController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.myBmb', 
	requires: [
		'KitchenSink.view.tab.SideNavigationTabs'
    ],
	isLoaded:false,
	items:[],
	tplid:"",
	editurl:"http://202.120.24.169:9550/index.html",
	previewurl:"http://202.120.24.169:9550/index.html",//http://202.120.24.169:8050/psc/ACEMDEV/EMPLOYEE/CRM/s/WEBLIB_SURVEY.TZ_PREVIEW.FieldFormula.Iscript_AppForm_Dispatcher?mode=Y

	addBmbTpl: function(){
	
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONREG_ADD_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ONREG_ADD_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('myBmbRegWindow');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		console.log(this.getView());
        win.show();
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