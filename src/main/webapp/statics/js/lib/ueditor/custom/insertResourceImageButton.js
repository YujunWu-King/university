UE.registerUI('insertResourceImage',function(editor,uiName){
    if(!editor.getOpt('customFlg')) {
        return;
    }

    //注册按钮执行时的command命令，使用命令默认就会带有回退操作
    editor.registerCommand(uiName,{
        execCommand:function(){
            //alert('execCommand:' + uiName)
            editor.fireEvent('insertResourceImageUE');
        }
    });
    
    //创建一个button
    var btn = new UE.ui.Button({
        //按钮的名字
        name:uiName,
        //提示
        title:'插入素材库图片',
        //需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
        //cssRules :'background: url("/university/statics/js/lib/ueditor/themes/default/images/icons.png") 0 -648px no-repeat;',
        cssRules : 'background-position: 619px 0;',
        //点击时执行的命令
        onclick:function () {

            var rootPanel = editor.rootPanel;
            var ueditorfield = rootPanel.down('ueditor[model=newsMaterial]');

            if(typeof ueditorfield.insertMaterialImg === "function"){
            	ueditorfield.insertMaterialImg(editor);
            }
        }
    });

    //当点到编辑内容上时，按钮要做的状态反射
    editor.addListener('selectionchange', function () {
        var state = editor.queryCommandState(uiName);
        if (state == -1) {
            btn.setDisabled(true);
            btn.setChecked(false);
        } else {
            btn.setDisabled(false);
            btn.setChecked(state);
        }
    });

    //因为你是添加button,所以需要返回这个button
    return btn;
}, 0/*index 指定添加到工具栏上的那个位置，默认时追加到最后,editorId 指定这个UI是那个编辑器实例上的，默认是页面上所有的编辑器都会添加这个按钮*/);