Ext.define('KitchenSink.AdvancedVType', {
    override: 'Ext.form.field.VTypes',

    daterange: function(val, field) {
        var date = field.parseDate(val);

        if (!date) {
            return false;
        }
        if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
            var start = field.up('form').down('#' + field.startDateField);
            start.setMaxValue(date);
            start.validate();
            this.dateRangeMax = date;
        }
        else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
            var end = field.up('form').down('#' + field.endDateField);
            end.setMinValue(date);
            end.validate();
            this.dateRangeMin = date;
        }
        /*
         * Always return true since we're only using this vtype to set the
         * min/max allowed values (these are tested for after the vtype test)
         */
        return true;
    },

    daterangeText: 'Start date must be less than end date',

    password: function(val, field) {
        if (field.initialPassField) {
            var pwd = field.up('form').down('#' + field.initialPassField);
            return (val == pwd.getValue());
        }
        return true;
    },

    passwordText: 'Passwords do not match',

    /*
     *Added by ShaweYet
     * 2015-07-02
     */
    gridLimitNumberRange: function(val, field){
        if(field.gridLimitNumberRange){
            var maxDataIndex = field.gridLimitNumberRange.max;
            var minDataIndex = field.gridLimitNumberRange.min;
            var grid =field.up('grid');
            var record = grid.getSelectionModel().getSelection()[0];
            var maxValue =parseInt(field.dataIndex==maxDataIndex?val:record.get(maxDataIndex));
            var minValue =parseInt(field.dataIndex==minDataIndex?val:record.get(minDataIndex));
        }
        if(minValue <= maxValue){
            return true;
        }else{
            return false;
        }
    },
    gridLimitNumberRangeText: 'Min value can not greater than max value!',

    /*
     *Added by ShaweYet
     * 2015-07-15
     */
    toUppercase: function(val, field){
        if(val){
            field.setValue(val.toUpperCase());
        }
        return true;
    },
    toLowercase: function(val, field){
        if(val){
            field.setValue(val.toLowerCase());
        }
        return true;
    }
});
