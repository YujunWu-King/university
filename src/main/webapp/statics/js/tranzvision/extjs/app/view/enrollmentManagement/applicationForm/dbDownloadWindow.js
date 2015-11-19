Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.dbDownloadWindow', {
    extend: 'Ext.window.Window',
    xtype: 'zldbWindow0',
	controller: 'appFormPackage',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.applicationForm.dbDownloadStore',
        'KitchenSink.view.enrollmentManagement.applicationForm.appFormPackageController'
	],
    modal:true,//背景遮罩
    header:false,
    minHeight: 150,
    maxHeight: 400,
    resizable:false,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'update',//默认更新
    y:25,
    initComponent: function(){
		var processingStatus = new KitchenSink.view.common.store.appTransStore("TZ_AE_STATUS");
		var listStore = new KitchenSink.view.enrollmentManagement.applicationForm.dbDownloadStore();
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'cldbForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                //heigth: 600,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelStyle: 'font-weight:bold'
                },

                items: [
                    {
                        xtype: 'tabpanel',
                        reference:'packageTabPanel',
                        activeTab: 0,
                        frame: false,
                        header:false,
                        width: 650,
                        minHeight: 200,
                        maxHeight: 400,
                        resizeTabs: true,
                        defaults: {
                            autoScroll: false
                        },
                        listeners: {
                            tabchange: function (tp, p) {
                                if (p.reference== 'dbDownloadGrid') {
                                    this.doLayout();
                                }
                            }

                        },
                        items: [
                            {
                                title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.materialsPacking","材料打包"),
                                xtype: 'form',
                                frame:false,
                                minHeight:150,
                                autoHeight:true,
                                reference: 'zldb0Form',
                                border: false,
                                bodyPadding: 10,
                                buttons: [{
                                        text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
                                        iconCls:"close",
                                        handler: 'qxZldb'
                                    }],
                                fieldDefaults: {
                                    msgTarget: 'side',
                                    labelStyle: 'font-weight:bold'
                                },
                                layout: {
                                    type: 'vbox',
                                    align: 'stretch'
                                },
                                items: [
                                    {
                                        xtype: 'label',
                                        style:'margin-top:20px',
                                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.inputPackageName","请输入压缩包文件名")
                                    },
                                    {
                                        layout:{
                                            type:'column'
                                        },
                                        items:[{
                                            xtype: 'textfield',
                                            name: 'ysFilesName',
                                            allowBlank: false,
                                            columnWidth:.8
                                        },{
                                            xtype:"button",
                                            width:120,
                                            style:'margin-left:8px',
                                            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.ensurePackage","确认打包"),
                                            handler: 'qrZldb',
                                            labelAlign: 'right',
                                            buttonAlign: 'left',
                                            columnWidth:.2
                                        }]
                                    },
                                    {
                                        xtype: 'textfield',
                                        name: 'appInsID',
                                        hidden: true
                                    }
                                ]
                            },
                            {
                                title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.packageResult","材料打包结果"),
                                xtype: 'grid',
                                autoHeight: true,
                                frame:false,
                                minHeight:200,
                                columnLines: true,
                                reference: 'dbDownloadGrid',
                                multiSelect: true,
                                store: listStore,
                                dockedItems: [{
                                    xtype: "toolbar",
                                    items: [
                                        {text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.query","查询"),  iconCls: "query",handler:"db0Query"},
                                        '-',
                                        {text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.delete","删除"),  iconCls: "remove",handler:"db0Delete"}
                                    ]
                                },{
                                    xtype:"toolbar",
                                    dock:"bottom",
                                    ui:"footer",
                                    items:['->',
                                        {minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.save","保存"),iconCls:"save",handler:'db0Save'}
                                        ,{minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),iconCls:"close",handler:'qxZldb'}]
                                }],
                                selModel: {
                                    type: 'checkboxmodel'
                                },

                                columns: [
                                    {text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_GD_BMGL_DBDL_CLS.fileName", "文件名称"),
                                        dataIndex: 'fileName',
                                        minWidth:100,
                                        flex:1
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_GD_BMGL_DBDL_CLS.czPerName", "操作人"),
                                        dataIndex: 'czPerName',
                                        width: 80
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_GD_BMGL_DBDL_CLS.bgTime", "打包时间"),
                                        dataIndex: 'bgTime',
                                        width: 180
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_GD_BMGL_DBDL_CLS.AEState", "状态"),
                                        dataIndex: 'AEState',
                                        width: 100,
										renderer:function(v){
											if(v){
												var rec = processingStatus.find('TValue',v,0,false,true,false);
												if(rec>-1){
													return processingStatus.getAt(rec).get("TSDesc");
												}else{
													return "";
												}
											}else{
												return "";
											}
										}
                                    },
                                    {
                                        xtype:'actioncolumn',
                                        header:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_GD_BMGL_DBDL_CLS.download", "下载"),
                                        align:'center',
                                        items:[
                                            {
                                            iconCls:'download',
                                            sortable:false,
                                            handler: "downloadFile"
                                        }
                                        ],
                                        width:60
                                    }
                                ],
                                bbar: {
                                    xtype: 'pagingtoolbar',
                                    pageSize: 5,
                                    listeners: {
                                        afterrender: function (pbar) {
                                            var grid = pbar.findParentByType("grid");
                                            pbar.setStore(grid.store);
                                        }
                                    },
                                    plugins: new Ext.ux.ProgressBarPager()
                                }
                            }
                        ]
                    }
                ]


            }]
        });
        this.callParent();
    }
});
