Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.classInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'auditClassInfo',
    controller: 'appFormClass',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'KitchenSink.view.enrollmentManagement.applicationForm.stuStore',
        'KitchenSink.view.enrollmentManagement.applicationForm.dynamicInfo.dynamicColumn'
    ],
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 42;/*button height plus panel body padding*/
            var formHeight = panel.lookupReference('classInfoForm').getHeight();
            var formPadding = 20;
            var grid = panel.child('grid[name=appFormApplicants]');
            grid.setHeight( height- formHeight -buttonHeight-formPadding);
        }
    },
    bodyPadding:10,
    constructor: function (cfg){
        Ext.apply(this,cfg);
        this.callParent();
    },
    initComponent:function(){
        var me = this;
        var appFormStuStore = new KitchenSink.view.enrollmentManagement.applicationForm.stuStore();

        var submitStateStore = me.initialData.submitStateStore,
	        auditStateStore = me.initialData.auditStateStore,
	        interviewResultStore = me.initialData.interviewResultStore
	        orgColorSortStore = me.initialData.orgColorSortStore;
    
        /*过滤器Options数据*/
        var colorSortFilterOptions=me.initialData.colorSortFilterOptions,
	    	submitStateFilterOptions=me.initialData.submitStateFilterOptions,
	    	auditStateFilterOptions=me.initialData.auditStateFilterOptions,
	    	interviewResultFilterOptions=me.initialData.interviewResultFilterOptions;
        
        /*初始颜色类别数据*/
        var initialColorSortData=me.initialData.initialColorSortData;
        var validColorSortStore =  new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_COLOR_SORT_T',
            condition:{TZ_JG_ID:{
                value:Ext.tzOrgID,
                operator:'01',
                type:'01'
            },TZ_COLOR_STATUS:{
                value:'N',
                operator:'02',
                type:'01'
            }},
            result:'TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE'
        });

        var dynamicColorSortStore = Ext.create("Ext.data.Store",{
            fields:[
                "TZ_COLOR_SORT_ID","TZ_COLOR_NAME","TZ_COLOR_CODE"
            ],
            data:initialColorSortData
        });
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                bodyPadding:'10px 0 10px 0',
                reference: 'classInfoForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },

                items: [{
                    xtype: 'textfield',
                    name: 'modalID',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.applyDirectionName","报考方向名称"),
                    name: 'className',
                    cls:'lanage_1',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.applyBatchName","申请批次"),
                    name: 'batchName',
                    cls:'lanage_1',
                    readOnly:true
                }
                ]
            },{
                xtype: 'grid',
                height:500,
                header:false,
                name:'appFormApplicants',
                frame: true,
                viewConfig : {
                    enableTextSelection:true
                },
                columnLines: true,
                listeners: {
                    afterrender: {
                    	fn: function(stuGrid){ 
                    		stuGrid.getStore().addListener("refresh",
                    				function(thisStore){
                    			stuGrid.getView().getSelectionModel().deselectAll();
                    		},this);
                    	}
                    }
                },
                dockedItems:{
                    overflowHandler: 'Menu',
                    xtype:"toolbar",
                    items:[
						{
						    text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.query","查询"),
						    iconCls:"query",
						    handler:"queryStudents"
						},'-',   
                        /*{
                            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.package","将选中人员材料批量打包"),
                            iconCls:"zip",
                            handler:"packageAndDownload"
                        },'-',*/
						{
                            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.packageAllLetter","将搜索结果考生推荐信批量打包"),
                            iconCls:"zip",
                            handler:"packageLetAndDownload"
                        },'-',
                        {
                            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.packageAll","将搜索结果考生材料批量打包"),
                            iconCls:"zip",
                            handler:"packageAllAndDownload"
                        },'-',
                        {
                            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.downloadPackages","查看打包结果并下载"),
                            iconCls:"download",
                            handler:"viewAndDownload"
                        }
                       ,'->',
                        {
                            xtype:'splitbutton',
                            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.moreOperation","更多操作"),
                            iconCls:  'list',
                            glyph: 61,
                            menu:[  {
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.exportExcel","导出Excel"),
                                glyph:'xf1c3@FontAwesome',
                                menu:[{
                                    text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.exportSearchResults","导出搜索结果人员信息到Excel"),
                                    glyph:'xf1c3@FontAwesome',
                                    name:'exportSearch',
                                    handler:'exportExcelOrDownload'
                                },{
                                    text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.exportApplicantsInfo","导出选中人员信息到Excel"),
                                    glyph:'xf1c3@FontAwesome',
                                    name:'exportExcel',
                                    handler:'exportExcelOrDownload'
                                },
                                {
                                        text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.downloadExcel","查看导出结果并下载"),
                                        glyph:'xf019@FontAwesome',
                                        name:'downloadExcel',
                                        handler:'exportExcelOrDownload'
                                 }]
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.printApplicationForm","打印报名表"),
                                glyph:'xf02f@FontAwesome',
                                handler:'printAppForm'
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.addHmdUser","加入黑名单"),
                                glyph:'xf067@FontAwesome',
                                handler:'addHmd'
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.viewEmailSendHis","查看邮件发送历史"),
                                glyph:'xf1da@FontAwesome',
                                handler:'viewMailHistory'
                            },{
								text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.sendEmailSelectedPerson","给选中人发送邮件"),
								glyph:'xf1d8@FontAwesome',
								handler:'sendEmlSelPers'
                            },{
                                text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.viewSqrInfo","查看申请人详情"),
                                iconCls:"view",
                                handler:'viewSqrInfo'
							}]
                        }
                    ]
                },
                plugins: [
                    {
                        ptype: 'gridfilters',
                        controller: 'appFormClass'
                    },
                    {
                        ptype: 'cellediting',
                        clicksToEdit: 1
                    },
                    {
                        ptype:'rowexpander',
                        rowBodyTpl : new Ext.XTemplate(
                            '<div class="x-grid-group-title" style="margin-left:80px;">',
                            '<table class="x-grid3-row-table" cellspacing="0" cellpadding="0" border="0" >',
                            '<tpl for="moreInfo">',
                            '<tr style="line-height:30px;">',
                            '<td  class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">{itemName}</td>' +
                                '<td style="font-weight: normal;max-width:800px;">{itemValue}</td>' +
                                '</tr>',
                            '</tpl>',
                            '</table>',
                            '</div>',{}),
                        lazyRender : true,
                        enableCaching : false
                    }
                ],
                selModel: {
                    type: 'checkboxmodel',
                    pruneRemoved: false
                },
                reference: 'stuGrid',
                multiSelect: true,
                store: appFormStuStore,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 1000,
                    store: appFormStuStore,
                    plugins: new Ext.ux.ProgressBarPager()
                },
                columns: [
                    {
                        xtype:'rownumberer',
                        minWidth:20,
                        maxWidth:80
                    },
                    {
                        text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.basicInfo","基本信息"),
                        lockable   : false,
                        menuDisabled: true,
                        columns:[
                            {
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.name","姓名"),
                                dataIndex: 'stuName',
                                width:100,
                                lockable   : false,
                                filter: {
                                    type: 'string'
                                }
                            },{
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.interviewApplicationID","面试申请号"),
                                dataIndex: 'interviewApplicationID',
                                width:110,
                                lockable   : false,
                                filter: {
                                    type: 'string'
                                }
                            },{
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.nationalID","证件号码"),
                                dataIndex: 'nationalID',
                                width:100,
                                lockable   : false,
                                filter: {
                                    type: 'string'
                                }
                            },{
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.submitState","提交状态"),
                                dataIndex: 'submitState',
                                lockable   : false,
                                width: 95,
                                filter: {
                                    type: 'list',
                                    options: submitStateFilterOptions
                                },
                                renderer:function(v){
                                    if(v){
                                        var index = submitStateStore.find('TValue',v,0,false,true,true);
                                        if(index>-1){
                                            return submitStateStore.getAt(index).get("TSDesc");
                                        }
                                        
                                        return "";
                                    }
                                }
                            },{
                                xtype:'datecolumn',
                                format:'Y-m-d',
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.submitDate","提交时间"),
                                dataIndex: 'submitDate',
                                lockable   : false,
                                width: 105,
                                filter: {
                                    type: 'date',
                                    format:'Y-m-d',
                                    active:true
                                }
                            },{
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.auditState","评审状态"),
                                dataIndex: 'auditState',
                                lockable   : false,
                                width: 95,
                                filter: {
                                    type: 'list',
                                    options: auditStateFilterOptions
                                },
                                renderer:function(v){
                                    if(v){
                                        var index = auditStateStore.find('TValue',v,0,false,true,true);
                                        if(index>-1){
                                            return auditStateStore.getAt(index).get("TSDesc");
                                        }
                                        
                                        return "";
                                    }
                                }
                            },{
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.interviewResult","面试结果"),
                                dataIndex: 'interviewResult',
                                lockable   : false,
                                width: 95,
                                filter: {
                                    type: 'list',
                                    options: interviewResultFilterOptions
                                },
                                renderer:function(v){
                                    if(v){
                                        var index = interviewResultStore.find('TValue',v,0,false,true,true);
                                        if(index>-1){
                                            return interviewResultStore.getAt(index).get("TSDesc");
                                        }
                                        
                                        return "";
                                    }
                                }
                            },{
                                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.colorType","类别"),
                                dataIndex: 'colorType',
                                lockable   : false,
                                width: 140,
                                filter: {
                                    type: 'list',
                                    options: colorSortFilterOptions
                                },
                                editor: {
                                    xtype: 'combo',
                                    queryMode:'local',
                                    valueField: 'TZ_COLOR_SORT_ID',
                                    displayField: 'TZ_COLOR_NAME',
                                    triggerAction: 'all',
                                    editable : false,
                                    triggers:{
                                        clear: {
                                            cls: 'x-form-clear-trigger',
                                            handler: function(field){
                                                field.setValue("");
                                            }
                                        }
                                    },
                                    store:dynamicColorSortStore,
                                    tpl: Ext.create('Ext.XTemplate',
                                        '<tpl for=".">',
                                        '<div class="x-boundlist-item"><div class="x-colorpicker-field-swatch-inner" style="margin-top:6px;width:30px;height:50%;background-color: #{TZ_COLOR_CODE}"></div><div style="margin-left:40px;display: block;  overflow:  hidden; white-space: nowrap; -o-text-overflow: ellipsis; text-overflow:  ellipsis;"> {TZ_COLOR_NAME}</div></div>',
                                        '</tpl>'
                                    ),
                                    displayTpl: Ext.create('Ext.XTemplate',
                                        '<tpl for=".">',
                                        '{TZ_COLOR_NAME}',
                                        '</tpl>'
                                    ),
                                    listeners: {
                                        focus: function (combo,event, eOpts) {
                                            var selList = this.findParentByType("grid").getView().getSelectionModel().getSelection();

                                            var colorSortID =selList[0].raw.colorType;

                                            var arrayData = new Array();
                                            for(var i=0;i<validColorSortStore.getCount();i++){
                                                arrayData.push(validColorSortStore.data['items'][i].data);
                                            };
                                            if(colorSortID!=null&&colorSortID.length>0&&validColorSortStore.find("TZ_COLOR_SORT_ID",colorSortID)==-1){
                                                var tmpRec = orgColorSortStore.getAt(orgColorSortStore.find("TZ_COLOR_SORT_ID",colorSortID));
                                                arrayData.push(tmpRec.data);
                                            };
                                            if(arrayData.length<1){
                                                arrayData.push({TZ_COLOR_SORT_ID:'',TZ_COLOR_NAME:'',TZ_COLOR_CODE:''});
                                            }
                                            combo.store.loadData(arrayData);
                                        },
                                        blur: function (combo,event, eOpts) {
                                            combo.store.loadData(initialColorSortData);
                                        }
                                    }
                                },
                                renderer:function(v){
                                    if(v){
                                        var rec = orgColorSortStore.find('TZ_COLOR_SORT_ID',v,0,true,true,false);
                                        if(rec>-1){
                                            return "<div  class='x-colorpicker-field-swatch-inner' style='width:30px;height:50%;background-color: #"+orgColorSortStore.getAt(rec).get("TZ_COLOR_CODE")+"'></div><div style='margin-left:40px;'>"+orgColorSortStore.getAt(rec).get("TZ_COLOR_NAME")+"</div>";
                                        }else{
                                            return Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.pleaseSelect","请选择...");
                                        }
                                    }
                                }
                            }
                        ]
                    }
                   ,{
                        xtype:'actioncolumn',
                        align:'center',
                        menuDisabled: true,
                        width:80,
                        items:[
                            {
                                tooltip:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.viewApplicationForm","查看报名表"),
                                sortable:false,
                                handler: "viewApplicationForm",
                                iconCls:'preview'
                            },
                            {
                                tooltip:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.printApplicationForm","打印报名表"),
                                sortable:false,
                                handler: "printAppForm",
                                iconCls:'print'
                            },
                            {
                                tooltip:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.auditApplicationForm","报名表审核"),
                                sortable:false,
                                handler: "auditApplicationForm",
                                iconCls:'audit'
                            }
                        ]
                    },{
                        xtype:'appFormDynamicColumn',
                        classID:me.classID,
                        store:appFormStuStore
                    }
                ]
            }]
        })
        this.callParent();
    },
    title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.applicantsList","报考学生列表"),
    bodyStyle:'overflow-y:hidden;overflow-x:hidden',
    buttons: [{
        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.save","保存"),
        iconCls:"save",
        handler: 'onStuInfoSave'
    }, {
        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.ensure","确定"),
        iconCls:"ensure",
        handler: 'onStuInfoEnsure'
    }, {
        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
        iconCls:"close",
        handler: 'onStuInfoClose'
    }]
});
