Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.setTjxPassword', {
    extend: 'Ext.window.Window',
    xtype: 'setTjxPassword',
    controller: 'auditApplicationForm',
    reference: 'setTjxPasswordWindow',
    title: '重置推荐信密码',
    width: 400,
    height: 200,
    minWidth: 350,
    minHeight: 200,
    layout: 'fit',
    resizable: true,
    modal: true,
    closeAction: 'hide',
	appTjxInsId:"",
    
    items: [{
        xtype: 'form',
        reference: 'setTjxPasswordForm',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,

        fieldDefaults: {
            msgTarget: 'side',
            labelAlign: 'left',
            labelWidth: 120,
            labelStyle: 'font-weight:bold'
        },
        
        items: [{
            xtype: 'textfield',
            //fieldLabel: '重置密码',
            fieldLabel:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.password","请输入新密码"),
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            maxLength: 32,
            name: 'password',
            inputType: 'password',
            allowBlank: false
        }],

        buttons: [{
            text: '确定',
            iconCls: 'ensure',
            handler: 'onSetPwdEnsure'
        }, {
            text: '关闭',
            iconCls: 'close',
            handler: 'onSetPwdClose'
        }]
    }]
});