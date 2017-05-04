Ext.define('KitchenSink.view.activity.applicants.exportExcelWindow', {
    extend: 'Ext.window.Window',
    xtype: 'bmrExportExcelWin',
	controller: 'applicantsMg',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.activity.applicants.exportExcelStore',
	],
    modal:true,//背景遮罩
    header:false,
    minHeight: 150,
    maxHeight: 400,
    resizable:false,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    y:80,
    
    constructor: function(config){
    	this.activityId = config.activityId;
    	this.exportObj = config.expBmr;
    	
    	this.hiddenPage0 = false;
    	if(config.type == "download"){
    		this.hiddenPage0 = true;
    	}
    	
    	this.callParent();
    },
    
    initComponent: function(){
    	var me = this;
    	var activityId = this.activityId;
		var processingStatus = new KitchenSink.view.common.store.appTransStore("TZ_AE_STATUS");
		
		var listStore = new KitchenSink.view.activity.applicants.exportExcelStore(activityId);
		
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'bmrExportForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
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
                                if (p.reference== 'downloadGrid') {
                                    this.doLayout();
                                }
                            }

                        },
                        items: [
                            {
                                title: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.bmrInfoExport","报名人导出"),
                                xtype: 'form',
                                frame:false,
                                minHeight:150,
                                autoHeight:true,
                                reference: 'exportInfoForm',
                                border: false,
                                hidden: me.hiddenPage0,
                                bodyPadding: 10,
                                buttons: [{
                                        text:Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.close","关闭"),
                                        iconCls:"close",
                                        handler: function(btn){
                                    		var win = btn.findParentByType('bmrExportExcelWin');
                                    		win.close();
                                    	}
                                }],

                                items: [
                                    {
                                        xtype: 'label',
                                        text: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.inputPackageName","请输入导出文件名"),
                                        style:{
                                        	marginTop: '10px',
                                        	marginBottom: '5px',
                                        	display: 'block'
                                        }
                                    },
                                    {
                                        layout:{
                                            type:'column'
                                        },
                                        items:[{
                                            xtype: 'textfield',
                                            name: 'FileName',
                                            allowBlank: false,
                                            columnWidth:.8
                                        },{
                                            xtype:"button",
                                            width:120,
                                            style:'margin-left:8px',
                                            text:Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.ensurePackage","确认导出"),
                                            labelAlign: 'right',
                                            buttonAlign: 'left',
                                            columnWidth:.2,
                                            handler: 'ensureExport'
                                        }]
                                    }
                                ]
                            },
                            {
                                title: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.packageResult","报名人导出结果"),
                                xtype: 'grid',
                                autoHeight: true,
                                frame:false,
                                minHeight:200,
                                columnLines: true,
                                reference: 'downloadGrid',
                                multiSelect: true,
                                store: listStore,
                                selModel: {
                                    type: 'checkboxmodel'
                                },
                                dockedItems: [{
                                    xtype: "toolbar",
                                    items: [
                                        {
                                        	text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.query","查询"),  
                                        	iconCls: "query",
                                        	handler:"exportQuery"		
                                        },
                                        '-',
                                        {
                                        	text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.delete","删除"),  
                                        	iconCls: "remove",
                                        	handler:"exportDelete"
                                        }
                                    ]
                                },{
                                    xtype:"toolbar",
                                    dock:"bottom",
                                    ui:"footer",
                                    items:['->',
                                        {
                                    		minWidth:80,
                                    		text:Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.save","保存"),
                                    		iconCls:"save",
                                    		handler:'exportGridSave'
                                        },{
                                        	minWidth:80,
                                        	text:Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.close","关闭"),
                                        	iconCls:"close",
                                        	handler: function(btn){
                                        		var win = btn.findParentByType('bmrExportExcelWin');
                                        		win.close();
                                        	}
                                        }]
                                }],
                                
                                columns: [
                                    {text: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.fileName", "文件名称"),
                                        dataIndex: 'fileName',
                                        minWidth:120,
                                        flex:1
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.czPerName", "操作人"),
                                        dataIndex: 'czPerName',
                                        width: 100
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.bgTime", "导出时间"),
                                        dataIndex: 'bgTime',
                                        width: 160
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.procStaDescr", "状态"),
                                        dataIndex: 'procStaDescr',
                                        width: 100
                                    },
                                    {
                                        xtype:'actioncolumn',
                                        header:Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.download", "下载"),
                                        align:'center',
                                        width:60,
                                        items:[{
                                            iconCls:'download',
                                            sortable:false,
                                            handler: "downloadFile",
                                            isDisabled:function(view ,rowIndex ,colIndex ,item ,record ){
                                                if(record.get("procState")=="SUCCEEDED"){
                                                    return false;
                                                }else{
                                                    return true;
                                                };
                                            }
                                        }]
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
