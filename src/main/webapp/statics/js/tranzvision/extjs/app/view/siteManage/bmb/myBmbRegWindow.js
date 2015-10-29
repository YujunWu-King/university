Ext.define('KitchenSink.view.template.bmb.myBmbRegWindow', {
    extend: 'Ext.window.Window',
    xtype: 'myBmbRegWindow', 
    title: '新建模板', 
	closable: true,
	closeAction: 'hide',
	modal :true,
	autoScroll : true,
	bodyStyle: 'padding: 5px;',
	actType: 'add',
 
	items: [{
		xtype: 'tabpanel',	
	config: {  
        fields: undefined  
    },
    height: 400,
    width: 800,

    ui: 'navigation',
    tabPosition: 'left',
    tabRotation: 0,
    tabBar: {
        border: false
    },

    defaults: {
        textAlign: 'left',
        bodyPadding: 15
    },
		items: [{
				itemId: 'add',
				title: '新建空白模板',
				glyph: 72,
				html:(function(){
					var divTmp = document.createElement("div");
					var txt = Ext.create('Ext.form.field.Text', {
						 fieldLabel: '模板名称',
						 labelStyle: 'font-weight:bold',
						 cls:'bmb_blank_text',
						 allowBlank: false
					}).render(divTmp)
					return divTmp.outerHTML;
				})()+'<div>空白模板</div>'
			},
			{
				itemId: 'predefine',
				title: '使用预定义模板',
				glyph: 117,
				html: (function(){
					var divTmp = document.createElement("div");
					var txt = Ext.create('Ext.form.field.Text', {
						 fieldLabel: '模板名称',
						 labelStyle: 'font-weight:bold',
						 cls:'bmb_predefine_text',
						 allowBlank: false
					}).render(divTmp)
					return divTmp.outerHTML;
				})() + '<div class="predefinetpllist">asdf</div>'
			}]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onBmbRegSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onBmbRegEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onBmbRegClose'
	}]
});
