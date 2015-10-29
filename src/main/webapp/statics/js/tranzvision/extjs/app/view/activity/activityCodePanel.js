Ext.define('KitchenSink.view.activity.activityCodePanel', {
    extend: 'Ext.window.Window',
    xtype: 'activityCode',
	controller: 'activityInfo',
	requires: [
	    	'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
	    	'KitchenSink.view.activity.activityInfoController'
	],
		reference: 'activityCodePanel',
    title: '活动手机版',
    width: 350,
	  height: 400,
    modal: true,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'menuInfoForm',
				layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 10,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
        	xtype: 'image',
        	name: "codeImage",
        	src: '',
        	width: 175,
        	maxWidth: 175,
        	height: 175,
        	margin: '0 0 0 74'
        },{
						html: '<div align="center">扫一扫，直接在手机上打开</div>'
        },{
        	 html: '<div align="center">手机访问地址：</div>'
        },{
        	xtype: 'displayfield',
        	name: 'phonePreviewUrl'
        }]
    }],
  buttons: [ {
		text: '关闭',
		iconCls:"close",
		handler: 'onPreviewPhoneArtClose'
	}]
});
