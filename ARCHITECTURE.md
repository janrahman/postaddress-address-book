# Architecture Notes

Some notes about the design process and visuals to aid talking points for the demo application.

## Quality requirements

| Id | Scenario                                                                                                                                     |
|----|----------------------------------------------------------------------------------------------------------------------------------------------|
| 1  | As a customer, I want to persist persons and addresses, so I can validate and manage addresses.                                              |
| 2  | As a API user, I want to call the RESTful endpoints with CRUD operations, so I can establish data consistency and integrity.                 |
| 3  | As a customer, I want to list all persons by address, so I can optimize the delivery processes.                                              |
| 4  | As a customer, I want to calculate the average age of all persons by postal code, so I can use the age demographics for marekting campaigns. |

## Context and Scope

```mermaid
---
title: Context view
---
flowchart LR
    user ---> client
    client --> rest_service 
    
```

## Solution strategy

```mermaid
---
title: Person Address Entity Relationship Model (Chen notation)
config:
---
graph TD
    HAS{has}
    PERSON -- " [n,m] " --- HAS
    HAS -- " [n,m] " --- ADDRESS
    EMAIL([<u>email</u>]) --- PERSON
    FIRSTNAME([firstname]) --- PERSON
    NAME([name]) --- PERSON
    GENDER([gender]) --- PERSON
    BIRTHDAY([birthday]) --- PERSON
    A_ID([<u>id</u>]) --- ADDRESS
    STREET([street]) --- ADDRESS
    STREET_NUM([street_number]) --- ADDRESS
    POSTAL_CODE([postal_code]) --- ADDRESS
    CITY([city]) --- ADDRESS
```

```mermaid
---
title: Person Address Relational Diagram
---

erDiagram
    PERSON {
        string email PK
        string firstname
        string name
        string gender
        string birthday
    }
    ADDRESS {
        int id PK
        string street
        int street_number
        int postal_code
        string city
    }
    PERSON_ADDRESS {
        string person_email PK, FK
        int address_id PK, FK
    }
    PERSON ||--o{ PERSON_ADDRESS: has
    ADDRESS ||--o{ PERSON_ADDRESS: has
```

## Building Block Level 1

## Crosscutting concepts

