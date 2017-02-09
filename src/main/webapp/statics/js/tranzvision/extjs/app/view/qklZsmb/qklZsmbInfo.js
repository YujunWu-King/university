Ext.define('KitchenSink.view.qklZsmb.qklZsmbInfo', {
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.Ueditor',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.qklZsmb.zsmbController'
    ],
    extend : 'Ext.panel.Panel',
    controller:'zsmbController',
    autoScroll:false,
    actType:'add',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    title:'证书模板定义',
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
                    fieldLabel: "机构",
                    hidden:true,
                    name: 'JgId',
                    value:Ext.tzOrgID
                },{
                    xtype: 'textfield',
                    fieldLabel: '证书模板编号',
                    name: 'certTmpl',
                    maxLength: 15,
                    allowBlank: false,
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ],
                },{
                    xtype: 'textfield',
                    fieldLabel: '证书模板名称',
                    name: 'tmplName',
                    allowBlank: false,
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ],
                },{
                    xtype: 'combo',
                    fieldLabel: '证书类型',
                    name: 'certTypeID',
                    allowBlank: false,
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ],
                    emptyText:'请选择...',
                    queryMode: 'remote',
                    editable:false,
                    valueField: 'TZ_CERT_TYPE_ID',
                    displayField: 'TZ_CERT_TYPE_NAME',
                    store:new KitchenSink.view.common.store.comboxStore({
                            recname: 'TZ_CERT_TYPE_TBL',
                            condition:{
                        },
                        result:'TZ_CERT_TYPE_ID,TZ_CERT_TYPE_NAME'
    })
    },{
    	xtype: 'combo',
        fieldLabel: '证书颁发机构',
        name: 'certJGID',
        allowBlank: false,
        afterLabelTextTpl: [
        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
    ],
        emptyText:'请选择...',
        queryMode: 'remote',
        editable:false,
        valueField: 'TZ_CERT_JG_ID',
        displayField: 'TZ_CERT_JG_NAME',
        store:new KitchenSink.view.common.store.comboxStore({
            recname: 'TZ_JG_LINKEDIN_TBL',
            condition:{},
        result:'TZ_CERT_JG_ID,TZ_CERT_JG_NAME'
})
},{
    xtype: 'combo',
        labelWidth: 100,
        editable: false,
        fieldLabel: '启用状态',
        name: 'useFlag',
        emptyText: '请选择',
        mode: "remote",
        valueField: 'useFlag',
        displayField: 'useSDesc',
        store: {
            fields: ["useSta", "useSDesc"],
            data: [
                {useFlag: "Y", useSDesc: "启用"},
                {useFlag: "N", useSDesc: "不启用"}
            ]
        },
        value:"N"
        //valueField: 'TValue',
        //displayField: 'TSDesc',
        //store: new KitchenSink.view.common.store.appTransStore("TZ_QY_BZ")
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
        title:'证书套打模板',
        xtype:'form',
        name:'certMergHtml1',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        height: 415,
        style:'border:0',
        items:[{
            xtype: 'ueditor',
            name: 'certMergHtml1',
            zIndex:999,
            height: 415,
            allowBlank: true

        }]
    },{
        title:'拥有人查看模版',
        xtype:'form',
        name:'certMergHtml2',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        height: 415,
        style:'border:0',
        items:[{
            xtype: 'ueditor',
            name: 'certMergHtml2',
            zIndex:999,
            height: 415,
            allowBlank: true

        }]
    },{
        title:'他人查看模版',
        xtype:'form',
        name:'certMergHtml3',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        height: 415,
        style:'border:0',
        items:[{
            xtype: 'ueditor',
            name: 'certMergHtml3',
            zIndex:999,
            height: 415,
            allowBlank: true

        }]
    }]
},{
    xtype: 'form',
        layout: 'hbox',
        width:'100%',
        height:'100%',
        name:'imagesForm',
        defaults:{
        margin:'20px 0 0 20px',
    },
    items:[{
        xtype: 'hiddenfield',
        name: 'titleImageName'
    },{
        xtype: 'hiddenfield',
        name: 'imageAUrl'
    },{
        margin:'20 35 0 10',
        xtype:'label',
        html:'<span style="font-weight:bold">'+ '标题图'+':</span>'
    },{
        xtype:'image',
        width:70,
        height:50,
        border:1,
        style: {
            borderColor: '#eee'
        },
        margin:'10 20 10 0',
        src:'',
        name:'titleImage'
    },{
        xtype:'button',
        text:'删除',
        listeners:{
            click:function(bt, value, eOpts){
                deleteImage(bt, value, eOpts);
            }
        }
    },{
        xtype: 'fileuploadfield',
        name: 'orguploadfile',
        buttonText: '上传',
        //msgTarget: 'side',
        buttonOnly:true,
        listeners:{
            change:function(file, value, eOpts){
                addAttach(file, value, "IMG");
            }
        }
    }]
}]
}],
buttons:[
    /*{
     text: '预览',
     handler:'seeTmplDfn',
     iconCls:'send'
     },*/
    {
        text: '保存',
        handler:'saveTmplDfn',
        iconCls:'save'
    },{
        text:'确定',
        handler:'ensureTmplDfn',
        iconCls:'ensure'
    },{
        text:'关闭',
        iconCls:'close',
        handler:'closeTmplDfn'
    }
]
});
this.callParent();
}
});
function deleteImage( bt, e, eOpts){
    bt.findParentByType("form").down('image[name=titleImage]').setSrc("");
    bt.findParentByType("form").findParentByType("form").down('hiddenfield[name=titleImageName]').setValue("");
    bt.findParentByType("form").findParentByType("form").down('hiddenfield[name=imageAUrl]').setValue("");

}


