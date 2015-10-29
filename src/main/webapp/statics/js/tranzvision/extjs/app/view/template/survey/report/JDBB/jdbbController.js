/**
 * Created by carmen on 2015/9/7.
 */
Ext.define('KitchenSink.view.template.survey.report.JDBB.jdbbController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.jdbbController',

    onJdbbInfoClose:function(){
    this.getView().close();
}
})



