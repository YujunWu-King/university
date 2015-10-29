Ext.define('KitchenSink.view.website.wzsy.personSetController', {
	extend: 'Ext.app.ViewController',
	alias: 'controller.personSetController',
	/*保存个人网站信息设置*/
    onPersonSetSave: function(btn) {
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var grid = this.getView().child("grid");
			//var grid = btn.findParentByType("panel").child("grid");
			//注册项数据
			var store = grid.getStore();
			//修改记录
			var editJson = "";
			var mfRecs = store.getModifiedRecords();
			for (var i = 0; i < mfRecs.length; i++) {
				if (editJson == "") {
					editJson = Ext.JSON.encode(mfRecs[i].data);
				} else {
					editJson = editJson + ',' + Ext.JSON.encode(mfRecs[i].data);
				}
			}
			var comParams = '"update": [';
			if (editJson != "") {
				comParams += '{"OperateType": "saveGrid","data": [' + editJson + ']},';
			}

			if (form.getValues() != "") {
				comParams += '{"OperateType": "saveForm","data": [' + Ext.JSON.encode(form.getValues()) + ']}';
			}
			comParams += "]";
			//提交参数
            console.log(Ext.JSON.encode(form.getValues()));
			var tzParams = '{"ComID":"TZ_SITE_PERSON_COM","PageID":"TZ_SITE_PERSON_STD","OperateType":"U","comParams":{' + comParams + '}}';

			Ext.tzSubmit(tzParams,
				function (responseData) {
					var tzParams1 = '{"ComID":"TZ_SITE_PERSON_COM","PageID":"TZ_SITE_PERSON_STD","OperateType":"QF","comParams":{}}';
					Ext.tzLoad(tzParams1,
						function (responseData) {
							//组件注册信息数据
							var formData = responseData.formData;
							form.setValues(formData);
							//页面注册信息列表数据
							//var listData = responseData.listData;
							//grid.store.loadData(listData);
							grid.store.load();
						}
					);
				}
                ,"",true,this);
		}
	}
});