SELECT A.TZ_JG_ID,A.TZ_JCSL_ID,B.TZ_JAVA_CLASS,A.TZ_JC_MC FROM TZ_JC_SHLI_T A,TZ_JINC_DY_T B
WHERE A.TZ_JG_ID=B.TZ_JG_ID
  AND A.TZ_JC_MC=B.TZ_JC_MC
  AND (   (    A.TZ_JHZX_DTTM<=NOW()
           AND (A.TZ_JCFWQ_MC=? OR A.TZ_JCFWQ_MC IS NULL OR A.TZ_JCFWQ_MC='')
          )
       OR (    A.TZ_JHZX_DTTM<=SUBTIME(NOW(),'00:30:00')
           AND A.TZ_JCFWQ_MC<>?
          )
      )
  AND A.TZ_JOB_YXZT='QUENED'
  AND (B.TZ_YXPT_LX=? OR B.TZ_YXPT_LX IS NULL OR B.TZ_YXPT_LX='')
  AND A.TZ_JG_ID=?
ORDER BY A.TZ_JCFWQ_MC ASC