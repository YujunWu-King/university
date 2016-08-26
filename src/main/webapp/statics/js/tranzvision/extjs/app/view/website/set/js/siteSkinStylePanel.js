var imgWin;
Ext.define('KitchenSink.view.website.set.js.siteSkinStylePanel',{
	 extend: 'Ext.panel.Panel',
	 requires: [
		 		'KitchenSink.view.website.set.js.styleWinStore'],
	 title: '站点风格选择',
	 autoHeight: true,
     layout: 'fit',
     resizable: true,
	 autoScroll: true,
	 constructor: function (config) {
		 this.siteId = config.siteId;
		 this.callParent();
	 },
	 listeners: {
		beforerender: function(panel) {
			var siteId = this.siteId;
			var tzParams = '{"ComID":"TZ_WEBSIT_SET_COM","PageID":"TZ_SITESTEM_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'"}}';
			Ext.tzLoad(tzParams,function(formData) {
				if (!formData.siteId){
					panel.close();
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
								//Ext.tzSetCompResourses("TZ_SITESTEM_COM");
								//是否有访问权限
								var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_WEBSIT_SET_COM"]["TZ_SITESTEM_STD"];
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
								
								cmp.actType='add';
								var form = cmp.child('form').getForm();
								var lanIntroduce = cmp.child('form').getComponent("languageIntroduce");
								form.findField('orgId').setValue(Ext.tzOrgID);
								form.findField('siteId').setValue("NEXT");
								lanIntroduce.setHidden(true);
							} 
						} 
					});	
				}
			});
		},
		afterrender: function(panel) {
			var siteId = this.siteId;
			//风格;
			var tzStoreParams = '{"siteId":"'+siteId+'"}';
			var dataview = panel.child("dataview");
			dataview.store.tzStoreParams = tzStoreParams;
			dataview.store.load();
		}
	 },
	 items:[{
		xtype:'dataview',
		frame:true,
	    autoHeight: true,
		layout:'hbox',
		store: {
			type: 'styleWinStore'
		},
		frame        : false,
		cls          : 'dd',
		itemSelector : 'dd',
		overItemCls  : 'over',
		trackOver    : true,
		tpl : Ext.create('Ext.XTemplate',
            '<div id="all-style" >','<div id="style-ct">',
                    '<div class="thumb-list3">',
                        '<dl >',
                            '<tpl for=".">',
                                '<dd><div class="thumb">',
									'<div class="skin_title"><h4>{skinName}</h4>',
										//'<tpl if="this.isCurrSkinType(values)"><span id="{siteId}_{skinId}" class="current display"></span></tpl>',
										//'<tpl if="this.isNotCurrSkinType(values)"><span id="{siteId}_{skinId}" class="current hidden"></span></tpl>',
									'</div>',
									'<div class="skinImg">',
										'<img src="'+ TzUniversityContextPath  +'{skinImg}"/>',
										'<tpl if="this.isNotCurrSkinType(values)">',
											'<div class="mask" onmouseover="showBtn(this);" onmouseout="hiddenBtn(this);">',
												'<div class="btn">',
													'<div><a class="selBtn" href="javascript:void(0);"  onclick=\'onChooseStyle(\"{siteId}\",\"{skinId}\",\"{siteIId}\",\"{siteInsMid}\");\'></a></div>',
													'<div><a class="preVBtn" href="javascript:void(0);" onclick=\'onPreview(\"{siteId}\",\"{skinId}\");\'></a></div>',
												'</div>',
											'</div>',
										'</tpl>',
										'<tpl if="this.isCurrSkinType(values)"><div class="current"><span class="currPic"></span>正在使用中</div><div class="skin_preview" onclick=\'onPreview(\"{siteId}\",\"{skinId}\");\'>预览</div></tpl>',
									'</div>',
									//'<a class="style-preview" href="javascript:void(0);" onclick="onPreview({siteId},{skinId})">预览</a>',
                                '</dd>',
                            '</tpl>',
                        '</dl>',
                    '</div>',
            '</div>','</div>', {
             isCurrSkinType: function(values){
                 return values.isCurrSkin == "Y";
             },
			 isNotCurrSkinType: function(values){
				 return  values.isCurrSkin != "Y";	 
			 }
        }),
		listeners:{
			selectionchange: function(ths, sel, eOpts ){
				if (sel.length >= 1){
					var $dplyItem = $(".thumb-list3 .thumb .display");
					$dplyItem.addClass("hidden");
					$dplyItem.removeClass("display");
					var $newItem = $("#"+sel[0].data.siteId+"_"+sel[0].data.skinId);
					if ($newItem.hasClass("hidden")){
						$newItem.removeClass("hidden");
						$newItem.addClass("display");	
					}
				}
			}
		}
	 }],
	 buttons: [
		{ text: '关闭',iconCls:"close",handler: function(){
				this.up("panel").close();
			}
		}
	]
});

