Ext.define('KitchenSink.view.tzLianxi.liuzh.bugMg.OprBug', {
    requires:[
    'Ext.data.*',
    'Ext.util.*',
    'Ext.toolbar.Paging',
    'Ext.ux.ProgressBarPager',
    'KitchenSink.view.tzLianxi.liuzh.bugMg.bugController',
    'KitchenSink.view.tzLianxi.liuzh.bugMg.bugModel'
    // 'KitchenSink.view.content.contentMg.artAttachmentModel',
      // 'KitchenSink.view.content.contentMg.artAttachmentStore'
],
    extend : 'Ext.panel.Panel',
    controller:'bugController',
    title:'Bug',
    autoScroll:false,
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    margin:'5px',
    constructor:function(action){
        this.action = action;
        this.dis=dis;
        this.initComponent();
        console.log(this.action);
        var button = this.action=='1'?[
            {
                text: '保存',handler:'bugSave'
                /*handler: function (btn) {

                //  var p=qwe.form.setValues();
                var panel = btn.findParentByType('panel');
                var form = panel.child('form');
                var formParams = form.getForm().getValues();
                if(formParams['name']==''||formParams['inputDate']==''||formParams['inputOprID']==''||formParams['responsableOprID']==''
                    ||formParams['espectDate']==''||formParams['status']==''){
                    Ext.Msg.alert('提示','您输入的信息不完整');
                }else {
                    var contentPanel = Ext.getCmp('tranzvision-framework-content-panel'), grid;
                    for (var x = contentPanel.items.items.length - 1; x >= 0; x--) {
                        if (contentPanel.items.items[x].title == 'Bug管理') {
                            grid = Ext.getCmp(contentPanel.items.keys[x]);
                        }
                    }
                    var jsonData = {};
                    jsonData.update = formParams;
                    jsonData.type = 'saveBug';
                    /*console.log(formParams);
                    console.log(Ext.encode(formParams));
                    console.log(Ext.decode(Ext.encode(formParams)));*/
                   /* Ext.Ajax.request({
                        url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_LX_BUG.LX_BUG.FieldFormula.iScript_bugMg',
                        params: {params: Ext.encode(jsonData)},
                        success: function () {
                            grid.getStore().reload();
                            contentPanel.remove(panel);
                        }
                    });
                    console.log(Ext.encode(jsonData));
                }
                /* Ext.Ajax.request({
                 url:'/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_LX_BUG.LX_BUG.FieldFormula.iScript_bugMg',
                 oarams:formParams,
                 method:'post',
                 success:function(response){
                 if(response.states.errcode == 0){
                 alert(response.states.errmsg);
                 }else{
                 Ext.getCmp('tranzvixion-wy-bugManagerAdmin').getStore().reload();

                 }
                 }
                 })

            }*/
            },

            {text: '确定', handler: 'bugEnsure'},
            {text: '取消', handler: 'bugCancel'}
        ]:[];

        Ext.apply(this, {
            items: [{
                xtype: 'form',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                //heigth: 600,

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 100,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype: 'textfield',
                    name: 'saveAttachAccessUrl',
                    hidden: true
                },{
                    xtype: 'textfield',
                    fieldLabel: '编号',
                    name: 'BugID', disabled:dis,
                    editable: false
                }, {
                    xtype: 'textfield',
                    fieldLabel: '说明',
                    name: 'name',
                    allowBlank: false,
                    disabled:dis
                }, {
                    xtype: 'datefield',
                    fieldLabel: '录入日期',
                    name: 'inputDate',
                    allowBlank: false,
                    maxValue: new Date(),
                    disabled:dis,
                    format:'Y-m-d'
                }, {
                    layout: {
                        type: 'column'
                    },
                    items:[{
                        xtype: 'textfield',
                        fieldLabel: '录入人',
                        name: 'inputOprID',
                        allowBlank: false,
                        disabled:dis,
                        editable: false,
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
                        type: 'column'
                    },
                    items:[{
                        xtype: 'textfield',
                        fieldLabel: '责任人',
                        name: 'responsableOprID',
                        allowBlank: false,
                        disabled:dis,
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
                    allowBlank: false,
                    minValue: new Date(),
                    disabled:dis,
                    format:'Y-m-d'
                }, {
                    xtype: 'combo',
                    fieldLabel: '处理状态',
                    name: 'status',
                    emptyText: '请选择',
                    queryMode: 'local',
                    editable: false,
                    valueField: 'status',
                    displayField: 'desc',
                    store: {
                        fields: ["status", "desc"],
                        data: [
                            {status: "0", desc: '新建'},
                            {status: "1", desc: '已分配'},
                            {status: "2", desc: '已修复'},
                            {status: "3", desc: '关闭'},
                            {status: "4", desc: '重新打开'},
                            {status: "5", desc: '取消'}

                        ]
                    },
                    maxeHeight: 150,
                    width: 300,
                    disabled:dis
                }, {
                    xtype: 'tabpanel',
                    frame: true,
                    activeTab: 0,
                    plain: false,
                    resizeTabs: true,
                    defaults: {
                        autoScroll: false
                    },
                    items: [{
                        title: "描述信息",
                        xtype: 'form',
                        layout: {
                            type: 'vbox',
                            align: 'stretch'
                        },
                        border: false,
                        bodyPadding: 10,
                        items: [{
                            xtype: 'ueditor',
                            fieldLabel: '描述信息',
                            name: 'descript',
                            hideLabel: true,
                            zIndex:999,
                            disabled:dis,
                            allowBlank: true

                        }]

                    }, /*{
                        title: "上传附件",
                        xtype: 'grid',
                        frame: true,
                        columnLines: true,
                        layout: 'column',
                        name: 'attachmentGrid',
                        reference: 'attachmentGrid',
                        style:"margin:10px",
                        store: {
                            fields: [
                                {name: 'attachmentID'},
                                {name: 'attachmentName'},
                                {name: 'attachmentUrl'}
                            ],
                            proxy: Ext.tzListProxy()
                        },
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
                            text: '附件名',
                            dataIndex: 'attachmentID',
                            sortable: false,
                            width: '40%'
                        }, {
                            text: '描述信息',
                            dataIndex: 'attachmentName',
                            sortable: false,
                            width: '40%',
                            editor: {
                                xtype: 'textfield'
                            },
                            flex: 1,
                             renderer: function(v){
                             var arr = v.split("|");
                             return '<a class="fileUpload" href="'+arr[1]+'" target="_blank">'+arr[0]+'</a>';
                                    }
                        }, {
                            text: '操作',
                            sortable: false,
                            width: '20%',
                            xtype: 'actioncolumn',
                            items: [
                                {icon: 'delete.png', tooltip: '删除附件', handler: 'deleteBug'}
                            ]
                        }]
                    },*/{
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
                            store: {
                                fields: [
                                    {name:'attachmentID'},
                                    {name: 'attachmentName'},
                                    {name: 'attachmentUrl'}
                                ],
                                proxy: Ext.tzListProxy()
                            },
                           /* store: {
                                fields: [
                                    {name: 'attachmentID'},
                                    {name: 'attachmentName'},
                                    {name: 'attachmentUrl'}
                                ],
                                proxy: Ext.tzListProxy()
                            },*/

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
                                }]
                            },"-",
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
                                renderer: function(v){
                                    var arr = v.split("|");
                                    return '<a href="'+arr[1]+'" target="_blank">'+arr[0]+'</a>';
                                }
                            },{
                                menuDisabled: true,
                                sortable: false,
                                width:60,
                                xtype: 'actioncolumn',
                                items:[
                                    {iconCls: 'remove',tooltip: '删除', handler: 'deleteArtAttenment'}
                                ]
                            }]
                        }]
                    }]
                }]


            }],
            buttons: button
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
                upUrl = file.findParentByType("artInfo").child("form").getForm().findField("saveAttachAccessUrl").getValue();
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
                upUrl = file.findParentByType("artInfo").child("form").getForm().findField("saveImageAccessUrl").getValue();
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

                    tzParams = '{"ComID":"TZ_CONTENT_MG_COM","PageID":"TZ_CONTENT_INF_STD","OperateType":"HTML","comParams":' + tzParams +'}';

                    Ext.Ajax.request({
                        //url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_ATT_D.TZ_GD_ATT_FILE.FieldFormula.Iscript_AddArtAttach',
                        url: Ext.tzGetGeneralURL,
                        params: {
                            tzParams: tzParams
                        },
                        success: function(response){
                            var responseText = eval( "(" + response.responseText + ")" );
                            if(responseText.success == 0){
                                //viewStore.reload();
                                var accessPath = action.result.msg.accessPath;
                                var sltPath = action.result.msg.accessPath;
                                if(accessPath.length == (accessPath.lastIndexOf("/")+1)){
                                    accessPath = accessPath + action.result.msg.sysFileName;
                                    sltPath = sltPath + responseText.minPicSysFileName;
                                    // sltPath = sltPath + "MINI_"+action.result.msg.sysFileName;
                                }else{
                                    accessPath = accessPath + "/" + action.result.msg.sysFileName;
                                    // 	sltPath = sltPath+ "/" + "MINI_"+action.result.msg.sysFileName;
                                    sltPath = sltPath+ "/" + responseText.minPicSysFileName;
                                }

                                if(attachmentType=="IMG"){
                                    file.findParentByType("tabpanel").down('image[name=titileImage]').setSrc(accessPath);
                                    file.findParentByType("form").findParentByType("form").down('hiddenfield[name=titleImageUrl]').setValue(accessPath);
                                }

                                if(attachmentType=="ATTACHMENT"){
                                    //var applyItemGrid = this.lookupReference('attachmentGrid');
                                    var applyItemGrid = file.findParentByType("grid")
                                    var r = Ext.create('KitchenSink.view.activity.attachmentModel', {
                                        "attachmentID": action.result.msg.sysFileName,
                                        "attachmentName": "<a href='"+accessPath+"' target='_blank'>"+action.result.msg.filename+"</a>",
                                        "attachmentUrl": accessPath
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

