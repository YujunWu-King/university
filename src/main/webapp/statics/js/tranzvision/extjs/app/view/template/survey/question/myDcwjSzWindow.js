Ext.define('KitchenSink.view.template.survey.question.myDcwjSzWindow', {
    extend: 'Ext.panel.Panel',
    xtype: 'myDcwjSzWindow',
    reference: 'myDcwjSzWindow',
    controller: 'wjdcController',
    title: '调查问卷设置',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',
    parentGridStore:"",

     initComponent:function(){
         Ext.apply(this,{
             items: [{
                 xtype: 'form',
                 reference: 'wjdcSzInfoForm',
                 layout: {
                     type: 'vbox',
                     align: 'stretch'
                 },
                 border: false,
                 //bodyPadding: 10,
                 // width:700,
                 style:"margin:8px",
                 bodyStyle:'overflow-y:auto;overflow-x:hidden',
                 fieldDefaults: {
                     msgTarget: 'side',
                     labelWidth: 95,
                     labelStyle: 'font-weight:bold'
                 },
             items:[{
                     xtype: 'fieldset',
                     title: '基本设置',
                     defaults: {
                         anchor: '100%'
                     },
                     items: [{
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
                             editable : false,
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
                             hiddenName:'TZ_DC_WJ_FB',
                             cls:'lanage_1',
                             store: new KitchenSink.view.common.store.appTransStore("TZ_DC_WJ_FB"),
                             editable : false,
                             readOnly:true,
                             queryMode: 'local',
                             value:'0',
                             listeners: {
                                 change:{
                                     fn: function (ch,eOpts ) {
                                       //如果发布状态为‘发布’，那么状态值可以修改；如果为‘未发布’，状态值不能修改
                                       //如果状态已经是发布状态，那么发布按钮不能用
                                         if(ch.getValue()==1){
                                             var form= ch.findParentByType("form").getForm();
                                             var state=form.findField("TZ_DC_WJ_ZT");
                                             state.setReadOnly(false);
                                             wjPublishBtn.setDisabled(true);
                                         }
                                     }
                                 }
                             },
                             name: 'TZ_DC_WJ_FB'
                         },
                         {
                             xtype: 'combobox',
                             fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_APP_TPL_LAN", "问卷语言"),
                             editable: false,
                             emptyText: '请选择',
                             queryMode: 'local',
                             name: 'TZ_APP_TPL_LAN',
                             hiddenName:'TZ_APP_TPL_LAN',
                             valueField: 'TValue',
                             cls:'lanage_1',
                             value:'ZHS',
                             displayField: 'TSDesc',
                             store: new KitchenSink.view.common.store.appTransStore("TZ_APP_TPL_LAN"),
                             allowBlank: false,
                             readOnly:true
                         },
                         {
                             layout: {
                                 type: 'column'
                             },
                             items: [
                                 {
                                     columnWidth: .4,
                                     xtype: 'textfield',
                                     fieldLabel: "问卷模板ID",
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
                                     columnWidth:0.15,
                                     xtype:'button',
                                     text:'重新加载模板',
                                     listeners:{
                                         click:function(button,e,eOpts ){
                                             Ext.Msg.confirm('警告', '是否要重新加载模板，点击确定，模板会被重新加载!', function(btn){
                                                 if(btn=='yes'){
                                                     //获得该页面上的问卷ID
                                                     var wjId= button.findParentByType("form").getForm().findField("TZ_DC_WJ_ID").value;
                                                     var tplId=button.findParentByType("form").getForm().findField("TZ_APP_TPL_ID").value;
                                                     //与后台交互，更新该问卷的json报文 U可以是add delete update
                                                     var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_XJWJ_STD","OperateType":"U","comParams":{"update":[{"wjId":"' + wjId + '","tplId":"'+tplId+'"}]}}';
                                                     Ext.tzLoad(tzParams, function (responseData) {
                                                         // console.log(responseData.responseText)
                                                     });
                                                 }
                                             });
                                         }
                                     }
                                 }

                             ]
                         },
                         {
                             xtype:'label',
                             text:'请注意：问卷仅在开始时间和结束时间期间内对外有效，其他时间调查对象不能进行问卷的填写。',
                             cls: 'lable_1',
                             style: 'margin-left:98px'
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
                             format: 'H:i:s',
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
                             format: 'H:i:s',
                             value:'17:30',
                             afterLabelTextTpl: [
                                 '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                             ],
                             name: 'TZ_DC_WJ_JSSJ'
                         },
                         {
                             xtype: 'ueditor',
                             fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_JTNR", "卷头内容"),
                             name: 'TZ_DC_JTNR',
                             zIndex:999
                         },
                         {
                             xtype: 'ueditor',
                             fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_JWNR", "卷尾内容"),
                             name: 'TZ_DC_JWNR',
                             zIndex:999
                         },
                         {
                             xtype:'textfield',
                             fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_URL", "发布URL"),
                             name:'TZ_DC_WJ_URL',
                             cls:'lanage_1',
                             readOnly:true
                         }, /*
                          {
                          xtype: 'textfield',
                          // fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_PC_URL", "PC-URL"),
                          fieldLabel:"PC-URL",
                          name: 'TZ_DC_WJ_PC_URL',
                          cls:'lanage_1',/*显灰样式，表示该字段不可修改
                          readOnly:true
                          },*/
                         /* {
                          xtype: 'textfield',
                          // fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_MB_URL", "移动-URL"),
                          fieldLabel:"移动-URL",
                          name: 'TZ_DC_WJ_MB_URL',
                          cls:'lanage_1',
                          readOnly:true
                          },*/
                         {
                             xtype: 'checkboxfield',
                             fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_DLZT", "非登录用户也可以参与本次调查"),
                             name: 'TZ_DC_WJ_DLZT',
                             boxLabel:'非登录用户也可以参与本次调查',
                             hideLabel:true,
                             ignoreChangesFlag:true,
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
                         name:'dtgz',
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
                     },{  xtype: 'radiogroup',
                         fieldLabel: '采集规则',
                         allowBlank: false,
                         msgTarget: 'side',
                         autoFitErrors: false,
                         name:'sjcjgz',
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
                     },{
                             xtype: 'radiogroup',
                             fieldLabel: '完成规则',
                             allowBlank: false,
                             msgTarget: 'side',
                             autoFitErrors: false,
                             name:'wcgz',
                             hidden:true,
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
                         },/* {
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
                          },*/{
                             xtype: 'checkboxfield',
                             fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_NEEDPWD", "启用调查密码"),
                             name: 'TZ_DC_WJ_NEEDPWD',
                             boxLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_NEEDPWD", "启用调查密码"),
                             hideLabel:true,
			     ignoreChangesFlag:true,
                             handler:'needPwdFun'
                         },{
                             xtype:'textfield',
                             fieldLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_PWD", "调查密码"),
                             name:'TZ_DC_WJ_PWD',
                             hidden:true

                         }
                     ]
                 }, {
                     xtype: 'fieldset',
                     title: '前导页/结果页设置',
                     defaults: {
                         anchor: '100%'
                     },
                     items: [
                         {
                             xtype:'checkboxfield',
                             fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_QYQD", "启用前导页"),
                             name: 'TZ_DC_WJ_QYQD',
                             inputValue:'Y',
                             uncheckedValue:'N',
                             boxLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_QYQD", "启用前导页"),
                             hideLabel:true
                         },
                         {
                             xtype: 'ueditor',
                             fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_QDNR", "前导页内容"),
                             name: 'TZ_DC_WJ_QDNR',
                             zIndex:999
                         },
                         {
                             xtype: 'ueditor',
                             fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_JGNR", "结果页内容"),
                             name: 'TZ_DC_WJ_JGNR',
                             zIndex:999
                         },
                         {
                             xtype:'checkboxfield',
                             fieldLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_SFTZ", "是否跳转"),
                             name: 'TZ_DC_WJ_SFTZ',
                             inputValue:'Y',
                             uncheckedValue:'N',
                             boxLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_SFTZ", "是否跳转"),
                             hideLabel:true
                         },{
                             xtype:'textfield',
                             fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_TZDZ", "跳转地址"),
                             name:'TZ_DC_WJ_TZDZ'
                         }, {
                             xtype:'textfield',
                             fieldLabel: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_WJGL_STD.TZ_DC_WJ_TLSJ", "停留时间(秒)"),
                             name:'TZ_DC_WJ_TLSJ',
                             value:0
                         }
                     ]
                 }]
             }],
             buttons: [{
                 text:'发布',
                 iconCls:' publish',
                 reference:'wjPublishBtn',
                 handler:'onWjdcRelease'
             },{
                 text: '保存',
                 iconCls: "save",
                 handler:'onFormSave'
             },{
                 text: '确定',
                 iconCls: "ensure",
                 handler:'onFormEnsure'
             },{
                 text: '关闭',
                 iconCls: "close",
                 handler:'onFormClose'
             }]
         })
         this.callParent();
         var wjPublishBtn=this.lookupReference('wjPublishBtn');
     }

});
