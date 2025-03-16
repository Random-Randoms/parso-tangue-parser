# Parso Tangue language specifications

## Tokens
### Identifiers
**Letter identifier** is any non-empty sequence of latin letters, digits or
underscores, not starting with a digit, and not coinciding with any of 
keywords.

**Operator identifier** is any non-empty sequence of operator symbols:
```+ - * / = > < %```.

### Keywords
There are 6 keywords: ```fun```, ```val```, ```var```, ```if```, ```else```,
```return```.

### Literals
**String literal** is a string, consisting of any printable characters
(but ```"```), surrounded by double quotes (```"```). Backslash notation
is supported for the following symbols: ```\n \r \t \b \" \' \\$```.

**Char literal** is any character, or a backslash notation character, 
surrounded by single quotes (```'```).

**Decimal literal** is a sequence of digits.

**Binary literal** is a sequence of binary digits (```01```), prepended
with a *binary literal prefix* (```0b``` or ```0B```). It represents a
non-negative binary integer.

**Hexadecimal literal** is a sequence of hexadecimal digits
(```0123456789abcdefABCDEF```), prepended with a *hexadecimal literal prefix*
(```0x``` or ```0X```). It represents a non-negative hexadecimal integer.

### Comments
Comment starts with a hashtag symbol (```#```) and lasts till the end of a
line.

### Symbols
Parso tangue uses this symbols in its syntax: ```,:(){}``` and LF (```\n```).
Other symbols are ignored.

## Syntax
### Expressions
There are five types of expressions:
 + Literal expression -- consists of a single literal:
 + Identifier expression -- consists of a single letter identifier
 + Binary operator expression -- two expressions, divided by operator 
identifier
```
<left operand: Letter identifier> <operator: Operator identifier> <right operand: Letter identifier>
```
 + Function call -- a function name, with an argument list, provided as
a sequence (possibly empty) of comma-separated expressions, surrounded
by parentheses
```
<function name: Letter identifier>(<argument1: expression>, ...)
```
 + Braced expression -- an expression, surrounded with parentheses
``` 
(<expression: Expression>)
```

Expressions cannot contain line breaks. Whitespace symbols are ignored.

All operators are applied right to left, any unknown operators are considered
to have priority of 1. Assign operators (```= += -= *= /= %=```) have 
priority of one. Comparison operators (```> < >= <= == !=```) have priority
of two. Plus and minus have priority of three, times, divide and integer 
divide have priority of 4.

### Statements
There are six types of statements in ParsoTangue:
  + Expression
  + Return statement: expression, prepended with a ```return``` keyword:
```
return <return value: Expression>
```
  + If statement:
``` 
if (<condition: Expression>) <body: Statement block>
```
  + If-else statement:
``` 
if (<condition: Expression> <true body: Statement block> else <false body: statement block>
```
  + Constant declaration:
```
val <name: LetterIdentifier>: <type: LetterIdentifier> = <value: Expression>
```
  + Variable declaration:
```
var <name: LetterIdentifier>: <type: LetterIdentifier> = <value: Expression>
```

### Statement block
Statement block has the following structure:
```
{
<statement1: Statement>
<statement2: Statement>
...
} 
```
Number of statement can be zero

### Function declaration
Function can be declared with the following syntax:
``` 
fun <function name: LetterIdentifier>(<arguments: Arguments declaration list>): <return type: Letter identifier> <body: Statement block>
```
where arguments declaration list has the following syntax:
``` 
<name1: LetterIdentifier>: <type1: LetterIdentifier>, <name2: LetterIdentifier>: <type2: LetterIdentifier>, ...
```
Number of arguments can be zero

### Top level entities
File consists of any number of top-level entities, being constant,
variable, or function declarations.