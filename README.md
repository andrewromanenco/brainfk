# BrainF*ck interpreter and Java compiler
 Another implementation for brainf*ck, using [the compiler-framework](https://github.com/andrewromanenco/compilerframework).

## Process
- Source file
- List of tokens
- Parsing Tree
- Abstract syntax tree
- Optimized abstract syntax tree (not optimizations are really implemented)
- Interpreter or Java Compiler runs over the abstract syntax tree

## Interpreter
java bfk.Interpreter &lt;path to brainf*ck source&gt;

## Javac
Translate an abstract syntax tree to java 1.7 byte code using asm5. Result is stored into a class file and is runnable as usual java console application.

java bfk.Javac &lt;path to brainf*ck source&gt; 

## Grammar
See bfk.common.BrainFKGrammar.java for grammar details

## License
The code is released under Apache License Version 2.0

## Contacts
- andrew@romanenco.com
- [romanenco.com](http://www.romanenco.com)
- https://twitter.com/andrewromanenco
