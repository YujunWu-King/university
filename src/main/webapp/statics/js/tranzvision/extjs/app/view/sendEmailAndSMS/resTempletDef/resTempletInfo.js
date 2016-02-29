Ext.define('KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'resTempletInfo', 
	controller: 'resTempletInfoMth',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletInfoMth',
		'KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletParaInfoStore',
		'KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletContentInfoStore'
	],

    title: '函件元模板定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    initComponent: function () {
        var useFlagStore = new KitchenSink.view.common.store.appTransStore("TZ_USE_FLAG")
        Ext.apply(this, {
            items: [{
                xtype: 'form',
                reference: 'resTempletForm',
                layout: {
                    type: 'vbox',       // Arrange child items vertically
                    align: 'stretch'    // 控件横向拉伸至容器大小
                },
                border: false,
                bodyPadding: 10,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 200,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.restempid","元模板编号"),
                    name: 'restempid',
                    value:'NEXT',
                    readOnly:true,
                    cls:'lanage_1'
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.restempname","元模板名称"),
                    name: 'restempname',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ],
                    allowBlank: false
                },
                    {
                        xtype: 'tabpanel',
                        frame: true,
                        border:false,
                        items:[
                            {
                                title:'基本信息',
                                items:[{
                                    layout: {
                                        type: 'vbox',       // Arrange child items vertically
                                        align: 'stretch'    // 控件横向拉伸至容器大小
                                    },
                                    border:false,
                                    bodyPadding: 10,
                                    bodyStyle:'overflow-y:auto;overflow-x:hidden',
                                    fieldDefaults: {
                                        msgTarget: 'side',
                                        labelWidth: 120,
                                        labelStyle: 'font-weight:bold'
                                    },
                                    items:[{
                                        xtype: 'combobox',
                                        fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.restemporg","机构"),
                                        forceSelection: true,
                                        editable: false,
                                        store: new KitchenSink.view.common.store.comboxStore({
                                            recname: 'TZ_JG_BASE_T',
                                            /*condition:{
                                             "TZ_JG_EFF_STA":{
                                             "value":"Y",
                                             "operator":"01",
                                             "type":"01"
                                             }
                                             },*/
                                            condition:{
                                                TZ_JG_EFF_STA:{
                                                    value:"Y",
                                                    operator:"01",
                                                    type:"01"
                                                }
                                            },
                                            result:'TZ_JG_ID,TZ_JG_NAME'
                                        }),
                                        value:Ext.tzOrgID,
                                        valueField: 'TZ_JG_ID',
                                        displayField: 'TZ_JG_NAME',
                                        //typeAhead: true,
                                        queryMode: 'local',
                                        name: 'restemporg',
                                        afterLabelTextTpl: [
                                            '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                        ],
                                        allowBlank: false,
                                        listeners:{
                                            select: function(combo,records,eOpts){

                                                var form = this.findParentByType("form").getForm();
                                                var tzParams = '{"restemporg":"' + combo.value + '"}';
                                                tzParams = '{"ComID":"TZ_RES_TMPL_MG_COM","PageID":"TZ_RES_TMPL_MG_STD","OperateType":"QF","comParams":' + tzParams +'}';

                                                Ext.Ajax.request({
                                                    url: Ext.tzGetGeneralURL,
                                                    params: {
                                                        tzParams: tzParams
                                                    },
                                                    success: function(response){

                                                        var responseText = Ext.util.JSON.decode(response.responseText);

                                                        var tempemailserv = responseText.comContent.tempemailserv;
                                                        var tempsmsserv = responseText.comContent.tempsmsserv;
                                                        var emailaddr = responseText.comContent.emailaddr;
                                                        var smssevname = responseText.comContent.smssevname;

                                                        //form.findField("tempemailserv").setValue(tempemailserv);
                                                        //form.findField("emailaddr").setValue(emailaddr);
                                                        //form.findField("tempsmsserv").setValue(tempsmsserv);
                                                        //form.findField("smssevname").setValue(smssevname);
														var initValues = {
															"tempemailserv":tempemailserv,
															"emailaddr":emailaddr,
															"tempsmsserv":tempsmsserv,
															"smssevname":smssevname
														}
														form.setValues(initValues);

                                                    },
                                                    failure: function (response) {
                                                        //Ext.MessageBox.alert("错误", "错误");
                                                    }
                                                });
                                            }
                                        }
                                    },{
                                        xtype: 'combobox',
                                        fieldLabel: Ext.tzGetResourse('TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.isneed','是否启用'),
                                        forceSelection: true,
                                        editable:false,
                                        store: useFlagStore,
                                        valueField: 'TValue',
                                        displayField: 'TSDesc',
                                        queryMode: 'remote',
                                        name: 'isneed',
                                        afterLabelTextTpl: [
                                            '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                        ],
                                        allowBlank: false
                                    },{
                                        xtype: 'combobox',
                                        fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.isDefaultOpen","默认开通模版"),
                                        name:'isDefaultOpen',
                                        value:'Y',
                                        forceSelection: true,
                                        editable:false,
                                        store: useFlagStore,
                                        valueField: 'TValue',
                                        displayField: 'TSDesc',
                                        queryMode: 'remote'
                                    },{
                                        xtype: 'combobox',
                                        fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.isExtendChildTmpl","机构可扩展子模版"),
                                        name:'isExtendChildTmpl',
                                        value:'Y',
                                        forceSelection: true,
                                        editable:false,
                                        store: useFlagStore,
                                        valueField: 'TValue',
                                        displayField: 'TSDesc',
                                        queryMode: 'remote'
                                    },{
                                        xtype: 'combobox',
                                        fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.ispersonalize","是否包含动态个性化内容"),
                                        name:'ispersonalize',
                                        forceSelection: true,
                                        editable:false,
                                        store: useFlagStore,
                                        valueField: 'TValue',
                                        displayField: 'TSDesc',
                                        queryMode: 'remote'
                                    },{
                                        layout: {
                                            type: 'column'
                                        },
                                        items:[{
                                            columnWidth:.5,
                                            xtype: 'textfield',
                                            fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.tempemailserv","邮箱服务配置"),
                                            name: 'tempemailserv',
                                            editable: false,
                                            triggers: {
                                                search: {
                                                    cls: 'x-form-search-trigger',
                                                    handler: "pmtSearchEmlServ"
                                                }
                                            }
                                        },{
                                            columnWidth:.5,
                                            xtype: 'displayfield',
                                            hideLabel: true,
                                            name: 'emailaddr',
                                            style:'margin-left:8px'
                                        }]
                                    },{
                                        layout: {
                                            type: 'column'
                                        },
                                        items:[{
                                            columnWidth:.5,
                                            xtype: 'textfield',
                                            fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.tempsmsserv","短信服务配置"),
                                            name: 'tempsmsserv',
                                            editable: false,
                                            triggers: {
                                                search: {
                                                    cls: 'x-form-search-trigger',
                                                    handler: "pmtSearchSmsServ"
                                                }
                                            }
                                        },{
                                            columnWidth:.5,
                                            xtype: 'displayfield',
                                            hideLabel: true,
                                            name: 'smssevname',
                                            style:'margin-left:8px'
                                        }]
                                    },{
                                        xtype: 'textarea',
                                        fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.baseinfodesc","描述"),
                                        name:'baseinfodesc'
                                    }]
                                }]
                            },
                            {
                                title:'模板参数列表',
                                items:[
                                    {
                                        layout: {
                                            type: 'vbox',       // Arrange child items vertically
                                            align: 'stretch'    // 控件横向拉伸至容器大小
                                        },
                                        border:false,
                                        bodyStyle:'overflow-y:auto;overflow-x:hidden',
                                        fieldDefaults: {
                                            msgTarget: 'side',
                                            labelWidth: 120,
                                            labelStyle: 'font-weight:bold'
                                        },
                                        items:[{
                                            xtype: 'textfield',
                                            fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.tempclassalias","模板参数类别名"),
                                            name: 'tempclassalias',
                                            margin:10
                                        },{
                                            xtype: 'grid',
                                            height: 300,
                                            border:false,
                                            selModel: {
                                                type: 'checkboxmodel'
                                            },
                                            multiSelect: true,
                                            viewConfig: {
                                                enableTextSelection: true
                                            },
                                            name: 'resTmplRaraGrid',
                                            reference: 'resTmplRaraGrid',
                                            store: {
                                                type: 'resTempletParaInfoStore'
                                            },
                                            dockedItems:[{
                                                xtype:"toolbar",
                                                items:[
                                                    {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addParaRow'},"-",
                                                    {text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editSelParaRow'},"-",
                                                    {text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteSelParaRow'}
                                                ]
                                            }],
                                            columnLines: true,    //显示纵向表格线
                                            /*
                                             plugins:[             //可编辑单元格
                                             Ext.create('Ext.grid.plugin.CellEditing', {
                                             clicksToEdit: 1   //单击进行编辑
                                             })
                                             ],*/
                                            columns: [{
                                                text: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.restempid","元模版编号"),
                                                sortable: true,
                                                dataIndex: 'restempid',
                                                hidden:true
                                            },{
                                                text: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.restemporg","机构"),
                                                sortable: true,
                                                dataIndex: 'restemporg',
                                                hidden:true
                                            },{
                                                text: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.paraid","参数名称"),
                                                sortable: true,
                                                dataIndex: 'paraid',
                                                editor:{
                                                    xtype:'textfield',
                                                    allowBlank:false,
                                                    editable: false
                                                },
                                                width: 150
                                            },{
                                                text: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.paraname","参数中文名"),
                                                sortable: true,
                                                dataIndex: 'paraname',
                                                width: 170
                                            },{
                                                text: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.paraalias","参数别名"),
                                                sortable: true,
                                                dataIndex: 'paraalias',
                                                editor:{
                                                    xtype:'textfield',
                                                    allowBlank:false
                                                },
                                                width: 170
                                            },{
                                                text: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.systvar","系统变量"),
                                                sortable: true,
                                                dataIndex: 'systvar',
                                                editor:{
                                                    xtype:'textfield',
                                                    allowBlank:false,
                                                    editable: false
                                                },
                                                width: 150
                                            },{
                                                text: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.systvarname","系统变量名称"),
                                                sortable: true,
                                                dataIndex: 'systvarname',
                                                minWidth: 150,
                                                flex:1
                                            },{
                                                menuDisabled: true,
                                                sortable: false,
                                                width:50,
                                                xtype: 'actioncolumn',
                                                items:[
                                                    {iconCls: 'edit',tooltip: '编辑',handler:'editParaRow'},
                                                    {iconCls: 'remove',tooltip: '删除',handler:'deleteParaRow'}
                                                ]
                                            }]
                                        }

                                        ]
                                    }]
                            },
                            {
                                title:'模板内容采集规则',
                                hidden:true,
                                items:[{
                                    layout: {
                                        type: 'vbox',       // Arrange child items vertically
                                        align: 'stretch'    // 控件横向拉伸至容器大小
                                    },
                                    border: 1,
                                    bodyPadding: 10,
                                    bodyStyle:'overflow-y:auto;overflow-x:hidden',
                                    fieldDefaults: {
                                        msgTarget: 'side',
                                        labelWidth: 120,
                                        labelStyle: 'font-weight:bold'
                                    },
                                    items:[
                                        {
                                            xtype: 'radio',
                                            boxLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.restempopra1","手工编辑模板内容"),
                                            name: 'restempopra',
                                            inputValue:'A'
                                        },{
                                            xtype: 'textfield',
                                            fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.recname","表名"),
                                            name:'recname'
                                        },{
                                            xtype: 'textfield',
                                            fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.recalias","别名"),
                                            name:'recalias'
                                        },
                                        {
                                            xtype: 'grid',
                                            height: 360,
                                            frame: true,
                                            selModel: {
                                                type: 'checkboxmodel'
                                            },
                                            multiSelect: true,
                                            viewConfig: {
                                                enableTextSelection: true
                                            },
                                            name: 'resTmplContentGrid',
                                            reference: 'resTmplContentGrid',

                                            store: {
                                                type: 'resTempletContentInfoStore'
                                            },

                                            dockedItems:[{
                                                xtype:"toolbar",
                                                items:[
                                                    {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addContentRow'},"-",
                                                    {text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editSelContentRow'},"-",
                                                    {text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteSelContentRow'}
                                                ]
                                            }],
                                            columnLines: true,    //显示纵向表格线

                                            columns: [{
                                                text: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.keyname","关键字"),
                                                sortable: true,
                                                dataIndex: 'keyname',
                                                editor:{
                                                    xtype:'textfield',
                                                    allowBlank:false,
                                                    editable: false
                                                },
                                                width: 200
                                            },{
                                                text: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.contentparaid","对应取值的模板参数"),
                                                sortable: true,
                                                dataIndex: 'paraid',
                                                editor:{
                                                    xtype:'textfield',
                                                    allowBlank:false,
                                                    editable: false
                                                },
                                                width: 200
                                            },{
                                                text: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.contentparaname","参数描述"),
                                                sortable: true,
                                                dataIndex: 'paraname',
                                                minWidth: 200,
                                                flex:1
                                            },{
                                                menuDisabled: true,
                                                sortable: false,
                                                width:40,
                                                xtype: 'actioncolumn',
                                                items:[
                                                    {iconCls: 'edit',tooltip: '编辑',handler:'editContentRow'},
                                                    {iconCls: 'remove',tooltip: '删除当前行数据',handler:'deleteContentRow'}
                                                ]
                                            }]
                                        },
                                        {
                                            xtype: 'radio',
                                            boxLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.restempopra2","通过AppClass取值"),
                                            name: 'restempopra',
                                            inputValue:'B'
                                        },{
                                            layout: {
                                                type: 'column'
                                            },
                                            bodyStyle:'padding:0 0 10px 0',
                                            items:[{
                                                columnWidth:.4,
                                                xtype: 'textfield',
                                                fieldLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.contclassid","应用程序类ID"),
                                                name: 'contclassid',
                                                editable: true
                                            },{
                                                columnWidth:.1,
                                                xtype:'button',
                                                minWidth:35,
                                                maxWidth:35,
                                                iconCls: "query",
                                                style:{
                                                    marginLeft:'5px',
                                                    marginRight:'10px'
                                                },
                                                handler:'pmtSearchResAppClass'
                                            },{
                                                columnWidth:.5,
                                                xtype: 'displayfield',
                                                hideLabel: true,
                                                name: 'contclassName'
                                            }]
                                        },{
                                            xtype: 'radio',
                                            boxLabel: Ext.tzGetResourse("TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_STD.restempopra3","其他"),
                                            name: 'restempopra',
                                            checked:true,
                                            inputValue:'C'
                                        }
                                    ]}]
                            }
                        ]
                    }]
            }]
        });
        this.callParent();
    },
    buttons: [{
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



