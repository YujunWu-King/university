

/**
 * 手机版选择批次确定按钮
 */

function selBatchSure() {

	var objs = $('[name=batchselradio]');
	var batchId = "";
	var classId = "";

	classId = $('input[name="batchselradio"]:checked').val();
	// alert(batchId+":"+classId);

	if (classId == null || classId == "") {
		alert("请选择要报名的班级");
	} else {
		checkHisApply(classId, "ZHS")

	}

}


/**
 * 手机版选择批次取消按钮
 */

function goBack() {

	window.history.back()

}

function checkHisApply(classId, languageCd) {
	var tzUrl=TzUniversityContextPath+"/dispatcher";
	var confirmValue = false;
	var tzParams = '{"ComID":"TZ_MCHOSE_AN_BATCH","PageID":"TZ_MCHOSE_BATH_STD","OperateType":"QF","comParams":{"classId":"' + classId + '","siteid":"' + siteId + '"}}';

	$.ajax({
		type: "POST",
		url:tzUrl ,
		data: {
			tzParams: tzParams
		},
		dataType: 'json',
		success: function(response) {
			var HaveHisApplyForm = response.comContent.HaveHisApplyForm;
			var HaveHCBJ = response.comContent.HaveHCBJ;
			if (HaveHCBJ == "true") {
				if (languageCd == "ENG") {
					alert("Our system has detected existing registration information from an application you previously started, and you are not allowed to  the application for other programs related.");
				} else {
					alert("系统检测到您已经申请了一个项目，不允许再申请相关的其他项目。");
				}
			} else {
				if (HaveHisApplyForm == "true") {
					if (languageCd == "ENG") {
						confirmValue = confirm("Our system has detected existing registration information from an application you previously started. Would you like to copy your previously entered application information into the new application form?");
					} else {
						confirmValue = confirm("系统检测到您曾经报过名，是否从过往报名表中带入历史数据？");
					}
				}

				if (confirmValue == true) {
					location.href = tzUrl + '?classid=appId&APPCOPY=Y&TZ_CLASS_ID=' + classId + '&SITE_ID=' + siteId;
				} else {
					location.href = tzUrl + '?classid=appId&TZ_CLASS_ID=' + classId + '&SITE_ID=' + siteId;
				}
			}
		}
	});
}