function onChooseStyle(siteId,skinId,siteIId,siteInsMid){
	var typeFlag;
	if (!siteInsMid || siteInsMid==""){
		typeFlag = "init";
	} else {
		typeFlag = "update";
	}
	var comParams = '"add":[{"typeFlag":"'+typeFlag+'","data":{"siteId":"'+siteId+'","skinId":"'+skinId+'","siteIId":"'+siteIId+'"}}]';
	var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_SITEI_SETED_STD","OperateType":"U","comParams":{'+comParams+'}}';
	
	Ext.Ajax.request({
		url: Ext.tzGetGeneralURL(),
		params: {
			tzParams: tzParams
		},
		success: function(response, opts) {
			var obj = Ext.util.JSON.decode(response.responseText);	
			if (obj.comContent.success==true)
			{
				var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
				contentPanel.getActiveTab().down("dataview").getStore().reload();
				
				var newTab=window.open('about:blank');
				try{
					setTimeout(function(){newTab.location.href= TzUniversityContextPath + '/decorate/index/'+ Ext.tzOrgID.toLowerCase() +'/'+ obj.comContent.siteId;},200);
				}catch(e){
					Ext.Msg.alert("提示","您的浏览器阻止弹出窗口，请您允许本站点的弹窗！");
				}	
			}else{
				Ext.Msg.show({
							title: '提示信息',
							msg: response.responseText,
							buttons: Ext.Msg.OK,
							icon: Ext.Msg.WARNING,
							buttonText: {
								ok: "确定"
							}
						});
			};
		}
	 });
}

function onPreview(siteId,skinId){
	var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_IMAGE_LL_STD","OperateType":"QF","comParams":{"typeflg":"all","siteId":"'+siteId+'","skinId":"'+skinId+'"}}';
	Ext.tzLoad(tzParams,function(resp){
		var imgArray = resp.skinList;
		var i = parseInt(resp.current);

		var imgPanel=Ext.create('Ext.form.Panel', {  
			bodyPadding: 5,  
			closeAction:'destroy',
			layout: 'fit', 
			items: [{
				name:'xl_pic',
				xtype: 'box',
				autoEl: {
					tag: 'img',    //指定为img标签  
					width: 800,
					//height: 500,
					src: TzUniversityContextPath + imgArray[i]
				}	
			}]
	  });
	  
	  	var winWidth,winHeight;
		//浏览器窗口宽度
		if (window.innerWidth){
			winWidth = window.innerWidth;
		}else if ((document.body) && (document.body.clientWidth)){
			winWidth = document.body.clientWidth;
		}
		// 获取浏览器窗口高度
		if (window.innerHeight){
			winHeight = window.innerHeight;
		}else if ((document.body) && (document.body.clientHeight)){
			winHeight = document.body.clientHeight;
		}

		 imgWin = Ext.widget('window', {
			 title: '站点风格预览',
			 closeAction: 'hide',
			 width: winWidth*0.85,
			 minWidth: 800,
			 minHeight: 600,
			 maxHeight: winHeight*0.9,
			 overflowY: 'auto',
			 overflowX: 'hidden',
			 resizable: true,
			 modal: true,
			 items:imgPanel,
			 dockedItems: [{
				layout: { pack: 'center'},
				xtype: 'toolbar',
				dock: 'bottom',
				ui: 'footer',
				items:[
				{text: '上一页',handler: function(){
					var comId=imgPanel.items.items[0].id;
					
						i = i -1;
						if (i < 0)
						{
							i=imgArray.length-1;
						}
						Ext.getCmp(comId).getEl().dom.src = TzUniversityContextPath + imgArray[i];	
					}
				},	
				{text: '下一页',handler: function(){
					var comId=imgPanel.items.items[0].id;
					i = i +1;
					if (i >= imgArray.length)
					{
						i = 0;
					}
						Ext.getCmp(comId).getEl().dom.src = TzUniversityContextPath + imgArray[i];	
				}}]
			}]
		});
		imgWin.show();
	});
}

function showBtn(mask){
	$(mask).children(".btn").css("display","block");
}

function hiddenBtn(mask){
	$(mask).children(".btn").css("display","none");	
}