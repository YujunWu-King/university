Ext.apply(Ext.form.field.VTypes, {  
  munuEditNameValidator:  function(value,field) {  
  var flag = false;  
  var actType = field.findParentByType("menuEdit").actType;

  Ext.Ajax.request({
     async: false,
     url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GNCDMENU.TZ_GNCD_MENU_FLD.FieldFormula.Iscript_MenuIdVld',
     params: {
        "menuId": value,
        "actType":actType
     },
     async:false,  
		 success: function(response){
				var responseText = eval( "(" + response.responseText + ")" );
				if(responseText.success == "true"){
					flag = true;
				}
			}
	});
  return flag;  
  },  
  munuEditNameValidatorText: '菜单节点编号已经被使用'
});

Ext.define('KitchenSink.view.security.menu.menuEditPanel', {
    extend: 'Ext.panel.Panel',
    //extend: 'Ext.Container',
    xtype: 'menuEdit',
	controller: 'menuEdit',
	requires: [
	    'Ext.data.*',
        //'Ext.grid.*',
        'Ext.util.*',
        //'Ext.toolbar.Paging',
        //'Ext.ux.ProgressBarPager',
	    'KitchenSink.view.security.menu.menuEditController',
        'Ext.data.TreeStore'
	],
    title: '机构功能菜单管理',
    width: 640,
    layout: 'column',
    viewModel: true,
    actType: 'update',
   /* layout: {
        type: 'table',
        columns: 2,
        tdAttrs: { style: 'padding: 10px;' }
    },*/
    /*defaults: {
        xtype: 'treepanel',
        width: 300,
        height: 200,
        rootVisible: false,

        store: 'Files'
    },
    themes: {
        classic: {
        },
        neptune: {
        }
    },
	*/
	
    initComponent: function() {
			//	var store = new KitchenSink.view.security.menu.Files();

        this.items = [
            {
                columnWidth: 0.3,
                margin: "10 5 0 0",
                title: '功能菜单树',
                xtype: 'treepanel',
                width: 220,
                height: 400,
                autoScroll : true,
                lines: true,
								rootVisible: false,
	        			listeners : {  
	            		beforeitemexpand : function(node,eOpts){
	            			try{
	            			 	// var tzParams ='{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENUXX_STD","OperateType":"QF","comParams":{"typeFlag":"NODE","menuId":"'+ node.data.nodeId+'"}}';               	
	                		//	this.getStore().setProxy({
											//			type: 'ajax',
											//			url:  '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_ZSGL_D.TZ_ZSGL.FieldFormula.IScript_TZ_ZSGL?tzParams='+tzParams,
											//			reader: {
											//				type : 'json', 
											//				rootProperty: 'comContent.root.menuTree'
											//			}
											//	});
											this.getStore().getProxy( ).extraParams.nodeId =  node.data.nodeId;
	                	}catch( e){
	                		
	                	}
	            		},
	            		itemclick: function( view , record, item, index, e, eOpts ){
	            			  var form = view.findParentByType("menuEdit").child("form").getForm();
	            			  var rootNode = form.findField("rootNode").getValue();
	            			  var tzParams = '{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENUXX_STD","OperateType":"QF","comParams":{"typeFlag":"FORM","menuId":"'+record.data.nodeId+'"}}';
	            			  Ext.tzLoad(tzParams,function(responseData){
                				//菜单信息数据
           					     var formData = responseData.formData;
                				 form.setValues(formData);
                				 form.findField("rootNode").setValue(rootNode);
         						  });
	            		}
	            	} 
            }, {
                columnWidth: 0.7,
                margin: "10 0 0 5",
                frame: true,
                //title: 'Framed Panel',
               // glyph: 117,
                xtype: 'form',
                reference: 'menuEditForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                height: 400,
                bodyStyle: 'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth:120,
                    labelStyle: 'font-weight:bold'
                },

                items: [{
                    xtype:"toolbar",
                    items:[
                        {text:"插入同级节点",tooltip:"插入同级节点",handler: 'inserMenueItem'},"-",
                        {text:"插入子节点",tooltip:"插入子节点",handler: 'inserChildMenueItem'},"-",
                        {text:"删除",tooltip:"删除",handler: 'removeMenueItem'}
                    ]},
                    {
                        xtype: 'textfield',
                        //fieldLabel: '菜单节点编号',
                        fieldLabel: Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENUXX_STD.menuId", "菜单节点编号"),
                        name: 'menuId',
                        beforeLabelTextTpl: [
                            '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                        ],
                        allowBlank: false,
                        validateOnChange: false,
												validateOnBlur: true,
                        vtype: 'munuEditNameValidator'
                    },
                    {
                        xtype: 'textfield',
                        //fieldLabel: '菜单名称',
                        fieldLabel: Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENUXX_STD.menuName", "菜单名称"),
                        name: 'menuName',
                        beforeLabelTextTpl: [
                            '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                        ],
                        allowBlank: false
                    },
                    {
                        xtype: 'combobox',
                        //fieldLabel: '有效状态',
                        fieldLabel: Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENUXX_STD.menuYxState", "有效状态"),
                        forceSelection: true,
                        valueField: 'TValue',
                        displayField: 'TSDesc',
                        store: new KitchenSink.view.common.store.appTransStore("TZ_YXX"),
                        typeAhead: true,
                        queryMode: 'local',
                        name: 'menuYxState'

                    },
                    {
                        xtype: 'textfield',
                        //fieldLabel: '菜单对应组件ID',
                        fieldLabel: Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENUXX_STD.comId", "菜单对应组件ID"),
                        name: 'comId'
                    },
                    {
                        xtype: 'textfield',
                        //fieldLabel: '菜单大图标ID',
                        fieldLabel: Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENUXX_STD.bigImgId", "菜单大图标ID"),
                        name: 'bigImgId'
                    },
                    {
                        xtype: 'textfield',
                        //fieldLabel: '菜单小图标ID',
                        fieldLabel: Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENUXX_STD.bigImgId", "菜单小图标ID"),
                        name: 'smallImgId'
                    },
                    {
                        xtype: 'textfield',
                        //fieldLabel: '帮助信息内容ID',
                        fieldLabel: Ext.tzGetResourse("TZ_AQ_MENU_COM.TZ_AQ_MENUXX_STD.helpId", "帮助信息内容ID"),
                        name: 'helpId'
                    },
                    {
                    	//插入同级节点还是子节点,Y:表示同级节点，'N'表示子节点;
                        xtype: 'textfield',
                        name: 'NodeType',
                        hidden: true
                    },
                    {
                    	//插入同级节点或子节点是在哪个节点上操作的;
                        xtype: 'textfield',
                        name: 'operateNode',
                        hidden: true
                    },
                    {
                    	//跟节点;
                        xtype: 'textfield',
                        name: 'rootNode',
                        hidden: true
                    }
                ]
            }
        ]

        this.callParent();
    },

    buttons: [{
        text: '复制菜单到当前机构',
        iconCls:"save",
        handler: 'copyFuncMenu'
    },{
        text: '保存',
        iconCls:"save",
        handler: 'onFormSave'
    }, {
			text: '确定',
			iconCls:"ensure",
			handler: 'onFormEnsure'
		}, {
			text: '关闭',
			iconCls:"close",
			handler: 'onFormClose'
		}]
});
