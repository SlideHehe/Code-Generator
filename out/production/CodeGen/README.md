# Code Generator for Pascal

## Subset of the language

Code generator can process integer numbers and arithmetic operations,
integer arrays, assignment operator and while loop.

## Input data

Code generator takes abstract binary tree (ABT) from the selected file.
After that, tree is converted to the internal data structure from the string.

Example:
```
Program{var{a{b{c{}{integer{}{}}}{integer{}{}}}{integer{}{}}}{r6{}{r7{}{}}}}{begin{}{OP_0{:={a{}{}}{10{}{}}}{OP_1{:={b{}{}}{5{}{}}}{OP_2{:={c{}{}}{3{}{}}}{OP_3{while{>{a{}{}}{0{}{}}}{OP_4{:={b{}{}}{a{}{c{}{+{}{b{}{+{}{}}}}}}}{OP_5{:={a{}{}}{a{}{1{}{-{}{}}}}}{}}}}{}}}}}}
```
Source Pascal code:
```
var a, b, c: integer;
begin
  a := 10;
  b := 5;
  c := 3;
  while a > 0 do begin
    b := a + c + b;
    a := a -1
  end
end.
```

### Output data

After program is executed, .asm file is generated with the code from ABT.


