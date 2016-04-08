Ext.define('KitchenSink.view.enrollmentManagement.color.colorWindow', {
    extend: 'Ext.window.Window',
    xtype: 'colorWindow',
    controller: 'colorSet',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.color.colorController'
    ],
    title:  Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.colors","颜色类别定义"),
    reference:'colorWindow',
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
           {
                xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.colorSortID","颜色ID"),
                name:'colorSortID',
                //allowBlank: false,
                ignoreChangesFlag: true,
                hidden:true,
                value:'NEXT'
            },
            {
                xtype: 'colorfield',
                fieldLabel:Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.color","颜色"),
                name: 'colorCode',
                allowBlank:false,
                renderer:function(value){
                    return "<div class='x-colorpicker-field-swatch-inner'' style='width:80%;height:50%;background-color: #"+value+"'></div>"+value;
                }
            },
            {
                xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.colorName","颜色名称"),
                maxLength:40,
                name: 'colorName',
                allowBlank:false
            }
        ]
    }],
    buttons: [{
        text:  Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.save","保存"),
        iconCls:"save",
        handler: function(btn){
            //获取窗口
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();
            win.doSave(win);
        }
    },{
        text:Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.ensure","确定"),
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

            var tzParams = '{"ComID":"TZ_BMGL_COLOR_COM","PageID":"TZ_BMGL_COLOR2_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
            Ext.tzSubmit(tzParams,function(response){
                win.findParentByType("colorSet").store.reload();
                win.close();
            },"",true,win);
        }
    }, {
        text: Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.close","关闭"),
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


        var tzParams = '{"ComID":"TZ_BMGL_COLOR_COM","PageID":"TZ_BMGL_COLOR2_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
        Ext.tzSubmit(tzParams,function(response){
            var colorId = formParams.colorSortID;
            if(colorId == 'NEXT'){
            	form.setValues({colorSortID:response});
            };
        	win.findParentByType("colorSet").store.reload();
        },"",true,this);

    }
});
