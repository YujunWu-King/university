Ext.define('KitchenSink.view.clueManagement.clueManagement.audit.auditApplicationView', {
    extend: 'Ext.panel.Panel',
    xtype: 'auditApplicationView',
    controller:'clueDetailController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        //'KitchenSink.view.enrollmentManagement.applicationForm.dynamicInfo.dynamicAttributeForm',
        'KitchenSink.view.clueManagement.clueManagement.audit.auditFormFileCheckStore',
        'KitchenSink.view.clueManagement.clueManagement.audit.referenceLetterStore',
        'KitchenSink.view.clueManagement.clueManagement.audit.fileStore',
        'KitchenSink.view.clueManagement.clueManagement.clueDetailController'
    ],
    title: "报名表审核",
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    viewModel: {
        data: {
            photoSrc: ''
        }
    },
    constructor: function (obj){
        this.appInsID=String(obj.appInsID);
        this.applicationFormTagStore=obj.applicationFormTagStore;
        this.chargeName=obj.chargeName;
        this.callParent();
    },
    listeners:{
        resize:function(panel){
            var auditApplicationForm = panel.down("form"),
                photoInfoForm = panel.down("#photoInfoForm");
            //重新设置照片区域表单宽度（父表单宽度-照片区域宽度145-20左右外边距）
            photoInfoForm.setWidth(auditApplicationForm.getWidth()-165);
        }
    },
    initComponent: function(){
        var me = this;
        var applicationFormTagStore=this.applicationFormTagStore;

        var storeGroup = {
            auditFormFileCheckAuditStateStore:new KitchenSink.view.common.store.appTransStore("TZ_BMB_DJWJSPZT"),
            auditRefLetterStateStore:new KitchenSink.view.common.store.appTransStore("TZ_APPFORM_STATE"),
            auditFormFileCheckBackMsgStore :new KitchenSink.view.common.store.comboxStore({
                recname:'TZ_CLS_HFDY_T',
                condition:{TZ_CLASS_ID:{
                    value:'',
                    operator:'01',
                    type:'01'
                },TZ_SBMINF_ID:{
                    value:'',
                    operator:'01',
                    type:'01'
                }},
                result:'TZ_SBMINF_REP'
            })
        };

        Ext.apply(this,{
            storeGroup:storeGroup,
            items: [
                {
                    xtype: 'form',
                    reference: 'auditApplicationForm',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    border: false,
                    bodyPadding: 10,
                    //heigth: 600,
                    bodyStyle:'overflow-y:auto;overflow-x:hidden',
                    fieldDefaults: {
                        msgTarget: 'side',
                        labelWidth: 100,
                        labelStyle: 'font-weight:bold'
                    },
                    items: [{
                        xtype: 'textfield',
                        name: 'classID',
                        hidden:true
                    },{
                        xtype: 'textfield',
                        name: 'oprID',
                        hidden:true
                    },{
                        xtype: 'textfield',
                        name: 'remark',
                        hidden:true
                    },{
                        layout:'column',
                        items:[{
                            xtype:'form',
                            itemId:'photoInfoForm',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            items:[{
                                xtype: 'textfield',
                                fieldLabel: "姓名",
                                name: 'stuName',
                                cls:'lanage_1',
                                readOnly:true
                            }, {
                                xtype: 'textfield',
                                fieldLabel: "班级名称",
                                name: 'className',
                                cls:'lanage_1',
                                readOnly:true
                            },{
                                xtype: 'textfield',
                                fieldLabel: "报名表编号",
                                name: 'appInsID',
                                cls:'lanage_1',
                                readOnly:true
                            }, {
                                xtype: 'combobox',
                                fieldLabel: "报名表状态",
                                editable:false,
                                name: 'appFormState',
                                emptyText:"",
                                valueField: 'TValue',
                                displayField: 'TSDesc',
                                store: new KitchenSink.view.common.store.appTransStore("TZ_APPFORM_STATE") ,
                                queryMode: 'local',
                                triggerAction: 'all',
                                cls:'lanage_1',
                                readOnly:true
                            }, {
                                xtype: 'combobox',
                                fieldLabel: "评审状态",
                                name: 'auditState',
                                editable:false,
                                emptyText:"",
                                valueField: 'TValue',
                                displayField: 'TSDesc',
                                store: new KitchenSink.view.common.store.appTransStore("TZ_AUDIT_STATE"),
                                queryMode: 'local',
                                triggerAction: 'all',
                                cls:'lanage_1',
                                readOnly:true
                            },{
                                xtype: 'tagfield',
                                fieldLabel:"标签",
                                name: 'tag',
                                store:applicationFormTagStore,
                                valueField: 'TZ_LABEL_ID',
                                displayField: 'TZ_LABEL_NAME',
                                queryMode: 'local',
                                editable:'false',
                                readOnly:true
                            }, {
                                xtype: 'textfield',
                                fieldLabel:"类别",
                                name: 'category',
                                cls:'lanage_1',
                                readOnly:true
                            }]
                        },{
                            xtype:'component',
                            itemId:'photo',
                            width:145,
                            bind:{
                                html:'<img src="{photoSrc}" style="width:140px;margin-left:5px;"/>'
                            }
                        }]
                    },{
                        xtype: 'textfield',
                        fieldLabel: "责任人",
                        name: 'personLiable',
                        value: this.chargeName,
                        cls:'lanage_1',
                        readOnly:true
                    },{
                        xtype: 'combobox',
                        fieldLabel: "学历检查",
                        editable:false,
                        name: 'degreeCheck',
                        valueField: 'TValue',
                        displayField: 'TSDesc',
                        store: new KitchenSink.view.common.store.appTransStore("TZ_XL_CHECK") ,
                        queryMode: 'local',
                        triggerAction: 'all',
                        cls:'lanage_1',
                        readOnly:true
                    }]
                },{
                    xtype: 'tabpanel',
                    frame: true,
                    activeTab: 0,
                    plain:false,
                    style:'margin:0 10px 0 10px',
                    resizeTabs:true,
                    defaults :{
                        autoScroll: false
                    },

                    listeners:{
                        tabchange:function(tabPanel, newCard, oldCard){
                            var queryType;
                            var form = tabPanel.findParentByType('auditApplicationView').child('form').getForm();
                            var classID = form.findField('classID').getValue();
                            var oprID = form.findField('oprID').getValue();


                            var tzStoreParams;
                            if (newCard.name == "refLetterAndAttachment"){
                                var refLetterStore = newCard.down('grid[name=refLetterGrid]').store;
                                var fileStore = newCard.down('grid[name=fileGrid]').store;
                                if(!refLetterStore.isLoaded()){
                                    tzStoreParams = '{"classID":"'+classID+'","oprID":"' + oprID + '","queryType":"REFLETTER"}';
                                    refLetterStore.tzStoreParams = tzStoreParams;
                                    refLetterStore.load();
                                }
                                if(!fileStore.isLoaded()){
                                    tzStoreParams = '{"classID":"'+classID+'","oprID":"' + oprID + '","queryType":"ATTACHMENT"}';
                                    fileStore.tzStoreParams = tzStoreParams;
                                    fileStore.load();
                                }
                            }
                            if (newCard.name == "materialCheck"){
                                var store = newCard.down("grid").store;
                                if(!store.isLoaded()){
                                    tzStoreParams = '{"classID":"'+classID+'","oprID":"' + oprID + '","queryType":"FILE"}';
                                    store.tzStoreParams = tzStoreParams;
                                    store.load({
                                        scope: this,
                                        callback: function(records, operation, success) {
                                            storeGroup.auditFormFileCheckBackMsgJsonData =new Array(records.length);
                                        }
                                    });

                                }
                            }
                            this.doLayout();

                        }
                    },
                    items:[
                        {
                            title: "考生资料提交检查",
                            xtype: 'form',
                            name:'materialCheck',
                            autoHeight:true,
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            border: false,
                            bodyStyle:'overflow-x:hidden',
                            items: [{
                                xtype: 'grid',
                                autoHeight: true,
                                minHeight:200,
                                bodyBorder:false,
                                columnLines: true,
                                viewConfig: {markDirty: false},
                                name: 'fileCheckGrid',
                                reference: 'fileCheckGrid',
                                store: {
                                    type: 'auditFormFileCheckStore'
                                },
                                columns: [
                                    {
                                        text: "内容简介",
                                        dataIndex: 'intro',
                                        sortable: false,
                                        minWidth:150,
                                        flex:1
                                    },{
                                        text: "审核状态",
                                        dataIndex: 'auditState',
                                        sortable: false,
                                        width:100,
                                        renderer:function(v){
                                            if(v){
                                                var rec = storeGroup.auditFormFileCheckAuditStateStore.find('TValue',v,0,false,false,true);
                                                if(rec>-1){
                                                    return storeGroup.auditFormFileCheckAuditStateStore.getAt(rec).get("TSDesc");
                                                }else{
                                                    return "";
                                                }
                                            }else{
                                                return "";
                                            }
                                        }
                                    },{
                                        text: "审核不通过原因",
                                        dataIndex: 'failedReason',
                                        sortable: false,
                                        width:150
                                    }
                                ]
                            },{
                                xtype: 'textarea',
                                name: 'materialRemark',
                                fieldLabel:'补充说明',
                                height:60,
                                labelStyle: 'font-weight:bold',
                                margin:10
                            }]
                        },
                        {
                            minHeight:250,
                            layout: {
                                type: 'accordion',
                                animate:true
                            },
                            tabBar: {
                                border: false
                            },
                            title: "推荐信和附件",
                            name:'refLetterAndAttachment',
                            items:[
                                {
                                    title: "推荐信",
                                    xtype: 'grid',
                                    columnLines: true,
                                    name: 'refLetterGrid',
                                    reference: 'refLetterGrid',
                                    store: {
                                        type: 'referenceLetterStore'
                                    },
                                    columns: [
                                        {
                                            text: "姓名",
                                            dataIndex: 'refLetterPerName',
                                            sortable: false,
                                            minWidth:90
                                        },{
                                            text:"邮箱",
                                            dataIndex: 'refLetterPerEmail',
                                            sortable: false,
                                            flex:1,
                                            minWidth:120
                                        },{
                                            text:"手机",
                                            dataIndex: 'refLetterPerPhone',
                                            sortable: false,
                                            width:120
                                        },{
                                            xtype:'linkcolumn',
                                            text:"查看推荐信",
                                            sortable:false,
                                            align:'center',
                                            items:[{
                                                text:"查看",
                                                handler: "viewRefLetter"
                                            }],
                                            width:105
                                        },{
                                            text: "提交状态",
                                            dataIndex: 'refLetterState',
                                            sortable: false,
                                            width:100,
                                            renderer:function(v,metadata,record){
                                                if(v){
                                                    var rec = storeGroup.auditRefLetterStateStore.find('TValue',v,0,false,true,false);
                                                    if(rec>-1){
                                                        return storeGroup.auditRefLetterStateStore.getAt(rec).get("TSDesc");
                                                    }else{
                                                        return "";
                                                    }
                                                }else{
                                                    return "";
                                                }
                                            }
                                        },{
                                            text:"推荐信",
                                            sortable: true,
                                            dataIndex: 'refLetterFile',
                                            minWidth: 150,
                                            flex:1,
                                            renderer:function(value,metadata,record){
                                                var refLetterSysFile = record.get('refLetterSysFile');
                                                var refLetterUserFile = record.get('refLetterUserFile');
                                                var refAccessUrl = record.get('refLetterAurl');

                                                if (refLetterSysFile == "" || refLetterSysFile == null	|| refAccessUrl == "" || refAccessUrl == null){
                                                    return "推荐信";
                                                }else{
                                                    return '<a href="'+ TzUniversityContextPath + refAccessUrl+'" target="_blank">'+refLetterUserFile+'</a>';
                                                }
                                            }
                                        }
                                    ]
                                },
                                {
                                    title:"附件",
                                    xtype: 'grid',
                                    columnLines: true,
                                    name: 'fileGrid',
                                    reference: 'fileGrid',
                                    store: {
                                        type: 'uploadFilesStore'
                                    },
                                    listeners:{
                                        viewready:function(panel){
                                            panel.store.findBy(function(record,id){
                                                var itemID = record.get('xuhao');
                                                $("a[ref="+itemID+"]").fancybox(
                                                    {
                                                        prevEffect : 'none',
                                                        nextEffect : 'none',
                                                        closeBtn  : true,
                                                        arrows    : false,
                                                        nextClick : true,
                                                        helpers : {
                                                            thumbs : {
                                                                width  : 50,
                                                                height : 50
                                                            },title : {
                                                                type : 'inside'
                                                            },
                                                            buttons	: {}

                                                        }
                                                    }
                                                );
                                            })

                                        }
                                    },
                                    store: {
                                        type: 'fileStore'
                                    },
                                    columns: [{
                                        text: "fancyboxId",
                                        dataIndex: 'xuhao',
                                        hidden:true
                                    },{
                                        text: "信息项ID",
                                        dataIndex: 'TZ_XXX_BH',
                                        hidden:true
                                    },{
                                        text: "附件名称",
                                        dataIndex: 'TZ_XXX_MC',
                                        minWidth:425
                                    },{
                                        text: "查看附件",
                                        dataIndex: 'fileLinkName',
                                        minWidth:  200,
                                        flex: 1,
                                        renderer: function(value,metadata,record) {
                                            var strFile = record.get('fileData');
                                            return strFile;
                                        }
                                    }]
                                }
                            ]
                        }]
                }]
        });
        this.callParent();
    },
    buttons: [{
        text: "关闭",
        iconCls:"close",
        handler: function(btn) {
            btn.findParentByType("panel").close();
        }
    }]
});