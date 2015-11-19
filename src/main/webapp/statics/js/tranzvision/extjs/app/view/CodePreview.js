Ext.define('KitchenSink.view.CodePreview', {
    extend: 'Ext.tab.Panel',
    requires: [
        'KitchenSink.view.CodeContent'
    ],

    xtype: 'codePreview',

    // The code must be read in LTR
    bodyPadding: 5,
    bodyStyle: 'direction: ltr;',

    tools: [{
        type: 'maximize',
        tooltip: TranzvisionMeikecityAdvanced.Boot.languagePackage['tz-frmwrk-lang-00005']
    }],
    showTitle: true,

    initComponent: function() {
        if (this.showTitle) {
            this.title = TranzvisionMeikecityAdvanced.Boot.languagePackage['tz-frmwrk-lang-00004'];
        }
        this.callParent(arguments);
    }
});
