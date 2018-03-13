var imgWin;
Ext.define('KitchenSink.view.website.set.js.styleWindow',{
	 extend: 'Ext.window.Window',
	 alias : 'widget.styleWindow',
	 requires: [
		 		'KitchenSink.view.website.set.js.styleWinStore'],
	 title: '站点整体风格选择',
	// height: 400,
    // width: 650,
	 autoHeight: true,
    // autoWidth:true,
     layout: 'fit',
     resizable: true,
	 autoScroll: true,
     modal: true,
	 items:[{
		xtype:'dataview',
		frame:true,
		width:480,
		height:280,
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
									'<div><h4>{skinName}</h4>',
										'<tpl if="this.isCurrSkinType(values)"><span id="{siteId}_{skinId}" class="current display"></span></tpl>',
										'<tpl if="this.isNotCurrSkinType(values)"><span id="{siteId}_{skinId}" class="current hidden"></span></tpl>',
									'</div>',
									'<img src="'+ TzUniversityContextPath +'{skinImg}"/>',
									'<a class="style-preview" href="javascript:void(0);" onclick="onPreview({siteId},{skinId})">预览</a>',
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
	 }],
	 buttons: [
		{ text: '保存',handler: function(){
		
			var nowWin = this.up("window");
			if (! this.up("window").down("dataview").getSelectionModel().getSelection()[0])
			{
				Ext.Msg.show({
						title: '提示信息',
						msg: "未选择风格！",
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.WARNING,
						buttonText: {
							ok: "关闭"
						}
					});
				return;
			}
			var siteId=this.up("window").down("dataview").getSelectionModel().getSelection()[0].data.siteId;
			var skinId=this.up("window").down("dataview").getSelectionModel().getSelection()[0].data.skinId;
			var skinType=this.up("window").down("dataview").getSelectionModel().getSelection()[0].data.skinType;
			var siteIId=this.up("window").down("dataview").getSelectionModel().getSelection()[0].data.siteIId;

			var comParams = '"add":[{"typeFlag":"init","data":{"siteId":"'+siteId+'","skinId":"'+skinId+'","skinType":"'+skinType+'","siteIId":"'+siteIId+'"}}]';

			var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_SITEI_SETED_STD","OperateType":"U","comParams":{'+comParams+'}}';
			var newTab=window.open('about:blank');
			
			 Ext.Ajax.request({
					url: Ext.tzGetGeneralURL(),
					params: {
						tzParams: tzParams
					},
					success: function(response, opts) {
						
						var obj = Ext.util.JSON.decode(response.responseText);	
						
							if (obj.comContent.success==true)
							{
								nowWin.close();
								//location="javascript:location.reload()";
								newTab.location.href=Ext.tzGetGeneralURL()+'?tzParams='+encodeURIComponent('{"ComID":"TZ_HOME_SETED_COM","PageID":"TZ_HOME_SETED_STD","OperateType":"HTML","comParams":{"siteId":"'+obj.comContent.siteId+'","oprate":"D"}}');
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
		}},
		{ text: '关闭',handler: function(){
			this.up("window").close();
	 }}
	]
});

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
					width: 820,
					height: 490,
					src:imgArray[i]
				}	
			}],
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
						Ext.getCmp(comId).getEl().dom.src = imgArray[i];	
					}
				},	
				{text: '下一页',handler: function(){
					var comId=imgPanel.items.items[0].id;
					i = i +1;
					if (i >= imgArray.length)
					{
						i = 0;
					}
						Ext.getCmp(comId).getEl().dom.src = imgArray[i];	
				}}]
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
			 width: winWidth-80,
			 height: winHeight-40,
			 minWidth: 1000,
			 minHeight: 600,
			 layout: 'fit',
			 resizable: true,
			 modal: true,
			 items:imgPanel
			});
		imgWin.show();
	});
}