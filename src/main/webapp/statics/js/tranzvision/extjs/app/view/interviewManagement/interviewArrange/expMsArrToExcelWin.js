Ext.define('KitchenSink.view.interviewManagement.interviewArrange.expMsArrToExcelWin', {
    extend: 'Ext.window.Window',
    xtype: 'msArrExportExcelWindow',
    controller: 'expMsArrToExcelController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.interviewManagement.interviewArrange.expMsArrToExcelStore',
        'KitchenSink.view.interviewManagement.interviewArrange.expMsArrToExcelController'
    ],
	
    modal:true,//背景遮罩
    header:false,
    resizable:false,
    minHeight: 150,
    maxHeight: 400,
    ignoreChangesFlag:true,
    //bodyStyle:'overflow-y:auto;overflow-x:hidden',
    selList:[],
    modalID:'',
    y:100,
    constructor:function(modalID){
        this.modalID = modalID;
        this.callParent();
    },
    initComponent: function(){
        var me =this;
        var modalID = this.modalID;
        //var processingStatus = new KitchenSink.view.common.store.appTransStore("TZ_AE_STATUS");
        var listStore = new KitchenSink.view.interviewManagement.interviewArrange.expMsArrToExcelStore();

        var excelTplStore = new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_EXPORT_TMP_T',
            condition:{
                TZ_JG_ID:{
                    value:Ext.tzOrgID,
                    operator:'01',
                    type:'01'
                },
                TZ_APP_MODAL_ID:{
                    value:modalID,
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
                        maxHeight: 380,
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
                                title: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.exportExcel", "导出Excel"),
                                xtype: 'form',
                                frame:false,
                                minHeight:150,
                                autoHeight:true,
                                reference: 'msArrExpExcelForm',
                                name:'msArrExpExcelForm',
                                border: false,
                                bodyPadding: 10,
                                buttons: [{
                                    text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.close", "关闭"),
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
                                                fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.selectExportTemplate", "选择导出Excel模板"),
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
                                                fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.inputExcelName", "请输入Excel文件名"),
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
                                                text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.ensureExport", "确认导出"),
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
                                title: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.exportResult", "Excel导出结果"),
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
                                        {text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.query", "查询"), iconCls: "query",handler:"queryExcel"},"-",
                                        {text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.delete", "删除"), iconCls: "delete",handler:"deleteExcel"}
                                    ]
                                },{
                                    xtype:"toolbar",
                                    dock:"bottom",
                                    ui:"footer",
                                    items:['->',
                                        {minWidth:80,text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.save", "保存"),iconCls:"save",handler:'exportExcelWindowSave'},
                                        {minWidth:80,text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.close", "关闭"),iconCls:"close",handler:'exportExcelWindowClose'}
                                    ]
                                }],
                                selModel: {
                                    type: 'checkboxmodel'
                                },

                                columns: [
                                    {
                                        text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.fileName", "文件名称"),
                                        dataIndex: 'fileName',
                                        minWidth:100,
                                        flex:1
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.operator", "操作人"),
                                        dataIndex: 'oprName',
                                        width: 80
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.exportTime", "导出时间"),
                                        dataIndex: 'oprTime',
                                        width: 180
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.state", "导出状态"),
                                        //dataIndex: 'engineStatus',
                                        dataIndex: 'runStatusDesc',
                                        width: 100,
                                        renderer:function(v){
                                        	return v;
                                        }
                                    },
                                    {
                                        xtype:'actioncolumn',
                                        text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.download", "下载"),
                                        align:'center',
                                        items:[
                                            {
                                                sortable:false,
                                                iconCls:'download',
                                                handler: "downloadFile",
                                                isDisabled:function(view ,rowIndex ,colIndex ,item ,record ){
                                                    if(record.get("fileUrl").length>0 && record.get("engineStatus") == "SUCCEEDED"){
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
                                    pageSize: 10,
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
