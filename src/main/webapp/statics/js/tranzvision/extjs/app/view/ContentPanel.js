Ext.define('KitchenSink.view.ContentPanel', {
    extend: 'Ext.tab.Panel',
    xtype: 'contentPanel',
    requires:['Ext.ux.TabCloseMenu', 'tranzvision.extension.ContentPanelMaximize'],
    id: 'tranzvision-framework-content-panel',
    plugins:[
        {
            ptype:'tabclosemenu',
            closeTabText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00076"),
            closeOthersTabsText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00077"),
            closeAllTabsText:TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00078"),
            closeIconCls:'tab_close'
        },
        {
            ptype:'contentpanelmaximize'
        }
    ],
    showTitle: true,
    scrollable: true,
    plain: true,
    border: false,
    defaults:
    {
        closable: true
    },
    header:
    {
        hidden: true
    }
});
