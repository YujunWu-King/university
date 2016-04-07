Ext.define('KitchenSink.view.enrollmentManagement.attributeSet.addformAttrWindow', {
    extend: 'Ext.window.Window',
    xtype: 'addformAttrWindow', 
	controller: 'addformAttrController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.enrollmentManagement.attributeSet.addformAttrController'
	],
    title:  Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.bmfjzd","报名附加字段"),
    reference:'addformAttrWindow',
    width:500,
    y:10,
	actType: 'add',
    modal:true,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        items: [
			{
				xtype: 'numberfield',
				fieldLabel: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.attrSeq","显示顺序"),
				name:'attrSeq',
                value:1000,
				hidden:true
			},{
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.attrValue","属性编号"),
				name:'attrValue',
				//allowBlank: false,
				ignoreChangesFlag: true,
				hidden:true,
				value:'NEXT'
			},
			{ 
				xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.attrDesc","属性名称"),
                maxLength:40,
                name: 'attrDesc',
				allowBlank:false
            },{
				xtype: 'combobox',
				fieldLabel: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.attrType","控件类型"),
				editable:false,
				emptyText:Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.pleaseSelect","请选择"),
				queryMode: 'remote',
				name: 'attrType',
				valueField: 'TValue',
				displayField: 'TSDesc',
				store: new KitchenSink.view.common.store.appTransStore("TZ_CONTROL_TYPE"),
				allowBlank:false
			},{
				xtype: 'combobox',
				fieldLabel:Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.enabled","是否启用") ,
				editable:false,
				queryMode: 'remote',
				name: 'attrEnabled',
				valueField: 'TValue',
				displayField: 'TSDesc',
				store: new KitchenSink.view.common.store.appTransStore("TZ_ATTR_ENABLED"),
				allowBlank:false,
				value:'Y'
			}
		]
    }],
    buttons: [{
		text:  Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.save","保存"),
		iconCls:"save",
		handler: function(btn){
			//获取窗口
			var win = btn.findParentByType("window");
			var form = win.child("form").getForm();
			win.doSave(win);
		}
	},{
		text:Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.ensure","确定"),
		iconCls:"ensure",
		//handler: 'ensureAttr'
		handler: function(btn){
		
			var win = btn.findParentByType("addformAttrWindow");
			
			var form = win.child("form").getForm();
			
			var grid = btn.findParentByType("appFormAttributeSet");

			if(!form.isValid()){
				return false;
			}
			//form datas
			var formParams = form.getValues();
			//Params			
			var tzParams = '{"ComID":"TZ_BMGL_SXSZ_COM","PageID":"TZ_BMSX_NEW_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
			Ext.tzSubmit(tzParams,function(response){
				var attrValue=response.attrValue;
				form.setValues({"attrValue":attrValue});
                grid.store.reload();
                win.close();
			},"",true,win);
		}
	}, {
		text: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.close","关闭"),
		iconCls:"close",
		handler: function(btn){
			var win = btn.findParentByType("window");
			var form = win.child("form").getForm();
			win.close();
		}
	}],
	doSave:function(win){
		//保存
		var form = win.child("form").getForm();
		if(!form.isValid()){
			return false;
		}
		var formParams = form.getValues();
		var tzParams = '{"ComID":"TZ_BMGL_SXSZ_COM","PageID":"TZ_BMSX_NEW_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
		Ext.tzSubmit(tzParams,function(response){
			var attrValue=response.attrValue;
			form.setValues({"attrValue":attrValue});
            win.findParentByType("appFormAttributeSet").store.reload();
		},"",true,this);
		
	}
});
