Ext.define('KitchenSink.view.website.set.js.siteSetView', {
    extend: 'Ext.view.View',
	 alias : 'widget.siteSetView',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.website.set.js.styleWindow',
		'KitchenSink.view.website.set.js.siteSetModel',
        'KitchenSink.view.website.set.js.siteSetStore'
    ],
	xtype: 'websitSet',
	title: '招生网站设置',
	store: {
        type: 'siteSetStore'
    },
    frame        : false,
    cls          : 'dd',
    itemSelector : 'dd',
    overItemCls  : 'over',
    trackOver    : true,
	 tpl          : Ext.create('Ext.XTemplate',
            '<div id="all-demos" >','<div id="sample-ct">',
                    '<div class="thumb-list">',
                        '<a name="{id}"></a>',
                        '<dl >',
                            '<tpl for=".">',
                                '<dd><div class="thumb"><img src="/{icon}"/></div>',
                                    '<div><h4>{text}',
                                        '<tpl if="this.isNew(values.status)">',
                                            '<span class="new-sample"> (New)</span>',
                                        '<tpl elseif="this.isUpdated(values.status)">',
                                            '<span class="updated-sample"> (Updated)</span>',
                                        '<tpl elseif="this.isExperimental(values.status)">',
                                            '<span class="new-sample"> (Experimental)</span>',
                                        '<tpl elseif="status">',
                                            '<span class="status"> ({status})</span>',
                                        '</tpl>',
                                    '</h4><p>{desc}</p></div>',
                                '</dd>',
                            '</tpl>',
                        '</dl>',
                    '</div>',
          
            '</div>','</div>', {
             isExperimental: function(status){
                 return status == 'experimental';
             },
             isNew: function(status){
                 return status == 'new';
             },
             isUpdated: function(status){
                 return status == 'updated';
             }
        }),
		listeners:{
			itemclick:function (thisView, record, item, index, e, eOpts){


				if (!record.data.siteId)
				{
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
								var styleWin=Ext.create("KitchenSink.view.website.set.js.styleWindow");
								styleWin.show();
							} 
						} 
					});
				}else{
					
					switch(record.data.type){
						case "baseinfo":

						Ext.tzSetCompResourses("TZ_GD_ZDGL_COM");
								//是否有访问权限
						var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDGL_COM"]["TZ_GD_ZDDY_STD"];

						if( pageResSet == "" || pageResSet == undefined){
							Ext.MessageBox.alert('提示', '您没有修改数据的权限');
							return;
						}
						//该功能对应的JS类
						var className = pageResSet["jsClassName"];
						if(className == "" || className == undefined){
							Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_ZDDY_STD，请检查配置。');
							return;
						}
		
						var contentPanel,cmp, className, ViewClass, clsProto;
						var themeName = Ext.themeName;
		
						 contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
   						 contentPanel.body.addCls('kitchensink-example');

						//className = 'KitchenSink.view.template.sitetemplate.siteTemplateInfo';
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
					//页面注册信息列表
					var tabpanel = panel.child("form").child("tabpanel");
					var grid = tabpanel.getActiveTab();
					//参数
					var tzParams = '{"ComID":"TZ_GD_ZDGL_COM","PageID":"TZ_GD_ZDDY_STD","OperateType":"QF","comParams":{"siteId":"'+record.data.siteId+'"}}';
					//加载数据
					Ext.tzLoad(tzParams,function(responseData){
						//组件注册信息数据
						var formData = responseData.formData;
						form.setValues(formData);
						//页面注册信息列表数据
						//var roleList = responseData.listData;	
						/*
						var tzStoreParams = '{"siteId":"'+record.data.siteId+'","queryID":"1"}';
						grid.store.tzStoreParams = tzStoreParams;
						grid.store.load();
						*/
					});
				});
		
				tab = contentPanel.add(cmp);     
		
				contentPanel.setActiveTab(tab);

				Ext.resumeLayouts(true);

				if (cmp.floating) {
				 cmp.show();
				}

							break;
						case "sitestyle":
							var styleWin=Ext.create("KitchenSink.view.website.set.js.styleWindow");
								styleWin.show();
							break;
						case "sitehome":
							//window.open(Ext.tzGetGeneralURL()+'?tzParams={"ComID":"TZ_HOME_SETED_COM","PageID":"TZ_HOME_SETED_STD","OperateType":"HTML","comParams":{"siteId":"'+record.data.siteId+'","oprate":"D"}}');
						window.open(Ext.tzGetGeneralURL()+'?classid=homePage&siteId='+record.data.siteId+'&oprate=D');
						
							break;
						case "sitelogin":
							window.open(Ext.tzGetGeneralURL()+'?tzParams='+encodeURIComponent('{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_SET_LOGINP_STD","OperateType":"HTML","comParams":{"siteId":"'+record.data.siteId+'","oprate":"D"}}'));
							break;
						case "siteenroll":
							window.open(Ext.tzGetGeneralURL()+'?tzParams='+encodeURIComponent('{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_SET_ENROLLP_STD","OperateType":"HTML","comParams":{"siteId":"'+record.data.siteId+'","oprate":"D"}}'));
							break;
						case "sitecontent" :
						Ext.tzSetCompResourses("TZ_CONTENT_MG_COM");
								//是否有访问权限
						var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CONTENT_MG_COM"]["TZ_CONTENT_STD"];

						if( pageResSet == "" || pageResSet == undefined){
							Ext.MessageBox.alert('提示', '您没有修改数据的权限');
							return;
						}
						//该功能对应的JS类
						var className = pageResSet["jsClassName"];
						if(className == "" || className == undefined){
							Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CONTENT_STD，请检查配置。');
							return;
						}
		
						var contentPanel,cmp, className, ViewClass, clsProto;
						var themeName = Ext.themeName;
		
						 contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
   						 contentPanel.body.addCls('kitchensink-example');

						//className = 'KitchenSink.view.template.sitetemplate.siteTemplateInfo';
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
				/*
					cmp.on('afterrender',function(panel){
					//组件注册表单信息;
					var form = panel.child('form').getForm();
					//页面注册信息列表
					var tabpanel = panel.child("form").child("tabpanel");
					var grid = tabpanel.getActiveTab();
					//参数
					var tzParams = '{"ComID":"TZ_CONTENT_MG_COM","PageID":"TZ_CONTENT_STD","OperateType":"QF","comParams":{"siteId":"'+record.data.siteId+'"}}';
					//加载数据
					Ext.tzLoad(tzParams,function(responseData){
						//组件注册信息数据
						var formData = responseData.formData;
						form.setValues(formData);
					});
				});
				*/
				tab = contentPanel.add(cmp);     
		
				contentPanel.setActiveTab(tab);

				Ext.resumeLayouts(true);

				if (cmp.floating) {
				 cmp.show();
				}

				break;
					}
				}
				
			}
		}
});
