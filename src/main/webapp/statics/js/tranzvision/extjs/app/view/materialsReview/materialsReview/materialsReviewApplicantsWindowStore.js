Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewApplicantsWindowStore', {
    extend: 'Ext.data.Store',
    alias: 'store.materialsReviewApplicantsWindowStore',
    model: 'KitchenSink.view.materialsReview.materialsReview.materialsReviewApplicantsWindowModel',
    autoLoad:false,
    comID: 'TZ_REVIEW_CL_COM',
    pageID: 'TZ_CLPS_ADDAPP_STD',
    pageSize:0,
    tzStoreParams:'{"cfgSrhId": "TZ_REVIEW_CL_COM.TZ_CLPS_ADDAPP_STD.TZ_CL_ADDAPP_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy(),
    listeners:{
        beforesync:function( options, eOpts){
            console.log("beforesync");
        },
        beforeprefetch:function(  operation, eOpts ){
    console.log("beforeprefetch")
}
    }


});