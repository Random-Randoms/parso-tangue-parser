# Parso tangue parser
### Usage
Provide source file as ```--source <source.txt>``` and output file
as ```--output <output.txt>``` cli arguments. AST will be loaded in output file as
json.

### Language specifications
See [specifications](SPECIFICATIONS.md)

### Features
Currently, correctly written parso-tangue code will be converted into correct
ast. If code has incorrect syntax, all top level entities before the
first mistake will be parsed correctly, while all other entities will likely
be discarded.