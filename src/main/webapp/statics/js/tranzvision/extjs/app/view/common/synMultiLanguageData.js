Ext.define('KitchenSink.view.common.synMultiLanguageData', {
    extend: 'Ext.window.Window',
    xtype: 'synMultiLanguageData',
    requires:
    [
      'KitchenSink.view.common.store.appTransStore'
    ],

    title: '同步资源',
    width:450,
    height:250,
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
          labelWidth: 100,
          labelStyle: 'font-weight:bold',
          padding:'20 40 0 20'
        },
        items:
        [
          {
            xtype: 'combobox',
            allowBlank:false,
            fieldLabel: '*源语言',
            name: 'sourceLanage',
            queryMode: 'remote',
            editable:false,
            valueField: 'TValue',
            displayField: 'TSDesc',
            store: new KitchenSink.view.common.store.appTransStore("TZ_LANGUAGE_ID")
          },
          {
            xtype: 'combobox',
            allowBlank:false,
            fieldLabel: '*目标语言',
            name: 'targetLanage',
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
          var me = this.findParentByType("synMultiLanguageData");

          var sLanguage = me.child('form').getForm().getValues()["sourceLanage"];
          var tLanguage = me.child('form').getForm().getValues()["targetLanage"];
          if(me.ensureHandler && typeof me.ensureHandler === "function")
          {
            me.ensureHandler(sLanguage,tLanguage);
          }

          me.close();
        }
      },
      {
        text: '关闭',
        iconCls:"close",
        handler: function(btn)
        {
          var me = this.findParentByType("synMultiLanguageData");;

          var sLanguage = me.child('form').getForm().getValues()["sourceLanage"];
          var tLanguage = me.child('form').getForm().getValues()["targetLanage"];
          if(me.cancelHandler && typeof me.cancelHandler === "function")
          {
            me.cancelHandler(sLanguage,tLanguage);
          }

          me.close();
        }
      }
    ]
});
