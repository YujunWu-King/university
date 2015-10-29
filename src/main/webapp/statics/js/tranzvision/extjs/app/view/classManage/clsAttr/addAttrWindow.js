Ext.define('KitchenSink.view.classManage.clsAttr.addAttrWindow', {
    extend: 'Ext.window.Window',
    xtype: 'addAttrWindow', 
	controller: 'addAttrController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.classManage.clsAttr.addAttrController'
	],
    title: Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_NEW_STD.bjgxhsx","班级个性化属性"),
    reference:'addAttrWindow',
    width:500,
    modal:true,
	actType: 'add',
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
				//xtype: 'numberfield',
				xtype: 'hidden',
				fieldLabel: Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_NEW_STD.attrSeq","显示顺序"),
				name:'attrSeq',
				ignoreChangesFlag: true,
				allowBlank: false
			},{
				xtype: 'textfield',
				fieldLabel: Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_NEW_STD.attrValue","属性编号"),
				name:'attrValue',
				//allowBlank: false,
				ignoreChangesFlag: true,
				hidden:true,
				value:'NEXT'
			},
			{ 
				xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_NEW_STD.attrDesc","属性名称"),
                name: 'attrDesc',
				allowBlank:false,
				ignoreChangesFlag: true
            },{
				xtype: 'combobox',
				fieldLabel:Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_NEW_STD.comType","控件类型"),
				editable:false,
				queryMode: 'remote',
				name: 'attrType',
				valueField: 'TValue',
				displayField: 'TSDesc',
				ignoreChangesFlag: true,
				store: new KitchenSink.view.common.store.appTransStore("TZ_CONTROL_TYPE"),
				allowBlank:false
			},{
				xtype: 'combobox',
				fieldLabel:Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_NEW_STD.enabled","是否启用"),
				editable:false,
				queryMode: 'remote',
				name: 'attrEnabled',
				valueField: 'TValue',
				displayField: 'TSDesc',
				ignoreChangesFlag: true,
				store: new KitchenSink.view.common.store.appTransStore("TZ_ATTR_ENABLED"),
				allowBlank:false,
				value:'Y'
			}
		]
    }],
    buttons: [{
		text:Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_NEW_STD.save","保存"),
		iconCls:"save",
		//handler: 'saveAttr',
		handler: function(btn){
			//获取窗口
			var win = btn.findParentByType("window");
			//修改密码信息表单
			var form = win.child("form").getForm();
			//重置表单
			
			//关闭窗口
			//win.close();
			win.doSave(win);

		}
	},{
		text:Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.ensure","确定"),
		iconCls:"ensure",
		//handler: 'ensureAttr'
		handler: function(btn){
		
			var win = btn.findParentByType("addAttrWindow");
			
			var form = win.child("form").getForm();
			
			var grid = btn.findParentByType("individuationSet");//.getview().child("grid");
			
			
			
			if(!form.isValid()){
				return false;
			}
			//form datas
			var formParams = form.getValues();
			//Params			
			var tzParams = '{"ComID":"TZ_GD_BJSX_COM","PageID":"TZ_GD_BJSX_NEW_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
			Ext.tzSubmit(tzParams,function(response){
				var attrValue=response.attrValue;
				form.setValues({"attrValue":attrValue});
				win.close();
				
				grid.store.reload();
				
			},"",false,this);
			//this.getView().close();

			
		}
	}, {
		text: Ext.tzGetResourse("TZ_GD_BJSX_COM.TZ_GD_BJSX_STD.close","关闭"),
		iconCls:"close",
		//handler: 'closeAttr',
		handler: function(btn){
			//获取窗口
			var win = btn.findParentByType("window");
			//修改密码信息表单
			var form = win.child("form").getForm();
			//关闭窗口
			win.close();
		}
	}],
	doSave:function(win){
		//保存
		var form = win.child("form").getForm();
		if(!form.isValid()){
			return false;
		}
		//form datas
		var formParams = form.getValues();
		//Params	
				
		var tzParams = '{"ComID":"TZ_GD_BJSX_COM","PageID":"TZ_GD_BJSX_NEW_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
		Ext.tzSubmit(tzParams,function(response){
			var attrValue=response.attrValue;
			form.setValues({"attrValue":attrValue});
		},"",false,this);
		
	}
});
