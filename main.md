System Requirements/Pre-requites
================================

1.  Java version 17

2.  Maven 4.0

3.  Python 3.8

Components configuration and installation
=========================================

Data Wrangling
--------------

It uses Azure Blob Storage for managing the CSV files uploaded and
downloaded. To configure, add the following details of the Azure Blob
Storage to the application.properties file:

1.  azure.storage.blob.account-name

2.  azure.storage.blob.account-key

3.  azure.storage.blob.endpoint=http://xxxx.blob.core.windows.net

4.  server.port=XXXX (Port on which the application will run)

FHIR Server Configuration
-------------------------

Please follow the following steps to configure 'Azure API for FHIR' as
the FHIR server:

1.  Create a subscription on the Azure portal

2.  Create a resource, 'Azure API for FHIR', which is a part of the
    subscription in a resource group. A resource group is a container
    that holds and manages all those resources that need to be managed
    in a group. Link:[Azure-Portal](https://portal.azure.com)

3.  Here is a resource called Azure Active Directory (AAD) which is used
    by Azure for identity and access management services. Each AAD can
    have single or multiple tenants, and each tenant has a Tenant ID
    associated with it. In AAD tenant, register a confidential client
    application. Each client application has an application client ID,
    and credentials. These credentials help the applications to identify
    themselves to authenticate when requesting access tokens. Create an
    application (App Registrations) in Azure Active Directory (AAD), and
    add it as an enterprise application in a tenant. These applications
    are registered in the AAD. Create the client secret ID and value in
    'Certificates & secrets' of the created application, and make a note
    of the Client secret value as it will be used to call FHIR APIs.
    Link: [Azure -
    AAD](https://portal.azure.com/feature.msaljs=false#blade/Microsoft_AAD_ IAM/ActiveDirectoryMenuBlade/RegisteredApps)

4.  Once the application is registered, it is granted access to the FHIR
    Server in the access control. There are various roles when granting
    access to the application such as FHIR Data Contributor, FHIR Data
    Converter, FHIR Data Exporter, FHIR Data Reader and FHIR Data
    Writer. Add the application created to the FHIR server using Role
    Assignment. Link:
    [AZure-FHIR-Server](https://portal.azure.com/?feature.msaljs=false#@HealthDataLabResearch.onmicrosoft.com/resource/subscriptions/0e4cf8ea-f94a-4626-ac99-3e9693168d25/resourcegroups/hdl-syntheticdata-rg/providers/Microsoft.HealthcareApis/services/closedfhirserver/users)

5.  Note: So essentially, there is an FHIR server ('Azure API for FHIR')
    and an application is registered in it. FHIR APIs are accessible
    using the credentials of the application. Applications are
    registered in Azure Active Directory tenant. Each tenant can have
    multiple applications.

**Configuration for the component** It uses FHIR server for uploading
the resources to the FHIR server. To configure, add following details of
the FHIR server to the application.properties file:
hdl.fhir.server.base.url = fhir server base Url
hdl.azure.app.client.reg.id = clientid - Application client registration
ID hdl.azure.app.client.reg.secret.value = clientsecret - Application
client registration secret value server.port=80xx (Port on which the
application will run) After the above steps, we are authorized to access
the FHIR API. To access FHIR API, the following steps:

1.  Since the FHIR service is secured by AAD, get an access token from
    the active directory. The request for getting an access token :

    1.  Request Type: POST

    2.  Request URL:
        https://login.microsoftonline.com/tenantid/oauth2/token (tenant
        ID- is the ID of the tenant registered under Azure Active
        Directory)

    3.  Body: In the 'x-www-form-urlencoded' format

2.  We get the access token in response to the above request. Using the
    Azure AD access token, we can request FHIR resources. For example,
    to get a list of patients:

    1.  Request Type: GET

    2.  Request URL: fhirurl/Patient

    3.  Authorization: Set type as 'Bearer Token' and value as the token
        received from the Azure AD

        ``` {.json language="json" startFrom="1"}
        {"menu": {
                          "grant_type": "Client_Credentials",
                          "client_id": "{{clientid}} (Is the client application ID)",
                          "client_secret" : "{{clientsecret}} (Client secret credentials)",
                          "resource" : "{{base_fhirurl}} (Base URL of the FHIR service)"
                          "popup": {
                        }}
        ```

Gretel Configuration
--------------------

API Documentation
=================

Data Wrangling
--------------

### Convert CSV to FHIR resources

1.  Request URL:
    http://localhost:XXXX/api/v1/data-wrangling/convert/npr/csv-to-fhir

2.  Request Type: POST

3.  Request Body: Will have form-data. With CSV file in \"file\" request
    param.

4.  Response Body: Will be a list of JSON objects (Example of single
    JSON object from the list)

    ``` {.json language="json" startFrom="1"}
    [
                           {
                                "patient": {
                                    "resourceType": "Patient",
                                    "id": null,
                                    "meta": null,
                                    "identifier": [
                                        {
                                            "use": "temp",
                                            "system": null,
                                            "value": "34940"
                                        }
                                    ],
                                    "active": false,
                                    "name": null,
                                    "gender": "female",
                                    "birthDate": "1941",
                                    "deceasedBoolean": false,
                                    "deceasedDateTime": "",
                                    "address": [
                                        {
                                            "use": "",
                                            "city": "Hordaland Fylkeskommune",
                                            "district": "",
                                            "state": "",
                                            "postalCode": "12",
                                            "country": "Norway"
                                        }
                                    ],
                                    "extension": [
                                        {
                                            "valueString": "3"
                                        }
                                    ]
                                },
                                "practitioner": {
                                    "resourceType": "Practitioner",
                                    "id": null,
                                    "meta": null,
                                    "identifier": [
                                        {
                                            "use": "temp",
                                            "system": null,
                                            "value": "44260"
                                        }
                                    ],
                                    "active": false,
                                    "name": null,
                                    "gender": "female",
                                    "birthDate": "1954"
                                },
                                "location": {
                                    "resourceType": "Location",
                                    "id": null,
                                    "meta": null,
                                    "identifier": null,
                                    "status": null,
                                    "name": "Helse Bergen HF Haukeland",
                                    "mode": null,
                                    "address": null
                                },
                                "condition": {
                                    "resourceType": "Condition",
                                    "id": null,
                                    "meta": null,
                                    "identifier": null,
                                    "category": null,
                                    "code": {
                                        "coding": [
                                            {
                                                "code": "D11",
                                                "system": "http://hl7.org/fhir/sid/icd-10",
                                                "display": null
                                            }
                                        ],
                                        "text": "ICD-10 Codes"
                                    },
                                    "subject": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Patient associated with the condition",
                                        "type": "Patient"
                                    },
                                    "encounter": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Encounter associated with Patient",
                                        "type": "Encounter"
                                    }
                                },
                                "encounter": {
                                    "resourceType": "Encounter",
                                    "id": null,
                                    "meta": null,
                                    "identifier": null,
                                    "status": "finished",
                                    "type": null,
                                    "subject": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Patient Hospitalized",
                                        "type": "Patient"
                                    },
                                    "location": [
                                        {
                                            "location": {
                                                "reference": "",
                                                "identifier": {
                                                    "use": null,
                                                    "system": null,
                                                    "value": ""
                                                },
                                                "display": "Institute Name where prescribed",
                                                "type": "Location"
                                            },
                                            "status": null
                                        }
                                    ],
                                    "hospitalization": {
                                        "dischargeDisposition": {
                                            "coding": [
                                                {
                                                    "code": "Other",
                                                    "system": "http://terminology.hl7.org/CodeSystem/discharge-disposition",
                                                    "display": null
                                                }
                                            ],
                                            "text": "Others"
                                        }
                                    },
                                    "period": {
                                        "start": "2012-12-07",
                                        "end": "2012-12-07"
                                    },
                                    "diagnosis": null,
                                    "participant": [
                                        {
                                            "individual": {
                                                "reference": "",
                                                "identifier": {
                                                    "use": null,
                                                    "system": null,
                                                    "value": ""
                                                },
                                                "display": "Practitioner Details for the patient hospitalized",
                                                "type": "Practitioner"
                                            }
                                        }
                                    ],
                                    "class": {
                                        "code": "PRENC",
                                        "system": "http://terminology.hl7.org/CodeSystem/v3-ActCode",
                                        "display": "Patient arrival mode for the Encounter"
                                    }
                                },
                                "medication": {
                                    "resourceType": "Medication",
                                    "id": null,
                                    "meta": null,
                                    "identifier": [
                                        {
                                            "use": null,
                                            "system": null,
                                            "value": "5390"
                                        }
                                    ],
                                    "code": {
                                        "coding": [
                                            {
                                                "code": "R03BB01",
                                                "system": "http://www.whocc.no/atc",
                                                "display": null
                                            }
                                        ],
                                        "text": "Atrovent inh aer 20mcg/dose ff"
                                    }
                                },
                                "medicationRequest": {
                                    "resourceType": "MedicationRequest",
                                    "id": null,
                                    "meta": null,
                                    "identifier": [
                                        {
                                            "use": null,
                                            "system": null,
                                            "value": "50442198"
                                        }
                                    ],
                                    "status": "unknown",
                                    "intent": "option",
                                    "category": [
                                        {
                                            "coding": [
                                                {
                                                    "code": "3",
                                                    "system": null,
                                                    "display": null
                                                }
                                            ],
                                            "text": "Blåreseptordningen §§ 2, 3a, 3b, 4 og 5 (gammel ordning §§ 2, 3, 4, 9, og 10a)"
                                        }
                                    ],
                                    "medicationReference": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Medications for the prescription",
                                        "type": "Medication"
                                    },
                                    "subject": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Patient for the prescription",
                                        "type": "Patient"
                                    },
                                    "encounter": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Encounter associated with the prescription",
                                        "type": "Encounter"
                                    },
                                    "recorder": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Practitioner who prescribed the prescription",
                                        "type": "Practitioner"
                                    },
                                    "note": [
                                        {
                                            "authorString": "Legal Reimbursement category for the prescription",
                                            "text": "\§ 2 Forhåndsgodkjent refusjon (tidligere § 9)"
                                        },
                                        {
                                            "authorString": "Legal Reimbursement code for the prescription",
                                            "text": "7"
                                        },
                                        {
                                            "authorString": "Reimbursement code for the prescription - ICD/ICPC",
                                            "text": "ICPC:R95"
                                        }
                                    ],
                                    "dosageInstruction": [
                                        {
                                            "text": "Defined daily dose of the drug",
                                            "doseAndRate": [
                                                {
                                                    "doseQuantity": {
                                                        "value": 0.12,
                                                        "unit": "mg"
                                                    },
                                                    "rateQuantity": {
                                                        "value": 0.0,
                                                        "unit": "Per Day"
                                                    }
                                                }
                                            ]
                                        }
                                    ]
                                },
                                "medicationDispense": {
                                    "resourceType": "MedicationDispense",
                                    "id": null,
                                    "meta": null,
                                    "status": "unknown",
                                    "medicationReference": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Medication details for the dispense",
                                        "type": "Medication"
                                    },
                                    "subject": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Patient for the prescription",
                                        "type": "Patient"
                                    },
                                    "authorizingPrescription": [
                                        {
                                            "reference": "",
                                            "identifier": {
                                                "use": null,
                                                "system": null,
                                                "value": ""
                                            },
                                            "display": "Prescription for the Medication",
                                            "type": "MedicationRequest"
                                        }
                                    ],
                                    "quantity": {
                                        "value": 3000.0,
                                        "unit": null
                                    },
                                    "daysSupply": {
                                        "value": 100000.0,
                                        "unit": null
                                    },
                                    "whenHandedOver": ""
                                }
                            } 
                        ]
    ```

### Convert FHIR resources to CSV

1.  Request URL:
    httphttp://localhost:XXXX/api/v1/data-wrangling/convert/npr/fhir-to-csv

2.  Request Type: POST

3.  Request Body: Will be a list of JSON objects (Example of single JSON
    object from the list) (in JSON format)

    ``` {.json language="json" startFrom="1"}
    [
                            {
                                "patient": {
                                    "resourceType": "Patient",
                                    "id": null,
                                    "meta": null,
                                    "identifier": [
                                        {
                                            "use": "temp",
                                            "system": null,
                                            "value": "34940"
                                        }
                                    ],
                                    "active": false,
                                    "name": null,
                                    "gender": "female",
                                    "birthDate": "1941",
                                    "deceasedBoolean": false,
                                    "deceasedDateTime": "",
                                    "address": [
                                        {
                                            "use": "",
                                            "city": "Hordaland Fylkeskommune",
                                            "district": "",
                                            "state": "",
                                            "postalCode": "12",
                                            "country": "Norway"
                                        }
                                    ],
                                    "extension": [
                                        {
                                            "valueString": "3"
                                        }
                                    ]
                                },
                                "practitioner": {
                                    "resourceType": "Practitioner",
                                    "id": null,
                                    "meta": null,
                                    "identifier": [
                                        {
                                            "use": "temp",
                                            "system": null,
                                            "value": "44260"
                                        }
                                    ],
                                    "active": false,
                                    "name": null,
                                    "gender": "female",
                                    "birthDate": "1954"
                                },
                                "location": {
                                    "resourceType": "Location",
                                    "id": null,
                                    "meta": null,
                                    "identifier": null,
                                    "status": null,
                                    "name": "Helse Bergen HF Haukeland",
                                    "mode": null,
                                    "address": null
                                },
                                "condition": {
                                    "resourceType": "Condition",
                                    "id": null,
                                    "meta": null,
                                    "identifier": null,
                                    "category": null,
                                    "code": {
                                        "coding": [
                                            {
                                                "code": "D11",
                                                "system": "http://hl7.org/fhir/sid/icd-10",
                                                "display": null
                                            }
                                        ],
                                        "text": "ICD-10 Codes"
                                    },
                                    "subject": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Patient associated with the condition",
                                        "type": "Patient"
                                    },
                                    "encounter": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Encounter associated with Patient",
                                        "type": "Encounter"
                                    }
                                },
                                "encounter": {
                                    "resourceType": "Encounter",
                                    "id": null,
                                    "meta": null,
                                    "identifier": null,
                                    "status": "finished",
                                    "type": null,
                                    "subject": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Patient Hospitalized",
                                        "type": "Patient"
                                    },
                                    "location": [
                                        {
                                            "location": {
                                                "reference": "",
                                                "identifier": {
                                                    "use": null,
                                                    "system": null,
                                                    "value": ""
                                                },
                                                "display": "Institute Name where prescribed",
                                                "type": "Location"
                                            },
                                            "status": null
                                        }
                                    ],
                                    "hospitalization": {
                                        "dischargeDisposition": {
                                            "coding": [
                                                {
                                                    "code": "Other",
                                                    "system": "http://terminology.hl7.org/CodeSystem/discharge-disposition",
                                                    "display": null
                                                }
                                            ],
                                            "text": "Others"
                                        }
                                    },
                                    "period": {
                                        "start": "2012-12-07",
                                        "end": "2012-12-07"
                                    },
                                    "diagnosis": null,
                                    "participant": [
                                        {
                                            "individual": {
                                                "reference": "",
                                                "identifier": {
                                                    "use": null,
                                                    "system": null,
                                                    "value": ""
                                                },
                                                "display": "Practitioner Details for the patient hospitalized",
                                                "type": "Practitioner"
                                            }
                                        }
                                    ],
                                    "class": {
                                        "code": "PRENC",
                                        "system": "http://terminology.hl7.org/CodeSystem/v3-ActCode",
                                        "display": "Patient arrival mode for the Encounter"
                                    }
                                },
                                "medication": {
                                    "resourceType": "Medication",
                                    "id": null,
                                    "meta": null,
                                    "identifier": [
                                        {
                                            "use": null,
                                            "system": null,
                                            "value": "5390"
                                        }
                                    ],
                                    "code": {
                                        "coding": [
                                            {
                                                "code": "R03BB01",
                                                "system": "http://www.whocc.no/atc",
                                                "display": null
                                            }
                                        ],
                                        "text": "Atrovent inh aer 20mcg/dose ff"
                                    }
                                },
                                "medicationRequest": {
                                    "resourceType": "MedicationRequest",
                                    "id": null,
                                    "meta": null,
                                    "identifier": [
                                        {
                                            "use": null,
                                            "system": null,
                                            "value": "50442198"
                                        }
                                    ],
                                    "status": "unknown",
                                    "intent": "option",
                                    "category": [
                                        {
                                            "coding": [
                                                {
                                                    "code": "3",
                                                    "system": null,
                                                    "display": null
                                                }
                                            ],
                                            "text": "Blåreseptordningen §§ 2, 3a, 3b, 4 og 5 (gammel ordning §§ 2, 3, 4, 9, og 10a)"
                                        }
                                    ],
                                    "medicationReference": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Medications for the prescription",
                                        "type": "Medication"
                                    },
                                    "subject": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Patient for the prescription",
                                        "type": "Patient"
                                    },
                                    "encounter": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Encounter associated with the prescription",
                                        "type": "Encounter"
                                    },
                                    "recorder": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Practitioner who prescribed the prescription",
                                        "type": "Practitioner"
                                    },
                                    "note": [
                                        {
                                            "authorString": "Legal Reimbursement category for the prescription",
                                            "text": "§ 2 Forhåndsgodkjent refusjon (tidligere § 9)"
                                        },
                                        {
                                            "authorString": "Legal Reimbursement code for the prescription",
                                            "text": "7"
                                        },
                                        {
                                            "authorString": "Reimbursement code for the prescription - ICD/ICPC",
                                            "text": "ICPC:R95"
                                        }
                                    ],
                                    "dosageInstruction": [
                                        {
                                            "text": "Defined daily dose of the drug",
                                            "doseAndRate": [
                                                {
                                                    "doseQuantity": {
                                                        "value": 0.12,
                                                        "unit": "mg"
                                                    },
                                                    "rateQuantity": {
                                                        "value": 0.0,
                                                        "unit": "Per Day"
                                                    }
                                                }
                                            ]
                                        }
                                    ]
                                },
                                "medicationDispense": {
                                    "resourceType": "MedicationDispense",
                                    "id": null,
                                    "meta": null,
                                    "status": "unknown",
                                    "medicationReference": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Medication details for the dispense",
                                        "type": "Medication"
                                    },
                                    "subject": {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Patient for the prescription",
                                        "type": "Patient"
                                    },
                                    "authorizingPrescription": [
                                        {
                                            "reference": "",
                                            "identifier": {
                                                "use": null,
                                                "system": null,
                                                "value": ""
                                            },
                                            "display": "Prescription for the Medication",
                                            "type": "MedicationRequest"
                                        }
                                    ],
                                    "quantity": {
                                        "value": 3000.0,
                                        "unit": null
                                    },
                                    "daysSupply": {
                                        "value": 100000.0,
                                        "unit": null
                                    },
                                    "whenHandedOver": ""
                                }
                            }
                        ]
    ```

4.  Response Body: CSV file

FHIR Adapter
------------

### Upload FHIR resources to FHIR server

1.  Request URL:
    http://localhost:XXXX/api/v1/fhir-server/upload?fhirServerUrl=https://hdlsyntheticdata.azurehealthcareapis.com

2.  Request Type: POST

3.  Request Body: Will be a list of JSON objects (Example of single JSON
    object from the list)

    ``` {.json language="json" startFrom="1"}
    [
                        {
                            "patient": {
                                "resourceType": "Patient",
                                "id": null,
                                "meta": null,
                                "identifier": [
                                    {
                                        "use": "temp",
                                        "system": null,
                                        "value": "34940"
                                    }
                                ],
                                "active": false,
                                "name": null,
                                "gender": "female",
                                "birthDate": "1941",
                                "deceasedBoolean": false,
                                "deceasedDateTime": "",
                                "address": [
                                    {
                                        "use": "",
                                        "city": "Hordaland Fylkeskommune",
                                        "district": "",
                                        "state": "",
                                        "postalCode": "12",
                                        "country": "Norway"
                                    }
                                ],
                                "extension": [
                                    {
                                        "valueString": "3"
                                    }
                                ]
                            },
                            "practitioner": {
                                "resourceType": "Practitioner",
                                "id": null,
                                "meta": null,
                                "identifier": [
                                    {
                                        "use": "temp",
                                        "system": null,
                                        "value": "44260"
                                    }
                                ],
                                "active": false,
                                "name": null,
                                "gender": "female",
                                "birthDate": "1954"
                            },
                            "location": {
                                "resourceType": "Location",
                                "id": null,
                                "meta": null,
                                "identifier": null,
                                "status": null,
                                "name": "Helse Bergen HF Haukeland",
                                "mode": null,
                                "address": null
                            },
                            "condition": {
                                "resourceType": "Condition",
                                "id": null,
                                "meta": null,
                                "identifier": null,
                                "category": null,
                                "code": {
                                    "coding": [
                                        {
                                            "code": "D11",
                                            "system": "http://hl7.org/fhir/sid/icd-10",
                                            "display": null
                                        }
                                    ],
                                    "text": "ICD-10 Codes"
                                },
                                "subject": {
                                    "reference": "",
                                    "identifier": {
                                        "use": null,
                                        "system": null,
                                        "value": ""
                                    },
                                    "display": "Patient associated with the condition",
                                    "type": "Patient"
                                },
                                "encounter": {
                                    "reference": "",
                                    "identifier": {
                                        "use": null,
                                        "system": null,
                                        "value": ""
                                    },
                                    "display": "Encounter associated with Patient",
                                    "type": "Encounter"
                                }
                            },
                            "encounter": {
                                "resourceType": "Encounter",
                                "id": null,
                                "meta": null,
                                "identifier": null,
                                "status": "finished",
                                "type": null,
                                "subject": {
                                    "reference": "",
                                    "identifier": {
                                        "use": null,
                                        "system": null,
                                        "value": ""
                                    },
                                    "display": "Patient Hospitalized",
                                    "type": "Patient"
                                },
                                "location": [
                                    {
                                        "location": {
                                            "reference": "",
                                            "identifier": {
                                                "use": null,
                                                "system": null,
                                                "value": ""
                                            },
                                            "display": "Institute Name where prescribed",
                                            "type": "Location"
                                        },
                                        "status": null
                                    }
                                ],
                                "hospitalization": {
                                    "dischargeDisposition": {
                                        "coding": [
                                            {
                                                "code": "Other",
                                                "system": "http://terminology.hl7.org/CodeSystem/discharge-disposition",
                                                "display": null
                                            }
                                        ],
                                        "text": "Others"
                                    }
                                },
                                "period": {
                                    "start": "2012-12-07",
                                    "end": "2012-12-07"
                                },
                                "diagnosis": null,
                                "participant": [
                                    {
                                        "individual": {
                                            "reference": "",
                                            "identifier": {
                                                "use": null,
                                                "system": null,
                                                "value": ""
                                            },
                                            "display": "Practitioner Details for the patient hospitalized",
                                            "type": "Practitioner"
                                        }
                                    }
                                ],
                                "class": {
                                    "code": "PRENC",
                                    "system": "http://terminology.hl7.org/CodeSystem/v3-ActCode",
                                    "display": "Patient arrival mode for the Encounter"
                                }
                            },
                            "medication": {
                                "resourceType": "Medication",
                                "id": null,
                                "meta": null,
                                "identifier": [
                                    {
                                        "use": null,
                                        "system": null,
                                        "value": "5390"
                                    }
                                ],
                                "code": {
                                    "coding": [
                                        {
                                            "code": "R03BB01",
                                            "system": "http://www.whocc.no/atc",
                                            "display": null
                                        }
                                    ],
                                    "text": "Atrovent inh aer 20mcg/dose ff"
                                }
                            },
                            "medicationRequest": {
                                "resourceType": "MedicationRequest",
                                "id": null,
                                "meta": null,
                                "identifier": [
                                    {
                                        "use": null,
                                        "system": null,
                                        "value": "50442198"
                                    }
                                ],
                                "status": "unknown",
                                "intent": "option",
                                "category": [
                                    {
                                        "coding": [
                                            {
                                                "code": "3",
                                                "system": null,
                                                "display": null
                                            }
                                        ],
                                        "text": "Blåreseptordningen §§ 2, 3a, 3b, 4 og 5 (gammel ordning §§ 2, 3, 4, 9, og 10a)"
                                    }
                                ],
                                "medicationReference": {
                                    "reference": "",
                                    "identifier": {
                                        "use": null,
                                        "system": null,
                                        "value": ""
                                    },
                                    "display": "Medications for the prescription",
                                    "type": "Medication"
                                },
                                "subject": {
                                    "reference": "",
                                    "identifier": {
                                        "use": null,
                                        "system": null,
                                        "value": ""
                                    },
                                    "display": "Patient for the prescription",
                                    "type": "Patient"
                                },
                                "encounter": {
                                    "reference": "",
                                    "identifier": {
                                        "use": null,
                                        "system": null,
                                        "value": ""
                                    },
                                    "display": "Encounter associated with the prescription",
                                    "type": "Encounter"
                                },
                                "recorder": {
                                    "reference": "",
                                    "identifier": {
                                        "use": null,
                                        "system": null,
                                        "value": ""
                                    },
                                    "display": "Practitioner who prescribed the prescription",
                                    "type": "Practitioner"
                                },
                                "note": [
                                    {
                                        "authorString": "Legal Reimbursement category for the prescription",
                                        "text": "§ 2 Forhåndsgodkjent refusjon (tidligere § 9)"
                                    },
                                    {
                                        "authorString": "Legal Reimbursement code for the prescription",
                                        "text": "7"
                                    },
                                    {
                                        "authorString": "Reimbursement code for the prescription - ICD/ICPC",
                                        "text": "ICPC:R95"
                                    }
                                ],
                                "dosageInstruction": [
                                    {
                                        "text": "Defined daily dose of the drug",
                                        "doseAndRate": [
                                            {
                                                "doseQuantity": {
                                                    "value": 0.12,
                                                    "unit": "mg"
                                                },
                                                "rateQuantity": {
                                                    "value": 0.0,
                                                    "unit": "Per Day"
                                                }
                                            }
                                        ]
                                    }
                                ]
                            },
                            "medicationDispense": {
                                "resourceType": "MedicationDispense",
                                "id": null,
                                "meta": null,
                                "status": "unknown",
                                "medicationReference": {
                                    "reference": "",
                                    "identifier": {
                                        "use": null,
                                        "system": null,
                                        "value": ""
                                    },
                                    "display": "Medication details for the dispense",
                                    "type": "Medication"
                                },
                                "subject": {
                                    "reference": "",
                                    "identifier": {
                                        "use": null,
                                        "system": null,
                                        "value": ""
                                    },
                                    "display": "Patient for the prescription",
                                    "type": "Patient"
                                },
                                "authorizingPrescription": [
                                    {
                                        "reference": "",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": ""
                                        },
                                        "display": "Prescription for the Medication",
                                        "type": "MedicationRequest"
                                    }
                                ],
                                "quantity": {
                                    "value": 3000.0,
                                    "unit": null
                                },
                                "daysSupply": {
                                    "value": 100000.0,
                                    "unit": null
                                },
                                "whenHandedOver": ""
                            }
                        }
                    ]
    ```

### Download FHIR resources from FHIR server

1.  Request URL:
    http://localhost:XXXX/api/v1/fhir-server/download?fhirServerUrl=https://hdlsyntheticdata.azurehealthcareapis.com

2.  Request Type: GET

3.  Response Body: Will be a list of JSON objects (Example of single
    JSON object from the list)

    ``` {.json language="json" startFrom="1"}
    [
                           {
                                "patient": {
                                    "resourceType": "Patient",
                                    "id": "9dbcfce2-2c3a-476a-9b39-eead46d3c725",
                                    "meta": {
                                        "profile": null,
                                        "versionId": "1",
                                        "lastUpdated": "2023-06-05T22:03:13.987+00:00"
                                    },
                                    "identifier": [
                                        {
                                            "use": "temp",
                                            "system": null,
                                            "value": "34940"
                                        }
                                    ],
                                    "active": false,
                                    "name": null,
                                    "gender": "female",
                                    "birthDate": "1941",
                                    "deceasedBoolean": false,
                                    "deceasedDateTime": null,
                                    "address": [
                                        {
                                            "use": null,
                                            "city": "Hordaland Fylkeskommune",
                                            "district": null,
                                            "state": null,
                                            "postalCode": "12",
                                            "country": "Norway"
                                        }
                                    ],
                                    "extension": [
                                        {
                                            "valueString": "3"
                                        }
                                    ]
                                },
                                "practitioner": {
                                    "resourceType": "Practitioner",
                                    "id": "6f8f2364-e104-47e6-a98d-3614e9d5337d",
                                    "meta": {
                                        "profile": null,
                                        "versionId": "1",
                                        "lastUpdated": "2023-06-05T22:03:14.824+00:00"
                                    },
                                    "identifier": [
                                        {
                                            "use": "temp",
                                            "system": null,
                                            "value": "44260"
                                        }
                                    ],
                                    "active": false,
                                    "name": null,
                                    "gender": "female",
                                    "birthDate": "1954"
                                },
                                "location": {
                                    "resourceType": "Location",
                                    "id": "e1318990-70da-46ba-bc24-508b4c8b5332",
                                    "meta": {
                                        "profile": null,
                                        "versionId": "1",
                                        "lastUpdated": "2023-06-05T22:03:15.288+00:00"
                                    },
                                    "identifier": null,
                                    "status": null,
                                    "name": "Helse Bergen HF Haukeland",
                                    "mode": null,
                                    "address": null
                                },
                                "condition": {
                                    "resourceType": "Condition",
                                    "id": "dd1641fb-2c01-4a92-85f8-61bfe6ef6bbe",
                                    "meta": {
                                        "profile": null,
                                        "versionId": "1",
                                        "lastUpdated": "2023-06-05T22:03:15.763+00:00"
                                    },
                                    "identifier": null,
                                    "category": null,
                                    "code": {
                                        "coding": [
                                            {
                                                "code": "D11",
                                                "system": "http://hl7.org/fhir/sid/icd-10",
                                                "display": null
                                            }
                                        ],
                                        "text": "ICD-10 Codes"
                                    },
                                    "subject": {
                                        "reference": "Patient/9dbcfce2-2c3a-476a-9b39-eead46d3c725",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": "9dbcfce2-2c3a-476a-9b39-eead46d3c725"
                                        },
                                        "display": "Patient associated with the condition",
                                        "type": "Patient"
                                    },
                                    "encounter": {
                                        "reference": "Encounter/0e5b5fdd-b3a1-40cb-b8eb-e17c6c97c41d",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": "0e5b5fdd-b3a1-40cb-b8eb-e17c6c97c41d"
                                        },
                                        "display": "Encounter associated with Patient",
                                        "type": "Encounter"
                                    }
                                },
                                "encounter": {
                                    "resourceType": "Encounter",
                                    "id": "0e5b5fdd-b3a1-40cb-b8eb-e17c6c97c41d",
                                    "meta": {
                                        "profile": null,
                                        "versionId": "1",
                                        "lastUpdated": "2023-06-05T22:03:15.506+00:00"
                                    },
                                    "extension": null,
                                    "identifier": null,
                                    "status": "finished",
                                    "type": null,
                                    "subject": {
                                        "reference": "Patient/9dbcfce2-2c3a-476a-9b39-eead46d3c725",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": "9dbcfce2-2c3a-476a-9b39-eead46d3c725"
                                        },
                                        "display": "Patient Hospitalized",
                                        "type": "Patient"
                                    },
                                    "location": [
                                        {
                                            "location": {
                                                "reference": "Location/e1318990-70da-46ba-bc24-508b4c8b5332",
                                                "identifier": {
                                                    "use": null,
                                                    "system": null,
                                                    "value": "e1318990-70da-46ba-bc24-508b4c8b5332"
                                                },
                                                "display": "Institute Name where prescribed",
                                                "type": "Location"
                                            },
                                            "status": null
                                        }
                                    ],
                                    "hospitalization": {
                                        "dischargeDisposition": {
                                            "coding": [
                                                {
                                                    "code": "Other",
                                                    "system": "http://terminology.hl7.org/CodeSystem/discharge-disposition",
                                                    "display": null
                                                }
                                            ],
                                            "text": "Others"
                                        }
                                    },
                                    "period": {
                                        "start": "2012-12-07",
                                        "end": "2012-12-07"
                                    },
                                    "participant": [
                                        {
                                            "individual": {
                                                "reference": "Practitioner/6f8f2364-e104-47e6-a98d-3614e9d5337d",
                                                "identifier": {
                                                    "use": null,
                                                    "system": null,
                                                    "value": "6f8f2364-e104-47e6-a98d-3614e9d5337d"
                                                },
                                                "display": "Practitioner Details for the patient hospitalized",
                                                "type": "Practitioner"
                                            }
                                        }
                                    ],
                                    "class": {
                                        "code": "PRENC",
                                        "system": "http://terminology.hl7.org/CodeSystem/v3-ActCode",
                                        "display": "Patient arrival mode for the Encounter"
                                    }
                                },
                                "medication": {
                                    "resourceType": "Medication",
                                    "id": "1de886f3-2471-4825-9e2a-1931580fc2d0",
                                    "meta": {
                                        "profile": null,
                                        "versionId": "1",
                                        "lastUpdated": "2023-06-05T22:03:15.993+00:00"
                                    },
                                    "identifier": [
                                        {
                                            "use": null,
                                            "system": null,
                                            "value": "5390"
                                        }
                                    ],
                                    "code": {
                                        "coding": [
                                            {
                                                "code": "R03BB01",
                                                "system": "http://www.whocc.no/atc",
                                                "display": null
                                            }
                                        ],
                                        "text": "Atrovent inh aer 20mcg/dose ff"
                                    }
                                },
                                "medicationRequest": {
                                    "resourceType": "MedicationRequest",
                                    "id": "18be387f-5343-4fba-9890-2f61101280c6",
                                    "meta": {
                                        "profile": null,
                                        "versionId": "1",
                                        "lastUpdated": "2023-06-05T22:03:16.184+00:00"
                                    },
                                    "identifier": [
                                        {
                                            "use": null,
                                            "system": null,
                                            "value": "50442198"
                                        }
                                    ],
                                    "status": "unknown",
                                    "intent": "option",
                                    "category": [
                                        {
                                            "coding": [
                                                {
                                                    "code": "3",
                                                    "system": null,
                                                    "display": null
                                                }
                                            ],
                                            "text": "Blåreseptordningen §§ 2, 3a, 3b, 4 og 5 (gammel ordning §§ 2, 3, 4, 9, og 10a)"
                                        }
                                    ],
                                    "medicationReference": {
                                        "reference": "Medication/1de886f3-2471-4825-9e2a-1931580fc2d0",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": "1de886f3-2471-4825-9e2a-1931580fc2d0"
                                        },
                                        "display": "Medications for the prescription",
                                        "type": "Medication"
                                    },
                                    "subject": {
                                        "reference": "Patient/9dbcfce2-2c3a-476a-9b39-eead46d3c725",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": "9dbcfce2-2c3a-476a-9b39-eead46d3c725"
                                        },
                                        "display": "Patient for the prescription",
                                        "type": "Patient"
                                    },
                                    "encounter": {
                                        "reference": "Encounter/0e5b5fdd-b3a1-40cb-b8eb-e17c6c97c41d",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": "0e5b5fdd-b3a1-40cb-b8eb-e17c6c97c41d"
                                        },
                                        "display": "Encounter associated with the prescription",
                                        "type": "Encounter"
                                    },
                                    "recorder": {
                                        "reference": "Practitioner/6f8f2364-e104-47e6-a98d-3614e9d5337d",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": "6f8f2364-e104-47e6-a98d-3614e9d5337d"
                                        },
                                        "display": "Practitioner who prescribed the prescription",
                                        "type": "Practitioner"
                                    },
                                    "note": [
                                        {
                                            "authorString": "Legal Reimbursement category for the prescription",
                                            "text": "\§ 2 Forhåndsgodkjent refusjon (tidligere \§ 9)"
                                        },
                                        {
                                            "authorString": "Legal Reimbursement code for the prescription",
                                            "text": "7"
                                        },
                                        {
                                            "authorString": "Reimbursement code for the prescription - ICD/ICPC",
                                            "text": "ICPC:R95"
                                        }
                                    ],
                                    "dosageInstruction": [
                                        {
                                            "text": "Defined daily dose of the drug",
                                            "doseAndRate": [
                                                {
                                                    "doseQuantity": {
                                                        "value": 0.12,
                                                        "unit": "mg"
                                                    },
                                                    "rateQuantity": {
                                                        "value": 0.0,
                                                        "unit": "Per Day"
                                                    }
                                                }
                                            ]
                                        }
                                    ]
                                },
                                "medicationDispense": {
                                    "resourceType": "MedicationDispense",
                                    "id": "5c5ff711-0cf7-469d-a9e2-4baf8f6cc16f",
                                    "meta": {
                                        "profile": null,
                                        "versionId": "1",
                                        "lastUpdated": "2023-06-05T22:03:16.319+00:00"
                                    },
                                    "status": "unknown",
                                    "medicationReference": {
                                        "reference": "Medication/1de886f3-2471-4825-9e2a-1931580fc2d0",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": "1de886f3-2471-4825-9e2a-1931580fc2d0"
                                        },
                                        "display": "Medication details for the dispense",
                                        "type": "Medication"
                                    },
                                    "subject": {
                                        "reference": "Patient/9dbcfce2-2c3a-476a-9b39-eead46d3c725",
                                        "identifier": {
                                            "use": null,
                                            "system": null,
                                            "value": "9dbcfce2-2c3a-476a-9b39-eead46d3c725"
                                        },
                                        "display": "Patient for the prescription",
                                        "type": "Patient"
                                    },
                                    "authorizingPrescription": [
                                        {
                                            "reference": "MedicationRequest/18be387f-5343-4fba-9890-2f61101280c6",
                                            "identifier": {
                                                "use": null,
                                                "system": null,
                                                "value": "18be387f-5343-4fba-9890-2f61101280c6"
                                            },
                                            "display": "Prescription for the Medication",
                                            "type": "MedicationRequest"
                                        }
                                    ],
                                    "quantity": {
                                        "value": 3000.0,
                                        "unit": null
                                    },
                                    "daysSupply": {
                                        "value": 100000.0,
                                        "unit": null
                                    },
                                    "whenHandedOver": null
                                }
                            }
                        ]
    ```

Synthetic FHIR Data Generator
-----------------------------

### Generate Synthetic Data and download it 

1.  Request URL:
    http://localhost:8080/api/v1/synthetic/generate-data?numberOfSynRecords=10000

2.  Request Type: POST

3.  Request Body: Will have form data. With CSV file in \"file\" key and
    numberOfSynRecords as the query parameter. (Upload a file against
    which synthetic data is to be generated)

4.  Response Body: Zip file with synthetic data

SyntHIR Pipeline
----------------

### Convert CSV to FHIR and upload to Sensitive FHIR server

### Download FHIR resources from Sensitive FHIR server and convert to CSV

### Generate Synthetic data, convert to FHIR resources and upload to Synthetic FHIR server

Components Deployment
=====================

Each of the components is developed using the Spring boot framework,
which has an embedded tomcat server. Following the following steps to
deploy and run each of the components independently:

1.  Set the port number of each of the components in the application
    configuration file

2.  Build each component with the command: 'mvn clean install'.

3.  To run each component: java -jar component-name.0.0.1.jar

SyntHIR Smart app
=====================
1.  npm install

2.  yarn start
