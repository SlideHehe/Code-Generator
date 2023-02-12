.model small
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
	jle end_label

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
