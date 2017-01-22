Ext.define('KitchenSink.view.schoolLibManagement.schoolLibMannage.schoolLibManageInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'schoolMgInfo', 
	controller: 'schoolMgConter',
	actType:'add',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
	    'KitchenSink.view.schoolLibManagement.schoolLibMannage.schoolLibManageController',
        'KitchenSink.view.common.store.appTransStore'
	],
    title: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.schoolLibInfo","院校库详情"), 
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'schoolInform',
				layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 130,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.orgId","机构代码"), 
						name: 'orgaID',
						readOnly: true,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            cls:'lanage_1'
            
        }, {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.ZHSName","中文名称"),
						name: 'chinaName',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        }, {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.ENGName","英文名称"),
						name: 'engName',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        },
        {
            layout: {
                type: 'column'
            },
            //bodyStyle:'padding:0 0 10px 0',
            items:[{
                columnWidth:.4,
                xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.country","国家"),
                //fieldLabel:'报名流程模版',
                name: 'country',
                editable: false,
                triggers: {
                    search: {
                        cls: 'x-form-search-trigger',
                        handler: "schoollibCountryChoose"
                    }
                }
            },{
                columnWidth:.6,
                xtype: 'displayfield',
                hideLabel: true,
                style:'margin-left:5px',
                name: 'countryID'
            }]
        }, {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.maindepartment","主管部门"),
						name: 'mainDeart',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        },
         {
            xtype: 'textfield',
						fieldLabel:  Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.city","所在城市"),
						name: 'city'
				
        },
        
        {
                    xtype: 'combobox',
                    fieldLabel:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.Level","办学层次"),
                    forceSelection: true,
                    allowBlank: false,
                    valueField: 'TValue',
                    displayField: 'TSDesc',
                    store: new KitchenSink.view.common.store.appTransStore("TZ_BXCJ_ZH"),
                    typeAhead: true,
                    queryMode: 'local',
                    name: 'level'
                
        },{
                    xtype: 'combobox',
                    fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.attribute","属性"),
                    forceSelection: true,
                    allowBlank: false,
                    valueField: 'TValue',
                    displayField: 'TSDesc',
                    store: new KitchenSink.view.common.store.appTransStore("TZ_SCH_SX"),
                    typeAhead: true,
                    queryMode: 'local',
                    name: 'attriBute'
                
                },
        {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_INFO_STD.dec","备注"),
						name: 'adddec'
						
        },
        
        {
                    xtype: 'combobox',
                    fieldLabel:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.State","洲"),
                    forceSelection: true,
                    allowBlank: false,
                    valueField: 'TValue',
                    displayField: 'TSDesc',
                    store: new KitchenSink.view.common.store.appTransStore("TZ_STATE_CHOSE"),
                    typeAhead: true,
                    queryMode: 'local',
                    name: 'state'
                    
              
                },
        {
           	xtype: 'combo',
            fieldLabel: Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.type","所属类别"),
			name: 'type',
			emptyText:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.pleaseSelect","请选择..."),
            queryMode: 'remote',
            editable:false,
			valueField: 'TZ_SCHOOL_TYPEID',
    		displayField: 'TZ_SCHOOL_TYPENAME',
			store:new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_SCHOOL_TYPE_TBL',
				condition:{
					TZ_SCHOOL_FLAG:{
						value: 'Y',
							operator:"01",
							type:"01"
					}
				},
				result:'TZ_SCHOOL_TYPEID,TZ_SCHOOL_TYPENAME'
			})

        },
        {
                    xtype: 'combobox',
                    fieldLabel:Ext.tzGetResourse("TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.hemisphere","所在半球"),
                    forceSelection: true,
                    allowBlank: false,
                    valueField: 'TValue',
                    displayField: 'TSDesc',
                    store: new KitchenSink.view.common.store.appTransStore("TZ_BQGS_XZ"),
                    typeAhead: true,
                    queryMode: 'local',
                    name: 'hemiHere'
                 /*   listeners: {
                        select: function (combo, record, index) {
                            form = combo.findParentByType("form").getForm();
                            // A:按站点发布   B:按栏目发布
                            if (combo.getValue() == "A") {
                                form.findField("coluId").hide();
                                form.findField("coluName").hide();
                                form.findField("siteId").show();
                                form.findField("siteName").show();
                            } else {
                                form.findField("coluId").show();
                                form.findField("coluName").show();
                                form.findField("siteId").hide();
                                form.findField("siteName").hide();
                            }
                        }
                    }*/
                }
        ]
    }],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onschoolSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'ensureonschoolSave'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onSchoolClose'
	}]
});
