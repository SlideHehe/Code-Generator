.model small
.386
.data
	k	dd	0
	i	dd	0
	m	dd	0
	mas1	dd	11 dup (0)
	mas2	dd	6 dup (0)
	r8	dd	0
	r9	dd	0
	r10	dd	0

.code
begin:
	mov ax,@Data
	mov ds, ax

OP_0:
	mov eax, 5
	mov i, eax

OP_1:
	mov eax, 1
	mov m, eax

OP_2:
	mov eax, i
	add eax, m
	mov r8, eax
	mov eax, 2
	mov edx, 2
	imul edx
	mov r9, eax
	mov eax, r8
	sub eax, r9
	mov k, eax

OP_3:
	mov eax, 1
	mov edi, eax
	mov eax, 1
	add eax, 1
	mov r8, eax
	mov eax, r8
	add eax, 4
	mov r9, eax
	mov eax, r9
	sub eax, 3
	mov esi, eax
	mov eax, edi
	mov mas2[esi], eax

OP_4:
	mov edi, eax
	mov eax, 3
	mov esi, eax
	mov eax, edi
	mov eax, mas2[esi]
	add eax, 8
	mov edi, eax
	mov eax, k
	add eax, m
	mov r8, eax
	mov eax, r8
	add eax, i
	mov r9, eax
	mov eax, r9
	sub eax, 10
	mov esi, eax
	mov eax, edi
	mov mas1[esi], eax

OP_5:
	mov eax, i
	mov ebx, eax
	mov eax, m
	mov ecx, eax
	cmp ecx, ebx
	jle end_label

OP_6:
	mov eax, i
	mov edi, eax
	mov eax, 1
	add eax, 1
	mov r8, eax
	mov eax, r8
	add eax, 1
	mov esi, eax
	mov eax, edi
	add eax, mas2[esi]
	mov i, eax

OP_7:
	mov eax, k
	mov edx, 2
	imul edx
	mov k, eax

	jmp OP_5

end_label:	
	mov ax, 4C00h
	int 21h

	end begin
