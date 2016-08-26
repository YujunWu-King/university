Ext.define('KitchenSink.view.website.set.js.siteHomeSet',{
	//extend: 'Ext.window.Window',
	extend: 'Ext.panel.Panel', 
	constructor: function (config) {
		 this.siteId = config.siteId;
		 this.callParent();
	 },

	listeners: {
		beforerender: function(panel) {
			var siteId = this.siteId;
			var tzParams = '{"ComID":"TZ_SITEHOME_SET_COM","PageID":"TZ_SITEHOME_STD","OperateType":"EJSON","comParams":{"siteId":"'+siteId+'"}}';
			Ext.tzLoad(tzParams,function(resp) {
				panel.close();
				if (!resp.siteId || resp.siteId==""){
					Ext.Msg.show({
						title: '提示信息',
						msg: '站点尚未开通，是否开通？',
						buttons: Ext.Msg.OKCANCEL,
						icon: Ext.Msg.WARNING,
						buttonText: {
							ok: "确定",
							cancel:"取消"
						},
						fn:function(e) {  
							if (e=="ok")
							{
								Ext.tzSetCompResourses("TZ_SITESTEM_COM");
								//是否有访问权限
								var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SITESTEM_COM"]["TZ_SITESTEM_STD"];
								if( pageResSet == "" || pageResSet == undefined){
									Ext.MessageBox.alert('提示', '您没有修改数据的权限');
									return;
								}
								//该功能对应的JS类
								var className = pageResSet["jsClassName"];
								if(className == "" || className == undefined){
									Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_SITESTEM_STD，请检查配置。');
									return;
								}
								
								var contentPanel, cmp, ViewClass, clsProto;
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
									if (!clsProto.themeInfo) {
										Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
											themeName + '\'. Is this intentional?');
									}
								}
								
								cmp = new ViewClass();
								tab = contentPanel.add(cmp);     
								contentPanel.setActiveTab(tab);
								Ext.resumeLayouts(true);
						
								if (cmp.floating) {
									cmp.show();
								}	
							} 
						} 
					});	
				} else if (!resp.siteMid || resp.siteMid==""){
					Ext.Msg.show({
						title: '提示信息',
						msg: '站点尚未进行实例化，是否现在实例化？',
						buttons: Ext.Msg.OKCANCEL,
						icon: Ext.Msg.WARNING,
						buttonText: {
							ok: "确定",
							cancel:"取消"
						},
						fn:function(e) {  
							if (e=="ok")
							{
								Ext.tzSetCompResourses("TZ_SITE_STYLE_COM");
								//是否有访问权限
								var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SITE_STYLE_COM"]["TZ_SITE_STYLE_STD"];
								if( pageResSet == "" || pageResSet == undefined){
									Ext.MessageBox.alert('提示', '您没有修改数据的权限');
									return;
								}
								//该功能对应的JS类
								var className = pageResSet["jsClassName"];
								if(className == "" || className == undefined){
									Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_SITE_STYLE_STD，请检查配置。');
									return;
								}
								
								var contentPanel, cmp, ViewClass, clsProto;
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
									if (!clsProto.themeInfo) {
										Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
											themeName + '\'. Is this intentional?');
									}
								}
								
								cmp = new ViewClass();
								tab = contentPanel.add(cmp);     
								contentPanel.setActiveTab(tab);
								Ext.resumeLayouts(true);
						
								if (cmp.floating) {
									cmp.show();
								}	
							} 
						} 
					});	
				} else {
					
					var newTab=window.open('about:blank');
					try{
					//	newTab.location.href = Ext.tzGetGeneralURL()+'?tzParams={"ComID":"TZ_HOME_SETED_COM","PageID":"TZ_HOME_SETED_STD","OperateType":"HTML","comParams":{"siteId":"'+resp.siteId+'","oprate":"D"}}';
						setTimeout(function(){newTab.location.href = TzUniversityContextPath + '/decorate/index/'+ Ext.tzOrgID.toLowerCase() +'/'+ resp.siteId},200);
					}catch(e){
						Ext.Msg.alert("提示","您的浏览器阻止弹出窗口，请您允许本站点的弹窗！");
					}			
				}
			});
		}
	}
})