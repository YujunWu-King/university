Ext.define('KitchenSink.view.advertisementTmplDefn.AdverTmplInfo', {
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.Ueditor',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.advertisementTmplDefn.AdverTmplController'
    ],
    extend : 'Ext.panel.Panel',
    controller:'adTmplMgController',
    autoScroll:false,
    actType:'add',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    title:Ext.tzGetResourse("TZ_AD_TMPL_COM.TZ_AD_INFO_STD.adtmpldefn","广告位模板定义"),
    frame:true,
    initComponent:function(){
        Ext.apply(this,{
            items:[{
                xtype:'form',
                frame:true,
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 100,
                    labelStyle: 'font-weight:bold'
                },
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                margin:'8px',
                style:'border:0px',
                items: [/*{
        			xtype: 'textfield',
        			fieldLabel:Ext.tzGetResourse("TZ_AD_TMPL_COM.TZ_AD_INFO_STD.jgid","机构"),
        			hidden:true,
        			name: 'adJgId',
        			value:Ext.tzOrgID
        		},*/{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_AD_TMPL_COM.TZ_AD_INFO_STD.adcertTmplId","广告位模板编号"),
                    labelWidth: 110,
                    name: 'adcertTmpl',
                    allowBlank: false,
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ],
                    cls:'lanage_1'
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_AD_TMPL_COM.TZ_AD_INFO_STD.adtmplName","广告位名称"), 
                    labelWidth: 110,
                    name: 'adtmplName',
                    allowBlank: false,
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]

                },{
					xtype: 'combo',
					labelWidth: 110,
					editable: false,
					fieldLabel:Ext.tzGetResourse("TZ_AD_TMPL_COM.TZ_AD_INFO_STD.useFlag","是否启用"),
					name: 'useFlag',
					emptyText:Ext.tzGetResourse("TZ_AD_TMPL_COM.TZ_AD_INFO_STD.chose","请选择"),
					mode: "remote",
					valueField: 'TValue',
					displayField: 'TSDesc',
					store: new KitchenSink.view.common.store.appTransStore("TZ_USE_FLAG")
				},{
                        xtype: 'tabpanel',
                        frame: true,
                        activeTab: 0,
                        plain: false,
                        resizeTabs: true,
                        defaults: {
                            autoScroll: false
                        },
                        items:[{
                                title:Ext.tzGetResourse("TZ_AD_TMPL_COM.TZ_AD_INFO_STD.adcertMergHtml1","广告位HTML"),
                                xtype:'form',
                                name:'adcertMergHtml',
                                layout: {
                                    type: 'vbox',
                                    align: 'stretch'
                                },
                                height: 415,
                                style:'border:0',
                                items:[{
                                    xtype: 'ueditor',
                                    name: 'adcertMergHtml',
                                    zIndex:999,
                                    height: 415,
                                    allowBlank: true

                                }]
                            },{
                                title:Ext.tzGetResourse("TZ_AD_TMPL_COM.TZ_AD_INFO_STD.xmlb","项目列表"),
                                xtype:'form',
                                name:'xmlb',
                                layout: {
                                    type: 'vbox',
                                    align: 'stretch'
                                },
                                height: 415,
                                style:'border:0',
                                items:[{
                                	 xtype: 'grid',
                                     columnLines: true,
                                     style:"margin:8px",
                                     selModel: {
                                     type: 'checkboxmodel'
                                    },
                                     columns:[{
                                     	text:Ext.tzGetResourse("TZ_AD_TMPL_COM.TZ_AD_LIST_STD.xmbh","项目编号") ,
                                        dataIndex: 'xmid',
                                        width: 200
                                     },
                                     {
                                     	text:Ext.tzGetResourse("TZ_AD_TMPL_COM.TZ_AD_LIST_STD.xmName","项目名称") ,
                                        dataIndex: 'xmName',
                                        width: 200
                                     }
                                     ]
                                                                     
                                }]
                            }]
                    }]
            }],
            buttons:[
                /*{
                    text: '预览',
                    handler:'seeTmplDfn',
                    iconCls:'preview'
                },*/
                {
                    text: Ext.tzGetResourse("TZ_AD_TMPL_COM.TZ_AD_INFO_STD.save","保存"),
                    handler:'onschoolSave',
                    iconCls:'save'
                },{
                    text:Ext.tzGetResourse("TZ_AD_TMPL_COM.TZ_AD_INFO_STD.sure","确定"),
                    handler:'ensureonschoolSave',
                    iconCls:'ensure'
                },{
                    text:Ext.tzGetResourse("TZ_AD_TMPL_COM.TZ_AD_INFO_STD.close","关闭"),
                    iconCls:'close',
                    handler:'onSchoolClose'
                }
            ]
        });
        this.callParent();
    }
});





