
Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.zldbWindow', {
  extend: 'Ext.window.Window',
    requires: [
        'KitchenSink.view.enrollmentManagement.applicationForm.appFormPackageController'
    ],
  xtype: 'zldbWindow0',
  controller: 'appFormPackage',
  title: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.materialsPacking","材料打包"),
  width: 500,
  minHeight: 200,
  maxHeight: 400,
  resizable: true,
  modal: true,
  closeAction: 'destroy',
  bodyStyle: 'overflow-y:auto;overflow-x:hidden',

    items: [{
    xtype: 'form',
    reference: 'zldb0Form',
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    border: false,
    bodyPadding: 10,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',

    fieldDefaults: {
        msgTarget: 'side',
        labelWidth: 130,
        labelStyle: 'font-weight:bold'
    },

    items: [{
        xtype: 'label',
        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.inputPackageName","请输入压缩包文件名")
        },{
        xtype: 'textfield',
        name: 'ysFilesName',
        allowBlank: false
       },{
        xtype: 'textfield',
        fieldLabel: '报名表编号',
        name: 'appInsID',
        hidden: true
    }]
}],
    buttons: [{
    text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.ensure","确定"),
    iconCls:"ensure",
    handler: 'qrZldb'
}, {
    text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
    iconCls:"close",
    handler: 'qxZldb'
}]
});
