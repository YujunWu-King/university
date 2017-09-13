Ext.define('KitchenSink.view.dispatchLoop.dispatchLoopController', {
    extend: 'Ext.app.ViewController',
    requires:['Ext.ux.IFrame'],
    alias: 'controller.dispatchLoopCon',

	//可配置搜索
	cfgSearchAct: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_DD_LOOP_COM.TZ_DD_LOOP_LIST.TZ_DD_LOOP_VW',
			condition:
			{
			},
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},

	addDispatchLoop: function() {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_DD_LOOP_COM"]["TZ_DD_LOOP_INFO"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_DD_LOOP_INFO，请检查配置。');
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

		if (clsProto.themes) {
			clsProto.themeInfo = clsProto.themes[themeName];

			if (themeName === 'gray') {
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
			} else if (themeName !== 'neptune' && themeName !== 'classic') {
				if (themeName === 'crisp-touch') {
					clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
				}
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
			}
			// <debug warn>
			// Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
			if (!clsProto.themeInfo) {
				Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
					themeName + '\'. Is this intentional?');
			}
			// </debug>
		}

		cmp = new ViewClass();

		tab = contentPanel.add(cmp);

		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
	editDispatchLoop:function () {
		//选中行
		var selList = this.getView().getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");
			return;
		}else if(checkLen >1){
			Ext.Msg.alert("提示","只能选择一条要修改的记录");
			return;
		}
		//机构ID
		var orgId = selList[0].get("orgId");
		var loopName = selList[0].get("loopName");

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_DD_LOOP_COM"]["TZ_DD_LOOP_INFO"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_DD_LOOP_INFO，请检查配置。');
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

		if (clsProto.themes) {
			clsProto.themeInfo = clsProto.themes[themeName];

			if (themeName === 'gray') {
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
			} else if (themeName !== 'neptune' && themeName !== 'classic') {
				if (themeName === 'crisp-touch') {
					clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
				}
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
			}
			// <debug warn>
			// Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
			if (!clsProto.themeInfo) {
				Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
					themeName + '\'. Is this intentional?');
			}
			// </debug>
		}
		cmp = new ViewClass();

		//操作类型设置为更新
		cmp.actType = "update";
		cmp.on('afterrender',function(panel){
			//组件注册表单信息;
			var form = panel.child('form').getForm();
			var yearForm = panel.down('tabpanel').down('form[name=yearForm]').getForm();
			var monthForm = panel.down('tabpanel').down('form[name=monthForm]').getForm();
			var dayForm = panel.down('tabpanel').down('form[name=dayForm]').getForm();
			var hourForm = panel.down('tabpanel').down('form[name=hourForm]').getForm();
			var minuteForm = panel.down('tabpanel').down('form[name=minuteForm]').getForm();
			var secondForm = panel.down('tabpanel').down('form[name=secondForm]').getForm();
			var customForm = panel.down('tabpanel').down('form[name=customForm]').getForm();
			form.findField("orgId").setReadOnly(true);
			form.findField("loopName").setReadOnly(true);

			//参数
			var tzParams = '{"ComID":"TZ_DD_LOOP_COM","PageID":"TZ_DD_LOOP_INFO","OperateType":"QF","comParams":{"orgId":"'+orgId+'","loopName":"'+ loopName +'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);
				yearForm.setValues(formData);
				monthForm.setValues(formData);
				dayForm.setValues(formData);
				hourForm.setValues(formData);
				minuteForm.setValues(formData);
				secondForm.setValues(formData);
				customForm.setValues(formData);
			
			});

		});

		tab = contentPanel.add(cmp);

		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}

	},
	saveDispatchLoop:function (btn) {

		var grid = btn.findParentByType("grid");
		var store = grid.getStore();
		var removeJson = "";
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
		var tzParams = '{"ComID":"TZ_DD_LOOP_COM","PageID":"TZ_DD_LOOP_LIST","OperateType":"U","comParams":{'+comParams+'}}';
		//保存数据
		Ext.tzSubmit(tzParams,function(){
			store.reload();
		},"",true,this);
	},
	ensureDispatchLoop:function (btn) {
		this.saveDispatchLoop(btn);
		this.view.close();
	},
	deleteDispatchLoop:function () {
		//选中行
		var selList = this.getView().getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.MessageBox.alert("提示", "您没有选中任何记录");
			return;
		}else{
			Ext.MessageBox.confirm("确认", "您确定要删除所选记录吗?", function(btnId){
				if(btnId == 'yes'){
					var tagStore = this.getView().store;
					tagStore.remove(selList);
				}
			},this);
		}
	},

	editDispatchLoopBL:function (view, rowIndex) {
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		var orgId = selRec.get("orgId");
		var loopName = selRec.get("loopName");

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_DD_LOOP_COM"]["TZ_DD_LOOP_INFO"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_DD_LOOP_INFO，请检查配置。');
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

		if (clsProto.themes) {
			clsProto.themeInfo = clsProto.themes[themeName];

			if (themeName === 'gray') {
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
			} else if (themeName !== 'neptune' && themeName !== 'classic') {
				if (themeName === 'crisp-touch') {
					clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
				}
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
			}
			// <debug warn>
			// Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
			if (!clsProto.themeInfo) {
				Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
					themeName + '\'. Is this intentional?');
			}
			// </debug>
		}
		cmp = new ViewClass();

		//操作类型设置为更新
		cmp.actType = "update";
		cmp.on('afterrender',function(panel){
			//组件注册表单信息;
			var form = panel.child('form').getForm();
			form.findField("orgId").setReadOnly(true);
			form.findField("loopName").setReadOnly(true);

			//参数
			var tzParams = '{"ComID":"TZ_DD_LOOP_COM","PageID":"TZ_DD_LOOP_INFO","OperateType":"QF","comParams":{"orgId":"'+orgId+'","loopName":"'+ loopName +'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);
			});

		});

		tab = contentPanel.add(cmp);

		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
	deleteDispatchLoopBL:function (view, rowIndex) {
		Ext.MessageBox.confirm("确认", "您确定要删除所选记录吗?", function(btnId){
			if(btnId == 'yes'){
				var store = view.findParentByType("grid").store;
				store.removeAt(rowIndex);
			}
		},this);
	},
	onDispatchLoopClose: function(btn){
		//关闭窗口
		this.getView().close();
	},

	//新增窗口保存方法
	onDispatchLoopInfoSave:function (btn) {

		var form = this.getView().child("form").getForm();
		var tzParams = this.getDispatchLoopInfoParams();
		var comView = this.getView();

		Ext.tzSubmit(tzParams,function(responseData){
			
			comView.actType = "update";
			var contentPanel;
			contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
			contentPanel.child("dispatchLoopCon").store.reload();
		},"",true,this);
	},
	//新增窗口确定方法
	onDispatchLoopInfoEnsure:function (btn) {
		
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取组件注册信息参数
			var tzParams = this.getDispatchLoopInfoParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				//关闭窗口
				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
				contentPanel.child("dispatchLoopCon").store.reload();
				comView.close();
			},"",true,this);
		}
	},

	onDispatchLoopInfoClose: function(btn){
		//关闭窗口
		this.getView().close();
	},

	//判断表单为新增还是编辑
	getDispatchLoopInfoParams:function () {

		var form = this.getView().child("form").getForm();
		//表单数据
		var formParams = form.getValues();
		//组件信息标志
		var actType = this.getView().actType;

		//集中定义JSON字符串，传递值到后台
		var params = {"orgId":"","loopName":"","loopDesc":"","status":""
					,"beginYear":"","endYear":"","yearList":"","yearLoopInterval":""
					,"beginMonth":"","endMonth":"","monthList":"","monthLoopInterval":""
					,"beginDay1":"","endDay1":"","day1List":"","day1LoopInterval":"","beginDate1":""
					,"beginDay2":"","endDay2":"","day2List":"","day2LoopInterval":"","appointedDate1":"","appointedWeek":"","appointedDate2":""
					,"beginHour":"","endHour":"","hourList":"","hourLoopInterval":""
					,"beginMinute":"","endMinute":"","minuteList":"","minuteLoopInterval":""
					,"beginSecond":"","endSecond":"","secondList":"","secondLoopInterval":""
					,"customYear":"","customMonth":"","customWeek":"","customDay":"","customHour":"","customMinute":"","customSecond":""
					,"yearCheck":"","monthCheck":"","day1Check":"","day2Check":"","hourCheck":"","minuteCheck":"","secondCheck":""}
		
		//集中获取表单
		var yearForm = this.getView().down('tabpanel').down('form[name=yearForm]').getForm().getValues();
		var monthForm = this.getView().down('tabpanel').down('form[name=monthForm]').getForm().getValues();
		var dayForm = this.getView().down('tabpanel').down('form[name=dayForm]').getForm().getValues();
		var hourForm = this.getView().down('tabpanel').down('form[name=hourForm]').getForm().getValues();
		var minuteForm = this.getView().down('tabpanel').down('form[name=minuteForm]').getForm().getValues();
		var secondForm = this.getView().down('tabpanel').down('form[name=secondForm]').getForm().getValues();
		var customForm = this.getView().down('tabpanel').down('form[name=customForm]').getForm().getValues();
		
		//集中定义value值
		var yearCheck;
		var monthCheck;
		var day1Check;
		var day2Check;
		var hourCheck;
		var minuteCheck;
		var secondCheck;
		
		
		//分别获取每个check的input值
		var yearType = Ext.getCmp('loopYear').items;
		var yearValue =  this.getView().down('tabpanel').down('form[name=yearForm]').getForm().findField("loopYear").getGroupValue(); 
		for (var i=0; i<yearType.length;i++){
			if (yearType.get(i).inputValue == yearValue){
				yearCheck = yearType.get(i).inputValue;
				
				if(yearCheck == undefined){
					params.yearCheck = "";
				}else{
					params.yearCheck = yearCheck;
				}
				
			  }
		}

		var monthType = Ext.getCmp('loopMonth').items;
		var monthValue =  this.getView().down('tabpanel').down('form[name=monthForm]').getForm().findField("loopMonth").getGroupValue(); 
		for (var i=0; i<monthType.length;i++){
			if (monthType.get(i).inputValue == monthValue){
				monthCheck = monthType.get(i).inputValue;
				if(monthCheck == undefined){
					params.monthCheck = "";
				}else{
					params.monthCheck = monthCheck
				}
				
			  }
		}
		
		var dayType = Ext.getCmp('loopDay').items;
		var dayValue =  this.getView().down('tabpanel').down('form[name=dayForm]').getForm().findField("loopDay").getGroupValue(); 
		for (var i=0; i<dayType.length;i++){
			if (dayType.get(i).inputValue == dayValue){
				day1Check = dayType.get(i).inputValue;
				
				if(day1Check == undefined){
					params.day1Check = "";
				}else{
					params.day1Check = day1Check
				}
			  }
		}
		
		var hourType = Ext.getCmp('loopHour').items;
		var hourValue =  this.getView().down('tabpanel').down('form[name=hourForm]').getForm().findField("loopHour").getGroupValue(); 
		for (var i=0; i<monthType.length;i++){
			if (hourType.get(i).inputValue == hourValue){
				hourCheck = hourType.get(i).inputValue;
				
				if(hourCheck == undefined){
					params.hourCheck = "";
				}else{
					params.hourCheck = hourCheck
				}
			  }
		}
		
		var minuteType = Ext.getCmp('loopMinute').items;
		var minuteValue =  this.getView().down('tabpanel').down('form[name=minuteForm]').getForm().findField("loopMin").getGroupValue(); 
		for (var i=0; i<minuteType.length;i++){
			if (minuteType.get(i).inputValue == minuteValue){
				minuteCheck = minuteType.get(i).inputValue;
				
				if(minuteCheck == undefined){
					params.minuteCheck = "";
				}else{
					params.minuteCheck = minuteCheck
				}
			  }
		}
		
		var secondType = Ext.getCmp('loopSecond').items;
		var secondValue =  this.getView().down('tabpanel').down('form[name=secondForm]').getForm().findField("loopSecond").getGroupValue(); 
		for (var i=0; i<secondType.length;i++){
			if (secondType.get(i).inputValue == secondValue){
				secondCheck = secondType.get(i).inputValue;
				if(secondCheck == undefined){
					params.secondCheck = "";
				}else{
					params.secondCheck = secondCheck
				}
			  }
		}
		
		//基本form
		params.orgId = formParams["orgId"];
		params.loopName = formParams["loopName"];
		params.loopDesc = formParams["loopDesc"];
		params.status = formParams["status"];
		
		//年
		params.beginYear = yearForm["beginYear"];
		params.endYear = yearForm["endYear"];
		params.yearList = yearForm["yearList"];
		params.yearLoopInterval = yearForm["yearLoopInterval"];
		
		//月
		params.beginMonth = monthForm["beginMonth"];
		params.endMonth = monthForm["endMonth"];
		params.monthList = monthForm["monthList"];
		params.monthLoopInterval = monthForm["monthLoopInterval"];
		
		//周、日
		params.beginDay1 = dayForm["beginDay1"];
		params.endDay1 = dayForm["endDay1"];
		params.day1List = dayForm["day1List"];
		params.day1LoopInterval = dayForm["day1LoopInterval"];
		params.beginDate1 = dayForm["beginDate1"];
		params.beginDay2 = dayForm["beginDay2"];
		params.endDay2 = dayForm["endDay2"];
		params.day2List = dayForm["day2List"];
		params.day2LoopInterval = dayForm["day2LoopInterval"];
		params.appointedDate1 = dayForm["appointedDate1"];
		params.appointedWeek = dayForm["appointedWeek"];
		params.appointedDate2 = dayForm["appointedDate2"];
		
		//时
		params.beginHour = hourForm["beginHour"];
		params.endHour = hourForm["endHour"];
		params.hourList = hourForm["hourList"];
		params.hourLoopInterval = hourForm["hourLoopInterval"];
		
		//分
		params.beginMinute = minuteForm["beginMinute"];
		params.endMinute = minuteForm["endMinute"];
		console.log("beginMinute======" + params.endMinute)
		params.minuteList = minuteForm["minuteList"];
		params.minuteLoopInterval = minuteForm["minuteLoopInterval"];
		
		//秒
		params.beginSecond = secondForm["beginSecond"];
		params.endSecond = secondForm["endSecond"];
		params.secondList = secondForm["secondList"];
		params.secondLoopInterval = secondForm["secondLoopInterval"];
		
		//自定义
		params.customSecond = customForm["customSecond"];
		params.customMinute = customForm["customMinute"];
		params.customHour = customForm["customHour"];
		params.customDay = customForm["customDay"];
		params.customWeek = customForm["customWeek"];
		params.customMonth = customForm["customMonth"];
		params.customYear = customForm["customYear"];
		
		//更新操作参数
		var comParams = "";

		//新增
		if(actType == "add"){
			comParams = '"add":[{"data":'+Ext.JSON.encode(params)+'}]';
		}
		//修改json字符串
		var editJson = "";
		if(actType == "update"){
			editJson = '{"data":'+Ext.JSON.encode(params)+'}';
		}
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}

		//提交参数
		var tzParams = '{"ComID":"TZ_DD_LOOP_COM","PageID":"TZ_DD_LOOP_LIST","OperateType":"U","comParams":{'+comParams+'}}';
		return tzParams;
	}
});
