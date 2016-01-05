Ext.define('KitchenSink.view.template.sitetemplate.menu.menuManges.menuTypeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.siteTypeInfo',
	requires: [
       'KitchenSink.view.template.sitetemplate.menu.menuManges.menuIconwindow'
    ],
	editSiteSkin: function(view, rowIndex){

		var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);


	 	//皮肤ID
   	 	var skin_id = selRec.get("skinId");
     	//显示栏目设置编辑页面

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDMB_COM"]["TZ_GD_MENUICON_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_MENUICON_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('menuIconwindow');
        


        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

		//操作类型设置为更新
		win.actType = "update";
		//站点模板基本信息
		var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];

		//类型ID
		var menutypeid = comSiteParams["menutypeid"];

		//模板栏目表单信息;
		var form = win.child('form').getForm();
		
		//参数
		var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_GD_MENUICON_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'","menutypeid":"'+menutypeid+'","skinId":"'+skin_id+'"}}';
		//加载数据
		Ext.tzLoad(tzParams,function(responseData){
			//栏目设置数据
			var formData = responseData.formData;
			form.setValues(formData);
			form.setValues({siteId:siteId});
			win.child('form').child('form').child("image").setSrc(TzUniversityContextPath + formData.menutypeimg);
			win.child('form').down('form[name=imgForm2]').child("image").setSrc(TzUniversityContextPath + formData.menunowimg);
			
		});
        win.show();
	
	},
	onFormSave: function(btn){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		//组件信息标志
		var actType = this.getView().actType;
		//更新操作参数
		var comParams = "";
		/*var editCheckbox=form.findField("menuisadd").getValue(); 
		
		if(editCheckbox==false){
			form.findField('menuisadd').setValue(1);
		}*/
		//页面注册信息表单
		/*var src = this.getView().child("form").child("form").child("image").src;
		var srcNow=	this.getView().child("form").down('form[name=imgForm2]').child("image").src;
		*/
		//表单数据
		var formParams = form.getValues();
		
		formParams["menutypeimg"] = "";
		formParams["menunowimg"] = "";
		
		//alert(src);
		//新增
		if(actType == "add"){
			comParams = '"add":['+Ext.JSON.encode(formParams)+']';
		}
		//修改json字符串
		var editJson = "";
		if(actType == "update"){
			editJson = Ext.JSON.encode(formParams);
		}
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_ZD_CDLXSZ_STD","OperateType":"U","comParams":{'+comParams+'}}';

		var comView = this.getView();

		Ext.tzSubmit(tzParams,function(responseData){
			comView.actType = "update";
			if(actType == "add" && btn != "but_ensure"){
				var menutypeid = responseData.menutypeid;
				form.findField('menutypeid').setValue(menutypeid);
			}
			
			var contentPanel;
			contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
			contentPanel.child("menuTypeManagement").store.reload();
		},"",true,this);
	},
	onFormEnsure: function(){
		this.onFormSave("but_ensure");
		this.getView().close();
	},
	onFormClose: function(){
		this.getView().close();
	}/*,
	onIconWindSave:function(){
		
	},
	onIconWindEnsure:function(){
	
	}*/
});