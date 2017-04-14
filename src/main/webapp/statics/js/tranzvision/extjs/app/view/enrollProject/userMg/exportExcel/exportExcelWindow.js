Ext.define('KitchenSink.view.enrollProject.userMg.exportExcel.exportExcelWindow', {
    extend: 'Ext.window.Window',
    xtype: 'userMgExportExcelWindow',
    controller: 'userMgExportExcel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.userMg.exportExcel.exportExcelStore',
        'KitchenSink.view.enrollProject.userMg.exportExcel.exportExcelController'
    ],
    modal:true,//背景遮罩
    header:false,
    resizable:false,
    minHeight: 150,
    maxHeight: 400,
    ignoreChangesFlag:true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    selList:[],
    modalID:'',
	searchSql:"",
	resultSource:'',
    y:25,
    constructor:function(){
        this.callParent();
    },
    initComponent: function(){
        var me =this;
       
		var processingStatus = new KitchenSink.view.common.store.appTransStore("TZ_AE_STATUS");
		var listStore = new KitchenSink.view.enrollProject.userMg.exportExcel.exportExcelStore;
		
        var excelTplStore = new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_EXPORT_TMP_T',
            condition:{
                TZ_JG_ID:{
                    value:Ext.tzOrgID,
                    operator:'01',
                    type:'01'
                },
                TZ_EXP_TMP_STATUS:{
                    value:'A',
                    operator:'01',
                    type:'01'
                },
                TZ_EXPORT_TMP_TYPE:{
                    value:'0',
                    operator:'01',
                    type:'01'
                }},
            result:'TZ_EXPORT_TMP_ID,TZ_EXPORT_TMP_NAME'
        })
        Ext.apply(this,{
            items: [{
                xtype: 'form',
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
                        reference:'exportExcelTabPanel',
                        activeTab: 0,
                        frame: false,
                        header:false,
                        width: 650,
                        minHeight: 200,
                        maxHeight: 400,
                        resizeTabs: true,
                        defaults: {
                            autoScroll: false
                            //margin:5
                        },
                        listeners: {
                            tabchange: function (tp, p) {
                                var queryType;
                                if (p.name == "exportExcelForm") {
                                }
                                if (p.name == "exportExcelGrid") {
                                    this.doLayout();
                                }

                            }

                        },
                        items: [
                            {
                                title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.exportExcel", "导出Excel"),
                                xtype: 'form',
                                frame:false,
                                minHeight:150,
                                autoHeight:true,
                                reference: 'exportExcelForm',
                                name:'exportExcelForm',
                                border: false,
                                bodyPadding: 10,
                                buttons: [{
                                    text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close", "关闭"),
                                    iconCls:"close",
                                    handler: 'exportExcelWindowClose'
                                }],
                                fieldDefaults: {
                                    msgTarget: 'side',
                                    labelStyle: 'font-weight:bold',
                                    labelWidth:150
                                },
                                layout: {
                                    type: 'vbox',
                                    align: 'stretch'
                                },
                                items: [
                                    {
                                        layout:{
                                            type:'column'
                                        },
                                        style:'margin-bottom:10px',
                                        items:[
                                            {
                                                fieldLabel:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.selectExportTemplate", "选择导出Excel模板"),
                                                xtype: 'combobox',
                                                name: 'excelTpl',
                                                style:'margin-top:10px',
                                                store:excelTplStore,
                                                valueField:'TZ_EXPORT_TMP_ID',
                                                displayField:'TZ_EXPORT_TMP_NAME',
                                                queryMode:'lcoal',
                                                allowBlank: false,
                                                editable:false,
                                                afterLabelTextTpl: [
                                                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                                ],
                                                columnWidth:.8
                                            }
                                        ]
                                    },
                                    {
                                        layout:{
                                            type:'column'
                                        },
                                        items:[
                                            {
                                                xtype: 'textfield',
                                                fieldLabel:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.inputExcelName", "请输入Excel文件名"),
                                                name: 'excelName',
                                                allowBlank: false,
                                                afterLabelTextTpl: [
                                                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                                ],
                                                columnWidth:.8
                                            },{
                                                xtype:"button",
                                                width:120,
                                                style:'margin-left:8px',
                                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.ensureExport", "确认导出"),
                                                handler: 'exportEnsure',
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
                                title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.exportResult", "Excel导出结果"),
                                xtype: 'grid',
                                frame:false,
                                minHeight:200,
                                columnLines: true,
                                reference: 'exportExcelGrid',
                                name:'exportExcelGrid',
                                multiSelect: true,
                                store: listStore,
                                dockedItems: [{
                                    xtype: "toolbar",
                                    items: [
                                        {text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.query", "查询"), iconCls: "query",handler:"queryExcel"},'-',
                                        {text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_EXP_EXCEL_STD.delete","删除"),  iconCls: "remove",handler:"excelDelete"}
                                    ]
                                },{
                                    xtype:"toolbar",
                                    dock:"bottom",
                                    ui:"footer",
                                    items:['->',
                                        {minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_EXP_EXCEL_STD.save","保存"),iconCls:"save",handler:'exportExcelSave'},
                                        {minWidth:80,text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close", "关闭"),iconCls:"close",handler:'exportExcelWindowClose'}]
                                }],
                                selModel: {
                                    type: 'checkboxmodel'
                                },

                                columns: [
                                    {
                                        text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.fileName", "文件名称"),
                                        dataIndex: 'fileName',
                                        minWidth:100,
                                        flex:1
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.operator", "操作人"),
                                        dataIndex: 'oprName',
                                        width: 80
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.exportTime", "导出时间"),
                                        dataIndex: 'oprTime',
                                        width: 180
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.state", "导出状态"),
                                        dataIndex: 'applicationEngineStatus',
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
                                        text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.download", "下载"),
                                        align:'center',
                                        items:[
                                            {
                                                sortable:false,
                                                iconCls:'download',
                                                handler: "downloadFile",
                                                isDisabled:function(view ,rowIndex ,colIndex ,item ,record ){
                                                    if(record .get("fileUrl").length>0){
                                                        return false
                                                    }else{
                                                        return true;
                                                    };
                                                }
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
