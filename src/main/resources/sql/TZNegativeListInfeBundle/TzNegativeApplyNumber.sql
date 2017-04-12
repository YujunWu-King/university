/**
 * 负面清单
 * 根据批次、班级作为参数，查询所有报名表状态等于提交且初审状态不等于审批拒绝的考生的OPRID，
 */

SELECT B.OPRID
FROM PS_TZ_FORM_WRK_T B,
     PS_TZ_APP_INS_T A
WHERE B.TZ_APP_INS_ID=A.TZ_APP_INS_ID
  AND A.TZ_APP_FORM_STA='U'
  AND B.TZ_FORM_SP_STA<>'B'
  AND B.TZ_BATCH_ID=?
  AND B.TZ_CLASS_ID=?