Ext.define('KitchenSink.view.siteManage.outsiteManage.tempController', {
	extend: 'Ext.app.ViewController',
	alias: 'controller.tempController', 
	
    //添加站点模板，页面显示；
	addOutTemp: function(btn) {
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WWZDGL_COM"]["TZ_GD_WWMBDY_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_WWMBDY_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('tempPanel');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		//操作类型设置为新增
		win.actType = "add";
		//站点模板基本信息
		var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];
        //模板信息表单
		var form = win.child("form").getForm();
		form.reset();
		form.setValues({siteId:siteId});
//		form.findField("siteId").setReadOnly(true);
//		form.findField("siteId").addCls("lanage_1");
        win.show();
    },
    
    
    //选中后编辑站点模版
    editSelectField : function(btn) {
		// 选中行
		var grid = btn.up('grid');
		var selList = grid.getSelectionModel().getSelection();
		// 选中行长度
		var checkLen = selList.length;
		if (checkLen == 0) {
			Ext.Msg.alert("提示", "请选择一条要编辑的记录");
			return;
		} else if (checkLen > 1) {
			Ext.Msg.alert("提示", "只能选择一条要编辑的记录");
			return;
		}
		var templateId = selList[0].get("templateId");
		this.editOutTemplateByID(templateId);
	},
    
    //编辑某一站点模版
	editField: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
	 	//模板集合id
   	 	var templateId = selRec.get("templateId");
     	//显示皮肤设置编辑页面
     	this.editOutTemplateByID(templateId);
    },
    
    
    editOutTemplateByID: function(templateId){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WWZDGL_COM"]["TZ_GD_WWMBDY_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_WWMBDY_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('tempPanel');
        
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
		//模板集合表单信息;
		var form = win.child('form').getForm();
		//参数
		var tzParams = '{"ComID":"TZ_GD_WWZDGL_COM","PageID":"TZ_GD_WWMBDY_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'","templateId":"'+templateId+'"}}';
		//加载数据
		Ext.tzLoad(tzParams,function(responseData){

			var formData = responseData.formData;
			form.setValues(formData);
			form.setValues({siteId:siteId});
		});
        win.show();
	},
	
	//新增或修改 模板提交
	onFormSave: function(btn){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		//组件信息标志
		var actType = this.getView().actType;
		//更新操作参数
		var comParams = "";
		//新增
		if(actType == "add"){
			comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
		}
		//修改json字符串
		var editJson = "";
		if(actType == "update"){
			editJson = Ext.JSON.encode(form.getValues());
		}
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_WWZDGL_COM","PageID":"TZ_GD_WWMBDY_STD","OperateType":"U","comParams":{'+comParams+'}}';

		var comView = this.getView();

		Ext.tzSubmit(tzParams,function(responseData){
			comView.actType = "update";
			if(actType == "add" && btn != "but_ensure"){
				var templateId = responseData.templateId;
				form.findField('templateId').setValue(templateId);
			}
			contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
			//contentPanel.child("artTypeList").store.reload();
			var templateGrid = contentPanel.child("tempListPanel").child("grid").store.reload();
		},"",true,this);
	},
	onFormEnsure: function(){
		this.onFormSave("but_ensure");
		//onFormClose();
		this.getView().close();
	},
	onFormClose: function(){
		this.getView().close();
	},
	
	
	/* 删除多行字段 */
	removeSelectField : function(obj, rowIndex) {
		// 是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WWZDGL_COM"]["TZ_GD_WWMBDY_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有删除数据的权限');
			return;
		}
		// 选中行
		var store = obj.findParentByType('grid').getStore();
		if (rowIndex.toString().match(/^\d+$/)) {
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId) {
				if (btnId == 'yes') {
					store.removeAt(rowIndex);
				}
			}, this);
		} else {
			var selList = obj.findParentByType('grid').getSelectionModel()
					.getSelection();
			// 选中行长度
			var checkLen = selList.length;
			if (checkLen == 0) {
				Ext.Msg.alert("提示", "请选择要删除的记录");
				return;
			} else {
				Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId) {
					if (btnId == 'yes') {
						store.remove(selList);
					}
				}, this);
			}

		}
	},
	/* 删除指定行字段 */
	removeField : function(view, rowIndex) {
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId) {
			if (btnId == 'yes') {
				var store = view.findParentByType("grid").store;
				store.removeAt(rowIndex);
			}
		}, this);
	},
	
	//保存数据
	saveTempList: function(obj){
		var grid = this.getView().child("grid");
		//Ext.MessageBox.alert('提示', grid);
		var store = grid.getStore();
		//删除json字符串
		var removeJson = "";
		//删除记录
		var removeRecs = store.getRemovedRecords();
		

		for(var i=0;i<removeRecs.length;i++){
			if(removeJson == ""){
				removeJson = Ext.JSON.encode(removeRecs[i].data);
			}else{
				removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
			}
		}
		var comParams = "";
		if(removeJson != ""){
			comParams = '"delete":[' + removeJson + "]";
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_WWZDGL_COM","PageID":"TZ_GD_WWMBDY_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
		Ext.tzSubmit(tzParams,function(){
			store.reload();			   
		},"",true,this);
	},
	closeTempList: function(obj){
		this.getView().close();
		//this.ensureTempList(obj);
	},
	//确定
	ensureTempList:function(obj) {
		var view = this.getView();
		var grid = view.child("grid");
		//Ext.MessageBox.alert('提示', grid);
		var store = grid.getStore();
		//删除json字符串
		var removeJson = "";
		//删除记录
		var removeRecs = store.getRemovedRecords();
		

		for(var i=0;i<removeRecs.length;i++){
			if(removeJson == ""){
				removeJson = Ext.JSON.encode(removeRecs[i].data);
			}else{
				removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
			}
		}
		var comParams = "";
		if(removeJson != ""){
			comParams = '"delete":[' + removeJson + "]";
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_WWZDGL_COM","PageID":"TZ_GD_WWMBDY_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
		Ext.tzSubmit(tzParams,function(){
			view.close();
		},"",true,this);
	}
});