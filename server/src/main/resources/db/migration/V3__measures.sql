insert into measure (measure_name, measure_json, last_updated)
values ('sample measure 1', '
    {
      "description": "Patients that are two years of age on the first day of the reporting year with an occurrence of a broken leg at some time during the reporting year",
      "minimumSystemVersion": "1.0.0",
      "traceRules": true,
      "steps": [
        {
          "stepId": 100,
          "ruleName": "AgesWithinDateRange",
          "parameters": [
            {
              "paramName": "FROM_AGE",
              "paramValue": "2",
              "paramType": "INTEGER"
            },
            {
              "paramName": "TO_AGE",
              "paramValue": "2",
              "paramType": "INTEGER"
            },
            {
              "paramName": "START_DATE",
              "paramValue": "$ryBegin",
              "paramType": "DATE"
            },
            {
              "paramName": "END_DATE",
              "paramValue": "$ryBegin",
              "paramType": "DATE"
            }
          ],
          "successStepId": 200,
          "failureStepId": 99999
        },
        {
          "stepId": 200,
          "ruleName": "SetResultCode",
          "parameters": [
            {
            "paramName": "RESULT_CODE",
            "paramValue": "DENOMINATOR",
            "paramType": "TEXT"
            }
          ],
          "successStepId": 99999,
          "failureStepId": 99999
        }
      ]
    }
', current_timestamp)
, ('sample measure 2','
    {
      "description": "Patients that are between the ages of 25 and 65 on the 12th day of the reporting year with an occurrence of a broken arm at some time during the reporting year",
      "minimumSystemVersion": "1.0.0",
      "traceRules": true,
      "steps": [
        {
          "stepId": 100,
          "ruleName": "AgesWithinDateRange",
          "parameters": [
            {
              "paramName": "FROM_AGE",
              "paramValue": "25",
              "paramType": "INTEGER"
            },
            {
              "paramName": "TO_AGE",
              "paramValue": "65",
              "paramType": "INTEGER"
            },
            {
              "paramName": "START_DATE",
              "paramValue": "$ryBegin",
              "paramType": "DATE"
            },
            {
              "paramName": "END_DATE",
              "paramValue": "$ryBegin",
              "paramType": "DATE"
            }
          ],
          "successStepId": 200,
          "failureStepId": 99999
        },
        {
          "stepId": 200,
          "ruleName": "SetResultCode",
          "parameters": [
            {
              "paramName": "RESULT_CODE",
              "paramValue": "DENOMINATOR",
              "paramType": "TEXT"
            }
          ],
          "successStepId": 99999,
          "failureStepId": 99999
        }
      ]
    }', current_timestamp);
