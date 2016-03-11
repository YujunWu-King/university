//@author mrt
//@url http://www.mhzg.net
//Extjs集成百度编辑器
Ext.define('Ext.ux.Ueditor',{
	extend:Ext.form.FieldContainer,
	mixins: {
        field: Ext.form.field.Field
	},
	alias: 'widget.ueditor',
	alternateClassName: 'Ext.form.UEditor',
	contentInfo: '',
	ueditorInstance: null,
    initialized: false,
    initComponent: function () {
        var me = this;
        //me.addEvents('initialize', 'change');
        var id = me.id + '-ueditor';
        me.html = '<script id="' + id + '" type="text/plain" name="' + me.name + '"></script>';
        me.callParent(arguments);
        me.initField();
        me.on('render', function () {
            //var width = me.width - 105;
            var width;
            if(me.width){
            	 width = me.width - 105;
            }else{
               width = '100%';
            }
            var height;
            if(!me.height){
            	 me.height = 300;
            }
            height = me.height - 109;
            
            var toolbars = [
                    ['source', '|', 'undo', 'redo', '|',
                        'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat',
        				'formatmatch',  'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist',
        				'insertunorderedlist', 'selectall', 'cleardoc', '|', 'fontfamily', 'fontsize', '|','searchreplace','spechars',
        				 'horizontal', '|','insertimage', 'attachment','|','indent', '|','justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 
                        'link', 'unlink', 'anchor','|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',                       
                        'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 
        				'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', '|', 'preview'
                         ]
                ];
            if(me.model){
            	var model = me.model;
            	if(model == "simple"){
            			toolbars = [[
                    'source', '|',
                        'bold', 'italic', 'underline', '|', 'forecolor', 'backcolor', 'insertorderedlist',
        				'insertunorderedlist',  '|', 'fontfamily', 'fontsize', '|', 'horizontal', 
        				'|','indent', '|','justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'link', 'unlink',  '|' , 'preview'   
                ]];
            	}
            	
            	if(model == "normal"){
            		 toolbars = [[
                    'source', '|',
                        'bold', 'italic', 'underline', '|', 'forecolor', 'backcolor', 'insertorderedlist',
        				'insertunorderedlist',  '|', 'fontfamily', 'fontsize', '|', 'horizontal', '|','insertimage', 'attachment',
        				'|','indent', '|','justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'link', 'unlink',  '|' , 'preview'   
                ]];
            	}
            }
            
            var config;
            var zIndex = 99900;
            if(me.zIndex){
            	zIndex = me.zIndex;
            }

            config = {initialFrameWidth: width, initialFrameHeight: height, zIndex: zIndex, toolbars: toolbars};
            
            me.ueditorInstance = UE.getEditor(id, config);
            me.ueditorInstance.ready(function () {
                me.initialized = true;
                me.fireEvent('initialize', me);

                //me.ueditorInstance.setContent(me.contentInfo);
                me.value = me.contentInfo;
                me.originalValue = me.contentInfo;
                
                if(me.value){
                	me.ueditorInstance.setContent(me.value);
                }
                me.ueditorInstance.addListener('contentChange', function () {
                    me.fireEvent('change', me);
                });

                //var panelXtype = me.panelXtype;
                //if(panelXtype){
                //	var panel = me.findParentByType(panelXtype);
                //	panel.commitChanges(panel);
                //}
            });
        });
    },
    getValue: function () {
        var me = this,
            value = '';
        if (me.initialized) {
            value = me.ueditorInstance.getContent();
        }
        me.value = value;
        return value;
    },
    setValue: function (value) {
        var me = this;
      
        if (value === null || value === undefined) {
            value = '';
        }
        me.contentInfo = value;
        me.value = value;
        if (me.initialized) {
            me.ueditorInstance.setContent(value);
        }
        return me;
    },
    onDestroy: function () {
		if (this.ueditorInstance === null || this.ueditorInstance === undefined) {
			return ;
        }else
		{
			this.ueditorInstance.destroy();
		}
		
        
    }
});