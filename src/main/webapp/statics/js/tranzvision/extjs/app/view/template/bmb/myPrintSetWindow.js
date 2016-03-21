Ext.define('KitchenSink.view.template.bmb.myPrintSetWindow', {
    extend: 'Ext.window.Window',
	xtype : 'myPrintSetWindow',
    requires: [
               'Ext.data.*',
               'Ext.grid.*',
               'Ext.util.*',
               'Ext.toolbar.Paging',
               'Ext.ux.ProgressBarPager',
               'KitchenSink.view.template.bmb.myBmbController'
           ],
	reference : 'myPrintSetWindow',
	title : '模板打印设置',
    width: 900,
    height:600,
    resizable: true,
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    modal: true,
    closeAction: 'hide',
    closable: true,
    autoScroll: true,
	items : [
	         {
	        	 xtype : 'form',
	        	 reference : 'myPrintSetWindow',
	        	 layout : {
	        		 type : 'vbox',
	        		 align : 'stretch'
	        	 },
	        	 border : false,
	        	 style : "margin:8px",
	        	 heigth: 600,
	        	 width: 840,
	        	 bodyStyle : 'overflow-y:auto;overflow-x:hidden',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 50,
					labelStyle : 'font-weight:bold'
				},
				items : [
				        {
				        	xtype:'textfield',
		                    name: 'tplid',
		                    hidden: true
				        },
						{
							xtype : 'ueditor',
							fieldLabel : Ext.tzGetResourse("TZ_ONLINE_REG_COM.TZ_ONREG_PRINT_STD.TZ_HEADER","页头"),
							name : 'header'
						},
						{
							xtype : 'ueditor',
							fieldLabel : Ext.tzGetResourse("TZ_ONLINE_REG_COM.TZ_ONREG_PRINT_STD.TZ_FOOTER","页尾"),
							name : 'footer'
						}
						]
	         	}
	         ],
	buttons : [ {
		text : '保存',
		iconCls : "save",
		handler : 'onPrintSave'
	}, {
		text : '确定',
		iconCls : "ensure",
		handler : 'onPrintEnsure'
	}, {
		text : '关闭',
		iconCls : "close",
		handler : 'onPrintClose'
	} ]

});
