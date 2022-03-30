parser grammar ShowParser;

showStatements:
     showCreate
     | showObjects
     | describe
   ;

showCreate:
    KW_SHOW KW_CREATE type=showType qualifiedName output?
;
output:
    KW_OUTPUT EQUAL identifier
;

showObjects:
    KW_SHOW (KW_FULL)? (type=showObjectTypes)
    ((KW_FROM | KW_IN) qualifiedName)?
    ((KW_LIKE string) | (KW_WHERE expression))?
    (KW_OFFSET offset=INTEGER_VALUE)?
    (KW_LIMIT limit=INTEGER_VALUE)?
    ;


describe:
    (KW_DESC | KW_DESCRIBE) type=showType qualifiedName
;

showObjectTypes:
    tableType? KW_TABLES
    | indicatorType? KW_INDICATORS
    | KW_DOMAINS
    | KW_DICTS
    | KW_TIMEPERIODS
    | KW_ADJUNCTS
    | KW_MEASUREUNITS
    | KW_PROCESSES
    | KW_LAYERS
    | KW_DICT KW_GROUPS
    | KW_MEASUREUNIT KW_GROUPS
    | KW_BUSINESS_CATEGORIES
    | KW_DIMENSIONS
    | KW_MARKETS
    | KW_SUBJECTS
    | KW_DIM_ATTRIBUTES
    | KW_CODES
    | KW_COLUMNS
;


showType:
    KW_TABLE
    | KW_INDICATOR
    | KW_DOMAIN
    | KW_DICT
    | KW_TIMEPERIOD
    | KW_ADJUNCT
    | KW_MEASUREUNIT
    | KW_PROCESS
    | KW_LAYER
    | KW_CODE
    | KW_GROUP
    | KW_PROCESS
    | KW_BUSINESS_CATEGORY
    | KW_MARKET
    | KW_SUBJECT
  ;