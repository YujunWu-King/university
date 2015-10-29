Ext.define('tranzvision.extension.ClearValue', {
    alias: 'plugin.clearvalue',
    trigger1Cls: Ext.baseCSSPrefix + 'form-clear-trigger',
    trigger2Cls: Ext.baseCSSPrefix + 'form-arrow-trigger',
    hasSearch: false,
    constructor: function(config) {
        var me = this;
        Ext.apply(me, config);
    },
    init: function(combo) {
        var me = this;
        combo.hasSearch = me.hasSearch;
        combo.trigger1Cls = me.trigger1Cls;
        combo.trigger2Cls = me.trigger2Cls;
        combo.onTrigger1Click = me.onTrigger1Click;
        combo.onTrigger2Click = me.onTrigger2Click;
        combo.on('afterrender', me.afterRender, combo);
    },
    afterRender: function() {
        this.triggerCell.item(0).setDisplayed(false);
    },
    onTrigger1Click: function() {
        var me = this;
        if (me.hasSearch) {
            me.setValue('');
            me.hasSearch = false;
            me.triggerCell.item(0).setDisplayed(false);
            me.updateLayout();
        }
    },
    onTrigger2Click: function() {
        this.onTriggerClick();
        this.hasSearch = true;
        this.triggerCell.item(0).setDisplayed(true);
    }
});
