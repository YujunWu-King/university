Ext.define('KitchenSink.view.template.survey.question.myDcwjSzWindow', {
    extend: 'Ext.window.Window',
    xtype: 'myDcwjSzWindow',
    reference: 'myDcwjSzWindow',
    title: '调查设置',
    closable: true,
    height: 500,
    width: 800,
    modal: true,
    autoScroll: true,
    bodyStyle: 'padding: 5px;',
    actType: 'add',
    items: [{
        xtype: 'form',
      //  reference: 'wjdcSzInfoForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        //bodyPadding: 10,
        width:700,
        style:"margin:8px",
        bodyStyle:'overflow-y:auto;overflow-x:hidden',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
       items:[
           {
               xtype: 'fieldset',
               title: '基本设置',
               defaults: {
                   anchor: '100%'
               },
               items: [
                   {
                       xtype: 'textfield',
                       fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_ID", "调查问卷ID"),
                       name: 'TZ_DC_WJ_ID',
                       hidden: true
                   },
                   {
                       xtype: 'textfield',
                       fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJBT", "标题"),
                       name: 'TZ_DC_WJBT',
                       allowBlank: false,
                       afterLabelTextTpl: [
                           '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                       ]
                   },
                   {
                       xtype: 'combobox',
                       fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_ZT", "状态"),
                       forceSelection: true,
                       valueField: 'TValue',
                       displayField: 'TSDesc',
                       name:'TZ_DC_WJ_ZT',
                       store: new KitchenSink.view.common.store.appTransStore("TZ_DC_WJ_ZT"),
                       typeAhead: true,
                       readOnly:true,
                       queryMode: 'local',
                       name: 'TZ_DC_WJ_ZT',
                       value: '0'
                   },
                   {
                       xtype: 'combobox',
                       fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_FB", "发布状态"),
                       forceSelection: true,
                       valueField: 'TValue',
                       displayField: 'TSDesc',
                       name:'TZ_DC_WJ_FB',
                       cls:'lanage_1',
                       store: new KitchenSink.view.common.store.appTransStore("TZ_DC_WJ_FB"),
                       typeAhead: true,
                       readOnly:true,
                       queryMode: 'local',
                      // value:'0',
                       listeners: {
                           change:{
                               fn: function (ch,eOpts ) {
                                   if(ch.getValue()==1){
                                        var form= ch.findParentByType("window").child("form").getForm();
                                        var state=form.findField("TZ_DC_WJ_ZT");
                                        state.setReadOnly(false);
                                   }
                               }
                           }
                       },
                       name: 'TZ_DC_WJ_FB'

                   },
                   {
                       layout: {
                           type: 'column'
                       },
                       items: [
                           {
                               columnWidth: .4,
                               xtype: 'textfield',
                               fieldLabel: "问卷模板",
                               name: 'TZ_APP_TPL_ID',
                               editable: false,
                               allowBlank: true,

                               triggers: {
                                   search: {
                                       cls: 'x-form-search-trigger',
                                       handler: "wjmb_mbChoice"
                                   }
                               }
                           },
                           {
                               columnWidth: .35,
                               xtype: 'displayfield',
                               hideLabel: true,
                               name: 'TZ_APP_TPL_MC',
                               style: 'margin-left:8px'
                           },{
                               columnWidth:.25,
                               xtype:'checkboxfield',
                               name:'reLoad',
                               boxLabel:'重新加载模板',
                               fieldLabel:'重新加载模板',
                               hideLabel:true,
                               value:false,
                               handler:function(checkBox,checked){
                                       /*Ext.Msg.confirm('提示', '勾选后模板将会被重新加载！', function(btn, text){
                                           console.log(btn,text);
                                           if (btn == 'yes'){
                                             checkBox.checked=true;
                                           }else{
                                             checkBox.checked=false;
                                           }
                                       });*/
                                     if(checked==true) {
                                       Ext.Msg.alert("提示",'勾选后模板将会被重新加载');
                                     }
                               }
                           }
                       ]
                   },
                   {
                       xtype: 'datefield',
                       fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_KSRQ", "开始日期"),
                       format: 'Y-m-d',
                       width: 350,
                       allowBlank:false,
                       afterLabelTextTpl: [
                           '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                       ],
                       name: 'TZ_DC_WJ_KSRQ'
                   },
                   {
                       xtype: 'timefield',
                       fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_KSSJ", "开始时间"),
                       width: 350,
                       format: 'H:i',
                       value:'8:30',
                       afterLabelTextTpl: [
                           '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                       ],
                       name: 'TZ_DC_WJ_KSSJ'
                   },
                   {
                       xtype: 'datefield',
                       fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_JSRQ", "结束日期"),
                       format: 'Y-m-d',
                       width: 350,
                       allowBlank:false,
                       afterLabelTextTpl: [
                           '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                       ],
                       name: 'TZ_DC_WJ_JSRQ'
                   },
                   {
                       xtype: 'timefield',
                       fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_JSSJ", "结束时间"),
                       width: 350,
                       format: 'H:i',
                       value:'17:30',
                       afterLabelTextTpl: [
                           '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                       ],
                       name: 'TZ_DC_WJ_JSSJ'
                   },
                   {
                       xtype: 'ueditor',
                       fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_JTNR", "卷头内容"),
                       name: 'TZ_DC_JTNR'
                   },
                   {
                       xtype: 'ueditor',
                       fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_JWNR", "卷尾内容"),
                       name: 'TZ_DC_JWNR'
                   },
                   {
                       xtype: 'textfield',
                      // fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_PC_URL", "PC-URL"),
                       fieldLabel:"PC-URL",
                       name: 'TZ_DC_WJ_PC_URL',
                       cls:'lanage_1',/*显灰样式，表示该字段不可修改*/
                       readOnly:true
                   },
                   {
                       xtype: 'textfield',
                      // fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_MB_URL", "移动-URL"),
                       fieldLabel:"移动-URL",
                       name: 'TZ_DC_WJ_MB_URL',
                       cls:'lanage_1',
                       readOnly:true
                   },
                   {
                       xtype: 'checkboxfield',
                       fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_DLZT", "非登录用户也可以参与本次调查"),
                       name: 'TZ_DC_WJ_DLZT',
                       boxLabel:'非登录用户也可以参与本次调查',
                       hideLabel:true,
                       handler:"checkBoxAction"
                   }
               ]
           },
           {
               xtype: 'fieldset',
               title: '规则设置',
               defaults: {
                   anchor: '100%'
               },
               items:[{
                   xtype: 'radiogroup',
                   fieldLabel: '答题规则',
                   msgTarget: 'side',
                   autoFitErrors: false,
                   id:'dtgz',
                   allowBlank:false,
                   afterLabelTextTpl: [
                       '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                   ],
                   defaults: {
                       flex: 1
                   },
                   layout: 'column',
                   items: [  {
                       boxLabel  : '调查过程中可以修改过往问题答案',
                       name       : 'TZ_DC_WJ_DTGZ',
                       inputValue: '0',
                       columnWidth: 0.5

                   }, {
                       boxLabel  : '不能修改过往问题答案',
                       name      : 'TZ_DC_WJ_DTGZ',
                       inputValue: '1',
                       columnWidth: 0.5
                   }, {
                       boxLabel  : '只要还在调查期间内，就可以修改已提交调查问卷的答案',
                       name      : 'TZ_DC_WJ_DTGZ',
                       inputValue: '2',
                       columnWidth: 1
                   }]
               },{     xtype: 'radiogroup',
                       fieldLabel: '采集规则',
                       allowBlank: false,
                       msgTarget: 'side',
                       autoFitErrors: false,
                       id:'sjcjgz',
                       afterLabelTextTpl: [
                           '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                       ],
                       layout: 'column',
                       items: [  {
                           boxLabel  : '同一个IP只能参与一次',
                           name      : 'TZ_DC_WJ_IPGZ',
                           inputValue: '0',
                           columnWidth:0.5

                       }, {
                           boxLabel  : '同一台电脑只能参与一次',
                           name      : 'TZ_DC_WJ_IPGZ',
                           inputValue: '1',
                           columnWidth:0.5
                       }, {
                           boxLabel  : '不限制',
                           name      : 'TZ_DC_WJ_IPGZ',
                           inputValue: '2',
                           columnWidth:0.5
                       },{
                           boxLabel  : '同一个账号只能参与一次',
                           name      : 'TZ_DC_WJ_IPGZ',
                           inputValue: '3',
                           columnWidth:0.5
                       }]
                   },
                  {
                       xtype: 'radiogroup',
                       fieldLabel: '完成规则',
                       allowBlank: false,
                       msgTarget: 'side',
                       autoFitErrors: false,
                       id:'wcgz',
                       afterLabelTextTpl: [
                           '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                       ],
                       layout: 'column',
                       items:  [
                            {
                                boxLabel  : '只有答完所有题才能结束调查',
                                name      : 'TZ_DC_WJ_JSGZ',
                                itemId:'TZ_DC_WJ_JSGZ_1',
                                inputValue: '0',
                                columnWidth:1

                            }, {
                                boxLabel  : '答题过程中即可单击完成调查以结束调查',
                                name      : 'TZ_DC_WJ_JSGZ',
                                itemId:'TZ_DC_WJ_JSGZ_2',
                                inputValue: '1',
                                columnWidth:1
                            }
                        ]
                   }, {
                       xtype: 'radiogroup',
                       fieldLabel: '调查方式',
                       msgTarget: 'side',
                       autoFitErrors: false,
                       id:'dcfs',
                       afterLabelTextTpl: [
                           '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                       ],
                       //allowBlank:true,
                       layout: 'column',
                       items: [ {  xtype: 'radiofield',
                                   boxLabel: '匿名调查',
                                   name: 'TZ_DC_WJ_NM',
                                   inputValue:'0',
                                   columnWidth:0.5},
                                 {   xtype: 'radiofield',
                                     boxLabel: '记名调查',
                                     name: 'TZ_DC_WJ_NM',
                                     inputValue: '1',
                                     columnWidth:0.5}]
                   },{
                       xtype: 'checkboxfield',
                       fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_NEEDPWD", "是否启用调查密码"),
                       name: 'TZ_DC_WJ_NEEDPWD',
                       boxLabel:'启用调查密码',
                       hideLabel:true,
                       handler:'needPwdFun'
                   },{
                       xtype:'textfield',
                       fieldLabel:'调查密码',
                       name:'TZ_DC_WJ_PWD',
                       hidden:true,
                       id:'TZ_DC_WJ_PWD'
                   }
               ]
           }]
    }],
    buttons: [{
        text:'发布',
        iconCls:' publish',
        handler:'onWjdcRelease'
    },{
        text: '保存',
        iconCls: "save",
        handler: function(btn){
         
           var win=btn.findParentByType("window");
           win.doSave(win);
        }
    },{
        text: '确定',
        iconCls: "ensure",
        handler:function(btn){
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();
            var grid = btn.findParentByType("wjdcInfo");

            if(!form.isValid()){
                return false;
            }
            var formParams = form.getValues();
            console.log(formParams);
            var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_WJSZ_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
            Ext.tzSubmit(tzParams,function(response){
                console.log(response.responseText)
                grid.store.reload();
                win.close();
            },"",true,win);
        }
    },
      {
        text: '关闭',
        iconCls: "close",
        handler: function(btn){
          var win=btn.findParentByType("window");
          win.close();
         }
      }],
   doSave:function(win){
       var form = win.child("form").getForm();
       if(!form.isValid() ){
           return false;
       }
       var formParams = form.getValues();
       console.log(formParams);
       var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_WJSZ_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
       Ext.tzSubmit(tzParams,function(response){
           win.findParentByType("wjdcInfo").store.reload();
       },"",true,this);

   }
});
