Ext.define('KitchenSink.view.tzLianxi.liuzh.lzhBugMg.updateBug', {
    requires: [
        'Ext.data.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.tzLianxi.liuzh.lzhBugMg.bugController'
    ],
    extend : 'Ext.panel.Panel',
    controller:'bugController',
    autoScroll:false,
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    title:'BUG信息',
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
                items: [{
                    xtype: 'textfield',
                    fieldLabel: '编号',
                    name: 'BugID',
                    editable:false,
                    value:'NEXT'
                }, {
                    xtype: 'textfield',
                    fieldLabel: '说明',
                    name: 'name'
                },{
                    xtype: 'datefield',
                    fieldLabel: '录入日期',
                    name: 'inputDate',
                    allowBlank: false,
                    maxValue: new Date(),
                    format:'Y-m-d'
                }, {
                    layout: {
                        type: 'column',
                        align: 'stretch'
                    },
                    items:[{
                        xtype: 'textfield',
                        fieldLabel: '录入人',
                        name: 'inputOprID',
                        allowBlank: false,
                        triggers: {
                            search: {
                                cls: 'x-form-search-trigger',
                                handler: "selectOprID"
                            }
                        }
                    }
                        ,{
                            columnWidth:.5,
                            xtype: 'displayfield',
                            hideLabel: true,
                            style:'margin-left:8px',
                            name: 'recOprName'
                        }
                    ]
                }, {
                    layout: {
                        type: 'column',
                        align: 'stretch'
                    },
                    items:[{
                        xtype: 'textfield',
                        fieldLabel: '责任人',
                        name: 'responsableOprID',
                        allowBlank: false,
                        triggers: {
                            search: {
                                cls: 'x-form-search-trigger',
                                handler: "selectOprID"
                            }
                        }
                    }
                        ,{
                            columnWidth:.5,
                            xtype: 'displayfield',
                            hideLabel: true,
                            style:'margin-left:8px',
                            name: 'resOprName'
                        }
                    ]
                }, {
                    xtype: 'datefield',
                    fieldLabel: '期望解决日期',
                    name: 'espectDate',
                    minValue: new Date(),
                    format:'Y-m-d',
                }, {
                    xtype: 'combo',
                    fieldLabel: '处理状态',
                    name: 'status',
                    emptyText: '',
                    queryMode: 'local',
                    valueField: 'status',
                    displayField: 'desc',
                    editable:false,
                    store: {
                        fields: ["status", "desc"],
                        data: [
                            {status:"0",desc:"新建"},
                            {status:"1",desc:"已分配"},
                            {status: "2", desc: '已修复'},
                            {status: "3", desc: '关闭'},
                            {status: "4", desc: '重新打开'},
                            {status:"5",desc:"取消"}
                        ]
                    }
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
                        title:'描述信息',
                        xtype:'form',
                        layout: {
                            type: 'vbox',
                            align: 'stretch'
                        },
                        style:'border:0',
                        items:[{
                            xtype: 'ueditor',
                            name: 'descript',
                            zIndex:999,
                            allowBlank: true

                        }]
                    },{
                        title: "附件集",
                        items: [{
                            xtype: 'grid',
                            height: 315,
                            frame: true,
                            columnLines: true,
                            //id: 'attachmentGrid',
                            name: 'attachmentGrid',
                            reference: 'attachmentGrid',
                            style:"border:0px",
                            store: new Ext.data.Store({
                                fields: [
                                    {name:'attachmentID'},
                                    {name: 'attachmentName'},
                                    {name: 'attachmentUrl'}
                                ],
                                pageSize:10,
                                proxy:{
                                    type: 'ajax',
                                    extraParams:{
                                        params:''
                                    },
                                    url :'/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_LX_BUG.LX_BUG.FieldFormula.iScript_bugMg',
                                    reader: {
                                        type: 'json',
                                        totalProperty: 'content.total',
                                        rootProperty: ' content.root'
                                    }
                                }
                            }),
                            selModel: {
                                type: 'checkboxmodel'
                            },
                            tbar: [{
                                xtype: 'form',
                                bodyStyle:'padding:10px 0px 0px 0px',
                                items:[{
                                    xtype: 'fileuploadfield',
                                    buttonText: '上传附件',
                                    name: 'attachmentUpload',
                                    buttonOnly:true,
                                    width: 88,
                                    listeners:{
                                        change:function(file, value, eOpts){
                                            addAttach(file, value, "ATTACHMENT");
                                        }
                                    }
                                }]},"-",
                                {iconCls: 'remove',text: '删除',tooltip:"删除选中的数据",handler: 'deleteArtAttenments'}],
                            columns: [{
                                text: '附件ID',
                                dataIndex: 'attachmentID',
                                sortable: false,
                                hidden: true
                            },{
                                text: '附件url',
                                dataIndex: 'attachmentUrl',
                                sortable: false,
                                hidden: true
                            },{
                                text: '附件名称',
                                dataIndex: 'attachmentName',
                                sortable: false,
                                minWidth: 100,
                                flex: 1,
                                renderer: function(v,d){
                                    return '<a href="'+ d.record.data.attachmentUrl +'" target="_blank">'+v+'</a>';
                                }
                            },{
                                menuDisabled: true,
                                sortable: false,
                                width:60,
                                xtype: 'actioncolumn',
                                items:[
                                    {iconCls: 'remove',tooltip: '删除', handler: 'deleteArtAttenments'}
                                ]
                            }]
                        }]

                    }]
                }]
            }],
            buttons:[{
                    text: '保存',
                    handler:'saveBug',
                    iconCls:'save'
                },{
                    text:'确定',
                    handler:'ensureBug',
                    iconCls:'ensure'
                },{
                    text:'关闭',
                    iconCls:'close',
                    handler:'cancel'
                }]
        });
        this.callParent();
    }
});
function addAttach(file, value, attachmentType){

    var form = file.findParentByType("form").getForm();

    if(value != ""){
        if(attachmentType=="IMG" || attachmentType=="TPJ"){
            var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
            if(fix.toLowerCase() != "jpg" && fix.toLowerCase() != "png" && fix.toLowerCase() != "gif" && fix.toLowerCase() != "bmp"){
                Ext.MessageBox.alert("提示","请上传jpg|png|gif|bmp格式的图片。");
                form.reset();
                return;
            };
        }

        //如果是附件则存在在附件的url中，如果是图片在存放在图片的url中;
        var dateStr = Ext.Date.format(new Date(), 'Ymd');


        var upUrl = "";
        if(attachmentType=="ATTACHMENT"){
            //upUrl = file.findParentByType("activityInfo").child("form").getForm().findField("saveAttachAccessUrl").getValue();
            upUrl = "/linkfile/FileUpLoad/attachment/";
            if(upUrl==""){
                Ext.Msg.alert("错误","未定义上传附件的路径，请与管理员联系");
                return;
            }else{
                if(upUrl.length == (upUrl.lastIndexOf("/")+1)){
                    upUrl = '/UpdServlet?filePath='+upUrl+dateStr;
                }else{
                    upUrl = '/UpdServlet?filePath='+upUrl+"/"+dateStr;
                }
            }
        }else{
            upUrl = file.findParentByType("activityInfo").child("form").getForm().findField("saveImageAccessUrl").getValue();
            if(upUrl==""){
                Ext.Msg.alert("错误","未定义上传图片的路径，请与管理员联系");
                return;
            }else{
                if(upUrl.length == (upUrl.lastIndexOf("/")+1)){
                    upUrl = '/UpdServlet?filePath='+upUrl+dateStr;
                }else{
                    upUrl = '/UpdServlet?filePath='+upUrl+"/"+dateStr;
                }
            }
        }
        var myMask = new Ext.LoadMask({
            msg    : '加载中...',
            target : Ext.getCmp('tranzvision-framework-content-panel')
        });

        myMask.show();

        form.submit({
            //url: '/UpdServlet?filePath=/linkfile/FileUpLoad/imagesWall',
            url: upUrl,
            //waitMsg: '图片正在上传，请耐心等待....',
            success: function (form, action) {

                var tzParams;
                var picViewCom;

                if(attachmentType=="TPJ"){
                    picViewCom = file.findParentByType("tabpanel").down('dataview[name=picView]');
                    tzParams = '{"order":' + picViewCom.getStore().getCount() + ',"attachmentType":"'+attachmentType+'","data":' + Ext.JSON.encode(action.result.msg) + '}';
                }else{
                    tzParams = '{"attachmentType":"' + attachmentType + '","data":' + Ext.JSON.encode(action.result.msg) + '}';
                }

                tzParams = '{"ComID":"TZ_HD_MANAGER_COM","PageID":"TZ_HD_INFO_STD","OperateType":"HTML","comParams":' + tzParams +'}';

                Ext.Ajax.request({
                    //url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_ATT_D.TZ_GD_ATT_FILE.FieldFormula.Iscript_AddArtAttach',
                    url: Ext.tzGetGeneralURL,
                    params: {
                        tzParams: tzParams
                    },
                    success: function(response){
                        var responseText = Ext.JSON.decode(response.responseText);
                        if(responseText.success == 0){
                            var accessPath = action.result.msg.accessPath;
                            var sltPath = action.result.msg.accessPath;
                            if(accessPath.length == (accessPath.lastIndexOf("/")+1)){
                                accessPath = accessPath + action.result.msg.sysFileName;
                                //sltPath = sltPath + "MINI_"+action.result.msg.sysFileName;
                                sltPath = sltPath + responseText.minPicSysFileName;
                            }else{
                                accessPath = accessPath + "/" + action.result.msg.sysFileName;
                                //sltPath = sltPath+ "/" + "MINI_"+action.result.msg.sysFileName;
                                sltPath = sltPath+ "/" + responseText.minPicSysFileName;
                            }


                            //viewStore.reload();
                            if(attachmentType=="IMG"){
                                //Ext.getCmp( "titileImage").setSrc(accessPath);
                                //Ext.getCmp( "titleImageUrl").setValue(accessPath);
                                file.findParentByType("tabpanel").down('image[name=titileImage]').setSrc(accessPath);
                                file.findParentByType("form").findParentByType("form").down('hiddenfield[name=titleImageUrl]').setValue(accessPath);
                                //Ext.ComponentQuery.query('image[name=titileImage]')[0].setSrc(accessPath);
                                //Ext.ComponentQuery.query('hiddenfield[name=titleImageUrl]')[0].setValue(accessPath);


                            }

                            if(attachmentType=="ATTACHMENT"){
                                //var applyItemGrid = this.lookupReference('attachmentGrid');
                                var applyItemGrid = file.findParentByType("grid")
                                var r = Ext.create('KitchenSink.view.activity.attachmentModel', {
                                    "attachmentID": action.result.msg.sysFileName,
                                    "attachmentName": "<a href='"+accessPath+"' target='_blank'>"+action.result.msg.filename+"</a>",
                                    "attachmentUrl": accessPath,
                                });
                                applyItemGrid.store.insert(0,r);
                            }

                            if(attachmentType=="TPJ"){

                                var viewStore = picViewCom.store;
                                var picsCount = viewStore.getCount();

                                var r = Ext.create('KitchenSink.view.activity.picModel', {
                                    "sysFileName": action.result.msg.sysFileName ,
                                    "index": picsCount+1,
                                    "src": accessPath,
                                    "caption": action.result.msg.filename,
                                    "picURL": "",
                                    "sltUrl": sltPath
                                });

                                viewStore.insert(picsCount ,r);
                                viewStore.loadData(r,true);

                                console.log(viewStore);
                                // Ext.Msg.alert("",Ext.JSON.encode(action.result.msg));
                            }
                        }else{
                            Ext.Msg.alert("提示", responseText.message);
                        }
                    },
                    failure: function (response) {
                        Ext.MessageBox.alert("错误", "上传失败");
                    }
                });

                //重置表单
                myMask.hide();
                form.reset();
            },
            failure: function (form, action) {
                myMask.hide();
                Ext.MessageBox.alert("错误", action.result.msg);
            }
        });


    }
}