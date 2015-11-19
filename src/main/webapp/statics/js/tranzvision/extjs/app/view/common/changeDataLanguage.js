Ext.define('KitchenSink.view.common.changeDataLanguage', {
    extend: 'Ext.window.Window',
    xtype: 'changeDataLanguage',
    requires:
    [
      'KitchenSink.view.common.store.appTransStore'
    ],

    title: '切换语言',
    width:450,
    height:200,
    modal:true,
    ignoreChangesFlag: true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    ensureHandler: null,
    cancelHandler: null,
    items:
    [
      {
        xtype: 'form',
        layout:
        {
          type: 'vbox',
          align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        ignoreLabelWidth: true,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',
        fieldDefaults:
        {
          msgTarget: 'side',
          labelWidth: 80,
          labelStyle: 'font-weight:bold',
          margin:'30 40 0 20'
        },
        items:
        [
          {
            xtype: 'combobox',
            fieldLabel: '语言名称',
            name: 'msgLanage',
            queryMode: 'remote',
            editable:false,
            valueField: 'TValue',
            displayField: 'TSDesc',
            store: new KitchenSink.view.common.store.appTransStore("TZ_LANGUAGE_ID")
          }
        ]
      }
    ],
    buttons:
    [
      {
        text: '确定',
        iconCls:"ensure",
        handler: function(btn)
        {
          var me = this.findParentByType("changeDataLanguage");

          var myLanguage = me.child('form').getForm().getValues()["msgLanage"];
          if(me.ensureHandler && typeof me.ensureHandler === "function")
          {
            me.ensureHandler(myLanguage);
          }

          me.close();
        }
      },
      {
        text: '关闭',
        iconCls:"close",
        handler: function(btn)
        {
          var me = this.findParentByType("changeDataLanguage");;

          var myLanguage = me.child('form').getForm().getValues()["msgLanage"];
          if(me.cancelHandler && typeof me.cancelHandler === "function")
          {
            me.cancelHandler(myLanguage);
          }

          me.close();
        }
      }
    ]
});
