.model small
.stack 64
.386

.data
	int	dd	0
	mas1	dd	20 dup (0)
	mas2	dd	20 dup (0)
	mas3	dd	20 dup (0)
	a	dd	0
	b	dd	0
	c	dd	0
	massive	dd	6 dup (0)
	r13	dd	0
	r14	dd	0
	r15	dd	0
	r16	dd	0
	r17	dd	0
	r18	dd	0
	r19	dd	0
	r20	dd	0
	r21	dd	0
	r22	dd	0
	r23	dd	0
	r24	dd	0
	r25	dd	0
	r26	dd	0
	r27	dd	0
	r28	dd	0
	r29	dd	0

.code
begin:
	mov ax,@Data
	mov ds, ax

OP_0:
	mov eax, 0
	sub eax, 1
	mov r13, eax
	mov eax, 0
	sub eax, r13
	mov r14, eax
	mov eax, 0
	sub eax, r14
	mov r15, eax
	mov eax, 0
	sub eax, r15
	mov a, eax

OP_1:
	mov eax, 10
	mov b, eax

OP_2:
	mov eax, 100
	mov c, eax

OP_3:
	mov eax, a
	add eax, b
	mov r13, eax
	mov eax, r13
	add eax, c
	mov r14, eax
	mov eax, r14
	imul a
	mov r15, eax
	mov eax, r15
	add eax, b
	mov r16, eax
	mov eax, r16
	sub eax, c
	mov int, eax

OP_4:
	mov eax, a
	push eax
	mov eax, 1
	add eax, 4
	mov esi, eax
	pop eax
	mov mas1[esi], eax

OP_5:
	mov eax, c
	push eax
	mov eax, 1
	add eax, 4
	mov esi, eax
	pop eax
	mov mas2[esi], eax

OP_6:
	mov eax, b
	push eax
	mov eax, 1
	add eax, 4
	mov esi, eax
	pop eax
	mov mas3[esi], eax

OP_7:
	mov eax, a
	add eax, b
	mov r13, eax
	push eax
	mov eax, c
	sub eax, c
	mov r14, eax
	mov eax, 0
	sub eax, r14
	mov r15, eax
	mov eax, r15
	add eax, 4
	mov esi, eax
	pop eax
	neg mas2[esi]
	mov eax, c
	push eax
	mov eax, c
	sub eax, c
	mov r17, eax
	mov eax, 0
	sub eax, r17
	mov r18, eax
	mov eax, r18
	add eax, 4
	mov esi, eax
	pop eax
	add eax, mas2[esi]
	mov r20, eax
	mov eax, r13
	sub eax, r20
	mov r21, eax
	mov eax, 50
	add eax, 45
	mov r22, eax
	mov eax, r21
	sub eax, r22
	mov r23, eax
	mov eax, 0
	sub eax, r23
	push eax
	mov eax, c
	push eax
	mov eax, b
	mov edx, 9
	imul edx
	mov r13, eax
	mov eax, c
	sub eax, r13
	mov r14, eax
	mov eax, r14
	sub eax, 9
	mov r15, eax
	mov eax, r15
	add eax, 4
	mov esi, eax
	pop eax
	sub eax, mas2[esi]
	mov r17, eax
	mov eax, r17
	add eax, 4
	mov esi, eax
	pop eax
	mov mas1[esi], eax

OP_8:
	mov eax, 54
	push eax
	mov eax, 100
	sub eax, c
	mov r13, eax
	mov eax, r13
	add eax, a
	mov r14, eax
	mov eax, r14
	add eax, 4
	mov esi, eax
	pop eax
	add eax, mas1[esi]
	mov r16, eax
	mov eax, r16
	push eax
	mov eax, 5
	push eax
	mov eax, 40
	sub eax, b
	mov r17, eax
	mov eax, r17
	push eax
	mov eax, b
	mov edx, 9
	imul edx
	mov r18, eax
	mov eax, c
	sub eax, r18
	mov r19, eax
	mov eax, r19
	sub eax, 9
	mov r20, eax
	mov eax, r20
	add eax, 4
	mov esi, eax
	pop eax
	sub eax, mas3[esi]
	mov r22, eax
	mov eax, r22
	sub eax, 19
	mov r23, eax
	mov eax, r23
	add eax, 4
	mov esi, eax
	pop eax
	imul mas2[esi]
	mov r25, eax
	mov eax, 10
	sub eax, r25
	mov r26, eax
	mov eax, r26
	add eax, 489
	mov r27, eax
	mov eax, r27
	add eax, 4
	mov esi, eax
	pop eax
	sub eax, mas1[esi]
	push eax
	mov eax, b
	add eax, a
	mov r13, eax
	mov eax, r13
	add eax, a
	mov r14, eax
	mov eax, r14
	add eax, 4
	mov esi, eax
	pop eax
	mov mas2[esi], eax

OP_9:
	mov eax, c
	mov ebx, eax
	mov eax, b
	imul b
	mov r13, eax
	mov eax, r13
	add eax, 5
	mov ecx, eax
	cmp ebx, ecx
	jg OP_12

OP_10:
	mov eax, c
	add eax, b
	mov r13, eax
	mov eax, r13
	sub eax, a
	push eax
	mov eax, c
	sub eax, 100
	mov esi, eax
	pop eax
	mov massive[esi], eax

OP_11:
	mov eax, c
	add eax, 1
	mov c, eax

	jmp OP_9

OP_12:
	mov eax, 5
	push eax
	mov eax, 4
	add eax, 4
	mov esi, eax
	pop eax
	mov mas1[esi], eax

program:	
	mov ax, 4C00h
	int 21h

	end begin
