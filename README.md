# Code Generator for Pascal

Code Generator is part of the Pascal Compiler, which translates source Pascal code
to Assembly language (Turbo Assembler x86).

## Subset of the language

Code generator can process integer numbers and arithmetic operations,
integer arrays, assignment operator and while loop.

## Input data

Code generator takes abstract binary tree (ABT) from the selected file.
After that, tree is converted to the internal data structure from the string.

Pascal code example:
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

ABT for that program:
```
Program{var{a{b{c{}{integer{}{}}}{integer{}{}}}{integer{}{}}}{r6{}{r7{}{}}}}{begin{}{OP_0{:={a{}{}}{10{}{}}}{OP_1{:={b{}{}}{5{}{}}}{OP_2{:={c{}{}}{3{}{}}}{OP_3{while{>{a{}{}}{0{}{}}}{OP_4{:={b{}{}}{a{}{c{}{+{}{b{}{+{}{}}}}}}}{OP_5{:={a{}{}}{a{}{1{}{-{}{}}}}}{}}}}{}}}}}}
```

### Output data

After program is executed, .asm file is generated with the code from ABT.

Assembly code from the example.
```
.model small
.stack 64
.386

.data
	a	dd	0
	b	dd	0
	c	dd	0
	r6	dd	0
	r7	dd	0

.code
begin:
	mov ax,@Data
	mov ds, ax

OP_0:
	mov eax, 10
	mov a, eax

OP_1:
	mov eax, 5
	mov b, eax

OP_2:
	mov eax, 3
	mov c, eax

OP_3:
	mov eax, a
	mov ebx, eax
	mov eax, 0
	mov ecx, eax
	cmp ebx, ecx
	jle program

OP_4:
	mov eax, a
	add eax, c
	mov r6, eax
	mov eax, r6
	add eax, b
	mov b, eax

OP_5:
	mov eax, a
	sub eax, 1
	mov a, eax

	jmp OP_3

program:	
	mov ax, 4C00h
	int 21h

	end begin
```
