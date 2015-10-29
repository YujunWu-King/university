Ext.define('KitchenSink.view.template.sitetemplate.menu.menuManges.menuIconController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.menuIconController',
	onIconWindSave:function(){
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
		var src = this.getView().child("form").child("form").child("image").src;
		var srcNow=	this.getView().child("form").down('form[name=imgForm2]').child("image").src;
		//表单数据
		var formParams = form.getValues();
		formParams["menutypeimg"] = src;
		formParams["menunowimg"] = srcNow;
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
		var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_GD_MENUICON_STD","OperateType":"U","comParams":{'+comParams+'}}';

		var comView = this.getView();

		Ext.tzSubmit(tzParams,function(responseData){
			comView.actType = "update";
			/*
			if(actType == "add" && btn != "but_ensure"){
				var menutypeid = responseData.menutypeid;
				form.findField('menutypeid').setValue(menutypeid);
			}
			
			var contentPanel;
			contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
			contentPanel.child("menuTypeManagement").store.reload();
			*/
		},"",true,this);
	},
	onIconWindEnsure:function(){
		this.onIconWindSave();
		this.getView().close();
	}
});