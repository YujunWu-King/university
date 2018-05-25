Ext.define('tranzvision.extension.grid.feature.CheckboxGrouping', {
    extend: 'Ext.grid.feature.Grouping',
    alias: 'feature.CheckboxGrouping',

    /** @property */
    targetCls: 'group-checkbox',
    /** @property */
    checkDataIndex: 'isChecked',
    startCollapsed: true,

    constructor: function(config) {
        //config.groupHeaderTpl = ['<input class="' + this.targetCls + '" {[values.record="undefined" ? "" : (values.record.get("' + this.checkDataIndex + '") ? "checked" : "")]} type="checkbox" > {name} (重复线索数量：{rows.length}个) ' ];
        config.groupHeaderTpl = ['<input class="' + this.targetCls + '" {[values.record.get("' + this.checkDataIndex + '") ? "checked" : ""]} type="checkbox" > ',
                                '{name:this.formatName}',
                                {
                                    formatName: function(name) {
                                        return name.split(",")[1];
                                    }
                                },
                                '(重复线索数量：{rows.length}个) ' ];
        //config.groupHeaderTpl = ['<input class="' + this.targetCls + '"  type="checkbox"> {name} (重复线索数量：{rows.length}个) ' ];
        this.callParent(arguments);
    },

    init: function(grid) {
        var store = grid.getStore();
        if (store) {
            store.on('update', this.onStoreUpdate, this);
        }
        this.callParent(arguments);
    },

    setupRowData: function(record, idx, rowValues) {
        this.callParent(arguments);
        // Ext JS 6 vs Ext JS 5.1.1 vs Ext JS 5.1.0-
        var groupInfo = this.groupRenderInfo || this.metaGroupCache || this.groupInfo;
        groupInfo.record = this.getParentRecord(record.get(this.getGroupField()));
    },

    /**
     * This method will only run once... on the initial load of the view... this
     * is so we can check the store for the grouped item's children... if they're
     * all checked, then we need to set the private variable to checked
     */
    checkAllGroups: function(groupName) {
        var store = this.view.getStore();
        var groupField = this.getGroupField();
        if (store) {
            var groups = store.getGroups();
            if (groups) {
                groups.each(function(groupRec) {
                    var allChecked = true;
                    var groupKey = groupRec.getGroupKey();
                    var checkGroup = true;
                    if (groupName) {
                        if (groupKey !== groupName) {
                            checkGroup = false;
                        }
                    }
                    if (checkGroup) {
                        groupRec.each(function(rec) {
                            allChecked = rec.get(this.checkDataIndex);
                            groupName = rec.get(groupField);
                            if (allChecked === false) {
                                return false;
                            }
                        }, this);
                        this.updateParentRecord(groupName, allChecked);
                    }
                }, this);
            }
        }
    },

    updateParentRecord: function(groupName, checked) {
        var parentRecord = this.getParentRecord(groupName);
        if (parentRecord) {
            parentRecord.set(this.checkDataIndex, checked);
            this.refreshView();
        }
    },

    getParentRecord: function(groupName) {
        var parentRecord;
        var metaGroup;
        // For Ext JS 6 and 5.1.1
        if (this.getMetaGroup) {
            metaGroup = this.getMetaGroup(groupName);
        }
        // For Ext JS 5.1-
        else {
            metaGroup = this.groupCache[groupName];
        }
        if (metaGroup) {
            parentRecord = metaGroup.placeholder;
        }
        return parentRecord;
    },

    /**
     * TODO: This might break... we're using a private variable here... but this
     * is the only way we can refresh the view without breaking any sort of
     * scrolling... I'm not sure how to only refresh the group header itself, so
     * I'm keeping the groupName as a param passing in... might be able to figure
     * this out later
     * @param {String} groupName
     */
    refreshView: function(groupName) {
        var view = this.view;
        if (view) {
            view.refreshView();
        }
    },

    onStoreUpdate: function(store, record, operation, modifiedFieldNames, details, eOpts) {
        var grid = this.grid;
        if (!this.updatingRecords && grid && record) {
            var groupName = record.get(this.getGroupField());
            this.checkAllGroups(groupName);
            grid.setSelection(record);
            this.refreshView(groupName);
        }
    },

    onGroupClick: function(grid, node, group, event, eOpts) {
        if (event && grid) {
            var target = event.getTarget('.' + this.targetCls);
            var store = grid.getStore();
            var groupRecord = this.getRecordGroup(event.record);
            if (target && store && groupRecord) {
                var checked = target.checked;
                this.updatingRecords = true;
                groupRecord.each(function(rec, index) {
                    rec.beginEdit();
                    rec.set(this.checkDataIndex, checked);
                    rec.endEdit(true);
                }, this);
                this.updatingRecords = false;
                this.updateParentRecord(group, checked);
            } else {
                this.callParent(arguments);
            }
        }
    }
});
