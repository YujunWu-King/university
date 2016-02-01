/*====================================================================
 + 功能描述：个人照片上传控件，可控制上传照片的格式及大小			++
 + 开发人：孔卫丽							++
 + 开发日期：2015-05-08							++
 =====================================================================*/
SurveyBuild.extend("bmrPhoto", "baseComponent", {
    itemName: "个人照片",
    title: "个人照片",
    "StorageType": "S",
    upFileType: "jpg",    //允许上传格式
    upFileSize: "2",    //允许上传大小
    isClip: "",    //是否允许剪裁
    _getHtml: function(data, previewmode) {
        var c = "";

        if (previewmode) {
            if(SurveyBuild.appInsId == "0"){
                var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"KSPHOTO"}}';
                $.ajax({
                    type: "get",
                    dataType: "JSON",
                    data: {
                        tzParams: params
                    },
                    async: false,
                    url: SurveyBuild.tzGeneralURL,
                    success: function(f) {
                        if (f.state.errcode == "0") {
                            var val = f.comContent.photo;
                            if (val && val.length > 1) {
                                data.value = val;
                                data["sysFileName"] = f.comContent.sysFileName;
                                data["filename"] = f.comContent.filename;
                                data["imaPath"] = f.comContent.imaPath;
                                data["path"] = f.comContent.path;
                            }
                        }
                    }
                });
            }

            c += '<div class="main_inner_content_info_autoheight_top">';
            c += '	<div class="main_inner_content_info_right_top">';
            c += '		<div class="main_inner_content_info_right_photo">';
            c += '        <input id="' + data.itemId + '" type="hidden" name="' + data.itemId + '" value = "' + data.value + '" data-id="' + data.instanceId + '">';
            c += '			<a id="photo' + data.itemId + '">';
            c += '				<img width="125" height="158" src="' + (data.value.length < 1 ? "/tranzvision/images/bjphoto.jpg": data.value) + '" id="photo">';
            c += '			</a>';
            c += '		</div>';
            c += '		<div style="background-image:none;" class="main_inner_content_info_right_text_2"><span class="reg_title_star">*</span>' + data.title;
            c += '			<div style="margin-left:200px;margin-top:-35px">';
            c += '				<div id="' + data.itemId + 'Tip" style="margin: 0px; padding: 0px; background: transparent none repeat scroll 0% 0%;" class="onShow">';
            c += '					<div class="onShow"></div>';
            c += '				</div>';
            c += '			</div>';
            c += '		</div>';
            c += '		<input type="hidden" value="" name="mbaSqphoto" id="mbaSqphoto">';
            c += '		<div class="main_inner_content_info_right_text_2">' + data.suffix + '</div>';
            c += '	</div>';
            c += '</div>';
        } else {
            c += '<div class="question-answer">';
            c += '	<div class="format">';
            c += '		<img src="/tranzvision/images/bjphoto.jpg" width="120" height="165" />';
            c += '		<span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span>';
            c += '	</div>';
            c += '</div>'
        }

        return c;
    },
    _edit: function(data) {
        var e = "";
        //后缀
        e += '<div class="edit_item_warp">';
        e += '  <span class="edit_item_label">后缀：<a data-animation="fade" data-reveal-id="myModal" class="big-link" onclick="SurveyBuild.showMsg(this,event)" data-for-id="help_suffix" href="#">(?)</a></span>';
        e += '  <input type="text" value="' + data.suffix + '" onkeyup="SurveyBuild.saveAttr(this,\'suffix\')" class="medium">';
        e += '</div>';

        //参数设置
        e += '<div class="edit_paramSet mt10">';
        e += '	<span class="title"><i class="icon-info-sign"></i> 参数设置</span>';
        e += '	<div class="groupbox">';
        e += '		<div class="edit_item_warp">';
        e += '          <span class="edit_item_label">文件格式：</span>';
        e += '         <input type="text" class="medium edit_attaType" onkeyup="SurveyBuild.saveAttr(this,\'upFileType\')" data_id="' + data.instanceId + '" value="' + data.upFileType + '"/>';
        e += '		</div>';
        e += '		<span style="margin-left:103px; font-size:12px;color:#FF0000">文件格式以“,”分隔</span>';

        //文件大小
        e += '		<div class="edit_item_warp">';
        e += '			 <span class="edit_item_label">文件大小：</span>';
        e += '         <input type="text" class="medium edit_attSize" style="width:175px;" onkeyup="SurveyBuild.saveAttr(this,\'upFileSize\')" data_id="' + data.instanceId + '" value="' + data.upFileSize + '"/>';
        e += '			 <span>M</span>';
        e += '		</div>';
        e += '		<span style="margin-left:103px; font-size:12px;color:#FF0000">输入允许上传的最大文件大小</span>';

        //是否允许剪裁
        e += '		<div class="edit_item_warp" style="margin-top: 5px">';
        e += '			<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isClip\')" ' + (data.isClip == "Y" ? "checked='checked'": "") + ' id="isClip">';
        e += '			<label for="isClip">允许剪裁</label>';
        e += '		</div>';

        //裁剪标准
        e += '		<div class="edit_item_warp mb10">';
        e += '          <span class="edit_item_label">裁剪标准：</span>';
        e += '		    <select id="clip" data-id="' + data.instanceId + '" onchange="SurveyBuild.saveAttr(this,\'clip\')">';
        e += '			    <option value="1" ' + (data.clip == "1" ? "selected='selected'": "") + '>证件照</option>';
        e += '			    <option value="2" ' + (data.clip == "2" ? "selected='selected'": "") + '>1寸标准照</option>';
        e += '			    <option value="3" ' + (data.clip == "3" ? "selected='selected'": "") + '>2寸标准照</option>';
        e += '			    <option value="4" ' + (data.clip == "4" ? "selected='selected'": "") + '>任意</option>';
        e += '		    </select>';
        e += '		</div>';
        e += '	</div>';
        e += '</div>';

        //规则设置
        e += '<div class="edit_jygz">';
        e += '	    <span class="title"><i class="icon-cog"></i> 校验规则</span>';
        e += '      <div class="groupbox">';
        e += '          <div class="edit_item_warp" style="margin-top:5px;">';
        e += '              <input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
        e += '          </div>';
        e += '      </div>';
        //高级设置
        e += '      <div class="edit_item_warp">';
        e += '          <a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '      </div>';
        e += '</div>';

        return e;
    },
    _eventbind: function(data) {
        var $photoBox = $("#photo" + data.itemId);
        var up;
        $photoBox.click(function(e) {
            var photoUrl = SurveyBuild.tzGeneralURL + '?tzParams=';
            var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_UP_PHOTO_STD","OperateType":"HTML","comParams":{"TPLID":"' + templId + '","siteId":"' + $("#siteId").val() + '"}}';
            photoUrl = photoUrl + window.escape(params);
            up = $.layer({
                type: 2,
                title: false,
                fix: false,
                closeBtn: 2,
                shadeClose: false,
                shade: [0.3, '#000', true],
                border: [3, 0.3, '#000', true],
                offset: ['50%', ''],
                area: ['840px', '610px'],
                iframe: {
                    src: photoUrl
                }
            });
        });
    }
});