function addAttach(file, value, attachmentType){

    var form = file.findParentByType("form").getForm();

    if(value != ""){
        if(attachmentType=="IMG" ){
            var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
            if(fix.toLowerCase() != "jpg" && fix.toLowerCase() != "jpeg" && fix.toLowerCase() != "png" && fix.toLowerCase() != "gif" && fix.toLowerCase() != "bmp"){
                Ext.MessageBox.alert("提示","请上传jpg|jpeg|png|gif|bmp格式的图片。");
                form.reset();
                return;
            };
        }


        var dateStr = Ext.Date.format(new Date(), 'Ymd');


        var upUrl = "";
        if(upUrl==""){
            upUrl = TzUniversityContextPath + '/UpdServlet?filePath=activity';
        }else{
            upUrl = TzUniversityContextPath + '/UpdServlet?filePath=activity';
        }

        var myMask = new Ext.LoadMask({
            msg    : '加载中...',
            target : Ext.getCmp('tranzvision-framework-content-panel')
        });

        myMask.show();
        form.submit({
            url: upUrl,
            //waitMsg: '图片正在上传，请耐心等待....',
            success: function (form, action) {
                var tzParams = '{"attachmentType":"' + attachmentType + '","data":' + Ext.JSON.encode(action.result.msg) + '}';
                tzParams = '{"ComID":"TZ_ZHENGSHU_COM","PageID":"TZ_MOBAN_INFO_STD","OperateType":"HTML","comParams":' + tzParams +'}';
                Ext.Ajax.request({
                    url: Ext.tzGetGeneralURL,
                    params: {
                        tzParams: tzParams
                    },
                    success: function(response){
                        var responseText = eval( "(" + response.responseText + ")" );
                        if(responseText.success == 0){
                            var accessPath = action.result.msg.accessPath;
                            var sltPath = action.result.msg.accessPath;
                            if(accessPath.length == (accessPath.lastIndexOf("/")+1)){
                                accessPath = accessPath + action.result.msg.sysFileName;
                                sltPath = sltPath + responseText.minPicSysFileName;
                            }else{
                                accessPath = accessPath + "/" + action.result.msg.sysFileName;
                                sltPath = sltPath+ "/" + responseText.minPicSysFileName;
                            }
                            if(attachmentType=="IMG"){
                                file.findParentByType("form").down('image[name=titleImage]').setSrc(TzUniversityContextPath + accessPath);
                                file.findParentByType("form").down('hiddenfield[name=titleImageName]').setValue(action.result.msg.sysFileName);
                                file.findParentByType("form").down('hiddenfield[name=imageAUrl]').setValue(accessPath);

                            }else{
                                Ext.Msg.alert("提示", responseText.message);
                            }
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




