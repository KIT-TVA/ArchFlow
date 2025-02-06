grammar cidl;

component: COMPONENT NAME IMPLEMENTS NAME
list_subcomponents
interface_provided
interface_required?
assembly*
delegation*;

method_header: information_flow_spec TYPE METHOD_NAME LPAREN (TYPE NAME)* RPAREN;

information_flow_spec: pre post;

pre: 'precondition:' (NAME SECURITY_LEVEL)*;

post: 'postcondition:' (NAME SECURITY_LEVEL)*;

list_subcomponents: (COMPONENT NAME)*;

interface_required: 'require' (method_header)+;

interface_provided: 'provide' (method_header)+;

assembly: 'assembly:' NAME '.' METHOD_NAME '->' NAME '.' METHOD_NAME;

delegation: 'delegate:' METHOD_NAME '->' NAME '.' METHOD_NAME;

component_interface: INTERFACE_TYPE INTERFACE_STRING;


//LEXER RULES
TYPE: 'void' | 'int' | 'bool' | 'FlightOffers' | 'FlightOffer' | 'Request' | 'CCD' | 'Airline';

COMPONENT: 'component';

IMPLEMENTS: 'implements';

LPAREN: '(';

RPAREN: ')';

SUBCOMPONENTS: 'subcomponents';


INTERFACE_STRING: 'interface' ;

//Lowercase names for variables (change when we agree on syntax)
NAME:[a-z]+;

//Lowercase name for methods (change when we agree on syntax)
METHOD_NAME:[a-z,_]+;

//Basic security levels
SECURITY_LEVEL:'L' | 'H' | 'S';

//Ignore whitespaces and newlines
WS: [ \t\r\n]+ -> skip;

//OLD
//component: COMPONENT NAME component_interface method_header+;

//method_header: information_flow_spec TYPE METHOD_NAME LPAREN (TYPE NAME)* RPAREN;

//information_flow_spec: pre post;

//pre: 'precondition:' (NAME SECURITY_LEVEL)*;

//post: 'postcondition:' (NAME SECURITY_LEVEL)*;

//component_interface: INTERFACE_TYPE INTERFACE_STRING;

//LEXER RULES
//TYPE: 'int' | 'bool' | 'FlightOffers' | 'Request' | 'CCD' | 'Airline';

//COMPONENT: 'component';

//LPAREN: '(';

//RPAREN: ')';

//SUBCOMPONENTS: 'subcomponents';

//ASSEMBLY: 'assembly: ';

//DELEGATION: 'delegate: ';

//INTERFACE_TYPE: 'required' | 'provided';

//INTERFACE_STRING: 'interface' ;

//Lowercase names for variables (change when we agree on syntax)
//NAME:[a-z]+;

//Lowercase name for methods (change when we agree on syntax)
//METHOD_NAME:[a-z,_]+;

//Basic security levels
//SECURITY_LEVEL:'L' | 'H' | 'S';

//Ignore whitespaces and newlines
//WS: [ \t\r\n]+ -> skip;
