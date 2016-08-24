Ext.define('KitchenSink.view.siteManage.simpleSiteManage.siteSimpleTemController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.siteSimpleTemController',
	
	onFormSave: function(btn){
		var panel = btn.findParentByType('panel');
		var form = panel.child('form').getForm();
		var lanIntroduce = panel.child('form').getComponent("languageIntroduce");
		
		if (form.isValid()) {
			var formParams = form.getValues();
			var actType = panel.actType;
			var lanDesc = form.findField("siteLanguage").getRawValue();//语言描述
			var tzParams = '{"ComID":"TZ_WEBSIT_SET_COM","PageID":"TZ_SITESTEM_STD","OperateType":"U","comParams":{"'+actType+'":['+Ext.JSON.encode(formParams)+']}}';

			if(actType == "add"){
				Ext.MessageBox.confirm('确认', '您是否确定要创建'+lanDesc+'语言版本的招生网站？站点语言一旦选定，您将无法自行切换修改，只能平台管理员进行切换修改，并且修改语言会导致所有历史网站的信息与数据丢失。', function(btnId){
					if(btnId == 'yes'){	
						Ext.tzSubmit(tzParams,function(resp){
							panel.actType = "update";
							form.setValues({siteId:resp.siteId});	
							form.findField('siteLanguage').setReadOnly(true);
							form.findField('siteLanguage').addCls("lanage_1");
							lanIntroduce.setHidden(false);
						},"",true,this);
					}
				});	
			}else{
				Ext.tzSubmit(tzParams,function(resp){},"",true,this);	
			}
		}
	},
	onFormClose: function(btn){
		btn.findParentByType('panel').close();
	}
});