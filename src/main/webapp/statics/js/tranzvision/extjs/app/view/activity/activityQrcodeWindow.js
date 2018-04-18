Ext.define('KitchenSink.view.activity.activityQrcodeWindow', {
	extend: 'Ext.window.Window',
	xtype: 'activityQrcodeWindow', 
	title: '活动二维码', 
	reference: 'activityQrcodeWindow',
	width: 630,
	height: 360,
	minWidth: 600,
	minHeight: 300,
	layout: 'fit',
	resizable: false,
	modal: true,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	
	constructor: function(config){
		this.callParent(Ext.apply(this,config));
    },
    
    initComponent: function(){
    	Ext.util.CSS.createStyleSheet("#qrCode2{margin-right: 0px !important;}");
    	
    	var qrcodeStore = this.qrcodeStore;
    	
    	Ext.apply(this,{
			items: [{
				xtype: 'form',	
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				border: false,
				bodyPadding: 15,
				items: [{
					name: 'qrCodeView',
					xtype:'dataview',
					cls: 'tz_qrcode_view',
					store: qrcodeStore,
					tpl:[
						'<tpl for=".">',
						'<div class="thumb-wrap tz-qrcode" style="padding:0px;width:280px;border:none;margin-left:0px;margin-right:30px;word-break: break-all;" id="qrCode{index}">',
						'<span style="font-weight:bold;">{qrcodeTitle}:</span>',
						'<div style="width:180px;height:180px;background:url({qrcodeImg}) no-repeat;background-size:110%;background-position: center;border:1px solid #c3c3c3;">',
						'</div>',
						'<span>{qrcodeContent}</span>',
						'</div>',
						'</tpl>',
						'<div class="x-clear"></div>',
					],
					itemSelector: 'div.thumb-wrap.tz-qrcode',
					listeners:{
						itemmouseenter:function(view, record, item, index, e, eOpts){
							
						},
						itemmouseleave:function(view, record, item, index, e, eOpts){
							
						}
					}
				}]
			}]
    	});
    	this.callParent();
    },
	buttons: [{
		text: '关闭',
		iconCls:"close",
		handler: function(btn){
			btn.findParentByType("window").close();
		}
	}]
});