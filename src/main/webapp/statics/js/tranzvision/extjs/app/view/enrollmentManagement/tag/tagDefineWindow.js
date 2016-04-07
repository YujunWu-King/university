Ext.define('KitchenSink.view.enrollmentManagement.tag.tagDefineWindow', {
    extend: 'Ext.window.Window',
    xtype: 'tagDefineWindow',
    controller: 'tagSet',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.tag.tagController'
    ],
    title:  Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.tags","标签设置"),
    reference:'tagDefineWindow',
    width:500,
    y:10,
    actType: 'add',
    modal:true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        items: [
            /*{
                xtype: 'numberfield',
                fieldLabel: Ext.tzGetResourse("TZ_BMGL_SXSZ_COM.TZ_BMSX_NEW_STD.attrSeq","显示顺序"),
                name:'attrSeq',
                value:1000,
                hidden:true
            },*/{
                xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.tagId","标签ID"),
                name:'tagId',
                //allowBlank: false,
                ignoreChangesFlag: true,
                hidden:true,
                value:'NEXT'
            },
            {
                xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.tagName","标签名称"),
                maxLength:40,
                name: 'tagName',
                allowBlank:false
            }
        ]
    }],
    buttons: [{
        text:  Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.save","保存"),
        iconCls:"save",
        handler: function(btn){
            //获取窗口
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();
            win.doSave(win);
        }
    },{
        text:Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.ensure","确定"),
        iconCls:"ensure",
        //handler: 'ensureAttr'
        handler: function(btn){
            //获取窗口
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();

            if(!form.isValid()){
                return false;
            }
            var formParams = form.getValues();

            var tzParams = '{"ComID":"TZ_BMGL_TAG_COM","PageID":"TZ_BMGL_TAG_BJ_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
            Ext.tzSubmit(tzParams,function(response){
                win.findParentByType("tagSet").store.reload();
                win.close();
            },"",true,win);
        }
    }, {
        text: Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.close","关闭"),
        iconCls:"close",
        handler: function(btn){
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();
            win.close();
        }
    }],
    doSave:function(win){
        //保存
        var form = win.child("form").getForm();
        if(!form.isValid()){
            return false;
        }
        var formParams = form.getValues();

        var tzParams = '{"ComID":"TZ_BMGL_TAG_COM","PageID":"TZ_BMGL_TAG_BJ_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
        Ext.tzSubmit(tzParams,function(response){
        win.findParentByType("tagSet").store.reload();
        },"",true,this);

    }
});
